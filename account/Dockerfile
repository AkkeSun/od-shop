FROM openjdk:21-jdk
ARG JAR_FILE=/build/libs/account.jar
COPY ${JAR_FILE} app.jar
ENV username default
EXPOSE 8080
VOLUME ["logs"]
ENTRYPOINT ["java", "-Xmx512m", "-Dconst.username=${username}","-jar", "app.jar"]