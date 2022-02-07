import sys
import os
import shutil
import random
import csv
import argparse


def generate_study(tests_dir, num_samples, num_samples_per_class):

    """Creates a questionnaire for readbility and usefulness of automatically generated test cases. In the first
    step, select a sample of the given test cases. In the second step, create an Excel file for answering
    questions about the readability and usefulness of the test cases.

        Args:
            tests_dir (str): Path to the directory containing the automatically generated test cases
            num_samples (int): Number of test methods to sample. Default is 20.
            num_samples_per_class (int): Number of test methods to sample per test class. Default is 4.
        """

    tests_dir = os.path.abspath(tests_dir)
    files_to_methods, sampled_tests_dir = __select_samples(tests_dir, num_samples, num_samples_per_class)
    __create_questionnaire(files_to_methods, sampled_tests_dir)

def __select_samples(tests_dir, num_samples, num_samples_per_class):
    test_files = [os.path.abspath(os.path.join(root, name)) for root, dirs, files in os.walk(tests_dir)
                   for name in files if name.endswith('.java')]

    num_sampled = 0

    files_to_methods = dict()

    sampled_tests_dir = os.path.abspath(os.path.join(os.path.dirname(tests_dir), os.path.basename(tests_dir)+"_sampled"))
    os.makedirs(sampled_tests_dir, exist_ok=True)

    while num_sampled < num_samples and test_files:
        current_file = random.choice(test_files)
        test_files.remove(current_file)
        dest_fpath = current_file.replace(os.path.basename(tests_dir), os.path.basename(sampled_tests_dir))
        os.makedirs(os.path.dirname(dest_fpath), exist_ok=True)
        shutil.copy(current_file, dest_fpath)
        remain_to_sample = num_samples-num_sampled
        if remain_to_sample < num_samples_per_class:
            num_samples_per_class = remain_to_sample
        methods = __get_method_names(current_file, num_samples_per_class)
        num_sampled += len(methods)
        files_to_methods[current_file] = methods

    print("sampled "+str(num_sampled) + " test methods, located in "+os.path.abspath(sampled_tests_dir))

    return files_to_methods, sampled_tests_dir

def __get_method_names(filename, num_samples_per_class):

    next_is_test = False

    method_prefix = "public void "

    method_names = []
    with open(filename) as file:
        for line in file:
            if '@Test' in line:
                next_is_test = True
            elif next_is_test:
                next_is_test = False				
                method_names.append(line[line.find(method_prefix)+len(method_prefix):line.index('(')])
    if len(method_names) <= num_samples_per_class:
        return method_names

    chosen_methods = []

    for i in range(num_samples_per_class):
        current_method = random.choice(method_names)
        method_names.remove(current_method)
        chosen_methods.append(current_method)

    return chosen_methods

def __create_questionnaire(files_to_methods, sampled_tests_dir):

    study_file = os.path.join(sampled_tests_dir, "user_study_questionnaire.csv")

    with open( study_file, "w") as csv_file:
        writer = csv.writer(csv_file, delimiter=',')
        # write header row
        writer.writerow(['Test File', 'Test Method',
                        'I understand what this test case is doing',
                        'I understand what the assertions in this test case are checking',
                        'I can describe the purpose of this test case',
                        'This test case performs meaningful actions',
                        'The assert statements in this test case are useful',
                        'This test case adds value on top of the other test methods in this file',
                        'I would add this test case to my unit test bucket',
                        'General comments on this test method (if any)'])
        writer.writerow(['', '',
                        'Choose one option: strongly agree, agree, neither, disagree, strongly disagree',
                        'Choose one option: strongly agree, agree, neither, disagree, strongly disagree',
                        'Choose one option: strongly agree, agree, neither, disagree, strongly disagree',
                        'Choose number: 1 (highest) to 5 (lowest)',
                        'Choose number: 1 (highest) to 5 (lowest)',
                        'Choose one option: strongly agree, agree, neither, disagree, strongly disagree',
                        'Choose one option: strongly agree, agree, neither, disagree, strongly disagree',
                        ''])
        for test_file in files_to_methods.keys():
            for test_method in files_to_methods[test_file]:
                line = [os.path.basename(test_file), test_method]
                writer.writerow(line)
        writer.writerow(['Optional Open Questions'])
        writer.writerow(['List main factors for tests to be unreadable',
                        'List main factors for tests to be unuseful',
                        'List suggestions to improve tests readability',
                        'List suggestions to improve tests usefulness',
                        'Your years of experience in software engineering',
                        'Your level of expertise in this application (1 (highest) to 5 (lowest))'])

    print("Created questionnaire for sampled test methods, located in " + os.path.abspath(study_file))

def main():

    parser = argparse.ArgumentParser(description='Tackle-Test Readability and Usefulness User Study',
                                     formatter_class=argparse.ArgumentDefaultsHelpFormatter)

    parser.add_argument('-td', '--tests-dir', type=str, required=True, dest='tests_dir', help='Path to tests directory')
    parser.add_argument('-n', '--num-samples', type=int, dest='num_samples', default=20,
                        help='Number of test methods to sample')
    parser.add_argument('-nc', '--num-per-class', type=int, dest='num_samples_per_class', default=4,
                        help='Number of test methods to sample per test class')

    # if no args specified, print help message and exit

    if len(sys.argv) == 1:
        parser.print_help()
        sys.exit(1)

    args = parser.parse_args()

    generate_study(args.tests_dir, args.num_samples, args.num_samples_per_class)

if __name__ == '__main__':  # pragma: no cover
    main()