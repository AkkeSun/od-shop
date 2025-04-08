FROM openjdk:21-jdk
ARG JAR_FILE=/build/libs/account.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-Xmx128m", "-jar", "app.jar"]