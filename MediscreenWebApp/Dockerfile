FROM openjdk:8-jre-slim
EXPOSE 8090
ARG JAR_FILE=target/*.jar
WORKDIR /mediscreen
COPY ${JAR_FILE} mediscreen.jar
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /mediscreen/mediscreen.jar" ]