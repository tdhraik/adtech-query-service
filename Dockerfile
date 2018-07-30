FROM openjdk:8
ADD target/adtech-0.0.1-SNAPSHOT.jar adtech-0.0.1-SNAPSHOT.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "adtech-0.0.1-SNAPSHOT.jar"]