package com.fulaan.salary.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.fulaan.utils.ExportUtil;
import com.pojo.exam.ExamSubjectDTO;
import com.pojo.exam.ScoreDTO;
import com.pojo.exam.ScoreEntry;
import com.pojo.salary.*;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import com.db.salary.SalaryDao;
import com.db.salary.SalaryItemDao;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.app.Page;
import com.pojo.salary.SalaryDto;
import com.sys.constants.Constant;

/**
 * 工资管理
 *
 * @author yang.ling
 */
@Service
public class SalaryService {

    private static final Logger logger = Logger.getLogger(SalaryService.class);

    private SalaryDao salaryDao = new SalaryDao();
    private SalaryItemDao salaryItemDao = new SalaryItemDao();

    /**
     * 批量添加工资数据
     *
     * @param @return 设定文件
     * @return String    返回类型
     * @throws
     * @Title: addSalaryInfo
     */
    public void addSalaryInfoBatch(List<SalaryDto> list) {
        List<DBObject> dbList = new ArrayList<DBObject>();
        for (int i = 0; list != null && i < list.size(); i++) {
            SalaryDto si = list.get(i);
            SalaryEntry obj = list.get(i).exportEntry(
                    si.getUid(), si.getUserName(),
                    si.getYear(), si.getMonth(), si.getNumber(), si.getMoney());
            dbList.add(obj.getBaseEntry());
        }
        salaryDao.addBatch(dbList);
    }

    /**
     * 修改工资数据
     *
     * @param @return 设定文件
     * @return String    返回类型
     * @throws
     * @Title: addSalaryInfo
     */
    public SalaryDto updateSalaryInfo(String id, String itemName, double m) {
        salaryDao.update(id, itemName, m);
        SalaryDto salaryDto = getSalaryDto(id);
        if (salaryDto != null && !salaryDto.getMoney().isEmpty()) {
            salaryDto.calculateRealSalary();
            salaryDao.updateRealSalary(id, salaryDto.getSs(), salaryDto.getMs(), salaryDto.getAs(),salaryDto.getRemark());
            List<SalaryItemDto> debit = new ArrayList<SalaryItemDto>();
            for (SalaryItemDto salaryItemDto : salaryDto.getMoney()) {
                if (!salaryItemDto.getType().equals("发款")) {
                    debit.add(salaryItemDto);
                }
            }
            salaryDto.setDebitCount(debit.size());
        }
        return salaryDto;
    }


    /**
     * 修改工资信息
     *
     * @param salaryDto
     */
    public void updateSalaryByImport(SalaryDto salaryDto) {
        if (salaryDto.getMoney().size() > 0) {
            salaryDao.updateAllSalaryItem(salaryDto);
            SalaryDto nowDto = getSalaryDto(salaryDto.getId());
            if (nowDto != null && !nowDto.getMoney().isEmpty()) {
                nowDto.calculateRealSalary();
                salaryDao.updateRealSalary(nowDto.getId(), nowDto.getSs(), nowDto.getMs(), nowDto.getAs(),salaryDto.getRemark());
            }
        }
    }

    /**
     * 获取薪资信息
     *
     * @param id
     * @return
     */
    public SalaryDto getSalaryDto(String id) {
        SalaryEntry entry = salaryDao.getSalaryDto(id);
        return entry == null ? null : new SalaryDto(entry);
    }

    /**
     * 查询工资列表信息
     *
     * @param @param userId
     * @param @param startY
     * @param @param endY
     * @param @param startM
     * @param @param endM
     * @return List<SalaryDto>    返回类型
     * @throws
     * @Title: findSalaryByInfo
     */
    public List<SalaryDto> findSalaryInfo(String userId, int start, int end) {
        BasicDBObject query = new BasicDBObject("uid", new ObjectId(userId)).append("dl", 0)
                .append("ym", new BasicDBObject(Constant.MONGO_GTE, start).append(Constant.MONGO_LTE, end));
        List<SalaryEntry> salaryEntryList = salaryDao.getSalaryEntryList(query, Constant.FIELDS);
        List<SalaryDto> salaryInfoList = new ArrayList<SalaryDto>();
        for (SalaryEntry salaryEntry : salaryEntryList) {
            SalaryDto salaryInfo = new SalaryDto(salaryEntry);
            salaryInfoList.add(salaryInfo);
        }
        return salaryInfoList;
    }

    /**
     * 根据年、月、日查询所有工资信息
     *
     * @param @param year
     * @param @param month
     * @param @param num
     * @return List<SalaryDto>    返回类型
     * @throws
     * @Title: findSalaryInfo
     */
    public List<SalaryDto> findSalaryInfo(String schoolId,
                                          int year, int month, int num) {
        BasicDBObject query = new BasicDBObject("y", year).append("sid", new ObjectId(schoolId))
                .append("m", month)
                .append("n", num)
                .append("dl",0);
        List<SalaryEntry> salaryEntryList = salaryDao.getSalaryEntryList(query, Constant.FIELDS);
        List<SalaryDto> salaryInfoList = new ArrayList<SalaryDto>();
        for (SalaryEntry salaryEntry : salaryEntryList) {
            SalaryDto salaryInfo = new SalaryDto(salaryEntry);
            salaryInfoList.add(salaryInfo);
        }
        return salaryInfoList;
    }

    /**
     * 生成模板
     *
     * @param util
     * @param year
     * @param month
     * @param num
     * @param schoolId
     */
    public void createTemplate(ExportUtil util, int year, int month, int num, String schoolId) {
        List<SalaryDto> salaryDtos = this.findSalaryInfo(schoolId, year, month, num);
        if (salaryDtos.size() > 0) {
            SalaryDto dto = salaryDtos.get(0);
            String fileName = dto.getTimesName();
            List<SalaryItemDto> salaryItemDtos = dto.getMoney();
            List<String> salarys = new ArrayList<String>(salaryItemDtos.size());
            List<String> datas = new ArrayList<String>(salaryItemDtos.size() + Constant.TWO);
            datas.add("标识");
            datas.add("姓名");
            for (SalaryItemDto dto1 : salaryItemDtos) {
                datas.add(dto1.getItemName());
                salarys.add("0");
            }
            datas.add("备注");
            util.addTitle(datas.toArray());
            //填充模板的数据区域
            datas.clear();
            for (SalaryDto salaryDto : salaryDtos) {
                datas.add(salaryDto.getId());
                datas.add(salaryDto.getUserName());
                datas.addAll(salarys);
                datas.add(salaryDto.getRemark());
                util.appendRow(datas.toArray());
                datas.clear();
            }
            util.setFileName(String.format("%s_%s", fileName, "模板.xlsx"));
        } else {
            List<String> datas = new ArrayList<String>(Constant.ONE);
            datas.add("未找到本次工资发放的人员信息");
            util.addTitle(datas.toArray());
            util.setFileName("工资模板.xlsx");
        }
    }


    /**
     * 修改某次工资的名称
     *
     * @param year
     * @param month
     * @param num
     * @param name
     */
    public void renameSalary(final String schoolId, final int year, final int month, final int num, final String name) {
        salaryDao.renameSalary(new ObjectId(schoolId), year, month, num, name);
    }

    /**
     * 添加工资条目信息
     *
     * @param @param  itemEntry
     * @param @return 设定文件
     * @return String    返回类型
     * @throws
     * @Title: addSalaryItemInfo
     */
    public String addSalaryItemInfo(SalaryItemDto itemInfo) {
        ObjectId id = this.salaryItemDao.add(itemInfo.exportEntryWithSID());
        return id.toString();
    }

    /**
     * 修改工资条目信息
     *
     * @param @param  itemEntry
     * @param @return 设定文件
     * @return String    返回类型
     * @throws
     * @Title: addSalaryItemInfo
     */
    public String updateSalaryItemInfo(SalaryItemDto itemInfo) {
        return this.salaryItemDao.update(itemInfo.getId(), itemInfo.exportEntry().getBaseEntry());
    }

    /**
     * 删除工资条目信息
     *
     * @param @param  itemEntry
     * @param @return 设定文件
     * @return String    返回类型
     * @throws
     * @Title: addSalaryItemInfo
     */
    public String deleteSalaryItemInfo(SalaryItemDto itemInfo) {
        if (null == itemInfo) {
            return null;
        }
        return this.salaryItemDao.delete(itemInfo.getId());
    }


    /**
     * 分页查询工资条目信息
     *
     * @param @param  page
     * @param @return 设定文件
     * @return List<SalaryItemDto>    返回类型
     * @throws
     * @Title: findSalaryItemInfo
     */
    public List<SalaryItemDto> findSalaryItemInfo(ObjectId schoolId) {

        List<SalaryItemEntry> itemEntryList = salaryItemDao.getSalaryItemEntryList(schoolId,Constant.FIELDS);

        List<SalaryItemDto> itemList = new ArrayList<SalaryItemDto>();
        for (SalaryItemEntry itemEntry : itemEntryList) {
            SalaryItemDto salaryInfo = new SalaryItemDto(itemEntry);
            itemList.add(salaryInfo);
        }
        return itemList;
    }

    /**
     * 删除指定工资表
     *
     * @param schoolId
     * @param year
     * @param month
     * @param num
     */
    public void deleteSalary(final String schoolId, final int year, final int month, final int num) {
        salaryDao.deleteSalary(new ObjectId(schoolId), year, month, num);
    }

    /**
     * 获取工资发放次数列表
     *
     * @param schoolId
     * @param year
     * @param month
     * @return
     */
    public Object[] getSalaryTimes(final String schoolId, final int year, final int month) {
        return salaryDao.getSalaryTimes(schoolId, year, month);
    }

    /**
     * 获取指定月份总的工资次数
     *
     * @param schoolId
     * @param year
     * @param month
     * @return
     */
    public int countSalaryTimes(final String schoolId, final int year, final int month) {
        return salaryDao.countSalaryTimes(schoolId, year, month);
    }

    /**
     *
     * @param itemInfo
     * @return
     */
    public int checkSalaryItem(SalaryItemDto itemInfo) {
        return salaryDao.selSalaryItem(itemInfo.getItemName(),itemInfo.getType(),itemInfo.getSchoolId());
    }

    public void updateRemark(String id, String m) {
        salaryDao.updateRemark(new ObjectId(id),m);
    }
}
