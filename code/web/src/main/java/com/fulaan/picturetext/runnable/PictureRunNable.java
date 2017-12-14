package com.fulaan.picturetext.runnable;

import com.db.backstage.UnlawfulPictureTextDao;
import com.db.controlphone.ControlAppUserDao;
import com.fulaan.backstage.dto.UnlawfulPictureTextDTO;
import com.fulaan.picturetext.service.CheckTextAndPicture;
import com.pojo.controlphone.ControlAppUserEntry;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * Created by James on 2017/11/24.
 *
 * //异步线程处理
 */
public class PictureRunNable{
    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(PictureRunNable.class);




    public static void send(final String contactId,final String userId,final int function,final int type,final String content) {
        new Thread(){
            public void run() {
                UnlawfulPictureTextDao unlawfulPictureTextDao = new UnlawfulPictureTextDao();
                System.out.println("新的线程在执行...");
                //System.out.println(prtNo);
                try{
                    CheckTextAndPicture.syncScanCheck(contactId,userId,function,type,content);
                }catch(Exception e){
                    UnlawfulPictureTextDTO dto = new UnlawfulPictureTextDTO();
                    dto.setType(type);
                    dto.setContent(content);
                    dto.setUserId(userId.toString());
                    dto.setFunction(function);
                    dto.setIsCheck(0);
                    dto.setContactId(contactId.toString());
                    unlawfulPictureTextDao.addEntry(dto.buildAddEntry());
                    logger.error("error",e);
                }
            }
        }.start();
    }

    public static void addApp(final ObjectId appId) {
        new Thread(){
            public void run() {
                ControlAppUserDao controlAppUserDao = new ControlAppUserDao();
                System.out.println("新的线程在执行...");
                //System.out.println(prtNo);
                boolean flag =true;
                int page = 1;
                try{
                    while(flag) {
                        List<ControlAppUserEntry> entryList =  controlAppUserDao.getUserList(page,10);
                        if(entryList.size()>0){
                            for(ControlAppUserEntry entry : entryList){
                                if(entry.getAppIdList() !=null && !entry.getAppIdList().contains(appId)){
                                    List<ObjectId> oids = entry.getAppIdList();
                                    oids.add(appId);
                                    entry.setAppIdList(oids);
                                    controlAppUserDao.updEntry(entry);
                                }
                            }
                        }else{
                            flag=false;
                        }
                        page++;
                    }
                }catch(Exception e){
                    logger.error("error",e);
                }
            }
        }.start();
    }

    public static void sendMessage(final String contactId,final String userId,final int function,final int type,final String content) {
        new Thread(){
            public void run() {
                UnlawfulPictureTextDao unlawfulPictureTextDao = new UnlawfulPictureTextDao();
                System.out.println("新的线程在执行...");
                //System.out.println(prtNo);
                try{
                    CheckTextAndPicture.syncScanCheck(contactId,userId,function,type,content);
                }catch(Exception e){
                    UnlawfulPictureTextDTO dto = new UnlawfulPictureTextDTO();
                    dto.setType(type);
                    dto.setContent(content);
                    dto.setUserId(userId.toString());
                    dto.setFunction(function);
                    dto.setIsCheck(0);
                    dto.setContactId(contactId.toString());
                    unlawfulPictureTextDao.addEntry(dto.buildAddEntry());
                    logger.error("error",e);
                }
            }
        }.start();
    }
}
