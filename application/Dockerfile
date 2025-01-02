FROM openjdk:17-oracle
ARG JAR_FILE=./application/target/*.jar
COPY ${JAR_FILE} application.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/application.jar"]