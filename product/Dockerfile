FROM eclipse-temurin:21-jdk-jammy
ARG JAR_FILE=build/libs/product.jar
COPY ${JAR_FILE} /app.jar
COPY opentelemetry-javaagent.jar /opentelemetry-javaagent.jar

ENV OTEL_LOGS_EXPORTER=otlp
ENV OTEL_RESOURCE_ATTRIBUTES=service.name=product
ENV OTEL_TRACES_EXPORTER=otlp
ENV OTEL_METRICS_EXPORTER=otlp
ENV OTEL_EXPORTER_OTLP_ENDPOINT=http://49.168.86.72:4318
ENV OTEL_EXPORTER_OTLP_PROTOCOL=http/protobuf
ENV OTEL_INSTRUMENTATION_LOGGING_EXPERIMENTAL_ENABLED=true

ENTRYPOINT ["java", "-javaagent:/opentelemetry-javaagent.jar", "-Xms128m", "-jar", "/app.jar"]
