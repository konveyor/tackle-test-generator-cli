---
name: Bug Report
about: Create a report to help us improve Tackle-Test
title: ''
labels: ''
assignees: ''

---

**Describe the bug**

A clear and concise description of what the failure is, including information about
the complete CLI command used (`generate` or `execute` command, with sub-command and options)
and the error message. 

**Attachments**

To help us diagnose the failure, please add these attachments to the issue, as appropriate:

- The toml file containing the configuration information used
- The log file created by running the CLI with the `--verbose` and `--log-level INFO` options
- If possible, a zip file containing the application classes, and the file specified as
  `app_classpath_file` option in the toml file together with the library dependencies listed
  in the file
- If the failure occurred for the `generate` command, please attach the following files if
  they were created during the failing run:
    - The CTD test plan JSON file (`<app-name>_ctd_models_and_test_plans.json`)
    - The building-block test sequences JSON files (`<app-name>_EvoSuiteTestGenerator_bb_test_sequences.json`
      and/or `<app-name>_RandoopTestGenerator_bb_test_sequences.json`)
- If the failure occurred for the `execute` command, please attach the zip file of the directory
  containing the generated test JUnit test cases

**Environment information**

 - CLI version
 - OS: [e.g., Linux, macOS, Windows]

**Screenshots**

If applicable, add screenshots to help explain your problem.

**Additional context**

Add any other context about the problem here.
