package com.fulaan.reportCard.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by scott on 2017/10/18.
 */
public class ExamGroupScoreDTO {

    private List<ExamGroupUserScoreDTO> examGroupUserScoreDTOs=new ArrayList<ExamGroupUserScoreDTO>();

    private int status;

    private String groupExamDetailId;

    private long version;
    
    private int showType;
    
    //是否点发送按钮 0：实时保存 1：发送
    private int isSend;

    public ExamGroupScoreDTO(){

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

    public List<ExamGroupUserScoreDTO> getExamGroupUserScoreDTOs() {
        return examGroupUserScoreDTOs;
    }

    public void setExamGroupUserScoreDTOs(List<ExamGroupUserScoreDTO> examGroupUserScoreDTOs) {
        this.examGroupUserScoreDTOs = examGroupUserScoreDTOs;
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
    
    
}
