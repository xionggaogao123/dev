package com.pojo.appvote;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.fcommunity.AttachmentEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by scott on 2017/11/6.
 * * vmc:voteMaxCount选项个数
 * vdt:voteDeadTime投票截止时间
 * vt:voteType投票类型
 */
public class AppVoteEntry extends BaseDBObject{


    public AppVoteEntry(DBObject dbObject){
        setBaseEntry((BasicDBObject)dbObject);
    }

    public AppVoteEntry(ObjectId subjectId,
                        ObjectId userId,
                        String subjectName,
                        String title,
                        String content,
                        ObjectId groupId,
                        ObjectId communityId,
                        String groupName,
                        List<AttachmentEntry> imageList,
                        List<String> voteContent,
                        int voteMaxCount,
                        long voteDeadTime,
                        int voteType,
                        int visiblePermission
                        ){
        BasicDBObject basicDBObject=new BasicDBObject()
                .append("sud",subjectId)
                .append("uid",userId)
                .append("su",subjectName)
                .append("tl",title)
                .append("cn",content)
                .append("gid",groupId)
                .append("gn",groupName)
                .append("cid",communityId)
                .append("il", MongoUtils.fetchDBObjectList(imageList))
                .append("vt",MongoUtils.convert(voteContent))
                .append("vmc",voteMaxCount)
                .append("vdt",voteDeadTime)
                .append("vty",voteType)
                .append("vp",visiblePermission)
                .append("sti",System.currentTimeMillis())
                .append("cc",Constant.ZERO)
                .append("vc",Constant.ZERO)
                .append("ir", Constant.ZERO);
        setBaseEntry(basicDBObject);
    }

    public void setSubmitTime(long submitTime){
        setSimpleValue("sti",submitTime);
    }

    public long getSubmitTime(){
        return getSimpleLongValue("sti");
    }

    public void setGroupName(String groupName){
        setSimpleValue("gn",groupName);
    }

    public String getGroupName(){
        return getSimpleStringValue("gn");
    }

    public void setVoteCount(int voteCount){
        setSimpleValue("vc",voteCount);
    }

    public int getVoteCount(){
        return getSimpleIntegerValue("vc");
    }

    public void setCommentCount(int commentCount){
        setSimpleValue("cc",commentCount);
    }

    public int getCommentCount(){
        return getSimpleIntegerValue("cc");
    }

    public void setVisiblePermission(int visiblePermission){
        setSimpleValue("vp",visiblePermission);
    }

    public int getVisiblePermission(){
        return getSimpleIntegerValue("vp");
    }

    public void setVoteType(int voteType){
        setSimpleValue("vty",voteType);
    }

    public int getVoteType(){
        return getSimpleIntegerValue("vty");
    }

    public long getVoteDeadTime(){
        return getSimpleLongValue("vdt");
    }

    public void setVoteMaxCount(int voteMaxCount){
        setSimpleValue("vmc",voteMaxCount);
    }

    public int getVoteMaxCount(){
        return getSimpleIntegerValue("vmc");
    }

    public void setVoteContent(List<String> voteContent){
        setSimpleValue("vt",MongoUtils.convert(voteContent));
    }


    public List<String> getVoteContent(){
        BasicDBList list = getDbList("vt");
        List<String> voteList=new ArrayList<String>();
        for (Object dbo : list) {
            voteList.add((String)dbo);
        }
        return voteList;
    }

    public void setImageList(List<AttachmentEntry> imageList){
        setSimpleValue("il",MongoUtils.fetchDBObjectList(imageList));
    }

    public List<AttachmentEntry> getImageList() {
        BasicDBList list = getDbList("il");
        List<AttachmentEntry> imageEntries = new ArrayList<AttachmentEntry>();
        for (Object dbo : list) {
            BasicDBObject dbObject = (BasicDBObject) dbo;
            imageEntries.add(new AttachmentEntry(dbObject));
        }
        return imageEntries;
    }

    public void setCommunityId(ObjectId communityId){
        setSimpleValue("cid",communityId);
    }

    public ObjectId getCommunityId(){
        return getSimpleObjecIDValue("cid");
    }

    public void setGroupId(ObjectId groupId){
        setSimpleValue("gid",groupId);
    }

    public ObjectId getGroupId(){
        return getSimpleObjecIDValue("gid");
    }

    public void setContent(String content){
        setSimpleValue("cn",content);
    }

    public String getContent(){
        return getSimpleStringValue("cn");
    }

    public void setTitle(String title){
        setSimpleValue("tl",title);
    }

    public String getTitle(){
        return getSimpleStringValue("tl");
    }

    public void setSubjectName(String subjectName){
        setSimpleValue("su",subjectName);
    }

    public String getSubjectName(){
        return getSimpleStringValue("su");
    }

    public void setUserId(ObjectId userId){
        setSimpleValue("uid",userId);
    }

    public ObjectId getUserId(){
        return getSimpleObjecIDValue("uid");
    }

    public void setSubjectId(ObjectId subjectId){
        setSimpleValue("sud",subjectId);
    }

    public ObjectId getSubjectId(){
        return getSimpleObjecIDValue("sud");
    }
}
