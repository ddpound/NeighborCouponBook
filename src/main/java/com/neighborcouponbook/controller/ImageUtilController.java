package com.neighborcouponbook.controller;

import com.neighborcouponbook.service.ImageUtilService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Log4j2
@RequiredArgsConstructor
@RequestMapping(value = "image-util")
@RestController
public class ImageUtilController {

    private final ImageUtilService imageUtilService;

    @PostMapping("/remove-background")
    public ResponseEntity<byte[]> removeBackground(
            @RequestParam("image") MultipartFile file) {
        try {
            // MultipartFile을 BufferedImage로 변환
            BufferedImage originalImage = ImageIO.read(file.getInputStream());

            // 배경 제거
            BufferedImage processedImage = imageUtilService.removeWithBackground(originalImage);

            // 결과 이미지를 byte 배열로 변환
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(processedImage, "PNG", baos);
            byte[] imageBytes = baos.toByteArray();

            // HTTP 응답 설정
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);
            headers.setContentLength(imageBytes.length);

            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);

        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
