package com.pojo.itempool;

import com.sys.utils.DateTimeUtils;

/**
 * 老师组卷
 * @author fourer
 *
 */
public class TestPaperDTO {

	private String id;
	private String name;
	private String subject;
	private int state;
	private String createTime;
	
	public TestPaperDTO(TestPaperEntry e)
	{
		this.id=e.getID().toString();
		this.name=e.getName();
		//SubjectType type =SubjectType.getSubjectType(e.getSubject());
		//this.subject =type.getName();
	
		this.state=e.getState();
		this.createTime=DateTimeUtils.convert(e.getID().getTime(),DateTimeUtils.DATE_YYYY_MM_DD_HH_MM);
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
