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
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

/**
 * @author xusy
 * 公司职员实体
 */
@Entity
@Table(name = "staff")
public class Staff implements Serializable{
	
	private static final long serialVersionUID = 6007378297788560517L;

	// 主键id
	private Integer id;
	
	// 工号
	private String jobNumber;
	
	// 姓名
	private String name;
	
	// 性别:0-女 	1-男
	private String gender;
	
	// 登录名
	private String loginName;
	
	// 密码
	private String password;
	
	// 部门
	private Department department;
	
	// 所属子部门
	private SubDepartment subDepartment;
	
	// 职称
	private String jobTitle;
	
	// 添加时间
	private Date addTime;
	
	// 更新时间
	private Date updateTime;

	// 参与的项目
	private List<Project> projects;
	
	// 是否删除  0：未删除 		1：删除
	private Integer isDeleted;
	
	// 是否为项目负责人
	/*
	 * 该字段与auth_role对应关系如下：
	 * 该字段值含义 "0": user角色
	 * "1": admin角色
	 * "2": superAdmin角色
	 * */
	private String isPrjOwner; 
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false, unique = true)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "job_number", length = 20)
	public String getJobNumber() {
		return jobNumber;
	}

	public void setJobNumber(String jobNumber) {
		this.jobNumber = jobNumber;
	}

	@Column(name = "name", length = 20)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "gender", length = 5)
	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	@Column(name = "login_name", length = 20, nullable = false)
	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	@Column(name = "password", length = 255, nullable = false)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@ManyToOne(targetEntity = Department.class, fetch = FetchType.EAGER)
	@JoinColumn(name = "department_id")
	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}
	
	@ManyToOne(targetEntity = SubDepartment.class, fetch = FetchType.EAGER)
	@JoinColumn(name = "sub_department_id")
	public SubDepartment getSubDepartment() {
		return subDepartment;
	}

	public void setSubDepartment(SubDepartment subDepartment) {
		this.subDepartment = subDepartment;
	}

	@Column(name = "job_title", length = 50)
	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	@Column(name = "add_time")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	@Column(name = "update_time")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@ManyToMany(targetEntity = Project.class, fetch = FetchType.EAGER)
	@JoinTable(name = "project_staff", joinColumns = {@JoinColumn(name = "staff_id")}, inverseJoinColumns = {@JoinColumn(name = "project_id")})
	@OrderBy("id")
	@Fetch(FetchMode.SUBSELECT)
	@NotFound(action = NotFoundAction.IGNORE)
	public List<Project> getProjects() {
		return projects;
	}

	public void setProjects(List<Project> projects) {
		this.projects = projects;
	}

	@Column(name = "is_prj_owner")
	public String getIsPrjOwner() {
		return isPrjOwner;
	}

	public void setIsPrjOwner(String isPrjOwner) {
		this.isPrjOwner = isPrjOwner;
	}

	@Column(name = "is_deleted")
	public Integer getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}
	
}
