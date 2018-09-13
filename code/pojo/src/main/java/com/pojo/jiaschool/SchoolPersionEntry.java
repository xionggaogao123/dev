package com.pojo.jiaschool;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by James on 2018-06-11.
 * 学校人员表（用来管理学校的相关人员）
 * id
 * userId             用户id                uid
 * schoolId           学校id                sid
 * name               校内称呼              nam
 * role               权限等级              rol     0 超级管理员   1 校级管理员    2 普通用户
 * type               身份类型              typ     1  老师    2   家长     3  学生
 *
 */
public class SchoolPersionEntry extends BaseDBObject {


    public SchoolPersionEntry(){

    }

    public SchoolPersionEntry(BasicDBObject object){
        super(object);
    }

    //添加构造
    public SchoolPersionEntry(
            ObjectId userId,
            ObjectId schoolId,
            String name,
            int role,
            int type
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append("uid", userId)
                .append("sid", schoolId)
                .append("nam", name)
                .append("rol", role)
                .append("typ", type)
                .append("isr", 0);
        setBaseEntry(dbObject);
    }

    //修改构造
    public SchoolPersionEntry(
            ObjectId id,
            ObjectId userId,
            ObjectId schoolId,
            String name,
            int role,
            int type
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append(Constant.ID, id)
                .append("uid", userId)
                .append("sid", schoolId)
                .append("nam", name)
                .append("rol", role)
                .append("typ", type)
                .append("isr", 0);
        setBaseEntry(dbObject);
    }

    //运营添加构造
    public SchoolPersionEntry(
            ObjectId userId,
            ObjectId schoolId,
            String name,
            int role,
            int type,
            String roleId
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append("uid", userId)
                .append("sid", schoolId)
                .append("nam", name)
                .append("rol", role)
                .append("typ", type)
                .append("roleId", roleId)
                .append("isr", 0);
        setBaseEntry(dbObject);
    }

    public ObjectId getUserId(){
        return getSimpleObjecIDValue("uid");
    }

    public void setUserId(ObjectId userId){
        setSimpleValue("uid",userId);
    }

    public ObjectId getSchoolId(){
        return getSimpleObjecIDValue("sid");
    }

    public void setSchoolId(ObjectId schoolId){
        setSimpleValue("sid",schoolId);
    }

    public String getName(){
        return getSimpleStringValue("nam");
    }

    public void setName(String name){
        setSimpleValue("nam",name);
    }

    public int getRole(){
        return getSimpleIntegerValue("rol");
    }
    public void setRole(int role){
        setSimpleValue("rol",role);
    }

    public int getType(){
        return getSimpleIntegerValue("typ");
    }

    public void setType(int type){
        setSimpleValue("typ",type);
    }

    public int getIsRemove(){
        return getSimpleIntegerValue("isr");
    }

    public void setIsRemove(int isRemove){
        setSimpleValue("isr",isRemove);
    }

    public String getRoleId(){
        return getSimpleStringValue("roleId");
    }

    public void setRoleId(String roleId){
        setSimpleValue("roleId",roleId);
    }

}
