package com.neighborcouponbook.model.search;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


/**
 * description
 * 페이징 정보를 담아둠
 * */
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CommonSearch {

    // 현재 페이지 번호
    private Long pageNumber = 1L;

    // 한 페이지에 보여줄 개수
    private Long pageSize = 10L;

    // 페이징 옵션 온오프
    private Boolean paging;

    // 오름차순 내림차순 정렬, 기본 오름차순 정렬
    private orderBy sortOrder = orderBy.asc;

    /**
     * pageSize 와 pageNumber, 그리고 리스트의 길이를 이용해 offset 시작위치를 계산해줌.
     * */
    public Long getOffset() {
        long returnOffset;

        if(getPageNumber() > 1L){
            returnOffset = (getPageNumber() - 1L) * getPageSize();
        }else{
            returnOffset = 1L;
        }

        return returnOffset;
    }

    public enum orderBy{
        desc, asc
    }
}
