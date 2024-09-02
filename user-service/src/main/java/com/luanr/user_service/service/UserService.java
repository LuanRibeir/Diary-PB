package com.luanr.user_service.service;

import java.util.List;
import java.util.Optional;

import com.luanr.user_service.model.User;

public interface UserService {
    User save(User user);

    List<User> findAll();

    Optional<User> findById(Long id);

    Optional<User> deleteById(Long id);

    Optional<User> updateById(Long id, User user) throws Exception;

    List<User> findByName(String name);

    Optional<User> addFriendById(Long id, Long friendId) throws Exception;

}
