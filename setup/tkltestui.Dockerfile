FROM maven:3-eclipse-temurin-11

# copy from python 3.9 image
#COPY --from=python:3.9-slim / /

# install python3.9
RUN apt-get update && apt-get install -y wget unzip gnupg2 libgtk2.0-0 python3.11 python3-pip
RUN update-alternatives --install /usr/bin/python3 python3 /usr/bin/python3.11 2 \
    && update-alternatives --config python3

# install google chrome
#RUN wget -q -O - https://dl-ssl.google.com/linux/linux_signing_key.pub | apt-key add - \
#	&& echo "deb http://dl.google.com/linux/chrome/deb/ stable main" >> /etc/apt/sources.list.d/google-chrome.list \
#	&& apt-get update -qqy \
#	&& apt-get -qqy install google-chrome-stable \
#	&& rm /etc/apt/sources.list.d/google-chrome.list \
#	&& rm -rf /var/lib/apt/lists/* /var/cache/apt/* \
#	&& sed -i 's/"$HERE\/chrome"/"$HERE\/chrome" --no-sandbox/g' /opt/google/chrome/google-chrome

# get chrome version
ARG CHROME_RELEASE=117
RUN curl https://googlechromelabs.github.io/chrome-for-testing/LATEST_RELEASE_$CHROME_RELEASE > /root/chrome_version

# install chrome
RUN CHROME_VERSION=$(cat /root/chrome_version) \
    && wget https://dl.google.com/linux/chrome/deb/pool/main/g/google-chrome-stable/google-chrome-stable_${CHROME_VERSION}-1_amd64.deb \
    && apt-get --fix-broken install -y ./google-chrome-stable_${CHROME_VERSION}-1_amd64.deb \
    && sed -i 's/"$HERE\/chrome"/"$HERE\/chrome" --no-sandbox/g' /opt/google/chrome/google-chrome

# install chromedriver
RUN CHROME_VERSION=$(cat /root/chrome_version) \
    && wget https://edgedl.me.gvt1.com/edgedl/chrome/chrome-for-testing/${CHROME_VERSION}/linux64/chromedriver-linux64.zip \
    && unzip chromedriver-linux64.zip && mv chromedriver-linux64/chromedriver /usr/bin/chromedriver \
    && chmod +x /usr/bin/chromedriver

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
