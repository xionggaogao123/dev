package com.fulaan.wrongquestion.service;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.db.newVersionGrade.NewVersionGradeDao;
import com.db.wrongquestion.*;
import com.fulaan.utils.pojo.KeyValue;
import com.fulaan.wrongquestion.dto.*;
import com.fulaan.wrongquestion.dto.ErrorBookDTO.AnswerExplainDTO;
import com.fulaan.wrongquestion.dto.ErrorBookDTO.ErrorBookAttachDTO;
import com.pojo.newVersionGrade.NewVersionGradeEntry;
import com.pojo.wrongquestion.*;
import com.pojo.wrongquestion.ErrorBookEntry.AnswerExplain;
import com.pojo.wrongquestion.ErrorBookEntry.ErrorBookAttach;
import com.sys.constants.Constant;
import com.sys.utils.DateTimeUtils;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by James on 2017/9/6.
 */
@Service
public class WrongQuestionService {

    private NewVersionGradeDao newVersionGradeDao = new NewVersionGradeDao();

    private SubjectClassDao subjectClassDao = new SubjectClassDao();

    private ErrorBookDao errorBookDao = new ErrorBookDao();

    private CreateGradeDao createGradeDao = new CreateGradeDao();

    private QuestionTypeDao questionTypeDao = new QuestionTypeDao();

    private TestTypeDao testTypeDao = new TestTypeDao();

    /**
     * 获取当前学期
     * @return
     */
    public KeyValue getCurrTermType() {
        KeyValue value=new KeyValue();
        DateTimeUtils time=new DateTimeUtils();
        //取得当前月
        int currMonth=time.getMonth();
        //取得当前年
        int currYear=time.getYear();
        int numb=1;
        for(int year=2017;year<=currYear;year++) {
            String schoolYear;
            if(year==currYear)
            {
                if (currMonth <= 2) {
                    schoolYear = (year - 1) + "-" + year + "学年";
                    value.setKey(numb++);
                    value.setValue(schoolYear);
                }else {
                    numb++;
                    if (currMonth < 9) {
                        schoolYear = (year - 1) + "-" + year + "学年";
                        value.setKey(numb++);
                        value.setValue(schoolYear);
                    }
                    if (currMonth >= 9) {
                        numb++;
                        schoolYear = year + "-" + (year + 1) + "学年";
                        value.setKey(numb++);
                        value.setValue(schoolYear);
                    }
                }
            }else {
                numb+=2;
            }
        }
        return value;
    }

    /**
     * 绑定年级
     */
    public String addGradeFromUser(NewVersionGradeDTO dto){
        KeyValue keyValue = this.getCurrTermType();
        dto.setYear(keyValue.getValue());
        NewVersionGradeEntry entry = dto.buildAddEntry();
        newVersionGradeDao.saveNewVersionGradeEntry(entry);
        return "成功绑定";
    }

    /**
     * 添加年级
     */
    public String addGradeEntry(CreateGradeDTO dto){
        CreateGradeEntry entry = dto.buildAddEntry();
        return createGradeDao.addCreateGradeEntry(entry).toString();
    }

    /**
     * 添加年级
     */
    public String addSubjectEntry(SubjectClassDTO dto){
        SubjectClassEntry entry = dto.buildAddEntry();
        return subjectClassDao.addSubjectEntry(entry).toString();
    }
    /**
     * 初次加载
     */
    public Map<String,Object> getGradeAndSubject(ObjectId userId){
        Map<String,Object> map = new HashMap<String, Object>();
       /* List<SubjectClassEntry> enties = subjectClassDao.getList();
        List<SubjectClassDTO> dtos = new ArrayList<SubjectClassDTO>();
        if(enties.size()>0){
            for(SubjectClassEntry en : enties){
                dtos.add(new SubjectClassDTO(en));
            }
        }
        map.put("subjectList",dtos);*/
        //获取当前学期
        KeyValue keyValue = this.getCurrTermType();
        NewVersionGradeEntry entry = newVersionGradeDao.getEntryByCondition(userId, keyValue.getValue());
        List<CreateGradeDTO> mlist = new ArrayList<CreateGradeDTO>();
        if(entry==null){
            NewVersionGradeDTO ndto = new NewVersionGradeDTO();
            //获得最大的type
            int number = newVersionGradeDao.getMaxGradeType(userId);
            if(number ==12){
                ndto.setGradeType(number);
            }else{
                ndto.setGradeType(number +1);
            }
            ndto.setUserId(userId.toString());
            ndto.setYear(keyValue.getValue());
            NewVersionGradeEntry entry1 = ndto.buildAddEntry();
            newVersionGradeDao.saveNewVersionGradeEntry(entry1);
            entry = newVersionGradeDao.getEntryByCondition(userId, keyValue.getValue());
        }else{

        }
        List<CreateGradeEntry> clist = new ArrayList<CreateGradeEntry>();
        CreateGradeEntry en3 = createGradeDao.getEntryByType(entry.getGradeType());
        clist.add(en3);
        CreateGradeEntry en4 = createGradeDao.getEntryByType(entry.getGradeType()+1);
        clist.add(en4);
        if(clist.size()>0){
            for(CreateGradeEntry enty : clist){
                mlist.add(new CreateGradeDTO(enty));
            }
        }
        map.put("gradeList",mlist);

        //加载学科
        List<String> stringList =new ArrayList<String>();
        String sename = mlist.get(0).getEname();
        if(mlist.size()>0){
            stringList =  mlist.get(0).getSubjectList();
        }
        List<ObjectId> objectIdList = new ArrayList<ObjectId>();
        for(String str : stringList){
            objectIdList.add(new ObjectId(str));
        }
        List<SubjectClassEntry> entries = subjectClassDao.getListByList(objectIdList);
        List<SubjectClassDTO> dtoList =   new ArrayList<SubjectClassDTO>();
        if(entries.size()>0){
            for(SubjectClassEntry entry1 : entries){
                dtoList.add(new SubjectClassDTO(entry1));
            }
        }
        map.put("subjectList",dtoList);


        //加载问题类型
        List<QuestionTypeDTO> dtoList1 = new ArrayList<QuestionTypeDTO>();
        if(dtoList.size()>0){
            ObjectId subjectId = new ObjectId(dtoList.get(0).getId());
            List<QuestionTypeEntry> entries1 = questionTypeDao.getList(subjectId,sename);
            if(entries1.size()>0){
                for(QuestionTypeEntry entry2 : entries1){
                    dtoList1.add(new QuestionTypeDTO(entry2));
                }
            }

        }
        map.put("questionTypeList",dtoList1);

        //加载测试类型
        List<TestTypeDTO> dtoList2 = new ArrayList<TestTypeDTO>();
        if(dtoList.size()>0){
            List<TestTypeEntry> entries2 = testTypeDao.getList(sename);
            if(entries2.size()>0){
                for(TestTypeEntry entry3 : entries2){
                    dtoList2.add(new TestTypeDTO(entry3));
                }
            }

        }
        map.put("TestTypeList",dtoList2);
        return map;
    }

    /**
     * 添加错题
     * @param errorDto
     */
    public String addErrorQuestion(ErrorBookDTO errorDto) {
        ErrorBookEntry entry = errorDto.bulidEntry();
        String id = errorBookDao.addToErrorBook(entry).toString();
        return id;
    }

    /**
     * 删除错题
     *
     * @param id
     */
    public void removeErrorQuestion(ObjectId id) {
        errorBookDao.removeFromErrorBook(id);
    }

    /**
     * 根据id获取错题
     * @param id
     * @return
     */
    public ErrorBookDTO getErrorQuestion(ObjectId id) {
        ErrorBookEntry entry = errorBookDao.getFromErrorBook(id);
        if(entry != null) {
            return new ErrorBookDTO(entry);
        }
        return null;
    }

    /**
     * 添加答案解析
     *
     * @param id
     * @param explain
     * @return
     */
    public ObjectId addExplain(ObjectId id, String explain, List<Object> attaDtoList) {
        ObjectId explainId = new ObjectId();

        List<ErrorBookAttach> attachList = new ArrayList<ErrorBookAttach>();
        if(attaDtoList != null
                && !attaDtoList.isEmpty()) {
            for(Object attach : attaDtoList) {
                String attachJsonStr = JSON.toJSONString(attach);
                ErrorBookAttachDTO attachDto = JSONObject.parseObject(attachJsonStr, ErrorBookAttachDTO.class);
                attachDto.setType(2);
                ErrorBookAttach attachEntry = attachDto.bulidEntry();
                attachEntry.setIsExplainFileFlag(true);
                attachList.add(attachEntry);
            }
        }
        List<ObjectId> attachIdList = errorBookDao.addAttachment(id, attachList); // 保存答案解析中附件

        AnswerExplain answerExplain = new AnswerExplain(explainId, explain, attachIdList);
        errorBookDao.addExplainToQuestion(id, answerExplain);
        return explainId;
    }


    /**
     * 删除答案解析
     *
     * @param id
     * @param explainId
     */
    public void removeExplain(ObjectId id, ObjectId explainId) {
        ErrorBookEntry errorBookEntry = errorBookDao.getFromErrorBook(id);
        List<AnswerExplain> explainList = errorBookEntry.getAnswerExplainList();
        List<ObjectId> attatchIdList = new ArrayList<ObjectId>();
        if (explainId != null && !explainList.isEmpty()) {
            for (AnswerExplain explain : explainList) {
                if (explainId.equals(explain.getExplainId())) {
                    attatchIdList = explain.getAttachIdList();
                    break;
                }
            }
        }
        List<ErrorBookAttach> attachList = errorBookEntry.getAttachmentList();
        List<ErrorBookAttach> toRemoveAttachList = new ArrayList<ErrorBookEntry.ErrorBookAttach>();
        for (ErrorBookAttach attach : attachList) {
            if (attatchIdList.contains(attach.getErrorBookAttachId())) {
                toRemoveAttachList.add(attach);
            }
        }
        errorBookDao.removeAttachment(id, toRemoveAttachList);
        errorBookDao.removeExplainFromQuestin(id, explainId);
    }

    /**
     * 掌握该题
     *
     * @param id
     */
    public void updateQuestionGraspStatus(ObjectId id) {
        errorBookDao.updateGraspStatus(id, Constant.ONE);
    }

    /**
     * 分页查询错题
     *
     * @param userId
     * @param subjectId
     * @param pointList
     * @param keyword
     * @param page
     * @param pageSize
     * @return
     */
    public List<ErrorBookDTO> findErrorQuestionDtoList(
            ObjectId userId, ObjectId subjectId, ObjectId gradeId,
            List<ObjectId> pointList, String keyword, int page, int pageSize, boolean isErrorQuestion) {

        int skip = page < 1 ? 0 : ((page - 1) * pageSize);

        List<ErrorBookEntry> entryList =
                errorBookDao.findErrorQuestionWithPaging(userId, subjectId, gradeId, pointList, keyword, skip, pageSize, isErrorQuestion);
        List<ErrorBookDTO> dtoList = new ArrayList<ErrorBookDTO>();
        if(entryList != null
                && !entryList.isEmpty()) {
            for(ErrorBookEntry entry : entryList) {

                Map<String, ErrorBookAttachDTO> attachDtoMap =
                        new HashMap<String, ErrorBookDTO.ErrorBookAttachDTO>();

                ErrorBookDTO errorBookDto = new ErrorBookDTO(entry);
                List<ErrorBookAttachDTO> allAttachDtoList = errorBookDto.getAttachmentList();
                if (allAttachDtoList != null
                        && !allAttachDtoList.isEmpty()) { // 将所有附件放入map
                    for (ErrorBookAttachDTO attachDto : allAttachDtoList) {
                        attachDtoMap.put(attachDto.getId(), attachDto);
                    }
                }

                List<AnswerExplainDTO> explainDtoList = errorBookDto.getExplainList();
                if (explainDtoList != null && !explainDtoList.isEmpty()) {
                    for (AnswerExplainDTO explain : explainDtoList) {
                        List<String> attachIdList = explain.getAttachIdList();
                        if (attachIdList != null) {
                            List<ErrorBookAttachDTO> explainAttachList =
                                    new ArrayList<ErrorBookDTO.ErrorBookAttachDTO>();
                            for (String attachId : attachIdList) {
                                ErrorBookAttachDTO attach = attachDtoMap.get(attachId);
                                if (attach != null) {
                                    explainAttachList.add(attach);
                                    attachDtoMap.remove(attachId);
                                }
                            }
                            explain.setExplainAttachs(explainAttachList);
                        }
                    }
                }

                List<ErrorBookAttachDTO> topicAttachDtoList =
                        new ArrayList<ErrorBookDTO.ErrorBookAttachDTO>();
                for (String key : attachDtoMap.keySet()) {
                    ErrorBookAttachDTO attachDto = attachDtoMap.get(key);
                    if (attachDto != null && !attachDto.isExplainFile()) {
                        topicAttachDtoList.add(attachDto);
                    }
                }
                errorBookDto.setTopicAttachs(topicAttachDtoList);

                dtoList.add(errorBookDto);
            }
        }

        return dtoList;
}

    public int countErrorQuestion(
            ObjectId userId, ObjectId subjectId, ObjectId gradeId,
            List<ObjectId> pointList, String keyword, boolean isErrorQuestion) {
        return errorBookDao.countQuestion(userId, subjectId, gradeId, pointList, keyword, isErrorQuestion);
    }

  /*  public static void  main(String args[]){

    }*/
   /* public void Test(){

    }*/
    public static void main(String[] args){
        TestTypeDao questionTypeDao = new TestTypeDao();
        //单元测试、期中测试、期末测试、月考、同步练习、模拟测试、竞赛试题、高考真题、其他
        String[] str = {"单元测试","期中测试","期末测试","月考","同步练习","模拟测试","竞赛试题","高考真题","其他"};
        for(int a= 0;a <str.length;a++){
            TestTypeEntry entry = new TestTypeEntry();
            entry.setName(str[a]);
            entry.setSename("senior");
            questionTypeDao.addTestTypeEntry(entry);
        }
    }
}
