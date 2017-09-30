package com.pojo.reportCard;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by scott on 2017/9/29.
 * {
 *     groupExamDetailId:考试Id
 *     aPercent:A率
 *     bPercent:B率
 *     cPercent:C率
 *     dPercent:D率
 * }
 */
public class RecordLevelEvaluateEntry extends BaseDBObject {

    public RecordLevelEvaluateEntry(DBObject dbObject){
        setBaseEntry((BasicDBObject)dbObject);
    }

    public RecordLevelEvaluateEntry(
            ObjectId groupExamDetailId,
            double aPercent,
            double bPercent,
            double cPercent,
            double dPercent
    ){
        BasicDBObject basicDBObject=new BasicDBObject()
                .append("eid",groupExamDetailId)
                .append("ap",aPercent)
                .append("bp",bPercent)
                .append("cp",cPercent)
                .append("dp",dPercent)
                .append("ir", Constant.ZERO);
        setBaseEntry(basicDBObject);
    }

    public void setDPercent(double dPercent){
        setSimpleValue("dp",dPercent);
    }

    public double getDpercent(){
        return getSimpleDoubleValue("dp");
    }

    public void setCPercent(double cPercent){
        setSimpleValue("cp",cPercent);
    }

    public double getCpercent(){
        return getSimpleDoubleValue("cp");
    }

    public void setBPercent(double bPercent){
        setSimpleValue("bp",bPercent);
    }

    public double getBpercent(){
        return getSimpleDoubleValue("bp");
    }

    public void setAPercent(double aPercent){
        setSimpleValue("ap",aPercent);
    }

    public double getApercent(){
        return getSimpleDoubleValue("ap");
    }

    public void setGroupExamDetailId(ObjectId groupExamDetailId){
       setSimpleValue("eid",groupExamDetailId);
    }

    public ObjectId getGroupExamDetailId(){
        return getSimpleObjecIDValue("eid");
    }

}
