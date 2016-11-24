package com.fulaan.file.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fulaan.educationbureau.service.EducationBureauService;
import com.pojo.educationbureau.EducationBureauEntry;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.fulaan.annotation.SessionNeedless;
import com.fulaan.base.controller.BaseController;
import com.fulaan.file.service.FileService;
import com.fulaan.resources.service.CloudResourceService;
import com.pojo.resources.ResourceEntry;
import com.sys.constants.Constant;
import com.sys.exceptions.IllegalParamException;
import com.sys.utils.QiniuFileUtils;
import com.sys.utils.RespObj;

/**
 * Created by Caocui on 2015/8/25.
 */
@Controller
@RequestMapping("/file1")
public class FileController extends BaseController {
    @Autowired
    private FileService fileService;
    @Autowired
    private CloudResourceService resourceService;

    @Autowired
    private EducationBureauService educationBureauService;

    /**
     * 文件上传
     *
     * @param request
     * @return
     */
    @SessionNeedless
    @RequestMapping("/upload")
    public void upload(HttpServletRequest request,HttpServletResponse response) {
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        Map<String,Object> resultMap = new HashMap<String,Object>();
        try {
            ObjectId schoolId = new ObjectId(getSessionValue().getSchoolId());
            int role=getSessionValue().getUserRole();
            EducationBureauEntry ebe = educationBureauService.selEducationByUserId(getUserId(),role,schoolId.toString());

        	resultMap.put("data",fileService.uploadFile(ebe.getID(), request, this.getUserId(), "file"));
        	resultMap.put("code","200");
        } catch (IOException e) {
        	resultMap.put("code","500");
        	resultMap.put("data","上传文件失败");
        	e.printStackTrace();
        } catch (IllegalParamException e) {
        	resultMap.put("code","500");
        	resultMap.put("data","上传文件失败");
        	e.printStackTrace();
        }finally{
        	PrintWriter pw;
    		try {
    			pw = response.getWriter();
    			pw.write(JSON.toJSONString(resultMap));
    			pw.flush();
    			pw.close();
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
        }


//        return JSON.toJSONString(respObj);
    }

    /**
     * 文件上传
     *
     * @param request
     * @param response
     * @return
     */
    @SessionNeedless
    @RequestMapping("/download")
    public void download(HttpServletRequest request, HttpServletResponse response) {
        try {
            fileService.downFile(request, response);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalParamException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 文件上传
     *
     * @param request
     * @param response
     * @return
     */
    @SessionNeedless
    @RequestMapping("/getResourceUrl")
    @ResponseBody
    public String getResourceUrl(@RequestParam(value="resourceId", defaultValue = "")String resourceId) {
    Map<String,String> resultMap = new HashMap<String,String>(); 
        ResourceEntry re = resourceService.getResourceEntryById(new ObjectId(resourceId));
        String url = QiniuFileUtils.getPath(re.getType(), re.getBucketkey());
        resultMap.put("url", url);
    	return JSON.toJSONString(resultMap);
    }
}
