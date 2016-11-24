package com.fulaan.zouban.service;

import com.db.school.SchoolDao;
import com.db.zouban.SchoolSubjectGroupDao;
import com.fulaan.zouban.dto.SchoolSubjectGroupDTO;
import com.pojo.school.SchoolEntry;
import com.pojo.school.Subject;
import com.pojo.zouban.SchoolSubjectGroupEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by fl on 2016/7/16.
 */
@Service
public class SchoolSubjectGroupService {

    private SchoolSubjectGroupDao schoolSubjectGroupDao = new SchoolSubjectGroupDao();
    private SchoolDao schoolDao = new SchoolDao();

    /**
     * 获取学科组合列表
     * @param schoolId
     * @param year
     * @param gradeId
     * @return
     * @throws Exception
     */
    public SchoolSubjectGroupDTO getSchoolSubjectGroupDTO(ObjectId schoolId, String year, ObjectId gradeId) throws Exception{
        SchoolSubjectGroupEntry schoolSubjectGroupEntry = schoolSubjectGroupDao.getEntry(schoolId, year, gradeId);
        if(schoolSubjectGroupEntry == null){
            schoolSubjectGroupEntry = bulidSchoolSubjectGroupEntry(schoolId, year, gradeId);
            schoolSubjectGroupDao.addEntry(schoolSubjectGroupEntry);
        }
        SchoolSubjectGroupDTO schoolSubjectGroupDTO =  new SchoolSubjectGroupDTO(schoolSubjectGroupEntry);
        List<SchoolSubjectGroupDTO.SubjectGroupDTO> subjectGroupDTOs = schoolSubjectGroupDTO.getSubjectGroupDTOs();
        List<ObjectId> subjectIds = getGradeSubjects(schoolId, gradeId);
        for(SchoolSubjectGroupDTO.SubjectGroupDTO subjectGroupDTO : subjectGroupDTOs){
            List<Integer> chooseState = new ArrayList<Integer>();
            for(ObjectId subjectId : subjectIds){
                if(subjectGroupDTO.getAdvSubjects().contains(subjectId.toString())){
                    chooseState.add(1);
                } else {
                    chooseState.add(0);
                }
            }
            subjectGroupDTO.setChooseState(chooseState);
        }
        return schoolSubjectGroupDTO;
    }

    private SchoolSubjectGroupEntry bulidSchoolSubjectGroupEntry(ObjectId schoolId, String year, ObjectId gradeId) throws Exception{
        List<ObjectId> subjectIds = getGradeSubjects(schoolId, gradeId);
        List<List<ObjectId>> result = new ArrayList<List<ObjectId>>();
        bulidSubjectGroups(0, new ArrayList<ObjectId>(), subjectIds, 3, result);
        Map<ObjectId, String> subjectNameMap = buildSubjectNameMap(schoolId, gradeId);
        List<SchoolSubjectGroupEntry.SubjectGroup> subjectGroups = new ArrayList<SchoolSubjectGroupEntry.SubjectGroup>();
        for(List<ObjectId> advSubjectIds : result){
            List<ObjectId> simSubjectIds = new ArrayList<ObjectId>(subjectIds);
            simSubjectIds.removeAll(advSubjectIds);
            String groupName = buildGroupName(advSubjectIds, subjectNameMap);
            SchoolSubjectGroupEntry.SubjectGroup subjectGroup = new SchoolSubjectGroupEntry.SubjectGroup(new ObjectId(), groupName, true, advSubjectIds, simSubjectIds);
            subjectGroups.add(subjectGroup);
        }
        SchoolSubjectGroupEntry schoolSubjectGroupEntry = new SchoolSubjectGroupEntry(schoolId, year, gradeId, subjectGroups);
        return schoolSubjectGroupEntry;
    }

    private Map<ObjectId, String> buildSubjectNameMap(ObjectId schoolId, ObjectId gradeId){
        Map<ObjectId, String> subjectNameMap = new HashMap<ObjectId, String>();

        List<String> subjectNames = new ArrayList<String>();
        Collections.addAll(subjectNames, "物理", "化学", "生物", "政治", "历史", "地理");
        SchoolEntry schoolEntry = schoolDao.getSchoolEntry(schoolId, Constant.FIELDS);
        List<Subject> subjects = schoolEntry.getSubjects();
        for(Subject subject : subjects){
            List<ObjectId> gradeIds = subject.getGradeIds();
            if(gradeIds.contains(gradeId) && subjectNames.contains(subject.getName())){
                subjectNameMap.put(subject.getSubjectId(), subject.getName());
            }
        }

        return subjectNameMap;
    }

    private String buildGroupName(List<ObjectId> advSubjects, Map<ObjectId, String> subjectNameMap){
        String groupName = "";
        if(advSubjects.size() > 0){
            for(ObjectId advSubject : advSubjects){
                String subjectName = subjectNameMap.get(advSubject);
                groupName += subjectName.substring(0,1) + "、";
            }
        }
        return groupName.substring(0, groupName.length() - 1);
    }

    /**
     * 按指定顺序返回学科Id列表
     * @param schoolId
     * @param gradeId
     * @return
     * @throws Exception
     */
    private List<ObjectId> getGradeSubjects(ObjectId schoolId, ObjectId gradeId) throws Exception{
        List<ObjectId> retSubjects = new ArrayList<ObjectId>();
        List<String> subjectNames = new ArrayList<String>();
        Collections.addAll(subjectNames, "物理", "化学", "生物", "政治", "历史", "地理");
        SchoolEntry schoolEntry = schoolDao.getSchoolEntry(schoolId, Constant.FIELDS);
        List<Subject> subjects = schoolEntry.getSubjects();
        Map<String, ObjectId> nameIdMap = new HashMap<String, ObjectId>();
        for(Subject subject : subjects){
            List<ObjectId> gradeIds = subject.getGradeIds();
            if(gradeIds.contains(gradeId) && subjectNames.contains(subject.getName())){
                nameIdMap.put(subject.getName(), subject.getSubjectId());
            }
        }
        for(String name : subjectNames){
            if (nameIdMap.containsKey(name)) {
                retSubjects.add(nameIdMap.get(name));
            }
        }
        if(retSubjects.size() != subjectNames.size()){
            throw new Exception("缺少科目或科目名称不标准");
        }
        return retSubjects;
    }

    private void bulidSubjectGroups(int i, List<ObjectId> advSubjectIds, List<ObjectId> subjectIds,int n, List<List<ObjectId>> result) {
        if(n==0){
//            System.out.println(advSubjectIds);
            result.add(advSubjectIds);
            return;
        }
        if(i==subjectIds.size()){
            return;
        }

        List<ObjectId> newAdvSubjectIds = new ArrayList<ObjectId>();
        newAdvSubjectIds.addAll(advSubjectIds);
        newAdvSubjectIds.add(subjectIds.get(i));
        bulidSubjectGroups(i + 1, newAdvSubjectIds, subjectIds, n - 1, result);
        bulidSubjectGroups(i + 1, advSubjectIds, subjectIds, n, result);
    }

    /**
     * 更新组合开放状态
     *
     * @param subjectGroupId
     * @param isPublic
     */
    public void updatePublicState(ObjectId subjectGroupId, Boolean isPublic){
        schoolSubjectGroupDao.updatePublicState(subjectGroupId, isPublic);
    }



    //
}
