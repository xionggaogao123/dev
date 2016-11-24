package com.db.user;

import com.db.school.ClassDao;
import com.db.school.SchoolDao;
import com.pojo.school.ClassEntry;
import com.pojo.user.UserExperienceLogEntry;
import com.pojo.user.UserSchoolYearExperienceEntry;
import com.sys.constants.Constant;
import org.junit.Test;

import java.util.List;

/**
 * Created by guojing on 2015/7/22.
 */
public class UserSchoolYearExperienceDaoTest{
    private UserSchoolYearExperienceDao dao =new UserSchoolYearExperienceDao();
    private SchoolDao schoolDao =new SchoolDao();
    private UserDao userDao=new UserDao();
    private UserExperienceLogDao expDao =new UserExperienceLogDao();
    private ClassDao classDao=new ClassDao();

/*    @Test
    public void addUserSchoolYearExperience(){
        List<SchoolEntry> schools=schoolDao.getAllSchoolEntry();
        for(SchoolEntry item:schools){
            List<UserEntry> users=userDao.getUserInfoBySchoolid(item.getID(), Constant.FIELDS);
            for(UserEntry user:users){
                List<UserExperienceLogEntry.ExperienceLog> expLogs=expDao.getExperienceLogbyTypeList(user.getID(),3);
                int schoolYearExp=0;
                for(UserExperienceLogEntry.ExperienceLog log:expLogs){
                    if(log.getExpLogTypeOrdinal()==3){
                        schoolYearExp+=log.getExperience();
                    }
                }
                ClassEntry classEntry = classDao.getClassEntryByStuId(user.getID(), Constant.FIELDS);
                if(classEntry!=null) {
                    UserSchoolYearExperienceEntry schoolYearExpEntry = new UserSchoolYearExperienceEntry(
                            user.getID(),
                            classEntry.getSchoolId(),
                            classEntry.getGradeId(),
                            classEntry.getID(),
                            schoolYearExp);
                    dao.addUserSchoolYearExperience(schoolYearExpEntry);
                }
            }

        }
    }*/

   

}
