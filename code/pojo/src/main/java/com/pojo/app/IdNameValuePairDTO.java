package com.pojo.app;

import org.bson.types.ObjectId;

import com.pojo.lesson.LessonEntry;
import com.pojo.school.ClassEntry;
import com.pojo.school.DepartmentEntry;
import com.pojo.user.UserEntry;
import com.sys.constants.Constant;

public class IdNameValuePairDTO {

	private Object id;
	private String name;
	private Object value;
	
	
	
	public IdNameValuePairDTO()
	{
	}
	
	
	public IdNameValuePairDTO(Object id,String name,Object value)
	{
		this.id=id;
		this.name=name;
		this.value=value;
	}
	
	
	public IdNameValuePairDTO(LessonEntry e) {
		super();
		this.id = e.getID();
		this.value = e.getName();
		this.name=e.getImgUrl();
	}
	
	
	public IdNameValuePairDTO(ObjectId id)
	{
		this.id=id;
	}
	
	public IdNameValuePairDTO(IdNameValuePair pair)
	{
		this.id=pair.getId() == null ? "" :pair.getId().toString();
		this.name=pair.getName();
		this.value=pair.getValue();
	}

	public IdNameValuePair exportEntry(){
		return new IdNameValuePair(new ObjectId((String)id), name, value);
	}
	
	public IdNameValuePairDTO(IdValuePair pair)
	{
		this.id=pair.getId();
		this.value=pair.getValue();
	}
	public IdNameValuePairDTO(UserEntry ue)
	{
		this.id=ue.getID();
		this.value=ue.getUserName();
	}
	public IdNameValuePairDTO(ClassEntry ce)
	{
		this.id=ce.getID();
		this.value=ce.getName();
	}
	public IdNameValuePairDTO(DepartmentEntry ce)
	{
		this.id=ce.getID();
		this.value=ce.getName();
	}
	
	

	public String getIdStr() {
		if(null!=this.id)
			return id.toString();
		return Constant.EMPTY;
	}

	public void setIdStr(String idStr) {
		//this.idStr = idStr;
	}


	public Object getId() {
		return id;
	}
	public void setId(Object id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	
	
	public IdNameValuePair build()
	{
		IdNameValuePair p =new IdNameValuePair(this.getId(), this.getName(), this.getValue());
		return p;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		IdNameValuePairDTO other = (IdNameValuePairDTO) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "IdNameValuePairDTO [id=" + id + ", name=" + name + ", value="
				+ value + "]";
	}
	
	
	
}
