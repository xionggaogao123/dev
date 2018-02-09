package com.fulaan.jiaschool.service;

import com.db.backstage.LogMessageDao;
import com.db.fcommunity.CommunityDao;
import com.db.jiaschool.HomeSchoolDao;
import com.db.jiaschool.SchoolCommunityDao;
import com.fulaan.backstage.dto.LogMessageDTO;
import com.fulaan.community.dto.CommunityDTO;
import com.fulaan.jiaschool.dto.HomeSchoolDTO;
import com.fulaan.jiaschool.dto.SchoolCommunityDTO;
import com.pojo.backstage.LogMessageType;
import com.pojo.fcommunity.CommunityEntry;
import com.pojo.jiaschool.HomeSchoolEntry;
import com.pojo.jiaschool.SchoolCommunityEntry;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by James on 2018/1/30.
 */
@Service
public class HomeSchoolService {

    private HomeSchoolDao homeSchoolDao = new HomeSchoolDao();

    private LogMessageDao logMessageDao = new LogMessageDao();

    private CommunityDao communityDao = new CommunityDao();

    private SchoolCommunityDao schoolCommunityDao= new SchoolCommunityDao();


    public Map<String,Object> getSchoolList(int schoolType,int page,int pageSize,String keyword){
        Map<String,Object> map = new HashMap<String, Object>();
        List<HomeSchoolEntry> entries = homeSchoolDao.getReviewList(schoolType,page,pageSize,keyword);
        List<HomeSchoolDTO> dtos = new ArrayList<HomeSchoolDTO>();
        for(HomeSchoolEntry entry : entries){
            dtos.add(new HomeSchoolDTO(entry));
        }
        int count = homeSchoolDao.getListCount(schoolType,page,pageSize,keyword);
        map.put("list",dtos);
        map.put("count",count);
        return map;
    }



    public String addNewSchoolEntry(HomeSchoolDTO dto){
        if(dto.getId() != null &&!dto.getId().equals("")){
            ObjectId oid = homeSchoolDao.addEntry(dto.updateEntry());
            return oid.toString();
        }else{
            int count = homeSchoolDao.getSortCount();
            dto.setSort(count+1000);
            ObjectId oid = homeSchoolDao.addEntry(dto.buildAddEntry());
            return oid.toString();
        }

    }

    public void delNewSchoolEntry(ObjectId id,ObjectId userId){
       HomeSchoolEntry entry =  homeSchoolDao.getEntryById(id);
        if(entry!=null){
            homeSchoolDao.delEntry(id);
            this.addLogMessage(id.toString(),"删除了学校"+entry.getName(), LogMessageType.school.getDes(),userId.toString());
        }
    }

    public CommunityDTO selectNewCommunityEntry(String searchId){
        CommunityEntry communityEntry = communityDao.findBySearchId(searchId);
        if(communityEntry!=null) {
            CommunityDTO communityDTO = new CommunityDTO(communityEntry);
            SchoolCommunityEntry schoolCommunityEntry = schoolCommunityDao.getEntryById(communityEntry.getID());
            if (schoolCommunityEntry != null) {//已绑定
                HomeSchoolEntry homeSchoolEntry = homeSchoolDao.getEntryById(schoolCommunityEntry.getSchoolId());
                communityDTO.setMemberCount(1);
                communityDTO.setOwerName(homeSchoolEntry.getName());
            }else{//未绑定
                communityDTO.setMemberCount(2);
                communityDTO.setOwerName("");
            }
            return communityDTO;
        }
        return null;
    }

    public void addSchoolSort(String communityId,String schoolId){
        SchoolCommunityEntry schoolCommunityEntry = schoolCommunityDao.getEntryById(new ObjectId(communityId));
        if(schoolCommunityEntry==null){
            SchoolCommunityDTO dto = new SchoolCommunityDTO();
            dto.setCommunityId(communityId);
            dto.setSchoolId(schoolId);
            schoolCommunityDao.addEntry(dto.buildAddEntry());
        }
    }

    public void delSchoolSort(ObjectId communityId){
        SchoolCommunityEntry schoolCommunityEntry = schoolCommunityDao.getEntryById(communityId);
        if(schoolCommunityEntry!=null){
            schoolCommunityDao.delEntry(communityId);
        }
    }

    public void addLogMessage(String contactId,String content,String function,String userId){
        LogMessageDTO dto = new LogMessageDTO();
        dto.setType(1);
        dto.setContactId(contactId);
        dto.setContent(content);
        dto.setFunction(function);
        dto.setUserId(userId);
        logMessageDao.addEntry(dto.buildAddEntry());
    }

}