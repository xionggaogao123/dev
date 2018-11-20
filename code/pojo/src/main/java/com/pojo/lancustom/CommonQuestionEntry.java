package com.pojo.lancustom;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;

public class CommonQuestionEntry extends BaseDBObject {

    /**
     * @Fields serialVersionUID : TODO(目的和意义)
     */
    private static final long serialVersionUID = 1L;

    public CommonQuestionEntry() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    public CommonQuestionEntry(DBObject dbObject) {
        setBaseEntry((BasicDBObject)dbObject);
    }

    public CommonQuestionEntry(BasicDBObject baseEntry) {
        super(baseEntry);
        // TODO Auto-generated constructor stub
    }
    
    public CommonQuestionEntry(String question, 
                               String answer, 
                               ObjectId pid,
                               int type) {
        BasicDBObject dbObject=new BasicDBObject()
            .append("question", question)
            .append("answer", answer)
            .append("pid", pid) 
            .append("type", type)
            .append("isr", Constant.ZERO);
        setBaseEntry(dbObject);
    }
    
    public CommonQuestionEntry(String question, 
                               String answer,
                               int type) {
        BasicDBObject dbObject=new BasicDBObject()
            .append("question", question)
            .append("answer", answer)
            .append("type", type)
            .append("isr", Constant.ZERO);
        setBaseEntry(dbObject);
    }
    
    public String getQuestion() {
        return getSimpleStringValue("question");
    }
    
    public void setQuestion(String question) {
        setSimpleValue("question",question);
    }
    
    public String getAnswer() {
        return getSimpleStringValue("answer");
    }
    
    public void setAnswer(String answer) {
        setSimpleValue("answer",answer);
    }

    public ObjectId getPid(){
        return getSimpleObjecIDValue("pid");
    }

    public void setPid(ObjectId pid){
        setSimpleValue("pid",pid);
    }
    
    public int getIsRemove(){
        return getSimpleIntegerValue("isr");
    }

    public void setIsRemove(int isRemove){
        setSimpleValue("isr",isRemove);
    }
    
    public int getType(){
        return getSimpleIntegerValue("type");
    }

    public void setType(int isRemove){
        setSimpleValue("type",isRemove);
    }

    public String getClasses(){
        return getSimpleStringValue("classes");
    }

    public void setClasses(String classes){
        setSimpleValue("classes",classes);
    }
}
