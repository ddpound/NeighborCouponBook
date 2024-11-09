package com.neighborcouponbook.model.search;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MenuRoleSearch {
    private Long menuRoleId;
    private Long menuId;
    private Long roleId;
    private Boolean read;
    private Boolean write;
    private Boolean download;
    private Boolean upload;
}
