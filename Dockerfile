# Use Maven image to build the application
FROM maven:3.9.5-eclipse-temurin-21 AS build

# Set the working directory
WORKDIR /app

# Copy the project files
COPY pom.xml .
COPY src ./src

# Build the application skip tests
RUN mvn clean install -DskipTests

# Use a lightweight JRE image to run the application
FROM eclipse-temurin:21-jre

# Set the working directory
WORKDIR /app

# Copy the built JAR file from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the default Spring Boot port
EXPOSE 8080

# Set environment variables
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/your_postgres_db \
    SPRING_DATASOURCE_USERNAME=your_postgres_user \
    SPRING_DATASOURCE_PASSWORD=your_postgres_password \
    RABBIT_HOST=your_rabbit_host \
    RABBIT_PORT=your_rabbit_port \
    RABBIT_USERNAME=your_rabbit_user \
    RABBIT_PASSWORD=your_rabbit_password \
    MINIO_URL=http://localhost:9001 \
    MINIO_ACCESSKEY=your_minio_user \
    MINIO_SECRETKEY=your_minio_password \
    MINIO_BUCKET=your_minio_bucket \
    API_KEY_NAME=your_api_key \
    API_KEY_VALUE=your_api_value \
    DISCORD_CHANNEL=your_discord_channel \
    DISCORD_TOKEN=your_discord_token

# Run the application
ENTRYPOINT ["java", "-Dspring.profiles.active=dev", "-jar", "app.jar"]