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



    public void saveOrUpdate(InstituteEntry entry){
        instituteDao.saveOrUpdate(entry);
    }
    /**
     * 获取分页列表
     * @param type
     * @param area
     * @param page
     * @param pageSize
     * @return
     */
    public List<InstituteDTO> getInstitutes(String regular,List<String> regionIds,List<String> itemTypeIds,String type,String area,int page,int pageSize,int sortType){
        List<InstituteDTO> dtos=new ArrayList<InstituteDTO>();
        List<InstituteEntry> entries=instituteDao.findInstituteEntries(regular,regionIds,itemTypeIds,type, area, page, pageSize,sortType);
        for(InstituteEntry instituteEntry:entries){
            InstituteDTO instituteDTO=new InstituteDTO(instituteEntry);
            String score=instituteDTO.getScore();
            int stip=(int)Double.parseDouble(score);
            int tip=0;
            List<Integer> tips=new ArrayList<Integer>();
            List<Integer> stips=new ArrayList<Integer>();
            for(int i=0;i<stip;i++){
                tips.add(tip);
            }
            for(int i=0;i<5-stip;i++){
                stips.add(tip);
            }
            instituteDTO.setScoreList(tips);
            instituteDTO.setUnScoreList(stips);
            dtos.add(instituteDTO);
        }
        return dtos;
    }

    public int countInstitutes(String regular,List<String> ids,List<String> itemTypeIds,String type,String area){
        return  instituteDao.countInstituteEntries(regular,ids,itemTypeIds,type, area);
    }


    public InstituteDTO findById(ObjectId id){
        InstituteEntry entry=instituteDao.findById(id);
        InstituteDTO instituteDTO=new InstituteDTO(entry);
        String score=instituteDTO.getScore();
        int stip=(int)Double.parseDouble(score);
        int tip=0;
        List<Integer> tips=new ArrayList<Integer>();
        List<Integer> stips=new ArrayList<Integer>();
        for(int i=0;i<stip;i++){
            tips.add(tip);
        }
        for(int i=0;i<5-stip;i++){
            stips.add(tip);
        }
        instituteDTO.setScoreList(tips);
        instituteDTO.setUnScoreList(stips);
        return instituteDTO;
    }

    public InstituteEntry find(ObjectId id){
        return instituteDao.findById(id);
    }



}
