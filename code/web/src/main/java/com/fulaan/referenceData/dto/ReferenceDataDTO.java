package com.fulaan.referenceData.dto;

import com.fulaan.pojo.Attachement;
import com.fulaan.util.NewStringUtil;
import com.pojo.fcommunity.AttachmentEntry;
import com.pojo.referenceData.ReferenceDataEntry;
import com.sys.utils.DateTimeUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2018-04-23.
 */
public class ReferenceDataDTO {

    private String id;
    private String userId;
    private String userName;
    private String communityId;
    private String communityName;
    private String subjectId;
    private String subjectName;
    private String createTime;
    private long longTime;
    private String size;
    private String title;
    private String content;
    private int type;//0 全部   1 文档  2 视频  3 音频  4 图片   5 其他
    private String suffix;
    private List<Attachement> attachements = new ArrayList<Attachement>();
    private List<String> communityIds = new ArrayList<String>();

    private int operation;  //0 不可删除   1 可删除

    public ReferenceDataDTO(){

    }
    public ReferenceDataDTO(ReferenceDataEntry e){
        if(e!=null){
            this.id = e.getID()==null?"":e.getID().toString();
            this.userId = e.getUserId()==null ? "":e.getUserId().toString();
            this.communityId = e.getCommunityId()==null ? "":e.getCommunityId().toString();
            this.subjectId = e.getSubjectId()==null ? "":e.getSubjectId().toString();
            this.size = e.getSize();
            this.content = e.getContent();
            this.title = NewStringUtil.toGoodJsonStr(e.getTitle());
            if(e.getCreateTime()!=0l){
                this.createTime = DateTimeUtils.getLongToStrTimeTwo(e.getCreateTime());
            }else{
                this.createTime = "";
            }
            this.type = e.getType();
            this.suffix = e.getSuffix();


            List<AttachmentEntry> attachmentEntries2 = e.getAttachmentList();
            if(attachmentEntries2 != null && attachmentEntries2.size()>0){
                for(AttachmentEntry entry2 : attachmentEntries2){
                    this.attachements.add(new Attachement(entry2));
                }
            }

        }else{
            new ReferenceDataDTO();
        }
    }

    public ReferenceDataEntry buildAddEntry(){
        ObjectId uId=null;
        if(this.getUserId()!=null&&!"".equals(this.getUserId())){
            uId=new ObjectId(this.getUserId());
        }
        ObjectId cId=null;
        if(this.getCommunityId()!=null&&!"".equals(this.getCommunityId())){
            cId=new ObjectId(this.getCommunityId());
        }
        ObjectId sId=null;
        if(this.getSubjectId()!=null&&!"".equals(this.getSubjectId())){
            sId=new ObjectId(this.getSubjectId());
        }
        List<AttachmentEntry> attachmentEntries=new ArrayList<AttachmentEntry>();
        if(attachements.size()>0){
            for(Attachement attachement:attachements){
                attachmentEntries.add(new AttachmentEntry(attachement.getUrl(), attachement.getFlnm(),
                        System.currentTimeMillis(),
                        uId));
            }
        }
        ReferenceDataEntry openEntry =
                new ReferenceDataEntry(
                        uId,
                        sId,
                        cId,
                        this.size,
                        this.title,
                        this.content,
                        this.type,
                        this.suffix,
                        attachmentEntries
                        );
        return openEntry;

    }

    public long getLongTime() {
        return longTime;
    }

    public void setLongTime(long longTime) {
        this.longTime = longTime;
    }

    public int getOperation() {
        return operation;
    }

    public void setOperation(int operation) {
        this.operation = operation;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public List<String> getCommunityIds() {
        return communityIds;
    }

    public void setCommunityIds(List<String> communityIds) {
        this.communityIds = communityIds;
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

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public List<Attachement> getAttachements() {
        return attachements;
    }

    public void setAttachements(List<Attachement> attachements) {
        this.attachements = attachements;
    }
}
