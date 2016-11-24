package com.db.temp;

import com.db.school.InterestClassDao;
import org.bson.types.ObjectId;

/**
 * Created by fl on 2015/12/23.
 */
public class DeleteInterestClass {
    private InterestClassDao interestClassDao = new InterestClassDao();

    private void delete() {
        ObjectId schoolId = new ObjectId("562f01d70cf2617509441c2c");//唐镇中学
        interestClassDao.deleteExpandClassBySchoolId(schoolId);
    }

    public static void main(String[] args) {
        new DeleteInterestClass().delete();
    }
}
