package com.lehaitien.gym.domain.model.Schedule;

import com.lehaitien.gym.domain.model.User.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "class_schedule_participant",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "class_schedule_id"}))
public class ClassScheduleParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", columnDefinition = "varchar(36)")
    String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_schedule_id", nullable = false)
    ClassSchedule classSchedule;

    @Column(name = "joined_at", nullable = false, updatable = false)
    @CreationTimestamp
    LocalDateTime joinedAt;

    @Column(name = "is_canceled", nullable = false)
    Boolean isCanceled = false;
}
