package com.fulaan.questionbook.service;

import com.db.questionbook.QuestionAdditionDao;
import com.db.questionbook.QuestionBookDao;
import com.db.questionbook.QuestionTagsDao;
import com.db.questionbook.QuestionWebTestDao;
import com.fulaan.picturetext.runnable.PictureRunNable;
import com.fulaan.questionbook.dto.*;
import com.pojo.backstage.PictureType;
import com.pojo.questionbook.QuestionAdditionEntry;
import com.pojo.questionbook.QuestionBookEntry;
import com.pojo.questionbook.QuestionWebTestEntry;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.bson.types.ObjectId;
import org.json.JSONException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by James on 2017/9/30.
 */
@Service
public class QuestionBookService {

    private QuestionAdditionDao questionAdditionDao = new QuestionAdditionDao();

    private QuestionBookDao questionBookDao = new QuestionBookDao();

    private QuestionTagsDao questionTagsDao =new QuestionTagsDao();

    private QuestionWebTestDao questionWebTestDao = new QuestionWebTestDao();
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

        //图片检测
        List<String> alist = dto.getImageList();
        if(alist != null && alist.size()>0){
            for(String entry5 : alist){
                PictureRunNable.send(oid.toString(), dto.getUserId(), PictureType.wrongImage.getType(), 1, entry5);
            }
        }
        //保存答案
       if(dto.getAnswerText()!=null && !dto.getAnswerText().equals("")){
            QuestionAdditionDTO dto1 = new QuestionAdditionDTO();
            dto1.setContent(dto.getAnswerText());
            dto1.setAnswerList(dto.getAnswerImg());
            dto1.setParentId(oid.toString());
            dto1.setAnswerType(1);//作业答案
            QuestionAdditionEntry entry1 = dto1.buildAddEntry();
            ObjectId qid = questionAdditionDao.addEntry(entry1);
            //图片检测
            List<String> alist2 = dto.getAnswerImg();
            if(alist2 != null && alist2.size()>0){
                for(String entry5 : alist2){
                    PictureRunNable.send(qid.toString(), dto.getUserId(), PictureType.answerImage.getType(), 1, entry5);
                }
            }
        }
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
        //图片检测
        List<String> alist = dto.getImageList();
        if(alist != null && alist.size()>0){
            for(String entry5 : alist){
                PictureRunNable.send(dto.getId(), dto.getUserId(), PictureType.wrongImage.getType(), 1, entry5);
            }
        }
        questionBookDao.updateEntry(dto.buildAddEntry());
    }
    /**
     * 修改解析
     */
    public void updateAnswerEntry(ObjectId userId,QuestionAdditionDTO dto){
        //图片检测
        List<String> alist = dto.getAnswerList();
        if(alist != null && alist.size()>0){
            for(String entry5 : alist){
                PictureRunNable.send(dto.getId(), userId.toString(), PictureType.answerImage.getType(), 1, entry5);
            }
        }
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
    public  Map<String,Object> getQuestionList(String gradeId,String subjectId,String questionTypeId,String testId,int type,int page,int pageSize,String keyword,ObjectId userId){
        List<QuestionBookEntry> entries = questionBookDao.getQuestionList(gradeId, subjectId, questionTypeId, testId, type, page, pageSize, keyword,userId);
        List<QuestionBookDTO> dtos = new ArrayList<QuestionBookDTO>();
        int count = questionBookDao.getQuestionListCount(gradeId, subjectId, questionTypeId, testId, type,keyword,userId);
        Map<String,Object> map = new HashMap<String, Object>();
        if(entries.size()>0){
            for(QuestionBookEntry entry : entries){
                dtos.add(new QuestionBookDTO(entry));
            }
        }
        map.put("list",dtos);
        map.put("count",count);
        return map;
    }
    public void addAnswerEntry(ObjectId userId,QuestionAdditionDTO dto){
        QuestionAdditionEntry entry = dto.buildAddEntry();
        QuestionAdditionEntry additionEntry = questionAdditionDao.getEntry(entry.getParentId(),entry.getAnswerType(),entry.getLevel());
        if(null != additionEntry){
            questionAdditionDao.delEntry(additionEntry.getID());
        }
        ObjectId oid = questionAdditionDao.addEntry(entry);

        //图片检测
        List<String> alist = dto.getAnswerList();
        if(alist != null && alist.size()>0){
            for(String entry5 : alist){
                PictureRunNable.send(oid.toString(), userId.toString(), PictureType.answerImage.getType(), 1, entry5);
            }
        }
    }
    /**
     * 今日复习
     */
    public List<QuestionBookDTO> getReviewList(ObjectId userId){
        //获得当前时间
        long current=System.currentTimeMillis();
        //获得时间批次
        long zero = current/(1000*3600*24)*(1000*3600*24)- TimeZone.getDefault().getRawOffset();//今天零点零分零秒的毫秒数
        //明日复习
        //List<QuestionBookEntry> entries = questionBookDao.getReviewList(userId,zero);
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

    //获取今日闯关数量
    public int getReviewListCount(ObjectId userId){
        //获得当前时间
        long current=System.currentTimeMillis();
        //获得时间批次
        long zero = current/(1000*3600*24)*(1000*3600*24)- TimeZone.getDefault().getRawOffset();//今天零点零分零秒的毫秒数
        //明日复习
        //List<QuestionBookEntry> entries = questionBookDao.getReviewList(userId,zero);
        int count = questionBookDao.getReviewListCount(userId, zero+1);
        return count;
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

    public void addQuestionTags(String name,ObjectId userId){
        QuestionTagsDTO dto = new QuestionTagsDTO();
        dto.setUserId(userId.toString());
        dto.setName(name);
        questionTagsDao.addEntry(dto.buildAddEntry());
    }
    public static void main(String[] args){
        QuestionWebTestDao questionTagsDao = new QuestionWebTestDao();
        QuestionWebTestDTO dto = new QuestionWebTestDTO();
        dto.setUserId("5a0021f03d4df9241620d155");
        dto.setTitle("难点组卷");
        dto.setCount(4);
        List<QuestionWebSizeDTO> dtos = new ArrayList<QuestionWebSizeDTO>();
        QuestionWebSizeDTO mdt = new QuestionWebSizeDTO();
        mdt.setAnswerHeight(50);
        mdt.setQuestionHeight(90);
        mdt.setQuestionId("5a30957827fddd0edca7829a");
        dtos.add(mdt);
        dto.setSizeList(dtos);
        questionTagsDao.addEntry(dto.buildAddEntry());
    }




    //查询所有试卷
    public Map<String,Object> selectTestListEntry(ObjectId userId,int page,int pageSize){
        Map<String,Object> map = new HashMap<String, Object>();
        List<QuestionWebTestDTO> dtos = new ArrayList<QuestionWebTestDTO>();
        List<QuestionWebTestEntry> entries = questionWebTestDao.getQuestionList(userId, page, pageSize);
        int count = questionWebTestDao.getReviewListCount(userId);
        for(QuestionWebTestEntry entry : entries){
            dtos.add(new QuestionWebTestDTO(entry));
        }
        map.put("list",dtos);
        map.put("count",count);
        return map;
    }

    //添加试卷
    public void addTestEntry(ObjectId userId,List<String> ids){
        QuestionWebTestDTO dto = new QuestionWebTestDTO();
        dto.setUserId(userId.toString());
        dto.setCount(ids.size());
        Date date = new Date();
        SimpleDateFormat sdf =   new SimpleDateFormat("yyyy-MM-dd");
        String str = sdf.format(date);
        int count = questionWebTestDao.getNameCount(str);
        int number = count + 1;
        dto.setTitle(str+number+"组卷");
        List<QuestionWebSizeDTO> sizeDTOs = new ArrayList<QuestionWebSizeDTO>();
        for(String id: ids){
            QuestionWebSizeDTO dto1 = new QuestionWebSizeDTO();
            dto1.setQuestionId(id);
            dto1.setAnswerHeight(100);
            dto1.setAnswerHeight(100);
        }
        dto.setSizeList(sizeDTOs);
        //dto.setTitle();
        questionWebTestDao.addEntry(dto.buildAddEntry());
    }
    //修改
    public void updTestEntry(QuestionWebTestDTO dto){
        questionWebTestDao.updEntry(dto.updateEntry());
    }



    public void addword(String questionaireId, String name, int page, int pageSize, String tmids, HttpServletResponse response) throws JSONException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        XWPFDocument doc = new XWPFDocument();
        XWPFParagraph p1 = doc.createParagraph();
        XWPFRun r1 = p1.createRun();
        String[] strings = {"dd","ds"};
        int i = 1;
        r1.setText("题目");
        r1.addBreak();
        r1.setText("答案");
        r1.addBreak();
        for (String s1 : strings) {
            r1.setText(i + ":" + s1);
            r1.addBreak();
            i++;
        }
        try {
            doc.write(os);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] content = os.toByteArray();
        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            response.setContentType("application/force-download");
            response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("" + "答案.docx", "UTF-8"));
            response.setContentLength(content.length);
            outputStream.write(content);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                    outputStream = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
