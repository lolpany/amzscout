FROM openjdk:11-jdk-slim
WORKDIR /home/amzscout
ARG REVISION
COPY target/spring-boot-app-${REVISION}.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]