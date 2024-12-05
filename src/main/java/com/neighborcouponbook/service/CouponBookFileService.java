package com.neighborcouponbook.service;

import com.neighborcouponbook.common.response.FileUploadResponse;
import com.neighborcouponbook.common.response.ZipResource;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CouponBookFileService {

    Resource downloadFile(Long fileId);

    FileUploadResponse uploadFiles(List<MultipartFile> files);

    ZipResource downloadFilesByGroupId(Long fileGroupId);

    void deleteFile(Long fileId);

    void deleteFilesByGroupId(Long groupId);

    /** 파일 그룹 시퀸스 실행하고 해당 값을 가져오는 메소드 */
    Long getNextFileGroupId();

    /** 파일 그룹의 현재 값만 체크해줌, 시퀸스 값이 올라가지않음. */
    Long getCurrentValue();

    void todayFileFolder();
}
