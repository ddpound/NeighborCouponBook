package com.neighborcouponbook.common.util;


import com.neighborcouponbook.model.vo.UserRoleVo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RoleListUtil {

    /**
     * userRole List 를 받아 ,를 붙여 String으로 반환
     * */
    public static StringBuilder roleListConvertToString(List<UserRoleVo> userRoleList){
        StringBuilder userRoleContactString = new StringBuilder();

        userRoleList.forEach(role ->{
            if(userRoleList.size() == 1){
                userRoleContactString.append(role.getRoleId());
            }else{
                userRoleContactString.append(role.getRoleId()).append(",");
            }
        });

        return userRoleContactString;
    }

    /**
     * String으로 된 role list 다시 리스트 화 반환
     * */
    public static List<Long> StringUserRoleConvertToUserRoleList(String roleListString){
        String[] stringList = roleListString.split(",");
        List<Long> returnList = new ArrayList<Long>();

        Arrays.stream(stringList).toList().forEach(longData -> {
            returnList.add(Long.parseLong(longData));
        });

        return returnList;
    }
}
