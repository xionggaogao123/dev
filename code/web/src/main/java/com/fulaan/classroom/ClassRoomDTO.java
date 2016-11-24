package com.fulaan.classroom;

import com.pojo.classroom.ClassroomEntry;
import org.bson.types.ObjectId;

/**
 * Created by qiangm on 2015/10/23.
 */
public class ClassRoomDTO {
    private String id;
    private String roomName;
    private String schoolId;
    private String classId;
    private String className;

    public ClassRoomDTO() {}

    public ClassRoomDTO(ClassroomEntry classroomEntry) {
        this.id = classroomEntry.getID().toString();
        this.roomName = classroomEntry.getRoomName();
        this.schoolId = classroomEntry.getSchoolId().toString();
        if (classroomEntry.getClassId() != null) {
            this.classId = classroomEntry.getClassId().toString();
        } else {
            this.classId = "";
            this.className = "";
        }

    }

    public ClassroomEntry exportEntry(){
        ClassroomEntry classroomEntry = new ClassroomEntry();
        if (id != null && id.equals("")) {
            classroomEntry.setID(new ObjectId(id));
        }
        classroomEntry.setSchoolId(new ObjectId(schoolId));
        classroomEntry.setClassId(new ObjectId(classId));
        classroomEntry.setRoomName(roomName);

        return classroomEntry;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
