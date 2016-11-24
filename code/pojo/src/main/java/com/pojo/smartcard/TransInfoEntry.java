package com.pojo.smartcard;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;

/**
 * 一卡通中号信息
 * <pre>
 * collectionName:accountinfo
 * </pre>
 * <pre>
 {
 nm:用户名称
 sx:性别
 cno:卡号
 nb:编号
 ac:帐号
 deg：身份
 dep:部门
 fl:账户标志
 my:主钱包余额
 myt:小钱包余额
 cf:卡标志
 }
 * Created by guojing on 2016/5/31.
 */
public class TransInfoEntry extends BaseDBObject {
    public TransInfoEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }
    public TransInfoEntry(
            String name,
            Long cardNo,
            String number,
            Integer accounts,
            String transType,
            Double transMoney,
            Long transDate,
            Long accountDay,
            Integer wId,
            Integer posId
    ) {
        super();
        BasicDBObject baseEntry =new BasicDBObject()
                .append("nm", name)
                .append("cno", cardNo)
                .append("nb", number)
                .append("ac", accounts)
                .append("tt", transType)
                .append("tm", transMoney)
                .append("td", transDate)
                .append("ad", accountDay)
                .append("wid",wId)
                .append("pid", posId);
        setBaseEntry(baseEntry);
    }

    public String getName() {
        return getSimpleStringValue("nm");
    }

    public void setName(String name) {
        setSimpleValue("nm", name);
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

    public String getTransType() {
        return getSimpleStringValue("tt");
    }

    public void setTransType(String transType) {
        setSimpleValue("tt", transType);
    }

    public Double getTransMoney() {
        return getSimpleDoubleValue("tm");
    }

    public void setTransMoney(Double transMoney) {
        setSimpleValue("tm", transMoney);
    }

    public Long getTransDate() {
        return getSimpleLongValue("td");
    }

    public void setTransDate(Long transDate) {
        setSimpleValue("td", transDate);
    }

    public Long getAccountDay() {
        return getSimpleLongValue("ad");
    }

    public void setAccountDay(Long accountDay) {
        setSimpleValue("ad", accountDay);
    }

    public Integer getWId() {
        return getSimpleIntegerValue("wid");
    }

    public void setWId(Integer wId) {
        setSimpleValue("wid", wId);
    }

    public Integer getPosId() {
        return getSimpleIntegerValue("pid");
    }

    public void setPosId(Integer posId) {
        setSimpleValue("pid", posId);
    }

}
