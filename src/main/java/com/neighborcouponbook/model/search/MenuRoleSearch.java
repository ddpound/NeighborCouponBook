package com.neighborcouponbook.model.search;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MenuRoleSearch extends CommonSearch{
    private Long menuRoleId;
    private Long menuId;
    private Long roleId;
    private Boolean read;
    private Boolean write;
    private Boolean download;
    private Boolean upload;
}
