package com.luanr.user_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.luanr.user_service.model.Friend;

public interface FriendRepository extends JpaRepository<Friend, Long> {

}
