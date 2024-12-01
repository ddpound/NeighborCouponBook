package com.neighborcouponbook.common.annotation;

import com.neighborcouponbook.model.Menu;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 해당 세팅은 초기 세팅에만 작동하는 코드입니다.
 * 운영중에는 사용할 일이 없습니다.
 * 메뉴 정보를 저장하기 위한 커스텀 어노테이션
 * */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MenuInformation {

    // 전체 데이터 직접 저장 할지 선택 옵션
    boolean menuDirectDBSave() default true;

    // REST, PAGE 인지 구분해주는 요소, 보통은 REST 타입
    Menu.MenuType menuType() default Menu.MenuType.REST;
    MenuRoleDetail[] menuAuthDetail() default {};

    @Retention(RetentionPolicy.RUNTIME)
    @interface MenuRoleDetail {
        String rolesName() default "";
        long roleId();                        // 권한 ID
        boolean read()     default true;
        boolean write()    default true;
        boolean upload()   default true;
        boolean download() default true;

        // 바로 저장할지 선택해주는 요소
        boolean menuRoleDirectDBSave() default true;
    }
}

