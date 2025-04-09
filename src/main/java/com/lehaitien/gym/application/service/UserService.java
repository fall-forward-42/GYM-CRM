package com.lehaitien.gym.application.service;


import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.lehaitien.gym.application.dto.request.User.CoachRequest;
import com.lehaitien.gym.application.dto.request.User.UserCreationRequest;
import com.lehaitien.gym.application.dto.request.User.UserUpdateRequest;
import com.lehaitien.gym.application.dto.response.User.UserResponse;
import com.lehaitien.gym.domain.constant.PredefinedRole;
import com.lehaitien.gym.domain.constant.UserStatus;
import com.lehaitien.gym.domain.exception.AppException;
import com.lehaitien.gym.domain.exception.ErrorCode;
import com.lehaitien.gym.domain.mapper.UserMapper;
import com.lehaitien.gym.domain.model.Authentication.Role;
import com.lehaitien.gym.domain.model.Branch.Branch;
import com.lehaitien.gym.domain.model.User.Coach;
import com.lehaitien.gym.domain.model.User.User;
import com.lehaitien.gym.domain.repository.BranchRepository;
import com.lehaitien.gym.domain.repository.CoachRepository;
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
    CoachRepository coachRepository;
    BranchRepository branchRepository;

    @Transactional
    public Coach saveCoach(User user, Integer salary, String specialization, Integer experienceYears, String certifications) {
        Coach coach = Coach.builder()
                .user(user)
                .branch(user.getBranch())
                .salary(salary != null ? salary : 5000000)
                .specialization(specialization)
                .experienceYears(experienceYears)
                .certifications(certifications)
                .build();

        return coachRepository.save(coach);
    }



    @Transactional
    public UserResponse createUser(UserCreationRequest request) {
        if (userRepository.existsByUsername(request.getUsername())  ) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
//        if(userRepository.existsByEmail(request.getEmail())){
//            throw new AppException(ErrorCode.EMAIL_EXISTED);
//        }
//        if(userRepository.existsByEmail(request.getCoachId())){
//            throw new AppException(ErrorCode.COACH_NOT_FOUND);
//        }
        Branch branch = branchRepository.findById(request.getBranchId())
                .orElseThrow(() -> new AppException(ErrorCode.BRANCH_NOT_FOUND));

        //Handle hash password
        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setStatus(UserStatus.ACTIVE);
        user.setBranch(branch);
        user.setBalance(0);


        //Kiểm tra tính hợp lệ của role truyền vào
        String predefinedRole = request.getRoles().iterator().next();
        if (!List.of(
                PredefinedRole.USER_ROLE,
                PredefinedRole.ADMIN_ROLE,
                PredefinedRole.COACH_ROLE,
                PredefinedRole.CLIENT_ROLE
        ).contains(predefinedRole)) {
            throw new AppException(ErrorCode.ROLE_NOT_VALID);
        }
        // Tìm role theo tên và gán cho user
        Role role = roleRepository.findByName(predefinedRole)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
        user.setRoles(Set.of(role));


        // Nếu là COACH, tạo bản ghi trong bảng Coach
        user = userRepository.save(user);

//        // Nếu user là COACH, tạo thông tin bổ sung cho bảng Coach
//        if (PredefinedRole.COACH_ROLE.equals(predefinedRole)) {
//
//            saveCoach(user, request.getSalary(), request.getSpecialization(), request.getExperienceYears(), request.getCertifications());
//        }

        return userMapper.toUserResponse(user);
    }

    @Transactional
    public UserResponse createCoach(CoachRequest request) {
        if (userRepository.existsByUsername(request.getUsername())  ) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
//        if(userRepository.existsByEmail(request.getEmail())){
//            throw new AppException(ErrorCode.EMAIL_EXISTED);
//        }
        Branch branch = branchRepository.findById(request.getBranchId())
                .orElseThrow(() -> new AppException(ErrorCode.BRANCH_NOT_FOUND));

        //Handle hash password
        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setStatus(UserStatus.ACTIVE);
        user.setBranch(branch);
        user.setBalance(0);



        //Kiểm tra tính hợp lệ của role truyền vào
        String predefinedRole = request.getRoles().iterator().next();
        if (!List.of(
                PredefinedRole.USER_ROLE,
                PredefinedRole.ADMIN_ROLE,
                PredefinedRole.COACH_ROLE,
                PredefinedRole.CLIENT_ROLE
        ).contains(predefinedRole)) {
            throw new AppException(ErrorCode.ROLE_NOT_VALID);
        }
        // Tìm role theo tên và gán cho user
        Role role = roleRepository.findByName(predefinedRole)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
        user.setRoles(Set.of(role));


        // Nếu là COACH, tạo bản ghi trong bảng Coach
        user = userRepository.save(user);

        // Nếu user là COACH, tạo thông tin bổ sung cho bảng Coach
        if (PredefinedRole.COACH_ROLE.equals(predefinedRole)) {

            saveCoach(user, request.getSalary(), request.getSpecialization(), request.getExperienceYears(), request.getCertifications());
        }

        return userMapper.toUserResponse(user);
    }

    @Transactional(readOnly = true)
    public List<UserResponse> getUsersByRole(String roleName) {
        List<User> users = userRepository.findAll(); // Lấy toàn bộ user
        List<User> filteredUsers = users.stream()
                .filter(user -> user.getRoles().stream()
                        .anyMatch(role -> role.getName().equalsIgnoreCase(roleName)))
                .toList();

        return filteredUsers.stream()
                .map(userMapper::toUserResponse)
                .toList();
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

        // Update Coach nếu có
        if (user.isCoach()) {
            Coach coach = coachRepository.findByUser(user)
                    .orElseThrow(() -> new AppException(ErrorCode.COACH_NOT_EXISTED));

            if (request.getSalary() != null) {
                coach.setSalary(request.getSalary());
            }
            if (request.getSpecialization() != null) {
                coach.setSpecialization(request.getSpecialization());
            }
            if (request.getExperienceYears() != null) {
                coach.setExperienceYears(request.getExperienceYears());
            }
            if (request.getCertifications() != null) {
                coach.setCertifications(request.getCertifications());
            }

            coachRepository.save(coach);
        }

//        user.setPassword(passwordEncoder.encode(request.getPassword()));
//        var roles = roleRepository.findAllById(request.getRoles());
//        user.setRoles(new HashSet<>(roles));

        return userMapper.toUserResponse(userRepository.save(user));
    }
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // Nếu là Coach, xóa bản ghi trong bảng Coach trước
        if (user.isCoach()) {
            coachRepository.deleteByUser(user);
        }

        userRepository.delete(user);
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