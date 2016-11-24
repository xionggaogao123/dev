package com.fulaan.smartcard.dto;

import com.pojo.smartcard.KaoQinInfoEntry;
import com.sys.utils.DateTimeUtils;

/**
 * Created by guojing on 2016/6/15.
 */
public class KaoQinDTO {

    private String id;
    private String userId;
    private String name;
    private Long cardNo;
    private String number;
    private Integer accounts;
    private String dateTime;
    private String cardDate;
    private Integer wId;
    private Integer posId;
    private String inOutFlag;
    private String shangXueDate;
    private String fangXueDate;
    private String state;

    public KaoQinDTO(){

    }

    public KaoQinDTO(KaoQinInfoEntry e){
        this.id=e.getID().toString();
        this.name=e.getName();
        this.cardNo=e.getCardNo();
        this.number=e.getNumber();
        this.accounts=e.getAccounts();
        this.dateTime=DateTimeUtils.getLongToStrTime(e.getCardDate());
        this.cardDate= DateTimeUtils.getLongToStrTimeThree(e.getCardDate());
        this.wId=e.getWId();
        this.posId=e.getPosId();
        this.inOutFlag=e.getInOutFlag();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCardNo() {
        return cardNo;
    }

    public void setCardNo(Long cardNo) {
        this.cardNo = cardNo;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Integer getAccounts() {
        return accounts;
    }

    public void setAccounts(Integer accounts) {
        this.accounts = accounts;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getCardDate() {
        return cardDate;
    }

    public void setCardDate(String cardDate) {
        this.cardDate = cardDate;
    }

    public Integer getwId() {
        return wId;
    }

    public void setwId(Integer wId) {
        this.wId = wId;
    }

    public Integer getPosId() {
        return posId;
    }

    public void setPosId(Integer posId) {
        this.posId = posId;
    }

    public String getInOutFlag() {
        return inOutFlag;
    }

    public void setInOutFlag(String inOutFlag) {
        this.inOutFlag = inOutFlag;
    }

    public String getShangXueDate() {
        return shangXueDate;
    }

    public void setShangXueDate(String shangXueDate) {
        this.shangXueDate = shangXueDate;
    }

    public String getFangXueDate() {
        return fangXueDate;
    }

    public void setFangXueDate(String fangXueDate) {
        this.fangXueDate = fangXueDate;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
