# TackleTest Installation Guide

1. [Downloading Java library Dependencies](#downloading-java-library-dependencies)
2. [Installing Prerequisites](#installing-prerequisites)
   1. [Prerequisites for TackleTest-Unit](#prerequisites-for-tackletest-unit)
   2. [Prerequisites for TackleTest-UI](#prerequisites-for-tackletest-ui)
3. [Installing the TackleTest CLI](#installing-the-tackletest-cli)
4. [Building the Docker image and running the CLI via docker or docker-compose](#building-the-docker-image-and-running-the-cli-via-docker-or-docker-compose)

## Downloading Java library Dependencies

To run TackleTest CLI from a locally built Docker image or a local installation, a few jar files need to be downloaded
from Maven repositories. This can be done by running the script `tkltest-lib/download_lib_jars.sh`

```buildoutcfg
cd tkltest-lib; ./download_lib_jars.sh
```
Windows users should run:
    
```buildoutcfg
cd tkltest-lib; download_lib_jars.sh
```
   
This downloads the Java libraries required by the CLI into the `tkltest-lib/` directory, including the updated versions of `tackle-test-generator-core` jars.

TackleTest-Unit performs CTD modeling and test-plan generation using the [NIST Automated Combinatorial Testing for Software](https://csrc.nist.gov/projects/automated-combinatorial-testing-for-software) tool, which is packaged with the CLI (in the `tkltest-lib` directory).

## Installing Prerequisites

### Prerequisites for TackleTest-Unit

1. Install Python 3.9

2. Install a JDK (versions 8-11 are supported). The JDK home directory has to be specified as a configuration option;
   see [tkltest-unit Configuration Options](unit/tkltest_unit_config_options.md) for details on
   configuration option.
   
3. Install one or more of the required build systems depending on the TackleTest features used: Ant, Maven, Gradle. Of these systems, Maven is required for installing the CLI; the others are optional and are required only if the respective tool features are used. TackleTest-Unit uses these build systems in two ways:

   - To run the generated tests: Along with generating JUnit test cases, the CLI generates an Ant `build.xml`, a Maven `pom.xml` or a Gradle `build.gradle`, which can be used for building and running the generated tests. Note that for using Gradle to build and run the generated tests, we require Gradle version 7.0 or higher. The build system to use can be configured using the `execute` command option `-bt/--build-type` (see [tkltest-unit Configuration Options](unit/tkltest_unit_config_options.md)). Install the build system that you prefer for running the tests.
   
   - To collect library dependencies of the application under test (AUT): The CLI can use the AUT's build file to collect the AUT's library dependencies automatically. This feature is supported for Gradle, Ant and Maven. Alternatively, the user has to specify the dependencies manually in a text file (see [Specifying the app under test](unit/user_guide.md#specifying-the-app-under-test)). If you plan to use the dependency computation feature with a Gradle or Ant build file, install Gradle or Ant respectively.

   > **NOTE:** For Ant, please make sure to install the optional JUnit task as well. On Linux, for example, this can be done via the package manager.
   > 
   > For Debian-based distributions:
   > ```commandline
   > sudo apt-get install ant-optional
   > ```
   >
   > For Fedora-based distributions:
   > ```commandline
   > sudo dnf install ant-junit
   > ```

### Prerequisites for TackleTest-UI

1. Install Python 3.9

2. Install JDK 8

3. Install the Chrome browser. Currently, TackleTest-UI supports UI test generation on the Chrome browser only; support
   for other browsers will be added in the future.

4. Install Maven; along with generating UI test cases (in Java), TackleTest-UI creates Maven pom.xml, which can be used for
   building and running the generated tests.

## Installing the TackleTest CLI

To install the CLI in a virtual enviornment (this would be the preferred installation mode to keep the TackleTest
installation isolated and avoid version conflicts), run the commands:

```buildoutcfg
python3 -m venv venv
source venv/bin/activate
pip install --editable .
```

Windows users should run:

```buildoutcfg
python3 -m venv venv
venv\Scripts\activate.bat
pip install --editable .
```

If you prefer to install the command globally, run the command

```buildoutcfg
pip install --editable .
```

After installation, TackleTest commands `tkltest-unit` and `tkltest-ui` will be available for use. Try them out:

```buildoutcfg
tkltest-unit --help
tkltest-ui --help
```

> Note: The editable mode (`pip install --editable`) allows to continue development and make changes and simply run the command without having to package
and re-install it.

## Building the Docker image and running the CLI via docker or docker-compose

For each released version of TackleTest, the docker image (tagged with the version number) is published on the GitHub Container Registry. These images can be pulled and used without requiring any set up. For the available images and instructions on using them, please visit the [TackleTest container images](https://github.com/konveyor/tackle-test-generator-cli/pkgs/container/tackle-test-generator-cli) page. To the build the TackleTest container locally using the latest (or a particular) code version, please go through the following instructions.

To run the CLI using `docker-compose` (to print the CLI `help` message), run one of the following commands in the CLI directory,
which builds the docker image for the CLI (called `tkltest-cli`) and then runs the CLI command; the docker
container is removed upon completion of the CLI command.

```buildoutcfg
docker-compose run --rm tkltest-cli tkltest-unit --help
docker-compose run --rm tkltest-cli tkltest-ui --help
```

Alternatively, to build and run the CLI using `docker` instead of `docker-compose`, run these commands in the CLI:

```buildoutcfg
docker build --tag tkltest-cli .
```

Then, assuming `/home/user/tkltest-workspace` is the host directory to be mounted on to the container, the following
commands can be used:
```buildoutcfg
docker run --rm -v /home/user/tkltest-workspace:/app/tackle-test-cli tkltest-cli tkltest-unit --help
docker run --rm -v /home/user/tkltest-workspace:/app/tackle-test-cli tkltest-cli tkltest-ui --help
```

The results of test generation or  execution in the container are available under the `/home/user/tkltest-workspace`
directory on the host machine (`/app/tackle-test-cli` in the container).
For TackleTest-Unit, in addition to the AUT configuration file, the AUT classes and library dependencies must also be placed
in a directory under `/home/user/tkltest-workspace`, so that they are available in the container.

For convenience in running the CLI via `docker` or `docker-compose`, you can create aliases, such as
one of the following.

For TackleTest-Unit:
```buildoutcfg
alias tkltest-unit='docker run --rm -v /home/user/tkltest-workspace:/app/tackle-test-cli tkltest-cli tkltest-unit'
```
```buildoutcfg
alias tkltest-unit='docker-compose run --rm tkltest-cli tkltest-unit'
```

For TackleTest-UI:
```buildoutcfg
alias tkltest-ui='docker run --rm -v /home/user/tkltest-workspace:/app/tackle-test-cli tkltest-cli tkltest-ui'
```
```buildoutcfg
alias tkltest-ui='docker-compose run --rm tkltest-cli tkltest-ui'
```

Note that for using the CLI via `docker-compose`, `docker-compose.yml` has to be checkout out (it might be better to clone the repo) and
that [the current directory is mounted onto the container](https://github.com/konveyor/tackle-test-generator-cli/blob/main/docker-compose.yml#L12). 
