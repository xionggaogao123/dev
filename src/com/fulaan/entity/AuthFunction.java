package com.fulaan.entity;

import java.io.Serializable;
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
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name = "auth_function")
public class AuthFunction implements Serializable{

	private static final long serialVersionUID = -450401441178403983L;

	private Integer id;
	
	// 权限名称
	private String functionName;
	
	// 权限标识
	private String functionFlag;
	
	private List<AuthRole> roleList;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false, unique = true)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "func_name")
	public String getFunctionName() {
		return functionName;
	}

	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}

	@Column(name = "func_flag")
	public String getFunctionFlag() {
		return functionFlag;
	}

	public void setFunctionFlag(String functionFlag) {
		this.functionFlag = functionFlag;
	}

	@ManyToMany(fetch = FetchType.LAZY, targetEntity = AuthRole.class)
	@JoinTable(name = "auth_role_function", joinColumns = {@JoinColumn(name = "func_id")}, inverseJoinColumns = {@JoinColumn(name = "role_id")})
	@Fetch(FetchMode.SUBSELECT)
	@OrderBy(value = "id")
	public List<AuthRole> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<AuthRole> roleList) {
		this.roleList = roleList;
	}
	
}
