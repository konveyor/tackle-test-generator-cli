FROM maven:3-openjdk-8-slim

# copy from python 3.9 image
COPY --from=python:3.9-slim / /

# install ant
RUN mkdir -p /usr/share/man/man1
RUN apt-get update && apt-get install -y ant wget unzip

# install gradle
ENV GRADLE_HOME /opt/gradle
ENV GRADLE_VERSION 7.4
RUN wget --no-verbose --output-document=gradle.zip https://services.gradle.org/distributions/gradle-${GRADLE_VERSION}-bin.zip \
    && unzip gradle.zip \
    && rm gradle.zip \
    && mv "gradle-${GRADLE_VERSION}" "${GRADLE_HOME}/" \
    && ln --symbolic "${GRADLE_HOME}/bin/gradle" /usr/bin/gradle \
    && gradle --version

# install java lib dependencies
WORKDIR /app/tackle-test-cli
COPY tkltest-lib/*.* ./tkltest-lib/
WORKDIR /app/tackle-test-cli/tkltest-lib
RUN ./download_lib_jars_unit.sh

# copy cli code and install tkltest command
WORKDIR /app/tackle-test-cli
COPY tkltest ./tkltest
COPY setup/tkltestunit.setup.py ./setup.py
COPY MANIFEST.in ./
RUN pip install .

# set entrypoint
ENTRYPOINT ["tkltest-unit"]
