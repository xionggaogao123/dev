package com.fulaan.utils;

import com.fulaan.indicator.dto.IndicatorDTO;
import com.fulaan.indicator.dto.IndicatorTreeDTO;
import com.fulaan.indicator.dto.IndicatorTreeSnapshotDTO;
import com.sys.props.Resources;
import org.json.JSONObject;
import org.springframework.web.client.RestTemplate;

/**
 * Created by guojing on 2016/11/17.
 */
public class RestAPIUtil {
    public static final String INDICATOR_URL = Resources.getProperty("indicator.server.name")==null?"http://116.62.5.65":Resources.getProperty("indicator.server.name");
    public static final RestTemplate restTemplate = new RestTemplate();

    /***************************************指标树管理开始***********************************************/
    /**
     * 分页查找指标树列表
     * @param schoolId
     * @param userId
     * @param name
     * @param page
     * @param pageSize
     * @return
     */
    public static String getIndicatorTreePageList(String schoolId, String userId, String name, int page, int pageSize)
    {
        String resoureUrl = INDICATOR_URL + "/indicatorTree/get/page/list/"+schoolId
                +"/"+userId+"/0/"+name+"/"+page+"/"+pageSize;
        String resultStr = restTemplate.getForObject(resoureUrl,String.class);
        return resultStr;
    }

    /**
     * 新增指标树
     * @param dto
     * @return
     */
    public static String addOrEditITree(IndicatorTreeDTO dto) {
        String resoureUrl = INDICATOR_URL + "/indicatorTree/post";
        String resultStr = restTemplate.postForObject(resoureUrl, dto, String.class);
        return resultStr;
    }

    /**
     * 查找指标树列表
     * @param schoolId
     * @param userId
     * @param owner
     * @param name
     * @return
     */
    public static String getIndicatorTreeList(String schoolId, String userId, int owner, String name) {
        String resoureUrl = INDICATOR_URL + "/indicatorTree/get/list/"+schoolId
                +"/"+userId+"/"+owner+"/"+name;
        String resultStr = restTemplate.getForObject(resoureUrl,String.class);
        return resultStr;
    }

    /**
     * 根据指标树id获取指标树信息
     * @param treeId
     * @return
     */
    public static String getIndicatorTreeById(String treeId) {
        String resoureUrl = INDICATOR_URL + "/indicatorTree/get/"+treeId;
        String resultStr = restTemplate.getForObject(resoureUrl, String.class);
        return resultStr;
    }


    public static void delITree(String id, String userId) {
        String resoureUrl = INDICATOR_URL + "/indicatorTree/delete/" + id + "/" + userId;
        restTemplate.delete(resoureUrl);
    }

    /***************************************指标树管理结束***********************************************/

    /***************************************指标管理开始***********************************************/
    /**
     * 查询指标列表信息
     * @param treeId
     * @param userId
     * @param parentId
     * @param level
     * @return
     */
    public static String getIndicatorList(String treeId, String userId, String parentId, int level) {
        String resoureUrl = INDICATOR_URL + "/indicator/level/get/"+treeId+"/"+userId+"/"+parentId+"/"+level;
        String resultStr = restTemplate.getForObject(resoureUrl,String.class);
        return resultStr;
    }

    /**
     * 查询指标列表信息
     * @param treeId
     * @param userId
     * @return
     */
    public static String getIndicatorList(String treeId, String userId) {
        String resoureUrl = INDICATOR_URL + "/indicator/all/get/"+treeId+"/"+userId;
        String resultStr = restTemplate.getForObject(resoureUrl,String.class);
        return resultStr;
    }

    /**
     * 根据指标id查询指标信息
     * @param id
     * @return
     */
    public static String getIndicatorById(String id) {
        String resoureUrl = INDICATOR_URL + "/indicator/get/"+id;
        String resultStr = restTemplate.getForObject(resoureUrl,String.class);
        return resultStr;
    }

    /**
     * 添加指标
     * @param dto
     * @return
     */
    public static String addOrEditIndicator(IndicatorDTO dto) {
        String resoureUrl = INDICATOR_URL + "/indicator/post";
        String resultStr = restTemplate.postForObject(resoureUrl, dto, String.class);
        return resultStr;
    }

    /**
     * 删除指标
     * @param id
     * @param userId
     */
    public static void delIndicator(String id, String userId) {
        String resoureUrl = INDICATOR_URL + "/indicator/delete/" + id + "/" + userId;
        restTemplate.delete(resoureUrl);
    }

    /***************************************指标管理结束***********************************************/

    /***************************************指标副本管理开始***********************************************/

    /**
     * 添加指标副本
     * @param dto
     * @return
     */
    public static String createIndicatorTreeSnapshot(IndicatorTreeSnapshotDTO dto) {
        String resoureUrl = INDICATOR_URL + "/snapshot/post";
        String resultStr = restTemplate.postForObject(resoureUrl, dto, String.class);
        String snapshotId = "";
        try{
            JSONObject dataJson = new JSONObject(resultStr);
            JSONObject message = dataJson.getJSONObject("message");
            snapshotId = message.getString("id");
        }catch (Exception e){
            e.printStackTrace();
        }
        return snapshotId;
    }

    /**
     * 根据指标id查询指标副本信息
     * @param id
     * @return
     */
    public static String getITreeSnapshotById(String id) {
        String resoureUrl = INDICATOR_URL + "/snapshot/get/"+id;
        String resultStr = restTemplate.getForObject(resoureUrl,String.class);
        return resultStr;
    }

    /**
     * 查询指标副本列表信息
     * @param id
     * @param parentId
     * @param level
     * @return
     */
    public static String getITreeSnapshotList(String id, String parentId, int level) {
        String resoureUrl = INDICATOR_URL + "/snapshot/get/level/"+id+"/"+parentId+"/"+level;
        String resultStr = restTemplate.getForObject(resoureUrl,String.class);
        return resultStr;
    }
    /***************************************指标副本管理结束***********************************************/
}
