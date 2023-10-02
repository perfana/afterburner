FROM openjdk:openjdk:21-ea-29-jdk-slim
VOLUME /tmp
ARG JAR_FILE
COPY $JAR_FILE afterburner-springboot.jar

ENTRYPOINT ["java","-jar","/afterburner-springboot.jar"]
