package com.pojo.teacherevaluation;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.pojo.app.IdValuePair;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * 考核要素+量化成绩  te_element   量化成绩的con是空列表
 * Created by fl on 2016/4/19.
 * si 学校id
 * y  年度 例如2015-2016年度  保存为2015-2016
 * nm 名称
 * sc 分值
 * ty 类型 1：考核要素  2：量化成绩
 * con  内容 List<IdValuePair>
 */
public class ElementEntry extends BaseDBObject {

    public ElementEntry(){}

    public ElementEntry(BasicDBObject baseEntry){
        setBaseEntry(baseEntry);
    }

    public ElementEntry(ObjectId schoolId, String year, String name, double score, int type, List<IdValuePair> content){
        BasicDBObject baseEntry = new BasicDBObject()
                .append("si", schoolId)
                .append("y", year)
                .append("nm", name)
                .append("sc", score)
                .append("ty", type)
                .append("con", MongoUtils.convert(MongoUtils.fetchDBObjectList(content)))
                ;
        setBaseEntry(baseEntry);
    }

    public ObjectId getSchoolId(){
        return getSimpleObjecIDValue("si");
    }

    public void setSchoolId(ObjectId schoolId){
        setSimpleValue("si", schoolId);
    }

    public String getYear(){
        return getSimpleStringValue("y");
    }

    public void setYear(String year){
        setSimpleValue("y", year);
    }

    public String getName(){
        return getSimpleStringValue("nm");
    }

    public void setName(String groupName){
        setSimpleValue("nm", groupName);
    }

    public double getElementScore(){
        return getSimpleDoubleValue("sc");
    }

    public void setElementScore(double elementScore){
        setSimpleValue("sc", elementScore);
    }

    public int getType(){
        return getSimpleIntegerValue("ty");
    }

    public void setType(int type){
        setSimpleValue("ty", type);
    }

    public List<IdValuePair> getElementContent(){
        List<IdValuePair> retList = new ArrayList<IdValuePair>();
        BasicDBList list = (BasicDBList)getSimpleObjectValue("con");
        if(null != list && !list.isEmpty()) {
            for (Object o : list) {
                retList.add(new IdValuePair((BasicDBObject)o));
            }
        }
        return retList;
    }

    public void setElementContent(List<IdValuePair> elementContent){
        setSimpleValue("con", MongoUtils.convert(MongoUtils.fetchDBObjectList(elementContent)));
    }
}
