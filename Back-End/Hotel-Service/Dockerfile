
FROM openjdk:17-jdk-alpine

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} hotel-service.jar

ENTRYPOINT ["java", "-jar", "/hotel-service.jar"]

EXPOSE 9090



