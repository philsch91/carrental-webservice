FROM openjdk:8-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} carrental-webservice.jar
ENTRYPOINT ["java","-jar","/carrental-webservice.jar"]
EXPOSE 443

