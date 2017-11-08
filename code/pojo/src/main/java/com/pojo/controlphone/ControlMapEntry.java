package com.pojo.controlphone;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.Date;

/**
 * Created by James on 2017/11/7.
 *
 ControlMapEntry(管控地图信息记录表)
 id
 userId             用户id                     uid
 parentId           父id                        pid
 dateTime           记录日期                   dtm
 longitude              经度                         lon
 latitude                纬度                         lat
 angle                 方向                          ang
 distance              距离						   dis
 speed                速度                          spe
 isSafe                是否安全                       iss
 */
public class ControlMapEntry extends BaseDBObject {
    public ControlMapEntry(){

    }

    public ControlMapEntry(BasicDBObject baseEntry){
        super(baseEntry);
    }
    //添加构造
    public ControlMapEntry(
            ObjectId parentId,
            ObjectId userId,
            String longitude,
            String latitude,
            String angle,
            String distance,
            int speed,
            int isSafe
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append("pid",parentId)
                .append("uid", userId)
                .append("dtm", new Date().getTime())
                .append("lon", longitude)
                .append("lat", latitude)
                .append("ang", angle)
                .append("dis", distance)
                .append("spe", speed)
                .append("iss", isSafe)
                .append("isr", 0);
        setBaseEntry(dbObject);
    }

    //修改构造
    public ControlMapEntry(
            ObjectId id,
            ObjectId parentId,
            ObjectId userId,
            String longitude,
            String latitude,
            String angle,
            String distance,
            int speed,
            int isSafe
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append(Constant.ID,id)
                .append("pid", parentId)
                .append("uid", userId)
                .append("dtm", new Date().getTime())
                .append("lon", longitude)
                .append("lat", latitude)
                .append("ang", angle)
                .append("dis", distance)
                .append("spe", speed)
                .append("iss", isSafe)
                .append("isr", 0);
        setBaseEntry(dbObject);
    }

    public ObjectId getParentId(){
        return getSimpleObjecIDValue("pid");
    }
    public void setParentId(ObjectId parentId){
        setSimpleValue("pid",parentId);
    }
    public ObjectId getUserId(){
        return getSimpleObjecIDValue("uid");
    }
    public void setUserId(ObjectId userId){
        setSimpleValue("uid",userId);
    }

    public String getLongitude(){
        return getSimpleStringValue("lon");
    }

    public void setLongitude(String longitude){
        setSimpleValue("lon",longitude);
    }
    public String getLatitude(){
        return getSimpleStringValue("lat");
    }

    public void setLatitude(String latitude){
        setSimpleValue("lat",latitude);
    }
    public String getAngle(){
        return getSimpleStringValue("ang");
    }

    public void setAngle(String angle){
        setSimpleValue("ang",angle);
    }



    public int getSpeed(){
        return getSimpleIntegerValue("spe");
    }

    public void setSpeed(int speed){
        setSimpleValue("spe",speed);
    }
    public int getIsSafe(){
        return getSimpleIntegerValue("iss");
    }

    public void setIsSafe(int isSafe){
        setSimpleValue("iss",isSafe);
    }

    public String getDistance(){
        return getSimpleStringValue("dis");
    }

    public void setDistance(String distance){
        setSimpleValue("dis",distance);
    }

    public long getDateTime(){
        return getSimpleLongValue("dtm");
    }
    public void setDateTime(long dateTime){
        setSimpleValue("dtm",dateTime);
    }

    public int getIsRemove(){
        return getSimpleIntegerValue("isr");
    }

    public void setIsRemove(int isRemove){
        setSimpleValue("isr",isRemove);
    }

}
