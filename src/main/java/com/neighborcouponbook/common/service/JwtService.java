package com.neighborcouponbook.common.service;

import com.neighborcouponbook.common.util.RoleListUtil;
import com.neighborcouponbook.model.CouponUser;
import com.neighborcouponbook.model.vo.UserRoleVo;
import com.neighborcouponbook.service.UserRoleService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class JwtService {

    @Value("${jwt.jwt-password}")
    String secretKey;

    @Value("${jwt.expiration-hours}")
    long expirationHours;

    @Value("${jwt.issuer}")
    String issuer;

    private final UserRoleService userRoleService;

    public String createToken(CouponUser couponUser, List<UserRoleVo> userRoleList) {
        StringBuilder userRoleContactString = RoleListUtil.roleListConvertToString(userRoleList);

        // 클레임 생성
        Map<String, Object> claims = new HashMap<>();
        claims.put("user_name", couponUser.getUserName());
        claims.put("user_id", couponUser.getUserId());
        claims.put("role_id", userRoleContactString.toString());  // 예: role 정보를 포함한 클레임 추가


        return Jwts.builder()
                .signWith(new SecretKeySpec(secretKey.getBytes(), SignatureAlgorithm.HS512.getJcaName()))   // HS512 알고리즘을 사용하여 secretKey를 이용해 서명
                .setSubject(couponUser.getUserName())  // JWT 토큰 제목
                .setClaims(claims)
                .setIssuer(issuer)  // JWT 토큰 발급자
                .setIssuedAt(Timestamp.valueOf(LocalDateTime.now()))    // JWT 토큰 발급 시간
                .setExpiration(Date.from(Instant.now().plus(expirationHours, ChronoUnit.HOURS)))    // JWT 토큰 만료 시간
                .compact(); // JWT 토큰 생성
    }

    public Claims validateTokenAndGetSubject(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey.getBytes())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        }catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }

    }
}
