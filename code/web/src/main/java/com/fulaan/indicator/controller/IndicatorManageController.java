package com.fulaan.indicator.controller;

import com.alibaba.fastjson.JSON;
import com.fulaan.base.controller.BaseController;
import com.fulaan.indicator.dto.IndicatorDTO;
import com.fulaan.indicator.dto.IndicatorTreeDTO;
import com.fulaan.indicator.service.IndicatorManageService;
import com.pojo.user.UserRole;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;
import com.sys.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

/**
 * Created by guojing on 2016/11/10.
 */
@Controller
@RequestMapping("/indicator")
public class IndicatorManageController extends BaseController {

    @Autowired
    private IndicatorManageService indicatorManageService;
    /**
     * 评价指标列表
     */
    @RequestMapping("/iTreeManage")
    @ResponseBody
    public ModelAndView showiTreeManagePage() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("indicator/iTreeManage");
        return mv;
    }

    /**
     * 根据Id查询评价指标
     * @param name
     * @return
     */
    @RequestMapping("/getITreeList")
    @ResponseBody
    public RespObj getITreeList(
            @RequestParam(value = "owner",defaultValue = "0") int owner,
            @RequestParam(value = "name",defaultValue = "*") String name
    ) {
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        try {
            String schoolId=getSchoolId().toString();
            String userId=getUserId().toString();
            List<IndicatorTreeDTO> list= indicatorManageService.getITreeList(schoolId, userId, owner, name);
            respObj.setMessage(list);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setMessage(e.getMessage());
        } finally {
            return respObj;
        }
    }

    /**
     * 根据Id查询评价指标
     * @param name
     * @return
     */
    @RequestMapping("/getITreePageList")
    @ResponseBody
    public RespObj getITreePageList(
            @RequestParam(value = "name",defaultValue = "*") String name,
            @RequestParam("page") int page,
            @RequestParam("pageSize") int pageSize
    ) {
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        try {
            if (pageSize == 0) {
                pageSize = 10;
            }
            String schoolId=getSchoolId().toString();
            String userId=getUserId().toString();
            Map<String, Object> map= indicatorManageService.getITreePageListMap(schoolId, userId, name, page, pageSize);
            respObj.setMessage(map);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setMessage(e.getMessage());
        } finally {
            return respObj;
        }
    }

    /**
     * 新增或修改评价指标
     * @param dto
     * @return
     */
    @RequestMapping("/addOrEditITree")
    @ResponseBody
    public String addOrEditITree(
            @RequestBody IndicatorTreeDTO dto
    ) {
        String schoolId=getSchoolId().toString();
        String userId=getUserId().toString();
        int role = getSessionValue().getUserRole();
        dto.setSchoolId(schoolId);
        if(StringUtil.isEmpty(dto.getId())){
            dto.setUpdaterId(userId);
        }else{
            dto.setCreaterId(userId);
        }
        if(UserRole.isManager(role)||UserRole.isHeadmaster(role)){
            dto.setType(1);
            dto.setOwner(1);
        }else if(UserRole.isTeacher(role)){
            dto.setType(2);
            dto.setOwner(2);
        }
        String resultStr = indicatorManageService.addOrEditITree(dto);
        return resultStr;
    }

    /**
     * 查询评价指标
     * @param id
     * @return
     */
    @RequestMapping("/getIndicatorTree")
    @ResponseBody
    public String getIndicatorTree(
            @RequestParam(value = "id",defaultValue = "") String id
    ) {
        RespObj resultObj = RespObj.SUCCESS;
        try {
            IndicatorTreeDTO dto = indicatorManageService.getITreeDTO(id);
            resultObj.setMessage(dto);
        }catch (Exception e){
            resultObj = RespObj.FAILD;
            resultObj.setMessage("查询失败!");
        }
        return JSON.toJSONString(resultObj);
    }

    /**
     * 删除评价指标
     * @param id
     * @return
     */
    @RequestMapping("/delIndicatorTree")
    @ResponseBody
    public String delIndicatorTree(
            @RequestParam(value = "id",defaultValue = "") String id
    ) {
        RespObj resultObj = RespObj.SUCCESS;
        try {
            String userId = getUserId().toString();
            indicatorManageService.delIndicatorTree(id, userId);
            resultObj.setMessage("删除成功!");
        }catch (Exception e){
            resultObj = RespObj.FAILD;
            resultObj.setMessage("删除失败!");
        }
        return JSON.toJSONString(resultObj);
    }
    /**
     * 评价指标列表
     */
    @RequestMapping("/indicatorManage")
    @ResponseBody
    public ModelAndView showIndicatorManagePage(
        @RequestParam("treeId") String treeId
    ) {
        ModelAndView mv = new ModelAndView();
        String userId=getUserId().toString();
        IndicatorTreeDTO dto = indicatorManageService.getITreeDTO(treeId);
        if(userId.equals(dto.getCreaterId())){
            dto.setIsHandle(true);
        }
        mv.addObject("dto",dto);
        mv.setViewName("indicator/indicatorManage");
        return mv;
    }

    /**
     * 查询评价指标
     * @param parentId
     * @param level
     * @return
     */
    @RequestMapping("/getIndicatorList")
    @ResponseBody
    public String getIndicatorList(
            @RequestParam("treeId") String treeId,
            @RequestParam(value = "parentId",defaultValue = "all") String parentId,
            @RequestParam(value = "level",defaultValue = "1") int level
    ) {
        String userId=getUserId().toString();
        String resultStr = indicatorManageService.getIndicatorList(treeId, userId, parentId, level);
        return resultStr;
    }

    /**
     * 根据Id查询评价指标
     * @param id
     * @return
     */
    @RequestMapping("/getIndicatorById")
    @ResponseBody
    public String getIndicatorList(
            @RequestParam(value = "id",defaultValue = "") String id
    ) {
        String resultStr = indicatorManageService.getIndicatorById(id);
        return resultStr;
    }

    /**
     * 新增或修改评价指标
     * @param dto
     * @return
     */
    @RequestMapping("/addOrEditIndicator")
    @ResponseBody
    public String addOrEditIndicator(
            @RequestBody IndicatorDTO dto

    ) {
        String schoolId=getSchoolId().toString();
        String userId=getUserId().toString();
        dto.setSchoolId(schoolId);
        dto.setUserId(userId);
        dto.setTeacherId(userId);
        String resultStr = indicatorManageService.addOrEditIndicator(dto);
        return resultStr;
    }

    /**
     * 删除评价指标
     * @param id
     * @return
     */
    @RequestMapping("/delIndicatorById")
    @ResponseBody
    public String delIndicatorById(
            @RequestParam(value = "id",defaultValue = "") String id
    ) {
        RespObj resultObj = RespObj.SUCCESS;
        try {
            String userId = getUserId().toString();
            indicatorManageService.delIndicator(id, userId);
            resultObj.setMessage("删除成功!");
        }catch (Exception e){
            resultObj = RespObj.FAILD;
            resultObj.setMessage("删除失败!");
        }
        return JSON.toJSONString(resultObj);
    }
}
