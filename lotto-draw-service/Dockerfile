# Build from project root: docker build -f lotto-draw-service/Dockerfile -t lotto-draw-service .

# Build stage
FROM gradle:8.14-jdk21-alpine AS builder

# Set working directory
WORKDIR /workspace

# Copy gradle wrapper and settings
COPY gradlew gradlew.bat ./
COPY gradle ./gradle
COPY settings.gradle build.gradle ./

# Copy all modules (for multi-module build)
COPY lotto-api ./lotto-api
COPY util ./util
COPY lotto-draw-service ./lotto-draw-service

# Build the service
RUN ./gradlew :lotto-draw-service:bootJar --no-daemon

# Runtime stage
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copy the built jar
COPY --from=builder /workspace/lotto-draw-service/build/libs/*.jar app.jar

# Expose port
EXPOSE 7002

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
