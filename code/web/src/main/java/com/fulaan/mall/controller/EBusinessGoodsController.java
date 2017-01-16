package com.fulaan.mall.controller;

import com.fulaan.annotation.LoginInfo;
import com.fulaan.annotation.ObjectIdType;
import com.fulaan.annotation.SessionNeedless;
import com.fulaan.base.BaseController;
import com.fulaan.cache.CacheHandler;
import com.fulaan.forum.service.FLogService;
import com.fulaan.mall.service.EGoodsCategoryService;
import com.fulaan.mall.service.EGoodsCommentService;
import com.fulaan.mall.service.EGoodsLogService;
import com.fulaan.mall.service.EGoodsService;
import com.fulaan.user.service.UserService;
import com.fulaan.utils.QiniuFileUtils;
import com.pojo.app.FileUploadDTO;
import com.pojo.app.SessionValue;
import com.pojo.ebusiness.*;
import com.pojo.forum.FLogDTO;
import com.pojo.user.UserEntry;
import com.pojo.user.UserRole;
import com.sys.constants.Constant;
import com.sys.exceptions.IllegalParamException;
import com.sys.utils.MD5Utils;
import com.sys.utils.RespObj;
import com.sys.utils.ValidationUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fl on 2016/1/14.
 */
@Controller
@RequestMapping("/mall")
public class EBusinessGoodsController extends BaseController {
    private static final Logger EBusinessLog = Logger.getLogger("EBusiness");

    @Autowired
    private EGoodsService eGoodsService;
    @Autowired
    private EGoodsCommentService eGoodsCommentService;
    @Autowired
    private EGoodsLogService eGoodsLogService;
    @Autowired
    private EGoodsCategoryService eGoodsCategoryService;
    @Autowired
    private UserService userService;
    @Autowired
    private FLogService fLogService;

    @SessionNeedless
    @RequestMapping("/index")
    @LoginInfo
    public String index(HttpServletRequest request, Map<String, Object> model) {
        loginInfo(model);

        FLogDTO fLogDTO = new FLogDTO();
        fLogDTO.setActionName("mall");
        SessionValue sv = getSessionValue();
        if (null != sv && !sv.isEmpty()) {
            fLogDTO.setPersonId(sv.getId());
        }
        fLogDTO.setPath("/mall/index.do");
        fLogDTO.setTime(System.currentTimeMillis());
        if (StringUtils.isNotBlank(request.getParameter("categoryId"))) {
            fLogDTO.setKeyId(request.getParameter("categoryId"));
            String item = request.getParameter("categoryId");
            if ("56eb6a1d0cf234ce7e479c24".equals(item)) {
                fLogDTO.setActionName("mallA");
            } else if ("56eb6a0b0cf234ce7e479c1c".equals(item)) {
                fLogDTO.setActionName("mallB");
            } else if ("56eb6a2d0cf234ce7e479c26".equals(item)) {
                fLogDTO.setActionName("mallC");
            } else if ("56eb6a350cf234ce7e479c27".equals(item)) {
                fLogDTO.setActionName("mallD");
            } else if ("56eb6a440cf234ce7e479c29".equals(item)) {
                fLogDTO.setActionName("mallD");
            }
        }
        fLogService.addFLog(fLogDTO);

        model.put("categoryId", request.getParameter("categoryId"));
        model.put("levelCategoryId", request.getParameter("levelCategoryId"));
        return "/mall/index";
    }

    @SessionNeedless
    @RequestMapping("/detail")
    @LoginInfo
    public String detail(HttpServletRequest request, Map<String, Object> model) {
        loginInfo(model);

        FLogDTO fLogDTO = new FLogDTO();
        fLogDTO.setActionName("productDetail");
        SessionValue sv = getSessionValue();
        if (null != sv && !sv.isEmpty()) {
            fLogDTO.setPersonId(sv.getId());
        }
        fLogDTO.setPath("/mall/detail.do");
        fLogDTO.setTime(System.currentTimeMillis());
        if (StringUtils.isNotBlank(request.getParameter("id"))) {
            fLogDTO.setKeyId(request.getParameter("id"));
        }
        fLogService.addFLog(fLogDTO);

        ObjectId goodsId = new ObjectId(request.getParameter("id"));
        model.put("isCollection", eGoodsLogService.isCollected(getUserId(), goodsId));
        return "/mall/detail";
    }

    @SessionNeedless
    @RequestMapping("/register")
    public String register(HttpServletRequest request, Map<String, Object> model) {
        loginInfo(model);
        return "/mall/register";
    }


    @SessionNeedless
    @RequestMapping("/loginInfo")
    @ResponseBody
    public Map<String, Object> getLoginInfo(HttpServletRequest request) {
        Map<String, Object> model = new HashMap<String, Object>();
        loginInfo(model);
        return model;
    }

    @SessionNeedless
    @RequestMapping("/discount")
    public String toDiscountPage(HttpServletRequest request, Map<String, Object> model) {
        loginInfo(model);
        return "/mall/discount";
    }


    @SessionNeedless
    @RequestMapping("/discount_h5")
    public String toDiscountPage_h5(Map<String, Object> model) {
        loginInfo(model);
        return "/mall/h5_discount";
    }


    private void loginInfo(Map<String, Object> model) {
        SessionValue sessionValue = getSessionValue();
        if (null == sessionValue || sessionValue.isEmpty()) {
            model.put("userName", "");
            model.put("userId", "");
            model.put("login", false);
            model.put("k6kt", -1);
        } else {
            model.put("userName", sessionValue.getUserName());
            model.put("userId", sessionValue.getId());
            model.put("login", true);
            model.put("k6kt", sessionValue.getK6kt());
            model.put("avatar", sessionValue.getMinAvatar());
            model.put("isStudent", UserRole.isStudent(sessionValue.getUserRole()));
        }
    }

    /**
     * 商品列表
     *
     * @param state         0正常销售 1已经下架 2未发布
     * @param sortType
     * @param page
     * @param pageSize
     * @param goodsCategory
     * @return
     */
    @SessionNeedless
    @RequestMapping("/goods")
    @ResponseBody
    public Map<String, Object> goodList(@RequestParam(required = false, defaultValue = "0") int state,
                                        @RequestParam(required = false, defaultValue = "") String goodsCategory,
                                        @RequestParam(required = false, defaultValue = "") String gradeCategory,
                                        @RequestParam(required = false, defaultValue = "0") int bookCategory,
                                        @RequestParam(required = false, defaultValue = "") String price,
                                        @RequestParam(required = false, defaultValue = "") String regular,
                                        @RequestParam(required = false, defaultValue = "-1") int activity,
                                        @RequestParam(required = false, defaultValue = "-1") int groupPurchase,
                                        @RequestParam(required = false, defaultValue = "-1") int maxOut,
                                        @RequestParam(required = false, defaultValue = "") String levelGoodsCategory,
                                        int sortType, int page, int pageSize) {
        Map<String, Object> model = new HashMap<String, Object>();
        if (goodsCategory.equals("ALL")) {
            goodsCategory = "";
        }
        int[] priceArr = new int[2];
        if (!price.equals("")) {
            String[] priceStr = price.split("~");
            if (priceStr.length == 2) {
                for (int i = 0; i < priceStr.length; i++) {
                    priceArr[i] = Integer.valueOf(priceStr[i]) * 100;
                }
            } else {
                priceArr[0] = Integer.valueOf(priceStr[0]) * 100;
                priceArr[1] = Integer.MAX_VALUE;
            }
        } else {
            priceArr = null;
        }
        List<EGoodsDTO> eGoodsDTOList = eGoodsService.getEGoodsList(state, activity, groupPurchase, maxOut, sortType, goodsCategory, levelGoodsCategory, gradeCategory, bookCategory, priceArr, regular, page, pageSize);
        int count = eGoodsService.getEGoodsCount(state, activity, groupPurchase, maxOut, goodsCategory, levelGoodsCategory, gradeCategory, bookCategory, priceArr, regular);
        model.put("list", eGoodsDTOList);
        model.put("count", count);
        EBusinessLog.info("GoodsList: UserId = " + getUserId() + "\tSortType = " + sortType + "\tPage = " + page);
        return model;
    }

    /**
     * 商品列表
     *
     * @param state 0正常销售 1已经下架 2未发布
     * @return
     */
    @SessionNeedless
    @RequestMapping("/integrateGoods")
    @ResponseBody
    public Map<String, Object> integrate(@RequestParam(required = false, defaultValue = "0") int state,
                                         @RequestParam(required = false, defaultValue = "-1") int activity,
                                         @RequestParam(required = false, defaultValue = "-1") int groupPurchase,
                                         @RequestParam(required = false, defaultValue = "") String goodsCategory,
                                         @RequestParam(required = false, defaultValue = "0") int bookCategory,
                                         @RequestParam(required = false, defaultValue = "") String regular,
                                         @RequestParam(required = false, defaultValue = "") String levelGoodsCategory,
                                         int sortType, int page, int pageSize) {
        Map<String, Object> model = new HashMap<String, Object>();
        if (goodsCategory.equals("ALL")) {
            goodsCategory = "";
        }
        List<EGoodsDTO> eGoodsDTOList = eGoodsService.getEGoodsList(state, activity, groupPurchase, -1, sortType, goodsCategory, levelGoodsCategory, "", bookCategory, null, regular, page, pageSize);
        int count = eGoodsService.getEGoodsCount(state, activity, groupPurchase, -1, goodsCategory, levelGoodsCategory, "", bookCategory, null, regular);
        model.put("list", eGoodsDTOList);
        model.put("count", count);
        EBusinessLog.info("GoodsList: UserId = " + getUserId() + "\tSortType = " + sortType + "\tPage = " + page);
        return model;
    }

    /**
     * 商品爆款列表
     *
     * @param state              0正常销售 1已经下架 2未发布
     * @param activity
     * @param groupPurchase
     * @param maxOut
     * @param goodsCategory
     * @param bookCategory
     * @param regular
     * @param levelGoodsCategory
     * @param sortType
     * @param page
     * @param pageSize
     * @return
     */
    @SessionNeedless
    @RequestMapping("/maxOut")
    @ResponseBody
    public Map<String, Object> maxOut(@RequestParam(required = false, defaultValue = "0") int state,
                                      @RequestParam(required = false, defaultValue = "-1") int activity,
                                      @RequestParam(required = false, defaultValue = "-1") int groupPurchase,
                                      @RequestParam(required = false, defaultValue = "-1") int maxOut,
                                      @RequestParam(required = false, defaultValue = "") String goodsCategory,
                                      @RequestParam(required = false, defaultValue = "0") int bookCategory,
                                      @RequestParam(required = false, defaultValue = "") String regular,
                                      @RequestParam(required = false, defaultValue = "") String levelGoodsCategory,
                                      int sortType, int page, int pageSize) {
        Map<String, Object> model = new HashMap<String, Object>();
        if (goodsCategory.equals("ALL")) {
            goodsCategory = "";
        }
        List<EGoodsDTO> eGoodsDTOList = eGoodsService.getEGoodsList(state, activity, groupPurchase, maxOut, sortType, goodsCategory, levelGoodsCategory, "", bookCategory, null, regular, page, pageSize);
        int count = eGoodsService.getEGoodsCount(state, activity, groupPurchase, maxOut, goodsCategory, levelGoodsCategory, "", bookCategory, null, regular);
        model.put("list", eGoodsDTOList);
        model.put("count", count);
        EBusinessLog.info("GoodsList: UserId = " + getUserId() + "\tSortType = " + sortType + "\tPage = " + page);
        return model;
    }

    /**
     * 商品详情
     */
    @SessionNeedless
    @RequestMapping(value = "/goods/{goodsId}", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> goodsDetail(@PathVariable("goodsId") @ObjectIdType ObjectId goodsId) {
        Map<String, Object> model = new HashMap<String, Object>();
        EGoodsDTO dto = eGoodsService.detail(goodsId);
        model.put("dto", dto);
        EBusinessLog.info("GoodsDetail: UserId = " + getUserId() + "\tGoodsId = " + goodsId);
        eGoodsService.updatePopular(goodsId);//增加人气
        eGoodsLog(dto, 1);//浏览记录
        return model;
    }

    @SessionNeedless
    @RequestMapping(value = "/goods/{goodsId}/comments", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> commentList(@PathVariable("goodsId") @ObjectIdType ObjectId goodsId,
                                           @RequestParam(required = false, defaultValue = "0") int onlyImg, int page, int pageSize) {
        Map<String, Object> model = new HashMap<String, Object>();
        List<EGoodCommentDTO> eGoodCommentDTOList = eGoodsCommentService.getEGoodComments(goodsId, onlyImg, page, pageSize);
        int count = eGoodsCommentService.getCommentCount(goodsId);
        model.put("list", eGoodCommentDTOList);
        model.put("count", count);
        return model;
    }

    /**
     * 新增评论
     *
     * @param goodsId
     * @param eGoodCommentDTO
     * @return
     */
    @RequestMapping(value = "/goods/{goodsId}/comments", method = RequestMethod.POST)
    @ResponseBody
    public RespObj addComment(@PathVariable("goodsId") String goodsId, @RequestBody EGoodCommentDTO eGoodCommentDTO) {
        RespObj respObj = RespObj.FAILD;
        try {
            EGoodsEntry eGoodsEntry = eGoodsService.getEGoodsEntry(new ObjectId(goodsId));
            if (null == eGoodsEntry) {
                respObj.setMessage("商品不存在");
            } else {
                eGoodCommentDTO.setGoodsId(goodsId);
                eGoodCommentDTO.setUserId(getSessionValue().getId());
                eGoodsCommentService.addComment(eGoodCommentDTO);
                respObj = RespObj.SUCCESS;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return respObj;
    }

    @RequestMapping(value = "/images", method = RequestMethod.POST)
    @ResponseBody
    public RespObj uploadImage(MultipartRequest request) {
        RespObj obj = RespObj.FAILD;
        List<FileUploadDTO> fileInfos = new ArrayList<FileUploadDTO>();
        try {
            Map<String, MultipartFile> fileMap = request.getFileMap();
            for (MultipartFile file : fileMap.values()) {
                String id = new ObjectId().toString();
                String fileKey = id + Constant.POINT + FilenameUtils.getExtension(file.getOriginalFilename());
                QiniuFileUtils.uploadFile(fileKey, file.getInputStream(), QiniuFileUtils.TYPE_IMAGE);
                String path = QiniuFileUtils.getPath(QiniuFileUtils.TYPE_DOCUMENT, fileKey);
                FileUploadDTO dto = new FileUploadDTO(id, fileKey, file.getOriginalFilename(), path);
                fileInfos.add(dto);
            }
            obj = new RespObj(Constant.SUCCESS_CODE, fileInfos);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return obj;
    }


    /**
     * 记录浏览日志
     *
     * @param type 1:浏览 2:收藏
     */
    private void eGoodsLog(EGoodsDTO dto, int type) {
        EGoodsLogEntry eGoodsLogEntry = new EGoodsLogEntry();
        eGoodsLogEntry.setIp(getIP());
        eGoodsLogEntry.setGoodsName(dto.getGoodsName());
        eGoodsLogEntry.setGoodsId(new ObjectId(dto.getGoodsId()));
        eGoodsLogEntry.setPrice(dto.getPrice());
        eGoodsLogEntry.setType(type);
        if (getUserId() == null) {//未登录
            eGoodsLogEntry.setUserName("");
            eGoodsLogEntry.setUserId(null);
            eGoodsLogEntry.setSchoolName("");
            eGoodsLogEntry.setRole(0);
            eGoodsLogEntry.setIsLogin(0);
        } else {
            eGoodsLogEntry.setUserName(getSessionValue().getUserName());
            eGoodsLogEntry.setUserId(getUserId());
            eGoodsLogEntry.setSchoolName(getSessionValue().getSchoolName());
            eGoodsLogEntry.setRole(getSessionValue().getUserRole());
            eGoodsLogEntry.setIsLogin(1);
        }

        eGoodsLogService.addEGoodsLog(getUserId(), eGoodsLogEntry);
    }

    @SessionNeedless
    @RequestMapping(value = "/categories", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> categories(
            @RequestParam(required = false, defaultValue = "1") int level,
            @RequestParam(required = false, defaultValue = "") String parentId) {
        Map<String, Object> model = new HashMap<String, Object>();
        ObjectId e = null;
        if (!"".equals(parentId)) {
            e = new ObjectId(parentId);
        }
        List<EGoodsCategoryDTO> goodsCategoryDTOs = eGoodsCategoryService.getEGoodsCategoryListByLevel(level, e);
        List<EGradeCategoryDTO> gradeCategoryDTOs = eGoodsCategoryService.getEGradeCategoryList();
        model.put("goodsCategories", goodsCategoryDTOs);
        model.put("gradeCategories", gradeCategoryDTOs);
        return model;
    }

    /**
     * 根据地区计算运费
     */
    @SessionNeedless
    @RequestMapping("/goods/{goodsId}/expressPrice")
    @ResponseBody
    public Map<String, Double> getExpressPrice(@ObjectIdType ObjectId goodsId, @RequestParam String province) {
        String p = "";
        try {
            p = URLDecoder.decode(province, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return eGoodsService.getExpressPrice(goodsId, p);
    }


    /**
     * 收藏商品
     */
    @RequestMapping(value = "/goods/{goodsId}/collection", method = RequestMethod.POST)
    @ResponseBody
    public RespObj collection(@PathVariable("goodsId") @ObjectIdType ObjectId goodsId) {
        RespObj respObj = RespObj.FAILD;
        if (eGoodsLogService.isCollected(getUserId(), goodsId)) {
            respObj.setMessage("您已收藏过该商品！");
        } else {
            try {
                EGoodsDTO dto = eGoodsService.detail(goodsId);
                eGoodsLog(dto, 2);
                respObj = RespObj.SUCCESS;
                respObj.setMessage("收藏成功！");
            } catch (Exception e) {
                e.printStackTrace();
                respObj.setMessage("收藏失败！");
            }
        }
        return respObj;
    }

    /**
     * 跳转到找回密码界面
     *
     * @return
     */
    @SessionNeedless
    @RequestMapping("/findUserPwd")
    public String findPwd(HttpServletRequest request, Map<String, Object> model) {
        loginInfo(model);
        return "/mall/findUserPwd";
    }


    /**
     * 手机验证身份
     *
     * @param userName
     * @param cacheKeyId
     * @param code
     * @param phoneNumber
     * @param response
     * @param request
     * @return
     */
    @SessionNeedless
    @RequestMapping(value = "/findUser/validate", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> validateUser(String userName, String cacheKeyId, String code, String phoneNumber,
                                            HttpServletResponse response, HttpServletRequest request) {
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("code", 500);
        Map<String, String> map = new HashMap<String, String>();


        UserEntry ue = userService.findByUserName(userName);

        if (null == ue) {
            model.put("message", "用户名错误");
            return model;
        }

        if (!ValidationUtils.isValidMobile(phoneNumber)) {
            model.put("message", "手机格式不对，非法手机");
            return model;
        }

        UserEntry mobileEntry = userService.findByMobile(phoneNumber);

        if (null != mobileEntry && !mobileEntry.getUserName().toLowerCase().equals(ue.getUserName())) {
            model.put("message", "此手机已经被占用");
            return model;
        }
        String cacheKey = CacheHandler.getKeyString(CacheHandler.CACHE_SHORTMESSAGE, cacheKeyId);
        String value = CacheHandler.getStringValue(cacheKey);
        if (StringUtils.isBlank(value)) {
            model.put("message", "验证码失效或者为空，请重新获取");
            return model;
        }
        String[] cache = value.split(",");
        if (!cache[1].equals(phoneNumber)) {
            model.put("message", "验证失败：手机号码与验证码不匹配");
            return model;
        }

        if (cache[0].equals(code)) {
            model.put("message", "身份验证成功");
            model.put("code", 200);
        } else {
            model.put("message", "身份验证失败");
        }
        return model;
    }

    @SessionNeedless
    @RequestMapping("/findUser/userCheck/mobile")
    @ResponseBody
    public Map<String, Object> mobile(String username, HttpServletRequest request) {

        Map<String, Object> obj = new HashMap<String, Object>();

        obj.put("code", Constant.FAILD_CODE);
        if (StringUtils.isBlank(username)) {
            obj.put("message", "用户名为空");
            return obj;
        }

        UserEntry e = userService.findByUserName(username.toLowerCase());
        if (null == e) {
            obj.put("message", "找不到用户");
            return obj;
        }
        obj.put("code", Constant.SUCCESS_CODE);
        if(StringUtils.isNotBlank(e.getMobileNumber())) {
            obj.put("phone", e.getMobileNumber());
        }
        obj.put("k6kt", e.getK6KT());
        int type;

        if (UserRole.isStudent(e.getRole())) {
            type = 0;
        } else if (UserRole.isParent(e.getRole())) {
            type = 1;
        } else if (UserRole.TEACHER.getRole() == e.getRole()) {
            type = 2;
        } else {
            type = 3;
        }
        obj.put("role", type);
        return obj;
    }


    @SessionNeedless
    @RequestMapping("/findUser/userCheck")
    @ResponseBody
    public Map<String, Object> checkFirst(String username, String vCode, HttpServletRequest request) {

        Map<String, Object> obj = new HashMap<String, Object>();

        obj.put("code", Constant.FAILD_CODE);
        if (StringUtils.isBlank(username) || StringUtils.isBlank(vCode)) {
            obj.put("message", "用户名或验证码为空");
            return obj;
        }

        UserEntry e = userService.findByUserName(username.toLowerCase());
        if (null == e) {
            obj.put("message", "找不到用户");
            return obj;
        }

        String cookieValue = getCookieValue(Constant.COOKIE_VALIDATE_CODE);

        String key = CacheHandler.getKeyString(CacheHandler.CACHE_VALIDATE_CODE, cookieValue);

        String rightCode = CacheHandler.getStringValue(key);

        if (StringUtils.isBlank(rightCode)) {
            obj.put("message", "验证码失效");
            return obj;
        }

        if (!rightCode.equalsIgnoreCase(vCode)) {
            obj.put("message", "验证码错误");
            return obj;
        }

        obj.put("code", Constant.SUCCESS_CODE);
        obj.put("phone", e.getMobileNumber());
        obj.put("k6kt", e.getK6KT());
        int type;

        if (UserRole.isStudent(e.getRole())) {
            type = 0;
        } else if (UserRole.isParent(e.getRole())) {
            type = 1;
        } else if (UserRole.TEACHER.getRole() == e.getRole()) {
            type = 2;
        } else {
            type = 3;
        }
        obj.put("role", type);
        return obj;
    }

    /**
     * 重设密码
     */
    @SessionNeedless
    @ResponseBody
    @RequestMapping("/findUser/resetPwd/mobile")
    public RespObj reSetPwd(String username, String pwd, String pwdAgain, HttpServletRequest request) {
        RespObj obj = new RespObj(Constant.FAILD_CODE);
        UserEntry e = userService.findByUserName(username.toLowerCase());
        if (null == e) {
            obj.setMessage("找不到用户");
            return obj;
        }
        try {
            userService.update(e.getID(), "pw", MD5Utils.getMD5(pwdAgain));
        } catch (IllegalParamException e1) {
            EBusinessLog.error("", e1);
        } catch (Exception e1) {
            EBusinessLog.error("", e1);
        }

        return RespObj.SUCCESS;
    }

    /**
     * 重设密码
     *
     * @param username
     * @param vCode
     * @return
     */
    @SessionNeedless
    @ResponseBody
    @RequestMapping("/findUser/resetPwd")
    public RespObj reSetPwd(String username, String pwd, String pwdAgain, String vCode, HttpServletRequest request) {
        RespObj obj = new RespObj(Constant.FAILD_CODE);
        UserEntry e = userService.findByUserName(username.toLowerCase());
        if (null == e) {
            obj.setMessage("找不到用户");
            return obj;
        }
        String cookieValue = getCookieValue(Constant.COOKIE_VALIDATE_CODE);

        String key = CacheHandler.getKeyString(CacheHandler.CACHE_VALIDATE_CODE, cookieValue);

        String rightCode = CacheHandler.getStringValue(key);

        if (StringUtils.isNotBlank(vCode) && !rightCode.equalsIgnoreCase(vCode)) {
            obj.setMessage("验证码错误");
            return obj;
        }

        try {
            userService.update(e.getID(), "pw", MD5Utils.getMD5(pwdAgain));
        } catch (IllegalParamException e1) {
            EBusinessLog.error("", e1);
        } catch (Exception e1) {
            EBusinessLog.error("", e1);
        }

        return RespObj.SUCCESS;
    }
}
