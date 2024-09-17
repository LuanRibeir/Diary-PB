package com.luanr.activity_service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.luanr.activity_service.model.Activity;
import com.luanr.activity_service.repository.ActivityRepository;

@DataJpaTest
public class ActivityTests {
    @Autowired
    private ActivityRepository activityRepository;

    @BeforeEach
    public void setUp() {
        // Clean up the db
        activityRepository.deleteAll();
    }

    @Test
    public void testSaveActivity() {
        Activity activity = new Activity();
        activity.setImg("https://i.imgur.com/test.jpg");
        activity.setDescription("Test Activity");
        activity.setAmount(100);
        activity.setUserId(1L);

        Activity savedActivity = activityRepository.save(activity);

        assertNotNull(savedActivity.getId());
        assertEquals("https://i.imgur.com/test.jpg", savedActivity.getImg());
        assertEquals("Test Activity", savedActivity.getDescription());
        assertEquals(100, savedActivity.getAmount());
        assertEquals(1L, savedActivity.getUserId());
        assertNotNull(savedActivity.getCreatedAt());
        assertNotNull(savedActivity.getUpdatedAt());
    }

    @Test
    public void testFindById() {
        Activity activity = new Activity();
        activity.setImg("https://i.imgur.com/test.jpg");
        activity.setDescription("Test Activity");
        activity.setAmount(100);
        activity.setUserId(1L);

        Activity savedActivity = activityRepository.save(activity);
        Optional<Activity> foundActivity = activityRepository.findById(savedActivity.getId());

        assertTrue(foundActivity.isPresent());
        assertEquals(savedActivity.getId(), foundActivity.get().getId());
    }

    @Test
    public void testFindAll() {
        Activity activity1 = new Activity();
        activity1.setImg("https://i.imgur.com/test1.jpg");
        activity1.setDescription("Test Activity 1");
        activity1.setAmount(100);
        activity1.setUserId(1L);

        Activity activity2 = new Activity();
        activity2.setImg("https://i.imgur.com/test2.jpg");
        activity2.setDescription("Test Activity 2");
        activity2.setAmount(200);
        activity2.setUserId(2L);

        activityRepository.save(activity1);
        activityRepository.save(activity2);

        List<Activity> activities = activityRepository.findAll();
        assertEquals(2, activities.size());
    }

    @Test
    public void testDeleteById() {
        Activity activity = new Activity();
        activity.setImg("https://i.imgur.com/test.jpg");
        activity.setDescription("Test Activity");
        activity.setAmount(100);
        activity.setUserId(1L);

        Activity savedActivity = activityRepository.save(activity);
        activityRepository.deleteById(savedActivity.getId());

        Optional<Activity> deletedActivity = activityRepository.findById(savedActivity.getId());
        assertFalse(deletedActivity.isPresent());
    }
}
