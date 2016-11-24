package com.db.temp;

import java.util.Date;

import org.bson.types.ObjectId;

import com.db.educationbureau.EducationBureauDao;
import com.pojo.educationbureau.EducationBureauEntry;

/**
 * 添加教育局用户
 *
 * @author fourer
 */
public class EducationBureau {

    public static void main(String[] args) {
        EducationBureauDao dao = new EducationBureauDao();
        Date currTime = new Date();
        Long createTime = currTime.getTime();
        EducationBureauEntry e = new EducationBureauEntry("安徽省教科院",
                "55934c13f6f28b7261c19c3d", "", "", createTime, createTime);
        ObjectId id = dao.addEducation(e);
        dao.addEduUser(id, new ObjectId("55934f27f6f28b7261c7d7c8"), createTime);
        //dao.addRelationSchool(id, new ObjectId("55934c15f6f28b7261c19c6f"),
        //		createTime);
        System.out.println(e.getID().toString());
    }

}
