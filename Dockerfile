FROM openjdk:11-oracle
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-Xmx128m", "-jar", "app.jar", "--server.port=8080"]
EXPOSE 8080