package com.pojo.lesson;

/**
 * 课程类型
 * @author fourer
 *
 */
public enum LessonType {

    UNKWON(0,"未知"),
	BACKUP_LESSON(1,"备课空间课程"),
	CLASS_LESSON(2,"班级课程"),
	SCHOOL_LESSON(3,"校本资源课程"),
	UNION_LESSON(4,"联盟资源课程"),
    MLESSONVOTE_LESSON(5,"微课评比课程"),
	EMARKET_LESSON(6,"电子超市课程"),
	;
	
	
	private int type;
	private String des;
	
	
	private LessonType(int type, String des) {
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
	
	
	public static LessonType getLessonType(int type)
	{
		for(LessonType lt :LessonType.values())
		{
			if(lt.getType()==type)
			{
				return lt;
			}
		}
		
		return null;
	}
	
}
