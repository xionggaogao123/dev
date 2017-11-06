package com.fulaan.controlphone.service;

import com.db.controlphone.ControlPhoneDao;
import com.fulaan.controlphone.dto.ControlPhoneDTO;
import com.pojo.controlphone.ControlPhoneEntry;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2017/11/3.
 */
@Service
public class ControlPhoneService {

    private ControlPhoneDao controlPhoneDao  = new ControlPhoneDao();


    public String addControlPhone(ControlPhoneDTO dto){
        ControlPhoneEntry entry = dto.buildAddEntry();
        String id = "";
        if(entry != null){
            id = controlPhoneDao.addEntry(entry);
        }
        return id;
    }

    public List<ControlPhoneDTO> getControlPhoneList(ObjectId parentId,ObjectId sonId){
        List<ControlPhoneDTO> dtos = new ArrayList<ControlPhoneDTO>();
        List<ControlPhoneEntry> entries = controlPhoneDao.getEntryListByparentIdAndUserId(parentId, sonId);
        if(entries.size()>0){
            for(ControlPhoneEntry entry : entries){
                dtos.add(new ControlPhoneDTO(entry));
            }
        }
        return dtos;
    }

    public void  delControlPhone(ObjectId id){
        controlPhoneDao.delEntry(id);
    }


    public void updateControlPhone(ObjectId id,String name,String phone){
        controlPhoneDao.updateEntry(name,phone,id);
    }

}
