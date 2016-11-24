/**
 * @Title: SalaryDto.java
 * @Package com.pojo.salary
 * @Description: TODO(用一句话描述该文件是做什么的)
 * @author yang.ling
 * @date 2015年7月19日 下午10:00:07
 * @version V1.0
 * @Copyright ycode Co.,Ltd.
 */
package com.pojo.salary;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.sys.utils.DateTimeUtils;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.List;

/**
 * @author yang.ling
 * @ClassName: SalaryDto
 * @date 2015年7月19日 下午10:00:07
 */
public class SalaryDto implements Serializable {
    private final DecimalFormat decimalFormat = new DecimalFormat("0.##");

    private static final long serialVersionUID = -1688401953152536851L;

    private String id;
    private String schoolId;
    private int index;
    private String timesName;
    private String uid;
    private String userName;
    private int year;
    private int month;
    private int number;
    private String salaryDate;
    private List<SalaryItemDto> money;
    private double ss;
    private double ms;
    private double as;

    private String ssStr;
    private String msStr;
    private String asStr;

    private List<SalaryItem> salaryItem;
    private List<List<SalaryItemDto>> debitList;
    private List<List<SalaryItemDto>> sendList;
    private int debitCount;

    private String createTime;

    private String remark;

    public SalaryDto() {
    }

    public SalaryDto(SalaryEntry salaryEntry) {
        this.id = salaryEntry.getId();
        this.uid = salaryEntry.getUserId();
        this.userName = salaryEntry.getUserName();
        this.year = salaryEntry.getYear();
        this.month = salaryEntry.getMonth();
        this.number = salaryEntry.getNumber();
        this.schoolId = salaryEntry.getSchoolId().toString();
        this.index = salaryEntry.getNumber();
        this.timesName = salaryEntry.getTimesName();
        this.money = salaryEntry.getMoney();
        this.salaryDate = salaryEntry.getSalaryDate();
        this.ss = salaryEntry.getS();
        this.ms = salaryEntry.getM();
        this.as = salaryEntry.getA();
        this.ssStr = decimalFormat.format(ss);
        this.msStr = decimalFormat.format(ms);
        this.asStr = decimalFormat.format(as);
        this.createTime = DateTimeUtils.getLongToStrTime(salaryEntry.getID().getTime());
        this.remark = salaryEntry.getRemark()==null?"":salaryEntry.getRemark();
    }

    public void calculateRealSalary() {
        double ss = 0;
        double ms = 0;
        double as = 0;
        BasicDBList list = new BasicDBList();
        for (int i = 0; i < this.money.size(); i++) {
            BasicDBObject e = new BasicDBObject();
            e.append("in", money.get(i).getItemName());
            e.append("it", money.get(i).getType());
            e.append("m", money.get(i).getM());
            if (SalaryItemDto.ADD.equals(money.get(i).getType())) {
                ss += money.get(i).getM();
            } else if (SalaryItemDto.SUB.equals(money.get(i).getType())) {
                ms += money.get(i).getM();
            }
            list.add(e);
        }
        this.ss = ss;
        this.ms = ms;
        this.as = ss - ms;
        this.ssStr = decimalFormat.format(this.ss);
        this.msStr = decimalFormat.format(this.ms);
        this.asStr = decimalFormat.format(this.as);
    }

    public String getSalaryDate() {
        return salaryDate;
    }

    public void setSalaryDate(String salaryDate) {
        this.salaryDate = salaryDate;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public String getSsStr() {
        return ssStr;
    }

    public void setSsStr(String ssStr) {
        this.ssStr = ssStr;
    }

    public String getMsStr() {
        return msStr;
    }

    public void setMsStr(String msStr) {
        this.msStr = msStr;
    }

    public String getAsStr() {
        return asStr;
    }

    public void setAsStr(String asStr) {
        this.asStr = asStr;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getTimesName() {
        return timesName;
    }

    public void setTimesName(String timesName) {
        this.timesName = timesName;
    }

    public SalaryEntry exportEntry(
            String uid, String unm,
            int y, int m, int n,
            List<SalaryItemDto> items) {

        return new SalaryEntry(
                uid, unm,
                y, m, n, schoolId, timesName,
                items,remark);
    }


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public List<SalaryItemDto> getMoney() {
        return money;
    }

    public void setMoney(List<SalaryItemDto> money) {
        this.money = money;
    }

    public double getSs() {
        return ss;
    }

    public void setSs(double ss) {
        this.ss = ss;
    }

    public double getMs() {
        return ms;
    }

    public void setMs(double ms) {
        this.ms = ms;
    }

    public double getAs() {
        return as;
    }

    public void setAs(long as) {
        this.as = as;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<SalaryItem> getSalaryItem() {
        return salaryItem;
    }

    public void setSalaryItem(List<SalaryItem> salaryItem) {
        this.salaryItem = salaryItem;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public List<List<SalaryItemDto>> getDebitList() {
        return debitList;
    }

    public void setDebitList(List<List<SalaryItemDto>> debitList) {
        this.debitList = debitList;
    }

    public List<List<SalaryItemDto>> getSendList() {
        return sendList;
    }

    public void setSendList(List<List<SalaryItemDto>> sendList) {
        this.sendList = sendList;
    }

    public int getDebitCount() {
        return debitCount;
    }

    public void setDebitCount(int debitCount) {
        this.debitCount = debitCount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
