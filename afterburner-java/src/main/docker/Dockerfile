FROM amazoncorretto:21.0.1-alpine3.18
VOLUME /tmp
ARG JAR_FILE
COPY $JAR_FILE afterburner-springboot.jar

ENTRYPOINT ["java","-jar","/afterburner-springboot.jar"]
