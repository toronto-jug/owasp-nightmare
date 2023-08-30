FROM amazoncorretto:18

ADD target/*.jar /app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]