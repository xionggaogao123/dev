package com.db.examresult;

import com.db.factory.MongoFacroty;
import com.pojo.examresult.PerformanceEntry;
import com.pojo.examresult.Score;
import org.bson.types.ObjectId;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fl on 2015/7/14.
 */
public class PerformanceDaoTest {
    PerformanceDao performanceDao = new PerformanceDao();

    @Test
    public void testDeleteSubject() {
        System.out.println(MongoFacroty.getAppDB());
        ObjectId performanceId = new ObjectId("5593afde2dacfb2a2cef50dd");
        ObjectId subjectId = new ObjectId("55935198f6f28b7261c9bf7e");
        performanceDao.deleteSubject(performanceId, subjectId);

    }

    @Test
    public void testFindPassCount(){
        System.out.println(MongoFacroty.getAppDB());
        ObjectId examId = new ObjectId("55ed68212dacfbf58ba2f5d0");
        ObjectId subjectId = new ObjectId("55935198f6f28b7261c9bf7e");
        int num = performanceDao.findPassCount(examId,subjectId,60);
        System.out.println(num);
    }

    @Test
    public void insertData(){
        double sbjScore = 77D; //===
        Score score = new Score(new ObjectId("55935198f6f28b7261c9bf80"),"语文", sbjScore, 0, 0, 100, 60);
        Score score1 = new Score(new ObjectId("55935198f6f28b7261c9bf81"),"数学", sbjScore, 0, 0, 100, 60);
        List<Score> scoreList = new ArrayList<Score>();
        scoreList.add(score);
        scoreList.add(score1);

        ObjectId studentId = new ObjectId("55935198f6f28b7261c9bf13"); //====
        String studentName = "李四"; //====
        ObjectId classId = new ObjectId("55935198f6f28b7261c9bf41");
        PerformanceEntry performanceEntry = new PerformanceEntry(new ObjectId("55c992b02dacf6eae56fb517"), studentId, studentName, classId, scoreList);
        performanceEntry.setScoreSum(sbjScore*2);
        performanceEntry.setAreaExamId(new ObjectId("562d99812dac52108161c939"));
        performanceEntry.setSchoolRanking(4);
        performanceEntry.setAreaRanking(9);
        ObjectId pId = performanceDao.save(performanceEntry);
        System.out.println(pId);
    }
}
