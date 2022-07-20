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

        correct_labels = [{2: ['On page "Address book", click "post login"',
                               ['On page "Address book", enter "user"', 'On page "Address book", enter "password"']],
                           3: ['On page "Address book", click "next birthdays"',
                               ['On page "Address book", enter "search for any text"',
                                'On page "Address book", select "checkbox"', 'On page "Address book", select "group"',
                                'On page "Address book", select "to group"']],
                           4: ['On page "Address book", click "export"',
                               ['On page "Address book", enter "search for any text"',
                                'On page "Address book", select "checkbox"', 'On page "Address book", select "group"',
                                'On page "Address book", select "to group"']],
                           5: ['On page "Address book ([none])", click "groups"', []],
                           6: ['On page "Address book", click "print all"',
                               ['On page "Address book", enter "search for any text"',
                                'On page "Address book", select "checkbox"', 'On page "Address book", select "group"',
                                'On page "Address book", select "to group"']],
                           7: ['On page "Address book (cEyhmIaV)", click "v8.0.0 - r475"', []],
                           9: ['On page "Address book", click "import"',
                               ['On page "Address book", enter "search for any text"',
                                'On page "Address book", select "checkbox"', 'On page "Address book", select "group"',
                                'On page "Address book", select "to group"']],
                           11: ['On page "Address book ([none])", click "v8.0.0 - r475"', []],
                           12: ['On page "Address book", click "print phones"',
                                ['On page "Address book", enter "search for any text"',
                                 'On page "Address book", select "checkbox"', 'On page "Address book", select "group"',
                                 'On page "Address book", select "to group"']]},
                          {2: ['On page "SpringPetclinicAngular", click "veterinarians"', []],
                           3: ['On page "SpringPetclinicAngular", click "owner add"', []]}]

        correct_method_labels = [
            {'2_3': 'On page "Address book", click "post login"', '4_5': 'On page "Address book", click "export"',
             '6_7': 'On page "Address book", click "print all"', '9_11': 'On page "Address book", click "import"',
             '12': 'On page "Address book", click "print phones"'},
            {'2_3': 'On page "SpringPetclinicAngular", click "veterinarians"'}]

        # check for all DOM fragments in these two files, that labels are produced correctly

        for file_num, crawl_paths in enumerate([crawl_paths_addressbook, crawl_paths_petclinic]):
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