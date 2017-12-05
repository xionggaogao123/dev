package com.fulaan.mailmessage.service;

import cn.jiguang.commom.utils.StringUtils;
import com.db.operation.AppOperationDao;
import com.fulaan.mailmessage.utils.SendEmailUtil;
import com.fulaan.operation.dto.AppOperationDTO;
import com.fulaan.user.service.UserService;
import com.pojo.operation.AppOperationEntry;
import com.pojo.user.UserDetailInfoDTO;
import com.pojo.user.UserEntry;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by James on 2017/12/4.
 */
@Service
public class MailMessageService {
    @Autowired
    private UserService userService;

    private static final String mailName = "james.zhang@fulaan.com";

    private static final Logger logger =Logger.getLogger(MailMessageService.class);

    private AppOperationDao appOperationDao = new AppOperationDao();

    public void sendMailMessage(ObjectId userId,String message){
        UserEntry entry = userService.findById(userId);
        this.addOwnerOperation(userId, message);
       /* MailUtils mailUtils = new MailUtils();
        String title = "来自"+entry.getUserName()+"的一封建议信";
        try{
            mailUtils.sendMail(title, mailName, message);
        }catch (Exception e){
            logger.error("error",e);
        }*/
        String title = "来自"+entry.getUserName()+"的一封建议信";
        SendEmailUtil mailUtils = new SendEmailUtil(mailName,title,message,"");
        try{
            mailUtils.send();
        }catch (Exception e){
            logger.error("error",e);
        }

    }


    //添加5 类型评论
    public void addOwnerOperation(ObjectId userId,String message){
        AppOperationDTO dto = new AppOperationDTO();
        dto.setContactId(userId.toString());
        dto.setParentId("");
        dto.setUserId(userId.toString());
        dto.setBackId("");
        dto.setLevel(1);
        dto.setType(0);
        dto.setRole(5);
        dto.setDescription(message);
        dto.setSecond(0);
        dto.setCover("");
        dto.setFileUrl("");
        AppOperationEntry en = dto.buildAddEntry();
        //获得当前时间
        long current=System.currentTimeMillis();
        en.setDateTime(current);
        appOperationDao.addEntry(en);
    }

    public Map<String,Object> getOwnerOperation(ObjectId userId,int page,int pageSize){
        List<AppOperationEntry> entries =  appOperationDao.getEntryListByParentId(userId, 5, page, pageSize);
        int count = appOperationDao.countStudentLoadTimes(userId,5);
        Map<String,Object> map2 = new HashMap<String, Object>();
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
        map2.put("list",dtos);
        map2.put("count",count);
        return map2;
    }
}
