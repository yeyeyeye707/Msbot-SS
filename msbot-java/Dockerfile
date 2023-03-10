# FROM openjdk:17
FROM maven:3.9.0-eclipse-temurin-17

COPY . .
RUN mvn clean install

# ARG JAR_FILE
# ADD target/${JAR_FILE} /app.jar
#JAR_FILE启动参数在docker-compose.yml中配置。
# ADD target/msbot.jar /app.jar

# CMD ["java", "-Xmx1024m", "-jar", "/app.jar"]
CMD mvn spring-boot:run