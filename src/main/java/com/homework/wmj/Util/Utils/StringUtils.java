package com.homework.wmj.Util.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StringUtils {
    
    public static Boolean isBlank(String str){
        return str == null || str.equals("");
    }

    public static String convertChatPart(Integer userAId, Integer userBId){
        if(userAId < userBId){
            return userAId + "-" + userBId;
        }else{
            return userBId + "-" + userAId;
        }
    }

    public static String excludeColumn(String columns,List<String> excludesColumns){
        String[] split = columns.split(",");
        List<String> columnsList = new ArrayList<>(Arrays.asList(split));
        columnsList = columnsList.stream()
                .filter(column -> {
                    boolean result = true;
                    for (String excludesColumn : excludesColumns) {
                        if (column.contains(excludesColumn)) {
                            result = false;
                        }
                    }
                    return result;
                }).collect(Collectors.toList());
        String result = org.apache.commons.lang3.StringUtils.join(columnsList, ",");
        return result;
    }

}
