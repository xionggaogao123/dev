package com.pojo.microlesson;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;

/**
 * Created by wang_xinxin on 2016/4/20.
 */
public class ScoreTypeEntry extends BaseDBObject {
    private static final long serialVersionUID = -8679029971377911450L;

    public ScoreTypeEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }


    public ScoreTypeEntry(String name,int score) {
        super();
        BasicDBObject baseEntry =new BasicDBObject()
                .append("name", name)
                .append("score", score);
        setBaseEntry(baseEntry);
    }

    public String getName() {
        return getSimpleStringValue("name");
    }
    public void setName(String name) {
        setSimpleValue("name", name);
    }
    public int getScore() {
        return getSimpleIntegerValue("score");
    }
    public void setScore(int score) {
        setSimpleValue("score", score);
    }
}
