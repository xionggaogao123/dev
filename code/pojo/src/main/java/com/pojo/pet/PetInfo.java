package com.pojo.pet;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

/**
 * Created by guojing on 2015/3/26.
 * 宠物信息
 *<pre>
 * {
 *  id :唯一性标示
 *  pid:宠物id
 *  pnm:宠物名称
 *  st:是否是选中宠物
 *  crt:创建时间,long
 *  ih:是否孵化
 *  upt:修改时间,long
 *
 * }
 * </pre>
 *
 * @author guojing
 */
public class PetInfo extends BaseDBObject {

    private static final long serialVersionUID = 7933557118492747487L;

    public PetInfo(BasicDBObject dbo){setBaseEntry(dbo);}

    /**
     * 构造器
     *
     */
    public PetInfo(int selecttype, long createdate,
                   int ishatch)
    {
        super();
        BasicDBObject dbo =new BasicDBObject()
                .append("id",new ObjectId())
                .append("pid", null)
                .append("pnm", null)
                .append("st", selecttype)
                .append("crt", createdate)
                .append("ih", ishatch)
                .append("upt", 0)
                ;
        setBaseEntry(dbo);
    }
    public PetInfo(ObjectId petid, String petname, int selecttype, long createdate, int ishatch,
                   long updatedate)
    {
        super();
        BasicDBObject dbo =new BasicDBObject()
                .append("id",new ObjectId())
                .append("pid", petid)
                .append("pnm", petname)
                .append("st", selecttype)
                .append("crt", createdate)
                .append("ih", ishatch)
                .append("upt", updatedate)
                ;
        setBaseEntry(dbo);
    }

    public PetInfo(ObjectId id, ObjectId petid, String petname, int selecttype, int ishatch,
                   long updatedate)
    {
        super();
        BasicDBObject dbo =new BasicDBObject()
                .append("id",id)
                .append("pid", petid)
                .append("pnm", petname)
                .append("st", selecttype)
                .append("ih", ishatch)
                .append("upt", updatedate)
                ;
        setBaseEntry(dbo);
    }

    public ObjectId getId() {
        return getSimpleObjecIDValue("id");
    }

    public void setId(ObjectId id) {
        setSimpleValue("id",id);
    }

    public ObjectId getPetid() {
        return getSimpleObjecIDValue("pid");
    }

    public void setPetid(ObjectId petid) {
        setSimpleValue("pid",petid);
    }

    public String getPetname() {
        return getSimpleStringValue("pnm");
    }

    public void setPetname(String petname) {
        setSimpleValue("pnm", petname);
    }

    public int getSelecttype() {
        return getSimpleIntegerValue("st");
    }

    public void setSelecttype(int selecttype) {
        setSimpleValue("st", selecttype);
    }

    public long getCreatedate() {
        return getSimpleLongValue("crt");
    }

    public void setCreatedate(long createdate) {
        setSimpleValue("crt",createdate);
    }

    public int getIshatch() {
        return getSimpleIntegerValue("ih");
    }

    public void setIshatch(int ishatch) {
        setSimpleValue("ih", ishatch);
    }

    public long getUpdatedate() {
        return getSimpleLongValue("upt");
    }

    public void setUpdatedate(long updatedate) {
        setSimpleValue("upt",updatedate);
    }
}
