package com.pojo.examresult;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**考试成绩表
 * Created by fl on 2015/6/12.
 *
 * {
 *         id:成绩表ID
 *         exId：考试id
 *         stuId:学生的id
 *         stuNm：学生的姓名
 *         cId:班级的Id
 *         scoList：学生的成绩
 *         [
 *          {
 *              subId:考试科目
 *              subScore:考试成绩
 *           }
 *         ]
 *         suc:总成绩
 *         aid:区域联考id
 *         sr:学校排名
 *         ar：区域排名
 *     }
 */
public class PerformanceEntry extends BaseDBObject implements Comparable{

    /**
	 * 
	 */
	private static final long serialVersionUID = -2689034026450549544L;

	/**
     * 构造函数
     * @param baseEntry
     */
    public PerformanceEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    /**
     * 构造函数
     * @param studentId
     * @param studentName
     * @param classId
     * @param scoreList
     */
    public PerformanceEntry(ObjectId examId, ObjectId studentId,String studentName,ObjectId classId, List<Score> scoreList) {
        super();
        BasicDBObject dbo = new BasicDBObject()
                .append("exId", examId)
                .append("stuId", studentId)
                .append("stuNm", studentName)
                .append("cId", classId)
                .append("sList", MongoUtils.convert(MongoUtils.fetchDBObjectList(scoreList)));
        setBaseEntry(dbo);
    }

    //getters and setters
    public ObjectId getId() {
        return getID();
    }

    public ObjectId getExamId() {
        return getSimpleObjecIDValue("exId");
    }

    public void setExamId(ObjectId studentId) {
        setSimpleValue("exId", studentId);
    }

    public ObjectId getStudentId() {
        return getSimpleObjecIDValue("stuId");
    }

    public void setStudentId(ObjectId studentId) {
        setSimpleValue("stuId", studentId);
    }

    public  ObjectId getClassId() {
        return  getSimpleObjecIDValue("cId");
    }

    public void setClassId(ObjectId classId) {
        setSimpleValue("cId", classId);
    }

    public String getStudentName() {
        return  getSimpleStringValue("stuNm");
    }

    public void setStudentName(String studentName) {
        setSimpleValue("stuNm", studentName);
    }

    public ObjectId getAreaExamId(){return getSimpleObjecIDValue("aid");}

    public void setAreaExamId(ObjectId areaExamId){setSimpleValue("aid", areaExamId);}

    public int getSchoolRanking(){
        if(getBaseEntry().containsField("sr")){
            return getSimpleIntegerValue("sr");
        } else {
            return 0;
        }
    }

    public void setSchoolRanking(int schoolRanking){
        setSimpleValue("sr", schoolRanking);
    }

    public int getAreaRanking(){
        if(getBaseEntry().containsField("ar")){
            return getSimpleIntegerValue("ar");
        } else {
            return 0;
        }
    }

    public void setAreaRanking(int areaRanking){
        setSimpleValue("ar",areaRanking);
    }

    public double getScoreSum() {
        return getSimpleDoubleValue("suc");
    }

    public void setScoreSum(double scoreSum) {
        setSimpleValue("suc", scoreSum);
    }

    public List<Score> getScoreList() {
        List<Score> scoreList = new ArrayList<Score>();
        BasicDBList list = (BasicDBList)getSimpleObjectValue("sList");
        if(null != list && !list.isEmpty()) {
            for(Object o : list) {
                scoreList.add(new Score((BasicDBObject)o));
            }
        }
        return scoreList;
    }

    public void setScoreList(List<Score> scoreList) {
        List<DBObject> list = MongoUtils.fetchDBObjectList(scoreList);
        setSimpleValue("sList", MongoUtils.convert(list));
    }

    @Override
    public int compareTo(Object o) {
        PerformanceEntry other = (PerformanceEntry)o;
        Double thisScore = getScoreSum();
        Double otherScore = other.getScoreSum();
        return thisScore.compareTo(otherScore);
    }
}
