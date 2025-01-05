package com.neighborcouponbook.model.vo;


import com.neighborcouponbook.common.util.NullChecker;
import com.neighborcouponbook.model.CouponBookFile;
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
public class CouponBookFileVo extends CommonColumnVo {

    private Long fileId;
    private Long fileGroupId;
    private Long fileSerialNo;
    private String physicalFilePath;
    private String physicalFileName;
    private String originalFileName;

    /** model -> vo */
    public CouponBookFileVo convertToVo(CouponBookFile couponBookFile) {
        CouponBookFileVo vo = new CouponBookFileVo();

        if(!NullChecker.isNull(couponBookFile.getFileId())) vo.setFileId(couponBookFile.getFileId());
        if(!NullChecker.isNull(couponBookFile.getFileGroupId())) vo.setFileGroupId(couponBookFile.getFileGroupId());
        if(!NullChecker.isNull(couponBookFile.getFileSerialNo())) vo.setFileSerialNo(couponBookFile.getFileSerialNo());
        if(!NullChecker.isNull(couponBookFile.getPhysicalFilePath())) vo.setPhysicalFilePath(couponBookFile.getPhysicalFilePath());
        if(!NullChecker.isNull(couponBookFile.getPhysicalFileName())) vo.setPhysicalFileName(couponBookFile.getPhysicalFileName());
        if(!NullChecker.isNull(couponBookFile.getOriginalFileName())) vo.setOriginalFileName(couponBookFile.getOriginalFileName());

        vo.settingCommonColumnVo(couponBookFile);

        return vo;
    }

    public List<CouponBookFileVo> convertToVoList(List<CouponBookFile> couponBookFiles) {
        List<CouponBookFileVo> voList = new ArrayList<>();

        for (CouponBookFile couponBookFile : couponBookFiles) {
            voList.add(convertToVo(couponBookFile));
        }

        return voList;
    }

}
