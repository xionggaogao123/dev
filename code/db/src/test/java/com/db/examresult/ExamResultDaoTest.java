package com.db.examresult;

import com.mongodb.DBCursor;
import com.pojo.examresult.ExamResultEntry;
import com.pojo.examresult.PerformanceEntry;
//import com.pojo.jointexam.JointExamEntry;
import org.bson.types.ObjectId;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fl on 2015/6/12.
 */
public class ExamResultDaoTest {
    ExamResultDao dao = new ExamResultDao();
    ObjectId oid = null;

    @Test
    public void addExamResultEntry()
    {
//        ExamResultEntry e = new ExamResultEntry("月考", "其他");
//        oid = dao.add(e);
//        System.out.println(e.getID());
//        System.out.println(oid);
    }
//    new ObjectId("54f16ba6fe5bfcc6c96b4ba3"), new ObjectId("54f16d1efe5b185d3fcbff98"), "销售", null,100  557a98322dac4d33afbfeeed

    @Test
    public void find() {
        addExamResultEntry();
        ExamResultEntry e = dao.getExamResultEntry(oid);
        System.out.println(oid);
        System.out.println(e.getID());
    }

    @Test
    public void findPerformanceList() {
        updatePerformanceList();
        List<ObjectId> performanceList = dao.getPerformanceList(oid);
        for(ObjectId i : performanceList) {
            System.out.println(i);
        }
    }

    @Test
    public void updatePerformanceList() {
        addExamResultEntry();
        List<ObjectId> performanceList = new ArrayList<ObjectId>();
        for(int i=0; i<10; i++) {
            performanceList.add(new ObjectId("54f16ba6fe5bfcc6c96b4ba3"));
        }
        dao.update(oid, performanceList);
    }

    @Test
    public void delete() {
        oid = new ObjectId("557a977f2dacd16a59455192");
        dao.delete(oid);
    }
//    @Test
//    public void test(){
//        JointExamDao jointExamDao = new JointExamDao();
//        JointExamEntry jointExamEntry = jointExamDao.find(new ObjectId("562d99812dac52108161c939"));
//        jointExamEntry.setEduId(new ObjectId("55d84c31132f244a095ce2ba"));
//        jointExamDao.update(jointExamEntry);
//    }
}
