package com.fulaan.indicator.dto;

import com.pojo.indicator.InterestEvaluate;
import com.pojo.indicator.InterestEvaluateEntry;
import com.sys.utils.DateTimeUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guojing on 2016/10/31.
 */
public class InterestEvaluateDTO {
    private String id;
    private String schoolId;
    private String activityId;
    private String activityName;
    private int termType;
    private String commonFromId;
    private String commonToId;
    private String commonToName;
    private String commonToIdsStr;
    private String tag;
    private String describe;
    private String appliedId;
    private String snapshotId;
    private String createDate;
    private String totalScore;
    private List<Indicator> zhiBiaos = new ArrayList<Indicator>();

    DateTimeUtils utils=new DateTimeUtils();

    public InterestEvaluateDTO() {

    }

    public InterestEvaluateDTO(InterestEvaluateEntry e) {
        if(e!=null) {
            this.id = e.getID() == null ? "" : e.getID().toString();
            this.schoolId = e.getSchoolId() == null ? "" : e.getSchoolId().toString();
            this.activityId = e.getActivityId() == null ? "" : e.getActivityId().toString();
            this.termType = e.getTermType();
            this.commonFromId = e.getCommonFromId() == null ? "" : e.getCommonFromId().toString();
            this.commonToId = e.getCommonToId() == null ? "" : e.getCommonToId().toString();
            this.tag = e.getTag() == null ? "" : e.getTag();
            if(e.getZhiBiaos()!=null&&e.getZhiBiaos().size()>0){
                for(InterestEvaluate ie:e.getZhiBiaos()){
                    Indicator indicator=new Indicator(ie);
                    this.zhiBiaos.add(indicator);
                }
            }
            if(e.getCreateDate()!=0l){
                this.createDate = utils.convert(e.getCreateDate(), utils.DATE_YYYY_MM_DD);
            }else {
                this.createDate = utils.convert(e.getID().getTime(), utils.DATE_YYYY_MM_DD);
            }
            this.totalScore="";
            this.appliedId= e.getAppliedId()== null ? "" : e.getAppliedId().toString();
            this.snapshotId= e.getSnapshotId() == null ? "" : e.getSnapshotId().toString();
            this.describe = e.getDescribe() == null ? "" : e.getDescribe();
        }else{
            new InterestEvaluateDTO();
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public int getTermType() {
        return termType;
    }

    public void setTermType(int termType) {
        this.termType = termType;
    }

    public String getCommonFromId() {
        return commonFromId;
    }

    public void setCommonFromId(String commonFromId) {
        this.commonFromId = commonFromId;
    }

    public String getCommonToId() {
        return commonToId;
    }

    public void setCommonToId(String commonToId) {
        this.commonToId = commonToId;
    }

    public String getCommonToName() {
        return commonToName;
    }

    public void setCommonToName(String commonToName) {
        this.commonToName = commonToName;
    }

    public String getCommonToIdsStr() {
        return commonToIdsStr;
    }

    public void setCommonToIdsStr(String commonToIdsStr) {
        this.commonToIdsStr = commonToIdsStr;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getAppliedId() {
        return appliedId;
    }

    public void setAppliedId(String appliedId) {
        this.appliedId = appliedId;
    }

    public String getSnapshotId() {
        return snapshotId;
    }

    public void setSnapshotId(String snapshotId) {
        this.snapshotId = snapshotId;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(String totalScore) {
        this.totalScore = totalScore;
    }

    public List<Indicator> getZhiBiaos() {
        return zhiBiaos;
    }

    public void setZhiBiaos(List<Indicator> zhiBiaos) {
        this.zhiBiaos = zhiBiaos;
    }

    public InterestEvaluateEntry buildInterestEvaluateEntry() {
        ObjectId apid = null;
        if(this.getAppliedId()!=null&&!"".equals(this.getAppliedId())){
            apid = new ObjectId(this.getAppliedId());
        }

        ObjectId stid = null;
        if(this.getSnapshotId()!=null&&!"".equals(this.getSnapshotId())){
            stid = new ObjectId(this.getSnapshotId());
        }
        List<InterestEvaluate> zbs = new ArrayList<InterestEvaluate>();
        if(this.getZhiBiaos()!=null&&this.getZhiBiaos().size()>0){
            for(Indicator indi : this.getZhiBiaos()){
                if(indi!=null) {
                    zbs.add(indi.buildInterestEvaluate());
                }
            }
        }
        InterestEvaluateEntry entry=new InterestEvaluateEntry(
                new ObjectId(this.getSchoolId()),
                new ObjectId(this.getActivityId()),
                this.getTermType(),
                new ObjectId(this.getCommonFromId()),
                new ObjectId(this.getCommonToId()),
                this.getTag(),
                this.getDescribe(),
                apid,
                stid,
                zbs
        );
        return entry;
    }
}
