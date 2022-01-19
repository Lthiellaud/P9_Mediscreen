FROM openjdk:8-jre-slim
EXPOSE 8082
ARG JAR_FILE=target/*.jar
WORKDIR /mnotes
COPY ${JAR_FILE} mnotes.jar
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar ./mnotes.jar"]