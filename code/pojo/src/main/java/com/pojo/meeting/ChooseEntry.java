package com.pojo.meeting;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * Created by wang_xinxin on 2016/8/14.
 */
public class ChooseEntry extends BaseDBObject {


    private static final long serialVersionUID = -256188587222660685L;

    public ChooseEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public ChooseEntry(String answer,List<ObjectId> userIds) {
        super();
        BasicDBObject baseEntry =new BasicDBObject()
                .append("id", new ObjectId())
                .append("ans", answer)
                .append("uis", MongoUtils.convert(userIds));
        setBaseEntry(baseEntry);
    }
    public ObjectId getId() {
        return getSimpleObjecIDValue("id");
    }
    public void setId(ObjectId id) {
        setSimpleValue("id", id);
    }
    public List<ObjectId> getUserIds() {
        return MongoUtils.getFieldObjectIDs(this, "uis");
    }
    public void setUserIds(List<ObjectId> userIds) {
        setSimpleValue("uis", MongoUtils.convert(userIds));
    }
    public String getAnswer() {
        return getSimpleStringValue("ans");
    }
    public void setAnswer(String answer) {
        setSimpleValue("ans", answer);
    }

}
