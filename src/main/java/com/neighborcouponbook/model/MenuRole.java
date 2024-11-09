package com.neighborcouponbook.model;

import com.neighborcouponbook.common.util.NullChecker;
import com.neighborcouponbook.model.vo.MenuRoleVo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 *
 *
 * */

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class MenuRole extends CommonColumn{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long menuRoleId;

    @Column(nullable = false)
    private Long menuId;

    @Column(nullable = false)
    private Long roleId;

    private Boolean read;
    private Boolean write;
    private Boolean download;
    private Boolean upload;

    public void createMenuRole(MenuRoleVo vo){
        if(!NullChecker.isNull(vo.getMenuRoleId())) this.menuRoleId = vo.getMenuRoleId();
        if(!NullChecker.isNull(vo.getMenuId())) this.menuId = vo.getMenuId();
        if(!NullChecker.isNull(vo.getRoleId())) this.roleId = vo.getRoleId();
        if(!NullChecker.isNull(vo.getRead())) this.read = vo.getRead();
        if(!NullChecker.isNull(vo.getWrite())) this.write = vo.getWrite();
        if(!NullChecker.isNull(vo.getDownload())) this.download = vo.getDownload();
        if(!NullChecker.isNull(vo.getUpload())) this.upload = vo.getUpload();
    }
}
