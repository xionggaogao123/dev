package com.fulaan.reportCard.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import com.db.reportCard.GroupExamDetailDao;
import com.db.reportCard.ScoreRepresentDao;
import com.db.wrongquestion.SubjectClassDao;
import com.fulaan.annotation.ObjectIdType;
import com.fulaan.base.BaseController;
import com.fulaan.reportCard.dto.ExamGroupDTO;
import com.fulaan.reportCard.dto.ExamGroupNewDto;
import com.fulaan.reportCard.dto.ExamGroupScoreDTO;
import com.fulaan.reportCard.dto.ExamGroupUserScoreDTO;
import com.fulaan.reportCard.dto.GroupExamDetailDTO;
import com.fulaan.reportCard.dto.GroupExamScoreDTO;
import com.fulaan.reportCard.dto.GroupExamUserRecordDTO;
import com.fulaan.reportCard.dto.GroupExamUserRecordStrDTO;
import com.fulaan.reportCard.dto.GroupExamUserRecordStrListDTO;
import com.fulaan.reportCard.dto.GroupExamVersionDTO;
import com.fulaan.reportCard.dto.ScoreRepresentDto;
import com.fulaan.reportCard.service.ReportCardNewService;
import com.pojo.reportCard.GroupExamDetailEntry;
import com.pojo.reportCard.GroupExamUserRecordEntry;
import com.pojo.reportCard.ScoreRepresentEntry;
import com.pojo.reportCard.VirtualUserEntry;
import com.pojo.user.UserEntry;
import com.pojo.wrongquestion.SubjectClassEntry;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * Created by scott on 2017/9/30.
 */
@Controller
@RequestMapping(value = "/web/reportCardNew")
@Api(value = "成绩单的所有接口")
public class ReportCardNewController extends BaseController {

    @Autowired
    private ReportCardNewService reportCardService;
    
    private SubjectClassDao subjectClassDao = new SubjectClassDao();
    
    private ScoreRepresentDao scoreRepresentDao = new ScoreRepresentDao();
    
    private GroupExamDetailDao groupExamDetailDao = new GroupExamDetailDao();
    
    private static final Logger logger =Logger.getLogger(ReportCardNewController.class);
    
    @ApiOperation(value = "判断是否需要上传学生并分配", httpMethod = "GET", produces = "application/json")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "导入模板已完成", response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/judgeIsExistMatch")
    @ResponseBody
    public RespObj judgeIsExistMatch(@ObjectIdType ObjectId communityId){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            int flag=reportCardService.judgeIsExistMatch(communityId);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(flag);
        }catch (Exception e){
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }


    @ApiOperation(value = "保存考试的信息", httpMethod = "POST", produces = "application/json")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "保存考试信息已完成", response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/saveGroupExamDetail")
    @ResponseBody
    public RespObj saveGroupExamDetail(@RequestBody ExamGroupNewDto examGroupDTO) {
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try {
            GroupExamDetailDTO groupExamDetailDTO = examGroupDTO.buildDTO();
            String result = new ObjectId().toString();
            //先判断是否绑定了学生
            if(reportCardService.isHaveRecordEntries(groupExamDetailDTO.getCommunityId())) {
                result = reportCardService.saveGroupExamDetail(groupExamDetailDTO,getUserId());
            }
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }
    
    /**
     * 查询详情信息
     *
     * @param groupExamDetailId
     * @return
     */
    @ApiOperation(value = "获取该条成绩单的详情信息(针对老师)", httpMethod = "GET", produces = "application/json")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "保存或编辑成绩列表已完成", response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getTeacherGroupExamDetail")
    @ResponseBody
    public RespObj getTeacherGroupExamDetail(@ObjectIdType ObjectId groupExamDetailId) {
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try {
            GroupExamDetailDTO detailDTO = reportCardService.
                    getTeacherGroupExamDetail(groupExamDetailId);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(detailDTO);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }

    /**
     * 查询分数段代表
     *
     * @param groupExamDetailId
     * @return
     */
    @ApiOperation(value = "查询", httpMethod = "GET", produces = "application/json")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "保存或编辑成绩列表已完成", response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getScoreRepresentById")
    @ResponseBody
    public RespObj getScoreRepresentById(String groupExamDetailId) {
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try {
            List<ScoreRepresentDto> listDto = reportCardService.getScoreRepresentById(new ObjectId(groupExamDetailId));
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(listDto);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }
    
    /**
     * 保存分数段代表
     *
     * @param groupExamDetailId
     * @return
     */
    @ApiOperation(value = "保存", httpMethod = "POST", produces = "application/json")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "保存或编辑成绩列表已完成", response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/saveScoreRepresent")
    @ResponseBody
    public RespObj saveScoreRepresent(String s) {
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try {
            reportCardService.saveScoreRepresent(s);
            respObj.setCode(Constant.SUCCESS_CODE);
         
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }
    
    @ApiOperation(value = "查询录入成绩的学生名单", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "保存或编辑成绩列表已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/searchRecordStudentScoresStr")
    @ResponseBody
    public RespObj searchRecordStudentScoresStr(String examGroupDetailId,
                                                String userRecordId,
                                                String teaType,
                                                String cur,
                                             @RequestParam(required = false,defaultValue = "-1")int score,
                                             @RequestParam(required = false,defaultValue = "-1")int scoreLevel,
                                             @RequestParam(required = false,defaultValue = "1")int type){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try{
            List<GroupExamUserRecordDTO> examScoreDTOs=reportCardService.searchRecordStudentScores(new ObjectId(examGroupDetailId));
            respObj.setMessage(this.trans(examScoreDTOs, userRecordId,teaType,cur, score, scoreLevel, type));
            respObj.setCode(Constant.SUCCESS_CODE);
        }catch (Exception e){
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }
    
    /**
     * 
     *〈简述〉GroupExamUserRecordDTO转GroupExamUserRecordStrDTO
     *〈详细描述〉
     * @author Administrator
     * @param list
     * @return
     */
    public List<GroupExamUserRecordStrListDTO> trans(List<GroupExamUserRecordDTO> list, String userRecordId, String teaType,String cur, int score, int scoeLevel, int type) {
        List<GroupExamUserRecordStrListDTO> l = new ArrayList<GroupExamUserRecordStrListDTO>();
        for (GroupExamUserRecordDTO g : list) {
            GroupExamUserRecordStrListDTO gs = new GroupExamUserRecordStrListDTO(g);
            GroupExamDetailEntry gede = groupExamDetailDao.getEntryById(new ObjectId(gs.getGroupExamDetailId()));
            List<String> subS = gs.getSubjectId();
            List<String> scoreS = gs.getScore();
            List<Integer> scoreSL = gs.getScoreLevel();
            List<String> rankS = gs.getRankList();
            List<ScoreRepresentEntry> srList = scoreRepresentDao.getScoreRepresentAll(new ObjectId(g.getGroupExamDetailId()));
            for (String s : subS) {
                SubjectClassEntry sc = subjectClassDao.getEntry(new ObjectId(s));
                gs.getSubjectName().add(sc.getName());
            }
            if (subS.size()>1) {
                gs.getSubjectName().add("总分");
            }
            
            if (StringUtils.isNotBlank(cur)) {
                gs.getSubjectNameWithDd().add(gs.getSubjectName().get(Integer.valueOf(cur)));
                if (gede.getRecordScoreType() == 1) {
                    gs.getSubjectNameWithDd().add("排名");
                    gs.getSubjectNameWithDd().add("等第");
                } else {
                    gs.getSubjectNameWithDd().add("排名");
                    gs.getSubjectNameWithDd().add("等第");
                }
                
                if (gede.getRecordScoreType() == 1) {
                    if (score != -1) {
                        int ii = compareScoreInt(scoreS.get(Integer.valueOf(cur)),new ScoreRepresentDto(srList.get(Integer.valueOf(cur))));
                        if (type == 1) {
                            
                            if (ii > score) {
                                continue;
                            }
                        } else {
                            if (ii < score) {
                                continue;
                            }
                        }
                    }
                    if ("1".equals(teaType)) {
                        if (gede.getFsShowType() == 0) {
                            gs.getScoreDd().add(scoreS.get(Integer.valueOf(cur)));
                            gs.getScoreDd().add("-");
                            gs.getScoreDd().add(compareScore(scoreS.get(Integer.valueOf(cur)),new ScoreRepresentDto(srList.get(Integer.valueOf(cur)))));
                        } else if (gede.getFsShowType() == 1) {
                            gs.getScoreDd().add(scoreS.get(Integer.valueOf(cur)));
                            gs.getScoreDd().add(rankS.get(Integer.valueOf(cur)).equals("-1")?"-":rankS.get(Integer.valueOf(cur)));
                            gs.getScoreDd().add("-");
                        } else if (gede.getFsShowType() == 2) {
                            gs.getScoreDd().add(scoreS.get(Integer.valueOf(cur)));
                            gs.getScoreDd().add("-");
                            gs.getScoreDd().add("-");
                        } else {
                            gs.getScoreDd().add("-");
                            gs.getScoreDd().add("-");
                            gs.getScoreDd().add(compareScore(scoreS.get(Integer.valueOf(cur)),new ScoreRepresentDto(srList.get(Integer.valueOf(cur)))));
                        }
                    } else {
                        gs.getScoreDd().add(scoreS.get(Integer.valueOf(cur)));
                        gs.getScoreDd().add(rankS.get(Integer.valueOf(cur)).equals("-1")?"-":rankS.get(Integer.valueOf(cur)));
                        gs.getScoreDd().add(compareScore(scoreS.get(Integer.valueOf(cur)),new ScoreRepresentDto(srList.get(Integer.valueOf(cur)))));
                    }
                    
                    gs.getScoreRep().add(compareScore(scoreS.get(Integer.valueOf(cur)),new ScoreRepresentDto(srList.get(Integer.valueOf(cur)))));
                } else {
                    if (scoeLevel != -1) {
                        if (scoeLevel == 89) {
                            if (scoreSL.get(Integer.valueOf(cur)) > 91 || scoreSL.get(Integer.valueOf(cur)) == -1 || scoreSL.get(Integer.valueOf(cur)) == -2) {
                                continue;
                            } 
                        } else if (scoeLevel == 92) {
                            if (scoreSL.get(Integer.valueOf(cur)) < 92 ||  scoreSL.get(Integer.valueOf(cur)) > 94 || scoreSL.get(Integer.valueOf(cur)) == -1 || scoreSL.get(Integer.valueOf(cur)) == -2) {
                                continue;
                            }
                        } else if (scoeLevel == 95) {
                            if (scoreSL.get(Integer.valueOf(cur)) < 95 ||  scoreSL.get(Integer.valueOf(cur)) > 97 || scoreSL.get(Integer.valueOf(cur)) == -1 || scoreSL.get(Integer.valueOf(cur)) == -2) {
                                continue;
                            }
                        } else {
                            if (scoreSL.get(Integer.valueOf(cur)) < 98 || scoreSL.get(Integer.valueOf(cur)) == -1 || scoreSL.get(Integer.valueOf(cur)) == -2) {
                                continue;
                            }
                        }
                    }
                    gs.getScoreDd().add(compareScoreLevel(scoreSL.get(Integer.valueOf(cur))));
                    //gs.getScoreDd().add(rankS.get(j));
                    gs.getScoreDd().add("-");
                    gs.getScoreDd().add("-");
                    gs.getScoreRep().add("-");
                }
                
                
            } else {
                for (String s : subS) {
                    SubjectClassEntry sc = subjectClassDao.getEntry(new ObjectId(s));
                    gs.getSubjectNameWithDd().add(sc.getName());
                    if (gede.getRecordScoreType() == 1) {
                        gs.getSubjectNameWithDd().add("排名");
                        gs.getSubjectNameWithDd().add("等第");
                    } else {
                        gs.getSubjectNameWithDd().add("排名");
                        gs.getSubjectNameWithDd().add("等第");
                    }
                    
                }
                if (subS.size()>1) {
                    gs.getSubjectNameWithDd().add("总分");
                }
                if (subS.size()>1) {
                    if (gede.getRecordScoreType() == 1) {
                        gs.getSubjectNameWithDd().add("排名");
                        gs.getSubjectNameWithDd().add("等第");
                    } else {
                        gs.getSubjectNameWithDd().add("排名");
                        gs.getSubjectNameWithDd().add("等第");
                    }
                }
                
                
                for (int j =0;j<scoreSL.size();j++) {
                    
                    if (gede.getRecordScoreType() == 1) {
                        if ("1".equals(teaType)) {
                            if (gede.getFsShowType() == 0) {
                                gs.getScoreDd().add(scoreS.get(j));
                                gs.getScoreDd().add("-");
                                gs.getScoreDd().add(compareScore(scoreS.get(j),new ScoreRepresentDto(srList.get(j))));
                            } else if (gede.getFsShowType() == 1) {
                                gs.getScoreDd().add(scoreS.get(j));
                                gs.getScoreDd().add(rankS.get(j).equals("-1")?"-":rankS.get(j));
                                gs.getScoreDd().add("-");
                            } else if (gede.getFsShowType() == 2) {
                                gs.getScoreDd().add(scoreS.get(j));
                                gs.getScoreDd().add("-");
                                gs.getScoreDd().add("-");
                            } else {
                                gs.getScoreDd().add("-");
                                gs.getScoreDd().add("-");
                                gs.getScoreDd().add(compareScore(scoreS.get(j),new ScoreRepresentDto(srList.get(j))));
                            }
                        } else {
                            gs.getScoreDd().add(scoreS.get(j));
                            gs.getScoreDd().add(rankS.get(j).equals("-1")?"-":rankS.get(j));
                            gs.getScoreDd().add(compareScore(scoreS.get(j),new ScoreRepresentDto(srList.get(j))));
                        }
                        
                        gs.getScoreRep().add(compareScore(scoreS.get(j),new ScoreRepresentDto(srList.get(j))));
                    } else {
                        
                        gs.getScoreDd().add(compareScoreLevel(scoreSL.get(j)));
                        //gs.getScoreDd().add(rankS.get(j));
                        gs.getScoreDd().add("-");
                        gs.getScoreDd().add("-");
                        gs.getScoreRep().add("-");
                    }
                    
                }
                
            }
            if (StringUtils.isNotBlank(userRecordId)) {
                if(gs.getId().equals(userRecordId)) {
                    gs.setOwnChild(true);
                }
            }
            
            
            l.add(gs);
        }
        return l;
    }
    
    private static final Map<Integer, String> mapone = new HashMap<Integer, String>();
    private static final Map<Integer, String> maptwo = new HashMap<Integer, String>();
    private static final Map<Integer, String> mapthree = new HashMap<Integer, String>();
    
    static {
        mapone.put(1, "优秀");
        mapone.put(2, "良好");
        mapone.put(3, "合格");
        mapone.put(4, "需努力");
        
        maptwo.put(1, "优");
        maptwo.put(2, "良");
        maptwo.put(3, "中");
        maptwo.put(4, "差");
        
        mapthree.put(1, "A");
        mapthree.put(2, "B");
        mapthree.put(3, "C");
        mapthree.put(4, "D");
        
        
    }
    
    public int compareScoreInt(String score, ScoreRepresentDto s) {
        int i = 1;
        if (StringUtils.isBlank(score)) {
            score = "0";

        } else if ("缺".equals(score)) {
            score = "-1";

        }
        if (new BigDecimal(score).compareTo(new BigDecimal(s.getScoreTwo()))>=0) {
           
        } else if (new BigDecimal(score).compareTo(new BigDecimal(s.getScoreFour()))>=0) {
            i =2;
        } else if (new BigDecimal(score).compareTo(new BigDecimal(s.getScoreSix()))>=0) {
            i = 3;
        } else {
            i = 4;
        }
        return i;
    }
    
    public String compareScoreLevel(Integer score) {
        if (score == 100) {
            return "A+";
        } else if (score == 99) {
            return "A";
        } else if (score == 98) {
            return "A-";
        } else if (score == 97) {
            return "B+";
        } else if (score == 96) {
            return "B";
        } else if (score == 95) {
            return "B-";
        } else if (score == 94) {
            return "C+";
        } else if (score == 93) {
            return "C";
        } else if (score == 92) {
            return "C-";
        } else if (score == 91) {
            return "D+";
        } else if (score == 90) {
            return "D";
        } else if (score == 89) {
            return "D-";
        } else if (score == -1) {
            return "缺";
        } else  {
            return "未填写";
        } 
    }
    
    public String compareScore(String score, ScoreRepresentDto s) {
        int i = 1;
        if (StringUtils.isBlank(score)) {
            //score = "0";
            return "-";
        } else if ("缺".equals(score)) {
            //score = "-1";
            return "-";
        }
        if (new BigDecimal(score).compareTo(new BigDecimal(s.getScoreTwo()))>=0) {
           
        } else if (new BigDecimal(score).compareTo(new BigDecimal(s.getScoreFour()))>=0) {
            i =2;
        } else if (new BigDecimal(score).compareTo(new BigDecimal(s.getScoreSix()))>=0) {
            i = 3;
        } else {
            i = 4;
        }
        if (s.getRepresentNameType() == 1) {
            return mapone.get(i);
        } else if (s.getRepresentNameType() == 2) {
            return maptwo.get(i);
        } else {
            return mapthree.get(i);
        }
    }
    
    @ApiOperation(value = "获取该考试的版本号", httpMethod = "GET", produces = "application/json")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "保存或编辑成绩列表已完成", response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getExamGroupVersion")
    @ResponseBody
    public RespObj getExamGroupVersion(@ObjectIdType ObjectId examGroupDetailId) {
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try {
            GroupExamVersionDTO groupExamVersionDTO = reportCardService.getExamGroupVersion(examGroupDetailId);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(groupExamVersionDTO);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }
    
    @ApiOperation(value = "保存或编辑成绩列表新dto", httpMethod = "GET", produces = "application/json")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "保存或编辑成绩列表已完成", response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/saveRecordExamScoreNew")
    @ResponseBody
    public RespObj saveRecordExamScoreNew(@RequestBody GroupExamScoreDTO examGroupScoreDTO) {
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try {
            GroupExamVersionDTO groupExamVersionDTO = reportCardService.getExamGroupVersion(
                    new ObjectId(examGroupScoreDTO.getGroupExamDetailId()));
            if (groupExamVersionDTO.getVersion() != examGroupScoreDTO.getVersion()) {
                List<GroupExamUserRecordStrListDTO> examScoreStrDTO = examGroupScoreDTO.getExamGroupUserScoreListDTOs();
     
                List<GroupExamUserRecordDTO> examScoreDTOs = this.transTo(examScoreStrDTO);
                reportCardService.saveRecordExamScore(examScoreDTOs, examGroupScoreDTO.getStatus(), examGroupScoreDTO.getIsSend());
                reportCardService.updateVersion(new ObjectId(examGroupScoreDTO.getGroupExamDetailId()),
                        examGroupScoreDTO.getVersion());
                reportCardService.updateShowType(new ObjectId(examGroupScoreDTO.getGroupExamDetailId()), examGroupScoreDTO.getShowType());
                reportCardService.updateFsShowType(new ObjectId(examGroupScoreDTO.getGroupExamDetailId()), examGroupScoreDTO.getFsShowType());
                respObj.setCode(Constant.SUCCESS_CODE);
                respObj.setMessage("保存或编辑成绩成功!");
            } else {
                respObj.setErrorMessage("不是最新的版本");
            }
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }
    
    /**
     * 
     *〈简述〉GroupExamUserRecordStrDTO转GroupExamUserRecordDTO
     *〈详细描述〉
     * @author Administrator
     * @param list
     * @return
     */
    public List<GroupExamUserRecordDTO> transTo(List<GroupExamUserRecordStrListDTO> list) {
        List<GroupExamUserRecordDTO> l = new ArrayList<GroupExamUserRecordDTO>();
        for (GroupExamUserRecordStrListDTO g : list) {
            GroupExamUserRecordDTO gs = new GroupExamUserRecordDTO(g);
            l.add(gs);
        }
        return l;
    }
    
    
    /**
    *
    * @param examGroupDetailId
    * @param response
    */
   @ApiOperation(value = "导出模板", httpMethod = "GET", produces = "application/json")
   @RequestMapping("/exportTemplateNew/{examGroupDetailId}")
   @ResponseBody
   public void exportTemplate(@PathVariable @ObjectIdType ObjectId examGroupDetailId,
                              HttpServletResponse response,
                              HttpServletRequest request) {
       try {
           reportCardService.exportTemplateNew(request,examGroupDetailId, response);
       } catch (Exception e) {
           e.printStackTrace();
       }
   }
   
   /**
   *
   * @param examGroupDetailId
   * @param response
   */
  @ApiOperation(value = "导出模板", httpMethod = "GET", produces = "application/json")
  @RequestMapping("/exportStuNew/{examGroupDetailId}")
  @ResponseBody
  public void exportStuNew(@PathVariable @ObjectIdType ObjectId examGroupDetailId,
                             HttpServletResponse response,
                             HttpServletRequest request) {
      try {
          reportCardService.exportStuNew(request,examGroupDetailId, response);
      } catch (Exception e) {
          e.printStackTrace();
      }
  }
   
   /**
   *
   * @param request
   * @return
   * @throws Exception
   */
  @ApiOperation(value = "导入模板", httpMethod = "GET", produces = "application/json")
  @ApiResponses(value = {@ApiResponse(code = 200, message = "导入模板已完成", response = String.class),
          @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
          @ApiResponse(code = 500, message = "服务器不能完成请求")})
  @RequestMapping("/importTemplate")
  @ResponseBody
  public RespObj importTemplate(HttpServletRequest request) throws Exception {
      RespObj respObj = new RespObj(Constant.FAILD_CODE);
      String groupExamId = request.getParameter("groupExamId");
      MultipartRequest multipartRequest = (MultipartRequest) request;
      try {
          MultiValueMap<String, MultipartFile> fileMap = multipartRequest.getMultiFileMap();
          for (List<MultipartFile> multipartFiles : fileMap.values()) {
              for (MultipartFile file : multipartFiles) {
                  System.out.println("----" + file.getOriginalFilename());
                  reportCardService.importTemplate(groupExamId,file.getInputStream());
              }
          }
          respObj.setCode(Constant.SUCCESS_CODE);
          respObj.setMessage("导入模板成功");
      } catch (Exception e) {
          e.printStackTrace();
          respObj.setErrorMessage(e.getMessage());
      }
      return respObj;
  }

}
