package com.pojo.app;

import java.util.ArrayList;
import java.util.List;
/**
 * 組信息,用于通知用户选择
 * @author fourer
 *
 * @param <T>
 */
public class UserGroupInfo<T> {

	private T t;
	private List<T> list =new ArrayList<T>();
	
	public UserGroupInfo(T t, List<T> list) {
		super();
		this.t = t;
		this.list = list;
	}
	public T getT() {
		return t;
	}
	public void setT(T t) {
		this.t = t;
	}
	public List<T> getList() {
		return list;
	}
	public void setList(List<T> list) {
		this.list = list;
	}
	
	
	
	
}
