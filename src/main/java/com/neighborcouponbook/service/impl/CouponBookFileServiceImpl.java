package com.neighborcouponbook.service.impl;

import com.neighborcouponbook.common.response.FileUploadResponse;
import com.neighborcouponbook.common.response.ZipResource;
import com.neighborcouponbook.repository.CouponBookFileRepository;
import com.neighborcouponbook.service.CouponBookFileService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Log4j2
@RequiredArgsConstructor
@Service
public class CouponBookFileServiceImpl implements CouponBookFileService {

    @PersistenceContext
    private EntityManager entityManager;

    private final CouponBookFileRepository couponBookFileRepository;

    @Override
    public Resource downloadFile(Long fileId) {
        return null;
    }

    @Override
    public FileUploadResponse uploadFiles(List<MultipartFile> files) {
        return null;
    }

    @Override
    public ZipResource downloadFilesByGroupId(String groupId) {
        return null;
    }

    @Override
    public void deleteFile(Long fileId) {

    }

    @Override
    public void deleteFilesByGroupId(String groupId) {

    }

    @Override
    public Long getNextFileGroupId() {
        // nextval()을 호출하여 다음 시퀀스 값을 가져옴
        Query query = entityManager.createNativeQuery(
                "SELECT nextval('group_sequence')"
        );
        return ((Number) query.getSingleResult()).longValue();
    }

    @Override
    public Long getCurrentValue() {
        Query query = entityManager.createNativeQuery(
                "SELECT currval('group_sequence')"
        );
        return ((Number) query.getSingleResult()).longValue();
    }
}
