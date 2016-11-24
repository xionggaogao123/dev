package com.db.temp;

import com.db.lesson.LessonDao;
import com.db.school.ExerciseItemDao;
import com.db.school.HomeWorkDao;
import com.pojo.lesson.LessonEntry;
import com.pojo.school.HomeWorkEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by fl on 2015/9/22.
 */
public class UpdateHomeworkFields {
    public static void main(String[] args) {
        //查找所有作业
        HomeWorkDao homeWorkDao = new HomeWorkDao();
        LessonDao lessonDao = new LessonDao();
        ExerciseItemDao exerciseItemDao = new ExerciseItemDao();
        List<HomeWorkEntry> homeWorkEntryList = homeWorkDao.findAllHomeworkEntry();
        if (homeWorkEntryList != null && homeWorkEntryList.size() > 0) {
            long size = homeWorkEntryList.size();
            long i = 0;
            for (HomeWorkEntry homeWorkEntry : homeWorkEntryList) {
                i++;
                System.out.println("==============================" + i + "/" + size);
                System.out.print("homeworkId=" + homeWorkEntry.getID().toString());
                LessonEntry lessonEntry = lessonDao.getLessonEntry(homeWorkEntry.getLessonId(), Constant.ZERO, Constant.FIVE);
                homeWorkEntry.setVideoNum(lessonEntry.getVideoIds().size());
                homeWorkEntry.setFileNum(lessonEntry.getDocumentCount());
                homeWorkEntry.setVoiceNum(homeWorkEntry.getVoiceFile().size());

                List<ObjectId> exercises = new ArrayList<ObjectId>();
                ObjectId exerciseId = lessonEntry.getExercise();
                exercises.add(exerciseId);
                Map<ObjectId, Integer> map = exerciseItemDao.statItemCount(exercises);
                Integer exerciseNum = exerciseId == null ? new Integer(0) : map.get(exerciseId);
                if (exerciseNum == null) {
                    homeWorkEntry.setExerciseNum(0);
                } else {
                    homeWorkEntry.setExerciseNum(exerciseNum);
                }
                homeWorkDao.updateHomeWork(homeWorkEntry);
                System.out.println("    更新成功");

            }
        } else {
            System.out.println("没查到作业");
        }
    }
}
