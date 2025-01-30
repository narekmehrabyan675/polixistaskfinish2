package producer;

import com.google.gson.Gson;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;
import java.util.Random;
import java.util.logging.Logger;

public class LocationProducer {
    private static final String TOPIC = "location_updates";
    private static final String BROKER = "kafka:9092";
    private static final Gson gson = new Gson();
    private static final Logger logger = Logger.getLogger(LocationProducer.class.getName());
    private final KafkaProducer<String, String> producer;


    // Конструктор
    public LocationProducer(KafkaProducer<String, String> producer) {
        this.producer = producer;
    }

    // Метод для отправки локации
    public void sendLocation(double latitude, double longitude) {
        String locationJson = String.format("{\"latitude\": %f, \"longitude\": %f}", latitude, longitude);
        producer.send(new ProducerRecord<>(TOPIC, locationJson));
        logger.info("Sent: " + locationJson);
    }


    public static void main(String[] args) {
        // Kafka producer config
        Properties props = new Properties();
        props.put("bootstrap.servers", BROKER);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        KafkaProducer<String, String> producer = new KafkaProducer<>(props);

        //Start is centre of Erevan
        double currentLatitude = 40.1792;
        double currentLongitude = 44.4991;

        Random random = new Random();

        logger.info("Starting LocationProducer...");

        while (true) {
            // Get random numbers near each oder so distance get lower like real life,for example for car
            double latitudeChange = (random.nextDouble() - 0.5) * 0.001;
            double longitudeChange = (random.nextDouble() - 0.5) * 0.001;

            currentLatitude += latitudeChange;
            currentLongitude += longitudeChange;

            // Check so coordinates stays in diapason
            currentLatitude = Math.min(90, Math.max(-90, currentLatitude));
            currentLongitude = Math.min(180, Math.max(-180, currentLongitude));

            long timestamp = System.currentTimeMillis();

            Location location = new Location(currentLatitude, currentLongitude, timestamp);
            String locationJson = gson.toJson(location);

            try {
                producer.send(new ProducerRecord<>(TOPIC, locationJson));
                logger.info("Sent: " + locationJson);
            } catch (Exception e) {
                logger.severe("Failed to send message: " + e.getMessage());
            }

            try {
                Thread.sleep(1000); // Send even 1 sec
            } catch (InterruptedException e) {
                logger.warning("Producer interrupted: " + e.getMessage());
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
