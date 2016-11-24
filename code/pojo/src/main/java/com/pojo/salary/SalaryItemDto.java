/**
 * @Title: SalaryDto.java
 * @Package com.pojo.salary
 * @Description: TODO(用一句话描述该文件是做什么的)
 * @author yang.ling
 * @date 2015年7月19日 下午10:00:07
 * @version V1.0
 * @Copyright ycode Co.,Ltd.
 */
package com.pojo.salary;

import java.io.Serializable;
import java.util.List;

import org.bson.types.ObjectId;

import com.mongodb.util.JSON;
import com.pojo.app.IdValuePairDTO;
import com.pojo.user.UserEntry;

/**
 * @author yang.ling
 * @ClassName: SalaryDto
 * @date 2015年7月19日 下午10:00:07
 */
public class SalaryItemDto implements Serializable {

    private static final long serialVersionUID = -3248753601133398909L;

    public static String ADD = "发款";
    public static String SUB = "扣款";

    private String id;
    private String itemName;
    private String type;
    private double m;
    private String mStr;
    private String schoolId;

    public SalaryItemDto() {
    }

    public SalaryItemDto(SalaryItemEntry salaryItemEntry) {
        this.id = salaryItemEntry.getID() == null ? null : salaryItemEntry.getID().toString();
        this.itemName = salaryItemEntry.getItemName();
        this.type = salaryItemEntry.getItemType();
    }

    public String getmStr() {
        return mStr;
    }

    public void setmStr(String mStr) {
        this.mStr = mStr;
    }

    public SalaryItemEntry exportEntry() {
        return new SalaryItemEntry(this.itemName, this.type);
    }
    
    public SalaryItemEntry exportEntryWithSID() {
    	return new SalaryItemEntry(this.itemName, this.type,new ObjectId(this.schoolId));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getM() {
        return m;
    }

    public void setM(double m) {
        this.m = m;
    }
    
    

    public String getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}

	@Override
    public String toString() {
        return JSON.serialize(this);
    }
    
    
}	
