package com.neighborcouponbook.common.util;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateCommonUtil {

    /**
     * 오늘 날짜를 지정된 형식의 문자열로 반환합니다.
     * @param format 날짜 형식
     * @return 형식화된 날짜 문자열
     */
    public static String getFormattedDate(String format) {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return today.format(formatter);
    }

}
