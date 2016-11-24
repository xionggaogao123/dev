package com.fulaan.zouban.service;

import com.db.zouban.EventConflictDao;
import com.db.zouban.TimetableConfDao;
import com.db.zouban.ZouBanCourseDao;
import com.fulaan.examresult.controller.IdNameDTO;
import com.fulaan.myclass.service.ClassService;
import com.fulaan.zouban.dto.EventConflictDTO;
import com.pojo.school.ClassInfoDTO;
import com.pojo.zouban.*;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by wangkaidong on 2016/10/10.
 */
@Service
public class EventConflictService {
    private EventConflictDao eventConflictDao = new EventConflictDao();
    private TimetableConfDao timetableConfDao = new TimetableConfDao();
    private ZouBanCourseDao zouBanCourseDao = new ZouBanCourseDao();

    @Autowired
    private ClassService classService;


    /**
     * 获取所有事务冲突
     *
     * @param term
     * @param gradeId
     * @return
     */
    public List<EventConflictDTO> getConflictList(String term, ObjectId gradeId) {
        List<EventConflictEntry> eventConflictEntries = eventConflictDao.findEventConflict(term, gradeId);
        TimetableConfEntry timetableConfEntry = timetableConfDao.getTimetableConf(term, gradeId);
        List<TimetableConfEntry.Event> eventList = timetableConfEntry.getEvent();

        Map<ObjectId, String> eventIdNameMap = new HashMap<ObjectId, String>();
        for (TimetableConfEntry.Event event : eventList) {
            eventIdNameMap.put(event.getID(), event.getName());
        }

        List<ClassInfoDTO> classInfoDTOList = classService.findClassByGradeId(gradeId.toString());
        Map<ObjectId, String> classNameMap = new HashMap<ObjectId, String>();

        for (ClassInfoDTO classInfoDTO : classInfoDTOList) {
            classNameMap.put(new ObjectId(classInfoDTO.getId()), classInfoDTO.getClassName());
        }

        List<EventConflictDTO> eventConflictDTOs = new ArrayList<EventConflictDTO>();


        for (EventConflictEntry eventConflictEntry : eventConflictEntries) {
            ZouBanCourseEntry zouBanCourseEntry = zouBanCourseDao.getCourseInfoById(eventConflictEntry.getCourseId());
            if (zouBanCourseEntry != null) {
                String className = "";
                if (zouBanCourseEntry.getType() == ZoubanType.FEIZOUBAN.getType() ||
                        zouBanCourseEntry.getType() == ZoubanType.ODDEVEN.getType()) {
                    if (classNameMap.containsKey(zouBanCourseEntry.getClassId().get(0))) {
                        className = classNameMap.get(zouBanCourseEntry.getClassId().get(0));
                    }
                }
                EventConflictDTO eventConflictDTO = new EventConflictDTO(eventIdNameMap.get(eventConflictEntry.getEventId()),
                        zouBanCourseEntry.getTeacherName(), zouBanCourseEntry.getClassName(),
                        eventConflictEntry.getX(), eventConflictEntry.getY(), className);
                eventConflictDTOs.add(eventConflictDTO);
            }
        }
        return eventConflictDTOs;
    }


    /**
     * 获取某个时间点的事务
     *
     * @param term
     * @param gradeId
     * @param x
     * @param y
     * @return
     */
    private List<TimetableConfEntry.Event> getEventList(String term, ObjectId gradeId, int x, int y) {
        TimetableConfEntry timetableConfEntry = timetableConfDao.getTimetableConf(term, gradeId);
        List<TimetableConfEntry.Event> eventList = timetableConfEntry.getEvent();
        List<TimetableConfEntry.Event> result = new ArrayList<TimetableConfEntry.Event>();
        for (TimetableConfEntry.Event event : eventList) {
            if (event.getPointList().contains(new PointEntry(x, y))) {
                result.add(event);
            }
        }

        return result;
    }


    /**
     * 检查老师是否有事务
     *
     * @param teacherId
     * @param eventList
     * @return
     */
    private ObjectId checkTeacherEvent(ObjectId teacherId, List<TimetableConfEntry.Event> eventList) {
        for (TimetableConfEntry.Event event : eventList) {
            List<ObjectId> teacherIdList = new ArrayList<ObjectId>();
            for (IdNamePair idNamePair : event.getTeacherList()) {
                teacherIdList.add(idNamePair.getId());
            }
            if (teacherIdList.contains(teacherId)) {
                return event.getID();
            }
        }
        return null;
    }


    /**
     * 排课添加冲突
     *
     * @param term
     * @param gradeId
     * @param x
     * @param y
     * @param courseIdList
     */
    public void addConflict(String term, ObjectId gradeId, int x, int y, Collection<ObjectId> courseIdList) {
        List<TimetableConfEntry.Event> eventList = getEventList(term, gradeId, x, y);
        List<ZouBanCourseEntry> zouBanCourseEntryList = zouBanCourseDao.findCourseListByIds(new ArrayList<ObjectId>(courseIdList));
        for (ZouBanCourseEntry zouBanCourseEntry : zouBanCourseEntryList) {
            ObjectId teacherId = zouBanCourseEntry.getTeacherId();
            ObjectId eventId = checkTeacherEvent(teacherId, eventList);
            if (eventId != null) {
                EventConflictEntry eventConflictEntry = new EventConflictEntry(term, gradeId, x, y, eventId, teacherId, zouBanCourseEntry.getID());
                eventConflictDao.addEventConflict(eventConflictEntry);
            }
        }
    }

    /**
     * 根据课程删除冲突
     *
     * @param term
     * @param gradeId
     * @param x
     * @param y
     */
    public void removeConflictByCourseList(String term, ObjectId gradeId, int x, int y, Collection<ObjectId> courseIdList) {
        eventConflictDao.removeEventConflictByCourseList(term, gradeId, x, y, new ArrayList<ObjectId>(courseIdList));
    }

    /**
     * 根据事务删除冲突
     *
     * @param eventId
     */
    public void removeEventConflictByEventId(ObjectId eventId) {
        eventConflictDao.removeEventConflictByEventId(eventId);
    }

    /**
     * 删除某天或某节的冲突
     *
     * @param term
     * @param gradeList
     * @param x
     * @param y
     */
    public void removeEventConflictByXY(String term, List<ObjectId> gradeList, int x, int y) {
        for (ObjectId gradeId : gradeList) {
            eventConflictDao.removeEventConflictByXY(term, gradeId, x, y);
        }
    }


    /**
     * 根据老师删除冲突
     *
     * @param term
     * @param gradeId
     * @param x
     * @param y
     * @param teacherIdList
     */
    public void removeConflictByTeacherList(String term, ObjectId gradeId, int x, int y, List<ObjectId> teacherIdList) {
        eventConflictDao.removeEventConflictByTeacherList(term, gradeId, x, y, teacherIdList);
    }

    public void removeConflictByCourseIdList(List<ObjectId> idList){
        eventConflictDao.removeEventConfictByCourseIdList(idList);
    }

}
