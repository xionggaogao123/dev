package com.fulaan.learningcenter.service;

import com.db.school.InteractLessonExamDetailDao;
import com.mongodb.DBObject;
import com.pojo.school.InteractLessonExamDetailEntry;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by guojing on 2015/11/24.
 */
@Service
public class InteractLessonExamDetailService {

    private static final Logger logger =Logger.getLogger(InteractLessonExamDetailService.class);

    InteractLessonExamDetailDao interactLessonExamDetailDao=new InteractLessonExamDetailDao();

    /**
     * 新建互动课堂考试
     * @param e
     * @return
     */
    public ObjectId addInteractLessonExamDetailEntry(InteractLessonExamDetailEntry e)
    {
        return interactLessonExamDetailDao.addInteractLessonExamDetailEntry(e);
    }

    /**
     * 新建互动课堂考试
     * @param list
     * @return
     */
    public void addInteractLessonExamDetailEntryList(List<DBObject> list)
    {
        interactLessonExamDetailDao.addInteractLessonExamDetailEntryList(list);
    }

    /**
     * 查询考试信息
     * @param lessonId
     * @param type
     * @param times
     * @param number
     * @param fields
     * @param orderBy
     * @return
     */
    public List<InteractLessonExamDetailEntry> getExamDetailEntryList(ObjectId lessonId, int type, int times, int number, DBObject fields, String orderBy) {
        return interactLessonExamDetailDao.findExamDetailEntryList(lessonId, type, times, number, fields, orderBy);
    }

    /**
     * 查询考试信息
     * @param lessonId
     * @param type
     * @param times
     * @param fields
     * @param orderBy
     * @return
     */
    public Map<ObjectId,List<InteractLessonExamDetailEntry>> getExamDetailEntryMap(ObjectId lessonId, int type, int times, DBObject fields, String orderBy) {
        return interactLessonExamDetailDao.getExamDetailEntryMap(lessonId, type, times, fields, orderBy);
    }
}
