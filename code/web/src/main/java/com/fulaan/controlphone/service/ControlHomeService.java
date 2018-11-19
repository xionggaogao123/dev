package com.fulaan.controlphone.service;

import com.db.controlphone.ControlHomeTimeDao;
import com.pojo.controlphone.ControlHomeTimeEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by James on 2018-11-16.
 */
@Service
public class ControlHomeService {

    private ControlHomeTimeDao controlHomeTimeDao = new ControlHomeTimeDao();



    public void addHomeEntry(ObjectId userId,
                             ObjectId sonId,
                             String dayStartTime,
                             String dayEndTime,
                             int dayHour,
                             String weekStartTime,
                             String weekEndTime,
                             int weekHour){
        //周常
        ControlHomeTimeEntry controlHomeTimeEntry = controlHomeTimeDao.getEntry(userId,sonId, Constant.ONE);
        if(controlHomeTimeEntry!=null){
            controlHomeTimeEntry.setSchoolTimeFrom("07:30");
            controlHomeTimeEntry.setSchoolTimeTo(dayStartTime);
            controlHomeTimeEntry.setBedTimeFrom(dayEndTime);
            controlHomeTimeEntry.setBedTimeTo("07:30");
            controlHomeTimeEntry.setHour(dayHour*60000);
            controlHomeTimeDao.addEntry(controlHomeTimeEntry);
        }else{
            ControlHomeTimeEntry controlHomeTimeEntry1 = new ControlHomeTimeEntry(Constant.ONE,
                    Constant.ONE,
                    "",
                    "",
                    "07:30",
                    dayStartTime,
                    dayEndTime,
                    "07:30",
                    userId,
                    sonId,
                    "周常",
                    dayHour*60000);
            controlHomeTimeDao.addEntry(controlHomeTimeEntry1);
        }

        //周末
        ControlHomeTimeEntry controlHomeTimeEntry2 = controlHomeTimeDao.getEntry(userId,sonId, Constant.SIX);
        if(controlHomeTimeEntry2!=null){
            controlHomeTimeEntry2.setSchoolTimeFrom("07:30");
            controlHomeTimeEntry2.setSchoolTimeTo(weekStartTime);
            controlHomeTimeEntry2.setBedTimeFrom(weekEndTime);
            controlHomeTimeEntry2.setBedTimeTo("07:30");
            controlHomeTimeEntry2.setHour(weekHour*60000);
            controlHomeTimeDao.addEntry(controlHomeTimeEntry2);
        }else{
            ControlHomeTimeEntry controlHomeTimeEntry1 = new ControlHomeTimeEntry(Constant.ONE,
                    Constant.SIX,
                    "",
                    "",
                    "07:30",
                    weekStartTime,
                    weekEndTime,
                    "07:30",
                    userId,
                    sonId,
                    "周末",
                    weekHour*60000);
            controlHomeTimeDao.addEntry(controlHomeTimeEntry1);
        }

    }


    public Map<String,Object> getHomeEntryList(ObjectId parentId,ObjectId sonId){
        Map<String,Object> map = new HashMap<String, Object>();
        List<ControlHomeTimeEntry> controlHomeTimeEntries =  controlHomeTimeDao.getEntryList(parentId, sonId);
        map.put("dayStartTime","17:00");
        map.put("dayEndTime","22:00");
        map.put("dayHour","30");
        map.put("weekStartTime","7:30");
        map.put("weekEndTime","22:00");
        map.put("weekHour","30");
        for(ControlHomeTimeEntry controlHomeTimeEntry: controlHomeTimeEntries){
            if(controlHomeTimeEntry.getWeek()==Constant.ONE){//周常
                map.put("dayStartTime",controlHomeTimeEntry.getSchoolTimeTo());
                map.put("dayEndTime",controlHomeTimeEntry.getBedTimeFrom());
                map.put("dayHour",controlHomeTimeEntry.getHour()/60000);
            }else if(controlHomeTimeEntry.getWeek()==Constant.SIX){//周末
                map.put("weekStartTime",controlHomeTimeEntry.getSchoolTimeTo());
                map.put("weekEndTime",controlHomeTimeEntry.getBedTimeFrom());
                map.put("weekHour",controlHomeTimeEntry.getHour()/60000);
            }else{

            }
        }
        return map;
    }

}
