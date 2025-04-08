FROM eclipse-temurin:21-jdk
ARG JAR_FILE=/build/libs/account.jar
COPY ${JAR_FILE} app.jar
ENV username default
EXPOSE 8081
VOLUME ["logs"]
ENTRYPOINT ["java", "-Dconst.username=${username}","-jar", "app.jar"]