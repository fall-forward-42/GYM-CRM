package com.lehaitien.gym.domain.model.User;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.lehaitien.gym.domain.constant.UserStatus;
import com.lehaitien.gym.domain.model.Authentication.Role;
import com.lehaitien.gym.domain.model.Branch.Branch;
import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "`user`", indexes = {
        @Index(columnList = "email", name = "idx_user_email", unique = true),
        @Index(name = "idx_user_userid", columnList = "user_id")
})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id", columnDefinition = "varchar(36)")
    String userId;

    @Column(name = "username", unique = true, nullable = false)
    String username;

    @Column(name = "full_name")
    String fullName;

    @Column(name = "email", unique = true, nullable = false)
    String email;

    String phone;
    String gender;
    String password;
    LocalDate dob;
    String cccd;
    String address;
    BigDecimal height;
    BigDecimal weight;
    String healthIssues;
    String imageUrl;

    @ManyToMany
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_user_id", columnDefinition = "varchar(36)"),
            inverseJoinColumns = @JoinColumn(name = "role_id", columnDefinition = "varchar(36)")
    )
    Set<Role> roles;

    @Enumerated(EnumType.STRING)
    UserStatus status;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "coach_id", referencedColumnName = "user_id", columnDefinition = "varchar(36)")
//    User coach;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    Coach coach;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "branch_id", referencedColumnName = "branch_id", columnDefinition = "varchar(36)")
    Branch branch;

    @Column(name = "balance")
    Integer balance = 0;


    @CreationTimestamp
    LocalDateTime createdAt;

    @UpdateTimestamp
    LocalDateTime updatedAt;

    public boolean isCoach() {
        return roles.stream().anyMatch(role -> "COACH".equalsIgnoreCase(role.getName()));
    }


}