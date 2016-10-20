package com.fulaan.common;

public class ProjectContent {

	/**
	 * 每页记录最大条数
	 */
	public static final int MAX_RESULT_PER_PAGE = 10;
	
	/**
	 * session中保存的登录用户属性名
	 */
	public static final String LOGIN_USER_IN_SESSION = "loginedStaff";
	
	/**
	 * 项目文档保存的根目录
	 */
	public static final String PROJECT_FILE_ROOT_PATH = "/prj_upload";
	
	/**
	 * superAdmin标识
	 */
	public static final String SUPERADMIN_FLAG = "SUPERADMIN";
	
	/**
	 * 删除标识
	 */
	public static final int DELETED_FLAG = 1;
	
	/**
	 * 未删除标识
	 */
	public static final int NOT_DELETED_FLAG = 0;
	
}
