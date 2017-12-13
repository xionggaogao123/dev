package com.pojo.questionbook;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

/**
 * Created by James on 2017/12/12.
 * questionId               问题id                qid
 * questionHeight           问题长度              qht
 * answerHeight             答题区长度            hei
 */
public class QuestionWebSizeEntry extends BaseDBObject {
    public QuestionWebSizeEntry(){

    }
    public QuestionWebSizeEntry(DBObject dbObject) {
        setBaseEntry((BasicDBObject) dbObject);
    }
    public QuestionWebSizeEntry(BasicDBObject object){
        super(object);
    }

    //添加构造
    public QuestionWebSizeEntry(
            ObjectId questionId,
            int questionHeight,
            int answerHeight
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append("qid", questionId)
                .append("qht", questionHeight)
                .append("hei", answerHeight);
        setBaseEntry(dbObject);
    }


    public ObjectId getQuestionId(){
        return getSimpleObjecIDValue("qid");
    }

    public void setQuestionId(ObjectId questionId){
        setSimpleValue("qid",questionId);
    }


    public int getQuestionHeight(){
        return getSimpleIntegerValue("qht");
    }

    public void setQuestionHeight(int questionHeight){
        setSimpleValue("qht",questionHeight);
    }
    public int getAnswerHeight(){
        return getSimpleIntegerValue("hei");
    }

    public void setAnswerHeight(int answerHeight){
        setSimpleValue("hei",answerHeight);
    }
}
