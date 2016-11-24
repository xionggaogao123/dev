package com.db.temp;

import com.db.examresult.ExamResultDao;
import com.pojo.examresult.ExamResultEntry;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fl on 2015/9/14.
 */
public class UpdateTerm {
    private static ExamResultDao examResultDao = new ExamResultDao();

    public static void main(String[] args) {
        List<String> nameList = new ArrayList<String>();
        for (String name : nameList) {
            List<ExamResultEntry> examResultEntryList = examResultDao.getExamResultEntryByName(name);
            if (examResultEntryList.size() == 1) {
                ExamResultEntry examResultEntry = examResultEntryList.get(0);
                examResultEntry.setSchoolYear("2015-2016学年第一学期");
                examResultDao.updateInfo(examResultEntry.getID(), examResultEntry);
            } else {
                System.out.println("不能确认考试");
            }
        }

    }
}
