package com.pojo.smalllesson;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by scott on 2017/9/28.
 * {
 *     复兰小课堂生成的个人的二维码以及code
 * }
 */
public class SmallLessonUserCodeEntry extends BaseDBObject{

    public SmallLessonUserCodeEntry(DBObject dbObject){
        setBaseEntry((BasicDBObject)dbObject);
    }

    public SmallLessonUserCodeEntry(ObjectId userId,String qrUrl,
                                    String code){
        BasicDBObject basicDBObject=new BasicDBObject()
                .append("uid",userId)
                .append("qul",qrUrl)
                .append("co",code)
                .append("ir", Constant.ZERO);
        setBaseEntry(basicDBObject);
    }

    public void setCode(String code){
        setSimpleValue("co",code);
    }

    public String getCode(){
        return getSimpleStringValue("co");
    }

    public void setQrUrl(String qrUrl){
        setSimpleValue("qul",qrUrl);
    }

    public String getQrUrl(){
        return getSimpleStringValue("qul");
    }

    public void setUserId(ObjectId userId){
        setSimpleValue("uid",userId);
    }

    public ObjectId getUserId(){
        return getSimpleObjecIDValue("uid");
    }
}
