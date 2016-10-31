package com.fulaan.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

/**
 * @author xusy 2016-9-22
 * 项目实体
 */
@Entity
@Table(name = "project")
public class Project implements Serializable{
	
	private static final long serialVersionUID = -6510549303151396090L;

	// 主键id
	private Integer id;
	
	// 项目编号
	private String projectNumber;
	
	// 项目名称
	private String projectName;
	
	// 项目描述
	private String projectDesc;
	
	// 项目负责人
	private Staff projectOwner;
	
	// 项目创建人
	private Staff projectCreater;
	
	// 项目文档存放根目录
	private String docsPath;
	
	// 项目开始日期
	private Date startDate;
	
	// 项目结束日期
	private Date endDate;
	
	// 项目创建日期
	private Date createdTime;
	
	// 更新时间
	private Date updateTime;

	// 项目成员
	private List<Staff> members;
	
	// 项目日志
	private List<ProjectLog> logs;
	
	@Id
	@Column(name = "id", nullable = false, unique = true)
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "project_number", length = 50, nullable = false)
	public String getProjectNumber() {
		return projectNumber;
	}

	public void setProjectNumber(String projectNumber) {
		this.projectNumber = projectNumber;
	}

	@Column(name = "project_name", length = 255, nullable = false)
	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	@Column(name = "project_desc")
	public String getProjectDesc() {
		return projectDesc;
	}

	public void setProjectDesc(String projectDesc) {
		this.projectDesc = projectDesc;
	}

	@ManyToOne(targetEntity = Staff.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "project_owner_id")
	public Staff getProjectOwner() {
		return projectOwner;
	}

	public void setProjectOwner(Staff projectOwner) {
		this.projectOwner = projectOwner;
	}
	
	@ManyToOne(targetEntity = Staff.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "project_creater_id")
	public Staff getProjectCreater() {
		return projectCreater;
	}

	public void setProjectCreater(Staff projectCreater) {
		this.projectCreater = projectCreater;
	}

	@Column(name = "docs_path", length = 255)
	public String getDocsPath() {
		return docsPath;
	}

	public void setDocsPath(String docsPath) {
		this.docsPath = docsPath;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "start_date")
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "end_date")
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_time")
	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "update_time")
	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@ManyToMany(targetEntity = Staff.class, fetch = FetchType.LAZY)
	@JoinTable(name = "project_staff", joinColumns = {@JoinColumn(name = "project_id")}, inverseJoinColumns = {@JoinColumn(name = "staff_id")})
	@Fetch(FetchMode.SUBSELECT)
	@OrderBy("id")
	@NotFound(action = NotFoundAction.IGNORE)
	public List<Staff> getMembers() {
		return members;
	}

	public void setMembers(List<Staff> members) {
		this.members = members;
	}

	@OneToMany(mappedBy = "project", fetch = FetchType.LAZY)
	public List<ProjectLog> getLogs() {
		return logs;
	}

	public void setLogs(List<ProjectLog> logs) {
		this.logs = logs;
	}
	
}
