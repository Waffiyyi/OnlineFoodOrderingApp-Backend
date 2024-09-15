FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY target/Online-Food-Ordering-0.0.1-SNAPSHOT.jar Online-Food-Ordering-0.0.1-SNAPSHOT.jar
EXPOSE 8011
ENTRYPOINT ["java", "-jar", "Online-Food-Ordering-0.0.1-SNAPSHOT.jar"]