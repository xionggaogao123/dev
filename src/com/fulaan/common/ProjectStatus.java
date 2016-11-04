package com.fulaan.common;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目状态枚举类
 * @author xusy
 * 2016-10-31 14:20:34
 */
public enum ProjectStatus {
	
	NEW_PRJ(0, "新建"),
	PROGRESSING(1, "进行中"),
	PENDING(2, "挂起"),
	DELAY(3, "延误"),
	COMPLETION(4, "已完成");
	
	private int code;
	
	private String status;

	ProjectStatus(int code, String status) {
		this.code = code;
		this.status = status;
	}
	
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	/**
	 * 是否为正确项目状态
	 * @return
	 */
	public static boolean isCorrectStatus(int code) {
		
		for(ProjectStatus ps : ProjectStatus.values()) {
			if(code == ps.getCode()) {
				return true;
			}
		}
		
		return false;
	}
	
	
	/**
	 * 获取状态
	 * @param code
	 * @return
	 */
	public static ProjectStatus getStatusByCode(int code) {
		for(ProjectStatus ps : ProjectStatus.values()) {
			if(code == ps.getCode()) {
				return ps;
			}
		}
		
		return null;
	}
	
	public static List<LabelValueVO> getStatusVO() {
		
		List<LabelValueVO> VOList = new ArrayList<LabelValueVO>();
		
		for(ProjectStatus ps : ProjectStatus.values()) {
			LabelValueVO vo = new LabelValueVO();
			vo.setValue(ps.getCode());
			vo.setLabel(ps.getStatus());
			VOList.add(vo);
		}
		
		return VOList;
	}
	
	public static void main(String[] args) {
		System.out.println(isCorrectStatus(0));
		for(ProjectStatus ps : ProjectStatus.values()) {
			LabelValueVO vo = new LabelValueVO();
			vo.setValue(ps.getCode());
			vo.setLabel(ps.getStatus());
			System.out.println(ps.getCode() + "-" + ps.getStatus() + ps);
		}
	}
	
}
