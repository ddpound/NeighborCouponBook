package com.neighborcouponbook.model;

import com.neighborcouponbook.common.util.NullChecker;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

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

    public void createMenu(String menuUri, String menuName, Long parentMenuId) {
        this.menuUri = menuUri;
        this.menuName = menuName;
        if(!NullChecker.isNull(parentMenuId)) this.parentMenuId = parentMenuId;
    }

    public void updateMenu(Menu menu){
        if(!NullChecker.isNullString(menu.getMenuUri())) this.menuUri = menu.getMenuUri();
        if(!NullChecker.isNullString(menu.getMenuName())) this.menuName = menu.getMenuName();
        if(!NullChecker.isNull(menu.getParentMenuId())) this.parentMenuId = menu.getParentMenuId();
    }

}
