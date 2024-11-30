package com.neighborcouponbook.model;

import com.neighborcouponbook.common.util.NullChecker;
import com.neighborcouponbook.model.vo.MenuVo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * 메뉴 테이블은 트리 계층구조를 가진다.
 *
 * */

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class Menu extends CommonColumn{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long menuId;

    @Column(nullable = false, unique = true)
    private String menuUri;

    private String menuName;

    private Long parentMenuId;

    private MenuType menuType = MenuType.REST;

    private MenuProperty menuProperty = MenuProperty.STATIC;

    public void createMenu(String menuUri, String menuName, Long parentMenuId) {
        this.menuUri = menuUri;
        this.menuName = menuName;
        if(!NullChecker.isNull(parentMenuId)) this.parentMenuId = parentMenuId;
    }

    public void createMenu(String menuUri, String menuName, Long parentMenuId, MenuType menuType, MenuProperty menuProperty) {
        this.menuUri = menuUri;
        this.menuName = menuName;
        this.menuType = menuType;
        this.menuProperty = menuProperty;
        if(!NullChecker.isNull(parentMenuId)) this.parentMenuId = parentMenuId;
    }

    public void updateMenu(Menu menu){
        if(!NullChecker.isNullString(menu.getMenuUri())) this.menuUri = menu.getMenuUri();
        if(!NullChecker.isNullString(menu.getMenuName())) this.menuName = menu.getMenuName();
        if(!NullChecker.isNull(menu.getParentMenuId())) this.parentMenuId = menu.getParentMenuId();
        if(!NullChecker.isNull(menu.getMenuType())) this.menuType = menu.getMenuType();
        if(!NullChecker.isNull(menu.getMenuProperty())) this.menuProperty = menu.getMenuProperty();
    }

    public enum MenuType {
        REST, PAGE
    }

    /**
     * 메뉴 속성
     * 1. DYNAMIC : 동적 URI, /my-data/{param}
     * 2. STATIC : 정적 URI, /my-data/all-list
     * */
    public enum MenuProperty{
        DYNAMIC, STATIC
    }

}
