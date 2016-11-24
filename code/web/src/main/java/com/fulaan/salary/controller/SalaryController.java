/**
 * @Title: SalaryController.java
 * @Package com.fulaan.salary.controller
 * @Description: TODO(用一句话描述该文件是做什么的)
 * @author yang.ling
 * @date 2015年7月20日 上午9:38:13
 * @version V1.0
 * @Copyright ycode Co.,Ltd.
 */
package com.fulaan.salary.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.QueryParam;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fulaan.annotation.SessionNeedless;
import com.fulaan.base.controller.BaseController;
import com.fulaan.salary.service.SalaryService;
import com.fulaan.user.service.UserService;
import com.fulaan.utils.ExportUtil;
import com.fulaan.utils.ImportExcelUtil;
import com.pojo.salary.SalaryDto;
import com.pojo.salary.SalaryItemDto;
import com.pojo.user.UserInfoDTO;
import com.sys.constants.Constant;
import com.sys.exceptions.IllegalParamException;
import com.sys.exceptions.ImportException;
import com.sys.utils.RespObj;

/**
 * @author yang.ling
 * @ClassName: SalaryController
 * @Description:
 * @date 2015年7月20日 上午9:38:13
 */
@Controller
@RequestMapping("/salary")
public class SalaryController extends BaseController {

    private static final Logger logger = Logger.getLogger(SalaryController.class);

    @Autowired
    private SalaryService salaryService;
    @Autowired
    private UserService userService;

    /**
     * 添加工资项目记录数据
     */
    @SessionNeedless
    @RequestMapping("/itemAdd")
    @ResponseBody
    public RespObj addItemInfo(@ModelAttribute("itemInfo") SalaryItemDto itemInfo) {

        RespObj result = new RespObj(Constant.FAILD_CODE);
        if (logger.isDebugEnabled()) {
            logger.debug("try to add salary item: " + itemInfo.toString());
        }
        itemInfo.setSchoolId(getSessionValue().getSchoolId());
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
     * 删除工资项目记录数据
     */
    @SessionNeedless
    @RequestMapping("/itemDelete")
    @ResponseBody
    public RespObj deleteItemInfo(@ModelAttribute("itemInfo") SalaryItemDto itemInfo) {

        RespObj result = new RespObj(Constant.FAILD_CODE);
        if (logger.isDebugEnabled()) {
            logger.debug("try to delete salary item: " + itemInfo.toString());
        }
        String itemId = salaryService.deleteSalaryItemInfo(itemInfo);
        if (itemId == null) {
            result.message = "删除工资项目失败，请稍后再试";
            if (logger.isDebugEnabled()) {
                logger.debug("add salary item failed: " + itemInfo.toString());
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("delete salary item success: " + itemInfo.toString());
        }
        result.code = Constant.SUCCESS_CODE;
        result.message = "删除工资项目成功";

        return result;
    }

//    /**
//     * 查询工资项目数据
//     */
//    @SessionNeedless
//    @RequestMapping("/itemList")
//    public ModelAndView retrieveItemInfo() {
//        ModelAndView mv = new ModelAndView();
//        ObjectId schoolId = new ObjectId(getSessionValue().getSchoolId());
//        List<SalaryItemDto> itemList = salaryService.findSalaryItemInfo(schoolId);
//        mv.addObject("salaryItemList", JSON.toJSONString(itemList));
//        mv.setViewName("salary/salaryItemList");
//        return mv;
//    }

    /**
     *
     * @return
     */
    @RequestMapping("/itemList")
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
     * 薪酬制表显示
     */
    @SessionNeedless
    @RequestMapping("/showTable")
    public ModelAndView showSalaryTable() {
        ModelAndView mv = new ModelAndView();
        List<String> yearList = new ArrayList<String>();
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        for (int i = year + 1; i > year - 10; i--) {
            yearList.add(i + Constant.EMPTY);
        }
        int month = cal.get(Calendar.MONTH) + 1;
        mv.addObject("yearList", yearList);
        mv.addObject("currYear", year);
        mv.addObject("currMonth", month < 10 ? "0" + month : month);
        mv.setViewName("salary/showTable");
        return mv;
    }

    /**
     * 获取某月份的当前工资次数
     *
     * @param year
     * @param month
     * @return
     */
    @SessionNeedless
    @RequestMapping("/getSalaryTimes")
    @ResponseBody
    public RespObj getSalaryTimes(Integer year, Integer month) {
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        respObj.setMessage(salaryService.getSalaryTimes(getSessionValue().getSchoolId(), year, month));
        return respObj;
    }

    /**
     * 生成导入工资的模板
     *
     * @param request
     * @param response
     * @throws IOException
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

    private boolean isNum(String str) {
        return str.matches("^(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
    }

    /**
     * 导入考试信息
     */
    @SessionNeedless
    @RequestMapping("/import")
    public void importSalary(HttpServletRequest request, HttpServletResponse response) throws IOException {
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        response.setContentType("text/html;charset=UTF-8");
        final ImportExcelUtil salaryImport = new ImportExcelUtil(0, 0, new ImportExcelUtil.IConvertRow() {
            @Override
            public Object convert(List<String> rowData, List<String> titles) throws ImportException {
                if (StringUtils.isEmpty(rowData.get(0))) {
                    throw new ImportException("模板文件第一列中存在空白内容，导入失败");
                }
                SalaryDto salaryDto = new SalaryDto();
                salaryDto.setMoney(new ArrayList<SalaryItemDto>(titles.size()));
                salaryDto.setId(rowData.get(0));
                salaryDto.setRemark(rowData.get(titles.size()-1));
                if (titles.size() > Constant.TWO) {
                    SalaryItemDto salaryItemDto;
                    for (int i = Constant.TWO; i < titles.size()-1; i++) {
                        //获取工资各项信息
                        salaryItemDto = new SalaryItemDto();
                        salaryItemDto.setItemName(StringUtils.trim(titles.get(i)));
                        if (!isNum(rowData.get(i))) {
                            throw new ImportException(String.format("【%s】的【%s】格式错误", rowData.get(1), titles.get(i)));
                        }
                        salaryItemDto.setM(Double.parseDouble(rowData.get(i)));
                        salaryDto.getMoney().add(salaryItemDto);
                    }
                }
                return salaryDto;
            }
        }, new ImportExcelUtil.ISaveData() {
            @Override
            public void save(List data) throws ImportException {
                for (SalaryDto dto : (List<SalaryDto>) data) {
                    salaryService.updateSalaryByImport(dto);
                }
            }
        });
        try {
            salaryImport.importData(request, getSessionValue().getId(), "salaryData");
            respObj.setCode(Constant.SUCCESS_CODE);
            response.getWriter().write(JSONObject.toJSON(respObj).toString());
        } catch (IllegalParamException e) {
            respObj.setMessage(e.getMessage());
            response.getWriter().write(JSONObject.toJSON(respObj).toString());
        } catch (ImportException e) {
            respObj.setMessage(e.getMessage());
            response.getWriter().write(JSONObject.toJSON(respObj).toString());
        } catch (Exception e) {
            respObj.setMessage("导入文件处理错误");
            response.getWriter().write(JSONObject.toJSON(respObj).toString());
        }

    }

    /**
     * 获取导出文件的名称
     *
     * @param request
     * @param fileName
     * @return
     * @throws UnsupportedEncodingException
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
     * 根据年份、月份、次数查询工资信息
     */
    @SessionNeedless
    @RequestMapping("/list")
    @ResponseBody
    public RespObj retrieveSalary(
            @QueryParam("year") int year,
            @QueryParam("month") int month,
            @QueryParam("num") int num) {
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        if (logger.isDebugEnabled()) {
            logger.debug("try to query salary (y,m,c): " + year + ":" + month + ":" + num);
        }
        respObj.setMessage(salaryService.findSalaryInfo(getSessionValue().getSchoolId(), year, month, num));
        return respObj;
    }

    /**
     * 修改某次工资的名称
     *
     * @return
     */
    @SessionNeedless
    @RequestMapping("/renameSalary")
    @ResponseBody
    public RespObj renameSalary(String name, int year, int month, int num) {
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        salaryService.renameSalary(getSessionValue().getSchoolId(), year, month, num, name);
        return respObj;
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
        salaryService.deleteSalary(getSessionValue().getSchoolId(), year, month, num);
        return respObj;
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
        int currNum = salaryService.countSalaryTimes(getSessionValue().getSchoolId(), year, month) + 1;
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

//    /**
//     * 查询工资记录数据
//     */
//    @SessionNeedless
//    @RequestMapping("/listSelf")
//    public ModelAndView retrieveSelfSalaryInfo() {
//        ModelAndView mv = new ModelAndView();
//        List<String> yearList = new ArrayList<String>();
//        Calendar cal = Calendar.getInstance();
//        int year = cal.get(Calendar.YEAR);
//        for (int i = year + 1; i > year - 10; i--) {
//            yearList.add(i + Constant.EMPTY);
//        }
//        int month = cal.get(Calendar.MONTH) + 1;
//        mv.addObject("yearList", yearList);
//        mv.addObject("currYear", year);
//        mv.addObject("currMonth", month < 10 ? "0" + month : month);
//        mv.setViewName("salary/viewSalary");
//        return mv;
//    }

    /**
     *
     * @return
     */
    @RequestMapping("/listSelf")
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
     * 获取区间工资信息
     *
     * @param begin
     * @param end
     * @return
     */
    @SessionNeedless
    @RequestMapping("/loadSalaryDetail")
    @ResponseBody
    public RespObj loadSalaryDetail(int begin, int end) {
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        respObj.setMessage(salaryService.findSalaryInfo(getSessionValue().getId(), begin, end));
        return respObj;
    }

}
