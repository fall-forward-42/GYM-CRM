package com.lehaitien.gym.domain.mapper;

import com.lehaitien.gym.application.dto.request.User.UserCreationRequest;
import com.lehaitien.gym.application.dto.request.User.UserUpdateRequest;
import com.lehaitien.gym.application.dto.response.User.UserResponse;
import com.lehaitien.gym.domain.model.Authentication.Permission;
import com.lehaitien.gym.domain.model.Authentication.Role;
import com.lehaitien.gym.domain.model.User.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;


@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "roles", ignore = true)
    User toUser(UserCreationRequest request);

    UserResponse toUserResponse(User user);

    @Mapping(target = "roles", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest request);


//    default Set<Role> mapRoles(Set<String> roles) {
//        if (roles == null) {
//            return Collections.emptySet();
//        }
//        return roles.stream()
//                .map(name -> new Role(name, "", Collections.emptySet())) // Role có constructor cần permissions
//                .collect(Collectors.toSet());
//    }
}