package com.fulaan.zouban.dto;

import com.pojo.zouban.ZoubanConfig;
import org.bson.types.ObjectId;

/**
 * Created by qiangm on 2016/3/14.
 */
public class ZoubanConfigDTO {
    private String id;
    private String schoolId;
    private String schoolName;
    private int mode;

    public ZoubanConfigDTO(ZoubanConfig zoubanConfig) {
        this.id=zoubanConfig.getID().toString();
        this.schoolId=zoubanConfig.getSchoolId().toString();
        this.mode=zoubanConfig.getZoubanMode();
    }

    public ZoubanConfig export()
    {
        ZoubanConfig zoubanConfig=new ZoubanConfig();
        zoubanConfig.setSchoolId(new ObjectId(this.schoolId));
        zoubanConfig.setZoubanMode(this.mode);
        return zoubanConfig;
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

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }
}
