package com.fulaan.teachermanage.dto;

import com.pojo.teachermanage.ResultEntry;

/**
 * Created by wang_xinxin on 2016/3/8.
 */
public class ResultDTO {
    private String introduce;
    private String level;
    private String time;
    private String open;

    public ResultDTO() {

    }
    public ResultDTO(ResultEntry entry) {
        this.introduce = entry.getIntroduce();
        this.level = entry.getLevel();
        this.time = entry.getTime();
        this.open = String.valueOf(entry.getOpen());
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }
}

