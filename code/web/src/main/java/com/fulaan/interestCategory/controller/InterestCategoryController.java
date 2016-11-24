package com.fulaan.interestCategory.controller;

import com.fulaan.annotation.ObjectIdType;
import com.fulaan.base.controller.BaseController;
import com.fulaan.interestCategory.service.InterestCategoryService;
import com.sys.utils.RespObj;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import java.util.HashMap;
import java.util.Map;

/**
 * Created by fl on 2015/11/18.
 */
@Controller
@RequestMapping("/interestCategory")
public class InterestCategoryController extends BaseController {
    @Autowired
    private InterestCategoryService interestCategoryService;

    @RequestMapping("/list")
    @ResponseBody
    public Map<String, Object> getInterestCategoryList(){
        String schoolId = getSessionValue().getSchoolId();
        return interestCategoryService.getInterestCategoryList(new ObjectId(schoolId));
    }

    @RequestMapping("/add")
    @ResponseBody
    public RespObj addInterestCategory(String name){
        RespObj respObj = RespObj.FAILD;
        String schoolId = getSessionValue().getSchoolId();
        try {
            interestCategoryService.addInterestCategory(new ObjectId(schoolId), name);
            respObj = RespObj.SUCCESS;
        } catch (Exception e){
            System.out.println("添加失败");
        }
        return respObj;
    }

    @RequestMapping("/delete")
    @ResponseBody
    public Map<String, Object> deleteInterestCategory(@ObjectIdType ObjectId interestCategoryId){
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("code", "500");
        try {
            String code = interestCategoryService.deleteInterestCategory(interestCategoryId, getSessionValue().getSchoolId());
            model.put("code", code);
        } catch (Exception e){
            System.out.println("删除失败");
        }
        return model;
    }

    @RequestMapping("/edit")
    @ResponseBody
    public RespObj editInterestCategory(@ObjectIdType ObjectId interestCategoryId, String name){
        RespObj respObj = RespObj.FAILD;
        try {
            interestCategoryService.editInterestCategory(interestCategoryId, name);
            respObj = RespObj.SUCCESS;
        } catch (Exception e){
            System.out.println("编辑失败");
        }
        return respObj;
    }

    @RequestMapping("/change")
    @ResponseBody
    public RespObj changeInterestCategory(@ObjectIdType ObjectId interestCategoryId, @ObjectIdType ObjectId classId){
        RespObj respObj = RespObj.FAILD;
        try {
            interestCategoryService.updateTypeId(classId, interestCategoryId);
            respObj = RespObj.SUCCESS;
        } catch (Exception e){
            System.out.println("调换失败");
        }
        return respObj;
    }
}
