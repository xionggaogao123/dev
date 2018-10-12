package com.fulaan.backstage.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.db.backstage.InOutStorageRecordDao;
import com.db.backstage.StorageManageDao;
import com.fulaan.backstage.dto.InOutStorageRecordDto;
import com.fulaan.utils.HSSFUtils;
import com.mongodb.DBObject;
import com.pojo.backstage.InOutStorageEntry;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Auther: taotao.chan
 * @Date: 2018/10/11 16:15
 * @Description:
 */
@Service
public class BackStageOutStorageFollowService {

    private static List<String> stringlist = new ArrayList<String>();
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static Calendar calendar = Calendar.getInstance();
    private static String inStorageTime = dateFormat.format(new Date());
    private static String inStorageYear = calendar.get(Calendar.YEAR)+"";
    private static String inStorageMonth = (calendar.get(Calendar.MONTH)+1)+"";

    private InOutStorageRecordDao inOutStorageRecordDao = new InOutStorageRecordDao();

    private StorageManageDao storageManageDao = new StorageManageDao();
    /**
     * 出库跟踪-按项目查找
     * @param page
     * @param pageSize
     * @param inputParams
     * @param projectId
     * @return
     */
    public Map<String, Object> getOutStorageListByProject(int page, int pageSize, String inputParams, String projectId) {
        Map<String, Object> result = new HashMap<String, Object>();
        List<InOutStorageRecordDto> inOutStorageRecordDtos = new ArrayList<InOutStorageRecordDto>();

        result = inOutStorageRecordDao.getOutStorageListByProject(page, pageSize, inputParams, projectId);
        List<InOutStorageEntry> inOutStorageEntryList = (ArrayList)result.get("entryList");
        for (InOutStorageEntry inOutStorageEntry : inOutStorageEntryList){
            if (inOutStorageEntry != null){
                inOutStorageRecordDtos.add(new InOutStorageRecordDto(inOutStorageEntry));
            }
        }
        result.put("dtos",inOutStorageRecordDtos);
        result.remove("entryList");
        return result;
    }

    /**
     * 出库跟踪-导入用户信息模板下载
     * @param response
     * @param request
     */
    public void exportOutStorageFollowUserTemplate(HttpServletRequest request, HttpServletResponse response) {
        String sheetName ="出库跟踪用户信息表";
        //创建工作簿
        HSSFWorkbook wb = new HSSFWorkbook();
        //创建sheet页
        HSSFSheet sheet = wb.createSheet(sheetName);

        //创建第二行字段头
        HSSFRow rowOne = sheet.createRow(0);
        //IMEI码
        HSSFCell cellImeiNo = rowOne.createCell(0);
        cellImeiNo.setCellValue("IMEI码");

        //家长姓名
        HSSFCell cellPName = rowOne.createCell(1);
        cellPName.setCellValue("家长姓名");


        //家长ID
        HSSFCell cellPId = rowOne.createCell(2);
        cellPId.setCellValue("家长ID");

        //家长手机号
        HSSFCell cellPMobile = rowOne.createCell(3);
        cellPMobile.setCellValue("家长手机号");

        //学生姓名
        HSSFCell cellCName = rowOne.createCell(4);
        cellCName.setCellValue("学生姓名");

        //学生ID
        HSSFCell cellCId = rowOne.createCell(5);
        cellCId.setCellValue("学生ID");

        //学生手机号
        HSSFCell cellCMobile = rowOne.createCell(6);
        cellCMobile.setCellValue("学生手机号");

        //学校
        HSSFCell cellCShool = rowOne.createCell(7);
        cellCShool.setCellValue("学校");
        //班级
        HSSFCell cellCClass = rowOne.createCell(8);
        cellCClass.setCellValue("班级");
        //班级
        HSSFCell cellCAddress = rowOne.createCell(9);
        cellCAddress.setCellValue("地址");

        String fileName = sheetName + ".xls";
        String userAgent = request.getHeader("USER-AGENT");
        HSSFUtils.exportExcel(userAgent, response, wb, fileName);
    }

    /**
     * 出库跟踪-导入用户信息导入
     * @param request
     * @return
     * @throws Exception
     */
    public String importOutStorageFollowUser(InputStream inputStream, HttpServletRequest request) throws Exception {
        StringBuffer sb = new StringBuffer();
        HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
        HSSFSheet sheet = workbook.getSheet(workbook.getSheetName(0));

        for (int i = 1 ; i<= sheet.getLastRowNum();i++){
            sheet.getRow(i).getCell(0).setCellType(Cell.CELL_TYPE_STRING);//防止imeiNo是纯数字，读取string类型报错
            String imeiNo = sheet.getRow(i).getCell(0) == null ? "" : sheet.getRow(i).getCell(0).getStringCellValue();
            String parentName = sheet.getRow(i).getCell(1) == null ? "" : sheet.getRow(i).getCell(1).getStringCellValue();

            sheet.getRow(i).getCell(2).setCellType(Cell.CELL_TYPE_STRING);//防止parentId是纯数字，读取string类型报错
            String parentId = sheet.getRow(i).getCell(2) == null ? "" : sheet.getRow(i).getCell(2).getStringCellValue();
            sheet.getRow(i).getCell(3).setCellType(Cell.CELL_TYPE_STRING);
            String parentMobile = sheet.getRow(i).getCell(3) == null ? "" : sheet.getRow(i).getCell(3).getStringCellValue();
            String studentName = sheet.getRow(i).getCell(4) == null ? "" : sheet.getRow(i).getCell(4).getStringCellValue();

            sheet.getRow(i).getCell(5).setCellType(Cell.CELL_TYPE_STRING);//防止studentId是纯数字，读取string类型报错
            String studentId = sheet.getRow(i).getCell(5) == null ? "" : sheet.getRow(i).getCell(5).getStringCellValue();
            sheet.getRow(i).getCell(6).setCellType(Cell.CELL_TYPE_STRING);
            String studentMobile = sheet.getRow(i).getCell(6) == null ? "" : sheet.getRow(i).getCell(6).getStringCellValue();
            String schoolName = sheet.getRow(i).getCell(7) == null ? "" : sheet.getRow(i).getCell(7).getStringCellValue();
            sheet.getRow(i).getCell(8).setCellType(Cell.CELL_TYPE_STRING);
            String accessClass = sheet.getRow(i).getCell(8) == null ? "" : sheet.getRow(i).getCell(8).getStringCellValue();
            String address = sheet.getRow(i).getCell(9) == null ? "" : sheet.getRow(i).getCell(9).getStringCellValue();

            //手机IMEI查验，如果未在出库状态则跳过，并告知前台
            int exist = inOutStorageRecordDao.findOutStorageByImeiNo(imeiNo);
            if (0 == exist){
                //imeiNo 未在出库状态
                sb.append("<p>第"+i+"列：");
                sb.append("  -----<span style='color:red;'>IMEI号"+imeiNo+"未在出库状态！</span>");
                sb.append("</p>");
            }else {
                //更新用户信息
                inOutStorageRecordDao.updateOutOutStorageByImeiNo(imeiNo, parentName, parentId, parentMobile,
                        studentName, studentId, studentMobile,
                        schoolName, accessClass, address);
            }
        }
        return sb == null?"success!":sb.toString();
    }

    /**
     * 出库跟踪-单个回收
     * @return
     */
    public List<String> singleRecycleInStorage(Map<String,Object> params) {
        List<String> result = new ArrayList<String>();
        //更改选中数据为已回收 即isr 为 1
        inOutStorageRecordDao.singleRecycleInStorageById(params.get("id").toString());
        result.add("回收记录ID："+params.get("id").toString());
        //当选则 维修入库
        // 1 添加当前手机进 维修管理
        if ("维修入库".equals(params.get("recycleType").toString())){
             String stringResult = addSinglePhoneToRepair(params);
             result.add(stringResult);
        }

        //当选则 退货入库
        // 1 添加当前手机 退货入库记录
        // 2 并更新当前手机 库存数据 状态 退货入库
        if ("退货入库".equals(params.get("recycleType").toString())){
            String stringResult = singlePhoneToReturn(params);
            result.add(stringResult);
        }

        //当选则 换货入库
        // 1 添加当前手机 换货入库记录
        // 2 添加 当前用户 待出库记录
        // 3 并更新当前手机 库存数据 状态 换货入库
        if ("换货入库".equals(params.get("recycleType").toString())){
            String stringResult = singlePhoneToExchangeReturn(params);
            result.add(stringResult);
        }

        //当选则 回收入库
        // 1 添加当前手机 回收入库记录
        // 2 并更新当前手机 库存数据 状态 回收入库
        if ("回收入库".equals(params.get("recycleType").toString())){
            String stringResult = singlePhoneToRecycleReturn(params);
            result.add(stringResult);
        }

        return result;
    }




    /**
     * 出库跟踪-单个回收 之
     * 添加当前手机进 维修管理
     * @param params
     * @return
     */
    private String addSinglePhoneToRepair(Map<String,Object> params) {
        String result = "";
        //解析数据
        String rowPojoIn = params.get("rowPojoIn").toString();
        JSONObject jsonObject = JSONObject.parseObject(rowPojoIn);
        JSONObject tjsonIn = (JSONObject)jsonObject.get("dataIn");
        System.out.println(tjsonIn);

        //添加到维修管理
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Calendar calendar = Calendar.getInstance();
//        String inStorageTime = dateFormat.format(new Date());
//        String inStorageYear = calendar.get(Calendar.YEAR)+"";
//        String inStorageMonth = (calendar.get(Calendar.MONTH)+1)+"";
//        List<String> stringlist = new ArrayList<String>();
        InOutStorageEntry inOutStorageEntry = new InOutStorageEntry(
                tjsonIn.get("imeiNo") == null ? "" : tjsonIn.get("imeiNo").toString(),
                tjsonIn.get("phoneModel") == null ? "" : tjsonIn.get("phoneModel").toString(),
                tjsonIn.get("color") == null ? "" : tjsonIn.get("color").toString(),
                tjsonIn.get("manufacturer") == null ? "" : tjsonIn.get("manufacturer").toString(),
                inStorageTime,
                inStorageYear,
                inStorageMonth,
                tjsonIn.get("storageStatus") == null ? "" : tjsonIn.get("storageStatus").toString(),
                params.get("comment") == null ? "" : params.get("comment").toString(),
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
                params.get("comment") == null ? "" : params.get("comment").toString(),
                tjsonIn.get("repairCommentList") == null ? "" : tjsonIn.get("repairCommentList").toString(),
                tjsonIn.get("repairCost") == null ? "" : tjsonIn.get("repairCost").toString(),
                "6",//待维修
                tjsonIn.get("commentType") == null ? "" : tjsonIn.get("commentType").toString(),
                stringlist
        );
        result += "维修管理:";
        result += inOutStorageRecordDao.addProjectOutStorageRecord(inOutStorageEntry);
        return result;
    }

    /**
     * 出库跟踪-单个回收 之
     * 当选则 退货入库
     * 1 添加当前手机 退货入库记录
     * 2 并更新当前手机 库存数据 状态 退货入库
     * @param params
     * @return
     */
    private String singlePhoneToReturn(Map<String,Object> params) {
        String result = "";
        //解析数据
        String rowPojoIn = params.get("rowPojoIn").toString();
        JSONObject jsonObject = JSONObject.parseObject(rowPojoIn);
        JSONObject tjsonIn = (JSONObject)jsonObject.get("dataIn");
        System.out.println(tjsonIn);

        //添加当前手机 退货入库记录
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Calendar calendar = Calendar.getInstance();
//        String inStorageTime = dateFormat.format(new Date());
//        String inStorageYear = calendar.get(Calendar.YEAR)+"";
//        String inStorageMonth = (calendar.get(Calendar.MONTH)+1)+"";
//        List<String> stringlist = new ArrayList<String>();
        InOutStorageEntry inOutStorageEntry = new InOutStorageEntry(
                tjsonIn.get("imeiNo") == null ? "" : tjsonIn.get("imeiNo").toString(),
                tjsonIn.get("phoneModel") == null ? "" : tjsonIn.get("phoneModel").toString(),
                tjsonIn.get("color") == null ? "" : tjsonIn.get("color").toString(),
                tjsonIn.get("manufacturer") == null ? "" : tjsonIn.get("manufacturer").toString(),
                inStorageTime,
                inStorageYear,
                inStorageMonth,
                tjsonIn.get("storageStatus") == null ? "" : tjsonIn.get("storageStatus").toString(),
                params.get("comment") == null ? "" : params.get("comment").toString(),
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
                params.get("comment") == null ? "" : params.get("comment").toString(),
                tjsonIn.get("repairCommentList") == null ? "" : tjsonIn.get("repairCommentList").toString(),
                tjsonIn.get("repairCost") == null ? "" : tjsonIn.get("repairCost").toString(),
                "1",//退货入库
                tjsonIn.get("commentType") == null ? "" : tjsonIn.get("commentType").toString(),
                stringlist
        );
        result += "退货入库log:";
        result += inOutStorageRecordDao.addProjectOutStorageRecord(inOutStorageEntry);

        //更新当前手机 库存数据 状态 退货入库
        String imeiNo = tjsonIn.get("imeiNo").toString();
        String storageStatus = "1";//退货入库
        String useStatus = "1";//可用状态


        result +="退货入库：";
        result += storageManageDao.updateStorageInfoByImeiNo(imeiNo, storageStatus, useStatus, inStorageTime, inStorageYear, inStorageMonth);
        return result;
    }

    /**
     * 出库跟踪-单个回收 之
     * 当选则 换货入库
     * 1 添加当前手机 换货入库记录
     * 2 添加 当前用户 待出库记录
     * 3 并更新当前手机 库存数据 状态 换货入库
     * @param params
     * @return
     */
    private String singlePhoneToExchangeReturn(Map<String,Object> params) {
        String result = "";
        //解析数据
        String rowPojoIn = params.get("rowPojoIn").toString();
        JSONObject jsonObject = JSONObject.parseObject(rowPojoIn);
        JSONObject tjsonIn = (JSONObject)jsonObject.get("dataIn");
        System.out.println(tjsonIn);

//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Calendar calendar = Calendar.getInstance();
//        String inStorageTime = dateFormat.format(new Date());
//        String inStorageYear = calendar.get(Calendar.YEAR)+"";
//        String inStorageMonth = (calendar.get(Calendar.MONTH)+1)+"";
//        List<String> stringlist = new ArrayList<String>();

        //添加当前手机 换货入库记录
        InOutStorageEntry inOutStorageEntry = new InOutStorageEntry(
                tjsonIn.get("imeiNo") == null ? "" : tjsonIn.get("imeiNo").toString(),
                tjsonIn.get("phoneModel") == null ? "" : tjsonIn.get("phoneModel").toString(),
                tjsonIn.get("color") == null ? "" : tjsonIn.get("color").toString(),
                tjsonIn.get("manufacturer") == null ? "" : tjsonIn.get("manufacturer").toString(),
                inStorageTime,
                inStorageYear,
                inStorageMonth,
                tjsonIn.get("storageStatus") == null ? "" : tjsonIn.get("storageStatus").toString(),
                params.get("comment") == null ? "" : params.get("comment").toString(),
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
                params.get("comment") == null ? "" : params.get("comment").toString(),
                tjsonIn.get("repairCommentList") == null ? "" : tjsonIn.get("repairCommentList").toString(),
                tjsonIn.get("repairCost") == null ? "" : tjsonIn.get("repairCost").toString(),
                "3",//换货入库
                tjsonIn.get("commentType") == null ? "" : tjsonIn.get("commentType").toString(),
                stringlist
        );
        result += "换货入库log:";
        result += inOutStorageRecordDao.addProjectOutStorageRecord(inOutStorageEntry);
        //添加 当前用户 待出库记录
        InOutStorageEntry inOutStorageEntry1 = new InOutStorageEntry(
                "",
//                tjsonIn.get("imeiNo") == null ? "" : tjsonIn.get("imeiNo").toString(),
                tjsonIn.get("phoneModel") == null ? "" : tjsonIn.get("phoneModel").toString(),
                tjsonIn.get("color") == null ? "" : tjsonIn.get("color").toString(),
                tjsonIn.get("manufacturer") == null ? "" : tjsonIn.get("manufacturer").toString(),
                inStorageTime,
                inStorageYear,
                inStorageMonth,
                tjsonIn.get("storageStatus") == null ? "" : tjsonIn.get("storageStatus").toString(),
                params.get("comment") == null ? "" : params.get("comment").toString(),
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
                params.get("comment") == null ? "" : params.get("comment").toString(),
                tjsonIn.get("repairCommentList") == null ? "" : tjsonIn.get("repairCommentList").toString(),
                tjsonIn.get("repairCost") == null ? "" : tjsonIn.get("repairCost").toString(),
                "5",//待换货出库
                tjsonIn.get("commentType") == null ? "" : tjsonIn.get("commentType").toString(),
                stringlist
        );
        result += "待换货出库log:";
        result += inOutStorageRecordDao.addProjectOutStorageRecord(inOutStorageEntry1);

        //更新当前手机 库存数据 状态 换货入库 3
        String imeiNo = tjsonIn.get("imeiNo").toString();
        String storageStatus = "3";//换货入库
        String useStatus = "1";//可用状态


        result +="换货入库：";
        result += storageManageDao.updateStorageInfoByImeiNo(imeiNo, storageStatus, useStatus, inStorageTime, inStorageYear, inStorageMonth);
        return result;
    }

    /**
     * 出库跟踪-单个回收 之
     * 当选则 回收入库
     * 1 添加当前手机 回收入库记录
     * 2 并更新当前手机 库存数据 状态 回收入库
     * @param params
     * @return
     */
    private String singlePhoneToRecycleReturn(Map<String,Object> params) {
        String result = "";
        //解析数据
        String rowPojoIn = params.get("rowPojoIn").toString();
        JSONObject jsonObject = JSONObject.parseObject(rowPojoIn);
        JSONObject tjsonIn = (JSONObject)jsonObject.get("dataIn");
        System.out.println(tjsonIn);

//        List<String> stringlist = new ArrayList<String>();
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Calendar calendar = Calendar.getInstance();
//        String inStorageTime = dateFormat.format(new Date());
//        String inStorageYear = calendar.get(Calendar.YEAR)+"";
//        String inStorageMonth = (calendar.get(Calendar.MONTH)+1)+"";

        //添加当前手机 回收入库记录
        InOutStorageEntry inOutStorageEntry = new InOutStorageEntry(
                tjsonIn.get("imeiNo") == null ? "" : tjsonIn.get("imeiNo").toString(),
                tjsonIn.get("phoneModel") == null ? "" : tjsonIn.get("phoneModel").toString(),
                tjsonIn.get("color") == null ? "" : tjsonIn.get("color").toString(),
                tjsonIn.get("manufacturer") == null ? "" : tjsonIn.get("manufacturer").toString(),
                inStorageTime,
                inStorageYear,
                inStorageMonth,
                tjsonIn.get("storageStatus") == null ? "" : tjsonIn.get("storageStatus").toString(),
                params.get("comment") == null ? "" : params.get("comment").toString(),
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
                params.get("comment") == null ? "" : params.get("comment").toString(),
                tjsonIn.get("repairCommentList") == null ? "" : tjsonIn.get("repairCommentList").toString(),
                tjsonIn.get("repairCost") == null ? "" : tjsonIn.get("repairCost").toString(),
                "4",//回收入库
                tjsonIn.get("commentType") == null ? "" : tjsonIn.get("commentType").toString(),
                stringlist
        );
        result += "回收入库log:";
        result += inOutStorageRecordDao.addProjectOutStorageRecord(inOutStorageEntry);

        //并更新当前手机 库存数据 状态 回收入库
        String imeiNo = tjsonIn.get("imeiNo").toString();
        String storageStatus = "4";//回收入库
        String useStatus = "1";//可用状态


        result +="回收入库：";
        result += storageManageDao.updateStorageInfoByImeiNo(imeiNo, storageStatus, useStatus, inStorageTime, inStorageYear, inStorageMonth);

        return result;
    }

    /**
     * 出库跟踪-批量回收
     * @return
     */
    public List<String> batchRecycleInStorage(Map<String,Object> params) {
        List<String> result = new ArrayList<String>();
        //更改选中数据为已回收 即isr 为 1
        inOutStorageRecordDao.batchRecycleInStorageByIds(params.get("ids").toString());
        result.add("回收记录ID："+params.get("ids").toString());
        //当选则 维修入库
        // 1 添加当前手机进 维修管理
        if ("维修入库".equals(params.get("recycleType").toString())){
            String stringResult = addBatchlePhoneToRepair(params);
            result.add(stringResult);
        }

        //当选则 退货入库
        // 1 添加当前手机 退货入库记录
        // 2 并更新当前手机 库存数据 状态 退货入库
        if ("退货入库".equals(params.get("recycleType").toString())){
            String stringResult = batchPhoneToReturn(params);
            result.add(stringResult);
        }

        //当选则 换货入库
        // 1 添加当前手机 换货入库记录
        // 2 添加 当前用户 待出库记录
        // 3 并更新当前手机 库存数据 状态 换货入库
        if ("换货入库".equals(params.get("recycleType").toString())){
            String stringResult = batchPhoneToExchangeReturn(params);
            result.add(stringResult);
        }

        //当选则 回收入库
        // 1 添加当前手机 回收入库记录
        // 2 并更新当前手机 库存数据 状态 回收入库
        if ("回收入库".equals(params.get("recycleType").toString())){
            String stringResult = batchPhoneToRecycleReturn(params);
            result.add(stringResult);
        }

        return result;
    }



    /**
     * 出库跟踪-批量回收 之
     * 当选则 维修入库
     * 添加当前手机进 维修管理
     * @param params
     * @return
     */
    private String addBatchlePhoneToRepair(Map<String,Object> params) {
        String result = "";
        //解析数据
        String rowPojoIn = params.get("rowPojoIn").toString();
        JSONObject jsonObject = JSONObject.parseObject(rowPojoIn);
        JSONArray jsonArray = (JSONArray)jsonObject.get("dataIn");
        System.out.println(jsonArray);

//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Calendar calendar = Calendar.getInstance();
//        String inStorageTime = dateFormat.format(new Date());
//        String inStorageYear = calendar.get(Calendar.YEAR)+"";
//        String inStorageMonth = (calendar.get(Calendar.MONTH)+1)+"";
//        List<String> stringlist = new ArrayList<String>();

        //并封装
        List<DBObject> dbObjectList = new ArrayList<DBObject>();
        for (int i = 0; i < jsonArray.size(); i++){
            JSONObject tjsonIn = (JSONObject)jsonArray.get(i);
            InOutStorageEntry inOutStorageEntry = new InOutStorageEntry(
                    tjsonIn.get("imeiNo") == null ? "" : tjsonIn.get("imeiNo").toString(),
                    tjsonIn.get("phoneModel") == null ? "" : tjsonIn.get("phoneModel").toString(),
                    tjsonIn.get("color") == null ? "" : tjsonIn.get("color").toString(),
                    tjsonIn.get("manufacturer") == null ? "" : tjsonIn.get("manufacturer").toString(),
                    inStorageTime,
                    inStorageYear,
                    inStorageMonth,
                    tjsonIn.get("storageStatus") == null ? "" : tjsonIn.get("storageStatus").toString(),
                    params.get("comment") == null ? "" : params.get("comment").toString(),
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
                    params.get("comment") == null ? "" : params.get("comment").toString(),
                    tjsonIn.get("repairCommentList") == null ? "" : tjsonIn.get("repairCommentList").toString(),
                    tjsonIn.get("repairCost") == null ? "" : tjsonIn.get("repairCost").toString(),
                    "6",//待维修
                    tjsonIn.get("commentType") == null ? "" : tjsonIn.get("commentType").toString(),
                    stringlist
            );
            dbObjectList.add(inOutStorageEntry.getBaseEntry());
        }
        inOutStorageRecordDao.addProjectOutStorageRecordList(dbObjectList);
        result += "批量维修入库:";
        result += "success";
        return result;
    }

    /**
     * 当选则 退货入库
     * 1 添加当前手机 退货入库记录
     *  2 并更新当前手机 库存数据 状态 退货入库
     * @param params
     * @return
     */
    private String batchPhoneToReturn(Map<String,Object> params) {
        String result = "";
        //解析数据
        String rowPojoIn = params.get("rowPojoIn").toString();
        JSONObject jsonObject = JSONObject.parseObject(rowPojoIn);
        JSONArray jsonArray = (JSONArray)jsonObject.get("dataIn");
        System.out.println(jsonArray);

//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Calendar calendar = Calendar.getInstance();
//        String inStorageTime = dateFormat.format(new Date());
//        String inStorageYear = calendar.get(Calendar.YEAR)+"";
//        String inStorageMonth = (calendar.get(Calendar.MONTH)+1)+"";
//        List<String> stringlist = new ArrayList<String>();

        //并封装
        List<DBObject> dbObjectList = new ArrayList<DBObject>();
        List<String> imeiNoList = new ArrayList<String>();
        //添加当前手机 退货入库记录
        for (int i = 0; i < jsonArray.size(); i++){
            JSONObject tjsonIn = (JSONObject)jsonArray.get(i);
            imeiNoList.add(tjsonIn.get("imeiNo") == null ? "" : tjsonIn.get("imeiNo").toString());
            InOutStorageEntry inOutStorageEntry = new InOutStorageEntry(
                    tjsonIn.get("imeiNo") == null ? "" : tjsonIn.get("imeiNo").toString(),
                    tjsonIn.get("phoneModel") == null ? "" : tjsonIn.get("phoneModel").toString(),
                    tjsonIn.get("color") == null ? "" : tjsonIn.get("color").toString(),
                    tjsonIn.get("manufacturer") == null ? "" : tjsonIn.get("manufacturer").toString(),
                    inStorageTime,
                    inStorageYear,
                    inStorageMonth,
                    tjsonIn.get("storageStatus") == null ? "" : tjsonIn.get("storageStatus").toString(),
                    params.get("comment") == null ? "" : params.get("comment").toString(),
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
                    params.get("comment") == null ? "" : params.get("comment").toString(),
                    tjsonIn.get("repairCommentList") == null ? "" : tjsonIn.get("repairCommentList").toString(),
                    tjsonIn.get("repairCost") == null ? "" : tjsonIn.get("repairCost").toString(),
                    "1",//退货入库
                    tjsonIn.get("commentType") == null ? "" : tjsonIn.get("commentType").toString(),
                    stringlist
            );
            dbObjectList.add(inOutStorageEntry.getBaseEntry());
        }
        inOutStorageRecordDao.addProjectOutStorageRecordList(dbObjectList);

        result += "批量退货入库:";
        result += "success";

        //更新当前手机 库存数据 状态 退货入库
        String storageStatus = "1";//退货入库
        String useStatus = "1";//可用状态


        storageManageDao.updateStorageInfoByImeiNoList(imeiNoList, storageStatus, useStatus, inStorageTime, inStorageYear, inStorageMonth);
        result += "退货入库：";
        result += "success";
        return result;
    }

    /**
     * 当选则 换货入库
     * 1 添加当前手机 换货入库记录
     * 2 添加 当前用户 待出库记录
     * 3 并更新当前手机 库存数据 状态 换货入库
     * @param params
     * @return
     */
    private String batchPhoneToExchangeReturn(Map<String,Object> params) {
        String result = "";
        //解析数据
        String rowPojoIn = params.get("rowPojoIn").toString();
        JSONObject jsonObject = JSONObject.parseObject(rowPojoIn);
        JSONArray jsonArray = (JSONArray)jsonObject.get("dataIn");
        System.out.println(jsonArray);

        //并封装
        List<DBObject> dbObjectList = new ArrayList<DBObject>();
        List<String> imeiNoList = new ArrayList<String>();

        //添加当前手机 换货入库记录
        for (int i = 0; i < jsonArray.size(); i++){
            JSONObject tjsonIn = (JSONObject)jsonArray.get(i);
            imeiNoList.add(tjsonIn.get("imeiNo") == null ? "" : tjsonIn.get("imeiNo").toString());
            //添加当前手机 换货入库记录
            // 添加 用户们 待出库记录
            InOutStorageEntry inOutStorageEntry = new InOutStorageEntry(
                    tjsonIn.get("imeiNo") == null ? "" : tjsonIn.get("imeiNo").toString(),
                    tjsonIn.get("phoneModel") == null ? "" : tjsonIn.get("phoneModel").toString(),
                    tjsonIn.get("color") == null ? "" : tjsonIn.get("color").toString(),
                    tjsonIn.get("manufacturer") == null ? "" : tjsonIn.get("manufacturer").toString(),
                    inStorageTime,
                    inStorageYear,
                    inStorageMonth,
                    tjsonIn.get("storageStatus") == null ? "" : tjsonIn.get("storageStatus").toString(),
                    params.get("comment") == null ? "" : params.get("comment").toString(),
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
                    params.get("comment") == null ? "" : params.get("comment").toString(),
                    tjsonIn.get("repairCommentList") == null ? "" : tjsonIn.get("repairCommentList").toString(),
                    tjsonIn.get("repairCost") == null ? "" : tjsonIn.get("repairCost").toString(),
                    "3",//换货入库
                    tjsonIn.get("commentType") == null ? "" : tjsonIn.get("commentType").toString(),
                    stringlist
            );
            dbObjectList.add(inOutStorageEntry.getBaseEntry());

            //添加 用户们 待出库记录
            InOutStorageEntry inOutStorageEntry1 = new InOutStorageEntry(
                    "",
                    tjsonIn.get("phoneModel") == null ? "" : tjsonIn.get("phoneModel").toString(),
                    tjsonIn.get("color") == null ? "" : tjsonIn.get("color").toString(),
                    tjsonIn.get("manufacturer") == null ? "" : tjsonIn.get("manufacturer").toString(),
                    inStorageTime,
                    inStorageYear,
                    inStorageMonth,
                    tjsonIn.get("storageStatus") == null ? "" : tjsonIn.get("storageStatus").toString(),
                    params.get("comment") == null ? "" : params.get("comment").toString(),
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
                    params.get("comment") == null ? "" : params.get("comment").toString(),
                    tjsonIn.get("repairCommentList") == null ? "" : tjsonIn.get("repairCommentList").toString(),
                    tjsonIn.get("repairCost") == null ? "" : tjsonIn.get("repairCost").toString(),
                    "5",//待换货出库
                    tjsonIn.get("commentType") == null ? "" : tjsonIn.get("commentType").toString(),
                    stringlist
            );
            dbObjectList.add(inOutStorageEntry1.getBaseEntry());
        }
        inOutStorageRecordDao.addProjectOutStorageRecordList(dbObjectList);

        String storageStatus = "3";//换货入库
        String useStatus = "1";//可用状态
        storageManageDao.updateStorageInfoByImeiNoList(imeiNoList, storageStatus, useStatus, inStorageTime, inStorageYear, inStorageMonth);
        result += "批量换货入库:";
        result += "success";
        return result;
    }

    /**
     * 出库跟踪-批量回收 之
     * 当选则 回收入库
     * 1 添加当前手机 回收入库记录
     * 2 并更新当前手机 库存数据 状态 回收入库
     * @param params
     * @return
     */
    private String batchPhoneToRecycleReturn(Map<String,Object> params) {
        String result = "";
        //解析数据
        String rowPojoIn = params.get("rowPojoIn").toString();
        JSONObject jsonObject = JSONObject.parseObject(rowPojoIn);
        JSONArray jsonArray = (JSONArray)jsonObject.get("dataIn");
        System.out.println(jsonArray);

        //并封装
        List<DBObject> dbObjectList = new ArrayList<DBObject>();
        List<String> imeiNoList = new ArrayList<String>();

        //添加当前手机 回收入库记录
        for (int i = 0; i < jsonArray.size(); i++){
            JSONObject tjsonIn = (JSONObject)jsonArray.get(i);
            imeiNoList.add(tjsonIn.get("imeiNo") == null ? "" : tjsonIn.get("imeiNo").toString());
            InOutStorageEntry inOutStorageEntry = new InOutStorageEntry(
                    tjsonIn.get("imeiNo") == null ? "" : tjsonIn.get("imeiNo").toString(),
                    tjsonIn.get("phoneModel") == null ? "" : tjsonIn.get("phoneModel").toString(),
                    tjsonIn.get("color") == null ? "" : tjsonIn.get("color").toString(),
                    tjsonIn.get("manufacturer") == null ? "" : tjsonIn.get("manufacturer").toString(),
                    inStorageTime,
                    inStorageYear,
                    inStorageMonth,
                    tjsonIn.get("storageStatus") == null ? "" : tjsonIn.get("storageStatus").toString(),
                    params.get("comment") == null ? "" : params.get("comment").toString(),
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
                    params.get("comment") == null ? "" : params.get("comment").toString(),
                    tjsonIn.get("repairCommentList") == null ? "" : tjsonIn.get("repairCommentList").toString(),
                    tjsonIn.get("repairCost") == null ? "" : tjsonIn.get("repairCost").toString(),
                    "4",//回收入库
                    tjsonIn.get("commentType") == null ? "" : tjsonIn.get("commentType").toString(),
                    stringlist
            );
            dbObjectList.add(inOutStorageEntry.getBaseEntry());

        }
        inOutStorageRecordDao.addProjectOutStorageRecordList(dbObjectList);

        String storageStatus = "4";//回收入库
        String useStatus = "1";//可用状态
        storageManageDao.updateStorageInfoByImeiNoList(imeiNoList, storageStatus, useStatus, inStorageTime, inStorageYear, inStorageMonth);
        result += "回收入库:";
        result += "success";
        return result;
    }
}
