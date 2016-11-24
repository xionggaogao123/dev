package com.fulaan.overallquality.controller;

import com.alibaba.fastjson.JSON;
import com.fulaan.annotation.UserRoles;
import com.fulaan.base.controller.BaseController;
import com.fulaan.myclass.service.ClassService;
import com.fulaan.myschool.controller.GradeView;
import com.fulaan.overallquality.dto.*;
import com.fulaan.overallquality.service.OverallQualityItemService;
import com.fulaan.school.service.SchoolService;
import com.fulaan.user.service.UserService;
import com.fulaan.utils.QiniuFileUtils;
import com.pojo.overallquality.ChangeState;
import com.pojo.school.ClassEntry;
import com.pojo.school.ClassInfoDTO;
import com.pojo.user.UserDetailInfoDTO;
import com.pojo.user.UserRole;
import com.sys.constants.Constant;
import com.sys.exceptions.FileUploadException;
import com.sys.exceptions.IllegalParamException;
import com.sys.utils.DateTimeUtils;
import com.sys.utils.RespObj;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * 综合素质评比项目
 * Created by guojing on 2016/8/4.
 */
@Controller
@RequestMapping("/qualityitem")
public class OverallQualityItemController extends BaseController {

    private static final Logger logger = Logger.getLogger(OverallQualityItemController.class);

    @Autowired
    private OverallQualityItemService overallQualityItemService;

    @Autowired
    private SchoolService schoolService;

    @Autowired
    private ClassService classService;

    @Autowired
    private UserService userService;

    /**
     * 德育项目管理页面
     *
     * @param model
     * @return
     */
    @RequestMapping("/qualityItemMge")
    public String qualityItemManage(Map<String, Object> model) throws IllegalParamException {
        int userRole = getSessionValue().getUserRole();
        ObjectId schoolId = getSchoolId();
        //判断用户角色
        if (UserRole.isHeadmaster(userRole)) {
            //获取学校下的全部年级信息
            List<GradeView> gradelist = schoolService.searchSchoolGradeList(schoolId);
            //sortGradeList(gradelist);
            model.put("gradelist", gradelist);
            model.put("refDate", DateTimeUtils.getCurrDate());
            model.put("curDate", DateTimeUtils.getCurrDate());
            List<OverallQualityScoreDTO> oqsList = overallQualityItemService.getQualityScoreList(schoolId);
            model.put("oqsList", oqsList);
        }else if (UserRole.isTeacher(userRole)) {//判断用户角色是否是老师角色
            List<ClassInfoDTO> classInfoList = classService.getSimpleClassInfoDTOs(getUserId(), schoolId);
            Map<String, GradeView> gradeMap = new HashMap<String, GradeView>();
            for (ClassInfoDTO ciDto : classInfoList) {
                GradeView gradeView = new GradeView();
                gradeView.setId(ciDto.getGradeId());
                String gradeName = ciDto.getGradeName() == null ? "" : ciDto.getGradeName();
                gradeView.setName(gradeName);
                gradeMap.put(ciDto.getGradeId(), gradeView);
            }
            List<GradeView> gradeList = new ArrayList<GradeView>();
            for (GradeView grade : gradeMap.values()) {
                gradeList.add(grade);
            }
            //sortGradeList(gradeList);
            model.put("gradelist", gradeList);
        }else if (UserRole.isStudentOrParent(userRole)) {
            ObjectId userId = getUserId();
            ClassInfoDTO classInfo = classService.getClassInfoDTOByStuId(userId, getSchoolId());
            model.put("className", classInfo.getClassName());
            model.put("gradeName", classInfo.getGradeName());
        }
        return "overallquality/qualityitemmge";
    }

    /**
     * 查询项目列表
     *
     * @return
     */
    @RequestMapping("searchQualityItemList")
    public @ResponseBody Map<String, Object> searchQualityItemList() {
        Map<String, Object> model = new HashMap<String, Object>();
        ObjectId schoolId = getSchoolId();
        List<OverallQualityItemDTO> list = overallQualityItemService.searchQualityItemList(schoolId);
        model.put("list", list);
        return model;
    }

    /**
     * 添加项目
     *
     * @param id
     * @param itemName
     * @param scoreId
     * @return
     */
    @UserRoles(noValue={UserRole.STUDENT,UserRole.PARENT})
    @RequestMapping("addOrEditQualityItem")
    public @ResponseBody Map<String, Object> addOrEditQualityItem(
            @RequestParam(value = "id", defaultValue = "") String id,
            @RequestParam("itemName") String itemName,
            @RequestParam("scoreId") String scoreId
    ) {
        Map<String, Object> model = new HashMap<String, Object>();
        ObjectId schoolId = getSchoolId();
        int count = overallQualityItemService.getQualityItemCount(id, schoolId, itemName);
        if (count > 0) {
            model.put("result", false);
        } else {
            //添加项目
            ObjectId itemId = overallQualityItemService.addOrEditQualityItem(id, schoolId, itemName, scoreId);
            model.put("result", true);
        }
        return model;
    }

    /**
     * 查询单条项目
     *
     * @param id
     * @return
     */
    @RequestMapping("getQualityItem")
    public @ResponseBody Map<String, Object> getQualityItem(
            @RequestParam("id") String id
    ) {
        Map<String, Object> model = new HashMap<String, Object>();
        OverallQualityItemDTO dto = overallQualityItemService.getQualityItem(new ObjectId(id));
        model.put("dto", dto);
        return model;
    }

    /**
     * 删除项目
     *
     * @param id
     * @return
     */
    @UserRoles(noValue={UserRole.STUDENT,UserRole.PARENT})
    @RequestMapping("delQualityItem")
    public @ResponseBody Map<String, Object> delQualityItem(
            @RequestParam("id") String id
    ) {
        Map<String, Object> model = new HashMap<String, Object>();
        overallQualityItemService.delQualityItem(new ObjectId(id));
        model.put("result", true);
        return model;
    }

    /**
     * 查询项目列表
     *
     * @return
     */
    @RequestMapping("searchClassWeeklyQuality")
    public @ResponseBody Map<String, Object> searchClassWeeklyQuality(
            @RequestParam("gradeId") String gradeId,
            @RequestParam("classId") String classId,
            @RequestParam("operate") String operate,
            @RequestParam("refDate") String refDate,
            @RequestParam(value = "suspend", defaultValue = "false") boolean suspend
    ) {
        Map<String, Object> model = new HashMap<String, Object>();
        if(!"".equals(classId)) {
            overallQualityItemService.searchClassWeeklyQuality(getSchoolId(), gradeId, classId, operate, refDate, suspend, model);
        }else{
            model.put("classId","");
        }
        return model;
    }

    /**
     * 编辑项目列表
     *
     * @return
     */
    @UserRoles(noValue={UserRole.STUDENT,UserRole.PARENT})
    @RequestMapping("editClassWeeklyQuality")
    public @ResponseBody Map<String, Object> editClassWeeklyQuality(
            @RequestParam(value = "id", defaultValue = "") String id,
            @RequestParam("itemId") String itemId,
            @RequestParam("gradeId") String gradeId,
            @RequestParam("classId") String classId,
            @RequestParam("flagDate") String flagDate,
            @RequestParam("flag") String flag
    ) {
        Map<String, Object> model = new HashMap<String, Object>();
        if(!"".equals(classId)) {
            overallQualityItemService.editClassWeeklyQuality(id, getSchoolId(), itemId, gradeId, classId, flagDate, flag);
            model.put("result", true);
        }else{
            model.put("result", false);
        }
        return model;
    }

    /**
     * 形象银行页面
     *
     * @param model
     * @return
     */
    @RequestMapping("/imageBank")
    public String imageBank(Map<String, Object> model) throws IllegalParamException {
        ObjectId schoolId = getSchoolId();
        int userRole = getSessionValue().getUserRole();
        //判断用户角色
        if (UserRole.isHeadmaster(userRole)) {
            //获取学校下的全部年级信息
            List<GradeView> gradelist = schoolService.searchSchoolGradeList(schoolId);
            //sortGradeList(gradelist);
            model.put("gradelist", gradelist);
        }else if (UserRole.isTeacher(userRole)) {//判断用户角色是否是老师角色
            List<ClassInfoDTO> classInfoList = classService.getSimpleClassInfoDTOs(getUserId(), schoolId);
            Map<String, GradeView> gradeMap = new HashMap<String, GradeView>();
            for (ClassInfoDTO ciDto : classInfoList) {
                GradeView gradeView = new GradeView();
                gradeView.setId(ciDto.getGradeId());
                String gradeName = ciDto.getGradeName() == null ? "" : ciDto.getGradeName();
                gradeView.setName(gradeName);
                gradeMap.put(ciDto.getGradeId(), gradeView);
            }
            List<GradeView> gradeList = new ArrayList<GradeView>();
            for (GradeView grade : gradeMap.values()) {
                gradeList.add(grade);
            }
            //sortGradeList(gradeList);
            model.put("gradelist", gradeList);
        }
        StuOverallQualityScoreDTO dto;
        if (UserRole.isStudent(userRole)) {
            dto=overallQualityItemService.getStuOverallQualityScore(getUserId());
            ClassInfoDTO classInfo = classService.getClassInfoDTOByStuId(getUserId(), getSchoolId());
            model.put("className", classInfo.getClassName());
            model.put("gradeName", classInfo.getGradeName());
        }else{
            dto =new StuOverallQualityScoreDTO();
        }
        model.put("dto", dto);
        model.put("refDate", DateTimeUtils.getCurrDate());
        model.put("curDate", DateTimeUtils.getCurrDate());
        return "overallquality/imagebank";
    }

    /**
     * 对班级list进行排序
     *
     * @param list
     */
    private void sortGradeList(List<GradeView> list) {
        Collections.sort(list, new Comparator<GradeView>() {
            public int compare(GradeView obj1, GradeView obj2) {
                int flag = obj1.getName().compareTo(obj2.getName());
                return flag;
            }
        });
    }

    /**
     * 获取年级的班级
     *
     * @param gradeId
     * @return Map
     */
    @RequestMapping("/getGradeClassValue")
    public @ResponseBody Map getGradeClassValue(@RequestParam("gradeId") String gradeId) throws IllegalParamException {
        //获取用户角色
        int userRole = getSessionValue().getUserRole();
        List<ClassInfoDTO> classInfoList = new ArrayList<ClassInfoDTO>();
        Map map = new HashMap();
        //判断用户角色
        if (UserRole.isHeadmaster(userRole)) {
            //通过年级id查询出班级信息
            List<ClassInfoDTO> list = classService.getGradeClassesInfo(gradeId);
            sortClassList(list);
            map.put("classList", list);
        }else if (UserRole.isTeacher(userRole)) { //判断用户角色是否是老师角色
            //获取年级的班级
            String schoolId = getSessionValue().getSchoolId();

            List<ClassInfoDTO> list = classService.getSimpleClassInfoDTOs(getUserId(), new ObjectId(schoolId));
            for (ClassInfoDTO dto : list) {
                String className = dto.getClassName() == null ? "" : dto.getClassName();
                dto.setClassName(className);
                if (gradeId.equals(dto.getGradeId())) {
                    classInfoList.add(dto);
                }
            }
            sortClassList(classInfoList);
            map.put("classList", classInfoList);
        }
        return map;
    }

    /**
     * 对班级list进行排序
     *
     * @param list
     */
    private void sortClassList(List<ClassInfoDTO> list) {
        Collections.sort(list, new Comparator<ClassInfoDTO>() {
            public int compare(ClassInfoDTO obj1, ClassInfoDTO obj2) {
                int flag = obj1.getClassName().compareTo(obj2.getClassName());
                return flag;
            }
        });
    }

    /**
     * 获取年级的班级
     *
     * @param classId
     * @return Map
     */
    @RequestMapping("/getClassStudentValue")
    public @ResponseBody Map getClassStudentValue(@RequestParam("classId") String classId){
        Map map = new HashMap();
        if(!"".equals(classId)) {
            ClassEntry classEntry = classService.getClassEntryById(new ObjectId(classId), Constant.FIELDS);
            //取得班级下的全部学生id
            List<ObjectId> stuIds = classEntry.getStudents();
            List<UserDetailInfoDTO> userInfos = userService.findUserInfoByIds(stuIds);
            map.put("userInfos", userInfos);
        }else{
            map.put("classId","");
        }
        return map;
    }

    /**
     * 查询学生项目列表
     *
     * @return
     */
    @RequestMapping("searchStuWeeklyQuality")
    public @ResponseBody Map<String, Object> searchStuWeeklyQuality(
            @RequestParam(value = "stuId", defaultValue = "") String stuId,
            @RequestParam(value ="operate", defaultValue = "curr") String operate,
            @RequestParam("refDate") String refDate,
            @RequestParam(value = "suspend", defaultValue = "false") boolean suspend
    ) {
        Map<String, Object> model = new HashMap<String, Object>();
        ObjectId userId=null;
        int userRole = getSessionValue().getUserRole();
        if(UserRole.isNotStudentAndParent(userRole)){
            if(!"".equals(stuId)){
                userId=new ObjectId(stuId);
            }else{
                return model;
            }
        }else if (UserRole.isStudent(userRole)){
            userId=getUserId();
        }
        overallQualityItemService.searchStuWeeklyQuality(userId, operate, refDate, suspend, model);
        return model;
    }


    /**
     * 编辑学生列表
     *
     * @return
     */
    @UserRoles(noValue={UserRole.STUDENT,UserRole.PARENT})
    @RequestMapping("editStuWeeklyQuality")
    public @ResponseBody Map<String, Object> editStuWeeklyQuality(
            @RequestParam(value = "id", defaultValue = "") String id,
            @RequestParam("currencyType") int currencyType,
            @RequestParam("gradeId") String gradeId,
            @RequestParam("classId") String classId,
            @RequestParam("stuId") String stuId,
            @RequestParam("flagDate") String flagDate,
            @RequestParam("flag") String flag
    ) {
        Map<String, Object> model = new HashMap<String, Object>();
        if(!"".equals(classId)) {
            overallQualityItemService.editStuWeeklyQuality(id, getSchoolId(), stuId, currencyType, gradeId, classId, flagDate, flag);
            model.put("result", true);
        }else{
            model.put("result", false);
        }
        return model;
    }


    /**
     * 形象银行页面
     *
     * @param model
     * @return
     */
    @RequestMapping("/imageMarket")
    public String imageMarket(Map<String, Object> model) throws IllegalParamException {
        int userRole = getSessionValue().getUserRole();
        if (UserRole.isStudent(userRole)) {
            StuOverallQualityScoreDTO dto=overallQualityItemService.getStuOverallQualityScore(getUserId());
            model.put("dto", dto);
            ClassInfoDTO classInfo = classService.getClassInfoDTOByStuId(getUserId(), getSchoolId());
            model.put("className", classInfo.getClassName());
            model.put("gradeName", classInfo.getGradeName());
        }
        //判断用户角色
        if (UserRole.isHeadmaster(userRole)) {
            //获取学校下的全部年级信息
            List<GradeView> gradelist = schoolService.searchSchoolGradeList(getSchoolId());
            //sortGradeList(gradelist);
            model.put("gradelist", gradelist);
        }else if (UserRole.isTeacher(userRole)) {//判断用户角色是否是老师角色
            List<ClassInfoDTO> classInfoList = classService.getSimpleClassInfoDTOs(getUserId(), getSchoolId());
            Map<String, GradeView> gradeMap = new HashMap<String, GradeView>();
            for (ClassInfoDTO ciDto : classInfoList) {
                GradeView gradeView = new GradeView();
                gradeView.setId(ciDto.getGradeId());
                String gradeName = ciDto.getGradeName() == null ? "" : ciDto.getGradeName();
                gradeView.setName(gradeName);
                gradeMap.put(ciDto.getGradeId(), gradeView);
            }
            List<GradeView> gradeList = new ArrayList<GradeView>();
            for (GradeView grade : gradeMap.values()) {
                gradeList.add(grade);
            }
            //sortGradeList(gradeList);
            model.put("gradelist", gradeList);
        }

        return "overallquality/imagemarket";
    }

    //得到文件名
    private String getFileName(MultipartFile file)
    {
        return new ObjectId().toString()+Constant.POINT+ file.getOriginalFilename();
    }

    /**
     * 上传图片
     * @param request
     * @return
     */
    @UserRoles(noValue={UserRole.STUDENT,UserRole.PARENT})
    @RequestMapping("/addPic")
    public void addPic(HttpServletRequest request, HttpServletResponse response) {
        Map result = new HashMap<String,Object>();
        String fileUrl="";
        String fileName="";
        try {
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
            for (MultipartFile file : fileMap.values()) {
                fileName =getFileName(file);
                RespObj upladTestPaper= QiniuFileUtils.uploadFile(fileName, file.getInputStream(), QiniuFileUtils.TYPE_IMAGE);
                if(upladTestPaper.getCode()!= Constant.SUCCESS_CODE)
                {
                    throw new FileUploadException();
                }
                fileUrl = QiniuFileUtils.getPath(QiniuFileUtils.TYPE_IMAGE,fileName);
            }
            result.put("qnKey", fileName);
            result.put("code", "200");
            result.put("picPath", fileUrl);
        }  catch (Exception e) {
            e.printStackTrace();
            result.put("code", "500");
            result.put("data", "上传图片失败");
        }finally {
            PrintWriter pw;
            try {
                pw = response.getWriter();
                pw.write(JSON.toJSONString(result));
                pw.flush();
                pw.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    /**
     * 添加商品
     *
     * @return
     */
    @UserRoles(noValue={UserRole.STUDENT,UserRole.PARENT})
    @RequestMapping("addOrEditGoods")
    public @ResponseBody Map<String, Object> addOrEditGoods(
            @RequestParam(value = "id", defaultValue = "") String id,
            @RequestParam("goodsName") String goodsName,
            @RequestParam("picPath") String picPath,
            @RequestParam("qnKey") String qnKey,
            @RequestParam("goodsPrice") int goodsPrice,
            @RequestParam("goodsStock") int goodsStock
    ) {
        Map<String, Object> model = new HashMap<String, Object>();
        overallQualityItemService.addOrEditGoods(id, getSchoolId(), goodsName, picPath, qnKey, goodsPrice, goodsStock, getUserId());
        model.put("result", true);
        return model;
    }


    /**
     * 查询项目列表
     *
     * @return
     */
    @RequestMapping("searchOverallQualityGoodsList")
    public @ResponseBody Map<String, Object> searchOverallQualityGoodsList() {
        Map<String, Object> model = new HashMap<String, Object>();
        ObjectId schoolId = getSchoolId();
        int userRole=getSessionValue().getUserRole();
        List<OverallQualityGoodsDTO> list = overallQualityItemService.searchOverallQualityGoodsList(schoolId, getUserId(), userRole);
        model.put("list", list);
        model.put("isStudent", UserRole.isStudentOrParent(userRole));
        return model;
    }

    /**
     * 编辑项目
     *
     * @param id
     * @return
     */
    @UserRoles(noValue={UserRole.STUDENT,UserRole.PARENT})
    @RequestMapping("getOverallQualityGoods")
    public @ResponseBody Map<String, Object> getOverallQualityGoods(
            @RequestParam("id") String id
    ) {
        Map<String, Object> model = new HashMap<String, Object>();
        OverallQualityGoodsDTO dto = overallQualityItemService.getOverallQualityGoods(new ObjectId(id));
        model.put("dto", dto);
        return model;
    }

    /**
     * 删除项目
     *
     * @param id
     * @return
     */
    @UserRoles(noValue={UserRole.STUDENT,UserRole.PARENT})
    @RequestMapping("delOverallQualityGoods")
    public @ResponseBody Map<String, Object> delOverallQualityGoods(
            @RequestParam("id") String id
    ) {
        Map<String, Object> model = new HashMap<String, Object>();
        overallQualityItemService.delOverallQualityGoods(new ObjectId(id));
        model.put("result", true);
        return model;
    }

    /**
     * 学生兑换商品
     *
     * @param id
     * @return
     */
    @UserRoles(value={UserRole.STUDENT})
    @RequestMapping("studentChangeGoods")
    public @ResponseBody Map<String, Object> studentChangeGoods(
            @RequestParam("id") String id
    ) {
        Map<String, Object> model = new HashMap<String, Object>();
        overallQualityItemService.studentChangeGoods(new ObjectId(id), getUserId(),model);
        return model;
    }


    /**
     * 查询学生兑换商品信息
     * @param gradeId
     * @param classId
     * @return
     */
    @UserRoles(noValue={UserRole.STUDENT,UserRole.PARENT})
    @RequestMapping("searchStuChangeGoodsList")
    public @ResponseBody Map<String, Object> searchStuChangeGoodsList(
            @RequestParam("gradeId") String gradeId,
            @RequestParam("classId") String classId
    ) {
        Map<String, Object> model = new HashMap<String, Object>();
        if(!"".equals(classId)) {
            List<StuChangeGoodsDTO> list = overallQualityItemService.searchStuChangeGoodsList(new ObjectId(gradeId), new ObjectId(classId), ChangeState.AUDIT);
            model.put("list", list);
        }else{
            model.put("classId","");
        }
        return model;
    }

    /**
     * 批量处理学生申请
     *@param ids
     * @return
     */
    @UserRoles(noValue={UserRole.STUDENT,UserRole.PARENT})
    @RequestMapping("batchHandleChangeGoods")
    public @ResponseBody Map<String,Object> batchHandleChangeGoods(
            @RequestParam("ids")String ids,
            @RequestParam(value = "refuseCon", defaultValue = "")String refuseCon,
            @RequestParam(value = "operate", defaultValue = "")String operate
            ) throws Exception{
        Map<String,Object>  model = new HashMap<String,Object>();
        ObjectId userId = getUserId();
        String[] arrIds=ids.split(",");
        List<String> cids= Arrays.asList(arrIds);
        //删除校园安全信息（逻辑删除）
        if(!"".equals(operate)){
            overallQualityItemService.batchHandleStuChangeGoods(cids, userId, refuseCon, operate, model);
        }
        return model;
    }

    /**
     * 查询学生兑换商品信息
     * @return
     */
    @RequestMapping("changeGoodsHistory")
    public @ResponseBody Map<String, Object> changeGoodsHistory(){
        Map<String, Object> model = new HashMap<String, Object>();
        int userRole=getSessionValue().getUserRole();
        List<StuChangeGoodsDTO> list=overallQualityItemService.changeGoodsHistory(getSchoolId(), getUserId(), userRole);
        model.put("list", list);
        return model;
    }

    /**
     * 文明部落
     *@param classId
     * @return
     */
    @RequestMapping("searchClassBuLuoInfo")
    public @ResponseBody Map<String,Object> searchClassBuLuoInfo(
        @RequestParam(value = "gradeId", defaultValue = "")String gradeId,
        @RequestParam(value = "classId", defaultValue = "")String classId
    ) throws Exception{
        Map<String,Object>  model = new HashMap<String,Object>();
        int userRole = getSessionValue().getUserRole();
        if (UserRole.isStudentOrParent(userRole)) {
            ObjectId userId = getUserId();
            ClassInfoDTO classInfo = classService.getClassInfoDTOByStuId(userId, getSchoolId());
            gradeId = classInfo.getGradeId();
            classId = classInfo.getId();
        }
        if(!"".equals(classId)){
            overallQualityItemService.searchClassBuLuoInfo(getSchoolId(), gradeId, classId, model);
        }else{
            model.put("classId","");
        }
        return model;
    }

    /**
     * 文明部落-建设
     *@param classId
     * @return
     */
    @RequestMapping("buLuoBuild")
    public String buLuoBuild(
            @RequestParam(value = "gradeId", defaultValue = "")String gradeId,
            @RequestParam(value = "classId", defaultValue = "")String classId,
            Map<String, Object> model
    ) throws Exception{
        int userRole=getSessionValue().getUserRole();
        boolean isTea=true;
        if(UserRole.isStudentOrParent(userRole)) {
            isTea=false;
        }
        if(!"".equals(classId)) {
            ClassInfoDTO classInfo = classService.findClassInfoByClassId(classId);
            model.put("className", classInfo.getClassName());
            model.put("gradeName", classInfo.getGradeName());
            overallQualityItemService.searchClassBuLuoInfoByParams(getSchoolId(), gradeId, classId, model);
        }else{
            model.put("classId","");
        }
        model.put("isTea", isTea);
        return "overallquality/buluobuild";
    }

    /**
     * 文明部落物品兑换
     *@param classId
     * @return
     */
    @UserRoles(noValue={UserRole.STUDENT,UserRole.PARENT})
    @RequestMapping("propChange")
    public @ResponseBody Map<String,Object> propChange(
            @RequestParam(value = "gradeId", defaultValue = "")String gradeId,
            @RequestParam(value = "classId", defaultValue = "")String classId,
            @RequestParam(value = "type", defaultValue = "")String type
    ) throws Exception{
        Map<String,Object>  model = new HashMap<String,Object>();
        if(!"".equals(classId)) {
            overallQualityItemService.propChange(gradeId, classId, type, model);
        }else{
            model.put("classId","");
        }
        return model;
    }
}
