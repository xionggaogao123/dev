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
        int month = cal.get(Calendar.MONTH)+ 1;
        dto.setMonth(month);
        long zero = 0l;
        if(dto.getDateTime() ==null && dto.getDateTime()==""){
            //获得当前时间
            long current=System.currentTimeMillis();
            //获得时间批次
            zero = current/(1000*3600*24)*(1000*3600*24)- TimeZone.getDefault().getRawOffset();//今天零点零分零秒的毫秒数
        }else{
            zero = DateTimeUtils.getStrToLongTime(dto.getDateTime(), "yyyy-MM-dd");
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
        if(entries != null && entries.size()>0){
            for(AppOperationEntry en : entries){
                AppOperationDTO dto = new AppOperationDTO(en);
                objectIdList.remove(dto.getUserId());
                uids.add(dto.getUserId());
                dtos.add(dto);
            }
        }
        //已提交
        List<UserDetailInfoDTO> udtos = userService.findUserInfoByUserIds(uids);
        Map<String,UserDetailInfoDTO> map2 = new HashMap<String, UserDetailInfoDTO>();
        if(udtos != null && udtos.size()>0){
            for(UserDetailInfoDTO dto4 : udtos){
                map.put(dto4.getId(),dto4);
            }
        }
        //未提交
        List<UserDetailInfoDTO> unList = userService.findUserInfoByUserIds(objectIdList);
        for(AppOperationDTO dto5 : dtos){
            dto5.setUserName(map2.get(dto5.getUserId()).getUserName());
            dto5.setUserUrl(map2.get(dto5.getUserId()).getImgUrl());
        }
        //已提交
        map.put("loadList",dtos);
        //已提交人数
        map.put("loadNumber",num);
        //未提交
        map.put("unLoadList",unList);
        //未提交人数
        map.put("unLoadNumber",unList.size());

        return map;
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
        List<AppRecordEntry> entries = appRecordDao.getEntryListByParentId2(oblist,zero);
        //已签到名单
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
        map.put("SignList",dtos);
        map.put("SignListNum",dtos.size());
       //未签到名单
        List<AppRecordDTO> dtos2 = new ArrayList<AppRecordDTO>();
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
                    dtos2.add(dto);
                }
            }
        }
        map.put("UnSignList",dtos2);
        map.put("UnSignListNum",dtos2.size());
        return map;
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
                if(en.getDateTime() <= zero){
                    en.setStatus(0);
                }
                AppCommentDTO dto = new AppCommentDTO(en);
                String ctm = dto.getCreateTime().substring(11,16);
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
            map2.put("isShow",1);
        }else{
            map2.put("isShow",2);
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
        if(entries2.size()>0){
            for(AppCommentEntry en : entries2){
                AppCommentDTO dto3 = new AppCommentDTO(en);
                if(dto3.getAdminId() != null && dto3.getAdminId().equals(userId.toString())){

                }else{
                    String ctm = dto3.getCreateTime().substring(11,16);
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
        List<String> olist = new ArrayList<String>();
        List<String> uids = new ArrayList<String>();
        List<ObjectId> obList = newVersionBindService.getCommunityIdsByUserId(studentId);
        List<AppCommentEntry> entries2 = appCommentDao.selectDateList2(obList, dateTime);
        UserDetailInfoDTO studtos = userService.getUserInfoById(studentId.toString());
        if(entries2.size()>0){
            for(AppCommentEntry en : entries2){
                AppCommentDTO dto3 = new AppCommentDTO(en);
                if(dto3.getAdminId() != null && olist.contains(dto3.getAdminId())){

                }else{
                    String ctm = dto3.getCreateTime().substring(11,16);
                    dto3.setCreateTime(ctm);
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
    public Map<String,Object> getOperationList(ObjectId id,int role,ObjectId userId,int page,int pageSize){
        Map<String,Object> map2 = new HashMap<String, Object>();
        AppCommentEntry entry2 = appCommentDao.getEntry(id);
        UserDetailInfoDTO studtos = userService.getUserInfoById(entry2.getAdminId().toString());
        AppCommentDTO dto2 = new AppCommentDTO(entry2);
        dto2.setAdminName(studtos.getUserName());
        dto2.setAdminUrl(studtos.getImgUrl());
         //上面的描述
        map2.put("desc",dto2);
        //添加一级评论
        AppCommentEntry entry = appCommentDao.getEntry(id);
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
            dto5.setUserName(map.get(dto5.getUserId()).getUserName());
            dto5.setUserUrl(map.get(dto5.getUserId()).getImgUrl());
            if(dto5.getBackId() != null && dto5.getBackId() != ""){
                dto5.setBackName(map.get(dto5.getBackId()).getUserName());
            }
        }
        //分页评论列表
        map2.put("list",dtos);
        return map2;
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
        if(en.getRole()==1){//家长
            entry.setTalkNumber(entry.getTalkNumber()+1);
        }else{//学生
            entry.setQuestionNumber(entry.getQuestionNumber()+1);
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
        if (en.getRole() == 1) {//家长
            entry.setTalkNumber(entry.getTalkNumber()+ 1);
        } else {//学生
            entry.setQuestionNumber(entry.getQuestionNumber()+1);
        }
        appCommentDao.updEntry(entry);
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
    public String goSign(ObjectId id,ObjectId userId){
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
     *查询详情
     *
     */
    public void updateEntry(AppCommentDTO dto){
        AppCommentEntry entry = dto.updateEntry();
        appCommentDao.updEntry(entry);
    }

    /**
     *删除作业
     *
     */
    public void delAppcommentEntry(ObjectId id){
        appCommentDao.delAppCommentEntry(id);
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
