package com.pojo.app;
/**
 * 文件上传结果
 * @author fourer
 *
 */
public class FileUploadDTO {

	private String id;
	private String fileKey;
	private String fileName;
	private String path;
	
	
	
	public FileUploadDTO(String id, String fileKey, String fileName, String path) {
		super();
		this.id = id;
		this.fileKey = fileKey;
		this.fileName = fileName;
		this.path = path;
	}
	
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFileKey() {
		return fileKey;
	}
	public void setFileKey(String fileKey) {
		this.fileKey = fileKey;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}



	@Override
	public String toString() {
		return "FileUploadDTO [id=" + id + ", fileKey=" + fileKey
				+ ", fileName=" + fileName + ", path=" + path + "]";
	}
	
	
	
}
