package com.fulaan.train;

import com.fulaan.annotation.LoginInfo;
import com.fulaan.annotation.ObjectIdType;
import com.fulaan.annotation.SessionNeedless;
import com.fulaan.annotation.UserRoles;
import com.fulaan.base.BaseController;
import com.fulaan.cache.RedisUtils;
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
import com.sys.exceptions.IllegalParamException;
import com.sys.utils.QiniuFileUtils;
import com.sys.utils.RespObj;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    public String trainList(Map<String, Object> model,HttpServletRequest request){
        try {
            model.put("location",0);
            String region;
            String instituteKeyValue = getCookieInstituteKeyValue(request);
            if(StringUtils.isNotBlank(instituteKeyValue)){
                Map<String,String> map=RedisUtils.getMap(instituteKeyValue);
                if(null!=map&&!map.isEmpty()){
                    String regionId=map.get("regionId");
                    if(StringUtils.isNotBlank(regionId)){
                        RegionEntry regionEntry=regionService.findById(new ObjectId(regionId));
                        region=regionEntry.getName().substring(0,2);
                        model.put("location",1);
                    }else{
                        region="上海";
                    }
                    String typeId=map.get("type");
                    if(StringUtils.isNotBlank(typeId)){
                        model.put("typeId",typeId);
                    }
                    String areaId=map.get("area");
                    if(StringUtils.isNotBlank(areaId)){
                        model.put("areaId",areaId);
                    }
                    model.put("region",region);

//                    Map<String, String>  position= getProvinceInfo.getAddresses("ip=" + getIP(), "utf-8");
//                    if (null != position && !position.isEmpty()) {
//                        String positionRegion=map.get("region");
//                        if(StringUtils.isNotBlank(positionRegion)){
//                            if(!model.get("region").equals(positionRegion.substring(0, 2))){
//                                model.put("location",1);
//                            }
//                        }
//
//                    }
                }else{
                    model.put("region", "上海");
                }
            }else{
                Map<String, String> map = getProvinceInfo.getAddresses("ip=" + getIP(), "utf-8");
                if (null == map || map.size() == 0) {
                    model.put("region", "上海");
                } else {
                    model.put("region", map.get("region").substring(0, 2));
                }
            }
        }catch(UnsupportedEncodingException  e){
            model.put("region", "上海");
        }
        return "/train/trainList";
    }

    private String getCookieInstituteKeyValue(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (null != cookies) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(Constant.COOKIE_INSTITUTE_INFO)) {
                    return cookie.getValue();
                }
            }
        }
        return Constant.EMPTY;
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


    @RequestMapping("/dealCriticism")
    @ResponseBody
    @UserRoles(UserRole.DISCUSS_MANAGER)
    public RespObj dealCriticism(@ObjectIdType ObjectId id,int remove) {
        criticismService.dealCriticism(id,remove);
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
                                 @RequestParam(defaultValue = "1", required = false) int sortType,HttpServletResponse response) {
//        if (lon == 0 && lat == 0) {
//            if(null!=getUserId()){
//                List<Double> locs = mateService.getCoordinates(getUserId());
//                if (locs != null && locs.size() == 2) {
//                    lon = locs.get(0);
//                    lat = locs.get(1);
//                }
//            }
//        }

        //状态记录
        //先清空cookie
        Cookie cookies[] = getRequest().getCookies();
        Cookie c = null;
        for (int i = 0; i < cookies.length; i++) {
            c = cookies[i];
            c.setMaxAge(0);
            if (c.getName().equals(Constant.COOKIE_INSTITUTE_INFO)) {
                RedisUtils.deleteKey(c.getValue());
            }
        }
        ObjectId cacheKey = new ObjectId();
        Map<String,String> cacheMap=new HashMap<String, String>();
        cacheMap.put("regionId",region);
        cacheMap.put("type",type);
        cacheMap.put("area",area);
        RedisUtils.cacheMap(cacheKey.toString(),cacheMap,Constant.SECONDS_IN_DAY);
        Cookie instituteCookie = new Cookie(Constant.COOKIE_INSTITUTE_INFO, cacheKey.toString());
        instituteCookie.setMaxAge(Constant.SECONDS_IN_DAY);
        instituteCookie.setPath(Constant.BASE_PATH);
        response.addCookie(instituteCookie);

//        return getInstituteData(lon, lat, distance, page, pageSize, type, area, region, itemType, regular, sortType);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("list", new ArrayList<InstituteDTO>());
        map.put("count", 0);
        map.put("page", page);
        map.put("pageSize", pageSize);
        return RespObj.SUCCESS(map);
    }


    private RespObj getInstituteData(double lon,double lat,int distance,int page,int pageSize,
                                    String type,String area,String region,String itemType,String regular,int sortType){
        if(distance < 0) {
            distance = 20000;
        } else {
            distance *= 500;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        List<String> regionsIds = new ArrayList<String>();
        List<String> itemTypeIds = new ArrayList<String>();
        if (StringUtils.isNotBlank(region) && StringUtils.isBlank(area)) {
            List<RegionDTO> dtos = regionService.getRegionList(3, new ObjectId(region));
            if(dtos.size()>0){
                for (RegionDTO regionDTO : dtos) {
                    regionsIds.add(regionDTO.getId());
                }
            }else{
                regionsIds.add(region);
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

    @RequestMapping("/getInstitutesInDetail")
    @SessionNeedless
    @ResponseBody
    public RespObj getInstitutesInDetail(@RequestParam(defaultValue = "10", required = false) int pageSize,
                                         @RequestParam(defaultValue = "1", required = false) int sortType,
                                         @RequestParam(defaultValue = "", required = false) String itemType){
//        return getInstituteData(0,0,-1,1,pageSize,"","","",itemType,"",sortType);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("list", new ArrayList<InstituteDTO>());
        map.put("count", 0);
        map.put("page", 1);
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
                                    @RequestParam(defaultValue = "0",required = false) int remove,
                                    @RequestParam(defaultValue = "1", required = false) int page,
                                    @RequestParam(defaultValue = "10", required = false) int pageSize) {
        Map<String, Object> map = new HashMap<String, Object>();
        List<CriticismDTO> dtos = criticismService.getCriticismDTOs(instituteId, page, pageSize,remove);
        int count = criticismService.countCriticisms(instituteId,remove);
        int goodCount=criticismService.countCriticismEntriesSort(instituteId,remove,4);
        int normalCount=criticismService.countCriticismEntriesSort(instituteId,remove,3);
        int badCount=criticismService.countCriticismEntriesSort(instituteId,remove,2);
        map.put("list", dtos);
        map.put("count", count);
        map.put("goodCount", goodCount);
        map.put("normalCount", normalCount);
        map.put("badCount", badCount);
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

        //先判断是否为最新10天注册的用户，若是，则不能评论
        if(null!=userId){
            long nowTime=System.currentTimeMillis();
            long deadTime=userId.getTime()+10L*24L*60L*60L*1000L;
            if(nowTime<deadTime){
                return RespObj.FAILD("新用户10天内不能评论!");
            }
        }
        if (null != entry) {
            int remove = entry.getRemove();
            if (remove == 0) {
                return RespObj.FAILD("已经评价过了!");
            } else {
                entry.setRemove(0);
                entry.setScore(score);
                entry.setComment(comment);
                criticismService.saveEntry(entry);
                saveInstituteEntry(instituteId);
                return RespObj.SUCCESS("评价成功!");
            }
        } else {
            criticismService.saveOrUpdate(comment, userId, instituteId, score);
            saveInstituteEntry(instituteId);
            return RespObj.SUCCESS("评价成功!");
        }

    }

    public void saveInstituteEntry(ObjectId instituteId) {
        int count = 0;
        InstituteEntry instituteEntry = instituteService.find(instituteId);
        List<CriticismDTO> dtos = criticismService.getCriticismDTOs(instituteId, 1, 500,0);
        for (CriticismDTO criticismDTO : dtos) {
            count += criticismDTO.getScore();
        }
        double dscore = count / dtos.size();
        instituteEntry.setScore(dscore);
        instituteService.saveOrUpdate(instituteEntry);
    }

    /**
     * 根据两个Id处理这一段之间的数据
     * @param startId
     * @param endId
     * @param type
     * @return
     */
    @RequestMapping("/batchDealTwoId")
    @ResponseBody
    public RespObj batchDealTwoId(@ObjectIdType ObjectId startId,@ObjectIdType ObjectId endId,int type){
        try {
            List<ObjectId> ids=new ArrayList<ObjectId>();
            String qiuNiuPath;
            String key = "defaultTypeForImage" + type;
            qiuNiuPath = RedisUtils.getString(key);
            if (StringUtils.isBlank(qiuNiuPath)) {
                String fileName;
                if (type == 0) {
                    fileName = "default.png";
                } else {
                    fileName = "default" + type + ".png";
                }
                String defaultImage = getRequest().getServletContext().getRealPath("/static") + "/images/upload/" + fileName;
                File file = new File(defaultImage);
                String fileKey = new ObjectId().toString() + ".png";
                QiniuFileUtils.uploadFile(fileKey, new FileInputStream(file), QiniuFileUtils.TYPE_IMAGE);
                qiuNiuPath = QiniuFileUtils.getPath(QiniuFileUtils.TYPE_IMAGE, fileKey);
                RedisUtils.cacheString(key,qiuNiuPath,Constant.SECONDS_IN_MONTH);
            }
            List<InstituteEntry> entries=instituteService.getEntriesByTwoId(startId,endId);
            for(InstituteEntry entry:entries){
                ids.add(entry.getID());
            }
            instituteService.batchImageByIds(ids,qiuNiuPath);

        }catch (Exception e){
            e.printStackTrace();
        }
        return RespObj.SUCCESS("处理成功");
    }

    /**
     * 处理默认图片数据
     * @param idOrNames
     * @return
     */
    @RequestMapping("/batchDefaultImage")
    @ResponseBody
    public RespObj batchDefaultImage(String idOrNames,int type){
        if(StringUtils.isNotBlank(idOrNames)){
            try {
                String qiuNiuPath;
                String key = "defaultTypeForImage" + type;
                qiuNiuPath = RedisUtils.getString(key);
                if (StringUtils.isBlank(qiuNiuPath)) {
                    String fileName;
                    if (type == 0) {
                        fileName = "default.png";
                    } else {
                        fileName = "default" + type + ".png";
                    }
                    String defaultImage = getRequest().getServletContext().getRealPath("/static") + "/images/upload/" + fileName;
                    File file = new File(defaultImage);
                    String fileKey = new ObjectId().toString() + ".png";
                    QiniuFileUtils.uploadFile(fileKey, new FileInputStream(file), QiniuFileUtils.TYPE_IMAGE);
                    qiuNiuPath = QiniuFileUtils.getPath(QiniuFileUtils.TYPE_IMAGE, fileKey);
                    RedisUtils.cacheString(key,qiuNiuPath,Constant.SECONDS_IN_MONTH);
                }
                if(idOrNames.contains("@")){
                    //处理Ids
                    String[] instituteIds=idOrNames.split("@");
                    List<ObjectId> ids=new ArrayList<ObjectId>();
                    for(String item:instituteIds){
                        ids.add(new ObjectId(item));
                    }
                    instituteService.batchImageByIds(ids,qiuNiuPath);
                }else if(idOrNames.contains("$")){
                    //处理名字
                    String[] instituteName=idOrNames.split("\\$");
                    List<String> names=Arrays.asList(instituteName);
                    instituteService.batchImageByNames(names,qiuNiuPath);
                }else{
                    List<ObjectId> ids=new ArrayList<ObjectId>();
                    ids.add(new ObjectId(idOrNames));
                    instituteService.batchImageByIds(ids,qiuNiuPath);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return RespObj.SUCCESS("处理成功");
        }else{
            return RespObj.FAILD("请传入Ids");
        }
    }



    /**
     * 处理还未处理的图片
     */
    @RequestMapping("/batchImages")
    @ResponseBody
    public RespObj batchImages(String instituteIds){
        try{
            List<ObjectId> ids=new ArrayList<ObjectId>();
            if(instituteIds.contains("@")){
                String[] temp=instituteIds.split("@");
                for(String item:temp){
                    ids.add(new ObjectId(item));
                }
            }else{
                ids.add(new ObjectId(instituteIds));
            }
            List<InstituteEntry> entries = instituteService.findInstitutesByIds(ids);
            batchImage(entries);
        }catch (Exception e){
            return RespObj.FAILD(e.getMessage());
        }
        return RespObj.SUCCESS;
    }


    /**
     * 批量处理残留数据
     * @param startId
     * @param endId
     * @return
     */
    @RequestMapping("/remainImage")
    @ResponseBody
    public RespObj remainImage(@ObjectIdType ObjectId startId,@ObjectIdType ObjectId endId){
        try{
            List<InstituteEntry> entries=instituteService.getEntriesByTwoId(startId,endId);
            batchImage(entries);
        }catch (Exception e){
            return RespObj.FAILD(e.getMessage());
        }
        return RespObj.SUCCESS;
    }


    /**
     * 批量删除数据
     * @param deleteIds
     * @return
     */
    @RequestMapping("/batchDeleteData")
    @ResponseBody
    public RespObj batchDeleteData(String deleteIds){
        try{
            List<ObjectId> ids=new ArrayList<ObjectId>();
            if(deleteIds.contains(";")){
                String[] temp=deleteIds.split(";");
                for(String item:temp){
                    ids.add(new ObjectId(item));
                }
            }else{
                ids.add(new ObjectId(deleteIds));
            }
            instituteService.removeData(ids);
        }catch (Exception e){
            return RespObj.FAILD(e.getMessage());
        }
        return RespObj.SUCCESS;
    }







    private void batchImage(List<InstituteEntry> entries) throws  IOException,IllegalParamException{
        for(InstituteEntry entry : entries) {
            String fileName = new ObjectId() + ".jpg";
            String parentPath = getRequest().getServletContext().getRealPath("/static") + "/images/upload";
//           String path= Resources.getProperty("upload.file");
            DownloadUtil.downLoadFromUrl(entry.getMainPic(), fileName, parentPath);
            String filePath = parentPath + "/" + fileName;
            String logoImg = getRequest().getServletContext().getRealPath("/static") + "/images/upload/logo.png";
            String waterImage = imageInit.mergeWaterMark(filePath, logoImg);
            File file1 = new File(filePath);
            File file = new File(waterImage);
            try {
                String extensionName = waterImage.substring(waterImage.indexOf(".") + 1, waterImage.length());
                String fileKey = new ObjectId().toString() + Constant.POINT + extensionName;
                QiniuFileUtils.uploadFile(fileKey, new FileInputStream(file), QiniuFileUtils.TYPE_IMAGE);
                String qiuNiuPath = QiniuFileUtils.getPath(QiniuFileUtils.TYPE_IMAGE, fileKey);
                entry.setImageUrl(qiuNiuPath);
                instituteService.saveOrUpdate(entry);
            }finally {
                file.delete();
                file1.delete();
            }
        }
    }


    /**
     * 批量处理图片
     */
    @RequestMapping("/batchDealImage")
    @ResponseBody
    public RespObj batchDealImage(@RequestParam(defaultValue = "1", required = false) int page,
                                  @RequestParam(defaultValue = "1000", required = false) int pageSize){
        try{
            List<InstituteEntry> entries = instituteService.findInstituteEntries(page, pageSize);
            batchImage(entries);
        }catch (Exception e){
            return RespObj.FAILD(e.getMessage());
        }
        return RespObj.SUCCESS;
    }

    /**
     * 数据进行清理
     * @param parentId
     * @return
     */
    @RequestMapping("/replaceRegion/{parentId}")
    @ResponseBody
    public RespObj replaceRegion(@PathVariable @ObjectIdType ObjectId parentId){
        List<RegionDTO> regionDTOs=regionService.getRegionList(3,parentId);
        for(RegionDTO regionDTO:regionDTOs){
            List<RegionDTO> regionDTOList=regionService.getRegionList(4,new ObjectId(regionDTO.getId()));
            if(regionDTOList.size()>0){
                for(RegionDTO dto:regionDTOList){
                    instituteService.updateRegionData(dto.getName(),dto.getId());
                }
            }
            instituteService.updateRegionData(regionDTO.getName(),regionDTO.getId());
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

    @RequestMapping("/region/setSort/{id}")
    @ResponseBody
    public RespObj regionSetSort(@PathVariable @ObjectIdType ObjectId id,int sort){
        regionService.setSort(id,sort);
        return RespObj.SUCCESS;
    }

    @RequestMapping("/itemType/setSort/{id}")
    @ResponseBody
    public RespObj itemTypeSetSort(@PathVariable @ObjectIdType ObjectId id,int sort){
        itemTypeService.setSort(id,sort);
        return RespObj.SUCCESS;
    }


}
