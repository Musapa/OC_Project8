# FROM tells Docker that this image is based on the OpenJDK 12 Alpine base image. This means we’ll be using Alpine Linux, which is lightweight and fast. It’s bundled up with a Java installation so we don’t have to worry about installing it separately.
FROM openjdk:12-jdk-alpine

#
ARG JAR_FILE

# COPY will copy the application jar file into the image
COPY ./build/libs/rewards-1.0-SNAPSHOT.jar app.jar

# CMD tells the Docker what command to run when we start a container of this image. In our case, we just want to run java specifying the jar file.
ENTRYPOINT ["java","-jar","/app.jar"]