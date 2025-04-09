package com.lehaitien.gym.domain.mapper;

import com.lehaitien.gym.application.dto.request.Schedule.ClassScheduleRequest;
import com.lehaitien.gym.application.dto.response.Schedule.ClassScheduleResponse;
import com.lehaitien.gym.domain.model.Schedule.ClassSchedule;
import com.lehaitien.gym.domain.model.Branch.Branch;
import com.lehaitien.gym.domain.model.User.User;
import com.lehaitien.gym.domain.model.Branch.BranchFacility;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ClassScheduleMapper {

    ClassScheduleMapper INSTANCE = Mappers.getMapper(ClassScheduleMapper.class);

    // ✅ Entity → DTO
    @Mapping(source = "branch", target = "branchId", qualifiedByName = "mapBranchToId")
    @Mapping(source = "coach", target = "teacherId", qualifiedByName = "mapCoachToId")
    @Mapping(source = "branchFacility", target = "facilityId", qualifiedByName = "mapFacilityToId")
    @Mapping(source = "startTime", target = "startTime")
    @Mapping(source = "endTime", target = "endTime")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "classType", target = "classType")
    ClassScheduleResponse toClassScheduleResponse(ClassSchedule classSchedule);

    // ✅ DTO → Entity cơ bản (nếu dùng create trực tiếp)
    @Mapping(target = "classScheduleId", ignore = true)
    @Mapping(target = "branch", ignore = true)
    @Mapping(target = "coach", ignore = true)
    @Mapping(target = "branchFacility", ignore = true)
    @Mapping(target = "status", constant = "SCHEDULED")
    @Mapping(target = "weekOfYear", ignore = true)
    @Mapping(target = "month", ignore = true)
    @Mapping(target = "year", ignore = true)
    @Mapping(target = "shift", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    ClassSchedule toClassSchedule(ClassScheduleRequest request);

    // ✅ Update entity từ request
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateClassSchedule(@MappingTarget ClassSchedule classSchedule, ClassScheduleRequest request);

    // ===== Custom mappers =====

    @Named("mapBranchToId")
    default String mapBranchToId(Branch branch) {
        return branch != null ? branch.getBranchId() : null;
    }

    @Named("mapCoachToId")
    default String mapCoachToId(User user) {
        return user != null ? user.getUserId() : null;
    }

    @Named("mapFacilityToId")
    default String mapFacilityToId(BranchFacility facility) {
        return facility != null ? facility.getFacilityId() : null;
    }
}
