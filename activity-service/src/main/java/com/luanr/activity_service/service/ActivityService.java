package com.luanr.activity_service.service;

import java.util.List;
import java.util.Optional;

import com.luanr.activity_service.model.Activity;
import com.luanr.activity_service.model.User;

public interface ActivityService {
    Activity add(Activity activity);

    List<Activity> findAll();

    Optional<Activity> updateById(Long id, Activity activity) throws Exception;

    Optional<Activity> deleteById(Long id);

}
