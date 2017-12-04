package com.fulaan.backstage.dto;

import com.pojo.backstage.SystemMessageEntry;
import com.sys.utils.DateTimeUtils;
import org.bson.types.ObjectId;

/**
 * Created by James on 2017/12/4.
 */
public class SystemMessageDTO {
    private String id;
    private String avatar;
    private String name;
    private String content;
    private String title;
    private String fileUrl;
    private String sourceName;
    private int fileType;
    private int type;
    private String sourceId;
    private int sourceType;
    private String createTime;
    public SystemMessageDTO(){

    }
    public SystemMessageDTO(SystemMessageEntry e){
        if(e!=null){
            this.id = e.getID()==null?"":e.getID().toString();
            this.sourceId = e.getSourceId()==null?"":e.getSourceId().toString();
            if(e.getCreateTime()!=0l){
                this.createTime = DateTimeUtils.getLongToStrTimeTwo(e.getCreateTime());
            }else{
                this.createTime = "";
            }
            this.avatar = e.getAvatar();
            this.type = e.getType();
            this.content = e.getContent();
            this.name = e.getName();
            this.title = e.getTitle();
            this.fileType = e.getFileType();
            this.fileUrl = e.getFileUrl();
            this.sourceType = e.getSourceType();
            this.sourceName = e.getSourceName();

        }else{
            new SystemMessageDTO();
        }
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
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

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
