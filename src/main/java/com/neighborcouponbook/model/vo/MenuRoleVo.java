package com.neighborcouponbook.model.vo;

import com.neighborcouponbook.common.util.NullChecker;
import com.neighborcouponbook.model.MenuRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MenuRoleVo extends CommonColumnVo{
    private Long menuRoleId;
    private Long menuId;
    private Long roleId;
    private Boolean read;
    private Boolean write;
    private Boolean download;
    private Boolean upload;

    public MenuRoleVo convertToVo(MenuRole menuRole) {
        MenuRoleVo menuRoleVo = new MenuRoleVo();

        if(!NullChecker.isNull(menuRole.getMenuRoleId()))   menuRoleVo.setMenuRoleId(menuRole.getMenuRoleId());
        if(!NullChecker.isNull(menuRole.getMenuId()))       menuRoleVo.setMenuId(menuRole.getMenuId());
        if(!NullChecker.isNull(menuRole.getRoleId()))       menuRoleVo.setRoleId(menuRole.getRoleId());
        if(!NullChecker.isNull(menuRole.getRead()))         menuRoleVo.setRead(menuRole.getRead());
        if(!NullChecker.isNull(menuRole.getWrite()))        menuRoleVo.setWrite(menuRole.getWrite());
        if(!NullChecker.isNull(menuRole.getDownload()))     menuRoleVo.setDownload(menuRole.getDownload());
        if(!NullChecker.isNull(menuRole.getUpload()))       menuRoleVo.setUpload(menuRole.getUpload());

        return menuRoleVo;
    }

    public List<MenuRoleVo> convertToVoList(List<MenuRole> menuRoles) {
        List<MenuRoleVo> menuRoleVoList = new ArrayList<>();

        for(MenuRole menuRole : menuRoles) {
            menuRoleVoList.add(convertToVo(menuRole));
        }

        return menuRoleVoList;
    }

}
