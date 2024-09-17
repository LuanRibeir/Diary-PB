package com.luanr.user_service;

import com.luanr.user_service.model.Activity;
import com.luanr.user_service.model.State;
import com.luanr.user_service.model.User;
import com.luanr.user_service.repository.FriendRepository;
import com.luanr.user_service.repository.UserRepository;
import com.luanr.user_service.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FriendRepository friendRepository;

    @Autowired
    private UserServiceImpl userService;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
        friendRepository.deleteAll();
    }

    @Test
    public void testSaveUser() throws Exception {
        User user = new User();
        user.setName("John Doe");
        user.setState(State.ACTIVE);

        User savedUser = userService.save(user);

        assertNotNull(savedUser.getId());
        assertEquals("John Doe", savedUser.getName());
        assertEquals(State.ACTIVE, savedUser.getState());
    }

    @Test
    public void testFindByName() throws Exception {
        User user1 = new User();
        user1.setName("Jane Doe");
        userService.save(user1);

        User user2 = new User();
        user2.setName("John Doe");
        userService.save(user2);

        List<User> users = userService.findByName("Jane Doe");
        assertEquals(1, users.size());
        assertEquals("Jane Doe", users.get(0).getName());
    }

    @Test
    public void testFindAll() throws Exception {
        User user1 = new User();
        user1.setName("Jane Doe");
        userService.save(user1);

        User user2 = new User();
        user2.setName("John Doe");
        userService.save(user2);

        List<User> users = userService.findAll();
        assertEquals(2, users.size());
    }

    @Test
    public void testFindById() throws Exception {
        User user = new User();
        user.setName("Jane Doe");
        User savedUser = userService.save(user);

        Optional<User> foundUser = userService.findById(savedUser.getId());
        assertTrue(foundUser.isPresent());
        assertEquals("Jane Doe", foundUser.get().getName());
    }

    @Test
    public void testDeleteById() throws Exception {
        User user = new User();
        user.setName("Jane Doe");
        User savedUser = userService.save(user);

        Optional<User> deletedUser = userService.deleteById(savedUser.getId());
        assertTrue(deletedUser.isPresent());
        assertEquals("Jane Doe", deletedUser.get().getName());
    }

    @Test
    public void testUpdateById() throws Exception {
        User user = new User();
        user.setName("Jane Doe");
        User savedUser = userService.save(user);

        user.setName("Jane Smith");
        Optional<User> updatedUser = userService.updateById(savedUser.getId(), user);

        assertTrue(updatedUser.isPresent());
        assertEquals("Jane Smith", updatedUser.get().getName());
    }

    @Test
    public void testAddFriendById() throws Exception {
        User user = new User();
        user.setName("John Doe");
        User savedUser = userService.save(user);

        User friend = new User();
        friend.setName("Friend");
        User savedFriend = userService.save(friend);

        Optional<User> updatedUser = userService.addFriendById(savedUser.getId(), savedFriend.getId());

        assertTrue(updatedUser.isPresent());
        assertEquals(1, updatedUser.get().getFriends().size());
        assertEquals("Friend", updatedUser.get().getFriends().get(0).getName());
    }

    @Test
    public void testAddInboxById() throws Exception {
        User user = new User();
        user.setName("John Doe");
        User savedUser = userService.save(user);

        Activity activity = new Activity();
        activity.setAmount(100);
        activity.setDescription("Test Activity");
        activity.setImg("https://i.imgur.com/test.jpg");
        activity.setUserId(1L);
        activity.setCreatedAt(LocalDateTime.now());
        activity.setUpdatedAt(LocalDateTime.now());

        userService.addInboxById(savedUser.getId(), activity);

        Optional<User> updatedUser = userService.findById(savedUser.getId());
        assertTrue(updatedUser.isPresent());
        assertEquals(1, updatedUser.get().getInbox().size());
        assertEquals("Test Activity", updatedUser.get().getInbox().get(0).getDescription());
        assertEquals("https://i.imgur.com/test.jpg", updatedUser.get().getInbox().get(0).getImg());
    }

    @Test
    public void testAddFriendById_Exception() throws Exception {
        User user = new User();
        user.setName("John Doe");
        User savedUser = userService.save(user);

        User friend = new User();
        friend.setName("Friend");
        User savedFriend = userService.save(friend);

        Exception exception = assertThrows(Exception.class, () -> {
            userService.addFriendById(savedUser.getId(), savedUser.getId());
        });

        assertEquals("You can't add yourself.", exception.getMessage());
    }
}
