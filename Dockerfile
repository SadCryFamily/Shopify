FROM openjdk:11
ADD /target/app-0.0.1-SNAPSHOT.jar backend.jar

COPY application-deploy.properties /backend/config/

ENV SPRING_CONFIG_LOCATION=file:/backend/config/application-deploy.properties
VOLUME /src/main/resources/application.properties:SPRING_CONFIG_LOCATION

ENTRYPOINT ["java", "-jar", "backend.jar"]