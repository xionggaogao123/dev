package com.fulaan.zouban.dto;

import com.pojo.zouban.CourseItem;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qiangm on 2015/9/15.
 */
public class CourseItemDTO {
    private String id;
    private int xIndex;
    private int yIndex;
    private List<CourseTeacherRoom> courseIdList;
    private int type;//课程类型
    private String subjectName;



    public CourseItemDTO() {
    }

    public CourseItemDTO(CourseItem courseItem) {
        this.id = courseItem.getId().toString();
        this.xIndex = courseItem.getXIndex();
        this.yIndex = courseItem.getYIndex();
        List<ObjectId> courseIdList = courseItem.getCourse();
        List<CourseTeacherRoom> courseStrList = new ArrayList<CourseTeacherRoom>();
        if (courseIdList != null && !courseIdList.isEmpty()) {
            for (ObjectId o : courseIdList) {
                CourseTeacherRoom ctr=new CourseTeacherRoom();
                ctr.setCourseId(o.toString());
                courseStrList.add(ctr);
            }
        }
        this.courseIdList = courseStrList;
        this.type = courseItem.getType();
    }

    public CourseItem export() {
        CourseItem courseItem = new CourseItem();
        courseItem.setId(new ObjectId(this.getId()));
        courseItem.setXIndex(this.getxIndex());
        courseItem.setYIndex(this.getyIndex());
        courseItem.setType(this.type);
        List<CourseTeacherRoom> courseIdStr = this.getCourseIdList();
        List<ObjectId> courseIdList = new ArrayList<ObjectId>();
        if (courseIdStr != null && !courseIdStr.isEmpty()) {
            for (CourseTeacherRoom str : courseIdStr) {
                courseIdList.add(new ObjectId(str.getCourseId()));
            }
        }
        courseItem.setCourse(courseIdList);
        return courseItem;
    }


    @Override
    public int hashCode() {
        return this.xIndex * 31 + this.yIndex * 15 - 10;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof CourseItemDTO) {
            CourseItemDTO courseItemDTO = (CourseItemDTO) o;
            if (courseItemDTO.getxIndex() == this.xIndex &&
                    courseItemDTO.getyIndex() == this.yIndex) {
                List<String> thisCurseIdList = new ArrayList<String>();
                for (CourseTeacherRoom ctr : courseIdList) {
                    thisCurseIdList.add(ctr.getCourseId());
                }

                for (CourseTeacherRoom ctr : courseItemDTO.getCourseIdList()) {
                    if (!thisCurseIdList.contains(ctr.getCourseId())) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getxIndex() {
        return xIndex;
    }

    public void setxIndex(int xIndex) {
        this.xIndex = xIndex;
    }

    public int getyIndex() {
        return yIndex;
    }

    public void setyIndex(int yIndex) {
        this.yIndex = yIndex;
    }

    public List<CourseTeacherRoom> getCourseIdList() {
        return courseIdList;
    }

    public void setCourseIdList(List<CourseTeacherRoom> courseIdList) {
        this.courseIdList = courseIdList;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }
}
