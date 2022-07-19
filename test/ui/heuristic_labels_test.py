import os
import json
import unittest
from importlib import resources
from tkltest.generate.ui.heuristic_labels import HeuristicLabel


class HeuristicLabelTest(unittest.TestCase):
    helper_file_dir = os.path.join('test', 'ui', 'helper_files')

    def test_heuristic_labels(self):
        """ Test the get_label function of the HeuristicLabel class by testing on example labels
                Parameters:
                            None
                Returns:
                            Assertion that labels are correctly produced for a few addressbook and petclinic DOM fragments """

        # Refer to eventable['element'] DOM fragments of addressbook and petclinic stored in the helper_files directory
        with open(os.path.join(self.helper_file_dir, 'crawl_paths_addressbook_small.json')) as file1:
            crawl_paths_addressbook = json.load(file1)
        with open(os.path.join(self.helper_file_dir, 'crawl_paths_petclinic_small.json')) as file2:
            crawl_paths_petclinic = json.load(file2)
        with open(os.path.join(self.helper_file_dir, 'crawl_paths_tmf_small.json')) as file3:
            crawl_paths_tmf = json.load(file3)

        correct_labels = [{2: ['On page "Address book", click "post login"', ['On page "Address book", enter "user"', 'On page "Address book", enter "password"']], 3: ['On page "Address book", click "next birthdays"', ['On page "Address book", enter "search for any text"', 'On page "Address book", select "checkbox"', 'On page "Address book", enter "group"', 'On page "Address book", enter "to group"']], 4: ['On page "Address book", click "export"', ['On page "Address book", enter "search for any text"', 'On page "Address book", select "checkbox"', 'On page "Address book", enter "group"', 'On page "Address book", enter "to group"']], 5: ['On page "Address book ([none])", click "groups"', []], 6: ['On page "Address book", click "print all"', ['On page "Address book", enter "search for any text"', 'On page "Address book", select "checkbox"', 'On page "Address book", enter "group"', 'On page "Address book", enter "to group"']], 7: ['On page "Address book (cEyhmIaV)", click "v8.0.0 - r475"', []], 9: ['On page "Address book", click "import"', ['On page "Address book", enter "search for any text"', 'On page "Address book", select "checkbox"', 'On page "Address book", enter "group"', 'On page "Address book", enter "to group"']], 11: ['On page "Address book ([none])", click "v8.0.0 - r475"', []], 12: ['On page "Address book", click "print phones"', ['On page "Address book", enter "search for any text"', 'On page "Address book", select "checkbox"', 'On page "Address book", enter "group"', 'On page "Address book", enter "to group"']]},
                          {2: ['On page "SpringPetclinicAngular", click "veterinarians"', []], 3: ['On page "SpringPetclinicAngular", click "owner add"', []]},
                          {2: ['On page "Table Maintenance Facility", click "important notice"', ['On page "Table Maintenance Facility", enter "search form"']], 3: ['On page "Table Maintenance Facility | News article detail", click "table maintenance facility"', ['On page "Table Maintenance Facility | News article detail", enter "search form"']], 4: ['On page "Table Maintenance Facility", click "gpp"', ['On page "Table Maintenance Facility", enter "search form"']], 5: ['On page "Table Maintenance Facility | News article detail", click "previous"', ['On page "Table Maintenance Facility | News article detail", enter "search form"']], 6: ['On page "Table Maintenance Facility", click "tptptp"', ['On page "Table Maintenance Facility", enter "search form"']], 7: ['On page "Table Maintenance Facility | News article detail", click "table maintenance facility"', ['On page "Table Maintenance Facility | News article detail", enter "search form"']], 8: ['On page "Table Maintenance Facility", click "pqr"', ['On page "Table Maintenance Facility", enter "search form"']], 9: ['On page "Table Maintenance Facility | News article detail", click "user activities"', ['On page "Table Maintenance Facility | News article detail", enter "search form"']], 10: ['On page "Table Maintenance Facility", click "testy"', ['On page "Table Maintenance Facility", enter "search form"']], 11: ['On page "Table Maintenance Facility | News article detail", click "administration"', ['On page "Table Maintenance Facility | News article detail", enter "search form"']], 12: ['On page "Table Maintenance Facility | Administration", click "table maintenance facility"', ['On page "Table Maintenance Facility | Administration", enter "search form"']], 13: ['On page "Table Maintenance Facility", click "<script>alert(\'xss’)</script>"', ['On page "Table Maintenance Facility", enter "search form"']], 14: ['On page "Table Maintenance Facility | News article detail", click "aiw bop"', ['On page "Table Maintenance Facility | News article detail", enter "search form"']], 15: ['On page "Table Maintenance Facility", click "show all news articles"', ['On page "Table Maintenance Facility", enter "search form"']], 16: ['On page "Table Maintenance Facility | News article summary", click "table maintenance facility"', ['On page "Table Maintenance Facility | News article summary", enter "search form"', 'On page "Table Maintenance Facility | News article summary", in table "News alerts", select "Important Notice"', 'On page "Table Maintenance Facility | News article summary", in table "News alerts", select "GPP"', 'On page "Table Maintenance Facility | News article summary", in table "News alerts", select "TPTPTP"', 'On page "Table Maintenance Facility | News article summary", in table "News alerts", select "pqr"', 'On page "Table Maintenance Facility | News article summary", in table "News alerts", select "testy"', 'On page "Table Maintenance Facility | News article summary", in table "Informational messages", select row 1', 'On page "Table Maintenance Facility | News article summary", in table "Informational messages", select row 2', 'On page "Table Maintenance Facility | News article summary", in table "Informational messages", select row 3', 'On page "Table Maintenance Facility | News article summary", in table "Informational messages", select "wSXwIaAy"', 'On page "Table Maintenance Facility | News article summary", in table "Informational messages", select "hLBtbeUA"', 'On page "Table Maintenance Facility | News article summary", in table "Informational messages", select "wRviYeWd"', 'On page "Table Maintenance Facility | News article summary", in table "Informational messages", select "Hello World!!!"', 'On page "Table Maintenance Facility | News article summary", in table "Informational messages", select "New Article"', 'On page "Table Maintenance Facility | News article summary", in table "Informational messages", select "TMF is moving to AccessHub"', 'On page "Table Maintenance Facility | News article summary", in table "Informational messages", select "ABC"', 'On page "Table Maintenance Facility | News article summary", in table "Informational messages", select "gggggggg"', 'On page "Table Maintenance Facility | News article summary", in table "Informational messages", select "Study In Flies Allows Researchers To Visualize Formation Of"']], 18: ['On page "Table Maintenance Facility", click "user activities"', ['On page "Table Maintenance Facility", enter "search form"']], 19: ['On page "Table Maintenance Facility | News article detail", click "aiw reference"', ['On page "Table Maintenance Facility | News article detail", enter "search form"']], 20: ['On page "Table Maintenance Facility | Administration", click "table maintenance facility"', ['On page "Table Maintenance Facility | Administration", enter "search form"']], 21: ['On page "Table Maintenance Facility", click "administration"', ['On page "Table Maintenance Facility", enter "search form"']], 22: ['On page "Table Maintenance Facility | Administration", click "user activities"', ['On page "Table Maintenance Facility | Administration", enter "search form"']]}]

        correct_method_labels = [
            {'2_3': 'On page "Address book", click "post login"', '4_5': 'On page "Address book", click "export"', '6_7': 'On page "Address book", click "print all"', '9_11': 'On page "Address book", click "import"', '12': 'On page "Address book", click "print phones"'},
            {'2_3': 'On page "SpringPetclinicAngular", click "veterinarians"'},
            {'2_3_4_5_6_7_8_9': 'On page "Table Maintenance Facility", click "gpp"', '10_11_12_13_14': 'On page "Table Maintenance Facility", click "testy"', '15_16_18': 'On page "Table Maintenance Facility", click "show all news articles"', '2_19': 'On page "Table Maintenance Facility | News article detail", click "aiw reference"', '2_11_20_21_22': 'On page "Table Maintenance Facility", click "administration"'}]

        # check for all DOM fragments in these two files, that labels are produced correctly

        for file_num, crawl_paths in enumerate([crawl_paths_addressbook, crawl_paths_petclinic, crawl_paths_tmf]):
            with resources.path('test.ui.helper_files', 'ranked_attributes_short.json') as attr_file:
                heuristic_label = HeuristicLabel(str(attr_file))
            heuristic_label.get_element_and_method_labels(crawl_paths)
            for crawl_path in crawl_paths:
                for eventable in crawl_path:
                    [eventable_label, form_field_labels] = heuristic_label.eventable_labels[eventable['id']]
                    assert (eventable_label == correct_labels[file_num][eventable['id']][0])
                    for i, form_field_label in enumerate(form_field_labels):
                        assert (form_field_label == correct_labels[file_num][eventable['id']][1][i])
                    eventable_id_path = '_'.join([
                        str(eventable['id']) for eventable in crawl_path
                    ])
                    assert (heuristic_label.method_labels[eventable_id_path] == correct_method_labels[file_num][eventable_id_path])



if __name__ == '__main__':
    unittest.main()