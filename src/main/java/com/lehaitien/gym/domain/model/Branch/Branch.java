package com.lehaitien.gym.domain.model.Branch;


import com.lehaitien.gym.domain.constant.BranchStatus;
import com.lehaitien.gym.domain.model.User.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "branch", indexes = {
        @Index(name = "idx_branch_name", columnList = "branch_name")
})
public class Branch {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "branch_id", columnDefinition = "varchar(36)")
    String branchId;

    @Column(name = "branch_name", nullable = false, unique = true)
    String branchName;

    @Column(name = "address", columnDefinition = "TEXT")
    String address;

    @Column(name = "phone", length = 15)
    String phone;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    BranchStatus status;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    LocalDateTime updatedAt;

    @OneToMany(mappedBy = "branch", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    Set<User> users;
}
