package com.fulaan.backstage.dto;

import com.pojo.backstage.JxmAppVersionEntry;
import org.bson.types.ObjectId;

/**
 * Created by James on 2017/11/27.
 */
public class JxmAppVersionDTO {

    private String id;
    private String name;
    private String fileUrl;
    private String version;
    private int versionCode;
    private int type;
    public JxmAppVersionDTO(){

    }
    public JxmAppVersionDTO(JxmAppVersionEntry e){
        if(e!=null){
            this.id = e.getID()==null?"":e.getID().toString();
            this.name = e.getName();
            this.fileUrl= e.getFileUrl();
            this.version = e.getVersion();
            this.versionCode =e.getVersionCode();
            this.type = e.getType();
        }else{
            new JxmAppVersionDTO();
        }
    }

    public JxmAppVersionEntry buildAddEntry(){
        JxmAppVersionEntry openEntry =
                new JxmAppVersionEntry(
                        this.name,
                        this.fileUrl,
                        this.version,
                        this.versionCode,
                        this.type
                );
        return openEntry;

    }
    public JxmAppVersionEntry updateEntry(){
        ObjectId Id=null;
        if(this.getId()!=null&&!"".equals(this.getId())){
            Id=new ObjectId(this.getId());
        }
        JxmAppVersionEntry openEntry =
                new JxmAppVersionEntry(
                        Id,
                        this.name,
                        this.fileUrl,
                        this.version,
                        this.versionCode,
                        this.type
                );
        return openEntry;

    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
