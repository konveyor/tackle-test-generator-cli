import os
import json
import unittest
from importlib import resources
from tkltest.ui.generate.heuristic_labels import HeuristicLabel


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

        correct_labels = [{2: ['on page "Address book", click "post login"',
                               ['on page "Address book", enter "user: admin"',
                                'on page "Address book", enter "password: secret"'], []],
                           3: ['on page "Address book", click "next birthdays"',
                               ['on page "Address book", enter "search for any text: YbtiuWaC"',
                                'on page "Address book", select "checkbox: 1"',
                                'on page "Address book", select "group: [none]"',
                                'on page "Address book", select "to_group: cEyhmIaV"'], ['number of results']],
                           4: ['on page "Address book", click "export"',
                               ['on page "Address book", enter "search for any text: MwVMxMPb"',
                                'on page "Address book", select "checkbox: 1"',
                                'on page "Address book", select "group: [none]"',
                                'on page "Address book", select "to_group: cEyhmIaV"'], ['number of results']],
                           5: ['on page "Address book ([none])", click "groups"', [], []],
                           6: ['on page "Address book", click "print all"',
                               ['on page "Address book", enter "search for any text: WgwwHunL"',
                                'on page "Address book", select "checkbox: 0"',
                                'on page "Address book", select "group: cEyhmIaV"',
                                'on page "Address book", select "to_group: cEyhmIaV"'], ['number of results']],
                           7: ['on page "Address book (cEyhmIaV)", click "v8.0.0 - r475"', [], []],
                           9: ['on page "Address book", click "import"',
                               ['on page "Address book", enter "search for any text: jVMIHazo"',
                                'on page "Address book", select "checkbox: 0"',
                                'on page "Address book", select "group: [none]"',
                                'on page "Address book", select "to_group: cEyhmIaV"'], ['number of results']],
                           11: ['on page "Address book ([none])", click "v8.0.0 - r475"', [], []],
                           12: ['on page "Address book", click "print phones"',
                                ['on page "Address book", enter "search for any text: FCFMqFDb"',
                                 'on page "Address book", select "checkbox: 0"',
                                 'on page "Address book", select "group: cEyhmIaV"',
                                 'on page "Address book", select "to_group: cEyhmIaV"'], ['number of results']]},
                          {2: ['on page "SpringPetclinicAngular", click "veterinarians"', [], []],
                           3: ['on page "SpringPetclinicAngular", click "owner add"', [], []]}]

        correct_method_labels = [
            {
                '2_3': 'This test navigates the path: click "post login".\n\t* Then it enters values in form "number of results" and submits the form.\n\t* The unique clickables in this path are: on page "Address book", click "post login", on page "Address book", click "next birthdays".',
                '4_5': 'This test enters values in form "number of results".\n\t* Then it navigates the path: click "export", click "groups".\n\t* The unique clickables in this path are: on page "Address book", click "export", on page "Address book ([none])", click "groups".',
                '6_7': 'This test enters values in form "number of results".\n\t* Then it navigates the path: click "print all", click "v8.0.0 - r475".\n\t* The unique clickables in this path are: on page "Address book", click "print all", on page "Address book (cEyhmIaV)", click "v8.0.0 - r475".',
                '9_11': 'This test enters values in form "number of results".\n\t* Then it navigates the path: click "import", click "v8.0.0 - r475".\n\t* The unique clickables in this path are: on page "Address book", click "import", on page "Address book ([none])", click "v8.0.0 - r475".',
                '12': 'This test enters values in form "number of results".\n\t* Then it navigates the path: click "print phones".\n\t* The unique clickables in this path are: on page "Address book", click "print phones".'},
            {
                '2_3': 'This test navigates the path: click "veterinarians", click "owner add".\n\t* The unique clickables in this path are: on page "SpringPetclinicAngular", click "veterinarians", on page "SpringPetclinicAngular", click "owner add".'}]

        # check for all DOM fragments in these two files, that labels are produced correctly

        for file_num, crawl_paths in enumerate([crawl_paths_addressbook, crawl_paths_petclinic]):
            with resources.path('test.ui.helper_files', 'ranked_attributes_short.json') as attr_file:
                heuristic_label = HeuristicLabel(str(attr_file))
            heuristic_label.get_element_and_method_labels(crawl_paths)
            for crawl_path in crawl_paths:
                for eventable in crawl_path:
                    [eventable_label, form_field_labels, form_fields] = heuristic_label.eventable_labels[eventable['id']]
                    assert (eventable_label == correct_labels[file_num][eventable['id']][0])
                    for i, form_field_label in enumerate(form_field_labels):
                        assert (form_field_label == correct_labels[file_num][eventable['id']][1][i])
                    for i, form_field in enumerate(form_fields):
                        assert (form_field == correct_labels[file_num][eventable['id']][2][i])
                    eventable_id_path = '_'.join([
                        str(eventable['id']) for eventable in crawl_path
                    ])
                    assert (heuristic_label.method_labels[eventable_id_path] == correct_method_labels[file_num][eventable_id_path])



if __name__ == '__main__':
    unittest.main()