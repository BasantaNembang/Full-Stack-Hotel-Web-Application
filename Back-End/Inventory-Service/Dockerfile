
FROM openjdk:17-jdk-alpine

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} inventory-service.jar

ENTRYPOINT ["java", "-Xms512m", "-Xmx1g", "-jar", "/inventory-service.jar"]

EXPOSE 8083

