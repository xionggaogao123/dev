package com.pojo.app;

import java.util.ArrayList;
import java.util.List;
/**
 * 
 * 分页DTO
 */
public class PageDTO<T> {

	private int count;
	
	private List<T> list =new ArrayList<T>();

	
	
	
	
	public PageDTO() {
		super();
	}

	public PageDTO(int count, List<T> list) {
		super();
		this.count = count;
		this.list = list;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}
	
	
	
}
