package com.fulaan.systemMessage.service;

import cn.jpush.api.push.model.audience.Audience;
import com.db.backstage.PushMessageDao;
import com.db.backstage.SystemMessageDao;
import com.db.backstage.TeacherApproveDao;
import com.db.controlphone.ControlSimpleDao;
import com.db.excellentCourses.ClassOrderDao;
import com.db.excellentCourses.ExcellentCoursesDao;
import com.db.excellentCourses.HourClassDao;
import com.db.fcommunity.CommunityDao;
import com.db.fcommunity.CommunityDetailDao;
import com.db.fcommunity.MemberDao;
import com.db.indexPage.IndexPageDao;
import com.db.operation.AppNewOperationDao;
import com.db.operation.AppNoticeDao;
import com.db.user.NewVersionBindRelationDao;
import com.db.user.UserDao;
import com.db.wrongquestion.SubjectClassDao;
import com.easemob.server.comm.constant.MsgType;
import com.fulaan.base.BaseService;
import com.fulaan.fgroup.service.EmService;
import com.fulaan.indexpage.dto.IndexPageDTO;
import com.fulaan.indexpage.dto.SystemMessageDTO;
import com.fulaan.operation.dto.AppCommentDTO;
import com.fulaan.operation.dto.AppNoticeDTO;
import com.fulaan.picturetext.runnable.PictureRunNable;
import com.fulaan.pojo.Attachement;
import com.fulaan.systemMessage.dto.AppNewOperationDTO;
import com.fulaan.systemMessage.dto.SimpleUserDTO;
import com.fulaan.user.service.UserService;
import com.fulaan.utils.JPushUtils;
import com.pojo.backstage.PictureType;
import com.pojo.backstage.PushMessageEntry;
import com.pojo.controlphone.ControlSimpleEntry;
import com.pojo.excellentCourses.ClassOrderEntry;
import com.pojo.excellentCourses.ExcellentCoursesEntry;
import com.pojo.excellentCourses.HourClassEntry;
import com.pojo.fcommunity.AttachmentEntry;
import com.pojo.fcommunity.CommunityDetailEntry;
import com.pojo.fcommunity.VideoEntry;
import com.pojo.indexPage.IndexPageEntry;
import com.pojo.newVersionGrade.CommunityType;
import com.pojo.operation.AppNewOperationEntry;
import com.pojo.user.NewVersionBindRelationEntry;
import com.pojo.user.UserDetailInfoDTO;
import com.pojo.user.UserEntry;
import com.pojo.wrongquestion.SubjectClassEntry;
import com.sys.constants.Constant;
import com.sys.utils.AvatarUtils;
import com.sys.utils.DateTimeUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.util.*;

/**
 * Created by James on 2018-05-22.
 */
@Service
public class SystemMessageService extends BaseService {
    //老师社群
   /* private static final String TEACHERCOMMUNIY = "5ae993953d4df93f01b11a36";
    private static final String TEACHERGROUP = "5ae993963d4df93f01b11a38";*/
    //线上
    private static final String TEACHERCOMMUNIY = "5ae993953d4df93f01b11a36";
    private static final String TEACHERGROUP = "5ae993963d4df93f01b11a38";
    //家长社群
   /* private static final String PARENTCOMMUNIY = "5acecca9bf2e792210a70583";
    private static final String PARENTGROUP = "5aceccaabf2e792210a70585";*/
    //线上
    private static final String PARENTCOMMUNIY = "5b04d9f53d4df9273f5c775a";
    private static final String PARENTGROUP = "5b04d9f53d4df9273f5c775c";
    //学生社群
   /* private static final String STUDENTCOMMUNIY = "5abaf547bf2e791a5457a584";
    private static final String STUDENTGROUP = "5abaf548bf2e791a5457a586";*/
    //线上
    private static final String STUDENTCOMMUNIY = "5b04d9eb3d4df9273f5c7747";
    private static final String STUDENTGROUP = "5b04d9eb3d4df9273f5c7749";

    private AppNoticeDao appNoticeDao = new AppNoticeDao();

    private IndexPageDao indexPageDao = new IndexPageDao();

    private SubjectClassDao subjectClassDao = new SubjectClassDao();

    private MemberDao memberDao = new MemberDao();

    private TeacherApproveDao teacherApproveDao = new TeacherApproveDao();

    private UserDao userDao = new UserDao();

    private CommunityDetailDao communityDetailDao = new CommunityDetailDao();

    private AppNewOperationDao appOperationDao = new AppNewOperationDao();

    private SystemMessageDao systemMessageDao = new SystemMessageDao();
    @Autowired
    private EmService emService;
    @Autowired
    private UserService userService;

    private CommunityDao communityDao = new CommunityDao();

    private NewVersionBindRelationDao newVersionBindRelationDao = new NewVersionBindRelationDao();

    //保存系统消息
    public  void  addEntry(ObjectId userId,AppCommentDTO dto){
        String coStr = dto.getComList();
        String[] strings = coStr.split(",");
        SubjectClassEntry subjectClassEntry = subjectClassDao.getList().get(0);
        String s = "";
        for(String str :strings){
            if(str.equals(TEACHERCOMMUNIY)){
                s = s + "老师"+"/";
            }else if(str.equals(PARENTCOMMUNIY)){
                s = s + "家长"+"/";
            }else if(str.equals(STUDENTCOMMUNIY)){
                s = s + "孩子"+"/";
            }
        }
        if(!s.equals("")){
            s = s.substring(0,s.length()-1);
        }
        for(String str:strings){
            if(str.equals(TEACHERCOMMUNIY)){//发送老师
                AppNoticeDTO appNoticeDTO=new AppNoticeDTO(
                        subjectClassEntry.getID().toString(),
                        s,
                        dto.getTitle(),
                        dto.getDescription(),
                        TEACHERGROUP,
                        str,
                        Constant.ONE,
                        dto.getVideoList(),
                        dto.getImageList(),
                        dto.getAttachements(),
                        dto.getVoiceList(),
                        dto.getSubject(),
                        dto.getSubjectId());
                appNoticeDTO.setUserId(userId.toString());
                ObjectId oid = appNoticeDao.saveAppNoticeEntry(appNoticeDTO.buildEntry());
                //添加临时记录表
                IndexPageDTO dto1 = new IndexPageDTO();
                dto1.setType(CommunityType.appNotice.getType());
                dto1.setUserId(userId.toString());
                dto1.setCommunityId(str);
                dto1.setContactId(oid.toString());
                IndexPageEntry entry = dto1.buildAddEntry();
                indexPageDao.addEntry(entry);
            }else if(str.equals(PARENTCOMMUNIY)){//发送家长
                AppNoticeDTO appNoticeDTO=new AppNoticeDTO(
                        subjectClassEntry.getID().toString(),
                        s,
                        dto.getTitle(),
                        dto.getDescription(),
                        PARENTGROUP,
                        str,
                        Constant.ONE,
                        dto.getVideoList(),
                        dto.getImageList(),
                        dto.getAttachements(),
                        dto.getVoiceList(),
                        dto.getSubject(),
                        dto.getSubjectId());
                appNoticeDTO.setUserId(userId.toString());
                ObjectId oid = appNoticeDao.saveAppNoticeEntry(appNoticeDTO.buildEntry());
                //添加临时记录表
                IndexPageDTO dto1 = new IndexPageDTO();
                dto1.setType(CommunityType.appNotice.getType());
                dto1.setUserId(userId.toString());
                dto1.setCommunityId(str);
                dto1.setContactId(oid.toString());
                IndexPageEntry entry = dto1.buildAddEntry();
                indexPageDao.addEntry(entry);

            }else if(str.equals(STUDENTCOMMUNIY)){//发送学生
                AppNoticeDTO appNoticeDTO=new AppNoticeDTO(
                        subjectClassEntry.getID().toString(),
                        s,
                        dto.getTitle(),
                        dto.getDescription(),
                        STUDENTGROUP,
                        str,
                        Constant.THREE,
                        dto.getVideoList(),
                        dto.getImageList(),
                        dto.getAttachements(),
                        dto.getVoiceList(),
                        dto.getSubject(),
                        dto.getSubjectId());
                appNoticeDTO.setUserId(userId.toString());
                appNoticeDao.saveAppNoticeEntry(appNoticeDTO.buildEntry());
            }
        }
    }

    //发送超级话题内容
    public  void  addHotEntry(ObjectId userId,AppCommentDTO dto){
        String coStr = dto.getComList();
        String[] strings = coStr.split(",");
        String s = "";
        for(String str :strings){
            if(str.equals(TEACHERCOMMUNIY)){
                s = s + "老师"+"/";
            }else if(str.equals(PARENTCOMMUNIY)){
                s = s + "家长"+"/";
            }else if(str.equals(STUDENTCOMMUNIY)){
                s = s + "孩子"+"/";
            }
        }
        if(!s.equals("")){
            s = s.substring(0,s.length()-1);
        }
        List<AttachmentEntry> attachmentEntries = new ArrayList<AttachmentEntry>();
        List<AttachmentEntry> vedios = new ArrayList<AttachmentEntry>();
        List<AttachmentEntry> imageEntries=new ArrayList<AttachmentEntry>();
        List<Attachement> images2 = dto.getImageList();
        if(images2.size()>0){
            for(Attachement image:images2){
                imageEntries.add(new AttachmentEntry(image.getUrl(), image.getFlnm(),
                        System.currentTimeMillis(),
                        userId));
            }
        }

        List<VideoEntry> videoEntries = new ArrayList<VideoEntry>();
        for(String str:strings){
            if(str.equals(TEACHERCOMMUNIY)){//发送老师
                CommunityDetailEntry entry = new CommunityDetailEntry(new ObjectId(TEACHERCOMMUNIY),
                        userId, dto.getTitle(), dto.getDescription(), Constant.THREE,
                        new ArrayList<ObjectId>(), attachmentEntries, vedios, imageEntries,
                        dto.getSubject(),  dto.getSubjectId(), s , null , null, 0,
                        new Date().getTime(), 1, videoEntries
                );//默认滚屏
               communityDetailDao.save(entry);
            }else if(str.equals(PARENTCOMMUNIY)){//发送家长
                CommunityDetailEntry entry = new CommunityDetailEntry(new ObjectId(PARENTCOMMUNIY),
                        userId, dto.getTitle(), dto.getDescription(), Constant.THREE,
                        new ArrayList<ObjectId>(), attachmentEntries, vedios, imageEntries,
                        dto.getSubject(),  dto.getSubjectId(), s , null , null, 0,
                        new Date().getTime(), 1, videoEntries
                );//默认滚屏
                communityDetailDao.save(entry);

            }else if(str.equals(STUDENTCOMMUNIY)){//发送学生
               /* CommunityDetailEntry entry = new CommunityDetailEntry(new ObjectId(STUDENTCOMMUNIY),
                        userId, dto.getTitle(), dto.getDescription(), Constant.THREE,
                        new ArrayList<ObjectId>(), attachmentEntries, vedios, imageEntries,
                        dto.getSubject(),  dto.getSubjectId(), s , null , null, 0,
                        new Date().getTime(), 1, videoEntries
                );
                communityDetailDao.save(entry);*/
            }
        }
    }



    public List<SimpleUserDTO> getBigList(String communityIds,ObjectId userId){
        List<SimpleUserDTO> dtos = new ArrayList<SimpleUserDTO>();
        List<ObjectId> oids = new ArrayList<ObjectId>();
        if(communityIds!=null && !communityIds.equals("")){
             String[] strings = communityIds.split(",");
             for(String s : strings){
                 oids.add(new ObjectId(s));
             }
        }else{
            return dtos;
        }

        if(oids.size()>0){
            List<ObjectId> memberList = memberDao.getAllGroupIdsMembers(oids);
            List<ObjectId> objectIdList = teacherApproveDao.selectMap(memberList);
            //objectIdList.remove(userId);
            if(objectIdList.size()>0){
                List<UserEntry> userEntries = userDao.getUserEntryList(objectIdList, Constant.FIELDS);
                for(UserEntry userEntry:userEntries){
                    SimpleUserDTO dto = new SimpleUserDTO();
                    dto.setId(userEntry.getID().toString());
                    String userName = StringUtils.isNotBlank(userEntry.getNickName())?userEntry.getNickName():userEntry.getUserName();
                    dto.setName(userName);
                    dto.setAvatar(AvatarUtils.getAvatar(userEntry.getAvatar(),userEntry.getRole(),userEntry.getSex()));
                    dto.setJiaId(userEntry.getGenerateUserCode());
                    if(!userId.equals(userEntry.getID())){
                        dtos.add(dto);
                    }
                }
            }
        }
        return dtos;
    }
    public List<SimpleUserDTO> getBigList2(String communityIds,ObjectId userId){
        List<SimpleUserDTO> dtos = new ArrayList<SimpleUserDTO>();
        List<ObjectId> oids = new ArrayList<ObjectId>();
        if(communityIds!=null && !communityIds.equals("")){
             String[] strings = communityIds.split(",");
             for(String s : strings){
                 oids.add(new ObjectId(s));
             }
        }else{
            return dtos;
        }
        List<ObjectId> oids1 = communityDao.getGroupIdsByCommunityIds(oids);
        if(oids1.size()>0){
            List<ObjectId> memberList = memberDao.getAllGroupIdsMembers(oids1);
            List<ObjectId> objectIdList = teacherApproveDao.selectMap(memberList);
            //objectIdList.remove(userId);
            if(objectIdList.size()>0){
                List<UserEntry> userEntries = userDao.getUserEntryList(objectIdList, Constant.FIELDS);
                for(UserEntry userEntry:userEntries){
                    SimpleUserDTO dto = new SimpleUserDTO();
                    dto.setId(userEntry.getID().toString());
                    String userName = StringUtils.isNotBlank(userEntry.getNickName())?userEntry.getNickName():userEntry.getUserName();
                    dto.setName(userName);
                    dto.setAvatar(AvatarUtils.getAvatar(userEntry.getAvatar(),userEntry.getRole(),userEntry.getSex()));
                    dto.setJiaId(userEntry.getGenerateUserCode());
                    dtos.add(dto);

                }
            }
        }
        return dtos;
    }

    public void sendText(String name,String avatar,ObjectId userId,String userIds,String message){
        Map<String, String> ext = new HashMap<String, String>();
        String[] strings = userIds.split(",");
        List<String> targets = new ArrayList<String>();
        for(String id : strings){

            targets.add(id);
        }
        if(targets.size()>0){
            Map<String, String> sendMessage = new HashMap<String, String>();
            sendMessage.put("type", MsgType.TEXT);
            sendMessage.put("msg", message);
            ext.put("groupStyle","");
            ext.put("avatar",avatar);
            ext.put("userId",userId.toString());
            ext.put("nickName",name);
            boolean falg = emService.sendTextMessage("users", targets, userId.toString(), ext, sendMessage);
            System.out.println(falg);
        }
    }


    /**
     * 根据作业id查找当前评论列表
     */
    public Map<String,Object> getOperationList(ObjectId id,int role,ObjectId userId,int page,int pageSize){
        Map<String,Object> map2 = new HashMap<String, Object>();
        CommunityDetailEntry entry2 = communityDetailDao.findByObjectId(id);
        if(entry2==null){
            return map2;
        }
        //关联H5查询
        List<ObjectId> ids = communityDetailDao.getURLListByUserId(entry2.getCommunityContent(),entry2.getCommunityTitle());
        //添加一级评论
        // AppCommentEntry entry = appCommentDao.getEntry(id);
        List<AppNewOperationEntry>  entries= appOperationDao.getEntryListByParentId(ids,role,page,pageSize);
        //添加二级评论
        List<ObjectId> plist = new ArrayList<ObjectId>();
        if(entries != null && entries.size()>0){
            for(AppNewOperationEntry entry1 : entries){
                plist.add(entry1.getID());
            }
        }
        List<AppNewOperationEntry> entries2= appOperationDao.getSecondList(plist);
        entries.addAll(entries2);
        //取图和姓名
        List<AppNewOperationDTO> dtos = new ArrayList<AppNewOperationDTO>();
        List<String> uids = new ArrayList<String>();
        if(entries != null && entries.size()>0){
            for(AppNewOperationEntry en : entries){
                AppNewOperationDTO dto = new AppNewOperationDTO(en);
                dto.setZanCount(en.getZanCount());
                dto.setIsZan(0);
                if (userId.toString().equals(en.getUserId().toString())) {
                    //可以删除
                    dto.setOperation(1);
                } else {
                    //不能删除
                    dto.setOperation(0);
                }
                if(en.getZanList().contains(userId)){
                    dto.setIsZan(1);
                }
                uids.add(dto.getUserId());
                if(dto.getBackId() != null && dto.getBackId() != ""){
                    uids.add(dto.getBackId());
                }
                dto.setRole(2);
               if(userId.equals(en.getUserId())){
                   dto.setRole(1);
               }
                dtos.add(dto);
            }
        }
        List<UserDetailInfoDTO> udtos = userService.findUserInfoByUserIds(uids);
        Map<String,UserDetailInfoDTO> map = new HashMap<String, UserDetailInfoDTO>();
        if(udtos != null && udtos.size()>0){
            for(UserDetailInfoDTO dto4 : udtos){
                map.put(dto4.getId(),dto4);
            }
        }
        for(AppNewOperationDTO dto5 : dtos){
            UserDetailInfoDTO dto9 = map.get(dto5.getUserId());
            if(dto9 != null) {
                String name = cn.jiguang.commom.utils.StringUtils.isNotEmpty(dto9.getNickName()) ? dto9.getNickName() : dto9.getUserName();
                dto5.setUserName(name);
                dto5.setUserUrl(dto9.getImgUrl());
            }
            if(dto5.getBackId() != null && dto5.getBackId() != "" && map.get(dto5.getBackId())!= null){
                UserDetailInfoDTO dto10 = map.get(dto5.getBackId());
                String name2 = cn.jiguang.commom.utils.StringUtils.isNotEmpty(dto10.getNickName()) ? dto10.getNickName() : dto10.getUserName();
                dto5.setBackName(name2);
            }
        }
        List<AppNewOperationDTO> olist = new ArrayList<AppNewOperationDTO>();
        for(AppNewOperationDTO dto6 : dtos){
            if(dto6.getLevel()==1){
                List<AppNewOperationDTO> dtoList = new ArrayList<AppNewOperationDTO>();
                for(AppNewOperationDTO dto7 : dtos){
                    if(dto7.getLevel()==2 && dto6.getId().equals(dto7.getParentId())){
                        dtoList.add(dto7);
                    }
                }
                dto6.setAlist(dtoList);
                olist.add(dto6);
            }
        }
        int count = appOperationDao.getEntryListByParentIdNum(ids,role);
        //分页评论列表
        map2.put("list",olist);
        map2.put("count",count);
        return map2;
    }

    /**
     * 根据作业id查找当前评论列表
     */
    public Map<String,Object> getAllOperationList(ObjectId id,int role,ObjectId userId,int page,int pageSize){
        Map<String,Object> map2 = new HashMap<String, Object>();
        CommunityDetailEntry entry2 = communityDetailDao.findByObjectId(id);
        if(entry2==null){
            return map2;
        }
        //关联H5查询
        List<ObjectId> ids = communityDetailDao.getURLListByUserId(entry2.getCommunityContent(),entry2.getCommunityTitle());
        //添加一级评论
        // AppCommentEntry entry = appCommentDao.getEntry(id);
        List<AppNewOperationEntry>  entries= appOperationDao.getEntryListByParentId(ids, role, page, pageSize);
        //添加二级评论
        List<ObjectId> plist = new ArrayList<ObjectId>();
        if(entries != null && entries.size()>0){
            for(AppNewOperationEntry entry1 : entries){
                plist.add(entry1.getID());
            }
        }
        List<AppNewOperationEntry> entries2= appOperationDao.getSecondList(plist);
        entries.addAll(entries2);
        //取图和姓名
        List<AppNewOperationDTO> dtos = new ArrayList<AppNewOperationDTO>();
        List<String> uids = new ArrayList<String>();
        if(entries != null && entries.size()>0){
            for(AppNewOperationEntry en : entries){
                AppNewOperationDTO dto = new AppNewOperationDTO(en);
                dto.setZanCount(en.getZanCount());
                dto.setIsZan(0);
                uids.add(dto.getUserId());
                if(dto.getBackId() != null && dto.getBackId() != ""){
                    uids.add(dto.getBackId());
                }
                dto.setRole(2);
                if(userId.equals(en.getUserId())){
                    dto.setRole(1);
                }
                dtos.add(dto);
            }
        }
        List<UserDetailInfoDTO> udtos = userService.findUserInfoByUserIds(uids);
        Map<String,UserDetailInfoDTO> map = new HashMap<String, UserDetailInfoDTO>();
        if(udtos != null && udtos.size()>0){
            for(UserDetailInfoDTO dto4 : udtos){
                map.put(dto4.getId(),dto4);
            }
        }
        for(AppNewOperationDTO dto5 : dtos){
            UserDetailInfoDTO dto9 = map.get(dto5.getUserId());
            if(dto9 != null) {
                String name = cn.jiguang.commom.utils.StringUtils.isNotEmpty(dto9.getNickName()) ? dto9.getNickName() : dto9.getUserName();
                dto5.setUserName(name);
                dto5.setUserUrl(dto9.getImgUrl());
            }
            if(dto5.getBackId() != null && dto5.getBackId() != "" && map.get(dto5.getBackId())!= null){
                UserDetailInfoDTO dto10 = map.get(dto5.getBackId());
                String name2 = cn.jiguang.commom.utils.StringUtils.isNotEmpty(dto10.getNickName()) ? dto10.getNickName() : dto10.getUserName();
                dto5.setBackName(name2);
            }
        }
        List<AppNewOperationDTO> olist = new ArrayList<AppNewOperationDTO>();
        for(AppNewOperationDTO dto6 : dtos){
            if(dto6.getLevel()==1){
                List<AppNewOperationDTO> dtoList = new ArrayList<AppNewOperationDTO>();
                for(AppNewOperationDTO dto7 : dtos){
                    if(dto7.getLevel()==2 && dto6.getId().equals(dto7.getParentId())){
                        dtoList.add(dto7);
                    }
                }
                dto6.setAlist(dtoList);
                olist.add(dto6);
            }
        }
        int count = appOperationDao.getEntryListByParentIdNum(ids, role);
        //分页评论列表
        map2.put("list",olist);
        map2.put("count",count);
        return map2;
    }

    /**
     * 发布二级评论
     * @return
     */
    public String addSecondOperation(AppNewOperationDTO dto){
        AppNewOperationEntry en = dto.buildAddEntry();
        //获得当前时间
        long current=System.currentTimeMillis();
        en.setDateTime(current);
        String id = appOperationDao.addEntry(en);
        return id;
    }


    public Map<String,Object> getMyOperationList(ObjectId id,int role,ObjectId userId){
        Map<String,Object> map2 = new HashMap<String, Object>();
        CommunityDetailEntry entry2 = communityDetailDao.findByObjectId(id);
        if(entry2==null){
            return map2;
        }
        //添加一级评论
        // AppCommentEntry entry = appCommentDao.getEntry(id);
        List<AppNewOperationEntry>  entries= appOperationDao.getEntryListByParentId2(id,userId, 1, 20);
        //添加二级评论
        List<ObjectId> plist = new ArrayList<ObjectId>();
        if(entries != null && entries.size()>0){
            for(AppNewOperationEntry entry1 : entries){
                plist.add(entry1.getID());
            }
        }
        List<AppNewOperationEntry> entries2= appOperationDao.getSecondList(plist);
        entries.addAll(entries2);
        //取图和姓名
        List<AppNewOperationDTO> dtos = new ArrayList<AppNewOperationDTO>();
        List<String> uids = new ArrayList<String>();
        if(entries != null && entries.size()>0){
            for(AppNewOperationEntry en : entries){
                AppNewOperationDTO dto = new AppNewOperationDTO(en);
                dto.setZanCount(en.getZanCount());
                dto.setIsZan(0);
                if(en.getZanList().contains(userId)){
                    dto.setIsZan(1);
                }
                uids.add(dto.getUserId());
                if(dto.getBackId() != null && dto.getBackId() != ""){
                    uids.add(dto.getBackId());
                }
                dto.setRole(1);
                // if(userId.equals())
                dtos.add(dto);
            }
        }
        List<UserDetailInfoDTO> udtos = userService.findUserInfoByUserIds(uids);
        Map<String,UserDetailInfoDTO> map = new HashMap<String, UserDetailInfoDTO>();
        if(udtos != null && udtos.size()>0){
            for(UserDetailInfoDTO dto4 : udtos){
                map.put(dto4.getId(),dto4);
            }
        }
        for(AppNewOperationDTO dto5 : dtos){
            UserDetailInfoDTO dto9 = map.get(dto5.getUserId());
            if(dto9 != null) {
                String name = cn.jiguang.commom.utils.StringUtils.isNotEmpty(dto9.getNickName()) ? dto9.getNickName() : dto9.getUserName();
                dto5.setUserName(name);
                dto5.setUserUrl(dto9.getImgUrl());
            }
            if(dto5.getBackId() != null && dto5.getBackId() != "" && map.get(dto5.getBackId())!= null){
                UserDetailInfoDTO dto10 = map.get(dto5.getBackId());
                String name2 = cn.jiguang.commom.utils.StringUtils.isNotEmpty(dto10.getNickName()) ? dto10.getNickName() : dto10.getUserName();
                dto5.setBackName(name2);
            }
        }
        List<AppNewOperationDTO> olist = new ArrayList<AppNewOperationDTO>();
        for(AppNewOperationDTO dto6 : dtos){
            if(dto6.getLevel()==1){
                List<AppNewOperationDTO> dtoList = new ArrayList<AppNewOperationDTO>();
                for(AppNewOperationDTO dto7 : dtos){
                    if(dto7.getLevel()==2 && dto6.getId().equals(dto7.getParentId())){
                        dtoList.add(dto7);
                    }
                }
                dto6.setAlist(dtoList);
                olist.add(dto6);
            }
        }
        //分页评论列表
        map2.put("list",olist);
        return map2;
    }
    /**
     *删除评论
     *
     */
    public void delAppOperationEntry(ObjectId pingId){
        //删除评论
        appOperationDao.delAppOperationEntry(pingId);
    }


    /**
     * 发布一级评论
     * @return
     */
    public void addOperationEntry(AppNewOperationDTO dto){
        AppNewOperationEntry en = dto.buildAddEntry();
        //获得当前时间
        long current=System.currentTimeMillis();
        en.setDateTime(current);
        String id = appOperationDao.addEntry(en);
        if(dto.getType()==1){
            //图片检测
            PictureRunNable.send(id, dto.getUserId(), PictureType.commentImage.getType(), 1, dto.getFileUrl());
        }
    }

    public void updateZan(ObjectId id,ObjectId userId,int zan){
        AppNewOperationEntry appNewOperationEntry = appOperationDao.getEntry(id);
        if(appNewOperationEntry!=null){
            List<ObjectId> zanList = appNewOperationEntry.getZanList();
            if(zan==0 && zanList.contains(userId)){//取消赞
                zanList.remove(userId);
                appNewOperationEntry.setZanList(zanList);
                appNewOperationEntry.setZanCount(appNewOperationEntry.getZanCount() - 1);
                appOperationDao.addEntry(appNewOperationEntry);
            }else if(zan==1 && !zanList.contains(userId)){
                zanList.add(userId);
                appNewOperationEntry.setZanList(zanList);
                appNewOperationEntry.setZanCount(appNewOperationEntry.getZanCount() + 1);
                appOperationDao.addEntry(appNewOperationEntry);
            }else{

            }
        }
    }

    /**
     * 设为精选留言
     */
    public static void addHotList(CommunityDetailEntry communityDetailEntry,ObjectId userId){
        SystemMessageDTO systemMessageDTO = new SystemMessageDTO();
        systemMessageDTO.setSourceName("复兰教育");
        systemMessageDTO.setName("家校美小助手");
        systemMessageDTO.setSourceId(communityDetailEntry.getID().toString());
        systemMessageDTO.setContent(communityDetailEntry.getCommunityContent());
        systemMessageDTO.setFileUrl("");
        systemMessageDTO.setAvatar("");
        systemMessageDTO.setFileType(0);
        systemMessageDTO.setSourceType(0);
        systemMessageDTO.setTitle(communityDetailEntry.getCommunityTitle());
        systemMessageDTO.setType(3);
        SystemMessageDao systemMessageDao = new SystemMessageDao();
        String id = systemMessageDao.addEntry(systemMessageDTO.buildAddEntry());
        IndexPageDTO dto1 = new IndexPageDTO();
        dto1.setType(CommunityType.system.getType());
        dto1.setUserId(userId.toString());
        dto1.setCommunityId(userId.toString());
        dto1.setContactId(id.toString());
        IndexPageEntry entry = dto1.buildAddEntry();
        IndexPageDao indexPageDao = new IndexPageDao();
        indexPageDao.addEntry(entry);

    }

    /**
     * 滚屏展示
     * @param id
     * @param role
     */
    public void addHotPing(ObjectId id,int role){

        communityDetailDao.updateHotEntry(id,role);

    }

    /**
     * 精选留言
     * @param id
     * @param role
     */
    public void addHotList(ObjectId id,int role){
        AppNewOperationDao appOperationDao = new AppNewOperationDao();
        AppNewOperationEntry appNewOperationEntry = appOperationDao.getEntry(id);
        if(appNewOperationEntry!=null){
            CommunityDetailEntry communityDetailEntry = communityDetailDao.findByObjectId(appNewOperationEntry.getContactId());
            if(communityDetailEntry!=null){
                if(role==2){
                    //发送通知
                    addHotList(communityDetailEntry,appNewOperationEntry.getUserId());
                }
                appOperationDao.updateHotlist(id,role);
            }
        }

    }

    public static void main(String[] args){
        SystemMessageService systemMessageService = new SystemMessageService();
        systemMessageService.addHotList(new ObjectId("5b2387b8bf2e791b383f6dca"), 2);

    }
    /**
     * 发送上课提醒（同步消息提醒）
     */
    public void sendClassNotice(ObjectId userId,int type,String title,String name){
        String description = "";
        if(type==1){//进去
            description  = "您的小孩"+name+"已经进入直播课堂,"+title+"正在上课!";
        }else{
            description  = "直播课程:"+title+"已开始，您的孩子"+name+"还没进入课堂!";
        }
        //添加系统信息
        SystemMessageDTO dto = new SystemMessageDTO();
        dto.setType(4);
        dto.setAvatar("");
        dto.setName(name);
        dto.setFileUrl("");
        dto.setSourceId("");
        dto.setContent(description);
        dto.setFileType(1);
        dto.setSourceName("");
        dto.setSourceType(0);
        dto.setTitle("");
        String id = systemMessageDao.addEntry(dto.buildAddEntry());

        //添加首页记录
        IndexPageDTO dto1 = new IndexPageDTO();
        dto1.setType(CommunityType.system.getType());
        dto1.setUserId(userId.toString());
        dto1.setCommunityId(userId.toString());
        dto1.setContactId(id.toString());
        IndexPageEntry entry = dto1.buildAddEntry();
        indexPageDao.addEntry(entry);
        //sendTestMessage(uid.toString());

        JPushUtils jPushUtils=new JPushUtils();
        Set<String> userIds = new HashSet<String>();
        userIds.add(userId.toString());
        Audience audience = Audience.alias(new ArrayList<String>(userIds));
        jPushUtils.pushRestIosbusywork(audience,"直播课堂通知", new HashMap<String, String>());
        jPushUtils.pushRestAndroidParentBusyWork(audience, description, "", "直播课堂通知", new HashMap<String, String>());

    }

    /**
     * 发送登陆提醒（同步消息提醒）
     */
    public void sendLoginNotice(ObjectId sonId,String name){
        NewVersionBindRelationEntry newVersionBindRelationEntry = newVersionBindRelationDao.getBindEntry(sonId);
        if(newVersionBindRelationEntry==null){
            return;
        }
        ObjectId userId = newVersionBindRelationEntry.getMainUserId();
        String description = "";
        description  = "您的小孩"+name+"已经登录了!";
        //添加系统信息
        SystemMessageDTO dto = new SystemMessageDTO();
        dto.setType(5);
        dto.setAvatar("");
        dto.setName(name);
        dto.setFileUrl("");
        dto.setSourceId("");
        dto.setContent(description);
        dto.setFileType(1);
        dto.setSourceName("");
        dto.setSourceType(0);
        dto.setTitle("");
        String id = systemMessageDao.addEntry(dto.buildAddEntry());

        //添加首页记录
        IndexPageDTO dto1 = new IndexPageDTO();
        dto1.setType(CommunityType.system.getType());
        dto1.setUserId(userId.toString());
        dto1.setCommunityId(userId.toString());
        dto1.setContactId(id.toString());
        IndexPageEntry entry = dto1.buildAddEntry();
        indexPageDao.addEntry(entry);

    }


    /**
     * 发送上课提醒（同步消息提醒）
     */
    public static void sendStaticNotice(ObjectId userId,int type,String title,String name){
        SystemMessageDao systemMessageDao = new SystemMessageDao();
        String description = "";
        if(type==1){//进去
            description  = "您的孩子"+name+"已经进入课堂:"+title;
        }else{
            description  = "请注意，您的孩子"+name+"尚未进入课堂:"+title;
        }
        //添加系统信息
        SystemMessageDTO dto = new SystemMessageDTO();
        dto.setType(4);
        dto.setAvatar("");
        dto.setName(name);
        dto.setFileUrl("");
        dto.setSourceId("");
        dto.setContent(description);
        dto.setFileType(1);
        dto.setSourceName("");
        dto.setSourceType(0);
        dto.setTitle("");
        String id = systemMessageDao.addEntry(dto.buildAddEntry());
        IndexPageDao indexPageDao = new IndexPageDao();
        //添加首页记录
        IndexPageDTO dto1 = new IndexPageDTO();
        dto1.setType(CommunityType.system.getType());
        dto1.setUserId(userId.toString());
        dto1.setCommunityId(userId.toString());
        dto1.setContactId(id.toString());
        IndexPageEntry entry = dto1.buildAddEntry();
        indexPageDao.addEntry(entry);
        //sendTestMessage(uid.toString());

        JPushUtils jPushUtils=new JPushUtils();
        Set<String> userIds = new HashSet<String>();
        userIds.add(userId.toString());
        Audience audience = Audience.alias(new ArrayList<String>(userIds));
        jPushUtils.pushRestIosbusywork(audience,"直播课堂通知", new HashMap<String, String>());
        jPushUtils.pushRestAndroidParentBusyWork(audience, description, "", "直播课堂通知", new HashMap<String, String>());

    }


    public  void getNowClassList(){
        //8分钟要上的课
        HourClassDao hourClassDao = new HourClassDao();
        ExcellentCoursesDao excellentCoursesDao = new ExcellentCoursesDao();
        long startTime = System.currentTimeMillis();
        String str = DateTimeUtils.getLongToStrTimeTwo(startTime).substring(0,11);
        long strNum = DateTimeUtils.getStrToLongTime(str, "yyyy-MM-dd");
        PushMessageDao pushMessageDao = new PushMessageDao();
        long endTime = startTime+10*60*1000;//未来10分钟要上的课
        List<HourClassEntry> hourClassEntries = hourClassDao.getIsNewObjectId(startTime, endTime);
        if(hourClassEntries.size()>0){
            for(HourClassEntry hourClassEntry:hourClassEntries){
                ExcellentCoursesEntry excellentCoursesEntry = excellentCoursesDao.getEntry(hourClassEntry.getParentId());
                if(pushMessageDao.isNotHave(hourClassEntry.getID())){
                    PushMessageEntry pushMessageEntry = new PushMessageEntry(
                        "复兰课堂",
                        new ArrayList<ObjectId>(),
                        hourClassEntry.getID(),
                        "未上课提醒",
                        excellentCoursesEntry.getTitle(),
                        hourClassEntry.getStartTime(),
                        strNum,
                        1,
                        0);
                    pushMessageDao.addEntry(pushMessageEntry);
                }
            }
        }
    }


    public void sendMessage(){
        //接受mqtt消息类   type 1
        ControlSimpleDao controlSimpleDao = new ControlSimpleDao();
        ControlSimpleEntry controlSimpleEntry = controlSimpleDao.getEntryForParent(2);
        String ip = "";
        try{
            InetAddress addr = InetAddress.getLocalHost();
            ip=addr.getHostAddress().toString();
        }catch (Exception e){

        }
        if(controlSimpleEntry!=null){
            long currentTime = System.currentTimeMillis();
            long oldTime = controlSimpleEntry.getTime();
            long newTime = oldTime+ 3*60*1000;//3分钟
            String oldIp = controlSimpleEntry.getIp();
            if(oldTime<currentTime){//最新时间
                if(currentTime<newTime){//正常回调中
                    if(ip.equals(oldIp)){//使用的服务器
                        controlSimpleDao.updateEntry(controlSimpleEntry.getID(),currentTime,ip);//更新时间戳
                    }else{
                        return;
                    }
                }else{//超时回调
                    controlSimpleDao.updateEntry(controlSimpleEntry.getID(),currentTime,ip);//更新时间戳
                }
            }else{//过期不采用
                return;
            }
        }else{
            ControlSimpleEntry controlSimpleEntry1 = new ControlSimpleEntry(ip,1,2);
            controlSimpleDao.addEntry(controlSimpleEntry1);
        }
        PushMessageDao pushMessageDao = new PushMessageDao();
        long current = System.currentTimeMillis();
        String str = DateTimeUtils.getLongToStrTimeTwo(current).substring(0,11);
        long strNum = DateTimeUtils.getStrToLongTime(str, "yyyy-MM-dd");
        List<PushMessageEntry> pushMessageEntries = pushMessageDao.selectList(strNum, 0, current);
        if(pushMessageEntries.size()>0){
            HourClassDao hourClassDao = new HourClassDao();
            ClassOrderDao classOrderDao = new ClassOrderDao();
            UserDao userDao = new UserDao();
            NewVersionBindRelationDao newVersionBindRelationDao = new NewVersionBindRelationDao();
            for(PushMessageEntry pushMessageEntry:pushMessageEntries){
                if(pushMessageEntry.getType()==1){// 1 直播未进入提示
                    HourClassEntry hourClassEntry =   hourClassDao.getEntry(pushMessageEntry.getContactId());
                    if(hourClassEntry!=null){
                        List<ClassOrderEntry> classOrderEntries = classOrderDao.getAllUserList(hourClassEntry.getID());
                        if(classOrderEntries.size()>0){
                            for(ClassOrderEntry classOrderEntry:classOrderEntries ){
                                UserEntry userEntry = userDao.findByUserId(classOrderEntry.getUserId());
                                if(userEntry!=null){
                                    String name = org.apache.commons.lang.StringUtils.isNotEmpty(userEntry.getNickName())?userEntry.getNickName():userEntry.getUserName();
                                    //sendStaticNotice(classOrderEntry.getUserId(),2,pushMessageEntry.getTitle(),name);
                                    NewVersionBindRelationEntry newVersionBindRelationEntry = newVersionBindRelationDao.getBindEntry(classOrderEntry.getUserId());
                                    if(newVersionBindRelationEntry!=null){
                                        sendClassNotice(newVersionBindRelationEntry.getMainUserId(),0,pushMessageEntry.getTitle(),name);
                                    }
                                }
                            }
                        }
                    }

                }
                pushMessageDao.delEntry(pushMessageEntry.getID());
            }
        }
    }
}
