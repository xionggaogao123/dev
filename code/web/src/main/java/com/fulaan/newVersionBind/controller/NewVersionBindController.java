package com.fulaan.newVersionBind.controller;

import com.fulaan.annotation.ObjectIdType;
import com.fulaan.base.BaseController;
import com.fulaan.newVersionBind.service.NewVersionBindService;
import com.fulaan.service.ConcernService;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by scott on 2017/9/5.
 */
@Controller
@RequestMapping("/newVersionBind")
public class NewVersionBindController extends BaseController {


    private static final Logger logger = Logger.getLogger(NewVersionBindController.class);

    @Autowired
    private NewVersionBindService newVersionBindService;




    @RequestMapping("/saveNewVersionEntry")
    @ResponseBody
    public RespObj saveNewVersionEntry(@ObjectIdType ObjectId bindId,
                                       int sex,String birthDate,
                                       @ObjectIdType ObjectId regionId,
                                       @ObjectIdType ObjectId regionAreaId,
                                       String relation,
                                       String schoolName){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            newVersionBindService.saveNewVersionBindRelationEntry(bindId, sex, birthDate, regionId, regionAreaId, relation, schoolName);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("保存信息成功!");
        }catch (Exception e){
            respObj.setMessage(e.getMessage());
        }
        return respObj;
    }

}
