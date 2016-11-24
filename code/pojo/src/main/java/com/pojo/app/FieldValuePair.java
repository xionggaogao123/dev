package com.pojo.app;

/**
 * 存储字段与字段值
 * @author fourer
 *
 */
public class FieldValuePair {
	private String field;
	private Object value;
	
	public FieldValuePair(String field, Object value) {
		super();
		this.field = field;
		this.value = value;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

}
