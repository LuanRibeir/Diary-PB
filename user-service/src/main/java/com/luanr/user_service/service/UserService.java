package com.luanr.user_service.service;

import java.util.List;
import java.util.Optional;

import com.luanr.user_service.model.Activity;
import com.luanr.user_service.model.User;

public interface UserService {
    User save(User user) throws Exception;

    List<User> findAll() throws Exception;

    Optional<User> findById(Long id) throws Exception;

    Optional<User> deleteById(Long id) throws Exception;

    Optional<User> updateById(Long id, User user) throws Exception;

    List<User> findByName(String name) throws Exception;

    Optional<User> addFriendById(Long id, Long friendId) throws Exception;

    void addInboxById(Long id, Activity message) throws Exception;

}
