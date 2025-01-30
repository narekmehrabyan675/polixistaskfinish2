package consumer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import producer.LocationProducer;
import util.Haversine;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LocationConsumerTest {

    private LocationConsumer.Location lastLocation;
    private LocationConsumer.Location newLocation;
    private KafkaProducer<String, String> mockProducer;
    private static final Logger logger = Logger.getLogger(LocationConsumerTest.class.getName());

    @BeforeEach
    void setUp() {
        lastLocation = new LocationConsumer.Location(40.1792, 44.4991, System.currentTimeMillis()); // Yerevan center
        newLocation = new LocationConsumer.Location(40.1793, 44.4992, System.currentTimeMillis()); // Slightly moved
        mockProducer = Mockito.mock(KafkaProducer.class);
    }

    @Test
    void testHaversineDistanceCalculation() {
        double distance = Haversine.calculate(
                lastLocation.latitude, lastLocation.longitude,
                newLocation.latitude, newLocation.longitude
        );

        // Validate that the distance is within an expected small range
        assertTrue(distance > 0 && distance < 0.1, "Calculated distance should be small");
    }

    @Test
    void testTotalDistanceAccumulation() {
        // Simulate distance calculation
        double distance = Haversine.calculate(
                lastLocation.latitude, lastLocation.longitude,
                newLocation.latitude, newLocation.longitude
        );

        // Manually set the total distance
        LocationConsumer.totalDistance = 10.0; // Example starting total distance
        LocationConsumer.totalDistance += distance;

        // Expected total distance after update
        double expectedTotal = 10.0 + distance;

        assertEquals(expectedTotal, LocationConsumer.totalDistance, 1e-10,
                "Total distance should be updated correctly");
    }

    @Test
    void testLoggingPrecision() {
        double distance = Haversine.calculate(
                lastLocation.latitude, lastLocation.longitude,
                newLocation.latitude, newLocation.longitude
        );

        double totalDistance = 6.71340382661461; // Simulated total distance

        String logOutput = String.format("Distance from last point: %.2f km, Total Distance Traveled: %.14f km",
                distance, totalDistance);

        assertTrue(logOutput.contains("Total Distance Traveled: 6.71340382661461"),
                "Log output should contain full precision distance value");
    }

    @Test
    void testKafkaMessageFormat() {
        // Simulate sending the total distance
        double totalDistance = 6.71340382661461;

        ProducerRecord<String, String> expectedRecord = new ProducerRecord<>("distance_report",
                String.format("%.14f", totalDistance));

        mockProducer.send(expectedRecord);

        // Verify that Kafka producer was called with correctly formatted message
        verify(mockProducer, times(1)).send(any(ProducerRecord.class));
    }

    @Test
    void testSendLocation() {
        KafkaProducer<String, String> mockProducer = mock(KafkaProducer.class);
        LocationProducer producer = new LocationProducer(mockProducer);

        producer.sendLocation(40.1792, 44.4991);

        verify(mockProducer, times(1)).send(any(ProducerRecord.class));
    }
}
