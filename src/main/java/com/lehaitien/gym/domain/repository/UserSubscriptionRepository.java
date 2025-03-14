package com.lehaitien.gym.domain.repository;


import com.lehaitien.gym.domain.constant.SubscriptionStatus;
import com.lehaitien.gym.domain.model.Subsription.UserSubscription;
import com.lehaitien.gym.domain.model.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserSubscriptionRepository extends JpaRepository<UserSubscription, String> {

    Collection<UserSubscription> findByUser(User user);
    boolean existsByUser(User user);

    List<UserSubscription> findByStatus(SubscriptionStatus status);

    Optional<UserSubscription> findByUser_UserIdAndStatus(String userId, SubscriptionStatus status);

    //Sắp xếp danh sách subscription theo số ngày còn lại (tăng dần)
    @Query("SELECT us FROM UserSubscription us " +
            "WHERE us.status = :status " +
            "ORDER BY DATEDIFF(us.endDate, :currentDate) ASC")
    List<UserSubscription> findByStatusOrderByDaysLeftAsc(
            @Param("status") SubscriptionStatus status,
            @Param("currentDate") LocalDate currentDate);

    // Sắp xếp danh sách subscription theo số ngày còn lại (giảm dần)
    @Query("SELECT us FROM UserSubscription us " +
            "WHERE us.status = :status " +
            "ORDER BY DATEDIFF(us.endDate, :currentDate) DESC")
    List<UserSubscription> findByStatusOrderByDaysLeftDesc(
            @Param("status") SubscriptionStatus status,
            @Param("currentDate") LocalDate currentDate);


}

