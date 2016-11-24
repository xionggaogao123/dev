package com.pojo.resources;

import com.pojo.app.FileType;
import com.pojo.video.VideoDTO;
import org.apache.commons.lang.StringUtils;

/**
 * 资源DTO，继承VideoDTO，方便融合
 * @author fourer
 *
 */
public class ResourceDTO extends VideoDTO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5918857855632091422L;

	private String termType;
	private String subjectName;
	private String bookType;
	private String gradeName;
    private String chapterName;
    private String partName;
	private int viewCount;
	private int pushCount;
	private int fileTypeInt=-1;
	private String fileType="其他";
	private String userName="";
	private String createrAvt;
	private String schoolName;
	private String createTime;
	private String from;
	
	public ResourceDTO(ResourceEntry e) {
		super(e);
		this.viewCount=e.getViewNumber();
		this.pushCount=e.getPushCount();
		FileType fy = FileType.getFileType(e.getType());
		if(null!=fy)
		{
			if(!fy.equals(FileType.OTHER_VIDEO))
			{
			   this.fileType=fy.getName().toUpperCase();
			}
			else
			{
			   this.fileType="转码视频";
			}
			
			this.fileTypeInt=fy.getType();
		}
		
		if(StringUtils.isBlank(super.getImageUrl()))
		{
			if(fy.equals(FileType.DOC) || fy.equals(FileType.DOCX) )
			{
				super.setImageUrl("/images/resource/word.png");
			}
			if(fy.equals(FileType.PPT)  )
			{
				super.setImageUrl("/images/resource/ppt.png");
			}
			if(fy.equals(FileType.MP3)  )
			{
				super.setImageUrl("/images/resource/mp3.png");
			}
			if(fy.equals(FileType.PDF)  )
			{
				super.setImageUrl("/images/resource/pdf.png");
			}
		}
	}

	public String getTermType() {
		return termType;
	}

	public void setTermType(String termType) {
		this.termType = termType;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public String getBookType() {
		return bookType;
	}

	public void setBookType(String bookType) {
		this.bookType = bookType;
	}

	public String getGradeName() {
		return gradeName;
	}

	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}

	public String getChapterName() {
		return chapterName;
	}

	public void setChapterName(String chapterName) {
		this.chapterName = chapterName;
	}

	public String getPartName() {
		return partName;
	}

	public void setPartName(String partName) {
		this.partName = partName;
	}

	public int getViewCount() {
		return viewCount;
	}

	public void setViewCount(int viewCount) {
		this.viewCount = viewCount;
	}

	public int getPushCount() {
		return pushCount;
	}

	public void setPushCount(int pushCount) {
		this.pushCount = pushCount;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public int getFileTypeInt() {
		return fileTypeInt;
	}

	public void setFileTypeInt(int fileTypeInt) {
		this.fileTypeInt = fileTypeInt;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getCreaterAvt() {
		return createrAvt;
	}

	public void setCreaterAvt(String createrAvt) {
		this.createrAvt = createrAvt;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}
}
