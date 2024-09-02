package com.luanr.user_service.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.luanr.user_service.model.Friend;
import com.luanr.user_service.model.User;
import com.luanr.user_service.repository.UserRepository;
import com.luanr.user_service.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public List<User> findByName(String name) {
        return userRepository.findByName(name);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> deleteById(Long id) {
        Optional<User> deletedUser = userRepository.findById(id);
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
    public Optional<User> addFriendById(Long id, Long friendId) throws Exception{
        if (id == friendId) {
            throw new Exception("You can't add yourself.");
        }

        User user = userRepository.findById(id).orElseThrow(() -> new Exception("User not found"));
        User uFriend = userRepository.findById(friendId).orElseThrow(() -> new Exception("Friend not found"));

        Friend friend = new Friend(friendId, uFriend.getName());

        List<Friend> friends = user.getFriends();
        friends.add(friend);
        user.setFriends(friends);

        userRepository.save(user);
        
        return userRepository.findById(id);
    }

}
