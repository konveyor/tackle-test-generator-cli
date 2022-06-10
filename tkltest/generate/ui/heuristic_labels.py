import json
import nltk
from nltk.corpus import stopwords
from nltk.stem import WordNetLemmatizer
import string
import logging


class HeuristicLabel:

    def __init__(self, rankings_file_path):
        """ initialize with the ranked attribute order to use while calculating the label

        Parameters:
                    rankings_file_path (str): A string path to the ranked_attributes.json file

        Returns:
                    Nothing, creates a ranked_attributes list and rankings_set set to find the labels"""

        with open(rankings_file_path) as f:
            self.ranked_attributes = json.load(f)

        # set of ranked attributes, to search efficiently
        self.rankings_set = set(self.ranked_attributes)

    def get_label(self, eventable):
        """ get heuristic labels by selecting the highest ranked attribute which this eventable has

        Parameters:
                    eventable (str): A json string path of the element field of the clickable

        Returns:
                    label (str): The label for this clickable """

        curr_eventable_attributes = dict()  # dictionary of ranked attributes for this eventable
        for attr in eventable:
            if attr in self.rankings_set:
                curr_eventable_attributes[attr] = self.preprocess(eventable[attr])

            # store nested attributes, within outer label 'attributes', as well
            elif attr == 'attributes':
                for nested_attr in eventable[attr]:
                    if nested_attr in self.rankings_set:
                        curr_eventable_attributes[nested_attr] = self.preprocess(
                            eventable[attr][nested_attr])


        logging.info('Getting highest ranked attribute for this eventable')

        for ranked_attr in self.ranked_attributes:
            if ranked_attr in curr_eventable_attributes and curr_eventable_attributes[ranked_attr] != '':
                label = curr_eventable_attributes[ranked_attr]
                break

        logging.info('Got highest ranked attribute for this eventable')
        return label


    def preprocess(self, s):
        """ preprocess value of ranked attributes of eventables

        Parameters:
                    s (str): A string word or phrase to be preprocessed

        Returns:
                    s (str): The preprocessed version of this input string """

        html_stop_words = ["a", "abbr", "acronym", "address", "area", "b", "base", "bdo", "big", "blockquote", "body",
                           "br",
                           "button", "caption", "cite", "code", "col", "colgroup", "dd", "del", "dfn", "div", "dl",
                           "doctype",
                           "dt", "em", "fieldset", "form", "h1", "h2", "h3", "h4", "h5", "h6", "head", "html", "hr",
                           "i", "img",
                           "input", "ins", "kbd", "label", "legend", "li", "link", "map", "meta", "noscript", "object",
                           "ol",
                           "optgroup", "option", "p", "param", "pre", "q", "samp", "script", "select", "small", "span",
                           "strong",
                           "style", "sub", "sup", "table", "tbody", "td", "text", "textarea", "tfoot", "th", "thead",
                           "title",
                           "tr", "tt", "ul", "var", "", "submit", "false", "action", "javascript", "javascript:;",
                           "href"]

        # tokenize
        s = self.tokenize(s)
        s = list(s.split(' '))

        # remove English and html stop words
        s = [word for word in s if word not in set(stopwords.words('english'))]
        s = [word for word in s if word not in set(html_stop_words)]

        # lemmatize tokens
        lemmatizer = WordNetLemmatizer()
        s = [lemmatizer.lemmatize(word) for word in s]
        s = ' '.join(s)

        # remove non-English words
        s = (word for word in nltk.wordpunct_tokenize(s) if word in set(nltk.corpus.words.words()))
        s = ' '.join(s)
        return s

    def tokenize(self, s: str):
        """ tokenize input string to remove uppercase, camel case, snake case, etc. eg. TableSortTrigger -> table sort trigger

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
                        if buffer != "": # uppercase character and non empty buffer

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





# for class testing
# if __name__ == "__main__":
#     heuristic_label = HeuristicLabel('ranked_attributes.json')
#     file1 = json.load(open('eventables_addressbook.json'))
#     file2 = json.load(open('eventables_petclinic.json'))
#     for file in [file1, file2]:
#         for eventable in file:
#             print(heuristic_label.get_label(eventable))
