package com.fulaan.examresult.service;

import com.db.examresult.ExamResultDao;
import com.db.examresult.PerformanceDao;
import com.pojo.examresult.ExamResultEntry;
import com.pojo.examresult.PerformanceEntry;
import com.pojo.examresult.Score;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by fl on 2015/7/3.
 */
@Service
public class PerformanceService {
    private PerformanceDao performanceDao = new PerformanceDao();
    @Resource
    private ExamResultService examResultService;

//    public PerformanceEntry getPerformanceEntryById(ObjectId pId) {
//        return performanceDao.getPerformanceById(pId);
//    }
    public PerformanceEntry getPerformanceEntryByExamId(ObjectId examId) {
        return examResultService.getPerformanceEntryList(examId, null, null, null).get(0);
    }

    public List<ObjectId> getSubjectListByExamId(ObjectId examId) {
        List<Score> scoreList = getPerformanceEntryByExamId(examId).getScoreList();
        List<ObjectId> subjectList = new ArrayList<ObjectId>();
        for(Score score : scoreList) {
            subjectList.add(score.getSubjectId());
        }
        return subjectList;
    }
}
