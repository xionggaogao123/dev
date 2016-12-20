package com.fulaan.util;

import com.db.train.InstituteCopyDao;
import com.db.train.ItemTypeNameCopyDao;
import com.db.train.RegionCopyDao;
import com.fulaan.cache.RedisUtils;
import com.pojo.questions.PropertiesObj;
import com.pojo.train.InstituteCopyEntry;
import com.pojo.train.ItemTypeCopyEntry;
import com.pojo.train.RegionCopyEntry;
import com.sys.constants.Constant;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.bson.types.ObjectId;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by admin on 2016/12/19.
 */
public class DatahandleUtil {

    private static RegionCopyDao regionCopyDao = new RegionCopyDao();
    private static InstituteCopyDao instituteCopyDao = new InstituteCopyDao();
    private static ItemTypeNameCopyDao itemTypeNameCopyDao = new ItemTypeNameCopyDao();

    public static void main(String[] args) throws Exception {
        start();
    }

    private static void exportData(List<PropertiesObj> areas, List<PropertiesObj> types) throws Exception {
        List<PropertiesObj> tempAreas = new ArrayList<PropertiesObj>();
        List<PropertiesObj> tempTypes = new ArrayList<PropertiesObj>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        File file = new File("D:\\gansu\\lanzhou.xlsx");
        XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(file));

        Sheet sheet = wb.getSheetAt(0);

        // 解析公式结果
        FormulaEvaluator evaluator = wb.getCreationHelper()
                .createFormulaEvaluator();

        int minRowIx = sheet.getFirstRowNum();
        int maxRowIx = sheet.getLastRowNum();
        List<InstituteCopyEntry> entries = new ArrayList<InstituteCopyEntry>();
        for (int rowIx = minRowIx + 1; rowIx <= maxRowIx; rowIx++) {
            Row row = sheet.getRow(rowIx);
            short minColIx = row.getFirstCellNum();
            short maxColIx = row.getLastCellNum();
            InstituteCopyEntry instituteCopyEntry = new InstituteCopyEntry();
            tempAreas.clear();
            tempTypes.clear();
            tempAreas.addAll(areas);
            tempTypes.addAll(types);
            for (short colIx = minColIx; colIx <= maxColIx; colIx++) {
                Integer integer = new Integer(colIx);
                Cell cell = row.getCell(integer);
                CellValue cellValue = evaluator.evaluate(cell);
                if (cellValue == null) {
                    continue;
                }
                // 经过公式解析，最后只存在Boolean、Numeric和String三种数据类型，此外就是Error了
                // 其余数据类型，根据官方文档，完全可以忽略http://poi.apache.org/spreadsheet/eval.html
                String temp = "";
                switch (cellValue.getCellType()) {
                    case Cell.CELL_TYPE_BOOLEAN:
                        temp = String.valueOf(cellValue.getBooleanValue());
                        break;
                    case Cell.CELL_TYPE_NUMERIC:
                        // 这里的日期类型会被转换为数字类型，需要判别后区分处理
                        if (DateUtil.isCellDateFormatted(cell)) {
                            temp = sdf.format(cell.getDateCellValue());
                        } else {
                            temp = String.valueOf(cellValue.getNumberValue());
                        }
                        break;
                    case Cell.CELL_TYPE_STRING:
                        temp = cellValue.getStringValue();
                        break;
                    case Cell.CELL_TYPE_FORMULA:
                        break;
                    case Cell.CELL_TYPE_BLANK:
                        break;
                    case Cell.CELL_TYPE_ERROR:
                        break;
                    default:
                        break;
                }

                setData(instituteCopyEntry, integer, temp, tempAreas, tempTypes);

            }
            entries.add(instituteCopyEntry);
        }

        //执行批量添加
        instituteCopyDao.batchInsertData(entries);
    }

    private static void setTempTypeData(Map<String, String> map, String temp, InstituteCopyEntry instituteCopyEntry, List<PropertiesObj> tempTypes, int itemType) {

        //针对层级二的处理
        int level = tempTypes.size() + 1;
        PropertiesObj type = new PropertiesObj();
        if (null != map && !map.isEmpty()) {
            String typeId = map.get("id");
            String lel = map.get("lel");
            String pid = map.get("pid");
            String pName = map.get("pName");
            if (itemType == 3 && lel.equals(String.valueOf(3))) {
                PropertiesObj obj=new PropertiesObj();
                obj.setId(pid);
                obj.setName(pName);
                tempTypes.add(obj);
            }
            type.setId(typeId);
            type.setName(temp);
        } else {
            map = new HashMap<String, String>();
            ItemTypeCopyEntry itemTypeCopyEntry = itemTypeNameCopyDao.getEntryByName(temp);
            if (null != itemTypeCopyEntry) {
                type.setId(itemTypeCopyEntry.getID().toString());
                type.setName(temp);
                map.put("id", itemTypeCopyEntry.getID().toString());
                map.put(itemTypeCopyEntry.getID().toString(),itemTypeCopyEntry.getName());
                map.put("lel", String.valueOf(itemTypeCopyEntry.getLevel()));
                String pName=map.get(itemTypeCopyEntry.getParentId().toString());
                if(StringUtils.isNotBlank(pName)){
                    if(itemType == 3 && itemTypeCopyEntry.getLevel()==3) {
                        PropertiesObj obj = new PropertiesObj();
                        obj.setId(map.get("pid"));
                        obj.setName(pName);
                        tempTypes.add(obj);
                    }
                }else {
                    //查询数据
                    map.put("pid", itemTypeCopyEntry.getParentId().toString());
                    ItemTypeCopyEntry entryByName = itemTypeNameCopyDao.find(itemTypeCopyEntry.getParentId());
                    map.put("pName", entryByName.getName());
                }

                RedisUtils.cacheMap(temp, map, Constant.SESSION_TIMEOUT_SECOND_INT);
            } else {
                map.put("pid", tempTypes.get(tempTypes.size() - 1).getId());
                map.put("pName", tempTypes.get(tempTypes.size() - 1).getName());
                ItemTypeCopyEntry entry = new ItemTypeCopyEntry(level, new ObjectId(tempTypes.get(tempTypes.size() - 1).getId()), temp);
                ObjectId id = itemTypeNameCopyDao.saveOrUpdate(entry);
                type.setId(id.toString());
                type.setName(temp);
                map.put("id", id.toString());
                map.put("lel", String.valueOf(level));
                RedisUtils.cacheMap(temp, map, Constant.SESSION_TIMEOUT_SECOND_INT);
            }
        }
        tempTypes.add(type);
        if (StringUtils.isNotBlank(temp) && itemType == 4) {
            instituteCopyEntry.setTypes(tempTypes);
        }

    }

    public static void setData(InstituteCopyEntry instituteCopyEntry, Integer integer, String temp, List<PropertiesObj> tempAreas, List<PropertiesObj> tempTypes) {
        instituteCopyEntry.setRemove(0);
        Map<String, String> map = RedisUtils.getMap(temp);
        switch (integer) {
            case 1:
                if (StringUtils.isNotBlank(temp)) {
                    PropertiesObj area = new PropertiesObj();
                    int regionLevel = 3;
                    if (null != map && !map.isEmpty()) {
                        String areaId = map.get("id");
                        area.setId(areaId);
                        area.setName(temp);
                    } else {
                        map = new HashMap<String, String>();
                        RegionCopyEntry regionCopyEntry = regionCopyDao.getEntryByName(temp);
                        if (null != regionCopyEntry) {
                            area.setId(regionCopyEntry.getID().toString());
                            area.setName(temp);
                            map.put("id", regionCopyEntry.getID().toString());
                            RedisUtils.cacheMap(temp, map, Constant.SESSION_TIMEOUT_SECOND_INT);
                        } else {
                            RegionCopyEntry entry = new RegionCopyEntry(regionLevel, new ObjectId(tempAreas.get(tempAreas.size() - 1).getId()), temp);
                            ObjectId id = regionCopyDao.saveOrUpdate(entry);
                            area.setId(id.toString());
                            area.setName(temp);
                            map.put("id", id.toString());
                            RedisUtils.cacheMap(temp, map, Constant.SESSION_TIMEOUT_SECOND_INT);
                        }
                    }
                    tempAreas.add(area);
                }
                instituteCopyEntry.setAreas(tempAreas);
                break;
            case 2:
                break;
            case 3:
                if (StringUtils.isNotBlank(temp)) {
                    setTempTypeData(map, temp, instituteCopyEntry, tempTypes, 3);
                }
                break;
            case 4:
                if (StringUtils.isNotBlank(temp)) {
                    setTempTypeData(map, temp, instituteCopyEntry, tempTypes, 4);
                } else {
                    instituteCopyEntry.setTypes(tempTypes);
                }
                break;
            case 5:
                if (StringUtils.isNotBlank(temp)) {
                    instituteCopyEntry.setName(temp);
                } else {
                    instituteCopyEntry.setName(Constant.EMPTY);
                }
                break;
            case 6:
                if (StringUtils.isNotBlank(temp)) {
                    instituteCopyEntry.setTelephone(temp.replace("咨询电话：", "").trim());
                } else {
                    instituteCopyEntry.setTelephone(Constant.EMPTY);
                }
                break;
            case 7:
                if (StringUtils.isNotBlank(temp)) {
                    instituteCopyEntry.setAddress(temp.replace("地址：", "").trim());
                } else {
                    instituteCopyEntry.setAddress(Constant.EMPTY);
                }
                break;
            case 8:
                if (StringUtils.isNotBlank(temp)) {
                    instituteCopyEntry.setChuangLiShiJian(temp.replace("<span class=\"title\">创立时间</span>", "").trim());
                } else {
                    instituteCopyEntry.setChuangLiShiJian(Constant.EMPTY);
                }
                break;
            case 9:
                if (StringUtils.isNotBlank(temp)) {
                    instituteCopyEntry.setShangHuJianJie(temp.replace("<span class=\"title\">商户介绍</span>", "").trim());
                } else {
                    instituteCopyEntry.setShangHuJianJie(Constant.EMPTY);
                }
                break;
            case 10:
                if (StringUtils.isNotBlank(temp)) {
                    instituteCopyEntry.setYingYeShiJian(temp.replace("<span class=\"title\">营业时间</span>", "").trim());
                } else {
                    instituteCopyEntry.setYingYeShiJian(Constant.EMPTY);
                }
                break;
            case 11:
                if (StringUtils.isNotBlank(temp)) {
                    String tempStr = temp.replace("特色服务", "").trim();
                    if (tempStr.contains(" ")) {
                        String[] str = tempStr.split(" ");
                        StringBuffer buffer = new StringBuffer();
                        for (String item : str) {
                            buffer.append(item.trim() + "|");
                        }
                        instituteCopyEntry.setTeSeFuWu(buffer.toString().substring(0, buffer.toString().length() - 1));
                    } else {
                        instituteCopyEntry.setTeSeFuWu(tempStr);
                    }
                } else {
                    instituteCopyEntry.setTeSeFuWu(Constant.EMPTY);
                }
                break;
            case 14:
                if (StringUtils.isNotBlank(temp)) {
                    Set<String> imageUrls = getImgStr(temp);
                    if (imageUrls.size() >= 0) {
                        List<String> image = new ArrayList<String>(imageUrls);
                        instituteCopyEntry.setMainPic(image.get(0));
                    } else {
                        instituteCopyEntry.setMainPic("http://www.fulaan.com/static/images/upload/default.png");
                    }
                } else {
                    instituteCopyEntry.setMainPic("http://www.fulaan.com/static/images/upload/default.png");
                }
                break;
            default:
                break;
        }
    }

    public static Set<String> getImgStr(String htmlStr) {
        Set<String> pics = new HashSet<String>();
        String img = "";
        Pattern p_image;
        Matcher m_image;
        //     String regEx_img = "<img.*src=(.*?)[^>]*?>"; //图片链接地址
        String regEx_img = "<img.*src\\s*=\\s*(.*?)[^>]*?>";
        p_image = Pattern.compile
                (regEx_img, Pattern.CASE_INSENSITIVE);
        m_image = p_image.matcher(htmlStr);
        while (m_image.find()) {
            // 得到<img />数据
            img = m_image.group();
            // 匹配<img>中的src数据
            Matcher m = Pattern.compile("src\\s*=\\s*\"?(.*?)(\"|>|\\s+)").matcher(img);
            while (m.find()) {
                pics.add(m.group(1));
            }
        }
        return pics;
    }

    public static void start() throws Exception {
        //拓展数据
        String[] Provinces = {"甘肃省"};
        String[] cities = {"兰州市"};
        String topType = "找培训";
        List<PropertiesObj> areas = new ArrayList<PropertiesObj>();
        List<PropertiesObj> types = new ArrayList<PropertiesObj>();
        for (String city : cities) {
            String province = Provinces[0];
            RegionCopyEntry regionCopyEntry = regionCopyDao.getEntryByName(province);
            ItemTypeCopyEntry itemTypeCopyEntry = itemTypeNameCopyDao.getEntryByName(topType);
            PropertiesObj obj = new PropertiesObj();
            if (null != regionCopyEntry) {
                int level = regionCopyEntry.getLevel();
                level += 1;
                obj.setId(regionCopyEntry.getID().toString());
                obj.setName(regionCopyEntry.getName());
                areas.add(obj);
                PropertiesObj obj1 = new PropertiesObj();
                obj1.setName(city);
                RegionCopyEntry copyEntry = regionCopyDao.getEntryByName(city);
                if (null != copyEntry) {
                    obj1.setId(copyEntry.getID().toString());
                } else {
                    RegionCopyEntry entry = new RegionCopyEntry(level, regionCopyEntry.getID(), city);
                    ObjectId id = regionCopyDao.saveOrUpdate(entry);
                    obj1.setId(id.toString());
                }
                areas.add(obj1);
            }

            if (null != itemTypeCopyEntry) {
                PropertiesObj propertiesObj = new PropertiesObj();
                propertiesObj.setId(itemTypeCopyEntry.getID().toString());
                propertiesObj.setName(itemTypeCopyEntry.getName());
                types.add(propertiesObj);
            }

            exportData(areas, types);

        }
    }
}
