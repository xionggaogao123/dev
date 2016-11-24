package com.fulaan.zouban.service;

import com.db.school.TeacherDao;
import com.db.zouban.SubjectConfDao;
import com.db.zouban.XuanKeConfDao;
import com.fulaan.examresult.controller.IdNameDTO;
import com.fulaan.myclass.service.ClassService;
import com.fulaan.myschool.controller.SubjectView;
import com.fulaan.school.service.SchoolService;
import com.fulaan.user.service.UserService;
import com.fulaan.zouban.dto.SubjectTeacher;
import com.mongodb.BasicDBObject;
import com.pojo.school.ClassInfoDTO;
import com.pojo.user.UserEntry;
import com.pojo.zouban.SubjectConfEntry;
import com.pojo.zouban.XuankeConfEntry;
import com.pojo.zouban.ZoubanType;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by wangkaidong on 2016/6/13.
 *
 * 走班公共方法Service
 */
@Service
public class CommonService {
    @Autowired
    private SchoolService schoolService;
    @Autowired
    private ClassService classService;
    @Autowired
    private UserService userService;

    private TeacherDao teacherDao = new TeacherDao();
    private SubjectConfDao subjectConfDao = new SubjectConfDao();
    private XuanKeConfDao xuanKeConfDao = new XuanKeConfDao();


    /**
     * 获取学年列表
     * @return
     */
    public List<String> getTermList(){
        List<String> termList = new ArrayList<String>();
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int startYear = 2015;
        for (int i = startYear; i <= year; i++) {
            termList.add(i + "-" + (i + 1) + "学年");
        }
        Collections.reverse(termList);
        return termList;
    }

    /**
     * 获取当前学期
     * @return
     */
    public String getCurrentTerm() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        if (month < 8 && month >= 2) {
            return (year - 1) + "-" + year + "学年第二学期";
        } else if (month >= 8) {
            return year + "-" + (year + 1) + "学年第一学期";
        } else {
            return (year - 1) + "-" + year + "学年第一学期";
        }
    }


    /**
     * 走班课学科列表
     * @param term
     * @param gradeId
     * @param schoolId
     * @return
     */
    public List<IdNameDTO> getSubjectList(String term, String gradeId, String schoolId) {
        return getSubjectListByType(term, gradeId, schoolId, ZoubanType.ZOUBAN.getType());
    }


    /**
     * 获取学科列表
     * @param term
     * @param gradeId
     * @param schoolId
     * @param type
     * @return
     */
    private List<IdNameDTO> getSubjectListByType(String term, String gradeId, String schoolId, int type) {
        List<IdNameDTO> subjectList = new ArrayList<IdNameDTO>();
        XuankeConfEntry xuankeConfEntry = xuanKeConfDao.findXuanKeConf(term, new ObjectId(gradeId));
        List<SubjectConfEntry> subjectConfEntryList = subjectConfDao.findSubjectConf(xuankeConfEntry.getID(), type);

        for (SubjectConfEntry subjectConfEntry : subjectConfEntryList) {
            IdNameDTO subject = new IdNameDTO();
            String subjectId = subjectConfEntry.getSubjectId().toString();
            subject.setId(subjectId);

            SubjectView subjectView = schoolService.findSubjectBySchoolIdAndSubId(schoolId, subjectId);
            subject.setName(subjectView.getName());

            subjectList.add(subject);
        }
        return subjectList;
    }


    /**
     * 获取学科老师列表
     *
     * @param gradeId
     * @param schoolId
     * @return
     */
    public List<SubjectTeacher> getSubjectTeacherList(String schoolId, String gradeId) {
        List<IdNameDTO> subjectList = new ArrayList<IdNameDTO>();
        List<SubjectView> subjectViews = schoolService.findSubjectListBySchoolIdAndGradeId(schoolId, gradeId);
        for(SubjectView subjectView:subjectViews){
            subjectList.add(new IdNameDTO(subjectView.getId(),subjectView.getName()));
        }
        List<SubjectTeacher> subjectTeacherList = new ArrayList<SubjectTeacher>();

        for (IdNameDTO idNameDTO : subjectList) {
            SubjectTeacher subjectTeacher = new SubjectTeacher();
            subjectTeacher.setSubjectId(idNameDTO.getId());
            subjectTeacher.setSubjectName(idNameDTO.getName());
            List<IdNameDTO> teacherList = findTeacherBySubject(gradeId, idNameDTO.getId());
            subjectTeacher.setTeacherList(teacherList);
            subjectTeacherList.add(subjectTeacher);
        }

        return subjectTeacherList;
    }

    /**
     * 根据学科以及年级获取老师
     *
     * @param subjectId
     * @param gradeId
     * @return
     */
    public List<IdNameDTO> findTeacherBySubject(String gradeId, String subjectId) {
        List<ClassInfoDTO> classInfoDTOList = classService.findClassByGradeId(gradeId);
        List<ObjectId> classIds = new ArrayList<ObjectId>();
        for (ClassInfoDTO classInfoDTO : classInfoDTOList) {
            classIds.add(new ObjectId(classInfoDTO.getId()));
        }

        List<IdNameDTO> teacherInfo = new ArrayList<IdNameDTO>();

        //根据学科和班级查询老师
        List<ObjectId> teacherIds = teacherDao.findTeacherBySubjectIdAndClassIds(new ObjectId(subjectId), classIds);
        Set<ObjectId> teacherIdSet = new HashSet<ObjectId>(teacherIds);
        List<ObjectId> teacherIdList = new ArrayList<ObjectId>(teacherIdSet);



        if (teacherIdList != null && teacherIdList.size() != 0) {
            Map<ObjectId, UserEntry> userMap = userService.getUserEntryMap(teacherIdList, new BasicDBObject("nm", 1));
            for (Map.Entry entry : userMap.entrySet()) {
                ObjectId teacherId = (ObjectId) entry.getKey();
                UserEntry teacher = (UserEntry) entry.getValue();

                IdNameDTO teacherIdName = new IdNameDTO();
                teacherIdName.setId(teacherId.toString());
                teacherIdName.setName(teacher.getUserName());
                teacherInfo.add(teacherIdName);
            }
        }
        return teacherInfo;
    }


    static ArrayList<Integer[]> cmn(Integer[] source, int n) {
        ArrayList<Integer[]> result = new ArrayList<Integer[]>();
        if (n == 1) {
            for (int i = 0; i < source.length; i++) {
                result.add(new Integer[]{source[i]});

            }
        } else if (source.length == n) {
            result.add(source);
        } else {
            Integer[] psource = new Integer[source.length - 1];
            for (int i = 0; i < psource.length; i++) {
                psource[i] = source[i];
            }
            result = cmn(psource, n);
            ArrayList<Integer[]> tmp = cmn(psource, n - 1);
            for (int i = 0; i < tmp.size(); i++) {
                Integer[] rs = new Integer[n];
                for (int j = 0; j < n - 1; j++) {
                    rs[j] = tmp.get(i)[j];
                }
                rs[n - 1] = source[source.length - 1];
                result.add(rs);
            }
        }
        return result;
    }


}
