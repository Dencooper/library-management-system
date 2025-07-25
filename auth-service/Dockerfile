FROM maven:3.8.5-openjdk-17-slim as build
WORKDIR /app

COPY ./auth-service ./auth-service
COPY ./common-service ./common-service

WORKDIR /app/common-service
RUN mvn clean install -DskipTests

WORKDIR /app/auth-service
RUN mvn clean package -DskipTests

FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=build /app/auth-service/target/*.jar auth-service.jar
EXPOSE 9004
ENTRYPOINT ["java", "-jar", "auth-service.jar"]
