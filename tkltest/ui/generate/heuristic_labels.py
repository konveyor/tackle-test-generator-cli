import json
import sys

import nltk
from nltk.corpus import stopwords
from nltk.stem import WordNetLemmatizer
import string
import logging
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

        Args:
                    rankings_file_path (str): A string path to the ranked_attributes.json file

        Returns:
                    Nothing, creates a ranked_attributes list and rankings_set set to find the labels"""
        self.english_words = set(nltk.corpus.words.words())
        self.english_stopwords = set(stopwords.words('english'))
        self.html_stop_words = set(["a", "abbr", "acronym", "address", "area", "b", "base", "bdo", "big", "blockquote",
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
                                "href"])

        with open(rankings_file_path) as f:
            self.ranked_attributes = json.load(f)

        # set of ranked attributes, to search efficiently
        self.rankings_set = set(self.ranked_attributes)
        self.eventable_counts = dict()
        self.eventable_labels = dict()


        with resources.path('tkltest.ui.generate', 'ranked_attributes_form_fields.json') as attr_file:
            with open(attr_file) as f:
                self.ranked_attributes_form_fields = json.load(f)



        with resources.path('tkltest.ui.generate', 'ranked_attributes_form_fields.json') as attr_file:
            with open(attr_file) as f:
                self.ranked_attributes_form_fields = json.load(f)

        self.empty_eventable_labels = 0
        self.empty_form_field_labels = 0



    ########################################## NLP preprocessing ########################################################

    def preprocess(self, s):
        """
        Preprocess value of ranked attributes of eventables-  tokenize, remove stop words, lemmatize and remove non-English words

        Args:
                    s (str): A string word or phrase to be preprocessed

        Returns:
                    s (str): The preprocessed version of this input string """

        s = self.remove_non_english_words(self.lemmatize(self.remove_stop_words(self.tokenize(s))))

        return s


    def tokenize(self, s: str):
        """
        Tokenize input string to remove uppercase, camel case, snake case, etc. eg. TableSortTrigger -> table sort trigger

        Args:
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
        s = [word for word in s if word not in self.english_stopwords]
        s = [word for word in s if word not in self.html_stop_words]
        return ' '.join(s)


    def lemmatize(self, s):
        s = list(s.split(' '))
        lemmatizer = WordNetLemmatizer()
        s = [lemmatizer.lemmatize(word) for word in s]
        s = ' '.join(s)
        return s


    def remove_non_english_words(self, s):
        logging.info('Removing non-english words for string of size {}'.format(len(s)))
        s = nltk.wordpunct_tokenize(s)
        logging.info('Tokenized s')
        s = list(word for word in s if word in self.english_words)
        logging.info('Removed non-english words to create array of size {}'.format(len(s)))
        s = ' '.join(s)
        logging.info('Joined s to a string')
        return s


    def remove_punctuation(self, s):
        if not s:
            return ''
        punc = '''!()-[]{};:'"\,<>.?@#$%^&*_~'''
        for chr in s:
            if chr in punc:
                s = s.replace(chr, '')
        return self.to_lowercase(s.strip())


    def to_lowercase(self, s):
        if not s:
            return ''
        return s.lower()

    ########################################## Compute Labels ########################################################

    def get_element_and_method_labels(self, crawl_paths):
        """
        Get element level and method level labels

        Args:
            crawl_paths (list): list of crawl paths, output by CrawlJax

        Returns:
            None (stores element and method level labels in self.eventable_labels and self.method_labels

        """
        # to store eventable and method labels, as well as frequency of eventable labels
        self.eventable_labels = dict()
        self.method_labels = dict()
        self.eventable_counts = dict()

        for crawl_path in crawl_paths:
            for eventable in crawl_path:
                label = self.get_label(eventable)[0]
                if label not in self.eventable_counts:
                    self.eventable_counts[label] = 1
                else:
                    self.eventable_counts[label] += 1

        # store method labels with id eventable path
        for crawl_path in crawl_paths:
            eventable_id_path = '_'.join([
                str(eventable['id']) for eventable in crawl_path
            ])
            method_label = self.get_method_label(crawl_path)
            self.method_labels[eventable_id_path] = method_label


    def get_method_label(self, crawl_path):
        """
        Calculates method level label

        Args:
            crawl_path (list): a single crawl path, depicting a single output test method

        Returns:
            method_label (str): label for the method

        """
        i = 0
        n = len(crawl_path)
        path = []
        method_label = []
        unique_eventables = []
        while i < n:
            eventable = crawl_path[i]
            if self.eventable_counts[self.eventable_labels[eventable['id']][0]] == 1:
                unique_eventables.append(self.eventable_labels[eventable['id']][0])
            flag = False
            for form_name in self.eventable_labels[eventable['id']][2]:
                form_fill_line = 'enters values in form "{}"'.format(form_name)
                # check if it submits this form
                if self.submits_form(self.eventable_labels[eventable['id']][0]):
                    flag = True
                    path.append('{} and submits the form.'.format(form_fill_line))
                else:
                    path.append('{}.'.format(form_fill_line))

           # if form is submitted, don't add the submitting clickable to the following navigation
            if flag:
                i += 1
                continue

            if i < n:
                eventable_path = [self.eventable_labels[crawl_path[i]['id']][0].split(',')[-1].strip()]
                i += 1

            # get navigation of clickables until the next form field is filled
            while i < n and len(self.eventable_labels[crawl_path[i]['id']][2]) == 0:
                eventable_path.append(self.eventable_labels[crawl_path[i]['id']][0].split(',')[-1].strip())
                if self.eventable_counts[self.eventable_labels[crawl_path[i]['id']][0]] == 1:
                    unique_eventables.append(self.eventable_labels[crawl_path[i]['id']][0])
                i += 1
            path.append('navigates the path: {}.'.format(', '.join(eventable_path)))

        # make final method summary
        method_label.append('This test {}'.format(path[0]))
        for line in path[1:]:
            method_label.append(('Then it {}'.format(line)))
        if len(unique_eventables) > 0:
            method_label.append('The unique clickables in this path are: {}.'.format(', '.join(unique_eventables)))

        return '\n\t* '.join(method_label)

    def submits_form(self, eventable_label):
        """
        Checks whether the eventable label marks a submission

        Args:
            eventable_label (str): check whether the eventable label is a submission

        Returns:
            boolean: whether or not the eventable label depicts a submission
        """

        # button label between double quotes in eventable label
        button_label = eventable_label.split('"')[-2].strip()

        # split button label into words to check if any submit words exist
        button_word_set = button_label.split(' ')
        submit_words = ['next', 'continue', 'enter', 'submit', 'save', 'done', 'update']
        for word in button_word_set:
            if word in submit_words:
                return True
        return False

    def get_label(self, eventable):
        """
        Get the label for an eventable element, its form field elements, and forms filled

        Args:
            eventable (dict): A json dict of the eventable

        Returns:
            label (str): The label for this eventable based on either the element or its context dom """

        if eventable['id'] in self.eventable_labels:
            return self.eventable_labels[eventable['id']]

        # get title of the source web page
        title = self.get_title(eventable['source']['dom'])

        element_dom = self.find_element(eventable['source']['dom'], eventable['identification'])

        heuristic_label = ''
        if element_dom is not None:
            # check if this element dom has text
            heuristic_label = element_dom.text

        # otherwise, calculate label using element dom
        if not heuristic_label or heuristic_label.strip() == '':
            heuristic_label = self.get_element_label(eventable['element'], element_dom)

            # calculate label using context dom
            if heuristic_label.strip() == '':
                context_dom = self.get_context_dom(eventable['source']['dom'],
                                                   eventable['identification'])
                heuristic_label = self.get_context_label(context_dom)

        heuristic_label = heuristic_label.strip().lower()
        if heuristic_label == '':
            self.empty_eventable_labels += 1

        # add title and verb to label
        heuristic_label = 'on page "{}", {} "{}"'.format(title, eventable['eventType'], heuristic_label)

        # get all form fields
        form_field_labels = self.get_form_field_labels(eventable)

        # get all forms filled before clicking this eventable
        forms_filled = set()
        for form_input in eventable['relatedFormInputs']:
            # check if form input is part of a form
            form_field_dom = self.find_element(eventable['source']['dom'], form_input['identification'])
            form_header = self.get_form_header(form_field_dom)
            if form_header != '':
                forms_filled.add(form_header)

        # store eventable and form field labels in dictionary
        self.eventable_labels[eventable['id']] = [heuristic_label, form_field_labels, list(forms_filled)]

        return [heuristic_label, form_field_labels, list(forms_filled)]


    def get_form_field_extended_dom(self, source_dom, form_field_identification, return_format = 'dom'):
        """
        Get the extended dom of a form field element, which contains the label for that field

        Args:
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

    def get_title(self, source_dom):
        """
        Get title of the source dom web page.

        Args:
            source_dom (str): string dom of source web page

        Returns:
            title (str): string title name
        """
        tree = html.fromstring(source_dom)
        title_element = tree.xpath('/html[1]/head[1]/title[1]')
        return title_element[0].text.strip()

    def get_form_field_labels(self, eventable):
        """
        Get label for all the form fields for this eventable element

        Args:
            eventable (dict): A json dict of the eventable

        Returns:
            form_field_labels (list): a list of labels corresponding to all the form fields for this eventable

        """
        # get title text for the source dom
        title = self.get_title(eventable['source']['dom'])

        # get all form field labels for this eventable
        form_field_labels = []
        for form_input in eventable['relatedFormInputs']:
            form_field_dom_str = self.find_element(eventable['source']['dom'], form_input['identification'], 'str')
            form_field_dom = self.find_element(eventable['source']['dom'], form_input['identification'])
            value = form_input['inputValues'][0]['value']

            # form field dom not found
            if form_field_dom is None:
                # if no dom found, not possible to calculate the label
                self.empty_form_field_labels += 1
                form_field_labels.append('Enter data into form field')
                continue

            form_field_label = ''
            # get type of the form field
            form_field_type = form_field_dom.get('type')
            if not form_field_type:
                form_field_type = 'form field'

            # handle select form fields
            if form_field_dom.tag == 'select':
                form_field_label = self.process_select_element(form_field_dom)


            if not form_field_label or form_field_label.strip() == '':
                # check if form field dom has any text
                form_field_label = self.remove_punctuation(form_field_dom.text)

            # if no text found in form field dom, try to get the label sibling of this dom
            if not form_field_label or form_field_label.strip() == '':
                form_field_label = self.get_label_sibling(form_field_dom)

            # if no label elements found, check for a few useful attributes
            if not form_field_label or form_field_label.strip() == '':
                for attr in ['aria-label', 'value', 'placeholder']:
                    form_field_label = self.remove_punctuation(form_field_dom.get(attr))
                    if form_field_label:
                        break

            # if still no label has been generated, check in the extended dom of the form field for a label or aria-label
            if not form_field_label or form_field_label.strip() == '':
                form_field_extended_dom = self.get_form_field_extended_dom(eventable['source']['dom'], form_input['identification'])
                form_field_label = self.get_label_from_dom(form_field_extended_dom)

            # process table element
            parent = form_field_dom.getparent()
            if parent.tag in ['tr', 'th', 'td']:
                form_field_label = 'on page "{}", {}'.format(title, self.process_table_element(form_field_dom, form_input, self.remove_punctuation(form_field_label)))

            # non table element
            else:
                # if no label has been generated so far, check directly with the ranked attributes for form fields
                if not form_field_label or form_field_label.strip() == '':
                    form_field_label = self.get_form_field_label_by_attribute(form_field_dom_str)

                # get final label based on form field type
                verb = 'select' if form_field_type in ['checkbox', 'file', 'radio'] or form_field_dom.tag == 'select' else 'enter'
                form_field_label = self.generate_concatenated_label(title, verb, form_field_type, form_field_label.strip(), value)

            form_field_labels.append(form_field_label)

        return form_field_labels

    def get_form_header(self, form_field_dom):
        """
        Get label/header for a form

        Args:
            form_field_dom (dom): dom of the form field element

        Returns:
            form header (str): label/header for the form

        """
        # if part of a form, get form header or label for the form element

        # go up till we find a form dom, if any
        parent_dom = form_field_dom
        while parent_dom is not None and parent_dom.tag != 'form':
            parent_dom = parent_dom.getparent()
        if parent_dom is None or parent_dom.tag != 'form':
            # no form element
            return ''
        prev_sibling = parent_dom.getprevious()
        while prev_sibling is not None and prev_sibling.tag not in ['h1', 'h2', 'h3', 'label']:
            prev_sibling = prev_sibling.getprevious()
        if prev_sibling is not None:
            if prev_sibling.tag in ['h1', 'h2', 'h3']:
                return self.remove_punctuation(prev_sibling.text)

            # label element
            label_sibling = prev_sibling
            label = label_sibling.text
            if label and label.strip() != '':
                return self.remove_punctuation(label)
            # a child of label could have the text
            for child in label_sibling.getchildren():
                label = child.text
                if label and label.strip() != '':
                    return self.remove_punctuation(label)
        # no form header or form label present
        return ''



    def generate_concatenated_label(self, title, verb, form_field_type, form_field_label, value):
        """
        Generate the complete label for an element by concatenating inputs

        Args:
            title (str): title of the source web page
            verb (str): verb to be used in concatenated sentence
            form_field_type (str): type of form field
            form_field_label (str): label calculated for just the form field element
            value (str): value selected/entered in the form field element

        Returns:
            form_field_label (str): concatenated label for the form field

        """
        if form_field_label is not None and form_field_label.strip() != '':
            form_field_label = 'on page "{}", {} "{}: {}"'.format(title, verb, form_field_label, value)
        else:
            self.empty_form_field_labels += 1
            if form_field_type not in ['checkbox', 'file', 'radio']:
                form_field_type = 'form field'
            form_field_label = 'on page "{}", {} "{}: {}"'.format(title, verb, form_field_type, value)
        return form_field_label


    def get_label_sibling(self, form_field_dom):
        """
        Get label text from the label sibling of the form field dom if it exists

        Args:
            form_field_dom (dom): dom of the form field

        Returns:

        """

        # if no id, only a previous sibling of the form field dom can be its label
        label = ''

        # if this label applies to more than one input element, need more than overall label to identify this element
        num_siblings_between = 0
        if not form_field_dom.get('id'):
            previous_sibling = form_field_dom.getprevious()
            while previous_sibling is not None and previous_sibling.tag != 'label':
                num_siblings_between += 1
                previous_sibling = previous_sibling.getprevious()

            if previous_sibling is not None:
                label = self.remove_punctuation(previous_sibling.text)
                if not label:
                    for child in previous_sibling.iterchildren():
                        if child.text is not None:
                            label = child.text

            if label and num_siblings_between > 0:
                # use the name attribute of the dom as well
                label = '{} [{}]'.format(label, self.remove_punctuation(form_field_dom.get('name')))

        # if id, search for the sibling which has this id
        else:
            form_field_id = form_field_dom.get('id')
            for sibling in form_field_dom.itersiblings(preceding=True):
                if sibling.get('for') == form_field_id:
                    label = self.remove_punctuation(sibling.text)
                    if not label:
                        for child in sibling.iterchildren():
                            if child.text is not None:
                                label = self.to_lowercase(child.text.strip())
                                break
            if not label or label.strip() == '':
                for sibling in form_field_dom.itersiblings(preceding=False):
                    if sibling.get('for') == form_field_id:
                        label = self.remove_punctuation(sibling.text)
                        if not label:
                            for child in sibling.iterchildren():
                                if child.text is not None:
                                    label = self.to_lowercase(child.text.strip())
                                    break
        return label

    def get_label_from_dom(self, dom):
        """
        Get the label from the dom by considering label and aria-label attributes within the dom, as well as a label for this entire dom

        Args:
            dom (dom): extended form field dom, either containing a label, following a label, or containing more than one eventable

        Returns:
            label (str): label extracted from this extended dom

        """
        label = ''
        if dom is None:
            return label
        label = dom.get('label')
        if not label:
            label = dom.get('aria-label')
        if not label:
            label = dom.text
        prev_sibling = dom.getprevious()
        if not label and prev_sibling is not None and prev_sibling.tag == 'label':
            label = prev_sibling.text
        return self.remove_punctuation(label)

    def process_table_element(self, form_field_dom, form_input, form_field_label):
        """
        Process an element of a table to produce a meaningful label

        Args:
            form_field_dom (dom): dom of the form field element in the table
            form_field_label: current label for this element; if empty, row needs to be calculated

        Returns:
            label (str): label for the table element
        """
        value = form_input['inputValues'][0]['value']
        form_field_type = form_field_dom.get('type')
        if not form_field_type:
            form_field_type = 'form field'

        # go up till tr tag reached, to get the row dom
        row_dom = form_field_dom
        while row_dom is not None and row_dom.tag != 'tr':
            row_dom = row_dom.getparent()

        # check if more than 1 input element in the row with the same type as this form field
        more_than_one_input_element = self.contains_more_than_one_input(row_dom, form_field_dom)

        # find table dom, to get table label
        table_dom = row_dom
        while table_dom is not None and table_dom.tag != 'table':
            table_dom = table_dom.getparent()

        # if more than one input element, need the column number and if it exists, column name
        col_label = ''
        if more_than_one_input_element:
            col_label = self.get_column_label(table_dom, row_dom, form_field_dom)

        table_label = self.get_table_label(table_dom)
        if not table_label or table_label.strip() == '':
            # check for form header
            form_header = self.get_form_header(table_dom)
            if form_header and form_header.strip() != '':
                table_label = form_header

        verb = 'select' if form_field_type in ['checkbox', 'file', 'radio'] else 'enter data in'

        # for empty form field label, calculate row number
        if form_field_label is None or form_field_label.strip() == '':
            form_field_row = 1
            for sibling in row_dom.itersiblings(preceding = True):
                form_field_row += 1
            form_field_label = 'row ' + str(form_field_row)

        if more_than_one_input_element:
            label = 'in table "{}", column "{}", {} "{}: {}"'.format(table_label.strip(), col_label, verb, form_field_label, value)
        else:
            label = 'in table "{}", {} "{}: {}"'.format(table_label, verb, form_field_label, value)
        return label



    def get_table_label(self, table_dom):
        """
        Get label for the table element

        Args:
            table_dom (dom):  dom of the table element

        Returns:
            table label (str): label for the table

        """
        table_label = ''
        first_child = table_dom.getchildren()[0]
        # if caption element exists for the table, choose that for the label
        if first_child is not None and first_child.tag == 'caption':
            table_label = first_child.text

        # if no caption element exists, search for a header previous sibling of the table
        else:
            prev_element = table_dom.getprevious()
            if prev_element is not None and prev_element.tag in ['h1', 'h2', 'h3']:
                table_label = prev_element.text
        return self.remove_punctuation(table_label)

    def get_column_label(self, table_dom, row_dom, form_field_dom):
        """
        Get label for column of a table

        Args:
            table_dom (dom): dom of the table
            row_dom (dom): dom of the row in the table which contains the form field
            form_field_dom (dom): dom of the form field element

        Returns:
            col_label (str): label for the column
        """
        col_num = 0
        for child in row_dom.getchildren():
            child = child.getchildren()[0]  # each row element is a td or th element
            if child == form_field_dom:
                break
            col_num += 0

        first_child = table_dom.getchildren()[0]
        if first_child is not None and first_child.tag == 'thead':
            header_row_dom = first_child.getchildren()[0]  # to get to the header row
            # get name of this column
            col_label = header_row_dom.getchildren()[col_num].text

        # if no name is present for this column number, its number is the column label
        if col_label is None or col_label == '':
            col_label = str(col_num)
        return col_label


    def contains_more_than_one_input(self, row_dom, form_field_dom):
        """
        Checks if there is more than one input element in the row dom with the same type as the form field dom

        Args:
            row_dom (dom): dom of the row
            form_field_dom (dom): dom of the form field

        Returns:
            more_than_one_input_element (boolean): whether or not more than one such element exists

        """
        # check if more than 1 input element in the row with the same type as this form field
        form_field_type = form_field_dom.get('type')
        if not form_field_type:
            form_field_type = 'form field'
        more_than_one_input_element = False
        for element in row_dom.iterchildren():
            for nested_element in element.iterchildren():  # each row element is a td or th element
                if nested_element.tag == 'input' and nested_element != form_field_dom and nested_element.get(
                        'type') != form_field_type:
                    more_than_one_input_element = True
                    break
        return more_than_one_input_element

    def process_select_element(self, form_field_dom):
        """
        Handle processing for select elements

        Args:
            form_field_dom (dom): dom of the form field

        Returns:
            label (str): label for the select form field

        """
        # go back through all siblings till a label element is found
        # combine with the element selected from the drop down
        main_label = ''
        element_name = form_field_dom.get('name')
        prev_sibling = form_field_dom.getprevious()
        # if more than one element depends on main label, only then use element name as well
        num_siblings_between = 0
        while prev_sibling is not None and prev_sibling.tag != 'label':
            num_siblings_between += 1
            prev_sibling = prev_sibling.getprevious()
        if prev_sibling is not None:
            main_label = prev_sibling.text
        if not main_label:
            main_label = ''
        if not element_name:
            element_name = ''
        main_label = self.remove_punctuation(main_label).strip()
        element_name = element_name.strip()
        label = ''
        if main_label != '' and element_name != '' and num_siblings_between > 0:
            # need element name
            label = '{} [{}]'.format(main_label, element_name)
        elif main_label != '':
            label = main_label
        elif element_name != '':
            label = element_name
        return label


    def get_form_field_label_by_attribute(self, form_field_dom:str):
        """
        Get the label for a form field element by referring to a ranking of attributes

        Args:
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
        return self.remove_punctuation(form_field_label)


    def get_element_label(self, eventable, element_dom):
        """ get heuristic labels by selecting the highest ranked attribute which this eventable has

        Args:
                    eventable (dict): A json dict of the element field of the eventable
                    element_dom (dom): dom fragment of the element

        Returns:
                    element_label (str): The label for this eventable based on the element itself"""

        element_label = ''

        # first check for a few useful attributes in element dom
        if element_dom is not None:
            if element_dom.get('title') is not None and element_dom.get('title') != '':
                return element_dom.get('title')

            if element_dom.get('alt') is not None and element_dom.get('alt') != '':
                return element_dom.get('alt')

            for child in element_dom.iterchildren():
                if child is not None and child.text is not None and child.text.strip() != '':
                    return child.text.strip()

            for child in element_dom.iterchildren():
                if child is not None and child.get('title') is not None and child.get('title').strip() != '':
                    return child.get('title')

            for child in element_dom.iterchildren():
                if child is not None and child.get('alt') is not None and child.get('alt').strip() != '':
                    return child.get('alt')

        # check in eventable dict
        for attr in self.ranked_attributes:

            if attr == 'text' and attr in eventable:
                element_label = self.preprocess(eventable[attr])

            elif 'attributes' in eventable and attr in eventable['attributes']:
                if attr == 'href':
                    # special case for "href" attribute because its value
                    # is usually longer and more complicated than other
                    # attributes
                    href = eventable['attributes']['href']
                    tokenized_href = self.preprocess(href.strip().split('?')[-1])
                    kw_model = KeyBERT()
                    keywords = kw_model.extract_keywords(tokenized_href, keyphrase_ngram_range=(1, 2),
                                                         stop_words='english', use_mmr=True,
                                                         diversity=0.7)
                    if keywords:
                        element_label = keywords[0][0]
                    else:
                        element_label = href.strip().split("/")[-1].split(".")[0]
                        element_label = self.preprocess(element_label)
                elif attr == 'value':
                    # usually has more value and does not need to be that preprocessed
                    element_label = self.to_lowercase(eventable['attributes'][attr])
                else:
                    element_label = self.preprocess(eventable['attributes'][attr])
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

        Args:
            context_dom (str): context DOM for which we need to calculate the label

        Returns:
            label (str): label for this context DOM
        """
        logging.info('Preprocessing context dom to generate a label')
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
        label = self.preprocess(label)
        return label.strip()


    def find_element(self, state_dom: str, eventable_element_identification: str, return_format: str = 'dom'):
        """
        Locates the eventable element in the web page

        Args:
            state_dom (str): DOM of the web page in string format
            eventable_element_identification (dict): identification to get the element
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

        Args:
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

        Args:
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

        Args:
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


    def parse_dom_to_dict(self, dom: str):
        """
        Parses input string dom to a dict using the xmltodict library

        Args:
            dom (str): input string DOM

        Returns:
            context_dom_dict (dict): dictionary of the parsed DOM
        """
        if not dom.endswith('>'):
            dom = dom[:dom.rindex('>') + 1]
        context_dom_str = r"<html>" + dom + r"</html>"
        context_dom_dict = xd.parse(context_dom_str)
        # print(f"context dom dict: {context_dom_dict}")
        return context_dom_dict


    def dict_value_to_str(self, context_dom_input):
        """
        Converts dict context dom to string

        Args:
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
    app = sys.argv[1]
    file = json.load(open('crawl_paths_{}.json'.format(app)))
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


