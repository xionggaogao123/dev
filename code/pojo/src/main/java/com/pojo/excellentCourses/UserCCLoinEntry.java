package com.pojo.excellentCourses;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
            long enterTime,
            long leaveTime
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

    //修改构造
    public UserCCLoinEntry(
            ObjectId id,
            ObjectId userId,
            ObjectId contactId,
            List<ObjectId> classIds
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append(Constant.ID, id)
                .append("uid", userId)
                .append("cid", contactId)
                .append("clt", classIds)
                .append("ctm", new Date().getTime())
                .append("typ",Constant.ZERO)
                .append("dtm",new Date().getTime())
                .append("isr",Constant.ZERO);
        setBaseEntry(dbObject);
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

    public void setClassIds(List<ObjectId> classIds){
        setSimpleValue("clt", MongoUtils.convert(classIds));
    }

    public List<ObjectId> getClassIds(){
        ArrayList<ObjectId> classIds = new ArrayList<ObjectId>();
        BasicDBList dbList = (BasicDBList) getSimpleObjectValue("clt");
        if(dbList != null && !dbList.isEmpty()){
            for (Object obj : dbList) {
                classIds.add((ObjectId)obj);
            }
        }
        return classIds;
    }

    public long getCreateTime(){
        return getSimpleLongValue("ctm");
    }

    public void setCreateTime(long createTime){
        setSimpleValue("ctm",createTime);
    }


    public long getDateTime(){
        return getSimpleLongValue("dtm");
    }

    public void setDateTime(long dateTime){
        setSimpleValue("dtm",dateTime);
    }
    public int getType(){
        return getSimpleIntegerValue("typ");
    }

    public void setType(int type){
        setSimpleValue("typ",type);
    }


    public int getIsRemove(){
        return getSimpleIntegerValue("isr");
    }

    public void setIsRemove(int isRemove){
        setSimpleValue("isr",isRemove);
    }

}
