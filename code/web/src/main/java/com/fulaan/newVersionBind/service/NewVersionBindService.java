package com.fulaan.newVersionBind.service;

import com.db.newVersionGrade.NewVersionGradeDao;
import com.db.user.NewVersionBindRelationDao;
import com.db.user.NewVersionUserRoleDao;
import com.fulaan.user.service.UserService;
import com.pojo.user.NewVersionBindRelationEntry;
import com.pojo.user.NewVersionUserRoleEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by scott on 2017/9/5.
 */
@Service
public class NewVersionBindService {


    private NewVersionBindRelationDao newVersionBindRelationDao=new NewVersionBindRelationDao();

    private NewVersionUserRoleDao newVersionUserRoleDao= new NewVersionUserRoleDao();

    private NewVersionGradeDao newVersionGradeDao = new NewVersionGradeDao();

    @Autowired
    private UserService userService;


    public void saveNewVersionBindRelationEntry (
            ObjectId mainUserId,
            ObjectId userId,
            int sex,String birthDate,
            ObjectId regionId,
            ObjectId regionAreaId,
            String relation,
            String schoolName,
            String avatar,
            int gradeType
    ){
        NewVersionBindRelationEntry entry= newVersionBindRelationDao.getBindEntry(mainUserId,userId);
        if(null==entry){
            try {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Date dateBirth = format.parse(birthDate);
                userService.updateUserBirthDateAndSex(userId,sex,dateBirth.getTime(),avatar);
                NewVersionBindRelationEntry relationEntry
                        =new NewVersionBindRelationEntry(mainUserId,
                        userId,
                        relation,
                        regionId,
                        regionAreaId,
                        schoolName);
                newVersionBindRelationDao.saveNewVersionBindEntry(relationEntry);
                NewVersionUserRoleEntry userRoleEntry=newVersionUserRoleDao.getEntry(userId);
                userRoleEntry.setNewRole(Constant.TWO);
                newVersionUserRoleDao.saveEntry(userRoleEntry);
            }catch (Exception e){
                throw  new RuntimeException("传入的生日数据有误!");
            }
        }else {
            throw  new RuntimeException("已经绑定了!");
        }
    }

}
