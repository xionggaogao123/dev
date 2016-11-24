package com.pojo.level;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;

/**
 * @author cxy 
 * 			2015-7-26 17:50:03
 *         等级设置Entry类 
 *         collectionName:level 
 *         等级名称 : ln(levelName) 
 *         分数范围 : sr(scoreRange) 
 *         删除标志位 : ir(isRemoved,0为未删除，1为已删除) 
 *         所属学校id : scid(schoolId)
 */
public class LevelEntry extends BaseDBObject {
	public LevelEntry(BasicDBObject baseEntry) {
		super(baseEntry);
	}

	public LevelEntry(ObjectId shcoolId) {
		super();

		BasicDBObject baseEntry = new BasicDBObject().append("scid", shcoolId)
				.append("ir", Constant.ZERO)
				.append("ln", "")
				.append("sr", Constant.ZERO);

		setBaseEntry(baseEntry);

	}

	public String getLevelName() {
		return getSimpleStringValue("ln");
	}

	public void setLevelName(String levelName) {
		setSimpleValue("ln", levelName);
	}

	public int getScoreRange() {
		return getSimpleIntegerValue("sr");
	}

	public void setScoreRange(String scoreRange) {
		setSimpleValue("sr", scoreRange);
	}

	// 默认未删除
	public int getIsRemove() {
		if (getBaseEntry().containsField("ir")) {
			return getSimpleIntegerValue("ir");
		}
		return Constant.ZERO;
	}

	public void setIsRemove(int isRemove) {
		setSimpleValue("ir", isRemove);
	}
	public String getSchoolId() {
		return getSimpleStringValue("scid");
	}
	public void setSchoolId(String schoolId) {
		setSimpleValue("scid", schoolId);
	}
}
