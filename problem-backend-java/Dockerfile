ARG ENVIRONMENT=prod

FROM maven:3.9-eclipse-temurin-21-noble AS build
RUN mkdir -p /app
WORKDIR app
COPY pom.xml ./
RUN mvn  -DskipTests=true verify --fail-never
COPY src src
RUN mvn -DskipTests=true verify package

FROM build AS build-local
RUN apt-get update
RUN apt-get install -y inotify-tools
RUN apt-get install sqlite3;
COPY start.sh start.sh
ENV COMMAND="./start.sh"

FROM eclipse-temurin:21-jre-noble AS build-prod
COPY --from=build ./app/target/service.jar service.jar
ENV COMMAND="java -jar service.jar"

FROM build-${ENVIRONMENT} AS release

CMD $COMMAND