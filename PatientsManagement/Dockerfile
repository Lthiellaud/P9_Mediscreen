FROM openjdk:8-jre-slim
EXPOSE 8081
ARG JAR_FILE=target/*.jar
WORKDIR /mpatients
COPY ${JAR_FILE} mpatients.jar
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar ./mpatients.jar"]