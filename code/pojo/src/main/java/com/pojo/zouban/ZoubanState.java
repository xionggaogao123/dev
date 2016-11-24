package com.pojo.zouban;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

/**
 * 走班状态表
 * {
 *     sid:学校id-------------->scholId
 *     gid：年级id------------->gradeId
 *     te:学期----------------->term
 *     st:状态------------------>state
 *     st2:状态2---------------->state2
 * }
 * Created by qiangm on 2015/10/14.
 */
public class ZoubanState extends BaseDBObject{
    public ZoubanState()
    {
        super();
    }

    public ZoubanState(BasicDBObject basicDBObject)
    {
        super(basicDBObject);
    }

    public ZoubanState(ObjectId schoolId, ObjectId gradeId, String term, int state)
    {
        super();
        BasicDBObject basicDBObject=new BasicDBObject()
                .append("sid",schoolId)
                .append("gid",gradeId)
                .append("te",term)
                .append("st",state)
                .append("st2",1);
        setBaseEntry(basicDBObject);
    }
    public void setSchoolId(ObjectId schoolId)
    {
        setSimpleValue("sid",schoolId);
    }
    public ObjectId getSchoolId()
    {
        return getSimpleObjecIDValue("sid");
    }

    public void setGradeId(ObjectId gradeId)
    {
        setSimpleValue("gid",gradeId);
    }
    public ObjectId getGradeId()
    {
        return getSimpleObjecIDValue("gid");
    }

    public void setTerm(String term)
    {
        setSimpleValue("te",term);
    }
    public String getTerm()
    {
        return getSimpleStringValue("te");
    }

    public void setState(int state)
    {
        setSimpleValue("st",state);
    }
    public int getState()
    {
        return getSimpleIntegerValue("st");
    }
    public void setState2(int state2)
    {
        setSimpleValue("st2",state2);
    }
    public int getState2()
    {
        if(this.getBaseEntry().containsField("st2"))
            return getSimpleIntegerValue("st2");
        else
            return 1;
    }
}
