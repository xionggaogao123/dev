package com.fulaan.moralculture.contraller;

import com.fulaan.base.controller.BaseController;
import com.fulaan.moralculture.dto.MoralCultureManageDTO;
import com.fulaan.moralculture.service.MoralCultureManageService;
import com.pojo.utils.DeleteState;
import org.apache.log4j.Logger;
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
 * Created by guojing on 2015/7/2.
 */
@Controller
@RequestMapping("/moralCultureManage")
public class MoralCultureManageController extends BaseController {

    private static final Logger logger = Logger.getLogger(MoralCultureManageController.class);

    @Autowired
    private MoralCultureManageService moralCultureManageService;
    /**
     * 德育项目管理页面
     * @param model
     * @return
     */
    @RequestMapping("/moralCultureManagePage")
    public String moralCultureManagePage(Map<String, Object> model) {
        return "moralculture/moralculturemanage";
    }

    /**
     * 添加德育项目
     * @param moralCultureManageDTO
     * @return
     */
    @RequestMapping("addMoralCultureProject")
    public @ResponseBody Map<String ,Object> addMoralCultureProject(MoralCultureManageDTO moralCultureManageDTO) {
        Map<String,Object> model = new HashMap<String,Object>();
        String userId = getSessionValue().getId();
        String schoolId = getSessionValue().getSchoolId();
        moralCultureManageDTO.setCreateBy(userId);
        moralCultureManageDTO.setSchoolId(schoolId);
        //添加德育项目
        ObjectId id=moralCultureManageService.addMoralCultureProject(moralCultureManageDTO);
        model.put("result",true);
        return model;
    }

    /**
     * 删除德育项目
     * @param id
     * @return
     */
    @RequestMapping("delMoralCultureProject")
    public @ResponseBody Map<String ,Object> delMoralCultureProject(@RequestParam("id") String id) {
        Map<String,Object> model = new HashMap<String,Object>();
        ObjectId userId = getUserId();
        //删除德育项目
        moralCultureManageService.delMoralCultureProject(id,userId);
        model.put("result",true);
        return model;
    }

    /**
     * 修改德育项目
     * @param id
     * @return
     */
    @RequestMapping("updMoralCultureProject")
    public @ResponseBody Map<String,Object> updMoralCultureProject(@RequestParam("id") String id,@RequestParam("moralCultureName") String moralCultureName) throws Exception{
        Map<String,Object>  model = new HashMap<String,Object>();
        ObjectId userId = getUserId();
        moralCultureManageService.updMoralCultureProject(id, moralCultureName,userId);
        model.put("result", true);
        return model;
    }

    /**
     * 德育项目列表
     * @return
     */
    @RequestMapping("selMoralCultureProjectList")
    public @ResponseBody Map<String,Object> selMoralCultureProjectList() {
        Map<String,Object> model = new HashMap<String,Object>();
        String schoolId = getSessionValue().getSchoolId();
        //根据条件获取德育项目列表
        List<MoralCultureManageDTO> list = moralCultureManageService.selMoralCultureProjectList(schoolId, DeleteState.NORMAL);
        model.put("list",list);
        return model;
    }

    /**
     * 查询单条德育项目
     * @param id
     * @return
     */
    @RequestMapping("selMoralCultureProjectInfo")
    public @ResponseBody Map<String,Object> selMoralCultureProjectInfo(@RequestParam("id") String id) {
        Map<String,Object> model = new HashMap<String,Object>();
        //根据id获取德育项目信息
        MoralCultureManageDTO dto = moralCultureManageService.selMoralCultureProjectInfo(id);
        model.put("data",dto);
        return model;
    }
}
