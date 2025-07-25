FROM maven:3.8.5-openjdk-17-slim as build
WORKDIR /app

COPY ./book-service ./book-service
COPY ./common-service ./common-service

WORKDIR /app/common-service
RUN mvn clean install -DskipTests

WORKDIR /app/book-service
RUN mvn clean package -DskipTests

FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=build /app/book-service/target/*.jar book-service.jar
EXPOSE 9001
ENTRYPOINT ["java", "-jar", "book-service.jar"]
