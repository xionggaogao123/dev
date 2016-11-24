package com.pojo.app;

import org.bson.types.ObjectId;
/**
 * 用于排序
 * @author fourer
 *
 */
public class IdValuePairSortDTO extends IdValuePairDTO {

	private long sort;
	private int type;
	public IdValuePairSortDTO(ObjectId id, Object value) {
		super(id, value);
		// TODO Auto-generated constructor stub
	}
	
	public long getSort() {
		return sort;
	}
	public void setSort(long sort) {
		this.sort = sort;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	
	

}
