package com.pojo.dorm;

import java.util.Date;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;

/**
 * 
 * @author zhanghao@ycode.cn   2015-12-8
 *
 *宿舍楼Entry
 *collectionName:dormbuilding
 *宿舍区ID:aid(dormAreaId)
 *宿舍楼名字：bnm(dormBuildingName)
 *创建时间：ct(createTime)
 *类别:tp(type)(0宿舍区，1宿舍楼，2宿舍楼层)
 *删除标志位 : ir(isRemoved,0为未删除，1为已删除)
 */
public class DormBuildingEntry extends BaseDBObject {
	public DormBuildingEntry(BasicDBObject baseEntry){
		super(baseEntry);
	};
	public DormBuildingEntry(ObjectId dormAreaId,String dormBuildingName){
		super();
		
		BasicDBObject baseEntry =new BasicDBObject().append("aid", dormAreaId)
				.append("bnm", dormBuildingName)
				.append("ct", System.currentTimeMillis())
				.append("ir", Constant.ZERO)
				.append("tp", "宿舍楼");
		setBaseEntry(baseEntry);
	}
	
	public ObjectId getDormAreaId(){
		return  getSimpleObjecIDValue("aid");
	}
	public void setDormAreaId(ObjectId dormAreaId){
		setSimpleValue("aid",dormAreaId);
	}
	
	public String getDormBuildingName(){
		return getSimpleStringValue("bnm");
	}
	public void setDormBuildingName(String dormBuildingName){
		setSimpleValue("bnm",dormBuildingName);
	}
	
	//默认未删除
	public int getIsRemove() {
		if(getBaseEntry().containsField("ir"))
		{
			return getSimpleIntegerValue("ir");
		}
			return Constant.ZERO;
		}
	public void setIsRemove(int isRemove) {
		setSimpleValue("ir", isRemove);
	}
	
	public String getType() {
        return getSimpleStringValue("tp");
    }

    public void setType(String type) {
        setSimpleValue("tp", type);
    }
}
