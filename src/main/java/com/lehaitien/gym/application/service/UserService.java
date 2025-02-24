package com.lehaitien.gym.application.service;


import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.lehaitien.gym.application.dto.request.User.UserCreationRequest;
import com.lehaitien.gym.application.dto.request.User.UserUpdateRequest;
import com.lehaitien.gym.application.dto.response.User.UserResponse;
import com.lehaitien.gym.domain.constant.PredefinedRole;
import com.lehaitien.gym.domain.constant.UserStatus;
import com.lehaitien.gym.domain.exception.AppException;
import com.lehaitien.gym.domain.exception.ErrorCode;
import com.lehaitien.gym.domain.mapper.UserMapper;
import com.lehaitien.gym.domain.model.Authentication.Role;
import com.lehaitien.gym.domain.model.User.User;
import com.lehaitien.gym.domain.repository.RoleRepository;
import com.lehaitien.gym.domain.repository.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {
    UserRepository userRepository;
    RoleRepository roleRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;


    @Transactional
    public UserResponse createUser(UserCreationRequest request,String predefinedRole) {
        if (userRepository.existsByUsername(request.getUsername()) || userRepository.existsByEmail(request.getEmail())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        User user = userMapper.toUser(request);


        user.setPassword(passwordEncoder.encode(request.getPassword()));


        //Kiểm tra tính hợp lệ của role truyền vào
        if (!List.of(
                PredefinedRole.USER_ROLE,
                PredefinedRole.ADMIN_ROLE,
                PredefinedRole.COACH_ROLE,
                PredefinedRole.CLIENT_ROLE
        ).contains(predefinedRole)) {
            throw new AppException(ErrorCode.ROLE_NOT_VALID);
        }

        Role role = roleRepository.findByName(predefinedRole)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

        user.setRoles(Set.of(role));


        // Xử lý Coach quản lý Client (nếu user là CLIENT)
        if (PredefinedRole.CLIENT_ROLE.equals(predefinedRole) && request.getCoachId() != null) {
            User coach = userRepository.findById(request.getCoachId())
                    .orElseThrow(() -> new AppException(ErrorCode.COACH_NOT_EXISTED));
            user.setCoach(coach);
        }


        user.setStatus(UserStatus.ACTIVE);

        return userMapper.toUserResponse(userRepository.save(user));
    }



    @Transactional(readOnly = true)
    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        log.info("getMyInfo-name: "+name);
        User user = userRepository.findByUsername(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return userMapper.toUserResponse(user);
    }
    @Transactional
    @PostAuthorize("returnObject.username == authentication.name")
    public UserResponse updateUser(String userId, UserUpdateRequest request) {

        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        userMapper.updateUser(user, request);

        // Update Password nếu có
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        // Update roles nếu có
        if (request.getRoles() != null && !request.getRoles().isEmpty()) {
            Set<Role> roles = new HashSet<>(roleRepository.findByNameIn(request.getRoles()));
            user.setRoles(roles);
        }

        // Update coach nếu có
        if (request.getCoachId() != null) {
            User coach = userRepository.findById(request.getCoachId())
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
            user.setCoach(coach);
        }

//        user.setPassword(passwordEncoder.encode(request.getPassword()));
//        var roles = roleRepository.findAllById(request.getRoles());
//        user.setRoles(new HashSet<>(roles));

        return userMapper.toUserResponse(userRepository.save(user));
    }
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(String userId) {
        if (!userRepository.existsById(userId)) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }
        userRepository.deleteById(userId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional(readOnly = true)
    public List<UserResponse> getUsers() {
        log.info("In method get Users");
        return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
    }
    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse getUser(String id) {
        return userMapper.toUserResponse(
                userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
    }
}