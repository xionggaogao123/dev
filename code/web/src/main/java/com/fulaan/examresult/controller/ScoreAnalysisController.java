package com.fulaan.examresult.controller;

import com.fulaan.annotation.ObjectIdType;
import com.fulaan.base.controller.BaseController;
import com.fulaan.examresult.service.ScoreSummaryService;
import com.fulaan.utils.ExportUtil;
import com.pojo.app.IdValuePairDTO1;
import com.pojo.examscoreanalysis.*;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fl on 2016/8/22.
 */
@RequestMapping("/scoreanalysis")
@Controller
public class ScoreAnalysisController extends BaseController {

	
	
    @Autowired
    private ScoreSummaryService scoreSummaryService;

    @RequestMapping(method = RequestMethod.GET)
    public String index(){
        return "/scoreanalysis/headmaster";
    }

    /**
     * 总结数据
     * @param examId
     * @param subjectId null表示所有学科  否则为具体学科
     * @param type  -1 全部  1 班级层面  2 年级层面
     * @return
     */
    @RequestMapping("/summary")
    @ResponseBody
    public RespObj getScoreSummaryByExamId(@ObjectIdType ObjectId examId, @RequestParam(required = false) ObjectId subjectId, int type){
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        List<ScoreSummaryDTO> list = scoreSummaryService.getScoreSummaryByExamId(examId, subjectId, type);
        respObj.setMessage(list);
        return respObj;
    }

    /**
     * 是否允许总分分析
     * @param examId
     * @return
     */
    @RequestMapping("/zoufenstate")
    @ResponseBody
    public RespObj getZoufenState(@ObjectIdType ObjectId examId){
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        Map<String, Boolean> states = scoreSummaryService.getZoufenState(examId);
        respObj.setMessage(states);
        return respObj;
    }

    @RequestMapping("/yswzongfen")
    @ResponseBody
    public RespObj getYSWZongfen(@ObjectIdType ObjectId examId){
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        List<ZongFenSummaryDTO> dtos = scoreSummaryService.getZongFenSummary(examId, ZongFenEntry.YSW);
        respObj.setMessage(dtos);
        return respObj;
    }

    @RequestMapping("/zongfen")
    @ResponseBody
    public RespObj getZongfen(@ObjectIdType ObjectId examId){
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        List<ZongFenSummaryDTO> dtos = scoreSummaryService.getZongFenSummary(examId, ZongFenEntry.ALL);
        respObj.setMessage(dtos);
        return respObj;
    }

    /**
     * 总分分析--个人维度
     * @param examId
     * @return
     */
    @RequestMapping("/studentzongfen")
    @ResponseBody
    public RespObj getStudentZongfen(@ObjectIdType ObjectId examId, int page, int pageSize){
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        Map<String, Object> model = new HashMap<String, Object>();
        List<ZongFenDTO> zongFenDTOs = scoreSummaryService.getZongFenDTOsByExamId(examId, page, pageSize);
        model.put("list", zongFenDTOs);
        model.put("count", scoreSummaryService.countZongFenByExamId(examId));
        model.put("page", page);
        model.put("pageSize", pageSize);
        respObj.setMessage(model);
        return respObj;
    }

    /**
     * 考试科目列表
     * @param examId
     * @return
     */
    @RequestMapping("/examsubjects")
    @ResponseBody
    public RespObj getExamSubjects(@ObjectIdType ObjectId examId){
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        List<IdValuePairDTO1> list = scoreSummaryService.getExamSubjects(examId);
        respObj.setMessage(list);
        return respObj;
    }

    /**
     * 单科学生排名
     * @param examId
     * @param subjectId
     * @param page
     * @param pageSize
     * @return
     */
    @RequestMapping("/stuSubScores")
    @ResponseBody
    public RespObj getScoreEntries(@ObjectIdType ObjectId examId, @ObjectIdType ObjectId subjectId, int page, int pageSize) throws Exception{
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        Map<String, Object> model = new HashMap<String, Object>();
        List<ThreePlusThreeScoreDTO> list = scoreSummaryService.getScoreEntries(examId, subjectId, page, pageSize);
        int count = scoreSummaryService.countScoreEntries(examId, subjectId);
        model.put("list", list);
        model.put("page", page);
        model.put("pageSize", pageSize);
        model.put("count", count);
        respObj.setMessage(model);
        return respObj;
    }

    /**
     * 导出三率一分
     * @param examId
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("/exportResult/3lv1fen")
    public void export_3lv1fen(@ObjectIdType ObjectId examId, HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream;charset=UTF-8");
        response.addHeader("Pargam", "no-cache");
        response.addHeader("Cache-Control", "no-cache");
        ExportUtil util = new ExportUtil();

        scoreSummaryService.exportResult_3lv1fen(util, examId);
        response.setHeader("Content-Disposition", "attachment;filename=" + getFileName(request, util.getFileName()));
        util.getBook().write(response.getOutputStream());
    }

    /**
     * 各科排名表
     * @param examId
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("/exportResult/subjectsRanking")
    public void export_subjectsRanking(@ObjectIdType ObjectId examId, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType("application/octet-stream;charset=UTF-8");
        response.addHeader("Pargam", "no-cache");
        response.addHeader("Cache-Control", "no-cache");
        ExportUtil util = new ExportUtil();

        scoreSummaryService.export_subjectsRanking(util, examId);
        response.setHeader("Content-Disposition", "attachment;filename=" + getFileName(request, util.getFileName()));
        util.getBook().write(response.getOutputStream());
    }

    /**
     * 年级学生总排名表
     * @param examId
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("/exportResult/zongbiao")
    public void export_zongbiao(@ObjectIdType ObjectId examId, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType("application/octet-stream;charset=UTF-8");
        response.addHeader("Pargam", "no-cache");
        response.addHeader("Cache-Control", "no-cache");
        ExportUtil util = new ExportUtil();

        scoreSummaryService.export_zongbiao(util, examId);
        response.setHeader("Content-Disposition", "attachment;filename=" + getFileName(request, util.getFileName()));
        util.getBook().write(response.getOutputStream());
    }

    /**
     * 前X名分布表
     * @param examId
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("/exportResult/topX")
    public void export_topX(@ObjectIdType ObjectId examId, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType("application/octet-stream;charset=UTF-8");
        response.addHeader("Pargam", "no-cache");
        response.addHeader("Cache-Control", "no-cache");
        ExportUtil util = new ExportUtil();

        scoreSummaryService.export_topX(util, examId);
        response.setHeader("Content-Disposition", "attachment;filename=" + getFileName(request, util.getFileName()));
        util.getBook().write(response.getOutputStream());
    }

    private String getFileName(HttpServletRequest request, String fileName) throws UnsupportedEncodingException {
        String agent = request.getHeader("User-Agent");
        if (agent != null && agent.toLowerCase().indexOf("firefox") >= 0) {
            fileName = new String(fileName.getBytes(Constant.UTF_8), Constant.ISO);
        } else {
            fileName = java.net.URLEncoder.encode(fileName, Constant.UTF_8);
        }
        return fileName;
    }


}
