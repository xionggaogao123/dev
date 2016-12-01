package com.fulaan.util;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by moslpc on 2016/11/30.
 */
public class StrUtils {

    public static List<String> splitToList(String str) {
        List<String> strList = new ArrayList<String>();
        if(StringUtils.isNotBlank(str)) {
            String[] strs = str.split(",");
            Collections.addAll(strList,strs);
        }
        return strList;
    }
}
