package com.pojo.itempool;

import org.bson.types.ObjectId;

import com.sys.constants.Constant;
import com.sys.utils.RespObj;
import com.sys.utils.ValidationUtils;

/**
 * 
 * 老师自动创建试卷参数包装类
 * @author fourer
 *
 */
public class TestPaperPTO {

	private String name;
	private String grade;
	private String subject;
	private Integer level;
	private Integer time;
	private String kws;
	private Integer ch;
	private Integer tf;
	private Integer gap;
	private Integer sub;
	

	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	public Integer getTime() {
		return time;
	}
	public void setTime(Integer time) {
		this.time = time;
	}
	public String getKws() {
		return kws;
	}
	public void setKws(String kws) {
		this.kws = kws;
	}
	public Integer getCh() {
		return ch;
	}
	public void setCh(Integer ch) {
		this.ch = ch;
	}
	public Integer getTf() {
		return tf;
	}
	public void setTf(Integer tf) {
		this.tf = tf;
	}
	public Integer getGap() {
		return gap;
	}
	public void setGap(Integer gap) {
		this.gap = gap;
	}
	public Integer getSub() {
		return sub;
	}
	public void setSub(Integer sub) {
		this.sub = sub;
	}
	
	
	
	
	public RespObj validate()
	{
        RespObj obj= new RespObj(Constant.FAILD_CODE);
		
		if(!ValidationUtils.isRequestStudentExerciseName(name))
		{
			obj.setMessage("名称不合法");
			return obj;
		}
		
		
        if(!ObjectId.isValid(this.subject))
        {
        	obj.setMessage("科目错误");
			return obj;
        }
        
        if(!ObjectId.isValid(this.getGrade()))
        {
        	obj.setMessage("学段错误");
			return obj;
        }
		
		
		
		int total=ch+tf+gap+sub;
		if( total>Constant.HUNDRED )
		{
			obj.setMessage("总题目数应该小于100");
			return obj;
		}
		
		return RespObj.SUCCESS;
		
	}
	
	
	
	
	
	
	
}
