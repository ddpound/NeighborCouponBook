package com.neighborcouponbook.controller.auth;

import com.neighborcouponbook.common.annotation.MenuInformation;
import com.neighborcouponbook.service.CouponBookFileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Files;
import java.nio.file.Paths;

@Log4j2
@RequiredArgsConstructor
@RequestMapping(value = "/auth/file")
@RestController
public class AuthFileController {

    private final CouponBookFileService couponBookFileService;

    @Operation(
            summary = "이미지 파일 로드",
            description = "이미지 파일을 불러오는 API",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(schema = @Schema(implementation = Long.class))
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "조회 성공",
                            useReturnTypeSchema = true
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "서버 에러"
                    )
            }
    )
    @GetMapping("image/{fileId}")
    public ResponseEntity<Resource> getShopImage(@PathVariable("fileId") Long fileId) {
        try {
            Resource imageResource = couponBookFileService.downloadFile(fileId);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + imageResource.getFilename() + "\"")
                    .body(imageResource);
        } catch (Exception e) {
            log.error("이미지 로딩 중 에러 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
