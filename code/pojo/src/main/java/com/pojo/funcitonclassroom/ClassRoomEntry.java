package com.pojo.funcitonclassroom;

import com.mongodb.BasicDBList;
import com.pojo.utils.MongoUtils;
import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;

import java.util.ArrayList;
import java.util.List;

/** 
 * @author chengwei@ycode.cn
 * @version 2015年12月21日 上午11:06:16 
 * 类说明 
 * 
 * 教室Entry
 * 教室id: _id
 * 学校id: sid(schoolId)
 * 序号:   num(number)
 * 创建时间: ct(createTime)
 * 教室名称: cn(classRoomName)
 * 管理者id: aid(administratorId)
 * 管理者名: an(administratorName)
 * 是否删除: ir(isRemove, 0为未删除，1为已删除)
 * 
 */
public class ClassRoomEntry extends BaseDBObject{
	//构造函数
	public ClassRoomEntry(){
		super();
	}
	
	public ClassRoomEntry(BasicDBObject baseEntry){
		super(baseEntry);
	}
	
	public ClassRoomEntry(ObjectId schoolId, int number, String classRoomName, 
			List<ObjectId> userIds){
		super();
		
		BasicDBObject baseEntry = new BasicDBObject()
		.append("num", number)
		.append("ct", System.currentTimeMillis())
		.append("cn", classRoomName)
		.append("ir", Constant.ZERO)
		.append("sid", schoolId)
		.append("aids", MongoUtils.convert(userIds));
		
		setBaseEntry(baseEntry);
	}
	
	//get/set方法
	public ObjectId getSchoolId(){
		return getSimpleObjecIDValue("sid");
	}
	
	public void setSchoolId(ObjectId schoolId){
		setSimpleValue("sid", schoolId);
	}
	
	public int getNumber(){
		return getSimpleIntegerValue("num");
	}
	
	public void setNumber(int number){
		setSimpleValue("num", number);
	}
	
	public String getClassRoomName(){
		return getSimpleStringValue("cn");
	}
	
	public void setClassRoomName(String classRoomName){
		setSimpleValue("cn", classRoomName);
	}

	public List<ObjectId> getUserIds() {
		List<ObjectId> retList =new ArrayList<ObjectId>();
		BasicDBList list =(BasicDBList)getSimpleObjectValue("aids");
		if(null!=list && !list.isEmpty())
		{
			for(Object o:list)
			{
				retList.add((ObjectId)o);
			}
		}
		return retList;
	}
	public void setUserIds(List<ObjectId> userIds) {
		setSimpleValue("aids", MongoUtils.convert(userIds));
	}
	
//	public ObjectId getAdministratorId(){
//		return getSimpleObjecIDValue("aid");
//	}
//
//	public void setAdministratorId(ObjectId administratorId){
//		setSimpleValue("aid", administratorId);
//	}
//
//	public String getAdministratorName(){
//		return getSimpleStringValue("an");
//	}
//
//	public void setAdministratorName(String administratorName){
//		setSimpleValue("an", administratorName);
//	}

}





