package com.fulaan.classroom.service;

import com.db.classroom.ClassroomDao;
import com.db.school.ClassDao;
import com.db.zouban.*;
import com.fulaan.classroom.ClassRoomDTO;
import com.pojo.classroom.ClassroomEntry;
import com.pojo.school.ClassEntry;
import com.pojo.zouban.*;
import com.sys.constants.Constant;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by qiangm on 2015/10/23.
 */
@Service
public class ClassroomService {
    private ClassroomDao classroomDao = new ClassroomDao();

    private ClassDao classDao = new ClassDao();
    private ZouBanCourseDao zouBanCourseDao = new ZouBanCourseDao();
    private XuanKeConfDao xuanKeConfDao = new XuanKeConfDao();
    private FenDuanDao fenDuanDao = new FenDuanDao();


    /**
     * 获取教室列表
     *
     * @param schoolId
     * @return
     */
    public List<ClassRoomDTO> findClassroomList(ObjectId schoolId) {
        List<ClassRoomDTO> classRoomDTOList = new ArrayList<ClassRoomDTO>();
        List<ClassroomEntry> classroomEntryList = classroomDao.findClassRoomEntryList(schoolId);
        for (ClassroomEntry c : classroomEntryList) {
            classRoomDTOList.add(new ClassRoomDTO(c));
        }
        return classRoomDTOList;
    }


    /**
     * 获取教室列表
     *
     * @param schoolId
     * @return
     */
    public List<ClassRoomDTO> findFreeClassroomList(ObjectId schoolId, String year, String classroom, int x, int y) {
        List<ObjectId> classroomIdList = new ArrayList<ObjectId>();
        List<ClassRoomDTO> classRoomDTOList = new ArrayList<ClassRoomDTO>();
        List<ClassroomEntry> classroomEntryList = classroomDao.findClassRoomEntryList(schoolId);
        for (ClassroomEntry c : classroomEntryList) {
            classroomIdList.add(c.getID());
        }
        //获取全校课表
        List<TimeTableEntry> timeTableEntryList = new TimeTableDao().findAllTimeTable(year, schoolId, 3, 0);
        List<ObjectId> courseIdList = new ArrayList<ObjectId>();
        for (TimeTableEntry timeTableEntry : timeTableEntryList) {
            List<CourseItem> courseItemList = timeTableEntry.getCourseList();
            for (CourseItem courseItem : courseItemList) {
                if (courseItem.getXIndex() == x && courseItem.getYIndex() == y) {
                    courseIdList.addAll(courseItem.getCourse());
                }
            }
        }

        List<ZouBanCourseEntry> zouBanCourseEntries = zouBanCourseDao.findCourseListByIds(courseIdList);
        for (ZouBanCourseEntry zouBanCourseEntry : zouBanCourseEntries) {
            for (ObjectId classroomId : classroomIdList) {
                if (zouBanCourseEntry.getClassRoomId()!=null&&zouBanCourseEntry.getClassRoomId().equals(classroomId)) {
                    classroomIdList.remove(classroomId);
                    break;
                }
            }
        }
        if (!StringUtils.isEmpty(classroom))
            classroomIdList.add(new ObjectId(classroom));
        for (ClassroomEntry c : classroomEntryList) {
            if (classroomIdList.contains(c.getID())) {
                classRoomDTOList.add(new ClassRoomDTO(c));
            }
        }
        return classRoomDTOList;
    }

    /**
     * 获取教室列表,小走班使用
     *
     * @param schoolId
     * @return
     */
    public List<ClassRoomDTO> findFreeClassroomListForXZB(ObjectId schoolId,String gradeId, String year, String classroom, int x, int y) {
        List<ObjectId> classroomIdList = new ArrayList<ObjectId>();
        List<ClassRoomDTO> classRoomDTOList = new ArrayList<ClassRoomDTO>();
        List<ClassroomEntry> classroomEntryList = classroomDao.findClassRoomEntryList(schoolId);
        for (ClassroomEntry c : classroomEntryList) {
            classroomIdList.add(c.getID());
        }
        //获取全校课表
        List<TimeTableEntry> timeTableEntryList = new TimeTableDao().findAllTimeTable(year, schoolId, 3, 0);
        List<ObjectId> courseIdList = new ArrayList<ObjectId>();
        for (TimeTableEntry timeTableEntry : timeTableEntryList) {
            List<CourseItem> courseItemList = timeTableEntry.getCourseList();
            for (CourseItem courseItem : courseItemList) {
                if (courseItem.getXIndex() == x && courseItem.getYIndex() == y) {
                    courseIdList.addAll(courseItem.getCourse());
                }
            }
        }

        List<ZouBanCourseEntry> zouBanCourseEntries = zouBanCourseDao.findCourseListByIds(courseIdList);
        for (ZouBanCourseEntry zouBanCourseEntry : zouBanCourseEntries) {
            for (ObjectId classroomId : classroomIdList) {
                if (zouBanCourseEntry.getClassRoomId()!=null&&zouBanCourseEntry.getClassRoomId().equals(classroomId)) {
                    classroomIdList.remove(classroomId);
                    break;
                }
            }
        }
        if (!StringUtils.isEmpty(classroom))
            classroomIdList.add(new ObjectId(classroom));
        for (ClassroomEntry c : classroomEntryList) {
            if (classroomIdList.contains(c.getID())) {
                classRoomDTOList.add(new ClassRoomDTO(c));
            }
        }
        //遍历所有段，除去该时间没有走班课的班级教室
        XuankeConfEntry xuankeConfEntry =  xuanKeConfDao.findXuanKeConf(year, new ObjectId(gradeId));
        List<ClassFengDuanEntry> classFengDuanEntries=fenDuanDao.getClassFenduanList(xuankeConfEntry.getID());
        for (ClassFengDuanEntry classFengDuanEntry:classFengDuanEntries)
        {
            List<ObjectId> classIds = classFengDuanEntry.getClassIds();
            boolean have=false;//是否有课，均无课则去掉所有班级教室
            for (ObjectId classId:classIds)
            {
                for (TimeTableEntry timeTableEntry : timeTableEntryList) {
                    if(timeTableEntry.getClassId().equals(classId))
                    {
                        List<CourseItem> courseItemList=timeTableEntry.getCourseList();
                        for (CourseItem courseItem:courseItemList)
                        {
                            if(courseItem.getXIndex()==x && courseItem.getYIndex()==y && courseItem.getType()==0)
                            {
                                have=true;
                                break;
                            }
                        }
                        break;
                    }
                }
            }
            if(!have)
            {
                for (ObjectId classId:classIds) {
                    for (ClassRoomDTO classRoomDTO : classRoomDTOList) {
                        if (classRoomDTO.getClassId().equals(classId.toString()))
                        {
                            classRoomDTOList.remove(classRoomDTO);
                            break;
                        }
                    }
                }
            }
        }
        return classRoomDTOList;
    }

    /**
     * 获取教室列表---分页
     *
     * @param schoolId
     * @return
     */
    public List<ClassRoomDTO> findClassroomList(ObjectId schoolId, int page, int pageSize) {
        List<ClassRoomDTO> classRoomDTOList = new ArrayList<ClassRoomDTO>();
        List<ClassroomEntry> classroomEntryList = classroomDao.findClassRoomEntryList(schoolId, page, pageSize);
        List<ClassEntry> classEntryList = classDao.findClassInfoBySchoolId(schoolId, null);
        Map<ObjectId, ClassEntry> classMap = new HashMap<ObjectId, ClassEntry>();
        if (classEntryList != null && classEntryList.size() != 0) {
            for (ClassEntry cls : classEntryList) {
                classMap.put(cls.getID(), cls);
            }
        }

        for (ClassroomEntry c : classroomEntryList) {
            if (c.getClassId() == null) {
                classRoomDTOList.add(new ClassRoomDTO(c));
            } else {
                ClassRoomDTO classRoomDTO = new ClassRoomDTO(c);
                classRoomDTO.setClassName(classMap.get(c.getClassId()) != null ? classMap.get(c.getClassId()).getName() : "其他");
                classRoomDTOList.add(classRoomDTO);
            }

        }
        return classRoomDTOList;
    }

    /**
     * 统计总数
     *
     * @param schoolId
     * @return
     */
    public int count(ObjectId schoolId) {
        return classroomDao.count(schoolId);
    }

    /**
     * 增加教室
     *
     * @param schoolId
     * @param name
     */
    public void addClassRoom(ObjectId schoolId, String name, String classid) {
        ClassroomEntry classroomEntry = new ClassroomEntry(schoolId, name, new ObjectId(classid));
        classroomDao.addClassRoom(classroomEntry);
    }

    /**
     * 删除教室
     *
     * @param classRoomId
     */
    public void deleteClassRoom(String classRoomId) {
        classroomDao.remove(new ObjectId(classRoomId));
    }

    /**
     * 修改教室
     *
     * @param classRoomId
     * @param name
     */
    public void updateClassroom(String classRoomId, String name,  String classid) {
        ClassroomEntry classroomEntry = new ClassroomEntry();
        classroomEntry.setID(new ObjectId(classRoomId));
        classroomEntry.setRoomName(name);
        if (!StringUtils.isEmpty(classid)) {
            classroomEntry.setClassId(new ObjectId(classid));
        }
        classroomDao.updateClassRoom(classroomEntry);
    }

    /**
     * 检查是否存在
     *
     * @param schoolId
     * @param name
     * @return
     */
    public boolean isExist(String schoolId, String name) {
        return classroomDao.classroomIsExisted(new ObjectId(schoolId), name);
    }



    /**
     * 获取classroomEntry
     *
     * @param classRoomIds
     * @return
     */
    public Map<ObjectId, ClassroomEntry> findClassRoomEntryMap(List<ObjectId> classRoomIds) {
        return classroomDao.findClassRoomEntryMap(classRoomIds, Constant.FIELDS);
    }


    /**
     * 根据id查找entry
     *
     * @param classroomId
     * @return
     */
    public ClassroomEntry findEntryById(ObjectId classroomId) {
        return classroomDao.findClassRoomById(classroomId);
    }


}
