package com.luanr.activity_service.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.luanr.activity_service.model.User;
import com.luanr.activity_service.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    public User findById(Long id) {
        RestClient restClient = RestClient.create();
        var serverUrl = String.format("http://localhost:8081/user/%d", id);
        User user = restClient.get()
                .uri(serverUrl)
                .retrieve()
                .toEntity(User.class).getBody();

        return user;
    }
}
