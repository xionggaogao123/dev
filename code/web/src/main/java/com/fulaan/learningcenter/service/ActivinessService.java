package com.fulaan.learningcenter.service;

import com.db.school.ActivinessDao;
import com.fulaan.learningcenter.dto.ActivinessDTO;
import com.fulaan.myclass.service.ClassService;
import com.fulaan.user.service.UserService;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.school.ActivinessEntry;
import com.pojo.school.ActivinessType;
import com.pojo.school.ClassEntry;
import com.pojo.user.UserEntry;
import com.pojo.utils.DeleteState;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * 互动课堂-活跃度service
 * Created by guojing on 2015/11/19.
 */
@Service
public class ActivinessService {
    private ActivinessDao activinessDao =new ActivinessDao();

    @Resource
    private ClassService classService;

    @Autowired
    private UserService userService;

    /**
     * 新建学生活跃度
     * @param e
     * @return
     */
    public ObjectId addActivinessEntry(ActivinessEntry e)
    {
        return activinessDao.addActivinessEntry(e);
    }

    /**
     * 新建学生活跃度
     * @param list
     * @return
     */
    public void addActivinessEntryList(List<DBObject> list)
    {
        activinessDao.addActivinessEntryList(list);
    }

    /**
     * 查询学生活跃度
     * @param lessonId
     * @param studentId
     * @param type
     * @param ds
     * @return
     */
    public ActivinessEntry findActivinessEntry(ObjectId lessonId, ObjectId studentId, int type, DeleteState ds)
    {
        return activinessDao.findActivinessEntry(lessonId,studentId,type,ds);
    }

    public Map<String, Object> getLessonActivinessList(ObjectId lessonId,ObjectId classId) {
        Map<String, Object> resultMap=new HashMap<String, Object>();
        ClassEntry e=classService.getClassEntryById(classId, null);
        List<ObjectId> stuids = new ArrayList<ObjectId>();
        if(e!=null){
            stuids=e.getStudents();
        }

        List<ActivinessEntry> list=findActivinessList(lessonId,null,0,DeleteState.NORMAL);

        List<ObjectId> subStuIds= MongoUtils.getFieldObjectIDs(list, "stid");
        stuids.addAll(subStuIds);

        Map<ObjectId, UserEntry> userMap=userService.getUserEntryMap2(stuids, new BasicDBObject("nm",1).append("avt", 1).append("ir",1));

        Map<ObjectId, ActivinessDTO> scoreMap=new HashMap<ObjectId, ActivinessDTO>();
        Map<ObjectId, ActivinessDTO> countMap=new HashMap<ObjectId, ActivinessDTO>();
        Set<ObjectId> signIn = new HashSet<ObjectId>();

        int answerCount=0;
        int answeredCount=0;

        Map<String, ObjectId> uMap=new HashMap<String, ObjectId>();
        for (UserEntry v : userMap.values()) {
            if(v.getIsRemove()== Constant.ZERO) {
                uMap.put(v.getUserName(),v.getID());
            }
        }

        for(ActivinessEntry entry:list){
            UserEntry userEntry=userMap.get(entry.getStudentId());
            ObjectId userId=null;
            if(userEntry!=null){
                userId=uMap.get(userEntry.getUserName());
                if(userId==null) {
                    continue;
                }
            }else{
                continue;
            }
            signIn.add(userId);
            ActivinessDTO scoreTotal = scoreMap.get(userId);
            if (scoreTotal == null) {
                scoreTotal = new ActivinessDTO();
                scoreTotal.setId(entry.getID().toString());
                scoreTotal.setLessonId(entry.getLessonId().toString());
                scoreTotal.setStuId(userId.toString());
                scoreTotal.setStuName(userEntry.getUserName());
            }
            scoreTotal.setScoreTotal(scoreTotal.getScoreTotal() + entry.getActiviness());
            scoreMap.put(userId, scoreTotal);

            ActivinessDTO countTimes = countMap.get(userId);
            if (countTimes == null) {
                countTimes = new ActivinessDTO();
                countTimes.setId(entry.getID().toString());
                countTimes.setLessonId(entry.getLessonId().toString());
                countTimes.setStuId(userId.toString());
                countTimes.setStuName(userEntry.getUserName());
            }
            if (entry.getType() == ActivinessType.ANSWER.getType() || entry.getType() == ActivinessType.ANSWERED.getType()) {
                //学生抢答（举手）
                if (entry.getType() == ActivinessType.ANSWER.getType()) {
                    countTimes.setRaceScore(countTimes.getRaceScore() + entry.getActiviness());
                    countTimes.setRaceCount(countTimes.getRaceCount() + entry.getCount());
                    answerCount += entry.getCount();
                }
                //学生抢答到
                if (entry.getType() == ActivinessType.ANSWERED.getType()) {
                    countTimes.setAnswerScore(countTimes.getAnswerScore() + entry.getActiviness());
                    countTimes.setAnswerCount(countTimes.getAnswerCount() + entry.getCount());
                    answeredCount += entry.getCount();
                }
            }
            countMap.put(userId, countTimes);

        }

        if(signIn.size()==0){
            resultMap.put("hiddenEcharts","Y");
        }else{
            for (Map.Entry<String, ObjectId> entry : uMap.entrySet()) {
                if(!signIn.contains(entry.getValue())){
                    ActivinessDTO scoreTotal=new ActivinessDTO();
                    ActivinessDTO countTimes=new ActivinessDTO();
                    scoreTotal.setLessonId(lessonId.toString());
                    scoreTotal.setStuId(entry.getValue().toString());
                    scoreTotal.setStuName(entry.getKey());
                    scoreTotal.setScoreTotal(0);
                    countTimes.setLessonId(lessonId.toString());
                    countTimes.setStuId(entry.getValue().toString());
                    countTimes.setStuName(entry.getKey());
                    countTimes.setAnswerScore(0);
                    countTimes.setAnswerCount(0);
                    countTimes.setRaceScore(0);
                    countTimes.setRaceCount(0);
                    scoreMap.put(entry.getValue(), scoreTotal);
                    countMap.put(entry.getValue(),countTimes);
                }
            }

            resultMap.put("hiddenEcharts","N");

            if(answerCount==0&&answeredCount==0){
                resultMap.put("hiddenTable","Y");
            }else{
                resultMap.put("hiddenTable","N");
            }

            List<ActivinessDTO> scoreTotalList=handelMapToList(scoreMap,1);
            List<ActivinessDTO> countTimesList=handelMapToList(countMap,2);
            resultMap.put("scoreTotal",scoreTotalList);
            resultMap.put("countTimes",countTimesList);
        }

        return resultMap;
    }

    private List<ActivinessDTO> handelMapToList(Map<ObjectId, ActivinessDTO> map, int type) {
        List<ActivinessDTO> list=new ArrayList<ActivinessDTO>();
        for (ActivinessDTO dto : map.values()) {
            list.add(dto);
        }
        if(type==1) {
            Collections.sort(list, new Comparator<ActivinessDTO>() {
                public int compare(ActivinessDTO obj1, ActivinessDTO obj2) {
                    int flag = obj2.getScoreTotal()-obj1.getScoreTotal();
                    if (flag == 0) {
                        flag =obj2.getAnswerCount()-obj1.getAnswerCount();
                        if(flag == 0){
                            flag =obj2.getRaceCount() -obj1.getRaceCount();
                            if(flag == 0){
                                return obj2.getStuName().compareTo(obj1.getStuName());
                            }else{
                                return flag;
                            }
                        }else{
                            return flag;
                        }
                    } else {
                        return flag;
                    }
                }
            });
        }
        if(type==2) {
            Collections.sort(list, new Comparator<ActivinessDTO>() {
                public int compare(ActivinessDTO obj1, ActivinessDTO obj2) {
                    int flag =obj2.getAnswerCount()-obj1.getAnswerCount();
                    if (flag == 0) {
                        flag =obj2.getRaceCount() -obj1.getRaceCount();
                        if(flag == 0){
                            return obj2.getStuName().compareTo(obj1.getStuName());
                        }else{
                            return flag;
                        }
                    } else {
                        return flag;
                    }
                }
            });
        }
        return list;
    }

    /**
     * 查询学生活跃度
     * @param lessonId
     * @param studentId
     * @param type
     * @param ds
     * @return
     */
    public List<ActivinessEntry> findActivinessList(ObjectId lessonId, ObjectId studentId, int type, DeleteState ds){
        return activinessDao.findActivinessList(lessonId,studentId,type,ds);
    }

    /**
     * 修改学生活跃度
     * @param e
     */
    public void updActivinessEntry(ActivinessEntry e)
    {
        activinessDao.updActivinessEntry(e);
    }

    /**
     * 添加学生活跃度
     * @param content
     * @param teacherId
     */
    public void addActivinessEntry(String content,String teacherId) {
        try {
            //将json字符串转换成json对象
            JSONObject dataJson=new JSONObject(content);
            //获取互动课堂Id
            String lessonId=dataJson.getString("lessonId");
            ObjectId lid=new ObjectId(lessonId);
            //获取活跃度data
            JSONArray data=dataJson.getJSONArray("data");
            //新增加学生活跃度集合
            List<DBObject> addList=new ArrayList<DBObject>();
            //更新学生活跃度集合
            List<ActivinessEntry> updList=new ArrayList<ActivinessEntry>();
            //遍历data
            for(int i=0;i<data.length();i++) {
                JSONObject info = data.getJSONObject(i);
                //获取活跃度type
                int type = info.getInt("type");
                //根据条件查询数据库中已存在的活跃度
                List<ActivinessEntry> list=findActivinessList(lid,null,type,DeleteState.NORMAL);
                //数据库中已存在的活跃度的学生id集合
                List<ObjectId> stuIds=new ArrayList<ObjectId>();
                Map<ObjectId, ActivinessEntry> tempMap=new HashMap<ObjectId, ActivinessEntry>();
                for(ActivinessEntry item:list){
                    stuIds.add(item.getStudentId());
                    tempMap.put(item.getStudentId(),item);
                }
                ActivinessType atype=ActivinessType.getActivinessType(type);
                //获取学生信息
                JSONArray students = info.getJSONArray("students");
                //遍历学生信息
                for(int j=0;j<students.length();j++) {
                    JSONObject stu = students.getJSONObject(j);
                    //获取学生id
                    String studentId = stu.getString("studentId");
                    if(!teacherId.equals(studentId)) {
                        //获取学生提交次数
                        int count = stu.getInt("count");
                        //活跃度积分
                        int activiness = 0;
                        ObjectId stuId = new ObjectId(studentId);
                        //判断学生是否已经提交过
                        if (stuIds.contains(stuId)) {
                            ActivinessEntry tempEntry = tempMap.get(stuId);
                            if (ActivinessType.isNotLoginAndUpload(type)) {
                                activiness = atype.getSocre() * count;
                                tempEntry.setActiviness(tempEntry.getActiviness() + activiness);
                            }
                            tempEntry.setCount(tempEntry.getCount() + count);
                            updList.add(tempEntry);
                        } else {
                            activiness = atype.getSocre();
                            ActivinessEntry entry = new ActivinessEntry(lid, stuId, type, activiness, count);
                            addList.add(entry.getBaseEntry());
                        }
                    }
                }
            }
            //批量新增活跃
            addActivinessEntryList(addList);
            //修改活跃度
            for(ActivinessEntry entry: updList) {
                updActivinessEntry(entry);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
