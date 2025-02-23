package com.lehaitien.gym.domain.mapper;

import com.lehaitien.gym.application.dto.request.PermissionRequest;
import com.lehaitien.gym.application.dto.response.PermissionResponse;
import com.lehaitien.gym.domain.model.Authentication.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);

    PermissionResponse toPermissionResponse(Permission permission);
}