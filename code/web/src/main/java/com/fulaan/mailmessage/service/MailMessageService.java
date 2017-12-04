package com.fulaan.mailmessage.service;

import com.fulaan.user.service.UserService;
import com.pojo.user.UserEntry;
import com.sys.mails.MailUtils;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by James on 2017/12/4.
 */
@Service
public class MailMessageService {
    @Autowired
    private UserService userService;

    private static final String mailName = "info@jiaxiaomei.com";

    private static final Logger logger =Logger.getLogger(MailMessageService.class);

    public void sendMailMessage(ObjectId userId,String message){
        UserEntry entry = userService.findById(userId);
        MailUtils mailUtils = new MailUtils();
        String title = "来自"+entry.getUserName()+"的一封建议信";
        try{
            mailUtils.sendMail(title, mailName, message);
        }catch (Exception e){
            logger.error("error",e);
        }

    }
}
