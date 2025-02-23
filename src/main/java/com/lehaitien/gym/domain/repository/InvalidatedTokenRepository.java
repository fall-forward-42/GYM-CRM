package com.lehaitien.gym.domain.repository;

import com.lehaitien.gym.domain.model.Authentication.InvalidatedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvalidatedTokenRepository extends JpaRepository<InvalidatedToken, String> {}