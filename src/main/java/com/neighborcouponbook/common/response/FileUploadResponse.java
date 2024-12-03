package com.neighborcouponbook.common.response;

import com.neighborcouponbook.model.vo.CouponBookFileVo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FileUploadResponse {
    private Long fileGroupId;
    private List<CouponBookFileVo> files;
}
