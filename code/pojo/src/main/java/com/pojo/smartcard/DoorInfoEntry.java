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
 cno:卡号
 nb:编号
 ac:帐号
 cd:刷卡日期
 wid:工作站号
 pid:终端机号
 iof:进出标志
 drn:门名称
 }
 * Created by guojing on 2016/5/31.
 */
public class DoorInfoEntry extends BaseDBObject {
    public DoorInfoEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }
    public DoorInfoEntry(
            String name,
            Long cardNo,
            String number,
            Integer accounts,
            Long cardDate,
            Integer wId,
            Integer posId,
            String inOutFlag,
            String doorName
    ) {
        super();
        BasicDBObject baseEntry =new BasicDBObject()
                .append("nm", name)
                .append("cno", cardNo)
                .append("nb", number)
                .append("ac", accounts)
                .append("cd", cardDate)
                .append("wid",wId)
                .append("pid", posId)
                .append("iof", inOutFlag)
                .append("drn", doorName);
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

    public Long getCardDate() {
        return getSimpleLongValue("cd");
    }

    public void setCardDate(Long cardDate) {
        setSimpleValue("cd", cardDate);
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

    public String getInOutFlag() {
        return getSimpleStringValue("iof");
    }

    public void setInOutFlag(String inOutFlag) {
        setSimpleValue("iof", inOutFlag);
    }

    public String getDoorName() {
        return getSimpleStringValue("drn");
    }

    public void setDoorName(String doorName) {
        setSimpleValue("drn", doorName);
    }
}
