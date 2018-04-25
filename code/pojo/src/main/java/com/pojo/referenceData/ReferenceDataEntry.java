package com.pojo.referenceData;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.fcommunity.AttachmentEntry;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by James on 2018-04-23.
 * 新参考资料
 * id                id                  id
 * userId            发布人              uid
 * communityId       社群                cid
 * subjectId         学科id              sid
 * createTime        创建时间            ctm
 * size              大小（kb）          siz
 * title             题目                tit
 * content           内容说明            con
 * type              文件类型            typ    //0 全部   1 文档  2 视频  3 音频  4 图片 5 其他
 * suffix            后缀                suf
 * attachements      附件                att
 *
 *
 */
public class ReferenceDataEntry extends BaseDBObject {

    public ReferenceDataEntry(){

    }

    public ReferenceDataEntry(BasicDBObject dbObject){
        super(dbObject);
    }


    public ReferenceDataEntry(
            ObjectId userId,
            ObjectId subjectId,
            ObjectId communityId,
            String size,
            String title,
            String content,
            int type,
            String suffix,
            List<AttachmentEntry> attachmentList
                            ){
        BasicDBList attachmentDbList = new BasicDBList();
        for(AttachmentEntry attachmentEntry:attachmentList){
            attachmentDbList.add(attachmentEntry.getBaseEntry());
        }
        BasicDBObject dbObject=new BasicDBObject()
                .append("uid", userId)
                .append("sid", subjectId)
                .append("cid", communityId)
                .append("ctm", new Date().getTime())
                .append("siz", size)
                .append("tit", title)
                .append("con", content)
                .append("typ",type)
                .append("suf",suffix)
                .append("atl", attachmentDbList)
                .append("isr", 0);
        setBaseEntry(dbObject);
    }

    public List<AttachmentEntry> getAttachmentList() {
        return getAttachments("atl");
    }

    private List<AttachmentEntry> getAttachments(String field){
        List<AttachmentEntry> imageList = new ArrayList<AttachmentEntry>();
        if (!getBaseEntry().containsField(field)) {
            return imageList;
        } else {
            BasicDBList list = (BasicDBList) getSimpleObjectValue(field);
            for(Object o :list){
                AttachmentEntry attachmentEntry = new AttachmentEntry((BasicDBObject)o);
                imageList.add(attachmentEntry);
            }
            return imageList;
        }
    }

    public void setAttachmentList(BasicDBList attachmentList){
        setSimpleValue("atl",attachmentList);
    }

    public ObjectId getUserId(){
        return getSimpleObjecIDValue("uid");
    }

    public void setUserId(ObjectId userId){
        setSimpleValue("uid",userId);
    }
    public ObjectId getCommunityId(){
        return getSimpleObjecIDValue("cid");
    }

    public void setCommunityId(ObjectId communityId){
        setSimpleValue("cid",communityId);
    }
    public ObjectId getSubjectId(){
        return getSimpleObjecIDValue("sid");
    }

    public void setSubjectId(ObjectId subjectId){
        setSimpleValue("sid",subjectId);
    }

    public long getCreateTime(){
        return getSimpleLongValue("ctm");
    }

    public void setCreateTime(long createTime){
        setSimpleValue("ctm",createTime);
    }

    public int getType(){
        return getSimpleIntegerValue("typ");
    }

    public void setType(int type){
        setSimpleValue("typ",type);
    }

    public String getSize(){
        return getSimpleStringValue("siz");
    }

    public void setSize(String size){
        setSimpleValue("siz",size);
    }

    public String getTitle(){
        return getSimpleStringValue("tit");
    }

    public void setTitle(String title){
        setSimpleValue("tit",title);
    }
    public String getContent(){
        return getSimpleStringValue("con");
    }

    public void setContent(String content){
        setSimpleValue("con",content);
    }
    public String getSuffix(){
        return getSimpleStringValue("suf");
    }

    public void setSuffix(String suffix){
        setSimpleValue("suf",suffix);
    }

    public int getIsRemove(){
        return getSimpleIntegerValue("isr");
    }

    public void setIsRemove(int isRemove){
        setSimpleValue("isr",isRemove);
    }
}
