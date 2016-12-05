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
        for(RegionEntry regionEntry:entries){
            dtos.add(new RegionDTO(regionEntry));
        }
        return dtos;
    }
}
