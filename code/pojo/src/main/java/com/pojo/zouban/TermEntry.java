package com.pojo.zouban;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

/**
 * Created by qiangm on 2015/11/5.
 * sid -----------------》schoolId
 * yaer------------------>学年
 * fts------------------>firstTermStart
 * fte------------------->firstTermEnd
 * sts-------------------->secondTermStart
 * ste-------------------->secondTermEnd
 */
public class TermEntry extends BaseDBObject {
    public TermEntry() {
        super();
    }

    public TermEntry(BasicDBObject basicDBObject) {
        super(basicDBObject);
    }

    public TermEntry(ObjectId schoolId, String year, Long fts, Long fte, Long sts,
                     Long ste) {
        BasicDBObject basicDBObject = new BasicDBObject()
                .append("sid", schoolId)
                .append("year", year)
                .append("fts", fts)
                .append("fte", fte)
                .append("sts", sts)
                .append("ste", ste);
        setBaseEntry(basicDBObject);
    }

    public ObjectId getSchoolId() {
        return getSimpleObjecIDValue("sid");
    }

    public void setSchoolId(ObjectId schoolId) {
        setSimpleValue("sid", schoolId);
    }

    public String getYear() {
        return getSimpleStringValue("year");
    }

    public void setYear(String year) {
        setSimpleValue("year", year);
    }

    public Long getFirstTermStart() {
        return getSimpleLongValue("fts");
    }

    public void setFirstTermStart(Long termStart) {
        setSimpleValue("fts", termStart);
    }

    public Long getFirstTermEnd() {
        return getSimpleLongValue("fte");
    }

    public void setFirstTermEnd(Long termEnd) {
        setSimpleValue("fte", termEnd);
    }

    public Long getSecondTermStart() {
        return getSimpleLongValue("sts");
    }

    public void setSecondTermStart(Long termEnd) {
        setSimpleValue("sts", termEnd);
    }

    public Long getSecondTermEnd() {
        return getSimpleLongValue("ste");
    }

    public void setSecondTermEnd(Long termEnd) {
        setSimpleValue("ste", termEnd);
    }
}
