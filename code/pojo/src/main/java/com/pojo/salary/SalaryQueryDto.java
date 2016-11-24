/**   
* @Title: SalaryQueryDto.java 
* @Package com.db.salary 
* @Description: TODO(用一句话描述该文件是做什么的) 
* @author yang.ling   
* @date 2015年7月18日 下午11:27:22 
* @version V1.0   
* @Copyright 版权所有@2005-2015 麦迪斯顿 Copyright Reserved 2005-2015 Medicalsystem Co.,Ltd.
*/ 
package com.pojo.salary;

import java.io.Serializable;

/** 
 * @ClassName: SalaryQueryDto 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author yang.ling
 * @date 2015年7月18日 下午11:27:22 
 *  
 */
public class SalaryQueryDto implements Serializable{


	private static final long serialVersionUID = -8715476301812157421L;
	
	private String userId;
	
	private int startYear;
	private int startMonth;
	
	private int endYear;
	private int endMonth;
	
	
	public int getStartYear() {
		return startYear;
	}
	public void setStartYear(int startYear) {
		this.startYear = startYear;
	}
	public int getStartMonth() {
		return startMonth;
	}
	public void setStartMonth(int startMonth) {
		this.startMonth = startMonth;
	}
	public int getEndYear() {
		return endYear;
	}
	public void setEndYear(int endYear) {
		this.endYear = endYear;
	}
	public int getEndMonth() {
		return endMonth;
	}
	public void setEndMonth(int endMonth) {
		this.endMonth = endMonth;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
}