package com.fulaan.business.service;

import com.db.business.BusinessManageDao;
import com.db.fcommunity.LoginLogDao;
import com.db.user.UserDao;
import com.fulaan.business.dto.BusinessManageDTO;
import com.fulaan.picturetext.runnable.PictureRunNable;
import com.fulaan.user.service.UserService;
import com.pojo.business.BusinessManageEntry;
import com.pojo.fcommunity.FLoginLogEntry;
import com.pojo.user.UserEntry;
import com.sys.constants.Constant;
import com.sys.utils.AvatarUtils;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by James on 2018/1/16.
 */
@Service
public class BusinessManageService {

    private BusinessManageDao businessManageDao =new BusinessManageDao();
    private LoginLogDao loginLogDao = new LoginLogDao();
    private UserDao userDao = new UserDao();

    private UserService userService = new UserService();

    //登陆生成
    public void getLoginInfo(ObjectId userId,int type ){
        PictureRunNable.addBusinessManageEntry(userId,type);
    }


    //自检
    public void checkBusinessManage(){
        PictureRunNable.updateBusinessManageEntry();
    }
    public void checkBusinessManage2(){
        PictureRunNable.updateBusinessManageEntry();
    }
    public static void main(String[] args){
        PictureRunNable.updateBusinessManageEntry();
    }
    //脚本
    public void createBusinessManage(){
        List<FLoginLogEntry> fLoginLogEntryList = loginLogDao.getUserResultList();
        Map<String,FLoginLogEntry> map = new HashMap<String, FLoginLogEntry>();
        Set<String> stringList = new HashSet<String>();
        if(fLoginLogEntryList.size()>0){
            for(FLoginLogEntry entry:fLoginLogEntryList){
                map.put(entry.getUserName(),entry);
                stringList.add(entry.getUserName());
            }
        }
        List<UserEntry> userEntries =   userDao.searchUsersByUserNames(stringList);
        if(userEntries.size()>0){
            for(UserEntry userEntry :userEntries){
                FLoginLogEntry fLoginLogEntry = map.get(userEntry.getUserName());
                int type = 1;
                if(fLoginLogEntry.getLoginPf().equals("Android")){
                    type =2;
                }else if(fLoginLogEntry.getLoginPf().equals("IOS")){
                    type =3;
                }else if(fLoginLogEntry.getLoginPf().equals("PC")){
                    type =1;
                }
                PictureRunNable.addFeiBusinessManageEntry(userEntry.getID(), type);
            }
        }
        //PictureRunNable.createBusinessManage();
    }

    //添加在线 时间
    public void addDuringTime(ObjectId userId,String userName){
        FLoginLogEntry fLoginLogEntry = loginLogDao.getEntry(userName);
        long time = System.currentTimeMillis();
        BusinessManageEntry businessManageEntry = businessManageDao.getEntry(userId);
        long durTime = time-fLoginLogEntry.getLoginTime();
        if(null!= businessManageEntry){
            if(durTime>0){
                businessManageEntry.setOnlineTime(businessManageEntry.getOnlineTime()+durTime);
            }
        }
    }

    //获得列表
    public Map<String,Object> getList(String jiaId,int page,int pageSize){
        String str = "";
        if(jiaId != null && !jiaId.equals("")){
            UserEntry userEntry = userDao.getJiaUserEntry(jiaId);
            if(userEntry !=null){
                str = userEntry.getID().toString();
            }
        }
        Map<String,Object> map = new HashMap<String, Object>();
        List<BusinessManageEntry> entries =  businessManageDao.getPageList(str,page, pageSize);
        List<BusinessManageDTO> list = new ArrayList<BusinessManageDTO>();
        List<ObjectId> userIds = new ArrayList<ObjectId>();
        if(entries.size()>0){
            for(BusinessManageEntry entry:entries){
                list.add(new BusinessManageDTO(entry));
                userIds.add(entry.getUserId());
            }
        }
        Map<ObjectId,UserEntry> userEntryMap=userService.getUserEntryMap(userIds, Constant.FIELDS);
        if(list.size()>0){
            for(BusinessManageDTO dto:list){
                UserEntry userEntry=userEntryMap.get(new ObjectId(dto.getUserId()));
                if(null!=userEntry){
                    dto.setAvatar(AvatarUtils.getAvatar(userEntry.getAvatar(), userEntry.getRole(), userEntry.getSex()));
                    dto.setUserName(userEntry.getUserName());
                }
            }
        }
        map.put("list",list);
        int count = businessManageDao.getReviewListCount();
        map.put("count",count);
        return map;
    }




}
