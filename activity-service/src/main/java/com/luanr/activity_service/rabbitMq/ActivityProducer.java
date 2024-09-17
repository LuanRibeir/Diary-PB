package com.luanr.activity_service.rabbitMq;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.luanr.activity_service.model.Activity;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.AmqpTemplate;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class ActivityProducer {
    private final AmqpTemplate amqpTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .setDateFormat(new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss"));

    public void send(Activity activity) throws JsonProcessingException {
        log.info("Producer attempting to send activity: {}", activity);

        try {
            String activityJson = objectMapper.writeValueAsString(activity);
            log.debug("Serialized activity to JSON: {}", activityJson);

            amqpTemplate.convertAndSend("activity-exc", "activity-rk", activityJson);
            log.info("Successfully sent activity to RabbitMQ with routing key 'activity-rk'");
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize activity: {}", activity, e);
            throw e;
        }
    }
}
