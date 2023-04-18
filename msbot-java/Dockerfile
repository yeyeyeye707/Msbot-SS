FROM openjdk:17

ENV HOME=/home/usr/app
RUN mkdir -p $HOME
WORKDIR $HOME


# ARG JAR_FILE
# ADD target/${JAR_FILE} /app.jar
#JAR_FILE启动参数在docker-compose.yml中配置。
ADD msbot.jar ./app.jar

CMD ["java", "-Xmx1024m", "-jar", "./app.jar"]