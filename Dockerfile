FROM maven:3-openjdk-8-slim

# copy from python 3.8 image
COPY --from=python:3.8-slim / /

# GitHub username and personal access token
ARG GITHUB_USERNAME
ARG GITHUB_TOKEN

# install ant
RUN mkdir -p /usr/share/man/man1
RUN apt-get update && apt-get install -y ant

# install gradle
RUN apt-get install -y gradle
RUN gradle --version

# copy cli code and install tkltest command
WORKDIR /app/tackle-test-cli
COPY tkltest ./tkltest
COPY setup.py ./
RUN pip install .

# install java lib dependencies
COPY lib/*.jar ./lib/
COPY lib/*.xml ./lib/
COPY lib/download_lib_jars.sh ./lib/
WORKDIR /app/tackle-test-cli/lib
RUN sed -ie "s|GITHUB_USERNAME|$GITHUB_USERNAME|g" settings.xml
RUN sed -ie "s|GITHUB_TOKEN|$GITHUB_TOKEN|g" settings.xml
RUN mvn -s ./settings.xml download:wget@get-randoop-jar download:wget@get-replacecall-jar
RUN mvn -s ./settings.xml dependency:copy-dependencies -DoutputDirectory=./download
#RUN ./download_lib_jars.sh

WORKDIR /app/tackle-test-cli

ENTRYPOINT ["tkltest"]
