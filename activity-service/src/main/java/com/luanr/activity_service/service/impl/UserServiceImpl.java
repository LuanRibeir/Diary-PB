package com.luanr.activity_service.service.impl;

import org.springframework.stereotype.Service;

import com.luanr.activity_service.model.User;
import com.luanr.activity_service.service.UserService;
import com.luanr.activity_service.service.feign.UserClient;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserClient userClient;

    public User findById(Long id) {
        return userClient.findById(id);
    }
}
