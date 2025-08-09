
FROM openjdk:17-jdk-alpine

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} user-service.jar

ENTRYPOINT ["java", "-jar", "/user-service.jar"]

EXPOSE 8081
