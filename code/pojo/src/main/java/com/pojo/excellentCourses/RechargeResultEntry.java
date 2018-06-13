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
 * 美豆 消费记录/充值记录
 * Created by James on 2018-05-08.
 *
 * id                                                id
 * behaviorId            账户id                      bid
 * userId                操作人                      uid
 * createTime            充值时间                    ctm
 * description           充值说明                    des
 * way                   充值方式                    way       0增加    1 微信     2银行卡   3 特殊   4 支付宝
 * type                  记录类型                    typ       0  消费    1 充值     2 提现  3 退款
 * money                 充值金额                    mon
 * sonId                 用途对象                    sid
 * contactId             课程id                      cid
 * classIdList           课节id (List<ObjectId>)     clt
 *
 */
public class RechargeResultEntry extends BaseDBObject {
    public RechargeResultEntry(){

    }

    public RechargeResultEntry(BasicDBObject dbObject){
        super(dbObject);
    }

    public RechargeResultEntry(
            ObjectId behaviorId,
            ObjectId userId,
            String description,
            int way,
            int type,
            int money,
            ObjectId sonId,
            ObjectId contactId,
            List<ObjectId> classList
    ){
        BasicDBObject dbObject = new BasicDBObject()
                .append("bid",behaviorId)
                .append("uid",userId)
                .append("des",description)
                .append("way",way)
                .append("typ",type)
                .append("mon",money)
                .append("sid",sonId)
                .append("cid",contactId)
                .append("clt",classList)
                .append("ctm",new Date().getTime())
                .append("isr", Constant.ZERO);
      setBaseEntry(dbObject);
    }

    public ObjectId getUserId(){
        return getSimpleObjecIDValue("uid");
    }

    public void setUserId(ObjectId userId){
        setSimpleValue("uid",userId);
    }
    public ObjectId getBehaviorId(){
        return getSimpleObjecIDValue("bid");
    }

    public void setBehaviorId(ObjectId behaviorId){
        setSimpleValue("bid",behaviorId);
    }

    public String getDescription(){
        return  getSimpleStringValue("des");
    }
    public void setDescription(String description){
        setSimpleValue("des",description);
    }


    public int getWay(){
        return getSimpleIntegerValue("way");
    }

    public void setWay(int way){
        setSimpleValue("way",way);
    }
    public int getType(){
        return getSimpleIntegerValue("typ");
    }

    public void setType(int type){
        setSimpleValue("typ",type);
    }

    public int getMoney(){
        return getSimpleIntegerValue("mon");
    }

    public void setMoney(int money){
        setSimpleValue("mon",money);
    }

    public ObjectId getContactId(){
        return getSimpleObjecIDValue("cid");
    }

    public void setContactId(ObjectId contactId){
        setSimpleValue("cid",contactId);
    }

    public ObjectId getSonId(){
        return getSimpleObjecIDValue("sid");
    }

    public void setSonId(ObjectId sonId){
        setSimpleValue("sid",sonId);
    }

    public void setClassList(List<ObjectId> classList){
        setSimpleValue("clt", MongoUtils.convert(classList));
    }

    public List<ObjectId> getClassList(){
        ArrayList<ObjectId> classList = new ArrayList<ObjectId>();
        BasicDBList dbList = (BasicDBList) getSimpleObjectValue("clt");
        if(dbList != null && !dbList.isEmpty()){
            for (Object obj : dbList) {
                classList.add((ObjectId)obj);
            }
        }
        return classList;
    }


    public long getCreateTime(){
        return getSimpleLongValue("ctm");
    }

    public void setCreateTime(int createTime){
        setSimpleValue("ctm",createTime);
    }

    public int getIsRemove(){
        return getSimpleIntegerValue("isr");
    }

    public void setIsRemove(int isRemove){
        setSimpleValue("isr",isRemove);
    }



}
