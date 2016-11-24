package com.pojo.letter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sys.utils.CustomDateSerializer;

/**
 * Created by yan on 2015/3/10.
 */
public class LetterDTO {



    private String letterId;
    private String senderId;
    private String letterchatid;
    private String senderchatid;
    private LetterType letterType;
    private LetterState letterState;
    private String content;
    private List<ReceiveInfo> receiveInfoList=new ArrayList<ReceiveInfo>();
    private Object extraData;

    private String senderName;
    private String recipientName;

    private String recipient;

    private String userImage;

    private int unread;//1未读

    private String userid;

    @JsonSerialize(using = CustomDateSerializer.class)
    private Date sendingTime;

    private String roleName;




    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }


    public Object getExtraData() {
        return extraData;
    }

    public void setExtraData(Object extraData) {
        this.extraData = extraData;
    }

    public String getLetterchatid() {
        return letterchatid;
    }

    public void setLetterchatid(String letterchatid) {
        this.letterchatid = letterchatid;
    }

    public String getSenderchatid() {
        return senderchatid;
    }

    public void setSenderchatid(String senderchatid) {
        this.senderchatid = senderchatid;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public LetterType getLetterType() {
        return letterType;
    }

    public void setLetterType(LetterType letterType) {
        this.letterType = letterType;
    }

    public LetterState getLetterState() {
        return letterState;
    }

    public void setLetterState(LetterState letterState) {
        this.letterState = letterState;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<ReceiveInfo> getReceiveInfoList() {
        return receiveInfoList;
    }

    public void setReceiveInfoList(List<ReceiveInfo> receiveInfoList) {
        this.receiveInfoList = receiveInfoList;
    }

    public String getLetterId() {
        return letterId;
    }

    public void setLetterId(String letterId) {
        this.letterId = letterId;
    }
    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public Date getSendingTime() {
        return sendingTime;
    }

    public void setSendingTime(Date sendingTime) {
        this.sendingTime = sendingTime;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public int getUnread() {
        return unread;
    }

    public void setUnread(int unread) {
        this.unread = unread;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
