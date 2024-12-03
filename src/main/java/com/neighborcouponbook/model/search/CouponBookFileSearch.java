package com.neighborcouponbook.model.search;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CouponBookFileSearch extends CommonSearch{

    private Long fileId;
    private Long fileGroupId;
    private Long fileSerialNo;
    private String physicalFilePath;
    private String physicalFileName;
    private String originalFileName;

}
