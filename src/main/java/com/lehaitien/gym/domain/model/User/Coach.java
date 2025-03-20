package com.lehaitien.gym.domain.model.User;

import com.lehaitien.gym.domain.model.Branch.Branch;
import com.lehaitien.gym.domain.model.User.User;
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
@Table(name = "coach")
public class Coach {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "coach_id", columnDefinition = "varchar(36)")
    String coachId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false, unique = true)
    User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", referencedColumnName = "branch_id", nullable = false)
    Branch branch;

    @Column(name = "salary", nullable = false)
    Integer salary;

    @Column(name = "specialization")
    String specialization; // Ví dụ: Yoga, Gym, Boxing,...

    @Column(name = "experience_years")
    Integer experienceYears; // Số năm kinh nghiệm

    @Column(name = "certifications")
    String certifications; // Bằng cấp, chứng chỉ

    @CreationTimestamp
    LocalDateTime createdAt;

    @UpdateTimestamp
    LocalDateTime updatedAt;
}
