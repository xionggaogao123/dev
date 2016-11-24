package com.fulaan.exam.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.fulaan.annotation.SessionNeedless;
import com.fulaan.base.controller.BaseController;
import com.fulaan.exam.service.ExamItemScoreService;
import com.fulaan.exam.service.ExamItemService;
import com.fulaan.exam.service.ExamService;
import com.fulaan.myclass.service.ClassService;
import com.fulaan.school.service.SchoolService;
import com.fulaan.user.service.UserService;
import com.fulaan.utils.ExportUtil;
import com.fulaan.utils.ImportExcelUtil;
import com.mongodb.DBObject;
import com.pojo.app.IdNameValuePairDTO;
import com.pojo.exam.ExamDTO;
import com.pojo.exam.ExamItemEntry;
import com.pojo.exam.ExamItemScoreEntry;
import com.pojo.exam.ExamSubjectEntry;
import com.pojo.school.ClassEntry;
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
@RequestMapping("/examItemScore")
public class ExamItemScoreController extends BaseController{
	@Autowired
	private ExamItemService examItemService;

	@Autowired
	private ExamItemScoreService examItemScoreService;

	@Autowired
	private ExamService examService;
	
	@Autowired
	private ClassService classService;
	
	@Autowired
	private SchoolService schoolService;
	
	@Autowired
	private UserService userService;

	/**
	 * 获取导出文件的名称
	 *
	 * @param request
	 * @param fileName
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private String getFileName(HttpServletRequest request, String fileName)
			throws UnsupportedEncodingException {
		String agent = request.getHeader("User-Agent");
		if (agent != null && agent.toLowerCase().indexOf("firefox") >= 0) {
			fileName = new String(fileName.getBytes(Constant.UTF_8),
					Constant.ISO);
		} else {
			fileName = java.net.URLEncoder.encode(fileName, Constant.UTF_8);
		}
		return fileName;
	}

	/**
	 * 导出模板
	 */
	@SessionNeedless
	@RequestMapping("/exportExamItemScoreModel")
	@ResponseBody
	public String exportModel(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		RespObj respObj = new RespObj(Constant.FAILD_CODE);
		// 查询
		if (StringUtils.isEmpty(request.getParameter("examId"))|| StringUtils.isEmpty(request.getParameter("subjectId"))) {
			respObj.setMessage("导出信息错误！");
			return JSONObject.toJSON(respObj).toString();
		}
		ObjectId examId = new ObjectId(request.getParameter("examId"));
		ObjectId subjectId = new ObjectId(request.getParameter("subjectId"));
		ExamDTO examDTO = examService.loadExam(examId.toString());
		ExamSubjectEntry examSubjectEntry = examService.loadExamSubject(examId.toString(), subjectId.toString());
		List<Map<String,String>> allExamScoreLeafs = getAllExamItems(examId,subjectId);

		response.setContentType("application/octet-stream;charset=UTF-8");
		response.addHeader("Pargam", "no-cache");
		response.addHeader("Cache-Control", "no-cache");
		ExportUtil util = null;
		try {
			util = new ExportUtil();
			List<String> datas = new ArrayList<String>();
			// 增加导入相关信息，以提示用户
			String examName = examDTO.getName();
			String subjectName = examSubjectEntry.getSubjectName();
			datas.add(examId.toString());
			datas.add(examName.toString());
			datas.add(subjectId.toString());
			datas.add(subjectName.toString());
			for(Map<String,String> m : allExamScoreLeafs){
				datas.add(m.get("name")==null?"":m.get("name") + "(" + m.get("score") + ")");
			}
			util.appendRow(datas.toArray());
			datas.clear();

			datas.add("学生ID");
			datas.add("姓名");
			datas.add("年级");
			datas.add("班级");
			for(Map<String,String> m : allExamScoreLeafs){
				datas.add(m.get("id"));
			}
			util.addTitle(datas.toArray());
			datas.clear();
			

			String gradeId = examDTO.getGradeId();
			String gradeName = examDTO.getGradeName();
			
			List<Map<String,String>> allStudents = new ArrayList<Map<String,String>>();
			

			Map<IdNameValuePairDTO, Set<IdNameValuePairDTO>> studentMap = userService.getStudentOrParentMap(new ObjectId(getSessionValue().getSchoolId()), 1);
			Set<Entry<IdNameValuePairDTO, Set<IdNameValuePairDTO>>> outSet = studentMap.entrySet();
			Iterator<Entry<IdNameValuePairDTO, Set<IdNameValuePairDTO>>> outIt = outSet.iterator();
			while (outIt.hasNext()) {
				Entry<IdNameValuePairDTO, Set<IdNameValuePairDTO>> outEntry = outIt.next();
				ClassEntry ce = null;
				
				ce = classService.getClassEntryById(new ObjectId(outEntry.getKey().getIdStr()), Constant.FIELDS);
				
				if (ce != null && ce.getGradeId() != null && gradeId.equals(ce.getGradeId() + "")) {
					
					Set<IdNameValuePairDTO> inSet = outEntry.getValue();
					Iterator<IdNameValuePairDTO> inIt = inSet.iterator();
					while (inIt.hasNext()) {
						IdNameValuePairDTO ivp = inIt.next();
						Map<String,String> m = new HashMap<String,String>();
						m.put("id",ivp.getIdStr());
						m.put("sName",ivp.getValue().toString());
						m.put("gName",gradeName);
						m.put("cName",ce.getName());
						allStudents.add(m);
					}
					
				}
			}

			try {
				sortWithKeyString(allStudents,"cName");
			} catch (Exception e) {
				e.printStackTrace();
				respObj.setMessage("导出信息错误！");
				return JSONObject.toJSON(respObj).toString();
			}
			
			for(Map<String,String> m : allStudents){
				datas.add(m.get("id"));
				datas.add(m.get("sName"));
				datas.add(m.get("gName"));
				datas.add(m.get("cName"));
				util.appendRow(datas.toArray());
				datas.clear();
			}
			
			String fileNameSuffix = "_" + examDTO.getName() + "_" + examSubjectEntry.getSubjectName()+"_小分分数录入模板.xlsx";
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
	 * 根据考试Id和科目Id获取相应的所有子叶节点的考试分类信息，作为导出导入之用
	 * @param examId
	 * @param subjectId
	 * @return
	 */
	private List<Map<String,String>> getAllExamItems(ObjectId examId,ObjectId subjectId){
		List<Map<String,String>> resultList = new ArrayList<Map<String,String>>();
		List<ExamItemEntry> entryList = examItemService.queryExamItemEntriesByIds(examId, subjectId);
		for(ExamItemEntry e : entryList){
			Map<String,String> m = new HashMap<String,String>();
			m.put("id", e.getItemId());
			m.put("name", e.getName());
			m.put("score",e.getScore() + "");
			resultList.add(m);
		}
		
		
		
		return resultList;
	}
	
	 /**
	 * 根据id进行排序
	 * @param srcList
	 * @return
	 */
	private List<Map<String,String>> sortWithKeyString(List<Map<String,String>> srcList,final String sortKey)throws ImportException{
		Comparator<Map<String,String>> comparator = new Comparator<Map<String,String>>() {
            @Override
            public int compare(Map<String,String> m1, Map<String,String> m2) {
                String s1 = m1.get(sortKey).toString();
                String s2 = m2.get(sortKey).toString();
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
     * 导入考试小分分类信息
     */
    @SessionNeedless
    @RequestMapping("/importExamItemScore")
    public void importProperty(HttpServletRequest request, HttpServletResponse response) throws IOException {
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        response.setContentType("text/html;charset=UTF-8");
        if (StringUtils.isEmpty(request.getParameter("examId")) || StringUtils.isEmpty(request.getParameter("subjectId"))) {
            respObj.setMessage("导入信息错误！");
            response.getWriter().write(JSONObject.toJSON(respObj).toString());
            return;
        }
        final ObjectId examId = new ObjectId(request.getParameter("examId"));
        final ObjectId subjectId = new ObjectId(request.getParameter("subjectId"));
        final List<Map<String,String>> allExamItems = getAllExamItems(examId, subjectId);
        final ImportExcelUtil propertyImport = new ImportExcelUtil(0, 1, new ImportExcelUtil.IConvertRow() {
            @Override
            public Object convert(List<String> rowData, List<String> titles) throws ImportException{
            	if(!("学生ID".equals(titles.get(0)) && "姓名".equals(titles.get(1)) && "年级".equals(titles.get(2)) && "班级".equals(titles.get(3)))){
            		 throw new ImportException("导入的文件错误、破损，请重新选择!");
            	}
            	Map<String,Object> map = new HashMap<String,Object>();
                map.put("userId", rowData.get(0));
                map.put("userName", rowData.get(1));
                map.put("gradeName", rowData.get(2));
                map.put("className", rowData.get(3));
                List<Map<String,String>> scores = new ArrayList<Map<String,String>>();
                for(int i=0;i<allExamItems.size();i++){
                	Map<String,String> m = new HashMap<String,String>();
                	m.put("itemId",titles.get(4 + i));
                	m.put("itemScore",rowData.get(4 + i)==null||"".equals(rowData.get(4 + i))?"0.0":rowData.get(4 + i));
                	scores.add(m);	
                }
                map.put("scoreList",scores);
                return map;
            }
        }, new ImportProperty(examId,subjectId,allExamItems));
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
	 * 导入数据内部类
	 * @author cxy
	 *
	 */
	private class ImportProperty implements ImportExcelUtil.ISaveData {
		private final ObjectId examId;
		private final ObjectId subjectId;
		private final List<Map<String,String>> items;
        public ImportProperty(ObjectId examId,ObjectId subjectId,List<Map<String,String>> allExamItems) {
        	super();
        	this.examId = examId; 
        	this.subjectId = subjectId; 
        	this.items = allExamItems;
        }
        /**
         * 存储方法
         */
        @Override
        public void save(List data) throws ImportException {

//        	sortWithId((List<Map<String,String>>)data);
//        	List<List<Map<String,String>>> leveledList = levelItem((List<Map<String,String>>)data);
//        	recursionCheckExamItemConfig(leveledList,0);
//        	if(leveledList.size() == 0){
//        		throw new ImportException("数据内容错误！分组数据为空！");
//        	}
//        	List<Map<String,String>> rootList = leveledList.get(0);
//        	for(int i =0;i<rootList.size();i++){
//        		Map<String,String> rootMap = rootList.get(i);
//        		ExamItemEntry e = new ExamItemEntry(examId, subjectId, rootMap.get("id"), rootMap.get("name"),
//						Double.parseDouble(rootMap.get("score")), "0",1);
//        		examItemService.addExamItemEntry(e);
//        		recursionSaveExamItemConfig(rootMap,leveledList,1);
//        	}
        	//转型
        	List<Map<String,Object>> srcMap = (ArrayList<Map<String,Object>>)data;
        	//验证分数是否超过总分的方法
        	checkScore(srcMap);
        	//验证通过，删删删！！！
        	examItemScoreService.deleteAllExamItemScore(examId, subjectId);
        	//删除完成，存存存！！！
        	saveScores(srcMap);
        	
        }
        
        private void checkScore(List<Map<String,Object>> srcMap)throws ImportException{
    		for(Map<String,Object> map : srcMap){
    			 List<Map<String,String>> scores = (List<Map<String,String>>)map.get("scoreList");
    			 for(Map<String,String> m : scores){
    				 double score = Double.parseDouble(m.get("itemScore"));
    				 double fullScore = Double.parseDouble(items.get(scores.indexOf(m)).get("score"));
    				 if(score > fullScore){
    					 throw new ImportException("分数数据异常！异常数据定位【年级名称:" + map.get("gradeName") + "】，【班级名称:" + map.get("className") +
    							 "】，【学生名称:" + map.get("userName") + "】，【题目ID:" + m.get("itemId") + "】，【异常分数为:" + score + "】，【题目满分为:"
    							 + fullScore + "】" );
    				 }
    			 }
    		}
    	}
        
        
        private void saveScores(List<Map<String,Object>> srcMap)throws ImportException{
        	List<DBObject> list = new ArrayList<DBObject>();
        	for(Map<String,Object> map : srcMap){
        		if(map.get("userId") == null || "".equals(map.get("userId")) ){
        			throw new ImportException("学生ID错误！异常数据定位【年级名称:" + map.get("gradeName") + "】，【班级名称:" + map.get("className") +
    							 "】，【学生名称:" + map.get("userName") + "】");
        		}
        		String studentId = map.get("userId").toString();
        		List<Map<String,String>> scores = (List<Map<String,String>>)map.get("scoreList");
        		for(Map<String,String> m : scores){
   				 	ExamItemScoreEntry e = new ExamItemScoreEntry(examId, subjectId, m.get("itemId"), studentId, Double.parseDouble(m.get("itemScore")));
//   				 	e.setID(new ObjectId());
   				 	list.add(e.getBaseEntry());
   				 	
   			 	}
        	}
        	examItemScoreService.addExamItemScoreEntryList(list);
        }
    }
}
