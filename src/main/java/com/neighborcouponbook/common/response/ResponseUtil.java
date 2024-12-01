package com.neighborcouponbook.common.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Slf4j
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

    public static <T> String responseEntityMapperString(T data, ResponseMetaData metaData , Integer state, String message, HttpStatus status) {
        try {
            ApiResponse<T> apiResponse = new ApiResponse<>(data, metaData, state, message);
            ObjectMapper objectMapper = new ObjectMapper();

            return objectMapper.writeValueAsString(apiResponse);
        }catch (Exception e) {
            log.error(e.getMessage());
            return "{'error':'error','message':'error message'}";
        }
    }
}
