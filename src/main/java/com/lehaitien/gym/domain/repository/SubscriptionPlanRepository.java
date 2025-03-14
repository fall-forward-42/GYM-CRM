package com.lehaitien.gym.domain.repository;


import com.lehaitien.gym.domain.model.Subsription.SubscriptionPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubscriptionPlanRepository extends JpaRepository<SubscriptionPlan, String> {
    boolean existsByPlanName(String planName);
    Optional<SubscriptionPlan> findByPlanName(String planName);
}

