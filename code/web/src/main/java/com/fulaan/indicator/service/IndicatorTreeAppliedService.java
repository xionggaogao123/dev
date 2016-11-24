package com.fulaan.indicator.service;

import com.db.indicator.IndicatorTreeAppliedDao;
import com.db.school.InterestClassDao;
import com.db.user.UserDao;
import com.fulaan.indicator.dto.IndicatorTreeAppliedDTO;
import com.fulaan.indicator.dto.IndicatorTreeSnapshotDTO;
import com.fulaan.utils.RestAPIUtil;
import com.mongodb.BasicDBObject;
import com.pojo.indicator.IndicatorTreeAppliedEntry;
import com.pojo.school.InterestClassDTO;
import com.pojo.school.InterestClassEntry;
import com.pojo.user.UserEntry;
import com.pojo.user.UserRole;
import com.sys.constants.Constant;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by guojing on 2016/11/14.
 */
@Service
public class IndicatorTreeAppliedService {

    private static final Logger logger = Logger.getLogger(IndicatorTreeAppliedService.class);

    private IndicatorTreeAppliedDao iTreeAppliedDao = new IndicatorTreeAppliedDao();

    private UserDao userDao=new UserDao();

    private InterestClassDao interestClassDao =new InterestClassDao();

    public ObjectId addITreeApplied(IndicatorTreeAppliedDTO dto) {
        String[] egIds = dto.getEvaluatedGroupIdsStr().split(",");
        dto.setEvaluatedGroupIds(Arrays.asList(egIds));
        String[] evaIds = dto.getEvaluaterIdsStr().split(",");
        dto.setEvaluaterIds(Arrays.asList(evaIds));
        IndicatorTreeSnapshotDTO itsDto = new  IndicatorTreeSnapshotDTO();
        itsDto.setTreeId(dto.getTreeId());
        itsDto.setSchoolId(dto.getSchoolId());
        itsDto.setCreaterId(dto.getCreaterId());
        itsDto.setDescribe(dto.getDescribe());
        String snapshotId = RestAPIUtil.createIndicatorTreeSnapshot(itsDto);
        dto.setSnapshotId(snapshotId);
        IndicatorTreeAppliedEntry entry=dto.buildIndicatorTreeAppliedEntry();
        ObjectId id = iTreeAppliedDao.addITreeAppliedEntry(entry);
        return id;
    }

    public void editITreeApplied(IndicatorTreeAppliedDTO dto) {
        IndicatorTreeAppliedEntry entry=dto.buildUpdIndicatorTreeAppliedEntry();
        entry.setID(new ObjectId(dto.getId()));
        iTreeAppliedDao.updITreeAppliedEntry(entry);
    }

    public Map<String, Object> getITreeAppliedPageListMap(ObjectId schoolId, int role, ObjectId userId, String name, int page, int pageSize) {
        Map<String, Object> map=new HashMap<String, Object>();
        int total = iTreeAppliedDao.getITreeAppliedCountByParam(schoolId, role, userId, name);
        int skip = page < 1 ? 0 : ((page - 1) * pageSize);
        List<IndicatorTreeAppliedEntry> list = iTreeAppliedDao.getITreeAppliedListByParam(schoolId, role, userId, name, skip, pageSize);
        List<ObjectId> uids = new ArrayList<ObjectId>();
        for(IndicatorTreeAppliedEntry entry : list){
            uids.add(entry.getCreaterId());
        }
        Map<ObjectId, UserEntry> userMap = userDao.getUserEntryMap(uids, new BasicDBObject("nm",1));
        List<IndicatorTreeAppliedDTO> rows = new ArrayList<IndicatorTreeAppliedDTO>();
        UserEntry userEntry = null;
        for(IndicatorTreeAppliedEntry entry : list){
            userEntry=userMap.get(entry.getCreaterId());
            IndicatorTreeAppliedDTO dto = new IndicatorTreeAppliedDTO(entry);
            if(userEntry != null) {
                dto.setCreaterName(userEntry.getUserName());
            }
            if(UserRole.isManager(role)||userId.equals(entry.getCreaterId())){
                dto.setIsHandle(true);
            }else{
                dto.setIsHandle(false);
            }
            rows.add(dto);
        }
        map.put("total", total);
        map.put("page", page);
        map.put("pageSize", pageSize);
        map.put("rows", rows);
        return map;
    }


    public List<InterestClassDTO> getInterestClassList(String id) {
        IndicatorTreeAppliedEntry entry = iTreeAppliedDao.getITreeAppliedById(new ObjectId(id));
        List<InterestClassDTO> reList = new ArrayList<InterestClassDTO>();
        if(entry!=null){
            List<InterestClassEntry> list = interestClassDao.findInterestClassEntrysByIds(entry.getEvaluateGroupIds(), Constant.FIELDS);
            List<ObjectId> userIds = new ArrayList<ObjectId>();
            for (InterestClassEntry item : list) {
                try {
                    InterestClassDTO dto = new InterestClassDTO(item);
                    dto.setTermType(entry.getTermType());
                    reList.add(dto);
                    userIds.add(item.getTeacherId());
                } catch (Exception ex) {
                    logger.error("", ex);
                }
            }
            /*构建userMap  用于注入userName属性*/
            Map<ObjectId, UserEntry> userMap = userDao.getUserEntryMap(userIds, Constant.FIELDS);
            for (InterestClassDTO dto : reList) {
                if ( null!= dto.getTeacherId() &&   ObjectId.isValid(dto.getTeacherId())) {
                    UserEntry userEntry = userMap.get(new ObjectId(dto.getTeacherId()));
                    if (userEntry != null) {
                        dto.setTeacherName(userEntry.getUserName());
                        dto.setTeacherAvatar("http://7xiclj.com1.z0.glb.clouddn.com/" + userEntry.getAvatar());
                    }
                }
                dto.setStudentCount(dto.getStudentList().size());
            }
        }
        return reList;
    }

    public IndicatorTreeAppliedDTO getITreeAppliedInfo(String id) {
        IndicatorTreeAppliedEntry entry = iTreeAppliedDao.getITreeAppliedById(new ObjectId(id));
        IndicatorTreeAppliedDTO dto = new IndicatorTreeAppliedDTO(entry);
        return dto;
    }

    public void delITreeApplied(String id, int role, ObjectId userId) {
        iTreeAppliedDao.delITreeAppliedEntry(new ObjectId(id), role, userId);
    }
}
