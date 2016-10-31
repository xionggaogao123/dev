package com.fulaan.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "project_log")
public class ProjectLog implements Serializable{

	private static final long serialVersionUID = 2978180762855906225L;

	private Integer id;
	
	private Project project;
	
	private String logInfo;
	
	private Staff createdUser;
	
	private Date createdTime;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false, unique = true)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Project.class)
	@JoinColumn(name = "prj_id")
	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	@Column(name = "log_info")
	public String getLogInfo() {
		return logInfo;
	}

	public void setLogInfo(String logInfo) {
		this.logInfo = logInfo;
	}

	@ManyToOne(targetEntity = Staff.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "created_user_id")
	public Staff getCreatedUser() {
		return createdUser;
	}
	
	public void setCreatedUser(Staff createdUser) {
		this.createdUser = createdUser;
	}
	
	@Column(name = "created_time")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
	
}
