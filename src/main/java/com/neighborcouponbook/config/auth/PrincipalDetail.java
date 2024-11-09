package com.neighborcouponbook.config.auth;

import com.neighborcouponbook.model.CouponUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@RequiredArgsConstructor
public class PrincipalDetail implements UserDetails {

    private final CouponUser couponUser;

    public Long getCouponUserId() {
        return couponUser.getUserId();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return couponUser.getPassword();
    }

    @Override
    public String getUsername() {
        return couponUser.getUserName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // true 안잠겨있다 라는 뜻
    }
    // 계정이 만료되지 않았는지 리턴한다(true 잠기지 않음)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 계정활성화가 완료 되어있는지 아닌지 (true를 해야 활성화)
    @Override
    public boolean isEnabled() {
        return true;
    }
}
