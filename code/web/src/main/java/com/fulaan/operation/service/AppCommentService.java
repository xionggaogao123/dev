package com.fulaan.operation.service;

import com.db.fcommunity.CommunityDao;
import com.db.fcommunity.GroupDao;
import com.db.fcommunity.MemberDao;
import com.db.operation.AppCommentDao;
import com.db.operation.AppOperationDao;
import com.db.operation.AppRecordDao;
import com.db.user.NewVersionBindRelationDao;
import com.fulaan.community.dto.CommunityDTO;
import com.fulaan.newVersionBind.service.NewVersionBindService;
import com.fulaan.operation.dto.AppCommentDTO;
import com.fulaan.operation.dto.AppOperationDTO;
import com.fulaan.operation.dto.AppRecordDTO;
import com.fulaan.service.CommunityService;
import com.fulaan.user.service.UserService;
import com.pojo.fcommunity.MemberEntry;
import com.pojo.operation.AppCommentEntry;
import com.pojo.operation.AppOperationEntry;
import com.pojo.operation.AppRecordEntry;
import com.pojo.user.NewVersionBindRelationEntry;
import com.pojo.user.UserDetailInfoDTO;
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
    private NewVersionBindRelationDao newVersionBindRelationDao = new NewVersionBindRelationDao();
    @Autowired
    private CommunityService communityService;
    @Autowired
    private UserService userService;
    @Autowired
    private NewVersionBindService newVersionBindService;


    /**
     * 发布作业
     * @return
     */
    public String addCommentEntry(AppCommentDTO dto,String comList){
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.YEAR);
        dto.setMonth(month);
        AppCommentEntry en = dto.buildAddEntry();
        //获得当前时间
        long current=System.currentTimeMillis();
        //获得时间批次
        long zero=current/(1000*3600*24)*(1000*3600*24)- TimeZone.getDefault().getRawOffset();//今天零点零分零秒的毫秒数
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
        for( CommunityDTO dto3 : sendList){
            en.setID(null);
            en.setRecipientId(new ObjectId(dto3.getId()));
            en.setRecipientName(dto3.getName());
            appCommentDao.addEntry(en);
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
     * 查询当前作业签到名单
     */
    public List<AppRecordDTO> selectRecordList(ObjectId id,int type){
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
        List<AppRecordEntry> entries = appRecordDao.getEntryListByParentId2(oblist,zero);
        if(type==1){//已签到名单
            List<AppRecordDTO> dtos = new ArrayList<AppRecordDTO>();
            if(entries.size()>0){
                for(AppRecordEntry en : entries){
                    if(en.getIsLoad()==1){
                        AppRecordDTO dto = new AppRecordDTO(en);
                        String ctm = dto.getCreateTime().substring(11,16);
                        dto.setDateTime(ctm);
                        dtos.add(dto);
                    }
                }
            }
            return dtos;
        }else{//未签到名单
            List<AppRecordDTO> dtos = new ArrayList<AppRecordDTO>();
            if(entries.size()>0){
                for(AppRecordEntry en : entries){
                    if(en.getIsLoad()==1){
                        oblist.remove(en.getUserId());
                    }
                }
            }
            List<UserDetailInfoDTO> udtos =  userService.findUserInfoByUserIds(objectIdList);
            for(ObjectId ob : oblist){
                for(UserDetailInfoDTO dto4 : udtos){
                    if(dto4.getId()!= null && dto4.getId().equals(ob.toString())){
                        AppRecordDTO dto = new AppRecordDTO();
                        dto.setUserName(dto4.getUserName());
                        dtos.add(dto);
                    }
                }
            }
            return dtos;
        }

    }

    /**
     * 按年查找用户发放作业情况
     */
    public List<String> selectResultList(int month,ObjectId userId){
        List<AppCommentEntry> entries = appCommentDao.selectResultList(userId, month);
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
    public Map<String,Object> selectDateList2(long dateTime,ObjectId userId){
        Map<String,Object> map2 = new HashMap<String, Object>();

        //发送的作业
        List<AppCommentEntry> entries = appCommentDao.selectDateList(userId, dateTime);
        List<String> uids = new ArrayList<String>();
        List<AppCommentDTO> dtos = new ArrayList<AppCommentDTO>();
        if(entries.size()>0){
            for(AppCommentEntry en : entries){
                AppCommentDTO dto = new AppCommentDTO(en);
                String ctm = dto.getCreateTime().substring(11,16);
                dto.setCreateTime(ctm);
                dto.setType(1);
                int num = appOperationDao.getEntryCount(en.getID());
                uids.add(dto.getAdminId());
                dto.setNumber(num);
                dtos.add(dto);
            }
        }

        List<NewVersionBindRelationEntry> nlist = newVersionBindRelationDao.getEntriesByMainUserId(userId);
        List<ObjectId> olist = new ArrayList<ObjectId>();
        List<CommunityDTO> communityDTOList = new ArrayList<CommunityDTO>();
        if(nlist.size()>0){
            for(NewVersionBindRelationEntry en : nlist){
                //获取孩子的社区
                communityDTOList.addAll(communityService.getCommunitys(userId, 1, 100));
            }
        }
        List<String> stuList = new ArrayList<String>();
        if(communityDTOList != null){
            for(CommunityDTO dto2 : communityDTOList){
                stuList.add(dto2.getId());
                olist.add(new ObjectId(dto2.getId()));
            }
        }
        //孩子收到的作业
        List<AppCommentEntry> entries2 = appCommentDao.selectDateList2(olist, dateTime);
        //List<UserDetailInfoDTO> udtos = userService.findUserInfoByUserIds(olist);
        List<UserDetailInfoDTO> studtos = userService.findUserInfoByUserIds(stuList);
      /*  Map<String,UserDetailInfoDTO> stumap = new HashMap<String, UserDetailInfoDTO>();
        if(studtos != null && studtos.size()>0){
            for(UserDetailInfoDTO dto4 : studtos){
                stumap.put(dto4.getId(),dto4);
            }
        }*/
        if(entries2.size()>0){
            for(AppCommentEntry en : entries2){
                AppCommentDTO dto3 = new AppCommentDTO(en);
                if(dto3.getAdminId() != null && dto3.getAdminId().equals(userId.toString())){

                }else{
                    int num = appOperationDao.getEntryCount(en.getID());
                    String ctm = dto3.getCreateTime().substring(11,16);
                    dto3.setCreateTime(ctm);
                    dto3.setNumber(num);
                    dto3.setType(2);
                    if(studtos.size()>0){
                        dto3.setSendUser(studtos.get(0).getUserName());
                    }
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
           dto5.setAdminName(map.get(dto5.getAdminId()).getUserName());
           dto5.setAdminUrl(map.get(dto5.getAdminId()).getImgUrl());
        }
        map2.put("list",dtos);
        Map<String,Object> map3 = this.isSign(userId, dateTime);
        map2.put("isload",map3);
        return map2;
    }
    /**
     * 按日查找用户发放作业情况
     */
    public Map<String,Object> selectDateList(long dateTime,ObjectId userId){
        Map<String,Object> map2 = new HashMap<String, Object>();

        //发送的作业
        List<AppCommentEntry> entries = appCommentDao.selectDateList(userId, dateTime);
        List<String> uids = new ArrayList<String>();
        List<AppCommentDTO> dtos = new ArrayList<AppCommentDTO>();
        if(entries.size()>0){
            for(AppCommentEntry en : entries){
                AppCommentDTO dto = new AppCommentDTO(en);
                String ctm = dto.getCreateTime().substring(11,16);
                dto.setCreateTime(ctm);
                dto.setType(1);
                int num = appOperationDao.getEntryCount(en.getID());
                uids.add(dto.getAdminId());
                dto.setNumber(num);
                dtos.add(dto);
            }
        }
        //
        List<NewVersionBindRelationEntry> nlist = newVersionBindRelationDao.getEntriesByMainUserId(userId);
        List<String> olist = new ArrayList<String>();
        if(nlist.size()>0){
            for(NewVersionBindRelationEntry entry : nlist){
                olist.add(entry.getUserId().toString());
            }
        }
        List<UserDetailInfoDTO> studtos = userService.findUserInfoByUserIds(olist);
        List<CommunityDTO> communityDTOList =communityService.getCommunitys(userId, 1, 100);
        List<ObjectId>  dlist = new ArrayList<ObjectId>();
        if(communityDTOList.size() >0){
            for(CommunityDTO dto : communityDTOList){
                dlist.add(new ObjectId(dto.getId()));
            }
        }
        List<AppCommentEntry> entries2 = appCommentDao.selectDateList2(dlist, dateTime);
        if(entries2.size()>0){
            for(AppCommentEntry en : entries2){
                AppCommentDTO dto3 = new AppCommentDTO(en);
                if(dto3.getAdminId() != null && dto3.getAdminId().equals(userId.toString())){

                }else{
                    int num = appOperationDao.getEntryCount(en.getID());
                    String ctm = dto3.getCreateTime().substring(11,16);
                    dto3.setCreateTime(ctm);
                    dto3.setNumber(num);
                    dto3.setType(2);
                    if(studtos.size()>0){
                        dto3.setSendUser(studtos.get(0).getUserName());
                    }
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
            dto5.setAdminName(map.get(dto5.getAdminId()).getUserName());
            dto5.setAdminUrl(map.get(dto5.getAdminId()).getImgUrl());
        }
        List<ObjectId> mlist = this.getMyRoleList(userId);
        if(mlist != null && mlist.size()>0){
            map2.put("isTeacher",1);
        }else{
            map2.put("isTeacher",2);
        }
        map2.put("list",dtos);
        Map<String,Object> map3 = this.isSign(userId, dateTime);
        map2.put("isload",map3);
        return map2;
    }
    /**
     * 学生端查询接受到的作业
     *
     */
    public List<AppCommentDTO> getStuLit(long dateTime,ObjectId studentId){
        List<AppCommentDTO> dtos = new ArrayList<AppCommentDTO>();
        //List<NewVersionBindRelationEntry> nlist = newVersionBindRelationDao.getEntriesByUserId(studentId);
        List<String> olist = new ArrayList<String>();
        //List<ObjectId> blist = new ArrayList<ObjectId>();
        List<String> uids = new ArrayList<String>();
        //获取家长端的所有社区
        /*List<CommunityDTO> communityDTOList = new ArrayList<CommunityDTO>();
        if(nlist.size()>0){
            for(NewVersionBindRelationEntry entry : nlist){
                blist.add(entry.getMainUserId());
                olist.add(entry.getMainUserId().toString());
                communityDTOList.addAll(communityService.getCommunitys(entry.getMainUserId(), 1, 100));
            }
        }
        //根据家长端的所有社区查找作业记录
        List<String> stuList = new ArrayList<String>();
        List<ObjectId> obList = new ArrayList<ObjectId>();
        if(communityDTOList != null){
            for(CommunityDTO dto2 : communityDTOList){
                stuList.add(dto2.getId());
                obList.add(new ObjectId(dto2.getId()));
            }
        }*/
        List<ObjectId> obList = newVersionBindService.getCommunityIdsByUserId(studentId);
        List<AppCommentEntry> entries2 = appCommentDao.selectDateList2(obList, dateTime);
        UserDetailInfoDTO studtos = userService.getUserInfoById(studentId.toString());
        if(entries2.size()>0){
            for(AppCommentEntry en : entries2){
                AppCommentDTO dto3 = new AppCommentDTO(en);
                if(dto3.getAdminId() != null && olist.contains(dto3.getAdminId())){

                }else{
                    int num = appOperationDao.getEntryCount(en.getID());
                    String ctm = dto3.getCreateTime().substring(11,16);
                    dto3.setCreateTime(ctm);
                    dto3.setNumber(num);
                    dto3.setType(2);
                    if(studtos != null){
                        dto3.setSendUser(studtos.getUserName());
                    }
                    uids.add(dto3.getAdminId());
                    dtos.add(dto3);
                }
            }
        }
        return dtos;
    }



    /**
     * 根据作业id查找当前评论列表
     */
    public List<AppOperationDTO> getOperationList(ObjectId id,ObjectId userId,int page,int pageSize){
        //添加一级评论
        AppCommentEntry entry = appCommentDao.getEntry(id);
        List<AppOperationEntry> entries = null;
        if(entry.getAdminId() != null && entry.getAdminId().equals(userId)){
            entries= appOperationDao.getEntryListByParentId(id,page,pageSize);
        }else{
            entries= appOperationDao.getEntryListByUserId(userId, id,page,pageSize);
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
            dto5.setUserName(map.get(dto5.getUserId()).getUserName());
            dto5.setUserUrl(map.get(dto5.getUserId()).getImgUrl());
            if(dto5.getBackId() != null && dto5.getBackId() != ""){
                dto5.setBackName(map.get(dto5.getBackId()).getUserName());
            }
        }
        return dtos;
    }

    /**
     * 发布评论
     * @return
     */
    public String addOperationEntry(AppOperationDTO dto){
        AppOperationEntry en = dto.buildAddEntry();
        //获得当前时间
        long current=System.currentTimeMillis();
        en.setDateTime(current);
        String id = appOperationDao.addEntry(en);
        return id;
    }
    /**
     * 发布评论
     * @return
     */
    public String addSecondOperation(AppOperationDTO dto){
        AppOperationEntry en = dto.buildAddEntry();
        //获得当前时间
        long current=System.currentTimeMillis();
        en.setDateTime(current);
        String id = appOperationDao.addEntry(en);
        return id;
    }

    /**
     * 是否签到
     */
    public Map<String,Object> isSign(ObjectId userId,long zero){
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
     * 签到
     */
    public String goSign(ObjectId id){
        long current=System.currentTimeMillis();
        appRecordDao.updateEntry(id);
        appRecordDao.updateEntry2(id,current);
        return "签到成功";
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
     * 获得用户的所有不具有管理员权限的社区id
     *
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