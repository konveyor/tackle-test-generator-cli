# TackleTest: Automated Unit and UI Test Generation

This repository contains a Python-based command-line interface (CLI) to TackleTest, which provides automated
test-generation capabilities for Java unit testing and end-to-end testing of web applications. TackleTest-Unit
(supported by the CLI command `tkltest-unit`) automatically generates unit-level test cases for Java applications.
TackleTest-UI (supported by the CLI command `tkltest-ui`), automatically generates end-to-end test cases for
web applications that exercise the application under test via its user interface.

These CLI commands invoke core test generators, whose codebase resides in the related
[tackle-test-generator-core](https://github.com/konveyor/tackle-test-generator-core) repository.

1. [Overview](#overview)
2. [Installing and running the CLI](#installing-and-running-tackletest-cli)
3. [TackleTest CLI in action on sample apps](#tackletest-cli-in-action-on-sample-apps)
4. TackleTest-Unit documentation: [quick-start guide](doc/unit/quick_start_guide.md), [detailed user guide](doc/unit/user_guide.md), [configuration options](doc/unit/tkltest_unit_config_options.md)
5. TackleTest-UI documentation: [quick-start guide](doc/ui/quick_start_guide.md), [detailed user guide](doc/ui/user_guide.md), [configuration options](doc/ui/tkltest_ui_config_options.md)
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

**TackleTest-UI** supports automatic generation of end-to-end test cases for web applications. These test cases
exercise the application under test via its user interface and drive execution through different application
tiers. Some of the features of TackleTest-UI include

- Model-based generation of test cases for web applications: infers state-transition model of the webapp under test and generates test cases from paths in the model 
- Integration with the state-of-the-art web-crawling tool [Crawljax](https://github.com/crawljax/crawljax)
- Generation two test suites using the TestNG framework: one using Crawljax API and the other using Selenium API

For more details of TackleTest-UI, see the [quick-start guide](doc/ui/quick_start_guide.md) and the
[detailed user guide](doc/ui/user_guide.md).

## Installing and running TackleTest CLI

The CLI command can be installed locally to be run, or it can be run in a Docker container, in which case
the various dependencies (e.g., Python, Java, Maven) need not be installed.

### Using a published Docker image

The simplest way is to run the CLI using a [published Docker image](https://github.com/orgs/konveyor/packages?repo_name=tackle-test-generator-cli). For each released version of TackleTest, the docker image (tagged with the version number) is published on the GitHub Container Registry. In addition to an image that supports both unit and UI testing, separate images that support respectively unit and UI testing only are also published. The commands below are illustrated for the consolidated image that supports both levels of testing.

To run TackleTest from docker, the directory containing the TackleTest configuration file for the application under
test (AUT)---along with the AUT classes and library dependencies, in the case of TackleTest-Unit---has to be mounted
onto the container. Suppose that the directory containing the AUT configuration file is `/home/user/aut`. To mount this
directory and run TackleTest with the AUT configuration file, use the following commands (for unit and UI test generation,
respectively) after pulling the image from the registry:

```buildoutcfg
docker run --rm -v /home/user/aut:/app/tackle-test-cli ghcr.io/konveyor/tackle-test-generator-cli:latest tkltest-unit --config-file /app/tackle-test-cli/tkltest_config.toml --verbose generate ctd-mplified'
```

```buildoutcfg
docker run --rm -v /home/user/aut:/app/tackle-test-cli ghcr.io/konveyor/tackle-test-generator-cli:latest tkltest-ui --config-file /app/tackle-test-cli/tkltest_config.toml --verbose generate'
```

Note that, in these commands, the configuration file is specified as an absolute path in the container
(`/app/tackle-test-cli/tkltest_config.toml`).  You can also specify a relative path, provided that path is
relative to `/app/tackle-test-cli` in the container.

The results of test generation or execution in the container are available under `/home/user/aut` on the
host machine (`/app/tackle-test-cli` in the container).

If you are using a unit/UI-testing-specific image, exclude `tkltest-unit` and `tkltest-ui` from the commands above as those images support only the `tkltest-unit` and `tkltest-ui` command, respectively.

[//]: # (&#40;This also requires that the classes, the library dependencies, and the configuration file for the app&#41; under test be placed in a directory  under the CLI directory, so that they are available in the container.)

For convenience in using the TackleTest via Docker, you can create a workspace directory for TackleTest (e.g.,
`/home/user/tkltest-workspace`) and create the following aliases:

```buildoutcfg
alias tkltest-unit='docker run --rm -v /home/user/tkltest-workspace:/app/tackle-test-cli ghcr.io/konveyor/tackle-test-generator-cli:latest tkltest-unit'
```
```buildoutcfg
alias tkltest-ui='docker run --rm -v /home/user/tkltest-workspace:/app/tackle-test-cli ghcr.io/konveyor/tackle-test-generator-cli:latest tkltest-ui'
```

With these aliases set, you can simply use `tkltest-unit` and `tkltest-ui` commands instead of the long `docker run ...` command.
If you have the TackleTest CLI repo cloned, you could use the CLI repo as the workspace directory.
If you are using a TackleTest image tagged other than `latest`, remember to substitute `latest` with the specific version tag
in the docker commands.

[//]: # (substituting `/path-to-the-cli-directory` with the path to be mounted onto the container---this would be the path to the CLI directory or the directory where the app under test would be set up for test generation.)

>Note: For running `tkltest-unit` via Docker, the option `java_jdk_home` (see [tkltest-unit configuration options](./doc/unit/tkltest_unit_config_options.md)) should be left empty because the environment variable JAVA_HOME is set in the container envrionment. Providing a value for `java_jdk_home` will override the JAVA_HOME setting in the container, resulting in failure.

### Installing a released version with all dependencies included

- Download and unzip [a released version of TackleTest](https://github.com/konveyor/tackle-test-generator-cli/releases)
with all dependencies included (i.e., a release archive file named `*-all-deps.tgz` or `*-all-deps.zip`)

- Install TackleTest CLI by following the [command installation instructions](doc/installation.md#installing-the-tackletest-cli)

### Installing from a repo snapshot

- Clone the repo or download codebase zip

- Download all library dependencies by following the [library download instructions](./doc/installation.md#downloading-library-dependencies)

- Install TackleTest CLI by following the [command installation instructions](doc/installation.md#installing-the-tackletest-cli)

After the CLI has been installed, try out the `tkltest-unit` and `tkltest-ui` commands:
```buildoutcfg
tkltest-unit --help
tkltest-ui --help
```

> Notes on upgrading an installation:
> - If you download a newer TackleTest release, follow the [command installation instructions](doc/installation.md#installing-the-tackletest-cli) and check the TackleTest version by running the command `tkltest-unit -v` or `tkltest-ui -v`.
> - If you upgrade the repo snapshot, please be sure to [rerun the download script](./doc/installation.md#downloading-java-library-dependencies) to pull in the updated [published library dependencies](https://github.com/orgs/konveyor/packages?repo_name=tackle-test-generator-core) for the [TackleTest Core](https://github.com/konveyor/tackle-test-generator-core) components.

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

1. Make sure that the sample web applications, which are included as Git submodules, are checked out:
   ```buildoutcfg
   git submodule init
   git submodule update
   ```
   Alternatively, you can get the sample web applications from [this repo](https://github.com/sinha108/tkltest-ui-webapps).

2. Deploy one of the sample web applications, `addressbook` or `petclinic`, in Docker:
   ```buildoutcfg
   cd test/ui/data/webapps/addressbook && ./deploy_app.sh start && cd ../../../../../
   ```
   The `addressbook` webapp can be opened at `http://localhost:3000/addressbook/`
   ```buildoutcfg
   cd test/ui/data/webapps/petclinic && ./deploy_app.sh start --build && cd ../../../../../
   ```
   The `petclinic` webapp can be opened at `http://localhost:8080/`

3. Run UI test generation on the app:
   ```buildoutcfg
   tkltest-ui --config-file ./test/ui/data/addressbook/tkltest_ui_config.toml --verbose generate
   ```
   or
   ```buildoutcfg
   tkltest-ui --config-file ./test/ui/data/petclinic/tkltest_ui_config.toml --verbose generate
   ```

This command will take a few minutes to run; during its execution, the Chrome browser will be started in headless
mode to launch the petclinic webapp for crawling and test generation. The  `--verbose` option redirects crawl
logs to a file called `<app-nane>_crawljax_runner.log`. You can view the progress of crawling via the
command `tail -f <logfile>`.

## Known Tool Issues

### TackleTest-Unit

1. On apps with native UI (e.g., swing), the tool can sometimes get stuck during sequence execution
   (even though it uses a Java agent for replacing calls to GUI components); as a workaround,
   users can exclude UI-related classes from the set of test targets.

2. Coverage in JEE apps could be low because of limited JEE mocking support.

3. A known issue on Windows OS is that TackleTest might exceed Windows limit of 260 characters for a file system folder path name length. 
 Tackle-test mimics the structure of the application under its output directory, to enable generating 
tests in the same package as the class under test, and gaining access to all its non-private members and methods. 
If your application has a deep package hierarchy, these paths might exceed the 260 characters length limit. For Windows 10,
there are online instructions available on how to enable long paths and avoid this limitation.

### TackleTest-UI

1. The form-fill actions performed by Crawljax have some limitations. Crawljax does not have information about disabled form fields and attempts to perform actions on them (e.g., via Selenium `sendKeys` method) without encountering exceptions. This can result in such actions occuring also in the generated test cases, but these tests would still pass (the action on a disabled field essentially becomes a NOP). Crawljax also does not perform any verification after performing the form-fill actions or analysis to associate form fields with submit buttons (other than the association provided in [form data specification](./doc/ui/tkltest_ui_config_options.md#form-data-specification)). Aside from the specified forms, Crawljax simply performs all form-fill actions available on a web page before performing any clicks. 

2. Currently, support is available for only the Chrome browser (in normal and "headless" modes); support for Firefox will 
be added soon.


## Additional Resources

### TackleTest-Unit resources
- [Article on TackleTest](https://opensource.com/article/21/8/tackle-test)
- [Short demo of TackleTest](https://www.youtube.com/watch?v=wpgmB_xvZaQ) (5 min)
- [Detailed TackleTest presentation and demo from Konveyor meetup](https://www.youtube.com/watch?v=qThqTFh2PM4&t) (48 min)
- [Slide deck on TackleTest](https://www.slideshare.net/KonveyorIO/tackletest-an-automatic-unitlevel-test-case-generator)

## Code of Conduct
Refer to Konveyor's Code of Conduct [here](https://github.com/konveyor/community/blob/main/CODE_OF_CONDUCT.md).
