package com.neighborcouponbook.common.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Slf4j
public class ResponseUtil {

    public static <T> ResponseEntity<ApiCommonResponse<T>> createResponse(ApiCommonResponse<T> apiCommonResponse) {
        return new ResponseEntity<>(apiCommonResponse, apiCommonResponse.getHttpStatus());
    }

    public static <T> ResponseEntity<ApiCommonResponse<T>> createResponse(T data, ResponseMetaData metaData , Integer state, String message, HttpStatus status) {
        ApiCommonResponse<T> apiCommonResponse = ApiCommonResponse.<T>builder().data(data).metaData(metaData).state(state).message(message).build();
        return new ResponseEntity<>(apiCommonResponse, status);
    }

    public static <T> ResponseEntity<ApiCommonResponse<T>> createResponse(T data, Integer state, String message, HttpStatus status) {
        ApiCommonResponse<T> apiCommonResponse = ApiCommonResponse.<T>builder().data(data).state(state).message(message).build();
        return new ResponseEntity<>(apiCommonResponse, status);
    }

    // 성공 응답을 위한 정적 팩토리 메서드
    public static <T> ResponseEntity<ApiCommonResponse<T>> createSuccessResponse(T data, String message) {
        return new ResponseEntity<>(
                ApiCommonResponse
                        .<T>builder()
                        .data(data)
                        .state(1)
                        .message(message)
                        .httpStatus(HttpStatus.OK)
                        .build(),
                HttpStatus.OK);
    }

    // 실패 응답을 위한 정적 팩토리 메서드
    public static <T> ResponseEntity<ApiCommonResponse<T>> createErrorResponse(String message) {
        return new ResponseEntity<>(
                ApiCommonResponse
                        .<T>builder()
                        .data(null)
                        .state(-1)
                        .message(message)
                        .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                        .build(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // 실패 응답을 위한 정적 팩토리 메서드
    public static <T> ResponseEntity<ApiCommonResponse<T>> createErrorResponse(String message, HttpStatus status) {
        return new ResponseEntity<>(
                ApiCommonResponse
                        .<T>builder()
                        .data(null)
                        .state(-1)
                        .message(message)
                        .httpStatus(status)
                        .build(), status);
    }

    public static ResponseEntity<ApiCommonResponse<String>> createSuccessResponse(Integer state, String message) {
        return new ResponseEntity<>(ApiCommonResponse.<String>builder().state(state).message(message).build(), HttpStatus.OK);
    }

    public static <T> String responseEntityMapperString(T data, ResponseMetaData metaData , Integer state, String message, HttpStatus status) {
        try {
            ApiCommonResponse<T> apiCommonResponse = ApiCommonResponse.<T>builder().data(data).metaData(metaData).state(state).message(message).build();
            ObjectMapper objectMapper = new ObjectMapper();

            return objectMapper.writeValueAsString(apiCommonResponse);
        }catch (Exception e) {
            log.error(e.getMessage());
            return "{'error':'error','message':'error message'}";
        }
    }
}
