package com.fulaan.property.controller;

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
import com.fulaan.property.service.PropertyService;
import com.fulaan.utils.ExportUtil;
import com.fulaan.utils.ImportExcelUtil;
import com.pojo.exam.SubjectScoreDTO;
import com.pojo.property.PropertyEntry;
import com.sys.constants.Constant;
import com.sys.exceptions.IllegalParamException;
import com.sys.exceptions.ImportException;
import com.sys.utils.RespObj;

/**
 * 校产管理controller
 * 
 * @author cxy
 *
 */
@Controller
@RequestMapping("/property")
public class PropertyController extends BaseController{
	@Autowired
	private PropertyService propertyService;
	
	/**
	 * 页面跳转
	 */
	@SessionNeedless
	@RequestMapping("/list")
	@ResponseBody
	public ModelAndView retrieveRepairInfo() {
		ModelAndView mv = new ModelAndView();
//		mv.addObject("message", JSON.toJSONString(messageMap));
		mv.setViewName("property/xiaochandengji");
		return mv;
	}
	
	/**
	 * 新增一条校产
	 * @param id
	 * @return
	 */
	@SessionNeedless
	@RequestMapping("/addProperty")
	@ResponseBody
	public String addCompetition(@RequestParam(value = "propertyNumber" , defaultValue="") String propertyNumber,
								 @RequestParam(value = "propertyName" , defaultValue="") String propertyName,
								 @RequestParam(value = "propertySpecifications" , defaultValue="") String propertySpecifications,
								 @RequestParam(value = "propertyOrgin" , defaultValue="") String propertyOrgin,
								 @RequestParam(value = "propertyBrand" , defaultValue="") String propertyBrand,
								 @RequestParam(value = "propertyClassificationId" , defaultValue="") String propertyClassificationId){
		if("".equals(propertyClassificationId)){
			return JSON.toJSONString(RespObj.FAILD); 
		}
		String schoolId = getSessionValue().getSchoolId();
		PropertyEntry pe = new PropertyEntry(new ObjectId(schoolId), propertyNumber, propertyName, propertySpecifications, propertyOrgin, propertyBrand, new ObjectId(propertyClassificationId));
		propertyService.addPropertyEntry(pe); 
		return JSON.toJSONString(RespObj.SUCCESS); 
	}
	
	/**
	 * 删除一条校产
	 * @param id
	 * @return
	 */
	@SessionNeedless
	@RequestMapping("/delProperty")
	@ResponseBody
	public String delProperty(@RequestParam(value = "propertyId" , defaultValue="") String propertyId){
		if("".equals(propertyId)){
			return JSON.toJSONString(RespObj.FAILD); 
		}
		propertyService.deleteProperty(new ObjectId(propertyId));
		return JSON.toJSONString(RespObj.SUCCESS);  
	}
	
	/**
	 * 新增一条校产
	 * @param id
	 * @return
	 */
	@SessionNeedless
	@RequestMapping("/editProperty")
	@ResponseBody
	public String editProperty(@RequestParam(value = "propertyId" , defaultValue="") String propertyId,
								 @RequestParam(value = "propertyNumber" , defaultValue="") String propertyNumber,
								 @RequestParam(value = "propertyName" , defaultValue="") String propertyName,
								 @RequestParam(value = "propertySpecifications" , defaultValue="") String propertySpecifications,
								 @RequestParam(value = "propertyOrgin" , defaultValue="") String propertyOrgin,
								 @RequestParam(value = "propertyBrand" , defaultValue="") String propertyBrand){
		if("".equals(propertyId)){
			return JSON.toJSONString(RespObj.FAILD); 
		}
		propertyService.updateProperty(new ObjectId(propertyId), propertyNumber, propertyName, propertySpecifications, propertyOrgin, propertyBrand);
		return JSON.toJSONString(RespObj.SUCCESS);  
	}
	
	/**
	 * 根据校产分类查询校产
	 * @param id
	 * @return
	 */
	@SessionNeedless
	@RequestMapping("/queryProperty")
	@ResponseBody
	public String queryProperty(@RequestParam(value = "propertyClassificationId" , defaultValue="") String propertyClassificationId,
								@RequestParam(value = "pageNo" , defaultValue="1") int pageNo,
								@RequestParam(value = "pageSize" , defaultValue="10") int pageSize){
		if("".equals(propertyClassificationId)){
			return JSON.toJSONString(RespObj.FAILD); 
		}
		String schoolId = getSessionValue().getSchoolId();
		List<PropertyEntry> srcList = propertyService.queryPropertiesBySchoolIdAndPropertyClassificationId(new ObjectId(schoolId), new ObjectId(propertyClassificationId));
		List<Map<String,Object>> fomartList = new ArrayList<Map<String,Object>>();
		for(PropertyEntry pe : srcList){
			Map<String,Object> m = new HashMap<String,Object>();
			m.put("propertyId", pe.getID().toString());
			m.put("propertyNumber", pe.getPropertyNumber());
			m.put("propertyName", pe.getPropertyName());
			m.put("propertySpecifications", pe.getPropertySpecifications());
			m.put("propertyOrgin", pe.getPropertyOrgin());
			m.put("propertyBrand", pe.getPropertyBrand());
			m.put("propertyClassificationId", pe.getPropertyClassificationId().toString());
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
            
            
            datas.add("校产标号");
            datas.add("校产名称");
            datas.add("校产产地");
            datas.add("校产品牌");
            datas.add("校产规格");
            util.addTitle(datas.toArray()); 
            
            util.setFileName(String.format("%s_%s", sdf.format(new Date()), "_校产登记导出模板.xlsx"));
            response.setHeader("Content-Disposition", "attachment;filename=" + getFileName(request, util.getFileName()));
            util.getBook().write(response.getOutputStream());
        } finally {
            if (util != null) {
                util.destroy();
            }
        }
		return null;
	}
	 
	private class ImportProperty implements ImportExcelUtil.ISaveData {
		private ObjectId propertyClassificationId;
        public ImportProperty(ObjectId propertyClassificationId) {
        	super();
        	this.propertyClassificationId = propertyClassificationId; 
        }

        @Override
        public void save(List data) throws ImportException {
//            scoreService.updateByImport(data, examId, UserRole.isHeadmaster(getSessionValue().getUserRole())
//                    || UserRole.isManager(getSessionValue().getUserRole()));
        	//这里写入数据库
        	ObjectId schoolId = new ObjectId(getSessionValue().getSchoolId());
        	for(List<String> l : (List<List<String>>)data){
        		PropertyEntry pe = new PropertyEntry(schoolId, l.get(0), l.get(1), l.get(4), l.get(2), l.get(3), propertyClassificationId);
        		propertyService.addPropertyEntry(pe); 
        	}
        }
    }

    /**
     * 导入校产信息
     */
    @SessionNeedless
    @RequestMapping("/import")
    public void importProperty(HttpServletRequest request, HttpServletResponse response) throws IOException {
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        response.setContentType("text/html;charset=UTF-8");
        if (StringUtils.isEmpty(request.getParameter("propertyClassificationId"))) {
            respObj.setMessage("导入信息错误！");
            response.getWriter().write(JSONObject.toJSON(respObj).toString());
            return;
        }
        ObjectId propertyClassificationId = new ObjectId(request.getParameter("propertyClassificationId"));
        final ImportExcelUtil propertyImport = new ImportExcelUtil(0, 0, new ImportExcelUtil.IConvertRow() {
            @Override
            public Object convert(List<String> rowData, List<String> titles) {
//            	Map<String,String> map = new HashMap<String,String>();
//                map.put("propertyNumber", rowData.get(0));
//                map.put("propertyName", rowData.get(1));
//                map.put("propertySpecifications", rowData.get(2));
//                map.put("propertyOrgin", rowData.get(3));
//                map.put("propertyBrand", rowData.get(4));
                return rowData;
            }
        }, new ImportProperty(propertyClassificationId));
        try {
        	propertyImport.importData(request, getSessionValue().getId(), "scoreData");
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
