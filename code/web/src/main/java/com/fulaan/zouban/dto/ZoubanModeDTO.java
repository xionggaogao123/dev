package com.fulaan.zouban.dto;

import com.pojo.app.IdNameValuePair;
import com.pojo.app.IdNameValuePairDTO;
import com.pojo.app.NameValuePair;
import com.pojo.app.NameValuePairDTO;
import com.pojo.zouban.ZoubanModeEntry;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangkaidong on 2016/7/13.
 */
public class ZoubanModeDTO {
    private String id;
    private String schoolId;
    private String schoolName;
    private List<NameValuePairDTO> mode = new ArrayList<NameValuePairDTO>();
    private List<IdNameValuePairDTO> gradeList = new ArrayList<IdNameValuePairDTO>();


    public ZoubanModeDTO() {
    }

    public ZoubanModeDTO(ZoubanModeEntry entry) {
        this.id = entry.getID().toString();
        this.schoolId = entry.getSchoolId().toString();
        this.schoolName = entry.getSchoolName();

        for (NameValuePair nvp : entry.getMode()) {
            mode.add(new NameValuePairDTO(nvp));
        }
        for (IdNameValuePair invp : entry.getGradeList()) {
            gradeList.add(new IdNameValuePairDTO(invp));
        }
    }

    public ZoubanModeEntry exportEntry() {
        ZoubanModeEntry entry = new ZoubanModeEntry();
        if (!StringUtils.isEmpty(id)) {
            entry.setID(new ObjectId(id));
        }
        entry.setSchoolId(new ObjectId(schoolId));
        entry.setSchoolName(schoolName);

        if (mode.size() > 0) {
            List<NameValuePair> modeList = new ArrayList<NameValuePair>();
            for (NameValuePairDTO dto : mode) {
                modeList.add(new NameValuePair(dto.getName(), dto.getValue()));
            }
            entry.setMode(modeList);
        }
        if (gradeList.size() > 0) {
            List<IdNameValuePair> grades = new ArrayList<IdNameValuePair>();
            for (IdNameValuePairDTO dto : gradeList) {
                grades.add(new IdNameValuePair(new ObjectId(dto.getIdStr()), dto.getName(), dto.getValue()));
            }
            entry.setGradeList(grades);
        }

        return entry;
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

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public List<NameValuePairDTO> getMode() {
        return mode;
    }

    public void setMode(List<NameValuePairDTO> mode) {
        this.mode = mode;
    }

    public List<IdNameValuePairDTO> getGradeList() {
        return gradeList;
    }

    public void setGradeList(List<IdNameValuePairDTO> gradeList) {
        this.gradeList = gradeList;
    }
}
