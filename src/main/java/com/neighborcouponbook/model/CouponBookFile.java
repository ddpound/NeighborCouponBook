package com.neighborcouponbook.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 쿠폰북의 공통 파일 테이블
 * */
@SuperBuilder
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Entity
public class CouponBookFile extends CommonColumn{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fileId;

    private Long fileGroupId;

    private Long fileSerialNo;

    private String physicalFilePath;

    private String physicalFileName;

    private String originalFileName;
}
