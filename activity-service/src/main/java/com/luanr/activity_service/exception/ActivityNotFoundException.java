package com.luanr.activity_service.exception;

public class ActivityNotFoundException extends RuntimeException {
    public ActivityNotFoundException(Long id) {
        super("Activity with id: [" + id + "] does not exist.");
    }
}
