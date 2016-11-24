package com.fulaan.educationbureau.service;

import com.db.app.RegionDao;
import com.db.educationbureau.EducationBureauDao;
import com.fulaan.educationbureau.dto.EducationBureauDTO;
import com.fulaan.school.service.SchoolService;
import com.fulaan.user.service.UserService;
import com.pojo.app.RegionDTO;
import com.pojo.app.RegionEntry;
import com.pojo.educationbureau.EducationBureauEntry;
import com.pojo.examregional.EducationSubject;
import com.pojo.school.Grade;
import com.pojo.school.SchoolDTO;
import com.pojo.school.SchoolEntry;
import com.pojo.user.UserDetailInfoDTO;
import com.pojo.user.UserInfoDTO;
import com.pojo.user.UserRole;
import com.sys.constants.Constant;
import com.sys.exceptions.ResultTooManyException;
import com.sys.utils.DateTimeUtils;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by guojing on 2015/4/9.
 */
@Service
public class EducationBureauService {
    @Autowired
    private SchoolService schoolService;

    @Resource
    private UserService userService;

    private EducationBureauDao educationBureauDao =new EducationBureauDao();

    private RegionDao regionDao = new RegionDao();

  

    
    /**
     * 通过userId查询教育局
     * @param userId
     */
    public EducationBureauEntry selEducationByUserId(ObjectId userId) {
       return educationBureauDao.selEducationByUserId(userId);
    }


    /**
     * 添加教育局
     */
    public ObjectId addEducation(EducationBureauDTO educationBureauDTO) {
        ObjectId id=null;
        //判断教育局名称educationName是否已经存在
        if(!educationNameIsExist(educationBureauDTO.getId(), educationBureauDTO.getEducationName())) {//不存在时
            educationBureauDTO.setCreateTime(new Date());
            //添加一个教育局信息
            id = educationBureauDao.addEducation(educationBureauDTO.buildEducationEntry());
        }
        return id;
    }


    public void delEducation(ObjectId id) {
        educationBureauDao.delEducation(id);
    }

    /**
     * 通过userId查询教育局
     * @param eduUserId
     */
    public EducationBureauEntry selEducationByEduUserId(ObjectId eduUserId) {
        return educationBureauDao.selEducationByUserId(eduUserId);
    }

    /**
     * 通过userId查询教育局
     * @param userId
     */
    public EducationBureauEntry selEducationByUserId(ObjectId userId, int role, String schoolId) {
        if(UserRole.isEducation(role)||UserRole.isSysManager(role)){
            return selEducationByEduUserId(userId);
        }else{
            SchoolEntry schoolEntry=schoolService.getSchoolEntry(new ObjectId(schoolId),Constant.FIELDS);
            ObjectId sId=schoolEntry.getID();
            List<EducationBureauEntry> eduList=educationBureauDao.selEducationBySchoolId(sId);
            if(eduList!=null&&eduList.size()>0){
                return eduList.get(0);
            }
            return null;
        }
    }

    /**
     * 查询全部教育局
     */
    public List<EducationBureauDTO> selAllEducation() {
        List<EducationBureauDTO> list=new ArrayList<EducationBureauDTO>();
        //查询全部教育局
        List<EducationBureauEntry> EElist= educationBureauDao.selAllEducation();
        for(EducationBureauEntry item: EElist){
            EducationBureauDTO educationBureauDTO =new EducationBureauDTO(item);
            list.add(educationBureauDTO);
        }
        return list;
    }

    /**
     * 修改教育局名称
     */
    public void updateEducationInfo(EducationBureauEntry entry) {
            Date currTime=new Date();
            Long updateTime= currTime.getTime();
            entry.setUpdateTime(updateTime);
            educationBureauDao.updateEducationInfo(entry);
    }

    /**
     * 增加一个教育局用户
     */
    public void addEduUser(String id,String userId) {
        //根据用户id查询教育局信息
        EducationBureauEntry eduEntry=selEducationByUserId(userId);
        boolean flag=true;
        //根据教育局id查询教育局信息
        EducationBureauDTO eduDto=new EducationBureauDTO(selEducationById(id));
        if(eduDto.getUserIds()!=null&&eduDto.getUserIds().size()>0){
            Set<String> set = new HashSet(eduDto.getUserIds());
            //判断要添加的用户是否已经存在了
            if(set.contains(userId)) {
                flag=false;
            }
        }
        if(flag){//要添加的用户不存在时
            Date currTime=new Date();
            Long updateTime= currTime.getTime();
            //将用户添加到教育局中
            educationBureauDao.addEduUser(new ObjectId(id), new ObjectId(userId), updateTime);
        }

    }

    /**
     * 删除一个教育局用户
     */
    public void delEduUser(String id,String userId) {
        Date currTime=new Date();
        Long updateTime= currTime.getTime();
        //从教育局中删除一个用户信息
        educationBureauDao.delEduUser(new ObjectId(id), new ObjectId(userId), updateTime);
    }

    /**
     * 增加一批教育局用户
     */
    public void addEduUsers(String id,List<String> userIds) {
        //根据教育局id查询教育局信息
        EducationBureauDTO eduDto=new EducationBureauDTO(selEducationById(id));
        List<ObjectId> uids=new ArrayList<ObjectId>();
        Set<String> set =new HashSet<String>();
        if(eduDto.getUserIds()!=null&&eduDto.getUserIds().size()>0) {
            set = new HashSet(eduDto.getUserIds());
        }
        //将教育局中不存在的用户挑选出来
        for(String userId : userIds){
            if(!set.contains(userId)) {
                uids.add(new ObjectId(userId));
            }
        }
        Date currTime=new Date();
        Long updateTime= currTime.getTime();
        //将不存在的用户添加到教育局中
        educationBureauDao.addEduUsers(new ObjectId(id), uids, updateTime);
    }

    /**
     * 增加一个教育局管辖学校
     */
    public void addRelationSchool(String id,String schoolId) {
        boolean flag=true;
        EducationBureauDTO eduDto=new EducationBureauDTO(selEducationById(id));
        DateTimeUtils time=new DateTimeUtils();
        String schoolCreateDate="";
        if(eduDto.getSchoolIds()!=null&&eduDto.getSchoolIds().size()>0){
            Set<String> set = new HashSet(eduDto.getSchoolIds());
            if(set.contains(schoolId)) {
                flag=false;
            }else{
                List<String> schoolIds=eduDto.getSchoolIds();
                schoolIds.add(schoolId);
                Collections.sort(schoolIds);
                schoolCreateDate=time.getLongToStrTime(new ObjectId(schoolIds.get(0)).getTime());
            }
        }else{
            schoolCreateDate=time.getLongToStrTime(new ObjectId(schoolId).getTime());
        }
        if(flag){
            Date currTime=new Date();
            Long updateTime= currTime.getTime();
            educationBureauDao.addRelationSchool(new ObjectId(id), schoolCreateDate, new ObjectId(schoolId), updateTime);
        }
    }

    /**
     * 删除一个教育局管辖学校
     */
    public void delRelationSchool(String id,String schoolId) {
        Date currTime=new Date();
        Long updateTime= currTime.getTime();
        educationBureauDao.delRelationSchool(new ObjectId(id), new ObjectId(schoolId),updateTime);
    }

    /**
     * 增加一批教育局管辖学校
     */
    public void addRelationSchools(String id,List<String> schoolIds) {
        EducationBureauDTO eduDto=new EducationBureauDTO(selEducationById(id));
        List<ObjectId> sids=new ArrayList<ObjectId>();
        Set<String> set =new HashSet<String>();
        if(eduDto.getSchoolIds()!=null&&eduDto.getSchoolIds().size()>0) {
            set = new HashSet(eduDto.getSchoolIds());
        }
        for(String schoolId : schoolIds){
            if (!set.contains(schoolId)) {
                sids.add(new ObjectId(schoolId));
            }
        }
        String schoolCreateDate="";
        if(sids.size()>0) {
            DateTimeUtils time=new DateTimeUtils();
            Collections.sort(sids);
            schoolCreateDate = time.getLongToStrTime(sids.get(0).getTime());
        }
        Date currTime=new Date();
        Long updateTime= currTime.getTime();
        educationBureauDao.addRelationSchools(new ObjectId(id), schoolCreateDate, sids, updateTime);
    }

    /**
     * 通过教育局id查询教育局
     */
    public EducationBureauEntry selEducationById(String id) {
        EducationBureauEntry educationBureauEntry = educationBureauDao.selEducationById(new ObjectId(id));
        return educationBureauEntry;
    }

    /**
     * 通过userId查询教育局
     */
    public EducationBureauEntry selEducationByUserId(String userId) {
        EducationBureauEntry educationBureauEntry = educationBureauDao.selEducationByUserId(new ObjectId(userId));
        return educationBureauEntry;
    }
    /**
     * 通过userIds查询教育局
     */
    public List<EducationBureauEntry> selEducationByUserId(List<String> userIds) {
        List<ObjectId> uids=new ArrayList<ObjectId>();
        for(String userId : userIds){
            uids.add(new ObjectId(userId));
        }
        List<EducationBureauEntry> list= educationBureauDao.selEducationByUserIds(uids);
        return list;
    }

    /**
     * 通过schoolId查询教育局
     */
    public List<EducationBureauEntry> selEducationBySchoolId(String schoolId) {
        List<EducationBureauEntry> list= educationBureauDao.selEducationBySchoolId(new ObjectId(schoolId));
        return list;
    }

    /**
     * 通过educationName查询教育局信息
     * @param educationName
     */
    public boolean educationNameIsExist(String id, String educationName) {
        EducationBureauEntry educationBureauEntry = educationBureauDao.selEducationByEducationName(educationName);
        if(educationBureauEntry ==null){
            return false;
        }else{
            id=id==null?"":id;
            if(!"".equals(id)){
                if(!id.equals(educationBureauEntry.getID().toString())){
                    return true;
                }else{
                    return false;
                }
            }else{
                return true;
            }
        }
    }

    /**
     * 查询教育局信息
     * @param userId
     */
    public EducationBureauDTO getEducationByEduUserId(ObjectId userId) {
        //查询教育局管理信息
        EducationBureauEntry educationBureauEntry = selEducationByEduUserId(userId);
        EducationBureauDTO dto=null;
        if(educationBureauEntry !=null){
            dto=new EducationBureauDTO(educationBureauEntry);
        }
        return dto;
    }

    public List<SchoolDTO> getEducationSchoolByUserId(ObjectId userId, int role, String schoolId) {
        EducationBureauEntry educationBureauEntry = selEducationByUserId(userId,role,schoolId);
        List<SchoolDTO> list=new ArrayList<SchoolDTO>();
        if(educationBureauEntry !=null){
            //获取教育管辖的学校id集合
            List<ObjectId> schoolIds= educationBureauEntry.getSchoolIds();
            if(null!=schoolIds && !schoolIds.isEmpty()){
                //查询出教育局管辖下的所有学校信息
                list=schoolService.findSchoolInfoBySchoolIds(schoolIds);
            }
        }
        return list;
    }

    //======================================== 教育局管理 ======================================

    /**
     * 年级列表
     * @param userId
     * @return
     */
    public Map<String, Object> getGradeList(ObjectId userId){
        Map<String, Object> model = new HashMap<String, Object>();
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        EducationBureauEntry educationBureauEntry = selEducationByEduUserId(userId);
        List<Grade> gradeList = educationBureauEntry.getGradeList();
        if(gradeList!=null && gradeList.size()>0){
            for(Grade grade : gradeList){
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("gradeName", grade.getName());
                map.put("gradeType", grade.getGradeType());
                map.put("gradeId", grade.getGradeId().toString());
                list.add(map);
            }
        }
        model.put("eduId", educationBureauEntry.getID().toString());
        model.put("gradeList", list);
        return model;
    }

    /**
     * 增加年级
     * @param eduId
     * @param gradeName
     * @param gradeType
     */
    public void addGrade(ObjectId eduId, String gradeName, int gradeType){
        Grade grade = new Grade(gradeName, gradeType, null, null);
        educationBureauDao.addGrade(eduId, grade);
    }

    /**
     * 编辑年级
     * @param eduId
     * @param gradeId
     * @param gradeName
     * @param gradeType
     */
    public void editGrade(ObjectId eduId, ObjectId gradeId, String gradeName, int gradeType){
        educationBureauDao.editGrade(eduId, gradeId, gradeName, gradeType);
    }

    /**
     * 删除年级
     * @param eduId
     * @param gradeId
     */
    public void delGrade(ObjectId eduId, ObjectId gradeId){
        educationBureauDao.delGrade(eduId, gradeId);
    }

    /**
     * 学科列表
     * @param userId
     * @return
     */
    public Map<String, Object> getSubjectList(ObjectId userId){
        Map<String, Object> model = new HashMap<String, Object>();
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        EducationBureauEntry educationBureauEntry = selEducationByEduUserId(userId);
        List<EducationSubject> subjectList = educationBureauEntry.getSubjects();
        if(subjectList!=null && subjectList.size()>0){
            for(EducationSubject subject : subjectList){
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("subjectName", subject.getName());
                map.put("subjectType", subject.getSubjectType());
                map.put("subjectId", subject.getSubjectId().toString());
                List<ObjectId> gradeIds = subject.getGradeIds();
                List<String> gradeList = new ArrayList<String>();
                for(ObjectId grade : gradeIds){
                    gradeList.add(grade.toString());
                }
                map.put("gradeList", gradeList);
                list.add(map);
            }
        }
        model.put("eduId", educationBureauEntry.getID().toString());
        model.put("subjectList", list);
        return model;
    }

    /**
     * 增加学科
     * @param eduId
     * @param subjectName
     * @param gradeList
     */
    public void addSubject(ObjectId eduId, String subjectName, int subjectType, String gradeList){
        List<ObjectId> gradeIdList = new ArrayList<ObjectId>();
        String[] grades = gradeList.split(",");
        for(String grade : grades){
            gradeIdList.add(new ObjectId(grade));
        }
        EducationSubject subject = new EducationSubject(subjectName,gradeIdList);
        subject.setSubjectType(subjectType);
        educationBureauDao.addSubject(eduId, subject);
    }

    /**
     * 编辑学科
     * @param eduId
     * @param subjectName
     * @param gradeList
     */
    public void editSubject(ObjectId eduId, ObjectId subjectId,String subjectName, int subjectType, String gradeList){
        List<ObjectId> gradeIdList = new ArrayList<ObjectId>();
        String[] grades = gradeList.split(",");
        for(String grade : grades){
            gradeIdList.add(new ObjectId(grade));
        }
        educationBureauDao.editSubject(eduId, subjectId, subjectName, subjectType, gradeIdList);
    }

    /**
     * 删除学科
     * @param eduId
     * @param subjectId
     */
    public void delSubject(ObjectId eduId, ObjectId subjectId){
        educationBureauDao.delSubject(eduId, subjectId);
    }


    /**
     * 按照级别和父code查询
     * @param level level大于0时生效
     * @param parentId parentCode不为“”时生效
     * @return
     * @throws ResultTooManyException
     */
    public List<RegionDTO> getRegionEntryList(int level,ObjectId parentId)
    {
        List<RegionDTO> result=new ArrayList<RegionDTO>();
        try {
            List<RegionEntry> list=regionDao.getRegionEntryList(level, parentId);
            for(RegionEntry entry:list){
                RegionDTO dto=new RegionDTO(entry);
                result.add(dto);
            }
            return result;
        }catch (ResultTooManyException e){
            e.printStackTrace();
        }
        return result;
    }

    public Map<String, Object> selEducationList(String eduName, ObjectId province, ObjectId city, ObjectId county, int page, int pageSize) {
        Map<String, RegionEntry> prmap=regionDao.getRegionEntryMap(province);
        Map<String, RegionEntry> cimap=regionDao.getRegionEntryMap(city);
        Map<String, RegionEntry> comap=regionDao.getRegionEntryMap(county);
        Map<String,Object> result = new HashMap<String,Object>();
        int count=educationBureauDao.selEducationCount(eduName, province, city, county);
        List<EducationBureauDTO> list=new ArrayList<EducationBureauDTO>();
        //查询教育局
        int skip=page < 1 ? 0 : ((page - 1) * pageSize);
        List<EducationBureauEntry> EElist= educationBureauDao.selEducationList(eduName,province,city,county,skip,pageSize);
        for(EducationBureauEntry item: EElist){
            EducationBureauDTO educationBureauDTO =new EducationBureauDTO(item);
            RegionEntry pr=prmap.get(item.getProvince());
            if(pr!=null){
                educationBureauDTO.setProvinceName(pr.getName());
            }else{
                educationBureauDTO.setProvinceName("");
            }
            RegionEntry ci=cimap.get(item.getCity());
            if(ci!=null){
                educationBureauDTO.setCityName(ci.getName());
            }else{
                educationBureauDTO.setCityName("");
            }
            RegionEntry co=comap.get(item.getCounty());
            if(co!=null){
                educationBureauDTO.setCountyName(co.getName());
            }else{
                educationBureauDTO.setCountyName("");
            }
            list.add(educationBureauDTO);
        }
        result.put("rows",list);
        result.put("total", count);
        result.put("page", page);
        result.put("pageSize", pageSize);
        return result;
    }

    public List<SchoolDTO> getSchoolList(ObjectId province, ObjectId city, ObjectId county, String schoolName,List<ObjectId> schoolIds) {
        Set<ObjectId> noschoolIds=new HashSet<ObjectId>(schoolIds);
        //查询全部教育局
        List<EducationBureauEntry> EElist= educationBureauDao.selAllEducation();
        for(EducationBureauEntry item: EElist){
            noschoolIds.addAll(item.getSchoolIds());
        }
        Set<ObjectId> regionIds=new HashSet<ObjectId>();
        if(city!=null){
            regionIds.add(city);
        }else if(province!=null){
            try {
                List<RegionEntry> relist = regionDao.getRegionEntryList(0, province);
                for(RegionEntry re:relist){
                    regionIds.add(re.getID());
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        List<SchoolDTO> list=schoolService.getSchoolEntryByRegion(regionIds,noschoolIds,schoolName);
        return list;
    }

    public List<UserInfoDTO> getAllEduUserList(String userName,List<ObjectId> userIds) {
        Set<ObjectId> noUserIds=new HashSet<ObjectId>(userIds);
        //查询全部教育局
        List<EducationBureauEntry> EElist= educationBureauDao.selAllEducation();
        for(EducationBureauEntry item: EElist){
            noUserIds.addAll(item.getUserIds());
        }
        List<UserInfoDTO>  rlist=new ArrayList<UserInfoDTO>();
        List<UserInfoDTO>  list=userService.getUserListByParam(UserRole.EDUCATION.getRole(),userName,noUserIds);
        for(UserInfoDTO dto:list){
            if(UserRole.isEducation(dto.getRole())){
                rlist.add(dto);
            }
        }
        return rlist;
    }

    public void getEducationInfoById(Map<String, Object> model, String id) {
        EducationBureauEntry entry=selEducationById(id);
        EducationBureauDTO eduDto=new EducationBureauDTO(entry);
        model.put("eduDto",eduDto);
        List<UserDetailInfoDTO> userList=new ArrayList<UserDetailInfoDTO>();
        if(entry.getUserIds()!=null&&entry.getUserIds().size()>0) {
            userList=userService.findUserInfoByIds(entry.getUserIds());
        }
        model.put("userList",userList);
        List<SchoolDTO> schoolList=new ArrayList<SchoolDTO>();
        if(entry.getSchoolIds()!=null&&entry.getSchoolIds().size()>0) {
            schoolList=schoolService.findSchoolInfoBySchoolIds(entry.getSchoolIds());
        }
        model.put("schoolList",schoolList);
        List<RegionDTO> provinces=getRegionEntryList(2, null);
        model.put("provinces",provinces);
        List<RegionDTO> citys =new ArrayList<RegionDTO>();
        if(StringUtils.isNotBlank(eduDto.getProvince())) {
            citys = getRegionEntryList(0, new ObjectId(eduDto.getProvince()));
        }
        model.put("citys", citys);
    }

    public List<EducationBureauDTO> getEducationListByParams(String eduName, List<ObjectId> noeduIds) {
        List<EducationBureauDTO> list=new ArrayList<EducationBureauDTO>();
        //查询全部教育局
        List<EducationBureauEntry> EElist= educationBureauDao.getEducationListByParams(eduName,noeduIds);
        for(EducationBureauEntry item: EElist){
            EducationBureauDTO educationBureauDTO =new EducationBureauDTO(item);
            list.add(educationBureauDTO);
        }
        return list;
    }

    public List<EducationBureauDTO> findEduInfoByEduIds(List<ObjectId> eduIds) {
        List<EducationBureauDTO> list=new ArrayList<EducationBureauDTO>();
        //查询全部教育局
        List<EducationBureauEntry> EElist= educationBureauDao.findEduInfoByEduIds(eduIds);
        for(EducationBureauEntry item: EElist){
            EducationBureauDTO educationBureauDTO =new EducationBureauDTO(item);
            list.add(educationBureauDTO);
        }
        return list;
    }

    public void educationCloud(ObjectId id) {
        EducationBureauEntry entry=educationBureauDao.selEducationById(id);
        if(entry.getOpenCloud()== Constant.ZERO){
            entry.setOpenCloud(Constant.ONE);
        }else if(entry.getOpenCloud()== Constant.ONE){
            entry.setOpenCloud(Constant.ZERO);
        }
        updateEducationInfo(entry);
    }
}
