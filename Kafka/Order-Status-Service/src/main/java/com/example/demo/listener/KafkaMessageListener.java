package com.example.demo.listener;

import com.example.demo.model.KafkaMessage;
import com.example.demo.model.OrderStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
@Slf4j
public class KafkaMessageListener {

    @Value("${app.kafka.kafkaMessageTopic-client}")
    private String OrderServiceTopicName;

    private final KafkaTemplate<String, OrderStatus> KafkaTemplate;

    public KafkaMessageListener(KafkaTemplate<String, OrderStatus> KafkaTemplate) {
        this.KafkaTemplate = KafkaTemplate;
    }

    @KafkaListener(topics = "${app.kafka.kafkaMessageTopic}",
                    groupId = "${app.kafka.kafkaMessageGroupId}",
                    containerFactory = "kafkaMessageConcurrentKafkaListenerContainerFactory")
    public void listen(@Payload KafkaMessage upsertMessage,
                       @Header(value = KafkaHeaders.RECEIVED_KEY, required = false) UUID key,
                       @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                       @Header(KafkaHeaders.RECEIVED_PARTITION) Integer partition,
                       @Header(KafkaHeaders.RECEIVED_TIMESTAMP) Long timestamp
                       ) {

        String orderState = Math.round(Math.random()) == 0 ? "CREATED" : "PROCESS";
        OrderStatus event = new OrderStatus(orderState, Instant.now());

        KafkaTemplate.send(OrderServiceTopicName, event);

    }

}
