# FROM openjdk:17
FROM maven:3.9.0-eclipse-temurin-17

ENV HOME=/home/usr/app
RUN mkdir -p $HOME
WORKDIR $HOME

ADD pom.xml $HOME
# COPY /ali-settings.xml /root/.m2/settings.xml
RUN mvn dependency:go-offline
# COPY . .
# RUN mvn clean install

ADD ./src $HOME/src
RUN mvn clean install

# ARG JAR_FILE
# ADD target/${JAR_FILE} /app.jar
#JAR_FILE启动参数在docker-compose.yml中配置。
# ADD target/msbot.jar /app.jar

# CMD ["java", "-Xmx1024m", "-jar", "/app.jar"]
CMD mvn spring-boot:run