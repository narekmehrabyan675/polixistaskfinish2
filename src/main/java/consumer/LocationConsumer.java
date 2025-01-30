package consumer;

import com.google.gson.Gson;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import util.Haversine;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

public class LocationConsumer {
    private static final String LOCATION_TOPIC = "location_updates";
    private static final String DISTANCE_TOPIC = "distance_report";
    private static final String BROKER = "kafka:9092";
    private static final Gson gson = new Gson();
    private static final Logger logger = Logger.getLogger(LocationConsumer.class.getName());

    public static double totalDistance = 0.0;
    private static Location lastLocation = null;
    private static final ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) {
        // Kafka consumer configuration
        Properties consumerProps = new Properties();
        consumerProps.put("bootstrap.servers", BROKER);
        consumerProps.put("group.id", "location-tracker");
        consumerProps.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        consumerProps.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(consumerProps);
        consumer.subscribe(Collections.singletonList(LOCATION_TOPIC));

        // Kafka producer configuration for distance_report
        Properties producerProps = new Properties();
        producerProps.put("bootstrap.servers", BROKER);
        producerProps.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producerProps.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        KafkaProducer<String, String> producer = new KafkaProducer<>(producerProps);

        logger.info("Starting LocationConsumer...");
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
            for (ConsumerRecord<String, String> record : records) {
                Location location = gson.fromJson(record.value(), Location.class);

                    if (lastLocation != null) {
                        double distance = Haversine.calculate(
                                lastLocation.latitude, lastLocation.longitude,
                                location.latitude, location.longitude
                        );
                        totalDistance += distance;

                        // Sending total sum on DISTANCE_TOPIC
                        producer.send(new ProducerRecord<>(DISTANCE_TOPIC, String.valueOf(totalDistance)));

                        logger.info(String.format("Distance from last point: %.2f km, Total Distance Traveled: %.16f km", distance, totalDistance));
                    } else {
                        logger.info("First location received: " + record.value());
                    }

                    lastLocation = location;
            }
        }
    }

    static class Location {
        double latitude;
        double longitude;
        long timestamp;

        public Location(double latitude, double longitude, long timestamp) {
            this.latitude = latitude;
            this.longitude = longitude;
            this.timestamp = timestamp;
        }
    }
}
