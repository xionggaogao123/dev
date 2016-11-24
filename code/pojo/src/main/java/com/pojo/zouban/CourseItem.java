package com.pojo.zouban;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * 课表嵌套数据 TimeTableEntry嵌套类
 * {
 * id:------------------------>id
 * xi: 星期------------------->xIndex
 * yi:  第几节课-------------->yIndex
 * cou: 课程id列表------------>course[]
 * ty:类型------------------->参照ZoubanType
 * }
 * Created by qiangm on 2015/9/15.
 */
public class CourseItem extends BaseDBObject {
    public CourseItem(BasicDBObject basicDBObject) {
        super(basicDBObject);
    }

    public CourseItem() {
        super();
        BasicDBObject basicDBObject = new BasicDBObject()
                .append("id", null)
                .append("xi", 0)
                .append("yi", 0)
                .append("cou", new BasicDBList())
                .append("ty", 0);
        setBaseEntry(basicDBObject);
    }

    public CourseItem(ObjectId id, int xIndex, int yIndex, List<ObjectId> list, int type) {
        super();
        BasicDBObject basicDBObject = new BasicDBObject()
                .append("id", id)
                .append("xi", xIndex)
                .append("yi", yIndex)
                .append("cou", MongoUtils.convert(list))
                .append("ty", type);
        setBaseEntry(basicDBObject);
    }

    public ObjectId getId() {
        return getSimpleObjecIDValue("id");
    }

    public void setId(ObjectId id) {
        setSimpleValue("id", id);
    }

    public int getXIndex() {
        return getSimpleIntegerValue("xi");
    }

    public void setXIndex(int xIndex) {
        setSimpleValue("xi", xIndex);
    }

    public int getYIndex() {
        return getSimpleIntegerValue("yi");
    }

    public void setYIndex(int yIndex) {
        setSimpleValue("yi", yIndex);
    }

    public List<ObjectId> getCourse() {
        List<ObjectId> courseList = new ArrayList<ObjectId>();
        BasicDBList list = (BasicDBList) getSimpleObjectValue("cou");
        if (list != null && !list.isEmpty()) {
            for (Object o : list) {
                courseList.add((ObjectId) o);
            }
        }
        return courseList;
    }

    public void setCourse(List<ObjectId> course) {
        setSimpleValue("cou", MongoUtils.convert(course));
    }

    public int getType() {
        return getSimpleIntegerValue("ty");
    }

    public void setType(int type) {
        setSimpleValue("ty", type);
    }
}
