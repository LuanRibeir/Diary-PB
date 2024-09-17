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
import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
@RequiredArgsConstructor
public class ActivityConsumer {
    private final UserService userService;
    private final ObjectMapper objectMapper;

    @RabbitListener(queues = { "activity-queue" })
    @Transactional
    public void receive(@Payload String message) throws Exception {
        try {
            log.info("Processing activity message");

            Activity activity = objectMapper.readValue(message, Activity.class);
            Long actUserId = activity.getUserId();

            User actUser = userService.findById(actUserId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found: " + actUserId));

            List<Friend> friends = actUser.getFriends();
            List<User> users = userService.findAll();

            for (User user : users) {
                boolean haveAsFriend = user.getFriends().stream().anyMatch(f -> f.getUserId().equals(actUserId));
                if (haveAsFriend) {
                    userService.addInboxById(user.getId(), activity);
                }
            }

            log.info("Message processed sucsessfuly.");
        } catch (Exception e) {
            log.error("Error processing message: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
