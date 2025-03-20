package com.lehaitien.gym.domain.repository;


import com.lehaitien.gym.domain.model.User.Coach;
import com.lehaitien.gym.domain.model.User.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CoachRepository extends JpaRepository<Coach, String> {
    Optional<Coach> findByUser(User user);
    void deleteByUser(User user);
}
