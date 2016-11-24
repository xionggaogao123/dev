package com.fulaan.educationbureau.dto;

import com.pojo.educationbureau.EducationBureauEntry;
import com.sys.utils.DateTimeUtils;
import org.bson.types.ObjectId;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by guojing on 2015/4/13.
 */
public class EducationBureauDTO implements Serializable {

    private static final long serialVersionUID = -5197596091397995948L;

    private String id;

    private String educationName;

    private String province;

    private String provinceName;

    private String city;

    private String cityName;

    private String county;

    private String countyName;

    private String educationLogo;

    private String schoolCreateDate;

    private List<String> userIds = new ArrayList<String>();

    private List<String> schoolIds = new ArrayList<String>();

    private Date createTime;

    private Date updateTime;

    /**
     * 是否开通云平台
     */
    private int openCloud;

    /**
     * 是否删除
     */
    private int isDelete;

    public EducationBureauDTO(){

    }

    public EducationBureauDTO(EducationBureauEntry educationBureauEntry) {
        if(educationBureauEntry !=null) {
            this.id = educationBureauEntry.getID() == null ? null : educationBureauEntry.getID().toString();
            this.educationName = educationBureauEntry.getEducationName();
            this.province = educationBureauEntry.getProvince();
            this.city = educationBureauEntry.getCity();
            this.county = educationBureauEntry.getCounty();
            this.educationLogo=educationBureauEntry.getEducationLogo();
            this.schoolCreateDate=educationBureauEntry.getSchoolCreateDate();
            if (educationBureauEntry.getUserIds() != null && !educationBureauEntry.getUserIds().isEmpty()) {
                this.userIds = new ArrayList<String>();
                for (ObjectId id : educationBureauEntry.getUserIds()) {
                    this.userIds.add(id.toString());
                }
            }

            if (educationBureauEntry.getSchoolIds() != null && !educationBureauEntry.getSchoolIds().isEmpty()) {
                this.schoolIds = new ArrayList<String>();
                for (ObjectId id : educationBureauEntry.getSchoolIds()) {
                    this.schoolIds.add(id.toString());
                }
            }

            this.createTime = new Date(educationBureauEntry.getCreateTime());

            this.updateTime = new Date(educationBureauEntry.getUpdateTime());

            this.openCloud=educationBureauEntry.getOpenCloud();

            this.isDelete = educationBureauEntry.getDeleteState();
        }else{
            new EducationBureauDTO();
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEducationName() {
        return educationName;
    }

    public void setEducationName(String educationName) {
        this.educationName = educationName;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getEducationLogo() {
        return educationLogo;
    }

    public void setEducationLogo(String educationLogo) {
        this.educationLogo = educationLogo;
    }

    public String getSchoolCreateDate() {
        return schoolCreateDate;
    }

    public void setSchoolCreateDate(String schoolCreateDate) {
        this.schoolCreateDate = schoolCreateDate;
    }

    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }

    public List<String> getSchoolIds() {
        return schoolIds;
    }

    public void setSchoolIds(List<String> schoolIds) {
        this.schoolIds = schoolIds;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public int getOpenCloud() {
        return openCloud;
    }

    public void setOpenCloud(int openCloud) {
        this.openCloud = openCloud;
    }

    public int getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(int isDelete) {
        this.isDelete = isDelete;
    }

    public EducationBureauEntry buildEducationEntry(){
        List<ObjectId> userIds = new ArrayList<ObjectId>();
        List<ObjectId> schoolIds = new ArrayList<ObjectId>();
        long createTime=this.getCreateTime()==null?0:this.getCreateTime().getTime();
        long updateTime=this.getUpdateTime()==null?0:this.getUpdateTime().getTime();
        if(getUserIds()!=null && !getUserIds().isEmpty())
        {
            for(String id: getUserIds())
            {
                userIds.add(new ObjectId(id));
            }
        }

        if(getSchoolIds()!=null && !getSchoolIds().isEmpty())
        {
            for(String id: getSchoolIds())
            {
                schoolIds.add(new ObjectId(id));
            }
        }

        String schoolCreateDate="";
        if(schoolIds.size()>0) {
            DateTimeUtils time=new DateTimeUtils();
            Collections.sort(schoolIds);
            schoolCreateDate = time.getLongToStrTime(schoolIds.get(0).getTime());
        }

        EducationBureauEntry educationBureauEntry = new EducationBureauEntry(
                getEducationName(),
                getProvince(),
                getCity(),
                getCounty(),
                getEducationLogo(),
                schoolCreateDate,
                userIds,
                schoolIds,
                createTime,
                updateTime
        );
        return educationBureauEntry;
    }
}
