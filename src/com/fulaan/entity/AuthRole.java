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
@Table(name = "auth_role")
public class AuthRole implements Serializable {
	
	private static final long serialVersionUID = 8568731912441048398L;

	private Integer id;
	
	// 角色名称
	private String name;
	
	private List<AuthFunction> functionList;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false, unique = true)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ManyToMany(targetEntity = AuthFunction.class, fetch = FetchType.EAGER)
	@JoinTable(name = "auth_role_function", joinColumns = {@JoinColumn(name = "role_id")}, inverseJoinColumns = {@JoinColumn(name = "func_id")})
	@Fetch(FetchMode.SUBSELECT)
	@OrderBy(value = "id")
	public List<AuthFunction> getFunctionList() {
		return functionList;
	}

	public void setFunctionList(List<AuthFunction> functionList) {
		this.functionList = functionList;
	}
	
}
