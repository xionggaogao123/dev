package com.fulaan.meeting.dto;


import java.util.List;

/**
 * Created by wang_xinxin on 2016/8/15.
 */
public class VoteDTO {

    private String id;

    private String name;

    private List<ChooseDTO> chooseDTOList;

    private int count;

    private boolean flag;

    private int status;

    private String userName;

    private String time;

    private String choose;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ChooseDTO> getChooseDTOList() {
        return chooseDTOList;
    }

    public void setChooseDTOList(List<ChooseDTO> chooseDTOList) {
        this.chooseDTOList = chooseDTOList;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getChoose() {
        return choose;
    }

    public void setChoose(String choose) {
        this.choose = choose;
    }
}
