package com.neighborcouponbook.common.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseUtil {


    public static <T> ResponseEntity<ApiResponse<T>> createResponse(T data, ResponseMetaData metaData ,Integer state, String message, HttpStatus status) {
        ApiResponse<T> apiResponse = new ApiResponse<>(data, metaData, state, message);
        return new ResponseEntity<>(apiResponse, status);
    }


    public static <T> ResponseEntity<ApiResponse<T>> createResponse(T data, Integer state, String message, HttpStatus status) {
        ApiResponse<T> apiResponse = new ApiResponse<>(data,null, state, message);
        return new ResponseEntity<>(apiResponse, status);
    }


    public static ResponseEntity<ApiResponse<String>> createSuccessResponse(Integer state, String message) {
        ApiResponse<String> apiResponse = new ApiResponse<>("", null , state, message);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}
