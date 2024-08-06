package com.luanr.activity_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.luanr.activity_service.model.Activity;

public interface ActivityRepository extends JpaRepository<Activity, Long> {

}
