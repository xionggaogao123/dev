package com.fulaan.wages.service;

import com.db.salary.SalaryDao;
import com.fulaan.utils.ExportUtil;
import com.mongodb.BasicDBObject;
import com.pojo.salary.SalaryDto;
import com.pojo.salary.SalaryEntry;
import com.pojo.salary.SalaryItem;
import com.pojo.salary.SalaryItemDto;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wang_xinxin on 2016/6/16.
 */

@Service
public class WagesService {

    private SalaryDao salaryDao = new SalaryDao();

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
    public Map findSalaryInfo(String schoolId,String userId,String name,
                                          int year, int month, int num,int page,int pageSize) {
        BasicDBObject query = new BasicDBObject("y", year).append("sid", new ObjectId(schoolId))
                .append("m", month)
                .append("n", num)
                .append("dl",0);

        if (!StringUtils.isEmpty(userId)) {
            query.append("uid",new ObjectId(userId));
        }
        if (!StringUtils.isEmpty(name)) {
            query.append("unm",MongoUtils.buildRegex(name));
        }
        if (pageSize == 0) {
            pageSize = 12;
        }
        List<SalaryEntry> salaryEntryList = salaryDao.getSalaryEntryList(query,page < 1 ? 0 : ((page - 1) * pageSize),pageSize, Constant.FIELDS);
        int count = salaryDao.getSalaryCount(query);
        List<SalaryDto> salaryInfoList = new ArrayList<SalaryDto>();
        for (SalaryEntry salaryEntry : salaryEntryList) {
            List<SalaryItem> salaryItemList = new ArrayList<SalaryItem>();
            SalaryDto salaryInfo = new SalaryDto(salaryEntry);
            List<SalaryItemDto> debitList = new ArrayList<SalaryItemDto>();
            List<SalaryItemDto> sendList = new ArrayList<SalaryItemDto>();
            SalaryItem salaryItem = new SalaryItem();
            for (SalaryItemDto salaryItemDto : salaryInfo.getMoney()) {
                if (salaryItemDto.getType().equals("发款")) {
                    sendList.add(salaryItemDto);
                } else if (salaryItemDto.getType().equals("扣款")) {
                    debitList.add(salaryItemDto);
                }
            }
            salaryItem.setDebitList(debitList);
            salaryItem.setSendList(sendList);
            salaryItem.setDebit(debitList.size() + 1);
            salaryItem.setSend(sendList.size() + 1);
            salaryItemList.add(salaryItem);
            salaryInfo.setSalaryItem(salaryItemList);
            salaryInfoList.add(salaryInfo);
        }
        Map map = new HashMap();
        map.put("salarylist",salaryInfoList);
        map.put("total", count);
        map.put("page", page);
        map.put("pageSize", pageSize);
        return map;
    }


    /**
     *
     * @param userId
     * @param year
     * @param month
     * @param num
     * @return
     */
    public List<SalaryDto> findMySalaryDetail(ObjectId userId,
                                          int year, int month, int num) {
        BasicDBObject query = new BasicDBObject("y", year).append("uid", userId)
                .append("m", month)
                .append("n", num)
                .append("dl",0);
        List<SalaryEntry> salaryEntryList = salaryDao.getSalaryEntryList(query, Constant.FIELDS);
        List<SalaryDto> salaryInfoList = new ArrayList<SalaryDto>();
        for (SalaryEntry salaryEntry : salaryEntryList) {
            List<SalaryItem> salaryItemList = new ArrayList<SalaryItem>();
            SalaryDto salaryInfo = new SalaryDto(salaryEntry);
            List<List<SalaryItemDto>> debitList = new ArrayList<List<SalaryItemDto>>();
            List<List<SalaryItemDto>> sendList = new ArrayList<List<SalaryItemDto>>();
            List<SalaryItemDto> debit = new ArrayList<SalaryItemDto>();
            List<SalaryItemDto> send = new ArrayList<SalaryItemDto>();
            SalaryItem salaryItem = new SalaryItem();
            for (SalaryItemDto salaryItemDto : salaryInfo.getMoney()) {
                if (salaryItemDto.getType().equals("发款")) {
                    send.add(salaryItemDto);
                } else if (salaryItemDto.getType().equals("扣款")) {
                    debit.add(salaryItemDto);
                }
            }

            salaryItem.setDebitList(debit);
            salaryItem.setSendList(send);
            salaryItem.setDebit(debit.size());
            salaryItem.setSend(send.size());
            salaryItemList.add(salaryItem);
            salaryInfo.setSalaryItem(salaryItemList);

            if (debit.size()!=0) {
                int a = debit.size()/3;
                int b = debit.size()%3;
                if (b!=0) {
                    for (int i=0;i<a;i++) {
                        debitList.add(debit.subList(i*3,i*3+3));
                    }
                    debitList.add(debit.subList(a*3,a*3+b));
                } else {
                    for (int i=0;i<a;i++) {
                        debitList.add(debit.subList(i*3,i*3+3));
                    }
                }
            }
            if (send.size()!=0) {
                int a = send.size()/3;
                int b = send.size()%3;
                if (b!=0) {
                    for (int i=0;i<a;i++) {
                        sendList.add(send.subList(i*3,i*3+3));
                    }
                    sendList.add(send.subList(a*3,a*3+b));
                } else {
                    for (int i=0;i<a;i++) {
                        sendList.add(send.subList(i*3,i*3+3));
                    }
                }
            }
            salaryInfo.setSendList(sendList);
            salaryInfo.setDebitList(debitList);
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
    public List<SalaryDto> findSalaryInfo(String schoolId,String userId,
                                          int year, int month, int num) {
        BasicDBObject query = new BasicDBObject("y", year).append("sid", new ObjectId(schoolId))
                .append("m", month)
                .append("n", num)
                .append("dl",0);

        if (!StringUtils.isEmpty(userId)) {
            query.append("uid",new ObjectId(userId));
        }
        List<SalaryEntry> salaryEntryList = salaryDao.getSalaryEntryList(query, Constant.FIELDS);
        List<SalaryDto> salaryInfoList = new ArrayList<SalaryDto>();
        for (SalaryEntry salaryEntry : salaryEntryList) {
            List<SalaryItem> salaryItemList = new ArrayList<SalaryItem>();
            SalaryDto salaryInfo = new SalaryDto(salaryEntry);
            List<SalaryItemDto> debitList = new ArrayList<SalaryItemDto>();
            List<SalaryItemDto> sendList = new ArrayList<SalaryItemDto>();
            SalaryItem salaryItem = new SalaryItem();
            for (SalaryItemDto salaryItemDto : salaryInfo.getMoney()) {
                if (salaryItemDto.getType().equals("发款")) {
                    sendList.add(salaryItemDto);
                } else if (salaryItemDto.getType().equals("扣款")) {
                    debitList.add(salaryItemDto);
                }
            }
            salaryItem.setDebitList(debitList);
            salaryItem.setSendList(sendList);
            salaryItem.setDebit(debitList.size() + 1);
            salaryItem.setSend(sendList.size() + 1);
            salaryItemList.add(salaryItem);
            salaryInfo.setSalaryItem(salaryItemList);
            salaryInfoList.add(salaryInfo);
        }

        return salaryInfoList;
    }

    /**
     *
     * @param util
     * @param year
     * @param month
     * @param num
     * @param userId
     * @param schoolId
     */
    public void createSalaryTemplate(ExportUtil util, int year, int month, int num, String userId, String schoolId) {
        List<SalaryDto> salaryDtos = this.findSalaryInfo(schoolId,userId, year, month, num);
        if (salaryDtos.size() > 0) {
            SalaryDto dto = salaryDtos.get(0);
            String fileName = dto.getTimesName();
            List<SalaryItemDto> salaryItemDtos = dto.getMoney();
            SalaryItem salaryItem =  dto.getSalaryItem().get(0);
            List<String> salarys = new ArrayList<String>(salaryItemDtos.size()+Constant.THREE);
            List<String> datas = new ArrayList<String>(salaryItemDtos.size() + Constant.FIVE);
            datas.add("序号");
            datas.add("姓名");
            List<SalaryItemDto> salaryItemDtos1 = salaryItem.getSendList();
            for (SalaryItemDto send : salaryItemDtos1) {
                datas.add(send.getItemName());
            }
            datas.add("应发小计");
            List<SalaryItemDto> salaryItemDtos2 = salaryItem.getDebitList();
            for (SalaryItemDto debit : salaryItemDtos2) {
                datas.add(debit.getItemName());
            }
            datas.add("扣款小计");
            datas.add("实发工资");
            datas.add("备注");
            util.addTitle(datas.toArray());
            //填充模板的数据区域
            datas.clear();
            int i=0;
            for (SalaryDto salaryDto : salaryDtos) {
                datas.add(String.valueOf(i + 1));
                datas.add(salaryDto.getUserName());
                SalaryItem salaryItemList = salaryDto.getSalaryItem().get(0);
                List<SalaryItemDto> salaryItemDtos3 = salaryItemList.getSendList();
                for (SalaryItemDto send : salaryItemDtos3) {
                    datas.add(String.valueOf(send.getM()));
                }
                datas.add(salaryDto.getSsStr());
                List<SalaryItemDto> salaryItemDtos4 = salaryItemList.getDebitList();
                for (SalaryItemDto debit : salaryItemDtos4) {
                    datas.add(String.valueOf(debit.getM()));
                }
                datas.add(salaryDto.getMsStr());
                datas.add(salaryDto.getAsStr());
                datas.add(salaryDto.getRemark());
//                datas.addAll(salarys);
                util.appendRow(datas.toArray());
                datas.clear();
                i++;
            }
            util.setFileName(String.format("%s_%s", fileName, "统计.xlsx"));
        } else {
            List<String> datas = new ArrayList<String>(Constant.ONE);
            datas.add("未找到本次工资发放的人员信息");
            util.addTitle(datas.toArray());
            util.setFileName("工资统计.xlsx");
        }
    }

    /**
     *
     * @param util
     * @param year
     * @param month
     * @param num
     * @param userId
     * @param schoolId
     */
    public void exprotSalaryDetail(ExportUtil util, int year, int month, int num, String userId, String schoolId) {
        List<SalaryDto> salaryDtos = this.findSalaryInfo(schoolId,userId, year, month, num);
        if (salaryDtos.size() > 0) {
            SalaryDto dto = salaryDtos.get(0);
            String fileName = dto.getTimesName();
            List<String> datas = new ArrayList<String>(Constant.SIX);
            //填充模板的数据区域
            datas.clear();
            for (SalaryDto salaryDto : salaryDtos) {
                datas.add("工资条");
                datas.add(year+"-"+month);
                datas.add("部门");
                datas.add("");
                datas.add("姓名");
                datas.add(salaryDto.getUserName());
                util.appendRow(datas.toArray());
                datas.clear();
                SalaryItem salaryItemList = salaryDto.getSalaryItem().get(0);
                List<SalaryItemDto> send = salaryItemList.getSendList();
                if (send.size()!=0) {
                    int a = send.size()/3;
                    int b = send.size()%3;
                    for (int i=0;i<a;i++) {
//                            sendList.add(send.subList(i*3,i*3+3));
                        datas.add(send.get(i*3).getItemName());
                        datas.add(String.valueOf(send.get(i*3).getM()));
                        datas.add(send.get(i*3+1).getItemName());
                        datas.add(String.valueOf(send.get(i*3+1).getM()));
                        datas.add(send.get(i*3+2).getItemName());
                        datas.add(String.valueOf(send.get(i*3+2).getM()));
                        util.appendRow(datas.toArray());
                        datas.clear();
                    }
                    if (b!=0) {
                        for (int j=0;j<3;j++) {
                            if (j+1<=b) {
                                datas.add(send.get(a*3+j).getItemName());
                                datas.add(String.valueOf(send.get(a*3+j).getM()));
                            } else {
                                datas.add("");
                                datas.add("");
                            }
                        }
                        util.appendRow(datas.toArray());
                        datas.clear();
                    }
                }
                datas.add("");
                datas.add("");
                datas.add("");
                datas.add("");
                datas.add("应发小计");
                datas.add(salaryDto.getSsStr());
                util.appendRow(datas.toArray());
                datas.clear();
                List<SalaryItemDto> debit = salaryItemList.getDebitList();
                if (debit.size()!=0) {
                    int a = debit.size()/3;
                    int b = debit.size()%3;
                    for (int i=0;i<a;i++) {
                        datas.add(debit.get(i*3).getItemName());
                        datas.add(String.valueOf(debit.get(i*3).getM()));
                        datas.add(debit.get(i*3+1).getItemName());
                        datas.add(String.valueOf(debit.get(i*3+1).getM()));
                        datas.add(debit.get(i*3+2).getItemName());
                        datas.add(String.valueOf(debit.get(i*3+2).getM()));
                        util.appendRow(datas.toArray());
                        datas.clear();
                    }
                    if (b!=0) {
                        for (int j=0;j<3;j++) {
                            if (j+1<=b) {
                                datas.add(debit.get(a*3+j).getItemName());
                                datas.add(String.valueOf(debit.get(a*3+j).getM()));
                            } else {
                                datas.add("");
                                datas.add("");
                            }
                            util.appendRow(datas.toArray());
                            datas.clear();
                        }
                    }
                }
                datas.add("");
                datas.add("");
                datas.add("");
                datas.add("");
                datas.add("扣款小计");
                datas.add(salaryDto.getMsStr());
                util.appendRow(datas.toArray());
                datas.clear();
                datas.add("");
                datas.add("");
                datas.add("");
                datas.add("");
                datas.add("实发工资");
                datas.add(salaryDto.getAsStr());
                util.appendRow(datas.toArray());
                datas.clear();
                datas.add("");
                util.appendRow(datas.toArray());
                datas.clear();
            }
            util.setFileName(String.format("%s_%s", fileName, ".xlsx"));
        } else {
            List<String> datas = new ArrayList<String>(Constant.ONE);
            datas.add("未找到本次工资发放的人员信息");
            util.addTitle(datas.toArray());
            util.setFileName("工资条.xlsx");
        }
    }
}
