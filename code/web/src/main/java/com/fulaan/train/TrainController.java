package com.fulaan.train;

import com.fulaan.annotation.LoginInfo;
import com.fulaan.annotation.SessionNeedless;
import com.fulaan.controller.BaseController;
import com.fulaan.train.dto.ItemTypeDTO;
import com.fulaan.train.dto.RegionDTO;
import com.fulaan.train.service.InstituteService;
import com.fulaan.train.service.ItemTypeService;
import com.fulaan.train.service.RegionService;
import com.sys.utils.RespObj;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2016/12/5.
 */
@Controller
@RequestMapping("/train")
public class TrainController extends BaseController{

    @Autowired
    private RegionService regionService;
    @Autowired
    private ItemTypeService itemTypeService;
    @Autowired
    private InstituteService instituteService;

    @RequestMapping("/trainList")
    @SessionNeedless
    @LoginInfo
    public String trainList(Map<String,Object> model){
        return"/train/trainList";
    }


    @RequestMapping("/trainDetail")
    @SessionNeedless
    @LoginInfo
    public String trainDetail(Map<String,Object> model){
        return "/train/trainDetail";
    }


    @RequestMapping("/getRegions")
    @SessionNeedless
    @ResponseBody
    public RespObj getRegions(@RequestParam(defaultValue = "2",required = false)int level,
                              @RequestParam(defaultValue = "",required = false)String regionId){
        ObjectId parentId= StringUtils.isNotBlank(regionId)?new ObjectId(regionId):null;
        List<RegionDTO> dtos=regionService.getRegionList(level,parentId);
        return RespObj.SUCCESS(dtos);
    }


    @RequestMapping("/getItemTypes")
    @SessionNeedless
    @ResponseBody
    public RespObj getItemTypes(@RequestParam(defaultValue = "2",required = false)int level,
                                @RequestParam(defaultValue = "",required = false)String itemTypeId){
        ObjectId parentId= StringUtils.isNotBlank(itemTypeId)?new ObjectId(itemTypeId):null;
        List<ItemTypeDTO> dtos=itemTypeService.getItemTypes(level,parentId);
        return RespObj.SUCCESS(dtos);
    }

    @RequestMapping("/getInstitutes")
    @SessionNeedless
    @ResponseBody
    public RespObj getInstitutes(@RequestParam(defaultValue = "1",required = false)int page,
                                 @RequestParam(defaultValue = "10",required = false)int pageSize,
                                 String type,
                                 String area){
        Map<String,Object> map=new HashMap<String,Object>();

        return  RespObj.SUCCESS(map);

    }





}
