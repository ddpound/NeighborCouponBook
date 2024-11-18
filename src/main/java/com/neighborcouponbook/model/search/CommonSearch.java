package com.neighborcouponbook.model.search;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @deprecated
 * 페이징 정보를 담아둠
 *
 * */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CommonSearch {

    // 현재 페이지 번호
    private Integer pageNumber;

    // 한 페이지에 보여줄 개수
    private Integer howManyShowOnAPage;


}
