package com.fulaan.train;

import com.fulaan.annotation.LoginInfo;
import com.fulaan.annotation.ObjectIdType;
import com.fulaan.annotation.SessionNeedless;
import com.fulaan.annotation.UserRoles;
import com.fulaan.cache.RedisUtils;
import com.fulaan.controller.BaseController;
import com.fulaan.playmate.service.MateService;
import com.fulaan.train.dto.CriticismDTO;
import com.fulaan.train.dto.InstituteDTO;
import com.fulaan.train.dto.ItemTypeDTO;
import com.fulaan.train.dto.RegionDTO;
import com.fulaan.train.service.CriticismService;
import com.fulaan.train.service.InstituteService;
import com.fulaan.train.service.ItemTypeService;
import com.fulaan.train.service.RegionService;
import com.fulaan.util.DownloadUtil;
import com.fulaan.util.GetLocation;
import com.fulaan.util.getProvinceInfo;
import com.fulaan.util.imageInit;
import com.pojo.questions.PropertiesObj;
import com.pojo.train.CriticismEntry;
import com.pojo.train.InstituteEntry;
import com.pojo.train.RegionEntry;
import com.pojo.user.UserRole;
import com.sys.constants.Constant;
import com.sys.utils.QiniuFileUtils;
import com.sys.utils.RespObj;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * Created by admin on 2016/12/5.
 */
@Controller
@RequestMapping("/train")
public class TrainController extends BaseController {

    @Autowired
    private MateService mateService;
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
    public String trainList(Map<String, Object> model){
        try {
            Map<String, String> map = getProvinceInfo.getAddresses("ip=" + getIP(), "utf-8");
            if (null == map || map.size() == 0) {
                model.put("region", "上海");
            } else {
                model.put("region", map.get("region").substring(0, 2));
            }
        }catch(UnsupportedEncodingException  e){
            model.put("region", "上海");
        }
        return "/train/trainList";
    }

    /**
     * 获取app端的区域数据
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping("/getAppRegions")
    @SessionNeedless
    @ResponseBody
    public RespObj getAppRegions(@ObjectIdType ObjectId itemTypeId,
                                 @RequestParam(defaultValue = "",required = false) String name) throws UnsupportedEncodingException{
//        String name="";
//
        if(StringUtils.isBlank(name)){
            Map<String,String> map=getProvinceInfo.getAddresses("ip="+getIP(),"utf-8");
            if(null==map||map.size()==0){
                name="上海";
            }else{
                if(StringUtils.isNotBlank(map.get("region"))&&map.get("region").length()>=2) {
                    name = map.get("region").substring(0, 2);
                }else{
                    name="上海";
                }
            }
        }

        RegionEntry regionEntry=regionService.getRegionEntry(name);
        List<RegionDTO> regionList=regionService.getRegionList(3, regionEntry.getID());
        List<ItemTypeDTO> itemTypes = itemTypeService.getItemTypes(2, itemTypeId);
        Map<String,Object> retMap=new HashMap<String,Object>();
        retMap.put("regionId",regionEntry.getID().toString());
        retMap.put("regionList",regionList);
        retMap.put("itemTypes",itemTypes);
        return RespObj.SUCCESS(retMap);
    }


    @RequestMapping("/create2dsphereIndex")
    @ResponseBody
    @UserRoles(UserRole.DISCUSS_MANAGER)
    public RespObj create2dsphereIndex() {
        instituteService.create2dsphereIndex();
        return RespObj.SUCCESS;
    }


    @RequestMapping("/removeCriticism")
    @ResponseBody
    @UserRoles(UserRole.DISCUSS_MANAGER)
    public RespObj removeCriticism(@ObjectIdType ObjectId id) {
        criticismService.removeCriticism(id);
        return RespObj.SUCCESS;
    }




    @RequestMapping("/trainDetail")
    @SessionNeedless
    @LoginInfo
    public String trainDetail(Map<String, Object> model) {
        String detailId = getRequest().getParameter("detailId");
        String itemId = getRequest().getParameter("itemId");
        ItemTypeDTO itemTypeDTO = itemTypeService.find(new ObjectId(itemId));
        model.put("itemDto", itemTypeDTO);
        model.put("instituteId", detailId);
        InstituteDTO dto = instituteService.findById(new ObjectId(detailId));
        model.put("dto", dto);
        return "/train/trainDetail";
    }


    @RequestMapping("/getRegions")
    @SessionNeedless
    @ResponseBody
    public RespObj getRegions(@RequestParam(defaultValue = "2", required = false) int level,
                              @RequestParam(defaultValue = "", required = false) String regionId) {
        ObjectId parentId = StringUtils.isNotBlank(regionId) ? new ObjectId(regionId) : null;
        List<RegionDTO> dtos = regionService.getRegionList(level, parentId);
        return RespObj.SUCCESS(dtos);
    }





    @RequestMapping("/getItemTypes")
    @SessionNeedless
    @ResponseBody
    public RespObj getItemTypes(@RequestParam(defaultValue = "2", required = false) int level,
                                @RequestParam(defaultValue = "", required = false) String itemTypeId) {
        ObjectId parentId = StringUtils.isNotBlank(itemTypeId) ? new ObjectId(itemTypeId) : null;
        List<ItemTypeDTO> dtos = itemTypeService.getItemTypes(level, parentId);
        return RespObj.SUCCESS(dtos);
    }

    /**
     *
     * @param lon
     * @param lat
     * @param distance
     * @param page
     * @param pageSize
     * @param type
     * @param area
     * @param region
     * @param itemType
     * @param regular
     * @param sortType
     * @return
     */
    @RequestMapping("/getInstitutes")
    @SessionNeedless
    @ResponseBody
    public RespObj getInstitutes(@RequestParam(value = "lon", required = false, defaultValue = "0") double lon,
                                 @RequestParam(value = "lat", required = false, defaultValue = "0") double lat,
                                 @RequestParam(value = "distance", required = false, defaultValue = "-1") int distance,
                                 @RequestParam(defaultValue = "1", required = false) int page,
                                 @RequestParam(defaultValue = "10", required = false) int pageSize,
                                 @RequestParam(defaultValue = "", required = false) String type,
                                 @RequestParam(defaultValue = "", required = false) String area,
                                 @RequestParam(defaultValue = "", required = false) String region,
                                 @RequestParam(defaultValue = "", required = false) String itemType,
                                 @RequestParam(defaultValue = "", required = false) String regular,
                                 @RequestParam(defaultValue = "1", required = false) int sortType) {
//        if (lon == 0 && lat == 0) {
//            if(null!=getUserId()){
//                List<Double> locs = mateService.getCoordinates(getUserId());
//                if (locs != null && locs.size() == 2) {
//                    lon = locs.get(0);
//                    lat = locs.get(1);
//                }
//            }
//        }
        if(distance < 0) {
            distance = 100000;
        } else {
            distance *= 500;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        List<String> regionsIds = new ArrayList<String>();
        List<String> itemTypeIds = new ArrayList<String>();
        if (StringUtils.isNotBlank(region) && StringUtils.isBlank(area)) {
            List<RegionDTO> dtos = regionService.getRegionList(3, new ObjectId(region));
            for (RegionDTO regionDTO : dtos) {
                regionsIds.add(regionDTO.getId());
            }
        }

        if (StringUtils.isNotBlank(itemType) && StringUtils.isBlank(type)) {
            List<ItemTypeDTO> dtos = itemTypeService.getItemTypes(2, new ObjectId(itemType));
            for (ItemTypeDTO itemTypeDTO : dtos) {
                itemTypeIds.add(itemTypeDTO.getId());
            }
        }

        List<InstituteDTO> dtos = instituteService.getInstitutes(regular, regionsIds, itemTypeIds, type, area, page, pageSize, sortType,lon,lat,distance);
        int count = instituteService.countInstitutes(regular, regionsIds, itemTypeIds, type, area,lon,lat,distance);
        map.put("list", dtos);
        map.put("count", count);
        map.put("page", page);
        map.put("pageSize", pageSize);
        return RespObj.SUCCESS(map);

    }


    /**
     * 获取用户评论信息
     *
     * @param instituteId
     * @param page
     * @param pageSize
     * @return
     */
    @RequestMapping("/getTrainComments/{instituteId}")
    @SessionNeedless
    @ResponseBody
    public RespObj getTrainComments(@PathVariable @ObjectIdType ObjectId instituteId,
                                    @RequestParam(defaultValue = "1", required = false) int page,
                                    @RequestParam(defaultValue = "10", required = false) int pageSize) {
        Map<String, Object> map = new HashMap<String, Object>();
        List<CriticismDTO> dtos = criticismService.getCriticismDTOs(instituteId, page, pageSize);
        int count = criticismService.countCriticisms(instituteId);
        map.put("list", dtos);
        map.put("count", count);
        map.put("page", page);
        map.put("pageSize", pageSize);
        return RespObj.SUCCESS(map);
    }


    @RequestMapping("/addTrainComment")
    @ResponseBody
    public RespObj addTrainComment(@ObjectIdType ObjectId instituteId,
                                   @RequestParam(defaultValue = "1", required = false) int score,
                                   @RequestParam(defaultValue = "", required = false) String comment
    ) {
        ObjectId userId = getUserId();
        //判断该用户是否评论过该培训信息
        CriticismEntry entry = criticismService.getEntry(instituteId, userId);

        if (null != entry) {
            int remove = entry.getRemove();
            if (remove == 0) {
                return RespObj.FAILD("已经评价过了!");
            } else {
                entry.setScore(score);
                entry.setComment(comment);
                criticismService.saveEntry(entry);
//                saveInstituteEntry(instituteId);
                return RespObj.SUCCESS("评价成功!");
            }
        } else {
            criticismService.saveOrUpdate(comment, userId, instituteId, score);
//            saveInstituteEntry(instituteId);
            return RespObj.SUCCESS("评价成功!");
        }

    }

    public void saveInstituteEntry(ObjectId instituteId) {
        int count = 0;
        InstituteEntry instituteEntry = instituteService.find(instituteId);
        List<CriticismDTO> dtos = criticismService.getCriticismDTOs(instituteId, 1, 500);
        for (CriticismDTO criticismDTO : dtos) {
            count += criticismDTO.getScore();
        }
        double dscore = count / dtos.size();
        instituteEntry.setScore(dscore);
        instituteService.saveOrUpdate(instituteEntry);
    }

    /**
     * 批量处理图片
     */
    @RequestMapping("/batchDealImage")
    @ResponseBody
    public RespObj batchDealImage(@RequestParam(defaultValue = "1", required = false) int page,
                                  @RequestParam(defaultValue = "2", required = false) int pageSize){
        try{
            List<InstituteEntry> entries = instituteService.findInstituteEntries(page, pageSize);
            for(InstituteEntry entry : entries) {
                String fileName="test4.jpg";
                String path="D:\\water";
                DownloadUtil.downLoadFromUrl(entry.getMainPic(),fileName,path);
                String filePath=path+"\\"+fileName;
                String logoImg = "D:/water/logo.png";
                String waterImage=imageInit.mergeWaterMark(filePath,logoImg);
                File file=new File(waterImage);
                String extensionName = fileName.substring(fileName.indexOf(".")+1,fileName.length());
                String fileKey = new ObjectId().toString() + Constant.POINT + extensionName;
                QiniuFileUtils.uploadFile(fileKey, new FileInputStream(file), QiniuFileUtils.TYPE_IMAGE);
                String qiuNiuPath = QiniuFileUtils.getPath(QiniuFileUtils.TYPE_IMAGE, fileKey);
                entry.setImageUrl(qiuNiuPath);
                instituteService.saveOrUpdate(entry);
            }
        }catch (Exception e){
            return RespObj.FAILD(e.getMessage());
        }
        return RespObj.SUCCESS;
    }


    /**
     * 处理institute数据
     *
     */
    @RequestMapping("/instituteData")
    @ResponseBody
    public RespObj instituteData(@RequestParam(defaultValue = "1", required = false) int page,
                                 @RequestParam(defaultValue = "1000", required = false) int pageSize) {
        try {
            List<InstituteEntry> entries = instituteService.findInstituteEntries(page, pageSize);
            for (InstituteEntry entry : entries) {
                if(StringUtils.isNotBlank(entry.getAddress())){
                    StringBuffer buffer=new StringBuffer();
                    List<PropertiesObj> areas = entry.getAreas();
                    for(PropertiesObj obj:areas){
                        buffer.append(obj.getName());
                    }
                    buffer.append(entry.getAddress());
                    String address = buffer.toString();
                    List<Double> temp=new ArrayList<Double>();
                    String[] o = GetLocation.getCoordinate(address);
                    if(o.length==2&&StringUtils.isNotBlank(o[0])){
                        temp.add(Double.parseDouble(o[0]));
                        temp.add(Double.parseDouble(o[1]));
                        InstituteEntry.Locations locations=new InstituteEntry.Locations();
                        locations.setCoordinates(temp);
                        locations.setType(Constant.DEFAULT_POINT);
                        entry.setLocations(locations);
                        instituteService.saveOrUpdate(entry);
                    }

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RespObj.SUCCESS;
    }

    protected String getIP() {
        HttpServletRequest request = getRequest();
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }


}
