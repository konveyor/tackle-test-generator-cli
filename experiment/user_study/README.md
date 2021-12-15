
# Readability and Usefulness User Study 

This README describes the user study script for generating a user study questionnaire on the readability and usefulness
of Tackle-Test automatically generated unit tests.

## Prerequisites

Python 3

## Usage

The only required input is a directory containing Tackle-Test automatically generated unit tests.

```shell
python3 user_study.py -td path/to/tests_dir
```

The script `user_study.py` performs two main steps. 

First, it samples test files from the given test directory, until reaching a threshold 
number of test methods. The default number of test methods to sample is 20. It can be changed via the 
argument `-n`. **Note:** this step may end up sampling more than the threshold number of methods,
because it samples entire test files. 

The sampled test files are copied to a new folder next to
the given tests directory and with the same directory name with the addition of `_samples` suffix.
The location of the sampled test files is also printed to the standard output.   

Second, it creates an Excel (csv) questionnaire that the user should answer with respect to the
sampled test methods.

The questionnaire has two parts: (1) a table, where each row relates to a specific test method 
(desginated by the test file and test method names), and (2) an optional open questions section, 
where the user can enter free-text input and information in accordance with the questions appearing in it.
 
The structure of the table is as follows: 
1. The first (header) row of the table contains the questions that relate to aspects of readability and usefulness of the test methods
2. The second row of the table contains instructions on the expected values to enter in each cell.
3. Each following row of the table relates to the test method that appears in the first two cells. The 
user should fill information in the next cells according to the header questions.
4. The last column of the table is where the user can optionally enter free-text comments about the row's test method. 

The location of the questionnaire is printed to the standard output. 

