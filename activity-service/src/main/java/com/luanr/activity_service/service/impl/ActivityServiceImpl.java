package com.luanr.activity_service.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.luanr.activity_service.exception.ActivityNotFoundException;
import com.luanr.activity_service.model.Activity;
import com.luanr.activity_service.rabbitMq.ActivityProducer;
import com.luanr.activity_service.repository.ActivityRepository;
import com.luanr.activity_service.service.ActivityService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class ActivityServiceImpl implements ActivityService {
    private final ActivityRepository activityRepository;
    private final ActivityProducer activityProducer;

    @Override
    public Activity add(Activity activity) throws JsonProcessingException {
        log.info("Service adding a new activity: {}", activity);

        if (activity.getImg() == null) {
            log.error("Image URL is null.");
            throw new IllegalArgumentException("Image URL cannot be null.");
        }
        if (activity.getDescription() == null || activity.getDescription().isEmpty()) {
            log.error("Description is null or empty.");
            throw new IllegalArgumentException("Description cannot be null or empty.");
        }
        if (activity.getAmount() < 0) {
            log.error("Amount is negative.");
            throw new IllegalArgumentException("Amount cannot be negative.");
        }
        if (activity.getUserId() == null) {
            log.error("User ID is null.");
            throw new IllegalArgumentException("User Id cannot be null.");
        }

        Activity act = activityRepository.save(activity);
        activityProducer.send(act);

        log.info("Activity saved successfully with ID: {}", act.getId());

        return act;
    }

    @Override
    public List<Activity> findAll() throws Exception {
        log.info("Service fetching all activities.");

        List<Activity> activities = activityRepository.findAll();

        if (activities.isEmpty()) {
            log.warn("No activities found.");
            throw new Exception("Activity list is empty.");
        }

        log.info("Found {} activities.", activities.size());
        return activities;
    }

    @Override
    public Optional<Activity> updateById(Long id, Activity activity) throws Exception {
        log.info("Service updating activity with ID: {}", id);

        Activity existingActivity = activityRepository.findById(id)
                .orElseThrow(() -> new ActivityNotFoundException(id));

        existingActivity.update(activity.getImg(), activity.getDescription(), activity.getAmount(),
                activity.getUserId());

        activityRepository.save(existingActivity);

        log.info("Activity updated successfully: {}", existingActivity);

        return Optional.of(existingActivity);
    }

    @Override
    public Optional<Activity> deleteById(Long id) throws Exception {
        log.info("Service deleting activity with ID: {}", id);

        Optional<Activity> deletedActivity = activityRepository.findById(id);

        if (deletedActivity.isEmpty()) {
            log.error("Activity with ID: {} not found.", id);
            throw new ActivityNotFoundException(id);
        }

        activityRepository.deleteById(id);

        log.info("Activity with ID: {} deleted successfully.", id);

        return deletedActivity;
    }
}
