package com.fulaan.reportCard.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by scott on 2017/10/18.
 */
public class GroupExamScoreDTO {

    private List<GroupExamUserRecordStrDTO> examGroupUserScoreDTOs=new ArrayList<GroupExamUserRecordStrDTO>();
    //新成绩单传这个
    private List<GroupExamUserRecordStrListDTO> examGroupUserScoreListDTOs=new ArrayList<GroupExamUserRecordStrListDTO>();
    //保存或发送 0保存 2发送
    private int status;

    private String groupExamDetailId;

    private long version;
    //谁的成绩  学生各自成绩：0   全班成绩：1
    private int showType;
    
    //是否点发送按钮 1：实时保存（实时编辑分数） 0：发送(点发送按钮发送通知)
    private int isSend;
    //成绩类型     //分数展示类型    0.分数和等第1.分数和排名2.仅分数3.仅等第
    private int fsShowType;

    public GroupExamScoreDTO(){

    }

    public String getGroupExamDetailId() {
        return groupExamDetailId;
    }

    public void setGroupExamDetailId(String groupExamDetailId) {
        this.groupExamDetailId = groupExamDetailId;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    

    public List<GroupExamUserRecordStrDTO> getExamGroupUserScoreDTOs() {
        return examGroupUserScoreDTOs;
    }

    public void setExamGroupUserScoreDTOs(List<GroupExamUserRecordStrDTO> examGroupUserScoreDTOs) {
        this.examGroupUserScoreDTOs = examGroupUserScoreDTOs;
    }
    
    

    public List<GroupExamUserRecordStrListDTO> getExamGroupUserScoreListDTOs() {
        return examGroupUserScoreListDTOs;
    }

    public void setExamGroupUserScoreListDTOs(List<GroupExamUserRecordStrListDTO> examGroupUserScoreListDTOs) {
        this.examGroupUserScoreListDTOs = examGroupUserScoreListDTOs;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getShowType() {
        return showType;
    }

    public void setShowType(int showType) {
        this.showType = showType;
    }

    public int getIsSend() {
        return isSend;
    }

    public void setIsSend(int isSend) {
        this.isSend = isSend;
    }

    public int getFsShowType() {
        return fsShowType;
    }

    public void setFsShowType(int fsShowType) {
        this.fsShowType = fsShowType;
    }
    
    
}
