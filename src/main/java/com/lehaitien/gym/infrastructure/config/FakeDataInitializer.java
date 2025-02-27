package com.lehaitien.gym.infrastructure.config;


import com.lehaitien.gym.domain.constant.BranchStatus;
import com.lehaitien.gym.domain.constant.UserStatus;
import com.lehaitien.gym.domain.model.Authentication.Role;
import com.lehaitien.gym.domain.model.Branch.Branch;
import com.lehaitien.gym.domain.model.User.User;
import com.lehaitien.gym.domain.repository.BranchRepository;
import com.lehaitien.gym.domain.repository.RoleRepository;
import com.lehaitien.gym.domain.repository.UserRepository;
import net.datafaker.Faker;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
    public ApplicationRunner initFakeData(UserRepository userRepository, RoleRepository roleRepository,
                                          BranchRepository branchRepository) {
        return args -> {
            if (branchRepository.count() <= 3) {
                List<Branch> branches = List.of(
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
                branchRepository.saveAll(branches);
                log.info("✅ Fake branches created.");
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
            }
        };
    }
}
