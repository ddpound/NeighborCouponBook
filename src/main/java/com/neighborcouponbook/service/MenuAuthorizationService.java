package com.neighborcouponbook.service;

import com.neighborcouponbook.common.components.AuthorizationResult;
import com.neighborcouponbook.common.context.RequestContext;

public interface MenuAuthorizationService {

    Integer refreshCache();
    AuthorizationResult authorize(RequestContext context);
}
