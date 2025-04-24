package com.lehaitien.gym.domain.repository;

import com.lehaitien.gym.domain.model.Schedule.ClassScheduleParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClassScheduleParticipantRepository extends JpaRepository<ClassScheduleParticipant, String> {

    List<ClassScheduleParticipant> findByUser_UserIdAndIsCanceledFalse(String userId);

    Optional<ClassScheduleParticipant> findByUser_UserIdAndClassSchedule_ClassScheduleId(String userId, String classScheduleId);

    boolean existsByUser_UserIdAndClassSchedule_ClassScheduleId(String userId, String classScheduleId);

    List<ClassScheduleParticipant> findByClassSchedule_ClassScheduleIdAndIsCanceledFalse(String classScheduleId);

}
