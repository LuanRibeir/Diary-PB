package com.luanr.user_service.rabbitMq;

import java.util.List;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luanr.user_service.model.Activity;
import com.luanr.user_service.model.Friend;
import com.luanr.user_service.model.User;
import com.luanr.user_service.service.UserService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ActivityConsumer {
    private final UserService userService;
    private final ObjectMapper objectMapper;

    @RabbitListener(queues = {"activity-queue"})
    @Transactional
    public void receive(@Payload String message) throws Exception {
        try {
            Activity activity = objectMapper.readValue(message, Activity.class);

            Long userId = activity.getUserId();

            User user = userService.findById(userId)
                                   .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

            List<Friend> friends = user.getFriends();

            for (Friend friend : friends) {
                userService.addInboxById(friend.getId(), activity);
            }
        } catch (Exception e) {
            System.err.println("Error processing message: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
