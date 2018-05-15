package com.fulaan.indexpage.dto;

import com.pojo.backstage.SystemMessageEntry;
import org.bson.types.ObjectId;

/**
 * Created by James on 2018-05-14.
 */
public class SystemMessageDTO {
    private String id;
    private String avatar;
    private String name;
    private String title;
    private String content;
    private String fileUrl;
    private String sourceName;
    private int fileType;
    private int type;
    private String sourceId;
    private int sourceType;

    public SystemMessageDTO(){

    }

    public SystemMessageEntry buildAddEntry(){
        ObjectId sId=null;
        if(this.getSourceId()!=null&&!"".equals(this.getSourceId())){
            sId=new ObjectId(this.getSourceId());
        }
        SystemMessageEntry openEntry =
                new SystemMessageEntry(
                        this.avatar,
                        this.name,
                        this.title,
                        this.content,
                        this.fileUrl,
                        this.sourceName,
                        this.fileType,
                        this.type,
                        sId,
                        this.sourceType
                );
        return openEntry;

    }
    public SystemMessageEntry updateEntry(){
        ObjectId Id=null;
        if(this.getId()!=null&&!"".equals(this.getId())){
            Id=new ObjectId(this.getId());
        }
        ObjectId sId=null;
        if(this.getSourceId()!=null&&!"".equals(this.getSourceId())){
            sId=new ObjectId(this.getSourceId());
        }
        SystemMessageEntry openEntry =
                new SystemMessageEntry(
                        Id,
                        this.avatar,
                        this.name,
                        this.title,
                        this.content,
                        this.fileUrl,
                        this.sourceName,
                        this.fileType,
                        this.type,
                        sId,
                        this.sourceType
                );
        return openEntry;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public int getFileType() {
        return fileType;
    }

    public void setFileType(int fileType) {
        this.fileType = fileType;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public int getSourceType() {
        return sourceType;
    }

    public void setSourceType(int sourceType) {
        this.sourceType = sourceType;
    }
}
