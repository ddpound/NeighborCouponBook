package com.neighborcouponbook.common.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "API 공통 응답 객체")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ApiCommonResponse<T> {
    @Schema(description = "응답 데이터")
    private T data;

    @Schema(description = "메타 데이터")
    private ResponseMetaData metaData;

    @Schema(description = "상태코드")
    private Integer state;

    @Schema(description = "응답메세지")
    private String message;
}
