package com.pojo.backstage;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by James on 2017/11/27.
 * id
 * name
 * version
 * url
 *type   0   freeme系统   1  sugar系统       6 全部
 */
public class JxmAppVersionEntry extends BaseDBObject{


    public JxmAppVersionEntry(){


    }

    public JxmAppVersionEntry(BasicDBObject baseEntry){
        super(baseEntry);
    }
    //添加构造
    public JxmAppVersionEntry(
            String name,
            String fileUrl,
            String version,
            int versionCode,
            int type
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append("nam", name)
                .append("url", fileUrl)
                .append("ver",version)
                .append("vco",versionCode)
                .append("typ",type)
                .append("isr", 0);
        setBaseEntry(dbObject);
    }

    //修改构造
    public JxmAppVersionEntry(
            ObjectId id,
            String name,
            String fileUrl,
            String version,
            int versionCode,
            int type
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append(Constant.ID,id)
                .append("nam", name)
                .append("url", fileUrl)
                .append("ver",version)
                .append("vco",versionCode)
                .append("typ",type)
                .append("isr", 0);
        setBaseEntry(dbObject);
    }



    public String getName(){
        return getSimpleStringValue("nam");
    }

    public void setName(String name){
        setSimpleValue("nam",name);
    }
    public String getFileUrl(){
        return getSimpleStringValue("url");
    }

    public void setFileUrl(String fileUrl){
        setSimpleValue("url",fileUrl);
    }
    public String getVersion(){
        return getSimpleStringValue("ver");
    }

    public void setVersion(String version){
        setSimpleValue("ver",version);
    }


    public int getVersionCode(){
        return getSimpleIntegerValueDef("vco",0);
    }

    public void setVersionCode(int versionCode){
        setSimpleValue("vco",versionCode);
    }

    public int getIsRemove(){
        return getSimpleIntegerValue("isr");
    }

    public void setIsRemove(int isRemove){
        setSimpleValue("isr",isRemove);
    }

    public int getType(){
        return getSimpleIntegerValueDef("typ",0);
    }

    public void setType(int type){
        setSimpleValue("typ",type);
    }
}
