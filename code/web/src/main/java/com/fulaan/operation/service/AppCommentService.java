package com.fulaan.operation.service;

import com.db.operation.AppCommentDao;
import com.db.operation.AppOperationDao;
import com.db.operation.AppRecordDao;
import com.db.user.NewVersionBindRelationDao;
import com.fulaan.community.dto.CommunityDTO;
import com.fulaan.dto.MemberDTO;
import com.fulaan.operation.dto.AppCommentDTO;
import com.fulaan.operation.dto.AppOperationDTO;
import com.fulaan.operation.dto.AppRecordDTO;
import com.fulaan.service.CommunityService;
import com.fulaan.user.service.UserService;
import com.fulaan.utils.pojo.KeyValue;
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
    private NewVersionBindRelationDao newVersionBindRelationDao = new NewVersionBindRelationDao();
    @Autowired
    private CommunityService communityService;
    @Autowired
    private UserService userService;


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
        List<AppRecordEntry> entries = null;
        if(type==1){
            entries = appRecordDao.getEntryListByParentId(id,1);
            List<AppRecordDTO> dtos = new ArrayList<AppRecordDTO>();
            if(entries.size()>0){
                for(AppRecordEntry en : entries){
                    AppRecordDTO dto = new AppRecordDTO(en);
                    String ctm = dto.getDateTime().substring(11,16);
                    dto.setDateTime(ctm);
                    dtos.add(dto);
                }
            }
            return dtos;
        }else{
            List<AppRecordEntry> entries2 = appRecordDao.getEntryListByParentId(id,1);
            List<MemberDTO> melist = communityService.getMembers(id);
            List<AppRecordDTO> dtos = new ArrayList<AppRecordDTO>();
            if(melist.size()>0){
                for(MemberDTO en : melist){
                    boolean fla = true;
                    for(AppRecordEntry entr : entries) {
                        if(en.getUserId()!= null && en.getUserId().equals(entr.getUserId().toString())){
                            fla = false;
                        }
                    }
                    if(fla){
                        AppRecordDTO dto = new AppRecordDTO();
                        dto.setUserName(en.getUserName());
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
    public Map<String,Object> selectDateList(long dateTime,ObjectId userId){
        Map<String,Object> map2 = new HashMap<String, Object>();
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
        if(communityDTOList != null){
            for(CommunityDTO dto2 : communityDTOList){
                olist.add(new ObjectId(dto2.getId()));
            }
        }
        List<AppCommentEntry> entries2 = appCommentDao.selectDateList2(olist, dateTime);
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
     * 根据作业id查找当前评论列表
     */
    public List<AppOperationDTO> getOperationList(ObjectId id,ObjectId userId){
        AppCommentEntry entry = appCommentDao.getEntry(id);
        List<AppOperationEntry> entries = null;
        if(entry.getAdminId() != null && entry.getAdminId().equals(userId)){
            entries= appOperationDao.getEntryListByParentId(id);
        }else{
            entries= appOperationDao.getEntryListByUserId(userId, id);
        }
        List<AppOperationDTO> dtos = new ArrayList<AppOperationDTO>();
        List<String> uids = new ArrayList<String>();
        if(entries != null && entries.size()>0){
            for(AppOperationEntry en : entries){
                AppOperationDTO dto = new AppOperationDTO(en);
                uids.add(dto.getUserId());
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
     * 获取当前学期
     * @return
     */
    public KeyValue getCurrTermType() {
        KeyValue value=new KeyValue();
        DateTimeUtils time=new DateTimeUtils();
        //取得当前月
        int currMonth=time.getMonth();
        //取得当前年
        int currYear=time.getYear();
        int numb=1;
        for(int year=2017;year<=currYear;year++) {
            String schoolYear;
            if(year==currYear)
            {
                if (currMonth <= 2) {
                    schoolYear = (year - 1) + "-" + year + "上半学期";
                    value.setKey(numb++);
                    value.setValue(schoolYear);
                }else {
                    numb++;
                    if (currMonth < 9) {
                        schoolYear = (year - 1) + "-" + year + "下半学期";
                        value.setKey(numb++);
                        value.setValue(schoolYear);
                    }
                    if (currMonth >= 9) {
                        numb++;
                        schoolYear = year + "-" + (year + 1) + "上半学期";
                        value.setKey(numb++);
                        value.setValue(schoolYear);
                    }
                }
            }else {
                numb+=2;
            }
        }
        return value;
    }
}
