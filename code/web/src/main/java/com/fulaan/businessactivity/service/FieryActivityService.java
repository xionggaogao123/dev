package com.fulaan.businessactivity.service;

import com.db.businessactivity.FieryActivityDao;
import com.fulaan.businessactivity.dto.FieryActivityDTO;
import com.pojo.businessactivity.FieryActivityEntry;
import com.pojo.user.UserRole;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by guojing on 2015/7/30.
 */
@Service
public class FieryActivityService {

    private static final Logger logger =Logger.getLogger(FieryActivityService.class);

    private FieryActivityDao fieryActivityDao =new FieryActivityDao();

    /**
     * 得到总数，用于分页
     * @return
     */
    public int getFieryActivityCount(ObjectId eduId, int role) {
        List<Integer> roles=getRoles(role);
        int count=fieryActivityDao.getFieryActivityCount(eduId, role, roles);
        return count;
    }

    private List<Integer> getRoles(int role) {
        Set<Integer> roles=new HashSet<Integer>();
        if(UserRole.isStudentOrParent(role)){
            roles.add(1);
            roles.add(3);
            roles.add(9);
            roles.add(11);
        }
        if(UserRole.isTeacher(role)){
            roles.add(2);
            roles.add(3);
            roles.add(10);
            roles.add(11);
        }
        if(UserRole.isHeadmaster(role)){
            roles.add(8);
            roles.add(9);
            roles.add(10);
            roles.add(11);
        }
        if(UserRole.isSysManager(role)){
            roles.add(0);
            roles.add(1);
            roles.add(2);
            roles.add(3);
            roles.add(8);
            roles.add(9);
            roles.add(10);
            roles.add(11);
        }
        return  new ArrayList<Integer>(roles);
    }

    /**
     * 得到火热活动集合，用于分页
     * @return
     */
    public List<FieryActivityDTO> getFieryActivitys(ObjectId eduId, int role, int skip, int limit) {
        List<FieryActivityDTO> retList =new ArrayList<FieryActivityDTO>();
        List<Integer> roles=getRoles(role);
        List<FieryActivityEntry> list= fieryActivityDao.getFieryActivitys(eduId, role, roles, skip, limit);
        FieryActivityDTO ne=null;
        for(FieryActivityEntry e:list)
        {
            try
            {
                ne=new FieryActivityDTO(e);
                retList.add(ne);
            }catch(Exception ex)
            {
                logger.error("", ex);
            }
        }
        return retList;
    }

    /**
     * 添加火热活动
     * @param e
     * @return
     */
    public ObjectId addFieryActivity(FieryActivityEntry e) {
        return fieryActivityDao.addFieryActivity(e);
    }

    /**
     * 火热活动详情
     * @param id
     * @return
     */
    public FieryActivityEntry getFieryActivityEntry(ObjectId id) {
        return fieryActivityDao.getFieryActivityEntry(id);
    }

    /**
     * 定时结束火热活动
     * @return
     */
    public void deleteFieryActivity(ObjectId id) {
        fieryActivityDao.deleteFieryActivity(id);
    }

    public void editFieryActivity(String id, FieryActivityEntry e) {
        fieryActivityDao.editFieryActivity(new ObjectId(id), e);
    }

    public List<FieryActivityDTO> getBusinessActivityImage(ObjectId eduId, int role, int skip, int limit) {
        List<FieryActivityDTO> retList =new ArrayList<FieryActivityDTO>();
        List<Integer> roles=getRoles(role);
        List<FieryActivityEntry> list= fieryActivityDao.getBusinessActivityImage(eduId, roles, skip, limit);
        FieryActivityDTO ne=null;
        for(FieryActivityEntry e:list)
        {
            try
            {
                ne=new FieryActivityDTO(e);
                retList.add(ne);
            }catch(Exception ex)
            {
                logger.error("", ex);
            }
        }
        return retList;
    }
}
