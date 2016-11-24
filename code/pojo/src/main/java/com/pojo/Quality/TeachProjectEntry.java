package com.pojo.Quality;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.lesson.LessonWare;
import com.pojo.utils.MongoUtils;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * Created by wang_xinxin on 2016/11/10.
 */
public class TeachProjectEntry extends BaseDBObject {
    private static final long serialVersionUID = 7536331464868288618L;

    public TeachProjectEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public TeachProjectEntry(String projectName,int count,String quality,String score) {
        super();
        BasicDBObject dbo =new BasicDBObject()
                .append("pnm", projectName)
                .append("cnt", count)
                .append("qty", quality)
                .append("sc", score);
        setBaseEntry(dbo);
    }

    public String getProjectName() {
        return getSimpleStringValue("pnm");
    }
    public void setProjectName(String projectName) {
        setSimpleValue("pnm", projectName);
    }
    public int getCount() {
        return getSimpleIntegerValue("cnt");
    }
    public void setCount(int count) {
        setSimpleValue("cnt", count);
    }
    public String getQuality() {
        return getSimpleStringValue("qty");
    }
    public void setQuality(String quality) {
        setSimpleValue("qty", quality);
    }
    public String getScore() {
        return getSimpleStringValue("sc");
    }
    public void setScore(String score) {
        setSimpleValue("sc", score);
    }
}
