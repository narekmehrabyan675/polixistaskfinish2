# Use OpenJDK base image
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy the compiled JAR file into the container
COPY target/location-tracking-1.0-SNAPSHOT.jar app.jar

# Expose the necessary ports
EXPOSE 9092

# Command to run the application
CMD ["java", "-jar", "app.jar"]
