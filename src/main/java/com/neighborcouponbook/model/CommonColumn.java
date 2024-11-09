package com.neighborcouponbook.model;

import com.neighborcouponbook.model.vo.CommonColumnVo;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;


/**
 * 시간과 주석 처리 공통 테이블
 * */
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@MappedSuperclass
public class CommonColumn {

    @CreationTimestamp
    private Timestamp createDate;

    private Long createUser;

    @UpdateTimestamp
    private Timestamp updateDate;

    private Long updateUser;

    private Boolean isDeleted;

    private String remarks;

    /**
     * DB에 관한 주석
     * */
    private String dbRemarks;

    /**
     * 생성할 때 관련 데이터 세팅
     *
     * */
    public void settingCreateData(Long createUserId) {
        this.isDeleted = false;
        this.createUser = createUserId;
    }

    // 객체 삭제처리
    public void softDeleteData(Long deleteUserId) {
        this.isDeleted = true;
        this.updateUser = deleteUserId;
    }

    public void updateData(Long updateUserId) {
        this.updateUser = updateUserId;
    }

    public void writeRemarks(String remarks) {
        this.remarks = remarks;
    }

    public void writeDbRemarks(String dbRemarks) {
        this.dbRemarks = dbRemarks;
    }

    public CommonColumnVo returnConvertToVo() {
        return CommonColumnVo.builder()
                .createDate(createDate)
                .createUser(createUser)
                .updateDate(updateDate)
                .updateUser(updateUser)
                .isDeleted(isDeleted)
                .createDate(createDate)
                .remarks(remarks)
                .dbRemarks(dbRemarks)
                .build();
    }

    /**
     * DB에서 받아온 공통 컬럼 데이터를 반환해줌
     * */
    public CommonColumn returnAllCommonColumn() {
        return CommonColumn.builder()
                .createDate(createDate)
                .createUser(createUser)
                .updateDate(updateDate)
                .updateUser(updateUser)
                .isDeleted(isDeleted)
                .createDate(createDate)
                .remarks(remarks)
                .dbRemarks(dbRemarks)
                .build();
    }
}
