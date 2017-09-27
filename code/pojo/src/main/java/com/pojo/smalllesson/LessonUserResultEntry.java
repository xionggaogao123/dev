package com.pojo.smalllesson;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 *  复兰互动小课堂用户记录表
 * Created by James on 2017/9/26.
 * id
 * lessonId      课程id          lid
 * userId        用户id          uid
 * userName     用户姓名         unm
 * score        活跃值           sco
 *
 *
 */
public class LessonUserResultEntry extends BaseDBObject {
    public LessonUserResultEntry(){

    }
    public LessonUserResultEntry(BasicDBObject baseEntry){
        super(baseEntry);
    }
    //添加构造
    public LessonUserResultEntry(
           ObjectId lessonId,
           ObjectId userId,
           String userName,
           int score
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append("lid", lessonId)
                .append("uid", userId)
                .append("unm", userName)
                .append("sco",score)
                .append("isr", 0);
        setBaseEntry(dbObject);
    }

    //修改构造
    public LessonUserResultEntry(
            ObjectId id,
            ObjectId lessonId,
            ObjectId userId,
            String userName,
            int score
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append(Constant.ID,id)
                .append("lid", lessonId)
                .append("uid", userId)
                .append("unm", userName)
                .append("sco",score)
                .append("isr", 0);
        setBaseEntry(dbObject);
    }

    public ObjectId getUserId(){
        return getSimpleObjecIDValue("uid");
    }

    public void setUserId(ObjectId userId){
        setSimpleValue("uid", userId);
    }

    public ObjectId getLessonId(){
        return getSimpleObjecIDValue("lid");
    }

    public void setLessonId(ObjectId lessonId){
        setSimpleValue("lid",lessonId);
    }

    public String getUserName(){
        return getSimpleStringValue("unm");
    }

    public void setUserName(String userName){
        setSimpleValue("unm",userName);
    }

    public int getScore(){
        return getSimpleIntegerValue("sco");
    }

    public void setScore(int score){
        setSimpleValue("sco",score);
    }


    public int getIsRemove(){
        return getSimpleIntegerValue("isr");
    }

    public void setIsRemove(int isRemove){
        setSimpleValue("isr",isRemove);
    }

}
