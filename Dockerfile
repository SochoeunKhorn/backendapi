FROM openjdk:17-jdk-slim

WORKDIR /app

EXPOSE 8080

COPY target/BackendAPI.war /app/BackendAPI.war

ENTRYPOINT ["java", "-jar","/app/BackendAPI.war"]