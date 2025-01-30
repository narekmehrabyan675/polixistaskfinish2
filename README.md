# Project Documentation: Location Tracking System with Kafka, JUnit, and Docker

## 1️⃣ Introduction  
This project is a real-time location tracking system built using Apache Kafka for message streaming, JUnit for unit testing, and Docker for containerization.  

### Key Components:
- LocationProducer → Simulates GPS location updates and sends them to Kafka.
- LocationConsumer → Reads location updates, calculates distances using Haversine formula, and publishes total distance traveled.
- DistanceReport → Consumes the total distance data and logs it.
- Docker Integration → Encapsulates all services for easy deployment and execution.
- JUnit Tests → Ensures correctness of distance calculations and Kafka message processing.

---

## 2️⃣ Technologies Used
| Technology         | Purpose |
|-------------------|---------|
| Java 17       | Main programming language |
| Apache Kafka  | Message streaming system |
| Kafka CLI     | Topic creation and management |
| JUnit 5       | Unit testing framework |
| Mockito       | Mocking Kafka components in tests |
| Docker        | Containerization of services |
| Maven         | Dependency and build management |

---

## 3️⃣ Running the Project with Docker

You can download zip file via GitHub , after unzip that and open on Intelij Idea, after run your Docker Desktop and after that go to Intelij Idea and on terminal write 
```sh
$ mvn clean package
```
```sh
$ docker-compose down
```
```sh
$ docker-compose up --build
```

You can via clone Repository too.

### Step 1: Clone the Repository
```sh
$ git clone https://github.com/narekmehrabyan675/polixistaskfinish2
$ cd location-tracking
```

### Step 2: Build and Run the Docker Containers
```sh
$ mvn clean package
```
```sh
$ docker-compose up --build
```
This will:
- Start Zookeeper  
- Start Kafka  
- Create Kafka topics  
- Run Producer, Consumer, and Report Services  

### Step 3: Verify Kafka Topics
To list Kafka topics:
```sh
$ docker exec -it kafka kafka-topics.sh --list --bootstrap-server kafka:9092
```

---

## 4️⃣ Running JUnit Tests
To run JUnit tests for distance calculations and Kafka message processing:
```sh
$ mvn test
```

### JUnit Test Coverage:
- HaversineTest → Ensures distance calculations are correct.
- LocationConsumerTest → Mocks Kafka input and validates distance updates.

---

## 5️⃣ Conclusion
This Kafka-based location tracking system enables real-time processing of location updates. With JUnit tests ensuring correctness and Docker simplifying deployment, the system is both robust and easy to manage.

