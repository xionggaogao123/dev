package com.pojo.school;

import com.pojo.app.SimpleDTO;
import com.sys.constants.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * 学校信息
 * @author fourer
 *
 */
public enum SchoolType {
	
	
	KINDERGARENER(1,"幼儿园"),
	PRIMARY(2,"小学"),
	JUNIOR(4,"初中"),
	SENIOR(8,"高中"),
	UNIVERSITY(16,"大学"),
	;
	
	private int type;
	private String name;
	
	
	
	private SchoolType(int type, String name) {
		this.type = type;
		this.name = name;
	}
	
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * 根据type得到SchoolType
	 * @param type
	 * @return
	 */
	public static  List<SchoolType> getSchoolType(int type)
	{
		List<SchoolType> retList =new ArrayList<SchoolType>(Constant.FIVE);
		
		if((type & Constant.ONE) ==Constant.ONE)
		{
			retList.add(KINDERGARENER);
		}
		if((type & Constant.TWO) ==Constant.TWO)
		{
			retList.add(PRIMARY);
		}
		if((type & Constant.FOUR) ==Constant.FOUR)
		{
			retList.add(JUNIOR);
		}
		if((type & Constant.EIGHT) ==Constant.EIGHT)
		{
			retList.add(SENIOR);
		}
		if((type & Constant.SIXTEEN) ==Constant.SIXTEEN)
		{
			retList.add(UNIVERSITY);
		}
		return retList;
	}
	
	
	public SimpleDTO toSimpleDTO()
	{
		SimpleDTO dto =new SimpleDTO(getType(), getName());
		return dto;
	}
	
}
