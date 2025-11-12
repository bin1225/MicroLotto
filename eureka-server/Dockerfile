# Build stage
FROM gradle:8.14-jdk21-alpine AS builder

# Set working directory
WORKDIR /workspace

# Copy gradle wrapper and settings
COPY gradlew gradlew.bat ./
COPY gradle ./gradle
COPY settings.gradle build.gradle ./

# Copy all modules (for multi-module build)
COPY eureka-server ./eureka-server

# Build the service
RUN ./gradlew :eureka-server:bootJar --no-daemon

# Runtime stage
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copy the built jar
COPY --from=builder /workspace/eureka-server/build/libs/*.jar app.jar

# Expose port
EXPOSE 8761

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]