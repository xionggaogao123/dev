package com.pojo.app;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 平台
 * @author fourer
 *
 */
public enum Platform {

	PC(1,"PC"),
	Android(2,"Android"),
	IOS(3,"IOS");

	private int type;
	private String name;

	Platform(int type, String name) {
		this.type = type;
		this.name = name;
	}

	public static Platform platform(String name){
		if(name.endsWith("Android")){
			return Android;
		}else if(name.endsWith("IOS")){
			return IOS;
		}
		return PC;
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

    public static Map<Integer, String> getPlatformMap() {
        Map<Integer, String> map = new LinkedHashMap<Integer, String>();
        for (Platform thisEnum : Platform.values()) {
            map.put(thisEnum.getType(), thisEnum.getName());
        }
        return map;
    }

	/**
	 * 移动平台
	 * @return
	 */
	public  boolean isMobile(){
    	return this == Platform.Android || this==Platform.IOS;
	}
}
