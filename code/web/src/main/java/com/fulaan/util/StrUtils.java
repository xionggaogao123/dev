package com.fulaan.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by moslpc on 2016/11/30.
 */
public class StrUtils {

    public static List<String> splitToList(String str) {
        String[] strs = str.split(",");
        List<String> strList = new ArrayList<String>();
        Collections.addAll(strList,strs);
        return strList;
    }
}
