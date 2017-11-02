package com.pojo.smalllesson;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by James on 2017/9/27.
 * id
 * lessonId      课程id         lid
 * userName      答题姓名       unm
 * userId        用户id         uid
 * number        次数           num
 * time          用时           tim
 * isTrue        是否正确       ist  0 未答  1 错  2 对
 * answer        答案           ans
 *
 */
public class  LessonAnswerEntry extends BaseDBObject {
    public LessonAnswerEntry(){

    }
    public LessonAnswerEntry(BasicDBObject baseEntry){
        super(baseEntry);
    }
    //添加构造
    public LessonAnswerEntry(
            ObjectId lessonId,
            ObjectId userId,
            String userName,
            int number,
            long time,
            int isTrue,
            String answer
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append("lid", lessonId)
                .append("uid", userId)
                .append("unm", userName)
                .append("num",number)
                .append("tim",time)
                .append("ist",isTrue)
                .append("ans", answer)
                .append("isr", 0);
        setBaseEntry(dbObject);
    }

    //修改构造
    public LessonAnswerEntry(
            ObjectId id,
            ObjectId lessonId,
            ObjectId userId,
            String userName,
            int number,
            long time,
            int isTrue,
            String answer
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append(Constant.ID,id)
                .append("lid", lessonId)
                .append("uid", userId)
                .append("unm", userName)
                .append("num",number)
                .append("tim",time)
                .append("ist", isTrue)
                .append("ans", answer)
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
    public String getAnswer(){
        return getSimpleStringValue("ans");
    }

    public void setAnswer(String answer){
        setSimpleValue("ans",answer);
    }

    public int getNumber(){
        return getSimpleIntegerValue("num");
    }

    public void setNumber(int number){
        setSimpleValue("num",number);
    }
    public int getTime(){
        return getSimpleIntegerValue("tim");
    }

    public void setTime(int time){
        setSimpleValue("tim",time);
    }
    public int getIsTrue(){
        return getSimpleIntegerValue("ist");
    }

    public void setIsTrue(int isTrue){
        setSimpleValue("ist",isTrue);
    }


    public int getIsRemove(){
        return getSimpleIntegerValue("isr");
    }

    public void setIsRemove(int isRemove){
        setSimpleValue("isr",isRemove);
    }
}
