FROM adoptopenjdk/openjdk11:alpine-slim
VOLUME ["/tmp"]
ARG JAR_FILE
COPY ${JAR_FILE} /app.jar
