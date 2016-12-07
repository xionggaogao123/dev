package com.fulaan.train;

import com.fulaan.annotation.LoginInfo;
import com.fulaan.annotation.ObjectIdType;
import com.fulaan.annotation.SessionNeedless;
import com.fulaan.controller.BaseController;
import com.fulaan.train.dto.CriticismDTO;
import com.fulaan.train.dto.InstituteDTO;
import com.fulaan.train.dto.ItemTypeDTO;
import com.fulaan.train.dto.RegionDTO;
import com.fulaan.train.service.CriticismService;
import com.fulaan.train.service.InstituteService;
import com.fulaan.train.service.ItemTypeService;
import com.fulaan.train.service.RegionService;
import com.pojo.train.CriticismEntry;
import com.pojo.train.InstituteEntry;
import com.sys.utils.RespObj;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
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
    @Autowired
    private CriticismService criticismService;

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
        String detailId=getRequest().getParameter("detailId");
        String itemId=getRequest().getParameter("itemId");
        ItemTypeDTO itemTypeDTO=itemTypeService.find(new ObjectId(itemId));
        model.put("itemDto",itemTypeDTO);
        model.put("instituteId",detailId);
        InstituteDTO dto=instituteService.findById(new ObjectId(detailId));
        model.put("dto",dto);
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

    /**
     *
     * @param page
     * @param pageSize
     * @param type
     * @param area
     * @param region
     * @param itemType
     * @return
     */
    @RequestMapping("/getInstitutes")
    @SessionNeedless
    @ResponseBody
    public RespObj getInstitutes(@RequestParam(defaultValue = "1",required = false)int page,
                                 @RequestParam(defaultValue = "10",required = false)int pageSize,
                                 @RequestParam(defaultValue = "",required = false)String type,
                                 @RequestParam(defaultValue = "",required = false)String area,
                                 @RequestParam(defaultValue = "",required = false)String region,
                                 @RequestParam(defaultValue = "",required = false)String itemType,
                                 @RequestParam(defaultValue = "",required = false)String regular,
                                 @RequestParam(defaultValue = "1",required = false)int sortType){
        Map<String,Object> map=new HashMap<String,Object>();
        List<String> regionsIds=new ArrayList<String>();
        List<String> itemTypeIds=new ArrayList<String>();
        if(StringUtils.isNotBlank(region)&&StringUtils.isBlank(area)){
            List<RegionDTO> dtos=regionService.getRegionList(3,new ObjectId(region));
            for(RegionDTO regionDTO:dtos){
                regionsIds.add(regionDTO.getId());
            }
        }

        if(StringUtils.isNotBlank(itemType)&&StringUtils.isBlank(type)){
            List<ItemTypeDTO> dtos=itemTypeService.getItemTypes(2,new ObjectId(itemType));
            for(ItemTypeDTO itemTypeDTO:dtos){
                itemTypeIds.add(itemTypeDTO.getId());
            }
        }

        List<InstituteDTO> dtos=instituteService.getInstitutes(regular,regionsIds,itemTypeIds,type, area, page, pageSize,sortType);
        int count=instituteService.countInstitutes(regular,regionsIds,itemTypeIds,type, area);
        map.put("list",dtos);
        map.put("count",count);
        map.put("page",page);
        map.put("pageSize",pageSize);
        return  RespObj.SUCCESS(map);

    }


    /**
     * 获取用户评论信息
     * @param instituteId
     * @param page
     * @param pageSize
     * @return
     */
    @RequestMapping("/getTrainComments/{instituteId}")
    @SessionNeedless
    @ResponseBody
    public RespObj getTrainComments(@PathVariable @ObjectIdType ObjectId instituteId,
                                    @RequestParam(defaultValue = "1",required = false)int page,
                                    @RequestParam(defaultValue = "10",required = false)int pageSize){
        Map<String,Object> map=new HashMap<String,Object>();
        List<CriticismDTO> dtos=criticismService.getCriticismDTOs(instituteId,page,pageSize);
        int count=criticismService.countCriticisms(instituteId);
        map.put("list",dtos);
        map.put("count",count);
        map.put("page",page);
        map.put("pageSize",pageSize);
        return  RespObj.SUCCESS(map);
    }


    @RequestMapping("/addTrainComment")
    @ResponseBody
    public RespObj addTrainComment(@ObjectIdType ObjectId instituteId,
                                   @RequestParam(defaultValue = "1",required = false)int score,
                                   @RequestParam(defaultValue = "",required = false)String comment
                                   ){
        ObjectId userId=getUserId();
        //判断该用户是否评论过该培训信息
        CriticismEntry entry=criticismService.getEntry(instituteId,userId);

        if(null!=entry){
            int remove=entry.getRemove();
            if(remove==0){
                return RespObj.FAILD("已经评价过了!");
            }else{
                entry.setScore(score);
                entry.setComment(comment);
                criticismService.saveEntry(entry);
//                saveInstituteEntry(instituteId);
                return RespObj.SUCCESS("评价成功!");
            }
        }else{
            criticismService.saveOrUpdate(comment,userId,instituteId,score);
//            saveInstituteEntry(instituteId);
            return RespObj.SUCCESS("评价成功!");
        }

    }

    public void saveInstituteEntry(ObjectId instituteId){
        int count=0;
        InstituteEntry instituteEntry=instituteService.find(instituteId);
        List<CriticismDTO> dtos=criticismService.getCriticismDTOs(instituteId,1,500);
        for(CriticismDTO criticismDTO:dtos){
            count+=criticismDTO.getScore();
        }
        double dscore=count/dtos.size();
        instituteEntry.setScore(dscore);
        instituteService.saveOrUpdate(instituteEntry);
    }

}
