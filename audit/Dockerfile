# Dockerfile
FROM eclipse-temurin:21-jre

WORKDIR /app

COPY target/audit-0.0.1.jar app.jar

EXPOSE 8000

ENTRYPOINT ["java", "-jar", "app.jar"]
