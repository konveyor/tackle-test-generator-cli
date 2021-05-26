########################################
# Build Image
########################################
FROM maven:3.6-jdk-8-slim as build

WORKDIR /app
COPY . .

#RUN mvn dependency:resolve
#RUN mvn package

RUN mvn clean
RUN mvn install



########################################
# Production Image
########################################
FROM open-liberty:microProfile2-java8-ibm

# Add build files
COPY --from=build --chown=1001:0 /app/daytrader-ee7/target/daytrader-ee7-1.0-SNAPSHOT.ear /config/apps/daytrader-ee7.ear
COPY --from=build --chown=1001:0 /app/daytrader-ee7/src/main/liberty/config/ /config/
COPY --from=build --chown=1001:0 /app/daytrader-ee7/src/main/liberty/resources/db2jcc4.jar /liberty/usr/shared/resources/db2jars/