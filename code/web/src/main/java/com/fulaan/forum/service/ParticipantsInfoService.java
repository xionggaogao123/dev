package com.fulaan.forum.service;

import com.db.forum.ParticipantsInfoDao;
import com.pojo.forum.ParticipantsInfoDTO;
import com.pojo.forum.ParticipantsInfoEntry;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by admin on 2017/1/11.
 */
@Service
public class ParticipantsInfoService {

    private ParticipantsInfoDao participantsInfoDao=new ParticipantsInfoDao();


    public boolean saveParticipateInfo(String name, String relation, int sex, int age,String school,ObjectId creator){

        if(validateRelation(relation)){
            ObjectId participator=new ObjectId();
            ParticipantsInfoEntry entry=new ParticipantsInfoEntry(creator,participator,name,age,sex,relation,school);
            participantsInfoDao.saveOrUpdate(entry);
            return true;
        }else{
            return false;
        }


    }

    public List<ParticipantsInfoDTO> getParticipates(ObjectId creator){
        List<ParticipantsInfoDTO> participantsInfoDTOs=new ArrayList<ParticipantsInfoDTO>();
        List<ParticipantsInfoEntry> entries=participantsInfoDao.getEntries(creator);
        for(ParticipantsInfoEntry entry:entries){
            participantsInfoDTOs.add(new ParticipantsInfoDTO(entry));
        }
        return participantsInfoDTOs;
    }

    public boolean validateRelation(String relation){
        Pattern emailPattern = Pattern.compile("^.+@.+\\..+$");
        Pattern phonePattern = Pattern.compile("^1[3|4|5|7|8][0-9]\\d{8}$");
        Matcher emailMatcher = emailPattern.matcher(relation);
        Matcher phoneMatcher = phonePattern.matcher(relation);
        return emailMatcher.matches()||phoneMatcher.matches();
    }

    public boolean updateParticipateInfo(ObjectId id,String name, String relation, int sex, int age,String school){
        if(validateRelation(relation)){
            ParticipantsInfoEntry entry=participantsInfoDao.getEntry(id);
            if(null!=entry){
                if(StringUtils.isNotBlank(name)){
                    entry.setName(name);
                }
                if(StringUtils.isNotBlank(relation)){
                    entry.setRelation(relation);
                }
                if(-1!=sex){
                    entry.setSex(sex);
                }
                if(0!=age){
                    entry.setAge(age);
                }
                if(StringUtils.isNotBlank(school)){
                    entry.setSchool(school);
                }
                participantsInfoDao.saveOrUpdate(entry);
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }


    public void removeParticipateInfo(ObjectId participateId){
        participantsInfoDao.removeInfo(participateId);
    }

}
