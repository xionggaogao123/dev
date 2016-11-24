package com.fulaan.docflow.service;


import com.db.docflow.DocFlowDao;
import com.db.educationbureau.EducationBureauDao;
import com.db.school.DepartmentDao;
import com.db.school.SchoolDao;
import com.db.user.UserDao;
import com.fulaan.docflow.dto.SimpleSchoolDTO;
import com.fulaan.user.service.UserService;
import com.pojo.docflow.*;
import com.pojo.educationbureau.EducationBureauEntry;
import com.pojo.school.DepartmentEntry;
import com.pojo.school.SchoolEntry;
import com.pojo.user.SimpleUserInfo;
import com.pojo.user.UserEntry;
import com.pojo.user.UserRole;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by qiangm on 2015/8/17.
 */

public class DocFlowService {
    private DocFlowDao docFlowDao=new DocFlowDao();
    private DepartmentDao departmentDao=new DepartmentDao();
    private UserDao userDao=new UserDao();
    private EducationBureauDao educationBureauDao = new EducationBureauDao();
    private SchoolDao schoolDao = new SchoolDao();
    private UserService userService = new UserService();
    /**
     * 根据条件统计公文总数
     *
     * @param userId
     * @param term
     * @param isHandle
     * @param keyWords
     * @param type
     * @return
     */
    public int getDocFlowCount(ObjectId userId,String term, Boolean isHandle, String keyWords,int type) {
        return docFlowDao.getDocFlowCount(userId, term, isHandle, keyWords, type,getDepartmentList(userId));
    }
    public int getDocFlowCountEdu(ObjectId userId,String term, String schoolIds, String keyWords,int type) {
        ObjectId educationId=educationBureauDao.selEducationByUserId(userId).getID();
        String[] arr=schoolIds.split(",");
        List<ObjectId> schoolIdList=new ArrayList<ObjectId>();
        for (String str:arr)
        {
            schoolIdList.add(new ObjectId(str));
        }
        return docFlowDao.getDocFlowCountEdu(educationId, term, schoolIdList, keyWords, type);
    }
    /**
     * 分页获取公文
     * @param term 学期
     * @param isHandle
     * @param keyWords
     * @param page
     * @param pageSize
     * @return
     */
    public List<SimpleDocFlowDTO> getDocFlowList(ObjectId userId,String term, Boolean isHandle, String keyWords, int page, int pageSize,int type) {
        List<DocFlowEntry> docFlowEntries=new ArrayList<DocFlowEntry>();
        if(type==0)
            docFlowEntries=docFlowDao.getPublishedDocFlowList(userId,term,keyWords,page,pageSize,getDepartmentList(userId));
        else if(type==1)
            docFlowEntries=docFlowDao.getDocFlowList(userId,term,isHandle,keyWords,page,pageSize);
        else if(type==2)
            docFlowEntries=docFlowDao.getMyDocFlowList(userId,term,keyWords,page,pageSize);
        List<SimpleDocFlowDTO> docFlowDTOs=new ArrayList<SimpleDocFlowDTO>();
        if(docFlowEntries!=null&&!docFlowEntries.isEmpty()) {
            for (DocFlowEntry docFlowEntry : docFlowEntries) {
                docFlowDTOs.add(new SimpleDocFlowDTO(docFlowEntry));
            }
        }
        return docFlowDTOs;
    }

    /**
     * 分页获取公文-----教育局使用
     * @param userId
     * @param term
     * @param schoolIds
     * @param keyWords
     * @param page
     * @param pageSize
     * @param type
     * @return
     */
    public List<SimpleDocFlowDTO> getDocFlowListEdu(ObjectId userId,String term, String schoolIds, String keyWords, int page, int pageSize,int type) {
        List<DocFlowEntry> docFlowEntries=new ArrayList<DocFlowEntry>();
        ObjectId educationId=educationBureauDao.selEducationByUserId(userId).getID();
        String[] arr=schoolIds.split(",");
        List<ObjectId> schoolIdList=new ArrayList<ObjectId>();
        for (String str:arr)
        {
            schoolIdList.add(new ObjectId(str));
        }
        if(type==0) {
            docFlowEntries = docFlowDao.getPublishedDocFlowListEdu(educationId, term, keyWords, page, pageSize, schoolIdList);
        }
        else if(type==1)
            docFlowEntries=docFlowDao.getSchoolDocFlowListEdu(term, keyWords, page, pageSize, schoolIdList.get(0));

        List<SimpleDocFlowDTO> docFlowDTOs=new ArrayList<SimpleDocFlowDTO>();
        if(docFlowEntries!=null&&!docFlowEntries.isEmpty()) {
            for (DocFlowEntry docFlowEntry : docFlowEntries) {
                docFlowDTOs.add(new SimpleDocFlowDTO(docFlowEntry));
            }
        }
        return docFlowDTOs;
    }

    /**
     * 添加公文
     * @param docFlowDTO
     * @return
     */
    public ObjectId addDocFlow(DocFlowDTO docFlowDTO)
    {
        DocFlowEntry docFlowEntry=docFlowDTO.export();
        return docFlowDao.addDocFlow(docFlowEntry);
    }

    /**
     * 修改公文
     * @param docFlowDTO
     * @param docCheckDTOs
     * @return
     */
    public boolean updateDocFlow(DocFlowDTO docFlowDTO,List<DocCheckDTO> docCheckDTOs,ObjectId stateId,int type)
    {
        List<DocCheckEntry> docCheckEntries=new ArrayList<DocCheckEntry>();
        for(DocCheckDTO docCheckDTO:docCheckDTOs)
        {
            docCheckEntries.add(docCheckDTO.export());
        }
        docFlowDao.updateDocFlow(docFlowDTO.exportSimple(),docCheckEntries,stateId,type);
        return false;
    }


    /**
     * 根据部门id列表获取部门名称
     * @param departmentIds
     * @return
     */
    public Map<String,String> getDepartmentsByDepIds(List<ObjectId> departmentIds)
    {
        Map<String,String> departmentMaps=new HashMap<String, String>();
        List<DepartmentEntry> departmentEntries=departmentDao.getDepartmentsByDepIds(departmentIds);
        if(departmentEntries!=null&&!departmentEntries.isEmpty())
        {
            for(DepartmentEntry departmentEntry : departmentEntries)
            {
                departmentMaps.put(departmentEntry.getID().toString(),departmentEntry.getName());
            }
        }
        return departmentMaps;
    }

    /**
     * 根据用户id列表返回id--姓名字典
     * @param userIds
     * @return
     */
    public Map<String,String> getUserNameByIds(List<ObjectId> userIds)
    {
        Map<String,String> returnMap=new HashMap<String, String>();
        Map<ObjectId, UserEntry> maps=userDao.getUserEntryMap(userIds, Constant.FIELDS);
        for (Map.Entry<ObjectId, UserEntry> entry : maps.entrySet()) {
            returnMap.put(entry.getKey().toString(),entry.getValue().getUserName());
        }
        return returnMap;
    }

    /**
     * 根据docId获取公文详情
     * @param docId
     * @return
     */
    public DocFlowDTO getDocDetail(ObjectId docId)
    {
        DocFlowEntry docFlowEntry=docFlowDao.getDocDetailById(docId);
        if(docFlowEntry!=null)
            return new DocFlowDTO(docFlowEntry);
        return null;
    }

    /**
     * 未读公文数量
     * @param term
     * @param userId
     * @return
     */
    public int getUnreadCount(String term,ObjectId userId)
    {
        return docFlowDao.getUnreadCount(term,userId,getDepartmentList(userId));
    }

    /**
     * 未审核数量
     * @param term
     * @param userId
     * @return
     */
    public int getUnCheckCount(String term,ObjectId userId)
    {
        return docFlowDao.getUnCheckCount(term, userId);
    }

    /**
     * 获取本人被驳回的公文数量
     * @param term
     * @param userId
     * @return
     */
    public int getRejectCount(String term,ObjectId userId)
    {
        return docFlowDao.getRejectCount(term,userId);
    }

    /**
     * 根据用户Id获取部门列表
     * @param userId
     * @return
     */
    public List<Map<String,String>> GetDepartmentList(ObjectId userId)
    {
        List<Map<String,String>> list=new ArrayList<Map<String, String>>();
        Map<String,String> map=new HashMap<String, String>();
        List<DepartmentEntry> departmentEntries=departmentDao.getDepartmentsByUserId(userId);
        if(departmentEntries!=null&&!departmentEntries.isEmpty())
        {
            for (DepartmentEntry departmentEntry : departmentEntries)
            {
                map=new HashMap<String, String>();
                map.put("id",departmentEntry.getID().toString());
                map.put("name",departmentEntry.getName());
                list.add(map);
                map.put(departmentEntry.getID().toString(),departmentEntry.getName());
            }
        }
        return list;
    }

    /**
     * 审阅公文
     * 流程，先根据docid找到该公文，将自己的审阅结果填充进去，再填入下一位审阅人信息
     * @param docId
     * @param handleType
     * @param receiveId
     * @param receiveDepartmentId
     * @param remark
     * @return
     */
    public boolean checkDoc(ObjectId docId, int handleType,ObjectId receiveId,
                            ObjectId receiveDepartmentId,String remark,List<IdUserFilePairDTO> fileList)
    {
        List<IdUserFilePair> files=new ArrayList<IdUserFilePair>();
        for(IdUserFilePairDTO idNameValuePairDTO:fileList)
        {
            files.add(new IdUserFilePair(new ObjectId(idNameValuePairDTO.getId()),new ObjectId(idNameValuePairDTO.getUserId()),
                    idNameValuePairDTO.getName(),
                    idNameValuePairDTO.getValue()));
        }
        return docFlowDao.checkDoc(docId,handleType,receiveId,receiveDepartmentId,remark,files);
    }

    /**
     * 撤销公文
     * @param docId
     * @return
     */
    public boolean revDoc(ObjectId docId,DocCheckDTO docCheckDTO)
    {
        return docFlowDao.revDoc(docId,docCheckDTO.export());
    }

    /**
     * 删除公文
     * @param docId
     * @return
     */
    public boolean delDoc(ObjectId docId)
    {
        return docFlowDao.delDoc(docId);
    }

    /**
     * 更新阅读人数
     * @param userId
     * @param docId
     */
    public void updateUnread(ObjectId userId,ObjectId docId)
    {
        docFlowDao.updateUnread(userId,docId);
    }

    /**
     * 根据公文作者查找作者所在学校的领导
     * @param userId
     * @return
     */
    public List<UserEntry> getSchoolLeader(ObjectId userId)
    {
        UserEntry userEntry = userDao.getUserEntry(userId, Constant.FIELDS);
        ObjectId schoolId = userEntry.getSchoolID();
        List<UserEntry> userEntryList=userDao.getSchoolLeader(schoolId);
        return userEntryList;
    }

    /**
     * 根据角色获取学校部门列表，即如果是校长或管理员，返回部门列表，其余返回空
     * @param userId
     * @return
     */
    public List<ObjectId> getDepartmentList(ObjectId userId)
    {
        UserEntry userEntry = userDao.getUserEntry(userId, Constant.FIELDS);
        int role = userEntry.getRole();
        if (UserRole.isHeadmaster(role) || UserRole.isManager(role)) {//如果是校长或管理员，只获取本学校部门发布的公文
            ObjectId schoolId = userEntry.getSchoolID();
            List<DepartmentEntry> departmentList = departmentDao.getDepartmentEntrys(schoolId);
            List<ObjectId> departmentIds = new ArrayList<ObjectId>();
            if (departmentList != null && !departmentList.isEmpty()) {
                for (DepartmentEntry departmentEntry : departmentList) {
                    departmentIds.add(departmentEntry.getID());
                }
            }
            return departmentIds;
        } else {
            return new ArrayList<ObjectId>();
        }
    }
    //查询一个教育局下属的所有学校校长
    public List<SimpleUserInfo> findAllHeadMasterList(String userId)
    {
        EducationBureauEntry educationBureauEntry=educationBureauDao.selEducationByUserId(new ObjectId(userId));
        List<ObjectId> schoolIds=educationBureauEntry.getSchoolIds();
        List<SimpleUserInfo> simpleUserInfos=new ArrayList<SimpleUserInfo>();
        List<SchoolEntry> schoolEntryList=schoolDao.getSchoolEntryList(schoolIds);

        List<UserEntry> userEntryList = userDao.getHeadmasterBySchoolIdsList(schoolIds, Constant.FIELDS);
        //List<UserEntry> userEntryList = userDao.getUserEntryBySchoolIdsList(schoolIds, Constant.FIELDS);
        //List<UserInfoDTO> userInfoDTOList=userService.findUserInfoBySchoolIds(schoolIds);
        for (UserEntry userEntry:userEntryList)
        {
            if(userEntry.getIsRemove()==0) {
                if (UserRole.isHeadmaster(userEntry.getRole())) {
                    SimpleUserInfo simpleUserInfo = new SimpleUserInfo(userEntry);
                    String schoolName = "";
                    for (SchoolEntry schoolEntry : schoolEntryList) {
                        if (schoolEntry.getID().equals(userEntry.getSchoolID())) {
                            schoolName = schoolEntry.getName();
                            break;
                        }
                    }
                    simpleUserInfo.setName(schoolName + "校长：" + simpleUserInfo.getName());
                    simpleUserInfos.add(simpleUserInfo);
                }
            }
        }

        return simpleUserInfos;
    }
    //查询一个教育局下面的所有学校
    public List<SimpleSchoolDTO> findAllSchoolList(String userId)
    {
        EducationBureauEntry educationBureauEntry=educationBureauDao.selEducationByUserId(new ObjectId(userId));
        List<ObjectId> schoolIds=educationBureauEntry.getSchoolIds();
        List<SchoolEntry> schoolEntryList=schoolDao.getSchoolEntryList(schoolIds);
        List<SimpleSchoolDTO> schoolDTOs=new ArrayList<SimpleSchoolDTO>();
        for (SchoolEntry schoolEntry:schoolEntryList)
        {
            schoolDTOs.add(new SimpleSchoolDTO(schoolEntry));
        }
        return schoolDTOs;
    }
}
