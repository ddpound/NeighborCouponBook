package com.neighborcouponbook.service.impl;

import com.neighborcouponbook.common.response.FileUploadResponse;
import com.neighborcouponbook.common.response.ZipResource;
import com.neighborcouponbook.common.util.DateCommonUtil;
import com.neighborcouponbook.model.CouponBookFile;
import com.neighborcouponbook.model.vo.CouponBookFileVo;
import com.neighborcouponbook.repository.CouponBookFileRepository;
import com.neighborcouponbook.service.CouponBookFileService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Log4j2
@RequiredArgsConstructor
@Service
public class CouponBookFileServiceImpl implements CouponBookFileService {

    @Value("${file-manage.root-file-path}")
    private String rootFilePath;

    @Value("${file-manage.folder-name}")
    private String fileFolderName;

    @PersistenceContext
    private EntityManager entityManager;

    private final CouponBookFileRepository couponBookFileRepository;

    @Override
    public Resource downloadFile(Long fileId) {
        try {
            CouponBookFile fileEntity = couponBookFileRepository.findById(fileId)
                    .orElseThrow(() -> new FileNotFoundException("File not found with id: " + fileId));

            Path filePath = Paths.get(fileEntity.getPhysicalFilePath()).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if(!resource.exists()) {
                throw new FileNotFoundException("File not found: " + fileEntity.getPhysicalFilePath());
            }

            return resource;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public FileUploadResponse uploadFiles(List<MultipartFile> files) {

        try{
            long serialNo = 1L;

            // 오늘 날짜의 폴더
            long newFileGroupId = getNextFileGroupId();
            List<CouponBookFileVo> newFiles = new ArrayList<>();

            for (MultipartFile file : files) {
                // 파일 저장 경로 생성
                Path uploadPath = Paths.get(rootFilePath)
                        .resolve(fileFolderName)
                        .resolve(DateCommonUtil.getFormattedDate("yyyymmdd"))
                        .toAbsolutePath()
                        .normalize();

                Files.createDirectories(uploadPath);

                // 파일명 중복 방지를 위한 고유 파일명 생성
                String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
                String fileExtension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
                String uniqueFileName = UUID.randomUUID() + "." + fileExtension;

                // 파일 저장
                Path targetLocation = uploadPath.resolve(uniqueFileName);
                Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

                // DB에 파일 정보 저장
                CouponBookFile fileEntity = new CouponBookFile();
                fileEntity.initCouponBookFile(newFileGroupId, serialNo++, uniqueFileName, uploadPath.toString(), originalFileName);
                CouponBookFile couponBookFile = couponBookFileRepository.save(fileEntity);
                newFiles.add(new CouponBookFileVo().convertToVo(couponBookFile));
            }

            return FileUploadResponse.builder().fileGroupId(newFileGroupId).files(newFiles).build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ZipResource downloadFilesByGroupId(Long fileGroupId) {
        try {
            List<CouponBookFile> files = couponBookFileRepository.findByFileGroupId(fileGroupId);
            if (files.isEmpty()) {
                throw new FileNotFoundException("No files found for group: " + fileGroupId);
            }

            // 임시 ZIP 파일 생성
            Path tempFile = Files.createTempFile("download-", ".zip");

            try (ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(tempFile))) {
                for (CouponBookFile file : files) {
                    Path filePath = Paths.get(file.getPhysicalFilePath()).normalize();
                    ZipEntry entry = new ZipEntry(file.getOriginalFileName());
                    zos.putNextEntry(entry);
                    Files.copy(filePath, zos);
                    zos.closeEntry();
                }
            }

            return new ZipResource(tempFile, "files.zip");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void deleteFile(Long fileId) {
        try {
            CouponBookFile fileEntity = couponBookFileRepository.findById(fileId)
                    .orElseThrow(() -> new FileNotFoundException("File not found with id: " + fileId));

            Path filePath = Paths.get(rootFilePath).resolve(fileEntity.getPhysicalFilePath()).normalize();
            Files.deleteIfExists(filePath);
            couponBookFileRepository.delete(fileEntity);
        }catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void deleteFilesByGroupId(Long groupId) {
        try {
            List<CouponBookFile> files = couponBookFileRepository.findByFileGroupId(groupId);
            for (CouponBookFile file : files) {
                Path filePath = Paths.get(file.getPhysicalFilePath()).normalize();
                Files.deleteIfExists(filePath);
            }
            couponBookFileRepository.deleteAll(files);
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
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

    /**
     * 오늘 날짜의 파일 폴더를 만들어주는 메소드
     * */
    @Override
    public void todayFileFolder() {
        try {
            String todayStr = DateCommonUtil.getFormattedDate("yyyymmdd");
            File directory = new File(rootFilePath + File.separator + todayStr);

            if (!directory.exists()) {
                boolean created = directory.mkdirs();
                if (!created) {
                    throw new RuntimeException("Failed to create directory: " + directory.getAbsolutePath());
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
