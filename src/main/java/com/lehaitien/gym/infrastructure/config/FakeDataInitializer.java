package com.lehaitien.gym.infrastructure.config;


import com.lehaitien.gym.application.dto.request.Branch.BranchFacilityRequest;
import com.lehaitien.gym.application.dto.response.Branch.BranchFacilityResponse;
import com.lehaitien.gym.application.service.BranchFacilityService;
import com.lehaitien.gym.domain.constant.BranchStatus;
import com.lehaitien.gym.domain.constant.FacilityStatus;
import com.lehaitien.gym.domain.constant.UserStatus;
import com.lehaitien.gym.domain.model.Authentication.Role;
import com.lehaitien.gym.domain.model.Branch.Branch;
import com.lehaitien.gym.domain.model.Branch.BranchFacility;
import com.lehaitien.gym.domain.model.User.User;
import com.lehaitien.gym.domain.repository.BranchFacilityRepository;
import com.lehaitien.gym.domain.repository.BranchRepository;
import com.lehaitien.gym.domain.repository.RoleRepository;
import com.lehaitien.gym.domain.repository.UserRepository;
import net.datafaker.Faker;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class FakeDataInitializer {

    private static final Faker faker = new Faker();
    private final PasswordEncoder passwordEncoder;

    @Bean
    public ApplicationRunner initFakeData(UserRepository userRepository,
                                          RoleRepository roleRepository,
                                          BranchRepository branchRepository,
                                          BranchFacilityRepository facilityRepository,
                                          BranchFacilityService facilityService) {
        return args -> {
            if (branchRepository.count() <= 3) {
                List<Branch> branchesToCreate  = List.of(
                        Branch.builder()
                                .branchName("Gym Quận 1")
                                .address("123 Nguyễn Trãi, Q1, HCM")
                                .phone("0123456789")
                                .status(BranchStatus.ACTIVE)
                                .users(Set.of()) // Đảm bảo tránh null
                                .build(),

                        Branch.builder()
                                .branchName("Gym Hà Đông")
                                .address("456 Trần Phú, Hà Đông, HN")
                                .phone("0987654321")
                                .status(BranchStatus.ACTIVE)
                                .users(Set.of())
                                .build(),

                        Branch.builder()
                                .branchName("Gym Biên Hòa")
                                .address("847/48/7, kp2, tổ 7 ")
                                .phone("09876543321")
                                .status(BranchStatus.ACTIVE)
                                .users(Set.of())
                                .build()
                );

                // **Lọc những branch chưa tồn tại trong database**
                List<Branch> newBranches = branchesToCreate.stream()
                        .filter(branch -> !branchRepository.existsByBranchName(branch.getBranchName()))
                        .toList();

                // **Chỉ lưu branch nếu có ít nhất 1 cái mới**
                if (!newBranches.isEmpty()) {
                    branchRepository.saveAll(newBranches);
                    log.info("✅ {} Fake branches created.", newBranches.size());
                } else {
                    log.info("✅ All fake branches already exist. No new branch created.");
                }
            }

            if (userRepository.count() <= 10) {
                Set<Role> clientRole = Set.of(roleRepository.findById("CLIENT").orElseThrow());
                List<Branch> branches = branchRepository.findAll();

                IntStream.range(1, 11).forEach(i -> {
                    Branch assignedBranch = branches.get(i % branches.size()); // Chia đều Users vào Branches

                    User user = User.builder()
                            .username(faker.name().username())
                            .fullName(faker.name().fullName())
                            .email(faker.internet().emailAddress())
                            .phone(faker.phoneNumber().cellPhone())
                            .gender(i % 2 == 0 ? "Male" : "Female")
                            .password(passwordEncoder.encode("123456")) // Mật khẩu mặc định
                            .dob(LocalDate.of(1990 + (i % 10), (i % 12) + 1, (i % 28) + 1))
                            .cccd(faker.number().digits(12))
                            .address(faker.address().fullAddress())
                            .height(BigDecimal.valueOf(faker.number().randomDouble(2, 1, 2)))
                            .weight(BigDecimal.valueOf(faker.number().randomDouble(2, 50, 100)))
                            .healthIssues(faker.medical().symptoms())
                            .roles(clientRole)
                            .status(UserStatus.ACTIVE)
                            .branch(assignedBranch)
                            .build();
                    userRepository.save(user);
                });
                log.info("✅ Fake users created.");


                // ✅ Thêm dữ liệu cho BranchFacility
//                if (facilityRepository.count() <= 10) {
//                    List<Branch> branchesForFacility = branchRepository.findAll();
//                    List<String> facilityNames = List.of("Phòng Cardio", "Khu Yoga", "Khu Boxing", "Phòng Gym", "Khu Xông Hơi");
//
//                    IntStream.range(1, 11).forEach(i -> {
//                        Branch assignedBranch = branchesForFacility.get(i % branchesForFacility.size());
//
//                        // 1️⃣ Tạo Facility
//                        BranchFacilityResponse facility = facilityService.createFacility(
//                                assignedBranch.getBranchId(),
//                                BranchFacilityRequest.builder()
//                                        .facilityName(facilityNames.get(i % facilityNames.size()) + " " + (i + 1))
//                                        .status(FacilityStatus.values()[i % FacilityStatus.values().length])
//                                        .capacity(faker.number().numberBetween(10, 50))
//                                        .build()
//                        );
//                    });
//                    log.info("✅ Fake branch facilities created.");
//                    }
            }
        };
    }
}
