package com.db.temp;

import com.db.emailmanage.EmailManageDao;
import com.pojo.emailmanage.EmailManageEntry;

/**
 * Created by guojing on 2016/5/19.
 */
public class EmailManageState {
    public static void main(String[] args) {
        EmailManageDao emailManageDao = new EmailManageDao();
        String emails="hero.guo@fulaan.com";
        int type=1;
        int section=4;
        String serverOuterIp="120.55.184.233";
        String serverInnerIp="10.117.17.109";
        EmailManageEntry entry=null;
        entry=emailManageDao.getEmailManageEntry(type);
        if(entry==null){
            entry=new EmailManageEntry(emails,type,section,serverOuterIp,serverInnerIp);
            emailManageDao.addEmailManageEntry(entry);
        }else{
            entry.setEmails(emails);
            entry.setType(type);
            entry.setUserDef(section);
            entry.setServerOuterIp(serverOuterIp);
            entry.setServerInnerIp(serverInnerIp);
            emailManageDao.updEmailManageEntry(entry);
        }
    }
}

