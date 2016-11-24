package com.pojo.guard;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;

/** 
 * @author chengwei@ycode.cn
 * @version 2015年12月8日 上午11:27:17 
 * 类说明 
 * 
 * 来访登记Entry类
 * 姓名: vnam(visitorName)
 * 性别: gd(gender 0位女,1为男)
 * 单位: cp(company)
 * 证件: pp(papers)
 * 联系电话: te(telephone)
 * 时间: vt(visitTime)
 * 人数:nop(numOfPeople)
 * 部门: dpt(department)
 * 对象: obj(object)
 * 事由: rs(reasons)
 * 是否删除: ir(isRemove 0为未删除，1为已删除)
 */
public class VisitorEntry extends BaseDBObject{
	/**
	 * 构造方法
	 * @param basicDBObject
	 */
	public VisitorEntry(BasicDBObject basicDBObject){
		super(basicDBObject);
	}
	
	public VisitorEntry(String visitorName, Integer gender, String company, String papers, String telephone,
			String visitTime,String numOfPeople, String department, String object, String reasons){
		super();
		
		BasicDBObject baseEntry = new BasicDBObject()
		.append("vnam", visitorName)
		.append("gd", gender)
		.append("cp", company)
		.append("pp", papers)
		.append("te", telephone)
		.append("vt", visitTime)
		.append("nop", numOfPeople)
		.append("dpt", department)
		.append("obj", object)
		.append("rs", reasons)
		.append("ir", Constant.ZERO);
		
		setBaseEntry(baseEntry);
	}
	
	/**
	 * get/set方法
	 * @return
	 */
	public String getVisitorName(){
		return getSimpleStringValue("vnam");
	}
	
	public void setVisitorName(String visitorName){
		setSimpleValue("vnam", visitorName);
	}
	
	public Integer getGender(){
		return getSimpleIntegerValue("gd");
	}
	
	public void setGener(Integer gender){
		setSimpleValue("gd", gender);
	}
	
	public String getCompany(){
		return getSimpleStringValue("cp");
	}
	
	public void setCompany(String company){
		setSimpleValue("cp", company);
	}
	
	public String getPapers(){
		return getSimpleStringValue("pp");
	}
	
	public void setPapers(String papers){
		setSimpleValue("pp", papers);
	}
	
	public String getTelephone(){
		return getSimpleStringValue("te");
	}
	
	public void setTelephone(String telephone){
		setSimpleValue("te", telephone);
	}
	
	public String getVisitTime(){
		return getSimpleStringValue("vt");
	}
	
	public void setVisitTime(String visitTime){
		setSimpleValue("vt", visitTime);
	}
	
	public String getNumOfPeople(){
		return getSimpleStringValue("nop");
	}
	
	public void setNumOfPeople(String numOfPeople){
		setSimpleValue("nop", numOfPeople);
	}
	
	public String getDepartment(){
		return getSimpleStringValue("dpt");
	}
	
	public void setDepartment(String department){
		setSimpleValue("dpt", department);
	}
	
	public String getObject(){
		return getSimpleStringValue("obj");
	}
	
	public void setObject(String object){
		setSimpleValue("obj", object);
	}
	
	public String getReasons(){
		return getSimpleStringValue("rs");
	}
	
	public void setReasons(String reasons){
		setSimpleValue("rs", reasons);
	}
	
	public Integer getIsRemove(){
		return getSimpleIntegerValue("ir");
	}
	
	public void setIsRemove(Integer isRemove){
		setSimpleValue("ir", isRemove);
	}
	
	
}