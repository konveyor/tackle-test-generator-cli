import json
import nltk
from nltk.corpus import stopwords
from nltk.stem import WordNetLemmatizer
import string
import logging



class HeuristicLabel:

    # parse and preprocess crawl path data for the object corresponding to this crawlpaths file
    def __init__(self, crawlfile_path, rankings_path):

        # print(string.__version__)

        self.crawlpaths = json.load(open(crawlfile_path))
        self.ranked_attributes = json.load(open(rankings_path))

        # set of ranked attributes
        self.rankings_set = set(self.ranked_attributes)

        # dictionary of clickable id to the dictionary of ranked attributes it has
        self.clickable_attributes = dict()

        # set of unique clickable ids
        self.unique_clickables = set()

        # iterating over crawlpaths to store all ranked attributes for each clickable in a dictionary
        for crawlpath in self.crawlpaths:

            # iterating over clickables in each crawlpath
            for clickable in crawlpath:
                curr_clickable_attributes = dict() # dictionary of ranked attributes for this clickable

                # clickable already seen in another crawl path
                if clickable['id'] in self.unique_clickables:
                    continue

                self.unique_clickables.add(clickable['id'])

                # iterate over attributes for each clickable to store preprocessed ranked attributes
                for attr in clickable['element']:
                    if attr in self.rankings_set:
                        curr_clickable_attributes[attr] = self.preprocess(clickable['element'][attr])

                    # store nested attributes, within outer label 'attributes', as well
                    elif attr == 'attributes':
                        for nested_attr in clickable['element'][attr]:
                            if nested_attr in self.rankings_set:
                                curr_clickable_attributes[nested_attr] = self.preprocess(clickable['element'][attr][nested_attr])

                # class dictionary of all clickable ids to their ranked attribute values
                self.clickable_attributes[clickable['id']] = curr_clickable_attributes
        logging.info('Stored preprocessed ranked attributes of all clickables in all crawlpaths')

    # preprocess value of ranked attributes of clickables
    def preprocess(self, s):
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

    # tokenize camel case/snake case strings eg. TableSortTrigger -> table sort trigger
    def tokenize(self, s: str):
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


    # to get heuristic labels by selecting the highest ranked attribute which this clickable has
    def get_labels(self):

        logging.info('Getting highest ranked attribute for each clickable')
        # to store final labels
        self.heuristic_labels = dict()

        for id in self.clickable_attributes:

            # default label is the empty string
            self.heuristic_labels[id] = ''

            # going through ranked attributes from highest to lowest ranked
            for ranked_attr in self.ranked_attributes:
                if ranked_attr in self.clickable_attributes[id] and self.clickable_attributes[id][ranked_attr] != '':
                    self.heuristic_labels[id] = self.clickable_attributes[id][ranked_attr]
                    break

        # dictionary of clickable id to string label

        logging.info('Got highest ranked attribute for each clickable')
        return self.heuristic_labels


# for class testing
# if __name__ == "__main__":
#     heuristic_label = HeuristicLabel('CrawlPaths.json', 'ranked_attributes.json')
#     heuristic_label_dict = heuristic_label.get_labels()
#     print(heuristic_label_dict)