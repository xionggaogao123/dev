package com.pojo.groupchatrecord;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import com.sys.utils.DateTimeUtils;
import org.bson.types.ObjectId;

/**
 * Created by scott on 2017/12/25.
 * {
 *     groupId:群组Id
 *     userId:用户Id（发话人）
 *     receivedId:接收人Id(接话人)
 *     submitTime:提交时间
 *     type:提交类型 1:文本 2:图片 3:视频 4:附件
 *     content:文本内容
 *     fileUrl:文件路径
 *     imageUrl:图片路径
 *     chatType:聊天类型 1:群聊天 2:单人聊
 *
 * }
 */
public class GroupChatRecordEntry extends BaseDBObject{

    public GroupChatRecordEntry(DBObject dbObject){
        setBaseEntry((BasicDBObject)dbObject);
    }

    public GroupChatRecordEntry(ObjectId groupId,
                                ObjectId userId,
                                ObjectId receiveId,
                                int type,
                                int chatType,
                                String content,
                                String fileUrl,
                                String imageUrl){
        BasicDBObject basicDBObject = new BasicDBObject()
                .append("gid",groupId)
                .append("rid",receiveId)
                .append("ct",chatType)
                .append("uid",userId)
                .append("ty",type)
                .append("cn",content)
                .append("furl",fileUrl)
                .append("sti",System.currentTimeMillis())
                .append("sd", DateTimeUtils.convert(System.currentTimeMillis(),DateTimeUtils.DATE_YYYY_MM_DD))
                .append("iurl",imageUrl)
                .append("ir", Constant.ZERO);
        setBaseEntry(basicDBObject);
    }

    public void setChatType(int chatType){
        setSimpleValue("ct",chatType);
    }

    public int getChatType(){
        return getSimpleIntegerValue("ct");
    }

    public void setReceiveId(ObjectId receiveId){
        setSimpleValue("rid",receiveId);
    }

    public ObjectId getReceiveId(){
        return getSimpleObjecIDValue("rid");
    }

    public void setSubmitDay(String submitDay){
        setSimpleValue("sd",submitDay);
    }

    public String getSubmitDay(){
        return getSimpleStringValue("sd");
    }

    public void setImageUrl(String imageUrl){
        setSimpleValue("iurl",imageUrl);
    }

    public String getImageUrl(){
        return getSimpleStringValue("iurl");
    }

    public void setSubmitTime(long submitTime){
        setSimpleValue("sti",submitTime);
    }

    public long getSubmitTime(){
        return getSimpleLongValue("sti");
    }

    public void setFileUrl(String fileUrl){
        setSimpleValue("furl",fileUrl);
    }

    public String getFileUrl(){
        return getSimpleStringValue("furl");
    }

    public void setContent(String content){
        setSimpleValue("cn",content);
    }

    public String getContent(){
        return getSimpleStringValue("cn");
    }

    public void setType(int type){
        setSimpleValue("ty",type);
    }

    public int getType(){
        return getSimpleIntegerValue("ty");
    }

    public void setUserId(ObjectId userId){
        setSimpleValue("uid",userId);
    }

    public ObjectId getUserId(){
        return getSimpleObjecIDValue("uid");
    }

    public void setGroupId(ObjectId groupId){
        setSimpleValue("gid",groupId);
    }

    public ObjectId getGroupId(){
        return getSimpleObjecIDValue("gid");
    }
}
