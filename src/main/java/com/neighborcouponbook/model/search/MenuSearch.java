package com.neighborcouponbook.model.search;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MenuSearch extends CommonSearch{
    private Long menuId;
    private String menuUri;
    private String menuName;
    private menuOrderBy sort;

    /**
     * order by에 사용될 enum
     * */
    public enum menuOrderBy {
        menuId, menuUri, menuName
    }
}
