package com.fulaan.dto;

public class ProjectFileDto {

	private int id;
	
	// 文件名称
	private String name;
	
	// 文件类型
	private int fileType;
	
	// 父目录id
	private int pdirId;

	public ProjectFileDto(int id, String name, int fileType, int pdirId) {
		super();
		this.id = id;
		this.name = name;
		this.fileType = fileType;
		this.pdirId = pdirId;
	}

	public ProjectFileDto() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getFileType() {
		return fileType;
	}

	public void setFileType(int fileType) {
		this.fileType = fileType;
	}

	public int getPdirId() {
		return pdirId;
	}

	public void setPdirId(int pdirId) {
		this.pdirId = pdirId;
	}
	
}
