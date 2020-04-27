
FROM openjdk:8-jdk-alpine
RUN apk add --no-cache bash
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
COPY ./order_start.sh order_start.sh
#ENTRYPOINT ["java","-jar","/app.jar", "--spring.profiles.active=dev"]
