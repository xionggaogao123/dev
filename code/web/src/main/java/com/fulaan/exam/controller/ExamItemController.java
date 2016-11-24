package com.fulaan.exam.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fulaan.annotation.SessionNeedless;
import com.fulaan.base.controller.BaseController;
import com.fulaan.exam.service.ExamItemScoreService;
import com.fulaan.exam.service.ExamItemService;
import com.fulaan.exam.service.ExamService;
import com.fulaan.utils.ExportUtil;
import com.fulaan.utils.ImportExcelUtil;
import com.pojo.exam.ExamDTO;
import com.pojo.exam.ExamItemEntry;
import com.pojo.exam.ExamSubjectEntry;
import com.sys.constants.Constant;
import com.sys.exceptions.IllegalParamException;
import com.sys.exceptions.ImportException;
import com.sys.utils.RespObj;

/**
 * 考试小分controller
 * 
 * @author cxy
 *
 */
@Controller
@RequestMapping("/examItem")
public class ExamItemController extends BaseController{
	@Autowired
	private ExamItemService examItemService;
	
	@Autowired
	private ExamItemScoreService examItemScoreService;
	
	@Autowired
	private ExamService examService;
	
	
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
	@RequestMapping("/exportExamItemModel")
	@ResponseBody
	public String exportModel(HttpServletRequest request, HttpServletResponse response)  throws IOException{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		RespObj respObj = new RespObj(Constant.FAILD_CODE);
		//查询
		if (StringUtils.isEmpty(request.getParameter("examId"))|| StringUtils.isEmpty(request.getParameter("subjectId"))) {
			respObj.setMessage("导出信息错误！");
			return JSONObject.toJSON(respObj).toString();
		}
		ObjectId examId = new ObjectId(request.getParameter("examId"));
		ObjectId subjectId = new ObjectId(request.getParameter("subjectId"));
		ExamDTO examDTO = examService.loadExam(examId.toString());
		ExamSubjectEntry examSubjectEntry = examService.loadExamSubject(examId.toString(), subjectId.toString());
		response.setContentType("application/octet-stream;charset=UTF-8");
        response.addHeader("Pargam", "no-cache");
        response.addHeader("Cache-Control", "no-cache");
        ExportUtil util = null;
        try {
            util = new ExportUtil();
            List<String> datas = new ArrayList<String>();
            
             
            datas.add("题号");
            datas.add("题目名称");
            datas.add("分值");
            util.addTitle(datas.toArray()); 
            datas.clear();
            datas.add("注意事项:");
            util.appendRow(datas.toArray());
            datas.clear();
            datas.add("1.请将所有单元格格式默认设置为【文本】。");
            util.appendRow(datas.toArray());
            datas.clear();
            datas.add("2.请按导出窗口中图片所示例的格式进行设置(ID为唯一标识，不能为空，且必须按照示例格式)。");
            util.appendRow(datas.toArray());
            datas.clear();
            datas.add("3.所有单元格请不要输入空格，输入的符号统一使用英文输入法中符号。");
            util.appendRow(datas.toArray());
            datas.clear();
            datas.add("4.在开始输入前，请清空本注意事项，除第一行蓝色标题外所有单元格清空后，再进行录入。");
            util.appendRow(datas.toArray());
            datas.clear();
            String fileNameSuffix = "_" + examDTO.getName() + "_" + examSubjectEntry.getSubjectName()+"_小分设置模板.xlsx";
			util.setFileName(String.format("%s_%s", sdf.format(new Date()),fileNameSuffix));
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
	 * 导入数据内部类
	 * @author cxy
	 *
	 */
	private class ImportProperty implements ImportExcelUtil.ISaveData {
		private final ObjectId examId;
		private final ObjectId subjectId;
        public ImportProperty(ObjectId examId,ObjectId subjectId) {
        	super();
        	this.examId = examId; 
        	this.subjectId = subjectId; 
        }
        /**
         * 存储方法
         */
        @Override
        public void save(List data) throws ImportException {
        	try {
        		//排序
            	sortWithId((List<Map<String,String>>)data);
            	//分级
            	List<List<Map<String,String>>> leveledList = levelItem((List<Map<String,String>>)data);
            	//递归验证数据
            	recursionCheckExamItemConfig(leveledList,0);
            	if(leveledList.size() == 0){
            		throw new ImportException("数据内容错误！分组数据为空！");
            	}
            	//删除之前数据
            	examItemService.deleteAllExamItem(examId, subjectId);
            	//递归保存
            	List<Map<String,String>> rootList = leveledList.get(0);
            	for(int i =0;i<rootList.size();i++){
            		Map<String,String> rootMap = rootList.get(i);
            		ExamItemEntry e = new ExamItemEntry(examId, subjectId, rootMap.get("id"), rootMap.get("name"),
    						Double.parseDouble(rootMap.get("score")), "0",1);
            		examItemService.addExamItemEntry(e);
            		recursionSaveExamItemConfig(rootMap,leveledList,1);
            	}
			} catch (ImportException e) {
				e.printStackTrace();
				throw e;
			}
        	
        	
        	
        }
        
        /**
    	 * 根据id进行排序
    	 * @param srcList
    	 * @return
    	 */
    	private List<Map<String,String>> sortWithId(List<Map<String,String>> srcList)throws ImportException{
    		Comparator<Map<String,String>> comparator = new Comparator<Map<String,String>>() {
                @Override
                public int compare(Map<String,String> m1, Map<String,String> m2) {
                    String s1 = m1.get("id").toString();
                    String s2 = m2.get("id").toString();
                	if(s1.compareTo(s2) > 0){
                		return 1;
                	}
                	if(s1.compareTo(s2) < 0){
                		return -1;
                	}
                	return 0;
                }
            };
            Collections.sort(srcList, comparator);
    		return srcList;
    	}
        
        /**
    	 * 根据层级进行分组
    	 * @param srcList
    	 * @return
    	 */
    	private List<List<Map<String,String>>> levelItem(List<Map<String,String>> srcList)throws ImportException{
    		int levelNum = 1;
    		char c = '-';
    		for(Map<String,String> m : srcList){
    			int num = 0;
    			if(m.get("id") == null || "".equals(m.get("id"))){
    				throw new ImportException("第  " + (srcList.indexOf(m) + 1) + "行ID填写出错！");
    			}
    			char[] chars = m.get("id").toCharArray();
    			for(int i = 0; i < chars.length; i++){
    			    if(c == chars[i]){
    			       num++;
    			    }
    			}
    			if(levelNum < num + 1){
    				levelNum = num + 1;
    			}
    		}
    		List<List<Map<String,String>>> leveledList = new ArrayList<List<Map<String,String>>>(levelNum);
    		for(int i=0;i<levelNum;i++){
    			leveledList.add(new ArrayList<Map<String,String>>());
    		}
    		for(Map<String,String> m : srcList){
    			int num = 0;
    			if(m.get("id") == null || "".equals(m.get("id"))){
    				throw new ImportException("第  " + (srcList.indexOf(m) + 1) + "行ID填写出错！");
    			}
    			char[] chars = m.get("id").toCharArray();
    			for(int i = 0; i < chars.length; i++){
    			    if(c == chars[i]){
    			       num++;
    			    }
    			}
    			leveledList.get(num).add(m);
    		}
    		return leveledList;
    	}
    	
    	/**
    	 * 递归验证
    	 * @param list
    	 * @throws ImportException
    	 */
    	private void recursionCheckExamItemConfig(List<List<Map<String,String>>> list,int index)throws ImportException{
    		if(list.size() == 0 ){
    			throw new ImportException("文件数据为空！请选择正确的文件上传或填写完整！"); 
    		}
    		
    		
    		List<Map<String,String>> parentList = list.get(index);
    		if(index == 0){
    			ExamSubjectEntry e = examService.loadExamSubject(examId.toString(), subjectId.toString());
    			double pFullScore = e.getFullMarks() * 1L;
    			double subFullScoreSum = 0L;
    			for(Map<String,String> m : parentList){
        			subFullScoreSum += Double.parseDouble(m.get("score"));
        		}
    			if(pFullScore != subFullScoreSum){
    				throw new ImportException("所有大题总分与考试总分不符！");
    			}
    		}
    		
    		
    		
    		if(list.size() < 2 || (list.size()  > (index + 2))){
    			return;
    		}
    		
    		List<Map<String,String>> childList = list.get(index + 1);
    		for(Map<String,String> m : parentList){
    			double pFullScore = Double.parseDouble(m.get("score"));
    			double subFullScoreSum = 0L;
    			for(Map<String,String> subM : childList){
    				if(subM.get("id").indexOf(m.get("id")) == 0){ 
    					subFullScoreSum += Double.parseDouble(subM.get("score"));
    				}
    			}
    			if(pFullScore != subFullScoreSum){
    				throw new ImportException("小题题目分数总和与大题不符，问题大题题号 : " + m.get("id"));
    			}
    		}
    		recursionCheckExamItemConfig(list , index + 1);
    	}
    	
    	/**
    	 * 递归存储考试小分分类数据
    	 * @param list
    	 * @throws ImportException
    	 */
    	private void recursionSaveExamItemConfig(Map<String,String> parentMap,List<List<Map<String,String>>> srcList,int index)throws ImportException{
    		if(srcList.size() == index ){
    			return;
    		}
    		List<Map<String,String>> childList = srcList.get(index);
    		boolean parentIsLeaf = true;
    		for(Map<String,String> subM : childList){
    			if(subM.get("id").indexOf(parentMap.get("id")) == 0){
    				ExamItemEntry e = new ExamItemEntry(examId, subjectId, subM.get("id"), subM.get("name"),
    																Double.parseDouble(subM.get("score")), parentMap.get("id"),1);
    				examItemService.addExamItemEntry(e);
    				recursionSaveExamItemConfig(subM ,srcList, index + 1);
    				parentIsLeaf = false;
    			}
    		}
    		if(!parentIsLeaf){
    			examItemService.changeToParentByIds(examId, subjectId, parentMap.get("id"));
    		}
    	}
        
        
        
    }

    /**
     * 导入考试小分分类信息
     */
    @SessionNeedless
    @RequestMapping("/importExamItem")
    public void importProperty(HttpServletRequest request, HttpServletResponse response) throws IOException {
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        response.setContentType("text/html;charset=UTF-8");
        if (StringUtils.isEmpty(request.getParameter("examId")) || StringUtils.isEmpty(request.getParameter("subjectId"))) {
            respObj.setMessage("导入信息错误！");
            response.getWriter().write(JSONObject.toJSON(respObj).toString());
            return;
        }
        ObjectId examId = new ObjectId(request.getParameter("examId"));
        ObjectId subjectId = new ObjectId(request.getParameter("subjectId"));
        final ImportExcelUtil propertyImport = new ImportExcelUtil(0, 0, new ImportExcelUtil.IConvertRow() {
            @Override
            public Object convert(List<String> rowData, List<String> titles) {
            	Map<String,String> map = new HashMap<String,String>();
            	String fomartId = rowData.get(0);
            	if(fomartId.indexOf(".") > -1){
            		fomartId = fomartId.split("\\.")[0];
            	}
                map.put("id", fomartId);
                map.put("name", rowData.get(1));
                map.put("score", rowData.get(2));
                return map;
            }
        }, new ImportProperty(examId,subjectId));
        try {
        	propertyImport.importData(request, getSessionValue().getId(), "examItemData");
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
	 * 查询树数据
	 * @param id
	 * @return
	 */
	@SessionNeedless
	@RequestMapping("/queryExamItemDetail")
	@ResponseBody
	public String delProperty(@RequestParam(value = "examId" , defaultValue="") String examId,
							  @RequestParam(value = "subjectId" , defaultValue="") String subjectId){
		if("".equals(examId) || "".equals(subjectId)){
			return JSON.toJSONString(RespObj.FAILD); 
		}
		List<ExamItemEntry> list = examItemService.queryExamItemEntriesByIds(new ObjectId(examId), new ObjectId(subjectId)); 
		Map<String,Object> resultMap = new HashMap<String,Object>();
		List<Map<String,String>> treeList = new ArrayList<Map<String,String>>(); 
		for(ExamItemEntry e : list){
			Map<String,String> m = new HashMap<String,String>();
			m.put("open", "true");
			m.put("id", e.getItemId());
			m.put("pId", e.getParentId());
			m.put("name", "【" + e.getItemId() + "】【" + (e.getName()==null?"":e.getName()) + "】【" + e.getScore() + "】" );
			treeList.add(m);
		}
		resultMap.put("treeData", treeList);
		return JSON.toJSONString(resultMap);  
	}
}
