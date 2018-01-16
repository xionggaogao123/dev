package com.fulaan.wrongquestion.controller;

import com.alibaba.fastjson.JSON;
import com.fulaan.annotation.SessionNeedless;
import com.fulaan.base.BaseController;
import com.fulaan.questionbook.dto.QuestionTagsDTO;
import com.fulaan.wrongquestion.dto.*;
import com.fulaan.wrongquestion.service.WrongQuestionService;
import com.pojo.app.FileUploadDTO;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import com.sys.utils.QiniuFileUtils;
import com.sys.utils.RespObj;
import io.swagger.annotations.*;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by James on 2017/9/6.
 */
@Api(value = "错题本")
@Controller
@RequestMapping("/jxmapi/wrongQuestion")
public class DefaultWrongQuestionController extends BaseController {
    @Autowired
    private WrongQuestionService wrongQuestionService;
    private static final Logger logger =Logger.getLogger(DefaultWrongQuestionController.class);
    /**
     *  绑定年级
     * @param dto
     * @return
     */
    @ApiOperation(value = "绑定年级", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/addNewVersionGradeEntry")
    @ResponseBody
    public String addCommentEntry(@ApiParam @RequestBody NewVersionGradeDTO dto){
        //
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            String result = wrongQuestionService.addGradeFromUser(dto);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setMessage("绑定年级失败!");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 添加学科
     * @PARAM DTO
     * @RETURN
     */
    @ApiOperation(value = "添加学科", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/addSubjectEntry")
    @ResponseBody
    public String addSubjectEntry(@ApiParam @RequestBody SubjectClassDTO dto){
        //
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            String result = wrongQuestionService.addSubjectEntry(dto);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setMessage("绑定年级失败!");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 添加年级
     * @PARAM DTO
     * @RETURN
     */
    @ApiOperation(value = "添加年级", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/addGradeEntry")
    @ResponseBody
    public String addGradeEntry(@ApiParam @RequestBody CreateGradeDTO dto){
        //
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            String result = wrongQuestionService.addGradeEntry(dto);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setMessage("添加年级失败!");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 年级,科目加载
     * @return
     */
    @ApiOperation(value = "年级", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getGradeAndSubject")
    @ResponseBody
    public String getGradeAndSubject(){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            Map<String,Object> result = wrongQuestionService.getGradeAndSubject(getUserId());
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("error",e);
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setMessage("年级、科目加载失败!");
        }
        return JSON.toJSONString(respObj);
    }
    /**
     * 大人端年级,科目加载
     * @return
     */
    @ApiOperation(value = "大人端年级,科目加载", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getGradeAndSubjectForParent")
    @ResponseBody
    public String getGradeAndSubjectForParent(@ApiParam(name = "studentId", required = true, value = "学生id") @RequestParam(value="studentId") String studentId,
                                              @ApiParam(name = "type", required = true, value = "类型") @RequestParam(value="type") int type){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            Map<String,Object> result = wrongQuestionService.getGradeAndSubjectForParent(getUserId(), new ObjectId(studentId),type);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setMessage("大人端年级,科目加载失败!");
        }
        return JSON.toJSONString(respObj);
    }
    /**
     * 问题,测试加载
     * @return
     */
    @ApiOperation(value = "问题,测试加载", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getQuestionAndTest")
    @ResponseBody
    public String getQuestionAndTest(@ApiParam(name = "subjectId", required = true, value = "学科id") @RequestParam(value="subjectId",defaultValue = "*") String subjectId,
                                     @ApiParam(name = "ename", required = true, value = "阶段名") @RequestParam(value="ename",defaultValue = "*") String ename){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            if(subjectId ==null || subjectId.equals("")||subjectId.equals("*")||ename == null || ename.equals("") || ename.equals("*") ){
                Map<String,Object> map = new HashMap<String, Object>();
                List<QuestionTypeDTO> dtoList1 = new ArrayList<QuestionTypeDTO>();
             /*   QuestionTypeDTO dto = new QuestionTypeDTO();
                dto.setName("不限");
                dtoList1.add(dto);*/
                map.put("questionTypeList",dtoList1);
                List<QuestionTagsDTO> dtoList2 = new ArrayList<QuestionTagsDTO>();
               /* TestTypeDTO dto1 = new TestTypeDTO();
                dto1.setName("不限");
                dtoList2.add(dto1);*/
                map.put("TestTypeList",dtoList2);
                respObj.setCode(Constant.SUCCESS_CODE);
                respObj.setMessage(map);
            }else{
                Map<String,Object> result = wrongQuestionService.getQuestionAndTest(getUserId(),new ObjectId(subjectId),ename);
                respObj.setCode(Constant.SUCCESS_CODE);
                respObj.setMessage(result);
            }

        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setMessage("问题,测试加载失败!");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 添加错题
     *
     * @param errorDto
     * @return
     */
    @ApiOperation(value = "添加错题", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @ResponseBody
    @RequestMapping(value = "/addNew")
    public RespObj addWrongTitleToErrorBook(@ApiParam @RequestBody ErrorBookDTO errorDto) {

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
    @ApiOperation(value = "删除错题", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @ResponseBody
    @RequestMapping(value = "/remove")
    public RespObj removeFromErrorBook(@ApiParam(name = "id", required = true, value = "错题id") @RequestParam String id) {

        RespObj resp = new RespObj(Constant.FAILD_CODE);

        try {
            ErrorBookDTO errorDto = wrongQuestionService.getErrorQuestion(new ObjectId(id));
            if(errorDto != null) {
                wrongQuestionService.removeErrorQuestion(new ObjectId(id));
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
    @ApiOperation(value = "为错题添加新的解析", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @ResponseBody
    @RequestMapping(value = "/explain")
    public RespObj addNewExplain(@ApiParam @RequestBody Map<String, Object> postMap) {

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
    @ApiOperation(value = "删除解析", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @ResponseBody
    @RequestMapping(value = "/remove/explain")
    public RespObj removeExplain(
            @ApiParam(name = "id", required = true, value = "错题id") @RequestParam String id,
            @ApiParam(name = "explainId", required = true, value = "解析id") @RequestParam String explainId) {

        RespObj resp = new RespObj(Constant.FAILD_CODE);

        try {
            ErrorBookDTO errorDto = wrongQuestionService.getErrorQuestion(new ObjectId(id));
            if(errorDto != null) {
                wrongQuestionService.removeExplain(new ObjectId(id), new ObjectId(explainId));
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
    @ApiOperation(value = "学会该错题", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @ResponseBody
    @RequestMapping(value = "/grasp")
    public RespObj updateQuestionGraspStatus( @ApiParam(name = "id", required = true, value = "错题id") @RequestParam String id) {

        RespObj resp = new RespObj(Constant.FAILD_CODE);

        try {
            String userId = getUserId().toString();
            ErrorBookDTO errorDto = wrongQuestionService.getErrorQuestion(new ObjectId(id));
            if (errorDto != null) {
                String uid = errorDto.getUserId();
                if (!uid.equals(userId.toString())) {
                    resp.setMessage("添加失败！");
                } else {
                    wrongQuestionService.updateQuestionGraspStatus(new ObjectId(id));
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
    @ApiOperation(value = "分页查询错题", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @ResponseBody
    @RequestMapping(value = "/find/questions")
    public RespObj queryQuestionWithPaging(
            @ApiParam @RequestBody Map<String, Object> obj,
            @ApiParam(name = "page", required = true, value = "分页起始页数") @RequestParam(required = true, defaultValue = "1") int page,
            @ApiParam(name = "pageSize", required = true, value = "每页条数") @RequestParam(required = true, defaultValue = "5") int pageSize) {

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
    @ApiOperation(value = "分页查询掌握的错题", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @ResponseBody
    @RequestMapping(value = "/find/grasp/questions")
    public RespObj queryGraspWithPaging(
            @ApiParam @RequestBody Map<String, Object> obj,
            @ApiParam(name = "page", required = true, value = "分页起始页数") @RequestParam(required = true, defaultValue = "1") int page,
            @ApiParam(name = "pageSize", required = true, value = "每页条数") @RequestParam(required = true, defaultValue = "5") int pageSize) {

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

    /**
     * 上传图片
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/images", method = RequestMethod.POST)
    @ResponseBody
    @SessionNeedless
    @ApiOperation(value = "上传图片到七牛", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "上传图片到七牛成功",response = RespObj.class),
            @ApiResponse(code = 500, message = "上传图片到七牛失败")})
    public RespObj uploadImage3(@ApiParam @RequestBody MultipartRequest request) {
        RespObj obj = new RespObj(Constant.FAILD_CODE);
        List<FileUploadDTO> fileInfos = new ArrayList<FileUploadDTO>();
        try {
            Map<String, MultipartFile> fileMap = request.getFileMap();
            List<String> list = new ArrayList<String>(fileMap.keySet());
            if (list.size() == 0) {
                obj.setMessage("未上传图片");
                return obj;
            }
            ObjectId id = new ObjectId();
            MultipartFile file = fileMap.get(list.get(0));
            String extensionName = FilenameUtils.getExtension(file.getOriginalFilename());
            String fileKey = id.toString() + Constant.POINT + extensionName;
            QiniuFileUtils.uploadFile(fileKey, file.getInputStream(), QiniuFileUtils.TYPE_IMAGE);
            String path = QiniuFileUtils.getPath(QiniuFileUtils.TYPE_IMAGE, fileKey);
            FileUploadDTO dto = new FileUploadDTO(id.toString(), fileKey, file.getOriginalFilename(), path);
            fileInfos.add(dto);
            obj = new RespObj(Constant.SUCCESS_CODE, fileInfos);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }
}
