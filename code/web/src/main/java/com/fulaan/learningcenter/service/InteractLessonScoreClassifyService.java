package com.fulaan.learningcenter.service;

import com.db.school.InteractLessonScoreClassifyDao;
import com.fulaan.learningcenter.dto.InteractLessonScoreClassifyDTO;
import com.fulaan.myclass.service.ClassService;
import com.pojo.school.ClassEntry;
import com.pojo.school.InteractLessonEntry;
import com.pojo.school.InteractLessonScoreClassifyEntry;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by guojing on 2015/11/24.
 */
@Service
public class InteractLessonScoreClassifyService {

    private static final Logger logger =Logger.getLogger(InteractLessonScoreClassifyService.class);

    InteractLessonScoreClassifyDao interactLessonScoreClassifyDao=new InteractLessonScoreClassifyDao();

    @Resource
    private InteractLessonService interactLessonService;

    @Resource
    private ClassService classService;

    //记录考试成绩分布次数
    private static Map<String,Integer> timesMap = new HashMap<String,Integer>();

    /**
     * 新建互动课堂考试
     * @param e
     * @return
     */
    public ObjectId addInteractLessonScoreClassifyEntry(InteractLessonScoreClassifyEntry e)
    {
        return interactLessonScoreClassifyDao.addInteractLessonScoreClassifyEntry(e);
    }

    /**
     * 新建互动课堂考试成绩分布
     * @param content
     */
    public void addInteractLessonScoreClassifyEntry(String content) {
        try {
            //将json字符串转换成json对象
            JSONObject dataJson=new JSONObject(content);
            //获取互动课堂Id
            String lessonId=dataJson.getString("lessonId");
            ObjectId lid=new ObjectId(lessonId);
            InteractLessonEntry lessonEntry=interactLessonService.getInteractLessonEntryById(lid);
            ClassEntry e=classService.getClassEntryById(lessonEntry.getClassId(), null);
            int totalStudent=1;
            if(e!=null){
                totalStudent=e.getTotalStudent()==0?1:e.getTotalStudent();
            }
            //考试次数
            int times =dataJson.getInt("number");
            String key=lessonId;
            if(timesMap.get(key)==null){
                timesMap.put(key,times);
            }else{
                times=timesMap.get(key)+1;
                timesMap.put(key,times);
            }
            //优秀人数
            int excellentNum =dataJson.getInt("excellent");
            //优秀率
            int excellentRate=excellentNum*100/totalStudent;

            //良好人数
            int goodNum =dataJson.getInt("pass");
            //良好率
            int goodRate=goodNum*100/totalStudent;

            //不及格人数
            int failureNum =dataJson.getInt("fail");
            //不及格率
            int failureRate=failureNum*100/totalStudent;

            //添加考试详细信息集合
            InteractLessonScoreClassifyEntry entry=new InteractLessonScoreClassifyEntry(
                    lid,
                    times,
                    excellentNum,
                    excellentRate,
                    goodNum,
                    goodRate,
                    failureNum,
                    failureRate
            );

            addInteractLessonScoreClassifyEntry(entry);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询课堂考试成绩分布
     * @param lessonId
     * @param times
     * @return
     */
    public InteractLessonScoreClassifyDTO getInteractLessonScoreClassifyDTO(ObjectId lessonId, int times) {
        InteractLessonScoreClassifyEntry entry=interactLessonScoreClassifyDao.findInteractLessonScoreClassifyEntry(lessonId, times);
        InteractLessonScoreClassifyDTO dto=new InteractLessonScoreClassifyDTO(entry);
        return dto;
    }
}
