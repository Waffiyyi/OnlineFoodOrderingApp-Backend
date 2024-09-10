FROM openjdk:17
LABEL maintainer = "eventbookingdevs"
WORKDIR /app
COPY Online-Food-Ordering-0.0.1-SNAPSHOT.jar /app/Online-Food-Ordering-0.0.1-SNAPSHOT.jar
EXPOSE 8011
ENTRYPOINT ["java", "-jar", "Online-Food-Ordering-0.0.1-SNAPSHOT.jar"]