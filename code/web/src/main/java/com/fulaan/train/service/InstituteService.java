package com.fulaan.train.service;

import com.db.train.InstituteDao;
import com.fulaan.train.dto.InstituteDTO;
import com.pojo.train.InstituteEntry;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/12/2.
 */
@Service
public class InstituteService {

    private InstituteDao instituteDao=new InstituteDao();


    /**
     * 获取分页列表
     * @param type
     * @param area
     * @param page
     * @param pageSize
     * @return
     */
    public List<InstituteDTO> getInstitutes(String type,String area,int page,int pageSize){
        List<InstituteDTO> dtos=new ArrayList<InstituteDTO>();
        List<InstituteEntry> entries=instituteDao.findInstituteEntries(type, area, page, pageSize);
        for(InstituteEntry instituteEntry:entries){
            dtos.add(new InstituteDTO(instituteEntry));
        }
        return dtos;
    }

    public int countInstitutes(String type,String area){
        return  instituteDao.countInstituteEntries(type, area);
    }


    public InstituteDTO findById(ObjectId id){
        InstituteEntry entry=instituteDao.findById(id);
        return new InstituteDTO(entry);
    }



}
