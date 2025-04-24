package com.lehaitien.gym.infrastructure.config;


import com.lehaitien.gym.application.dto.request.Branch.BranchFacilityRequest;
import com.lehaitien.gym.application.dto.response.Branch.BranchFacilityResponse;
import com.lehaitien.gym.application.service.BranchFacilityService;
import com.lehaitien.gym.domain.constant.*;
import com.lehaitien.gym.domain.exception.AppException;
import com.lehaitien.gym.domain.exception.ErrorCode;
import com.lehaitien.gym.domain.model.Authentication.Role;
import com.lehaitien.gym.domain.model.Branch.Branch;
import com.lehaitien.gym.domain.model.Branch.BranchFacility;
import com.lehaitien.gym.domain.model.Payment.Payment;
import com.lehaitien.gym.domain.model.Schedule.ClassSchedule;
import com.lehaitien.gym.domain.model.Schedule.ClassScheduleParticipant;
import com.lehaitien.gym.domain.model.Subsription.SubscriptionPlan;
import com.lehaitien.gym.domain.model.Subsription.UserSubscription;
import com.lehaitien.gym.domain.model.User.Coach;
import com.lehaitien.gym.domain.model.User.User;
import com.lehaitien.gym.domain.repository.*;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
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
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.IntStream;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class FakeDataInitializer {

    private static final Faker faker = new Faker();
    private final PasswordEncoder passwordEncoder;

    @Value("${app.seed-data}")
    private boolean seedDataEnabled;


    @NonFinal
    static final String ADMIN_USER_NAME = "admin";

    @NonFinal
    static final String ADMIN_PASSWORD = "admin";

    @NonFinal
    static final String ADMIN_EMAIL = "admin@gmail.com";

    @Bean
    public ApplicationRunner initFakeData(UserRepository userRepository,
                                          RoleRepository roleRepository,
                                          BranchRepository branchRepository,
                                          BranchFacilityRepository facilityRepository,
                                          SubscriptionPlanRepository subscriptionPlanRepository,
                                          UserSubscriptionRepository userSubscriptionRepository,
                                          CoachRepository coachRepository,
                                          PaymentRepository paymentRepository,
                                          ClassScheduleRepository classScheduleRepository,
                                          ClassScheduleParticipantRepository participantRepository

                                         ) {


        return args -> {
            if (!seedDataEnabled) {
                log.info(" Seed data is disabled. Skipping fake data initialization.");
                return;
            }

            log.info("Seeding fake data...");

            if (userRepository.findByUsername(ADMIN_USER_NAME).isEmpty()) {
                roleRepository.save(Role.builder()
                        .name(PredefinedRole.USER_ROLE)
                        .description("User role")
                        .build());

                roleRepository.save(Role.builder()
                        .name(PredefinedRole.CLIENT_ROLE)
                        .description("Client role")
                        .build());

                roleRepository.save(Role.builder()
                        .name(PredefinedRole.COACH_ROLE)
                        .description("Couch role")
                        .build());

                Role adminRole = roleRepository.save(Role.builder()
                        .name(PredefinedRole.ADMIN_ROLE)
                        .description("Admin role")
                        .build());


                var roles = new HashSet<Role>();
                roles.add(adminRole);

                User user = User.builder()
                        .username(ADMIN_USER_NAME)
                        .email(ADMIN_EMAIL)
                        .password(passwordEncoder.encode(ADMIN_PASSWORD))
                        .balance(0)
                        .roles(roles)
                        .build();

                userRepository.save(user);
                log.warn("✅ admin user has been created with default password: admin, please change it");
            }

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
                            .balance(0)
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
                                .planName("Gói VIP 2 năm")
                                .duration(365*2)
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
            // Thêm vào phần initFakeData
            if (userRepository.count() <= 15) {
                Set<Role> coachRole = Set.of(roleRepository.findByName(PredefinedRole.COACH_ROLE)
                        .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND)));

                List<String> specializations = List.of("Gym", "Yoga", "CrossFit", "Bodybuilding", "Cardio");
                List<String> certifications = List.of("NASM", "ACE", "ISSA", "NSCA", "CF-L1");

                IntStream.range(1, 6).forEach(i -> { // Tạo 5 Coach
                    Branch assignedBranch = branches.get(i % branches.size());

                    User coach = User.builder()
                            .username(faker.name().username() + "_coach")
                            .fullName(faker.name().fullName())
                            .email(faker.internet().emailAddress())
                            .phone(faker.phoneNumber().cellPhone())
                            .gender(i % 2 == 0 ? "Male" : "Female")
                            .password(passwordEncoder.encode("422003")) // Mật khẩu mặc định
                            .dob(LocalDate.of(1985 + (i % 10), (i % 12) + 1, (i % 28) + 1))
                            .cccd(faker.number().digits(12))
                            .address(faker.address().fullAddress())
                            .height(BigDecimal.valueOf(faker.number().randomDouble(2, 1, 2)))
                            .weight(BigDecimal.valueOf(faker.number().randomDouble(2, 50, 100)))
                            .roles(coachRole)
                            .status(UserStatus.ACTIVE)
                            .branch(assignedBranch)
                            .balance(0)
                            .build();
                    userRepository.save(coach);

                    // Tạo Coach entity riêng biệt
                    Coach coachInfo = Coach.builder()
                            .user(coach)
                            .branch(assignedBranch)
                            .salary(faker.number().numberBetween(7000000, 20000000)) // Random lương
                            .specialization(specializations.get(i % specializations.size()))
                            .experienceYears(faker.number().numberBetween(1, 10)) // 1 - 10 năm kinh nghiệm
                            .certifications(certifications.get(i % certifications.size()))
                            .build();
                    coachRepository.save(coachInfo);
                });

                log.info("✅ Fake coaches created.");
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

            // Fake Payments
            if (paymentRepository.count() < clients.size() * 2) { // 2 giao dịch mỗi client
                clients.forEach(user -> {
                    IntStream.range(0, 2).forEach(i -> {
                        PaymentMethod method = PaymentMethod.values()[faker.random().nextInt(PaymentMethod.values().length)];
                        PaymentStatus status = PaymentStatus.PAID;
                        int amount = faker.number().numberBetween(300_000, 5_000_000);

                        // Cộng vào balance trước khi lưu
                        user.setBalance(user.getBalance() + amount);
                        userRepository.save(user);

                        Payment payment = Payment.builder()
                                .user(user)
                                .paymentMethod(method)
                                .amount(amount)
                                .transactionId(UUID.randomUUID().toString())
                                .status(status)
                                .paymentDate(LocalDateTime.now().minusDays(faker.number().numberBetween(1, 60)))
                                .build();

                        paymentRepository.save(payment);
                    });
                });
                log.info("✅ Fake payments created.");
            }

            if (classScheduleRepository.count() < 200) {
                List<User> coaches = userRepository.findByRoles_Name("COACH");
                List<BranchFacility> facilities = facilityRepository.findAll();

                for (Branch branch : branches) {
                    // Lọc coach thuộc branch này
                    List<User> branchCoaches = coaches.stream()
                            .filter(c -> c.getBranch().getBranchId().equals(branch.getBranchId()))
                            .toList();

                    if (branchCoaches.isEmpty()) continue;

                    // Lọc facility thuộc branch
                    List<BranchFacility> branchFacilities = facilities.stream()
                            .filter(f -> f.getBranch().getBranchId().equals(branch.getBranchId()))
                            .toList();

                    for (BranchFacility facility : branchFacilities) {
                        User coach = faker.options().nextElement(branchCoaches);
                        ClassShift shift = faker.options().option(ClassShift.class);
                        ClassType classType = faker.options().option(ClassType.class);
                        WeekMode weekMode = faker.options().option(WeekMode.class);
                        int numberOfSessions = faker.number().numberBetween(2, 3); // 2–3 buổi mỗi tuần

                        List<DayOfWeek> selectedDays = getDaysForWeek(numberOfSessions, weekMode);
                        LocalDate start = LocalDate.now();
                        LocalDate end = start.plusMonths(3); // 3 tháng tới

                        LocalDate current = start;
                        while (!current.isAfter(end)) {
                            for (DayOfWeek day : selectedDays) {
                                LocalDate sessionDate = current.with(day);
                                if (sessionDate.isBefore(start) || sessionDate.isAfter(end)) continue;

                                LocalDateTime startTime = getShiftStartTime(sessionDate, shift);
                                LocalDateTime endTime = startTime.plusMinutes(90);

                                int weekOfYear = startTime.get(WeekFields.ISO.weekOfWeekBasedYear());
                                int month = startTime.getMonthValue();
                                int year = startTime.getYear();

                                ClassSchedule schedule = ClassSchedule.builder()
                                        .branch(branch)
                                        .branchFacility(facility)
                                        .coach(coach)
                                        .startTime(startTime)
                                        .endTime(endTime)
                                        .status(ClassStatus.SCHEDULED)
                                        .classType(classType)
                                        .shift(shift)
                                        .weekOfYear(weekOfYear)
                                        .month(month)
                                        .year(year)
                                        .maxParticipants(faker.number().numberBetween(10, 30))
                                        .build();

                                classScheduleRepository.save(schedule);

                                List<User> availableClients = userRepository.findByRoles_Name("CLIENT");

                                IntStream.range(0, faker.number().numberBetween(3, 8)).forEach(i -> {
                                    User client = faker.options().nextElement(availableClients);

                                    // Tránh duplicate (đăng ký rồi thì thôi)
                                    boolean alreadyJoined = participantRepository
                                            .findByUser_UserIdAndClassSchedule_ClassScheduleId(client.getUserId(), schedule.getClassScheduleId())
                                            .isPresent();

                                    if (!alreadyJoined) {
                                        participantRepository.save(ClassScheduleParticipant.builder()
                                                .user(client)
                                                .classSchedule(schedule)
                                                .isCanceled(false)
                                                .build());
                                    }
                                });
                            }

                            current = current.plusWeeks(1);
                        }
                    }
                }

                log.info("✅ Fake class schedules (3 tháng cho mỗi branch & facility) đã được tạo.");
            }


            log.info("✅ All fake data created.");


        };
    }
    private List<DayOfWeek> getDaysForWeek(int numberOfSessions, WeekMode weekMode) {
        List<DayOfWeek> days = weekMode == WeekMode.EVEN
                ? List.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY)
                : List.of(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY, DayOfWeek.SATURDAY);
        return days.subList(0, Math.min(numberOfSessions, days.size()));
    }

    private LocalDateTime getShiftStartTime(LocalDate date, ClassShift shift) {
        return switch (shift) {
            case MORNING -> date.atTime(6, 0);
            case AFTERNOON -> date.atTime(14, 0);
            case EVENING -> date.atTime(18, 0);
        };
    }

}
