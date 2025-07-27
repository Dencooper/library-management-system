FROM maven:3.8.5-openjdk-17-slim as build
WORKDIR /app

COPY ./notification-service ./notification-service
COPY ./common-service ./common-service

WORKDIR /app/common-service
RUN mvn clean install -DskipTests

WORKDIR /app/notification-service
RUN mvn clean package -DskipTests

FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=build /app/notification-service/target/*.jar notification-service.jar
EXPOSE 9005
ENTRYPOINT ["java", "-jar", "notification-service.jar"]
