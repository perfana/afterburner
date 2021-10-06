FROM openjdk:8-jdk-alpine
VOLUME /tmp
ARG JAR_FILE
COPY $JAR_FILE afterburner-springboot.jar

ENTRYPOINT ["java","-jar","/afterburner-springboot.jar"]
