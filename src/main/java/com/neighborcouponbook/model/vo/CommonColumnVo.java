package com.neighborcouponbook.model.vo;

import com.neighborcouponbook.common.util.NullChecker;
import com.neighborcouponbook.model.CommonColumn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.sql.Timestamp;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CommonColumnVo {

    private Timestamp createDate;
    private Long createUser;
    private Timestamp updateDate;
    private Long updateUser;
    private Boolean isDeleted;
    private String remarks;
    private String dbRemarks;

    // 해당 vo list 결과값의 총 개수
    private Long totalCount;

    /**
     * DB 공통 컬럼을 그대로 vo 로 전환해줌
     * */
    public void settingCommonColumnVo(CommonColumn commonColumn){
        if(!NullChecker.isNull(commonColumn.getCreateDate()))
            this.createDate = commonColumn.getCreateDate();

        if(!NullChecker.isNull(commonColumn.getCreateUser()))
            this.createUser = commonColumn.getCreateUser();

        if(!NullChecker.isNull(commonColumn.getUpdateDate()))
            this.updateDate = commonColumn.getUpdateDate();

        if(!NullChecker.isNull(commonColumn.getUpdateUser()))
            this.updateUser = commonColumn.getUpdateUser();

        if(!NullChecker.isNull(commonColumn.getIsDeleted()))
            this.isDeleted = commonColumn.getIsDeleted();

        if(!NullChecker.isNull(commonColumn.getRemarks()))
            this.remarks = commonColumn.getRemarks();

        if(!NullChecker.isNull(commonColumn.getDbRemarks()))
            this.dbRemarks = commonColumn.getDbRemarks();
    }

}
