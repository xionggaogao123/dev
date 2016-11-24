package com.fulaan.zouban.dto;

import org.bson.types.ObjectId;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by qiangm on 2015/11/9.
 */
public class ZoubanNoticeDTO implements Serializable {
    private String id;
    private String userName;
    private String typeStr;
    private String publishTime;
    private String description;
    private List<Integer> weekList;
    private String stateStr;
    private List<String> classList;
    private List<NoticeDetailDTO> noticeDetailDTOs;
    private int state;

    public ZoubanNoticeDTO() {
    }


    public ZoubanNoticeDTO(ObjectId id, String userName, int type, String description, int state,
                           List<Integer> weekList, List<String> classList, List<NoticeDetailDTO> noticeDetailDTOs) {
        this.id = id.toString();
        this.userName = userName;
        this.typeStr = convertType(type);
        this.publishTime = convertPublishTime(id.getTime());
        this.description = description;
        this.weekList = weekList;
        this.classList = classList;
        this.noticeDetailDTOs = noticeDetailDTOs;
        this.stateStr = convertState(state);
        this.state = state;
    }


    private String convertState(int state) {
        String stateStr = null;
        switch (state) {
            case 0:
                stateStr = "未发布";
                break;
            case 1:
                stateStr = "已发布";
                break;
            default:
                stateStr = "";
        }
        return stateStr;
    }

    private String convertType(int type) {
        String typeStr = null;
        switch (type) {
            case 1:
                typeStr = "临时调课";
                break;
            case 2:
                typeStr = "长期调课";
                break;
            default:
                typeStr = "";
        }
        return typeStr;
    }

    private String convertPublishTime(long publishTime) {
        Date date = new Date(publishTime);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        return format.format(date);
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTypeStr() {
        return typeStr;
    }

    public void setTypeStr(String typeStr) {
        this.typeStr = typeStr;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Integer> getWeekList() {
        return weekList;
    }

    public void setWeekList(List<Integer> weekList) {
        this.weekList = weekList;
    }

    public String getStateStr() {
        return stateStr;
    }

    public void setStateStr(String stateStr) {
        this.stateStr = stateStr;
    }

    public List<String> getClassList() {
        return classList;
    }

    public void setClassList(List<String> classList) {
        this.classList = classList;
    }

    public List<NoticeDetailDTO> getNoticeDetailDTOs() {
        return noticeDetailDTOs;
    }

    public void setNoticeDetailDTOs(List<NoticeDetailDTO> noticeDetailDTOs) {
        this.noticeDetailDTOs = noticeDetailDTOs;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
