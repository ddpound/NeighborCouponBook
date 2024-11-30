package com.neighborcouponbook.common.components;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthorizationResult {
    private final boolean authorized;
    private final String errorMessage;

    // Static factory methods
    public static AuthorizationResult authorized() {
        return AuthorizationResult.builder()
                .authorized(true)
                .build();
    }

    public static AuthorizationResult denied(String message) {
        return AuthorizationResult.builder()
                .authorized(false)
                .errorMessage(message)
                .build();
    }
}
