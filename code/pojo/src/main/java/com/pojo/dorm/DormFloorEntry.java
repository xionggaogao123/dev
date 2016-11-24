package com.pojo.dorm;

import java.util.Date;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
/**
 * 
 * @author zhanghao@ycode.cn   2015-12-08
 * 
 *宿舍层Entry
 *collectionName:dormfloor
 *宿舍楼ID：bid(dormBuildingId)
 *宿舍层名字：fnm(dormFloorName)
 *创建时间：ct(createTime)
 *类别:tp(type)(0宿舍区，1宿舍楼，2宿舍楼层)
 *删除标志位 : ir(isRemoved,0为未删除，1为已删除)
 */
public class DormFloorEntry extends BaseDBObject {
	public DormFloorEntry(BasicDBObject baseEntry){
		super(baseEntry);
	};
	public DormFloorEntry(ObjectId dormBuildingId,String dormFloorName){
		super();
		
		BasicDBObject baseEntry =new BasicDBObject().append("bid", dormBuildingId)
				.append("fnm", dormFloorName)
				.append("ct", System.currentTimeMillis())
				.append("ir", Constant.ZERO)
				.append("tp", "宿舍层");
		setBaseEntry(baseEntry);
	}
	
	public ObjectId getDormBuildingId(){
		return getSimpleObjecIDValue("bid");
	}
	public void setDormBuildingId(ObjectId dormBuildingId){
		setSimpleValue("bid",dormBuildingId);
	}
	
	public String getdormFloorName(){
		return getSimpleStringValue("fnm");
	}
	public void setDormFloorName(String dormFloorName){
		setSimpleValue("fnm",dormFloorName);
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
