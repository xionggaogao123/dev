package com.fulaan.managecount.controller;

import com.fulaan.base.controller.BaseController;
import com.fulaan.cache.CacheHandler;
import com.fulaan.educationbureau.dto.EducationBureauDTO;
import com.fulaan.educationbureau.service.EducationBureauService;
import com.fulaan.managecount.dto.ManageCountDTO;
import com.fulaan.managecount.service.ManageCountService;
import com.fulaan.myschool.controller.GradeView;
import com.fulaan.utils.CommonUtils;
import com.fulaan.utils.pojo.KeyValue;
import com.pojo.WebSpiderSchool.WebSpiderSchoolDTO;
import com.pojo.app.RegionDTO;
import com.pojo.managecount.ParentCountType;
import com.pojo.managecount.StudentCountType;
import com.pojo.managecount.TeacherCountType;
import com.pojo.managecount.TimeType;
import com.pojo.school.ClassInfoDTO;
import com.pojo.school.GradeType;
import com.pojo.school.SchoolDTO;
import com.pojo.school.SchoolType;
import com.pojo.user.UserRole;
import com.sys.constants.Constant;
import com.sys.exceptions.IllegalParamException;
import com.sys.exceptions.ResultTooManyException;
import com.sys.utils.DateTimeUtils;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by guojing on 2015/4/8.
 */
@Controller
@RequestMapping("/manageCount")
public class ManageCountController extends BaseController {
    private static final Logger logger =Logger.getLogger(ManageCountController.class);

    @Autowired
    private ManageCountService manageCountService;

    @Autowired
    private EducationBureauService educationBureauService;

    /*
    * 跳转到教育局管辖下的所有学校页面
    * */
    @RequestMapping("/countMain")
    public String countMain(Map<String, Object> model,@RequestParam(required=false,defaultValue="1")int a) {
        //获取用户角色
        int userRole=getSessionValue().getUserRole();
        String url="";
        //判断用户角色是否是教育局角色
        if(UserRole.isEducation(userRole)){
            //查询教育局用户管理下的学校
            EducationBureauDTO dto=manageCountService.getEducationByUserId(getUserId());
            if("安徽省教科院".equals(dto.getEducationName())){
                String provinceId=dto.getProvince();

                model.put("provinceId",provinceId);

                List<RegionDTO> regions=educationBureauService.getRegionEntryList(0, new ObjectId(provinceId));

                model.put("citys",regions);

                List<SchoolType> schTypes=SchoolType.getSchoolType(14);
                model.put("schTypes",schTypes);

                if (a==10000) {
                    url="manageCount/countMainc";//管理统计-教育局首页
                } else {
                    url="manageCount/countMain";//管理统计-教育局首页
                }
            }else{
                List<SchoolDTO> list=manageCountService.getEducationSchoolByUserId(getUserId());
                model.put("list",list);
                if (a==10000) {
                    url="manageCount/countMain2c";
                } else {
                    url="manageCount/countMain2";
                }
            }
        }else{

        }
        return url;
    }

    /**
    * 管理统计-学校信息
    * @param schoolType
    * @return Map
    */
    @RequestMapping("/getSchoolValueList")
    public @ResponseBody Map getSchoolValueList(
            @RequestParam("provinceId")String provinceId,
            String cityId,
            String countyId,
            @RequestParam(required=false,defaultValue="0")int schoolType,
            int page,
            int pageSize
    ){
        Map map = new HashMap();
        List<WebSpiderSchoolDTO> list=manageCountService.getSchoolValueList(provinceId,cityId,countyId,schoolType);
        List<WebSpiderSchoolDTO> resultList=listImitatePage(list,page,pageSize);
        map.put("rows",resultList);
        map.put("total", list.size());
        map.put("page", page);
        map.put("pageSize", pageSize);
        return map;
    }

    /**
     * 模拟对list分页查询
     * @param list
     * @param page
     * @param pageSize
     * @return
     */
    private List<WebSpiderSchoolDTO> listImitatePage(List<WebSpiderSchoolDTO> list,int page,int pageSize) {
        int totalCount =list.size();
        int pageCount=0;
        int m=totalCount%pageSize;
        if(m>0){
            pageCount=totalCount/pageSize+1;
        } else {
            pageCount=totalCount/pageSize;
        }
        List<WebSpiderSchoolDTO> subList=new ArrayList<WebSpiderSchoolDTO>();
        if(list!=null&&list.size()>0) {
            if (m == 0) {
                subList = list.subList((page - 1) * pageSize, pageSize * (page));
            } else {
                if (page == pageCount) {
                    subList = list.subList((page - 1) * pageSize, totalCount);
                } else {
                    subList = list.subList((page - 1) * pageSize, pageSize * (page));
                }
            }
        }
        return subList;
    }


    /*
    * 跳转到教育局管辖下的所有学校页面
    * */
    @RequestMapping("/educationschools")
    public String educationSchools(Map<String, Object> model,@RequestParam(required=false,defaultValue="1")int a) {
        //获取用户角色
        int userRole=getSessionValue().getUserRole();
        String url="";
        //判断用户角色是否是教育局角色
        if(UserRole.isEducation(userRole)){
            //查询教育局用户管理下的学校
            List<SchoolDTO> list=manageCountService.getEducationSchoolByUserId(getUserId());
            model.put("list",list);
            DateTimeUtils time=new DateTimeUtils();
            String currTime=time.getChineseDate();
            model.put("currTime",currTime);
            //获取时间区域名称list
            List<KeyValue> timeAreaList=CommonUtils.enumMapToList(TimeType.getTimeTypeMap());
            model.put("timeAreas",timeAreaList);
            if (a==10000) {
                url="manageCount/educationSchoolsc";
            } else {
                url = "manageCount/educationSchools";//管理统计-教育局首页
            }
        }else{

        }
        return url;
    }

    /**
     * 管理统计-获取年级Type
     * @param schoolType
     * @return Map
     */
    @RequestMapping("/getSchoolGradeTypeValue")
    public @ResponseBody Map getSchoolGradeTypeValue(@RequestParam("schoolType")int schoolType){
        Map map = new HashMap();
        List<GradeType> gradelist=GradeType.getGradeTypeBySchoolType(schoolType);
        List<GradeView> retList =new ArrayList<GradeView>();
        for(GradeType gt:gradelist){
            GradeView gv=new GradeView();
            gv.setGradeType(gt.getId());
            gv.setName(gt.getName());
            retList.add(gv);
        }
        map.put("retList",retList);
        //获取年级的Type
        return map;
    }

    /**
     * 教育局统计
     * @param gradeType
     * @param schoolId
     * @param dateStart
     * @param dateEnd
     * @return
     */
    @RequestMapping("/eduschoolstotaldata")
    public  @ResponseBody String eduSchoolsTotalData(
            @RequestParam("gradeType") int gradeType,
            @RequestParam("schoolId") String schoolId,
            @RequestParam("dateStart") String dateStart,
            @RequestParam("dateEnd") String dateEnd) throws ResultTooManyException {
        try {
            dateStart=handleTime(dateStart, 1);
            dateEnd=handleTime(dateEnd, 2);
            String params="";
            if(schoolId!=null&&!"".equals(schoolId)){//当班级id且年级id为空时，判断学校id是否为空
                params+=schoolId;
            }
            params+=gradeType;
            params+=dateStart;
            params+=dateEnd;
            EducationBureauDTO dto=manageCountService.getEducationByUserId(getUserId());
            params+=dto.getId();
            String key=   CacheHandler.getKeyString(CacheHandler.CACHE_SCHOOL_MANAGER_KEY,params);
            String value = CacheHandler.getStringValue(key);
            if (value!=null){
                return value;
            }
            String userId=getUserId().toString();
            //查询用户统计数据
            Map<String, Object> result = manageCountService.eduSchoolsTotalData(userId,gradeType,schoolId,dateStart,dateEnd);
            String jsonstr = JSONObject.valueToString(result);     //将集合解析为 json对象语句
            CacheHandler.cache(key,jsonstr, Constant.SECONDS_IN_DAY);
            return jsonstr;
            //return result;
        }catch (Exception ex) {
            logger.error("", ex);
        }
        return null;
    }

    /**
     * 教育局用户查询各学校访问统计
     * @param gradeType
     * @param schoolId
     * @param dateStart
     * @param dateEnd
     * @return
     */
    @RequestMapping("/eduAccessAnalysis")
    public  @ResponseBody List<ManageCountDTO> eduAccessAnalysis(
            @RequestParam("gradeType") int gradeType,
            @RequestParam("schoolId") String schoolId,
            @RequestParam("dateStart") String dateStart,
            @RequestParam("dateEnd") String dateEnd) throws ResultTooManyException {
        dateStart=handleTime(dateStart, 1);
        dateEnd=handleTime(dateEnd, 2);
        //查询用户的访问统计
        List<ManageCountDTO> accessTotalList = manageCountService.eduAccessAnalysis(getUserId().toString(),gradeType,schoolId,dateStart,dateEnd);
        return accessTotalList;
    }

    /**
     * 教育局用户查询各学校访问人员统计
     * @param gradeType
     * @param schoolId
     * @param dateStart
     * @param dateEnd
     * @return
     */
    @RequestMapping("/eduPeopleTotal")
    public @ResponseBody Map<String, Object> eduPeopleTotal(
            @RequestParam("gradeType") int gradeType,
            @RequestParam("schoolId") String schoolId,
            @RequestParam("dateStart") String dateStart,
            @RequestParam("dateEnd") String dateEnd)
            throws ResultTooManyException{
        Map<String, Object> result = new HashMap<String, Object>();
        dateStart=handleTime(dateStart, 1);
        dateEnd=handleTime(dateEnd, 2);
        //分别查询老师、学生、家长访问的数量，返回Map集合
        result = manageCountService.eduPeopleTotal(getUserId().toString(), gradeType, schoolId, dateStart, dateEnd);
        return result;
    }

    /**
     * 备课统计
     * @param gradeType
     * @param schoolId
     * @param dateStart
     * @param dateEnd
     * @return
     */
    @RequestMapping("/eduLessonCount")
    public @ResponseBody Map<String,Object> eduLessonCount(
            @RequestParam("gradeType") int gradeType,
            @RequestParam("schoolId") String schoolId,
            @RequestParam("dateStart") String dateStart,
            @RequestParam("dateEnd") String dateEnd) {
        Map<String, Object> result = new HashMap<String, Object>();
        dateStart=handleTime(dateStart, 1);
        dateEnd=handleTime(dateEnd, 2);
        //备课统计、资源统计，返回Map集合
        result = manageCountService.eduLessonCount(getUserId().toString(), gradeType, schoolId, dateStart, dateEnd);
        return result;
    }

    /**
     * 跳转每个学校页面
     *
     * @param model
     * @return
     */
    @RequestMapping("/schooltotal")
    public String schooltotal(@RequestParam(required=false,defaultValue="1")int a, Map<String, Object> model) {
       
    	String schoolid=getSessionValue().getSchoolId();
    	//获取用户角色
        int userRole=getSessionValue().getUserRole();
        String url="";
        //获取学校信息
        SchoolDTO schoolDTO=manageCountService.searchSchoolInfo(schoolid);
        //获取当前时间
        DateTimeUtils time=new DateTimeUtils();
        String currTime=time.getChineseDate();
        model.put("schoolDTO",schoolDTO);
        model.put("currTime",currTime);
        //获取时间区域名称list
        List<KeyValue> timeAreaList=CommonUtils.enumMapToList(TimeType.getTimeTypeMap());
        model.put("timeAreas",timeAreaList);
        model.put("schoolid",schoolid);
        //判断用户角色是否是教育局角色或用户角色是校长，且当用户角色是校长时查询的学校必须是自己所在学校
        if(UserRole.isEducation(userRole)||((UserRole.isHeadmaster(userRole)||UserRole.isManager(userRole))&&schoolid.equals(getSessionValue().getSchoolId()))){
            //获取学校下的全部年级信息
            List<GradeView> gradelist = manageCountService.searchSchoolGradeInfo(schoolid);
            model.put("gradelist", gradelist);
            if(a==10000) {
                url="manageCount/totalpagec";//管理统计-校长、教育局用户统计首页
            } else {
                url="manageCount/totalpage";//管理统计-校长、教育局用户统计首页
            }
        }else if(UserRole.isTeacher(userRole)&&schoolid.equals(getSessionValue().getSchoolId())){
            if(a==10000) {
                url="manageCount/teachertotalpagec";//管理统计-老师用户统计首页
            } else {
                url="manageCount/teachertotalpage";//管理统计-老师用户统计首页
            }
        }
        return url;
    }


    /**
     * 管理统计-获取老师所带的班级
     * @return Map
     */
    @RequestMapping("/getTeacherClass")
    public @ResponseBody Map getTeacherClass() throws IllegalParamException {
        Map map = new HashMap();
        ObjectId teacherId=getUserId();
        ObjectId schoolId=new ObjectId(getSessionValue().getSchoolId());
        //获取年级的班级
        List<ClassInfoDTO> classList=manageCountService.getTeacherClassesInfo(teacherId,schoolId);
        map.put("classList",classList);
        return map;
    }

    /**
     * 访问统计
     * @param gradeId
     * @param classId
     * @param schoolid
     * @return
     */
    @RequestMapping("/schooltotaldata")
    public  @ResponseBody String schoolTotalData(
            @RequestParam("gradeId") String gradeId,
            @RequestParam("classId") String classId,
            @RequestParam("schoolid") String schoolid,
            @RequestParam("dateStart") String dateStart,
            @RequestParam("dateEnd") String dateEnd) throws ResultTooManyException {
        try {
            dateStart=handleTime(dateStart, 1);
            dateEnd=handleTime(dateEnd, 2);
            String params="";
            if(classId!=null&&!"".equals(classId)){ //判断班级id是否为空
                params+=classId;
            }

            if(gradeId!=null&&!"".equals(gradeId)){//当班级id为空时，判断年级id是否为空
                params+=gradeId;
            }
            if(schoolid!=null&&!"".equals(schoolid)){//当班级id且年级id为空时，判断学校id是否为空
                params+=schoolid;
            }
            params+=dateStart;
            params+=dateEnd;
            String key= CacheHandler.getKeyString(CacheHandler.CACHE_SCHOOL_MANAGER_KEY,params);
            String value = CacheHandler.getStringValue(key);
            if (value!=null){
                return value;
            }

            //查询用户统计数据
            Map<String, Object> result = manageCountService.schoolTotalData(gradeId, classId, schoolid, dateStart, dateEnd);
            String jsonstr = JSONObject.valueToString(result);     //将集合解析为 json对象语句
            CacheHandler.cache(key,jsonstr, Constant.SECONDS_IN_DAY);
            return jsonstr;
            //return result;
        }catch (Exception ex) {
			logger.error("", ex);
		}
        return null;
    }

    /**
     * 访问统计
     * @param classId
     * @param dateStart
     * @param dateEnd
     * @return
     */
    @RequestMapping("/teacherClassTotalData")
    public  @ResponseBody String teacherClassTotalData(
            @RequestParam("classId") String classId,
            @RequestParam("dateStart") String dateStart,
            @RequestParam("dateEnd") String dateEnd) throws ResultTooManyException {
        try {
            dateStart=handleTime(dateStart, 1);
            dateEnd=handleTime(dateEnd, 2);
            String params="";
            if(classId!=null&&!"".equals(classId)){ //判断班级id是否为空
                params+=classId;
            }
            params+=dateStart;
            params+=dateEnd;
            String key= CacheHandler.getKeyString(CacheHandler.CACHE_SCHOOL_MANAGER_KEY,params);
            String value = CacheHandler.getStringValue(key);
            if (value!=null){
                 return value;
            }

            //查询用户统计数据
            Map<String, Object> result = manageCountService.teacherClassTotalData(classId, dateStart, dateEnd);
            String jsonstr = JSONObject.valueToString(result);     //将集合解析为 json对象语句
            CacheHandler.cache(key,jsonstr, Constant.SECONDS_IN_DAY);
            return jsonstr;
            //return result;
        }catch (Exception ex) {
            logger.error("", ex);
        }
        return null;
    }


    /**
     * 访问统计
     * @param gradeId
     * @param classId
     * @param schoolid
     * @return
     */
    @RequestMapping("/accessAnalysis")
    public  @ResponseBody List<ManageCountDTO> accessAnalysis(
            @RequestParam("gradeId") String gradeId,
            @RequestParam("classId") String classId,
            @RequestParam("schoolid") String schoolid,
            @RequestParam("dateStart") String dateStart,
            @RequestParam("dateEnd") String dateEnd) throws ResultTooManyException {
        dateStart=handleTime(dateStart, 1);
        dateEnd=handleTime(dateEnd, 2);
        //查询用户的访问统计
        List<ManageCountDTO> accessTotalList = manageCountService.accessAnalysis(gradeId,classId,schoolid,dateStart,dateEnd);
        return accessTotalList;
    }

    /**
     * 访问人员统计
     * @param gradeId
     * @param classId
     * @return
     */
    @RequestMapping("/peopletotal")
    public @ResponseBody Map<String, Object> peopletotal(
            @RequestParam("gradeId") String gradeId,
            @RequestParam("classId") String classId,
            @RequestParam("schoolid") String schoolid,
            @RequestParam("dateStart") String dateStart,
            @RequestParam("dateEnd") String dateEnd)
            throws ResultTooManyException{
        Map<String, Object> result = new HashMap<String, Object>();
        dateStart=handleTime(dateStart, 1);
        dateEnd=handleTime(dateEnd, 2);
        //分别查询老师、学生、家长访问的数量，返回Map集合
        result = manageCountService.peopletotal(gradeId,classId,schoolid,dateStart,dateEnd);
        return result;
    }

    /**
     * 备课统计
     * @param gradeId
     * @param classId
     * @return
     */
    @RequestMapping("/lessonCount")
    public @ResponseBody Map<String,Object> lessonCount(
            @RequestParam("gradeId") String gradeId,
            @RequestParam("classId") String classId,
            @RequestParam("schoolid") String schoolid,
            @RequestParam("dateStart") String dateStart,
            @RequestParam("dateEnd") String dateEnd) {
        Map<String, Object> result = new HashMap<String, Object>();
        dateStart=handleTime(dateStart, 1);
        dateEnd=handleTime(dateEnd, 2);
        //备课统计、资源统计，返回Map集合
        result = manageCountService.lessonCount(gradeId,classId,schoolid,dateStart,dateEnd);
        return result;
    }


    /**
     * 管理统计-功能使用统计
     * @param schoolId
     * @return
     */
    @RequestMapping("/functionUse")
    public String functionUseCount(
            Map<String, Object> model,
            @RequestParam(required=false,defaultValue="1")int a){
    	
    	
    	String schoolId=getSessionValue().getSchoolId();
    	
        //获取用户角色
        int userRole=getSessionValue().getUserRole();
        String url="";
        //获取学校信息
        SchoolDTO schoolDTO = manageCountService.searchSchoolInfo(schoolId);
        model.put("schoolDTO", schoolDTO);
        model.put("schoolId", schoolId);
        //获取时间区域名称list
        List<KeyValue> timeAreaList = CommonUtils.enumMapToList(TimeType.getTimeTypeMap());
        model.put("timeAreas", timeAreaList);
        DateTimeUtils time = new DateTimeUtils();
        //获取当前时间
        String currTime = time.getChineseDate();
        model.put("currTime", currTime);
        //判断用户角色是否是教育局角色或用户角色是校长，且当用户角色是校长时查询的学校必须是自己所在学校
        if(UserRole.isEducation(userRole)||((UserRole.isHeadmaster(userRole)||UserRole.isManager(userRole))&&schoolId.equals(getSessionValue().getSchoolId()))){
            //获取过滤条件角色List
            List<KeyValue> roleList = CommonUtils.enumMapToList(UserRole.getManageCountRoleMap());
            model.put("roles", roleList);
            //获取老师权限的功能名称list
            List<KeyValue> teachFunList = CommonUtils.enumMapToList(TeacherCountType.getTeachFunMap());
            model.put("roleFuns", teachFunList);
            //获取学校的年级信息List
            List<GradeView> gradeList = manageCountService.searchSchoolGradeInfo(schoolId);
            model.put("grades", gradeList);
            if(a==10000) {
                url="manageCount/functionUseCountc";//管理统计-功能使用统计
            } else {
                url="manageCount/functionUseCount";//管理统计-功能使用统计
            }
        }else if(UserRole.isTeacher(userRole)&&schoolId.equals(getSessionValue().getSchoolId())){
            //获取过滤条件角色List
            List<KeyValue> roleList = CommonUtils.enumMapToList(UserRole.getTeacherManageCountRoleMap());
            model.put("roles", roleList);
            //获取老师权限的功能名称list
            List<KeyValue> stuFunList = CommonUtils.enumMapToList(StudentCountType.getStudFunMap());
            model.put("roleFuns", stuFunList);
            if(a==10000) {
                url="manageCount/teacherFunctionUseCountc";//管理统计-老师用户功能使用统计
            } else {
                url="manageCount/teacherFunctionUseCount";//管理统计-老师用户功能使用统计
            }
        }
        return url;
    }

    /**
     * 管理统计-获取时间区间
     * @param timeArea
     * @return Map
     */
    @RequestMapping("/getTimeAreaVal")
    public @ResponseBody Map getTimeAreaValue(@RequestParam("timeArea") Integer timeArea){
        Map map = new HashMap();
        DateTimeUtils time=new DateTimeUtils();
        //获取当前日期
        String currDate=time.getCurrDate();
        //判断时间区域是否是全部
        if(timeArea== TimeType.ALL.getState()){
            String dateStart="";
            int userRole=getSessionValue().getUserRole();
            //判断用户角色是否是教育局角色
            if(UserRole.isEducation(userRole)){
                //查询教育局用户管理下的学校
                EducationBureauDTO dto=manageCountService.getEducationByUserId(getUserId());
                if(dto!=null){
                    dateStart=dto.getSchoolCreateDate();
                }
                if(dateStart==null||"".equals(dateStart)){
                    dateStart="2014-09-01";
                }
            }else{
                String schoolId=getSessionValue().getSchoolId();
                dateStart=time.getLongToStrTime(new ObjectId(schoolId).getTime());
            }
            map.put("dateStart",dateStart);
            map.put("dateEnd",currDate);
        }else if(timeArea== TimeType.CURR_SCHOOL_YEAR.getState()){//判断时间区域是否是本学年
            //取得当前月
            int currMonth=time.getMonth();
            //取得当前年
            int currYear=time.getYear();
            int startYear;
            int endYear;
            //判断当前月是否在8月到12月之间（包含12月），及判断当前时间是否在下半年的学期
            if (currMonth > 8 && currMonth < 13) {//如果在
                startYear = currYear;//学年的开始年是当前年
                endYear = currYear + 1;//学年的结束年是下一年
            } else {//如果不在
                startYear = currYear - 1;//学年的开始年是上一年
                endYear = currYear;//学年的结束年是当前年
            }
            map.put("dateStart",startYear+"-08-31");
            map.put("dateEnd",endYear+"-08-30");
        }else if(timeArea==TimeType.CURR_SCHOOL_TERM.getState()){//判断时间区域是否是本学期
            //取得当前月
            int currMonth=time.getMonth();
            //取得当前年
            int currYear=time.getYear();
            String dateStart="";
            String dateEnd="";
            //判断当前月是否在8月到12月之间（包含12月），及判断当前时间是否在下半年的学期
            if ((currMonth > 8 && currMonth < 13)||currMonth<2) {//如果在
                if(currMonth<2){
                    currYear=currYear-1;
                }
                dateStart=currYear+"-09-01";
                dateEnd=currYear + 1+"-01-31";
            } else {//如果不在
                dateStart=currYear+"-02-01";
                dateEnd=currYear+"-08-31";
            }
            map.put("dateStart",dateStart);
            map.put("dateEnd",dateEnd);
        }else if(timeArea==TimeType.CURR_MONTH.getState()){//判断时间区域是否是本月
            map.put("dateStart",time.getMinMonthDate(currDate));
            map.put("dateEnd",time.getMaxMonthDate(currDate));
        }else if(timeArea==TimeType.CURR_WEEK.getState()){//判断时间区域是否是本周
            map.put("dateStart",time.getCurrentMonday(currDate));
            map.put("dateEnd",time.getPreviousSunday(currDate));
        }else if(timeArea==TimeType.CURR_DAY.getState()){//判断时间区域是否是本周
            map.put("dateStart",currDate);
            map.put("dateEnd", currDate);
        }else if(timeArea==TimeType.USER_DEFINED.getState()){//判断时间区域是否是自定义
            map.put("dateStart","");
            map.put("dateEnd","");
        }
        return map;
    }

    /**
     * 管理统计-获取角色功能
     * @param role
     * @return Map
     */
    @RequestMapping("/getRoleFunValue")
    public @ResponseBody Map getRoleFunValue(@RequestParam("role") Integer role){
        Map map = new HashMap();
        //判断用户角色是否是老师
        if(role==UserRole.TEACHER.getRole()){
            //获取老师权限的功能名称list
            List<KeyValue> roleList= CommonUtils.enumMapToList(TeacherCountType.getTeachFunMap());
            map.put("roleFuns",roleList);
        }else if(role==UserRole.STUDENT.getRole()){//判断用户角色是否是学生
            //获取学生权限的功能名称list
            List<KeyValue> roleList=CommonUtils.enumMapToList(StudentCountType.getStudFunMap());
            map.put("roleFuns",roleList);
        }else if(role==UserRole.PARENT.getRole()){//判断用户角色是否是家长
            //获取家长权限的功能名称list
            List<KeyValue> roleList=CommonUtils.enumMapToList(ParentCountType.getParentFunMap());
            map.put("roleFuns",roleList);
        }
        return map;
    }

    /**
     * 管理统计-获取年级的班级
     * @param gradeId
     * @return Map
     */
    @RequestMapping("/getGradeClassValue")
    public @ResponseBody Map getGradeClassValue(@RequestParam("gradeId") String gradeId){
        Map map = new HashMap();
        //获取年级的班级
        List<ClassInfoDTO> classList=manageCountService.getGradeClassesInfo(gradeId);
        map.put("classList",classList);
        return map;
    }

    /**
     * 管理统计-校园排行榜
     * @param dateStart
     * @param dateEnd
     * @param role
     * @param gradeId
     * @param classId
     * @param funId
     * @return Map
     */
    @RequestMapping("/rankingList")
    public @ResponseBody Map getSchoolRankingList(
            String schoolId,
            String gradeId,
            String classId,
            @RequestParam("dateStart") String dateStart,
            @RequestParam("dateEnd") String dateEnd,
            @RequestParam("role") Integer role,
            @RequestParam("funId") Integer funId,
            @RequestParam("size") Integer size)
            throws ResultTooManyException {
        Map map = new HashMap();
        size=size==null?10:size;
        gradeId=gradeId==null?"":gradeId;
        classId=classId==null?"":classId;
        dateStart=handleTime(dateStart, 1);
        dateEnd=handleTime(dateEnd, 2);
        //判断查询的功能是否是微校园发帖数或微家园发帖数
        if(funId==TeacherCountType.MICRO_CAMPUS_POST_NUM.getState()
                ||funId==StudentCountType.MICRO_HOME_POST_NUM.getState()
                ||funId==ParentCountType.MICRO_HOME_POST_NUM.getState()){
            //微校园、微家园发帖数 *
            map=manageCountService.getMicroCampusOrHomePostNum(role,size, schoolId, gradeId, classId, dateStart, dateEnd);
        }

        if(role==UserRole.TEACHER.getRole()) {
            if(funId==TeacherCountType.PREPARATION_UPLOAD_NUM.getState()){
                //班级课程上传、备课上传数
                map = manageCountService.getClassOrPrepUploadNum(funId, size, schoolId, gradeId, classId, dateStart, dateEnd);
            }else if(funId==TeacherCountType.HOMEWORK_UPLOAD_NUM.getState()){
                //作业上传数*
                map=manageCountService.getHomeworkUploadNum(size, schoolId, gradeId, classId, dateStart, dateEnd);
            }else if(funId==TeacherCountType.PAPERS_UPLOAD_NUM.getState()){
                //试卷上传数*
                map=manageCountService.getPapersUploadNum(size, schoolId, gradeId, classId, dateStart, dateEnd);
            }else if(funId==TeacherCountType.NOTICE_NUM.getState()){
                //通知发布数*
                map = manageCountService.getNoticePublishNum(size, schoolId, gradeId, classId, dateStart, dateEnd);
            }
        }else if(role==UserRole.STUDENT.getRole()){
            if(funId==StudentCountType.CLASSES_WATCH_NUM.getState()){
                //班级课程观看数*
                map=manageCountService.getClassesWatchNum(size, schoolId, gradeId, classId, dateStart, dateEnd);
            }else if(funId==StudentCountType.JOB_COMPLETION_NUM.getState()){
                //作业完成数*
                map=manageCountService.getJobCompletionNum(size, schoolId, gradeId, classId, dateStart, dateEnd);
            }else if(funId==StudentCountType.PAPERS_COMPLETION_NUM.getState()){
                //试卷完成数*
                map=manageCountService.getPapersCompletionNum(size, schoolId, gradeId, classId, dateStart, dateEnd);
            }else if(funId==StudentCountType.CLOUD_CURRICULUM_VIEW_NUM.getState()){
                //云课程观看数*
                map=manageCountService.getCloudCurriculumViewNum(size, schoolId, gradeId, classId, dateStart, dateEnd);
            }
        }else if(role==UserRole.PARENT.getRole()){

        }

        return map;
    }

    /**
     * 管理统计-使用时间分布图
     * @param dateStart
     * @param dateEnd
     * @param role
     * @param gradeId
     * @param classId
     * @param funId
     * @return Map
     */
    @RequestMapping("/useTimeDistMap")
    public @ResponseBody Map getUseTimeDistMap(
            String schoolId,
            String gradeId,
            String classId,
            @RequestParam("dateStart") String dateStart,
            @RequestParam("dateEnd") String dateEnd,
            @RequestParam("role") Integer role,
            @RequestParam("funId") Integer funId)
            throws ResultTooManyException{
        Map map = new HashMap();
        gradeId=gradeId==null?"":gradeId;
        classId=classId==null?"":classId;
        dateStart=handleTime(dateStart, 1);
        dateEnd=handleTime(dateEnd, 2);
        if(funId==TeacherCountType.MICRO_CAMPUS_POST_NUM.getState()
                ||funId==StudentCountType.MICRO_HOME_POST_NUM.getState()
                ||funId==ParentCountType.MICRO_HOME_POST_NUM.getState()){
            //微校园、微家园发帖数 *
            map=manageCountService.getMicroCampusOrHomePostUseTime(role, schoolId, gradeId, classId, dateStart, dateEnd);
        }

        if(role==UserRole.TEACHER.getRole()) {
            if(funId==TeacherCountType.PREPARATION_UPLOAD_NUM.getState()) {
                //班级课程、备课上传数
                map = manageCountService.getClassOrPrepUploadUseTime(funId, schoolId, gradeId, classId, dateStart, dateEnd);
            }else if(funId==TeacherCountType.HOMEWORK_UPLOAD_NUM.getState()){
                //作业上传数
                map=manageCountService.getHomeworkUploadUseTime(schoolId, gradeId, classId, dateStart, dateEnd);
            }else if(funId==TeacherCountType.PAPERS_UPLOAD_NUM.getState()){
                //试卷上传数
                map=manageCountService.getPapersUploadUseTime(schoolId, gradeId, classId, dateStart, dateEnd);
            }else if(funId==TeacherCountType.NOTICE_NUM.getState()){
                //通知发布数
                map=manageCountService.getNoticeUseTime(schoolId, gradeId, classId, dateStart, dateEnd);
            }
        }else if(role==UserRole.STUDENT.getRole()){
            if(funId==StudentCountType.CLASSES_WATCH_NUM.getState()){
                //班级课程观看数
                map=manageCountService.getClassesWatchUseTime(schoolId, gradeId, classId, dateStart, dateEnd);
            }else if(funId==StudentCountType.JOB_COMPLETION_NUM.getState()){
                //作业完成数
                map=manageCountService.getJobCompletionUseTime(schoolId, gradeId, classId, dateStart, dateEnd);
            }else if(funId==StudentCountType.PAPERS_COMPLETION_NUM.getState()){
                //试卷完成数
                map=manageCountService.getPapersCompletionUseTime(schoolId, gradeId, classId, dateStart, dateEnd);
            }else if(funId==StudentCountType.CLOUD_CURRICULUM_VIEW_NUM.getState()){
                //云课程观看数
                map=manageCountService.getCloudCurriculumViewUseTime(schoolId, gradeId, classId, dateStart, dateEnd);
            }
        }else if(role==UserRole.PARENT.getRole()){

        }
        return map;
    }


    /**
     * 管理统计-分页查询数量
     * @param dateStart
     * @param dateEnd
     * @param role
     * @param gradeId
     * @param classId
     * @param funId
     * @return Map
     */
    @RequestMapping("/pagingQuery")
    public @ResponseBody Page<ManageCountDTO> getPagingQueryList(
            String schoolId,
            String gradeId,
            String classId,
            @RequestParam("dateStart") String dateStart,
            @RequestParam("dateEnd") String dateEnd,
            @RequestParam("role") Integer role,
            @RequestParam("funId") Integer funId,
            Pageable pageable) throws ResultTooManyException {
        gradeId=gradeId==null?"":gradeId;
        classId=classId==null?"":classId;

        dateStart=handleTime(dateStart, 1);
        dateEnd=handleTime(dateEnd, 2);

        if(funId==TeacherCountType.MICRO_CAMPUS_POST_NUM.getState()
                ||funId==StudentCountType.MICRO_HOME_POST_NUM.getState()
                ||funId==ParentCountType.MICRO_HOME_POST_NUM.getState()){
            //微校园、微家园发帖数 *
            return manageCountService.getMicroCampussOrHomePostNumPage(role, schoolId, gradeId, classId, dateStart, dateEnd,pageable);
        }
        if(role==UserRole.TEACHER.getRole()) {
            if(funId==TeacherCountType.PREPARATION_UPLOAD_NUM.getState()) {
                //班级课程上传、备课上传数
                return manageCountService.getClassOrPrepUploadNumPage(funId, schoolId, gradeId, classId, dateStart, dateEnd, pageable);
            }else if(funId==TeacherCountType.HOMEWORK_UPLOAD_NUM.getState()){
                //作业上传数*
                return manageCountService.getHomeworkUploadNumPage(schoolId, gradeId, classId, dateStart, dateEnd, pageable);
            }else if(funId==TeacherCountType.PAPERS_UPLOAD_NUM.getState()){
                //试卷上传数*
                return manageCountService.getPapersUploadNumPage(schoolId, gradeId, classId, dateStart, dateEnd, pageable);
            }else if(funId==TeacherCountType.NOTICE_NUM.getState()){
                //通知发布数*
                return manageCountService.getNoticeNumPage(schoolId, gradeId, classId, dateStart, dateEnd, pageable);
            }
        }else if(role==UserRole.STUDENT.getRole()){
            if(funId==StudentCountType.CLASSES_WATCH_NUM.getState()){
                //班级课程观看数*
                return manageCountService.getClassesWatchNumPage(schoolId, gradeId, classId, dateStart, dateEnd, pageable);
            }else if(funId==StudentCountType.JOB_COMPLETION_NUM.getState()){
                //作业完成数*
                return manageCountService.getJobCompletionNumPage(schoolId, gradeId, classId, dateStart, dateEnd, pageable);
            }else if(funId==StudentCountType.PAPERS_COMPLETION_NUM.getState()){
                //试卷完成数*
                return manageCountService.getPapersCompletionNumPage(schoolId, gradeId, classId, dateStart, dateEnd, pageable);
            }else if(funId==StudentCountType.CLOUD_CURRICULUM_VIEW_NUM.getState()){
                //云课程观看数*
                return manageCountService.getCloudCurriculumViewNumPage(schoolId, gradeId, classId, dateStart, dateEnd, pageable);
            }
        }else if(role==UserRole.PARENT.getRole()){

        }
        return null;
    }

    /**
     * 管理统计-功能使用统计明细页面
     * @param
     * @return
     */
    @RequestMapping("/functionUsePage")
    public String functionUsePage(Map<String, Object> model,
          @RequestParam("schoolId")String schoolId,
          @RequestParam("operateUserId") String operateUserId,
          @RequestParam("userName") String userName,
          @RequestParam("funId") Integer funId,
          @RequestParam("role") Integer role,
          @RequestParam("dateStart") String dateStart,
          @RequestParam("dateEnd") String dateEnd
    ){
        int userRole=getSessionValue().getUserRole();
        String url="";
        //SchoolDTO schoolDTO = manageCountService.searchSchoolInfo(schoolId);
        //model.put("schoolDTO", schoolDTO);
        model.put("schoolId", schoolId);
        model.put("operateUserId", operateUserId);
        model.put("funId", funId);
        model.put("userName", userName);
        String funName = "";
        if (role == UserRole.TEACHER.getRole()) {
            if (funId == TeacherCountType.MICRO_CAMPUS_POST_NUM.getState()) {
                //微校园发帖数 *
                funName = TeacherCountType.MICRO_CAMPUS_POST_NUM.getDes();
            } else if (funId == TeacherCountType.HOMEWORK_UPLOAD_NUM.getState()) {
                //作业上传数*
                funName = TeacherCountType.HOMEWORK_UPLOAD_NUM.getDes();
            } else if (funId == TeacherCountType.PAPERS_UPLOAD_NUM.getState()) {
                //试卷上传数*
                funName = TeacherCountType.PAPERS_UPLOAD_NUM.getDes();
            } else if (funId == TeacherCountType.PREPARATION_UPLOAD_NUM.getState()) {
                //备课上传数*
                funName = TeacherCountType.PREPARATION_UPLOAD_NUM.getDes();
            } else if (funId == TeacherCountType.NOTICE_NUM.getState()) {
                //通知发布数*
                funName = TeacherCountType.NOTICE_NUM.getDes();
            }
        } else if (role == UserRole.STUDENT.getRole()) {
            if (funId == StudentCountType.MICRO_HOME_POST_NUM.getState()) {
                //微家园发帖数*
                funName = StudentCountType.MICRO_HOME_POST_NUM.getDes();
            } else if (funId == StudentCountType.CLASSES_WATCH_NUM.getState()) {
                //班级课程观看数*
                funName = StudentCountType.CLASSES_WATCH_NUM.getDes();
            } else if (funId == StudentCountType.JOB_COMPLETION_NUM.getState()) {
                //作业完成数*
                funName = StudentCountType.JOB_COMPLETION_NUM.getDes();
            } else if (funId == StudentCountType.PAPERS_COMPLETION_NUM.getState()) {
                //试卷完成数*
                funName = StudentCountType.PAPERS_COMPLETION_NUM.getDes();
            } else if (funId == StudentCountType.CLOUD_CURRICULUM_VIEW_NUM.getState()) {
                //云课程观看数*
                funName = StudentCountType.CLOUD_CURRICULUM_VIEW_NUM.getDes();
            }
        } else if (role == UserRole.PARENT.getRole()) {
            //微家园发帖数*
            funName = ParentCountType.MICRO_HOME_POST_NUM.getDes();
        }

        model.put("funName", funName.substring(0, funName.length() - 1));
        model.put("role", role);
        model.put("dateStart", dateStart);
        model.put("dateEnd", dateEnd);
        if(UserRole.isEducation(userRole)||((UserRole.isHeadmaster(userRole)||UserRole.isManager(userRole)||UserRole.isTeacher(userRole))
                &&schoolId.equals(getSessionValue().getSchoolId()))){
            url="manageCount/functionUseDetailPage";//管理统计-功能使用明细
        }
        return url;
    }

    /**
     * 管理统计-查询功能使用统计明细
     * @param operateUserId
     * @param funId
     * @return Map
     */
    @RequestMapping("/functionUseDetailPage")
    public @ResponseBody Page<ManageCountDTO> functionUseDetailPage(
            @RequestParam("operateUserId") String operateUserId,
            @RequestParam("dateStart") String dateStart,
            @RequestParam("dateEnd") String dateEnd,
            @RequestParam("role")Integer role,
            @RequestParam("funId") Integer funId,
            Pageable pageable) throws ResultTooManyException {
        dateStart=handleTime(dateStart, 1);
        dateEnd=handleTime(dateEnd, 2);
        if(funId==TeacherCountType.MICRO_CAMPUS_POST_NUM.getState()
                ||funId==StudentCountType.MICRO_HOME_POST_NUM.getState()
                ||funId==ParentCountType.MICRO_HOME_POST_NUM.getState()){
            //微校园、微家园发帖数 *
            return manageCountService.getMicroCampusOrHomePostDetailPage(role, operateUserId, dateStart, dateEnd, pageable);
        }

        if(role==UserRole.TEACHER.getRole()) {
            if(funId==TeacherCountType.PREPARATION_UPLOAD_NUM.getState()) {
                //班级课程、备课上传数
                return manageCountService.getClassOrPrepUploadDetailPage(funId, operateUserId, dateStart, dateEnd, pageable);
            }else if(funId==TeacherCountType.HOMEWORK_UPLOAD_NUM.getState()){
                //作业上传数*
                return manageCountService.getHomeworkUploadDetailPage(operateUserId, dateStart, dateEnd, pageable);
            }else if(funId==TeacherCountType.PAPERS_UPLOAD_NUM.getState()){
                //试卷上传数*
                return manageCountService.getPapersUploadDetailPage(operateUserId, dateStart, dateEnd, pageable);
            }else if(funId==TeacherCountType.NOTICE_NUM.getState()){
                //通知发布数*
                return manageCountService.getNoticeDetailPage(operateUserId, dateStart, dateEnd, pageable);
            }
        }else if(role==UserRole.STUDENT.getRole()){
            if(funId==StudentCountType.CLASSES_WATCH_NUM.getState()){
                //班级课程观看数*
                return manageCountService.getClassesWatchDetailPage(operateUserId, dateStart, dateEnd, pageable);
            }else if(funId==StudentCountType.JOB_COMPLETION_NUM.getState()){
                //作业完成数*
                return manageCountService.getJobCompletionDetailPage(operateUserId, dateStart, dateEnd, pageable);
            }else if(funId==StudentCountType.PAPERS_COMPLETION_NUM.getState()){
                //试卷完成数*
                return manageCountService.getPapersCompletionDetailPage(operateUserId, dateStart, dateEnd, pageable);
            }else if(funId==StudentCountType.CLOUD_CURRICULUM_VIEW_NUM.getState()){
                //云课程观看数*
                return manageCountService.getCloudCurriculumViewDetailPage(operateUserId, dateStart, dateEnd, pageable);
            }
        }else if(role==UserRole.PARENT.getRole()){

        }
        return null;
    }

    /**
     * 给日期字符串拼接时分秒
     * @param dateTime 日期
     * @param type 1：表示开始时间，2：表示结束时间
     * @return
     */
    private String handleTime(String dateTime, int type){
        if(type==1) {
            //判断查询条件的开始时间是否为空
            dateTime = dateTime == null ? "" : dateTime;
            if (!"".equals(dateTime)) {//不等于空时，拼接" 00:00:00"
                dateTime = dateTime + " 00:00:00";
            } else {//等于空时，设置默认时间
                dateTime = "2014-09-01 00:00:00";
            }
        }
        if(type==2){
            //判断查询条件的结束时间是否为空
            dateTime=dateTime==null?"":dateTime;
            if(!"".equals(dateTime)){//不等于空时，拼接" 23:59:59"
                dateTime=dateTime+" 23:59:59";
            }else{//等于空时，设置默认时间
                dateTime=DateTimeUtils.getCurrDate()+" 23:59:59";
            }
        }
        return dateTime;
    }
}
