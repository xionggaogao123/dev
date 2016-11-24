package com.pojo.microlesson;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

/**
 * Created by wang_xinxin on 2015/8/27.
 */
public class TypeLessonEntry extends BaseDBObject {
    private static final long serialVersionUID = 3851956261858326498L;

    public TypeLessonEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }


    public TypeLessonEntry(String id,String value) {
        super();
        BasicDBObject baseEntry =new BasicDBObject()
                .append("id", id)
                .append("v", value);
        setBaseEntry(baseEntry);
    }

    public String getId() {
        return getSimpleStringValue("id");
    }
    public void setId(String id) {
        setSimpleValue("id", id);
    }
    public String getValue() {
        return getSimpleStringValue("v");
    }
    public void setValue(String value) {
        setSimpleValue("v", value);
    }


}
