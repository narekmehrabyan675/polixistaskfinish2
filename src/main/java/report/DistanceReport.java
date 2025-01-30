package report;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class DistanceReport {
    private static final String DISTANCE_TOPIC = "distance_report";
    private static final String BROKER = "kafka:9092";

    public static void main(String[] args) {
        // Kafka consumer config
        Properties props = new Properties();
        props.put("bootstrap.servers", BROKER);
        props.put("group.id", "distance-reporter");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList(DISTANCE_TOPIC));

        System.out.println("Starting DistanceReport...");
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
            for (ConsumerRecord<String, String> record : records) {
                System.out.println("Total Distance Traveled: " + record.value() + " km");
            }
        }
    }
}
