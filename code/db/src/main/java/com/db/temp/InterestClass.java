package com.db.temp;

import com.db.lesson.DirDao;
import com.db.school.InterestClassDao;
import com.db.school.SchoolDao;
import com.db.school.TeacherClassSubjectDao;
import com.mongodb.BasicDBObject;
import com.pojo.app.IdNameValuePair;
import com.pojo.app.IdValuePair;
import com.pojo.lesson.DirEntry;
import com.pojo.lesson.DirType;
import com.pojo.school.*;
import com.sys.constants.Constant;

import org.bson.types.ObjectId;

import java.io.IOException;
import java.util.*;

/**
 * Created by wang_xinxin on 2015/9/14.
 */
public class InterestClass {

    private InterestClassDao classdao = new InterestClassDao();
    private TeacherClassSubjectDao teacherClassSubjectDao = new TeacherClassSubjectDao();
    private SchoolDao schoolDao = new SchoolDao();
    private DirDao dirDao = new DirDao();

    public void deleteschoolinterest() throws IOException {
        List<InterestClassEntry> classlist = classdao.findClassBySchoolId(new ObjectId("55934c15f6f28b7261c19ca4"), 1, null, null);
        if (classlist != null && classlist.size() != 0) {
            for (InterestClassEntry entry : classlist) {
                classdao.deleteExpandClassById(entry.getID());
                //删除tcsubject中关系
                teacherClassSubjectDao.deleteById(entry.getID());
                ObjectId tclid = teacherClassSubjectDao.getTcsubjectId(entry.getID(), entry.getTeacherId(), entry.getSubjectId());
                if (tclid != null) {
                    teacherClassSubjectDao.deleteById(tclid);
                    dirDao.deleteByOwnId(tclid);
                }
            }
        }

    }

    public void insertclass() throws IOException {
        List<InterestClassEntry> classlist = classdao.findClassBySchoolId(new ObjectId("55934c14f6f28b7261c19c5e"), 1, null, null);
        List<InterestClassStudent> newstulist1 = new ArrayList<InterestClassStudent>();
        List<InterestClassStudent> newstulist2 = new ArrayList<InterestClassStudent>();
        InterestClassEntry centry = new InterestClassEntry();
        if (classlist != null && classlist.size() != 0) {
            for (InterestClassEntry entry : classlist) {
                ObjectId id = entry.getID();
                if (entry.getFirstTerm() == 1 || entry.getSecondTerm() == 1) {
                    List<InterestClassStudent> stulist = entry.getInterestClassStudents();
                    if (stulist != null && stulist.size() != 0) {
                        newstulist1 = new ArrayList<InterestClassStudent>();
                        newstulist2 = new ArrayList<InterestClassStudent>();
                        for (InterestClassStudent stu : stulist) {
                            if (stu.getCourseType() == 1) {
                                newstulist1.add(stu);
                            } else if (stu.getCourseType() == 2) {
                                newstulist2.add(stu);
                            }
                        }
                        ObjectId relationid1 = null;
                        ObjectId relationid2 = null;
                        String classname = entry.getClassName();
                        if (newstulist1 != null && newstulist1.size() != 0) {
                            entry.setInterestClassStudents(newstulist1);
                            entry.setClassName(classname + "短课1");
                            entry.setFirstTerm(1);
                            entry.setSecondTerm(0);
                            entry.setRelationId(new ObjectId());
                            entry.setID(new ObjectId());
                            relationid1 = classdao.addExpandClass(entry);
                            addTeacherSubject(relationid1, entry.getTeacherId(), entry.getSubjectId());
                        }
                        if (newstulist2 != null && newstulist2.size() != 0) {
                            entry.setID(new ObjectId());
                            entry.setFirstTerm(0);
                            entry.setSecondTerm(1);
                            entry.setInterestClassStudents(newstulist2);
                            entry.setRelationId(relationid1);
                            entry.setClassName(classname + "短课2");
                            relationid2 = classdao.addExpandClass(entry);
                            classdao.updateExpandClassRelation(relationid1, relationid2);
                            addTeacherSubject(relationid1, entry.getTeacherId(), entry.getSubjectId());
                        }

                    }
                    classdao.deleteExpandClassById(id);
                    //删除tcsubject中关系
                    teacherClassSubjectDao.deleteById(id);
                    ObjectId tclid = teacherClassSubjectDao.getTcsubjectId(id, entry.getTeacherId(), entry.getSubjectId());
                    if (tclid != null) {
                        teacherClassSubjectDao.deleteById(tclid);
                        dirDao.deleteByOwnId(tclid);
                    }

                }
            }
        }

    }

    private void addTeacherSubject(ObjectId classid, ObjectId teacherid, ObjectId subjectid) {
        InterestClassEntry classEntry = classdao.findEntryByClassId(classid);
        ObjectId schoolId = classEntry.getSchoolId();
        SchoolEntry schoolEntry = schoolDao.getSchoolEntry(schoolId, Constant.FIELDS);
        List<Subject> subjects = schoolEntry.getSubjects();
        Map<ObjectId, String> subjectMap = new HashMap<ObjectId, String>();
        for (Subject subject : subjects) {
            subjectMap.put(subject.getSubjectId(), subject.getName());
        }
        TeacherClassSubjectEntry teacherClassLessonEntry = new TeacherClassSubjectEntry(new BasicDBObject());
        teacherClassLessonEntry.setTeacherId(teacherid);
        teacherClassLessonEntry.setClassInfo(new IdValuePair(classid, classEntry.getClassName()));
        teacherClassLessonEntry.setSubjectInfo(new IdValuePair(subjectid, subjectMap.get(subjectid)));
        //插入teacherClassSubjectEntry
        ObjectId tclId = teacherClassSubjectDao.addTeacherClassSubjectEntry(teacherClassLessonEntry);
        //新建dir
        DirEntry entry = new DirEntry(tclId, classEntry.getClassName() + subjectMap.get(subjectid), null, 0, DirType.CLASS_LESSON);
        dirDao.addDirEntry(entry);
    }


    private void updateTerm() {
        classdao.deleteKey("tm");
        List<InterestClassEntry> list = classdao.findAllInterestClass();
        for (InterestClassEntry entry : list) {
//            if(entry.getSchoolId().toString().equals("55934c14f6f28b7261c19c62")){
            ObjectId classId = entry.getID();
//            entry.setTerm(getTerm(classId.getTime()));
            IdNameValuePair term = new IdNameValuePair(null, getTerm(classId.getTime()), entry.getTermType());
            classdao.updateTerm(classId, term);
//            }

        }
        System.out.println(list.size());
    }

    /**
     * 根据时间计算所处学期
     *
     * @param time
     * @return
     */
    private String getTerm(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String schoolYear;
        if (month < 9 && month > 2) {
            schoolYear = (year - 1) + "-" + year + "学年第二学期";
        } else if (month >= 9) {
            schoolYear = year + "-" + (year + 1) + "学年第一学期";
        } else {
            schoolYear = (year - 1) + "-" + year + "学年第一学期";
        }
        System.out.println(year + "/" + month + "/" + day + "       " + schoolYear);
        return schoolYear;
    }


    private void ss() {
        ObjectId schoolId = new ObjectId("55934c15f6f28b7261c19ca4");
        List<InterestClassEntry> classEntryList = classdao.findClassBySchoolId(schoolId, 1, "56e2032f0cf2bd4da79ac515", null);
        for (InterestClassEntry entry : classEntryList) {
            System.out.println(entry.getID().toString() + "\t" + entry.getClassName());
            List<InterestClassStudent> students = entry.getInterestClassStudents();
            for (InterestClassStudent student : students) {
                student.setTermType(2);
            }

            classdao.addExpandClass(entry);
        }
    }


    public static void main(String[] args) throws IOException {

//        new InterestClass().insertclass();
//        new InterestClass().updateTerm();
//        new InterestClass().getTerm(1435719600000l);

//        new InterestClass().addTypeId();
        new InterestClass().ss();

    }
}
