package com.pojo.lesson;

import java.io.Serializable;
/**
 * 
 * @author fourer
 *
 */
public class DirDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4644573757196339123L;

	private String id;
	private String name;
	private String parentId;
	private int sort;
	
	
	public DirDTO(DirEntry e) {
		super();
		this.id=e.getID().toString();
		this.name=e.getDirName();
		if(null!=e.getParentId())
		{
		  this.parentId=e.getParentId().toString();
		}
		this.sort=e.getSort();
	}
	
	
	//@todo
	public DirDTO() {
		super();
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
	
	
}
