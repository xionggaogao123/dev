package com.fulaan.elect.service;

import com.db.elect.ElectDao;
import com.fulaan.elect.dto.ElectDTO;
import com.mongodb.BasicDBList;
import com.pojo.app.FieldValuePair;
import com.pojo.elect.Candidate;
import com.pojo.elect.ElectEntry;
import com.sys.exceptions.IllegalParamException;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by qinbo on 15/2/28.
 * 投票选举
 * @author qinbo
 */
@Service
public class ElectService {


    private static final Logger logger= Logger.getLogger(ElectService.class);
    
    private ElectDao electDao = new ElectDao();
    private String electId;

    /** 查询选举 ， 按照学校id和所在班级id列表
     * @param schoolId 学校id
     * @param classIds 老师或学生所在的班级列表
     * @param page 页数，0开始。
     * @param size 每页数量
     * @return 所有当前用户可见的选举信息列表
     * @throws Exception
     */
    public List<ElectEntry> findElectsBySchoolId(String schoolId,List<ObjectId> classIds,ObjectId userId,int page,int size) throws Exception
    {
        if(!ObjectId.isValid(schoolId))
        {
            throw new IllegalParamException("the id ["+schoolId+" ] is valid!!");
        }
        
        List<ElectEntry> elects = electDao.getElectEntryBySchoolId(new ObjectId(schoolId),classIds,page*size,size);

        //处理数据，将可显示的publish置为0，不可显示的置为1
        for (ElectEntry electEntry:elects)
        {
            Calendar currentDate = new GregorianCalendar();
            currentDate.set(Calendar.HOUR_OF_DAY, 0);
            currentDate.set(Calendar.MINUTE, 0);
            currentDate.set(Calendar.SECOND, 0);
            long time=currentDate.getTimeInMillis()-100000;
            if(electEntry.getEndTime()>=time)//未结束，若为发起人，publish为0，其余为1
            {
                if(electEntry.getPublish()==0)
                {
                    electEntry.setPublish(0);
                }
                else {
                    if (electEntry.getPublisherId().equals(userId)) {
                        electEntry.setPublish(10);
                    }
                    else
                    {
                        electEntry.setPublish(11);
                    }
                }
            }
            else//已结束,若公布，publish不变；若为发起人，publish为0，其余为1
            {
                if(electEntry.getPublish()==0)
                {
                    electEntry.setPublish(0);
                }
                else if(electEntry.getPublish()==1 && electEntry.getPublisherId().equals(userId))
                {
                    electEntry.setPublish(10);
                }
                else
                {
                    electEntry.setPublish(11);
                }
            }
        }
        
        if(elects.isEmpty())
        {
            logger.info("can not find ElectEntry;schoolId=" + schoolId);
        }
        return elects;
    }


    /** 读取当前用户可见的选举数量
     * @param schoolId 学校id
     * @param classIds 老师或学生所在的班级列表
     * @return 数量
     * @throws Exception
     */
    public int getElectCountBySchoolId(String schoolId, List<ObjectId> classIds)throws Exception
    {
        if(!ObjectId.isValid(schoolId))
        {
            throw new IllegalParamException("the id ["+schoolId+" ] is valid!!");
        }

        return electDao.getElectCountbySchoolId(new ObjectId(schoolId),classIds);
    }

    /** 数据库中增加一个选举
     * @param electEntry 要增加的选举
     */
    public ObjectId addElect(ElectEntry electEntry)
    {
        return electDao.add(electEntry);
    }


    /** 增加竞选人的实现，如果cands为null，则先创建一个
     * @param candidate 要增加的竞选人信息
     * @param electId 竞选人竞选的选举
     * @throws IllegalParamException
     */
    public void addCandidateToElect(Candidate candidate,String electId,ObjectId userId)throws IllegalParamException{
        if(!ObjectId.isValid(electId))
        {
            throw new IllegalParamException("the id ["+electId+" ] is valid!!");
        }


        ElectEntry electEntry = findOne(electId,userId);
        if(electEntry.getCandidates() == null)
        {
            electEntry.setCandidateList(new ArrayList<Candidate>());
            FieldValuePair fvpCands= new FieldValuePair("cands",new BasicDBList());
            electDao.update(new ObjectId(electId),fvpCands);
        }



        electDao.addCandidate(new ObjectId(electId),candidate);
    }

    /** 删除选举
     * @param electId 要删除的选举的id
     */
    public void removeElectById (String electId) throws IllegalParamException{
        if(!ObjectId.isValid(electId))
        {
            throw new IllegalParamException("the id ["+electId+" ] is valid!!");
        }
        electDao.remove(new ObjectId(electId));
    }

    /** 更新一个选举的基本信息，name,description,enddate,classids
     * @param electDTO
     * @return
     * @throws IllegalParamException
     */
    public ElectEntry updateElectBasicInfo(ElectDTO electDTO) throws  IllegalParamException{
        if(!ObjectId.isValid(electDTO.getId()))
        {
            throw new IllegalParamException("the id ["+electDTO.getId()+" ] is valid!!");
        }
        ObjectId electId = new ObjectId(electDTO.getId());

        FieldValuePair fvpName = new FieldValuePair("nm",electDTO.getName());
        FieldValuePair fvpDescription = new FieldValuePair("desc" , electDTO.getDescription());
        FieldValuePair fvpEndDate = new FieldValuePair("edt",electDTO.getEndDate().getTime());
        //todo: 不能把arrylist直接作为dblist传
        FieldValuePair fvpClassIds = new FieldValuePair("cls",electDTO.getClassIds());
        electDao.update(electId, fvpName,fvpDescription,fvpEndDate,fvpClassIds);

        ElectEntry electEntry = electDao.findOne(electId);

        return electEntry;

    }

    /** 更新竞选人信息
     * @param candidate 要更新的竞选人信息，已填充到该实体中
     * @param electId 选举id
     * @return 更新后的选举
     * @throws IllegalParamException
     */
    public ElectEntry updateCandidate(Candidate candidate,String electId,ObjectId userId) throws IllegalParamException{
        if(!ObjectId.isValid(electId))
        {
            throw new IllegalParamException("the id ["+electId+" ] is valid!!");
        }
        ElectEntry electEntry = findOne(electId,userId);
        List<Candidate> candidateList = electEntry.getCandidates();
        for(Candidate ca : candidateList)
        {
            if(ca.getUserId().equals(candidate.getUserId())){
                ca.setManifesto(candidate.getManifesto());
                ca.setPicUrls(candidate.getPicUrls());
                ca.setVideoId(candidate.getVideoId());
                ca.setVoiceUrl(candidate.getVoiceUrl());

            }
        }
        BasicDBList dbList = new BasicDBList();
        for(Candidate cand : candidateList)
        {
            dbList.add(cand.getBaseEntry());
        }

        FieldValuePair fvpCands= new FieldValuePair("cands",dbList);
        electDao.update(new ObjectId(electId),fvpCands);
        return electEntry;
    }


    /**  整体更新候选人列表
     * @param candidateList 要更新的候选人列表信息
     * @param electId 要更新的选举id
     * @return 更新后的选举
     * @throws IllegalParamException
     */
    public ElectEntry updateCandidateList(List<Candidate> candidateList,
                                          String electId,ObjectId userId) throws IllegalParamException{
        if(!ObjectId.isValid(electId))
        {
            throw new IllegalParamException("the id ["+electId+" ] is valid!!");
        }

        BasicDBList dbList = new BasicDBList();
        for(Candidate cand : candidateList)
        {
            dbList.add(cand.getBaseEntry());
        }

        FieldValuePair fvpCands= new FieldValuePair("cands",dbList);
        electDao.update(new ObjectId(electId),fvpCands);
        return findOne(electId,userId);
    }


    /** 移除竞选人
     * @param electId 选举id
     * @param candidateId 竞选人id
     * @return
     * @throws IllegalParamException
     */
    public ElectEntry removeCandidate(String electId,String candidateId) throws  IllegalParamException{
        if(!ObjectId.isValid(electId) || !ObjectId.isValid(candidateId))
        {
            throw new IllegalParamException("the id ["+electId+" ] is valid!!");
        }
        ElectEntry electEntry = findOne(electId,new ObjectId(candidateId));
        List<Candidate> candidateList = electEntry.getCandidates();
        for(Candidate ca : candidateList)
        {
            if(ca.getUserId().toString().equals(candidateId)){
                candidateList.remove(ca);
                break;

            }
        }
        BasicDBList dbList = new BasicDBList();
        for(Candidate cand : candidateList)
        {
            dbList.add(cand.getBaseEntry());
        }
        FieldValuePair fvpCands= new FieldValuePair("cands",dbList);
        electDao.update(new ObjectId(electId),fvpCands);

        return electEntry;
    }

    /**  根据id查找选举
     * @param electId
     * @return
     * @throws IllegalParamException
     */
    public ElectEntry findOne(String electId,ObjectId userId)throws  IllegalParamException{
        this.electId = electId;

        if(!ObjectId.isValid(electId))
        {
            throw new IllegalParamException("the id ["+electId+" ] is valid!!");
        }
        ElectEntry electEntry = electDao.findOne(new ObjectId(electId));

        Calendar currentDate = new GregorianCalendar();
        currentDate.set(Calendar.HOUR_OF_DAY, 0);
        currentDate.set(Calendar.MINUTE, 0);
        currentDate.set(Calendar.SECOND, 0);
        long time=currentDate.getTimeInMillis()-100000;
        if(electEntry.getEndTime()>=time)//未结束，若为发起人，publish为0，其余为1
        {
            if(electEntry.getPublish()==0)
            {
                electEntry.setPublish(0);
            }
            else {
                if (electEntry.getPublisherId().equals(userId)) {
                    electEntry.setPublish(10);
                }
                else
                {
                    electEntry.setPublish(11);
                }
            }
        } else//已结束,若公布，publish不变；若为发起人，publish为0，其余为1
        {
            if(electEntry.getPublish()==0)
            {
                electEntry.setPublish(0);
            }
            else if(electEntry.getPublish()==1 && electEntry.getPublisherId().equals(userId))
            {
                electEntry.setPublish(10);
            }
            else
            {
                electEntry.setPublish(11);
            }
        }

        return electEntry;
    }

}
