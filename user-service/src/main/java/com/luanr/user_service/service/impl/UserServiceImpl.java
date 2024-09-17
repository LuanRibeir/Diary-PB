package com.luanr.user_service.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import org.springframework.stereotype.Service;

import com.luanr.user_service.model.Activity;
import com.luanr.user_service.model.Friend;
import com.luanr.user_service.model.State;
import com.luanr.user_service.model.User;
import com.luanr.user_service.repository.UserRepository;
import com.luanr.user_service.repository.FriendRepository;
import com.luanr.user_service.service.UserService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final FriendRepository friendRepository;

    @Override
    public User save(User user) throws Exception {
        if (user.getName() == null || user.getName().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty.");
        }

        user.setState(State.ACTIVE);
        return userRepository.save(user);
    }

    @Override
    public List<User> findByName(String name) throws Exception {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty.");
        }

        return userRepository.findByName(name);
    }

    @Override
    public List<User> findAll() throws Exception {
        List<User> users = userRepository.findAll();

        if (users.isEmpty()) {
            throw new Exception("Users list is empty.");
        }

        return users;
    }

    @Override
    public Optional<User> findById(Long id) throws Exception {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new Exception("User with id [" + id + "] not found."));

        return Optional.of(existingUser);
    }

    @Override
    public Optional<User> deleteById(Long id) throws Exception {
        Optional<User> deletedUser = userRepository.findById(id);

        if (deletedUser.isEmpty()) {
            throw new Exception("User with id [" + id + "] not found.");
        }

        userRepository.deleteById(id);

        return deletedUser;
    }

    @Override
    public Optional<User> updateById(Long id, User user) throws Exception {

        if (!userRepository.existsById(id)) {
            throw new Exception("User does not exist.");
        }

        user.setId(id);
        userRepository.save(user);

        return userRepository.findById(id);
    }

    @Override
    @Transactional
    public Optional<User> addFriendById(Long id, Long friendId) throws Exception {
        if (id == friendId) {
            throw new IllegalArgumentException("You can't add yourself.");
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new Exception("User with id [" + id + "] not found."));
        User uFriend = userRepository.findById(friendId)
                .orElseThrow(() -> new Exception("Friend with id [" + friendId + "] not found."));

        Friend friend = new Friend(friendId, uFriend.getName());
        friendRepository.save(friend);

        user.getFriends().add(friend);
        userRepository.save(user);

        return userRepository.findById(id);
    }

    @Override
    public void addInboxById(Long id, Activity activity) throws Exception {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User with id [" + id + "] not found."));

        if (user.getInbox().stream().noneMatch(a -> a.getId().equals(activity.getId()))) {
            user.getInbox().add(activity);
            userRepository.save(user);
            log.info("Added activity [" + activity.getId() + "] to user [" + id + "]");
        } else {
            log.info("Activity " + activity.getId() + " already exists for user " + id);
        }
    }

}
