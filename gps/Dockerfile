FROM openjdk:12-jdk-alpine

ARG JAR_FILE

COPY ./build/libs/gps-1.0-SNAPSHOT.jar app.jar

ENTRYPOINT ["java","-jar","/app.jar"]