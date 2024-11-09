package com.neighborcouponbook.common.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseUtil {

    /**
     * 데이터 반환 예시
     * */
    public static <T> ResponseEntity<ApiResponse<T>> createResponse(T data, Integer state, String message, HttpStatus status) {
        ApiResponse<T> apiResponse = new ApiResponse<>(data, state, message);
        return new ResponseEntity<>(apiResponse, status);
    }

    /**
     * 성공응답 반환
     * */
    public static ResponseEntity<ApiResponse<String>> createSuccessResponse(Integer state, String message) {
        ApiResponse<String> apiResponse = new ApiResponse<>("", state, message);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}
