package com.pojo.teachermanage;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

/**
 * 老师课程项目
 * <pre>
 * collectionName:project
 * </pre>
 * <pre>
 * {
 *  cnm:培训课程
 *  con:说明
 *  si:学校id
 *  sc:分数
 *  flg:删除
 * }
 * </pre>
 * Created by wang_xinxin on 2016/2/29.
 */
public class CourseProjectEntry extends BaseDBObject {


    private static final long serialVersionUID = 6262926262649994039L;

    public CourseProjectEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public CourseProjectEntry(String course, String content,ObjectId schoolId,int score,int delflg) {
        super();
        BasicDBObject baseEntry =new BasicDBObject()
                .append("cnm", course)
                .append("con", content)
                .append("si",schoolId)
                .append("sc", score)
                .append("flg", delflg);
        setBaseEntry(baseEntry);
    }

    public String getCourse() {
        return getSimpleStringValue("cnm");
    }
    public void setCourse(String course) {
        setSimpleValue("cnm",course);
    }

    public String getContent() {
        return getSimpleStringValue("con");
    }

    public void setContent(String content) {
        setSimpleValue("con",content);
    }

    public ObjectId getSchoolId() {
        return getSimpleObjecIDValue("si");
    }

    public void setSchoolId(ObjectId schoolId) {
        setSimpleValue("si",schoolId);
    }

    public int getScore() {
        return getSimpleIntegerValue("sc");
    }

    public void setScore(int score) {
        setSimpleValue("sc",score);
    }

    public int getDelflg() {
        return getSimpleIntegerValue("flg");
    }

    public void setDelflg(int delflg) {
        setSimpleValue("flg",delflg);
    }
}
