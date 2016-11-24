package com.pojo.exam;

import java.io.Serializable;

import com.sys.constants.Constant;


/**
 * 学生考试结果
 * @author fourer
 *
 */
public class StudentExamResDTO implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7634518118240266088L;
	
	private String userId;
	private String userName;
	private Double score;
	private String time;
	private int isGoods;
	//是否已经批改过
	private int isHandled=Constant.ONE; //1已经处理过 0没有
	
	
	
	
	
	public Double getScore() {
		if(this.score<=Constant.DEFAULT_VALUE_DOUBLE)
		{
			return Constant.DEFAULT_VALUE_DOUBLE;
		}
		return score;
	}
	public void setScore(Double score) {
		this.score = score;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public int getIsGoods() {
		return isGoods;
	}
	public void setIsGoods(int isGoods) {
		this.isGoods = isGoods;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public int getIsHandled() {
		return isHandled;
	}
	public void setIsHandled(int isHandled) {
		this.isHandled = isHandled;
	}

}
