package com.pojo.backstage;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.Date;

/**
 * Created by James on 2017/12/4.
 * 1 系统推送消息
 *  avatar      ava         头像
 *  name          nam         姓名
 *  title         tit         标题
 *  content       con         内容
 *  fileUrl      url         附件路径
 *  fileType     fty         附件类型   1 图片  2 语音  3 视屏 4 附件
 *  type         typ           类型           1 新手引导      2系统通知
 *  createTime   ctm          创建时间
 *  sourceId       sid          来源
 *  sourceType    sty          来源模块
 *  sourceName    snm           社区名称
 */
public class SystemMessageEntry extends BaseDBObject {

    public SystemMessageEntry(){

    }

    public SystemMessageEntry(BasicDBObject baseEntry){
        super(baseEntry);
    }
    //添加构造
    public SystemMessageEntry(
            String avatar,
            String name,
            String title,
            String content,
            String fileUrl,
            String sourceName,
            int fileType,
            int type,
            ObjectId sourceId,
            int sourceType

    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append("ava", avatar)
                .append("nam", name)
                .append("tit", title)
                .append("con", content)
                .append("url", fileUrl)
                .append("snm",sourceName)
                .append("fty", fileType)
                .append("typ", type)
                .append("sid", sourceId)
                .append("sty", sourceType)
                .append("ctm", new Date().getTime())
                .append("isr", 0);
        setBaseEntry(dbObject);
    }

    //修改构造
    public SystemMessageEntry(
            ObjectId id,
            String avatar,
            String name,
            String title,
            String content,
            String fileUrl,
            String sourceName,
            int fileType,
            int type,
            ObjectId sourceId,
            int sourceType

    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append(Constant.ID,id)
                .append("ava", avatar)
                .append("nam", name)
                .append("tit", title)
                .append("con", content)
                .append("url", fileUrl)
                .append("snm",sourceName)
                .append("fty", fileType)
                .append("typ", type)
                .append("sid", sourceId)
                .append("sty", sourceType)
                .append("ctm", new Date().getTime())
                .append("isr", 0);
        setBaseEntry(dbObject);
    }


    public ObjectId getSourceId(){
        return getSimpleObjecIDValue("sid");
    }
    public void setSourceId(ObjectId sourceId){
        setSimpleValue("sid",sourceId);
    }

    public String getAvatar(){
        return getSimpleStringValue("ava");
    }

    public void setAvatar(String avatar){
        setSimpleValue("ava",avatar);
    }
    public String getName(){
        return getSimpleStringValue("nam");
    }

    public void setName(String name){
        setSimpleValue("nam",name);
    }
    public String getTitle(){
        return getSimpleStringValue("tit");
    }

    public void setTitle(String title){
        setSimpleValue("tit",title);
    }
    public String getContent(){
        return getSimpleStringValue("con");
    }

    public void setContent(String content){
        setSimpleValue("con",content);
    }




    public String getFileUrl(){
        return getSimpleStringValue("url");
    }

    public void setFileUrl(String fileUrl){
        setSimpleValue("url",fileUrl);
    }


    public String getSourceName(){
        return getSimpleStringValue("snm");
    }

    public void setSourceName(String sourceName){
        setSimpleValue("snm",sourceName);
    }


    public int getType(){
        return getSimpleIntegerValue("typ");
    }

    public void setType(int type){
        setSimpleValue("typ",type);
    }

    public int getFileType(){
        return getSimpleIntegerValue("fty");
    }

    public void setFileType(int fileType){
        setSimpleValue("fty",fileType);
    }
    public int getSourceType(){
        return getSimpleIntegerValue("sty");
    }

    public void setSourceType(int sourceType){
        setSimpleValue("sty",sourceType);
    }

    public long getCreateTime(){
        return getSimpleLongValue("ctm");
    }
    public void setCreateTime(long createTime){
        setSimpleValue("ctm",createTime);
    }

    public int getIsRemove(){
        return getSimpleIntegerValue("isr");
    }

    public void setIsRemove(int isRemove){
        setSimpleValue("isr",isRemove);
    }

}
