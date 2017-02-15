package com.fulaan.community.dto;

import com.pojo.fcommunity.LatestGroupDynamicEntry;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by scott on 2017/2/15.
 */
public class LatestGroupDynamicDTO {
    private String id;
    private String userId;
    private String communityId;
    private List<String> readedList=new ArrayList<String>();
    private String communityDetailId;
    private int type;
    private String nickName;
    private String message;
    private int readed;

    public LatestGroupDynamicDTO(LatestGroupDynamicEntry entry){
        this.id=entry.getID().toString();
        this.userId=entry.getUserId().toString();
        this.communityId=entry.getCommunityId().toString();
        this.communityDetailId=entry.getCommunityDetailId().toString();
        List<ObjectId> reads=entry.getReadedList();
        for(ObjectId item:reads){
            readedList.add(item.toString());
        }
        this.type=entry.getType();
        this.message=entry.getMessage();
    }

    public int getReaded() {
        return readed;
    }

    public void setReaded(int readed) {
        this.readed = readed;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public List<String> getReadedList() {
        return readedList;
    }

    public void setReadedList(List<String> readedList) {
        this.readedList = readedList;
    }

    public String getCommunityDetailId() {
        return communityDetailId;
    }

    public void setCommunityDetailId(String communityDetailId) {
        this.communityDetailId = communityDetailId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
