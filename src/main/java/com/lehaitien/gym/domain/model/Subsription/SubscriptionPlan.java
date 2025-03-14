package com.lehaitien.gym.domain.model.Subsription;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "subscription_plan", indexes = {
        @Index(name = "idx_plan_name", columnList = "plan_name")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SubscriptionPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "plan_id", columnDefinition = "varchar(36)")
    String planId;

    @Column(name = "plan_name", nullable = false, unique = true)
    String planName;

    @Column(name = "duration", nullable = false)
    Integer duration; // Số ngày

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    Integer price;

    @Column(name = "description", columnDefinition = "TEXT")
    String description;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    LocalDateTime updatedAt;
}

