package com.pojo.excellentCourses;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

/**
 * Created by James on 2018-11-26.
 *
 * 用户  课程   直播间      三者关系表
 *
 */
public class UserClassRoomEntry extends BaseDBObject {
    public UserClassRoomEntry(){

    }


    public UserClassRoomEntry(BasicDBObject dbObject){
        super(dbObject);
    }

    public UserClassRoomEntry(
            ObjectId userId,
            ObjectId contactId,
            String roomId
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append("uid",userId)
                .append("cid",contactId)
                .append("rid",roomId);
        setBaseEntry(dbObject);
    }

    public void setUserId(ObjectId userId){
        setSimpleValue("uid", userId);
    }

    public ObjectId getUserId(){
        return getSimpleObjecIDValue("uid");
    }

    public void setContactId(ObjectId contactId){
        setSimpleValue("cid",contactId);
    }

    public ObjectId getContactId(){
        return getSimpleObjecIDValue("cid");
    }

    public void setRoomId(String roomId){
        setSimpleValue("rid",roomId);
    }

    public String getRoomId(){
        return getSimpleStringValue("rid");
    }
}
