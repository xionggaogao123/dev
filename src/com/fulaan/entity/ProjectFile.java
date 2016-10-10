package com.fulaan.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "file")
public class ProjectFile implements Serializable{

	private static final long serialVersionUID = 6511107232852787384L;

	private Integer id;
	
	private String fileName;
	
	private String filePath;
	
	private int fileType;
	
	private Directory parentDir;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false, unique = true)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "file_name")
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Column(name = "file_path")
	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	@Column(name = "file_type")
	public int getFileType() {
		return fileType;
	}

	public void setFileType(int fileType) {
		this.fileType = fileType;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "dir_id")
	public Directory getParentDir() {
		return parentDir;
	}

	public void setParentDir(Directory parentDir) {
		this.parentDir = parentDir;
	}
	
}
