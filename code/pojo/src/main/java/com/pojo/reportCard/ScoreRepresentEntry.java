package com.pojo.reportCard;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;

/**
 * 
 * <简述>
 * <详细描述>groupExamDetailId:考试id
 *          subjectId:科目id
 *          subjectName:科目名称
 *          maxScore:总分
 *          。
 *          。
 *          representNameType:各级代表类型
 *          1:优秀、良好、合格、需努力
 *          2：优、良、中、差
 *          3：A\B\C\D
 *     
 * @author   Brant
 * @version  $Id$
 * @since
 * @see
 */
public class ScoreRepresentEntry extends BaseDBObject {

    public ScoreRepresentEntry(DBObject dbObject) {
        setBaseEntry((BasicDBObject)dbObject);
    }

    public ScoreRepresentEntry(BasicDBObject baseEntry) {
        super(baseEntry);
        // TODO Auto-generated constructor stub
    }

    public ScoreRepresentEntry(ObjectId groupExamDetailId,
                               ObjectId subjectId,
                               String subjectName,
                               String maxScore,
                               String scoreOne,
                               String scoreTwo,
                               String scoreThree,
                               String scoreFour,
                               String scoreFive,
                               String scoreSix,
                               String scoreSeven,
                               String scoreEight,
                               int sort,
                               int representNameType) {
        BasicDBObject basicDBObject=new BasicDBObject()
            .append("groupExamDetailId",groupExamDetailId)
            .append("subjectId", subjectId)
            .append("subjectName", subjectName)
            .append("maxScore", maxScore)
            .append("scoreOne", scoreOne)
            .append("scoreTwo", scoreTwo)
            .append("scoreThree", scoreThree)
            .append("scoreFour", scoreFour)
            .append("scoreFive", scoreFive)
            .append("scoreSix", scoreSix)
            .append("scoreSeven", scoreSeven)
            .append("scoreEight", scoreEight)
            .append("sort", sort)
            .append("representNameType", representNameType)
            .append("ir", Constant.ZERO);
            
    setBaseEntry(basicDBObject);
        
    }
    
    public ScoreRepresentEntry(ObjectId groupExamDetailId,
                    
                               String subjectName,
                               String maxScore,
                               String scoreOne,
                               String scoreTwo,
                               String scoreThree,
                               String scoreFour,
                               String scoreFive,
                               String scoreSix,
                               String scoreSeven,
                               String scoreEight,
                               int sort,
                               int representNameType) {
        BasicDBObject basicDBObject=new BasicDBObject()
            .append("groupExamDetailId",groupExamDetailId)
            .append("subjectName", subjectName)
            .append("maxScore", maxScore)
            .append("scoreOne", scoreOne)
            .append("scoreTwo", scoreTwo)
            .append("scoreThree", scoreThree)
            .append("scoreFour", scoreFour)
            .append("scoreFive", scoreFive)
            .append("scoreSix", scoreSix)
            .append("scoreSeven", scoreSeven)
            .append("scoreEight", scoreEight)
            .append("sort", sort)
            .append("representNameType", representNameType)
            .append("ir", Constant.ZERO);
            
    setBaseEntry(basicDBObject);
        
    }
    
    public ScoreRepresentEntry(ObjectId id,
                               ObjectId groupExamDetailId,
                               ObjectId subjectId,
                               String subjectName,
                               String maxScore,
                               String scoreOne,
                               String scoreTwo,
                               String scoreThree,
                               String scoreFour,
                               String scoreFive,
                               String scoreSix,
                               String scoreSeven,
                               String scoreEight,
                               int sort,
                               int representNameType) {
        BasicDBObject basicDBObject=new BasicDBObject()
            .append("_id", id)
            .append("groupExamDetailId",groupExamDetailId)
            .append("subjectId", subjectId)
            .append("subjectName", subjectName)
            .append("maxScore", maxScore)
            .append("scoreOne", scoreOne)
            .append("scoreTwo", scoreTwo)
            .append("scoreThree", scoreThree)
            .append("scoreFour", scoreFour)
            .append("scoreFive", scoreFive)
            .append("scoreSix", scoreSix)
            .append("scoreSeven", scoreSeven)
            .append("scoreEight", scoreEight)
            .append("sort", sort)
            .append("representNameType", representNameType)
            .append("ir", Constant.ZERO);
            
    setBaseEntry(basicDBObject);
        
    }
    
    public ScoreRepresentEntry(
                               ObjectId groupExamDetailId,
                    
                               String subjectName,
                               String maxScore,
                               String scoreOne,
                               String scoreTwo,
                               String scoreThree,
                               String scoreFour,
                               String scoreFive,
                               String scoreSix,
                               String scoreSeven,
                               String scoreEight,
                               int sort,
                               int representNameType,
                               ObjectId id) {
        BasicDBObject basicDBObject=new BasicDBObject()
            .append("_id", id)
            .append("groupExamDetailId",groupExamDetailId)
            .append("subjectName", subjectName)
            .append("maxScore", maxScore)
            .append("scoreOne", scoreOne)
            .append("scoreTwo", scoreTwo)
            .append("scoreThree", scoreThree)
            .append("scoreFour", scoreFour)
            .append("scoreFive", scoreFive)
            .append("scoreSix", scoreSix)
            .append("scoreSeven", scoreSeven)
            .append("scoreEight", scoreEight)
            .append("sort", sort)
            .append("representNameType", representNameType)
            .append("ir", Constant.ZERO);
            
    setBaseEntry(basicDBObject);
        
    }
    
    public void setGroupExamDetailId(ObjectId groupExamDetailId){
        setSimpleValue("groupExamDetailId",groupExamDetailId);
    }

    public ObjectId getGroupExamDetailId(){
        return getSimpleObjecIDValue("groupExamDetailId");
    }
    
    public void setSubjectId(ObjectId subjectId){
        setSimpleValue("subjectId",subjectId);
    }

    public ObjectId getSubjectId(){
        return getSimpleObjecIDValue("subjectId");
    }
    
    
    public void setSubjectName(String subjectName){
        setSimpleValue("subjectName",subjectName);
    }

    public String getSubjectName(){
        return getSimpleStringValue("subjectName");
    }
    
    public void setMaxScore(String maxScore){
        setSimpleValue("maxScore",maxScore);
    }

    public String getMaxScore(){
        return getSimpleStringValue("maxScore");
    }
    
    public void setScoreOne(String scoreOne){
        setSimpleValue("scoreOne",scoreOne);
    }

    public String getScoreOne(){
        return getSimpleStringValue("scoreOne");
    }
    
    public void setScoreTwo(String scoreTwo){
        setSimpleValue("scoreTwo",scoreTwo);
    }

    public String getScoreTwo(){
        return getSimpleStringValue("scoreTwo");
    }
    
    public void setScoreThree(String scoreThree){
        setSimpleValue("scoreThree",scoreThree);
    }

    public String getScoreThree(){
        return getSimpleStringValue("scoreThree");
    }
    
    public void setScoreFour(String scoreFour){
        setSimpleValue("scoreFour",scoreFour);
    }

    public String getScoreFour(){
        return getSimpleStringValue("scoreFour");
    }
    
    public void setScoreFive(String scoreFive){
        setSimpleValue("scoreFive",scoreFive);
    }

    public String getScoreFive(){
        return getSimpleStringValue("scoreFive");
    }
    
    public void setScoreSix(String scoreSix){
        setSimpleValue("scoreSix",scoreSix);
    }

    public String getScoreSix(){
        return getSimpleStringValue("scoreSix");
    }
    
    public void setScoreSeven(String scoreSeven){
        setSimpleValue("scoreSeven",scoreSeven);
    }

    public String getScoreSeven(){
        return getSimpleStringValue("scoreSeven");
    }
    
    public void setScoreEight(String scoreEight){
        setSimpleValue("scoreEight",scoreEight);
    }

    public String getScoreEight(){
        return getSimpleStringValue("scoreEight");
    }
    
    public void setRepresentNameType(int representNameType){
        setSimpleValue("representNameType",representNameType);
    }

    public int getRepresentNameType(){
        return getSimpleIntegerValue("representNameType");
    }
    
    public void setSort(int sort){
        setSimpleValue("sort",sort);
    }

    public int getSort(){
        return getSimpleIntegerValue("sort");
    }
    
}
