package com.fulaan.schoolsecurity.service;

import com.db.schoolsecurity.SchoolSecurityDao;
import com.pojo.schoolsecurity.SchoolSecurityEntry;
import com.pojo.utils.DeleteState;
import com.sys.exceptions.ResultTooManyException;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by guojing on 2015/6/18.
 */

@Service
public class SchoolSecurityService {

    private static final Logger logger= Logger.getLogger(SchoolSecurityService.class);

    private SchoolSecurityDao schoolSecurityDao = new SchoolSecurityDao();

    /**
     * 发表校园安全
     * @param schoolSecurityEntry
     */
    public ObjectId addSchoolSecurity(SchoolSecurityEntry schoolSecurityEntry) {
        return schoolSecurityDao.addSchoolSecurity(schoolSecurityEntry);
    }

    /**
     * 删除校园安全
     * @param id
     * @param userId
     */
    public void deleteSchoolSecurityInfo(String id, ObjectId userId) {
        schoolSecurityDao.deleteSchoolSecurity(new ObjectId(id), userId,new Date().getTime());
    }

    /**
     * 批量删除校园安全
     * @param ids
     * @param userId
     */
    public void batchDeleteSchoolSecurity(List<String> ids, ObjectId userId) {
        List<ObjectId> list=new ArrayList<ObjectId>();
        for(String id:ids){
            list.add(new ObjectId(id));
        }
        schoolSecurityDao.batchDeleteSchoolSecurity(list, userId, new Date().getTime());
    }

    /**
     * 处理校园安全
     * @param id
     * @param userId
     */
    public void handleSchoolSecurity(String id, ObjectId userId) {
        schoolSecurityDao.handleSchoolSecurity(new ObjectId(id), userId,new Date().getTime());
    }

    /**
     * 校园安全信息数量
     * @param handleState
     * @param schoolId
     * @return
     */
    public int selSchoolSecurityCount(int handleState, String schoolId) {
        return schoolSecurityDao.getSchoolSecurityCount(handleState, schoolId);
    }

    /**
     * 校园安全信息集合
     * @param handleState
     * @param userId
     * @param schoolId
     * @param page
     * @param pageSize
     * @return
     */
    public List<SchoolSecurityEntry> selSchoolSecurityInfo(int handleState, ObjectId userId, String schoolId, int page, int pageSize) throws ResultTooManyException {
        List<SchoolSecurityEntry> list = schoolSecurityDao.getSchoolSecurityEntryList(handleState,userId, DeleteState.NORMAL, schoolId,page < 1 ? 0 : ((page - 1) * pageSize),pageSize);
        return list;
    }

    /**
     * 查询单条校园安全
     * @param id
     * @return
     */
    public SchoolSecurityEntry selOneSchoolSecurityInfo(String id) {
        return schoolSecurityDao.getOneSchoolSecurityInfo(new ObjectId(id), null);
    }
}
