package com.fulaan.newVersionBind.controller;

import com.fulaan.annotation.ObjectIdType;
import com.fulaan.annotation.SessionNeedless;
import com.fulaan.base.BaseController;
import com.fulaan.newVersionBind.dto.NewVersionBindRelationDTO;
import com.fulaan.newVersionBind.service.NewVersionBindService;
import com.pojo.user.NewVersionBindRelationEntry;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by scott on 2017/9/5.
 */
@Controller
@RequestMapping("/newVersionBind")
public class NewVersionBindController extends BaseController {


    private static final Logger logger = Logger.getLogger(NewVersionBindController.class);

    @Autowired
    private NewVersionBindService newVersionBindService;


    /**
     * 填写信息绑定接口
     * @param userId
     * @param nickName
     * @param sex
     * @param birthDate
     * @param provinceName
     * @param regionName
     * @param regionAreaName
     * @param relation
     * @param avatar
     * @param schoolName
     * @param gradeType
     * @return
     */
    @RequestMapping("/saveNewVersionEntry")
    @ResponseBody
    public RespObj saveNewVersionEntry(
            @ObjectIdType ObjectId userId,
            String nickName,
            int sex,String birthDate,
            String provinceName,
            String regionName,
            String regionAreaName,
            int relation,
            String avatar,
            String schoolName,
            int gradeType){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            newVersionBindService.saveNewVersionBindRelationEntry(getUserId(),userId, sex, birthDate,provinceName, regionName, regionAreaName, relation, schoolName,avatar,
                    gradeType,nickName);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("绑定成功！");
        }catch (Exception e){
            respObj.setMessage(e.getMessage());
        }
        return respObj;
    }

    /**
     * 获取某个家长的绑定的所有的学生列表
     * @return
     */
    @RequestMapping("/getNewVersionBindDtos")
    @ResponseBody
    public RespObj getNewVersionBindDtos(){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            List<NewVersionBindRelationDTO> dtoList=newVersionBindService.getNewVersionBindDtos(getUserId());
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(dtoList);
        }catch (Exception e){
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }


    @RequestMapping("/getNewVersionBindStudent")
    @ResponseBody
    public RespObj getNewVersionBindStudent(){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            NewVersionBindRelationDTO dto=newVersionBindService.getNewVersionBindStudent(getUserId());
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(dto);
        }catch (Exception e){
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }





    @RequestMapping("/addEntry")
    @ResponseBody
    public RespObj saveNewVersionEntry(@RequestParam("id") String id){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            newVersionBindService.saveNewVersionEntry(new ObjectId(id),getUserId());
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("保存信息成功!");
        }catch (Exception e){
            respObj.setMessage(e.getMessage());
        }
        return respObj;
    }

}
