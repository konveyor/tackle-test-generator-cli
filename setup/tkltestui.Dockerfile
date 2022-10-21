FROM maven:3-openjdk-8-slim

# copy from python 3.9 image
COPY --from=python:3.9-slim / /

# install ant
RUN mkdir -p /usr/share/man/man1
RUN apt-get update && apt-get install -y wget
RUN apt-get install -y gnupg2 libgtk2.0-0 enchant-2

# install google chrome
RUN wget -q -O - https://dl-ssl.google.com/linux/linux_signing_key.pub | apt-key add - \
	&& echo "deb http://dl.google.com/linux/chrome/deb/ stable main" >> /etc/apt/sources.list.d/google-chrome.list \
	&& apt-get update -qqy \
	&& apt-get -qqy install google-chrome-stable \
	&& rm /etc/apt/sources.list.d/google-chrome.list \
	&& rm -rf /var/lib/apt/lists/* /var/cache/apt/* \
	&& sed -i 's/"$HERE\/chrome"/"$HERE\/chrome" --no-sandbox/g' /opt/google/chrome/google-chrome

# install java lib dependencies
WORKDIR /app/tackle-test-cli
COPY tkltest-lib/*.* ./tkltest-lib/
WORKDIR /app/tackle-test-cli/tkltest-lib
RUN ./download_lib_jars_ui.sh

# copy cli code and install tkltest command
WORKDIR /app/tackle-test-cli
COPY tkltest ./tkltest
COPY setup/tkltestui.setup.py ./setup.py
COPY MANIFEST.in ./
RUN pip install .

# set entrypoint
ENTRYPOINT ["tkltest-ui"]
