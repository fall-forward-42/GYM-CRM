package com.lehaitien.gym.application.dto.request.Schedule;


import com.lehaitien.gym.domain.constant.ClassShift;
import com.lehaitien.gym.domain.constant.ClassType;
import com.lehaitien.gym.domain.constant.WeekMode;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ClassScheduleRequest {
    private ClassType classType; // Offline/Online
    private int numberOfSessionsPerWeek; // số buổi mỗi tuần
    private LocalDateTime startDate; // ngày bắt đầu tạo lịch
    private LocalDateTime endDate; // ngày kết thúc tạo lịch

    private String branchId;     // ID chi nhánh
    private String coachId;      // ID giáo viên
    private String facilityId;   // ID cơ sở vật chất

    private ClassShift shift;    // Ca học: MORNING, AFTERNOON, EVENING
    private WeekMode weekMode;    // EVEN hoặc ODD
}

