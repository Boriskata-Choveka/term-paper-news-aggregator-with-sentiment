# Stage 1: Build the application
FROM maven:3.8.7-openjdk-18 AS build
WORKDIR /app
# Copy the Maven project files
COPY pom.xml .
# Download project dependencies (this leverages Docker cache)
RUN mvn dependency:go-offline
# Copy the source code
COPY src ./src
# Package the application; adjust the maven goal if necessary.
RUN mvn clean package -DskipTests

# Stage 2: Create the runtime image
FROM openjdk:18-jdk-slim
WORKDIR /app
# Copy the compiled JAR from the build stage. Adjust the jar name to match your build artifact.
COPY --from=build /app/target/NewsAggregatorwithSentiment-1.0-SNAPSHOT.jar app.jar
# Optionally expose a port if your application listens on one.
EXPOSE 8080
# Define the entrypoint for the container
ENTRYPOINT ["java", "-jar", "app.jar"]
