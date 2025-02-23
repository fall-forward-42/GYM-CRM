package com.lehaitien.gym.domain.repository;

import com.lehaitien.gym.domain.model.Authentication.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PermissionRepository extends JpaRepository<Permission, String> {}