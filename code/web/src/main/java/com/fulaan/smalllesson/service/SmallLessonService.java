package com.fulaan.smalllesson.service;

import com.db.business.ModuleTimeDao;
import com.db.smalllesson.LessonAnswerDao;
import com.db.smalllesson.LessonUserResultDao;
import com.db.smalllesson.SmallLessonCodeDao;
import com.db.smalllesson.SmallLessonDao;
import com.db.user.UserDao;
import com.fulaan.smalllesson.api.LessonAPI;
import com.fulaan.smalllesson.dto.LessonAnswerDTO;
import com.fulaan.smalllesson.dto.LessonUserResultDTO;
import com.fulaan.smalllesson.dto.SmallLessonDTO;
import com.fulaan.user.service.UserService;
import com.mongodb.DBObject;
import com.pojo.instantmessage.ApplyTypeEn;
import com.pojo.smalllesson.LessonAnswerEntry;
import com.pojo.smalllesson.LessonUserResultEntry;
import com.pojo.smalllesson.SmallLessonCodeEntry;
import com.pojo.smalllesson.SmallLessonEntry;
import com.pojo.user.UserDetailInfoDTO;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by James on 2017/9/27.
 */
@Service
public class SmallLessonService {

    private SmallLessonDao smallLessonDao = new SmallLessonDao();
    private LessonUserResultDao lessonUserResultDao = new LessonUserResultDao();
    private LessonAnswerDao lessonAnswerDao = new LessonAnswerDao();
    private UserDao userDao = new UserDao();
    @Autowired
    private SmallLessonUserCodeService smallLessonUserCodeService;
    @Autowired
    private UserService userService;

    private SmallLessonCodeDao smallLessonCodeDao = new SmallLessonCodeDao();

    private ModuleTimeDao moduleTimeDao = new ModuleTimeDao();
    //列表查询用户课程
    public Map<String,Object> getLessonList(ObjectId userId,int page,int pageSize){
        Map<String,Object> map = new HashMap<String, Object>();
        List<SmallLessonDTO> dtos = new ArrayList<SmallLessonDTO>();
        List<SmallLessonEntry> entries = smallLessonDao.getLessonPageList(userId, page, pageSize);
        if(entries.size() >0){
            for(SmallLessonEntry entry : entries){
                dtos.add(new SmallLessonDTO(entry));
            }
        }
        int count = smallLessonDao.getNumber(userId);
        map.put("rows",dtos);
        map.put("count",count);
        return map;
    }

    //查询课程用户活跃列表
    public Map<String,Object> getUserResultList(ObjectId lessonId,int page,int pageSize){
        Map<String,Object> map = new HashMap<String, Object>();
        List<LessonUserResultDTO> dtos = new ArrayList<LessonUserResultDTO>();
        List<LessonUserResultEntry> entries = lessonUserResultDao.getUserResultList(lessonId, page, pageSize);
        int count2 = lessonUserResultDao.getNumber(lessonId);
        Object count = lessonUserResultDao.getAllScore(lessonId);
        List<String> uids = new ArrayList<String>();
        if(entries.size() >0){
            for(LessonUserResultEntry entry : entries){
                dtos.add(new LessonUserResultDTO(entry));
                uids.add(entry.getUserId().toString());
            }
        }
        List<LessonUserResultDTO> udto3 = new ArrayList<LessonUserResultDTO>();
        List<LessonUserResultDTO> udto4 = new ArrayList<LessonUserResultDTO>();
        if(dtos.size()>0){
            List<UserDetailInfoDTO> udtos2 = userService.findUserInfoByUserIds(uids);
            Map<String,UserDetailInfoDTO> map3 = new HashMap<String, UserDetailInfoDTO>();
            if(udtos2 != null && udtos2.size()>0){
                for(UserDetailInfoDTO dto4 : udtos2){
                    map3.put(dto4.getId(),dto4);
                }
            }
            int i = 0;
            for(LessonUserResultDTO dto2:dtos){
                UserDetailInfoDTO dto = map3.get(dto2.getUserId());
                if(dto != null){
                    String name = dto.getNickName()!=null?dto.getNickName():dto.getName();
                    dto2.setUserName(name);
                }
                dto2.setParming((page-1)*pageSize+i+1);
                if( i <= 4){
                    udto3.add(dto2);
                }else{
                    udto4.add(dto2);
                }
                i++;
            }
        }
        map.put("dto1",udto3);
        map.put("dto2",udto4);
        map.put("count",count2);
        map.put("all",count);
        return map;
    }

    public List<Integer> getIntList(ObjectId lessonId){
        List<Integer> ins = lessonAnswerDao.getIntResultList(lessonId);
        Set<Integer> set = new HashSet<Integer>();
        set.addAll(ins);
        List<Integer> objectIdList = new ArrayList<Integer>();
        objectIdList.addAll(set);
        return objectIdList;
    }
    //查询答题列表
    public Map<String,Object> selectAnswerList(ObjectId userId,int number,int page,int pageSize,int type){
        Map<String,Object> map = new HashMap<String, Object>();
        List<LessonAnswerDTO> dtos = new ArrayList<LessonAnswerDTO>();
        List<LessonAnswerEntry> entries = lessonAnswerDao.getUserResultPageList(userId, number, page, pageSize);
        int count = lessonAnswerDao.getNumber(userId, number);
        List<String> uids = new ArrayList<String>();
        if(entries.size() >0){
            for(LessonAnswerEntry entry : entries){
                dtos.add(new LessonAnswerDTO(entry));
                uids.add(entry.getUserId().toString());
            }
        }
        if(dtos.size()>0){
            List<UserDetailInfoDTO> udtos2 = userService.findUserInfoByUserIds(uids);
            Map<String,UserDetailInfoDTO> map3 = new HashMap<String, UserDetailInfoDTO>();
            if(udtos2 != null && udtos2.size()>0){
                for(UserDetailInfoDTO dto4 : udtos2){
                    map3.put(dto4.getId(),dto4);
                }
            }
            int i = 1;
            for(LessonAnswerDTO dto2:dtos){
                UserDetailInfoDTO dto = map3.get(dto2.getUserId());
                if(dto != null){
                    String name = dto.getNickName()!=null?dto.getNickName():dto.getName();
                    dto2.setUserName(name);
                }
                dto2.setParming((page-1)*pageSize+i);
                i++;
            }
        }

        map.put("list",dtos);
        map.put("count",count);

        if(type==2){
            List<LessonAnswerEntry> entries2 = lessonAnswerDao.getUserResultList(userId,number);
            int a = 0;//未答
            int b = 0;//错误
            int c = 0;//正确
            if(entries2.size()>0){
                for(LessonAnswerEntry entry : entries2){
                   if(entry.getIsTrue()==0){
                       a++;
                   }else if(entry.getIsTrue()==1){
                       b++;
                   }else{
                       c++;
                   }
                }
            }
            map.put("a",a);
            map.put("b",b);
            map.put("c",c);
        }
        return map;
    }

    //修改课程名
    public void updateLessonName(ObjectId lessonId,String name){
       smallLessonDao.updateSmallLessonEntry(lessonId, name);
    }

    //添加课程
    public SmallLessonDTO addLessonEntry(String userId,String userName){
        long current=System.currentTimeMillis();
        SmallLessonEntry entry2 = smallLessonDao.getEntryByUserId(new ObjectId(userId));
        if(entry2==null){
            SmallLessonDTO dto = new SmallLessonDTO();
            dto.setUserId(userId);
            dto.setName(this.getName(new ObjectId(userId),userName));
            dto.setType(0);
            SmallLessonEntry entry = dto.buildAddEntry();
            ObjectId lessonId = smallLessonDao.addEntry(entry);
            Map<String,String> dto2 = smallLessonUserCodeService.getSmallLessonCode(lessonId);
            //
            String imagUrl = dto2.get("qrUrl");
            String code = dto2.get("code");
            entry.setImageUrl(imagUrl);
            entry.setCode(code);
            entry.setID(lessonId);
            smallLessonDao.updEntry(entry);
            SmallLessonDTO dto3 = new SmallLessonDTO(entry);
            //小课堂发送记录
            moduleTimeDao.addEntry(new ObjectId(userId), ApplyTypeEn.smallLesson.getType());
            return dto3;
            //return null;
        }else{
            if(current - entry2.getCreateTime() > 1000*70 ){
                //非正常下课判断
                entry2.setType(1);
                long time2 = entry2.getCreateTime()-entry2.getDateTime();
                int time = (int)time2/60000;
                entry2.setNodeTime(time);
                smallLessonDao.updEntry(entry2);
                smallLessonUserCodeService.removeSmallLessonCode(entry2.getCode());
                SmallLessonDTO dto = new SmallLessonDTO();
                dto.setUserId(userId);
                dto.setName(this.getName(new ObjectId(userId),userName));
                dto.setType(0);
                SmallLessonEntry entry = dto.buildAddEntry();
                ObjectId lessonId = smallLessonDao.addEntry(entry);
                Map<String,String> dto2 = smallLessonUserCodeService.getSmallLessonCode(lessonId);
                //
                String imagUrl = dto2.get("qrUrl");
                String code = dto2.get("code");
                entry.setImageUrl(imagUrl);
                entry.setCode(code);
                entry.setID(lessonId);
                smallLessonDao.updEntry(entry);
                SmallLessonDTO dto4 = new SmallLessonDTO(entry);
                //小课堂发送记录
                moduleTimeDao.addEntry(new ObjectId(userId), ApplyTypeEn.smallLesson.getType());
                return dto4;
            }else{
                return null;
            }
        }

    }

    //加入课程（扫码）
    public Map<String,Object> addStuEntry(ObjectId userId,String userName,ObjectId teacherId){
        SmallLessonEntry entry = smallLessonDao.getActiveEntry(teacherId);
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("lessonId",teacherId.toString());
        if(entry == null){
            map.put("code",1);
            map.put("msg","该课程已结束");
            return map;
        }else{
            LessonUserResultEntry entry2 = lessonUserResultDao.getEntry(teacherId,userId);
            if(entry2!= null){
                map.put("code",2);
                map.put("msg","重新进入课堂");
                return map;
            }else{
                LessonUserResultDTO dto = new LessonUserResultDTO();
                dto.setUserId(userId.toString());
                dto.setUserName(userName);
                dto.setLessonId(entry.getID().toString());
                dto.setScore(0);
                LessonUserResultEntry entry1 = dto.buildAddEntry();
                lessonUserResultDao.addEntry(entry1);
                map.put("code",2);
                map.put("msg","该课程进行中");
                return map;
            }

        }

    }
    //加入课程（输码）
    public Map<String,Object> addStuEntryByCode(ObjectId userId,String userName,String str){
        String code = str.toLowerCase();
        SmallLessonCodeEntry dto2=smallLessonCodeDao.getCodeEntry(code);
        Map<String,Object> map = new HashMap<String, Object>();
        if(dto2 == null){
            map.put("lessonId","");
            map.put("code",1);
            map.put("msg","该课程码不存在");
            return map;
        }

        SmallLessonEntry entry = smallLessonDao.getEntryByCode(code);
        if(entry == null){
            map.put("lessonId","");
            map.put("code",1);
            map.put("msg","该课程已结束");
            return map;
        }else{
            LessonUserResultEntry entry2 = lessonUserResultDao.getEntry(entry.getID(),userId);
            if(entry2!= null){
                map.put("lessonId",entry2.getLessonId().toString());
                map.put("code",2);
                map.put("msg","重新进入课堂");
                return map;
            }else {
                LessonUserResultDTO dto = new LessonUserResultDTO();
                dto.setUserId(userId.toString());
                dto.setUserName(userName);
                dto.setLessonId(entry.getID().toString());
                dto.setScore(0);
                LessonUserResultEntry entry1 = dto.buildAddEntry();
                lessonUserResultDao.addEntry(entry1);
                map.put("lessonId", entry.getID().toString());
                map.put("code", 2);
                map.put("msg", "该课程进行中");
                return map;
            }
        }

    }


    //修改课程
    public void updLessonEntry(String userId,int time){
        SmallLessonEntry entry = smallLessonDao.getEntry2(new ObjectId(userId));
        entry.setType(1);
        entry.setNodeTime(time);
        smallLessonDao.updEntry(entry);
    }
    //一分钟调用接口
    public void getTimeLoading(ObjectId userId){
        SmallLessonEntry entry = smallLessonDao.getEntry2(userId);
        long current=System.currentTimeMillis();
        smallLessonDao.getTimeLoading(entry.getID(), current);
    }

    private String getName(ObjectId userId,String userName){
        List<SmallLessonEntry> entries = smallLessonDao.getLessonList(userId);
        int number = entries.size();
        if(number < 10){
            return userName + "老师00" + number + "课";
        }else if(number < 100){
            return userName + "老师0" + number + "课";
        }else{
            return userName + "老师" + number + "课";
        }
    }

    //删除课程
    public void delLessonEntry(ObjectId lessonId){
        SmallLessonEntry entry = smallLessonDao.getEntry(lessonId);
        if(entry!= null){
            smallLessonDao.delSmallLessonEntry(lessonId);
            smallLessonUserCodeService.removeSmallLessonCode(entry.getCode());
        }
    }

    //获得登陆信息
    public Map<String,Object> getUserInfo(ObjectId userId){
        Map<String,Object> map = new HashMap<String, Object>();
        List<SmallLessonEntry> entries = smallLessonDao.getLessonList(userId);
        int number = 0;
        int time = 0;
        String loginTime = "";
        if(entries.size()>0){
            for(SmallLessonEntry entry : entries){
                time += entry.getNodeTime();
            }
            number = entries.size();
            SmallLessonDTO dto = new SmallLessonDTO(entries.get(0));
            loginTime = dto.getDateTime();
        }
        map.put("loginTime",loginTime);
        map.put("number",number);
        map.put("time",time);
        return map;
    }

    //添加活跃用户信息
    public void addUserResult(List<String> userIds,ObjectId lessonId){
        List<ObjectId> olist = new ArrayList<ObjectId>();
        if(userIds != null && userIds.size()>0){
            for(String str : userIds){
                olist.add(new ObjectId(str));
            }
        }
        List<LessonUserResultEntry> entries = lessonUserResultDao.getLetterUserResultList(olist,lessonId);
        if(entries.size()>0){
            for(LessonUserResultEntry entry : entries){
                entry.setScore(entry.getScore()+1);
                lessonUserResultDao.updEntry(entry);
            }
        }
    }

    //批量添加题目
    public void addAnswerList(List<LessonAnswerDTO> dtos){
        List<DBObject> dbList = new ArrayList<DBObject>();
        if(dtos != null && dtos.size()>0){
            for (int i = 0; dtos != null && i < dtos.size(); i++) {
                LessonAnswerDTO si = dtos.get(i);
                LessonAnswerEntry obj = si.buildAddEntry();
                dbList.add(obj.getBaseEntry());
            }
        }
        //批量添加
        lessonAnswerDao.addBatch(dbList);
    }


    //修改头像
    public void updateAvatar(ObjectId userId,String avatar){
        userDao.updateAvater(userId,avatar);
    }
    //修改姓名
    public void updateUserNickName(ObjectId userId,String nickName){
        userDao.updateUserNickName(userId, nickName);
    }
    //修改姓名
    public void updateUserSex(ObjectId userId,int sex){
        userDao.updateUserSex(userId, sex);
    }

    public void checkUnLesson(){
        SmallLessonDao smallLessonDao = new SmallLessonDao();
        long current=System.currentTimeMillis();
        long time = current-10*60*1000;
        List<SmallLessonEntry> entries= smallLessonDao.getEntryList(time);
        List<String> stringList = new ArrayList<String>();
        List<ObjectId> objectIdList =  new ArrayList<ObjectId>();
        if(entries.size()>0){
           for(SmallLessonEntry entry : entries){
               stringList.add(entry.getID().toString());
               objectIdList.add(entry.getID());
           }
        }
        smallLessonDao.delSmallLessonEntryList(objectIdList);
        if(stringList.size()>0){
            LessonAPI.addCustomVote(stringList);
        }
    }

}
