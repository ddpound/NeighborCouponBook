package com.neighborcouponbook.common.context;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RequestContext {
    private String clientIp;
    private String requestUri;
    private String token;
    private String method;
    private String userAgent;      // 브라우저 정보
    private String acceptLanguage; // 선호 언어
    private Map<String, String> headers; // 모든 헤더 정보
}
