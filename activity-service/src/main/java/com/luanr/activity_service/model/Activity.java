package com.luanr.activity_service.model;

import java.time.LocalDateTime;
import java.time.ZoneId;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Entity
@Table(name = "\"activity\"")
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "activity_image", nullable = false)
    private String img;

    @Column(name = "activity_description", nullable = false)
    private String description;

    @Column(name = "activity_amount", nullable = false)
    @Min(value = 0)
    private Integer amount;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "activity_name", nullable = false)
    private String name;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    void prePersist() {
        this.createdAt = this.updatedAt = LocalDateTime.now(ZoneId.systemDefault());
    }

    @PreUpdate
    void preUpdate() {
        this.updatedAt = LocalDateTime.now(ZoneId.systemDefault());
    }

    @Builder
    public Activity(String img, String description, Integer amount, Long userId, String name) {
        this.img = img;
        this.description = description;
        this.amount = amount;
        this.userId = userId;
        this.name = name;
        this.createdAt = LocalDateTime.now(ZoneId.systemDefault());
    }

    public void update(String img, String description, Integer amount, Long userId) {
        this.img = img;
        this.description = description;
        this.amount = amount;
        this.userId = userId;
        this.updatedAt = LocalDateTime.now(ZoneId.systemDefault());
    }
}
