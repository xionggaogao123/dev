package com.fulaan_old.experience.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pojo.user.ExpLogType;
import com.pojo.user.UserExperienceLogEntry.ExperienceLog;
import com.sys.utils.CustomDateSerializer;
import com.sys.utils.DateTimeUtils;
import org.bson.types.ObjectId;

import java.util.Date;

/**
 * Created by guojing on 2015/3/31.
 */
public class ExperienceLogDTO {

    private String userId;

    private String experiencename;

    private int experience;

    private ExpLogType expLogType;

    private int expLogTypeOrdinal;

    /**
     * 创建时间
     */
    @JsonSerialize(using = CustomDateSerializer.class)
    private Date createtime;

    private String relateId;

    public ExperienceLogDTO() {

    }

    public ExperienceLogDTO(ExperienceLog experienceLog) {
        this.experiencename = experienceLog.getDes();
        this.experience = experienceLog.getExperience();
        this.createtime = new Date(experienceLog.getTime());
        this.expLogTypeOrdinal = experienceLog.getExpLogTypeOrdinal();
        this.relateId = experienceLog.getRelariveId() == null ? null : experienceLog.getRelariveId().toString();

    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getExperiencename() {
        return experiencename;
    }

    public void setExperiencename(String experiencename) {
        this.experiencename = experiencename;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public ExpLogType getExpLogType() {
        return expLogType;
    }

    public void setExpLogType(ExpLogType expLogType) {
        this.expLogType = expLogType;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public String getCreatetimeStr() {
        if (getCreatetime() != null) {
            DateTimeUtils time = new DateTimeUtils();
            return time.dateToStrLong(getCreatetime(), time.DATE_YYYY_MM_DD_HH_MM_SS_H);
        } else {
            return "";
        }
    }

    public String getRelateId() {
        return relateId;
    }

    public void setRelateId(String relateId) {
        this.relateId = relateId;
    }

    public int getExpLogTypeOrdinal() {
        return expLogTypeOrdinal;
    }

    public void setExpLogTypeOrdinal(int expLogTypeOrdinal) {
        this.expLogTypeOrdinal = expLogTypeOrdinal;
    }

    public ExperienceLog buildExperienceLog() {
        long createtime = this.getCreatetime() == null ? 0 : this.getCreatetime().getTime();
        ObjectId relateId = this.getRelateId() == null ? null : new ObjectId(this.getRelateId());
        ExperienceLog expLog = new ExperienceLog(
                this.getExperiencename(),
                this.getExperience(),
                createtime,
                this.getExpLogTypeOrdinal(),
                relateId
        );
        return expLog;
    }
}
