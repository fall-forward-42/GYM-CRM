package com.lehaitien.gym.presentation.controller;

import com.lehaitien.gym.application.dto.request.ApiResponse;
import com.lehaitien.gym.application.dto.request.Schedule.ClassScheduleRequest;
import com.lehaitien.gym.application.dto.response.Schedule.ClassScheduleResponse;
import com.lehaitien.gym.application.service.ClassScheduleService;
import com.lehaitien.gym.domain.constant.ClassShift;
import com.lehaitien.gym.domain.constant.ClassStatus;
import com.lehaitien.gym.domain.constant.ClassType;
import com.lehaitien.gym.domain.constant.WeekMode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/class-schedules")
@RequiredArgsConstructor
@Tag(name = "Class Schedule Management", description = "CRUD & Automation for class schedules")
public class ClassScheduleController {

    private final ClassScheduleService classScheduleService;

    @GetMapping("/my-classes")
    public ApiResponse<List<ClassScheduleResponse>> getMyJoinedClasses(@RequestParam String userId) {
        return ApiResponse.<List<ClassScheduleResponse>>builder()
                .result(classScheduleService.getClassSchedulesByUser(userId))
                .build();
    }

    @PostMapping("/{classScheduleId}/join")

    public ApiResponse<String> joinClassSchedule(@PathVariable String classScheduleId, @RequestParam String userId) {
        classScheduleService.joinClassSchedule(classScheduleId, userId);
        return ApiResponse.<String>builder()
                .result("Joined class successfully")
                .build();
    }

    @PostMapping("/{classScheduleId}/cancel")
    public ApiResponse<String> cancelClassSchedule(@PathVariable String classScheduleId, @RequestParam String userId) {
        classScheduleService.cancelClassSchedule(classScheduleId, userId);
        return ApiResponse.<String>builder()
                .result("Canceled class successfully")
                .build();
    }

    @PostMapping("/fixed")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Tự động tạo lịch học cố định theo tuần, ca học và khoảng thời gian")
    public ApiResponse<List<ClassScheduleResponse>> createFixedSchedule(
            @RequestParam ClassType classType,
            @RequestParam ClassShift shift,
            @RequestParam WeekMode weekMode,
            @Valid @RequestBody ClassScheduleRequest request) {
        request.setClassType(classType);
        request.setShift(shift);
        request.setWeekMode(weekMode);

        return ApiResponse.<List<ClassScheduleResponse>>builder()
                .result(classScheduleService.createFixedSchedule(request))
                .build();
    }

    @GetMapping("/{classScheduleId}")
    @Operation(summary = "Lấy chi tiết một lịch học theo ID")
    public ApiResponse<ClassScheduleResponse> getSchedule(@PathVariable String classScheduleId) {
        return ApiResponse.<ClassScheduleResponse>builder()
                .result(classScheduleService.getClassScheduleById(classScheduleId))
                .build();
    }

    @GetMapping("/branch/{branchId}")
    @Operation(summary = "Lấy danh sách lịch học của một chi nhánh cụ thể")
    public ApiResponse<List<ClassScheduleResponse>> getSchedulesByBranch(@PathVariable String branchId) {
        return ApiResponse.<List<ClassScheduleResponse>>builder()
                .result(classScheduleService.getClassSchedulesByBranch(branchId))
                .build();
    }

    @GetMapping("")
    @Operation(summary = "Lấy tất cả lịch học")
    public ApiResponse<List<ClassScheduleResponse>> getAllSchedules() {
        return ApiResponse.<List<ClassScheduleResponse>>builder()
                .result(classScheduleService.getAllClassSchedules())
                .build();
    }

    @DeleteMapping("/{classScheduleId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Xoá một lịch học theo ID")
    public ApiResponse<String> deleteSchedule(@PathVariable String classScheduleId) {
        classScheduleService.deleteClassSchedule(classScheduleId);
        return ApiResponse.<String>builder()
                .result("Class schedule deleted successfully")
                .build();
    }

    @GetMapping("/filter")
    @Operation(summary = "Lọc lịch học theo nhiều tiêu chí thông qua query params (không phân trang)")
    public ApiResponse<List<ClassScheduleResponse>> filterSchedules(
            @RequestParam(required = false) String branchId,
            @RequestParam(required = false) String facilityId,
            @RequestParam(required = false) String coachId,
            @RequestParam(required = false) Integer weekOfYear,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) ClassShift shift,
            @RequestParam(required = false) ClassStatus status,
            @RequestParam(required = false) ClassType classType
    ) {
        List<ClassScheduleResponse> result = classScheduleService.filterClassSchedules(
                branchId, facilityId, coachId, weekOfYear, month, year, shift, status, classType
        );

        return ApiResponse.<List<ClassScheduleResponse>>builder()
                .result(result)
                .build();
    }
}

