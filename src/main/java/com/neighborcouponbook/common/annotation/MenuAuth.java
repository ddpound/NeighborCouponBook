package com.neighborcouponbook.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 메뉴 정보를 저장하기 위한 커스텀 어노테이션
 * */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MenuAuth {
    MenuAuthDetail[] menuAuthDetail() default {};

    @Retention(RetentionPolicy.RUNTIME)
    @interface MenuAuthDetail {
        String rolesName() default "";
        long roleId();                        // 권한 ID
        boolean read()     default true;
        boolean write()    default true;
        boolean upload()   default true;
        boolean download() default true;
    }

}

