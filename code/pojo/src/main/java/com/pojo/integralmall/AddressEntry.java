package com.pojo.integralmall;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;

/**
 * 
 * <简述>
 * <详细描述>
 * @author   yaojintao
 * @version  $Id$
 * @since
 * @see
 */
public class AddressEntry extends BaseDBObject{

    public AddressEntry() {
        
    }
    
    public AddressEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }
    
    public AddressEntry(String name,
                        String telphone,
                        String area,
                        String detail,
                        ObjectId userId) {
        BasicDBObject dbObject=new BasicDBObject()
            .append("name", name)
            .append("telphone", telphone)
            .append("area", area)
            .append("detail", detail)
            .append("uid", userId)
            .append("isr", Constant.ZERO);
        setBaseEntry(dbObject);
    }
    
    
    public String getName(){
        return getSimpleStringValue("name");
    }

    public void setName(String name){
        setSimpleValue("name",name);
    }
    
    public String getTelphone(){
        return getSimpleStringValue("telphone");
    }

    public void setTelphone(String telphone){
        setSimpleValue("telphone",telphone);
    }
    
    public String getArea(){
        return getSimpleStringValue("area");
    }

    public void setArea(String area){
        setSimpleValue("area",area);
    }
    
    public String getDetail(){
        return getSimpleStringValue("detail");
    }

    public void setDetail(String detail){
        setSimpleValue("detail",detail);
    }
    
    public ObjectId getUserId(){
        return getSimpleObjecIDValue("uid");
    }

    public void setUserId(ObjectId userId){
        setSimpleValue("uid",userId);
    }
    
    public int getIsRemove(){
        return getSimpleIntegerValue("isr");
    }

    public void setIsRemove(int isRemove){
        setSimpleValue("isr",isRemove);
    }
}
