package com.fulaan.newVersionBind.service;

import com.db.user.NewVersionBindRelationDao;
import com.fulaan.user.service.UserService;
import com.pojo.user.NewVersionBindRelationEntry;
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

    @Autowired
    private UserService userService;


    public void saveNewVersionBindRelationEntry (
            ObjectId bindId,
            int sex,String birthDate,
            ObjectId regionId,
            ObjectId regionAreaId,
            String relation,
            String schoolName
    ){
        NewVersionBindRelationEntry entry= newVersionBindRelationDao.getEntry(bindId);
        if(null!=entry){
            ObjectId userId=entry.getUserId();
            try {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Date dateBirth = format.parse(birthDate);
                userService.updateUserBirthDateAndSex(userId,sex,dateBirth.getTime());
                newVersionBindRelationDao.saveEntry(bindId, regionId, regionAreaId, relation, schoolName);
            }catch (Exception e){
                throw  new RuntimeException("传入的生日数据有误!");
            }
        }else {
            throw  new RuntimeException("传入参数出错!");
        }
    }

}
