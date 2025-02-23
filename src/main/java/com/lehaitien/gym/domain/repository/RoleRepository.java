package com.lehaitien.gym.domain.repository;

import com.lehaitien.gym.domain.model.Authentication.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RoleRepository extends JpaRepository<Role, String> {}