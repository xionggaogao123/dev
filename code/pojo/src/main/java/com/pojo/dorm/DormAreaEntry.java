package com.pojo.dorm;

import java.util.Date;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;

/**
 * 
 * @author zhanghao@ycode.cn 2015-12-8
 * 
 *         宿舍区Entry类 collectionName:dormarea 
 *         学校ID：scid(schoolId)
 *         宿舍区名字：dnm(dormAreaName) 
 *         创建时间：ct(createTime) 
 *         删除标志位 : ir(isRemoved,0为未删除，1为已删除)
 * 		宿舍区Entry类 collectionName:dormarea 学校ID：scid(schoolId)
 *		      宿舍区名字：dnm(dormAreaName) 创建时间：ct(createTime)
 * 		类别:tp(type)(0宿舍区，1宿舍楼，2宿舍楼层) 删除标志位 : ir(isRemoved,0为未删除，1为已删除)
 *
 */
public class DormAreaEntry extends BaseDBObject {
	public DormAreaEntry(BasicDBObject baseEntry) {
		super(baseEntry);
	};

	public DormAreaEntry(ObjectId schoolId, String dormAreaName) {
		super();

		BasicDBObject baseEntry = new BasicDBObject().append("scid", schoolId)
				.append("dnm", dormAreaName).append("ct", System.currentTimeMillis())
				.append("ir", Constant.ZERO)
				.append("tp", "宿舍区");
		setBaseEntry(baseEntry);
	}

	public ObjectId getSchoolId() {
		return getSimpleObjecIDValue("scid");
	}

	public void setSchoolId(ObjectId schoolId) {
		setSimpleValue("scid", schoolId);
	}

	public String getDormAreaName() {
		return getSimpleStringValue("dnm");
	}

	public void setDormAreaName(String dormAreaName) {
		setSimpleValue("dnm", dormAreaName);
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
	
    public String getType() {
        return getSimpleStringValue("tp");
    }

    public void setType(String type) {
        setSimpleValue("tp", type);
    }
}
