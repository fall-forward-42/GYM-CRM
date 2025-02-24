package com.lehaitien.gym.domain.repository;

import com.lehaitien.gym.domain.model.Authentication.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;


@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
    Set<Role> findByNameIn(Set<String> names);
    Optional<Role> findByName(String name);
}