package com.zgj.mps.dao;

import com.zgj.mps.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    List<UserRole> findAllByUserId(Long id);

    int deleteByUserId(long userId);
}