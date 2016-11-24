package com.pojo.notice;

import java.util.ArrayList;
import java.util.List;

import com.pojo.app.IdNameValuePair;
import com.pojo.app.IdNameValuePairDTO;
import com.sys.utils.DateTimeUtils;

/**
 * 通知DTO
 * @author fourer
 *
 */
public class NoticeDTO {

	private String id;
	private String teacherId;
	private String teacherName;
	private String avator;
	private String title;
	private String content;
	private Long bt;
	private Long et;
	private List<IdNameValuePairDTO> voiceFile=new ArrayList<IdNameValuePairDTO>();
	private List<IdNameValuePairDTO> docFile=new ArrayList<IdNameValuePairDTO>();
	private String time;
	private int top;
	private int totalCount;
	private int alreadyCount;
	private int already=0; //1已经查看
	private int isSelfOper=0; //是不是自己在操作 0，不是 1是

	public NoticeDTO(NoticeEntry e)
	{
		this.id=e.getID().toString();
		this.title=e.getName();
		this.content=e.getContent();
		this.teacherId=e.getTeacherId().toString();
		try
		{
			 this.bt=e.getBeginTime();
			 this.et=e.getEndTime();
		}catch(Exception ex)
		{}
		for(IdNameValuePair p:e.getVoiceFile())
		{
			this.voiceFile.add(new IdNameValuePairDTO(p));
		}
		
		for(IdNameValuePair p:e.getDocFile())
		{
			this.docFile.add(new IdNameValuePairDTO(p));
		}
		this.time=DateTimeUtils.convert(e.getID().getTime(), DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_H);
		this.top=e.getIsTop();
	}
	
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	public List<IdNameValuePairDTO> getVoiceFile() {
		return voiceFile;
	}

	public void setVoiceFile(List<IdNameValuePairDTO> voiceFile) {
		this.voiceFile = voiceFile;
	}

	public List<IdNameValuePairDTO> getDocFile() {
		return docFile;
	}

	public void setDocFile(List<IdNameValuePairDTO> docFile) {
		this.docFile = docFile;
	}

	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}

	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getTop() {
		return top;
	}
	public void setTop(int top) {
		this.top = top;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}



	public int getAlready() {
		return already;
	}



	public void setAlready(int already) {
		this.already = already;
	}



	public Long getBt() {
		return bt;
	}

	public void setBt(Long bt) {
		this.bt = bt;
	}

	public Long getEt() {
		return et;
	}

	public void setEt(Long et) {
		this.et = et;
	}

	public int getAttachCount() {
		return voiceFile.size()+docFile.size();
	}

	public String getAvator() {
		return avator;
	}

	public void setAvator(String avator) {
		this.avator = avator;
	}

	public String getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}



	public int getIsSelfOper() {
		return isSelfOper;
	}



	public void setIsSelfOper(int isSelfOper) {
		this.isSelfOper = isSelfOper;
	}



	public int getTotalCount() {
		return totalCount;
	}



	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}



	public int getAlreadyCount() {
		return alreadyCount;
	}



	public void setAlreadyCount(int alreadyCount) {
		this.alreadyCount = alreadyCount;
	}



	
	
	
	
}
