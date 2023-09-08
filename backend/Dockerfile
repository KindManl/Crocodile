FROM maven:3.8.5-jdk-11-slim

COPY target/crocodile-0.0.1-SNAPSHOT.jar backend/app.jar

EXPOSE 8181
CMD ["java", "-jar", "backend/app.jar"]
