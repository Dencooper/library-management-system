# Stage 1: Build the Spring Boot application
FROM maven:3.8.5-openjdk-17-slim as build

WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Stage 2: Create runtime image
FROM openjdk:21-jdk-slim

WORKDIR /app
COPY --from=build /app/target/*.jar discovery-server.jar

EXPOSE 8761
ENTRYPOINT ["java", "-jar", "discovery-server.jar"]
