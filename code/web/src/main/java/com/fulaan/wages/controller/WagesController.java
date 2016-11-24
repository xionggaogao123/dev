package com.fulaan.wages.controller;

import com.fulaan.annotation.SessionNeedless;
import com.fulaan.base.controller.BaseController;
import com.fulaan.salary.service.SalaryService;
import com.fulaan.user.service.UserService;
import com.fulaan.utils.ExportUtil;
import com.fulaan.wages.service.WagesService;
import com.pojo.salary.SalaryDto;
import com.pojo.salary.SalaryItemDto;
import com.pojo.user.UserInfoDTO;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.QueryParam;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * Created by wang_xinxin on 2016/6/16.
 */
@Controller
@RequestMapping("/wages")
public class WagesController extends BaseController {
    private static final Logger logger = Logger.getLogger(WagesController.class);

    @Autowired
    private WagesService wagesService;

    @Autowired
    private SalaryService salaryService;

    @Autowired
    private UserService userService;


    /**
     *
     * @return
     */
    @RequestMapping("/gzgl")
    public String gzgl(Map<String, Object> model) {
        String url="";
        int userRole=getSessionValue().getUserRole();
        List<String> yearList = new ArrayList<String>();
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        for (int i = year + 1; i > year - 10; i--) {
            yearList.add(i + Constant.EMPTY);
        }
        int month = cal.get(Calendar.MONTH) + 1;
        model.put("yearList", yearList);
        model.put("currYear", year);
        model.put("currMonth", month < 10 ? "0" + month : month);
        return "/gzgl/xcgl";
    }

    /**
     *
     * @return
     */
    @RequestMapping("/gztj")
    public String gztj(Map<String, Object> model) {
        String url="";
        int userRole=getSessionValue().getUserRole();
        List<String> yearList = new ArrayList<String>();
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        for (int i = year + 1; i > year - 10; i--) {
            yearList.add(i + Constant.EMPTY);
        }
        int month = cal.get(Calendar.MONTH) + 1;
//        List<UserInfoDTO> userInfoDTOs = userService.findTeatherBySchoolId(getSessionValue().getSchoolId());
//        model.put("users",userInfoDTOs);
        model.put("yearList", yearList);
        model.put("currYear", year);
        model.put("currMonth", month < 10 ? "0" + month : month);
        return "/gzgl/gztj";
    }

    /**
     *
     * @return
     */
    @RequestMapping("/wdgz")
    public String wdgz(Map<String, Object> model) {
        List<String> yearList = new ArrayList<String>();
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        for (int i = year + 1; i > year - 10; i--) {
            yearList.add(i + Constant.EMPTY);
        }
        int month = cal.get(Calendar.MONTH) + 1;
        model.put("yearList", yearList);
        model.put("currYear", year);
        model.put("currMonth", month < 10 ? "0" + month : month);
        return "/gzgl/wdgz";
    }


    /**
     * 查询工资项目数据
     */
    @RequestMapping("/itemList")
    @ResponseBody
    public Map retrieveItemInfo() {
        Map map = new HashMap();
        ObjectId schoolId = new ObjectId(getSessionValue().getSchoolId());
        List<SalaryItemDto> itemList = salaryService.findSalaryItemInfo(schoolId);
        map.put("rows",itemList);
       return map;
    }

    /**
     * @param itemInfo
     * @return
     */
    @RequestMapping("/itemAdd")
    @ResponseBody
    public RespObj addItemInfo(@ModelAttribute("itemInfo") SalaryItemDto itemInfo) {

        RespObj result = new RespObj(Constant.FAILD_CODE);
        if (logger.isDebugEnabled()) {
            logger.debug("try to add salary item: " + itemInfo.toString());
        }
        itemInfo.setSchoolId(getSessionValue().getSchoolId());
        int count = salaryService.checkSalaryItem(itemInfo);
        if (count>0) {
            result.message = "添加工资项目重复！";
            result.code = Constant.FAILD_CODE;
            return result;
        }
        String itemId = salaryService.addSalaryItemInfo(itemInfo);
        if (itemId == null) {
            result.message = "添加工资项目失败，请稍后再试";
            if (logger.isDebugEnabled()) {
                logger.debug("add salary item failed: " + itemInfo.toString());
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("add salary item success: " + itemInfo.toString());
        }
        result.code = Constant.SUCCESS_CODE;
        result.message = "添加工资项目成功";

        return result;
    }

    /**
     *
     * @param id
     * @return
     */
    @RequestMapping("/itemDelete")
    @ResponseBody
    public RespObj deleteItemInfo(String id) {

        RespObj result = new RespObj(Constant.FAILD_CODE);
        SalaryItemDto itemInfo = new SalaryItemDto();
        itemInfo.setId(id);
        String itemId = salaryService.deleteSalaryItemInfo(itemInfo);
        if (itemId == null) {
            result.message = "删除工资项目失败，请稍后再试";
            if (logger.isDebugEnabled()) {
                logger.debug("add salary item failed: " + id.toString());
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("delete salary item success: " + id.toString());
        }
        result.code = Constant.SUCCESS_CODE;
        result.message = "删除工资项目成功";

        return result;
    }

    /**
     * 修改工资项目记录数据
     */
    @SessionNeedless
    @RequestMapping("/itemUpdate")
    @ResponseBody
    public RespObj updateItemInfo(@ModelAttribute("itemInfo") SalaryItemDto itemInfo) {

        RespObj result = new RespObj(Constant.FAILD_CODE);
        if (logger.isDebugEnabled()) {
            logger.debug("try to update salary item: " + itemInfo.toString());
        }
        String itemId = salaryService.updateSalaryItemInfo(itemInfo);
        if (itemId == null) {
            result.message = "修改工资项目失败，请稍后再试";
            if (logger.isDebugEnabled()) {
                logger.debug("add salary item failed: " + itemInfo.toString());
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("add salary item success: " + itemInfo.toString());
        }
        result.code = Constant.SUCCESS_CODE;
        result.message = "修改工资项目成功";
        return result;
    }

    /**
     * 获取某月份的当前工资次数
     *
     * @param year
     * @param month
     * @return
     */
    @RequestMapping("/getSalaryTimes")
    @ResponseBody
    public RespObj getSalaryTimes(Integer year, Integer month) {
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        respObj.setMessage(salaryService.getSalaryTimes(getSessionValue().getSchoolId(), year, month));
        return respObj;
    }

    /**
     * 根据年份、月份、次数查询工资信息
     */
    @RequestMapping("/list")
    @ResponseBody
    public Map retrieveSalary(
            @QueryParam("year") int year,
            @QueryParam("month") int month,
            @QueryParam("num") int num,
            @QueryParam("userId") String userId,
            @QueryParam("name") String name,
            @RequestParam("page") int page,@RequestParam("pageSize") int pageSize) {
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        if (logger.isDebugEnabled()) {
            logger.debug("try to query salary (y,m,c): " + year + ":" + month + ":" + num);
        }
        return wagesService.findSalaryInfo(getSessionValue().getSchoolId(), userId,name, year, month, num, page, pageSize);
    }

    /**
     * 制表操作，制作工资表(某年、月、次的工资表)
     */
    @SessionNeedless
    @RequestMapping("/tabulate")
    @ResponseBody
    public RespObj tabulateSalary(
            @QueryParam("year") int year,
            @QueryParam("month") int month) {
        RespObj result = new RespObj(Constant.FAILD_CODE);
        String schoolId = this.getSessionValue().getSchoolId();
        List<UserInfoDTO> salaryList = userService.findTeatherBySchoolId(schoolId);
        List<SalaryItemDto> itemList = salaryService.findSalaryItemInfo(new ObjectId(schoolId));
        if (null == itemList || itemList.size() == 0) {
            result.message = "没有工资项目, 请先添加";
            return result;
        }
        Object[] numAry = salaryService.getSalaryTimes(getSessionValue().getSchoolId(), year, month);
        int currNum = 1;
        if (numAry.length!=0) {
            currNum = Integer.valueOf(numAry[numAry.length-1].toString())+1;
        }
        List<SalaryDto> mtList = new ArrayList<SalaryDto>();
        for (int i = 0; salaryList != null && i < salaryList.size(); i++) {
            SalaryDto salaryDto = new SalaryDto();
            salaryDto.setSchoolId(getSessionValue().getSchoolId());
            salaryDto.setTimesName(String.format("%d年%s月份第%d次工资单", year, month < 10 ? "0" + month : Constant.EMPTY + month, currNum));
            salaryDto.setUid(salaryList.get(i).getId());
            salaryDto.setUserName(salaryList.get(i).getName());
            salaryDto.setYear(year);
            salaryDto.setMonth(month);
            salaryDto.setNumber(currNum);

            List<SalaryItemDto> mList = new ArrayList<SalaryItemDto>();
            for (int j = 0; j < itemList.size(); j++) {
                SalaryItemDto it = new SalaryItemDto();
                it.setItemName(itemList.get(j).getItemName());
                it.setType(itemList.get(j).getType());
                it.setM(0);
                mList.add(it);
            }

            salaryDto.setMoney(mList);
            salaryDto.setSs(0);
            salaryDto.setMs(0);
            salaryDto.setAs(0);

            mtList.add(salaryDto);
        }
        salaryService.addSalaryInfoBatch(mtList);
        result.setCode(Constant.SUCCESS_CODE);
        result.message = salaryService.getSalaryTimes(getSessionValue().getSchoolId(), year, month);
        return result;
    }

    /**
     * 修改工资数据中某一工资项目值
     */
    @SessionNeedless
    @RequestMapping("/update")
    @ResponseBody
    public RespObj updateSalaryInfo(
            @QueryParam("id") String id,
            @QueryParam("itemName") String itemName,
            @QueryParam("m") double m) {

        RespObj result = new RespObj(Constant.FAILD_CODE);
        if (logger.isDebugEnabled()) {
            logger.debug("try to update salary info: id:" + id + ", itemName:" + itemName);
        }
        SalaryDto salaryDto = salaryService.updateSalaryInfo(id, itemName, m);
        if (logger.isDebugEnabled()) {
            logger.debug("add salary item success: ");
        }
        result.code = Constant.SUCCESS_CODE;
        result.setMessage(salaryDto);
        return result;
    }

    /**
     * 删除指定工资表
     *
     * @param year
     * @param month
     * @param num
     * @return
     */
    @SessionNeedless
    @RequestMapping("/deleteSalary")
    @ResponseBody
    public RespObj deleteSalary(
            @QueryParam("year") int year,
            @QueryParam("month") int month, @QueryParam("num") int num) {
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        Object[] indexs = salaryService.getSalaryTimes(getSessionValue().getSchoolId(), year, month);
        if (num!=Integer.valueOf(indexs[indexs.length-1].toString())) {
            respObj.message = "只能删除最大次数！";
            respObj.code = Constant.FAILD_CODE;
        } else {
            salaryService.deleteSalary(getSessionValue().getSchoolId(), year, month, num);
        }

        return respObj;
    }

    /**
     * 生成导入工资的模板
     *
     * @param request
     * @param response
     * @throws java.io.IOException
     */
    @SessionNeedless
    @RequestMapping("/template")
    public String template(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (StringUtils.isEmpty(request.getParameter("year"))
                || StringUtils.isEmpty(request.getParameter("month"))
                || StringUtils.isEmpty(request.getParameter("num"))) {
            return null;
        }
        response.setContentType("application/octet-stream;charset=UTF-8");
        response.addHeader("Pargam", "no-cache");
        response.addHeader("Cache-Control", "no-cache");
        ExportUtil util = null;
        try {
            util = new ExportUtil();
            int year = Integer.parseInt(request.getParameter("year"));
            int month = Integer.parseInt(request.getParameter("month"));
            int num = Integer.parseInt(request.getParameter("num"));

            //创建考试成绩模板
            this.salaryService.createTemplate(util, year, month, num, getSessionValue().getSchoolId());
            response.setHeader("Content-Disposition", "attachment;filename=" + getFileName(request, util.getFileName()));
            util.getBook().write(response.getOutputStream());
        } finally {
            if (util != null) {
                util.destroy();
            }
        }
        return null;
    }

    /**
     * 获取导出文件的名称
     *
     * @param request
     * @param fileName
     * @return
     * @throws java.io.UnsupportedEncodingException
     */
    private String getFileName(HttpServletRequest request, String fileName) throws UnsupportedEncodingException {
        String agent = request.getHeader("User-Agent");
        if (agent != null && agent.toLowerCase().indexOf("firefox") >= 0) {
            fileName = new String(fileName.getBytes(Constant.UTF_8), Constant.ISO);
        } else {
            fileName = java.net.URLEncoder.encode(fileName, Constant.UTF_8);
        }
        return fileName;
    }

    /**
     * 根据年份、月份、次数查询工资信息
     */
    @RequestMapping("/selMySalaryDetail")
    @ResponseBody
    public RespObj selMySalaryDetail(
            @QueryParam("year") int year,
            @QueryParam("month") int month,
            @QueryParam("num") int num) {
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        if (logger.isDebugEnabled()) {
            logger.debug("try to query salary (y,m,c): " + year + ":" + month + ":" + num);
        }
        respObj.setMessage(wagesService.findMySalaryDetail(getUserId(), year, month, num));
        return respObj;
    }

    /**
     * 导出工资
     *
     * @param request
     * @param response
     * @throws java.io.IOException
     */
    @SessionNeedless
    @RequestMapping("/exprotSalaryList")
    public String exprotSalaryList(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (StringUtils.isEmpty(request.getParameter("year"))
                || StringUtils.isEmpty(request.getParameter("month"))
                || StringUtils.isEmpty(request.getParameter("num"))) {
            return null;
        }
        response.setContentType("application/octet-stream;charset=UTF-8");
        response.addHeader("Pargam", "no-cache");
        response.addHeader("Cache-Control", "no-cache");
        ExportUtil util = null;
        try {
            util = new ExportUtil();
            int year = Integer.parseInt(request.getParameter("year"));
            int month = Integer.parseInt(request.getParameter("month"));
            int num = Integer.parseInt(request.getParameter("num"));
            String userId = request.getParameter("userId");

            //创建考试成绩模板
            this.wagesService.createSalaryTemplate(util, year, month, num, userId, getSessionValue().getSchoolId());
            response.setHeader("Content-Disposition", "attachment;filename=" + getFileName(request, util.getFileName()));
            util.getBook().write(response.getOutputStream());
        } finally {
            if (util != null) {
                util.destroy();
            }
        }
        return null;
    }
    /**
     * 导出工资条
     *
     * @param request
     * @param response
     * @throws java.io.IOException
     */
    @SessionNeedless
    @RequestMapping("/exprotSalaryDetail")
    public String exprotSalaryDetail(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (StringUtils.isEmpty(request.getParameter("year"))
                || StringUtils.isEmpty(request.getParameter("month"))
                || StringUtils.isEmpty(request.getParameter("num"))) {
            return null;
        }
        response.setContentType("application/octet-stream;charset=UTF-8");
        response.addHeader("Pargam", "no-cache");
        response.addHeader("Cache-Control", "no-cache");
        ExportUtil util = null;
        try {
            util = new ExportUtil();
            int year = Integer.parseInt(request.getParameter("year"));
            int month = Integer.parseInt(request.getParameter("month"));
            int num = Integer.parseInt(request.getParameter("num"));
            String userId = request.getParameter("userId");

            //创建考试成绩模板
            this.wagesService.exprotSalaryDetail(util, year, month, num,userId, getSessionValue().getSchoolId());
            response.setHeader("Content-Disposition", "attachment;filename=" + getFileName(request, util.getFileName()));
            util.getBook().write(response.getOutputStream());
        } finally {
            if (util != null) {
                util.destroy();
            }
        }
        return null;
    }

    /**
     * 修改工资数据中某一工资项目值
     */
    @SessionNeedless
    @RequestMapping("/updateRemark")
    @ResponseBody
    public RespObj updateRemark(
            @QueryParam("id") String id,
            @QueryParam("m") String m) {

        RespObj result = new RespObj(Constant.FAILD_CODE);
        if (logger.isDebugEnabled()) {
            logger.debug("try to update salary info: id:" + id);
        }
        salaryService.updateRemark(id, m);
        if (logger.isDebugEnabled()) {
            logger.debug("add salary item success: ");
        }
        result.code = Constant.SUCCESS_CODE;
        return result;
    }
}
