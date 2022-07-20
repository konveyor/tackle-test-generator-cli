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
        self.eventable_counts = dict()
        self.eventable_labels = dict()


        with resources.path('tkltest.generate.ui', 'ranked_attributes_form_fields.json') as attr_file:
            with open(attr_file) as f:
                self.ranked_attributes_form_fields = json.load(f)



        with resources.path('tkltest.generate.ui', 'ranked_attributes_form_fields.json') as attr_file:
            with open(attr_file) as f:
                self.ranked_attributes_form_fields = json.load(f)

        self.empty_eventable_labels = 0
        self.empty_form_field_labels = 0



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

    def get_element_and_method_labels(self, crawl_paths):
        """
        Get element level and method level labels

        Parameters:
            crawl_paths (list): list of crawl paths, output by CrawlJax

        Returns:
            None (stores element and method level labels in self.eventable_labels and self.method_labels

        """
        # to store eventable and method labels
        self.eventable_labels = dict()
        self.method_labels = dict()

        for crawl_path in crawl_paths:
            for eventable in crawl_path:
                self.get_label(eventable)

        # get label counts of eventables to sort by frequency of occurrence in crawl paths
        self.get_label_counts(crawl_paths)

        # store method labels with id eventable path
        for crawl_path in crawl_paths:
            eventable_id_path = '_'.join([
                str(eventable['id']) for eventable in crawl_path
            ])
            method_label = self.get_method_label(crawl_path)
            self.method_labels[eventable_id_path] = method_label


    def get_label_counts(self, crawl_paths):
        """
        Calculates frequency of occurrence of each clickable label

        Parameters:
            crawl_paths (list): list of crawl paths

        Returns:
            None (stores label frequencies in self.label_counts)

        """

        # store frequency of occurrence of each label in all crawl paths
        self.label_counts = dict()
        for crawl_path in crawl_paths:
            for eventable in crawl_path:
                eventable_label = self.eventable_labels[eventable['id']][0]
                if eventable_label not in self.label_counts:
                    self.label_counts[eventable_label] = 0
                self.label_counts[eventable_label] += 1


    def get_method_label(self, crawl_path):
        """
        Calculates method level label

        Parameters:
            crawl_path (list): a single crawl path, depicting a single output test method

        Returns:
            method_label (str): label for the method

        """

        # eventables in current crawl path
        curr_eventable_labels = [
            self.eventable_labels[eventable['id']][0] for eventable in crawl_path
        ]

        # sort current eventable labels based on frequency of occurrence of the label in all crawl paths
        sorted_eventable_labels = sorted(curr_eventable_labels, key=lambda ele: self.label_counts[ele])
        method_label = []

        # calculate method label based on sorted eventable labels in this path
        if self.label_counts[sorted_eventable_labels[0]] == 1:
            method_label.append(sorted_eventable_labels[0]) # clickable only occurs in this crawlpath, unique label
        else:
            for label in curr_eventable_labels:
                method_label.append(label) # use all clickables in crawlpath for the label

        return ', '.join(method_label)


    def get_label(self, eventable):
        """
        Get the label for an eventable element, either based on element or its context (in the case of no relevant element attributes)

        Parameters:
            eventable (dict): A json dict of the eventable

        Returns:
            label (str): The label for this eventable based on either the element or its context dom """

        if eventable['id'] in self.eventable_labels:
            return self.eventable_labels[eventable['id']]

        # get title of the source web page
        tree = html.fromstring(eventable['source']['dom'])
        title_element = tree.xpath('/html[1]/head[1]/title[1]')
        title = title_element[0].text.strip()

        element_dom = self.find_element(eventable['source']['dom'], eventable['identification'])

        heuristic_label = ''
        if element_dom is not None:
            # check if this element dom has text
            heuristic_label = element_dom.text

        # otherwise, calculate label using element dom
        if not heuristic_label or heuristic_label.strip() == '':
            heuristic_label = self.get_element_label(eventable['element'])

            # calculate label using context dom
            if heuristic_label.strip() == '':
                context_dom = self.get_context_dom(eventable['source']['dom'],
                                                   eventable['identification'])
                heuristic_label = self.get_context_label(context_dom)

        heuristic_label = heuristic_label.strip().lower()
        if heuristic_label == '':
            self.empty_eventable_labels += 1

        # add title and verb to label
        heuristic_label = 'On page "' + title + '", ' + eventable['eventType'] + ' "' + heuristic_label + '"'

        # get all form fields
        form_field_labels = self.get_form_field_labels(eventable)

        # store eventable and form field labels in dictionary
        self.eventable_labels[eventable['id']] = [heuristic_label, form_field_labels]

        return [heuristic_label, form_field_labels]


    def get_form_field_extended_dom(self, source_dom, form_field_identification, return_format = 'dom'):
        """
        Get the extended dom of a form field element, which contains the label for that field

        Parameters:
            source_dom (str): DOM of the web page in string format
            form_field_xpath (str): xpath of the form field element in string format
            return_format (str): specifies what format to return the extended dom in (str/dom)

        Returns:
            form_field_extended_dom (str/dom): extended dom with label attribute for the form field

        """
        # get form field dom
        form_field_dom = self.find_element(source_dom, form_field_identification)
        if form_field_dom is None:
            return None

        # get a parent that has a label,text or has more than one eventable
        while form_field_dom.getparent():
            form_field_dom = form_field_dom.getparent()
            if form_field_dom.get('label') is not None or form_field_dom.get(
                    'aria-label') is not None or form_field_dom.text is not None or self.contains_more_than_one_eventable(
                    form_field_dom):
                break

            # possible to have a label for the nested form field dom, before this parent
            prev_sibling = form_field_dom.getprevious()
            if prev_sibling is not None and prev_sibling.tag == 'label':
                break

        if return_format == 'str':
            return etree.tostring(form_field_dom).decode('UTF-8')
        return form_field_dom


    def get_form_field_labels(self, eventable):
        """
        Get label for all the form fields for this eventable element

        Parameters:
            eventable (dict): A json dict of the eventable

        Returns:
            form_field_labels (list): a list of labels corresponding to all the form fields for this eventable

        """
        # get title text for the source dom
        tree = html.fromstring(eventable['source']['dom'])
        title_element = tree.xpath('/html[1]/head[1]/title[1]')
        title = title_element[0].text.strip()

        # get all form field labels for this eventable
        form_field_labels = []
        for form_input in eventable['relatedFormInputs']:

            form_field_dom_str = self.find_element(eventable['source']['dom'], form_input['identification'], 'str')
            form_field_dom = self.find_element(eventable['source']['dom'], form_input['identification'])

            # form field dom not found
            if form_field_dom is None:
                # if no dom found, not possible to calculate the label
                self.empty_form_field_labels += 1
                form_field_label = 'On page "' + title + '", enter data into form field'
                form_field_labels.append(form_field_label)
                continue

            # check if form field dom has any text
            form_field_label = form_field_dom.text

            # if no text found in form field dom, try to get the label sibling of this dom
            if form_field_label is None or form_field_label.strip() == '':

                # if no id, only previous sibling of the form field dom can be its label
                if not form_field_dom.get('id'):
                    previous_sibling = form_field_dom.getprevious()
                    if previous_sibling is not None and previous_sibling.tag == 'label':
                        # label for this element
                        form_field_label = previous_sibling.text

                # if id, search for the preceding sibling which has this id
                else:
                    form_field_id = form_field_dom.get('id')
                    for sibling in form_field_dom.itersiblings(preceding=True):
                        if sibling.get('for') == form_field_id:
                            form_field_label = sibling.text
                            break

            # if no label elements found, check for a few useful attributes
            if not form_field_label or form_field_label == '':
                for attr in ['aria-label', 'value', 'placeholder']:
                    form_field_label = form_field_dom.get(attr)
                    if form_field_label is not None:
                        break

            # if still no label has been generated, check in the extended dom of the form field for a label or aria-label
            if not form_field_label or form_field_label == '':
                form_field_extended_dom = self.get_form_field_extended_dom(eventable['source']['dom'], form_input['identification'])
                if form_field_extended_dom is not None:
                    form_field_label = form_field_extended_dom.get('label')
                    if not form_field_label:
                        form_field_label = form_field_extended_dom.get('aria-label')
                    if not form_field_label:
                        form_field_label = form_field_extended_dom.text
                    prev_sibling = form_field_extended_dom.getprevious()
                    if not form_field_label and prev_sibling is not None and prev_sibling.tag == 'label':
                        form_field_label = prev_sibling.text

            # get type of the form field
            form_field_type = form_field_dom.get('type')
            if not form_field_type:
                form_field_type = 'form field'
            form_field_tag = form_field_dom.tag

            # process table element
            parent = form_field_dom.getparent()
            if parent.tag in ['tr', 'th', 'td']:
                form_field_label = 'On page "' + title + '", ' + self.process_table_element(form_field_dom, form_field_label)

            # non table element
            else:
                # if no label has been generated so far, check directly with the ranked attributes for form fields
                if not form_field_label or form_field_label.strip() == '':
                    form_field_label = self.get_form_field_label_by_attribute(form_field_dom_str)

                # remove punctuation
                punc = '''!()-[]{};:'"\,<>./?@#$%^&*_~'''
                for chr in form_field_label:
                    if chr in punc:
                        form_field_label = form_field_label.replace(chr, '')

                form_field_label = form_field_label.strip().lower()

                # get final label based on form field type
                if form_field_label is not None and form_field_label != '':
                    if form_field_type in ['checkbox', 'file', 'radio'] or form_field_tag == 'select':
                        form_field_label = 'On page "' + title + '", select "' + form_field_label + '"'
                    else:
                        form_field_label = 'On page "' + title + '", enter "' + form_field_label + '"'

                # empty form field label
                else:
                    self.empty_form_field_labels += 1
                    if form_field_type in ['checkbox', 'file', 'radio'] or form_field_tag == 'select':
                        form_field_label = 'On page "' + title + '", ' + 'select "' + form_field_type + '"'
                    else:
                        form_field_label = 'On page "' + title + '", enter data into form field'

            form_field_labels.append(form_field_label)

        return form_field_labels


    def process_table_element(self, form_field_dom, form_field_label):
        """
        Process an element of a table to produce a meaningful label

        Parameters:
            form_field_dom (dom): dom of the form field element in the table
            form_field_label: current label for this element; if empty, row needs to be calculated

        Returns:
            label (str): label for the table element
        """
        form_field_type = form_field_dom.get('type')
        if not form_field_type:
            form_field_type = 'form field'
        # go up till tr tag reached, to get the row dom
        row_dom = form_field_dom
        while row_dom is not None and row_dom.tag != 'tr':
            row_dom = row_dom.getparent()

        # check if more than 1 input element in the row with the same type as this form field
        more_than_one_input_element = False
        for element in row_dom.iterchildren():
            for nested_element in element.iterchildren():  #each row element is a td or th element
                if nested_element.tag == 'input' and nested_element != form_field_dom and nested_element.get('type') != form_field_type:
                    more_than_one_input_element = True
                    break

        # if more than one input element, then column is also needed
        # calculate column number
        if more_than_one_input_element:
            col_num = 0
            for child in row_dom.getchildren():
                child = child.getchildren()[0] #each row element is a td or th element
                if child == form_field_dom:
                    break
                col_num += 0

        # find table dom, to get table label
        table_dom = row_dom
        while table_dom is not None and table_dom.tag != 'table':
            table_dom = table_dom.getparent()

        col_label = ''
        table_label = ''
        first_child = table_dom.getchildren()[0]

        # if more than one input element, need the column number and if it exists, column name
        if more_than_one_input_element:
            if first_child is not None and first_child.tag == 'thead':
                header_row_dom = first_child.getchildren()[0]  # to get to the header row
                col_label = header_row_dom.getchildren()[col_num].text
            if col_label is None or col_label == '':
                col_label = str(col_num)

        # if caption element exists for the table, choose that for the label
        if first_child is not None and first_child.tag == 'caption':
            table_label = first_child.text

        # if no caption element exists, search for a header previous sibling of the table
        else:
            prev_element = table_dom.getprevious()
            if prev_element.tag in ['h1', 'h2', 'h3']:
                table_label = prev_element.text

        verb = 'select ' if form_field_type in ['checkbox', 'file', 'radio'] or form_field_dom.tag == 'select' else 'enter data in '

        # for empty form field label, calculate row number
        if form_field_label is None or form_field_label.strip() == '':

            form_field_row = 1
            for sibling in row_dom.itersiblings(preceding = True):
                form_field_row += 1

            form_field_label = 'row ' + str(form_field_row)

            # add column label as part of the label if more than one input element exists in the row
            if more_than_one_input_element:
                label = 'in table "' + table_label.strip() + '", column "' + col_label + '", ' + verb + form_field_label
            else:
                label = 'in table "' + table_label.strip() + '", ' + verb + form_field_label

        # form field label not empty, row is not needed
        else:
            # add column label as part of the label if more than one input element exists in the row
            if more_than_one_input_element:
                label = 'in table "' + table_label.strip()+ '", column "' + col_label + '", ' + verb + '"' + form_field_label + '"'
            else:
                label = 'in table "' + table_label.strip()+ '", ' + verb + '"' + form_field_label + '"'

        return label


    def get_form_field_label_by_attribute(self, form_field_dom:str):
        """
        Get the label for a form field element by referring to a ranking of attributes

        Parameters:
            form_field_dom (str): the dom of the form field element

        Returns:
            form_field_label (str): the label for the form field element

        """

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
                form_field_label = self.remove_non_english_words(self.lemmatize(self.tokenize(attributes[attr])))
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


    def find_element(self, state_dom: str, eventable_element_identification: str, return_format: str = 'dom'):
        """
        Locates the eventable element in the web page

        Parameters:
            state_dom (str): DOM of the web page in string format
            eventable_element_xpath (str): xpath of the eventable element in string format
            return_format (str): return format of eventable element location, default 'dom'

        Returns:
            HtmlElement location of the element in the web page
        """
        eventable_element_by_method = eventable_element_identification['how']
        eventable_element_identification = eventable_element_identification['value'].lower()


        try:
            # get eventable based on by method
            tree = html.fromstring(state_dom)
            if eventable_element_by_method == 'xpath':
                element = tree.xpath(eventable_element_identification)
            elif eventable_element_by_method == 'id':
                element = tree.xpath('//*[@id="%s"]' % eventable_element_identification)
            elif eventable_element_by_method == 'name':
                element = tree.xpath('//*[@name="%s"]' % eventable_element_identification)
            elif eventable_element_by_method == 'tag':
                element = tree.xpath('//*[@tagName="%s"]' % eventable_element_identification)
            elif eventable_element_by_method == 'text':
                element = tree.xpath('//*[@linkText="%s"]' % eventable_element_identification)
            elif eventable_element_by_method == 'partialText':
                element = tree.xpath('//*[@partialLinkText="%s"]' % eventable_element_identification)

            if not element or len(element) == 0:
                return None

            if return_format == "str":
                return etree.tostring(element[0]).decode('UTF-8')
            return element[0]
        except Exception as e:
            logging.debug(eventable_element_identification)
            logging.debug(e)
            return None

    def get_context_dom(self, state_dom: str, eventable_element_identification: str):
        """
        Gets context dom of the eventable element by finding the oldest parent of the eventable element without another child

        Parameters:
            state_dom (str): DOM of the web page
            eventable_element_xpath (str): xpath of the eventable in this DOM

        Returns:
            Context dom of the eventable in string format
        """

        curr_dom = self.find_element(state_dom, eventable_element_identification)

        if curr_dom is None:
            return ""
        # stop when we find more than one eventable element
        # at most find the fourth parent
        iterations = 0
        while curr_dom.getparent() and iterations <= 3:
            curr_dom_etree = ''
            try:
                curr_dom_string = etree.tostring(curr_dom).decode('UTF-8').strip()
                if not curr_dom_string.endswith(">"):
                    curr_dom_string = curr_dom_string[:curr_dom_string.rindex(">") + 1]
                curr_dom_etree = etree.parse(StringIO(curr_dom_string))

            except Exception as e:

                # TODO: check whether normal execution should continue after exception handling

                logging.warning(curr_dom_string)
                logging.warning("Exception while getting parent", e)
                return ''
            parent = curr_dom.getparent()
            if (parent is None) or (parent and curr_dom_etree and self.contains_more_than_one_eventable(
                    curr_dom_etree)):
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

        dom_str = etree.tostring(dom).decode('UTF-8')
        dom_without_context = etree.parse(StringIO(dom_str))

        xpath_res = dom_without_context.xpath("//*[self::a or self::input[@type='submit'] or self::button]")

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
    # save analysis outputs in tkltest/generate/ui/analysis_outputs

    file = json.load(open('crawl_paths_tmf_small.json'))
    curr_labels = dict()
    method_labels = dict()
    total_clickables = 0
    total_form_field_elements = 0
    empty_clickable_labels = 0
    empty_form_field_labels = 0

    eventable_dom_label_table = []
    heuristic_label = HeuristicLabel('ranked_attributes_short.json')
    heuristic_label.get_element_and_method_labels(file)
    for crawlpath in file:
        for eventable in crawlpath:
            curr_labels[eventable['id']] = heuristic_label.eventable_labels[eventable['id']]
            eventable_id_path = '_'.join([
                str(eventable['id']) for eventable in crawlpath
            ])
            method_labels[eventable_id_path] = heuristic_label.method_labels[eventable_id_path]

    print(curr_labels)
    print(method_labels)
