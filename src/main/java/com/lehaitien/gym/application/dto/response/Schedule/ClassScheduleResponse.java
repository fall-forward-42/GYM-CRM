package com.lehaitien.gym.application.dto.response.Schedule;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ClassScheduleResponse {

    private String classScheduleId;

    private String branchId;
    private String teacherId;
    private String facilityId;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private String status;
    private String classType;

    private int weekOfYear;
    private int month;
    private int year;
    private String shift;
}
