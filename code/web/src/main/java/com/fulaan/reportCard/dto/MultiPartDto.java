package com.fulaan.reportCard.dto;

import java.util.List;

public class MultiPartDto {
    
    private String userId;

    private String userName;
    
    private List<MultiDto> multiDtoList;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<MultiDto> getMultiDtoList() {
        return multiDtoList;
    }

    public void setMultiDtoList(List<MultiDto> multiDtoList) {
        this.multiDtoList = multiDtoList;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    
    
    
}
