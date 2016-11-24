package com.pojo.news;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;

/**
 * Created by yan on 2015/3/16.
 */
public class NewsEntry extends BaseDBObject {

    /**
	 * 
	 */
	private static final long serialVersionUID = 2295714391503293286L;
	/**
     * title 标题 ->ti
     * column 栏目 ->col
     * pinned 置顶 ->pin
     * thumb 缩略图 ->thu
     * digest 摘要 ->dig
     * content 正文 ->con
     * readcount 阅读量 ->rc
     * userId 发起人 ->uid
     * schoolId 学校Id ->sid
     * educationId 教育局Id ->eid
     */

    public NewsEntry(){
        BasicDBObject basicDBObject=new BasicDBObject();
        basicDBObject.append("ti","").
                append("col", null).
                append("pin", 0).
                append("thu", "").
                append("dig", "").
                append("con", "").
                append("rc",0).
                append("uid", null).
                append("sid", null).
                append("eid", null);
    }
    public NewsEntry(BasicDBObject basicDBObject){
        super(basicDBObject);
    }

    /**
     * get部分
     */
    public String getTitle()
    {
        return getSimpleStringValue("ti");
    }
    public ObjectId getColumn()
    {
        return getSimpleObjecIDValue("col");
    }
    public Integer getPinned()
    {
        return getSimpleIntegerValue("pin");
    }
    public String getThumb()
    {
        return getSimpleStringValue("thu");
    }
    public String getDigest()
    {
        return getSimpleStringValue("dig");
    }
    public String getContent()
    {
        return getSimpleStringValue("con");
    }
    public int getReadCount(){return getSimpleIntegerValue("rc");}
    public ObjectId getUserId()
    {
        return getSimpleObjecIDValue("uid");
    }
    public ObjectId getSchoolId()
    {
        return getSimpleObjecIDValue("sid");
    }
    public ObjectId getEducationId()
    {
        return getSimpleObjecIDValue("eid");
    }

    /**
     * set部分
     */

    public void setTitle(String title)
    {
        setSimpleValue("ti",title);
    }
    public void setColumn(ObjectId objectId)
    {
        setSimpleValue("col",objectId);
    }
    public void setPinned(int pinned)
    {
        setSimpleValue("pin",pinned);
    }
    public void setThumb(String thumb)
    {
        setSimpleValue("thu",thumb);
    }
    public void setDigest(String digest)
    {
        setSimpleValue("dig",digest);
    }
    public void setContent(String content)
    {
        setSimpleValue("con",content);
    }
    public void setUserId(Object userId)
    {
        setSimpleValue("uid",userId);
    }
    public void setSchoolId(ObjectId schoolId)
    {
        setSimpleValue("sid",schoolId);
    }
    public void setReadCount(int readCount){setSimpleValue("rc",readCount);}
    public void setEducationId(ObjectId educationId)
    {
        setSimpleValue("eid",educationId);
    }

}
