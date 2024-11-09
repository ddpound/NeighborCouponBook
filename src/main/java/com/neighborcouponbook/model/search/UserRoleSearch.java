package com.neighborcouponbook.model.search;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserRoleSearch {
    private Long userRoleId;
    private Long userId;
    private Long roleId;
}
