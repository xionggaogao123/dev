package com.pojo.interestCategory;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.pojo.app.IdNameValuePair;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fl on 2016/1/25.
 * sid:学校id
 * tms:学期列表List<IdNameValuePair> id保留 name学期名称 value对应的termType
 */
public class InterestClassTermsEntry extends BaseDBObject{

    public InterestClassTermsEntry(){}

    public InterestClassTermsEntry(BasicDBObject baseEntry){
        setBaseEntry(baseEntry);
    }

    public InterestClassTermsEntry(ObjectId schoolId, List<IdNameValuePair> terms){
        BasicDBObject baseEntry = new BasicDBObject()
                .append("sid", schoolId)
                .append("tms", MongoUtils.convert(MongoUtils.fetchDBObjectList(terms)));
        setBaseEntry(baseEntry);
    }

    public ObjectId getSchoolId(){
        return getSimpleObjecIDValue("sid");
    }

    public void setSchoolId(ObjectId schoolId){
        setSimpleValue("sid", schoolId);
    }

    public List<IdNameValuePair> getTerms(){
        List<IdNameValuePair> retList = new ArrayList<IdNameValuePair>();
        BasicDBList list = (BasicDBList)getSimpleObjectValue("tms");
        if(null!=list && list.size()>0){
            for(Object o : list){
                retList.add(new IdNameValuePair((BasicDBObject)o));
            }
        }
        return retList;
    }

    public void setTerms(List<IdNameValuePair> terms){
        setSimpleValue("tms", MongoUtils.convert(MongoUtils.fetchDBObjectList(terms)));
    }
}
