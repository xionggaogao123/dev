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
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
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
@Controller
public class BannerController {

    private static final Logger logger = Logger.getLogger(BannerController.class);

    private BannerDao bannerDao = new BannerDao();

    private BannerDao appBannerDao = new BannerDao();

    @RequestMapping("/vv/banners")
    @UserRoles(UserRole.DISCUSS_MANAGER) //这个代表只有管理员可以访问
    public String manager(Map<String, Object> map) {
        List<Banner> banners = bannerDao.getBanners();
        map.put("banners", banners);
        map.put("count", bannerDao.countBanner(2));
        return "/admin/banner";
    }

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
    @RequestMapping("/v1/app/banner")
    @SessionNeedless
    @ResponseBody
    public RespObj getBanners() {
        return RespObj.SUCCESS(AppBanner.getList(appBannerDao.get()));
    }

    @ResponseBody
    @RequestMapping("/v1/app/deleteBanner")
    @UserRoles(UserRole.DISCUSS_MANAGER)//这个代表只有管理员可以访问
    public RespObj deleteApp(String id) {
        appBannerDao.deleteAppBanner(id);
        return RespObj.SUCCESS;
    }

    @RequestMapping("/v1/app/updateStatus")
    @ResponseBody
    @UserRoles(UserRole.DISCUSS_MANAGER) //这个代表只有管理员可以访问
    public RespObj bannerActionApp(@RequestParam(value = "action", defaultValue = "1") int action, String id) {
        appBannerDao.updateAppStatus(id, action);
        return RespObj.SUCCESS;
    }

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

    @RequestMapping("/v1/app/countBanner")
    @ResponseBody
    @UserRoles(UserRole.DISCUSS_MANAGER) //这个代表只有管理员可以访问
    public RespObj insertBanner(int status) {
        return RespObj.SUCCESS(appBannerDao.countBanner(status));
    }
}
