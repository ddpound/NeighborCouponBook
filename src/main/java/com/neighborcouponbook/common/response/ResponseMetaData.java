package com.neighborcouponbook.common.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * response data 를 설명해주는 메타 데이터 객체
 * */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResponseMetaData {

    // 데이터 총 개수
    private Long dataTotalCount;

    // 데이터 설명
    private String dataDescription;
}
