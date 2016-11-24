package com.pojo.examscoreanalysis;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

/**总分表
 * exid 考试id
 * acid 行政班id
 * stuid 学生id
 * yzf 语数英总分
 * ycr 语数英班级排名
 * ygr 语数英年级排名
 * zf 全科总分
 * cr 全科班级排名
 * gr 全科年级排名
 * Created by fl on 2016/8/23.
 */
public class ZongFenEntry extends BaseDBObject {

    public static final int YSW = 1;

    public static final int ALL = 2;

    public ZongFenEntry(){}

    public ZongFenEntry(BasicDBObject baseEntry){
        setBaseEntry(baseEntry);
    }

    public ZongFenEntry(ObjectId examId, ObjectId adminClassId, ObjectId studentId, double yswZongFen, int yswClassRank, int yswGradeRank,
                        double zongFen, int classRank, int gradeRank){
        BasicDBObject baseEntry = new BasicDBObject()
                .append("exid", examId)
                .append("acid", adminClassId)
                .append("stuid", studentId)
                .append("yzf", yswZongFen)
                .append("ycr", yswClassRank)
                .append("ygr", yswGradeRank)
                .append("zf", zongFen)
                .append("cr", classRank)
                .append("gr", gradeRank);
        setBaseEntry(baseEntry);
    }

    public int getClassRank() {
        return getSimpleIntegerValue("cr");
    }

    public void setClassRank(int classRank) {
        setSimpleValue("cr", classRank);
    }

    public ObjectId getExamId() {
        return getSimpleObjecIDValue("exid");
    }

    public void setExamId(ObjectId examId) {
        setSimpleValue("exid", examId);
    }

    public int getGradeRank() {
        return getSimpleIntegerValue("gr");
    }

    public void setGradeRank(int gradeRank) {
        setSimpleValue("gr", gradeRank);
    }

    public ObjectId getStudentId() {
        return getSimpleObjecIDValue("stuid");
    }

    public void setStudentId(ObjectId studentId) {
        setSimpleValue("stuid", studentId);
    }

    public int getYswClassRank() {
        return getSimpleIntegerValue("ycr");
    }

    public void setYswClassRank(int yswClassRank) {
        setSimpleValue("ycr", yswClassRank);
    }

    public int getYswGradeRank() {
        return getSimpleIntegerValue("ygr");
    }

    public void setYswGradeRank(int yswGradeRank) {
        setSimpleValue("ygr", yswGradeRank);
    }

    public double getYswZongFen() {
        return getSimpleDoubleValue("yzf");
    }

    public void setYswZongFen(double yswZongFen) {
        setSimpleValue("yzf", yswZongFen);
    }

    public double getZongFen() {
        return getSimpleDoubleValue("zf");
    }

    public void setZongFen(double zongFen) {
        setSimpleValue("zf", zongFen);
    }

    public ObjectId getAdminClassId() {
        return getSimpleObjecIDValue("acid");
    }

    public void setAdminClassId(ObjectId adminClassId) {
        setSimpleValue("acid", adminClassId);
    }

    public int getGradeRankByType(int type){
        if(type == YSW){
            return getYswGradeRank();
        } else {
            return getGradeRank();
        }
    }
}
