package com.fulaan.controller;

import com.db.fcommunity.BannerDao;
import com.fulaan.annotation.SessionNeedless;
import com.fulaan.annotation.UserRoles;
import com.pojo.fcommunity.AppBanner;
import com.pojo.fcommunity.AppBannerEntry;
import com.pojo.fcommunity.Banner;
import com.pojo.fcommunity.BannerEntity;
import com.pojo.user.UserRole;
import com.sys.utils.QiniuFileUtils;
import com.sys.utils.RespObj;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * Created by jerry on 2016/9/7.
 * Banner管理
 */
@Api(value="Banner管理")
@Controller
public class BannerController {

    private BannerDao bannerDao = new BannerDao();

    private BannerDao appBannerDao = new BannerDao();
    @ApiOperation(value = "manager", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/vv/banners")
    @UserRoles(UserRole.DISCUSS_MANAGER) //这个代表只有管理员可以访问
    public String manager(Map<String, Object> map) {
        List<Banner> banners = bannerDao.getBanners();
        map.put("banners", banners);
        map.put("count", bannerDao.countBanner(2));
        return "/admin/banner";
    }
    @ApiOperation(value = "add", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/vv/add")
    @UserRoles(UserRole.DISCUSS_MANAGER)//这个代表只有管理员可以访问
    public String add(String name, String targetId, int status, @RequestParam("file") MultipartFile file) throws Exception {
        if (!file.isEmpty()) {
            String fileName = String.valueOf(System.currentTimeMillis()) + ".jpg";
            QiniuFileUtils.uploadFile(fileName, file.getInputStream(), QiniuFileUtils.TYPE_SOUND);
            String image = QiniuFileUtils.getPath(QiniuFileUtils.TYPE_SOUND, fileName);
            BannerEntity entity = new BannerEntity.Builder().setName(name)
                    .setTargetId(targetId)
                    .setImageUrl(image)
                    .setStatus(status).build();
            bannerDao.save(entity);
        }
        return "redirect:banners.do";
    }
    @ApiOperation(value = "delete", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/vv/delete")
    @ResponseBody
    @UserRoles(UserRole.DISCUSS_MANAGER) //这个代表只有管理员可以访问
    public RespObj delete(String id) {
        bannerDao.delete(id);
        return RespObj.SUCCESS;
    }

    /**
     * action 1:下线
     * 2:上线
     *
     * @param action
     * @param id
     * @return
     */
    @ApiOperation(value = "下线", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/vv/updateStatus")
    @ResponseBody
    @UserRoles(UserRole.DISCUSS_MANAGER) //这个代表只有管理员可以访问
    public RespObj bannerAction(@RequestParam(value = "action", defaultValue = "1") int action, String id) {
        bannerDao.updateStatus(id, action);
        return RespObj.SUCCESS;
    }


    /**
     * 给App的banner
     *
     * @return
     */
    @ApiOperation(value = "给App的banner", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/v1/app/banner")
    @SessionNeedless
    @ResponseBody
    public RespObj getBanners() {
        return RespObj.SUCCESS(AppBanner.getList(appBannerDao.get()));
    }
    @ApiOperation(value = "deleteApp", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @ResponseBody
    @RequestMapping("/v1/app/deleteBanner")
    @UserRoles(UserRole.DISCUSS_MANAGER)//这个代表只有管理员可以访问
    public RespObj deleteApp(String id) {
        appBannerDao.deleteAppBanner(id);
        return RespObj.SUCCESS;
    }
    @ApiOperation(value = "bannerActionApp", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/v1/app/updateStatus")
    @ResponseBody
    @UserRoles(UserRole.DISCUSS_MANAGER) //这个代表只有管理员可以访问
    public RespObj bannerActionApp(@RequestParam(value = "action", defaultValue = "1") int action, String id) {
        appBannerDao.updateAppStatus(id, action);
        return RespObj.SUCCESS;
    }
    @ApiOperation(value = "insertBanner", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/v1/app/insertBanner")
    @UserRoles(UserRole.DISCUSS_MANAGER) //这个代表只有管理员可以访问
    public String insertBanner(String goodId, int status, String goodName,
                               @RequestParam("file") MultipartFile file) throws Exception {
        if (!file.isEmpty()) {
            String fileName = String.valueOf(System.currentTimeMillis());
            QiniuFileUtils.uploadFile(fileName, file.getInputStream(), QiniuFileUtils.TYPE_SOUND);
            String image = QiniuFileUtils.getPath(QiniuFileUtils.TYPE_SOUND, fileName);
            appBannerDao.save(new AppBannerEntry(image, goodName, new ObjectId(goodId), status));
        }
        return "redirect:/admin/appBannerManager";
    }
    @ApiOperation(value = "insertBanner", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/v1/app/countBanner")
    @ResponseBody
    @UserRoles(UserRole.DISCUSS_MANAGER) //这个代表只有管理员可以访问
    public RespObj insertBanner(int status) {
        return RespObj.SUCCESS(appBannerDao.countBanner(status));
    }
}
