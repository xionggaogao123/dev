package com.fulaan.user.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by moslpc on 2016/8/26.
 */
public class MapUtil {

    public static Map<String, Object> put(String key, Object value) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(key, value);
        return map;
    }
}
