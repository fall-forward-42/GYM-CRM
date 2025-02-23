package com.lehaitien.gym.domain.mapper;

import com.lehaitien.gym.application.dto.request.RoleRequest;
import com.lehaitien.gym.application.dto.response.RoleResponse;
import com.lehaitien.gym.domain.model.Authentication.Permission;
import com.lehaitien.gym.domain.model.Authentication.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;


@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);


    RoleResponse toRoleResponse(Role role);

    // Chuyển Set<String> sang Set<Permission>
//    default Set<Permission> mapPermissions(Set<String> permissions) {
//        if (permissions == null) {
//            return Collections.emptySet();
//        }
//        return permissions.stream()
//                .map(name -> new Permission(name, ""))
//                .collect(Collectors.toSet());
//    }
}