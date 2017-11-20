package com.fulaan.backstage.dto;

/**
 * Created by James on 2017/11/18.
 */
public class UnlawfulPictureTextDTO {
    /* createTime          创建时间             ctm
 userId               用户id              uid
 function              功能               fun
 type                1 图片 2 文字       typ
 isCheck               是否通过           isc
 isRemove              是否删除           isr
 content               内容               con*/
    private String id;
    private String userId;
    private String createTime;
    private String function;
    private int type;
    private int isCheck;
    private int isRemove;
    private String content;

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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getIsCheck() {
        return isCheck;
    }

    public void setIsCheck(int isCheck) {
        this.isCheck = isCheck;
    }

    public int getIsRemove() {
        return isRemove;
    }

    public void setIsRemove(int isRemove) {
        this.isRemove = isRemove;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
