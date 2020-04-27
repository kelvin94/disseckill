
FROM openjdk:8-jdk-alpine
RUN apk add --no-cache bash
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
COPY ./mq_start.sh mq_start.sh
