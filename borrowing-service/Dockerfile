FROM maven:3.8.5-openjdk-17-slim as build
WORKDIR /app

COPY ./borrowing-service ./borrowing-service
COPY ./common-service ./common-service

WORKDIR /app/common-service
RUN mvn clean install -DskipTests

WORKDIR /app/borrowing-service
RUN mvn clean package -DskipTests

FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=build /app/borrowing-service/target/*.jar borrowing-service.jar
EXPOSE 9003
ENTRYPOINT ["java", "-jar", "borrowing-service.jar"]
