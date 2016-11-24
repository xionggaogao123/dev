package com.pojo.smartcard;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

/**
 * 一卡通中号信息
 * <pre>
 * collectionName:accountinfo
 * </pre>
 * <pre>
 {
 si:学校id
 nm:用户名称
 sx:性别
 cno:卡号
 nb:编号
 ac:帐号
 deg：身份
 dep:部门
 af:账户标志
 my:主钱包余额
 myt:小钱包余额
 cf:卡标志
 }
 * Created by guojing on 2016/5/31.
 */
public class AccountInfoEntry extends BaseDBObject {
    public AccountInfoEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }
    public AccountInfoEntry(
            ObjectId schoolId,
            String name,
            String sex,
            Long cardNo,
            String number,
            Integer accounts,
            String physicalNo,
            String degree,
            String department,
            Double money,
            Double money2,
            Integer accountFlag,
            Integer cardFlag
    ) {
        super();
        BasicDBObject baseEntry =new BasicDBObject()
                .append("si", schoolId)
                .append("nm", name)
                .append("sx", sex)
                .append("cno", cardNo)
                .append("nb", number)
                .append("ac", accounts)
                .append("pno", physicalNo)
                .append("deg", degree)
                .append("dep", department)
                .append("my", money)
                .append("myt", money2)
                .append("af",accountFlag)
                .append("cf", cardFlag);
        setBaseEntry(baseEntry);
    }

    public ObjectId getSchoolId() {
        return getSimpleObjecIDValue("si");
    }

    public void setSchoolId(ObjectId schoolId) {
        setSimpleValue("si", schoolId);
    }

    public ObjectId getUserId() {
        return getSimpleObjecIDValue("uid");
    }

    public void setUserId(ObjectId userId) {
        setSimpleValue("uid", userId);
    }

    public String getName() {
        return getSimpleStringValue("nm");
    }

    public void setName(String name) {
        setSimpleValue("nm", name);
    }

    public String getSex() {
        return getSimpleStringValue("sx");
    }

    public void setSex(String sex) {
        setSimpleValue("sx", sex);
    }

    public Long getCardNo() {
        return getSimpleLongValue("cno");
    }

    public void setCardNo(Long cardNo) {
        setSimpleValue("cno", cardNo);
    }

    public String getNumber() {
        return getSimpleStringValue("nb");
    }

    public void setNumber(String number) {
        setSimpleValue("nb", number);
    }

    public Integer getAccounts() {
        return getSimpleIntegerValue("ac");
    }

    public void setAccounts(Integer accounts) {
        setSimpleValue("ac", accounts);
    }

    public String getPhysicalNo() {
        return getSimpleStringValue("pno");
    }

    public void setPhysicalNo(String physicalNo) {
        setSimpleValue("pno", physicalNo);
    }

    public String getDegree() {
        return getSimpleStringValue("deg");
    }

    public void setDegree(String degree) {
        setSimpleValue("deg", degree);
    }

    public String getDepartment() {
        return getSimpleStringValue("dep");
    }

    public void setDepartment(String department) {
        setSimpleValue("dep", department);
    }

    public Double getMoney() {
        return getSimpleDoubleValue("my");
    }

    public void setMoney(Double money) {
        setSimpleValue("my", money);
    }

    public Double getMoneyTwo() {
        return getSimpleDoubleValue("myt");
    }

    public void setMoneyTwo(Double moneyTwo) {
        setSimpleValue("myt", moneyTwo);
    }

    public Integer getAccountFlag() {
        return getSimpleIntegerValue("af");
    }

    public void setAccountFlag(Integer accountFlag) {
        setSimpleValue("af", accountFlag);
    }

    public Integer getCardFlag() {
        return getSimpleIntegerValue("cf");
    }

    public void setCardFlag(Integer cardFlag) {
        setSimpleValue("cf", cardFlag);
    }
}
