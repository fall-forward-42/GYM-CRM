package com.lehaitien.gym.domain.repository;


import com.lehaitien.gym.domain.constant.ClassStatus;
import com.lehaitien.gym.domain.model.Schedule.ClassSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassScheduleRepository extends JpaRepository<ClassSchedule, String>, JpaSpecificationExecutor<ClassSchedule> {
    List<ClassSchedule> findByBranch_BranchId(String branchId);

    List<ClassSchedule> findByBranch_BranchIdAndBranchFacility_FacilityIdAndWeekOfYearAndYear(
            String branchId, String facilityId, int weekOfYear, int year);

    List<ClassSchedule> findByBranch_BranchIdAndBranchFacility_FacilityIdAndMonthAndYear(
            String branchId, String facilityId, int month, int year);


    List<ClassSchedule> findByBranchFacility_FacilityNameAndStatus(String facilityName, ClassStatus status);
}
