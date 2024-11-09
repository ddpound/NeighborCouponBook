package com.neighborcouponbook.model.search;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MenuSearch {
    private Long menuId;
    private String menuUri;
    private String menuName;
}
