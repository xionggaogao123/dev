package com.fulaan.project.controller;

import com.alibaba.fastjson.JSON;
import com.fulaan.annotation.SessionNeedless;
import com.fulaan.base.BaseController;
import com.fulaan.project.service.ProjectService;
import com.fulaan.project.service.SubProjectService;
import com.fulaan.user.service.UserService;
import com.pojo.project.ProjectEntry;
import com.pojo.project.SubProjectEntry;
import com.pojo.user.UserEntry;
import com.sys.utils.RespObj;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
@Api(value="",hidden = true)
@Controller
@RequestMapping("/project")
public class ProjectController extends BaseController {
    @Autowired
    private ProjectService projectService;
    @Autowired
    private SubProjectService subProjectService;
    @Autowired
    private UserService userService;


    private SimpleDateFormat publishTimeSDF = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 列表页面跳转
     */
    @ApiOperation(value = "列表页面跳转", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = ModelAndView.class)})
    @SessionNeedless
    @RequestMapping("/listPage")
    @ResponseBody
    public ModelAndView listPage() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("project/projectList");
        return mv;
    }


    /**
     * 新增页面跳转
     */
    @ApiOperation(value = "新增页面跳转", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = ModelAndView.class)})
    @SessionNeedless
    @RequestMapping("/addPage")
    @ResponseBody
    public ModelAndView addPage() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("project/projectAdd");
        return mv;
    }

    /**
     * 详情页面跳转
     */
    @ApiOperation(value = "详情页面跳转", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = ModelAndView.class)})
    @SessionNeedless
    @RequestMapping("/detailPage")
    @ResponseBody
    public ModelAndView detailPage(@RequestParam(value = "id", defaultValue = "") String id) {
        ModelAndView mv = new ModelAndView();
        String userId = getSessionValue().getId();
        userService.findById(new ObjectId(userId));
        ProjectEntry e = projectService.getProjectEntryById(new ObjectId(id));

        List<SubProjectEntry> subEntries = subProjectService.querySubProjectionEntriesByParentId(new ObjectId(id));
        List<Map<String, Object>> subList = new ArrayList<Map<String, Object>>();
        List<String> subUserNameList = new ArrayList<String>();
        for (SubProjectEntry se : subEntries) {
            subList.add(fomartSubProjectEntry(se));
            if (!subUserNameList.contains(se.getPublishName())) {
                subUserNameList.add(se.getPublishName());
            }
        }
        mv.addObject("id", id);
        mv.addObject("title", e.getProjectTitleName());
        mv.addObject("content", e.getProjectProfile());
        mv.addObject("masterName", e.getProjectUserName());
        mv.addObject("subProjectList", JSON.toJSONString(subList));
        mv.addObject("subProjectNum", subList.size());
        mv.addObject("memberList", JSON.toJSONString(subUserNameList));
        mv.setViewName("project/projectDetail");
        return mv;
    }

    private Map<String, Object> fomartSubProjectEntry(SubProjectEntry se) {
        Map<String, Object> m = new HashMap<String, Object>();
        m.put("id", se.getID().toString());
        m.put("title", se.getSubProjectName());
        m.put("content", se.getSubProjectContent());
        m.put("userName", se.getPublishName());
        m.put("subProjectName", se.getSubProjectName());
        m.put("publishTime", publishTimeSDF.format(new Date(se.getPublishTime())));
        return m;
    }

    /**
     * 新增一个课题信息
     *
     * @return
     */
    @ApiOperation(value = "新增一个课题信息", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @SessionNeedless
    @RequestMapping("/addProject")
    @ResponseBody
    public String addPreparation(@RequestParam(value = "projectTitleName", defaultValue = "") String projectTitleName,
                                 @RequestParam(value = "startTime", defaultValue = "") String startTime,
                                 @RequestParam(value = "endTime", defaultValue = "") String endTime,
                                 @RequestParam(value = "projectLevel", defaultValue = "") String projectLevel,
                                 @RequestParam(value = "projectProfile", defaultValue = "") String projectProfile,
                                 @RequestParam(value = "projectCover", defaultValue = "") String projectCover) {
        ObjectId userId = new ObjectId(getSessionValue().getId());
        UserEntry ue = userService.findById(userId);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            projectService.addProjectEntry(new ProjectEntry(userId, ue.getUserName(),
                    projectTitleName, projectLevel, sdf.parse(startTime).getTime(),
                    sdf.parse(endTime).getTime(), projectProfile, System.currentTimeMillis(), projectCover));
        } catch (ParseException e) {
            e.printStackTrace();
            return JSON.toJSONString(RespObj.FAILD);
        }
        return JSON.toJSONString(RespObj.SUCCESS);
    }

    /**
     * 根据条件查询列表信息
     *
     * @return
     */
    @ApiOperation(value = "根据条件查询列表信息", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @SessionNeedless
    @RequestMapping("/queryList")
    @ResponseBody
    public String queryList(@RequestParam(value = "level", defaultValue = "ALL") String level,
                            @RequestParam(value = "keyword", defaultValue = "") String keyword,
                            @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
                            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        List<ProjectEntry> srcList = projectService.queryProjectionEntries(level, keyword);
        List<Map<String, Object>> fomartList = fomartProjectEntryList(srcList);
        List pagedList = getListByPage(fomartList, pageNo, pageSize);

        Map<String, Object> pageMap = new HashMap<String, Object>();
        pageMap.put("total", fomartList.size());
        pageMap.put("pageNo", pageNo);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("datas", pagedList);
        resultMap.put("pagejson", pageMap);
        return JSON.toJSONString(resultMap);
    }

    /**
     * 根据分页信息进行数据筛选
     *
     * @param src
     * @param pageNo
     * @param pageSize
     * @return
     */
    private List<? extends Object> getListByPage(List<? extends Object> src, int pageNo, int pageSize) {
        List<Object> list = new ArrayList<Object>();
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
     * 组装前台要用的list
     *
     * @param srcList
     * @return
     */
    private List<Map<String, Object>> fomartProjectEntryList(List<ProjectEntry> srcList) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (ProjectEntry e : srcList) {
            list.add(uploadProjectEntryEntryInMap(e));
        }
        return list;
    }

    /**
     * 将一个PreparationEntry组装成一个前台能用的Map
     *
     * @param e
     * @return
     */
    private Map<String, Object> uploadProjectEntryEntryInMap(ProjectEntry e) {
        Map<String, Object> m = new HashMap<String, Object>();
        m.put("id", e.getID().toString());
        m.put("name", e.getProjectTitleName());
        m.put("userName", e.getProjectUserName());
        m.put("level", e.getProjectLevel());
        m.put("publishTime", publishTimeSDF.format(new Date(e.getPublishTime())));
        m.put("startTime", publishTimeSDF.format(new Date(e.getStartTime())));
        m.put("endTime", publishTimeSDF.format(new Date(e.getEndTime())));
        return m;
    }
}
