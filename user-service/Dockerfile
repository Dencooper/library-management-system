FROM maven:3.8.5-openjdk-17-slim as build
WORKDIR /app

COPY ./user-service ./user-service
COPY ./common-service ./common-service

WORKDIR /app/common-service
RUN mvn clean install -DskipTests

WORKDIR /app/user-service
RUN mvn clean package -DskipTests

FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=build /app/user-service/target/*.jar user-service.jar
EXPOSE 9002
ENTRYPOINT ["java", "-jar", "user-service.jar"]
