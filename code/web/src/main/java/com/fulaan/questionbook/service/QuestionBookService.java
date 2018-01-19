package com.fulaan.questionbook.service;

import cn.jiguang.commom.utils.StringUtils;
import com.db.fcommunity.CommunityDao;
import com.db.fcommunity.GroupDao;
import com.db.fcommunity.MemberDao;
import com.db.fcommunity.NewVersionCommunityBindDao;
import com.db.newVersionGrade.NewVersionSubjectDao;
import com.db.questionbook.*;
import com.db.reportCard.VirtualUserDao;
import com.db.user.NewVersionBindRelationDao;
import com.fulaan.picturetext.runnable.PictureRunNable;
import com.fulaan.picturetext.service.CheckTextAndPicture;
import com.fulaan.questionbook.dto.*;
import com.fulaan.user.service.UserService;
import com.pojo.backstage.PictureType;
import com.pojo.fcommunity.CommunityEntry;
import com.pojo.newVersionGrade.NewVersionSubjectEntry;
import com.pojo.questionbook.*;
import com.pojo.reportCard.VirtualUserEntry;
import com.pojo.user.UserDetailInfoDTO;
import com.sys.constants.Constant;
import com.sys.props.Resources;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xwpf.usermodel.Document;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
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

    private QuestionReadDao questionReadDao = new QuestionReadDao();

    private MemberDao memberDao = new MemberDao();

    private GroupDao groupDao = new GroupDao();

    private NewVersionBindRelationDao newVersionBindRelationDao = new NewVersionBindRelationDao();

    private NewVersionCommunityBindDao newVersionCommunityBindDao = new NewVersionCommunityBindDao();

    private CommunityDao communityDao = new CommunityDao();

    private VirtualUserDao virtualUserDao = new VirtualUserDao();

    private NewVersionSubjectDao newVersionSubjectDao = new NewVersionSubjectDao();
    @Autowired
    private UserService userService;
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
        //增加记录
        questionReadDao.updateEntry1(new ObjectId(dto.getUserId()));
        return "添加成功";
    }
    public String addQuestionNewBookEntry(QuestionBookDTO dto) throws Exception{
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
        //文本检测
        Map<String,Object> flag = CheckTextAndPicture.checkText(dto.getDescription());
        String f = (String)flag.get("bl");
        if(f.equals("1")){
            return (String)flag.get("text");
            //return;
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
        //增加记录
        questionReadDao.updateEntry1(new ObjectId(dto.getUserId()));
       // questionReadDao.getEntryById();

        return oid.toString();
    }

    //增加记录


    /**
     * 删除已学会的错题
     */
    public void delAlreadyEntry(ObjectId id){
        questionBookDao.delEntry(id);
    }
    /**
     * 修改错题
     */
    public void updateEntry(QuestionBookDTO dto) throws Exception{
        //图片检测
        List<String> alist = dto.getImageList();
        if(alist != null && alist.size()>0){
            for(String entry5 : alist){
                PictureRunNable.send(dto.getId(), dto.getUserId(), PictureType.wrongImage.getType(), 1, entry5);
            }
        }
        //文本检测
        Map<String,Object> flag = CheckTextAndPicture.checkText(dto.getDescription());
        String f = (String)flag.get("bl");
        if(f.equals("1")){
            //return (String)flag.get("text");
            return;
        }
        questionBookDao.updateEntry(dto.buildAddEntry());
    }
    /**
     * 修改解析
     */
    public void updateAnswerEntry(ObjectId userId,QuestionAdditionDTO dto) throws Exception{
        //图片检测
        List<String> alist = dto.getAnswerList();
        if(alist != null && alist.size()>0){
            for(String entry5 : alist){
                PictureRunNable.send(dto.getId(), userId.toString(), PictureType.answerImage.getType(), 1, entry5);
            }
        }
        //文本检测
        Map<String,Object> flag = CheckTextAndPicture.checkText(dto.getContent());
        String f = (String)flag.get("bl");
        if(f.equals("1")){
            //return (String)flag.get("text");
            return;
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

    /**
     * 多条件组合查询列表
     */
    public  Map<String,Object> getAllQuestionList(String gradeId,String subjectId,String questionTypeId,String testId,int type,int page,int pageSize,String keyword,ObjectId userId,ObjectId parentId){
        List<QuestionBookEntry> entries = new ArrayList<QuestionBookEntry>();
        if(type==2){//是老师
            NewVersionSubjectEntry newVersionSubjectEntry = newVersionSubjectDao.getEntryByUserId(parentId);
            if(subjectId == null || subjectId.equals("")){
                if(newVersionSubjectEntry!=null && newVersionSubjectEntry.getSubjectList().size()>0){
                    entries = questionBookDao.getQuestionList(gradeId, subjectId, questionTypeId, testId, 1, page, pageSize, keyword,userId);
                }else{

                   entries = questionBookDao.getAllQuestionList(gradeId, newVersionSubjectEntry.getSubjectList(), questionTypeId, testId, 1, page, pageSize, keyword,userId);

                }
               // entries.addAll(questionBookDao.getQuestionList(gradeId, subjectId, questionTypeId, testId, 1, page, pageSize, keyword,userId));
            }else{
                entries = questionBookDao.getQuestionList(gradeId, subjectId, questionTypeId, testId, 1, page, pageSize, keyword,userId);
            }

        }else{//是家长
            entries = questionBookDao.getQuestionList(gradeId, subjectId, questionTypeId, testId, 1, page, pageSize, keyword,userId);
        }
        //entries = questionBookDao.getQuestionList(gradeId, subjectId, questionTypeId, testId, 1, page, pageSize, keyword,userId);
        List<QuestionBookDTO> dtos = new ArrayList<QuestionBookDTO>();
        int count = questionBookDao.getQuestionListCount(gradeId, subjectId, questionTypeId, testId, 1,keyword,userId);
        Map<String,Object> map = new HashMap<String, Object>();
        if(entries.size()>0){
            for(QuestionBookEntry entry : entries){
                dtos.add(new QuestionBookDTO(entry));
            }
        }
        //消除记录
        questionReadDao.removeItemById(parentId,userId);

        map.put("list",dtos);
        map.put("count",count);
        return map;
    }
    public void addAnswerEntry(ObjectId userId,QuestionAdditionDTO dto)throws Exception{
        QuestionAdditionEntry entry = dto.buildAddEntry();
        QuestionAdditionEntry additionEntry = questionAdditionDao.getEntry(entry.getParentId(),entry.getAnswerType(),entry.getLevel());
        if(null != additionEntry){
            questionAdditionDao.delEntry(additionEntry.getID());
        }
        //文本检测
        Map<String,Object> flag = CheckTextAndPicture.checkText(dto.getContent());
        String f = (String)flag.get("bl");
        if(f.equals("1")){
            //return (String)flag.get("text");
            return;
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
            }else if(level==3){
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

    public void addQuestionTags(String name,ObjectId userId) throws Exception{
        QuestionTagsDTO dto = new QuestionTagsDTO();
        dto.setUserId(userId.toString());
        dto.setName(name);
        //文本检测
        Map<String,Object> flag = CheckTextAndPicture.checkText(name);
        String f = (String)flag.get("bl");
        if(f.equals("1")){
            //return (String)flag.get("text");
            return;
        }
        questionTagsDao.addEntry(dto.buildAddEntry());
    }

    public void delQuestionTags(ObjectId userId,ObjectId id){
        QuestionTagsEntry entry = questionTagsDao.getEntryById(id);
        if(entry!= null  && entry.getUserId().equals(userId)){
            questionTagsDao.delEntry(id);
        }
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
    public String addTestEntry(ObjectId userId,List<String> ids){
        QuestionWebTestDTO dto = new QuestionWebTestDTO();
        dto.setUserId(userId.toString());
        dto.setCount(ids.size());
        Date date = new Date();
        SimpleDateFormat sdf =   new SimpleDateFormat("yyyy-MM-dd");
        String str = sdf.format(date);
        int count = questionWebTestDao.getNameCount(str);
        int number = count + 1;
        dto.setTitle(str+"组卷"+number);
        List<QuestionWebSizeDTO> sizeDTOs = new ArrayList<QuestionWebSizeDTO>();
        for(String id: ids){
            QuestionWebSizeDTO dto1 = new QuestionWebSizeDTO();
            dto1.setQuestionId(id);
            dto1.setAnswerHeight(100);
            dto1.setAnswerHeight(100);
            sizeDTOs.add(dto1);
        }
        dto.setSizeList(sizeDTOs);
        //dto.setTitle();
        ObjectId oid = questionWebTestDao.addEntry(dto.buildAddEntry());
        return oid.toString();
    }
    //修改
    public void updTestEntry(QuestionWebTestDTO dto){
        questionWebTestDao.updEntry(dto.updateEntry());
    }
    //修改名称
    public void updTestNameById(ObjectId id,String name){
        QuestionWebTestEntry entry = questionWebTestDao.getEntryById(id);
        if(entry!=null){
            questionWebTestDao.updTestNameById(id,name);
        }
    }

    public void delTestEntry(ObjectId id){
        questionWebTestDao.delEntry(id);
    }

    public Map<String,Object> selectTestEntry(ObjectId id){
        Map<String,Object> map = new HashMap<String, Object>();
        QuestionWebTestEntry entry = questionWebTestDao.getEntryById(id);
        List<QuestionWebSizeDTO>  dtos = new ArrayList<QuestionWebSizeDTO>();
        if(null != entry){
            map.put("dto",new QuestionWebTestDTO(entry));
            List<QuestionWebSizeEntry> list =  entry.getSizeList();
            for(QuestionWebSizeEntry entry1 : list){
                QuestionBookEntry entry2 = questionBookDao.getEntryById(entry1.getQuestionId());
                QuestionWebSizeDTO dto =  new QuestionWebSizeDTO(entry1);
                dto.setQuestionBookDTO(new QuestionBookDTO(entry2));
                dtos.add(dto);
            }
        }else{
            map.put("dto",new QuestionWebTestDTO());

        }
        map.put("list",dtos);
        return map;
    }



    public void addword(String questionaireId ,HttpServletResponse response) throws Exception {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        QuestionWebTestEntry entry =  questionWebTestDao.getEntryById(new ObjectId(questionaireId));
        List<QuestionWebSizeEntry> questionWebSizeEntries = entry.getSizeList();
        CustomXWPFDocument doc = new CustomXWPFDocument();
        int i = 1;
        final String anOSName = System.getProperty("os.name");
        String bathPath = Resources.getProperty("upload.file");
        if (anOSName.toLowerCase().startsWith("windows")) {
            bathPath= Resources.getProperty("uploads.file");
        }
        for (QuestionWebSizeEntry s1 : questionWebSizeEntries) {
            XWPFParagraph p1 = doc.createParagraph();
            XWPFRun r1 = p1.createRun();
            QuestionBookEntry entry1 = questionBookDao.getEntryById(s1.getQuestionId());
            r1.setText("("+i+")"+entry1.getDescription());
            int k = s1.getQuestionHeight();
            for(int j = 0;j<k;j++){
                r1.addBreak();
            }
            try{
                List<String> stringList = entry1.getImageList();
                if(stringList != null && stringList.size()>0){
                    for(String str : stringList){
                        String imageFileName = new ObjectId().toString() + Constant.POINT + "png";
                        String imageFilePath = bathPath +"/"+ imageFileName;
                        if(anOSName.toLowerCase().startsWith("windows")) {
                            imageFilePath = bathPath + "\\" + imageFileName;
                        }
                        InputStream pic = downLoadFromUrl2(str, imageFileName, imageFilePath);
                        //File imageFile = new File(imageFilePath);
                       // InputStream pic = new FileInputStream(imageFile);
                        byte [] picbytes = IOUtils.toByteArray(pic);
                        String str5 = doc.addPictureData(picbytes, Document.PICTURE_TYPE_JPEG);
                        doc.createPicture(str5,doc.getNextPicNameNumber(Document.PICTURE_TYPE_JPEG), 100, 100);
                       // imageFile.delete();
                        pic.close();
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            XWPFParagraph p2 = doc.createParagraph();
            XWPFRun r2 = p2.createRun();
            int l = s1.getAnswerHeight();
            for(int j = 0;j<l;j++){
                r2.addBreak();
            }

            i++;
        }
        try {
            doc.write(os);
        } catch (Exception e) {
            e.printStackTrace();
        }
        byte[] content = os.toByteArray();
        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            response.setContentType("application/force-download");
            response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(entry.getTitle() + ".docx", "UTF-8"));
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

    public void addNewword(String questionaireId ,HttpServletResponse response) throws Exception {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        QuestionWebTestEntry entry =  questionWebTestDao.getEntryById(new ObjectId(questionaireId));
        List<QuestionWebSizeEntry> questionWebSizeEntries = entry.getSizeList();
        CustomXWPFDocument doc = new CustomXWPFDocument();
        XWPFParagraph p1 = doc.createParagraph();
        XWPFRun r1 = p1.createRun();
        int i = 1;
        final String anOSName = System.getProperty("os.name");
        String bathPath = Resources.getProperty("upload.file");
        if (anOSName.toLowerCase().startsWith("windows")) {
            bathPath= Resources.getProperty("uploads.file");
        }
        for (QuestionWebSizeEntry s1 : questionWebSizeEntries) {
            QuestionBookEntry entry1 = questionBookDao.getEntryById(s1.getQuestionId());
            r1.setText("("+i+")"+entry1.getDescription());
            int k = s1.getQuestionHeight();
            for(int j = 0;j<k;j++){
                r1.addBreak();
            }
            try{
                List<String> stringList = entry1.getImageList();
                if(stringList != null && stringList.size()>0){
                    for(String str : stringList){
                        String imageFileName = new ObjectId().toString() + Constant.POINT + "png";
                        String imageFilePath = bathPath +"/"+ imageFileName;
                        if(anOSName.toLowerCase().startsWith("windows")) {
                            imageFilePath = bathPath + "\\" + imageFileName;
                        }
                        InputStream pic = downLoadFromUrl2(str, imageFileName, imageFilePath);
                        //File imageFile = new File(imageFilePath);
                        // InputStream pic = new FileInputStream(imageFile);
                        byte [] picbytes = IOUtils.toByteArray(pic);
                        String str5 = doc.addPictureData(picbytes, Document.PICTURE_TYPE_JPEG);
                        doc.createPicture(str5,doc.getNextPicNameNumber(Document.PICTURE_TYPE_JPEG), 100, 100);
                        // imageFile.delete();
                        pic.close();
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            int l = s1.getAnswerHeight();
            for(int j = 0;j<l;j++){
                r1.addBreak();
            }

            i++;
        }
        try {
            doc.write(os);
        } catch (Exception e) {
            e.printStackTrace();
        }
        byte[] content = os.toByteArray();
        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            response.setContentType("application/force-download");
            response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(entry.getTitle() + ".docx", "UTF-8"));
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
    public static void  downLoadFromUrl(String urlStr,String fileName,String savePath) throws IOException{
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        //设置超时间为3秒
        conn.setConnectTimeout(3*1000);
        //防止屏蔽程序抓取而返回403错误
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

        //得到输入流
        InputStream inputStream = conn.getInputStream();
        //获取自己数组
        byte[] getData = readInputStream(inputStream);

        //文件保存位置
        File saveDir = new File(savePath);
        if(!saveDir.exists()){
            saveDir.mkdir();
        }
        File file = new File(saveDir+File.separator+fileName);
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(getData);
        if(fos!=null){
            fos.close();
        }
        if(inputStream!=null){
            inputStream.close();
        }


        System.out.println("info:"+url+" download success");

    }

    public static InputStream  downLoadFromUrl2(String urlStr,String fileName,String savePath) throws IOException{
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        //设置超时间为3秒
        conn.setConnectTimeout(3*1000);
        //防止屏蔽程序抓取而返回403错误
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

        //得到输入流
        InputStream inputStream = conn.getInputStream();
        return inputStream;
    }


    /**
     * 从输入流中获取字节数组
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static  byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }

   /* public static void main(String[] args) {
        try{
            downLoadFromUrl("http://101.95.48.97:8005/res/upload/interface/apptutorials/manualstypeico/6f83ce8f-0da5-49b3-bac8-fd5fc67d2725.png",
                    "百度.jpg","d:/resource/images/diaodiao/country/");
        }catch (Exception e) {
            // TODO: handle exception
        }
    }*/

    /************************* 家校美（大人端）*****************************/
    public Map<String,Object> getIndexList(ObjectId userId){
       Map<String,Object> map = new HashMap<String, Object>();
        List<TypeResultDTO> dtos = new ArrayList<TypeResultDTO>();
        //查询所有该用户的绑定关系
        List<ObjectId> childIds = newVersionBindRelationDao.getIdsByMainUserId(userId);
        //查询具有管理员权限的社区
        List<ObjectId> oids = getMyRoleList(userId);
        if(oids.size()>0){
            List<CommunityEntry> communityEntries = communityDao.findByObjectIds(oids);
            for(CommunityEntry entry : communityEntries){
                TypeResultDTO dto = new TypeResultDTO();
                dto.setId(entry.getID().toString());
                dto.setName(entry.getCommunityName());
                dto.setType(2);
                dtos.add(dto);
            }
        }

        if(childIds.size()>0){
            /*List<UserDetailInfoDTO> infos = userService.findUserInfoByIds(childIds);
            for(UserDetailInfoDTO entry : infos){
                TypeResultDTO dto = new TypeResultDTO();
                dto.setId(entry.getId());
                dto.setName(entry.getName());
                dto.setType(1);
            }*/
            TypeResultDTO dto = new TypeResultDTO();
            dto.setId("");
            dto.setName("我的小孩");
            dto.setType(1);
            dtos.add(dto);
        }
        map.put("topList",dtos);
        List<QuestionReadDTO> dtoList = new ArrayList<QuestionReadDTO>();
        if(oids.size() >0){
            map.put("isShow",1);
            //List<ObjectId> objectIdList = newVersionCommunityBindDao.getStudentListByCommunityId(oids.get(0));
            dtoList = this.getQuestionReadDTO(oids.get(0), userId);
        }else if(childIds.size() >0 ){
            map.put("isShow",2);
            dtoList = this.getQuestionReadSonDTO(childIds,userId);
        }else{
            map.put("isShow",3);
        }
        map.put("list",dtoList);
        return map;
    }

    public Map<String,Object> getIndexPageList(ObjectId userId,String contactId,int type){
        Map<String,Object> map = new HashMap<String, Object>();
        List<QuestionReadDTO> dtoList = new ArrayList<QuestionReadDTO>();
        List<ObjectId> childIds  = new ArrayList<ObjectId>();
        if(type==1){
            //childIds.add(contactId);
            //查询所有该用户的绑定关系
            List<ObjectId> childIds2 = newVersionBindRelationDao.getIdsByMainUserId(userId);
            childIds.addAll(childIds2);
            if(childIds.size()>0){
                dtoList = this.getQuestionReadSonDTO(childIds,userId);
            }
        }else if(type==2){
            dtoList = this.getQuestionReadDTO(new ObjectId(contactId), userId);
        }else{
        }
        map.put("list",dtoList);
        return map;
    }
    //群组
    public List<QuestionReadDTO> getQuestionReadDTO(ObjectId communityId,ObjectId parentId){
        List<QuestionReadDTO> dtoList = new ArrayList<QuestionReadDTO>();
        List<VirtualUserEntry> virtualUserEntries = virtualUserDao.getAllVirtualUsers(communityId);
        List<ObjectId> userIds = new ArrayList<ObjectId>();
        Map<ObjectId,VirtualUserEntry> map3 = new HashMap<ObjectId, VirtualUserEntry>();
        if(virtualUserEntries.size()>0){
            for(VirtualUserEntry virtualUserEntry : virtualUserEntries){
                userIds.add(virtualUserEntry.getUserId());
                map3.put(virtualUserEntry.getUserId(),virtualUserEntry);
            }
        }
        List<QuestionReadEntry> entries = questionReadDao.getReviewList(userIds,parentId,2);
        Map<ObjectId,QuestionReadEntry> map4 = new HashMap<ObjectId, QuestionReadEntry>();
        List<ObjectId> childs = new ArrayList<ObjectId>();
        if(entries.size()>0){
            for(QuestionReadEntry entry : entries){
                childs.add(entry.getUserId());
                map4.put(entry.getUserId(),entry);
            }
        }
       // List<ObjectId> objectIdList = new ArrayList<ObjectId>();
       // objectIdList.addAll(userIds);
        //objectIdList.removeAll(childs);
        for(ObjectId oid : userIds){
            if(childs.contains(oid)){
                QuestionReadEntry questionReadEntry = map4.get(oid);
                VirtualUserEntry dto9 = map3.get(oid);
                if(questionReadEntry!=null && dto9 != null){
                    QuestionReadDTO questionReadDTO = new QuestionReadDTO(questionReadEntry);
                    questionReadDTO.setUserName(dto9.getUserName());
                    questionReadDTO.setImageUrl("");
                    questionReadDTO.setStudyNum(dto9.getUserNumber());
                    //questionReadDTO.setDto(null);
                    dtoList.add(questionReadDTO);
                }
            }else{
                VirtualUserEntry dto9 = map3.get(oid);
                QuestionReadEntry questionReadEntry = new QuestionReadEntry();
                if(dto9 != null){
                    int count = questionBookDao.getAllQuestionListCount(oid);
                    questionReadEntry.setID(null);
                    questionReadEntry.setType(2);
                    questionReadEntry.setUnReadNum(count);
                    questionReadEntry.setUserId(oid);
                    questionReadEntry.setParentId(parentId);
                    questionReadEntry.setIsRemove(0);
                    questionReadDao.addEntry(questionReadEntry);
                    QuestionReadDTO questionReadDTO = new QuestionReadDTO(questionReadEntry);
                    questionReadDTO.setUserName(dto9.getUserName());
                    questionReadDTO.setImageUrl("");
                   // questionReadDTO.setDto(null);
                    questionReadDTO.setStudyNum(dto9.getUserNumber());
                    dtoList.add(questionReadDTO);
                }

            }

        }
        return dtoList;
    }

    //孩子
    public List<QuestionReadDTO> getQuestionReadSonDTO(List<ObjectId> userIds,ObjectId parentId){
        List<QuestionReadDTO> dtoList = new ArrayList<QuestionReadDTO>();
        List<QuestionReadEntry> entries = questionReadDao.getReviewList(userIds,parentId,1);
        Map<ObjectId,QuestionReadEntry> map4 = new HashMap<ObjectId, QuestionReadEntry>();
        List<ObjectId> childs = new ArrayList<ObjectId>();
        if(entries.size()>0){
            for(QuestionReadEntry entry : entries){
                childs.add(entry.getUserId());
                map4.put(entry.getUserId(),entry);
            }
        }
        List<String> sids = new ArrayList<String>();
        if(userIds.size()>0){
            for(ObjectId id : userIds){
                sids.add(id.toString());
            }
        }
        List<UserDetailInfoDTO> udtos2 = userService.findUserInfoByUserIds(sids);
        Map<String,UserDetailInfoDTO> map3 = new HashMap<String, UserDetailInfoDTO>();
        if(udtos2 != null && udtos2.size()>0) {
            for (UserDetailInfoDTO dto4 : udtos2) {
                map3.put(dto4.getId(), dto4);
            }
        }
        for(ObjectId oid : userIds){
            if(childs.contains(oid)){
                QuestionReadEntry questionReadEntry = map4.get(oid);
                UserDetailInfoDTO dto9 = map3.get(oid.toString());
                if(questionReadEntry!=null && dto9 != null){
                    QuestionReadDTO questionReadDTO = new QuestionReadDTO(questionReadEntry);
                    String name = StringUtils.isNotEmpty(dto9.getNickName())?dto9.getNickName():dto9.getUserName();
                    questionReadDTO.setUserName(name);
                    questionReadDTO.setImageUrl(dto9.getImgUrl());
                    List<QuestionBookEntry> entries1 = questionBookDao.getAllQuestionList(oid);
                    questionReadDTO.setAllNum(entries1.size());
                    //questionReadDTO.setDto(new QuestionBookDTO(entries1.get(0)));
                    questionReadDTO.setStudyNum("");
                    dtoList.add(questionReadDTO);
                }
            }else{
                UserDetailInfoDTO dto9 = map3.get(oid.toString());
                if(dto9 != null){
                    int count = questionBookDao.getAllQuestionListCount(oid);
                    QuestionReadEntry readEntry = new QuestionReadEntry();
                    readEntry.setID(null);
                    readEntry.setType(1);
                    readEntry.setUnReadNum(count);
                    readEntry.setUserId(oid);
                    readEntry.setParentId(parentId);
                    readEntry.setIsRemove(0);
                    questionReadDao.addEntry(readEntry);
                    QuestionReadDTO questionReadDTO = new QuestionReadDTO(readEntry);
                    String name = StringUtils.isNotEmpty(dto9.getNickName())?dto9.getNickName():dto9.getUserName();
                    questionReadDTO.setUserName(name);
                    questionReadDTO.setImageUrl(dto9.getImgUrl());
                    questionReadDTO.setType(1);
                    questionReadDTO.setAllNum(count);
                    questionReadDTO.setUnReadNum(count);
                    questionReadDTO.setStudyNum("");
                    //QuestionBookEntry bookEntry = new QuestionBookEntry();
                    //List<QuestionBookEntry> entries1 = questionBookDao.getAllQuestionList(oid);
                   /* if(entries.size()>0){
                         questionReadDTO.setDto(new QuestionBookDTO(entries1.get(0)));
                    }else{
                         questionReadDTO.setDto(null);
                    }*/

                    dtoList.add(questionReadDTO);
                }

            }
        }




        return dtoList;
    }

    public List<ObjectId> getMyRoleList(ObjectId userId){
        List<ObjectId> olsit = memberDao.getManagerGroupIdsByUserId(userId);
        List<ObjectId> mlist =   groupDao.getGroupIdsList(olsit);
        return mlist;
    }
}
