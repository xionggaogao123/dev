package com.fulaan.dto;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fulaan.entity.ProjectLog;

public class ProjectLogDto {

	private int id;
	
	private String createdUserName;
	
	private Date createdTime;
	
	private String createDate;
	
	private String logInfo;

	public ProjectLogDto() {}
	
	public ProjectLogDto(ProjectLog projectLog) {
		this.id = projectLog.getId();
		this.createdUserName = projectLog.getCreatedUser().getName();
		this.logInfo = projectLog.getLogInfo();
		this.createdTime = projectLog.getCreatedTime();
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		this.createDate = format.format(this.createdTime);
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCreatedUserName() {
		return createdUserName;
	}

	public void setCreatedUserName(String createdUserName) {
		this.createdUserName = createdUserName;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public String getLogInfo() {
		return logInfo;
	}

	public void setLogInfo(String logInfo) {
		this.logInfo = logInfo;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	
}
