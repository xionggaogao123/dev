package com.fulaan.dto;

public class StaffDto {
	
	private Integer id;
	
	private String jobNumber;
	
	private String name;
	
	private String gender;
	
	private String department;
	
	private String jobTitle;

	public StaffDto() { }
	
	public StaffDto(Integer id, String jobNumber, String name, String gender, String department, String jobTitle) {
		super();
		this.id = id;
		this.jobNumber = jobNumber;
		this.name = name;
		this.gender = gender;
		this.department = department;
		this.jobTitle = jobTitle;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getJobNumber() {
		return jobNumber;
	}

	public void setJobNumber(String jobNumber) {
		this.jobNumber = jobNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}
	
}
