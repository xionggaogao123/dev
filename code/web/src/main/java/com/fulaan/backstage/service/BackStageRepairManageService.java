package com.fulaan.backstage.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.db.backstage.InOutStorageRecordDao;
import com.db.backstage.StorageManageDao;
import com.fulaan.backstage.dto.InOutStorageRecordDto;
import com.mongodb.DBObject;
import com.pojo.backstage.InOutStorageEntry;
import com.pojo.backstage.StorageManageEntry;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Auther: taotao.chan
 * @Date: 2018/9/25 10:44
 * @Description:
 */
@Service
public class BackStageRepairManageService {

    private static List<String> stringlist = new ArrayList<String>();
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static Calendar calendar = Calendar.getInstance();

    private InOutStorageRecordDao inOutStorageRecordDao = new InOutStorageRecordDao();

    private StorageManageDao storageManageDao = new StorageManageDao();



    /**
     * 维修管理-获取当前型号库存手机颜色
     * @return
     */
    public List<String> getCurrentModelColorForRepair(String phoneModel) {
        return storageManageDao.getCurrentModelColorForRepair(phoneModel);
    }


    /**
     * 维修管理-获取手机型号
     * @return
     */
    public List<String> getPhoneModel() {
        return storageManageDao.getPhoneModel();
    }

    /**
     * 维修管理-新增
     * @param map
     * @return
     */
    public String addRepairManage(Map map) {
        String result = "";
            //新增
        int existCount = storageManageDao.findDataByImeiNo(map.get("imeiNo").toString());
        //查看当前IMEI是否存在库存表
        // 存在不可在此处添加维修
        if(existCount > 0){
            result = "exist";
            return result;
        }
        //对拼接的维修范围做处理
        List<String> repairCommentList = new ArrayList<String>();
        if (map.get("repairCommentList") != null){
            for (String repairComment : map.get("repairCommentList").toString().split(",")){
                if (repairComment != ""){
                    repairCommentList.add(repairComment);
                }
            }
        }
        //查看当前IMEI是否存在库存表
        // 不存在即可添加
        if (existCount == 0){
            List<DBObject> dbObjectList = new ArrayList<DBObject>();
            //封装新增进库存数据
            StorageManageEntry storageManageEntry = new StorageManageEntry(
                    map.get("imeiNo") == null ? "" : map.get("imeiNo").toString(),
                    map.get("phoneModel") == null ? "" : map.get("phoneModel").toString(),
                    map.get("color") == null ? "" : map.get("color").toString(),
                    "",
                    "6",//待维修状态手机
                    map.get("comment") == null ? "" : map.get("comment").toString(),
                    "0",//不可用
                    "",
                    repairCommentList
            );
            dbObjectList.add(storageManageEntry.getBaseEntry());
            storageManageDao.saveList(dbObjectList);
            //封装出入库记录数据
            //封装出入库记录数据声明
//            List<DBObject> inOutDbObjectList = new ArrayList<DBObject>();
//            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            Calendar calendar = Calendar.getInstance();
//            InOutStorageEntry inOutStorageEntry = new InOutStorageEntry(
//                    map.get("imeiNo") == null ? "" : map.get("imeiNo").toString(),
//                    map.get("phoneModel") == null ? "" : map.get("phoneModel").toString(),
//                    map.get("color") == null ? "" : map.get("color").toString(),
//                    "",
//                    dateFormat.format(new Date()),
//                    calendar.get(Calendar.YEAR)+"",
//                    (calendar.get(Calendar.MONTH) + 1)+"",
//                    "6",//待维修状态手机
//                    map.get("comment") == null ? "" : map.get("comment").toString(),
//                    "",
//                    "",
//                    "",
//                    "",
//                    "",
//                    "",
//                    "",
//                    "",
//                    "",
//                    "",
//                    "",
//                    "",
//                    "",
//                    "",
//                    "",
//                    "",
//                    "",
//                    "",
//                    "",
//                    "",
//                    "",
//                    "6",//待维修状态手机
//                    "",
//                    repairCommentList,
//                    "0"//未读
//            );
//            inOutDbObjectList.add(inOutStorageEntry.getBaseEntry());
//            inOutStorageRecordDao.addProjectOutStorageRecordList(inOutDbObjectList);
        }


        InOutStorageEntry inOutStorageEntry = new InOutStorageEntry(
                map.get("imeiNo") == null ? "" : map.get("imeiNo").toString(),
                map.get("phoneModel") == null ? "" : map.get("phoneModel").toString(),
                map.get("color") == null ? "" : map.get("color").toString(),
                "",
                "",
                "",
                "",
                "-100",//手动维修添加
                map.get("comment") == null ? "" : map.get("comment").toString(),
                "",
                "",
                "",
                map.get("schoolName") == null ? "" : map.get("schoolName").toString(),
                map.get("accessClass") == null ? "" : map.get("accessClass").toString(),
                "",
                "",
                map.get("address") == null ? "" : map.get("address").toString(),
                "",
                "",
                "",
                "",
                map.get("parentName") == null ? "" : map.get("parentName").toString(),
                map.get("parentMobile") == null ? "" : map.get("parentMobile").toString(),
                map.get("parentId") == null ? "" : map.get("parentId").toString(),
                map.get("studentName") == null ? "" : map.get("studentName").toString(),
                "",
                "",
                "",
                map.get("repairCommentList") == null ? "" : map.get("repairCommentList").toString(),
                map.get("repairCost") == null ? "" : map.get("repairCost").toString(),
                "6",//待维修
                "",
                repairCommentList,
                map.get("isPay") == null ? "" : map.get("isPay").toString(),
                map.get("payFrom") == null ? "" : map.get("payFrom").toString(),
                "",
                "",
                "0"//未读
            );
        result = inOutStorageRecordDao.addRepairManage(inOutStorageEntry);
        return result;
    }

    /**
     * 维修管理-列表数据
     * @return
     */
    public Map<String,Object> getRepairManageList(int page, int pageSize, String inputParams, String year, String month, int isr) {
        Map<String,Object> result = new HashMap<String, Object>();
        result = inOutStorageRecordDao.getRepairManageList(page, pageSize, inputParams, year, month, isr);
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

    /**
     * 维修管理-修改
     * @param map
     * @return
     */
    public String updateRepairManage(Map map) {
        String result = "";
        result = inOutStorageRecordDao.updateRepairManage(map);
        return result;
    }

    /**
     * 维修管理-维修完成操作
     * @param map
     * @return
     */
    public List<String> completeRepairManage(Map map) {
        List<String> resultList = new ArrayList<String>();
        //更新待维修数据
        String repairId = inOutStorageRecordDao.updateRepairManage(map);
        resultList.add("更新待维修数据Id:"+repairId);
        //当维修完成，选择 出库发货
        if ("出库发货".equals(map.get("afterRepair").toString())){
            String result = completeRepairToOutStorage(map);
            resultList.add("出库发货:"+result);
        }
        //当维修完成，选择 手机入库
        if ("手机入库".equals(map.get("afterRepair").toString())){
            String result = completeRepairToInStorage(map);
            resultList.add("手机入库:"+result);
        }
        //当维修完成，选择 更换新手机
        if ("更换新手机".equals(map.get("afterRepair").toString())){
            String result = completeRepairToChangePhoneOutStorage(map);
            resultList.add("更换新手机:"+result);
        }
        return resultList;
    }

    /**
     * 当维修完成，选择 更换新手机
     * 更换新手机
     * 1 添加发货管理 imeiNo置空
     * 2 添加入库记录
     * 3 再把imeiNo库存激活
     * @param map
     * @return
     */
    private String completeRepairToChangePhoneOutStorage(Map map) {
        String result = "";
        //解析数据
        String rowPojoIn = map.get("rowPojoIn").toString();
        JSONObject jsonObject = JSONObject.parseObject(rowPojoIn);
        JSONObject tjsonIn = (JSONObject)jsonObject.get("dataIn");
        System.out.println(tjsonIn);
        //1 添加发货管理 imeiNo置空
        List<String> stringlist = new ArrayList<String>();
        InOutStorageEntry inOutStorageEntry = new InOutStorageEntry(
//                tjsonIn.get("imeiNo") == null ? "" : tjsonIn.get("imeiNo").toString(),
                "",
                tjsonIn.get("phoneModel") == null ? "" : tjsonIn.get("phoneModel").toString(),
                tjsonIn.get("color") == null ? "" : tjsonIn.get("color").toString(),
                tjsonIn.get("manufacturer") == null ? "" : tjsonIn.get("manufacturer").toString(),
                tjsonIn.get("inStorageTime") == null ? "" : tjsonIn.get("inStorageTime").toString(),
                tjsonIn.get("inStorageYear") == null ? "" : tjsonIn.get("inStorageYear").toString(),
                tjsonIn.get("inStorageMonth") == null ? "" : tjsonIn.get("inStorageMonth").toString(),
                tjsonIn.get("storageStatus") == null ? "" : tjsonIn.get("storageStatus").toString(),
                tjsonIn.get("comment") == null ? "" : tjsonIn.get("comment").toString(),
                tjsonIn.get("projectId") == null ? "" : tjsonIn.get("projectId").toString(),
                tjsonIn.get("projectName") == null ? "" : tjsonIn.get("projectName").toString(),
                tjsonIn.get("projectDockPeople") == null ? "" : tjsonIn.get("projectDockPeople").toString(),
                tjsonIn.get("schoolName") == null ? "" : tjsonIn.get("schoolName").toString(),
                tjsonIn.get("accessClass") == null ? "" : tjsonIn.get("accessClass").toString(),
                tjsonIn.get("accessObj") == null ? "" : tjsonIn.get("accessObj").toString(),
                tjsonIn.get("contactInfo") == null ? "" : tjsonIn.get("contactInfo").toString(),
                tjsonIn.get("address") == null ? "" : tjsonIn.get("address").toString(),
                tjsonIn.get("deliveryTime") == null ? "" : tjsonIn.get("deliveryTime").toString(),
                "快递",
                "",
                "",
                tjsonIn.get("parentName") == null ? "" : tjsonIn.get("parentName").toString(),
                tjsonIn.get("parentMobile") == null ? "" : tjsonIn.get("parentMobile").toString(),
                tjsonIn.get("parentId") == null ? "" : tjsonIn.get("parentId").toString(),
                tjsonIn.get("studentName") == null ? "" : tjsonIn.get("studentName").toString(),
                tjsonIn.get("studentMobile") == null ? "" : tjsonIn.get("studentMobile").toString(),
                tjsonIn.get("studentId") == null ? "" : tjsonIn.get("studentId").toString(),
                "",
                map.get("repairCommentList") == null ? "" : map.get("repairCommentList").toString(),
                map.get("repairCost") == null ? "" : map.get("repairCost").toString(),
                "5",//添加出库记录
                map.get("commentType") == null ? "" : map.get("commentType").toString(),
                map.get("needRepairComment") == null ? stringlist : jsontoList(map.get("needRepairComment")),
                map.get("isPay") == null ? "" : map.get("isPay").toString(),
                map.get("payFrom") == null ? "" : map.get("payFrom").toString(),
                map.get("afterRepair") == null ? "" : map.get("afterRepair").toString(),
                map.get("repairType") == null ? "" : map.get("repairType").toString(),
                "0"//未读
        );
        result +="发货记录：";
        result += inOutStorageRecordDao.addProjectOutStorageRecord(inOutStorageEntry);
        //2 添加入库记录
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        String inStorageTime = dateFormat.format(new Date());
        String inStorageYear = calendar.get(Calendar.YEAR)+"";
        String inStorageMonth = (calendar.get(Calendar.MONTH)+1)+"";

        InOutStorageEntry inOutStorageEntry1 = new InOutStorageEntry(
                tjsonIn.get("imeiNo") == null ? "" : tjsonIn.get("imeiNo").toString(),
                tjsonIn.get("phoneModel") == null ? "" : tjsonIn.get("phoneModel").toString(),
                tjsonIn.get("color") == null ? "" : tjsonIn.get("color").toString(),
                tjsonIn.get("manufacturer") == null ? "" : tjsonIn.get("manufacturer").toString(),
                inStorageTime,
                inStorageYear,
                inStorageMonth,
                tjsonIn.get("storageStatus") == null ? "" : tjsonIn.get("storageStatus").toString(),
                tjsonIn.get("comment") == null ? "" : tjsonIn.get("comment").toString(),
                tjsonIn.get("projectId") == null ? "" : tjsonIn.get("projectId").toString(),
                tjsonIn.get("projectName") == null ? "" : tjsonIn.get("projectName").toString(),
                tjsonIn.get("projectDockPeople") == null ? "" : tjsonIn.get("projectDockPeople").toString(),
                tjsonIn.get("schoolName") == null ? "" : tjsonIn.get("schoolName").toString(),
                tjsonIn.get("accessClass") == null ? "" : tjsonIn.get("accessClass").toString(),
                tjsonIn.get("accessObj") == null ? "" : tjsonIn.get("accessObj").toString(),
                tjsonIn.get("contactInfo") == null ? "" : tjsonIn.get("contactInfo").toString(),
                tjsonIn.get("address") == null ? "" : tjsonIn.get("address").toString(),
                tjsonIn.get("deliveryTime") == null ? "" : tjsonIn.get("deliveryTime").toString(),
                "",
                "",
                "",
                tjsonIn.get("parentName") == null ? "" : tjsonIn.get("parentName").toString(),
                tjsonIn.get("parentMobile") == null ? "" : tjsonIn.get("parentMobile").toString(),
                tjsonIn.get("parentId") == null ? "" : tjsonIn.get("parentId").toString(),
                tjsonIn.get("studentName") == null ? "" : tjsonIn.get("studentName").toString(),
                tjsonIn.get("studentMobile") == null ? "" : tjsonIn.get("studentMobile").toString(),
                tjsonIn.get("studentId") == null ? "" : tjsonIn.get("studentId").toString(),
                "",
                map.get("repairCommentList") == null ? "" : map.get("repairCommentList").toString(),
                map.get("repairCost") == null ? "" : map.get("repairCost").toString(),
                "2",//添加入库记录
                map.get("commentType") == null ? "" : map.get("commentType").toString(),
                map.get("needRepairComment") == null ? stringlist : jsontoList(map.get("needRepairComment")),
                map.get("isPay") == null ? "" : map.get("isPay").toString(),
                map.get("payFrom") == null ? "" : map.get("payFrom").toString(),
                map.get("afterRepair") == null ? "" : map.get("afterRepair").toString(),
                map.get("repairType") == null ? "" : map.get("repairType").toString(),
                "0"//未读
        );
        result +="入库记录：";
        result += inOutStorageRecordDao.addProjectOutStorageRecord(inOutStorageEntry1);
        //3 再把imeiNo库存激活（维修入库 storageStatus 2）

        String imeiNo = tjsonIn.get("imeiNo").toString();
        String storageStatus = "2";//维修入库
        String useStatus = "1";//可用状态

        result +="维修入库：";
        result += storageManageDao.updateStorageInfoByImeiNo(imeiNo, storageStatus, useStatus, inStorageTime, inStorageYear, inStorageMonth);
        return result;
    }

    /**
     * 当维修完成，选择 手机入库
     * 1 添加入库记录
     * 2 再把imeiNo库存激活
     * @param map
     * @return
     */
    private String completeRepairToInStorage(Map map) {
        String inStorageTime = dateFormat.format(new Date());
        String inStorageYear = calendar.get(Calendar.YEAR)+"";
        String inStorageMonth = (calendar.get(Calendar.MONTH)+1)+"";
        String result = "";
        //解析数据
        String rowPojoIn = map.get("rowPojoIn").toString();
        JSONObject jsonObject = JSONObject.parseObject(rowPojoIn);
        JSONObject tjsonIn = (JSONObject)jsonObject.get("dataIn");
        System.out.println(tjsonIn);
        //2 添加入库记录
        System.out.println(inStorageTime);
        InOutStorageEntry inOutStorageEntry1 = new InOutStorageEntry(
                tjsonIn.get("imeiNo") == null ? "" : tjsonIn.get("imeiNo").toString(),
                tjsonIn.get("phoneModel") == null ? "" : tjsonIn.get("phoneModel").toString(),
                tjsonIn.get("color") == null ? "" : tjsonIn.get("color").toString(),
                tjsonIn.get("manufacturer") == null ? "" : tjsonIn.get("manufacturer").toString(),
                inStorageTime,
                inStorageYear,
                inStorageMonth,
                tjsonIn.get("storageStatus") == null ? "" : tjsonIn.get("storageStatus").toString(),
                tjsonIn.get("comment") == null ? "" : tjsonIn.get("comment").toString(),
                tjsonIn.get("projectId") == null ? "" : tjsonIn.get("projectId").toString(),
                tjsonIn.get("projectName") == null ? "" : tjsonIn.get("projectName").toString(),
                tjsonIn.get("projectDockPeople") == null ? "" : tjsonIn.get("projectDockPeople").toString(),
                tjsonIn.get("schoolName") == null ? "" : tjsonIn.get("schoolName").toString(),
                tjsonIn.get("accessClass") == null ? "" : tjsonIn.get("accessClass").toString(),
                tjsonIn.get("accessObj") == null ? "" : tjsonIn.get("accessObj").toString(),
                tjsonIn.get("contactInfo") == null ? "" : tjsonIn.get("contactInfo").toString(),
                tjsonIn.get("address") == null ? "" : tjsonIn.get("address").toString(),
                tjsonIn.get("deliveryTime") == null ? "" : tjsonIn.get("deliveryTime").toString(),
                "",
                "",
                "",
                tjsonIn.get("parentName") == null ? "" : tjsonIn.get("parentName").toString(),
                tjsonIn.get("parentMobile") == null ? "" : tjsonIn.get("parentMobile").toString(),
                tjsonIn.get("parentId") == null ? "" : tjsonIn.get("parentId").toString(),
                tjsonIn.get("studentName") == null ? "" : tjsonIn.get("studentName").toString(),
                tjsonIn.get("studentMobile") == null ? "" : tjsonIn.get("studentMobile").toString(),
                tjsonIn.get("studentId") == null ? "" : tjsonIn.get("studentId").toString(),
                "",
                map.get("repairCommentList") == null ? "" : map.get("repairCommentList").toString(),
                map.get("repairCost") == null ? "" : map.get("repairCost").toString(),
                "2",//添加入库记录
                map.get("commentType") == null ? "" : map.get("commentType").toString(),
                map.get("needRepairComment") == null ? stringlist : jsontoList(map.get("needRepairComment")),
                map.get("isPay") == null ? "" : map.get("isPay").toString(),
                map.get("payFrom") == null ? "" : map.get("payFrom").toString(),
                map.get("afterRepair") == null ? "" : map.get("afterRepair").toString(),
                map.get("repairType") == null ? "" : map.get("repairType").toString(),
                "0"//未读
        );
        result +="入库记录：";
        result += inOutStorageRecordDao.addProjectOutStorageRecord(inOutStorageEntry1);
        //3 再把imeiNo库存激活（维修入库 storageStatus 2）

        String imeiNo = tjsonIn.get("imeiNo").toString();
        String storageStatus = "2";//维修入库
        String useStatus = "1";//可用状态
        System.out.println(inStorageTime);
        result +="维修入库：";
        result += storageManageDao.updateStorageInfoByImeiNo(imeiNo, storageStatus, useStatus, inStorageTime, inStorageYear, inStorageMonth);
        return result;
    }

    /**
     * 当维修完成，选择 出库发货
     * @param map
     * @return
     */
    private String completeRepairToOutStorage(Map map) {
        String inStorageTime = dateFormat.format(new Date());
        String inStorageYear = calendar.get(Calendar.YEAR)+"";
        String inStorageMonth = (calendar.get(Calendar.MONTH)+1)+"";
        String result = "";
        //解析数据
        String rowPojoIn = map.get("rowPojoIn").toString();
        JSONObject jsonObject = JSONObject.parseObject(rowPojoIn);
        JSONObject tjsonIn = (JSONObject)jsonObject.get("dataIn");
        System.out.println(tjsonIn);
        //发货前做入库记录
        InOutStorageEntry inOutStorageEntry1 = new InOutStorageEntry(
                tjsonIn.get("imeiNo") == null ? "" : tjsonIn.get("imeiNo").toString(),
                tjsonIn.get("phoneModel") == null ? "" : tjsonIn.get("phoneModel").toString(),
                tjsonIn.get("color") == null ? "" : tjsonIn.get("color").toString(),
                tjsonIn.get("manufacturer") == null ? "" : tjsonIn.get("manufacturer").toString(),
                inStorageTime,
                inStorageYear,
                inStorageMonth,
                tjsonIn.get("storageStatus") == null ? "" : tjsonIn.get("storageStatus").toString(),
                tjsonIn.get("comment") == null ? "" : tjsonIn.get("comment").toString(),
                tjsonIn.get("projectId") == null ? "" : tjsonIn.get("projectId").toString(),
                tjsonIn.get("projectName") == null ? "" : tjsonIn.get("projectName").toString(),
                tjsonIn.get("projectDockPeople") == null ? "" : tjsonIn.get("projectDockPeople").toString(),
                tjsonIn.get("schoolName") == null ? "" : tjsonIn.get("schoolName").toString(),
                tjsonIn.get("accessClass") == null ? "" : tjsonIn.get("accessClass").toString(),
                tjsonIn.get("accessObj") == null ? "" : tjsonIn.get("accessObj").toString(),
                tjsonIn.get("contactInfo") == null ? "" : tjsonIn.get("contactInfo").toString(),
                tjsonIn.get("address") == null ? "" : tjsonIn.get("address").toString(),
                tjsonIn.get("deliveryTime") == null ? "" : tjsonIn.get("deliveryTime").toString(),
                "",
                "",
                "",
                tjsonIn.get("parentName") == null ? "" : tjsonIn.get("parentName").toString(),
                tjsonIn.get("parentMobile") == null ? "" : tjsonIn.get("parentMobile").toString(),
                tjsonIn.get("parentId") == null ? "" : tjsonIn.get("parentId").toString(),
                tjsonIn.get("studentName") == null ? "" : tjsonIn.get("studentName").toString(),
                tjsonIn.get("studentMobile") == null ? "" : tjsonIn.get("studentMobile").toString(),
                tjsonIn.get("studentId") == null ? "" : tjsonIn.get("studentId").toString(),
                "",
                map.get("repairCommentList") == null ? "" : map.get("repairCommentList").toString(),
                map.get("repairCost") == null ? "" : map.get("repairCost").toString(),
                "2",//添加入库记录
                map.get("commentType") == null ? "" : map.get("commentType").toString(),
                map.get("needRepairComment") == null ? stringlist : jsontoList(map.get("needRepairComment")),
                map.get("isPay") == null ? "" : map.get("isPay").toString(),
                map.get("payFrom") == null ? "" : map.get("payFrom").toString(),
                map.get("afterRepair") == null ? "" : map.get("afterRepair").toString(),
                map.get("repairType") == null ? "" : map.get("repairType").toString(),
                "0"//未读
        );
        result += "入库记录：";
        result += inOutStorageRecordDao.addProjectOutStorageRecord(inOutStorageEntry1);
        //添加到发货记录
        List<String> stringlist = new ArrayList<String>();
        InOutStorageEntry inOutStorageEntry = new InOutStorageEntry(
                tjsonIn.get("imeiNo") == null ? "" : tjsonIn.get("imeiNo").toString(),
                tjsonIn.get("phoneModel") == null ? "" : tjsonIn.get("phoneModel").toString(),
                tjsonIn.get("color") == null ? "" : tjsonIn.get("color").toString(),
                tjsonIn.get("manufacturer") == null ? "" : tjsonIn.get("manufacturer").toString(),
                tjsonIn.get("inStorageTime") == null ? "" : tjsonIn.get("inStorageTime").toString(),
                tjsonIn.get("inStorageYear") == null ? "" : tjsonIn.get("inStorageYear").toString(),
                tjsonIn.get("inStorageMonth") == null ? "" : tjsonIn.get("inStorageMonth").toString(),
                tjsonIn.get("storageStatus") == null ? "" : tjsonIn.get("storageStatus").toString(),
                tjsonIn.get("comment") == null ? "" : tjsonIn.get("comment").toString(),
                tjsonIn.get("projectId") == null ? "" : tjsonIn.get("projectId").toString(),
                tjsonIn.get("projectName") == null ? "" : tjsonIn.get("projectName").toString(),
                tjsonIn.get("projectDockPeople") == null ? "" : tjsonIn.get("projectDockPeople").toString(),
                tjsonIn.get("schoolName") == null ? "" : tjsonIn.get("schoolName").toString(),
                tjsonIn.get("accessClass") == null ? "" : tjsonIn.get("accessClass").toString(),
                tjsonIn.get("accessObj") == null ? "" : tjsonIn.get("accessObj").toString(),
                tjsonIn.get("contactInfo") == null ? "" : tjsonIn.get("contactInfo").toString(),
                tjsonIn.get("address") == null ? "" : tjsonIn.get("address").toString(),
                tjsonIn.get("deliveryTime") == null ? "" : tjsonIn.get("deliveryTime").toString(),
//                tjsonIn.get("deliveryMethod") == null ? "" : tjsonIn.get("deliveryMethod").toString(),
                "快递",
                "",
                "",
                tjsonIn.get("parentName") == null ? "" : tjsonIn.get("parentName").toString(),
                tjsonIn.get("parentMobile") == null ? "" : tjsonIn.get("parentMobile").toString(),
                tjsonIn.get("parentId") == null ? "" : tjsonIn.get("parentId").toString(),
                tjsonIn.get("studentName") == null ? "" : tjsonIn.get("studentName").toString(),
                tjsonIn.get("studentMobile") == null ? "" : tjsonIn.get("studentMobile").toString(),
                tjsonIn.get("studentId") == null ? "" : tjsonIn.get("studentId").toString(),
                "",
                map.get("repairCommentList") == null ? "" : map.get("repairCommentList").toString(),
                map.get("repairCost") == null ? "" : map.get("repairCost").toString(),
                "5",//维修出库发货
                map.get("commentType") == null ? "" : map.get("commentType").toString(),
                map.get("needRepairComment") == null ? stringlist : jsontoList(map.get("needRepairComment")),
                map.get("isPay") == null ? "" : map.get("isPay").toString(),
                map.get("payFrom") == null ? "" : map.get("payFrom").toString(),
                map.get("afterRepair") == null ? "" : map.get("afterRepair").toString(),
                map.get("repairType") == null ? "" : map.get("repairType").toString(),
                "0"//未读
        );
        result += "出库记录：";
        result += inOutStorageRecordDao.addProjectOutStorageRecord(inOutStorageEntry);
        return result;
    }
    private List<String> jsontoList(Object needRepairComment) {
        List<String> stringList = new ArrayList<String>();
        String needRepairCommentString = needRepairComment.toString().replace("[","").replace("]","");
        String[] strings= needRepairCommentString.split(",");
        for (String string : strings){
            stringList.add(string);
        }
        System.out.println(stringList);
        return stringList;
    }

    /**
     * 维修管理-删除
     * @param map
     * @return
     */
    public String delRepairManage(Map map) {
        String result = "维修记录：";
        result += inOutStorageRecordDao.updateRepairManage(map);//逻辑删除
        result += "库存：";
        result += storageManageDao.delFromRepairManage(map);
        return result;
    }
}
