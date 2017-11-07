package com.pojo.controlphone;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * (管控安全地址表)
 * Created by James on 2017/11/6.
 Id
 userId             用户id                    uid
 parentId            父id                     pid
 name           地址名称                    nam
 address          地址实际名称                add
 radii             半径                        rad
 coordinate         坐标                       coo
 isOpen            是否启用                   iso
 */
public class ControlSafeAddressEntry extends BaseDBObject {
    public ControlSafeAddressEntry(){

    }
    public ControlSafeAddressEntry(BasicDBObject object){
        super(object);
    }

    //添加构造
    public ControlSafeAddressEntry(
            ObjectId parentId,
            ObjectId userId,
            String name,
            String address,
            int radii,
            String coordinate,
            int isOpen
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append("pid", parentId)
                .append("uid", userId)
                .append("nam", name)
                .append("add", address)
                .append("rad", radii)
                .append("coo", coordinate)
                .append("iso", isOpen)
                .append("isr", 0);
        setBaseEntry(dbObject);
    }

    //修改构造
    public ControlSafeAddressEntry(
            ObjectId id,
            ObjectId parentId,
            ObjectId userId,
            String name,
            String address,
            int radii,
            String coordinate,
            int isOpen
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append(Constant.ID,id)
                .append("pid", parentId)
                .append("uid", userId)
                .append("nam", name)
                .append("add", address)
                .append("rad", radii)
                .append("coo", coordinate)
                .append("iso", isOpen)
                .append("isr", 0);
        setBaseEntry(dbObject);
    }

    public ObjectId getParentId(){
        return getSimpleObjecIDValue("pid");
    }
    public void setParentId(ObjectId parentId){
        setSimpleValue("pid",parentId);
    }
    public ObjectId getUserId(){
        return getSimpleObjecIDValue("uid");
    }
    public void setUserId(ObjectId userId){
        setSimpleValue("uid",userId);
    }

    public String getName(){
        return getSimpleStringValue("nam");
    }
    public void setName(String name){
        setSimpleValue("nam",name);
    }
    public String getAddress(){
        return getSimpleStringValue("add");
    }
    public void setAddress(String address){
        setSimpleValue("add",address);
    }
    public String getCoordinate(){
        return getSimpleStringValue("coo");
    }
    public void setCoordinate(String coordinate){
        setSimpleValue("coo",coordinate);
    }
    public int getIsOpen(){
        return getSimpleIntegerValue("iso");
    }
    public void setIsOpen(int isOpen){
        setSimpleValue("iso",isOpen);
    }
    public int getRadii(){
        return getSimpleIntegerValue("rad");
    }
    public void setRadii(int radii){
        setSimpleValue("rad",radii);
    }
    public int getIsRemove(){
        return getSimpleIntegerValue("isr");
    }

    public void setIsRemove(int isRemove){
        setSimpleValue("isr",isRemove);
    }
}
