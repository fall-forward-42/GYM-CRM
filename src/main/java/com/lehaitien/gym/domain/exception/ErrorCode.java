package com.lehaitien.gym.domain.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.Getter;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Uncategorized error", HttpStatus.BAD_REQUEST),
    USER_EXISTED(1002, "User existed", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1003, "Username must be at least {min} characters", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1004, "Password must be at least {min} characters", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1005, "User not existed", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1006, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1007, "You do not have permission", HttpStatus.FORBIDDEN),
    INVALID_DOB(1008, "Your age must be at least {min}", HttpStatus.BAD_REQUEST),
    ROLE_NOT_VALID(1009, "Roles is not valid", HttpStatus.BAD_REQUEST),
    ROLE_NOT_FOUND(1010, "Roles is not found", HttpStatus.BAD_REQUEST),
    COACH_NOT_EXISTED(1011, "Coach not existed", HttpStatus.NOT_FOUND),
    IMAGE_NOT_FOUND(1011, "Image not existed", HttpStatus.NOT_FOUND),



    //Branch
    BRANCH_EXISTED(1011, "Branch is existed", HttpStatus.BAD_REQUEST),
    BRANCH_NOT_FOUND(1011, "Branch is not found", HttpStatus.NOT_FOUND),
    USER_ALREADY_IN_BRANCH(1011, "User is already in this branch", HttpStatus.BAD_REQUEST),
    USER_NOT_IN_BRANCH(1011, "User is not part of this branch", HttpStatus.BAD_REQUEST),
    //Facility
    FACILITY_EXISTED(1011, "Facility is existed", HttpStatus.BAD_REQUEST),
    FACILITY_NOT_FOUND(1011, "Facility is not found", HttpStatus.NOT_FOUND),
    ;






    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
}