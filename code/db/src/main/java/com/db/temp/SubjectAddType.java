package com.db.temp;

import com.db.lesson.DirDao;
import com.db.school.ClassDao;
import com.db.school.SchoolDao;
import com.db.school.TeacherClassSubjectDao;
import com.pojo.school.SchoolEntry;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.util.List;

/**
 * Created by fl on 2015/11/12.
 */
public class SubjectAddType {

    private SchoolDao schoolService = new SchoolDao();
    private ClassDao classService = new ClassDao();
    private TeacherClassSubjectDao teacherClassSubjectService = new TeacherClassSubjectDao();
    private DirDao dirDao = new DirDao();

    public void updateAll() throws IOException {
        int skip = 0;
        int limit = 200;

        while (true) {
            List<SchoolEntry> schoolList = schoolService.getSchoolEntry(skip, limit);

            if (null == schoolList || schoolList.isEmpty()) {
                break;
            }
            for (SchoolEntry se : schoolList) {
                updateSubjectType(se);
            }
            skip = skip + 200;
        }

    }

    private void updateSubjectType(SchoolEntry schoolEntry) {

    }
}
