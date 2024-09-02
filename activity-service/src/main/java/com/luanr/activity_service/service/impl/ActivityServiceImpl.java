package com.luanr.activity_service.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.luanr.activity_service.model.Activity;
import com.luanr.activity_service.rabbitMq.ActivityProducer;
import com.luanr.activity_service.repository.ActivityRepository;
import com.luanr.activity_service.service.ActivityService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ActivityServiceImpl implements ActivityService {
    private final ActivityRepository activityRepository;
    private final ActivityProducer activityProducer;

    @Override
    public Activity add(Activity activity) throws JsonProcessingException {
        Activity act = activityRepository.save(activity);
        activityProducer.send(activity);
        return act;
    }

    @Override
    public List<Activity> findAll() {
        return activityRepository.findAll();
    }

    @Override
    public Optional<Activity> updateById(Long id, Activity activity) throws Exception {
        if (!activityRepository.existsById(id)) {
            throw new Exception("Activity does not exist.");
        }

        activity.setId(id);
        activityRepository.save(activity);

        return activityRepository.findById(id);
    }

    @Override
    public Optional<Activity> deleteById(Long id) {
        Optional<Activity> deletedActivity = activityRepository.findById(id);
        activityRepository.deleteById(id);
        return deletedActivity;
    }
}
