package com.pojo.leave;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.school.Subject;
import org.bson.types.ObjectId;

/**教师请假
 * _id
 * tea:teacherId   老师id
 * si:schoolId      学校id
 * ti:title         标题
 * con:content      请假理由
 * dtf:dateFrom     请假开始日期
 * dte:dateEnd      请假结束日期
 * cc:classCount    请假期间有多少节课
 * dt:date          请假日期
 * re:reply         回复
 * rp:replyPerson   回复人
 * te:term
 * dl:delete
 * Created by qiangm on 2016/3/1.
 */
public class LeaveEntry extends BaseDBObject {
    public LeaveEntry()
    {
        super();
    }
    public LeaveEntry(BasicDBObject basicDBObject)
    {
        super(basicDBObject);
    }
    public LeaveEntry(ObjectId userId,ObjectId schoolId,String title,String content,long date1,long date2,int classCount,long date,
                      int reply,ObjectId replyPerson,String term)
    {
        BasicDBObject basicDBObject=new BasicDBObject()
                .append("_id",new ObjectId())
                .append("tea", userId)
                .append("si",schoolId)
                .append("ti", title)
                .append("con",content)
                .append("dtf",date1)
                .append("dte",date2)
                .append("cc",classCount)
                .append("dt",date)
                .append("re",reply)
                .append("rp",replyPerson)
                .append("te",term)
                .append("dl", 0);
        setBaseEntry(basicDBObject);
    }
    public void setTeacherId(ObjectId teacherId)
    {
        setSimpleValue("tea",teacherId);
    }
    public ObjectId getTeacherId()
    {
        return getSimpleObjecIDValue("tea");
    }
    public void setSchoolId(ObjectId schoolId)
    {
        setSimpleValue("si",schoolId);
    }
    public ObjectId getSchoolId()
    {
        return getSimpleObjecIDValue("si");
    }
    public void setTitle(String title)
    {
        setSimpleValue("ti",title);
    }
    public String getTitle()
    {
        return getSimpleStringValue("ti");
    }
    public void setContent(String content)
    {
        setSimpleValue("con",content);
    }
    public String getContent()
    {
        return getSimpleStringValue("con");
    }
    public void setDateFrom(long date1)
    {
        setSimpleValue("dtf",date1);
    }
    public long getDateFrom()
    {
        return getSimpleLongValue("dtf");
    }
    public void setDateEnd(long date2)
    {
        setSimpleValue("dte",date2);
    }
    public long getDateEnd()
    {
        return getSimpleLongValue("dte");
    }
    public void setClassCount(int classCount)
    {
        setSimpleValue("cc",classCount);
    }
    public int getClassCount()
    {
        return getSimpleIntegerValue("cc");
    }
    public void setDate(long date)
    {
        setSimpleValue("dt",date);
    }
    public long getDate()
    {
        return getSimpleLongValue("dt");
    }
    public void setReply(int reply)
    {
        setSimpleValue("re",reply);
    }
    public int getReply()
    {
        return getSimpleIntegerValue("re");
    }
    public void setReplyPerson(ObjectId personId)
    {
        setSimpleValue("rp",personId);
    }
    public ObjectId getReplyPerson()
    {
        return getSimpleObjecIDValue("rp");
    }
    public void setDelete(int delete)
    {
        setSimpleValue("dl", delete);
    }
    public int getDelete()
    {
        if(!this.getBaseEntry().containsField("dl"))
            return 0;
        return getSimpleIntegerValue("dl");
    }
    public void setTerm(String term)
    {
        setSimpleValue("te", term);
    }
    public String getTerm()
    {
        if(!this.getBaseEntry().containsField("te"))
            return "2015-2016学年第二学期";
        return getSimpleStringValue("te");
    }
}
