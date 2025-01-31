FROM openjdk:17-jdk-slim

# Install dos2unix
RUN apt-get update && apt-get install -y dos2unix && rm -rf /var/lib/apt/lists/*

# Copy scrypt to container
COPY create-topics.sh /usr/local/bin/create-topics.sh

# Translate scrypt to Windows
RUN dos2unix /usr/local/bin/create-topics.sh
RUN chmod +x /usr/local/bin/create-topics.sh

WORKDIR /app

COPY target/location-tracking-1.0-SNAPSHOT.jar app.jar

EXPOSE 9092

CMD ["java", "-jar", "app.jar"]
