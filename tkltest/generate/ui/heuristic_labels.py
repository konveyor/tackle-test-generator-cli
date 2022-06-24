import json
import nltk
from nltk.corpus import stopwords
from nltk.stem import WordNetLemmatizer
import string
import logging
import enchant
from lxml import html, etree
import xmltodict as xd
from io import StringIO
import collections
from keybert import KeyBERT

from importlib import resources
from flair.data import Sentence
from flair.models import SequenceTagger
import pandas as pd



class HeuristicLabel:

    def __init__(self, rankings_file_path):
        """
        Initialize with the ranked attribute order to use while calculating the label

        Parameters:
                    rankings_file_path (str): A string path to the ranked_attributes.json file

        Returns:
                    Nothing, creates a ranked_attributes list and rankings_set set to find the labels"""

        self.html_stop_words = ["a", "abbr", "acronym", "address", "area", "b", "base", "bdo", "big", "blockquote",
                                "body",
                                "br",
                                "button", "caption", "cite", "code", "col", "colgroup", "dd", "del", "dfn", "div", "dl",
                                "doctype",
                                "dt", "em", "fieldset", "form", "h1", "h2", "h3", "h4", "h5", "h6", "head", "html",
                                "hr",
                                "i", "img",
                                "input", "ins", "kbd", "label", "legend", "li", "link", "map", "meta", "noscript",
                                "object",
                                "ol",
                                "optgroup", "option", "p", "param", "pre", "q", "samp", "script", "select", "small",
                                "span",
                                "strong",
                                "style", "sub", "sup", "table", "tbody", "td", "text", "textarea", "tfoot", "th",
                                "thead",
                                "title",
                                "tr", "tt", "ul", "var", "", "submit", "false", "action", "javascript", "javascript:;",
                                "href"]

        with open(rankings_file_path) as f:
            self.ranked_attributes = json.load(f)

        # set of ranked attributes, to search efficiently
        self.rankings_set = set(self.ranked_attributes)


        with resources.path('tkltest.generate.ui', 'ranked_attributes_form_fields.json') as attr_file:
            with open(attr_file) as f:
                self.ranked_attributes_form_fields = json.load(f)


    ########################################## NLP preprocessing ########################################################

    def preprocess(self, s):
        """
        Preprocess value of ranked attributes of eventables-  tokenize, remove stop words, lemmatize and remove non-English words

        Parameters:
                    s (str): A string word or phrase to be preprocessed

        Returns:
                    s (str): The preprocessed version of this input string """

        s = self.remove_non_english_words(self.lemmatize(self.remove_stop_words(self.tokenize(s))))

        return s

    def tokenize(self, s: str):
        """
        Tokenize input string to remove uppercase, camel case, snake case, etc. eg. TableSortTrigger -> table sort trigger

        Parameters:
                    s (str): A string word or phrase to be tokenized

        Returns:
                    s (str): The tokenized version of this input string """

        result = ""
        buffer = ""
        for word in s.split():

            # if whole string uppercase and len > 1, convert whole word to lowercase
            if word.isupper():
                if len(word) > 1:
                    result += word.lower() + " "
            else:
                for c in word:
                    if c in string.ascii_lowercase:
                        buffer += c
                    elif c in string.ascii_uppercase:
                        if buffer != "":  # uppercase character and non empty buffer

                            # uppercase character and more than 1 element in buffer, then add buffer contents and space to result as a lowercase word
                            if len(buffer) > 1:
                                result += buffer + " "

                            # reset buffer in the case of uppercase char and non empty buffer
                            buffer = ""

                        # either way add lowercase of this character to buffer
                        buffer += c.lower()

                    # not ascii
                    else:

                        # non empty buffer, do the same thing
                        if buffer != "":
                            if len(buffer) > 1:
                                result += buffer + " "

                            # reset buffer in the case of non ascii character and non empty buffer
                            buffer = ""

                # if at end, buffer not empty (last few all lowercase), add these contents to result
                if buffer != "":
                    if len(buffer) > 1:
                        result += buffer + " "
                    buffer = ""
        return result

    def remove_stop_words(self, s):
        s = list(s.split(' '))
        s = [word for word in s if word not in set(stopwords.words('english'))]
        s = [word for word in s if word not in set(self.html_stop_words)]
        return ' '.join(s)

    def lemmatize(self, s):
        s = list(s.split(' '))
        lemmatizer = WordNetLemmatizer()
        s = [lemmatizer.lemmatize(word) for word in s]
        s = ' '.join(s)
        return s

    def remove_non_english_words(self, s):
        s = (word for word in nltk.wordpunct_tokenize(s) if word in set(nltk.corpus.words.words()))
        s = ' '.join(s)
        return s

    ########################################## Compute Labels ########################################################

    def get_label(self, eventable):
        """
        Get the label for an eventable element, either based on element or its context (in the case of no relevant element attributes)

        Parameters:
            eventable (dict): A json dict of the element field of the eventable

        Returns:
            label (str): The label for this eventable based on either the element or its context dom """


        heuristic_label = self.get_element_label(eventable['element'])

        if heuristic_label.strip() == '':
            context_dom = self.get_context_dom(eventable['source']['dom'],
                                               eventable['identification']['value'].lower())
            context_label = self.get_context_label(context_dom)
            heuristic_label = context_label
        heuristic_label = heuristic_label.strip()

        # load tagger
        tagger = SequenceTagger.load("flair/pos-english")
        heuristic_label_flair = Sentence(heuristic_label)
        tagger.predict(heuristic_label_flair)
        contains_verb = False
        heuristic_label_pos = dict()
        for token in heuristic_label_flair.tokens:
            heuristic_label_pos[token.text] = token.labels[0].value

        for word in heuristic_label_pos:
            if heuristic_label_pos[word] in ['VB', 'VBD', 'VBG', 'VBN', 'VBP', 'VBZ']:


                contains_verb = True
                break
        if not contains_verb:
            heuristic_label = eventable['eventType'] + ' ' + heuristic_label

        form_field_labels = []
        for form_input in eventable['relatedFormInputs']:
            form_field_dom = self.find_element(eventable['source']['dom'], form_input['identification']['value'].lower(), 'str')

            form_field_label = self.get_form_field_label(form_field_dom).strip()

            form_field_label_flair = Sentence(form_field_label)
            tagger.predict(form_field_label_flair)
            contains_verb = False
            form_field_label_pos = dict()
            for token in form_field_label_flair.tokens:
                form_field_label_pos[token.text] = token.labels[0].value
            for word in form_field_label_pos:
                if form_field_label_pos[word] in ['VB', 'VBD', 'VBG', 'VBN', 'VBP', 'VBZ']:
                    contains_verb = True
                    break
            if len(form_field_label_pos) > 0 and not contains_verb:
                form_field_label = 'enter' + ' ' + form_field_label

            if form_field_label == '':
                form_field_label = 'Enter data into form field'
            form_field_labels.append(form_field_label)

        return [heuristic_label, form_field_labels]

    def get_form_field_label(self, form_field_dom:str):
        if not form_field_dom:
            return ''

        attributes = dict()
        form_field_dom = self.parse_dom_to_dict(form_field_dom)

        for k1 in form_field_dom.keys():
            for k2 in form_field_dom[k1].keys():
                for k3 in form_field_dom[k1][k2].keys():
                    attributes[k3[1:]] = form_field_dom[k1][k2][k3]

        form_field_label = ''
        for attr in self.ranked_attributes_form_fields:
            if attr in attributes:
                form_field_label = (self.lemmatize(self.tokenize(attributes[attr])))
                break
        return form_field_label


    def get_element_label(self, eventable):
        """ get heuristic labels by selecting the highest ranked attribute which this eventable has

        Parameters:
                    eventable (dict): A json dict of the element field of the eventable

        Returns:
                    element_label (str): The label for this eventable based on the element itself"""

        element_label = ''
        for attr in self.ranked_attributes:

            if attr == 'text' and attr in eventable:
                element_label = self.process_attribute_value(eventable[attr])
            elif 'attributes' in eventable and attr in eventable['attributes']:
                if attr == 'href':
                    # special case for "href" attribute because its value
                    # is usually longer and more complicated than other
                    # attributes
                    href = eventable['attributes']['href']
                    tokenized_href = self.process_attribute_value(href.strip().split("?")[-1])
                    kw_model = KeyBERT()
                    keywords = kw_model.extract_keywords(tokenized_href, keyphrase_ngram_range=(1, 2),
                                                         stop_words='english', use_mmr=True,
                                                         diversity=0.7)
                    if keywords:
                        element_label = keywords[0][0]
                    else:
                        element_label = href.strip().split("/")[-1].split(".")[0]
                        element_label = self.process_attribute_value(element_label)
                else:
                    element_label = self.process_attribute_value(eventable['attributes'][attr])
            if element_label != '':
                break

        if element_label == '':
            preprocessed_eventable = self.remove_non_english_words(
                self.lemmatize(self.tokenize(self.preprocess_eventable_element_details(eventable))))
            kw_model = KeyBERT()
            keywords = kw_model.extract_keywords(preprocessed_eventable, keyphrase_ngram_range=(1, 2),
                                                 stop_words='english',
                                                 use_mmr=True, diversity=0.7)
            if keywords:
                element_label = keywords[0][0]

        logging.info('Got highest ranked attribute for this eventable')

        return element_label



    def get_context_label(self, context_dom: str):
        """
        Get label for a context DOM

        Parameters:
            context_dom (str): context DOM for which we need to calculate the label

        Returns:
            label (str): label for this context DOM
        """

        preprocessed_context_dom = self.dict_value_to_str(self.parse_dom_to_dict(context_dom))

        preprocessed_context_dom = self.preprocess(preprocessed_context_dom)

        kw_model = KeyBERT()
        keywords = kw_model.extract_keywords(preprocessed_context_dom, keyphrase_ngram_range=(1, 2),
                                             stop_words=self.html_stop_words,
                                             use_mmr=True,
                                             diversity=0.8)
        logging.debug(f"keywords: {keywords}")
        if keywords:
            label = str(keywords[0][0])
            # element_label = str(keywords)
            logging.debug(f"keyword: {label}")
        else:
            label_list = []
            for word in preprocessed_context_dom.split(" "):
                if len(word.strip()) > 0 and word.strip() not in label_list:
                    label_list.append(word.strip())
            label = " ".join(label_list)
        label = self.process_attribute_value(label)
        return label.strip()

    def find_element(self, state_dom: str, eventable_element_xpath: str, return_format: str = 'dom'):
        """
        Locates the eventable element in the web page

        Parameters:
            state_dom (str): DOM of the web page in string format
            eventable_element_xpath (str): xpath of the eventable element in string format
            return_format (str): return format of eventable element location, default 'dom'

        Returns:
            HtmlElement location of the element in the web page
        """

        try:
            tree = html.fromstring(state_dom)
            element = tree.xpath(eventable_element_xpath)
            if not element or len(element) == 0:
                return None
            if return_format == "str":
                return etree.tostring(element[0]).decode('UTF-8')
            return element[0]
        except Exception as e:
            logging.debug(eventable_element_xpath)
            logging.debug(e)
            return None

    def get_context_dom(self, state_dom: str, eventable_element_xpath: str):
        """
        Gets context dom of the eventable element by finding the oldest parent of the eventable element without another child

        Parameters:
            state_dom (str): DOM of the web page
            eventable_element_xpath (str): xpath of the eventable in this DOM

        Returns:
            Context dom of the eventable in string format
        """

        curr_dom = self.find_element(state_dom, eventable_element_xpath)

        # TODO: ask about space

        if curr_dom is None:
            return " "
        # stop when we find more than one eventable element
        # at most find the fourth parent
        iterations = 0
        while curr_dom.getparent() and iterations <= 3:
            curr_dom_without_context = ''
            try:
                curr_dom_string = etree.tostring(curr_dom).decode('UTF-8').strip()
                if not curr_dom_string.endswith(">"):
                    curr_dom_string = curr_dom_string[:curr_dom_string.rindex(">") + 1]
                curr_dom_without_context = etree.parse(StringIO(curr_dom_string))

            except Exception as e:

                # TODO: check whether normal execution should continue after exception handling

                logging.warning(curr_dom_string)
                logging.warning("Exception while getting parent", e)
            parent = curr_dom.getparent()
            if (parent is None) or (parent and curr_dom_without_context and self.contains_more_than_one_eventable(
                    curr_dom_without_context)):
                return etree.tostring(curr_dom).decode('UTF-8')
            curr_dom = parent
            iterations += 1
        return etree.tostring(curr_dom).decode('UTF-8')

    def contains_more_than_one_eventable(self, dom):
        """
        Checks whether the DOM has more than one eventable

        Parameters:
            dom (str): string DOM

        Returns:
            Boolean (True if the DOM contains more than one eventable, False otherwise)
        """

        xpath_res = dom.xpath("//*[self::a or self::input[@type='submit'] or self::button]")
        logging.debug(f"xpath result: {xpath_res}")
        logging.debug(f"length: {len(xpath_res)}")
        if len(xpath_res) > 1:
            return True
        return False

    def preprocess_eventable_element_details(self, eventable_element_details):
        """
        Preprocesses eventable['element'] to return a string

        Parameters:
            eventable_element_details (dict): a dictionary of eventable['element']

        Returns:
            preprocessed_eventable_details (str): preprocessed version of the input
        """
        text = ''
        for value in eventable_element_details.values():
            if type(value) == dict:
                text = text + json.dumps(list(value.values())) + " "
            elif type(value) == str:
                text = text + value + " "
        logging.debug(f"text: {text}")
        preprocessed_eventable_details = self.preprocess(text)
        return preprocessed_eventable_details

    def process_attribute_value(self, text):
        # TODO: check how this is different from normal preprocessing
        """
        Process a string attribute value of an eventable

        Parameters:
            text (str): an attribute of an eventable to be processed

        Returns:
            preprocessed_attr (str): the preprocessed attribute value
        """
        preprocessed_attr = ''
        dictionary = enchant.Dict("en_US")
        punc = '''!()-[]{};:'"\,<>./?@#$%^&*_~'''
        text = self.preprocess(text)
        english = False
        if text:
            for button_text_word in text.split(" "):
                if button_text_word in punc:
                    continue
                if not dictionary.check(button_text_word.lower()):
                    english = False
                    break
                english = True
            if english:
                preprocessed_attr = text
        logging.debug(f"text: {text}, isEnglish: {english}, element label: {preprocessed_attr}")
        return preprocessed_attr.strip()

    def parse_dom_to_dict(self, dom: str):
        """
        Parses input string dom to a dict using the xmltodict library

        Parameters:
            dom (str): input string DOM

        Returns:
            context_dom_dict (dict): dictionary of the parsed DOM
        """
        context_dom_str = r"<html>" + dom + r"</html>"
        context_dom_dict = xd.parse(context_dom_str)
        # print(f"context dom dict: {context_dom_dict}")
        return context_dom_dict

    def dict_value_to_str(self, context_dom_input):
        """
        Converts dict context dom to string

        Parameters:
            context_dom_input (dict):  dict of context dom

        Returns:
            parsed string of this dom
        """
        if type(context_dom_input) == dict or type(context_dom_input) == collections.OrderedDict:
            res = ""
            for value in context_dom_input.values():
                res += self.dict_value_to_str(value)
            return res
        elif type(context_dom_input) == str:
            return context_dom_input
        elif type(context_dom_input) == list:
            res = ""
            for context_dom_item in context_dom_input:
                res += self.dict_value_to_str(context_dom_item)
            return res
        elif type(context_dom_input) == tuple:
            if len(context_dom_input) == 2:
                return context_dom_input[1]
            else:
                return " ".join(context_dom_input)
        else:
            logging.debug(f"missed {type(context_dom_input)}")
            return ""



# for class testing and performance analysis
if __name__ == "__main__":
    heuristic_label = HeuristicLabel('ranked_attributes.json')

    file = json.load(open('crawl_paths_addressbook_small.json'))
    curr_labels = dict()
    total_clickables = 0
    total_form_field_elements = 0
    empty_clickable_labels = 0
    empty_form_field_labels = 0

    eventable_dom_label_table = []
    for crawlpath in file[:1]:
        for eventable in crawlpath:
            if eventable['id'] not in curr_labels:
                curr_labels[eventable['id']] = heuristic_label.get_label(eventable)
                eventable_dom_label_table.append([eventable['id'],
                                                   eventable['element'],
                                                   heuristic_label.find_element(eventable['source']['dom'], eventable['identification']['value'].lower(), 'str'),
                                                   heuristic_label.get_context_dom(eventable['source']['dom'], eventable['identification']['value'].lower()),
                                                   curr_labels[eventable['id']][0]])
                total_clickables += 1
                total_form_field_elements += len(curr_labels[eventable['id']][1])
                if curr_labels[eventable['id']][0] == '':
                    empty_clickable_labels += 1
                for form_field_label in curr_labels[eventable['id']][1]:
                    if form_field_label == 'Enter data into form field':
                        empty_form_field_labels += 1
    eventable_dom_label_table = pd.DataFrame(eventable_dom_label_table, columns=['id', 'eventable[element]','curr_dom', 'context_dom', 'label'])
    clickable_percentage = 'N/A'
    if total_clickables > 0:
        clickable_percentage = (1 - empty_clickable_labels/total_clickables) * 100
    form_field_percentage = 'N/A'
    if total_form_field_elements > 0:
        form_field_percentage = (1 - empty_form_field_labels/total_form_field_elements) * 100
    results = {'Number of Clickables': total_clickables, 'Number of Form Field Elements': total_form_field_elements,
               'Empty Clickable Labels': empty_clickable_labels, 'Empty Form Field Labels': empty_form_field_labels,
               'Percentage of Labels computed for clickables': clickable_percentage,
               'Percentage of labels computed for form fields': form_field_percentage}
    output_file = open('analysis_outputs/label_analysis_results.json', 'w')
    output_file.write(json.dumps(results))

    output_file = open('analysis_outputs/labels_computed.json', 'w')
    output_file.write(json.dumps(curr_labels))


    eventable_dom_label_table.to_csv('analysis_outputs/eventable_dom_label_table.csv')
