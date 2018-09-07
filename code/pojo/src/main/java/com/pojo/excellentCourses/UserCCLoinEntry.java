package com.pojo.excellentCourses;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by James on 2018-09-06.
 * 用户上课记录表--同步cc直播间进出记录
 *
 * roomId           直播间id           rid
 * contactId        关联课程id         cid
 * userId           uid                uid
 * userName         用户名             unm
 * userIp           登陆ip             uip
 * enterTime        进入时间           etm
 * leaveTime        离开时间           ltm
 */
public class UserCCLoinEntry extends BaseDBObject {
    public UserCCLoinEntry(){

    }

    public UserCCLoinEntry(BasicDBObject object){
        super(object);
    }


    //添加构造
    public UserCCLoinEntry(
            String roomId,
            ObjectId userId,
            ObjectId contactId,
            String userName,
            String userIp,
            String enterTime,
            String leaveTime
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append("rid",roomId)
                .append("uid", userId)
                .append("cid", contactId)
                .append("unm",userName)
                .append("uip",userIp)
                .append("etm", enterTime)
                .append("ltm",leaveTime)
                .append("isr",Constant.ZERO);
        setBaseEntry(dbObject);
    }
    public String getRoomId(){
        return getSimpleStringValue("rid");
    }
    public void setRoomId(String roomId){
        setSimpleValue("rid",roomId);
    }

    public ObjectId getUserId(){
        return getSimpleObjecIDValue("uid");
    }
    public void setUserId(ObjectId userId){
        setSimpleValue("uid",userId);
    }
    public ObjectId getContactId(){
        return getSimpleObjecIDValue("cid");
    }
    public void setContactId(ObjectId contactId){
        setSimpleValue("cid",contactId);
    }

    public String getUserName(){
        return getSimpleStringValue("unm");
    }
    public void setUserName(String userName){
        setSimpleValue("unm",userName);
    }

    public String getUserIp(){
        return getSimpleStringValue("uip");
    }
    public void setUserIp(String userIp){
        setSimpleValue("uip",userIp);
    }

    public String getEnterTime(){
        return getSimpleStringValue("etm");
    }

    public void setEnterTime(long enterTime){
        setSimpleValue("etm",enterTime);
    }


    public String getLeaveTime(){
        return getSimpleStringValue("ltm");
    }

    public void setLeaveTime(long leaveTime){
        setSimpleValue("ltm",leaveTime);
    }

    public int getIsRemove(){
        return getSimpleIntegerValue("isr");
    }

    public void setIsRemove(int isRemove){
        setSimpleValue("isr",isRemove);
    }

}
