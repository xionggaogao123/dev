package com.fulaan.newVersionBind.service;

import cn.jpush.api.push.model.audience.Audience;
import com.db.newVersionGrade.NewVersionGradeDao;
import com.db.user.NewVersionBindRelationDao;
import com.db.user.NewVersionUserRoleDao;
import com.fulaan.user.service.UserService;
import com.fulaan.utils.JPushUtils;
import com.fulaan.utils.pojo.KeyValue;
import com.fulaan.wrongquestion.dto.NewVersionGradeDTO;
import com.fulaan.wrongquestion.service.WrongQuestionService;
import com.pojo.newVersionGrade.NewVersionGradeEntry;
import com.pojo.user.NewVersionBindRelationEntry;
import com.pojo.user.NewVersionUserRoleEntry;
import com.pojo.user.UserEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

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

    @Autowired
    private WrongQuestionService wrongQuestionService;


    public void saveNewVersionBindRelationEntry (
            ObjectId mainUserId,
            ObjectId userId,
            int sex,String birthDate,
            ObjectId regionId,
            ObjectId regionAreaId,
            String relation,
            String schoolName,
            String avatar,
            int gradeType,
            String nickName
    ){
        NewVersionBindRelationEntry entry= newVersionBindRelationDao.getBindEntry(userId);
        if(null==entry){
            try {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Date dateBirth = format.parse(birthDate);
                userService.updateUserBirthDateAndSex(userId,sex,dateBirth.getTime(),avatar,nickName);
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
                //绑定年级
                KeyValue keyValue = wrongQuestionService.getCurrTermType();
                NewVersionGradeEntry gradeEntry = newVersionGradeDao.getEntryByCondition(userId, keyValue.getValue());
                if(null==gradeEntry) {
                    NewVersionGradeDTO dto = new NewVersionGradeDTO(userId.toString(),keyValue.getValue(),gradeType);
                    wrongQuestionService.addGradeFromUser(dto);
                }
                //发送环信消息
                List<String> tags = new ArrayList<String>();
                tags.add(userId.toString());
                Audience audience = Audience.alias(tags);
                JPushUtils jPushUtils=new JPushUtils();
                UserEntry userEntry=userService.findById(userId);
                Map<String,String> extras = new HashMap<String, String>();
                extras.put("type","1");
                jPushUtils.pushRestAndroid(audience, "你的账号已激活", userEntry.getUserName(), "您有新的通知", extras);
                jPushUtils.pushRestIos(audience, "你的账号已激活", extras);
                jPushUtils.pushRestWinPhone(audience, "你的账号已激活");
            }catch (Exception e){
                throw  new RuntimeException("传入的生日数据有误!");
            }
        }else {
            throw  new RuntimeException("已经绑定了!");
        }
    }

}
