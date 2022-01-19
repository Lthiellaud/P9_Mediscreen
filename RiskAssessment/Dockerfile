FROM openjdk:8-jre-slim
EXPOSE 8083
ARG JAR_FILE=target/*.jar
WORKDIR /massessment
COPY ${JAR_FILE} massessment.jar
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar ./massessment.jar"]