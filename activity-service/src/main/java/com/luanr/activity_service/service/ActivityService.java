package com.luanr.activity_service.service;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.luanr.activity_service.model.Activity;

public interface ActivityService {
    Activity add(Activity activity) throws JsonProcessingException;

    List<Activity> findAll() throws Exception;

    Optional<Activity> updateById(Long id, Activity activity) throws Exception;

    Optional<Activity> deleteById(Long id) throws Exception;

}
