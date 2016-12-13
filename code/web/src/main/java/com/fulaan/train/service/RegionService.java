package com.fulaan.train.service;

import com.db.train.RegionDao;
import com.fulaan.train.dto.RegionDTO;
import com.pojo.train.RegionEntry;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/12/2.
 */
@Service
public class RegionService {

    private RegionDao regionDao=new RegionDao();

    public List<RegionDTO> getRegionList(int level,ObjectId parentId){
        List<RegionDTO> dtos=new ArrayList<RegionDTO>();
        List<RegionEntry> entries=regionDao.getRegionEntries(level,parentId);
        if(level==2) {
            for (RegionEntry regionEntry : entries) {
                RegionDTO regionDTO = new RegionDTO(regionEntry);
                regionDTO.setName(regionDTO.getName().substring(0,2));
                dtos.add(regionDTO);
            }
        }else{
            for (RegionEntry regionEntry : entries) {
                dtos.add(new RegionDTO(regionEntry));
            }
        }
        return dtos;
    }

    public RegionEntry getRegionEntry(String name){
        return regionDao.getRegionEntry(name);
    }

    public void setSort(ObjectId id,int sort){
        regionDao.setSort(id, sort);
    }

}
