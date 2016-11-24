package com.pojo.resources;

import org.apache.commons.lang.StringUtils;

import com.pojo.app.FileType;
import com.pojo.video.VideoDTO;

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

	private int viewCount;
	private int pushCount;
	private int fileTypeInt=-1;
	private String fileType="其他";
	private String userName="";
	
	public ResourceDTO(ResourceEntry e) {
		super(e);
		this.viewCount=e.getViewNumber();
		this.pushCount=e.getPushCount();
		FileType fy =FileType.getFileType(e.getType());
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

	
	
}
