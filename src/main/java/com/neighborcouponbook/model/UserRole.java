package com.neighborcouponbook.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * user 가 어떤 권한을 가지고 있는지 나타내는 테이블
 * user 는 다중 권한을 가지고 있을 수 있다.
 * */

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class UserRole extends CommonColumn{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userRoleId;

    private Long userId;

    private Long roleId;

    public void createUserRole(Long userId, Long roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }

}
