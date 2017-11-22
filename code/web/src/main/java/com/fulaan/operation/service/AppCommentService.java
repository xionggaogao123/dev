package com.fulaan.operation.service;

import cn.jiguang.commom.utils.StringUtils;
import cn.jpush.api.push.model.audience.Audience;
import com.db.fcommunity.CommunityDao;
import com.db.fcommunity.GroupDao;
import com.db.fcommunity.MemberDao;
import com.db.indexPage.IndexPageDao;
import com.db.indexPage.WebHomePageDao;
import com.db.newVersionGrade.NewVersionSubjectDao;
import com.db.operation.AppCommentDao;
import com.db.operation.AppOperationDao;
import com.db.operation.AppRecordDao;
import com.db.operation.AppRecordResultDao;
import com.db.user.NewVersionBindRelationDao;
import com.db.wrongquestion.SubjectClassDao;
import com.fulaan.community.dto.CommunityDTO;
import com.fulaan.instantmessage.service.RedDotService;
import com.fulaan.newVersionBind.service.NewVersionBindService;
import com.fulaan.operation.dto.AppCommentDTO;
import com.fulaan.operation.dto.AppOperationDTO;
import com.fulaan.operation.dto.AppRecordDTO;
import com.fulaan.operation.dto.AppRecordResultDTO;
import com.fulaan.pojo.User;
import com.fulaan.service.CommunityService;
import com.fulaan.user.service.UserService;
import com.fulaan.utils.JPushUtils;
import com.fulaan.wrongquestion.dto.SubjectClassDTO;
import com.mongodb.DBObject;
import com.pojo.fcommunity.MemberEntry;
import com.pojo.indexPage.WebHomePageEntry;
import com.pojo.instantmessage.ApplyTypeEn;
import com.pojo.newVersionGrade.NewVersionSubjectEntry;
import com.pojo.operation.AppCommentEntry;
import com.pojo.operation.AppOperationEntry;
import com.pojo.operation.AppRecordEntry;
import com.pojo.operation.AppRecordResultEntry;
import com.pojo.user.NewVersionBindRelationEntry;
import com.pojo.user.UserDetailInfoDTO;
import com.pojo.user.UserEntry;
import com.pojo.wrongquestion.SubjectClassEntry;
import com.sys.constants.Constant;
import com.sys.utils.AvatarUtils;
import com.sys.utils.DateTimeUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by James on 2017/8/25.
 */
@Service
public class AppCommentService {

    private AppCommentDao appCommentDao = new AppCommentDao();
    private AppOperationDao appOperationDao = new AppOperationDao();
    private AppRecordDao appRecordDao = new AppRecordDao();
    private MemberDao memberDao = new MemberDao();
    private GroupDao groupDao = new GroupDao();
    private CommunityDao communityDao = new CommunityDao();
    private SubjectClassDao subjectClassDao = new SubjectClassDao();
    private IndexPageDao indexPageDao = new IndexPageDao();
    private WebHomePageDao webHomePageDao = new WebHomePageDao();
    private NewVersionSubjectDao newVersionSubjectDao = new NewVersionSubjectDao();
    private NewVersionBindRelationDao newVersionBindRelationDao = new NewVersionBindRelationDao();
    private AppRecordResultDao appRecordResultDao = new AppRecordResultDao();
    @Autowired
    private CommunityService communityService;
    @Autowired
    private UserService userService;
    @Autowired
    private NewVersionBindService newVersionBindService;
    @Autowired
    private RedDotService redDotService;





    /**
     * 发布作业
     * @return
     */
    public String addCommentEntry(AppCommentDTO dto,String comList)throws Exception{
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH)+ 1;
        dto.setMonth(month);
        long zero = 0l;
        if(dto.getDateTime() ==null || dto.getDateTime().equals("")){
            //获得当前时间
            long current=System.currentTimeMillis();
            //获得时间批次
            zero = current/(1000*3600*24)*(1000*3600*24)- TimeZone.getDefault().getRawOffset();//今天零点零分零秒的毫秒数
        }else{
            zero = DateTimeUtils.getStrToLongTime(dto.getDateTime(), "yyyy-MM-dd HH:mm");
        }
        dto.setDateTime("");

        AppCommentEntry en = dto.buildAddEntry();
        en.setDateTime(zero);
        List<CommunityDTO> communityDTOList = communityService.getCommunitys(new ObjectId(dto.getAdminId()), 1, 100);
        List<CommunityDTO> sendList = new ArrayList<CommunityDTO>();
        String[]  strar = comList.split(",");
        if(comList != null && communityDTOList != null){
            for(CommunityDTO dto2 : communityDTOList){
                for(String str : strar){
                    if(str != null && str.equals(dto2.getId())){
                        sendList.add(dto2);
                    }
                }
            }
        }
        JPushUtils jPushUtils=new JPushUtils();

        for(CommunityDTO dto3 : sendList){
            en.setID(null);
            en.setRecipientId(new ObjectId(dto3.getId()));
            en.setRecipientName(dto3.getName());
            String oid = appCommentDao.addEntry(en);
            List<ObjectId> objectIdList =new ArrayList<ObjectId>();

            //添加临时记录表//暂时不显示
          /*  if(dto.getStatus()==0){
                IndexPageDTO dto1 = new IndexPageDTO();
                dto1.setType(CommunityType.appComment.getType());
                dto1.setCommunityId(dto3.getId());
                dto1.setContactId(oid);
                IndexPageEntry entry = dto1.buildAddEntry();
                indexPageDao.addEntry(entry);
                objectIdList.add(new ObjectId(dto3.getId()));
            }*/
            int status=Constant.ZERO;
            if(dto.getStatus()==0){
                status=Constant.TWO;
            }
            WebHomePageEntry pageEntry=new WebHomePageEntry(Constant.ONE, new ObjectId(dto.getAdminId()),
                    new ObjectId(dto3.getId()),
                    new ObjectId(oid),
                    new ObjectId(dto.getSubjectId()),
                    null, status);
            webHomePageDao.saveWebHomeEntry(pageEntry);

            objectIdList.add(new ObjectId(dto3.getId()));
            redDotService.addEntryList(objectIdList,new ObjectId(dto.getAdminId()), ApplyTypeEn.operation.getType(),4);
        }
        try {
            for (CommunityDTO dto3 : sendList) {
                ObjectId groupId = communityDao.getGroupId(new ObjectId(dto3.getId()));
                if (null != groupId && dto.getStatus() == 0) {
                    List<MemberEntry> memberEntries = memberDao.getAllMembers(groupId);
                    Set<String> userIds = new HashSet<String>();
                    for (MemberEntry memberEntry : memberEntries) {
                        userIds.add(memberEntry.getUserId().toString());
                    }
                    Audience audience = Audience.alias(new ArrayList<String>(userIds));
                    jPushUtils.pushRestIosbusywork(audience, dto.getTitle(), new HashMap<String, String>());
                    jPushUtils.pushRestAndroidParentBusyWork(audience, dto.getDescription(), "", dto.getTitle(), new HashMap<String, String>());
                    List<String> bindUserIds = newVersionBindService.getStudentIdListByCommunityId(new ObjectId(dto3.getId()));
                    if (bindUserIds.size() > 0) {
                        Audience studentAudience = Audience.alias(new ArrayList<String>(bindUserIds));
                        jPushUtils.pushRestAndroidStudentBusyWork(studentAudience, dto.getDescription(), "", dto.getTitle(),
                                new HashMap<String, String>());
                    }
                }
            }
        }catch (Exception e){
            throw new Exception("推送失败");
        }
        return "成功导入";
    }

    /**
     * 查询当前老师今天发布的作业
     *
     */
    public List<AppCommentDTO> selectListByTeacherId(ObjectId userId){
        //获得当前时间
        long current=System.currentTimeMillis();
        //获得时间批次
        long zero=current/(1000*3600*24)*(1000*3600*24)- TimeZone.getDefault().getRawOffset();//今天零点零分零秒的毫秒数
        List<AppCommentEntry> entries = appCommentDao.getEntryListByUserId(userId,zero);
        List<AppCommentDTO> dtos = new ArrayList<AppCommentDTO>();
        if(entries.size()>0){
            for(AppCommentEntry en : entries){
                AppCommentDTO dto = new AppCommentDTO(en);
                int num = appOperationDao.getEntryCount(en.getID());
                dto.setNumber(num);
                dtos.add(dto);
            }
        }
        return dtos;
    }
    /**
     * 查找当前家长收到的作业
     *
     */
    public List<AppCommentDTO> selectListFromParent(ObjectId userId){
        //获得当前时间
        long current=System.currentTimeMillis();
        //获得时间批次
        long zero=current/(1000*3600*24)*(1000*3600*24)- TimeZone.getDefault().getRawOffset();//今天零点零分零秒的毫秒数
       // List<AppCommentEntry> ids = appCommentDao.getEntryListByUserId(userId,zero);

        //todo  查找出当前家长的孩子所收到的所有作业
        List<NewVersionBindRelationEntry> nlist = newVersionBindRelationDao.getEntriesByMainUserId(userId);
        List<ObjectId> olist = new ArrayList<ObjectId>();
        if(nlist.size()>0){
            for(NewVersionBindRelationEntry en : nlist){
                olist.add(en.getUserId());
            }
        }
        ;
        List<AppCommentDTO> dtos = new ArrayList<AppCommentDTO>();
       /* if(entries.size()>0){
            for(AppCommentEntry en : entries){
                AppCommentDTO dto = new AppCommentDTO(en);
                int num = appOperationDao.getEntryCount(en.getID());
                dto.setNumber(num);
            }
        }*/
        return dtos;
    }
    /**
     * 查询当前作业提交名单
     */
    public Map<String,Object> selectStudentLoad(ObjectId id,int page,int pageSize){
        Map<String,Object> map = new HashMap<String, Object>();
        //查询已提交
        List<AppOperationEntry> entries =appOperationDao.getEntryListByParentId(id,3,page,pageSize);
        int num = appOperationDao.countStudentLoadTimes(id, 3);
        //获得该作业的所有发放学生
        //获得作业
        AppCommentEntry aen = appCommentDao.getEntry(id);
        //所有学生
        List<String> objectIdList = newVersionBindService.getStudentIdListByCommunityId(aen.getRecipientId());
        List<AppOperationDTO> dtos = new ArrayList<AppOperationDTO>();
        List<String> uids = new ArrayList<String>();
        uids.add(aen.getAdminId().toString());
        List<ObjectId> plist = new ArrayList<ObjectId>();
        if(entries != null && entries.size()>0){
            for(AppOperationEntry en : entries){
                AppOperationDTO dto = new AppOperationDTO(en);
                objectIdList.remove(dto.getUserId());
                uids.add(dto.getUserId());
                plist.add(en.getID());
                dtos.add(dto);
            }
        }
        //已提交
        //添加二级评论
        List<AppOperationEntry> entries2= appOperationDao.getSecondList(plist);
        List<AppOperationDTO> dtos2 = new ArrayList<AppOperationDTO>();
        if(entries2 != null && entries2.size()>0){
            for(AppOperationEntry en2 : entries2){
                AppOperationDTO dto = new AppOperationDTO(en2);
                uids.add(dto.getUserId());
                if(dto.getBackId() != null && dto.getBackId() != ""){
                    uids.add(dto.getBackId());
                }
                dtos2.add(dto);
            }
        }
        List<UserDetailInfoDTO> udtos2 = userService.findUserInfoByUserIds(uids);
        Map<String,UserDetailInfoDTO> map3 = new HashMap<String, UserDetailInfoDTO>();
        if(udtos2 != null && udtos2.size()>0){
            for(UserDetailInfoDTO dto4 : udtos2){
                map3.put(dto4.getId(),dto4);
            }
        }
        for(AppOperationDTO dto5 : dtos){
            UserDetailInfoDTO dto9 = map3.get(dto5.getUserId());
            if(dto9 != null){
                String name = StringUtils.isNotEmpty(dto9.getNickName())?dto9.getNickName():dto9.getUserName();
                dto5.setUserName(name);
                dto5.setUserUrl(dto9.getImgUrl());
            }
        }
        for(AppOperationDTO dto6 : dtos2){
            UserDetailInfoDTO dto10 = map3.get(dto6.getUserId());
            if(dto10 != null){
                String name = StringUtils.isNotEmpty(dto10.getNickName())?dto10.getNickName():dto10.getUserName();
                dto6.setUserName(name);
                dto6.setUserUrl(dto10.getImgUrl());
            }
            if(dto6.getBackId() != null && dto6.getBackId() != "" && map3.get(dto6.getBackId())!= null){
                UserDetailInfoDTO dto11 = map3.get(dto6.getBackId());
                String name2 = StringUtils.isNotEmpty(dto11.getNickName())?dto11.getNickName():dto11.getUserName();
                dto6.setBackName(name2);
            }
        }
        for(AppOperationDTO dto6 : dtos){
            if(dto6.getLevel()==1){
                List<AppOperationDTO> dtoList = new ArrayList<AppOperationDTO>();
                for(AppOperationDTO dto7 : dtos2){
                    if(dto7.getLevel()==2 && dto6.getId().equals(dto7.getParentId())){
                        dtoList.add(dto7);
                    }
                }
                dto6.setAlist(dtoList);
            }
        }
        //未提交
        List<UserDetailInfoDTO> unList = userService.findUserInfoByUserIds(objectIdList);
        List<User> ulsit = new ArrayList<User>();
        for(UserDetailInfoDTO userDetailInfoDTO : unList){
            String name3 = StringUtils.isNotEmpty(userDetailInfoDTO.getNickName())?userDetailInfoDTO.getNickName():userDetailInfoDTO.getUserName();
            User user = new User();
            user.setAvator(userDetailInfoDTO.getImgUrl());
            user.setSex(userDetailInfoDTO.getSex());
            user.setNickName(name3);
            user.setUserName(name3);
            user.setUserId(userDetailInfoDTO.getId());
            ulsit.add(user);
        }

        //已提交
        map.put("loadList",dtos);
        //已提交人数
        map.put("loadNumber",num);
        //未提交
        map.put("unLoadList",ulsit);
        //未提交人数
        map.put("unLoadNumber",ulsit.size());
        AppCommentDTO dtoa = new AppCommentDTO(aen);
        UserDetailInfoDTO dto12 = map3.get(dtoa.getAdminId());
        String name = StringUtils.isNotEmpty(dto12.getNickName())?dto12.getNickName():dto12.getUserName();
        dtoa.setAdminName(name);
        dtoa.setAdminUrl(dto12.getImgUrl());
        map.put("desc",dtoa);
        return map;
    }
public static void main(String[] args){
    int i = 1;
    System.out.print(i);
}
    /**
     * 查询当前作业签到名单
     */
    public Map<String,Object> selectRecordList(ObjectId id){
        Map<String,Object> map = new HashMap<String, Object>();
        //获得作业
        AppCommentEntry aen = appCommentDao.getEntry(id);
        //获得需要签到的id
        List<String> objectIdList = this.getMyRoleList2(aen.getRecipientId());
        List<ObjectId> oblist = new ArrayList<ObjectId>();
        //获得当前时间
        long current=System.currentTimeMillis();
        //获得时间批次
        long zero=current/(1000*3600*24)*(1000*3600*24)- TimeZone.getDefault().getRawOffset();//今天零点零分零秒的毫秒数
        if(objectIdList.size()>0){
            for(String str : objectIdList){
                oblist.add(new ObjectId(str));
            }
        }
        //List<AppRecordEntry> entries = appRecordDao.getEntryListByParentId2(oblist,zero);
        //已签到名单
       // List<AppRecordDTO> dtos = new ArrayList<AppRecordDTO>();
        List<ObjectId> objectIdList1 = new ArrayList<ObjectId>();
        List<AppRecordResultEntry> entries1 = appRecordResultDao.getEntryListByParentId(id);
        List<AppRecordResultDTO> dtoList = new ArrayList<AppRecordResultDTO>();
        if(entries1.size()>0){
            for(AppRecordResultEntry en1 : entries1){
                //已签到的
                objectIdList1.add(en1.getUserId());
                dtoList.add(new AppRecordResultDTO(en1));
            }
        }
        oblist.removeAll(objectIdList1);
        Map<String,AppRecordResultDTO> map1 = new HashMap<String, AppRecordResultDTO>();
        for(AppRecordResultDTO dto1 : dtoList){
            map1.put(dto1.getUserId(),dto1);
        }
        List<User> sign=new ArrayList<User>();
        if(objectIdList1.size()>0){
            saveUser(sign,objectIdList1);
        }
        for(User user : sign){
            user.setTime(map1.get(user.getUserId()).getCreateTime());
        }
        map.put("SignList",sign);
        map.put("SignListNum",sign.size());
        List<User> unSign=new ArrayList<User>();
        if(oblist.size()>0){
            saveUser(unSign,oblist);
        }
        map.put("UnSignList",unSign);
        map.put("UnSignListNum",unSign.size());
        return map;
    }

    public void saveUser(List<User> users,List<ObjectId> userIds){
        Map<ObjectId,UserEntry> userEntryMap=userService.getUserEntryMap(userIds, Constant.FIELDS);
        for(Map.Entry<ObjectId,UserEntry> userEntryEntry:userEntryMap.entrySet()){
            UserEntry userEntry=userEntryEntry.getValue();
            User user=new User(userEntry.getUserName(),
                    userEntry.getNickName(),
                    userEntry.getID().toString(),
                    AvatarUtils.getAvatar(userEntry.getAvatar(), userEntry.getRole(), userEntry.getSex()),
                    userEntry.getSex(),
                    "");
            String name = StringUtils.isNotEmpty(user.getNickName())?user.getNickName():user.getUserName();
            user.setNickName(name);
            user.setUserName(name);
            users.add(user);
        }
    }

    /**
     * 按月查找用户发放作业情况
     */
    public List<String> selectResultList(int month,ObjectId userId){
        List<Integer> ilist = new ArrayList<Integer>();
        ilist.add(month);
        ilist.add(month-1);
        ilist.add(month+1);
        //获得所有社区
        List<CommunityDTO> communityDTOList =communityService.getCommunitys(userId, 1, 100);
        List<ObjectId>  dlist = new ArrayList<ObjectId>();
        if(communityDTOList.size() >0){
            for(CommunityDTO dto : communityDTOList){
                dlist.add(new ObjectId(dto.getId()));
            }
        }
        //发出的
        List<AppCommentEntry> entries = appCommentDao.selectResultList(userId, ilist);
        //收到的
        List<AppCommentEntry> entries2 = appCommentDao.selectDateListMonth(dlist, ilist);
        entries.addAll(entries2);
        //Set<Integer> set=new HashSet<Integer>(list);
        List<String> dtos = new ArrayList<String>();
        if(entries.size()>0){
            for(AppCommentEntry en : entries){
                AppCommentDTO dto = new AppCommentDTO(en);
                dtos.add(dto.getDateTime().substring(0, 10));
            }
        }


        Set<String> set=new HashSet<String>(dtos);
        List<String> dtos2 = new ArrayList<String>();
        dtos2.addAll(set);
        return dtos2;
    }
    /**
     * 按月查找学生收到作业情况
     */
    public List<String> selectStudentResultList(int month,ObjectId userId){
        List<Integer> ilist = new ArrayList<Integer>();
        ilist.add(month);
        ilist.add(month-1);
        ilist.add(month+1);
        List<ObjectId> obList = newVersionBindService.getCommunityIdsByUserId(userId);
        //收到的
        List<AppCommentEntry> entries = appCommentDao.selectDateListMonth(obList, ilist);
        //Set<Integer> set=new HashSet<Integer>(list);
        List<String> dtos = new ArrayList<String>();
        if(entries.size()>0){
            for(AppCommentEntry en : entries){
                AppCommentDTO dto = new AppCommentDTO(en);
                dtos.add(dto.getDateTime().substring(0, 10));
            }
        }


        Set<String> set=new HashSet<String>(dtos);
        List<String> dtos2 = new ArrayList<String>();
        dtos2.addAll(set);
        return dtos2;
    }

    /**
     * 按日查找用户发放作业情况
     */
    public Map<String,Object> selectDateList(long dateTime,ObjectId userId){
        Map<String,Object> map2 = new HashMap<String, Object>();
        //获得当前时间
        long current=System.currentTimeMillis();
        //获得时间批次
        long zero=current/(1000*3600*24)*(1000*3600*24)- TimeZone.getDefault().getRawOffset();//今天零点零分零秒的毫秒数
        //发送的作业
        List<AppCommentEntry> entries = appCommentDao.selectDateList(userId, dateTime);
        if(dateTime==zero){
            //保存的作业
            List<AppCommentEntry> entries1 = appCommentDao.selectWillDateList(userId);
            entries.addAll(entries1);
        }
        List<String> uids = new ArrayList<String>();
        List<AppCommentDTO> dtos = new ArrayList<AppCommentDTO>();
        if(entries.size()>0){
            for(AppCommentEntry en : entries){
                if(en.getDateTime() <= zero && en.getStatus()==1){
                    en.setStatus(0);
                }
                AppCommentDTO dto = new AppCommentDTO(en);
                String ctm = dto.getCreateTime();
                dto.setCreateTime(ctm);
                dto.setType(1);
                uids.add(dto.getAdminId());
                dtos.add(dto);
            }
        }

        //获得所有收到的作业
        //获得绑定关系(是否存在）
        List<NewVersionBindRelationEntry> nlist = newVersionBindRelationDao.getEntriesByMainUserId(userId);
        if(nlist.size() >0){
            map2.put("isShow",1);//签到展示
        }else{
            map2.put("isShow",2);//非家长
        }
        /*if(nlist.size()>0){
            for(NewVersionBindRelationEntry entry : nlist){
                uids.add(entry.getUserId().toString());
            }
        }*/
        //获得所有社区
        List<CommunityDTO> communityDTOList =communityService.getCommunitys(userId, 1, 100);
        List<ObjectId>  dlist = new ArrayList<ObjectId>();
        if(communityDTOList.size() >0){
            for(CommunityDTO dto : communityDTOList){
                dlist.add(new ObjectId(dto.getId()));
            }
        }
        List<AppCommentEntry> entries2 = appCommentDao.selectDateList2(dlist, dateTime);
        List<ObjectId> dto4s = new ArrayList<ObjectId>();
        if(entries2.size()>0){
            for(AppCommentEntry en : entries2){
                AppCommentDTO dto3 = new AppCommentDTO(en);
                if(dto3.getAdminId() != null && dto3.getAdminId().equals(userId.toString())){

                }else{
                    String ctm = dto3.getCreateTime();
                    dto3.setCreateTime(ctm);
                    dto3.setType(2);
                    uids.add(dto3.getAdminId());
                    dtos.add(dto3);
                    dto4s.add(en.getID());
                }
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
                String name = StringUtils.isNotEmpty(dto9.getNickName())?dto9.getNickName():dto9.getUserName();
                dto5.setAdminName(name);
                dto5.setAdminUrl(dto9.getImgUrl());
            }
        }
        List<ObjectId> mlist = this.getMyRoleList(userId);
        if(mlist != null && mlist.size()>0){
            map2.put("isTeacher",1);//是老师
        }else{
            map2.put("isTeacher",2);//非老师
        }
        map2.put("list",dtos);
        Map<String,Object> map3 = this.isSign(userId, dateTime,dto4s);
        map2.put("isload",map3);

        //清除红点
        redDotService.cleanResult(userId,ApplyTypeEn.operation.getType(),dateTime);
        return map2;
    }

    /**
     * 按日查找用户发放作业情况
     */
    public Map<String,Object> selectDatePageList(long dateTime,ObjectId userId,int page,int pageSize,int type){
        List<AppCommentDTO> dtos = new ArrayList<AppCommentDTO>();
        //获得当前时间
        long current=System.currentTimeMillis();
        //获得时间批次
        long zero=current/(1000*3600*24)*(1000*3600*24)- TimeZone.getDefault().getRawOffset();//今天零点零分零秒的毫秒数
        Map<String,Object> map3 = new HashMap<String, Object>();
        List<String> uids = new ArrayList<String>();
        List<ObjectId> dto4s = new ArrayList<ObjectId>();
        int count = 0;
        if(type==1){
            //发送的作业
            List<AppCommentEntry> entries = appCommentDao.selectDateListPage(userId, page, pageSize);
            count = appCommentDao.getNumber(userId);
            if(page==1){
                //保存的作业
                List<AppCommentEntry> entries1 = appCommentDao.selectWillDateList(userId);
                entries.addAll(entries1);
            }
            if(entries.size()>0){
                for(AppCommentEntry en : entries){
                    if(en.getDateTime() <= zero && en.getStatus()==1){
                        en.setStatus(0);
                    }
                    AppCommentDTO dto = new AppCommentDTO(en);
                    String ctm = dto.getCreateTime();
                    dto.setCreateTime(ctm);
                    dto.setType(1);
                    uids.add(dto.getAdminId());
                    dtos.add(dto);
                }
            }
        }else{
            List<CommunityDTO> communityDTOList =communityService.getCommunitys(userId, 1, 100);
            List<ObjectId>  dlist = new ArrayList<ObjectId>();
            if(communityDTOList.size() >0){
                for(CommunityDTO dto : communityDTOList){
                    dlist.add(new ObjectId(dto.getId()));
                }
            }
            List<AppCommentEntry> entries2 = appCommentDao.selectPageDateList2(dlist, userId, page, pageSize);
            count = appCommentDao.getPageNumber(dlist, userId);
            if(entries2.size()>0){
                for(AppCommentEntry en : entries2){
                    AppCommentDTO dto3 = new AppCommentDTO(en);
                    if(dto3.getAdminId() != null && dto3.getAdminId().equals(userId.toString())){

                    }else{
                        String ctm = dto3.getCreateTime();
                        dto3.setCreateTime(ctm);
                        dto3.setType(2);
                        uids.add(dto3.getAdminId());
                        dtos.add(dto3);
                        dto4s.add(en.getID());
                    }
                }
            }
        }
        List<NewVersionBindRelationEntry> nlist = newVersionBindRelationDao.getEntriesByMainUserId(userId);
        if(nlist.size() >0){
            map3.put("isShow",1);//签到展示
        }else{
            map3.put("isShow",2);//非家长
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
                String name = StringUtils.isNotEmpty(dto9.getNickName())?dto9.getNickName():dto9.getUserName();
                dto5.setAdminName(name);
                dto5.setAdminUrl(dto9.getImgUrl());
            }
        }
        List<ObjectId> mlist = this.getMyRoleList(userId);
        if(mlist != null && mlist.size()>0){
            map3.put("isTeacher",1);//是老师
        }else{
            map3.put("isTeacher",2);//非老师
        }
        Map<String,Object> map4 = this.isSign(userId, dateTime,dto4s);
        map3.put("isload",map4);
        map3.put("list",dtos);
        map3.put("count",count);

        //清除红点
        redDotService.cleanResult(userId,ApplyTypeEn.operation.getType(),dateTime);
        return map3;
    }
    /**
     * 学生端查询接受到的作业
     *
     */
    public List<AppCommentDTO> getStuLit(long dateTime,ObjectId studentId){
        List<AppCommentDTO> dtos = new ArrayList<AppCommentDTO>();
        List<String> olist = new ArrayList<String>();
        List<String> uids = new ArrayList<String>();
        List<ObjectId> obList = newVersionBindService.getCommunityIdsByUserId(studentId);
        List<AppCommentEntry> entries2 = appCommentDao.selectDateList2(obList, dateTime);
        //UserDetailInfoDTO studtos = userService.getUserInfoById(studentId.toString());
        uids.add(studentId.toString());
        if(entries2.size()>0){
            for(AppCommentEntry en : entries2){
                AppCommentDTO dto3 = new AppCommentDTO(en);
                if(dto3.getAdminId() != null && olist.contains(dto3.getAdminId())){

                }else{
                    String ctm = dto3.getCreateTime();
                    dto3.setCreateTime(ctm);
                    dto3.setType(2);
                    uids.add(dto3.getAdminId());
                    dtos.add(dto3);
                }
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
                String name = StringUtils.isNotEmpty(dto9.getNickName())?dto9.getNickName():dto9.getUserName();
                dto5.setAdminName(name);
                dto5.setAdminUrl(dto9.getImgUrl());
                dto5.setSendUser(map.get(studentId.toString()).getImgUrl());
            }
        }
        //清除红点
        redDotService.cleanResult(studentId,ApplyTypeEn.operation.getType(),dateTime);
        return dtos;
    }

    public List<AppCommentDTO> getPageStuLit(long dateTime,ObjectId studentId,int page,int pageSize){
        List<AppCommentDTO> dtos = new ArrayList<AppCommentDTO>();
        List<String> olist = new ArrayList<String>();
        List<String> uids = new ArrayList<String>();
        List<ObjectId> obList = newVersionBindService.getCommunityIdsByUserId(studentId);
        List<AppCommentEntry> entries2 = appCommentDao.selectPageDateList2(obList, studentId,page,pageSize);
        //UserDetailInfoDTO studtos = userService.getUserInfoById(studentId.toString());
        uids.add(studentId.toString());
        if(entries2.size()>0){
            for(AppCommentEntry en : entries2){
                AppCommentDTO dto3 = new AppCommentDTO(en);
                if(dto3.getAdminId() != null && olist.contains(dto3.getAdminId())){

                }else{
                    String ctm = dto3.getCreateTime();
                    dto3.setCreateTime(ctm);
                    dto3.setType(2);
                    uids.add(dto3.getAdminId());
                    dtos.add(dto3);
                }
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
                String name = StringUtils.isNotEmpty(dto9.getNickName())?dto9.getNickName():dto9.getUserName();
                dto5.setAdminName(name);
                dto5.setAdminUrl(dto9.getImgUrl());
                dto5.setSendUser(map.get(studentId.toString()).getImgUrl());
            }
        }
        //清除红点
        redDotService.cleanResult(studentId,ApplyTypeEn.operation.getType(),dateTime);
        return dtos;
    }

    /**
     * 根据作业id查找当前评论列表
     */
    public Map<String,Object> getOperationList(ObjectId id,int role,int label,ObjectId userId,int page,int pageSize){
        Map<String,Object> map2 = new HashMap<String, Object>();
        AppCommentEntry entry2 = appCommentDao.getEntry(id);
        UserDetailInfoDTO studtos = userService.getUserInfoById(entry2.getAdminId().toString());
        String name3= StringUtils.isNotEmpty(studtos.getNickName()) ? studtos.getNickName() : studtos.getUserName();
        AppCommentDTO dto2 = new AppCommentDTO(entry2);
        if(entry2.getAdminId()!= null && entry2.getAdminId().equals(userId)){
            dto2.setType(1);
        }else{
            dto2.setType(2);
        }
        dto2.setAdminName(name3);
        dto2.setAdminUrl(studtos.getImgUrl());
         //上面的描述
         map2.put("desc",dto2);
        //添加一级评论
       // AppCommentEntry entry = appCommentDao.getEntry(id);
        List<AppOperationEntry> entries = null;
        if(label==1){
            entries= appOperationDao.getEntryListByParentId(id,role,page,pageSize);
        }else{
            entries= appOperationDao.getEntryListByUserId(userId,role,id,page,pageSize);
        }

        //添加二级评论
        List<ObjectId> plist = new ArrayList<ObjectId>();
        if(entries != null && entries.size()>0){
            for(AppOperationEntry entry1 : entries){
                plist.add(entry1.getID());
            }
        }
        List<AppOperationEntry> entries2= appOperationDao.getSecondList(plist);
        entries.addAll(entries2);


        //取图和姓名
        List<AppOperationDTO> dtos = new ArrayList<AppOperationDTO>();
        List<String> uids = new ArrayList<String>();
        if(entries != null && entries.size()>0){
            for(AppOperationEntry en : entries){
                AppOperationDTO dto = new AppOperationDTO(en);
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
        for(AppOperationDTO dto5 : dtos){
            UserDetailInfoDTO dto9 = map.get(dto5.getUserId());
            if(dto9 != null) {
                String name = StringUtils.isNotEmpty(dto9.getNickName()) ? dto9.getNickName() : dto9.getUserName();
                dto5.setUserName(name);
                dto5.setUserUrl(dto9.getImgUrl());
            }
            if(dto5.getBackId() != null && dto5.getBackId() != "" && map.get(dto5.getBackId())!= null){
                UserDetailInfoDTO dto10 = map.get(dto5.getBackId());
                String name2 = StringUtils.isNotEmpty(dto10.getNickName()) ? dto10.getNickName() : dto10.getUserName();
                dto5.setBackName(name2);
            }
        }
        List<AppOperationDTO> olist = new ArrayList<AppOperationDTO>();
        for(AppOperationDTO dto6 : dtos){
            if(dto6.getLevel()==1){
                List<AppOperationDTO> dtoList = new ArrayList<AppOperationDTO>();
                for(AppOperationDTO dto7 : dtos){
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
     * 根据id查找当前评论列表
     */
    public Map<String,Object> getNoticeList(ObjectId id,int role,ObjectId userId,int page,int pageSize){
        Map<String,Object> map2 = new HashMap<String, Object>();
        //添加一级评论
        //AppNoticeEntry entry = appNoticeDao.getAppNoticeEntry(id);
        List<AppOperationEntry> entries = null;
        if(role==1){
            entries= appOperationDao.getEntryListByParentId(id,role,page,pageSize);
        }else{
            entries= appOperationDao.getEntryListByUserId(userId,role,id,page,pageSize);
        }
        //添加二级评论
        List<ObjectId> plist = new ArrayList<ObjectId>();
        if(entries != null && entries.size()>0){
            for(AppOperationEntry entry1 : entries){
                plist.add(entry1.getID());
            }
        }
        List<AppOperationEntry> entries2= appOperationDao.getSecondList(plist);
        entries.addAll(entries2);


        //取图和姓名
        List<AppOperationDTO> dtos = new ArrayList<AppOperationDTO>();
        List<String> uids = new ArrayList<String>();
        if(entries != null && entries.size()>0){
            for(AppOperationEntry en : entries){
                AppOperationDTO dto = new AppOperationDTO(en);
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
        for(AppOperationDTO dto5 : dtos){
            UserDetailInfoDTO dto9 = map.get(dto5.getUserId());
            if(dto9 != null){
                String name = StringUtils.isNotEmpty(dto9.getNickName()) ? dto9.getNickName() : dto9.getUserName();
                dto5.setUserName(name);
                dto5.setUserUrl(dto9.getImgUrl());
            }
            if(dto5.getBackId() != null && dto5.getBackId() != "" && map.get(dto5.getBackId())!= null){
                UserDetailInfoDTO dto10 = map.get(dto5.getBackId());
                String name2 = StringUtils.isNotEmpty(dto10.getNickName()) ? dto10.getNickName() : dto10.getUserName();
                dto5.setBackName(name2);
            }
        }
        List<AppOperationDTO> olist = new ArrayList<AppOperationDTO>();
        for(AppOperationDTO dto6 : dtos){
            if(dto6.getLevel()==1){
                List<AppOperationDTO> dtoList = new ArrayList<AppOperationDTO>();
                for(AppOperationDTO dto7 : dtos){
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

    //加载二级评论（分页）
    public List<AppOperationDTO> getSecondList(ObjectId parentId){
        AppOperationEntry entry2 = appOperationDao.getEntry(parentId);
        if(entry2==null){
            return null;
        }
        //List<AppOperationDTO> dtoList = new ArrayList<AppOperationDTO>();
        List<AppOperationEntry> entries = appOperationDao.getSecondListByParentId(parentId);
        /*if(entries.size()>0){
            for(AppOperationEntry entry: entries){
                dtoList.add(new AppOperationDTO(entry));
            }
        }*/
        entries.add(entry2);
        List<AppOperationDTO> dtos = new ArrayList<AppOperationDTO>();
        List<String> uids = new ArrayList<String>();
        if(entries != null && entries.size()>0){
            for(AppOperationEntry en : entries){
                AppOperationDTO dto = new AppOperationDTO(en);
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
        for(AppOperationDTO dto5 : dtos){
            UserDetailInfoDTO dto9 = map.get(dto5.getUserId());
            if(dto9 != null){
                String name = StringUtils.isNotEmpty(dto9.getNickName())?dto9.getNickName():dto9.getUserName();
                dto5.setUserName(name);
                dto5.setUserUrl(dto9.getImgUrl());
            }
            if(dto5.getBackId() != null && dto5.getBackId() != "" && map.get(dto5.getBackId())!= null){
                UserDetailInfoDTO dto10 = map.get(dto5.getBackId());
                String name2 = StringUtils.isNotEmpty(dto10.getNickName())?dto10.getNickName():dto10.getUserName();
                dto5.setBackName(name2);
            }
        }
        List<AppOperationDTO> olist = new ArrayList<AppOperationDTO>();
        for(AppOperationDTO dto6 : dtos){
            if(dto6.getLevel()==1){
                List<AppOperationDTO> dtoList = new ArrayList<AppOperationDTO>();
                for(AppOperationDTO dto7 : dtos){
                    if(dto7.getLevel()==2 && dto6.getId().equals(dto7.getParentId())){
                        dtoList.add(dto7);
                    }
                }
                dto6.setAlist(dtoList);
                olist.add(dto6);
            }
        }
        return olist;


    }
    /**
     * 学生发布作品
     * @return
     */
    public String addOperationEntryFromStrudent(AppOperationDTO dto){
        AppOperationEntry en = dto.buildAddEntry();
        //获得当前时间
        long current=System.currentTimeMillis();
        en.setDateTime(current);
        String id = appOperationDao.addEntry(en);
        AppCommentEntry entry = appCommentDao.getEntry(en.getContactId());
        //修改提交数
        entry.setLoadNumber(entry.getLoadNumber() + 1);
        appCommentDao.updEntry(entry);
        return id;
    }

    /**
     * 发布一级评论
     * @return
     */
    public String addOperationEntry(AppOperationDTO dto){
        AppOperationEntry en = dto.buildAddEntry();
        //获得当前时间
        long current=System.currentTimeMillis();
        en.setDateTime(current);
        String id = appOperationDao.addEntry(en);
        AppCommentEntry entry = appCommentDao.getEntry(en.getContactId());
        //修改讨论数
        if(en.getRole()==1){//家长讨论
            entry.setTalkNumber(entry.getTalkNumber()+1);
        }else if(en.getRole()==2){//学生讨论
            entry.setQuestionNumber(entry.getQuestionNumber()+1);
        }else if(en.getRole()==3){//
            entry.setLoadNumber(entry.getLoadNumber() + 1);
        }
        appCommentDao.updEntry(entry);
        return id;
    }
    /**
     * 发布二级评论
     * @return
     */
    public String addSecondOperation(AppOperationDTO dto){
        AppOperationEntry en = dto.buildAddEntry();
        //获得当前时间
        long current=System.currentTimeMillis();
        en.setDateTime(current);
        String id = appOperationDao.addEntry(en);
        AppCommentEntry entry = appCommentDao.getEntry(en.getContactId());
        //修改讨论数
       /* if (en.getRole() == 1) {//家长
            entry.setTalkNumber(entry.getTalkNumber() + 1);
        }*/
        appCommentDao.updEntry(entry);
        return id;
    }

    /**
     * 是否签到
     */
    public Map<String,Object> isSign2(ObjectId userId,long zero){
        Map<String,Object> map = new HashMap<String, Object>();
        //String result = "1";//未签到
        UserDetailInfoDTO userInfo = userService.getUserInfoById(userId.toString());
        AppRecordEntry entry = appRecordDao.getEntryListByParentId3(userId, zero);
        if(entry ==null){
            AppRecordDTO dto = new AppRecordDTO();
            dto.setUserId(userId.toString());
            dto.setUserName(userInfo.getUserName());
            dto.setIsLoad(0);
            AppRecordEntry entry1 = dto.buildAddEntry();
            entry1.setDateTime(zero);
            String str = appRecordDao.addEntry(entry1);
            map.put("type","1");
            map.put("id",str);
        }else{
            if(entry.getIsLoad()==0){
                map.put("type","1");
                map.put("id",entry.getID().toString());
            }else{
                map.put("type","2");
                map.put("id",entry.getID().toString());
            }
        }
        return map;
    }

    /**
     * 是否签到
     */
    public Map<String,Object> isSign(ObjectId userId,long zero,List<ObjectId> ids){
        //查找有没有新的未签到
        //已签到的list
        Map<String,Object> map = new HashMap<String, Object>();
        List<ObjectId> objectIdList = appRecordResultDao.getEntryList(userId,zero);
        if(ids.size()>objectIdList.size()){
            map.put("type","1");
        }else{
            map.put("type","2");
        }
        //String result = "1";//未签到
        UserDetailInfoDTO userInfo = userService.getUserInfoById(userId.toString());
        AppRecordEntry entry = appRecordDao.getEntryListByParentId3(userId, zero);
        if(entry ==null){
            AppRecordDTO dto = new AppRecordDTO();
            dto.setUserId(userId.toString());
            dto.setUserName(userInfo.getUserName());
            dto.setIsLoad(0);
            AppRecordEntry entry1 = dto.buildAddEntry();
            entry1.setDateTime(zero);
            String str = appRecordDao.addEntry(entry1);

            map.put("id",str);
        }else{
            if(entry.getIsLoad()==0){
                map.put("id",entry.getID().toString());
            }else{
                map.put("id",entry.getID().toString());
            }
        }
        return map;
    }
    /**
     * 签到
     */
    public String goSign2(ObjectId id,ObjectId userId){
        long current=System.currentTimeMillis();
        appRecordDao.updateEntry(id);
        appRecordDao.updateEntry2(id,current);
        AppRecordEntry entry = appRecordDao.getEntry(id);
        //所有社区
        List<CommunityDTO> communityDTOList =communityService.getCommunitys(userId, 1, 100);
        List<ObjectId>  dlist = new ArrayList<ObjectId>();
        if(communityDTOList.size() >0){
            for(CommunityDTO dto : communityDTOList){
                dlist.add(new ObjectId(dto.getId()));
            }
        }
        //收到的
        List<AppCommentEntry> entries2 = appCommentDao.selectDateList2(dlist, entry.getDateTime());
        if(entries2.size()>0){
            for(AppCommentEntry en : entries2){
                //修改签到数
                en.setWriteNumber(en.getWriteNumber()+1);
                appCommentDao.updEntry(en);
            }
        }

        return "签到成功";
    }

    public String goSign(ObjectId id,ObjectId userId){
        AppRecordEntry entry = appRecordDao.getEntry(id);
        //所有社区
        List<CommunityDTO> communityDTOList =communityService.getCommunitys(userId, 1, 100);
        List<ObjectId>  dlist = new ArrayList<ObjectId>();
        if(communityDTOList.size() >0){
            for(CommunityDTO dto : communityDTOList){
                dlist.add(new ObjectId(dto.getId()));
            }
        }
        //收到的
        List<AppCommentEntry> entries2 = appCommentDao.selectDateList2(dlist, entry.getDateTime());
        //已签到的作业list
        List<ObjectId> objectIdList = appRecordResultDao.getEntryList(userId,entry.getDateTime());
        List<AppRecordResultDTO> dtOlist = new ArrayList<AppRecordResultDTO>();
        if(entries2.size()>0){
            for(AppCommentEntry en : entries2){
                //修改签到数
                if(!objectIdList.contains(en.getID())){
                    en.setWriteNumber(en.getWriteNumber()+1);
                    appCommentDao.updEntry(en);
                    AppRecordResultDTO dto2 = new AppRecordResultDTO();
                    dto2.setUserId(userId.toString());
                    dto2.setIsLoad(2);
                    dto2.setParentId(en.getID().toString());
                    dtOlist.add(dto2);
                }
            }
        }
        if(dtOlist.size()>0){
            addAppRecordResultList(dtOlist);
        }
        return "签到成功";
    }
    /**
     * 批量增加红点记录
     * @param list
     */
    public void addAppRecordResultList(List<AppRecordResultDTO> list) {
        //获得当前时间
        long current=System.currentTimeMillis();
        //获得时间批次
        long zero=current/(1000*3600*24)*(1000*3600*24)- TimeZone.getDefault().getRawOffset();//今天零点零分零秒的毫秒数
        List<DBObject> dbList = new ArrayList<DBObject>();
        for (int i = 0; list != null && i < list.size(); i++) {
            AppRecordResultDTO si = list.get(i);
            AppRecordResultEntry obj = si.buildAddEntry();
            obj.setDateTime(zero);
            dbList.add(obj.getBaseEntry());
        }
        //导入新纪录
        if(dbList.size()>0) {
            appRecordResultDao.addBatch(dbList);
        }
    }
    /**
     * 获得用户的所有具有管理员权限的社区id
     *
     */
    public List<ObjectId> getMyRoleList(ObjectId userId){
        List<ObjectId> olsit = memberDao.getGroupIdsList(userId);
        List<ObjectId> clist = new ArrayList<ObjectId>();
        List<ObjectId> mlist =   groupDao.getGroupIdsList(olsit);
        return mlist;
    }

    /**
     *根据社区id返回社区中不具有管理员权限的人
     *
     */
    public List<String> getMyRoleList2(ObjectId id){
        //获得groupId
        ObjectId obj =   communityDao.getGroupIdByCommunityId(id);
        List<MemberEntry> olist = memberDao.getMembers(obj, 1, 1000);
        List<String> clist = new ArrayList<String>();
        if(olist.size()>0){
            for(MemberEntry en : olist){
                if(en.getRole()==0){
                    clist.add(en.getUserId().toString());
                }
            }
        }
        return clist;
    }
    /**
     *查询详情
     *
     */
    public AppCommentDTO selectAppCommentEntry(ObjectId id){
        AppCommentEntry entry = appCommentDao.getEntry(id);
        return new AppCommentDTO(entry);
    }
    /**
     *修改
     *
     */
    public void updateEntry(AppCommentDTO dto)throws Exception{
        List<CommunityDTO> communityDTOList = communityService.getCommunitys(new ObjectId(dto.getAdminId()), 1, 100);
        AppCommentEntry entry = dto.updateEntry();
        String st = dto.getComList();
        String[] str = st.split(",");
        for(CommunityDTO dto2 : communityDTOList){
            if(dto2.getId() != null && dto2.getId().equals(str[0])){
                entry.setRecipientId(new ObjectId(str[0]));
                entry.setRecipientName(dto2.getName());
            }
        }
        //获得当前时间
        long current=System.currentTimeMillis();
        //获得时间批次
        long zero=current/(1000*3600*24)*(1000*3600*24)- TimeZone.getDefault().getRawOffset();//今天零点零分零秒的毫秒数
        entry.setDateTime(zero);
        appCommentDao.updEntry(entry);
        JPushUtils jPushUtils=new JPushUtils();
        try {
            ObjectId groupId = communityDao.getGroupId(new ObjectId(str[0]));
            if (null != groupId && dto.getStatus() == 0) {
                List<MemberEntry> memberEntries = memberDao.getAllMembers(groupId);
                Set<String> userIds = new HashSet<String>();
                for (MemberEntry memberEntry : memberEntries) {
                    userIds.add(memberEntry.getUserId().toString());
                }
                Audience audience = Audience.alias(new ArrayList<String>(userIds));
                jPushUtils.pushRestIosbusywork(audience, dto.getTitle(), new HashMap<String, String>());
                jPushUtils.pushRestAndroidParentBusyWork(audience, dto.getDescription(), "", dto.getTitle(), new HashMap<String, String>());
                List<String> bindUserIds = newVersionBindService.getStudentIdListByCommunityId(new ObjectId(str[0]));
                if (bindUserIds.size() > 0) {
                    Audience studentAudience = Audience.alias(new ArrayList<String>(bindUserIds));
                    jPushUtils.pushRestAndroidStudentBusyWork(studentAudience, dto.getDescription(), "", dto.getTitle(),
                            new HashMap<String, String>());
                }
            }
        }catch (Exception e){
            throw new Exception("推送失败");
        }
    }

    /**
     *删除作业
     *
     */
    public void delAppcommentEntry(ObjectId id,ObjectId userId){
        AppCommentEntry e = appCommentDao.getEntry(id);
        if(e!= null && e.getAdminId() != null && e.getAdminId().equals(userId)){
            appCommentDao.delAppCommentEntry(id);
        }
    }
    /**
     *删除评论
     *
     */
    public void delAppOperationEntry(ObjectId id,ObjectId pingId,int role){
        //删除评论
        appOperationDao.delAppOperationEntry(pingId);
        //修改作业的评论数量
        AppCommentEntry entry = appCommentDao.getEntry(id);
        if(role==1){
            entry.setTalkNumber(entry.getTalkNumber()-1);
        }else if(role==2){
            entry.setQuestionNumber(entry.getQuestionNumber() - 1);
        }else{
            entry.setLoadNumber(entry.getLoadNumber() - 1);
        }
        appCommentDao.updEntry(entry);
    }

    /**
     * 查询老师绑定的学科
     */
    public List<SubjectClassDTO> selectTeacherSubjectList(ObjectId userId){
        List<SubjectClassDTO> dtos = new ArrayList<SubjectClassDTO>();
        NewVersionSubjectEntry entry = newVersionSubjectDao.getEntryByUserId(userId);
        List<SubjectClassEntry> entries = null;
        if(entry==null || entry.getSubjectList().size()==0){
            entries = subjectClassDao.getList();
        }else{
            List<ObjectId> olist = entry.getSubjectList();
           entries = subjectClassDao.getListByList(olist);
        }
        if(entries.size()>0){
            for(SubjectClassEntry entry1 : entries){
                dtos.add(new SubjectClassDTO(entry1));
            }
        }
        return dtos;
    }
    /**
     * 查询老师绑定的学科
     */
    public List<SubjectClassDTO> selectTeacherSubjectList2(ObjectId userId){
        List<SubjectClassDTO> dtos = new ArrayList<SubjectClassDTO>();
        NewVersionSubjectEntry entry = newVersionSubjectDao.getEntryByUserId(userId);
        List<SubjectClassEntry> entries = subjectClassDao.getList();
        List<ObjectId> objectIdList = new ArrayList<ObjectId>();
        if(entry != null && entry.getSubjectList().size()>0){
            objectIdList = entry.getSubjectList();
        }
        if(entries.size()>0){
            for(SubjectClassEntry entry1 : entries){
                SubjectClassDTO dto = new SubjectClassDTO(entry1);
                if(objectIdList.contains(entry1.getID())){
                    dto.setType(2);
                }else{
                    dto.setType(1);
                }
                dtos.add(dto);
            }
        }
        return dtos;
    }

    /**
     * 根据课程id 查询用户的课程列表
     *
     */
    public List<SubjectClassDTO> getSubjectByList(List<ObjectId> olist){
        List<SubjectClassDTO> dtos = new ArrayList<SubjectClassDTO>();
        List<SubjectClassEntry> enties = subjectClassDao.getListByList(olist);
        if(enties.size()>0){
            for(SubjectClassEntry entry : enties){
                dtos.add(new SubjectClassDTO(entry));
            }
        }
        return dtos;
    }

    /**
     * 编辑作业
     */

 /*   *//**
     * 获得用户的所有不具有管理员权限的社区id
     *
     *//*
    public List<String> getMyRoleList2(ObjectId id){
        //获得groupId
        ObjectId obj =   communityDao.getGroupIdByCommunityId(id);
        List<MemberEntry> olist = memberDao.getMembers(obj, 1, 1000);
        List<String> clist = new ArrayList<String>();
        if(olist.size()>0){
            for(MemberEntry en : olist){
                if(en.getRole()==0){
                    clist.add(en.getUserId().toString());
                }
            }
        }
        return clist;
    }
    */
  /*  public List<String> getMyRoleList2(ObjectId id){
        //获得groupId
        ObjectId obj =   groupDao.getGroupIdByCommunityId(id);
        List<MemberEntry> olist = memberDao.getMembers(obj, 1, 10000);
        List<String> clist = new ArrayList<String>();
        if(olist.size()>0){
            for(MemberEntry en : olist){
                if(en.getRole()==1 || en.getRole()==2){
                    clist.add(en.getUserId().toString());
                }
            }
        }
        return clist;
    }*/
}
