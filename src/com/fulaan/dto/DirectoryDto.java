package com.fulaan.dto;

public class DirectoryDto {
	
	private int id;
	
	// 目录名称
	private String name;
	
	// 父目录id
	private int pdirId;

	public DirectoryDto() {}
	
	public DirectoryDto(int id, String name, int pdirId) {
		super();
		this.id = id;
		this.name = name;
		this.pdirId = pdirId;
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

	public int getPdirId() {
		return pdirId;
	}

	public void setPdirId(int pdirId) {
		this.pdirId = pdirId;
	}
	
}
