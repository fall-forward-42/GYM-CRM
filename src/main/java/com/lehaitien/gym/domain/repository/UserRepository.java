package com.lehaitien.gym.domain.repository;

import java.util.Optional;

import com.lehaitien.gym.domain.model.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<User, String> {

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    Optional<User> findByUsername(String username);
}