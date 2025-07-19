FROM eclipse-temurin:21-jdk-jammy
ARG JAR_FILE=build/libs/product.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-Xms128m", "-jar", "app.jar"]