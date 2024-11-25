package com.neighborcouponbook.model.search;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CouponUserSearch extends CommonSearch {
    private Long   userId;
    private String userLoginId;
    private String userName;
    private CouponUserSort sort;

    public enum CouponUserSort{
        userId, userLoginId, userName
    }
}
