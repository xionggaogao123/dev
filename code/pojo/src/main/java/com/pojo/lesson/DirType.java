package com.pojo.lesson;

/**
 * 目录类型
 * @author fourer
 *
 */
public enum DirType {
   //1备课空间 2 班级课程 3校本资源 4联盟资源 5微课评比 6电子超市
	BACK_UP(1,"备课空间"),
	CLASS_LESSON(2,"班级课程"),
	SCHOOL_RESOURCE(3,"校本资源"),
	UNION_RESOURCE(4,"联盟资源"),
	MICRO_LESSON(5,"微课评比"),
	EMARKET(6,"电子超市"),
	//下面为党建的目录类型
	LEARNING_CENTRE(7,"学习天地"),
	BUILD_CIVILIZATION(8, "文明创建"),
	TOPICS_EDUCATION(9, "专题教育"),
	VOLUNTARY_SERVICE(10, "志愿服务"),
	PARTY_COURSE(11, "党课活动"),
	TEAM_BUILDING(12, "队伍建设"),
	CO_CONSTRUCTION(13, "共建活动"),
	MEMBER_MIEN(14, "党员风采"),
	WORK_ARRANGEMENT(15, "工作布置"),
	ORGNIZATION_LIFE(16, "组织生活"),
	;
	
	
	private int type;
	private String des;
	
	
	private DirType(int type, String des) {
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
	
	
	public static DirType getDirType(int type)
	{
		for(DirType dy:DirType.values())
		{
			if(dy.getType()==type)
			{
				return dy;
			}
		}
		return null;
	}
	
}
