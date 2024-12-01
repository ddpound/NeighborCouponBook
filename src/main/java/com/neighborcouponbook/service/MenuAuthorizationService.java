package com.neighborcouponbook.service;

import com.neighborcouponbook.common.components.AuthorizationResult;
import com.neighborcouponbook.common.context.RequestContext;
import com.neighborcouponbook.model.vo.MenuVo;

import java.util.List;

public interface MenuAuthorizationService {

    Integer refreshCache();
    AuthorizationResult authorize(RequestContext context);
    List<MenuVo> searchFilteringMenuList(List<MenuVo> menuVoList, String requestUri);
}
