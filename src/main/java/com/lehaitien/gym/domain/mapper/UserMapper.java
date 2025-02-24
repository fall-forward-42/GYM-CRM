package com.lehaitien.gym.domain.mapper;

import com.lehaitien.gym.application.dto.request.User.UserCreationRequest;
import com.lehaitien.gym.application.dto.request.User.UserUpdateRequest;
import com.lehaitien.gym.application.dto.response.User.UserResponse;
import com.lehaitien.gym.domain.model.Authentication.Permission;
import com.lehaitien.gym.domain.model.Authentication.Role;
import com.lehaitien.gym.domain.model.User.User;
import org.mapstruct.*;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;


@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {
    // Chuyển đổi từ CreationRequest sang Entity, ignore các trường xử lý riêng
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "password", ignore = true) // xử lý riêng vì phải encode
    @Mapping(target = "roles", ignore = true) // xử lý ánh xạ riêng từ service
    @Mapping(target = "coach", ignore = true) // xử lý riêng trong service
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    User toUser(UserCreationRequest request);

    // Mapping từ User sang UserResponse (để trả về client)
    @Mapping(target = "roles", source = "roles", qualifiedByName = "rolesToStrings")
    @Mapping(target = "coachId", source = "coach.userId")
    UserResponse toUserResponse(User user);

    // Cập nhật entity từ UpdateRequest, bỏ qua trường xử lý đặc biệt
    @Mapping(target = "password", ignore = true) // encode riêng trong service
    @Mapping(target = "roles", ignore = true) // xử lý trong service
    @Mapping(target = "coach", ignore = true) // xử lý riêng trong service
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest request);

    // Helper method để map roles Set<Role> -> Set<String>
    @Named("rolesToStrings")
    default Set<String> rolesToStrings(Set<Role> roles) {
        return roles == null ? null : roles.stream()
                .map(Role::getName)
                .collect(java.util.stream.Collectors.toSet());
    }

}