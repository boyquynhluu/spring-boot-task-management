# ===== Stage 1: Build =====
FROM maven:3.9.9-eclipse-temurin-17 AS builder

WORKDIR /app

# Copy pom trước để cache dependency
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy source
COPY src ./src

# Build jar
RUN mvn clean package -DskipTests

# ===== Stage 2: Run =====
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copy jar từ stage build
COPY --from=builder /app/target/*.jar app.jar

# Expose port
EXPOSE 8081

# Run app
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=prod"]