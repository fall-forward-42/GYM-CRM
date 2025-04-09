package com.lehaitien.gym.domain.model.Schedule;


import com.lehaitien.gym.domain.constant.ClassShift;
import com.lehaitien.gym.domain.model.Branch.Branch;
import com.lehaitien.gym.domain.model.Branch.BranchFacility;
import com.lehaitien.gym.domain.model.User.User;
import com.lehaitien.gym.domain.constant.ClassStatus;
import com.lehaitien.gym.domain.constant.ClassType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "class_schedule", indexes = {
        @Index(name = "idx_class_schedule_start_time", columnList = "start_time")
})
public class ClassSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "class_schedule_id", columnDefinition = "varchar(36)")
    String classScheduleId;

    @ManyToOne
    @JoinColumn(name = "branch_id", nullable = false)
    Branch branch;

    @ManyToOne
    @JoinColumn(name = "facility_id")
    BranchFacility branchFacility;

    @ManyToOne
    @JoinColumn(name = "coach_id", nullable = false)
    User coach;

    @Column(name = "start_time", nullable = false)
    LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    ClassStatus status;

    @Column(name = "max_participants")
    Integer maxParticipants;

    @Enumerated(EnumType.STRING)
    @Column(name = "class_type", nullable = false)
    ClassType classType;

    //: tuần, tháng, năm
    @Column(name = "week_of_year")
    Integer weekOfYear;

    @Column(name = "month")
    Integer month;

    @Column(name = "year")
    Integer year;

    @Enumerated(EnumType.STRING)
    @Column(name = "shift")
    ClassShift shift;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    LocalDateTime updatedAt;
}

