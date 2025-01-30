package com.neighborcouponbook.service.impl;

import com.neighborcouponbook.service.ImageUtilService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.image.BufferedImage;

@RequiredArgsConstructor
@Log4j2
@Service
public class ImageUtilServiceImpl implements ImageUtilService {

    // 흰색 임계값 (0-255)
    private static final int WHITE_THRESHOLD = 245;

    @Override
    public BufferedImage removeWithBackground(BufferedImage originalImage) {

        // ARGB 형식으로 새 이미지 생성
        BufferedImage newImage = new BufferedImage(
                originalImage.getWidth(),
                originalImage.getHeight(),
                BufferedImage.TYPE_INT_ARGB
        );

        // 각 픽셀을 순회하면서 처리
        for (int x = 0; x < originalImage.getWidth(); x++) {
            for (int y = 0; y < originalImage.getHeight(); y++) {
                // 원본 이미지의 RGB 값 가져오기
                int rgb = originalImage.getRGB(x, y);

                // RGB 컴포넌트 추출
                int red = (rgb >> 16) & 0xff;
                int green = (rgb >> 8) & 0xff;
                int blue = rgb & 0xff;

                // 픽셀이 흰색에 가까운지 확인
                if (red >= WHITE_THRESHOLD &&
                        green >= WHITE_THRESHOLD &&
                        blue >= WHITE_THRESHOLD) {
                    // 완전 투명하게 설정
                    newImage.setRGB(x, y, new Color(0, 0, 0, 0).getRGB());
                } else {
                    // 원본 색상 유지
                    newImage.setRGB(x, y, rgb);
                }
            }
        }

        return newImage;
    }
}
