package com.fulaan.equipment.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fulaan.annotation.SessionNeedless;
import com.fulaan.base.controller.BaseController;
import com.fulaan.equipment.service.EquipmentService;
import com.fulaan.utils.ExportUtil;
import com.fulaan.utils.ImportExcelUtil;
import com.pojo.equipment.EquipmentEntry;
import com.sys.constants.Constant;
import com.sys.exceptions.IllegalParamException;
import com.sys.exceptions.ImportException;
import com.sys.utils.RespObj;

/**
 * 器材管理controller
 * 
 * @author cxy
 *
 */
@Controller
@RequestMapping("/equipment")
public class EquipmentController extends BaseController{
	@Autowired
	private EquipmentService equipmentService;
	
	/**
	 * 页面跳转
	 */
	@SessionNeedless
	@RequestMapping("/list")
	@ResponseBody
	public ModelAndView retrieveRepairInfo() {
		ModelAndView mv = new ModelAndView();
//		mv.addObject("message", JSON.toJSONString(messageMap));
		mv.setViewName("equipment/equipment");
		return mv;
	}
	
	/**
	 * 新增一条器材
	 * @param id
	 * @return
	 */
	@SessionNeedless
	@RequestMapping("/addEquipment")
	@ResponseBody
	public String addCompetition(@RequestParam(value = "equipmentNumber" , defaultValue="") String equipmentNumber,
								 @RequestParam(value = "equipmentName" , defaultValue="") String equipmentName,
								 @RequestParam(value = "equipmentSpecifications" , defaultValue="") String equipmentSpecifications,
								 @RequestParam(value = "equipmentOrgin" , defaultValue="") String equipmentOrgin,
								 @RequestParam(value = "equipmentBrand" , defaultValue="") String equipmentBrand,
								 @RequestParam(value = "equipmentClassificationId" , defaultValue="") String equipmentClassificationId,
								 @RequestParam(value = "equipmentUserName" , defaultValue="") String equipmentUserName){
		if("".equals(equipmentClassificationId)){
			return JSON.toJSONString(RespObj.FAILD); 
		}
		String schoolId = getSessionValue().getSchoolId();
		EquipmentEntry pe = new EquipmentEntry(new ObjectId(schoolId), equipmentNumber, equipmentName,
				equipmentSpecifications, equipmentOrgin, equipmentBrand, new ObjectId(equipmentClassificationId),equipmentUserName);
		equipmentService.addEquipmentEntry(pe); 
		return JSON.toJSONString(RespObj.SUCCESS); 
	}
	
	/**
	 * 删除一条器材
	 * @param id
	 * @return
	 */
	@SessionNeedless
	@RequestMapping("/delEquipment")
	@ResponseBody
	public String delEquipment(@RequestParam(value = "equipmentId" , defaultValue="") String equipmentId){
		if("".equals(equipmentId)){
			return JSON.toJSONString(RespObj.FAILD); 
		}
		equipmentService.deleteEquipment(new ObjectId(equipmentId));
		return JSON.toJSONString(RespObj.SUCCESS);  
	}
	
	/**
	 * 新增一条器材
	 * @param id
	 * @return
	 */
	@SessionNeedless
	@RequestMapping("/editEquipment")
	@ResponseBody
	public String editEquipment(@RequestParam(value = "equipmentId" , defaultValue="") String equipmentId,
								 @RequestParam(value = "equipmentNumber" , defaultValue="") String equipmentNumber,
								 @RequestParam(value = "equipmentName" , defaultValue="") String equipmentName,
								 @RequestParam(value = "equipmentSpecifications" , defaultValue="") String equipmentSpecifications,
								 @RequestParam(value = "equipmentOrgin" , defaultValue="") String equipmentOrgin,
								 @RequestParam(value = "equipmentBrand" , defaultValue="") String equipmentBrand,
								 @RequestParam(value = "equipmentUserName" , defaultValue="") String equipmentUserName){
		if("".equals(equipmentId)){
			return JSON.toJSONString(RespObj.FAILD); 
		}
		equipmentService.updateEquipment(new ObjectId(equipmentId), equipmentNumber,
				equipmentName, equipmentSpecifications, equipmentOrgin, equipmentBrand,equipmentUserName);
		return JSON.toJSONString(RespObj.SUCCESS);  
	}
	
	/**
	 * 根据器材分类查询器材
	 * @param id
	 * @return
	 */
	@SessionNeedless
	@RequestMapping("/queryEquipment")
	@ResponseBody
	public String queryEquipment(@RequestParam(value = "equipmentClassificationId" , defaultValue="") String equipmentClassificationId,
								 @RequestParam(value = "pageNo" , defaultValue="1") int pageNo,
								 @RequestParam(value = "pageSize" , defaultValue="10") int pageSize){ 
		if("".equals(equipmentClassificationId)){
			return JSON.toJSONString(RespObj.FAILD); 
		}
		String schoolId = getSessionValue().getSchoolId();
		List<EquipmentEntry> srcList = equipmentService.queryPropertiesBySchoolIdAndEquipmentClassificationId(new ObjectId(schoolId), new ObjectId(equipmentClassificationId));
		List<Map<String,Object>> fomartList = new ArrayList<Map<String,Object>>();
		for(EquipmentEntry pe : srcList){
			Map<String,Object> m = new HashMap<String,Object>();
			m.put("equipmentId", pe.getID().toString());
			m.put("equipmentNumber", pe.getEquipmentNumber());
			m.put("equipmentName", pe.getEquipmentName());
			m.put("equipmentSpecifications", pe.getEquipmentSpecifications());
			m.put("equipmentOrgin", pe.getEquipmentOrgin());
			m.put("equipmentBrand", pe.getEquipmentBrand());
			m.put("equipmentClassificationId", pe.getEquipmentClassificationId().toString());
			m.put("equipmentUserName", pe.getEquipmentUserName());
			fomartList.add(m);
		}
		List pagedList = getListByPage(fomartList,pageNo,pageSize);
		Map<String,Object> pageMap = new HashMap<String,Object>();
		pageMap.put("total",fomartList.size());
		pageMap.put("pageNo",pageNo); 
		Map<String,Object> resultMap = new HashMap<String,Object>(); 
		resultMap.put("datas", pagedList);
		resultMap.put("pagejson",pageMap);
		return JSON.toJSONString(resultMap); 
	}
	
	/**
	 * 根据分页信息进行数据筛选
	 * @param src
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	private List<? extends Object> getListByPage(List<? extends Object> src,int pageNo,int pageSize){
		List<Object> list = new ArrayList<Object>();
		int startIndex = (pageNo - 1) * pageSize;
		int endIndex = (pageNo * pageSize) - 1;
		if(src.size() < pageNo * pageSize){
			endIndex = src.size() - 1;
		}
		for(int i=startIndex;i<endIndex + 1;i++){
			list.add(src.get(i));
		}
		return list;
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
	 * 导出模板

	 */
	@SessionNeedless
	@RequestMapping("/exportModel")
	@ResponseBody
	public String exportModel(HttpServletRequest request, HttpServletResponse response)  throws IOException{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		//查询
		
		response.setContentType("application/octet-stream;charset=UTF-8");
        response.addHeader("Pargam", "no-cache");
        response.addHeader("Cache-Control", "no-cache");
        ExportUtil util = null;
        try {
            util = new ExportUtil();
            List<String> datas = new ArrayList<String>();
            
            
            datas.add("器材标号");
            datas.add("器材名称");
            datas.add("器材产地");
            datas.add("器材品牌");
            datas.add("器材规格");
            datas.add("使用人员");
            util.addTitle(datas.toArray()); 
            
            util.setFileName(String.format("%s_%s", sdf.format(new Date()), "_器材登记导出模板.xlsx"));
            response.setHeader("Content-Disposition", "attachment;filename=" + getFileName(request, util.getFileName()));
            util.getBook().write(response.getOutputStream());
        } finally {
            if (util != null) {
                util.destroy();
            }
        }
		return null;
	}
	
	private class ImportEquipment implements ImportExcelUtil.ISaveData {
		private ObjectId equipmentClassificationId;
        public ImportEquipment(ObjectId equipmentClassificationId) {
        	super();
        	this.equipmentClassificationId = equipmentClassificationId; 
        }

        @Override
        public void save(List data) throws ImportException {
//            scoreService.updateByImport(data, examId, UserRole.isHeadmaster(getSessionValue().getUserRole())
//                    || UserRole.isManager(getSessionValue().getUserRole()));
        	//这里写入数据库
        	ObjectId schoolId = new ObjectId(getSessionValue().getSchoolId());
        	for(List<String> l : (List<List<String>>)data){
        		EquipmentEntry pe = new EquipmentEntry(schoolId, l.get(0), l.get(1), l.get(4), l.get(2), l.get(3), equipmentClassificationId,l.get(5));
        		equipmentService.addEquipmentEntry(pe); 
        	}
        }
    }

    /**
     * 导入器材信息
     */
    @SessionNeedless
    @RequestMapping("/import")
    public void importEquipment(HttpServletRequest request, HttpServletResponse response) throws IOException {
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        response.setContentType("text/html;charset=UTF-8");
        if (StringUtils.isEmpty(request.getParameter("equipmentClassificationId"))) {
            respObj.setMessage("导入信息错误！");
            response.getWriter().write(JSONObject.toJSON(respObj).toString());
            return;
        }
        ObjectId equipmentClassificationId = new ObjectId(request.getParameter("equipmentClassificationId"));
        final ImportExcelUtil equipmentImport = new ImportExcelUtil(0, 0, new ImportExcelUtil.IConvertRow() {
            @Override
            public Object convert(List<String> rowData, List<String> titles) {
                return rowData;
            }
        }, new ImportEquipment(equipmentClassificationId));
        try {
        	equipmentImport.importData(request, getSessionValue().getId(), "scoreData");
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
}
