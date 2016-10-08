package com.fulaan.common;

/**
 * 通用返回结果
 * @author xusy
 */
public class CommonResult {
	
	/**
	 * 返回结果码
	 */
	private int code;
	
	/**
	 * 结果
	 */
	private String result;
	
	/**
	 * 相关信息
	 */
	private String msg;

	public CommonResult() {}
	
	/**
	 * 构造方法
	 * @param code
	 * @param result
	 * @param msg
	 */
	public CommonResult(int code, String result, String msg) {
		this.code = code;
		this.result = result;
		this.msg = msg;
	}
	
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
}
