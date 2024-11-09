package com.neighborcouponbook.repository;

import com.neighborcouponbook.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
}
