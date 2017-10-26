package com.fulaan.questionbook.service;

import com.db.questionbook.QuestionAdditionDao;
import com.db.questionbook.QuestionBookDao;
import com.fulaan.questionbook.dto.QuestionAdditionDTO;
import com.fulaan.questionbook.dto.QuestionBookDTO;
import com.pojo.questionbook.QuestionAdditionEntry;
import com.pojo.questionbook.QuestionBookEntry;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by James on 2017/9/30.
 */
@Service
public class QuestionBookService {

    private QuestionAdditionDao questionAdditionDao = new QuestionAdditionDao();

    private QuestionBookDao questionBookDao = new QuestionBookDao();


    /**
     * 添加错题
     * @param dto
     * @param answerContent
     * @param answerList
     * @param analysisContent
     * @param analysisList
     * @return
     */
    public String addQuestionBookEntry(QuestionBookDTO dto,String answerContent,List<String> answerList,String analysisContent,List<String> analysisList){
        //保存作业
        QuestionBookEntry entry = dto.buildAddEntry();
        //获得当前时间
        long current=System.currentTimeMillis();
        //获得时间批次
        long zero = current/(1000*3600*24)*(1000*3600*24)- TimeZone.getDefault().getRawOffset();//今天零点零分零秒的毫秒数
        entry.setPageTime(zero);
        entry.setType(1);
        entry.setLevel(1);
        entry.setDateTime(current);
        ObjectId oid = questionBookDao.addEntry(entry);
        //保存答案
        QuestionAdditionDTO dto1 = new QuestionAdditionDTO();
        dto1.setContent(answerContent);
        dto1.setAnswerList(answerList);
        dto1.setParentId(oid.toString());
        dto1.setAnswerType(1);//作业答案
        QuestionAdditionEntry entry1 = dto1.buildAddEntry();
        questionAdditionDao.addEntry(entry1);
        //保存解析
        QuestionAdditionDTO dto2 = new QuestionAdditionDTO();
        dto2.setContent(analysisContent);
        dto2.setAnswerList(analysisList);
        dto2.setParentId(oid.toString());
        dto2.setAnswerType(2);//作业解析
        QuestionAdditionEntry entry2 = dto2.buildAddEntry();
        questionAdditionDao.addEntry(entry2);
        return "添加成功";
    }
    public String addQuestionNewBookEntry(QuestionBookDTO dto){
        //保存作业
        QuestionBookEntry entry = dto.buildAddEntry();
        //获得当前时间
        long current=System.currentTimeMillis();
        //获得时间批次
        long zero = current/(1000*3600*24)*(1000*3600*24)- TimeZone.getDefault().getRawOffset();//今天零点零分零秒的毫秒数
        entry.setPageTime(zero);
        entry.setType(1);
        entry.setLevel(1);
        entry.setDateTime(current);
        ObjectId oid = questionBookDao.addEntry(entry);

        return oid.toString();
    }

    /**
     * 删除已学会的错题
     */
    public void delAlreadyEntry(ObjectId id){
        questionBookDao.delEntry(id);
    }
    /**
     * 修改错题
     */
    public void updateEntry(QuestionBookDTO dto){
        questionBookDao.updateEntry(dto.buildAddEntry());
    }
    /**
     * 修改解析
     */
    public void updateAnswerEntry(QuestionAdditionDTO dto){
        questionAdditionDao.updateEntry(dto.buildAddEntry());
    }

    /**
     * 查询一个错题
     * @param id
     */
     public Map<String,Object> getEntry(ObjectId id){
         Map<String,Object> map = new HashMap<String, Object>();
         QuestionBookEntry entry = questionBookDao.getEntryById(id);
         if(entry != null){
             List<QuestionAdditionEntry> entries = questionAdditionDao.getListByParentId(entry.getID());
             map.put("obj",new QuestionBookDTO(entry));
             List<QuestionAdditionDTO> dtos = new ArrayList<QuestionAdditionDTO>();
             if(entries.size()>0){
                 for(QuestionAdditionEntry entry1 : entries){
                     dtos.add(new QuestionAdditionDTO(entry1));
                 }
             }
             List<QuestionAdditionDTO> dtos1 = new ArrayList<QuestionAdditionDTO>();
             List<QuestionAdditionDTO> dtos2 = new ArrayList<QuestionAdditionDTO>();
             List<QuestionAdditionDTO> dtos3 = new ArrayList<QuestionAdditionDTO>();
             for(QuestionAdditionDTO dto3 : dtos){
                 if(dto3.getAnswerType()==1){
                     dtos1.add(dto3);
                 }else if(dto3.getAnswerType()==2){
                     dtos2.add(dto3);
                 }else{
                     dtos3.add(dto3);
                 }
             }
             map.put("list1",dtos1);//答案
             map.put("list2",dtos2);//解析
             map.put("list3",dtos3);//我的回答
         }
         return map;
    }

    /**
     * 多条件组合查询列表
     */
    public List<QuestionBookDTO> getQuestionList(String gradeId,String subjectId,String questionTypeId,String testId,int type,int page,int pageSize,String keyword,ObjectId userId){
        List<QuestionBookEntry> entries = questionBookDao.getQuestionList(gradeId, subjectId, questionTypeId, testId, type, page, pageSize, keyword,userId);
        List<QuestionBookDTO> dtos = new ArrayList<QuestionBookDTO>();
        if(entries.size()>0){
            for(QuestionBookEntry entry : entries){
                dtos.add(new QuestionBookDTO(entry));
            }
        }
        return dtos;
    }
    public void addAnswerEntry(QuestionAdditionDTO dto){
        QuestionAdditionEntry entry = dto.buildAddEntry();
        questionAdditionDao.addEntry(entry);
    }
    /**
     * 今日复习
     */
    public List<QuestionBookDTO> getReviewList(ObjectId userId){
        //获得当前时间
        long current=System.currentTimeMillis();
        //获得时间批次
        long zero = current/(1000*3600*24)*(1000*3600*24)- TimeZone.getDefault().getRawOffset();//今天零点零分零秒的毫秒数
        List<QuestionBookEntry> entries = questionBookDao.getReviewList(userId,zero+1);
        List<QuestionBookDTO> dtos = new ArrayList<QuestionBookDTO>();
        List<ObjectId> olist = new ArrayList<ObjectId>();
        if(entries.size()>0){
            for(QuestionBookEntry entry : entries){
                olist.add(entry.getID());
                dtos.add(new QuestionBookDTO(entry));
            }
        }
        //todo
        List<QuestionAdditionEntry> entries1 = questionAdditionDao.getListByParentIdList(olist);
        for(QuestionBookDTO dto : dtos){
            for(QuestionAdditionEntry entry : entries1){
                if(dto.getId() != null && dto.getId().equals(entry.getParentId().toString())){
                    //1 答案 2解析 3 解答
                    if(entry.getAnswerType()== 2){
                        List<QuestionAdditionDTO> jxList = dto.getJxList();
                        jxList.add(new QuestionAdditionDTO(entry));
                        dto.setJxList(jxList);
                    }else if(entry.getAnswerType()==1){
                        List<QuestionAdditionDTO> daList = dto.getDaList();
                        daList.add(new QuestionAdditionDTO(entry));
                        dto.setDaList(daList);
                    }else{
                        List<QuestionAdditionDTO> wdList = dto.getWdList();
                        wdList.add(new QuestionAdditionDTO(entry));
                        dto.setWdList(wdList);
                    }
                }
            }
        }
        return dtos;
    }
    /**
     * 今日复习展示
     */

    public void updateQuestionBook(ObjectId questionId,int type,int level){
        //获得当前时间
        long current=System.currentTimeMillis();
        //获得时间批次
        long zero = current/(1000*3600*24)*(1000*3600*24)- TimeZone.getDefault().getRawOffset();//今天零点零分零秒的毫秒数
        if(type==1){
            //只要点不会，明天继续展示
            long time = zero + 1*24*60*60*1000;
            questionBookDao.updateQuestionBook(questionId,time,1);
        }else{
            if(level==1){
                //改为7天后显示
                long time = zero + 7*24*60*60*1000;
                questionBookDao.updateQuestionBook(questionId,time,2);
            }else if(level==2){
                //改为30天后显示
                long time = zero + 30*24*60*60*1000;
                questionBookDao.updateQuestionBook(questionId,time,3);
            }else{
                //改为已学会
                questionBookDao.changeQuestionBook(questionId,current);
            }
        }
    }
    /**
     * 我又不会了
     */
    public void changeEntryState(ObjectId questionId){
        //获得当前时间
        long current=System.currentTimeMillis();
        //获得时间批次
        long zero = current/(1000*3600*24)*(1000*3600*24)- TimeZone.getDefault().getRawOffset();//今天零点零分零秒的毫秒数
        questionBookDao.changeUnQuestionBook(questionId,zero,current);
    }
}
