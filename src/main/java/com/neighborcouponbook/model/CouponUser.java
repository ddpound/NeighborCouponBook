package com.neighborcouponbook.model;

import com.neighborcouponbook.common.components.CommonBCryptPasswordEncoder;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 쿠폰북 유저 테이블
 * posrtresql 예약어 이슈로 인한 user -> CouponUser로 변경
 * */
@SuperBuilder
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Entity
public class CouponUser extends CommonColumn{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(unique = true, nullable = false)
    private String userLoginId;

    @Column(unique = false, nullable = false)
    private String userName;

    @Column(nullable = false)
    private String password;

    public void initCouponUser(Long userId, String userLoginId, String userName, String password) {
        this.userId = userId;
        this.userLoginId = userLoginId;
        this.userName = userName;
        this.password = password;
    }

    /**
     * @param userLoginId login id 입니다.
     * @param userName nickName입니다.
     * @param password create메소드 안에 암호화가 이미 있습니다. 암호화 지정이 필요없습니다.
     * */
    public void settingCouponUser(String userLoginId, String userName, String password) {
        CommonBCryptPasswordEncoder passwordEncoder = new CommonBCryptPasswordEncoder();

        this.userLoginId = userLoginId;
        this.userName = userName;
        this.password = passwordEncoder.getBCryptPasswordEncoder().encode(password);
    }

    public void changePassword(String password){
        CommonBCryptPasswordEncoder passwordEncoder = new CommonBCryptPasswordEncoder();

        this.password = passwordEncoder.getBCryptPasswordEncoder().encode(password);
    }

}
