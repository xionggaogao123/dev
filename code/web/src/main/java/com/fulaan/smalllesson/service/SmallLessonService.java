package com.fulaan.smalllesson.service;

import com.db.smalllesson.LessonAnswerDao;
import com.db.smalllesson.LessonUserResultDao;
import com.db.smalllesson.SmallLessonDao;
import com.fulaan.smalllesson.dto.LessonAnswerDTO;
import com.fulaan.smalllesson.dto.LessonUserResultDTO;
import com.fulaan.smalllesson.dto.SmallLessonDTO;
import com.fulaan.smalllesson.dto.SmallLessonUserCodeDTO;
import com.mongodb.DBObject;
import com.pojo.smalllesson.LessonAnswerEntry;
import com.pojo.smalllesson.LessonUserResultEntry;
import com.pojo.smalllesson.SmallLessonEntry;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/9/27.
 */
@Service
public class SmallLessonService {

    private SmallLessonDao smallLessonDao = new SmallLessonDao();
    private LessonUserResultDao lessonUserResultDao = new LessonUserResultDao();
    private LessonAnswerDao lessonAnswerDao = new LessonAnswerDao();
    @Autowired
    private SmallLessonUserCodeService smallLessonUserCodeService;
    //列表查询用户课程
    public List<SmallLessonDTO> getLessonList(ObjectId userId,int page,int pageSize){
        List<SmallLessonDTO> dtos = new ArrayList<SmallLessonDTO>();
        List<SmallLessonEntry> entries = smallLessonDao.getLessonPageList(userId, page, pageSize);
        if(entries.size() >0){
            for(SmallLessonEntry entry : entries){
                dtos.add(new SmallLessonDTO(entry));
            }
        }
        return dtos;
    }

    //查询课程用户活跃列表
    public List<LessonUserResultDTO> getUserResultList(ObjectId userId,int page,int pageSize){
        List<LessonUserResultDTO> dtos = new ArrayList<LessonUserResultDTO>();
        List<LessonUserResultEntry> entries = lessonUserResultDao.getUserResultList(userId, page, pageSize);
        if(entries.size() >0){
            for(LessonUserResultEntry entry : entries){
                dtos.add(new LessonUserResultDTO(entry));
            }
        }
        return dtos;
    }

    //查询答题列表
    public Map<String,Object> selectAnswerList(ObjectId userId,int number,int page,int pageSize,int type){
        Map<String,Object> map = new HashMap<String, Object>();
        List<LessonAnswerDTO> dtos = new ArrayList<LessonAnswerDTO>();
        List<LessonAnswerEntry> entries = lessonAnswerDao.getUserResultPageList(userId, number, page, pageSize);
        if(entries.size() >0){
            for(LessonAnswerEntry entry : entries){
                dtos.add(new LessonAnswerDTO(entry));
            }
        }
        map.put("list",dtos);
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
    public SmallLessonUserCodeDTO addLessonEntry(String userId,String userName){
        SmallLessonDTO dto = new SmallLessonDTO();
        dto.setUserId(userId);
        dto.setName(this.getName(new ObjectId(userId),userName));
        dto.setType(0);
        SmallLessonEntry entry = dto.buildAddEntry();
        smallLessonDao.addEntry(entry);
        SmallLessonUserCodeDTO dto2 = smallLessonUserCodeService.getDto(new ObjectId(userId));
        return dto2;
    }

    //加入课程（扫码）
    public Map<String,Object> addStuEntry(ObjectId userId,String userName,ObjectId teacherId){
        SmallLessonEntry entry = smallLessonDao.getEntryByUserId(teacherId);
        Map<String,Object> map = new HashMap<String, Object>();
        if(entry == null){
            map.put("code",1);
            map.put("msg","该课程已结束");
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
    //加入课程（输码）
    public Map<String,Object> addStuEntry2(ObjectId userId,String userName,ObjectId teacherId){
        SmallLessonEntry entry = smallLessonDao.getEntryByUserId(teacherId);
        Map<String,Object> map = new HashMap<String, Object>();
        if(entry == null){
            map.put("code",1);
            map.put("msg","该课程已结束");
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


    //修改课程
    public void updLessonEntry(String userId,int time){
        SmallLessonEntry entry = smallLessonDao.getEntry2(new ObjectId(userId));
        entry.setType(1);
        entry.setNodeTime(time);
        smallLessonDao.updEntry(entry);
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
       smallLessonDao.delSmallLessonEntry(lessonId);
    }

    //获得登陆信息
    public Map<String,Object> getUserInfo(ObjectId userId){
        Map<String,Object> map = new HashMap<String, Object>();
        List<SmallLessonEntry> entries = smallLessonDao.getLessonList(userId);
        int number = 0;
        int time = 0;
        if(entries.size()>0){
            for(SmallLessonEntry entry : entries){
                time += entry.getNodeTime();
            }
            number = entries.size();
        }
        map.put("number",number);
        map.put("time",time);
        return map;
    }

    //添加活跃用户信息
    public void addUserResult(List<String> userIds){
        List<ObjectId> olist = new ArrayList<ObjectId>();
        if(userIds != null && userIds.size()>0){
            for(String str : userIds){
                olist.add(new ObjectId(str));
            }
        }
        List<LessonUserResultEntry> entries = lessonUserResultDao.getLetterUserResultList(olist);
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
}
