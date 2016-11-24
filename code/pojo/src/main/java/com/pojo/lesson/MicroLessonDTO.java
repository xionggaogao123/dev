package com.pojo.lesson;

import com.sys.constants.Constant;

public class MicroLessonDTO extends LessonDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8952839750401257689L;
	
	private String userName=Constant.EMPTY;
	private String schoolName=Constant.EMPTY;
	
	
	
	public MicroLessonDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public MicroLessonDTO(LessonEntry e) {
		super(e);
		// TODO Auto-generated constructor stub
	}
	
	
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getSchoolName() {
		return schoolName;
	}
	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}
	
	

}
