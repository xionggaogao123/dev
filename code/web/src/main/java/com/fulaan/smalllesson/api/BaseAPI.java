package com.fulaan.smalllesson.api;

import com.sys.props.Resources;
import org.springframework.web.client.RestTemplate;

/**
 * Created by guojing on 2017/1/5.
 */
public class BaseAPI {
    public static final String BO_URL = Resources.getProperty("bo.server.name");
    public static final String TA_URL = Resources.getProperty("ta.server.name");
    public static final String SMALL_URL = "http://121.41.46.29";
    public static final RestTemplate restTemplate = new RestTemplate();

    public static final String SERVICE_URL = "http://gateway.system.eduyun.cn:40015";
    public static final String CC_URL = "http://api.csslcloud.net/api";
    public static final String GANKAO_URL = "https://www.gankao.com/api";
    /**
     * get方法
     * @param resoureUrl
     * @return
     */
    public static String getForObject(String resoureUrl) {
        resoureUrl = BO_URL + resoureUrl;
        String resultStr = restTemplate.getForObject(resoureUrl, String.class);
        return resultStr;
    }

    /**
     * post方法
     * @param resoureUrl
     * @return
     */
    public static String postForObject(String resoureUrl, Object obj) {
        resoureUrl = BO_URL + resoureUrl;
        String resultStr = restTemplate.postForObject(resoureUrl, obj, String.class);
        return resultStr;
    }

    /**
     * 平台post方法
     * @param resoureUrl
     * @return
     */
    public static String postForToken(String resoureUrl, Object obj) {
        resoureUrl = SERVICE_URL + resoureUrl;
        String resultStr = restTemplate.postForObject(resoureUrl, obj, String.class);
        return resultStr;
    }

    /**
     * 平台get方法
     * @param resoureUrl
     * @return
     */
    public static String getForToken(String resoureUrl) {
        resoureUrl = SERVICE_URL + resoureUrl;
        String resultStr = restTemplate.getForObject(resoureUrl, String.class);
        return resultStr;
    }

    /**
     * CC get方法
     * @param resoureUrl
     * @return
     */
    public static String getCCForToken(String resoureUrl) {
        resoureUrl = CC_URL + resoureUrl;
        String resultStr = restTemplate.getForObject(resoureUrl, String.class);
        return resultStr;
    }

    /**
     * 赶考网 get方法
     * @param resoureUrl
     * @return
     */
    public static String getGanKaoForToken(String resoureUrl) {
        resoureUrl = GANKAO_URL + resoureUrl;
        String resultStr = restTemplate.getForObject(resoureUrl, String.class);
        return resultStr;
    }

    /**
     * CC get方法
     * @param resoureUrl
     * @return
     */
    public static String getZhanCCForToken(String resoureUrl,String json) {
        resoureUrl = CC_URL + resoureUrl;
        String resultStr = restTemplate.getForObject(resoureUrl, String.class,json);
        return resultStr;
    }

    /**
     * put方法
     * @param resoureUrl
     * @return
     */
    public static void put(String resoureUrl, Object obj) {
        resoureUrl = BO_URL + resoureUrl;
        restTemplate.put(resoureUrl, obj, String.class);
    }

    /**
     * delete方法
     * @param resoureUrl
     * @return
     */
    public static void delete(String resoureUrl) {
        resoureUrl = BO_URL + resoureUrl;
        restTemplate.delete(resoureUrl);
    }

    /**
     * get方法
     * @param resoureUrl
     * @return
     */
    public static String getTaForObject(String resoureUrl) {
        resoureUrl = TA_URL + resoureUrl;
        String resultStr = restTemplate.getForObject(resoureUrl, String.class);
        return resultStr;
    }

    /**
     * get方法
     * @param resoureUrl
     * @return
     */
    public static String getLessonForObject(String resoureUrl) {
        resoureUrl = SMALL_URL + resoureUrl;
        String resultStr = restTemplate.getForObject(resoureUrl, String.class);
        return resultStr;
    }

    /**
     * post方法
     * @param resoureUrl
     * @return
     */
    public static String postTaForObject(String resoureUrl, Object obj) {
        resoureUrl = TA_URL + resoureUrl;
        String resultStr = restTemplate.postForObject(resoureUrl, obj, String.class);
        return resultStr;
    }

    /**
     * post方法
     * @param resoureUrl
     * @return
     */
    public static void taPut(String resoureUrl, Object obj) {
        resoureUrl = TA_URL + resoureUrl;
        restTemplate.put(resoureUrl, obj, String.class);
    }

    /**
     * delete方法
     * @param resoureUrl
     * @return
     */
    public static void taDelete(String resoureUrl) {
        resoureUrl = TA_URL + resoureUrl;
        restTemplate.delete(resoureUrl);
    }
}
