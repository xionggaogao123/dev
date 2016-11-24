package com.fulaan.repair.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.fulaan.annotation.ObjectIdType;
import com.fulaan.annotation.SessionNeedless;
import com.fulaan.base.controller.BaseController;
import com.fulaan.department.service.DepartmentService;
import com.fulaan.educationbureau.service.EducationBureauService;
import com.fulaan.examresult.service.ExamResultService;
import com.fulaan.repair.service.RepairService;
import com.fulaan.school.service.SchoolService;
import com.fulaan.user.service.UserService;
import com.mongodb.BasicDBObject;
import com.pojo.educationbureau.EducationBureauEntry;
import com.pojo.repair.RepairEntry;
import com.pojo.school.DepartmentEntry;
import com.pojo.school.SchoolEntry;
import com.pojo.user.UserEntry;
import com.pojo.user.UserRole;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;

/**
 * 报修controller,处理报修功能请求
 *
 * @author cxy
 */
@Controller
@RequestMapping("/repair")
public class RepairController extends BaseController {

    @Autowired
    private RepairService repairService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private UserService userService;

    @Autowired
    private ExamResultService examResultService;

    @Autowired
    private SchoolService schoolService;

    @Autowired
    private EducationBureauService educationBureauService;

    /**
     * 报修页面跳转
     */

    @RequestMapping("/list")
    @ResponseBody
    public ModelAndView retrieveRepairInfo() {
        String schoolId = getSessionValue().getSchoolId();
        String userName = getSessionValue().getUserName();
        String userId = getSessionValue().getId();
        //获取当前学年的学期
        List<String> list = examResultService.getUsableTerm();
        Map<String, Object> messageMap = new HashMap<String, Object>();
        //组装当前用户Id和name
        Map<String, String> userMap = new HashMap<String, String>();
        userMap.put("userName", userName);
        userMap.put("userId", userId);
        messageMap.put("userInfo", userMap);
        //messageMap.put("list", list);
        // 获取所有部门并重新组装
        List<DepartmentEntry> depsSrc = departmentService.getDepartmentEntrys(new ObjectId(
                schoolId));
        List<Map<String, String>> deps = new ArrayList<Map<String, String>>();
        for (int i = 0; i < depsSrc.size(); i++) {
            DepartmentEntry de = depsSrc.get(i);
            Map<String, String> m = new HashMap<String, String>();
            m.put("id", de.getID().toString());
            m.put("name", de.getName());
            deps.add(m);
        }
        messageMap.put("departments", deps);
        messageMap.put("schoolId", schoolId);
        ModelAndView mv = new ModelAndView();
        mv.addObject("message", JSON.toJSONString(messageMap));
        mv.addObject("list", list);
        mv.setViewName("repair/baoxiuguanli");
        return mv;
    }

    /**
     * 获取部门列表
     * armstrong
     * @return
     */
    @RequestMapping("/getDepartmentList")
    @ResponseBody
    public List<Map<String, String>> getDepartmentList() {
        String schoolId = getSessionValue().getSchoolId();
        List<DepartmentEntry> departmentEntrys = departmentService.getDepartmentEntrys(new ObjectId(
                schoolId));
        List<Map<String, String>> deps = new ArrayList<Map<String, String>>();
        for (int i = 0; i < departmentEntrys.size(); i++) {
            DepartmentEntry de = departmentEntrys.get(i);
            Map<String, String> m = new HashMap<String, String>();
            m.put("id", de.getID().toString());
            m.put("name", de.getName());
            deps.add(m);
        }
        return deps;
    }

    /**
     * 查看报修记录页面查询报修记录
     *
     * @return
     */

    @RequestMapping("/queryRepairsForView")
    @ResponseBody
    public String queryRepairs(@RequestParam(value = "termType", defaultValue = "All") String termType,
                               @RequestParam(value = "repairProcedure", defaultValue = "ALL") String repairProcedure,
                               @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
                               @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<RepairEntry> resultList = repairService.queryRepairsBydeclaredAndFields(getUserId(), termType, repairProcedure);
        List<RepairEntry> pagedList = getListByPage(resultList, pageNo, pageSize);
        List<Map<String, Object>> formatList = formatResultList(pagedList);
        resultMap.put("repairData", formatList);
        Map<String, String> pageMap = new HashMap<String, String>();
        pageMap.put("total", resultList.size() + "");
        pageMap.put("pageNo", pageNo + "");
        resultMap.put("pagejson", pageMap);
        return JSON.toJSONString(resultMap);
    }

    /**
     * 管理报修记录页面查询报修记录
     *
     * @return
     */

    @RequestMapping("/queryRepairsForManage")
    @ResponseBody
    public String queryRepairsForManage(@RequestParam(value = "termType", defaultValue = "ALL") String termType,
                                        @RequestParam(value = "repairDepartmentId", defaultValue = "ALL") String repairDepartmentId,
                                        @RequestParam(value = "repairProcedure", defaultValue = "ALL") String repairProcedure,
                                        @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
                                        @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        String schoolId = getSessionValue().getSchoolId();
        int role = getSessionValue().getUserRole();
        boolean isAdmin = UserRole.isManager(role) || UserRole.isHeadmaster(role) || UserRole.isK6ktHelper(role);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        //TODO
        List<RepairEntry> resultList = new ArrayList<RepairEntry>();
        if ("ALL".equals(repairProcedure)) {
            resultList = repairService.queryRepairsForManagePage("ALL".equals(repairDepartmentId) ? null : new ObjectId(repairDepartmentId),
                    termType, repairProcedure, isAdmin ? null : new ObjectId(getSessionValue().getId()), new ObjectId(schoolId), null, null);
        } else if ("待处理".equals(repairProcedure)) {
            //上报之后未分配维修人员的
            List<RepairEntry> ylist = new ArrayList<RepairEntry>();
            ylist = repairService.queryRepairsForManagePage("ALL".equals(repairDepartmentId) ? null : new ObjectId(repairDepartmentId),
                    termType, "已受理", isAdmin ? null : new ObjectId(getSessionValue().getId()), new ObjectId(schoolId), 1, 0);
            //纯粹的状态就是待受理的
            resultList = repairService.queryRepairsForManagePage("ALL".equals(repairDepartmentId) ? null : new ObjectId(repairDepartmentId),
                    termType, repairProcedure, isAdmin ? null : new ObjectId(getSessionValue().getId()), new ObjectId(schoolId), null, null);
            for (RepairEntry re : ylist) {
                resultList.add(re);
            }
        } else if ("已受理".equals(repairProcedure)) {
            //未上报到教育局的，状态是已受理的
            resultList = repairService.queryRepairsForManagePage("ALL".equals(repairDepartmentId) ? null : new ObjectId(repairDepartmentId),
                    termType, repairProcedure, isAdmin ? null : new ObjectId(getSessionValue().getId()), new ObjectId(schoolId), 0, null);
            //已经上报到教育局的，且已经分配给维修人员的
            List<RepairEntry> ylist = new ArrayList<RepairEntry>();
            ylist = repairService.queryRepairsForManagePage("ALL".equals(repairDepartmentId) ? null : new ObjectId(repairDepartmentId),
                    termType, repairProcedure, isAdmin ? null : new ObjectId(getSessionValue().getId()), new ObjectId(schoolId), 1, 1);
            for (RepairEntry re : ylist) {
                resultList.add(re);
            }
        } else if ("已完毕".equals(repairProcedure)) {
            resultList = repairService.queryRepairsForManagePage("ALL".equals(repairDepartmentId) ? null : new ObjectId(repairDepartmentId),
                    termType, repairProcedure, isAdmin ? null : new ObjectId(getSessionValue().getId()), new ObjectId(schoolId), null, null);
        }
        List<RepairEntry> pagedList = getListByPage(resultList, pageNo, pageSize);
        List<Map<String, Object>> formatList = formatResultList(pagedList);
        Map<String, String> pageMap = new HashMap<String, String>();
        pageMap.put("total", resultList.size() + "");
        pageMap.put("pageNo", pageNo + "");
        resultMap.put("pagejson", pageMap);
        resultMap.put("repairData", formatList);
        return JSON.toJSONString(resultMap);
    }

    private List<RepairEntry> getListByPage(List<RepairEntry> src, int pageNo, int pageSize) {
        List<RepairEntry> list = new ArrayList<RepairEntry>();
        int startIndex = (pageNo - 1) * pageSize;
        int endIndex = (pageNo * pageSize) - 1;
        if (src.size() < pageNo * pageSize) {
            endIndex = src.size() - 1;
        }
        for (int i = startIndex; i < endIndex + 1; i++) {
            list.add(src.get(i));
        }
        return list;
    }

    /**
     * 新增报修记录
     *
     * @return
     */
    @RequestMapping("/addRepair")
    @ResponseBody
    public String addRepair(@RequestParam(value = "userId", defaultValue = "") String userId,
                            @RequestParam(value = "userName", defaultValue = "") String userName,
                            @RequestParam(value = "repairDepartmentId", defaultValue = "") String repairDepartmentId,
                            @ObjectIdType(isRequire=false,field="depUser") ObjectId depUser,
                            @RequestParam(value = "repairPlace", defaultValue = "") String repairPlace,
                            @RequestParam(value = "phone", defaultValue = "") String phone,
                            @RequestParam(value = "repairType", defaultValue = "") String repairType,
                            @RequestParam(value = "repairContent", defaultValue = "") String repairContent,
                            @RequestParam(value = "imgPath", defaultValue = "") String imgPath
                            
    		) {
    	
        String schoolYear = examResultService.getTerm(System.currentTimeMillis());
        String schoolId = getSessionValue().getSchoolId();
        String schoolName = schoolService.getSchoolEntry(new ObjectId(schoolId), Constant.FIELDS).getName();
        
        
        
        List<EducationBureauEntry> schList = educationBureauService.selEducationBySchoolId(schoolId);
        ObjectId educationId = null;
        if (schList.size() > 0) {
            educationId = schList.get(0).getID();
        } else {
            //return JSON.toJSONString(RespObj.FAILD);
        }

        if ("".equals(userId) || "".equals(repairDepartmentId) || "".equals(repairType)) {
            return JSON.toJSONString(RespObj.FAILD);
        }
        
        RepairEntry re = new RepairEntry(schoolYear, new ObjectId(userId), userName, new Date().getTime(),
                repairPlace, phone, repairType, new ObjectId(repairDepartmentId),
                departmentService.getDepartmentEntryById(new ObjectId(repairDepartmentId)).getName(),
                repairContent, "待处理", new ObjectId(schoolId), schoolName, educationId, "", "", 0, 0,imgPath);
        
        UserEntry ue =userService.searchUserId(depUser);
        if(null!=ue)
        {
        	re.setSolveRepairPersonId(ue.getID());
        	re.setSolveRepairPersonName(ue.getUserName());
        }
        ObjectId resultId = repairService.addRepairEntry(re);
        if (resultId == null) {
            return JSON.toJSONString(RespObj.FAILD);
        }
        return JSON.toJSONString(RespObj.SUCCESS);
    }

    /**
     * 更新一条报修记录
     *
     * @return
     */

    @RequestMapping("/updateRepair")
    @ResponseBody
    public RespObj updateRepair(@RequestParam(value = "id", defaultValue = "") String id,
                                @RequestParam(value = "repairDepartmentId", defaultValue = "") String repairDepartmentId,
                                @RequestParam(value = "repairPlace", defaultValue = "") String repairPlace,
                                @RequestParam(value = "phone", defaultValue = "") String phone,
                                @RequestParam(value = "repairType", defaultValue = "") String repairType,
                                @RequestParam(value = "repairContent", defaultValue = "") String repairContent,
                                @RequestParam(value = "imgPath", defaultValue = "") String imgPath
                                
    		) {
        if ("".equals(id) || "".equals(repairDepartmentId) || "".equals(repairType)) {
            return RespObj.FAILD;
        }
        
       
        RepairEntry re = repairService.getRepairEntry(new ObjectId(id));
        if ("已受理".equals(re.getRepairProcedure())) {
            return new RespObj("300");
        }
        repairService.updateRepair(new ObjectId(id), new ObjectId(repairDepartmentId),
                departmentService.getDepartmentEntryById(new ObjectId(repairDepartmentId)).getName(),
                repairPlace, repairType, repairContent, phone,imgPath);
        return RespObj.SUCCESS;
    }

    /**
     * 更新一条报修记录的评价
     *
     * @return
     */

    @RequestMapping("/updateEvaluation")
    @ResponseBody
    public String updateEvaluation(@RequestParam(value = "starNum", defaultValue = "All") String starNum,
                                   @RequestParam(value = "id", defaultValue = "All") String id) {
        int evaluation = Integer.parseInt(starNum);
        repairService.updateRepariEvaluation(new ObjectId(id), evaluation);
        return JSON.toJSONString(RespObj.SUCCESS);
    }

    /**
     * 删除一条报修记录
     *
     * @return
     */

    @RequestMapping("/deleteRepair")
    @ResponseBody
    public RespObj deleteRepair(@RequestParam(value = "id", defaultValue = "") String id) {
        if ("".equals(id)) {
            return RespObj.FAILD;
        }
        RepairEntry re = repairService.getRepairEntry(new ObjectId(id));
        if ("已受理".equals(re.getRepairProcedure())) {
            return new RespObj("300");
        }
        repairService.deleteRepair(new ObjectId(id));
        return RespObj.SUCCESS;
    }

    /**
     * 格式化查询到的Repair结果集，供前台使用
     *
     * @param srcList
     * @return
     */
    private List<Map<String, Object>> formatResultList(List<RepairEntry> srcList) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<Map<String, Object>> formatList = new ArrayList<Map<String, Object>>();
        
        
        
        List<ObjectId> uids=MongoUtils.getFieldObjectIDs(srcList, "dui");
        
        
        Map<ObjectId, UserEntry> map=  userService.getUserEntryMap(uids, new BasicDBObject("nm",1));
        
        
        
        UserEntry ue=null;
        
        for (RepairEntry r : srcList) {
            Map<String, Object> m = new HashMap<String, Object>();
            m.put("id", r.getID().toString());
            m.put("repairTermType", r.getRepairTermType());
            m.put("declareRepairPersonId", r.getDeclareRepairPersonId().toString());
            m.put("declareRepairPersonName", r.getDeclareRepairPersonName());
            m.put("repairDate", sdf.format((new Date(r.getRepairTime()))));
            m.put("repairPlace", r.getRepairPlace());
            m.put("phone", r.getPhone());
            m.put("repairType", r.getRepairType());
            m.put("repairDepartmentId", r.getRepairDepartmentId().toString());
            m.put("repairDepartmentName", r.getRepairDepartmentName());
            m.put("repairContent", r.getRepairContent());
            m.put("repairProcedure", r.getRepairProcedure());
            m.put("repairResult", r.getRepairResult());
            m.put("solveRepairPersonId", r.getSolveRepairPersonId());
            m.put("solveRepairPersonName", r.getSolveRepairPersonName());
            m.put("repariEvaluation", r.getRepariEvaluation());
            m.put("manager", r.getManager());
            m.put("isReport", r.getIsReport());
            m.put("educationId", r.getEducationId());
            m.put("reportReason", r.getReportReason());
            m.put("schoolId", r.getSchoolId());
            m.put("schoolName", r.getSchoolName());
            m.put("isResolve", r.getIsResolve());
            m.put("imagePath", r.getImagePath());
            
            
            
            
            
            
            
            
            
            
            
            
            
            formatList.add(m);
        }
        return formatList;
    }

    /**
     * 根据部门ID查询部门下所有人员
     *
     * @return
     */

    @RequestMapping("/queryMemberByDepartmentId")
    @ResponseBody
    public String updateEvaluation(@RequestParam(value = "departmentId", defaultValue = "All") String departmentId) {
        DepartmentEntry de = departmentService.getDepartmentEntryById(new ObjectId(departmentId));
        List<ObjectId> memberIds = de.getMembers();
        List<Map<String, String>> memberList = new ArrayList<Map<String, String>>();
        for (ObjectId memberId : memberIds) {
            UserEntry ue = userService.searchUserId(memberId);
            if (ue != null) {
                Map<String, String> m = new HashMap<String, String>();
                m.put("userId", memberId.toString());
                m.put("userName", ue.getUserName());
                memberList.add(m);
            }
        }
        return JSON.toJSONString(memberList);
    }

    /**
     * 将维修任务指派给一个User
     *
     * @return
     */

    @RequestMapping("/deliverRepairMission")
    @ResponseBody
    public String deliverRepairMission(@RequestParam(value = "workerId", defaultValue = "") String workerId,
                                       @RequestParam(value = "repairId", defaultValue = "") String repairId) {
        if ("".equals(workerId)) {
            return JSON.toJSONString(RespObj.FAILD);
        }
        repairService.deliverRepairMissionToUser(new ObjectId(repairId), new ObjectId(workerId),
                userService.searchUserId(new ObjectId(workerId)).getUserName());
        return JSON.toJSONString(RespObj.SUCCESS);
    }

    /**
     * 提交维修结果
     *
     * @return
     */

    @RequestMapping("/commitRepairResult")
    @ResponseBody
    public String commitRepairResult(@RequestParam(value = "resultContent", defaultValue = "") String resultContent,
                                     @RequestParam(value = "repairId", defaultValue = "") String repairId) {
        if ("".equals(repairId)) {
            return JSON.toJSONString(RespObj.FAILD);
        }
        repairService.commitRepairResult(new ObjectId(repairId), resultContent);
        return JSON.toJSONString(RespObj.SUCCESS);
    }


    /**
     * 根据学校id集合查询学校信息集合
     *
     * @return
     */

    @RequestMapping("/findSchools")
    @ResponseBody
    public List<Map<String, Object>> findSchools() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        String userId = getSessionValue().getId();
        List<ObjectId> schoolIdList = educationBureauService.selEducationByEduUserId(new ObjectId(userId)).getSchoolIds();
        List<SchoolEntry> schoolList = repairService.getSchoolList(schoolIdList);
        for (SchoolEntry se : schoolList) {
            Map<String, Object> map = new HashMap<String, Object>();
            ObjectId id = se.getID();
            String name = se.getName();
            map.put("id", id);
            map.put("name", name);
            list.add(map);
        }
        return list;
    }

    /**
     * 教育局页面跳转
     */

    @RequestMapping("/eduList")
    @ResponseBody
    public ModelAndView retrieveRepairInfos() {
        //获取当前学年的学期
        List<String> list = examResultService.getUsableTerm();
        //获取所有学校的集合
        List<Map<String, Object>> shlList = findSchools();

        ModelAndView mv = new ModelAndView();
        mv.addObject("list", list);
        mv.addObject("shlList", shlList);
        mv.setViewName("repair/edubaoxiuguanli");
        return mv;
    }


    /**
     * 教育局中的报修列表
     *
     * @param educationId
     * @param repairTermType
     * @param repairProcedure
     * @param schoolId
     * @return
     */

    @RequestMapping("/findEduList")
    @ResponseBody
    public String queryRepairsForEducation(
            @RequestParam(value = "termType", defaultValue = "ALL") String termType,
            @RequestParam(value = "repairShoolId", defaultValue = "ALL") String schoolId,
            @RequestParam(value = "repairProcedure", defaultValue = "ALL") String repairProcedure,
            @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        ObjectId educationId = educationBureauService.selEducationByEduUserId(new ObjectId(getSessionValue().getId())).getID();
        //TODO
        List<RepairEntry> repairList = new ArrayList<RepairEntry>();
        if ("ALL".equals(repairProcedure)) {
            repairList = repairService.queryRepairsForEducation(educationId, termType, repairProcedure, "ALL".equals(schoolId) ? null : new ObjectId(schoolId), 1, null);
        } else if ("待处理".equals(repairProcedure)) {
            repairList = repairService.queryRepairsForEducation(educationId, termType, "已受理", "ALL".equals(schoolId) ? null : new ObjectId(schoolId), 1, 0);
        } else if ("已受理".equals(repairProcedure)) {
            repairList = repairService.queryRepairsForEducation(educationId, termType, repairProcedure, "ALL".equals(schoolId) ? null : new ObjectId(schoolId), 1, 1);
        } else if ("已完毕".equals(repairProcedure)) {
            repairList = repairService.queryRepairsForEducation(educationId, termType, repairProcedure, "ALL".equals(schoolId) ? null : new ObjectId(schoolId), 1, 1);
        }
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<RepairEntry> pagedList = getListByPage(repairList, pageNo, pageSize);
        List<Map<String, Object>> formatEduList = formatEduList(pagedList);
        Map<String, String> pageMap = new HashMap<String, String>();
        pageMap.put("total", repairList.size() + "");
        pageMap.put("pageNo", pageNo + "");
        resultMap.put("pagejson", pageMap);
        resultMap.put("repairData", formatEduList);
        return JSON.toJSONString(resultMap);
    }


    /**
     * 格式化查询到的Repair结果集，供前台使用(教育局部分)
     *
     * @param srcList
     * @return
     */
    private List<Map<String, Object>> formatEduList(List<RepairEntry> srcList) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<Map<String, Object>> formatList = new ArrayList<Map<String, Object>>();
        for (RepairEntry re : srcList) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("phone", re.getPhone());
            map.put("repairType", re.getRepairType());
            map.put("repairDepartmentName", re.getRepairDepartmentName());
            map.put("repariEvaluation", re.getRepariEvaluation());
            map.put("id", re.getID().toString());
            map.put("repairTermType", re.getRepairTermType());
            map.put("repairDate", sdf.format((new Date(re.getRepairTime()))));
            map.put("solveRepairPersonName", re.getSolveRepairPersonName());
            map.put("schoolName", re.getSchoolName());
            map.put("schlloId", re.getSchoolId());
            map.put("manager", re.getManager());
            map.put("isReport", re.getIsReport());
            map.put("repairPlace", re.getRepairPlace());
            map.put("repairContent", re.getRepairContent());
            map.put("reportReason", re.getReportReason());
            map.put("repairProcedure", re.getRepairProcedure());
            map.put("repairResult", re.getRepairResult());
            map.put("educationId", re.getEducationId());
            map.put("isResolve", re.getIsResolve());
            ;
            formatList.add(map);
        }
        return formatList;
    }


    /**
     * 上报教育局后更新一条报修记录
     *
     * @return
     */

    @RequestMapping("/updateRepairs")
    @ResponseBody
    public String updateReports(@RequestParam(value = "id", defaultValue = "") String id,
                                @RequestParam(value = "repairDepartmentName", defaultValue = "") String repairDepartmentName,
                                @RequestParam(value = "phone", defaultValue = "") String phone,
                                @RequestParam(value = "manager", defaultValue = "") String manager,
                                @RequestParam(value = "reason", defaultValue = "") String reportReason) {
        if ("".equals(id)) {
            return JSON.toJSONString(RespObj.FAILD);
        }
        String schoolId = getSessionValue().getSchoolId();
        manager = getSessionValue().getUserName();
        repairService.updateReport(new ObjectId(id), new ObjectId(schoolId), repairDepartmentName, phone, reportReason, manager);
        return JSON.toJSONString(RespObj.SUCCESS);
    }

    /**
     * 教育局指派维修人员
     *
     * @param id
     * @param solveRepairPersonName
     * @return
     */

    @RequestMapping("/updateEduRepairs")
    @ResponseBody
    public String updateEduRepair(@RequestParam(value = "id", defaultValue = "") String id,
                                  @RequestParam(value = "solveRepairPersonName", defaultValue = "") String repairDepartmentName) {
        if ("".equals(id)) {
            return JSON.toJSONString(RespObj.FAILD);
        }
        repairService.updateEduRepair(new ObjectId(id), repairDepartmentName);
        return JSON.toJSONString(RespObj.SUCCESS);
    }

    /**
     * 更新维修结果
     *
     * @param id
     * @param repairResult
     * @return
     */

    @RequestMapping("/updateEduResult")
    @ResponseBody
    public String updateEduResult(@RequestParam(value = "id", defaultValue = "") String id,
                                  @RequestParam(value = "repairResult", defaultValue = "") String repairResult) {
        if ("".equals(id)) {
            return JSON.toJSONString(RespObj.FAILD);
        }
        repairService.updateEduResult(new ObjectId(id), repairResult);
        return JSON.toJSONString(RespObj.SUCCESS);
    }
    
    
    
    
}
