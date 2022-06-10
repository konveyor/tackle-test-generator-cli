import os
import json
import unittest
from importlib import resources
from tkltest.generate.ui.heuristic_labels import HeuristicLabel

class HeuristicLabelTest(unittest.TestCase):
    helper_file_dir = os.path.join('test', 'ui', 'helper_files')

    def test_heuristic_labels(self):

        with open(os.path.join(self.helper_file_dir, 'ids_addressbook.json')) as file1:
            ids_addressbook = json.load(file1)
        with open(os.path.join(self.helper_file_dir, 'ids_petclinic.json')) as file2:
            ids_petclinic = json.load(file2)
        with open(os.path.join(self.helper_file_dir, 'eventables_addressbook.json')) as file1:
            eventables_addressbook = json.load(file1)
        with open(os.path.join(self.helper_file_dir, 'eventables_petclinic.json')) as file2:
            eventables_petclinic = json.load(file2)
        with resources.path('tkltest.generate.ui', 'ranked_attributes.json') as attr_file:
            heuristic_label = HeuristicLabel(str(attr_file))
        correct_labels = [
            {2: 'login', 3: 'next birthday', 4: 'export', 5: 'group', 6: 'print', 7: 'note', 9: 'import', 11: 'note',
             12: 'print phone', 13: 'add new', 14: 'next', 16: 'add', 17: 'note', 21: 'note', 22: 'delete',
             23: 'telephone', 24: 'document', 25: 'last name', 26: 'mail'},
            {2: 'veterinarian', 3: 'brand', 4: 'add new', 9: 'save vet', 10: 'specialty', 11: 'owner', 12: 'pet type',
             13: 'specialty', 14: 'edit', 15: 'edit', 16: 'brand', 22: 'vet', 23: 'back'}]
        ids = [ids_addressbook, ids_petclinic]
        for file_num, eventable_elements in enumerate([eventables_addressbook, eventables_petclinic]):
            id_index = 0
            for eventable_element in eventable_elements:
                assert (heuristic_label.get_label(eventable_element) == correct_labels[file_num][ids[file_num][id_index]])
                id_index += 1


if __name__ == '__main__':
    unittest.main()






    