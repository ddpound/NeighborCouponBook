package com.neighborcouponbook.common.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Slf4j
public class ResponseUtil {


    public static <T> ResponseEntity<ApiCommonResponse<T>> createResponse(T data, ResponseMetaData metaData , Integer state, String message, HttpStatus status) {
        ApiCommonResponse<T> apiCommonResponse = new ApiCommonResponse<>(data, metaData, state, message);
        return new ResponseEntity<>(apiCommonResponse, status);
    }


    public static <T> ResponseEntity<ApiCommonResponse<T>> createResponse(T data, Integer state, String message, HttpStatus status) {
        ApiCommonResponse<T> apiCommonResponse = new ApiCommonResponse<>(data,null, state, message);
        return new ResponseEntity<>(apiCommonResponse, status);
    }

    public static ResponseEntity<ApiCommonResponse<String>> createSuccessResponse(Integer state, String message) {
        ApiCommonResponse<String> apiCommonResponse = new ApiCommonResponse<>("", null , state, message);
        return new ResponseEntity<>(apiCommonResponse, HttpStatus.OK);
    }

    public static <T> String responseEntityMapperString(T data, ResponseMetaData metaData , Integer state, String message, HttpStatus status) {
        try {
            ApiCommonResponse<T> apiCommonResponse = new ApiCommonResponse<>(data, metaData, state, message);
            ObjectMapper objectMapper = new ObjectMapper();

            return objectMapper.writeValueAsString(apiCommonResponse);
        }catch (Exception e) {
            log.error(e.getMessage());
            return "{'error':'error','message':'error message'}";
        }
    }
}
