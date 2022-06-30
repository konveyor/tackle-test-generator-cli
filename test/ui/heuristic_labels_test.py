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



        correct_labels = [{2: ['click post login', ['enter user', 'enter password']], 3: ['click birthday',
                                                                                          ['search for any text',
                                                                                           'enter checkbox',
                                                                                           'enter group',
                                                                                           'enter to group']],
                           4: ['click export',
                               ['search for any text', 'enter checkbox', 'enter group', 'enter to group']],
                           5: ['click group', []], 6: ['click view',
                                                       ['search for any text', 'enter checkbox', 'enter group',
                                                        'enter to group']], 7: ['click note', []], 9: ['click import',
                                                                                                       [
                                                                                                           'search for any text',
                                                                                                           'enter checkbox',
                                                                                                           'enter group',
                                                                                                           'enter to group']],
                           11: ['click note', []],
                           12: ['click view',
                                ['search for any text', 'enter checkbox', 'enter group', 'enter to group']]},
                          {2: ['click veterinarian true', []], 3: ['owner add', []]}]

        # check for all DOM fragments in these two files, that labels are produced correctly

        for file_num, crawl_paths in enumerate([crawl_paths_addressbook, crawl_paths_petclinic]):
            with resources.path('test.ui.helper_files', 'ranked_attributes_short.json') as attr_file:
                heuristic_label = HeuristicLabel(str(attr_file))
            heuristic_label.get_element_and_method_labels(crawl_paths)
            for crawl_path in crawl_paths:
                for eventable in crawl_path:
                    [eventable_label, form_field_labels] = heuristic_label.get_label(eventable)
                    print('\n\n\n',[eventable_label, form_field_labels])
                    print('correct label:')
                    print(correct_labels[file_num][eventable['id']][0])
                    print(correct_labels[file_num][eventable['id']][1])
                    assert (eventable_label == correct_labels[file_num][eventable['id']][0])
                    for i, form_field_label in enumerate(form_field_labels):
                        assert (form_field_label == correct_labels[file_num][eventable['id']][1][i])
                    # assert (heuristic_label.method_labels[])



if __name__ == '__main__':
    unittest.main()