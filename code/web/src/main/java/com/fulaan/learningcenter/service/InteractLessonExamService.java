package com.fulaan.learningcenter.service;

import com.db.school.InteractLessonExamDao;
import com.fulaan.learningcenter.dto.InteractLessonAnswerCountDTO;
import com.fulaan.learningcenter.dto.InteractLessonExamDTO;
import com.fulaan.learningcenter.dto.InteractLessonExamDetailDTO;
import com.fulaan.myclass.service.ClassService;
import com.fulaan.user.service.UserService;
import com.fulaan.utils.pojo.KeyValue;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.school.*;
import com.pojo.user.UserEntry;
import com.sys.constants.Constant;
import com.sys.utils.DateTimeUtils;
import com.sys.utils.NumberUtils;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by guojing on 2015/11/24.
 */
@Service
public class InteractLessonExamService {

    private static final Logger logger =Logger.getLogger(InteractLessonExamService.class);

    InteractLessonExamDao interactLessonExamDao=new InteractLessonExamDao();

    @Resource
    private InteractLessonExamDetailService interactLessonExamDetailService;

    @Resource
    private InteractLessonService interactLessonService;

    @Resource
    private ClassService classService;

    @Resource
    private UserService userService;

    @Resource
    private InteractLessonFileService interactLessonFileService;

    //记录考试次数
    private static Map<String,Integer> timesMap = new HashMap<String,Integer>();

    /**
     * 新建互动课堂考试
     * @param e
     * @return
     */
    public ObjectId addInteractLessonExamEntry(InteractLessonExamEntry e)
    {
        return interactLessonExamDao.addInteractLessonExamEntry(e);
    }

    /**
     * 新建互动课堂考试
     * @param content
     */
    public void addInteractLessonExamEntry(int type, String content) {
        try {
            //将json字符串转换成json对象
            JSONObject dataJson=new JSONObject(content);
            //获取互动课堂Id
            String lessonId=dataJson.getString("lessonId");
            ObjectId lid=new ObjectId(lessonId);

            //试卷名称
            String examName = dataJson.getString("examName");

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

            //添加考试详细信息集合
            List<DBObject> addDetailList = new ArrayList<DBObject>();
            InteractLessonExamEntry examEntry=new InteractLessonExamEntry(lid, uid, type, times, examName);
            if(type==1) {

                ObjectId examId = addInteractLessonExamEntry(examEntry);
                //获取互动课堂考试data
                JSONObject info = dataJson.getJSONObject("data");
                //获取试题信息
                JSONArray questions = info.getJSONArray("questions");
                //遍历试题信息
                for (int j = 0; j < questions.length(); j++) {
                    JSONObject que = questions.getJSONObject(j);
                    DBObject baseEntry=handleJsonObject(lid, examId, uid, times, examName, type, que);
                    addDetailList.add(baseEntry);
                }
            }

            if(type==2){
                //考试正确率
                int correctRate = dataJson.getInt("correctRate");
                examEntry.setCorrectRate(correctRate);
                //String correctRateStr = correctRate+"%";
                //考试用时
                int useTime = dataJson.getInt("useTime");
                String useTimeStr = DateTimeUtils.getDuration(useTime);
                examEntry.setUseTime(useTimeStr);

                ObjectId examId = addInteractLessonExamEntry(examEntry);

                //获取互动课堂学生考试data
                JSONArray datas = dataJson.getJSONArray("data");
                for (int j = 0; j < datas.length(); j++) {
                    JSONObject info = datas.getJSONObject(j);
                    DBObject baseEntry=handleJsonObject(lid, examId, uid, times, examName, type, info);
                    addDetailList.add(baseEntry);
                }
            }
            interactLessonExamDetailService.addInteractLessonExamDetailEntryList(addDetailList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据类型处理Json对象
     * @param lid
     * @param examId
     * @param uid
     * @param times
     * @param examName
     * @param type
     * @param info
     * @return
     * @throws Exception
     */
    private  DBObject handleJsonObject(ObjectId lid, ObjectId examId, ObjectId uid,int times,String examName,int type,JSONObject info) throws Exception{
        //题目答案
        String answerDes = "";
        //题目答案
        int answer = 0;
        //正确答案
        String correctDes = "";
        //正确答案
        int correct = 0;
        //答题结果
        int result = 0;
        //题目内容
        String question = "";
        //获取题目序号
        int nb = info.getInt("id");
        InteractLessonQuestionType questionType =null;
        if (type == 2) {
            //获取题目类型
            int format = info.getInt("format");
            questionType = InteractLessonQuestionType.getInteractLessonQuestionType(format);
            result = info.getInt("result");
        }else{
            //获取题目类型
            String format = info.getString("format");
            questionType = InteractLessonQuestionType.getInteractLessonQuestionType(format);
            //获取题目内容
            question = info.getString("question").replace("\n", "").replace("/<BR>/ig", "");
        }

        String qFormat = questionType.getType();
        String qFormatDes = questionType.getDes();

        if (InteractLessonQuestionType.isChoiceOrBoolOrMulti(questionType.getId())) {
            answer = info.getInt("answer");
            answerDes = InteractLessonAnswerType.getInteractLessonAnswerTypeDes(answer);
            if (type == 2) {
                correct = info.getInt("correct");
                correctDes = InteractLessonAnswerType.getInteractLessonAnswerTypeDes(correct);
            }
        } else {
            if (type == 2) {
                answerDes = info.getString("answer");
                if(questionType.getId()!=InteractLessonQuestionType.SUBJECTIVE.getId()) {
                    correctDes = info.getString("correct");
                }
            }else{
                if(questionType.getId()!=InteractLessonQuestionType.SUBJECTIVE.getId()){
                    answerDes = info.getString("answer");
                }
            }
        }
        InteractLessonExamDetailEntry entry = new InteractLessonExamDetailEntry(
                lid,
                examId,
                uid,
                type,
                times,
                examName,
                nb,
                qFormat,
                qFormatDes,
                answer,
                answerDes,
                correct,
                correctDes,
                result,
                question
        );
        return entry.getBaseEntry();
    }
    /**
     * 获取互动课堂考试次数
     * @param lessonId
     * @return
     */
    public List<KeyValue> getExamTimesList(ObjectId lessonId) {
        List<KeyValue> resultList=new ArrayList<KeyValue>();
        DBObject fields =new BasicDBObject("ts", Constant.ONE);
        List<InteractLessonExamEntry> list=interactLessonExamDao.findExamEntryList(lessonId, 1, 0, fields, "ts");
        if(list!=null&&list.size()>0){
            InteractLessonExamEntry entry=list.get(0);
            int maxVal=entry.getTimes();
            for(int i=1;i<=maxVal;i++){
                KeyValue keyValue = new KeyValue();
                keyValue.setKey(i);
                String value="第"+ NumberUtils.formatInteger(i)+"次考试";
                keyValue.setValue(value);
                resultList.add(keyValue);
            }
        }
        return resultList;
    }


    /**
     * 获取互动课堂考试列表
     * @param lessonId
     * @param type
     * @param times
     * @return
     */
    public void getInteractLessonExamList(ObjectId lessonId, ObjectId classId, int type, int times, Map<String, Object> map) {
        List<InteractLessonExamDTO> resultList=new ArrayList<InteractLessonExamDTO>();
        ClassEntry e=classService.getClassEntryById(classId, null);
        List<ObjectId> stuids = new ArrayList<ObjectId>();
        if(e!=null){
            stuids=e.getStudents();
        }


        List<InteractLessonExamEntry> list=interactLessonExamDao.findExamEntryList(lessonId, type, times, null, "cr");

        for(InteractLessonExamEntry item : list){
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

        DBObject fields =new BasicDBObject("eid", Constant.ONE)
                .append("ui", Constant.ONE)
                .append("ts", Constant.ONE)
                .append("nb", Constant.ONE)
                .append("ft", Constant.ONE)
                .append("ard", Constant.ONE)
                .append("ret",Constant.ONE);
        Map<ObjectId,List<InteractLessonExamDetailEntry>> examDetailMap= interactLessonExamDetailService.getExamDetailEntryMap(lessonId, type, times, fields, "nb");

        Map<String,InteractLessonFileEntry> examFileMap= interactLessonFileService.getExamFileEntryMap(lessonId, InteractLessonFileType.SUBJECTIVE.getType());

        Set<ObjectId> signIn = new HashSet<ObjectId>();
        for(InteractLessonExamEntry entry : list){
            InteractLessonExamDTO dto=new InteractLessonExamDTO(entry);
            UserEntry userEntry=userMap.get(entry.getUserId());
            ObjectId userId=null;
            if(userEntry!=null){
                userId=uMap.get(userEntry.getUserName());
                if(userId==null) {
                    continue;
                }
                dto.setUserId(userId.toString());
                dto.setUserName(userEntry.getUserName());
            }else{
                continue;
            }

            signIn.add(userId);

            List<InteractLessonExamDetailEntry> examDetailList=examDetailMap.get(entry.getID());

            if(examDetailList!=null&&examDetailList.size()>0){
                List<InteractLessonExamDetailDTO> examDetailDtoList=new ArrayList<InteractLessonExamDetailDTO>();
                for(InteractLessonExamDetailEntry examDetailEntry : examDetailList){
                    InteractLessonExamDetailDTO examDetailDTO=new InteractLessonExamDetailDTO();
                    examDetailDTO.setId(examDetailEntry.getID().toString());
                    examDetailDTO.setExamId(examDetailEntry.getExamId().toString());
                    //examDetailDTO.setUserId(examDetailEntry.getUserId().toString());
                    examDetailDTO.setTimes(examDetailEntry.getTimes());
                    examDetailDTO.setNumber(examDetailEntry.getNumber());
                    examDetailDTO.setFormat(examDetailEntry.getFormat());
                    examDetailDTO.setAnswerDes(examDetailEntry.getAnswerDes());
                    if(InteractLessonQuestionType.SUBJECTIVE.getType().equals(examDetailEntry.getFormat())){
                        InteractLessonFileEntry fileEntry=examFileMap.get(examDetailEntry.getAnswerDes());
                        if(fileEntry!=null){
                            examDetailDTO.setAnswerFilePath(fileEntry.getFilePath());
                        }else{
                            examDetailDTO.setAnswerFilePath("");
                        }
                    }
                    examDetailDTO.setResult(examDetailEntry.getResult());
                    examDetailDtoList.add(examDetailDTO);
                }
                dto.setExamDetailList(examDetailDtoList);
            }
            resultList.add(dto);
        }
        if(signIn.size()==0){
            map.put("hiddenEcharts","Y");
        }else {
            map.put("hiddenEcharts","N");
            for (Map.Entry<String, ObjectId> entry : uMap.entrySet()) {
                if(!signIn.contains(entry.getValue())){
                    InteractLessonExamDTO dto = new InteractLessonExamDTO();
                    dto.setLessonId(lessonId.toString());
                    dto.setId(entry.getValue().toString());
                    dto.setUserName(entry.getKey());
                    dto.setUseTime("");
                    resultList.add(dto);
                }
            }
            map.put("examList", resultList);
        }
        //return resultList;
    }

    /**
     * 查询学生考试-单题模式-题目编号列表
     * @param lessonId
     * @param type
     * @param times
     * @return
     */
    public List<KeyValue> getExamQuestionNumberList(ObjectId lessonId, int type, int times) {
        List<KeyValue> resultList=new ArrayList<KeyValue>();
        DBObject fields =new BasicDBObject("nb", Constant.ONE);
        List<InteractLessonExamDetailEntry> list=interactLessonExamDetailService.getExamDetailEntryList(lessonId, type, times, 0, fields, "nb");
        if(list!=null&&list.size()>0){
            InteractLessonExamDetailEntry entry=list.get(list.size()-1);
            int maxVal=entry.getNumber();
            for(int i=1;i<=maxVal;i++){
                KeyValue keyValue = new KeyValue();
                keyValue.setKey(i);
                String value="第"+ NumberUtils.formatInteger(i)+"题";
                keyValue.setValue(value);
                resultList.add(keyValue);
            }
        }
        return resultList;
    }

    /**
     * 查询学生考试-单题模式
     * @param lessonId
     * @param classId
     * @param times
     * @param number
     * @return
     */
    public Map<String, Object> getInteractLessonSingleQuestionList(ObjectId lessonId, ObjectId classId, int type, int times, int number) {

        Map<String,Object> map=new HashMap<String, Object>();

        List<InteractLessonAnswerCountDTO> answerCountList=new ArrayList<InteractLessonAnswerCountDTO>();

        List<InteractLessonExamDetailDTO> stuExamDetailList=new ArrayList<InteractLessonExamDetailDTO>();

        ClassEntry e=classService.getClassEntryById(classId, null);
        List<ObjectId> stuids = new ArrayList<ObjectId>();
        int totalStudent=1;
        if(e!=null){
            if(e.getStudents()!=null&&e.getStudents().size()>0){
                totalStudent=e.getStudents().size();
                stuids=e.getStudents();
            }
        }

        List<InteractLessonExamDetailEntry> list=interactLessonExamDetailService.getExamDetailEntryList(lessonId, type, times, number, null, "nb");
        for(InteractLessonExamDetailEntry item : list){
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

        Map<String,InteractLessonFileEntry> examFileMap= interactLessonFileService.getExamFileEntryMap(lessonId, InteractLessonFileType.SUBJECTIVE.getType());

        Map<Integer,Integer> countMap=new HashMap<Integer, Integer>();
        String format="";
        Set<ObjectId> signIn = new HashSet<ObjectId>();
        int doCount=0;
        for(InteractLessonExamDetailEntry entry:list){
            if(entry.getType()==1) {
                format=entry.getFormat();
                map.put("format",format);
                map.put("answer",entry.getAnswerDes());
                if(InteractLessonQuestionType.isNotChoiceAndBool(format)) {
                    for(int i=0;i<3;i++) {
                        InteractLessonAnswerCountDTO answerCountDTO = new InteractLessonAnswerCountDTO();
                        answerCountDTO.setAnswer(i);
                        if(i==0) {
                            answerCountDTO.setAnswerDes("正确");
                        }
                        if(i==1) {
                            answerCountDTO.setAnswerDes("错误");
                        }
                        if(i==2) {
                            answerCountDTO.setAnswerDes("未做");
                        }
                        answerCountList.add(answerCountDTO);
                    }
                }else{
                    List<InteractLessonAnswerType> answerTypelist = InteractLessonAnswerType.getInteractLessonAnswerType(format);
                    for (InteractLessonAnswerType answerType : answerTypelist) {
                        InteractLessonAnswerCountDTO answerCountDTO = new InteractLessonAnswerCountDTO();
                        answerCountDTO.setAnswer(answerType.getAnswer());
                        answerCountDTO.setAnswerDes(answerType.getDes());
                        answerCountList.add(answerCountDTO);
                    }
                }
            }else{
                InteractLessonExamDetailDTO quickAnswerDTO=new InteractLessonExamDetailDTO(entry);
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

                if(InteractLessonQuestionType.isNotChoiceAndBool(format)) {
                    //entry.getResult(): 0：未做，1：正确，2：错误
                    int result=0;
                    if(entry.getResult()==0) {
                        result=2;
                    }
                    if(entry.getResult()==1) {
                        result=0;
                    }
                    if(entry.getResult()==2) {
                        result=1;
                    }
                    if(entry.getResult()!=0) {
                        Integer resultCount=countMap.get(result);
                        if (resultCount != null) {
                            countMap.put(result, resultCount + 1);
                        } else {
                            countMap.put(result, 1);
                        }
                    }
                }else {
                    Integer answerCount = countMap.get(entry.getAnswer());
                    if (answerCount != null) {
                        countMap.put(entry.getAnswer(), answerCount + 1);
                    } else {
                        countMap.put(entry.getAnswer(), 1);
                    }
                }

                if(entry.getResult()!=0) {
                    doCount++;
                }
                if(InteractLessonQuestionType.SUBJECTIVE.getType().equals(entry.getFormat())){
                    InteractLessonFileEntry fileEntry = examFileMap.get(entry.getAnswerDes());
                    if(fileEntry!=null){
                        quickAnswerDTO.setAnswerFilePath(fileEntry.getFilePath());
                    }else{
                        quickAnswerDTO.setAnswerFilePath("");
                    }
                }
                stuExamDetailList.add(quickAnswerDTO);
            }
        }

        for (Map.Entry<String, ObjectId> entry : uMap.entrySet()) {
            if(!signIn.contains(entry.getValue())){
                InteractLessonExamDetailDTO dto=new InteractLessonExamDetailDTO();
                dto.setLessonId(lessonId.toString());
                dto.setUserId(entry.getValue().toString());
                dto.setUserName(entry.getKey());
                dto.setAnswerDes("");
                stuExamDetailList.add(dto);
            }
        }

        if(map.get("answer")==null){
            map.put("answer","");
        }

        map.put("stuExamDetail",stuExamDetailList);

        for(InteractLessonAnswerCountDTO dto :answerCountList){
            int count = 0;
            if(InteractLessonQuestionType.isNotChoiceAndBool(format)) {
                if(dto.getAnswer()!=2) {
                    count = countMap.get(dto.getAnswer()) == null ? 0 : countMap.get(dto.getAnswer());
                }else{
                    count = countMap.get(2) == null ? 0 : countMap.get(2);
                    count += totalStudent - doCount;
                }
            }else {
                if (dto.getAnswer() != 0) {
                    count = countMap.get(dto.getAnswer()) == null ? 0 : countMap.get(dto.getAnswer());
                } else {
                    count = countMap.get(0) == null ? 0 : countMap.get(0);
                    count += totalStudent - doCount;
                }
            }
            dto.setCount(count);
            int rate = count * 100 / totalStudent;
            String rateStr = rate + "%";
            dto.setRate(rateStr);
        }
        map.put("answerCount", answerCountList);
        return map;
    }

    /**
     * 查询考试试卷
     * @param lessonId
     * @param
     * @param times
     * @return
     */
    public List<InteractLessonExamDetailDTO> getInteractLessonExamText(ObjectId lessonId, int type, int times) {
        List<InteractLessonExamDetailDTO> examDetailList=new ArrayList<InteractLessonExamDetailDTO>();
        DBObject fields =new BasicDBObject("lid", Constant.ONE)
                .append("eid", Constant.ONE)
                .append("ts", Constant.ONE)
                .append("nm", Constant.ONE)
                .append("nb", Constant.ONE)
                .append("ft", Constant.ONE)
                .append("ftd", Constant.ONE)
                .append("ar",Constant.ONE)
                .append("ard", Constant.ONE)
                .append("qt", Constant.ONE);
        List<InteractLessonExamDetailEntry> list=interactLessonExamDetailService.getExamDetailEntryList(lessonId, type, times, 0, fields, "nb");
        for(InteractLessonExamDetailEntry examDetailEntry : list){
            InteractLessonExamDetailDTO examDetailDTO=new InteractLessonExamDetailDTO();
            examDetailDTO.setId(examDetailEntry.getID().toString());
            examDetailDTO.setLessonId(examDetailEntry.getLessonId().toString());
            examDetailDTO.setExamId(examDetailEntry.getExamId().toString());
            examDetailDTO.setTimes(examDetailEntry.getTimes());
            examDetailDTO.setExamName(examDetailEntry.getExamName());
            examDetailDTO.setNumber(examDetailEntry.getNumber());
            examDetailDTO.setFormat(examDetailEntry.getFormat());
            examDetailDTO.setFormatDes(examDetailEntry.getFormatDes());
            examDetailDTO.setAnswer(examDetailEntry.getAnswer());
            examDetailDTO.setAnswerDes(examDetailEntry.getAnswerDes());
            examDetailDTO.setQuestion(examDetailEntry.getQuestion());
            examDetailList.add(examDetailDTO);
        }
        return examDetailList;
    }
}
