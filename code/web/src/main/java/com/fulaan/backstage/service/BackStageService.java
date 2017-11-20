package com.fulaan.backstage.service;

import com.db.backstage.UnlawfulPictureTextDao;
import com.db.controlphone.ControlSetBackDao;
import com.db.controlphone.ControlSetTimeDao;
import com.pojo.controlphone.ControlSetBackEntry;
import com.pojo.controlphone.ControlSetTimeEntry;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

/**
 * Created by James on 2017/11/18.
 */
@Service
public class BackStageService {
    private UnlawfulPictureTextDao unlawfulPictureTextDao = new UnlawfulPictureTextDao();

    private ControlSetBackDao controlSetBackDao = new ControlSetBackDao();

    private ControlSetTimeDao controlSetTimeDao = new ControlSetTimeDao();

    public static void main(String[] args){
        ControlSetBackDao controlSetBackDao = new ControlSetBackDao();
        ControlSetBackEntry entry1 = new ControlSetBackEntry();
        entry1.setType(1);
        entry1.setBackTime(15);
        entry1.setAppTime(15);
        controlSetBackDao.addEntry(entry1);
    }


    public void addBackTimeEntry(ObjectId userId,int time){
        ControlSetBackEntry entry = controlSetBackDao.getEntry();
        if(null == entry){
            ControlSetBackEntry entry1 = new ControlSetBackEntry();
            entry1.setType(1);
            entry1.setBackTime(time);
            entry1.setAppTime(24 * 60);
            controlSetBackDao.addEntry(entry1);
        }else{
            entry.setBackTime(time);
            controlSetBackDao.updEntry(entry);
        }

    }
    public void addAppBackTimeEntry(ObjectId userId,int time){
        ControlSetBackEntry entry = controlSetBackDao.getEntry();
        if(null == entry){
            ControlSetBackEntry entry1 = new ControlSetBackEntry();
            entry1.setType(1);
            entry1.setAppTime(time);
            entry1.setBackTime(24*60);
            controlSetBackDao.addEntry(entry1);
        }else{
            entry.setAppTime(time);
            controlSetBackDao.updEntry(entry);
        }

    }
    public void addSetTimeListEntry(ObjectId userId,int time){
        long time2 = time * 60 * 1000;
        long hours2 = (time2 % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long minutes2 = (time2 % (1000 * 60 * 60)) / (1000 * 60);
        String timeStr = "";
        if(hours2 != 0 ){
            timeStr = timeStr + hours2+"小时";
        }
        if(minutes2 != 0){
            timeStr = timeStr + minutes2+"分钟";
        }
        ControlSetTimeEntry controlSetTimeEntry = new ControlSetTimeEntry();
        controlSetTimeEntry.setName(timeStr);
        controlSetTimeEntry.setTime(time);
        controlSetTimeDao.addEntry(controlSetTimeEntry);

    }



}
