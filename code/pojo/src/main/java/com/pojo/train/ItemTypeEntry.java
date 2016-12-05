package com.pojo.train;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

/**
 * 项目类型信息
 * <pre>
 * collectionName:regions
 * </pre>
 * <pre>
 * {
 *  lel: 1一级分类   2二级分类    3三级分类   4四级分类
 *  pid:父级分类代码,当lel=1时，pid为""
 *  nm:名称
 *  so:排序代码，仅仅用于同级分类排序
 * }
 * Created by guojing on 2016/10/11.
 */
public class ItemTypeEntry extends BaseDBObject {
    /**
     *
     */
    private static final long serialVersionUID = 3231758154243974662L;



    public ItemTypeEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public ItemTypeEntry(int level, ObjectId parentId, String name) {
        this(level,parentId,name,0);
    }

    public ItemTypeEntry(int level,ObjectId parentId,String name,int sort) {
        BasicDBObject dbo =new BasicDBObject()
                .append("lel", level)
                .append("pid", parentId)
                .append("nm", name)
                .append("so", sort)
                ;
        setBaseEntry(dbo);
    }


    public int getLevel() {
        return getSimpleIntegerValue("lel");
    }
    public void setLevel(int level) {
        setSimpleValue("lel", level);
    }
    //	public String getCode() {
//		return getSimpleStringValue("cd");
//	}
//	public void setCode(String code) {
//		setSimpleValue("cd", code);
//	}
    public ObjectId getParentId() {
        return getSimpleObjecIDValue("pid");
    }
    public void setParentId(ObjectId parentid) {
        setSimpleValue("pid", parentid);
    }
    public String getName() {
        return getSimpleStringValue("nm");
    }
    public void setName(String name) {
        setSimpleValue("nm", name);
    }
    public int getSort() {
        return getSimpleIntegerValue("so");
    }
    public void setSort(int sort) {
        setSimpleValue("so", sort);
    }
}
