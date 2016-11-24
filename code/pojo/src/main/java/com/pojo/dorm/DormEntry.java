package com.pojo.dorm;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;

/**
 * 学生宿舍表
 * @author caotiecheng 2015/12/8
 * 对应数据库集合 Constant.COLLECTION_DORM="dorm"
 * 生成时间 ： ct (createTime)long
 * 学校id:		scid(schoolId)
 * 宿舍名称：dmn(dormName)
 * 床位数量：bdn(bedNumber)
 * 宿舍电话：dp(dormPhone)
 * 设备配置：equ(equipment)
 * 宿舍区id：daid
 * 宿舍楼id：dbid
 * 宿舍层id：dfid(dormFloorId)
 * 删除标志位 : ir(isRemoved,0为未删除，1为已删除)
 * 宿舍学生：slis(studentList)
 */

public class DormEntry extends BaseDBObject{
	public DormEntry(BasicDBObject baseEntry){
		super(baseEntry);
	};

	public DormEntry(ObjectId schoolId,ObjectId dormAreaId,ObjectId dormBuildingId,ObjectId dormFloorId,
			String dormName,int bedNumber,String dormPhone, String equipment){

		super();
		 BasicDBObject baseEntry = new BasicDBObject()
			 .append("daid", dormAreaId)
			 .append("dbid", dormBuildingId)
			 .append("dfid",dormFloorId)
			 .append("dmn",dormName)
			 .append("bdn",bedNumber)
			 .append("dp",dormPhone)
			 .append("equ",equipment)
			 .append("scid", schoolId)
			 .append("ir", Constant.ZERO)
			 .append("ct", System.currentTimeMillis());
		 setBaseEntry(baseEntry);
	}

	public ObjectId getDormAreaId() {
		return getSimpleObjecIDValue("daid");
	}
	public void setDormAreaId(ObjectId dormAreaId) {
		setSimpleValue("daid", dormAreaId);
	}
	
	public ObjectId getDormBuildingId() {
		return getSimpleObjecIDValue("dbid");
	}
	public void setDormBuildingId(ObjectId dormBuildingId) {
		setSimpleValue("dbid", dormBuildingId);
	}
	
	public ObjectId getDormFloorId() {
		return getSimpleObjecIDValue("dfid");
	}
	public void setDormFloorId(ObjectId dormFloorId) {
		setSimpleValue("dfid", dormFloorId);
	}

	public String getDormName(){
		return getSimpleStringValue("dmn");
	}
	public void setDormName(String dormName){
		setSimpleValue("dmn",dormName);
	}
	
	public int getBedNumber(){
		return getSimpleIntegerValue("bdn");
	}
	public void setBedNumber(int bedNumber){
		setSimpleValue("bdn",bedNumber);
	}
	
	public String getDormPhone(){
		return getSimpleStringValue("dp");
	}
	public void setDormPhone(String dormPhone){
		setSimpleValue("dp",dormPhone);
	}
	
	public String getEquipment(){
		return getSimpleStringValue("equ");
	}
	public void setEquipment(String equipment){
		setSimpleValue("equ",equipment);
	}
	
	public ObjectId getSchoolId() {
		return getSimpleObjecIDValue("scid");
	}
	public void setSchoolId(ObjectId schoolId) {
		setSimpleValue("scid", schoolId);
	}
	
	public List<DormStudentEntry> getDormStudentList(){
		List<DormStudentEntry> resultList = new ArrayList<DormStudentEntry>();
		BasicDBList list =(BasicDBList)getSimpleObjectValue("slis");
		if(null!=list && !list.isEmpty())
		{
			for(Object o:list)
			{
				resultList.add(new DormStudentEntry((BasicDBObject)o));
			}
		}
		return resultList;
	}

	public void setDormStudentList(List<DormStudentEntry> dormStudentList){
		List<DBObject> list=MongoUtils.fetchDBObjectList(dormStudentList);
		setSimpleValue("slis",MongoUtils.convert(list));
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
}
