FROM openjdk:8-jdk-alpine
VOLUME /tmp
ARG JAR_FILE
COPY ${JAR_FILE} afterburner-springboot.jar
# urandom to speed up tomcat startup
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/afterburner-springboot.jar"]