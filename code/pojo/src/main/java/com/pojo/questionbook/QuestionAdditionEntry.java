package com.pojo.questionbook;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.List;

/**
 * Created by James on 2017/9/30.
 *
 * Id
 parentId    					问题id				pid
 content						内容                con
 answerList                 	图片list             	anl
 answerType			    1 答案 2解析 3 解答			aty
 createTime                  创建时间                 ctm
 level                    阶段（1.转1   2转7  3转30） lev
 */
public class QuestionAdditionEntry  extends BaseDBObject {
    public QuestionAdditionEntry(){

    }
    public QuestionAdditionEntry(BasicDBObject baseEntry){
        super(baseEntry);
    }
    //添加构造
    public QuestionAdditionEntry(
            ObjectId parentId,
            String content,
            List<String> answerList,
            int answerType,
            int level
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append("pid", parentId)
                .append("con", content)
                .append("ctm", new Date().getTime())
                .append("anl", answerList)
                .append("aty", answerType)
                .append("lev",level)
                .append("isr", 0);
        setBaseEntry(dbObject);
    }

    //修改构造
    public QuestionAdditionEntry(
            ObjectId id,
            ObjectId parentId,
            String content,
            long createTime,
            List<String> answerList,
            int answerType,
            int level
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append(Constant.ID,id)
                .append("pid", parentId)
                .append("con", content)
                .append("ctm", createTime)
                .append("anl", answerList)
                .append("aty", answerType)
                .append("lev",level)
                .append("isr", 0);
        setBaseEntry(dbObject);
    }

    public ObjectId getParentId(){
        return getSimpleObjecIDValue("pid");
    }

    public void setParentId(ObjectId parentId){
        setSimpleValue("pid",parentId);
    }

    public int getAnswerType(){
        return getSimpleIntegerValue("aty");
    }

    public void setAnswerType(int answerType){
        setSimpleValue("aty",answerType);
    }

    public String getContent(){
        return getSimpleStringValue("con");
    }

    public void setContent(String content){
        setSimpleValue("con",content);
    }

    public long getCreateTime(){
        return getSimpleLongValue("ctm");
    }

    public void setCreateTime(long createTime){
        setSimpleValue("ctm",createTime);
    }
    public void setAnswerList(List<String> answerList){
        setSimpleValue("anl",answerList);
    }

    public List<String> getAnswerList(){
        @SuppressWarnings("rawtypes")
        List answerList =(List)getSimpleObjectValue("anl");
        return answerList;
    }
    public int getLevel(){
        return getSimpleIntegerValue("lev");
    }

    public void setLevel(int level){
        setSimpleValue("lev",level);
    }

    public int getIsRemove(){
        return getSimpleIntegerValue("isr");
    }

    public void setIsRemove(int isRemove){
        setSimpleValue("isr",isRemove);
    }
}
