package com.fulaan.wrongquestion.controller;

import com.alibaba.fastjson.JSON;
import com.fulaan.base.BaseController;
import com.fulaan.utils.pojo.KeyValue;
import com.fulaan.wrongquestion.dto.ErrorBookDTO;
import com.fulaan.wrongquestion.dto.NewVersionGradeDTO;
import com.fulaan.wrongquestion.dto.SubjectClassDTO;
import com.fulaan.wrongquestion.service.WrongQuestionService;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by James on 2017/9/6.
 */
@Controller
@RequestMapping("/wrongQuestion")
public class WrongQuestionController extends BaseController {
    @Autowired
    private WrongQuestionService wrongQuestionService;

    /**
     *  绑定年级
     * @param dto
     * @return
     */
    @RequestMapping("/addNewVersionGradeEntry")
    @ResponseBody
    public String addCommentEntry(NewVersionGradeDTO dto){
        //
        KeyValue keyValue = wrongQuestionService.getCurrTermType();
        dto.setYear(keyValue.getValue());
        RespObj respObj=null;
        try {
            String result = wrongQuestionService.addGradeFromUser(dto);
            respObj = RespObj.SUCCESS;
            respObj.setMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
            respObj = RespObj.FAILD;
            respObj.setMessage("绑定年级失败!");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 添加学科
     * @PARAM DTO
     * @RETURN
     */
    @RequestMapping("/addNewVersionGradeEntry")
    @ResponseBody
    public String addSubjectEntry(SubjectClassDTO dto){
        //
        RespObj respObj=null;
        try {
            //String result = wrongQuestionService.addGradeFromUser(dto);
            respObj = RespObj.SUCCESS;
           //respObj.setMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
            respObj = RespObj.FAILD;
            respObj.setMessage("绑定年级失败!");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 年级、科目加载
     * @param userId
     * @return
     */
    @RequestMapping("/getGradeAndSubject")
    @ResponseBody
    public String getGradeAndSubject(@RequestParam("userId") String userId){
        RespObj respObj=null;
        try {
            Map<String,Object> result = wrongQuestionService.getGradeAndSubject(new ObjectId(userId));
            respObj = RespObj.SUCCESS;
            respObj.setMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
            respObj = RespObj.FAILD;
            respObj.setMessage("年级、科目加载失败!");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 添加错题
     *
     * @param errorDto
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/addNew")
    public RespObj addWrongTitleToErrorBook(@RequestBody ErrorBookDTO errorDto) {

        RespObj resp = new RespObj(Constant.FAILD_CODE);

        try {
            errorDto.setUserId(getUserId().toString());
            String id = wrongQuestionService.addErrorQuestion(errorDto);
            resp.setMessage(id);
        } catch (Exception e) {
            resp.setMessage("出错了！");
        }

        return resp;
    }

    /**
     * 删除错题
     *
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/remove")
    public RespObj removeFromErrorBook(@RequestParam ObjectId id) {

        RespObj resp = new RespObj(Constant.FAILD_CODE);

        try {
            ErrorBookDTO errorDto = wrongQuestionService.getErrorQuestion(id);
            if(errorDto != null) {
                wrongQuestionService.removeErrorQuestion(id);
                resp.setCode(Constant.SUCCESS_CODE);
            } else {
                resp.setMessage("不错在该错题！");
            }
        } catch (Exception e) {
            resp.setMessage("出错了！");
        }

        return resp;
    }

    /**
     * 为错题添加新的解析
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/explain")
    public RespObj addNewExplain(@RequestBody Map<String, Object> postMap) {

        RespObj resp = new RespObj(Constant.FAILD_CODE);

        try {
            String questionId = (String) postMap.get("id");
            if(!StringUtils.isEmpty(questionId)) {
                ObjectId id = new ObjectId(questionId);
                ErrorBookDTO errorDto = wrongQuestionService.getErrorQuestion(id);
                if(errorDto != null) {
                    String explain = (String) postMap.get("explain");
                    List<Object> attachDtoList = (List<Object>) postMap.get("attachs");
                    ObjectId explId = wrongQuestionService.addExplain(id, explain, attachDtoList);
                    resp.setMessage(explId.toString());
                    resp.setCode(Constant.SUCCESS_CODE);
                } else {
                    resp.setMessage("不错在该错题！");
                }
            } else {
                resp.setMessage("请检查参数");
            }
        } catch (Exception e) {
            resp.setMessage("出错了！");
        }

        return resp;
    }

    /**
     * 删除解析
     *
     * @param id
     * @param explainId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/remove/explain")
    public RespObj removeExplain(
            @RequestParam ObjectId id,
            @RequestParam ObjectId explainId) {

        RespObj resp = new RespObj(Constant.FAILD_CODE);

        try {
            ErrorBookDTO errorDto = wrongQuestionService.getErrorQuestion(id);
            if(errorDto != null) {
                wrongQuestionService.removeExplain(id, explainId);
                resp.setCode(Constant.SUCCESS_CODE);
            } else {
                resp.setMessage("不错在该错题！");
            }
        } catch (Exception e) {
            resp.setMessage("出错了！");
        }
        return resp;
    }


    /**
     * 学会该错题
     *
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/grasp")
    public RespObj updateQuestionGraspStatus(@RequestParam ObjectId id) {

        RespObj resp = new RespObj(Constant.FAILD_CODE);

        try {
            String userId = getUserId().toString();
            ErrorBookDTO errorDto = wrongQuestionService.getErrorQuestion(id);
            if (errorDto != null) {
                String uid = errorDto.getUserId();
                if (!uid.equals(userId.toString())) {
                    resp.setMessage("添加失败！");
                } else {
                    wrongQuestionService.updateQuestionGraspStatus(id);
                    resp.setCode(Constant.SUCCESS_CODE);
                }
            } else {
                resp.setMessage("添加失败，不存在改错题！");
            }
        } catch (Exception e) {
            resp.setMessage("出错了！");
        }
        return resp;
    }

    /**
     * 分页查询错题
     *
     * @param page
     * @param pageSize
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/find/questions")
    public RespObj queryQuestionWithPaging(
            @RequestBody Map<String, Object> obj,
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "5") int pageSize) {

        RespObj resp = new RespObj(Constant.FAILD_CODE);

        try {
            ObjectId userId = getUserId();
            ObjectId subjectId = new ObjectId((String)obj.get("subjectId"));
            ObjectId gradeId = new ObjectId((String)obj.get("gradeId"));
            List<String> pointStrList = (ArrayList)obj.get("pointList");
            String keyword = (String)obj.get("kw");

            List<ObjectId> pointList = MongoUtils.convertToObjectIdList(pointStrList);

            List<ErrorBookDTO> bookDtoList =
                    wrongQuestionService.findErrorQuestionDtoList(userId, subjectId, gradeId, pointList, keyword, page, pageSize, true);
            int count = wrongQuestionService.countErrorQuestion(userId, subjectId, gradeId, pointList, keyword, true);

            Map<String, Object> result = new HashMap<String, Object>();
            result.put("count", count);
            result.put("data", bookDtoList);

            resp.setCode(Constant.SUCCESS_CODE);
            resp.setMessage(result);
        } catch (Exception e) {
            resp.setMessage("出错了！");
        }

        return resp;
    }

    /**
     * 分页查询掌握的错题
     * @param page
     * @param pageSize
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/find/grasp/questions")
    public RespObj queryGraspWithPaging(
            @RequestBody Map<String, Object> obj,
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "5") int pageSize) {

        RespObj resp = new RespObj(Constant.FAILD_CODE);

        try {
            ObjectId userId = getUserId();
            ObjectId subjectId = new ObjectId((String)obj.get("subjectId"));
            ObjectId gradeId = new ObjectId((String)obj.get("gradeId"));
            List<String> pointStrList = (ArrayList)obj.get("pointList");
            String keyword = (String)obj.get("kw");

            List<ObjectId> pointList = MongoUtils.convertToObjectIdList(pointStrList);

            List<ErrorBookDTO> bookDtoList =
                    wrongQuestionService.findErrorQuestionDtoList(userId, subjectId, gradeId, pointList, keyword, page, pageSize, false);
            int count = wrongQuestionService.countErrorQuestion(userId, subjectId, gradeId, pointList, keyword, false);

            Map<String, Object> result = new HashMap<String, Object>();
            result.put("count", count);
            result.put("data", bookDtoList);

            resp.setCode(Constant.SUCCESS_CODE);
            resp.setMessage(result);

        } catch (Exception e) {
            resp.setMessage("出错了！");
        }

        return resp;
    }


}
