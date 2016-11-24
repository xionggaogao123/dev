package com.fulaan.zouban.dto;

import com.pojo.zouban.ClassFengDuanEntry;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qiangm on 2016/4/26.
 */
public class ClassFenDuanDTO {
    private String fenDuanId;
    private String xuankeId;
    private int fenDuanIndex;
    private String fenDuanName;
    private List<String> classIds;
    private List<String> classNames;

    public ClassFenDuanDTO() {
    }

    public ClassFenDuanDTO(String fenDuanId, String xuankeId, int fenDuanIndex, String fenDuanName, List<String> classIds, List<String> classNames) {
        this.fenDuanId = fenDuanId;
        this.xuankeId = xuankeId;
        this.fenDuanIndex = fenDuanIndex;
        this.fenDuanName = fenDuanName;
        this.classIds = classIds;
        this.classNames = classNames;
    }

    public ClassFenDuanDTO(ClassFengDuanEntry entry) {
        this.xuankeId = entry.getXuanKeId().toString();
        this.fenDuanId = entry.getID().toString();
        this.fenDuanIndex = entry.getGroup();
        this.fenDuanName = entry.getGroupName();
        List<ObjectId> classObjIds = entry.getClassIds();
        List<String> classIds = new ArrayList<String>();
        for (ObjectId objId : classObjIds) {
            classIds.add(objId.toString());
        }
        this.classIds = classIds;
    }

    public ClassFengDuanEntry export() {
        ClassFengDuanEntry classFengDuanEntry = new ClassFengDuanEntry();
        if(fenDuanId != null){
            classFengDuanEntry.setID(new ObjectId(fenDuanId));
        }
        classFengDuanEntry.setXuanKeId(new ObjectId(xuankeId));
        classFengDuanEntry.setGroup(fenDuanIndex);
        classFengDuanEntry.setGroupName(fenDuanName);
        List<ObjectId> classObjIds = new ArrayList<ObjectId>();
        for (String str : classIds) {
            classObjIds.add(new ObjectId(str));
        }
        classFengDuanEntry.setClassIds(classObjIds);

        return classFengDuanEntry;
    }



    public String getFenDuanId() {
        return fenDuanId;
    }

    public void setFenDuanId(String fenDuanId) {
        this.fenDuanId = fenDuanId;
    }

    public String getXuankeId() {
        return xuankeId;
    }

    public void setXuankeId(String xuankeId) {
        this.xuankeId = xuankeId;
    }

    public int getFenDuanIndex() {
        return fenDuanIndex;
    }

    public void setFenDuanIndex(int fenDuanIndex) {
        this.fenDuanIndex = fenDuanIndex;
    }

    public String getFenDuanName() {
        return fenDuanName;
    }

    public void setFenDuanName(String fenDuanName) {
        this.fenDuanName = fenDuanName;
    }

    public List<String> getClassIds() {
        return classIds;
    }

    public void setClassIds(List<String> classIds) {
        this.classIds = classIds;
    }

    public List<String> getClassNames() {
        return classNames;
    }

    public void setClassNames(List<String> classNames) {
        this.classNames = classNames;
    }
}
