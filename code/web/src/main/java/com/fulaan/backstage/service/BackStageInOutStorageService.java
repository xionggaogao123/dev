package com.fulaan.backstage.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.db.backstage.InOutStorageRecordDao;
import com.db.backstage.PhonesProjectDao;
import com.db.backstage.StorageManageDao;
import com.fulaan.backstage.dto.InOutStorageRecordDto;
import com.fulaan.backstage.dto.PhonesProjectDto;
import com.mongodb.DBObject;
import com.pojo.backstage.InOutStorageEntry;
import com.pojo.backstage.PhonesProjectEntry;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: taotao.chan
 * @Date: 2018-9-20 09:51:06
 * @Description:
 */
@Service
public class BackStageInOutStorageService {

    private PhonesProjectDao phonesProjectDao = new PhonesProjectDao();

    private InOutStorageRecordDao inOutStorageRecordDao = new InOutStorageRecordDao();

    private StorageManageDao storageManageDao = new StorageManageDao();

    public void batchOutStorage(Map map) {
        /**
         * 判断是个人 还是 项目
         */
        String projectId = "";
        if (map.get("outStorageType") != null && map.get("outStorageType").toString() == "1"){
            /**
             * 对项目信息新增或者更新
             */
            //没有projectId 就新增

            if (map.get("projectId") != null && map.get("projectId").toString() == "" ){
                projectId = phonesProjectDao.saveProjectEntry(new PhonesProjectEntry(
                        //构造无Id
                        map.get("projectName").toString(),
                        map.get("projectDockPeople").toString(),
                        map.get("schoolName").toString(),
                        map.get("accessClass").toString(),
                        map.get("accessObj").toString(),
                        map.get("contactInfo").toString(),
                        map.get("address").toString()
                ));
            }else {
                projectId = phonesProjectDao.saveProjectEntry(new PhonesProjectEntry(
                        //构造有Id
                        new ObjectId(map.get("projectId").toString()),
                        map.get("projectName").toString(),
                        map.get("projectDockPeople").toString(),
                        map.get("schoolName").toString(),
                        map.get("accessClass").toString(),
                        map.get("accessObj").toString(),
                        map.get("contactInfo").toString(),
                        map.get("address").toString()
                ));
            }
        }else {

        }

        //增加项目出库信息
        map.put("projectId", projectId);
        addProjectOutStorageRecord(map);

        //对库存手机状态更新成出库状态 5
        storageManageDao.updateStorageInfoByIds(map);
    }

    /**
     * 增加项目出库信息
     * @param map
     */
    private void addProjectOutStorageRecord(Map map) {
        if (map.get("storageListInfo") != null && map.get("storageListInfo").toString() != "") {
            //解析数据
            String storageListInfo = map.get("storageListInfo").toString();
            JSONObject jsonObject = JSONObject.parseObject(storageListInfo);
            JSONArray jsonArray = (JSONArray)jsonObject.get("dataList");

            //解析并封装
            List<DBObject> dbObjectList = new ArrayList<DBObject>();
            InOutStorageEntry inOutStorageEntry = null;
            JSONObject tjsonIn;
            List<String> stringlist = new ArrayList<String>();
            for (int i = 0; i < jsonArray.size(); i++){
                tjsonIn = (JSONObject)jsonArray.get(i);
//                System.out.println(tjsonIn);
                inOutStorageEntry = new InOutStorageEntry(
                        tjsonIn.get("imeiNo") == null ? "" : tjsonIn.get("imeiNo").toString(),
                        tjsonIn.get("phoneModel") == null ? "" : tjsonIn.get("phoneModel").toString(),
                        tjsonIn.get("color") == null ? "" : tjsonIn.get("color").toString(),
                        tjsonIn.get("manufacturer") == null ? "" : tjsonIn.get("manufacturer").toString(),
                        tjsonIn.get("inStorageTime") == null ? "" : tjsonIn.get("inStorageTime").toString(),
                        tjsonIn.get("inStorageYear") == null ? "" : tjsonIn.get("inStorageYear").toString(),
                        tjsonIn.get("inStorageMonth") == null ? "" : tjsonIn.get("inStorageMonth").toString(),
                        tjsonIn.get("storageStatus") == null ? "" : tjsonIn.get("storageStatus").toString(),
                        tjsonIn.get("comment") == null ? "" : tjsonIn.get("comment").toString(),
                        map.get("projectId") == null ? "" : map.get("projectId").toString(),
                        map.get("projectName") == null ? "" : map.get("projectName").toString(),
                        map.get("projectDockPeople") == null ? "" : map.get("projectDockPeople").toString(),
                        map.get("schoolName") == null ? "" : map.get("schoolName").toString(),
                        map.get("accessClass") == null ? "" : map.get("accessClass").toString(),
                        map.get("accessObj") == null ? "" : map.get("accessObj").toString(),
                        map.get("contactInfo") == null ? "" : map.get("contactInfo").toString(),
                        map.get("address") == null ? "" : map.get("address").toString(),
                        map.get("deliveryTime") == null ? "" : map.get("deliveryTime").toString(),
                        map.get("deliveryMethod") == null ? "" : map.get("deliveryMethod").toString(),
                        "",
                        "",
                        map.get("parentName") == null ? "" : map.get("parentName").toString(),
                        map.get("parentMobile") == null ? "" : map.get("parentMobile").toString(),
                        map.get("parentId") == null ? "" : map.get("parentId").toString(),
                        map.get("studentName") == null ? "" : map.get("studentName").toString(),
                        "",
                        "",
                        "",
                        "",
                        "",
                        "5",//出库
                        tjsonIn.get("commentType") == null ? "" : tjsonIn.get("commentType").toString(),
//                        tjsonIn.get("needRepairComment") == null ? stringlist : (ArrayList<String>)tjsonIn.get("needRepairComment")
                        tjsonIn.get("needRepairComment") == null ? stringlist : jsontoList(tjsonIn.get("needRepairComment"))
                );
                dbObjectList.add(inOutStorageEntry.getBaseEntry());
            }
            inOutStorageRecordDao.addProjectOutStorageRecordList(dbObjectList);
        }
    }

    private List<String> jsontoList(Object needRepairComment) {
        List<String> stringList = new ArrayList<String>();

        String needRepairCommentString = needRepairComment.toString().replace("[","").replace("]","");
//        System.out.println(needRepairCommentString);
        String[] strings= needRepairCommentString.split(",");
        for (String string : strings){
            stringList.add(string);
        }
        System.out.println(stringList);
        return stringList;
    }


    /**
     * 项目列表
     * @return
     */
    public List<PhonesProjectDto> getProjectList() {
        List<PhonesProjectDto> phonesProjectDtoList = new ArrayList<PhonesProjectDto>();
        List<PhonesProjectEntry> phonesProjectEntryList = phonesProjectDao.getAllProjects();
        for (PhonesProjectEntry entry : phonesProjectEntryList){
            phonesProjectDtoList.add(new PhonesProjectDto(entry));
        }
        return phonesProjectDtoList;
    }

    /**
     * 获取出库记录
     * @param page
     * @param pageSize
     * @param inputParams
     * @param year
     * @param month
     * @return
     */
    public Map<String,Object> getOutStorageHistoryList(int page, int pageSize, String inputParams, String year, String month) {
        Map<String,Object> result = new HashMap<String, Object>();
        result = inOutStorageRecordDao.getOutStorageHistoryList(page, pageSize, inputParams, year, month);
        List<InOutStorageRecordDto> inOutStorageRecordDtoList = new ArrayList<InOutStorageRecordDto>();
        List<InOutStorageEntry> inOutStorageEntryList = (ArrayList)result.get("entryList");
        for (InOutStorageEntry inOutStorageEntry : inOutStorageEntryList){
            if (inOutStorageEntry != null){
                inOutStorageRecordDtoList.add(new InOutStorageRecordDto(inOutStorageEntry));
            }
        }
        result.put("dtos",inOutStorageRecordDtoList);
        result.remove("entryList");
        return result;
    }
}
