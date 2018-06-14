package com.fulaan.systemMessage.service;

import com.db.backstage.TeacherApproveDao;
import com.db.fcommunity.CommunityDetailDao;
import com.db.fcommunity.MemberDao;
import com.db.indexPage.IndexPageDao;
import com.db.operation.AppNewOperationDao;
import com.db.operation.AppNoticeDao;
import com.db.user.UserDao;
import com.db.wrongquestion.SubjectClassDao;
import com.easemob.server.comm.constant.MsgType;
import com.fulaan.base.BaseService;
import com.fulaan.fgroup.service.EmService;
import com.fulaan.indexpage.dto.IndexPageDTO;
import com.fulaan.operation.dto.AppCommentDTO;
import com.fulaan.operation.dto.AppNoticeDTO;
import com.fulaan.picturetext.runnable.PictureRunNable;
import com.fulaan.pojo.Attachement;
import com.fulaan.systemMessage.dto.AppNewOperationDTO;
import com.fulaan.systemMessage.dto.SimpleUserDTO;
import com.fulaan.user.service.UserService;
import com.pojo.backstage.PictureType;
import com.pojo.fcommunity.AttachmentEntry;
import com.pojo.fcommunity.CommunityDetailEntry;
import com.pojo.fcommunity.VideoEntry;
import com.pojo.indexPage.IndexPageEntry;
import com.pojo.newVersionGrade.CommunityType;
import com.pojo.operation.AppNewOperationEntry;
import com.pojo.user.UserDetailInfoDTO;
import com.pojo.user.UserEntry;
import com.pojo.wrongquestion.SubjectClassEntry;
import com.sys.constants.Constant;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    @Autowired
    private EmService emService;
    @Autowired
    private UserService userService;

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
                        dto.getSubject(),  dto.getSubjectId(), str, null , null, 0,
                        new Date().getTime(), 1, videoEntries
                );
               communityDetailDao.save(entry);
            }else if(str.equals(PARENTCOMMUNIY)){//发送家长
                CommunityDetailEntry entry = new CommunityDetailEntry(new ObjectId(PARENTCOMMUNIY),
                        userId, dto.getTitle(), dto.getDescription(), Constant.THREE,
                        new ArrayList<ObjectId>(), attachmentEntries, vedios, imageEntries,
                        dto.getSubject(),  dto.getSubjectId(), str, null , null, 0,
                        new Date().getTime(), 1, videoEntries
                );
                communityDetailDao.save(entry);

            }else if(str.equals(STUDENTCOMMUNIY)){//发送学生
                CommunityDetailEntry entry = new CommunityDetailEntry(new ObjectId(STUDENTCOMMUNIY),
                        userId, dto.getTitle(), dto.getDescription(), Constant.THREE,
                        new ArrayList<ObjectId>(), attachmentEntries, vedios, imageEntries,
                        dto.getSubject(),  dto.getSubjectId(), str, null , null, 0,
                        new Date().getTime(), 1, videoEntries
                );
                communityDetailDao.save(entry);
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
                    if(!userId.equals(userEntry.getID())){
                        dtos.add(dto);
                    }
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
        //添加一级评论
        // AppCommentEntry entry = appCommentDao.getEntry(id);
        List<AppNewOperationEntry>  entries= appOperationDao.getEntryListByParentId(id,role,page,pageSize);
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
        int count = appOperationDao.getEntryListByParentIdNum(id,role);
        //分页评论列表
        map2.put("list",olist);
        map2.put("count",count);
        return map2;
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
}
