package com.pojo.reportCard;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by scott on 2017/12/13.
 */
public class ReportCardSignEntry extends BaseDBObject{

    public ReportCardSignEntry(DBObject dbObject){
        setBaseEntry((BasicDBObject)dbObject);
    }

    public ReportCardSignEntry(ObjectId parentId,
                               ObjectId groupExamDetailId,
                               ObjectId userRecordId,
                               int type,
                               long signTime){
        BasicDBObject basicDBObject = new BasicDBObject()
                .append("pid",parentId)
                .append("gei",groupExamDetailId)
                .append("uri",userRecordId)
                .append("ty",type)
                .append("sti",signTime)
                .append("ir", Constant.ZERO);
        setBaseEntry(basicDBObject);
    }

    public void setType(int type){
        setSimpleValue("ty",type);
    }

    public int getType(){
        return getSimpleIntegerValue("ty");
    }

    public void setUserRecordId(ObjectId userRecordId){
        setSimpleValue("uri",userRecordId);
    }

    public ObjectId getUserRecordId(){
        return getSimpleObjecIDValue("uri");
    }

    public void setSignTime(long signTime){
        setSimpleValue("sti",signTime);
    }

    public long getSignTime(){
        return getSimpleLongValue("sti");
    }

    public void setGroupExamDetailId(ObjectId groupExamDetailId){
        setSimpleValue("gei",groupExamDetailId);
    }

    public ObjectId getGroupExamDetailId(){
        return getSimpleObjecIDValue("gei");
    }

    public void setParentId(ObjectId parentId){
        setSimpleValue("pid",parentId);
    }

    public ObjectId getParentId(){
        return getSimpleObjecIDValue("pid");
    }
}
