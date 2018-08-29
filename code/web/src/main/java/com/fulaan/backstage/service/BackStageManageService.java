package com.fulaan.backstage.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.db.backstage.JurisdictionTreeDao;
import com.db.backstage.UserRoleJurisdictionDao;
import com.fulaan.backstage.dto.UserRoleJurisdictionDto;
import com.pojo.backstage.JurisdictionTreeEntry;
import com.pojo.backstage.UserRoleJurisdictionEntry;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Auther: taotao.chan
 * @Date: 2018/8/22 16:44
 * @Description:
 */
@Service
public class BackStageManageService {

    UserRoleJurisdictionDao jurisdictionDao = new UserRoleJurisdictionDao();
    JurisdictionTreeDao jurisdictionTreeDao = new JurisdictionTreeDao();

    public String saveJurisdiction(Map map) {
        String msg = "";
        if ("".equals(map.get("id")) || null == map.get("id")) {
            //获取路径，组装成list
            List<String> pathList = new ArrayList<String>();
            String[] str = map.get("path") == null ? null : map.get("path").toString().split(",");
            for (int i = 0; i < str.length; i++) {
                if (!pathList.contains(str[i])) {
                    pathList.add(str[i]);
                }
            }
            //获取社区行为权限，组装成list
            List<String> comList = new ArrayList<String>();
            String[] comStr = map.get("community") == null ? null : map.get("community").toString().split(",");
            for (int i = 0; i < comStr.length; i++) {
                if (!comList.contains(comStr[i])) {
                    comList.add(comStr[i]);
                }
            }
            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date now = new Date();
            UserRoleJurisdictionEntry jurisdictionEntry = new UserRoleJurisdictionEntry(
                    map.get("level").toString(),
                    comList,
                    pathList,
                    new ObjectId(map.get("userId").toString()),
                    f.format(now),
                    new ObjectId(map.get("userId").toString()),
                    f.format(now)
            );
            msg = jurisdictionDao.addEntry(jurisdictionEntry);
        } else {
            //获取路径，组装成list
            List<String> pathList = new ArrayList<String>();
            if (map.get("path") != null) {
                String[] str = map.get("path").toString().split(",");
                for (int i = 0; i < str.length; i++) {
                    if (!pathList.contains(str[i])) {
                        pathList.add(str[i]);
                    }
                }
            }
            //获取社区行为权限，组装成list
            List<String> comList = new ArrayList<String>();
            if (map.get("community") != null) {
                String[] comStr = map.get("community").toString().split(",");
                for (int i = 0; i < comStr.length; i++) {
                    if (!comList.contains(comStr[i])) {
                        comList.add(comStr[i]);
                    }
                }
            }
            msg = jurisdictionDao.updateRoleJurisdiction(map);
        }
        return msg;
    }

    public List<UserRoleJurisdictionDto> getJurisdiction(Map map) {
        List<UserRoleJurisdictionEntry> jurisdictionEntries = jurisdictionDao.getJurisdiction(map);
        List<UserRoleJurisdictionDto> jurisdictionDtos = new ArrayList<UserRoleJurisdictionDto>();
        for (UserRoleJurisdictionEntry entry : jurisdictionEntries) {
            jurisdictionDtos.add(new UserRoleJurisdictionDto(entry));
        }
        return jurisdictionDtos;
    }

    public Object getJurisdictionTree(Map map) {
        Object object = null;
        List<JurisdictionTreeEntry> entryList = jurisdictionTreeDao.getJurisdictionTree(map);
        JSONObject pajsObject = null;
        for (JurisdictionTreeEntry entry : entryList) {
            //查找跟节点
            if (StringUtils.isEmpty(entry.getParentId())) {
                //开始封装tree的json
                pajsObject = new JSONObject();
//                pajsObject.put("id",entry.getID().toString());
                pajsObject.put("id", "");
                pajsObject.put("label", entry.getName());
                //判断跟节点是否有children子节点
                for (JurisdictionTreeEntry entryTemp : entryList) {
                    if (entry.getID().toString().equals(entryTemp.getParentId())) {
                        pajsObject.put("children", getChildren(entryList, entry.getID().toString()));
                    }
                }
            }
        }
        object = pajsObject;
        return object;
    }

    /**
     * 递归寻找子节点
     *
     * @param entryList
     * @param id
     * @return
     */
    private Object getChildren(List<JurisdictionTreeEntry> entryList, String id) {
        Object object = null;
        JSONArray jsonArray = new JSONArray();
        for (JurisdictionTreeEntry entry : entryList) {
            //parentId不为空 为子节点
            if (!StringUtils.isEmpty(entry.getParentId())) {
                //上一节点的Id匹配当前节点的parentId 匹配成功 当前节点则为上一节点的子节点
                if (id.equals(entry.getParentId())) {
                    //开始封装tree的json
                    JSONObject pajsObject = pajsObject = new JSONObject();
//                    pajsObject.put("id",entry.getID().toString());
                    pajsObject.put("label", entry.getName());
                    //判断跟节点是否有子children节点
                    for (JurisdictionTreeEntry entryTemp : entryList) {
                        //判断当前节点是否有children子节点 有子节点 Id就封装成空
                        if (entry.getID().toString().equals(entryTemp.getParentId())) {
                            pajsObject.put("id", "");
                            pajsObject.put("children", getChildren(entryList, entry.getID().toString()));
                        } else {
//                            当前节点为叶子节点
                            pajsObject.put("id", entry.getID().toString());
                        }
                    }
                    jsonArray.add(pajsObject);
                }

            }
        }
        object = jsonArray;
        return object;
    }

}
