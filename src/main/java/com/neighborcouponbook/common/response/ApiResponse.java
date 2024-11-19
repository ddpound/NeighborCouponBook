package com.neighborcouponbook.common.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ApiResponse<T> {
    private T data;
    private ResponseMetaData metaData;
    private Integer state;
    private String message;
}
