package com.neighborcouponbook.common.util;

public class NullChecker {

    public static Boolean isNull(Object obj) {
        return obj == null;
    }

    public static Boolean isNullString(Object obj) {
        if(obj == null) return true;

        if(obj instanceof String str){
            return str.isEmpty();
        }

        return false;
    }
}
