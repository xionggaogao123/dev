package com.fulaan.indicator.service;

import com.db.user.UserDao;
import com.fulaan.indicator.dto.IndicatorDTO;
import com.fulaan.indicator.dto.IndicatorTreeDTO;
import com.fulaan.utils.RestAPIUtil;
import com.mongodb.BasicDBObject;
import com.pojo.user.UserEntry;
import com.sys.utils.JsonUtil;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by guojing on 2016/11/10.
 */
@Service
public class IndicatorManageService {

    private UserDao userDao=new UserDao();

    public Map<String, Object> getITreePageListMap(String schoolId, String userId, String name, int page, int pageSize) {
        String resultStr = RestAPIUtil.getIndicatorTreePageList(schoolId, userId, name, page, pageSize);
        Map<String, Object> map=new HashMap<String, Object>();
        try{
            JSONObject dataJson = new JSONObject(resultStr);
            JSONObject message = dataJson.getJSONObject("message");
            int total = message.getInt("count");
            JSONArray rows = message.getJSONArray("row");
            List<ObjectId> uids = new ArrayList<ObjectId>();
            List<IndicatorTreeDTO> list = new ArrayList<IndicatorTreeDTO>();
            if(rows!=null&&rows.length()>0) {
                for (int j = 0; j < rows.length(); j++) {
                    JSONObject info = rows.getJSONObject(j);
                    IndicatorTreeDTO dto = (IndicatorTreeDTO) JsonUtil.JSONToObj(info.toString(), IndicatorTreeDTO.class);
                    uids.add(new ObjectId(dto.getCreaterId()));
                    list.add(dto);
                }
                Map<ObjectId, UserEntry> userMap = userDao.getUserEntryMap(uids, new BasicDBObject("nm",1));
                UserEntry userEntry = null;
                for(IndicatorTreeDTO dto:list) {
                    userEntry = userMap.get(new ObjectId(dto.getCreaterId()));
                    if(userEntry != null) {
                        dto.setCreaterName(userEntry.getUserName());
                    }
                    dto.setCreateDate(dto.getCreateDate().substring(0,10));
                }
            }
            map.put("total", total);
            map.put("page", page);
            map.put("pageSize", pageSize);
            map.put("rows", list);
        }catch (Exception e){
            e.printStackTrace();
        }
        return map;
    }


    public IndicatorTreeDTO getITreeDTO(String treeId) {
        String resultStr=RestAPIUtil.getIndicatorTreeById(treeId);
        IndicatorTreeDTO dto = new IndicatorTreeDTO();
        try{
            JSONObject dataJson = new JSONObject(resultStr);
            JSONObject message = dataJson.getJSONObject("message");
            dto = (IndicatorTreeDTO) JsonUtil.JSONToObj(message.toString(), IndicatorTreeDTO.class);
            if(dto.getId()!=null) {
                UserEntry user = userDao.getUserEntry(new ObjectId(dto.getCreaterId()), new BasicDBObject("nm", 1));
                if(user != null) {
                    dto.setCreaterName(user.getUserName());
                }
                dto.setCreateDate(dto.getCreateDate().substring(0, 10));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return dto;
    }

    public List<IndicatorTreeDTO> getITreeList(String schoolId, String userId, int owner, String name) {
        String resultStr = RestAPIUtil.getIndicatorTreeList(schoolId, userId, owner, name);
        List<IndicatorTreeDTO> list = new ArrayList<IndicatorTreeDTO>();
        try{
            JSONObject dataJson = new JSONObject(resultStr);
            JSONArray rows = dataJson.getJSONArray("message");
            List<ObjectId> uids = new ArrayList<ObjectId>();
            if(rows!=null&&rows.length()>0) {
                for (int j = 0; j < rows.length(); j++) {
                    JSONObject info = rows.getJSONObject(j);
                    IndicatorTreeDTO dto = (IndicatorTreeDTO) JsonUtil.JSONToObj(info.toString(), IndicatorTreeDTO.class);
                    uids.add(new ObjectId(dto.getCreaterId()));
                    list.add(dto);
                }
                Map<ObjectId, UserEntry> userMap = userDao.getUserEntryMap(uids, new BasicDBObject("nm",1));
                UserEntry userEntry = null;
                for(IndicatorTreeDTO dto:list) {
                    userEntry = userMap.get(new ObjectId(dto.getCreaterId()));
                    if(userEntry != null) {
                        dto.setCreaterName(userEntry.getUserName());
                    }
                    dto.setCreateDate(dto.getCreateDate().substring(0,10));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }

    public String addOrEditITree(IndicatorTreeDTO dto) {
        String resultStr=RestAPIUtil.addOrEditITree(dto);
        return resultStr;
    }

    public void delIndicatorTree(String id, String userId) {
        RestAPIUtil.delITree(id, userId);
    }

    public String getIndicatorList(String treeId, String userId, String parentId, int level) {
        String resultStr=RestAPIUtil.getIndicatorList(treeId, userId, parentId, level);
        return resultStr;
    }

    public String getIndicatorById(String id) {
        String resultStr=RestAPIUtil.getIndicatorById(id);
        return resultStr;
    }

    public String addOrEditIndicator(IndicatorDTO dto) {
        String resultStr=RestAPIUtil.addOrEditIndicator(dto);
        return resultStr;
    }

    public void delIndicator(String id, String userId) {
        RestAPIUtil.delIndicator(id, userId);
    }
}
