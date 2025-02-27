package com.lehaitien.gym.domain.repository;


import com.lehaitien.gym.domain.model.Branch.Branch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BranchRepository extends JpaRepository<Branch, String> {
    boolean existsByBranchName(String branchName);
    Optional<Branch> findByBranchName(String branchName);
}
