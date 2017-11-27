package com.fulaan.backstage.service;

import com.db.backstage.JxmAppVersionDao;
import com.db.backstage.TeacherApproveDao;
import com.db.backstage.UnlawfulPictureTextDao;
import com.db.controlphone.ControlPhoneDao;
import com.db.controlphone.ControlSchoolTimeDao;
import com.db.controlphone.ControlSetBackDao;
import com.db.controlphone.ControlSetTimeDao;
import com.db.fcommunity.CommunityDao;
import com.db.fcommunity.CommunityDetailDao;
import com.db.operation.AppCommentDao;
import com.db.operation.AppNoticeDao;
import com.db.operation.AppOperationDao;
import com.db.questionbook.QuestionAdditionDao;
import com.db.questionbook.QuestionBookDao;
import com.db.user.UserDao;
import com.fulaan.backstage.dto.JxmAppVersionDTO;
import com.fulaan.backstage.dto.TeacherApproveDTO;
import com.fulaan.backstage.dto.UnlawfulPictureTextDTO;
import com.fulaan.controlphone.dto.ControlPhoneDTO;
import com.fulaan.controlphone.dto.ControlSchoolTimeDTO;
import com.pojo.appnotice.AppNoticeEntry;
import com.pojo.backstage.JxmAppVersionEntry;
import com.pojo.backstage.PictureType;
import com.pojo.backstage.TeacherApproveEntry;
import com.pojo.backstage.UnlawfulPictureTextEntry;
import com.pojo.controlphone.ControlPhoneEntry;
import com.pojo.controlphone.ControlSchoolTimeEntry;
import com.pojo.controlphone.ControlSetBackEntry;
import com.pojo.controlphone.ControlSetTimeEntry;
import com.pojo.fcommunity.AttachmentEntry;
import com.pojo.fcommunity.CommunityDetailEntry;
import com.pojo.operation.AppCommentEntry;
import com.pojo.operation.AppOperationEntry;
import com.pojo.questionbook.QuestionAdditionEntry;
import com.pojo.questionbook.QuestionBookEntry;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by James on 2017/11/18.
 */
@Service
public class BackStageService {
    private UnlawfulPictureTextDao unlawfulPictureTextDao = new UnlawfulPictureTextDao();

    private ControlSetBackDao controlSetBackDao = new ControlSetBackDao();

    private ControlSetTimeDao controlSetTimeDao = new ControlSetTimeDao();

    private ControlPhoneDao controlPhoneDao = new ControlPhoneDao();

    private ControlSchoolTimeDao controlSchoolTimeDao = new ControlSchoolTimeDao();

    private UserDao userDao = new UserDao();

    private CommunityDao communityDao =new CommunityDao();

    private AppNoticeDao appNoticeDao = new AppNoticeDao();

    private AppCommentDao appCommentDao = new AppCommentDao();

    private CommunityDetailDao communityDetailDao = new CommunityDetailDao();

    private AppOperationDao appOperationDao = new AppOperationDao();

    private QuestionBookDao questionBookDao = new QuestionBookDao();

    private QuestionAdditionDao questionAdditionDao = new QuestionAdditionDao();
    
    private TeacherApproveDao teacherApproveDao = new TeacherApproveDao();

    private JxmAppVersionDao jxmAppVersionDao = new JxmAppVersionDao();

    private static String imageUrl = "";


    public static void main(String[] args){
       /* ControlSetBackDao controlSetBackDao = new ControlSetBackDao();
        ControlSetBackEntry entry1 = new ControlSetBackEntry();
        entry1.setType(1);
        entry1.setBackTime(15);
        entry1.setAppTime(15);
        controlSetBackDao.addEntry(entry1);*/
        TeacherApproveDTO dto = new TeacherApproveDTO();
        dto.setUserId("5a0021f03d4df9241620d155");
        dto.setName("小佳和晓梅的爸爸");
        dto.setType(1);
        dto.setApplyTime("2011-11-23 17:04:00");
        TeacherApproveDao teacherApproveDao1 = new TeacherApproveDao();
        teacherApproveDao1.addEntry(dto.buildAddEntry());
    }


    public void addBackTimeEntry(ObjectId userId,int time){
        ControlSetBackEntry entry = controlSetBackDao.getEntry();
        if(null == entry){
            ControlSetBackEntry entry1 = new ControlSetBackEntry();
            entry1.setType(1);
            entry1.setBackTime(time);
            entry1.setAppTime(24 * 60);
            controlSetBackDao.addEntry(entry1);
        }else{
            entry.setBackTime(time);
            controlSetBackDao.updEntry(entry);
        }

    }
    public void addAppBackTimeEntry(ObjectId userId,int time){
        ControlSetBackEntry entry = controlSetBackDao.getEntry();
        if(null == entry){
            ControlSetBackEntry entry1 = new ControlSetBackEntry();
            entry1.setType(1);
            entry1.setAppTime(time);
            entry1.setBackTime(24*60);
            controlSetBackDao.addEntry(entry1);
        }else{
            entry.setAppTime(time);
            controlSetBackDao.updEntry(entry);
        }

    }
    public void addSetTimeListEntry(ObjectId userId,int time){
        long time2 = time * 60 * 1000;
        long hours2 = (time2 % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long minutes2 = (time2 % (1000 * 60 * 60)) / (1000 * 60);
        String timeStr = "";
        if(hours2 != 0 ){
            timeStr = timeStr + hours2+"小时";
        }
        if(minutes2 != 0){
            timeStr = timeStr + minutes2+"分钟";
        }
        ControlSetTimeEntry controlSetTimeEntry = new ControlSetTimeEntry();
        controlSetTimeEntry.setName(timeStr);
        controlSetTimeEntry.setTime(time);
        controlSetTimeDao.addEntry(controlSetTimeEntry);

    }
    public void addPhoneEntry(String name, String phone){
        ControlPhoneEntry entry = controlPhoneDao.getEntry(phone);
        if(null == entry){
            ControlPhoneDTO dto = new ControlPhoneDTO();
            dto.setName(name);
            dto.setPhone(phone);
            dto.setType(1);
            controlPhoneDao.addEntry(dto.buildAddEntry());
        }else{
            entry.setName(name);
            controlPhoneDao.updEntry(entry);
        }

    }

    public void addSchoolTime(String startTime,String endTime,int week){
        ControlSchoolTimeEntry entry = controlSchoolTimeDao.getEntry(week);
        if(null==entry){
            ControlSchoolTimeDTO dto = new ControlSchoolTimeDTO();
            dto.setStartTime(startTime);
            dto.setEndTime(endTime);
            dto.setWeek(week);
            dto.setType(1);
            controlSchoolTimeDao.addEntry(dto.buildAddEntry());
        }else{
            entry.setStartTime(startTime);
            entry.setStartTime(endTime);
            controlSchoolTimeDao.updEntry(entry);
        }
    }
    public void addOtherSchoolTime(String startTime,String endTime,String dateTime){
        ControlSchoolTimeEntry entry = controlSchoolTimeDao.getOtherEntry(dateTime);
        if(null==entry){
            ControlSchoolTimeDTO dto = new ControlSchoolTimeDTO();
            dto.setStartTime(startTime);
            dto.setEndTime(endTime);
            dto.setDataTime(dateTime);
            dto.setType(2);
            controlSchoolTimeDao.addEntry(dto.buildAddEntry());
        }else{
            entry.setStartTime(startTime);
            entry.setStartTime(endTime);
            controlSchoolTimeDao.updEntry(entry);
        }
    }
    //教师认证
    public Map<String,Object> selectTeacherList(ObjectId userId,int type,String searchId,int page,int pageSize){
        Map<String,Object> map = new HashMap<String, Object>();
        List<TeacherApproveEntry> entries = teacherApproveDao.selectContentList(searchId, type, page, pageSize);
        int count = teacherApproveDao.getNumber(searchId, type);
        List<TeacherApproveDTO> dtos = new ArrayList<TeacherApproveDTO>();
        if(entries.size()>0){
            for(TeacherApproveEntry entry : entries){

                dtos.add(new TeacherApproveDTO(entry));
            }
        }

        map.put("list",dtos);
        map.put("count",count);
        return map;
    }
    public void addTeacherList(ObjectId id,int type){
        teacherApproveDao.updateEntry(id,type);
    }


    public List<JxmAppVersionDTO> getAllAppVersion(){
        List<JxmAppVersionDTO> dtos = new ArrayList<JxmAppVersionDTO>();
        List<JxmAppVersionEntry> entries = jxmAppVersionDao.getIsNewObjectId();
        for(JxmAppVersionEntry entry : entries){
            dtos.add(new JxmAppVersionDTO(entry));
        }

        return dtos;
    }
    /*public static void main(String[] args){

    }*/



    //添加图片显示认证
    public void addYellowPicture(ObjectId userId,String imageUrl,int ename,ObjectId contactId){
        UnlawfulPictureTextDTO dto = new UnlawfulPictureTextDTO();
        dto.setType(1);
        dto.setContent(imageUrl);
        dto.setUserId(userId.toString());
        dto.setFunction(ename);
        dto.setIsCheck(0);
        dto.setContactId(contactId.toString());
        unlawfulPictureTextDao.addEntry(dto.buildAddEntry());
    }

    //列表
    public Map<String,Object> selectContentList(int isCheck,String id,int page,int pageSize){
        Map<String,Object> map = new HashMap<String, Object>();
        List<UnlawfulPictureTextEntry> entries = unlawfulPictureTextDao.selectContentList(isCheck,id,page,pageSize);
        int count = unlawfulPictureTextDao.getNumber(isCheck,id);
        List<UnlawfulPictureTextDTO> dtos = new ArrayList<UnlawfulPictureTextDTO>();
        for(UnlawfulPictureTextEntry entry : entries){
            dtos.add(new UnlawfulPictureTextDTO(entry));
        }
        map.put("list",dtos);
        map.put("count",count);
        return map;
    }
    //通过
    public void passContentEntry(ObjectId id){
        unlawfulPictureTextDao.passContentEntry(id);
    }


    //删除
    public void deleteContentEntry(ObjectId id){
        unlawfulPictureTextDao.deleteContentEntry(id);
        UnlawfulPictureTextEntry entry = unlawfulPictureTextDao.getEntryById(id);
        if(entry != null) {
            ObjectId cid = entry.getContactId();
            String url = entry.getContent();
            if(entry.getType()== PictureType.userUrl.getType()){//用户头像
                //替换用户头像
                userDao.updateAvater(cid,imageUrl);
            }else if(entry.getType()== PictureType.communityLogo.getType()){//社区logo
                //替换社区logo
                communityDao.updateCommunityLogo(cid,imageUrl);
            }else if(entry.getType()== PictureType.noticeImage.getType()){//通知
                AppNoticeEntry entry1 = appNoticeDao.getAppNoticeEntry(cid);
                List<AttachmentEntry> alist = entry1.getImageList();
                if(alist != null && alist.size()>0){
                    for(AttachmentEntry entry2 : alist){
                        if(entry2.getUrl()!=null && entry2.getUrl().contains(url)){
                            entry2.setUrl(imageUrl);
                        }
                    }
                }
                appNoticeDao.updEntry(entry1);
            }else if(entry.getType()== PictureType.operationImage.getType()){//作业
                AppCommentEntry entry1 = appCommentDao.getEntry(cid);
                List<AttachmentEntry> alist = entry1.getImageList();
                if(alist != null && alist.size()>0){
                    for(AttachmentEntry entry2 : alist){
                        if(entry2.getUrl()!=null && entry2.getUrl().contains(url)){
                            entry2.setUrl(imageUrl);
                        }
                    }
                }
                appCommentDao.updEntry(entry1);
            }else if(entry.getType()== PictureType.activeImage.getType()){//活动报名
                CommunityDetailEntry entry1 = communityDetailDao.findByObjectId(cid);
                List<AttachmentEntry> alist = entry1.getImageList();
                if(alist != null && alist.size()>0){
                    for(AttachmentEntry entry2 : alist){
                        if(entry2.getUrl()!=null && entry2.getUrl().contains(url)){
                            entry2.setUrl(imageUrl);
                        }
                    }
                }
                communityDetailDao.updEntry(entry1);
            }else if(entry.getType()== PictureType.studyImage.getType()){//学习用品
                CommunityDetailEntry entry1 = communityDetailDao.findByObjectId(cid);
                List<AttachmentEntry> alist = entry1.getImageList();
                if(alist != null && alist.size()>0){
                    for(AttachmentEntry entry2 : alist){
                        if(entry2.getUrl()!=null && entry2.getUrl().contains(url)){
                            entry2.setUrl(imageUrl);
                        }
                    }
                }
                communityDetailDao.updEntry(entry1);

            }else if(entry.getType()== PictureType.happyImage.getType()){//兴趣小组
                CommunityDetailEntry entry1 = communityDetailDao.findByObjectId(cid);
                List<AttachmentEntry> alist = entry1.getImageList();
                if(alist != null && alist.size()>0){
                    for(AttachmentEntry entry2 : alist){
                        if(entry2.getUrl()!=null && entry2.getUrl().contains(url)){
                            entry2.setUrl(imageUrl);
                        }
                    }
                }
                communityDetailDao.updEntry(entry1);
            }else if(entry.getType()== PictureType.commentImage.getType()){//评论
                AppOperationEntry entry1 = appOperationDao.getEntry(cid);
                entry1.setFileUrl(imageUrl);
                appOperationDao.updEntry(entry1);
            }else if(entry.getType()== PictureType.wrongImage.getType()){//错题本
                QuestionBookEntry entry1 = questionBookDao.getEntryById(cid);
                List<String> slist = entry1.getImageList();
                List<String> nList = new ArrayList<String>();
                if(slist != null && slist.size()>0){
                    for(String str : slist){
                        if(str!=null && str.contains(url)){
                            nList.add(imageUrl);
                        }else{
                            nList.add(str);
                        }
                    }
                }
                entry1.setImageList(nList);
                questionBookDao.updateEntry(entry1);

            }else if(entry.getType()== PictureType.answerImage.getType()){//错题解析
                QuestionAdditionEntry entry1 = questionAdditionDao.getEntryByObjectId(cid);
                List<String> slist = entry1.getAnswerList();
                List<String> nList = new ArrayList<String>();
                if(slist != null && slist.size()>0){
                    for(String str : slist){
                        if(str!=null && str.contains(url)){
                            nList.add(imageUrl);
                        }else{
                            nList.add(str);
                        }
                    }
                }
                entry1.setAnswerList(nList);
                questionAdditionDao.updateEntry(entry1);
            }
        }

    }


}
