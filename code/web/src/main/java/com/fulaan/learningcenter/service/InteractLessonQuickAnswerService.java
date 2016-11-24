package com.fulaan.learningcenter.service;

import com.db.school.InteractLessonQuickAnswerDao;
import com.fulaan.learningcenter.dto.InteractLessonAnswerCountDTO;
import com.fulaan.learningcenter.dto.InteractLessonQuickAnswerDTO;
import com.fulaan.myclass.service.ClassService;
import com.fulaan.user.service.UserService;
import com.fulaan.utils.pojo.KeyValue;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.school.ClassEntry;
import com.pojo.school.InteractLessonAnswerType;
import com.pojo.school.InteractLessonQuestionType;
import com.pojo.school.InteractLessonQuickAnswerEntry;
import com.pojo.user.UserEntry;
import com.sys.constants.Constant;
import com.sys.utils.DateTimeUtils;
import com.sys.utils.NumberUtils;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by guojing on 2015/11/26.
 */
@Service
public class InteractLessonQuickAnswerService {

    private static final Logger logger =Logger.getLogger(InteractLessonQuickAnswerService.class);
    InteractLessonQuickAnswerDao interactLessonQuickAnswerDao=new InteractLessonQuickAnswerDao();

    @Resource
    private ClassService classService;

    @Resource
    private UserService userService;

    //记录快速答题次数
    private static Map<String,Integer> timesMap = new HashMap<String,Integer>();

    /**
     * 新建互动课堂快速答题
     * @param e
     * @return
     */
    public ObjectId addInteractLessonQuickAnswerEntry(InteractLessonQuickAnswerEntry e)
    {
        return interactLessonQuickAnswerDao.addInteractLessonQuickAnswerEntry(e);
    }

    /**
     * 新建互动课堂快速答题
     * @param content
     */
    public void addInteractLessonQuickAnswerEntry(int type, String content) {
        try{
            //将json字符串转换成json对象
            JSONObject dataJson=new JSONObject(content);

            //获取互动课堂Id
            String lessonId=dataJson.getString("lessonId");
            ObjectId lid=new ObjectId(lessonId);

            //获取互动课堂用户Id
            String userId=dataJson.getString("userId");
            ObjectId uid=new ObjectId(userId);

            //考试次数
            int times =dataJson.getInt("number");

            String key=lessonId;
            if(type==1){
                key+=type;
                if(timesMap.get(key)==null){
                    timesMap.put(key,times);
                }else{
                    times=timesMap.get(key)+1;
                    timesMap.put(key,times);
                }
            }else{
                key+=1;
                times=timesMap.get(key);
            }

            String useTimeStr = "";
            if(type==2) {
                //考试用时
                int useTime = dataJson.getInt("useTime");
                useTimeStr = DateTimeUtils.getDuration(useTime);
            }
            //获取题目类型
            int format = dataJson.getInt("format");
            InteractLessonQuestionType questionType = InteractLessonQuestionType.getInteractLessonQuestionType(format);
            String qFormat = questionType.getType();
            String qFormatDes = questionType.getDes();

            //获取题目答案
            int answer = dataJson.getInt("answer");
            String answerDes = InteractLessonAnswerType.getInteractLessonAnswerTypeDes(answer);

            InteractLessonQuickAnswerEntry entry = new InteractLessonQuickAnswerEntry(
                    lid,
                    uid,
                    type,
                    times,
                    qFormat,
                    qFormatDes,
                    answer,
                    answerDes,
                    useTimeStr
            );
            addInteractLessonQuickAnswerEntry(entry);
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 获取互动课堂快速答题次数
     * @param lessonId
     * @return
     */
    public List<KeyValue> getQuickAnswerTimesList(ObjectId lessonId) {
        List<KeyValue> resultList=new ArrayList<KeyValue>();
        DBObject fields =new BasicDBObject("ts", Constant.ONE);
        List<InteractLessonQuickAnswerEntry> list=interactLessonQuickAnswerDao.findQuickAnswerEntryList(lessonId, 1, 0, fields, "ts");
        if(list!=null&&list.size()>0){
            InteractLessonQuickAnswerEntry entry=list.get(0);
            int maxVal=entry.getTimes();
            for(int i=1;i<=maxVal;i++){
                KeyValue keyValue = new KeyValue();
                keyValue.setKey(i);
                String value="第"+ NumberUtils.formatInteger(i)+"次答题";
                keyValue.setValue(value);
                resultList.add(keyValue);
            }
        }
        return resultList;
    }


    /**
     * 互动课堂快速答题 获取学生回答列表
     * @param lessonId
     * @param times
     * @return
     */
    public Map<String,Object> getStuQuickAnswerTextList(ObjectId lessonId, ObjectId classId, int times) {

        Map<String,Object> map=new HashMap<String, Object>();

        List<InteractLessonAnswerCountDTO> answerCountList=new ArrayList<InteractLessonAnswerCountDTO>();
        List<InteractLessonQuickAnswerDTO> stuQuickAnswerList=new ArrayList<InteractLessonQuickAnswerDTO>();
        ClassEntry e=classService.getClassEntryById(classId, null);
        List<ObjectId> stuids = new ArrayList<ObjectId>();
        int totalStudent=1;
        if(e!=null){
            if(e.getStudents()!=null&&e.getStudents().size()>0){
                totalStudent=e.getStudents().size();
                stuids=e.getStudents();
            }
        }

        List<InteractLessonQuickAnswerEntry> list=interactLessonQuickAnswerDao.findQuickAnswerEntryList(lessonId, 0, times, null, "");
        for(InteractLessonQuickAnswerEntry item : list){
            if(item.getType()!=1) {
                stuids.add(item.getUserId());
            }
        }
        Map<ObjectId, UserEntry> userMap=userService.getUserEntryMap2(stuids, new BasicDBObject("nm",1).append("ir",1));

        Map<String, ObjectId> uMap=new HashMap<String, ObjectId>();
        for (UserEntry v : userMap.values()) {
            if(v.getIsRemove()== Constant.ZERO) {
                uMap.put(v.getUserName(),v.getID());
            }
        }

        Map<Integer,Integer> countMap=new HashMap<Integer, Integer>();

        Set<ObjectId> signIn = new HashSet<ObjectId>();

        for(InteractLessonQuickAnswerEntry entry:list){
            if(entry.getType()==1) {
                String format=entry.getFormat();
                List<InteractLessonAnswerType> answerTypelist=InteractLessonAnswerType.getInteractLessonAnswerType(format);
                for(InteractLessonAnswerType answerType:answerTypelist){
                    InteractLessonAnswerCountDTO answerCountDTO=new InteractLessonAnswerCountDTO();
                    answerCountDTO.setAnswer(answerType.getAnswer());
                    answerCountDTO.setAnswerDes(answerType.getDes());
                    answerCountList.add(answerCountDTO);
                }
            }else{
                InteractLessonQuickAnswerDTO quickAnswerDTO=new InteractLessonQuickAnswerDTO(entry);
                UserEntry userEntry=userMap.get(entry.getUserId());
                ObjectId userId=null;
                if(userEntry!=null){
                    userId=uMap.get(userEntry.getUserName());
                    if(userId==null) {
                        continue;
                    }
                    quickAnswerDTO.setUserId(userId.toString());
                    quickAnswerDTO.setUserName(userEntry.getUserName());
                }else{
                    continue;
                }
                signIn.add(userId);
                Integer answerCount = countMap.get(entry.getAnswer());
                if (answerCount != null) {
                    countMap.put(entry.getAnswer(), answerCount + 1);
                }else{
                    countMap.put(entry.getAnswer(), 1);
                }

                if(userEntry!=null){
                    quickAnswerDTO.setUserName(userEntry.getUserName());
                }
                stuQuickAnswerList.add(quickAnswerDTO);
            }
        }

        if(signIn.size()==0){
            map.put("hiddenEcharts","Y");
        }else {
            map.put("hiddenEcharts","N");

            for (Map.Entry<String, ObjectId> entry : uMap.entrySet()) {
                if(!signIn.contains(entry.getValue())){
                    InteractLessonQuickAnswerDTO dto = new InteractLessonQuickAnswerDTO();
                    dto.setLessonId(lessonId.toString());
                    dto.setUserId(entry.getValue().toString());
                    dto.setUserName(entry.getKey());
                    dto.setAnswerDes("");
                    dto.setUseTime("");
                    stuQuickAnswerList.add(dto);
                }
            }
            map.put("stuQuickAnswer", stuQuickAnswerList);

            for (InteractLessonAnswerCountDTO dto : answerCountList) {
                int count = 0;
                if (dto.getAnswer() != 0) {
                    count = countMap.get(dto.getAnswer()) == null ? 0 : countMap.get(dto.getAnswer());
                } else {
                    count = totalStudent - signIn.size();
                }
                dto.setCount(count);
                int rate = count * 100 / totalStudent;
                String rateStr = rate + "%";
                dto.setRate(rateStr);
            }
            map.put("answerCount", answerCountList);
        }
        return map;
    }

}
