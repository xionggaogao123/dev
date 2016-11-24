package com.fulaan.indicator.dto;

import com.fulaan.utils.StringUtil;
import com.pojo.indicator.IndicatorTreeAppliedEntry;
import com.sys.utils.DateTimeUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guojing on 2016/11/14.
 */
public class IndicatorTreeAppliedDTO {

    private String id;
    private String name;
    private String treeId;
    private String schoolId;
    private String snapshotId;
    private int termType;
    private String evaluatedGroupIdsStr;
    private List<String> evaluatedGroupIds = new ArrayList<String>();
    private String evaluaterIdsStr;
    private List<String> evaluaterIds = new ArrayList<String>();
    private int evaluateType;
    private String finishTime;
    private String createrId;
    private String createrName;
    private String createDate;
    private String updaterId;
    private String updaterName;
    private String updateDate;
    private String describe;
    private int isValid;
    private boolean isHandle;

    DateTimeUtils utils=new DateTimeUtils();

    public IndicatorTreeAppliedDTO(){

    }

    public IndicatorTreeAppliedDTO(IndicatorTreeAppliedEntry e){
        if(e!=null) {
            this.id = e.getID() == null ? "" : e.getID().toString();
            this.name = e.getName();
            this.treeId = e.getTreeId() == null ? "" : e.getTreeId().toString();
            this.schoolId = e.getSchoolId() == null ? "" : e.getSchoolId().toString();
            this.snapshotId = e.getSnapshotId() == null ? "" : e.getSnapshotId().toString();
            this.termType = e.getTermType();
            if(e.getEvaluateGroupIds()!=null&&e.getEvaluateGroupIds().size()>0){
                for(ObjectId egid:e.getEvaluateGroupIds()){
                    this.evaluatedGroupIds.add(egid.toString());
                }
            }
            if(e.getEvaluaterIds()!=null&&e.getEvaluaterIds().size()>0){
                for(ObjectId eid:e.getEvaluaterIds()){
                    this.evaluaterIds.add(eid.toString());
                }
            }
            this.evaluateType = e.getEvaluateType();
            if(e.getFinishTime()!=0l){
                this.finishTime = utils.convert(e.getFinishTime(), utils.DATE_YYYY_MM_DD);
            }else{
                this.finishTime = "";
            }
            this.createrId = e.getCreaterId() == null ? "" : e.getCreaterId().toString();
            if(e.getCreateDate()!=0l){
                this.createDate = utils.convert(e.getCreateDate(), utils.DATE_YYYY_MM_DD);
            }else{
                this.createDate = "";
            }

            this.updaterId = e.getUpdaterId() == null ? "" : e.getUpdaterId().toString();
            if(e.getUpdateDate()!=0l){
                this.updateDate = utils.convert(e.getUpdateDate(), utils.DATE_YYYY_MM_DD);
            }else{
                this.updateDate = "";
            }

            this.describe = e.getDescribe() == null ? "" : e.getDescribe();
        }else{
            new IndicatorTreeAppliedDTO();
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getTreeId() {
        return treeId;
    }

    public void setTreeId(String treeId) {
        this.treeId = treeId;
    }

    public String getSnapshotId() {
        return snapshotId;
    }

    public void setSnapshotId(String snapshotId) {
        this.snapshotId = snapshotId;
    }

    public int getTermType() {
        return termType;
    }

    public void setTermType(int termType) {
        this.termType = termType;
    }

    public String getEvaluatedGroupIdsStr() {
        return evaluatedGroupIdsStr;
    }

    public void setEvaluatedGroupIdsStr(String evaluatedGroupIdsStr) {
        this.evaluatedGroupIdsStr = evaluatedGroupIdsStr;
    }

    public List<String> getEvaluatedGroupIds() {
        return evaluatedGroupIds;
    }

    public void setEvaluatedGroupIds(List<String> evaluatedGroupIds) {
        this.evaluatedGroupIds = evaluatedGroupIds;
    }

    public String getEvaluaterIdsStr() {
        return evaluaterIdsStr;
    }

    public void setEvaluaterIdsStr(String evaluaterIdsStr) {
        this.evaluaterIdsStr = evaluaterIdsStr;
    }

    public List<String> getEvaluaterIds() {
        return evaluaterIds;
    }

    public void setEvaluaterIds(List<String> evaluaterIds) {
        this.evaluaterIds = evaluaterIds;
    }

    public int getEvaluateType() {
        return evaluateType;
    }

    public void setEvaluateType(int evaluateType) {
        this.evaluateType = evaluateType;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    public String getCreaterId() {
        return createrId;
    }

    public void setCreaterId(String createrId) {
        this.createrId = createrId;
    }

    public String getCreaterName() {
        return createrName;
    }

    public void setCreaterName(String createrName) {
        this.createrName = createrName;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getUpdaterId() {
        return updaterId;
    }

    public void setUpdaterId(String updaterId) {
        this.updaterId = updaterId;
    }

    public String getUpdaterName() {
        return updaterName;
    }

    public void setUpdaterName(String updaterName) {
        this.updaterName = updaterName;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public int getIsValid() {
        return isValid;
    }

    public void setIsValid(int isValid) {
        this.isValid = isValid;
    }

    public boolean getIsHandle() {
        return isHandle;
    }

    public void setIsHandle(boolean isHandle) {
        this.isHandle = isHandle;
    }

    public IndicatorTreeAppliedEntry buildIndicatorTreeAppliedEntry() {
        ObjectId treId=null;
        if(this.getTreeId()!=null&&!"".equals(this.getTreeId())){
            treId=new ObjectId(this.getTreeId());
        }

        ObjectId shtId = null;
        if(StringUtil.isEmpty(this.getSnapshotId())){
            shtId = new ObjectId(this.getSnapshotId());
        }

        List<ObjectId> egIds=new ArrayList<ObjectId>();
        if(this.getEvaluatedGroupIds()!=null&&this.getEvaluatedGroupIds().size()>0){
            for(String strid:this.getEvaluatedGroupIds()){
                egIds.add(new ObjectId(strid));
            }
        }

        List<ObjectId> evIds=new ArrayList<ObjectId>();
        if(this.getEvaluaterIds()!=null&&this.getEvaluaterIds().size()>0){
            for(String strid:this.getEvaluaterIds()){
                evIds.add(new ObjectId(strid));
            }
        }

        long fTime =0l;
        if(StringUtil.isEmpty(this.getFinishTime())){
            fTime=utils.getStrToLongTime(this.getFinishTime()+" 23:59:59");
        }else{
            fTime=utils.getStrToLongTime(utils.getCurrDate()+" 23:59:59");
        }

        ObjectId crtId=null;
        if(this.getCreaterId()!=null&&!"".equals(this.getCreaterId())){
            crtId=new ObjectId(this.getCreaterId());
        }
        IndicatorTreeAppliedEntry entry = new IndicatorTreeAppliedEntry(
                this.getName(),
                new ObjectId(this.getSchoolId()),
                treId,
                shtId,
                this.getTermType(),
                egIds,
                evIds,
                getEvaluateType(),
                fTime,
                crtId,
                this.getDescribe()
        );
        return entry;
    }

    public IndicatorTreeAppliedEntry buildUpdIndicatorTreeAppliedEntry() {
        long fTime =0l;
        if(StringUtil.isEmpty(this.getFinishTime())){
            fTime=utils.getStrToLongTime(this.getFinishTime()+" 23:59:59");
        }else{
            fTime=utils.getStrToLongTime(utils.getCurrDate()+" 23:59:59");
        }

        ObjectId updId=null;
        if(this.getUpdaterId()!=null&&!"".equals(this.getUpdaterId())){
            updId=new ObjectId(this.getUpdaterId());
        }
        IndicatorTreeAppliedEntry entry = new IndicatorTreeAppliedEntry(
                this.getName(),
                fTime,
                updId,
                this.getDescribe()
        );
        return entry;
    }
}
