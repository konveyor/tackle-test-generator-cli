# TackleTest: Automated Unit and UI Test Generation

This repository contains a Python-based command-line interface (CLI) to TackleTest, which provides automated
test-generation capabilities for Java unit testing and end-to-end testing of web applications. TackleTest-Unit
(supported by the CLI command `tkltest-unit`) automatically generates unit-level test cases for Java applications.
TackleTest-UI (supported by the CLI command `tkltest-ui`), automatically generates end-to-end test cases for
web applications that exercise the application under test via its user interface.

These CLI commands invoke code test generators, whose code base resides in the related
[tackle-test-generator-core](https://github.com/konveyor/tackle-test-generator-core) repository.

1. [Overview](#overview)
2. [Installing and running the CLI](#installing-and-running-tackletest-cli)
3. [TackleTest CLI in action on sample apps](#tackletest-cli-in-action-on-sample-apps)
4. TackleTest-Unit documentation: [quick-start guide](doc/unit/quick_start_guide.md), [detailed user guide](doc/unit/user_guide.md)
5. TackleTest-UI documentation: [quick-start guide](doc/ui/quick_start_guide.md), [detailed user guide](doc/ui/quick_start_guide.md)
6. [Known tool issues](#known-tool-issues)
7. [Additional resources (demos, presentations, blog)](#additional-resources)

## Overview

**TackleTest-Unit** automatically generates unit test cases for Java applications. Each generated test
case focuses on exercising the visible methods of an application class. Test generation can
be done on a specific set of application classes or the entire application.

Among its many features, TackleTest-Unit:

- Supports automatic generation of test assertions: tests can be generated with or without assertions
- Combines different coverage metrics and generates coverage reports: the supported coverage metrics include conventional code coverage criteria, such as statement and branch coverage, as well as a novel type-based combinatorial coverage coverage of Java methods
- Accommodates developer-written unit test suites: it can perform coverage-driven augmentation and directed test generation to enhance the coverage achieved by a given test suite
- Supports multiple build tools: Maven, Gradle, and Ant
- Integrates two opens-source test-generation tools: [EvoSuite](https://www.evosuite.org/) and [Randoop](https://randoop.github.io/randoop/)

For more details of TackleTest-Unit, see the [quick-start guide](doc/unit/quick_start_guide.md) and the
[detailed user guide](doc/unit/user_guide.md).

**TackleTest-UI** supports generation of end-to-end test cases for web applications. These test cases
exercise the application under test via its user interface and drive execution through different application
tiers. Some of the features of TackleTest-UI include

- Model-based generation of test cases for web applications: infers state-transition model of the webapp under test and generated test cases from paths in the model 
- Integration with the state-of-the-art web-crawling tool [Crawljax](https://github.com/crawljax/crawljax)
- Generation two test suites using the TestNG framework: one using Crawljax API and the other using Selenium API

For more details of TackleTest-UI, see the [quick-start guide](doc/ui/quick_start_guide.md) and the
[detailed user guide](doc/ui/user_guide.md).

## Installing and running TackleTest CLI

The CLI command can be installed locally to be run, or it can be run in a Docker container, in which case
the various dependencies (e.g., Python, Java, Maven) need not be installed.

### Using a published Docker image

The simplest way is to run the CLI is using a [published Docker image](https://github.com/konveyor/tackle-test-generator-cli/pkgs/container/tackle-test-generator-cli). For each released version of TackleTest, the docker image (tagged with the version number) is published on the GitHub Container Registry.

After pulling the image, set up the following alias commands for convenience:

```buildoutcfg
alias tkltest-unit='docker run --rm -v /path-to-the-cli-directory:/app/tackle-test-cli ghcr.io/konveyor/tackle-test-generator-cli:latest tkltest-unit'
```
```buildoutcfg
alias tkltest-ui='docker run --rm -v /path-to-the-cli-directory:/app/tackle-test-cli ghcr.io/konveyor/tackle-test-generator-cli:latest tkltest-ui'
```

substituting `/path-to-the-cli-directory` with the path to be mounted onto the container---this would be the path
to the CLI directory or the directory where the app under test would be set up for test generation.
Also, if you are using an image tagged other than `latest`, substiute the tag for `latest` in the alias command.

The results of test generation or execution in the container are available in `/path-to-the-cli-directory` on the
host machine. This also requires that the classes, the library dependencies, and the configuration file for the app
under test be placed in a directory
under the CLI directory, so that they are available in the container.

### Installing a released version with all dependencies included

- Download and unzip [a released version of TackleTest](https://github.com/konveyor/tackle-test-generator-cli/releases)
with all dependencies included (i.e., a release archive file named `*-all-deps.tgz` or `*-all-deps.zip`).

- Install TackleTest CLI by following the [command installation instructions](doc/installation.md#installing-the-tackletest-cli).

### Installing from a repo snapshot

- Clone the repo or download codebase zip

- Download all library dependencies by following the [library download instructions](./doc/installation.md#downloading-library-dependencies).

- Install TackleTest CLI by following the [command installation instructions](doc/installation.md#installing-the-tackletest-cli).

After the CLI has been installed, try out the `tkltest-unit` and `tkltest-ui` commands:
```buildoutcfg
tkltest-unit --help
tkltest-ui --help
```

> Notes on upgrading an installation:
> - If you download a newer TackleTest release, follow the [command installation instructions](doc/installation.md#installing-the-tackletest-cli) and check the TackleTest version by running the command `tkltest-unit -v` or `tkltest-ui -v`.
> - To pull in updated [published library dependencies](https://github.com/orgs/konveyor/packages?repo_name=tackle-test-generator-core) for the [TackleTest Core](https://github.com/konveyor/tackle-test-generator-core) components, please make sure to delete the corresponding jar files from `lib/download` as well as from your local Maven repository (`~/.m2/repository`) before [running the download script](./doc/installation.md#downloading-java-library-dependencies).

## TackleTest CLI in action on sample apps

To see the TackleTest-Unit in action on a sample Java application, set `JAVA_HOME` to your JDK installation
and run the command:
```buildoutcfg
tkltest-unit --config-file ./test/data/irs/tkltest_config.toml --verbose generate ctd-amplified
```

This command will take a few minutes to complete. The `--verbose` option allows to view its progress in the standard output. 
Note that during test sequence initialization phase, the output is not printed to the standard output but rather to log files.
You can open those log (via `tail -f <logfile>`) to view the progress during this phase.

To see the TackleTest-UI in action on a sample web application, follow these steps:

1. Deploy sample web application in Docker:
   ```buildoutcfg
   cd test/ui/data/webapps/petclinic && ./deploy_app.sh start && cd ../../../../../
   ```
   The deployed web app `petclinic` can be opened at `http://localhost:8080/`

2. Run UI test generation on the app:
   ```buildoutcfg
   tkltest-ui --config-file ./test/ui/data/petclinic/tkltest_ui_config.toml --verbose generate
   ```

This command will take a few minutes to run; during its execution, the Chrome browser will be started in headless
mode to launch the petclinic web for crawling and test generation.

## Known Tool Issues

### TackleTest-Unit

1. On apps with native UI (e.g., swing), the tool can sometimes get stuck during sequence execution
   (even though it uses a Java agent for replacing calls to GUI components); as a workaround,
   users can exclude UI-related classes from the set of test targets.

2. Coverage in JEE apps could be low because of limited JEE mocking support.

3. A known issue on Windows OS is that Tackle-test might exceed Windows limit of 260 characters for a file system folder path name length. 
 Tackle-test mimics the structure of the application under its output directory, to enable generating 
tests in the same package as the class under test, and gaining access to all its non-private members and methods. 
If your application has a deep package hierarchy, these paths might exceed the 260 characters length limit. For Windows 10,
there are online instructions available on how to enable long paths and avoid this limitation.

### TackleTest-UI

1. TBD


## Additional Resources

### TackleTest-Unit resources
- [Article on TackleTest](https://opensource.com/article/21/8/tackle-test)
- [Short demo of TackleTest](https://www.youtube.com/watch?v=wpgmB_xvZaQ) (5 min)
- [Detailed TackleTest presentation and demo from Konveyor meetup](https://www.youtube.com/watch?v=qThqTFh2PM4&t) (48 min)
- [Slide deck on TackleTest](https://www.slideshare.net/KonveyorIO/tackletest-an-automatic-unitlevel-test-case-generator)
