package com.fulaan.backstage.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.db.backstage.*;
import com.db.user.UserDao;
import com.fulaan.backstage.dto.LogMessageDTO;
import com.fulaan.backstage.dto.StorageManageDto;
import com.fulaan.backstage.dto.UserLogResultDTO;
import com.fulaan.backstage.dto.UserRoleJurisdictionDto;
import com.fulaan.user.service.UserService;
import com.fulaan.utils.HSSFUtils;
import com.mongodb.DBObject;
import com.pojo.backstage.*;
import com.pojo.user.UserEntry;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Auther: taotao.chan
 * @Date: 2018-9-17 14:35:11
 * @Description:
 */
@Service
public class BackStageStorageManageService {

    private StorageManageDao storageManageDao = new StorageManageDao();

    private InOutStorageRecordDao inOutStorageRecordDao = new InOutStorageRecordDao();

    /**
     * 生成模板
     * @param request
     * @param response
     */
    public void exportStorageTemplate(HttpServletRequest request, HttpServletResponse response) {
        String sheetName ="库存批量导入模板";
        //创建工作簿
        HSSFWorkbook wb = new HSSFWorkbook();
        //创建sheet页
        HSSFSheet sheet = wb.createSheet(sheetName);

//        //创建第一行-入库原因选择
//        HSSFRow rowZero = sheet.createRow(0);
//        //入库原因
//        HSSFCell cellStorageStatus = rowZero.createCell(0);
//        cellStorageStatus.setCellValue("入库原因");
        //入库原因选择
//        HSSFCell cellStorageStatusValue = rowZero.createCell(1);
//        String[] storageStatusValueList = { "新机入库", "退货入库" };
//        sheet = setHSSFValidation(sheet, storageStatusValueList, 0, 0, 1, 1);// 第一行第二列设置为选择列表形式.

        //创建第二行字段头
        HSSFRow rowOne = sheet.createRow(0);
        //IMEI码
        HSSFCell cellImeiNo = rowOne.createCell(0);
        cellImeiNo.setCellValue("IMEI码");

        //手机型号
        HSSFCell cellPhoneModel = rowOne.createCell(1);
        cellPhoneModel.setCellValue("手机型号");


        //颜色
        HSSFCell cellColor = rowOne.createCell(2);
        cellColor.setCellValue("颜色");

        //厂商
        HSSFCell cellManufacturer = rowOne.createCell(3);
        cellManufacturer.setCellValue("厂商");



        //备注
        HSSFCell cellComment = rowOne.createCell(4);
        cellComment.setCellValue("备注");

//        //状态
//        HSSFCell cellUseStatus = rowOne.createCell(5);
//        cellUseStatus.setCellValue("状态");
//        String[] useStatusValueList = { "可用","已冻结" };
//        sheet = setHSSFValidation(sheet, useStatusValueList, 2, 3000, 5, 5);// 第一行第二列设置为选择列表形式.

        String fileName = sheetName + ".xls";
        String userAgent = request.getHeader("USER-AGENT");
        HSSFUtils.exportExcel(userAgent, response, wb, fileName);
    }

//    /**
//     * 设置某些列的值只能输入预制的数据,显示下拉框.
//     * @param sheet 要设置的sheet.
//     * @param textlist 下拉框显示的内容
//     * @param firstRow 开始行
//     * @param endRow 结束行
//     * @param firstCol   开始列
//     * @param endCol  结束列
//     * @return 设置好的sheet.
//     */
//    public static HSSFSheet setHSSFValidation(HSSFSheet sheet,
//                                                   String[] textlist, int firstRow, int endRow, int firstCol,
//                                                   int endCol) {
//        // 加载下拉列表内容
//        DVConstraint constraint = DVConstraint.createExplicitListConstraint(textlist);
//        // 设置数据有效性加载在哪个单元格上,四个参数分别是：起始行、终止行、起始列、终止列
//        CellRangeAddressList regions = new CellRangeAddressList(firstRow,endRow, firstCol, endCol);
//        // 数据有效性对象
//        HSSFDataValidation data_validation_list = new HSSFDataValidation(regions, constraint);
//        sheet.addValidationData(data_validation_list);
//        return sheet;
//    }

    /**
     * 导入操作
     * @param inputStream
     * @param request
     * @return
     * @throws Exception
     */
    public String importStorageTemplate(InputStream inputStream, HttpServletRequest request) throws Exception {
        HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
        HSSFSheet sheet = workbook.getSheet(workbook.getSheetName(0));

        //封装新增进库存数据声明
        List<DBObject> dbObjectList = new ArrayList<DBObject>();
        StorageManageEntry storageManageEntry = null;
        //封装出入库记录数据声明
        List<DBObject> inOutDbObjectList = new ArrayList<DBObject>();
        InOutStorageEntry inOutStorageEntry = null;
        //对当前excel里面imeiNo的数据判重
        List<String> imeiNoList = new ArrayList<String>();
        //开始解析封装数据
        List<String> needRepairComment = new ArrayList<String>();
        StringBuffer sb = new StringBuffer();
        for (int i = 1 ; i<= sheet.getLastRowNum();i++){
            sheet.getRow(i).getCell(0).setCellType(Cell.CELL_TYPE_STRING);//防止imeiNo是纯数字，读取string类型报错
            String imeiNo = sheet.getRow(i).getCell(0) == null ? "" : sheet.getRow(i).getCell(0).getStringCellValue();
            String phoneModel = sheet.getRow(i).getCell(1) == null ? "" : sheet.getRow(i).getCell(1).getStringCellValue();
            String color = sheet.getRow(i).getCell(2) == null ? "" : sheet.getRow(i).getCell(2).getStringCellValue();
            String manufacturer = sheet.getRow(i).getCell(3) == null ? "" : sheet.getRow(i).getCell(3).getStringCellValue();
            String comment = sheet.getRow(i).getCell(4) == null ? "" : sheet.getRow(i).getCell(4).getStringCellValue();
            //手机IMEI查验，如果更新过则跳过，并告知前台
            int exist = storageManageDao.findDataByImeiNo(imeiNo);
            //对当前excel里面imeiNo的数据判重
            if (imeiNoList.contains(imeiNo)){
                exist = 1;
            }else {
                imeiNoList.add(imeiNo);
            }
            if (exist == 0){
                //封装新增进库存数据
                storageManageEntry = new StorageManageEntry(
                        imeiNo,
                        phoneModel,
                        color,
                        manufacturer,
                        "0",//新机入库
                        comment,
                        "1",//可用
                        "",
                        needRepairComment
                );
                dbObjectList.add(storageManageEntry.getBaseEntry());
                //封装出入库记录数据
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Calendar calendar = Calendar.getInstance();
                inOutStorageEntry = new InOutStorageEntry(
                        imeiNo,
                        phoneModel,
                        color,
                        manufacturer,
                        dateFormat.format(new Date()),
                        calendar.get(Calendar.YEAR)+"",
                        (calendar.get(Calendar.MONTH) + 1)+"",
                        "0",
                        comment,
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "0",//新机入库
                        "",
                        needRepairComment
                );
                inOutDbObjectList.add(inOutStorageEntry.getBaseEntry());
            }else {
                sb.append("<p>第"+i+"列：");
                sb.append("  -----<span style='color:red;'>IMEI号"+imeiNo+"已存在！</span>");
                sb.append("</p>");
            }

        }
        //当有新增的才进入保存
        if (dbObjectList.size()>0){
            storageManageDao.saveList(dbObjectList);
            inOutStorageRecordDao.addProjectOutStorageRecordList(inOutDbObjectList);
        }

        return sb == null?"success!":sb.toString();
    }

    /**
     * 库存Table数据查询
     * @param page
     * @param pageSize
     * @param imeiNo
     * @param storageStatus
     * @param useStatus
     * @param year
     * @param month
     * @return
     */
    public Map<String,Object> getStorageInfoList(int page, int pageSize, String imeiNo, String storageStatus, String useStatus, String year, String month) {
        Map<String,Object> result = new HashMap<String, Object>();
        result = storageManageDao.getStorageInfoList(page, pageSize, imeiNo, storageStatus, useStatus, year, month);

        List<StorageManageDto> storageManageDtoList = new ArrayList<StorageManageDto>();
        List<StorageManageEntry> storageManageEntryList = (ArrayList)result.get("entryList");
        for (StorageManageEntry storageManageEntry : storageManageEntryList){
            if (storageManageEntry != null){
                storageManageDtoList.add(new StorageManageDto(storageManageEntry));
            }
        }
        result.put("dtos",storageManageDtoList);
        result.remove("entryList");
        return result;
    }

    /**
     * 库存查询下拉条件
     * @return
     */
    public Map<String,Object> getStorageOption() {
        Map<String,Object> result = new HashMap<String, Object>();
        //库存总量
        int countAll = storageManageDao.getStorageCount();
        result.put("countAll",countAll);
        //入库原因分组
        int countNewJoin = storageManageDao.getStorageListGroupByStorageStatus();
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("name","全部("+countAll+")");
        jsonObject1.put("value","");
        jsonArray.add(jsonObject1);

        JSONObject jsonObject2 = new JSONObject();
        jsonObject2.put("name","新机入库("+countNewJoin+")");
        jsonObject2.put("value","0");
        jsonArray.add(jsonObject2);

        JSONObject jsonObject3 = new JSONObject();
        jsonObject3.put("name","其他入库("+(countAll-countNewJoin)+")");
        jsonObject3.put("value","1+3+4");
        jsonArray.add(jsonObject3);

        result.put("storageStatusOption",jsonArray);

        //状态分组
        int countCanUse = storageManageDao.getStorageListGroupByUseStatus();
        JSONArray jsonArrayStatus = new JSONArray();
        JSONObject jsonObject1Status = new JSONObject();
        jsonObject1Status.put("name","全部("+countAll+")");
        jsonObject1Status.put("value","");
        jsonArrayStatus.add(jsonObject1Status);

        JSONObject jsonObject2Status = new JSONObject();
        jsonObject2Status.put("name","可用("+countCanUse+")");
        jsonObject2Status.put("value","1");
        jsonArrayStatus.add(jsonObject2Status);

        JSONObject jsonObject3Status = new JSONObject();
        jsonObject3Status.put("name","冻结("+(countAll-countCanUse)+")");
        jsonObject3Status.put("value","0");
        jsonArrayStatus.add(jsonObject3Status);

        result.put("useStatusOption",jsonArrayStatus);

        return result;
    }

    public String updateStorageInfoById(Map map) {
        return storageManageDao.updateStorageInfoById(map);
    }

    public String updateStorageInfoByIds(Map map) {
        return storageManageDao.updateStorageInfoByIds(map);
    }
}
