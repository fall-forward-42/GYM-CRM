package com.lehaitien.gym.application.service;

import com.lehaitien.gym.application.dto.request.Schedule.ClassScheduleRequest;
import com.lehaitien.gym.application.dto.response.Schedule.ClassScheduleResponse;
import com.lehaitien.gym.domain.constant.ClassShift;
import com.lehaitien.gym.domain.constant.ClassStatus;
import com.lehaitien.gym.domain.constant.ClassType;
import com.lehaitien.gym.domain.constant.WeekMode;
import com.lehaitien.gym.domain.exception.AppException;
import com.lehaitien.gym.domain.exception.ErrorCode;
import com.lehaitien.gym.domain.mapper.ClassScheduleMapper;

import com.lehaitien.gym.domain.model.Branch.Branch;
import com.lehaitien.gym.domain.model.Branch.BranchFacility;
import com.lehaitien.gym.domain.model.Schedule.ClassSchedule;
import com.lehaitien.gym.domain.model.Schedule.ClassScheduleParticipant;
import com.lehaitien.gym.domain.model.User.User;
import com.lehaitien.gym.domain.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClassScheduleService {

    private final ClassScheduleRepository classScheduleRepository;
    private final BranchRepository branchRepository;
    private final UserRepository userRepository;
    private final BranchFacilityRepository branchFacilityRepository;
    private final ClassScheduleMapper classScheduleMapper;
    private final ClassScheduleParticipantRepository classScheduleParticipantRepository;

    private List<DayOfWeek> getDaysForWeek(int numberOfSessions, WeekMode weekMode) {
        List<DayOfWeek> days = weekMode == WeekMode.EVEN
                ? List.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY)     // 2, 4, 6
                : List.of(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY, DayOfWeek.SATURDAY);  // 3, 5, 7

        // Trả về subList theo số buổi yêu cầu
        return days.subList(0, Math.min(numberOfSessions, days.size()));
    }

    private LocalDateTime getShiftStartTime(LocalDate date, ClassShift shift) {
        return switch (shift) {
            case MORNING -> date.atTime(6, 0);
            case AFTERNOON -> date.atTime(14, 0);
            case EVENING -> date.atTime(18, 0);
        };
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public List<ClassScheduleResponse> createFixedSchedule(ClassScheduleRequest request) {
        Branch branch = branchRepository.findById(request.getBranchId())
                .orElseThrow(() -> new AppException(ErrorCode.BRANCH_NOT_FOUND));

        User coach = userRepository.findById(request.getCoachId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        BranchFacility facility = branchFacilityRepository.findById(request.getFacilityId())
                .orElseThrow(() -> new AppException(ErrorCode.FACILITY_NOT_FOUND));

        //int sessionsPerWeek = request.getNumberOfSessionsPerWeek();

        List<DayOfWeek> selectedDays = getDaysForWeek(
                request.getNumberOfSessionsPerWeek(),
                request.getWeekMode() != null ? request.getWeekMode() : WeekMode.EVEN // fallback mặc định EVEN
        );

        List<ClassScheduleResponse> results = new ArrayList<>();

        // Duyệt từng tuần từ start đến end
        LocalDate current = request.getStartDate().toLocalDate();
        LocalDate end = request.getEndDate().toLocalDate();

        while (!current.isAfter(end)) {
            for (DayOfWeek day : selectedDays) {
                LocalDate sessionDate = current.with(day);
                if (sessionDate.isBefore(request.getStartDate().toLocalDate()) || sessionDate.isAfter(end)) continue;

                LocalDateTime startTime = getShiftStartTime(sessionDate, request.getShift());
                LocalDateTime endTime = startTime.plusMinutes(90); // mỗi lớp 90 phút

                int weekOfYear = startTime.get(WeekFields.ISO.weekOfWeekBasedYear());
                int month = startTime.getMonthValue();
                int year = startTime.getYear();

                ClassSchedule schedule = ClassSchedule.builder()
                        .branch(branch)
                        .coach(coach)
                        .branchFacility(facility)
                        .startTime(startTime)
                        .endTime(endTime)
                        .status(ClassStatus.SCHEDULED)
                        .classType(request.getClassType())
                        .shift(request.getShift())
                        .weekOfYear(weekOfYear)
                        .month(month)
                        .year(year)
                        .build();

                schedule = classScheduleRepository.save(schedule);
                results.add(classScheduleMapper.toClassScheduleResponse(schedule));
            }

            current = current.plusWeeks(1);
        }

        return results;
    }


    @Transactional(readOnly = true)
    public List<ClassScheduleResponse> getClassSchedulesByUser(String userId) {
        List<ClassScheduleParticipant> participants =
                classScheduleParticipantRepository.findByUser_UserIdAndIsCanceledFalse(userId);

        return participants.stream()
                .map(participant -> classScheduleMapper.toClassScheduleResponse(participant.getClassSchedule()))
                .toList();
    }

    @Transactional
    public void joinClassSchedule(String classScheduleId, String userId) {
        ClassSchedule classSchedule = classScheduleRepository.findById(classScheduleId)
                .orElseThrow(() -> new AppException(ErrorCode.CLASS_SCHEDULE_NOT_FOUND));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        boolean alreadyJoined = classScheduleParticipantRepository
                .existsByUser_UserIdAndClassSchedule_ClassScheduleId(userId, classScheduleId);

        if (alreadyJoined) {
            throw new AppException(ErrorCode.DUPLICATE_ENTRY);
        }

        ClassScheduleParticipant participant = ClassScheduleParticipant.builder()
                .user(user)
                .classSchedule(classSchedule)
                .isCanceled(false)
                .build();

        classScheduleParticipantRepository.save(participant);
        log.info("User {} joined class schedule {}", userId, classScheduleId);
    }

    @Transactional
    public void cancelClassSchedule(String classScheduleId, String userId) {
        ClassScheduleParticipant participant = classScheduleParticipantRepository
                .findByUser_UserIdAndClassSchedule_ClassScheduleId(userId, classScheduleId)
                .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND));
        participant.setIsCanceled(true);
        classScheduleParticipantRepository.save(participant);

        log.info("User {} canceled class schedule {}", userId, classScheduleId);
    }

    // Lấy thông tin lịch học theo ID
    @Transactional(readOnly = true)
    public ClassScheduleResponse getClassScheduleById(String classScheduleId) {
        ClassSchedule classSchedule = classScheduleRepository.findById(classScheduleId)
                .orElseThrow(() -> new AppException(ErrorCode.CLASS_SCHEDULE_NOT_FOUND));

        return classScheduleMapper.toClassScheduleResponse(classSchedule);
    }

    // Lấy tất cả lịch học của một chi nhánh
    @Transactional(readOnly = true)
    public List<ClassScheduleResponse> getClassSchedulesByBranch(String branchId) {
        return classScheduleRepository.findByBranch_BranchId(branchId).stream()
                .map(classScheduleMapper::toClassScheduleResponse)
                .toList();
    }

    // Lấy tất cả lịch học trong hệ thống
    @Transactional(readOnly = true)
    public List<ClassScheduleResponse> getAllClassSchedules() {
        return classScheduleRepository.findAll().stream()
                .map(classScheduleMapper::toClassScheduleResponse)
                .toList();
    }

    // Cập nhật lịch học
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public ClassScheduleResponse updateClassSchedule(String classScheduleId, ClassScheduleRequest request) {
        ClassSchedule classSchedule = classScheduleRepository.findById(classScheduleId)
                .orElseThrow(() -> new AppException(ErrorCode.CLASS_SCHEDULE_NOT_FOUND));

        // Cập nhật thông tin lịch học từ request
        classScheduleMapper.updateClassSchedule(classSchedule, request);
        classSchedule = classScheduleRepository.save(classSchedule);

        log.info("Class schedule updated successfully: {}", classSchedule.getClassScheduleId());

        // Trả về ClassScheduleResponse
        return classScheduleMapper.toClassScheduleResponse(classSchedule);
    }

    // Xóa lịch học
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteClassSchedule(String classScheduleId) {
        if (!classScheduleRepository.existsById(classScheduleId)) {
            throw new AppException(ErrorCode.CLASS_SCHEDULE_NOT_FOUND);
        }

        classScheduleRepository.deleteById(classScheduleId);

        log.info("Class schedule deleted: {}", classScheduleId);
    }

    @Transactional(readOnly = true)
    public List<ClassScheduleResponse> getSchedulesByWeek(String branchId, String facilityId, int weekOfYear, int year) {
        return classScheduleRepository
                .findByBranch_BranchIdAndBranchFacility_FacilityIdAndWeekOfYearAndYear(branchId, facilityId, weekOfYear, year)
                .stream()
                .map(classScheduleMapper::toClassScheduleResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ClassScheduleResponse> getSchedulesByMonth(String branchId, String facilityId, int month, int year) {
        return classScheduleRepository
                .findByBranch_BranchIdAndBranchFacility_FacilityIdAndMonthAndYear(branchId, facilityId, month, year)
                .stream()
                .map(classScheduleMapper::toClassScheduleResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ClassScheduleResponse> filterClassSchedules(
            String branchId,
            String facilityId,
            String coachId,
            Integer weekOfYear,
            Integer month,
            Integer year,
            ClassShift shift,
            ClassStatus status,
            ClassType classType
    ) {
        Specification<ClassSchedule> spec = Specification.where(null);

        if (branchId != null)
            spec = spec.and((root, query, cb) -> cb.equal(root.get("branch").get("branchId"), branchId));

        if (facilityId != null)
            spec = spec.and((root, query, cb) -> cb.equal(root.get("branchFacility").get("facilityId"), facilityId));

        if (coachId != null)
            spec = spec.and((root, query, cb) -> cb.equal(root.get("coach").get("userId"), coachId));

        if (weekOfYear != null)
            spec = spec.and((root, query, cb) -> cb.equal(root.get("weekOfYear"), weekOfYear));

        if (month != null)
            spec = spec.and((root, query, cb) -> cb.equal(root.get("month"), month));

        if (year != null)
            spec = spec.and((root, query, cb) -> cb.equal(root.get("year"), year));

        if (shift != null)
            spec = spec.and((root, query, cb) -> cb.equal(root.get("shift"), shift));

        if (status != null)
            spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), status));

        if (classType != null)
            spec = spec.and((root, query, cb) -> cb.equal(root.get("classType"), classType));

        return classScheduleRepository.findAll(spec, Sort.by(Sort.Direction.ASC, "startTime")).stream()
                .map(classScheduleMapper::toClassScheduleResponse)
                .toList();
    }

}
