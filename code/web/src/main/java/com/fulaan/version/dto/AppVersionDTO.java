package com.fulaan.version.dto;

import com.pojo.version.AppVersionEntry;
import com.sys.constants.Constant;

public class AppVersionDTO {

	private int  resultCode= Constant.ZERO;
	private String version;
	private String des;
	private String nextVersion;
	
	public AppVersionDTO(String version) {
		super();
		this.version = version;
	}
	
	public AppVersionDTO(String version, String des,String nextVersion) {
		super();
		this.version = version;
		this.des=des;
		this.nextVersion = nextVersion;
	}

	public AppVersionDTO(int code,String version, String des,String nextVersion) {
		super();
		this.resultCode = code;
		this.version = version;
		this.des=des;
		this.nextVersion =nextVersion;
	}
	public AppVersionEntry buildEntry(){
		return new AppVersionEntry(this.resultCode,this.version,this.des,this.nextVersion);
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
	public String getDes() {
		return des;
	}
	public void setDes(String des) {
		this.des = des;
	}

	public String getNextVersion() {
		return nextVersion;
	}

	public void setNextVersion(String nextVersion) {
		this.nextVersion = nextVersion;
	}
}
