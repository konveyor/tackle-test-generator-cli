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

        with resources.path('tkltest.generate.ui', 'ranked_attributes_short.json') as attr_file:
            heuristic_label = HeuristicLabel(str(attr_file))

        correct_labels = [
            {2: 'click post login', 3: 'click birthday', 4: 'click export', 5: 'click group', 6: 'click view',
             7: 'click note', 9: 'click import', 11: 'click note', 12: 'click view', 13: 'click edit', 14: 'click next',
             16: 'add', 17: 'click note', 21: 'click note', 22: 'click trigger sort', 23: 'click sort telephone',
             24: 'click trigger sort', 25: 'click sort trigger', 26: 'click sort mail'},
            {2: 'click veterinarian true', 3: 'owner add', 4: 'add', 9: 'click vet default', 10: 'click specialty',
             11: 'click owner true', 12: 'click type active', 13: 'click specialty', 14: 'click default delete',
             15: 'click default delete', 16: 'owner add', 22: 'click vet', 23: 'save vet'}]
        # check for all DOM fragments in these two files, that labels are produced correctly
        for file_num, crawl_paths in enumerate([crawl_paths_addressbook, crawl_paths_petclinic]):
            for crawl_path in crawl_paths:
                for eventable in crawl_path:
                    # print('\n\n\n', eventable['element'])
                    assert (heuristic_label.get_label(eventable) == correct_labels[file_num][eventable['id']])


if __name__ == '__main__':
    unittest.main()






