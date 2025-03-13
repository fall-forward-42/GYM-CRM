package com.lehaitien.gym.domain.repository;

import com.lehaitien.gym.domain.model.Branch.BranchFacility;
import com.lehaitien.gym.domain.model.Branch.BranchFacilityImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BranchFacilityImageRepository extends JpaRepository<BranchFacilityImage, Long> {
    List<BranchFacilityImage> findByBranchFacility(BranchFacility facility);
    void deleteByBranchFacility(BranchFacility facility);
   void  deleteByBranchFacility_FacilityId(String branchFacility_facilityId);

}