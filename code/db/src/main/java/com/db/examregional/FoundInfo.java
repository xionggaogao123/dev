package com.db.examregional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.bson.types.ObjectId;


import com.db.educationbureau.EducationBureauDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.pojo.educationbureau.EducationBureauEntry;
import com.pojo.examregional.EducationSubject;
import com.pojo.school.Grade;
import com.sys.constants.Constant;

public class FoundInfo {
    public static void main(String[] args) {
        EducationBureauDao edudao = new EducationBureauDao();
        EducationBureauEntry eduenrty = edudao.selEducationById(new ObjectId("55f240909b1d16c6529aa965"));
        String cityName = eduenrty.getCity();
        String conutyName = eduenrty.getCounty();
        long createTime = eduenrty.getCreateTime();
        String educationName = eduenrty.getEducationName();
        String provinceName = eduenrty.getProvince();
        long updateTime = eduenrty.getUpdateTime();
        List<ObjectId> userList = eduenrty.getUserIds();
        String scrd = eduenrty.getSchoolCreateDate();
        List<ObjectId> schList = eduenrty.getSchoolIds();
        Grade grade1 = new Grade(new ObjectId(), "一年级", 1, null, null);
        Grade grade2 = new Grade(new ObjectId(), "二年级", 2, null, null);
        Grade grade3 = new Grade(new ObjectId(), "三年级", 3, null, null);
        Grade grade4 = new Grade(new ObjectId(), "四年级", 4, null, null);
        Grade grade5 = new Grade(new ObjectId(), "五年级", 5, null, null);
        Grade grade6 = new Grade(new ObjectId(), "六年级", 6, null, null);
        Grade grade7 = new Grade(new ObjectId(), "初一", 7, null, null);
        Grade grade8 = new Grade(new ObjectId(), "初二", 8, null, null);
        Grade grade9 = new Grade(new ObjectId(), "初三", 9, null, null);
        Grade grade10 = new Grade(new ObjectId(), "高一", 10, null, null);
        Grade grade11 = new Grade(new ObjectId(), "高二", 11, null, null);
        Grade grade12 = new Grade(new ObjectId(), "高三", 12, null, null);
        List<Grade> gradeList = new ArrayList<Grade>();
        gradeList.add(grade1);
        gradeList.add(grade2);
        gradeList.add(grade3);
        gradeList.add(grade4);
        gradeList.add(grade5);
        gradeList.add(grade6);
        gradeList.add(grade7);
        gradeList.add(grade8);
        gradeList.add(grade9);
        gradeList.add(grade10);
        gradeList.add(grade11);
        gradeList.add(grade12);
        List<ObjectId> list = new ArrayList<ObjectId>();
        list.add(grade1.getGradeId());
        list.add(grade2.getGradeId());
        list.add(grade3.getGradeId());
        list.add(grade4.getGradeId());
        list.add(grade5.getGradeId());
        list.add(grade6.getGradeId());
        list.add(grade7.getGradeId());
        list.add(grade8.getGradeId());
        list.add(grade9.getGradeId());
        list.add(grade10.getGradeId());
        list.add(grade11.getGradeId());
        list.add(grade12.getGradeId());
        eduenrty.setGradeList(gradeList);
        List<EducationSubject> sublist = new ArrayList<EducationSubject>();

        EducationSubject edu = new EducationSubject(null, 1, null);
        edu.setName("语文");
        edu.setSubjectId(new ObjectId());
        edu.setGradeIds(list);
        edu.setSubjectType(1);

        EducationSubject edu1 = new EducationSubject(null, 2, null);
        edu1.setName("数学");
        edu1.setSubjectId(new ObjectId());
        edu1.setGradeIds(list);
        edu1.setSubjectType(2);

        EducationSubject edu3 = new EducationSubject(null, 3, null);
        List<ObjectId> list1 = new ArrayList<ObjectId>();
        list1.add(grade3.getGradeId());
        list1.add(grade4.getGradeId());
        list1.add(grade5.getGradeId());
        list1.add(grade6.getGradeId());
        list1.add(grade7.getGradeId());
        list1.add(grade8.getGradeId());
        list1.add(grade9.getGradeId());
        list1.add(grade10.getGradeId());
        list1.add(grade11.getGradeId());
        list1.add(grade12.getGradeId());
        edu3.setName("英语");
        edu3.setSubjectId(new ObjectId());
        edu3.setGradeIds(list1);
        edu3.setSubjectType(3);

        EducationSubject edu4 = new EducationSubject(null, 4, null);
        List<ObjectId> list2 = new ArrayList<ObjectId>();

        list2.add(grade8.getGradeId());
        list2.add(grade9.getGradeId());
        list2.add(grade10.getGradeId());
        list2.add(grade11.getGradeId());
        list2.add(grade12.getGradeId());
        edu4.setName("物理");
        edu4.setSubjectId(new ObjectId());
        edu4.setGradeIds(list2);
        edu4.setSubjectType(4);

        EducationSubject edu5 = new EducationSubject(null, 5, null);
        List<ObjectId> list3 = new ArrayList<ObjectId>();

        list3.add(grade9.getGradeId());
        list3.add(grade10.getGradeId());
        list3.add(grade11.getGradeId());
        list3.add(grade12.getGradeId());
        edu5.setName("化学");
        edu5.setSubjectId(new ObjectId());
        edu5.setGradeIds(list3);
        edu5.setSubjectType(5);

        EducationSubject edu6 = new EducationSubject(null, 6, null);
        List<ObjectId> list4 = new ArrayList<ObjectId>();
        list4.add(grade10.getGradeId());
        list4.add(grade11.getGradeId());
        list4.add(grade12.getGradeId());
        edu6.setName("生物");
        edu6.setSubjectId(new ObjectId());
        edu6.setGradeIds(list4);
        edu6.setSubjectType(6);

        EducationSubject edu7 = new EducationSubject(null, 7, null);
        List<ObjectId> list5 = new ArrayList<ObjectId>();
        list5.add(grade10.getGradeId());
        list5.add(grade11.getGradeId());
        list5.add(grade12.getGradeId());
        edu7.setName("地理");
        edu7.setSubjectId(new ObjectId());
        edu7.setGradeIds(list5);
        edu7.setSubjectType(7);

        EducationSubject edu8 = new EducationSubject(null, 8, null);
        List<ObjectId> list6 = new ArrayList<ObjectId>();
        list6.add(grade10.getGradeId());
        list6.add(grade11.getGradeId());
        list6.add(grade12.getGradeId());
        edu8.setName("历史");
        edu8.setSubjectId(new ObjectId());
        edu8.setGradeIds(list6);
        edu8.setSubjectType(8);

        EducationSubject edu9 = new EducationSubject(null, 9, null);
        List<ObjectId> list7 = new ArrayList<ObjectId>();
        list7.add(grade10.getGradeId());
        list7.add(grade11.getGradeId());
        list7.add(grade12.getGradeId());
        edu9.setName("政治");
        edu9.setSubjectId(new ObjectId());
        edu9.setGradeIds(list7);
        edu9.setSubjectType(9);
        sublist.add(edu);
        sublist.add(edu1);
        sublist.add(edu3);
        sublist.add(edu4);
        sublist.add(edu5);
        sublist.add(edu6);
        sublist.add(edu7);
        sublist.add(edu8);
        sublist.add(edu9);

        eduenrty.setSubjects(sublist);
        ImpDao dao = new ImpDao();
        dao.save(eduenrty);
        System.out.println("111111");
    }


}
//BasicDBObject query =new BasicDBObject(Constant.ID,new ObjectId("55f240909b1d16c6529aa965"));
//BasicDBObject updateValue =new BasicDBObject().append("grs", edu.getGradeList())
//		.append("subs", edu.getSubjects());
//update(MongoFacroty.getAppDB(), Constant.COLLECTION_EDUCATION_BUREAU, query, updateValue);