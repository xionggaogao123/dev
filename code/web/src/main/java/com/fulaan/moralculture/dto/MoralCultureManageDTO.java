package com.fulaan.moralculture.dto;

import com.pojo.moralculture.MoralCultureManageEntry;
import com.sys.utils.DateTimeUtils;
import org.bson.types.ObjectId;

/**
 * Created by guojing on 2015/7/2.
 */
public class MoralCultureManageDTO {

    /**
     * id
     */
    private String id;

    /**
     * 名称
     */
    private String moralCultureName;

    /**
     * 学校id
     */
    private String schoolId;

    /**
     * 学校名称
     */
    private String schoolName;

    /**
     * 创建人id
     */
    private String createBy;

    /**
     * 创建人name
     */
    private String createName;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 修改人id
     */
    private String updateBy;

    /**
     * 修改人name
     */
    private String updateName;

    /**
     * 修改时间
     */
    private String updateTime;

    /**
     * 是否删除
     */
    private int isDelete;

    public MoralCultureManageDTO() {

    }

    public MoralCultureManageDTO(MoralCultureManageEntry entry) {
        this.id = entry.getID().toString();
        this.moralCultureName = entry.getMoralCultureName();
        this.schoolId=entry.getSchoolId().toString();
        this.createBy  = entry.getCreateBy().toString();
        this.createTime =DateTimeUtils.convert(entry.getCreateTime(), DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_SS);
        this.isDelete = entry.getDeleteState();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMoralCultureName() {
        return moralCultureName;
    }

    public void setMoralCultureName(String moralCultureName) {
        this.moralCultureName = moralCultureName;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public String getUpdateName() {
        return updateName;
    }

    public void setUpdateName(String updateName) {
        this.updateName = updateName;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public int getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(int isDelete) {
        this.isDelete = isDelete;
    }

    public MoralCultureManageEntry buildMoralCultureManageEntry() {
        MoralCultureManageEntry mralCultureManageEntry = new MoralCultureManageEntry(
                getMoralCultureName(),
                new ObjectId(getSchoolId()),
                new ObjectId(getCreateBy())
        );
        return mralCultureManageEntry;

    }
}
