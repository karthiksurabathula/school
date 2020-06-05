#docker build -t karthiksurabathula/springboot:latest .
FROM java:8-jdk-alpine
RUN apk --no-cache add curl
COPY ./school-app-v1.jar /usr/app/
COPY ./application.yml /usr/app/
WORKDIR /usr/app
EXPOSE 8082
#ENTRYPOINT ["java", "-jar", "school-app-v1.jar","--spring.config.location=application.yml"]
ENTRYPOINT exec java $JAVA_OPTS -jar school-app-v1.jar --spring.config.location=application.yml