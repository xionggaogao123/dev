package com.fulaan.leave.dto;

import com.pojo.leave.LeaveEntry;
import com.pojo.leave.ReplyEnum;
import org.bson.types.ObjectId;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by qiangm on 2016/3/1.
 */
public class LeaveDTO {
    private String id;
    private String userId;
    private String schoolId;
    private String userName;
    private String title;
    private String content;
    private long date1;
    private String dateFrom;
    private long date2;
    private String dateEnd;
    private int classCount;
    private long applyDate;
    private String applyDateStr;
    private int reply;
    private String replyMessage;
    private String replyPersonId;
    private String replyPersonName;
    private String term;


    public LeaveDTO() {
    }
    public LeaveDTO(LeaveEntry leaveEntry)
    {
        this.id=leaveEntry.getID().toString();
        this.userId=leaveEntry.getTeacherId().toString();
        this.schoolId=leaveEntry.getSchoolId().toString();
        this.title=leaveEntry.getTitle();
        this.content=leaveEntry.getContent();
        this.date1=leaveEntry.getDateFrom();
        this.dateFrom=convertLongToDateSimple(leaveEntry.getDateFrom());
        this.date2=leaveEntry.getDateEnd();
        this.dateEnd=convertLongToDateSimple(leaveEntry.getDateEnd());
        this.classCount=leaveEntry.getClassCount();
        this.applyDate=leaveEntry.getDate();
        this.applyDateStr=convertLongToDate(leaveEntry.getDate());
        this.reply=leaveEntry.getReply();
        this.replyMessage= ReplyEnum.getCheckState(leaveEntry.getReply());
        if(leaveEntry.getReplyPerson()!=null)
            this.replyPersonId=leaveEntry.getReplyPerson().toString();
        this.term=leaveEntry.getTerm();
    }
    public String convertLongToDate(long timeStart)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(timeStart);
        return  sdf.format(date);
    }
    public String convertLongToDateSimple(long timeStart)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        Date date = new Date(timeStart);
        return  sdf.format(date);
    }
    public LeaveEntry export()
    {
        LeaveEntry leaveEntry=new LeaveEntry();
        leaveEntry.setTeacherId(new ObjectId(this.userId));
        leaveEntry.setSchoolId(new ObjectId(this.schoolId));
        leaveEntry.setTitle(this.title);
        leaveEntry.setContent(this.content);
        leaveEntry.setDateFrom(this.date1);
        leaveEntry.setDateEnd(this.date2);
        leaveEntry.setClassCount(this.classCount);
        leaveEntry.setDate(this.applyDate);
        leaveEntry.setReply(this.reply);
        leaveEntry.setTerm(this.term);
        if(this.replyPersonId.equals(""))
        leaveEntry.setReplyPerson(new ObjectId(this.replyPersonId));
        return leaveEntry;
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

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getDate1() {
        return date1;
    }

    public void setDate1(long date1) {
        this.date1 = date1;
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    public long getDate2() {
        return date2;
    }

    public void setDate2(long date2) {
        this.date2 = date2;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }

    public int getClassCount() {
        return classCount;
    }

    public void setClassCount(int classCount) {
        this.classCount = classCount;
    }

    public long getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(long applyDate) {
        this.applyDate = applyDate;
    }

    public int getReply() {
        return reply;
    }

    public void setReply(int reply) {
        this.reply = reply;
    }

    public String getReplyMessage() {
        return replyMessage;
    }

    public void setReplyMessage(String replyMessage) {
        this.replyMessage = replyMessage;
    }

    public String getReplyPersonId() {
        return replyPersonId;
    }

    public void setReplyPersonId(String replyPersonId) {
        this.replyPersonId = replyPersonId;
    }

    public String getReplyPersonName() {
        return replyPersonName;
    }

    public void setReplyPersonName(String replyPersonName) {
        this.replyPersonName = replyPersonName;
    }

    public String getApplyDateStr() {
        return applyDateStr;
    }

    public void setApplyDateStr(String applyDateStr) {
        this.applyDateStr = applyDateStr;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }
}
