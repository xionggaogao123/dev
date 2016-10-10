package com.fulaan.common;

/**
 * 文件扩展名枚举类
 * @author xusy
 */
public enum FileExtension {
	
	DOC(1, "doc"),
	DOCX(2, "docx"),
	EXCEL(3, "xls"),
	EXCEL2(4, "xlsx"),
	TEXT(5, "txt"),
	ZIP(6, "zip"),
	RAR(7, "rar"),
	IMAGE(8, "jpg|png|gif|jpeg"),
	PDF(9, "pdf");
	
	private int code;
	
	private String extension;
	
	FileExtension(int code, String extension) {
		this.code = code;
		this.extension = extension;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}
	
	/**
	 * 根据拓展名匹配文件类型
	 * @param extension
	 * @return
	 */
	public static int matchFileType(String extension) {
		
		if(extension == null 
				|| "".equals(extension.trim())) {
			return -1;
		}
		
		for(FileExtension ext : FileExtension.values()) {
			if(FileExtension.IMAGE.equals(ext)) {
				String[] imageTypes = ext.getExtension().split("\\|");
				for(String type : imageTypes) {
					if(extension.equals(type)) {
						return IMAGE.code;
					}
				}
			}
			
			if(extension.equals(ext.getExtension())) {
				return ext.getCode();
			}
			
		}
 		
		return -1;
	}
	
	public static void main(String[] args) {
		System.out.println(matchFileType("doc"));
	}
	
}
