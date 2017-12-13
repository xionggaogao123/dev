package com.pojo.questionbook;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by James on 2017/12/12.
 * id\
 * userId             用户id                uid
 * title             标题                   tit
 * createTime        创建时间               ctm
 * List<QuestionWebSizeEntry>   题目列表    qlt
 * count              题目总数              cou
 */
public class QuestionWebTestEntry extends BaseDBObject {

    public QuestionWebTestEntry(){

    }

    public QuestionWebTestEntry(BasicDBObject dbObject){
        super(dbObject);
    }

    //添加构造
    public QuestionWebTestEntry(
            String title,
            ObjectId userId,
            List<QuestionWebSizeEntry> sizeList,
            int  count
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append("tit", title)
                .append("uid",userId)
                .append("qlt", MongoUtils.fetchDBObjectList(sizeList))
                .append("cou",count)
                .append("ctm", new Date().getTime())
                .append("isr", 0);
        setBaseEntry(dbObject);
    }

    //修改构造
    public QuestionWebTestEntry(
            ObjectId id,
            String title,
            ObjectId userId,
            List<QuestionWebSizeEntry> sizeList,
            int  count
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append(Constant.ID,id)
                .append("tit", title)
                .append("uid", userId)
                .append("qlt", MongoUtils.fetchDBObjectList(sizeList))
                .append("cou",count)
                .append("ctm", new Date().getTime())
                .append("isr", 0);
        setBaseEntry(dbObject);
    }
    public ObjectId getUserId(){
        return getSimpleObjecIDValue("uid");
    }

    public void setUserId(ObjectId userId){
        setSimpleValue("uid",userId);
    }
    public String getTitle(){
        return getSimpleStringValue("tit");
    }

    public void setTitle(String title){
        setSimpleValue("tit",title);
    }

    public int getIsRemove(){
        return getSimpleIntegerValue("isr");
    }

    public void setIsRemove(int isRemove){
        setSimpleValue("isr",isRemove);
    }
    public void setSizeList( List<QuestionWebSizeEntry> sizeList){
        setSimpleValue("qlt", MongoUtils.fetchDBObjectList(sizeList));
    }

    public List<QuestionWebSizeEntry> getSizeList() {
        BasicDBList list = getDbList("qlt");
        List<QuestionWebSizeEntry> videoEntries = new ArrayList<QuestionWebSizeEntry>();
        for (Object dbo : list) {
            BasicDBObject dbObject = (BasicDBObject) dbo;
            videoEntries.add(new QuestionWebSizeEntry(dbObject));
        }
        return videoEntries;
    }

    public long getCreateTime(){
        return getSimpleLongValue("ctm");
    }
    public void setCreateTime(long createTime){
        setSimpleValue("ctm",createTime);
    }

    public int getCount(){
        return getSimpleIntegerValue("cou");
    }

    public void setCount(int count){
        setSimpleValue("cou",count);
    }

}
