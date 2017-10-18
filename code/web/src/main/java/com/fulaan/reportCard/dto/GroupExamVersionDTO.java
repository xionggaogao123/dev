package com.fulaan.reportCard.dto;

import com.pojo.reportCard.GroupExamVersionEntry;

/**
 * Created by scott on 2017/10/18.
 */
public class GroupExamVersionDTO {

    private String id;
    private String groupExamDetailId;
    private long version;

    public GroupExamVersionDTO(){

    }

    public GroupExamVersionDTO(GroupExamVersionEntry examVersionEntry){
        this.id=examVersionEntry.getID().toString();
        this.groupExamDetailId=examVersionEntry.getGroupExamDetailId().toString();
        this.version=examVersionEntry.getVersion();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroupExamDetailId() {
        return groupExamDetailId;
    }

    public void setGroupExamDetailId(String groupExamDetailId) {
        this.groupExamDetailId = groupExamDetailId;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }
}
