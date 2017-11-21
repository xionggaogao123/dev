package com.fulaan.backstage.service;

import com.db.backstage.UnlawfulPictureTextDao;
import com.db.controlphone.ControlPhoneDao;
import com.db.controlphone.ControlSchoolTimeDao;
import com.db.controlphone.ControlSetBackDao;
import com.db.controlphone.ControlSetTimeDao;
import com.fulaan.backstage.dto.UnlawfulPictureTextDTO;
import com.fulaan.controlphone.dto.ControlPhoneDTO;
import com.fulaan.controlphone.dto.ControlSchoolTimeDTO;
import com.pojo.backstage.UnlawfulPictureTextEntry;
import com.pojo.controlphone.ControlPhoneEntry;
import com.pojo.controlphone.ControlSchoolTimeEntry;
import com.pojo.controlphone.ControlSetBackEntry;
import com.pojo.controlphone.ControlSetTimeEntry;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by James on 2017/11/18.
 */
@Service
public class BackStageService {
    private UnlawfulPictureTextDao unlawfulPictureTextDao = new UnlawfulPictureTextDao();

    private ControlSetBackDao controlSetBackDao = new ControlSetBackDao();

    private ControlSetTimeDao controlSetTimeDao = new ControlSetTimeDao();

    private ControlPhoneDao controlPhoneDao = new ControlPhoneDao();

    private ControlSchoolTimeDao controlSchoolTimeDao = new ControlSchoolTimeDao();


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
    public void addPhoneEntry(String name, String phone){
        ControlPhoneEntry entry = controlPhoneDao.getEntry(phone);
        if(null == entry){
            ControlPhoneDTO dto = new ControlPhoneDTO();
            dto.setName(name);
            dto.setPhone(phone);
            dto.setType(1);
            controlPhoneDao.addEntry(dto.buildAddEntry());
        }else{
            entry.setName(name);
            controlPhoneDao.updEntry(entry);
        }

    }

    public void addSchoolTime(String startTime,String endTime,int week){
        ControlSchoolTimeEntry entry = controlSchoolTimeDao.getEntry(week);
        if(null==entry){
            ControlSchoolTimeDTO dto = new ControlSchoolTimeDTO();
            dto.setStartTime(startTime);
            dto.setEndTime(endTime);
            dto.setWeek(week);
            dto.setType(1);
            controlSchoolTimeDao.addEntry(dto.buildAddEntry());
        }else{
            entry.setStartTime(startTime);
            entry.setStartTime(endTime);
            controlSchoolTimeDao.updEntry(entry);
        }
    }
    public void addOtherSchoolTime(String startTime,String endTime,String dateTime){
        ControlSchoolTimeEntry entry = controlSchoolTimeDao.getOtherEntry(dateTime);
        if(null==entry){
            ControlSchoolTimeDTO dto = new ControlSchoolTimeDTO();
            dto.setStartTime(startTime);
            dto.setEndTime(endTime);
            dto.setDataTime(dateTime);
            dto.setType(2);
            controlSchoolTimeDao.addEntry(dto.buildAddEntry());
        }else{
            entry.setStartTime(startTime);
            entry.setStartTime(endTime);
            controlSchoolTimeDao.updEntry(entry);
        }
    }

    //添加图片显示认证
    public void addYellowPicture(ObjectId userId,String imageUrl,int ename,ObjectId contactId){
        UnlawfulPictureTextDTO dto = new UnlawfulPictureTextDTO();
        dto.setType(1);
        dto.setContent(imageUrl);
        dto.setUserId(userId.toString());
        dto.setFunction(ename);
        dto.setIsCheck(0);
        dto.setContactId(contactId.toString());
        unlawfulPictureTextDao.addEntry(dto.buildAddEntry());
    }


    public Map<String,Object> selectContentList(int isCheck,String id,int page,int pageSize){
        Map<String,Object> map = new HashMap<String, Object>();
        List<UnlawfulPictureTextEntry> entries = unlawfulPictureTextDao.selectContentList(isCheck,id,page,pageSize);
        int count = unlawfulPictureTextDao.getNumber(isCheck,id);
        List<UnlawfulPictureTextDTO> dtos = new ArrayList<UnlawfulPictureTextDTO>();
        for(UnlawfulPictureTextEntry entry : entries){
            dtos.add(new UnlawfulPictureTextDTO(entry));
        }
        map.put("list",dtos);
        map.put("count",count);
        return map;
    }

    public void passContentEntry(ObjectId id){
        unlawfulPictureTextDao.passContentEntry(id);
    }

    public void deleteContentEntry(ObjectId id){
        unlawfulPictureTextDao.deleteContentEntry(id);
        UnlawfulPictureTextEntry entry = unlawfulPictureTextDao.getEntryById(id);
        if(entry != null) {

        }

    }


}
