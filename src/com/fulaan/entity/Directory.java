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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.core.annotation.Order;

/**
 * 文档目录entity
 * @author xusy
 */
@Entity
@Table(name = "directory")
public class Directory implements Serializable{

	private static final long serialVersionUID = -4173185791044195972L;

	private Integer id;
	
	// 目录名称
	private String name;
	
	// 父目录
	private Directory parentDir;
	
	// 子目录
	private List<Directory> childDirs;
	
	// 目录下文件
	private List<ProjectFile> files;

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

	@ManyToOne(targetEntity = Directory.class, fetch = FetchType.EAGER)
	@JoinColumn(name = "pid")
	public Directory getParentDir() {
		return parentDir;
	}

	public void setParentDir(Directory parentDir) {
		this.parentDir = parentDir;
	}

	@OneToMany(targetEntity = Directory.class, fetch = FetchType.EAGER, mappedBy = "parentDir")
	@Fetch(FetchMode.SUBSELECT)
	@OrderBy("name")
	public List<Directory> getChildDirs() {
		return childDirs;
	}

	public void setChildDirs(List<Directory> childDirs) {
		this.childDirs = childDirs;
	}

	@OneToMany(targetEntity = ProjectFile.class, fetch = FetchType.EAGER, mappedBy = "parentDir")
	@Fetch(FetchMode.SUBSELECT)
	public List<ProjectFile> getFiles() {
		return files;
	}

	public void setFiles(List<ProjectFile> files) {
		this.files = files;
	}
	
}
