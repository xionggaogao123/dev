package com.pojo.app;

import com.sys.constants.Constant;

public class AppVersionDTO {

	private int  resultCode=Constant.ZERO;
	private String version;
	
	
	public AppVersionDTO(String version) {
		super();
		this.version = version;
	}
	public int getResultCode() {
		return resultCode;
	}
	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	
	
}
