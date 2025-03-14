package com.lehaitien.gym.infrastructure.config;


import com.lehaitien.gym.application.dto.request.Branch.BranchFacilityRequest;
import com.lehaitien.gym.application.dto.response.Branch.BranchFacilityResponse;
import com.lehaitien.gym.application.service.BranchFacilityService;
import com.lehaitien.gym.domain.constant.BranchStatus;
import com.lehaitien.gym.domain.constant.FacilityStatus;
import com.lehaitien.gym.domain.constant.SubscriptionStatus;
import com.lehaitien.gym.domain.constant.UserStatus;
import com.lehaitien.gym.domain.model.Authentication.Role;
import com.lehaitien.gym.domain.model.Branch.Branch;
import com.lehaitien.gym.domain.model.Branch.BranchFacility;
import com.lehaitien.gym.domain.model.Subsription.SubscriptionPlan;
import com.lehaitien.gym.domain.model.Subsription.UserSubscription;
import com.lehaitien.gym.domain.model.User.User;
import com.lehaitien.gym.domain.repository.*;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${app.seed-data:true}")
    private boolean seedDataEnabled;

    @Bean
    public ApplicationRunner initFakeData(UserRepository userRepository,
                                          RoleRepository roleRepository,
                                          BranchRepository branchRepository,
                                          BranchFacilityRepository facilityRepository,
                                          SubscriptionPlanRepository subscriptionPlanRepository,
                                          UserSubscriptionRepository userSubscriptionRepository
                                         ) {
        return args -> {
            if (!seedDataEnabled) {
                log.info(" Seed data is disabled. Skipping fake data initialization.");
                return;
            }

            log.info("Seeding fake data...");

            if (branchRepository.count() <= 2) {
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

            List<Branch> branches = branchRepository.findAll();

            if (userRepository.count() <= 10) {
                Set<Role> clientRole = Set.of(roleRepository.findById("CLIENT").orElseThrow());


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

            //**Tạo Branch Facilities**
            if (facilityRepository.count() < 10) {
                List<String> facilityNames = List.of("Yoga", "Boxing", "Gym", "Cardio", "Sauna");

                IntStream.range(1, 11).forEach(i -> {
                    Branch assignedBranch = branches.get(i % branches.size());

                    String facilityName = facilityNames.get(i % facilityNames.size()) + " " + (i + 1);


//                    // Kiểm tra Facility đã tồn tại chưa
//                    if (facilityRepository.existsByFacilityName(facilityName)) {
//                        return; // Nếu tồn tại thì bỏ qua
//                    }

                    // Tạo mới Facility và lưu vào DB
                    BranchFacility facility = BranchFacility.builder()
                            .facilityName(facilityName)
                            .status(FacilityStatus.values()[i % FacilityStatus.values().length])
                            .capacity(faker.number().numberBetween(10, 50))
                            .branch(assignedBranch)  // Gán Branch cho Facility
                            .build();

                    facilityRepository.save(facility);
                });

                log.info("✅ Fake facilities created.");
            }

            // **Tạo Subscription Plans (Gói tập)**
            if (subscriptionPlanRepository.count() < 5) {
                List<SubscriptionPlan> plansToCreate = List.of(
                        SubscriptionPlan.builder()
                                .planName("Gói 1 tháng")
                                .duration(30)
                                .price(500000)
                                .description("Gói tập 1 tháng, sử dụng không giới hạn.")
                                .build(),

                        SubscriptionPlan.builder()
                                .planName("Gói 3 tháng")
                                .duration(90)
                                .price(1400000)
                                .description("Gói tập 3 tháng, tiết kiệm hơn so với gói tháng.")
                                .build(),

                        SubscriptionPlan.builder()
                                .planName("Gói 6 tháng")
                                .duration(180)
                                .price(2700000)
                                .description("Gói tập 6 tháng, ưu đãi tốt hơn.")
                                .build(),

                        SubscriptionPlan.builder()
                                .planName("Gói 12 tháng")
                                .duration(365)
                                .price(5000000)
                                .description("Gói tập 1 năm, rẻ nhất trên mỗi tháng.")
                                .build(),

                        SubscriptionPlan.builder()
                                .planName("Gói VIP trọn đời")
                                .duration(99999)
                                .price(20000000)
                                .description("Gói tập trọn đời, dành riêng cho khách hàng VIP.")
                                .build()
                );

                // **Chỉ lưu gói tập nếu nó chưa tồn tại**
                List<SubscriptionPlan> newPlans = plansToCreate.stream()
                        .filter(plan -> !subscriptionPlanRepository.existsByPlanName(plan.getPlanName()))
                        .toList();

                if (!newPlans.isEmpty()) {
                    subscriptionPlanRepository.saveAll(newPlans);
                    log.info("✅ {} Fake subscription plans created.", newPlans.size());
                } else {
                    log.info("✅ All subscription plans already exist. No new plan created.");
                }
            }

            //  **Tạo Fake User Subscriptions (Chỉ CLIENTS)**
            List<User> clients = userRepository.findByRoles_Name("CLIENT");
            List<SubscriptionPlan> availablePlans = subscriptionPlanRepository.findAll();

            if (userSubscriptionRepository.count() < clients.size() && !availablePlans.isEmpty()) {
                clients.forEach(user -> {
                    // Kiểm tra nếu User chưa có Subscription
                    if (!userSubscriptionRepository.existsByUser(user)) {
                        SubscriptionPlan randomPlan = availablePlans.get(faker.random().nextInt(availablePlans.size()));
                        LocalDate startDate = LocalDate.now().minusDays(faker.number().numberBetween(0, 30));
                        LocalDate endDate = startDate.plusDays(randomPlan.getDuration());

                        UserSubscription subscription = UserSubscription.builder()
                                .user(user)
                                .subscriptionPlan(randomPlan)
                                .startDate(startDate)
                                .endDate(endDate)
                                .status(SubscriptionStatus.ACTIVE)
                                .build();

                        userSubscriptionRepository.save(subscription);
                    }
                });
                log.info("✅ Fake user subscriptions created.");
            }

        };
    }
}
