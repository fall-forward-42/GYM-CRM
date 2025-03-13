package com.lehaitien.gym.domain.repository;

import com.lehaitien.gym.domain.model.Branch.BranchFacility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BranchFacilityRepository extends JpaRepository<BranchFacility, String> {
    List<BranchFacility> findByBranch_BranchId(String branchId);
    boolean existsByFacilityName(String facilityName);
}

