package com.pojo.Quality;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.docflow.IdUserFilePair;
import com.pojo.lesson.LessonWare;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wang_xinxin on 2016/11/10.
 */
public class TeacherPlainEntry extends BaseDBObject {

    private static final long serialVersionUID = -1058237434235914724L;

    public TeacherPlainEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public TeacherPlainEntry(String term,ObjectId schoolId,ObjectId teacherId,String teachName,String content,List<IdUserFilePair> docs) {
        super();
        BasicDBObject dbo =new BasicDBObject()
                .append("term", term)
                .append("tid", teacherId)
                .append("sid",schoolId)
                .append("teanm", teachName)
                .append("con", content)
                .append("ct",System.currentTimeMillis())
                .append("dcl", MongoUtils.convert(MongoUtils.fetchDBObjectList(docs)))
                .append("ir", Constant.ZERO);
        setBaseEntry(dbo);
    }
    public String getTerm() {
        return getSimpleStringValue("term");
    }
    public void setTerm(String term) {
        setSimpleValue("term", term);
    }
    public ObjectId getTeacherId() {
        return getSimpleObjecIDValue("tid");
    }
    public void setTeacherId(ObjectId teacherId) {
        setSimpleValue("tid", teacherId);
    }
    public ObjectId getSchoolId() {
        return getSimpleObjecIDValue("sid");
    }
    public void setSchoolId(ObjectId schoolId) {
        setSimpleValue("sid", schoolId);
    }
    public String getContent() {
        return getSimpleStringValue("con");
    }
    public void setContent(String content) {
        setSimpleValue("con", content);
    }
    public String getTeachName() {
        return getSimpleStringValue("teanm");
    }
    public void setTeachName(String teachName) {
        setSimpleValue("teanm", teachName);
    }

    public List<IdUserFilePair> getDocFile() {
        List<IdUserFilePair> retList = new ArrayList<IdUserFilePair>();
        BasicDBList list = (BasicDBList) getSimpleObjectValue("dcl");
        if (null != list && !list.isEmpty()) {
            for (Object o : list) {
                retList.add(new IdUserFilePair((BasicDBObject) o));
            }
        }
        return retList;
    }

    public void setDocFile(List<IdUserFilePair> df) {
        List<DBObject> list = MongoUtils.fetchDBObjectList(df);
        setSimpleValue("dcl", MongoUtils.convert(list));
    }
}
