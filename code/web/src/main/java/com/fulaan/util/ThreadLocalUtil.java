package com.fulaan.util;

import com.fulaan.user.service.UserService;
import com.pojo.user.UserEntry;
import org.bson.types.ObjectId;

/**
 * Created by admin on 2016/12/5.
 */
public class ThreadLocalUtil implements Runnable {

    private static UserService userService=new UserService();
    private static ThreadLocal<User> userThreadLocal=new ThreadLocal<User>();

    public void setUser(String id){
        User user=new User();
        user.setId(id);
        userThreadLocal.set(user);
    }

    @Override
    public void run() {
//        User user=getUser();
        String id=Thread.currentThread().getName();
        UserEntry userEntry=userService.findByUserId(new ObjectId(id));
        String qrCode= QRUtils.getPersonQrUrl(new ObjectId(id));
        userEntry.setQRCode(qrCode);
        userService.addUser(userEntry) ;
    }


    public  User getUser(){
        User user = userThreadLocal.get();
        if (null == user) {
            user = new User();
            userThreadLocal.set(user);
        }
        return user;
    }
}

class User{
    String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
