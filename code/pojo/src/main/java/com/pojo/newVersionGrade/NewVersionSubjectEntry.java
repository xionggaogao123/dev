package com.pojo.newVersionGrade;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**用户学科关系绑定表
 * Created by James on 2017/9/22.
 * id
 * userId         用户个人id         uid
 * subjectList     学科list          slt
 * isRemove        是否删除          isr
 *
 *
 */
public class NewVersionSubjectEntry extends BaseDBObject {
    public NewVersionSubjectEntry(BasicDBObject baseEntry){
        super(baseEntry);
    }

    public NewVersionSubjectEntry(DBObject dbObject){
        setBaseEntry((BasicDBObject) dbObject);
    }

    public NewVersionSubjectEntry(ObjectId userId,
                                List<ObjectId> subjectList){
        BasicDBObject basicDBObject = new BasicDBObject()
                .append("uid", userId)
                .append("slt",subjectList)
                .append("isr", Constant.ZERO);
        setBaseEntry(basicDBObject);
    }
    public NewVersionSubjectEntry(ObjectId id,ObjectId userId,
                                  List<ObjectId> subjectList){
        BasicDBObject basicDBObject = new BasicDBObject()
                .append(Constant.ID, id)
                .append("uid", userId)
                .append("slt",subjectList)
                .append("isr", Constant.ZERO);
        setBaseEntry(basicDBObject);
    }

    public void setSubjectList(List<ObjectId> subjectList){
        setSimpleValue("slt", MongoUtils.convert(subjectList));
    }

    public List<ObjectId> getSubjectList(){
        ArrayList<ObjectId> subjectList = new ArrayList<ObjectId>();
        BasicDBList dbList = (BasicDBList) getSimpleObjectValue("slt");
        if(dbList != null && !dbList.isEmpty()){
            for (Object obj : dbList) {
                subjectList.add((ObjectId)obj);
            }
        }
        return subjectList;
    }

    public ObjectId getUserId(){
        return getSimpleObjecIDValue("uid");
    }

    public void setUserId(ObjectId userId){
        setSimpleValue("uid",userId);
    }
    public int getIsRemove(){
        return getSimpleIntegerValue("isr");
    }

    public void setIsRemove(int isRemove){
        setSimpleValue("isr",isRemove);
    }
}
