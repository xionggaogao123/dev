package com.pojo.forum;

import org.bson.types.ObjectId;

/**
 * Created by admin on 2016/7/6.
 */
public class FInformationDTO {

    private String id;
    private String userId;
    private String personId;
    private String content;
    private long time;
    private String timeText;
    private int type;
    private String imageSrc;
    private int count;
    private int scan;
    private String nickName;
    private String acceptName;
    private int acceptType;

    public FInformationDTO(){}


    public FInformationDTO(FInformationEntry fInformationEntry){
        this.id=fInformationEntry.getID().toString();
        this.userId=fInformationEntry.getUserId().toString();
        this.type=fInformationEntry.getType();
        this.personId=fInformationEntry.getPersonId().toString();
        this.content=fInformationEntry.getContent();
        this.scan=fInformationEntry.getScan();
    }

    public static FInformationDTO systemMessage(FInformationEntry fInformationEntry){
        FInformationDTO dto = new FInformationDTO();
        dto.setId(fInformationEntry.getID().toString());
        dto.setPersonId(fInformationEntry.getPersonId().toString());
        dto.setContent(fInformationEntry.getContent());
        dto.setType(fInformationEntry.getType());
        return dto;
    }



    public FInformationEntry exportEntry(){
        FInformationEntry fInformationEntry=new FInformationEntry();
        if(id != null && !id.equals("")){
            fInformationEntry.setID(new ObjectId(id));
        }
        if(!userId.equals("")){
            fInformationEntry.setUserId(new ObjectId(userId));
        }
        if(!personId.equals("")){
            fInformationEntry.setPersonId(new ObjectId(personId));
        }
        fInformationEntry.setType(type);
        fInformationEntry.setContent(content);
        fInformationEntry.setScan(scan);
        return fInformationEntry;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTimeText() {
        return timeText;
    }

    public void setTimeText(String timeText) {
        this.timeText = timeText;
    }

    public String getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getScan() {
        return scan;
    }

    public void setScan(int scan) {
        this.scan = scan;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAcceptName() {
        return acceptName;
    }

    public void setAcceptName(String acceptName) {
        this.acceptName = acceptName;
    }

    public int getAcceptType() {
        return acceptType;
    }

    public void setAcceptType(int acceptType) {
        this.acceptType = acceptType;
    }
}
