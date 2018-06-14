package com.fulaan.indexpage.service;

import cn.jiguang.commom.utils.StringUtils;
import com.db.backstage.SystemMessageDao;
import com.db.backstage.TeacherApproveDao;
import com.db.fcommunity.CommunityDetailDao;
import com.db.fcommunity.MemberDao;
import com.db.indexPage.IndexPageDao;
import com.db.operation.AppCommentDao;
import com.db.operation.AppNoticeDao;
import com.fulaan.backstage.dto.SystemMessageDTO;
import com.fulaan.dto.VideoDTO;
import com.fulaan.indexpage.dto.IndexPageDTO;
import com.fulaan.operation.dto.AppCommentDTO;
import com.fulaan.operation.dto.AppNoticeDTO;
import com.fulaan.pojo.Attachement;
import com.fulaan.service.CommunityService;
import com.fulaan.systemMessage.dto.SuperTopicDTO;
import com.fulaan.user.service.UserService;
import com.pojo.appnotice.AppNoticeEntry;
import com.pojo.backstage.SystemMessageEntry;
import com.pojo.backstage.TeacherApproveEntry;
import com.pojo.fcommunity.CommunityDetailEntry;
import com.pojo.indexPage.IndexPageEntry;
import com.pojo.newVersionGrade.CommunityType;
import com.pojo.operation.AppCommentEntry;
import com.pojo.user.UserDetailInfoDTO;
import com.pojo.user.UserEntry;
import com.sys.constants.Constant;
import com.sys.utils.AvatarUtils;
import com.sys.utils.TimeChangeUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by James on 2017/9/28.
 */
@Service
public class IndexPageService {
    private AppCommentDao appCommentDao = new AppCommentDao();

    private AppNoticeDao appNoticeDao = new AppNoticeDao();
    @Autowired
    private CommunityService communityService;
    @Autowired
    private UserService userService;

    private IndexPageDao indexPageDao = new IndexPageDao();

    private MemberDao memberDao =new MemberDao();

    private SystemMessageDao systemMessageDao = new SystemMessageDao();

    private TeacherApproveDao teacherApproveDao = new TeacherApproveDao();

    private CommunityDetailDao communityDetailDao = new CommunityDetailDao();
    //老师社群
   // private static final String TEACHERCOMMUNIY = "5ae993953d4df93f01b11a36";
    //线上
   private static final String TEACHERCOMMUNIY = "5ae993953d4df93f01b11a36";
    //家长社群
   // private static final String PARENTCOMMUNIY = "5acecca9bf2e792210a70583";
    //线上
   private static final String PARENTCOMMUNIY = "5b04d9f53d4df9273f5c775a";
    //学生社群
   //private static final String STUDENTCOMMUNIY = "5abaf547bf2e791a5457a584";
    //线上
    private static final String STUDENTCOMMUNIY = "5b04d9eb3d4df9273f5c7747";




    public Map<String,Object> getIndexList(ObjectId userId,int page,int pageSize){
        List<Map<String,Object>> list = new ArrayList<Map<String, Object>>();
        //作业逻辑
        //List<ObjectId> ids = memberDao.selectMyRoleList(userId);
       // List<ObjectId> obl = communityDao.selectCommunityByGroupIds(ids);
        //通知逻辑
        List<ObjectId>  dlist =communityService.getCommunitys3(userId, 1, 100);
      /*  List<ObjectId>  dlist = new ArrayList<ObjectId>();
        if(communityDTOList.size() >0){
            for(CommunityDTO dto : communityDTOList){
                dlist.add(new ObjectId(dto.getId()));
            }
        }*/
        dlist.add(userId);
        List<IndexPageEntry> entrys = indexPageDao.getPageList(dlist,userId, page, pageSize);
        int count = indexPageDao.countPageList(dlist,userId);
        //作业
        List<ObjectId> appList = new ArrayList<ObjectId>();
        Map<String,Object> appMap =new HashMap<String, Object>();
        //通知
        List<ObjectId> noList = new ArrayList<ObjectId>();
        List<ObjectId> syList = new ArrayList<ObjectId>();
        List<String> stringList = new ArrayList<String>();
        if(entrys.size()>0){
            for(IndexPageEntry entry : entrys){
                if(entry.getType()==1){
                    //作业
                    appList.add(entry.getContactId());
                }else if(entry.getType()==2){
                    //通知
                    noList.add(entry.getContactId());
                }else if(entry.getType()==3 && entry.getUserId() != null && entry.getUserId().equals(userId)){
                    noList.add(entry.getContactId());
                }else if(entry.getType()==4){
                    syList.add(entry.getContactId());
                }
                stringList.add(entry.getContactId().toString());
            }
        }
        //作业
        List<String> uids = new ArrayList<String>();
        if(appList.size()>0){
            List<AppCommentDTO> dtos = new ArrayList<AppCommentDTO>();
            List<AppCommentEntry> appCommentEntries = appCommentDao.getEntryListByIds(appList);
            if(appCommentEntries.size()>0){
                for(AppCommentEntry en : appCommentEntries){
                    AppCommentDTO dto3 = new AppCommentDTO(en);
                    dto3.setType(2);
                    uids.add(dto3.getAdminId());
                    dtos.add(dto3);
                }
            }

            List<UserDetailInfoDTO> udtos = userService.findUserInfoByUserIds(uids);
            Map<String,UserDetailInfoDTO> map = new HashMap<String, UserDetailInfoDTO>();
            if(udtos != null && udtos.size()>0){
                for(UserDetailInfoDTO dto4 : udtos){
                    map.put(dto4.getId(),dto4);
                }
            }
            for(AppCommentDTO dto5 : dtos){
                UserDetailInfoDTO dto9 = map.get(dto5.getAdminId());
                if(dto9 != null){
                    String name = StringUtils.isNotEmpty(dto9.getNickName()) ? dto9.getNickName() : dto9.getUserName();
                    dto5.setAdminName(name);
                    dto5.setAdminUrl(dto9.getImgUrl());
                }
            }
            for(AppCommentDTO dto6 : dtos){
                Map<String,Object> ob1 = new HashMap<String, Object>();
                ob1.put("tag", CommunityType.appComment.getDes());
                ob1.put("groupName",dto6.getRecipientName());
                ob1.put("id",dto6.getId());
                ob1.put("userName",dto6.getAdminName());
                ob1.put("subject",dto6.getSubject());
                ob1.put("avatar",dto6.getAdminUrl());
                ob1.put("title",dto6.getTitle());
                ob1.put("time", dto6.getCreateTime());
                ob1.put("content",dto6.getDescription());
                ob1.put("imageList",dto6.getImageList());
                ob1.put("commentCount",dto6.getTalkNumber());
                ob1.put("videoList",dto6.getVideoList());
                ob1.put("voiceList",dto6.getVoiceList());
                ob1.put("attachements",dto6.getAttachements());
                list.add(ob1);
            }
        }
        //通知
        if(noList.size()>0){
           List<AppNoticeEntry> appNoticeEntries = appNoticeDao.getAppNoticeEntriesByIds(noList);
            List<ObjectId> userIds=new ArrayList<ObjectId>();
            for(AppNoticeEntry entry:appNoticeEntries){
                userIds.add(entry.getUserId());
            }
            Map<ObjectId,UserEntry> userEntryMap=userService.getUserEntryMap(userIds, Constant.FIELDS);
            for(AppNoticeEntry entry:appNoticeEntries){
                AppNoticeDTO dto8=new AppNoticeDTO(entry);
                UserEntry userEntry=userEntryMap.get(entry.getUserId());
                if(null!=userEntry){
                    dto8.setAvatar(AvatarUtils.getAvatar(userEntry.getAvatar(), userEntry.getRole(), userEntry.getSex()));
                    String name = StringUtils.isNotEmpty(userEntry.getNickName()) ? userEntry.getNickName() : userEntry.getUserName();
                    dto8.setUserName(name);
                }
                Map<String,Object> ob1 = new HashMap<String, Object>();
                ob1.put("tag", CommunityType.appNotice.getDes());
                ob1.put("cardType",1);
                ob1.put("groupName",dto8.getGroupName());
                ob1.put("id",dto8.getId());
                ob1.put("userName",org.apache.commons.lang.StringUtils.isNotEmpty(userEntry.getNickName())?userEntry.getNickName():userEntry.getUserName());
                ob1.put("subject",dto8.getSubject());
                ob1.put("avatar",dto8.getAvatar());
                ob1.put("title",dto8.getTitle());
                ob1.put("time",dto8.getTime());
                ob1.put("content",dto8.getContent());
                ob1.put("imageList",dto8.getImageList());
                ob1.put("commentCount",dto8.getCommentCount());
                ob1.put("videoList",dto8.getVideoList());
                ob1.put("voiceList",dto8.getVoiceList());
                ob1.put("attachements",dto8.getAttachements());
                ob1.put("isRead",0);
                if(dto8.getReadList().contains(userId.toString())){
                    ob1.put("isRead",1);
                }
                //设置已阅和未阅的人数
                List<ObjectId> reads=entry.getReaList();
                List<ObjectId> members=memberDao.getAllMemberIds(entry.getGroupId());
                members.remove(userId);
                ob1.put("totalReadCount", members.size());
                members.removeAll(reads);
                ob1.put("readCount", reads.size());
                ob1.put("unReadCount",members.size());
                ob1.put("timeExpression",TimeChangeUtils.getChangeTime(entry.getSubmitTime()));
                if(dto8.getUserId().equals(userId.toString())){
                    ob1.put("isOwner",true);
                }else{
                    ob1.put("isOwner",false);
                }
                list.add(ob1);

            }

        }
        Map<String,Object> ob5 = new HashMap<String, Object>();
        if(syList.size()>0){
            List<SystemMessageEntry> systemMessageEntries = systemMessageDao.selectContentList(syList);
            for(SystemMessageEntry entry:systemMessageEntries){
                SystemMessageDTO dto8 = new SystemMessageDTO(entry);
                if(entry.getType()==1){
                    Map<String,Object> ob1 = new HashMap<String, Object>();
                    ob1.put("tag", CommunityType.system.getDes());
                    ob1.put("cardType",2);
                    ob1.put("groupName","");
                    ob1.put("id",dto8.getId());
                    ob1.put("userName","家校美小助手");
                    ob1.put("subject","");
                    ob1.put("avatar","http://7xiclj.com1.z0.glb.clouddn.com/5a26565027fddd1db08722f1.png");
                    ob1.put("title","家校美使用向导");
                    ob1.put("time",dto8.getCreateTime());
                    ob1.put("content","家校美，一款富有魔力的产品！");
                  /*  List<Attachement> imageList=new ArrayList<Attachement>();
                    Attachement a = new Attachement();
                    a.setUrl("");
                    imageList.add(a);*/
                    ob1.put("imageList",new ArrayList<Attachement>());
                    ob1.put("commentCount",0);
                    ob1.put("videoList",new ArrayList<VideoDTO>());
                    ob1.put("voiceList",new ArrayList<Attachement>());
                    ob1.put("attachements",new ArrayList<Attachement>());
                    ob1.put("isRead",0);
                    ob1.put("totalReadCount", 0);
                    ob1.put("readCount", 0);
                    ob1.put("unReadCount",0);
                    ob1.put("timeExpression","");
                    ob1.put("isOwner",true);
                    //不显示新手引导
                    //ob5 = ob1;
                    //list.add(ob1);
                }else if(entry.getType()==2){
                    Map<String,Object> ob1 = new HashMap<String, Object>();
                    ob1.put("tag", CommunityType.system.getDes());
                    ob1.put("cardType",3);
                    ob1.put("groupName",dto8.getSourceName());
                    ob1.put("id",dto8.getId());
                    ob1.put("userName","家校美小助手");
                    ob1.put("subject","");
                    ob1.put("avatar","http://7xiclj.com1.z0.glb.clouddn.com/5a26565027fddd1db08722f1.png");
                    ob1.put("title","恭喜您创建了一个新社群");
                    ob1.put("time",dto8.getCreateTime());
                    if(dto8.getContent()!=null && !dto8.getContent().equals("")){
                        ob1.put("content","恭喜您于"+dto8.getCreateTime().substring(0,11)+"日成功创建了“"
                                + dto8.getSourceName()+"”社群，您的社群id是:"+dto8.getContent()+"，您是该班级社群的“社长”，拥有一切特权。\n 此外您后期最多" +
                                "可以指定设置10位成员为“副社长”，他们也能拥有各项发帖权利。");
                    }else{
                        ob1.put("content","恭喜您于"+dto8.getCreateTime().substring(0,11)+"日成功创建了“"
                                + dto8.getSourceName()+"”社群，您是该班级社群的“社长”，拥有一切特权。\n 此外您后期最多" +
                                "可以指定设置10位成员为“副社长”，他们也能拥有各项发帖权利。");
                    }

                  /*  List<Attachement> imageList=new ArrayList<Attachement>();
                    Attachement a = new Attachement();
                    a.setUrl("");
                    imageList.add(a);*/
                    ob1.put("imageList",new ArrayList<Attachement>());
                    ob1.put("commentCount",0);
                    ob1.put("videoList",new ArrayList<VideoDTO>());
                    ob1.put("voiceList",new ArrayList<Attachement>());
                    ob1.put("attachements",new ArrayList<Attachement>());
                    ob1.put("isRead",0);
                    ob1.put("totalReadCount", 0);
                    ob1.put("readCount", 0);
                    ob1.put("unReadCount",0);
                    ob1.put("timeExpression","");
                    ob1.put("isOwner",true);
                    list.add(ob1);
                }else{

                }
            }
        }
        if(ob5.size()!=0){
            list.add(ob5);
        }
        Map<String,Object> map = new HashMap<String, Object>();
        List<Map<String,Object>> list2 = new ArrayList<Map<String, Object>>();
        if(list.size()>0){
            for(String str : stringList){
                for(Map<String,Object> map4 : list){
                    String string = (String)map4.get("id");
                    if(str.equals(string)){
                        list2.add(map4);
                    }
                }
            }
        }
        map.put("count",count);
        map.put("list",list2);
        return map;
    }

    public Map<String,Object> getNewIndexList(ObjectId userId,int page,int pageSize){
        List<Map<String,Object>> list = new ArrayList<Map<String, Object>>();
        //通知逻辑
        List<ObjectId>  dlist =communityService.getCommunitys3(userId, 1, 100);
        dlist.add(userId);
        TeacherApproveEntry teacherApproveEntry = teacherApproveDao.getEntry(userId);
        if(teacherApproveEntry!=null && teacherApproveEntry.getType()==2){//认证大V
            dlist.add(new ObjectId(TEACHERCOMMUNIY));
        }else{
            dlist.add(new ObjectId(PARENTCOMMUNIY));
        }
        List<IndexPageEntry> entrys = indexPageDao.getPageList(dlist,userId, page, pageSize);
        int count = indexPageDao.countPageList(dlist,userId);
        //作业
        List<ObjectId> appList = new ArrayList<ObjectId>();
        Map<String,Object> appMap =new HashMap<String, Object>();
        //通知
        List<ObjectId> noList = new ArrayList<ObjectId>();
        List<ObjectId> syList = new ArrayList<ObjectId>();
        List<String> stringList = new ArrayList<String>();
        if(entrys.size()>0){
            for(IndexPageEntry entry : entrys){
                if(entry.getType()==1){
                    //作业
                    appList.add(entry.getContactId());
                }else if(entry.getType()==2){
                    //通知
                    noList.add(entry.getContactId());
                }else if(entry.getType()==3 && entry.getUserId() != null && entry.getUserId().equals(userId)){
                    noList.add(entry.getContactId());
                }else if(entry.getType()==4){
                    syList.add(entry.getContactId());
                }
                stringList.add(entry.getContactId().toString());
            }
        }
        //作业
        List<String> uids = new ArrayList<String>();
        if(appList.size()>0){
            List<AppCommentDTO> dtos = new ArrayList<AppCommentDTO>();
            List<AppCommentEntry> appCommentEntries = appCommentDao.getEntryListByIds(appList);
            if(appCommentEntries.size()>0){
                for(AppCommentEntry en : appCommentEntries){
                    AppCommentDTO dto3 = new AppCommentDTO(en);
                    dto3.setType(2);
                    uids.add(dto3.getAdminId());
                    dtos.add(dto3);
                }
            }

            List<UserDetailInfoDTO> udtos = userService.findUserInfoByUserIds(uids);
            Map<String,UserDetailInfoDTO> map = new HashMap<String, UserDetailInfoDTO>();
            if(udtos != null && udtos.size()>0){
                for(UserDetailInfoDTO dto4 : udtos){
                    map.put(dto4.getId(),dto4);
                }
            }
            for(AppCommentDTO dto5 : dtos){
                UserDetailInfoDTO dto9 = map.get(dto5.getAdminId());
                if(dto9 != null){
                    String name = StringUtils.isNotEmpty(dto9.getNickName()) ? dto9.getNickName() : dto9.getUserName();
                    dto5.setAdminName(name);
                    dto5.setAdminUrl(dto9.getImgUrl());
                }
            }
            for(AppCommentDTO dto6 : dtos){
                Map<String,Object> ob1 = new HashMap<String, Object>();
                ob1.put("tag", CommunityType.appComment.getDes());
                ob1.put("groupName",dto6.getRecipientName());
                ob1.put("id",dto6.getId());
                ob1.put("userName",dto6.getAdminName());
                ob1.put("subject",dto6.getSubject());
                ob1.put("avatar",dto6.getAdminUrl());
                ob1.put("title",dto6.getTitle());
                ob1.put("time", dto6.getCreateTime());
                ob1.put("content",dto6.getDescription());
                ob1.put("imageList",dto6.getImageList());
                ob1.put("commentCount",dto6.getTalkNumber());
                ob1.put("videoList",dto6.getVideoList());
                ob1.put("voiceList",dto6.getVoiceList());
                ob1.put("attachements",dto6.getAttachements());
                list.add(ob1);
            }
        }
        //通知
        if(noList.size()>0){
            List<AppNoticeEntry> appNoticeEntries = appNoticeDao.getAppNoticeEntriesByIds(noList);
            List<ObjectId> userIds=new ArrayList<ObjectId>();
            for(AppNoticeEntry entry:appNoticeEntries){
                userIds.add(entry.getUserId());
            }
            Map<ObjectId,UserEntry> userEntryMap=userService.getUserEntryMap(userIds, Constant.FIELDS);
            for(AppNoticeEntry entry:appNoticeEntries){
                AppNoticeDTO dto8=new AppNoticeDTO(entry);
                UserEntry userEntry=userEntryMap.get(entry.getUserId());
                if(null!=userEntry){
                    dto8.setAvatar(AvatarUtils.getAvatar(userEntry.getAvatar(), userEntry.getRole(), userEntry.getSex()));
                    /*String name = StringUtils.isNotEmpty(userEntry.getNickName()) ? userEntry.getNickName() : userEntry.getUserName();
                    dto8.setUserName(name);*/
                }
                Map<String,Object> ob1 = new HashMap<String, Object>();
                if(dto8.getCommunityId().equals(TEACHERCOMMUNIY) || dto8.getCommunityId().equals(PARENTCOMMUNIY)){
                    ob1.put("tag", CommunityType.newSystem.getDes());
                    ob1.put("cardType",4);
                    ob1.put("groupName",dto8.getGroupName());
                    ob1.put("id",dto8.getId());
                    ob1.put("userName",entry.getUserName());
                   /* if(dto8.getCommunityId().equals(TEACHERCOMMUNIY)){
                        ob1.put("subject","老师");
                    }else{
                        ob1.put("subject","家长");
                    }*/
                    ob1.put("subject",dto8.getSubject());
                    ob1.put("avatar","http://7xiclj.com1.z0.glb.clouddn.com/5a26565027fddd1db08722f1.png");
                    ob1.put("title",dto8.getTitle());
                    ob1.put("time",dto8.getTime());
                    ob1.put("content",dto8.getContent());
                    ob1.put("imageList",dto8.getImageList());
                    ob1.put("commentCount",dto8.getCommentCount());
                    ob1.put("videoList",dto8.getVideoList());
                    ob1.put("voiceList",dto8.getVoiceList());
                    ob1.put("attachements",dto8.getAttachements());
                    ob1.put("isRead",0);
                    if(dto8.getReadList().contains(userId.toString())){
                        ob1.put("isRead",1);
                    }
                    //设置已阅和未阅的人数
                    List<ObjectId> reads=entry.getReaList();
                    ob1.put("totalReadCount", 0);
                    ob1.put("readCount", reads.size());
                    ob1.put("unReadCount",0);
                    ob1.put("timeExpression",TimeChangeUtils.getChangeTime(entry.getSubmitTime()));
                    if(dto8.getUserId().equals(userId.toString())){
                        ob1.put("isOwner",true);
                    }else{
                        ob1.put("isOwner",false);
                    }
                }else{
                    ob1.put("tag", CommunityType.appNotice.getDes());
                    ob1.put("cardType",1);
                    ob1.put("groupName",dto8.getGroupName());
                    ob1.put("id",dto8.getId());
                    ob1.put("userName",org.apache.commons.lang.StringUtils.isNotEmpty(userEntry.getNickName())?userEntry.getNickName():userEntry.getUserName());
                    ob1.put("subject",dto8.getSubject());
                    ob1.put("avatar",dto8.getAvatar());
                    ob1.put("title",dto8.getTitle());
                    ob1.put("time",dto8.getTime());
                    ob1.put("content",dto8.getContent());
                    ob1.put("imageList",dto8.getImageList());
                    ob1.put("commentCount",dto8.getCommentCount());
                    ob1.put("videoList",dto8.getVideoList());
                    ob1.put("voiceList",dto8.getVoiceList());
                    ob1.put("attachements",dto8.getAttachements());
                    ob1.put("isRead",0);
                    if(dto8.getReadList().contains(userId.toString())){
                        ob1.put("isRead",1);
                    }
                    //设置已阅和未阅的人数
                    List<ObjectId> reads=entry.getReaList();
                    List<ObjectId> members=memberDao.getAllMemberIds(entry.getGroupId());
                    members.remove(userId);
                    ob1.put("totalReadCount", members.size());
                    members.removeAll(reads);
                    ob1.put("readCount", reads.size());
                    ob1.put("unReadCount",members.size());
                    ob1.put("timeExpression",TimeChangeUtils.getChangeTime(entry.getSubmitTime()));
                    if(dto8.getUserId().equals(userId.toString())){
                        ob1.put("isOwner",true);
                    }else{
                        ob1.put("isOwner",false);
                    }
                }
                list.add(ob1);

            }

        }
        Map<String,Object> ob5 = new HashMap<String, Object>();
        if(syList.size()>0){
            List<SystemMessageEntry> systemMessageEntries = systemMessageDao.selectContentList(syList);
            for(SystemMessageEntry entry:systemMessageEntries){
                SystemMessageDTO dto8 = new SystemMessageDTO(entry);
                if(entry.getType()==1){
                    Map<String,Object> ob1 = new HashMap<String, Object>();
                    ob1.put("tag", CommunityType.system.getDes());
                    ob1.put("cardType",2);
                    ob1.put("groupName","");
                    ob1.put("id",dto8.getId());
                    ob1.put("userName","家校美小助手");
                    ob1.put("subject","");
                    ob1.put("avatar","http://7xiclj.com1.z0.glb.clouddn.com/5a26565027fddd1db08722f1.png");
                    ob1.put("title","家校美使用向导");
                    ob1.put("time",dto8.getCreateTime());
                    ob1.put("content","家校美，一款富有魔力的产品！");
                  /*  List<Attachement> imageList=new ArrayList<Attachement>();
                    Attachement a = new Attachement();
                    a.setUrl("");
                    imageList.add(a);*/
                    ob1.put("imageList",new ArrayList<Attachement>());
                    ob1.put("commentCount",0);
                    ob1.put("videoList",new ArrayList<VideoDTO>());
                    ob1.put("voiceList",new ArrayList<Attachement>());
                    ob1.put("attachements",new ArrayList<Attachement>());
                    ob1.put("isRead",0);
                    ob1.put("totalReadCount", 0);
                    ob1.put("readCount", 0);
                    ob1.put("unReadCount",0);
                    ob1.put("timeExpression","");
                    ob1.put("isOwner",true);
                    ob5 = ob1;
                    //list.add(ob1);
                }else if(entry.getType()==2){
                    Map<String,Object> ob1 = new HashMap<String, Object>();
                    ob1.put("tag", CommunityType.system.getDes());
                    ob1.put("cardType",3);
                    ob1.put("groupName",dto8.getSourceName());
                    ob1.put("id",dto8.getId());
                    ob1.put("userName","家校美小助手");
                    ob1.put("subject","");
                    ob1.put("avatar","http://7xiclj.com1.z0.glb.clouddn.com/5a26565027fddd1db08722f1.png");
                    ob1.put("title","恭喜您创建了一个新社群");
                    ob1.put("time",dto8.getCreateTime());
                    if(dto8.getContent()!=null && !dto8.getContent().equals("")){
                        ob1.put("content","恭喜您于"+dto8.getCreateTime().substring(0,11)+"日成功创建了“"
                                + dto8.getSourceName()+"”社群，您的社群id是:"+dto8.getContent()+"，您是该班级社群的“社长”，拥有一切特权。\n 此外您后期最多" +
                                "可以指定设置10位成员为“副社长”，他们也能拥有各项发帖权利。");
                    }else{
                        ob1.put("content","恭喜您于"+dto8.getCreateTime().substring(0,11)+"日成功创建了“"
                                + dto8.getSourceName()+"”社群，您是该班级社群的“社长”，拥有一切特权。\n 此外您后期最多" +
                                "可以指定设置10位成员为“副社长”，他们也能拥有各项发帖权利。");
                    }

                  /*  List<Attachement> imageList=new ArrayList<Attachement>();
                    Attachement a = new Attachement();
                    a.setUrl("");
                    imageList.add(a);*/
                    ob1.put("imageList",new ArrayList<Attachement>());
                    ob1.put("commentCount",0);
                    ob1.put("videoList",new ArrayList<VideoDTO>());
                    ob1.put("voiceList",new ArrayList<Attachement>());
                    ob1.put("attachements",new ArrayList<Attachement>());
                    ob1.put("isRead",0);
                    ob1.put("totalReadCount", 0);
                    ob1.put("readCount", 0);
                    ob1.put("unReadCount",0);
                    ob1.put("timeExpression","");
                    ob1.put("isOwner",true);
                    list.add(ob1);
                }else{

                }
            }
        }
        if(ob5.size()!=0){
            list.add(ob5);
        }
        Map<String,Object> map = new HashMap<String, Object>();
        List<Map<String,Object>> list2 = new ArrayList<Map<String, Object>>();
        if(list.size()>0){
            for(String str : stringList){
                for(Map<String,Object> map4 : list){
                    String string = (String)map4.get("id");
                    if(str.equals(string)){
                        list2.add(map4);
                    }
                }
            }
        }
        map.put("count",count);
        map.put("list",list2);
        return map;
    }

    public List<SuperTopicDTO> getHotList(int role){
        List<SuperTopicDTO> superTopicDTOs = new ArrayList<SuperTopicDTO>();
        List<ObjectId> objectIdList = new ArrayList<ObjectId>();
        if(role==2){//认证大V
            objectIdList.add(new ObjectId(TEACHERCOMMUNIY));
        }else{
            objectIdList.add(new ObjectId(PARENTCOMMUNIY));
        }
        List<CommunityDetailEntry> entries =  communityDetailDao.getHotDetails(objectIdList, Constant.THREE, 1);
        for(CommunityDetailEntry communityDetailEntry:entries){
            SuperTopicDTO superTopicDTO = new SuperTopicDTO(communityDetailEntry);
            superTopicDTOs.add(superTopicDTO);
        }

        return superTopicDTOs;
    }
    public Map<String,Object> getHotTopicList(ObjectId userId,int page,int pageSize){
        Map<String,Object> map = new HashMap<String, Object>();
        List<SuperTopicDTO> superTopicDTOs = new ArrayList<SuperTopicDTO>();
        List<ObjectId> objectIdList = new ArrayList<ObjectId>();
        TeacherApproveEntry teacherApproveEntry = teacherApproveDao.getEntry(userId);
        if(teacherApproveEntry!=null && teacherApproveEntry.getType()==2){//认证大V
            objectIdList.add(new ObjectId(TEACHERCOMMUNIY));

        }else{
            objectIdList.add(new ObjectId(PARENTCOMMUNIY));
        }
        List<CommunityDetailEntry> entries =  communityDetailDao.getAllHotDetails(objectIdList, Constant.THREE,page,pageSize);
        int count = communityDetailDao.countAllHotDetails(objectIdList, Constant.THREE);
        for(CommunityDetailEntry communityDetailEntry:entries){
            SuperTopicDTO superTopicDTO = new SuperTopicDTO(communityDetailEntry);
            superTopicDTOs.add(superTopicDTO);
        }
        map.put("list",superTopicDTOs);
        map.put("count",count);
        return map;
    }

    public Map<String,Object> getNewHotIndexList(ObjectId userId,int page,int pageSize){

        List<Map<String,Object>> list = new ArrayList<Map<String, Object>>();
        //通知逻辑
        List<ObjectId>  dlist =communityService.getCommunitys3(userId, 1, 100);
        dlist.add(userId);
        TeacherApproveEntry teacherApproveEntry = teacherApproveDao.getEntry(userId);
        List<SuperTopicDTO> superTopicDTOs = null;
        if(teacherApproveEntry!=null && teacherApproveEntry.getType()==2){//认证大V
            dlist.add(new ObjectId(TEACHERCOMMUNIY));
            superTopicDTOs=getHotList(2);
        }else{
            dlist.add(new ObjectId(PARENTCOMMUNIY));
            superTopicDTOs=getHotList(1);
        }
        List<IndexPageEntry> entrys = indexPageDao.getPageList(dlist,userId, page, pageSize);
        int count = indexPageDao.countPageList(dlist,userId);
        //作业
        List<ObjectId> appList = new ArrayList<ObjectId>();
        Map<String,Object> appMap =new HashMap<String, Object>();
        //通知
        List<ObjectId> noList = new ArrayList<ObjectId>();
        List<ObjectId> syList = new ArrayList<ObjectId>();
        List<String> stringList = new ArrayList<String>();
        if(entrys.size()>0){
            for(IndexPageEntry entry : entrys){
                if(entry.getType()==1){
                    //作业
                    appList.add(entry.getContactId());
                }else if(entry.getType()==2){
                    //通知
                    noList.add(entry.getContactId());
                }else if(entry.getType()==3 && entry.getUserId() != null && entry.getUserId().equals(userId)){
                    noList.add(entry.getContactId());
                }else if(entry.getType()==4){
                    syList.add(entry.getContactId());
                }
                stringList.add(entry.getContactId().toString());
            }
        }
        //作业
        List<String> uids = new ArrayList<String>();
        if(appList.size()>0){
            List<AppCommentDTO> dtos = new ArrayList<AppCommentDTO>();
            List<AppCommentEntry> appCommentEntries = appCommentDao.getEntryListByIds(appList);
            if(appCommentEntries.size()>0){
                for(AppCommentEntry en : appCommentEntries){
                    AppCommentDTO dto3 = new AppCommentDTO(en);
                    dto3.setType(2);
                    uids.add(dto3.getAdminId());
                    dtos.add(dto3);
                }
            }

            List<UserDetailInfoDTO> udtos = userService.findUserInfoByUserIds(uids);
            Map<String,UserDetailInfoDTO> map = new HashMap<String, UserDetailInfoDTO>();
            if(udtos != null && udtos.size()>0){
                for(UserDetailInfoDTO dto4 : udtos){
                    map.put(dto4.getId(),dto4);
                }
            }
            for(AppCommentDTO dto5 : dtos){
                UserDetailInfoDTO dto9 = map.get(dto5.getAdminId());
                if(dto9 != null){
                    String name = StringUtils.isNotEmpty(dto9.getNickName()) ? dto9.getNickName() : dto9.getUserName();
                    dto5.setAdminName(name);
                    dto5.setAdminUrl(dto9.getImgUrl());
                }
            }
            for(AppCommentDTO dto6 : dtos){
                Map<String,Object> ob1 = new HashMap<String, Object>();
                ob1.put("tag", CommunityType.appComment.getDes());
                ob1.put("groupName",dto6.getRecipientName());
                ob1.put("id",dto6.getId());
                ob1.put("userName",dto6.getAdminName());
                ob1.put("subject",dto6.getSubject());
                ob1.put("avatar",dto6.getAdminUrl());
                ob1.put("title",dto6.getTitle());
                ob1.put("time", dto6.getCreateTime());
                ob1.put("content",dto6.getDescription());
                ob1.put("imageList",dto6.getImageList());
                ob1.put("commentCount",dto6.getTalkNumber());
                ob1.put("videoList",dto6.getVideoList());
                ob1.put("voiceList",dto6.getVoiceList());
                ob1.put("attachements",dto6.getAttachements());
                list.add(ob1);
            }
        }
        //通知
        if(noList.size()>0){
            List<AppNoticeEntry> appNoticeEntries = appNoticeDao.getAppNoticeEntriesByIds(noList);
            List<ObjectId> userIds=new ArrayList<ObjectId>();
            for(AppNoticeEntry entry:appNoticeEntries){
                userIds.add(entry.getUserId());
            }
            Map<ObjectId,UserEntry> userEntryMap=userService.getUserEntryMap(userIds, Constant.FIELDS);
            for(AppNoticeEntry entry:appNoticeEntries){
                AppNoticeDTO dto8=new AppNoticeDTO(entry);
                UserEntry userEntry=userEntryMap.get(entry.getUserId());
                if(null!=userEntry){
                    dto8.setAvatar(AvatarUtils.getAvatar(userEntry.getAvatar(), userEntry.getRole(), userEntry.getSex()));
                    /*String name = StringUtils.isNotEmpty(userEntry.getNickName()) ? userEntry.getNickName() : userEntry.getUserName();
                    dto8.setUserName(name);*/
                }
                Map<String,Object> ob1 = new HashMap<String, Object>();
                if(dto8.getCommunityId().equals(TEACHERCOMMUNIY) || dto8.getCommunityId().equals(PARENTCOMMUNIY)){
                    ob1.put("tag", CommunityType.newSystem.getDes());
                    ob1.put("cardType",4);
                    ob1.put("groupName",dto8.getGroupName());
                    ob1.put("id",dto8.getId());
                    ob1.put("userName",entry.getUserName());
                   /* if(dto8.getCommunityId().equals(TEACHERCOMMUNIY)){
                        ob1.put("subject","老师");
                    }else{
                        ob1.put("subject","家长");
                    }*/
                    ob1.put("subject",dto8.getSubject());
                    ob1.put("avatar","http://7xiclj.com1.z0.glb.clouddn.com/5a26565027fddd1db08722f1.png");
                    ob1.put("title",dto8.getTitle());
                    ob1.put("time",dto8.getTime());
                    ob1.put("content",dto8.getContent());
                    ob1.put("imageList",dto8.getImageList());
                    ob1.put("commentCount",dto8.getCommentCount());
                    ob1.put("videoList",dto8.getVideoList());
                    ob1.put("voiceList",dto8.getVoiceList());
                    ob1.put("attachements",dto8.getAttachements());
                    ob1.put("isRead",0);
                    if(dto8.getReadList().contains(userId.toString())){
                        ob1.put("isRead",1);
                    }
                    //设置已阅和未阅的人数
                    List<ObjectId> reads=entry.getReaList();
                    ob1.put("totalReadCount", 0);
                    ob1.put("readCount", reads.size());
                    ob1.put("unReadCount",0);
                    ob1.put("timeExpression",TimeChangeUtils.getChangeTime(entry.getSubmitTime()));
                    if(dto8.getUserId().equals(userId.toString())){
                        ob1.put("isOwner",true);
                    }else{
                        ob1.put("isOwner",false);
                    }
                }else{
                    ob1.put("tag", CommunityType.appNotice.getDes());
                    ob1.put("cardType",1);
                    ob1.put("groupName",dto8.getGroupName());
                    ob1.put("id",dto8.getId());
                    ob1.put("userName",org.apache.commons.lang.StringUtils.isNotEmpty(userEntry.getNickName())?userEntry.getNickName():userEntry.getUserName());
                    ob1.put("subject",dto8.getSubject());
                    ob1.put("avatar",dto8.getAvatar());
                    ob1.put("title",dto8.getTitle());
                    ob1.put("time",dto8.getTime());
                    ob1.put("content",dto8.getContent());
                    ob1.put("imageList",dto8.getImageList());
                    ob1.put("commentCount",dto8.getCommentCount());
                    ob1.put("videoList",dto8.getVideoList());
                    ob1.put("voiceList",dto8.getVoiceList());
                    ob1.put("attachements",dto8.getAttachements());
                    ob1.put("isRead",0);
                    if(dto8.getReadList().contains(userId.toString())){
                        ob1.put("isRead",1);
                    }
                    //设置已阅和未阅的人数
                    List<ObjectId> reads=entry.getReaList();
                    List<ObjectId> members=memberDao.getAllMemberIds(entry.getGroupId());
                    members.remove(userId);
                    ob1.put("totalReadCount", members.size());
                    members.removeAll(reads);
                    ob1.put("readCount", reads.size());
                    ob1.put("unReadCount",members.size());
                    ob1.put("timeExpression",TimeChangeUtils.getChangeTime(entry.getSubmitTime()));
                    if(dto8.getUserId().equals(userId.toString())){
                        ob1.put("isOwner",true);
                    }else{
                        ob1.put("isOwner",false);
                    }
                }
                list.add(ob1);

            }

        }
        Map<String,Object> ob5 = new HashMap<String, Object>();
        if(syList.size()>0){
            List<SystemMessageEntry> systemMessageEntries = systemMessageDao.selectContentList(syList);
            for(SystemMessageEntry entry:systemMessageEntries){
                SystemMessageDTO dto8 = new SystemMessageDTO(entry);
                if(entry.getType()==1){
                    Map<String,Object> ob1 = new HashMap<String, Object>();
                    ob1.put("tag", CommunityType.system.getDes());
                    ob1.put("cardType",2);
                    ob1.put("groupName","");
                    ob1.put("id",dto8.getId());
                    ob1.put("userName","家校美小助手");
                    ob1.put("subject","");
                    ob1.put("avatar","http://7xiclj.com1.z0.glb.clouddn.com/5a26565027fddd1db08722f1.png");
                    ob1.put("title","家校美使用向导");
                    ob1.put("time",dto8.getCreateTime());
                    ob1.put("content","家校美，一款富有魔力的产品！");
                  /*  List<Attachement> imageList=new ArrayList<Attachement>();
                    Attachement a = new Attachement();
                    a.setUrl("");
                    imageList.add(a);*/
                    ob1.put("imageList",new ArrayList<Attachement>());
                    ob1.put("commentCount",0);
                    ob1.put("videoList",new ArrayList<VideoDTO>());
                    ob1.put("voiceList",new ArrayList<Attachement>());
                    ob1.put("attachements",new ArrayList<Attachement>());
                    ob1.put("isRead",0);
                    ob1.put("totalReadCount", 0);
                    ob1.put("readCount", 0);
                    ob1.put("unReadCount",0);
                    ob1.put("timeExpression","");
                    ob1.put("isOwner",true);
                    ob5 = ob1;
                    //list.add(ob1);
                }else if(entry.getType()==2){
                    Map<String,Object> ob1 = new HashMap<String, Object>();
                    ob1.put("tag", CommunityType.system.getDes());
                    ob1.put("cardType",3);
                    ob1.put("groupName",dto8.getSourceName());
                    ob1.put("id",dto8.getId());
                    ob1.put("userName","家校美小助手");
                    ob1.put("subject","");
                    ob1.put("avatar","http://7xiclj.com1.z0.glb.clouddn.com/5a26565027fddd1db08722f1.png");
                    ob1.put("title","恭喜您创建了一个新社群");
                    ob1.put("time",dto8.getCreateTime());
                    if(dto8.getContent()!=null && !dto8.getContent().equals("")){
                        ob1.put("content","恭喜您于"+dto8.getCreateTime().substring(0,11)+"日成功创建了“"
                                + dto8.getSourceName()+"”社群，您的社群id是:"+dto8.getContent()+"，您是该班级社群的“社长”，拥有一切特权。\n 此外您后期最多" +
                                "可以指定设置10位成员为“副社长”，他们也能拥有各项发帖权利。");
                    }else{
                        ob1.put("content","恭喜您于"+dto8.getCreateTime().substring(0,11)+"日成功创建了“"
                                + dto8.getSourceName()+"”社群，您是该班级社群的“社长”，拥有一切特权。\n 此外您后期最多" +
                                "可以指定设置10位成员为“副社长”，他们也能拥有各项发帖权利。");
                    }

                  /*  List<Attachement> imageList=new ArrayList<Attachement>();
                    Attachement a = new Attachement();
                    a.setUrl("");
                    imageList.add(a);*/
                    ob1.put("imageList",new ArrayList<Attachement>());
                    ob1.put("commentCount",0);
                    ob1.put("videoList",new ArrayList<VideoDTO>());
                    ob1.put("voiceList",new ArrayList<Attachement>());
                    ob1.put("attachements",new ArrayList<Attachement>());
                    ob1.put("isRead",0);
                    ob1.put("totalReadCount", 0);
                    ob1.put("readCount", 0);
                    ob1.put("unReadCount",0);
                    ob1.put("timeExpression","");
                    ob1.put("isOwner",true);
                    list.add(ob1);
                }else if(entry.getType()==3){
                    Map<String,Object> ob1 = new HashMap<String, Object>();
                    ob1.put("tag", CommunityType.system.getDes());
                    ob1.put("cardType",3);
                    ob1.put("groupName",dto8.getSourceName());
                    ob1.put("id",dto8.getId());
                    ob1.put("userName","家校美小助手");
                    ob1.put("subject",dto8.getContent());
                    ob1.put("avatar","http://7xiclj.com1.z0.glb.clouddn.com/5a26565027fddd1db08722f1.png");
                    ob1.put("title","您的留言被选为精选留言了");
                    ob1.put("time",dto8.getCreateTime());
                    ob1.put("content",dto8.getFileType());
                    /*if(dto8.getContent()!=null && !dto8.getContent().equals("")){
                        ob1.put("content","恭喜您于"+dto8.getCreateTime().substring(0,11)+"日成功创建了“"
                                + dto8.getSourceName()+"”社群，您的社群id是:"+dto8.getContent()+"，您是该班级社群的“社长”，拥有一切特权。\n 此外您后期最多" +
                                "可以指定设置10位成员为“副社长”，他们也能拥有各项发帖权利。");
                    }else{
                        ob1.put("content","恭喜您于"+dto8.getCreateTime().substring(0,11)+"日成功创建了“"
                                + dto8.getSourceName()+"”社群，您是该班级社群的“社长”，拥有一切特权。\n 此外您后期最多" +
                                "可以指定设置10位成员为“副社长”，他们也能拥有各项发帖权利。");
                    }*/

                  /*  List<Attachement> imageList=new ArrayList<Attachement>();
                    Attachement a = new Attachement();
                    a.setUrl("");
                    imageList.add(a);*/
                    ob1.put("imageList",new ArrayList<Attachement>());
                    ob1.put("commentCount",0);
                    ob1.put("videoList",new ArrayList<VideoDTO>());
                    ob1.put("voiceList",new ArrayList<Attachement>());
                    ob1.put("attachements",new ArrayList<Attachement>());
                    ob1.put("isRead",0);
                    ob1.put("totalReadCount", 0);
                    ob1.put("readCount", 0);
                    ob1.put("unReadCount",0);
                    ob1.put("timeExpression","");
                    ob1.put("isOwner",true);
                    list.add(ob1);

                }else{

                }
            }
        }
        if(ob5.size()!=0){
            list.add(ob5);
        }
        Map<String,Object> map = new HashMap<String, Object>();
        List<Map<String,Object>> list2 = new ArrayList<Map<String, Object>>();
        if(list.size()>0){
            for(String str : stringList){
                for(Map<String,Object> map4 : list){
                    String string = (String)map4.get("id");
                    if(str.equals(string)){
                        list2.add(map4);
                    }
                }
            }
        }
        map.put("count",count);
        map.put("list",list2);
        map.put("hotList",superTopicDTOs);
        return map;
    }
    public Map<String,Object> getRoleList(String userName,int page,int pageSize,ObjectId userId){
        Map<String,Object> map = new HashMap<String, Object>();
        List<ObjectId> oids = new ArrayList<ObjectId>();
        //appNoticeDao
        List<Map<String,Object>> list = new ArrayList<Map<String, Object>>();
        TeacherApproveEntry teacherApproveEntry = teacherApproveDao.getEntry(userId);
        if(teacherApproveEntry!=null && teacherApproveEntry.getType()==2){//认证大V
            // dlist.add(new ObjectId(TEACHERCOMMUNIY));
            oids.add(new ObjectId(TEACHERCOMMUNIY));
        }else{
            // dlist.add(new ObjectId(PARENTCOMMUNIY));
            oids.add(new ObjectId(PARENTCOMMUNIY));
        }
        /*oids.add(new ObjectId(TEACHERCOMMUNIY));
        oids.add(new ObjectId(PARENTCOMMUNIY));
        oids.add(new ObjectId(STUDENTCOMMUNIY));*/
        List<AppNoticeEntry> entries = appNoticeDao.getRoleList(oids, page, pageSize, userName);
        int count = appNoticeDao.countRoleList(oids,userName);
        for(AppNoticeEntry entry : entries){
            AppNoticeDTO dto8 = new AppNoticeDTO(entry);
            Map<String,Object> ob1 = new HashMap<String, Object>();
            ob1.put("tag", CommunityType.newSystem.getDes());
            ob1.put("cardType",4);
            ob1.put("groupName",dto8.getGroupName());
            ob1.put("id",dto8.getId());
            ob1.put("userName",entry.getUserName());
            ob1.put("subject",dto8.getSubject());
            ob1.put("avatar",dto8.getAvatar());
            ob1.put("title",dto8.getTitle());
            ob1.put("time",dto8.getTime());
            ob1.put("content",dto8.getContent());
            ob1.put("imageList",dto8.getImageList());
            ob1.put("commentCount",dto8.getCommentCount());
            ob1.put("videoList",dto8.getVideoList());
            ob1.put("voiceList",dto8.getVoiceList());
            ob1.put("attachements",dto8.getAttachements());
            ob1.put("isRead",0);
            if(dto8.getReadList().contains(userId.toString())){
                ob1.put("isRead",1);
            }
            //设置已阅和未阅的人数
            List<ObjectId> reads=entry.getReaList();
            ob1.put("totalReadCount", 0);
            ob1.put("readCount", reads.size());
            ob1.put("unReadCount",0);
            ob1.put("timeExpression",TimeChangeUtils.getChangeTime(entry.getSubmitTime()));
            if(dto8.getUserId().equals(userId.toString())){
                ob1.put("isOwner",true);
            }else{
                ob1.put("isOwner",false);
            }
            list.add(ob1);
        }
        map.put("count",count);
        map.put("list",list);
        return map;

    }
    public void delEntry(ObjectId id){
        indexPageDao.delEntry(id);
    }

    public void addIndexPage(String communityId,String contactId,int type,ObjectId userId){
        //添加临时记录表
        IndexPageDTO dto1 = new IndexPageDTO();
        dto1.setType(type);
        dto1.setCommunityId(communityId);
        dto1.setUserId(userId.toString());
        dto1.setContactId(contactId);
        IndexPageEntry entry = dto1.buildAddEntry();
        indexPageDao.addEntry(entry);
    }

    /**
     * 添加系统消息
     */
    public void addSystemMessage(){


    }

}
