package com.fulaan.teacherevaluation.controller;

import com.fulaan.annotation.ObjectIdType;
import com.fulaan.base.controller.BaseController;
import com.fulaan.teacherevaluation.service.EvaluationConfigService;
import com.fulaan.teacherevaluation.service.EvaluationItemService;
import com.pojo.app.IdValuePairDTO1;
import com.pojo.teacherevaluation.EvaluationItemDTO;
import com.pojo.teacherevaluation.RankingDTO;
import com.pojo.teacherevaluation.SettingDTO;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created by fl on 2016/4/22.
 */
@Controller
@RequestMapping("/teacherevaluation/{evaluationId}/items")
public class EvaluationItemController extends BaseController {

    private static final Logger logger =Logger.getLogger(EvaluationItemController.class);

    @Autowired
    private EvaluationItemService evaluationItemService;

    @Autowired
    private EvaluationConfigService evaluationConfigService;

    @RequestMapping(method = RequestMethod.GET)
    public String index(@PathVariable @ObjectIdType ObjectId evaluationId, Map<String, Object> model){
        model.putAll(evaluationItemService.formateEvaluationRole(evaluationId, getSessionValue().getUserRole(), getUserId()));
        model.put("evaluationId", evaluationId);
        SettingDTO settingDTO = evaluationConfigService.getSetting(evaluationId);
        long now = System.currentTimeMillis();
        int timeState = now < settingDTO.getEvaluationTimeBegin() ? -1 : now < settingDTO.getEvaluationTimeEnd() ? 0 : 1;
        model.put("timeState", timeState);
        return "/teacherevaluation/item";
    }

    @RequestMapping(value = "/rule", method = RequestMethod.GET)
    @ResponseBody
    public RespObj getRule(@PathVariable @ObjectIdType ObjectId evaluationId){
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        String rule = evaluationItemService.getRule(evaluationId);
        respObj.setMessage(rule);
        return respObj;
    }

    /**
     * 获取老师个人陈述
     * @param teacherId
     * @param evaluationId
     * @return
     */
    @RequestMapping(value = "/statements/{teacherId}", method = RequestMethod.GET)
    @ResponseBody
    public RespObj getTeacherPersonalStatement(@PathVariable @ObjectIdType ObjectId teacherId, @PathVariable @ObjectIdType ObjectId evaluationId){
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        String statement = evaluationItemService.getTeacherStatement(teacherId, evaluationId);
        respObj.setMessage(statement);
        return respObj;
    }

    /**
     * 更新老师个人陈述
     * @param teacherId
     * @param evaluationId
     * @return
     */
    @RequestMapping(value = "/statements/{teacherId}", method = RequestMethod.POST)
    @ResponseBody
    public RespObj updateTeacherPersonalStatement(@PathVariable @ObjectIdType ObjectId teacherId, @PathVariable @ObjectIdType ObjectId evaluationId, String statement){
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        evaluationItemService.updateTeacherStatement(teacherId, evaluationId, statement);
        return respObj;
    }

    /**
     * 获取老师实证资料和量化成绩
     * @param teacherId
     * @param evaluationId
     * @return
     */
    @RequestMapping(value = "/evidence/{teacherId}", method = RequestMethod.GET)
    @ResponseBody
    public RespObj getTeacherEvidenceAndLiangHuaScore(@PathVariable @ObjectIdType ObjectId teacherId, @PathVariable @ObjectIdType ObjectId evaluationId){
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        Map<String, Object> map = evaluationItemService.getTeacherEvidenceAndLiangHuaScore(teacherId, evaluationId);
        respObj.setMessage(map);
        return respObj;
    }

    /**
     * 更新老师实证资料
     * @param teacherId
     * @param evaluationId
     * @param evidence
     * @return
     */
    @RequestMapping(value = "/evidence/{teacherId}", method = RequestMethod.POST)
    @ResponseBody
    public RespObj updateTeacherEvidenceAndLiangHuaScore(@PathVariable @ObjectIdType ObjectId teacherId, @PathVariable @ObjectIdType ObjectId evaluationId, String evidence){
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        try {
            evaluationItemService.updateTeacherEvidence(teacherId, evaluationId, evidence);
        } catch (Exception e){
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setMessage(e.getMessage());
        } finally {
            return respObj;
        }
    }

    /**
     * 得到本组老师列表
     * @param evaluationId
     * @return
     */
    @RequestMapping(value = "/groupTeachers", method = RequestMethod.GET)
    @ResponseBody
    public RespObj getMyGroupTeachers(@PathVariable @ObjectIdType ObjectId evaluationId){
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        Map<String, Object> model = evaluationItemService.getMyGroupTeachers(evaluationId, getUserId());
        respObj.setMessage(model);
        return respObj;
    }

    /**
     * 得到老师个人陈述、实证资料和分数
     * @param teacherId
     * @param evaluationId
     * @return
     */
    @RequestMapping(value = "/statementAndEvidenceAndScore/{teacherId}", method = RequestMethod.GET)
    @ResponseBody
    public RespObj getTeacherStatementAndEvidenceAndHuPingScore(@PathVariable @ObjectIdType ObjectId teacherId, @PathVariable @ObjectIdType ObjectId evaluationId){
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        Map<String, Integer> roleMap = evaluationItemService.formateEvaluationRole(evaluationId, getSessionValue().getUserRole(), getUserId());
        Map<String, Object> map = evaluationItemService.getTeacherStatementAndEvidenceAndScore(teacherId, evaluationId, getUserId(), roleMap.get("leader") == 1);
        respObj.setMessage(map);
        return respObj;
    }

    /**
     * 得到老师个人陈述、实证资料
     * @param teacherId
     * @param evaluationId
     * @return
     */
    @RequestMapping(value = "/statementAndEvidence/{teacherId}", method = RequestMethod.GET)
    @ResponseBody
    public RespObj getTeacherStatementAndEvidence(@PathVariable @ObjectIdType ObjectId teacherId, @PathVariable @ObjectIdType ObjectId evaluationId){
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        Map<String, Object> map = evaluationItemService.getTeacherStatementAndEvidence(teacherId, evaluationId);
        respObj.setMessage(map);
        return respObj;
    }

    /**
     * 查看老师互评成绩
     * @param teacherId
     * @param evaluationId
     * @return
     */
    @RequestMapping(value = "/huping/{teacherId}", method = RequestMethod.GET)
    @ResponseBody
    public RespObj getTeacherHuPingScore(@PathVariable @ObjectIdType ObjectId teacherId, @PathVariable @ObjectIdType ObjectId evaluationId){
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        List<Map<String, Object>> scores = evaluationItemService.getTeacherElementScoreList(teacherId,evaluationId, 1);
        respObj.setMessage(scores);
        return respObj;
    }

//    /**
//     * 更新用户对某个老师的考核打分
//     * @param teacherId
//     * @param evaluationId
//     * @param kaoHeScore
//     * @return
//     */
//    @RequestMapping(value = "/kaohe/{teacherId}", method = RequestMethod.POST)
//    @ResponseBody
//    public RespObj updateTeacherEvaluationScore(@PathVariable @ObjectIdType ObjectId teacherId, @PathVariable @ObjectIdType ObjectId evaluationId, String kaoHeScore){
//        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
//        try {
//            evaluationItemService.updateTeacherEvaluationScore(teacherId, evaluationId, kaoHeScore, getUserId());
//        } catch (Exception e){
//            respObj.setMessage(e.getMessage());
//            respObj.setCode(Constant.FAILD_CODE);
//        } finally {
//            return respObj;
//        }
//    }

    /**
     * 打分
     * @param evaluationId
     * @param kaoHeScore
     * @param tijiao 0保存  1提交
     * @param mode 考核模式  1打分  2等级
     * @return
     */
    @RequestMapping(value = "/kaoheScore", method = RequestMethod.POST)
    @ResponseBody
    public RespObj updateEvaluationScore(@PathVariable @ObjectIdType ObjectId evaluationId, String kaoHeScore, int tijiao, int mode){
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        try {
            evaluationItemService.updateEvaluationScore(evaluationId, kaoHeScore, getUserId(), tijiao, mode);
        } catch (Exception e){
            e.printStackTrace();
            respObj.setMessage(e.getMessage());
            respObj.setCode(Constant.FAILD_CODE);
        } finally {
            return respObj;
        }
    }

    /**
     * 计算排名
     * @param evaluationId
     * @return
     */
    @RequestMapping(value = "/calculateranking", method = RequestMethod.GET)
    @ResponseBody
    public RespObj calculateRanking(@PathVariable @ObjectIdType ObjectId evaluationId){
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
//        List<RankingDTO> rankingDTOs = evaluationItemService.calculateStandardScores(evaluationId);
//        respObj.setMessage(rankingDTOs);
        evaluationItemService.calculateScoresAndRanking(evaluationId);
        return respObj;
    }

    /**
     * 获取排名
     * @param evaluationId
     * @return
     */
    @RequestMapping(value = "/ranking", method = RequestMethod.GET)
    @ResponseBody
    public RespObj getRanking(@PathVariable @ObjectIdType ObjectId evaluationId){
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        try {
            List<RankingDTO> rankingDTOs = evaluationItemService.getStdScoreRanking(evaluationId, getUserId());
            respObj.setMessage(rankingDTOs);
        } catch (Exception e){
            e.printStackTrace();
        }
        return respObj;
    }

    /**
     * 某老师考核元素打分详情
     * @param teacherId
     * @param evaluationId
     * @return
     */
    @RequestMapping(value = "/elementScores/{teacherId}", method = RequestMethod.GET)
    @ResponseBody
    public RespObj getTeacherElementScores(@PathVariable @ObjectIdType ObjectId teacherId, @PathVariable @ObjectIdType ObjectId evaluationId){
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        Map<String, Object> map = evaluationItemService.getTeacherElementScores(teacherId, evaluationId);
        respObj.setMessage(map);
        return respObj;
    }

    /**
     * 考核小组领导对某个老师考核详情
     * @param teacherId
     * @param evaluationId
     * @return
     */
    @RequestMapping(value = "/leaderScores/{teacherId}", method = RequestMethod.GET)
    @ResponseBody
    public RespObj getTeacherLeaderScore(@PathVariable @ObjectIdType ObjectId teacherId, @PathVariable @ObjectIdType ObjectId evaluationId){
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        List<Map<String, Object>> scores = evaluationItemService.getTeacherElementScoreList(teacherId, evaluationId, 2);
        respObj.setMessage(scores);
        return respObj;
    }

    /**
     * 考核小组成员对某个老师考核详情
     * @param teacherId
     * @param evaluationId
     * @return
     */
    @RequestMapping(value = "/groupScores/{teacherId}", method = RequestMethod.GET)
    @ResponseBody
    public RespObj getTeacherGroupScore(@PathVariable @ObjectIdType ObjectId teacherId, @PathVariable @ObjectIdType ObjectId evaluationId){
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        List<Map<String, Object>> scores = evaluationItemService.getTeacherElementScoreList(teacherId, evaluationId, 3);
        respObj.setMessage(scores);
        return respObj;
    }

    /**
     * 我的成绩
     * @return
     */
    @RequestMapping(value = "/myScores", method = RequestMethod.GET)
    @ResponseBody
    public RespObj myScore(){
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        List<Map<String, Object>> list = evaluationItemService.getMyScores(getSchoolId(), getUserId());
        respObj.setMessage(list);
        return respObj;
    }

    /**
     * 教师小组教师列表及状态（状态类型由type指定）
     * @param evaluationId
     * @param groupId
     * @param type 1:实证资料 2：校长查看老师互评 3：考核打分
     * @return
     */
    @RequestMapping(value = "/groupTeachersAndStat", method = RequestMethod.GET)
    @ResponseBody
    public RespObj getGroupTeachersAndState(@PathVariable @ObjectIdType ObjectId evaluationId, @ObjectIdType ObjectId groupId, int type){
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        List<Map<String, Object>> list = evaluationItemService.getGroupTeachersAndState(evaluationId, groupId, getUserId(), type);
        respObj.setMessage(list);
        return respObj;
    }

    /**
     * 清空量化成绩、互评成绩、考核打分
     * @param evaluationId
     * @return
     */
    @RequestMapping(value = "/restScores", method = RequestMethod.POST)
    @ResponseBody
    public RespObj restScores(@PathVariable @ObjectIdType ObjectId evaluationId){
        logger.info(getUserId()+" reset teacher evaluation scores");
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        evaluationItemService.restScores(evaluationId);
        return  respObj;
    }

    /**
     * 检查完整性
     * @param evaluationId
     * @param model
     * @return
     */
    @RequestMapping(value = "/integrity", method = RequestMethod.GET)
    public String checkIntegrity(@PathVariable @ObjectIdType ObjectId evaluationId, Model model){
        List<Map<String, Object>> list = evaluationItemService.checkIntegrity(evaluationId);
        model.addAttribute("list", list);
        model.addAttribute("count", list.size());
        model.addAttribute("evid", evaluationId.toString());
        return "/teacherevaluation/integrity";
    }

    /**
     * 导出完整性检查结果
     * @param evaluationId
     * @param response
     */
    @RequestMapping(value = "/exportIntegrity", method = RequestMethod.GET)
    public void exportIntegrity(@PathVariable @ObjectIdType ObjectId evaluationId, HttpServletResponse response){
        evaluationItemService.exportIntegrity(evaluationId, response);
    }

    /**
     * 可用小组，考核小组领导及成员可以看到所有小组，普通老师只能看到自己所在小组
     * @param evaluationId
     * @return
     */
    @RequestMapping(value = "/availableGroups")
    @ResponseBody
    public RespObj availableGroups(@PathVariable @ObjectIdType ObjectId evaluationId){
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        List<Map<String, Object>> list = evaluationItemService.availableGroups(evaluationId, getSessionValue().getUserRole(), getUserId());
        respObj.setMessage(list);
        return respObj;
    }

    /**
     * 可用老师，小组中除自己以外的所有老师
     * @param evaluationId
     * @return
     */
    @RequestMapping(value = "/group/{groupId}/availableTeachers")
    @ResponseBody
    public RespObj availableTeachers(@PathVariable @ObjectIdType ObjectId evaluationId, @PathVariable @ObjectIdType ObjectId groupId){
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        List<EvaluationItemDTO> list = evaluationItemService.availableTeachers(evaluationId, groupId, getUserId());
        respObj.setMessage(list);
        return respObj;
    }

    //controller结束
}
