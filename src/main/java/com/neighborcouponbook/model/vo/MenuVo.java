package com.neighborcouponbook.model.vo;

import com.neighborcouponbook.common.util.NullChecker;
import com.neighborcouponbook.model.Menu;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MenuVo extends CommonColumnVo{

    private Long menuId;
    private String menuUri;
    private String menuName;
    private Long parentMenuId;
    private Menu.MenuType menuType = Menu.MenuType.REST;
    private Menu.MenuProperty menuProperty = Menu.MenuProperty.STATIC;

    public MenuVo convertToMenuVo(Menu menu) {
        MenuVo menuVo = new MenuVo();

        if(!NullChecker.isNull(menu.getMenuId())) menuVo.setMenuId(menu.getMenuId());
        if(!NullChecker.isNullString(menu.getMenuUri())) menuVo.setMenuUri(menu.getMenuUri());
        if(!NullChecker.isNullString(menu.getMenuName())) menuVo.setMenuName(menu.getMenuName());
        if(!NullChecker.isNull(menu.getParentMenuId())) menuVo.setParentMenuId(menu.getParentMenuId());

        if(!NullChecker.isNull(menu.returnAllCommonColumn())) menuVo.settingCommonColumnVo(menu.returnAllCommonColumn());

        if(!NullChecker.isNull(menu.getMenuType())) menuVo.setMenuType(menu.getMenuType());
        if(!NullChecker.isNull(menu.getMenuProperty())) menuVo.setMenuProperty(menu.getMenuProperty());

        return menuVo;
    }

    public List<MenuVo> convertToMenuVoList(List<Menu> menus) {
        List<MenuVo> menuVoList = new ArrayList<>();
        menus.forEach(menu -> menuVoList.add(convertToMenuVo(menu)));

        return menuVoList;
    }

}
