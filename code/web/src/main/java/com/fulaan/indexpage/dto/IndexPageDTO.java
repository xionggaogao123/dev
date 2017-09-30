package com.fulaan.indexpage.dto;

import com.pojo.indexPage.IndexPageEntry;
import org.bson.types.ObjectId;

/**
 * Created by James on 2017/9/29.
 */
public class IndexPageDTO {
    /* ObjectId id,
            int type,
            ObjectId communityId,
            ObjectId contactId*/
    private String id;
    private int type;// 1 作业    2 通知
    private String communityId;
    private String contactId;

    public IndexPageDTO(){

    }
    public IndexPageDTO(IndexPageEntry e){
        if(e!=null){
            this.id = e.getID()==null?"":e.getID().toString();
            this.type = e.getType();
            this.communityId = e.getCommunityId() == null ? "" : e.getCommunityId().toString();
            this.contactId = e.getContactId() == null ? "" : e.getContactId().toString();
        }else{
            new IndexPageDTO();
        }
    }

    public IndexPageEntry buildAddEntry(){
        ObjectId cId=null;
        if(this.getCommunityId()!=null&&!"".equals(this.getCommunityId())){
            cId=new ObjectId(this.getCommunityId());
        }
        ObjectId tId=null;
        if(this.getContactId()!=null&&!"".equals(this.getContactId())){
            tId=new ObjectId(this.getContactId());
        }
        IndexPageEntry openEntry =
                new IndexPageEntry(
                        this.type,
                        cId,
                        tId
                );
        return openEntry;

    }
    public IndexPageEntry updateEntry(){
        ObjectId Id=null;
        if(this.getId()!=null&&!"".equals(this.getId())){
            Id=new ObjectId(this.getId());
        }
        ObjectId cId=null;
        if(this.getCommunityId()!=null&&!"".equals(this.getCommunityId())){
            cId=new ObjectId(this.getCommunityId());
        }
        ObjectId tId=null;
        if(this.getContactId()!=null&&!"".equals(this.getContactId())){
            tId=new ObjectId(this.getContactId());
        }
        IndexPageEntry openEntry =
                new IndexPageEntry(
                        Id,
                        this.type,
                        cId,
                        tId
                );
        return openEntry;

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

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }
}
