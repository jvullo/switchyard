package com.joevullo.switchyard.orders;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.kafka.core.KafkaTemplate;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest
@Testcontainers
@DisplayName("Kafka integration")
class OrderKafkaIT {

    @Container
    @ServiceConnection
    static KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("apache/kafka:3.9.0"));

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private OrderProperties orderProperties;

    @Test
    @DisplayName("the broker starts and the context is pointed at it")
    void brokerIsWiredIn() {
        assertThat(kafka.isRunning()).isTrue();
        assertThat(kafka.getBootstrapServers()).isNotBlank();
    }

    @Test
    @DisplayName("KafkaTemplate resolves against the container, not localhost:9092")
    void templatePointsAtContainer() {
        var configured = kafkaTemplate.getProducerFactory()
                .getConfigurationProperties()
                .get("bootstrap.servers")
                .toString();

        assertThat(configured).contains(kafka.getFirstMappedPort().toString());
    }

    @Test
    @DisplayName("order properties bind from application.yml")
    void propertiesBind() {
        assertThat(orderProperties.maxOrderTotal()).isEqualByComparingTo("5000.00");
        assertThat(orderProperties.topic()).isEqualTo("orders.events");
    }
}
