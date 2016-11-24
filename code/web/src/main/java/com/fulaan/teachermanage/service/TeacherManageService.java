package com.fulaan.teachermanage.service;

import com.db.school.ClassDao;
import com.db.school.SchoolDao;
import com.db.school.TeacherClassSubjectDao;
import com.db.school.TeacherDao;
import com.db.teachermanage.*;
import com.db.user.UserDao;
import com.fulaan.groupdiscussion.service.EaseMobService;
import com.fulaan.teachermanage.dto.*;
import com.fulaan.utils.PinYin2Abbreviation;
import com.pojo.app.FieldValuePair;
import com.pojo.school.ClassEntry;
import com.pojo.school.SchoolEntry;
import com.pojo.school.TeacherClassSubjectEntry;
import com.pojo.teachermanage.*;
import com.pojo.user.UserEntry;
import com.pojo.user.UserRole;
import com.sys.constants.Constant;
import com.sys.utils.MD5Utils;
import com.sys.utils.ValidationUtils;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by wang_xinxin on 2016/2/29.
 */

@Service
public class TeacherManageService {

    private CourseProjectDao courseProjectDao = new CourseProjectDao();

    private UserDao userDao = new UserDao();

    private SchoolDao schoolDao = new SchoolDao();

    private ClassDao classDao=new ClassDao();

    private TeacherDao teacherDao = new TeacherDao();

    private TeacherClassSubjectDao teacherClassSubjectDao = new TeacherClassSubjectDao();
    @Autowired
    private EaseMobService easeMobService;

    private CertificateDao certificateDao = new CertificateDao();

    private ContinueEducationDao continueEducationDao = new ContinueEducationDao();

    private EducationDao educationDao = new EducationDao();

    private JobDao jobDao = new JobDao();

    private PartTimeDao partTimeDao = new PartTimeDao();

    private ResultDao resultDao = new ResultDao();

    private ResumeDao resumeDao = new ResumeDao();

    private TitleDao titleDao = new TitleDao();

    private PostionDao postionDao = new PostionDao();

    /**
     * 增加教师课程项目
     * @param courseProjectEntry
     */
    public void addProjectEntry(CourseProjectEntry courseProjectEntry) {
        courseProjectDao.addProjectEntry(courseProjectEntry);
    }

    /**
     * 删除教师课程项目
     * @param projectId
     */
    public void delProjectEntry(ObjectId projectId) {
        courseProjectDao.delProjectEntry(projectId);
    }

    public List<CourseProjectDTO> getCourseProjectListBySchoolId(ObjectId schoolId) {
        List<CourseProjectDTO> courseProjectDTOList = new ArrayList<CourseProjectDTO>();
        List<CourseProjectEntry> courseProjectEntryList = courseProjectDao.getCourseProjectListBySchoolId(schoolId, null);
        if (courseProjectEntryList!=null && courseProjectEntryList.size()!=0) {
            for (CourseProjectEntry courseProjectEntry : courseProjectEntryList) {
                courseProjectDTOList.add(new CourseProjectDTO(courseProjectEntry));
            }
        }
        return courseProjectDTOList;
    }

    public List<TeacherInfoDTO> getTeacherList(String grade, int customize, String keyword, int type,String schoolId) {

//        Map<ObjectId,UserEntry> userEntryMap = new HashMap<ObjectId, UserEntry>();
//        if (userEntryList!=null && userEntryList.size()!=0) {
//            for (UserEntry user : userEntryList) {
//                userEntryMap.put(user.getID(),user);
//            }
//        }
        List<ObjectId> userids = new ArrayList<ObjectId>();
        if (!StringUtils.isEmpty(grade)){
            List<ClassEntry> classEntryList=classDao.findClassEntryByGradeId(new ObjectId(grade));
            if (classEntryList!=null && classEntryList.size()!=0) {
                for (ClassEntry classEntry : classEntryList) {
                    userids.addAll(classEntry.getTeachers());
                }
            }
        }
        List<UserEntry> userEntryList = userDao.getUserInfoList(keyword, customize, schoolId,userids);
        List<ObjectId> teacherids = new ArrayList<ObjectId>();
        if (userEntryList!=null && userEntryList.size()!=0) {
            for (UserEntry user : userEntryList) {
                teacherids.add(user.getID());
            }
        }
        List<TeacherClassSubjectEntry> teacherClassSubjectEntryList = teacherClassSubjectDao.getTeacherClassSubjectEntryByUidsList(teacherids, null);
        List<TeacherInfoDTO> teacherInfoDTOList = new ArrayList<TeacherInfoDTO>();
        if (userEntryList!=null && userEntryList.size()!=0) {
            for (UserEntry user : userEntryList) {
                List<String> poslist = new ArrayList<String>();
                if (teacherClassSubjectEntryList!=null && teacherClassSubjectEntryList.size()!=0) {
                    for (TeacherClassSubjectEntry tcentry : teacherClassSubjectEntryList) {
                        if (user.getID().equals(tcentry.getTeacherId())) {
                            if (!(tcentry.getClassInfo().getValue().toString()).contains("毕业")) {
                                poslist.add(tcentry.getClassInfo().getValue().toString()+tcentry.getSubjectInfo().getValue().toString());
                            }
                        }
                    }
                }
                TeacherInfoDTO teacherInfoDTO = new TeacherInfoDTO();
                teacherInfoDTO.setLetter(StringUtils.isEmpty(user.getLetter()) ? PinYin2Abbreviation.cn2py(user.getUserName().substring(0,1)).toUpperCase(): user.getLetter().substring(0, 1).toUpperCase());
                teacherInfoDTO.setId(user.getID().toString());
                teacherInfoDTO.setPostionDec(user.getPostionDec()==null?UserRole.getRoleDescription(user.getRole()):user.getPostionDec());
                teacherInfoDTO.setName(user.getRealUserName());
                teacherInfoDTO.setUsername(user.getNickName());
                teacherInfoDTO.setJobNumber(user.getJobnumber()==null?"":user.getJobnumber());
                teacherInfoDTO.setInfos(poslist);
                String role = "";
                teacherInfoDTO.setIsmanage(2);

                if (UserRole.isHeadmaster(user.getRole())) {
                    teacherInfoDTO.setUserRole(UserRole.HEADMASTER.getRole());
                    role = UserRole.HEADMASTER.getDes();
                } else if (UserRole.isTeacher(user.getRole())) {
                    teacherInfoDTO.setUserRole(UserRole.TEACHER.getRole());
                    role = UserRole.TEACHER.getDes();
                    if (UserRole.isManager(user.getRole())) {
                        teacherInfoDTO.setIsmanage(1);
                    }else{
                        teacherInfoDTO.setIsmanage(0);
                    }
                } else if (UserRole.isManager(user.getRole())) {
                    teacherInfoDTO.setUserRole(UserRole.ADMIN.getRole());
                    role = UserRole.ADMIN.getDes();
                } else if (UserRole.isDoorKeeper(user.getRole())) {
                    teacherInfoDTO.setUserRole(UserRole.DOORKEEPER.getRole());
                    role = UserRole.DOORKEEPER.getDes();
                } else if (UserRole.isDormManager(user.getRole())) {
                    teacherInfoDTO.setUserRole(UserRole.DORMMANAGER.getRole());
                    role = UserRole.DORMMANAGER.getDes();
                }

                teacherInfoDTO.setRole(role);
                if (type==0) {
                    teacherInfoDTOList.add(teacherInfoDTO);
                } else if (type==1) {
                    if (poslist!=null && poslist.size()!=0) {
                         teacherInfoDTOList.add(teacherInfoDTO);
                    }
                } else {
                    if (poslist==null || poslist.size()==0) {
                        teacherInfoDTOList.add(teacherInfoDTO);
                    }
                }
            }
        }
        return teacherInfoDTOList;
    }

    /**
     * 初始密码
     * @param userId
     * @param schoolId
     * @param rds
     */
    public void initPwdOfTeacher(String userId,String userName, String schoolId, int rds,Map<String,Object> model) {
        SchoolEntry schoolEntry = schoolDao.getSchoolEntry(new ObjectId(schoolId), Constant.FIELDS);
        model.put("pwd",schoolEntry.getInitialPassword());
        try {
            if(rds==0) {
                teacherDao.updatePwd(new ObjectId(userId), MD5Utils.getMD5String(schoolEntry.getInitialPassword()));
                model.put("nm",userName);
            } else {
                teacherDao.updatePwdBySchoolId(new ObjectId(schoolId), MD5Utils.getMD5String(schoolEntry.getInitialPassword()));
                model.put("nm","全校老师");
            }
            model.put("flg",true);

        } catch (Exception e) {
            model.put("flg",false);
        }
    }

    /**
     * 添加简历
     * @param resumeDTO
     * @param schoolId
     */
    public void addOrUpdateTeacherInfo(ResumeDTO resumeDTO,String schoolId,Map<String,Object> model) {
        SchoolEntry schoolEntry = schoolDao.getSchoolEntry(new ObjectId(schoolId),Constant.FIELDS);
        String pwd = schoolEntry.getInitialPassword();
        ObjectId userid = null;
        boolean flg = true;
        int userRole=resumeDTO.getUserrole();
        if (resumeDTO.getIsmanage()==1) {
            //只有老师可以附带管理员功能
            if (UserRole.isTeacher(userRole)) {
                userRole += UserRole.ADMIN.getRole();
            }
        }
        if (resumeDTO.getType()==1) {
            userid = new ObjectId(resumeDTO.getId());
            UserEntry uentry = userDao.getUserEntry(userid, Constant.FIELDS);
            int role = uentry.getRole();
            //如果原来是校长或老师，修改为校长，则增加角色(modify by miaoqiang)
            if (UserRole.isTeacher(role)&& userRole == 8 ) {
                role = userRole | UserRole.TEACHER.getRole();
            } else {
                role = userRole;
            }

            userDao.update(userid,new FieldValuePair("r",role),
                    new FieldValuePair("sex",resumeDTO.getSex()),
                    new FieldValuePair("pos",resumeDTO.getOrganization()),
                    new FieldValuePair("nm",resumeDTO.getName()),
                    new FieldValuePair("nnm",resumeDTO.getUsername()),
                    new FieldValuePair("jnb",resumeDTO.getTeachernumber()),
                    new FieldValuePair("posdec",resumeDTO.getPostiondec())
            );

            resumeDao.removeResumeEntry(userid);
            educationDao.removeEducationEntry(userid);
            jobDao.removeJobEntry(userid);
            titleDao.removeTitleEntry(userid);
            postionDao.removePostionEntry(userid);
            partTimeDao.removePartTimeEntry(userid);
            resultDao.removeResultEntry(userid);
            certificateDao.removeCertificateEntry(userid);
            continueEducationDao.removeContinueEducationEntry(userid);
        } else {
            List<UserEntry> userEntryList=userDao.findEntryByUserName(resumeDTO.getName());
            UserEntry uentry = userDao.searchUserByLoginName(resumeDTO.getName());
            if (ValidationUtils.isRequestModile(resumeDTO.getName())) {
                flg = false;
                model.put("mesg", "用户名不能为手机号，请重新输入！");
            }
            if ((userEntryList!=null && userEntryList.size()!=0) || uentry!=null) {
                flg = false;
                model.put("mesg","用户名重复，请重新输入！");
            }
            if (flg) {
                UserEntry user = teacherDao.addTeacherInfo(new ObjectId(schoolId), userRole, resumeDTO.getSex(), resumeDTO.getName(), resumeDTO.getUsername(), pwd, resumeDTO.getOrganization(), resumeDTO.getTeachernumber(),resumeDTO.getPostiondec());
                userid = user.getID();
                boolean k = true;
                if (StringUtils.isEmpty(user.getChatId())) {
                    k = false;
                } else {
                    easeMobService.createNewUser(user.getChatId());
                }
            }
        }
        if (flg) {
            resumeDao.addResumeEntry(resumeDTO.buildResume(userid,schoolId));
            String tab1 = resumeDTO.getTab1();
            if (!StringUtils.isEmpty(tab1)) {
                String[] arg = tab1.split(";");
                if (arg!=null && arg.length!=0) {
                    for (int i=0;i<arg.length;i++) {
                        String[] type= arg[i].split(",");
                        educationDao.addEducationEntry(new EducationEntry(userid,new ObjectId(schoolId),type[0],type[1],type[2],type[3],type[4].equals("是")?1:0));
                    }
                }
            }
            String tab2 = resumeDTO.getTab2();
            if (!StringUtils.isEmpty(tab2)) {
                String[] arg = tab2.split(";");
                if (arg!=null && arg.length!=0) {
                    for (int i=0;i<arg.length;i++) {
                        String[] type= arg[i].split(",");
                        jobDao.addJobEntry(new JobEntry(userid, new ObjectId(schoolId), type[0], type[1], type[2], type[3].equals("是") ? 1 : 0));
                    }
                }
            }
            String tab3 = resumeDTO.getTab3();
            if (!StringUtils.isEmpty(tab3)) {
                String[] arg = tab3.split(";");
                if (arg!=null && arg.length!=0) {
                    for (int i=0;i<arg.length;i++) {
                        String[] type= arg[i].split(",");
                        titleDao.addTitleEntry(new TitleEntry(userid, new ObjectId(schoolId), type[0], type[1], type[2],type[3],type[4], type[5].equals("是") ? 1 : 0));
                    }
                }
            }
            String tab4 = resumeDTO.getTab4();
            if (!StringUtils.isEmpty(tab4)) {
                String[] arg = tab4.split(";");
                if (arg!=null && arg.length!=0) {
                    for (int i=0;i<arg.length;i++) {
                        String[] type= arg[i].split(",");
                        postionDao.addPostionEntry(new PostionEntry(userid, new ObjectId(schoolId), type[0], type[1], type[2], type[3].equals("是") ? 1 : 0));
                    }
                }
            }
            String tab5 = resumeDTO.getTab5();
            if (!StringUtils.isEmpty(tab5)) {
                String[] arg = tab5.split(";");
                if (arg!=null && arg.length!=0) {
                    for (int i=0;i<arg.length;i++) {
                        String[] type= arg[i].split(",");
                        partTimeDao.addPartTimeEntry(new PartTimeEntry(userid, new ObjectId(schoolId), type[0], type[1], type[2], type[3].equals("是") ? 1 : 0));
                    }
                }
            }
            String tab6 = resumeDTO.getTab6();
            if (!StringUtils.isEmpty(tab6)) {
                String[] arg = tab6.split(";");
                if (arg!=null && arg.length!=0) {
                    for (int i=0;i<arg.length;i++) {
                        String[] type= arg[i].split(",");
                        resultDao.addResultEntry(new ResultEntry(userid, new ObjectId(schoolId), type[0], type[1], type[2], type[3].equals("是") ? 1 : 0));
                    }
                }
            }
            String tab7 = resumeDTO.getTab7();
            if (!StringUtils.isEmpty(tab7)) {
                String[] arg = tab7.split(";");
                if (arg!=null && arg.length!=0) {
                    for (int i=0;i<arg.length;i++) {
                        String[] type= arg[i].split(",");
                        certificateDao.addCertificateEntry(new CertificateEntry(userid, new ObjectId(schoolId), type[0], type[1], type[2], type[3].equals("是") ? 1 : 0));
                    }
                }
            }
            String tab8 = resumeDTO.getTab8();
            if (!StringUtils.isEmpty(tab8)) {
                String[] arg = tab8.split(";");
                if (arg!=null && arg.length!=0) {
                    for (int i=0;i<arg.length;i++) {
                        String[] type= arg[i].split(",");
                        continueEducationDao.addContinueEducationEntry(new ContinueEducationEntry(userid, new ObjectId(schoolId), type[0], type[1], type[2],type[3],type[6],type[7],Integer.parseInt(type[4]),type[8],type[5].equals("是") ? 1 : 0));
                    }
                }
            }
        }
        model.put("type",flg);
    }

    public void delTeacherInfo(ObjectId teacherId) {
        userDao.logicRemoveUser(teacherId);
        resumeDao.removeResumeEntry(teacherId);
        educationDao.removeEducationEntry(teacherId);
        jobDao.removeJobEntry(teacherId);
        titleDao.removeTitleEntry(teacherId);
        postionDao.removePostionEntry(teacherId);
        partTimeDao.removePartTimeEntry(teacherId);
        resultDao.removeResultEntry(teacherId);
        certificateDao.removeCertificateEntry(teacherId);
        continueEducationDao.removeContinueEducationEntry(teacherId);
    }

    public List<ContinueEducationDTO> getProjectDetailList(String projectId,String schoolId,int type) {
        String pid = projectId;
        List<ContinueEducationEntry> continueEducationEntryList = new ArrayList<ContinueEducationEntry>();
        if (StringUtils.isEmpty(projectId)) {
            List<CourseProjectEntry> courseProjectEntryList = courseProjectDao.getCourseProjectListBySchoolId(new ObjectId(schoolId), null);
            if (courseProjectEntryList!=null && courseProjectEntryList.size()!=0) {
                continueEducationEntryList = continueEducationDao.getContinueEducationListById(courseProjectEntryList.get(0).getID().toString(),type,null);
            }
        } else {
            continueEducationEntryList = continueEducationDao.getContinueEducationListById(projectId,type,null);
        }

        ContinueEducationDTO continueEducationDTO = null;

        List<ObjectId> userIdList = new ArrayList<ObjectId>();
        if (continueEducationEntryList!=null && continueEducationEntryList.size()!=0) {
            for (ContinueEducationEntry entry : continueEducationEntryList) {
                userIdList.add(entry.getUserId());
                pid = entry.getCourse();
            }
        }
        List<ContinueEducationDTO> continueEducationDTOList = new ArrayList<ContinueEducationDTO>();
        Map<ObjectId, UserEntry> userEntryMap = userDao.getUserEntryMap(userIdList, null);
        CourseProjectEntry courseProjectEntry = courseProjectDao.getCourseProjectListById(new ObjectId(pid),null);
        if (continueEducationEntryList!=null && continueEducationEntryList.size()!=0) {
            for (ContinueEducationEntry entry : continueEducationEntryList) {
                continueEducationDTO = new ContinueEducationDTO();
                continueEducationDTO.setCertificate(entry.getCertificate());
                continueEducationDTO.setScore(entry.getRecord());
                continueEducationDTO.setUserid(entry.getUserId().toString());
                continueEducationDTO.setUsername("");
                if (userEntryMap.get(entry.getUserId()) != null) {
                    continueEducationDTO.setUsername(userEntryMap.get(entry.getUserId()).getUserName());
                }

                continueEducationDTO.setCourse(courseProjectEntry.getCourse());
                continueEducationDTOList.add(continueEducationDTO);
            }
        }
        return continueEducationDTOList;
    }

    /**
     * 数据
     * @param userid
     */
    public void getTeacherDetail(ObjectId userid,String schoolId,Map<String,Object> model) {
        UserEntry userEntry = userDao.getUserEntry(userid,null);
        ResumeEntry resumeEntry = resumeDao.getResumeList(userid,null);
        List<EducationEntry> educationEntryList = educationDao.getEducationList(userid, null);
        List<EducationDTO> educationDTOs = new ArrayList<EducationDTO>();
        if (educationEntryList!=null && educationEntryList.size()!=0) {
            for (EducationEntry educationEntry : educationEntryList) {
                educationDTOs.add(new EducationDTO(educationEntry));
            }
        }
        model.put("educationEntryList",educationDTOs);
        List<JobEntry> jobEntryList = jobDao.getJobList(userid, null);
        List<JobDTO> jobDTOs = new ArrayList<JobDTO>();
        if (jobEntryList!=null && jobEntryList.size()!=0) {
            for (JobEntry jobEntry : jobEntryList) {
                jobDTOs.add(new JobDTO(jobEntry));
            }
        }
        model.put("jobEntryList",jobDTOs);
        List<TitleEntry> titleEntryList = titleDao.getTitleList(userid, null);
        List<TitleDTO> titleDTOs = new ArrayList<TitleDTO>();
        if (titleEntryList!=null && titleEntryList.size()!=0) {
            for (TitleEntry entry : titleEntryList) {
                titleDTOs.add(new TitleDTO(entry));
            }
        }
        model.put("titleEntryList",titleDTOs);
        List<PostionEntry> postionEntryList = postionDao.getPostionList(userid,null);
        List<PostionDTO> postionDTOs = new ArrayList<PostionDTO>();
        if (postionEntryList!=null && postionEntryList.size()!=0) {
            for (PostionEntry entry : postionEntryList) {
                postionDTOs.add(new PostionDTO(entry));
            }
        }
        model.put("postionEntryList",postionDTOs);
        List<PartTimeEntry> partTimeEntryList = partTimeDao.getPartTimeList(userid,null);
        List<PartTimeDTO> partTimeDTOs = new ArrayList<PartTimeDTO>();
        if (partTimeEntryList!=null && partTimeEntryList.size()!=0) {
            for (PartTimeEntry entry : partTimeEntryList) {
                partTimeDTOs.add(new PartTimeDTO(entry));
            }
        }
        model.put("partTimeEntryList",partTimeDTOs);
        List<ResultEntry> resultEntryList = resultDao.getResultList(userid,null);
        List<ResultDTO> resultDTOs = new ArrayList<ResultDTO>();
        if (resultEntryList!=null && resultEntryList.size()!=0) {
            for (ResultEntry entry : resultEntryList) {
                resultDTOs.add(new ResultDTO(entry));
            }
        }
        model.put("resultEntryList",resultDTOs);
        List<CertificateEntry> certificateEntryList = certificateDao.getCertificateList(userid,null);
        List<CertificateDTO> certificateDTOs = new ArrayList<CertificateDTO>();
        if (certificateEntryList!=null && certificateEntryList.size()!=0) {
            for (CertificateEntry entry : certificateEntryList) {
                certificateDTOs.add(new CertificateDTO(entry));
            }
        }
        model.put("certificateEntryList",certificateDTOs);
        List<CourseProjectDTO> courseProjectList = getCourseProjectListBySchoolId(new ObjectId(schoolId));
        model.put("courseProjectList",courseProjectList);
        List<ContinueEducationEntry> continueEducationEntryList = continueEducationDao.getContinueEducationList(userid,null);
        List<ContinueEducationDTO> continueEducationDTOList = new ArrayList<ContinueEducationDTO>();
        if (continueEducationEntryList!=null && continueEducationEntryList.size()!=0) {
            for (ContinueEducationEntry entry : continueEducationEntryList) {
                ContinueEducationDTO cdto = new ContinueEducationDTO(entry);
                if (courseProjectList!=null && courseProjectList.size()!=0) {
                    for (CourseProjectDTO dto : courseProjectList) {
                        if (dto.getId().equals(entry.getCourse())) {
                            cdto.setCourseName(dto.getCourse());
                        }
                    }
                }
                continueEducationDTOList.add(cdto);
            }
        }
        model.put("continueEducationEntryList",continueEducationDTOList);
        ResumeDTO resumeDTO = new ResumeDTO(userEntry,resumeEntry);

        String role = "";
        int userRole=0;
        resumeDTO.setIsmanage(2);
        if (UserRole.isHeadmaster(userEntry.getRole())) {
            userRole=UserRole.HEADMASTER.getRole();
            role = UserRole.HEADMASTER.getDes();
        } else if (UserRole.isTeacher(userEntry.getRole())) {
            userRole=UserRole.TEACHER.getRole();
            role = UserRole.TEACHER.getDes();
            if (UserRole.isManager(userEntry.getRole())) {
                resumeDTO.setIsmanage(1);
            }else{
                resumeDTO.setIsmanage(0);
            }
        } else if (UserRole.isManager(userEntry.getRole())) {
            userRole=UserRole.ADMIN.getRole();
            role = UserRole.ADMIN.getDes();
        } else if (UserRole.isDoorKeeper(userEntry.getRole())) {
            userRole=UserRole.DOORKEEPER.getRole();
            role = UserRole.DOORKEEPER.getDes();
        } else if (UserRole.isDormManager(userEntry.getRole())) {
            userRole=UserRole.DORMMANAGER.getRole();
            role = UserRole.DORMMANAGER.getDes();
        }
        resumeDTO.setUserrole(userRole);
        resumeDTO.setRole(role);
        model.put("resumeDTO",resumeDTO);
    }
}
