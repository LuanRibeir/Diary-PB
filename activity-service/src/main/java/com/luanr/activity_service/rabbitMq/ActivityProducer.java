package com.luanr.activity_service.rabbitMq;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.luanr.activity_service.model.Activity;

import org.springframework.amqp.core.AmqpTemplate;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ActivityProducer {
    private final AmqpTemplate amqpTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void send(Activity activity) throws JsonProcessingException { 
       amqpTemplate.convertAndSend("activity-exc", "activity-rk", objectMapper.writeValueAsString(activity)); 
    }
}
