package com.pojo.Quality;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

/**
 * 教师质量
 * <pre>
 * collectionName:quality
 * </pre>
 * <pre>
 * {
 *  term:学期
 *  ti:老师ID
 *  sc:得分
 * }
 * </pre>
 * Created by wang_xinxin on 2016/10/24.
 */
public class QualityEntry extends BaseDBObject {
    private static final long serialVersionUID = 7573629289728740917L;

    public QualityEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public QualityEntry(String term,ObjectId teacherId,String comment,String score) {
        super();
        BasicDBObject dbo =new BasicDBObject()
                .append("term", term)
                .append("ti", teacherId)
                .append("com",comment)
                .append("sc", score);
        setBaseEntry(dbo);
    }

    public ObjectId getTeacherId() {
        return getSimpleObjecIDValue("ti");
    }
    public void setTeacherId(ObjectId teacherId) {
        setSimpleValue("ti", teacherId);
    }
    public String getScore() {
        return getSimpleStringValue("sc");
    }
    public void setScore(String score) {
        setSimpleValue("sc", score);
    }
    public String getTerm() {
        return getSimpleStringValue("term");
    }
    public void setTerm(String term) {
        setSimpleValue("term", term);
    }
    public String getComment() {
        return getSimpleStringValue("com");
    }
    public void setComment(String comment) {
        setSimpleValue("com", comment);
    }
}
