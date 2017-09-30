package com.fulaan.questionbook.service;

import com.db.questionbook.QuestionAdditionDao;
import com.db.questionbook.QuestionBookDao;
import com.fulaan.questionbook.dto.QuestionAdditionDTO;
import com.fulaan.questionbook.dto.QuestionBookDTO;
import com.pojo.questionbook.QuestionAdditionEntry;
import com.pojo.questionbook.QuestionBookEntry;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

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

    /**
     * 删除已学会的错题
     */
    public void delAlreadyEntry(ObjectId id){
        questionBookDao.delEntry(id);
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
    /**
     * 今日复习
     */
    public List<QuestionBookDTO> getReviewList(ObjectId userId){
        //获得当前时间
        long current=System.currentTimeMillis();
        //获得时间批次
        long zero = current/(1000*3600*24)*(1000*3600*24)- TimeZone.getDefault().getRawOffset();//今天零点零分零秒的毫秒数
        List<QuestionBookEntry> entries = questionBookDao.getReviewList(userId,zero);
        List<QuestionBookDTO> dtos = new ArrayList<QuestionBookDTO>();
        if(entries.size()>0){
            for(QuestionBookEntry entry : entries){
                dtos.add(new QuestionBookDTO(entry));
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
                questionBookDao.changeQuestionBook(questionId);
            }
        }
    }
}
