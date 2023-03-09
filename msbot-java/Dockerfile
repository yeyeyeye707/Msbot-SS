FROM openjdk:17

ARG JAR_FILE
ADD target/${JAR_FILE} /app.jar

CMD ["java", "-Xmx1024m", "-jar", "/app.jar"]