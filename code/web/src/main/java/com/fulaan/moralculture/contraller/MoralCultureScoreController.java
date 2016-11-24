package com.fulaan.moralculture.contraller;

import com.fulaan.base.controller.BaseController;
import com.fulaan.moralculture.dto.MoralCultureDTO;
import com.fulaan.moralculture.dto.MoralCultureManageDTO;
import com.fulaan.moralculture.dto.MoralCultureScore;
import com.fulaan.moralculture.service.MoralCultureManageService;
import com.fulaan.moralculture.service.MoralCultureScoreService;
import com.fulaan.myclass.service.ClassService;
import com.fulaan.myschool.controller.GradeView;
import com.fulaan.user.service.UserService;
import com.pojo.school.ClassEntry;
import com.pojo.school.ClassInfoDTO;
import com.pojo.user.UserDetailInfoDTO;
import com.pojo.user.UserEntry;
import com.pojo.user.UserRole;
import com.pojo.utils.DeleteState;
import com.sys.constants.Constant;
import com.sys.exceptions.IllegalParamException;
import com.sys.utils.DateTimeUtils;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fulaan.utils.pojo.KeyValue;
import java.util.*;

/**
 * Created by guojing on 2015/7/2.
 */
@Controller
@RequestMapping("/moralCultureScore")
public class MoralCultureScoreController extends BaseController {

    private static final Logger logger = Logger.getLogger(MoralCultureScoreController.class);

    @Autowired
    private MoralCultureManageService moralCultureManageService;

    @Autowired
    private MoralCultureScoreService moralCultureScoreService;

    //先得到学生所在的班级
    @Autowired
    private ClassService classService;

    @Autowired
    private UserService userService;

    /**
     * 德育项目首页页面
     * @param model
     * @return
     */
    @RequestMapping("/moralCultureHomePage")
    public String moralCultureHomePage(Map<String, Object> model) throws IllegalParamException {
        //获取用户角色
        int userRole=getSessionValue().getUserRole();
        String schoolId = getSessionValue().getSchoolId();
        //根据条件获取德育项目列表
        List<MoralCultureManageDTO> list = moralCultureManageService.selMoralCultureProjectList(schoolId, DeleteState.NORMAL);
        model.put("list",list);

        List<KeyValue> semesters= getSemesters();
        model.put("semesters", semesters);
        String url="";
        //判断用户角色是否是老师角色
        if(UserRole.isTeacher(userRole)){
            List<ClassInfoDTO>  classInfoList = classService.getSimpleClassInfoDTOs(getUserId(), new ObjectId(schoolId));
            Map<String,GradeView> gradeMap=new HashMap<String,GradeView>();
            for(ClassInfoDTO ciDto:classInfoList){
                GradeView gradeView=new GradeView();
                gradeView.setId(ciDto.getGradeId());
                String gradeName=ciDto.getGradeName()==null?"":ciDto.getGradeName();
                gradeView.setName(gradeName);
                gradeMap.put(ciDto.getGradeId(),gradeView);
            }
            List<GradeView> gradeList = new ArrayList<GradeView>();
            for (GradeView grade : gradeMap.values()) {
                gradeList.add(grade);
            }
            sortGradeList(gradeList);
            model.put("gradeList", gradeList);
            //sortClassList(classInfoList);
            //model.put("classList", classInfoList);
            url="moralculture/teachermoralculture";
        }
        //判断用户角色是否是学生角色
        if (UserRole.isStudentOrParent(userRole)){
            url="moralculture/studentmoralculture";
        }
        return url;
    }

    /**
     * 获取年级的班级
     * @param gradeId
     * @return Map
     */
    @RequestMapping("/getGradeClassValue")
    public @ResponseBody Map getGradeClassValue(@RequestParam("gradeId") String gradeId) throws IllegalParamException{
        //获取用户角色
        int userRole=getSessionValue().getUserRole();
        List<ClassInfoDTO>  classInfoList =new ArrayList<ClassInfoDTO>();
        Map map = new HashMap();
        //判断用户角色是否是老师角色
        if(UserRole.isTeacher(userRole)){
            //获取年级的班级
            String schoolId = getSessionValue().getSchoolId();

            List<ClassInfoDTO>  list = classService.getSimpleClassInfoDTOs(getUserId(), new ObjectId(schoolId));
            for(ClassInfoDTO dto: list){
                String className=dto.getClassName()==null?"":dto.getClassName();
                dto.setClassName(className);
                if(gradeId.equals(dto.getGradeId())){
                    classInfoList.add(dto);
                }
            }
            sortClassList(classInfoList);
        }
        map.put("classList",classInfoList);
        return map;
    }

    /**
     * 对班级list进行排序
     * @param list
     */
    private void sortClassList(List<ClassInfoDTO> list){
        Collections.sort(list, new Comparator<ClassInfoDTO>() {
            public int compare(ClassInfoDTO obj1 , ClassInfoDTO obj2) {
                int flag=obj1.getClassName().compareTo(obj2.getClassName());
                return flag;
            }
        });
    }

    /**
     * 对班级list进行排序
     * @param list
     */
    private void sortGradeList(List<GradeView> list){
        Collections.sort(list, new Comparator<GradeView>() {
            public int compare(GradeView obj1 , GradeView obj2) {
                int flag=obj1.getName().compareTo(obj2.getName());
                return flag;
            }
        });
    }

    /**
     * 个人成绩
     * @return
     */
    @RequestMapping("selPersonalMoralCultureScore")
    public @ResponseBody Map<String,Object> selPersonalMoralCultureScore(@RequestParam("semesterId")String semesterId) {
        Map<String,Object> model = new HashMap<String,Object>();
        String userId = "";
        if(UserRole.isParent(getSessionValue().getUserRole()))
        {
            UserEntry ue=userService.searchUserId(getUserId());
            userId=ue.getConnectIds().get(0).toString();
        }else{
            userId = getSessionValue().getId();
        }
        String schoolId = getSessionValue().getSchoolId();
        //根据条件获取德育项目列表
        List<MoralCultureManageDTO> list = moralCultureManageService.selMoralCultureProjectList(schoolId, DeleteState.NORMAL);
        ClassEntry classEntry = classService.getClassEntryByStuId(new ObjectId(userId), Constant.FIELDS);
        MoralCultureDTO personal = moralCultureScoreService.selPersonalMoralCultureScore(userId, classEntry.getGradeId().toString(), classEntry.getID().toString(), semesterId,list);
        model.put("personalScore",personal.getMoralCultureScores());
        return model;
    }

    /**
     * 学生添加修改德育项目成绩
     * @param semesterId
     * @param ids
     * @param scores
     * @return
     */
    @RequestMapping("addOrEditMoralCultureScoreInfo")
    public @ResponseBody
    Map<String ,Object> addOrEditMoralCultureScoreInfo(@RequestParam("semesterId")String semesterId, @RequestParam("ids")List<String> ids,@RequestParam("scores")List<String> scores) {
        Map<String,Object> model = new HashMap<String,Object>();
        String userId = getSessionValue().getId();
        String userName=getSessionValue().getUserName();
        String schoolId = getSessionValue().getSchoolId();
        ClassEntry classEntry = classService.getClassEntryByStuId(getUserId(), Constant.FIELDS);
        MoralCultureDTO mcDto=new MoralCultureDTO();
        mcDto.setUserId(userId);
        mcDto.setUserName(userName);
        mcDto.setSchoolId(schoolId);
        mcDto.setSemesterId(semesterId);
        mcDto.setGradeId(classEntry.getGradeId().toString());
        mcDto.setClassId(classEntry.getID().toString());
        List<MoralCultureScore> mcsList=new ArrayList<MoralCultureScore>();
        for(int i=0;i<ids.size();i++){
            MoralCultureScore mcs=new MoralCultureScore();
            mcs.setProjectId(ids.get(i));
            mcs.setProjectScore(scores.get(i));
            mcs.setCreateTime(new Date());
            mcs.setUpdateTime(new Date());
            mcsList.add(mcs);
        }
        mcDto.setMoralCultureScores(mcsList);
        //学生添加德育项目成绩
        ObjectId id= moralCultureScoreService.addOrEditMoralCultureScoreInfo(mcDto);
        model.put("result",true);
        return model;
    }

    /**
     * 德育项目成绩页面
     * @param model
     * @return
     */
    @RequestMapping("/moralCultureScorePage")
    public String moralCultureScorePage(Map<String, Object> model,String semesterId,String userId) {
        model.put("semesterId", semesterId);
        //获取用户角色
        int userRole=getSessionValue().getUserRole();
        //判断用户角色是否是老师角色
        if(UserRole.isTeacher(userRole)) {
            model.put("userId", userId);
            ClassEntry classEntry = classService.getClassEntryByStuId(new ObjectId(userId), Constant.FIELDS);
            //取得班级下的全部学生id
            List<ObjectId> stuIds=classEntry.getStudents();

            List<UserDetailInfoDTO> userInfos=userService.findUserInfoByIds(stuIds);

            model.put("userInfos", userInfos);
        }

        List<KeyValue> semesters= getSemesters();
        model.put("semesters", semesters);

        return "moralculture/studentmoralculturescore";
    }


    /**
     * 个人成绩vs班级均分
     * @return
     */
    @RequestMapping("selPersonalVsClassAvgScore")
    public @ResponseBody Map<String,Object> selPersonalVsClassAvgScore(@RequestParam("semesterId")String semesterId,String userId) {
        Map<String,Object> model = new HashMap<String,Object>();
        String schoolId = getSessionValue().getSchoolId();
        //根据条件获取德育项目列表
        List<MoralCultureManageDTO> list = moralCultureManageService.selMoralCultureProjectList(schoolId, DeleteState.NORMAL);
        model.put("list",list);
        if("".equals(userId)){
            if(UserRole.isParent(getSessionValue().getUserRole()))
            {
                UserEntry ue=userService.searchUserId(getUserId());
                userId=ue.getConnectIds().get(0).toString();
            }else{
                userId = getSessionValue().getId();
            }
        }
        ClassEntry classEntry = classService.getClassEntryByStuId(new ObjectId(userId), Constant.FIELDS);
        MoralCultureDTO personal = moralCultureScoreService.selPersonalMoralCultureScore(userId,classEntry.getGradeId().toString(),classEntry.getID().toString(),semesterId,list);
        model.put("personalScore",personal.getMoralCultureScores());

        List<MoralCultureScore> avgScore=moralCultureScoreService.selClassAvgMoralCultureScore(classEntry.getGradeId().toString(),classEntry.getID().toString(),semesterId,list);
        model.put("avgScore",avgScore);
        return model;
    }

    /**
     * 全班级学生成绩
     * @return
     */
    @RequestMapping("selClassAllStudentScore")
    public @ResponseBody Map<String,Object> selClassAllStudentScore(@RequestParam("semesterId")String semesterId,@
            RequestParam("gradeId")String gradeId,@RequestParam("classId")String classId) {
        Map<String,Object> model = new HashMap<String,Object>();
        String schoolId = getSessionValue().getSchoolId();
        //根据条件获取德育项目列表
        List<MoralCultureManageDTO> list = moralCultureManageService.selMoralCultureProjectList(schoolId, DeleteState.NORMAL);
        ClassEntry classEntry = classService.getClassEntryById(new ObjectId(classId), Constant.FIELDS);
        List<MoralCultureDTO> allScore=moralCultureScoreService.selClassAllStudentScore(classEntry,semesterId,list);
        model.put("allScore",allScore);
        return model;
    }


    private List<KeyValue> getSemesters(){
        List<KeyValue> list=new ArrayList<KeyValue>();
        KeyValue keyValue=new KeyValue();
        keyValue.setKey(1);
        keyValue.setValue("2013-2014学年&nbsp;&nbsp;&nbsp;第二学期");
        list.add(keyValue);

        DateTimeUtils time=new DateTimeUtils();
        //取得当前月
        int currMonth=time.getMonth();
        //取得当前年
        int currYear=time.getYear();
        for(int year=2015;year<=currYear;year++) {

            String schoolYear;
            if(year==currYear) {
                if (currMonth >= 2) {
                    KeyValue value=new KeyValue();
                    schoolYear = (year - 1) + "-" + year + "学年&nbsp;&nbsp;&nbsp;第一学期";
                    value.setKey(list.size()+1);
                    value.setValue(schoolYear);
                    list.add(value);
                    KeyValue value1=new KeyValue();
                    schoolYear = (year - 1) + "-" + year + "学年&nbsp;&nbsp;&nbsp;第二学期";
                    value1.setKey(list.size()+1);
                    value1.setValue(schoolYear);
                    list.add(value1);
                    if (currMonth >= 9) {
                        KeyValue value2 = new KeyValue();
                        schoolYear = year + "-" + (year + 1) + "学年&nbsp;&nbsp;&nbsp;第一学期";
                        value2.setKey(list.size() + 1);
                        value2.setValue(schoolYear);
                        list.add(value2);
                    }
                }
            }else{
                KeyValue value=new KeyValue();
                schoolYear = (year - 1) + "-" + year + "学年&nbsp;&nbsp;&nbsp;第一学期";
                value.setKey(list.size()+1);
                value.setValue(schoolYear);
                list.add(value);
                KeyValue value1=new KeyValue();
                schoolYear = (year - 1) + "-" + year + "学年&nbsp;&nbsp;&nbsp;第二学期";
                value1.setKey(list.size()+1);
                value1.setValue(schoolYear);
                list.add(value1);
            }
        }
        sortSemesters(list);
        return list;
    }

    /**
     * 对班级list进行排序
     * @param list
     */
    private void sortSemesters(List<KeyValue> list){
        Collections.sort(list, new Comparator<KeyValue>() {
            public int compare(KeyValue obj1 , KeyValue obj2) {
                int flag=obj2.getKey()-obj1.getKey();
                return flag;
            }
        });
    }
}
