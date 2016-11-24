/**   
* @Title: SalaryItemType.java 
* @Package com.pojo.salary 
* @Description: TODO(用一句话描述该文件是做什么的) 
* @author yang.ling   
* @date 2015年7月18日 下午9:38:06 
* @version V1.0   
* @Copyright ycode Co.,Ltd.
*/ 
package com.pojo.salary;

/** 
 * @ClassName: SalaryItemType 
 * @Description: 工资项目类型，扣发款
 * @author yang.ling
 * @date 2015年7月18日 下午9:38:07 
 *  
 */
public enum SalaryItemType {
	
	PLUS(1, "发款"),
	MINUS(2, "扣款");
	
	private int type;
	private String des;
	
	
	private SalaryItemType(int type, String des) {
		this.type = type;
		this.des = des;
	}


	public int getType() {
		return type;
	}


	public void setType(int type) {
		this.type = type;
	}


	public String getDes() {
		return des;
	}


	public void setDes(String des) {
		this.des = des;
	}
}
