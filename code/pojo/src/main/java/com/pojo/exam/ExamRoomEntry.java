package com.pojo.exam;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;

/**
 * 考场资源Entry类
 *
 * @author cxy
 *         2015-7-26 17:50:03
 *         collectionName:examroom
 *         考场号 : ernu(examRoomNumber)
 *         考场名称 : erna(examRoomName)
 *         座位数 : ersn(examRoomSitNumber)
 *         备注 : erps(examRoomPostscript)
 *         删除标志位 : ir(isRemoved,0为未删除，1为已删除)
 *         所属学校id : scid(schoolId)
 */
public class ExamRoomEntry extends BaseDBObject {

    public ExamRoomEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public ExamRoomEntry(ObjectId shcoolId) {
        super();

        BasicDBObject baseEntry = new BasicDBObject().append("scid", shcoolId)
                .append("ernu", "")
                .append("erna", "")
                .append("ersn", 30)
                .append("erps", "")
                .append("ir", Constant.ZERO);

        setBaseEntry(baseEntry);

    }

    public String getExamRoomNumber() {
        return getSimpleStringValue("ernu");
    }

    public void setExamRoomNumber(String examRoomNumber) {
        setSimpleValue("ernu", examRoomNumber);
    }

    public String getExamRoomName() {
        return getSimpleStringValue("erna");
    }

    public void setExamRoomName(String examRoomName) {
        setSimpleValue("erna", examRoomName);
    }

    public int getExamRoomSitNumber() {
        return getSimpleIntegerValue("ersn");
    }

    public void setExamRoomSitNumber(String examRoomSitNumber) {
        setSimpleValue("ersn", examRoomSitNumber);
    }

    public String getExamRoomPostscript() {
        return getSimpleStringValue("erps");
    }

    public void setExamRoomPostscript(String examRoomPostscript) {
        setSimpleValue("erps", examRoomPostscript);
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
