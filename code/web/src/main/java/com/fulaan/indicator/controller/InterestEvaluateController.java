package com.fulaan.indicator.controller;

import com.fulaan.base.controller.BaseController;
import com.fulaan.indicator.dto.IndicatorTreeAppliedDTO;
import com.fulaan.indicator.dto.InterestEvaluateDTO;
import com.fulaan.indicator.service.IndicatorTreeAppliedService;
import com.fulaan.indicator.service.InterestEvaluateService;
import com.fulaan.utils.RestAPIUtil;
import com.pojo.school.InterestClassDTO;
import com.pojo.user.UserDetailInfoDTO;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.text.Collator;
import java.util.*;

/**
 * Created by guojing on 2016/10/31.
 */
@Controller
@RequestMapping("/evaluate")
public class InterestEvaluateController extends BaseController {

    @Autowired
    private InterestEvaluateService interestEvaluateService;

    @Autowired
    private IndicatorTreeAppliedService iTreeAppliedService;

    /**
     * 评价名称列表
     */
    @RequestMapping("/evaluateManage")
    @ResponseBody
    public ModelAndView showEvaluateManagePage() {
        ModelAndView mv = new ModelAndView();
        int role= getSessionValue().getUserRole();
        //mv.addObject("isHeadMaster",UserRole.isHeadmaster(role));
        mv.setViewName("indicator/evaluateManage");
        return mv;
    }

    /**
     * 添加评价信息
     * @param dto
     * @return
     */
    @RequestMapping("/addITreeApplied")
    @ResponseBody
    public RespObj addITreeApplied(
            @RequestBody IndicatorTreeAppliedDTO dto
    ) {
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        Map<String,String> map = new HashMap<String, String>();
        try {
            if (dto != null) {
                String schoolId=getSchoolId().toString();
                String userId=getUserId().toString();
                dto.setSchoolId(schoolId);
                dto.setCreaterId(userId);
                dto.setEvaluaterIdsStr(userId);
                dto.setEvaluateType(2);
                ObjectId id=iTreeAppliedService.addITreeApplied(dto);
                map.put("id", id.toString());
                map.put("msg", "保存成功!");
            }
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
     * 编辑评价信息
     * @param dto
     * @return
     */
    @RequestMapping("/editITreeApplied")
    @ResponseBody
    public RespObj editITreeApplied(
            @RequestBody IndicatorTreeAppliedDTO dto
    ) {
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        Map<String,String> map = new HashMap<String, String>();
        try {
            if (dto != null) {
                String userId=getUserId().toString();
                dto.setUpdateDate(userId);
                iTreeAppliedService.editITreeApplied(dto);
                map.put("msg", "保存成功!");
            }
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
     * 添加评价信息
     * @param id
     * @return
     */
    @RequestMapping("/delITreeApplied")
    @ResponseBody
    public RespObj delITreeApplied(
        @RequestParam(value = "id") String id
    ) {
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        try {
            int role = getSessionValue().getUserRole();
            ObjectId userId = getUserId();
            iTreeAppliedService.delITreeApplied(id, role, userId);

            respObj.setMessage("删除成功!");
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setMessage(e.getMessage());
        } finally {
            return respObj;
        }
    }

    /**
     * 评价页面
     */
    @RequestMapping("/getITreeApplied")
    @ResponseBody
    public RespObj getITreeApplied(
            @RequestParam(value = "id") String id
    ) {
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        try {
            IndicatorTreeAppliedDTO dto = iTreeAppliedService.getITreeAppliedInfo(id);
            respObj.setMessage(dto);
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
    @RequestMapping("/getITreeAppliedList")
    @ResponseBody
    public RespObj getITreeAppliedList(
            @RequestParam(value = "name",defaultValue = "") String name,
            @RequestParam("page") int page,
            @RequestParam("pageSize") int pageSize
    ) {
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        try {
            ObjectId schoolId=getSchoolId();
            ObjectId userId=getUserId();
            int role= getSessionValue().getUserRole();
            Map<String, Object> map= iTreeAppliedService.getITreeAppliedPageListMap(schoolId, role, userId, name, page, pageSize);
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
     * 评价页面
     */
    @RequestMapping("/gotoInterestEvaluate")
    @ResponseBody
    public ModelAndView gotoInterestEvaluate(
        @RequestParam(value = "id") String id
    ) {
        ModelAndView mv = new ModelAndView();
        //mv.addObject("appliedId", appliedId);
        IndicatorTreeAppliedDTO dto = iTreeAppliedService.getITreeAppliedInfo(id);
        mv.addObject("dto", dto);
        mv.setViewName("indicator/interestEvaluate");
        return mv;
    }

    /**
     * 查询学生列表
     * @param id
     * @return
     */
    @RequestMapping("/getInterestClassList")
    @ResponseBody
    public RespObj getInterestClassList(
            @RequestParam String id
    ) {
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        try {
            //获取学生列表
            List<InterestClassDTO> list = iTreeAppliedService.getInterestClassList(id);
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
     * 查询学生列表
     * @param activityId
     * @param termType
     * @param stuState
     * @return
     */
    @RequestMapping("/getStudentList")
    @ResponseBody
    public RespObj getStudentList(
            @RequestParam String appliedId,
            @RequestParam String activityId,
            @RequestParam(required = false, defaultValue = "-1")int termType,
            @RequestParam(required = false, defaultValue = "1")int stuState
    ) {
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        try {
            //获取学生列表
            List<UserDetailInfoDTO> stuList = interestEvaluateService.findInterestClassStuByActivityId(appliedId, activityId, termType, stuState);
            Collections.sort(stuList, new Comparator<UserDetailInfoDTO>() {
                @Override
                public int compare(UserDetailInfoDTO o1, UserDetailInfoDTO o2) {
                    String name1 = o1.getUserName();
                    String name2 = o2.getUserName();
                    return Collator.getInstance(Locale.CHINESE).compare(name1, name2);
                }
            });
            respObj.setMessage(stuList);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setMessage(e.getMessage());
        } finally {
            return respObj;
        }
    }

    /**
     * 评价名称列表
     */
    @RequestMapping("/evaluateResult")
    @ResponseBody
    public ModelAndView evaluateResult() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("indicator/evaluateResult");
        return mv;
    }

    /**
     * 评价名称列表
     */
    @RequestMapping("/evaluateResultInfo")
    @ResponseBody
    public ModelAndView evaluateResultInfo(
        @RequestParam(value = "appliedId") String appliedId
    ) {
        ModelAndView mv = new ModelAndView();
        //mv.addObject("appliedId", appliedId);
        IndicatorTreeAppliedDTO dto = iTreeAppliedService.getITreeAppliedInfo(appliedId);
        mv.addObject("dto", dto);
        mv.setViewName("indicator/evaluateResultInfo");
        return mv;
    }

    /**
     * 查询学生列表
     * @param activityId
     * @param termType
     * @return
     */
    @RequestMapping("/getStudentResultList")
    @ResponseBody
    public RespObj getStudentResultList(
            @RequestParam String appliedId,
            @RequestParam String activityId,
            @RequestParam(required = false, defaultValue = "-1")int termType,
            @RequestParam(value = "name",defaultValue = "") String name,
            @RequestParam("page") int page,
            @RequestParam("pageSize") int pageSize
    ) {
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        try {
            //获取学生列表
            Map<String, Object> map = interestEvaluateService.getStudentResultMap(appliedId, activityId, termType, name, page, pageSize);
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
     * 学生评价详情
     */
    @RequestMapping("/studentEvaluateDetail")
    @ResponseBody
    public ModelAndView studentEvaluateDetail(
            @RequestParam(value = "id") String id
    ) {
        ModelAndView mv = new ModelAndView();
        InterestEvaluateDTO dto = interestEvaluateService.getStudentEvaluateById(id);
        mv.addObject("dto", dto);
        mv.setViewName("indicator/studentEvaluateDetail");
        return mv;
    }
    /**
     * 添加学生评价信息
     * @param evadto
     * @return
     */
    @RequestMapping("/addEvaluate")
    @ResponseBody
    public RespObj addEvaluate(
            @RequestBody InterestEvaluateDTO evadto
    ) {
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        try {
            if (evadto != null) {
                interestEvaluateService.addEvaluateList(evadto, getSchoolId(), getUserId());
            }
            respObj.setMessage("保存成功!");
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setMessage(e.getMessage());
        } finally {
            return respObj;
        }
    }


    /**
     * 修改学生评价信息
     * @param evadto
     * @return
     */
    @RequestMapping("/updEvaluate")
    @ResponseBody
    public RespObj updEvaluate(
        @RequestBody InterestEvaluateDTO evadto
    ) {
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        try {
            if (evadto != null) {
                interestEvaluateService.updEvaluateList(evadto, getSchoolId(), getUserId());
            }
            respObj.setMessage("保存成功!");
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setMessage(e.getMessage());
        } finally {
            return respObj;
        }
    }

    /**
     * 查询指标树信息
     * @param parentId
     * @param level
     * @param activityId
     * @param commonToId
     * @param termType
     * @param stuState
     * @return
     */
    @RequestMapping("/getStudentEvaluateList")
    @ResponseBody
    public RespObj getStudentEvaluateList(
        @RequestParam String appliedId,
        @RequestParam(value = "snapshotId",defaultValue = "") String snapshotId,
        @RequestParam(value = "parentId",defaultValue = "all") String parentId,
        @RequestParam(value = "level",defaultValue = "1") int level,
        @RequestParam("activityId") String activityId,
        @RequestParam(value = "commonToId",defaultValue = "") String commonToId,
        @RequestParam(value = "termType",defaultValue = "-1") int termType,
        @RequestParam(required = false, defaultValue = "1")int stuState
    ) {
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        try {
            String resultStr= RestAPIUtil.getITreeSnapshotList(snapshotId, parentId, level);
            InterestEvaluateDTO dto = interestEvaluateService.getStudentEvaluate(resultStr, appliedId, activityId, commonToId, termType, stuState);
            respObj.setMessage(dto);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setMessage(e.getMessage());
        } finally {
            return respObj;
        }
    }
}
