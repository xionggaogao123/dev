package com.pojo.ebusiness;

import com.sys.utils.DateTimeUtils;
import org.bson.types.ObjectId;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by fl on 2016/3/7.
 */
public class EVoucherDTO {
    private String id;
    private String number;
    private String userId;
    private int denomination;
    private long expTime;
    private int state;


    private String stateInfo;
    private String expTimeInfo;
    private String userName;
    private String rechargeTimeInfo;

    public EVoucherDTO(){}

    public EVoucherDTO(String id, String number, String userId, int denomination, long expTime, int state) {
        this.id = id;
        this.number = number;
        this.userId = userId;
        this.denomination = denomination;
        this.expTime = expTime;
        this.state = state;
    }

    public EVoucherDTO(EVoucherEntry entry){
        this.id = entry.getID().toString();
        this.number = entry.getNumber();
        this.userId = entry.getUserId()==null ? "" : entry.getUserId().toString();
        this.denomination = entry.getDenomination();
        this.expTime = entry.getExpTime();
        this.state = entry.getState();
        this.stateInfo = convertStateToStateInfo();
        this.expTimeInfo = convertExpTimeToExpTimeInfo();
        this.rechargeTimeInfo = convertExpTimeToRechargeTimeInfo(entry.getID(),entry.getActivity());
    }

    private String convertStateToStateInfo(){
        switch (state){
            case 0 : return "未使用";
            case 1 : return "已使用";
            case 2 : return "已过期";
            case 3 : return "未发放";
            case 4 : return "已发放";
            case 5 : return "已删除";
            default: return "未发放";
        }

    }

    private String convertExpTimeToRechargeTimeInfo(ObjectId id,int activity){
        if(activity==0){
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(expTime);
            calendar.add(Calendar.DATE, -30);
            long rechargeTime = calendar.getTimeInMillis();
            return DateTimeUtils.convert(rechargeTime, "yyyy/MM/dd");
        }else{
            return DateTimeUtils.convert(id.getTime(),DateTimeUtils.DATE_YYYY_MM_DD_);
        }

    }

    private String convertExpTimeToExpTimeInfo(){
        return DateTimeUtils.convert(expTime, DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_SS);
    }

    public EVoucherEntry exportEntry(){
        EVoucherEntry eVoucherEntry = new EVoucherEntry();
        if(!id.equals("")){
            eVoucherEntry.setID(new ObjectId(id));
        }
        eVoucherEntry.setNumber(number);
        eVoucherEntry.setUserId(new ObjectId(userId));
        eVoucherEntry.setDenomination(denomination);
        eVoucherEntry.setExpTime(expTime);
        eVoucherEntry.setState(state);
        return eVoucherEntry;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getDenomination() {
        return denomination;
    }

    public void setDenomination(int denomination) {
        this.denomination = denomination;
    }

    public long getExpTime() {
        return expTime;
    }

    public void setExpTime(long expTime) {
        this.expTime = expTime;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    public String getExpTimeInfo() {
        return expTimeInfo;
    }

    public void setExpTimeInfo(String expTimeInfo) {
        this.expTimeInfo = expTimeInfo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRechargeTimeInfo() {
        return rechargeTimeInfo;
    }

    public void setRechargeTimeInfo(String rechargeTimeInfo) {
        this.rechargeTimeInfo = rechargeTimeInfo;
    }
}
