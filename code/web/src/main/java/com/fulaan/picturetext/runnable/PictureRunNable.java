package com.fulaan.picturetext.runnable;

import com.db.backstage.UnlawfulPictureTextDao;
import com.fulaan.backstage.dto.UnlawfulPictureTextDTO;
import com.fulaan.picturetext.service.CheckTextAndPicture;

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

}
