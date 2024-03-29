package com.fulaan.admin;

import com.db.factory.MongoFacroty;
import com.db.fcommunity.BannerDao;
import com.db.forum.FSectionDao;
import com.db.user.UserDao;
import com.fulaan.annotation.ObjectIdType;
import com.fulaan.annotation.SessionNeedless;
import com.fulaan.annotation.UserRoles;
import com.fulaan.base.BaseController;
import com.fulaan.mall.service.*;
import com.fulaan.user.service.UserService;
import com.fulaan.forum.service.FInformationService;
import com.fulaan.forum.service.FPostService;
import com.fulaan.forum.service.FReplyService;
import com.fulaan.forum.service.FSectionService;
import com.mongodb.BasicDBObject;
import com.pojo.ebusiness.*;
import com.pojo.fcommunity.AppBanner;
import com.pojo.fcommunity.Banner;
import com.pojo.forum.FPostDTO;
import com.pojo.forum.FSectionCountDTO;
import com.pojo.forum.FSectionDTO;
import com.pojo.forum.FSectionEntry;
import com.pojo.user.UserEntry;
import com.pojo.user.UserRole;
import com.sys.constants.Constant;
import com.sys.exceptions.IllegalParamException;
import com.sys.utils.DateTimeUtils;
import com.sys.utils.RespObj;
import io.swagger.annotations.*;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Created by fl on 2016/1/29.
 * 管理员
 */
@Api(value="管理员")
@RequestMapping("/admin")
@Controller
public class AdminController extends BaseController {

    private static final Logger EBusinessLog = Logger.getLogger(AdminController.class);
    @Autowired
    private EGoodsService eGoodsService;
    @Autowired
    private EGoodsCommentService eGoodsCommentService;
    @Autowired
    private UserService userService;
    @Autowired
    private EGoodsCategoryService eGoodsCategoryService;
    @Autowired
    private EBusinessOrderService eBusinessOrderService;
    @Autowired
    private EExpressTemplateService eExpTemplateService;
    @Autowired
    private EBusinessVoucherService eBusinessVoucherService;
    @Autowired
    private ECategoryVideoService eCategoryVideoService;
    @Autowired
    private FSectionService fSectionService;
    @Autowired
    private FPostService fPostService;
    private BannerDao appBannerDao = new BannerDao();
    @Autowired
    private FInformationService fInformationService;
    @Autowired
    private FReplyService fReplyService;

    private UserDao mUserDao = new UserDao();
    private FSectionDao fSectionDao = new FSectionDao();

    /**
     * 管理员入口
     *
     * @return /admin/admin
     */
    @ApiOperation(value = "管理员入口", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping(method = RequestMethod.GET)
    @UserRoles(UserRole.DISCUSS_MANAGER)
    public String showAdminPage() {
        return "/admin/admin";
    }

    /**
     * 添加评论、删除商品页面
     *
     * @return /admin/addComment
     */
    @ApiOperation(value = "添加评论、删除商品页面", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/addComment")
    @UserRoles(UserRole.DISCUSS_MANAGER)
    public String showCommentPage() {
        return "/admin/addComment";
    }

    /**
     * 添加/编辑商品页面
     *
     * @return /admin/upsetGoods
     */
    @ApiOperation(value = "添加/编辑商品页面", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/upsetGoods")
    @UserRoles(UserRole.DISCUSS_MANAGER)
    public String upsetGoods() {
        return "/admin/upsetGoods";
    }

    /**
     * 编辑帖子页面
     *
     * @return /admin/setActivity
     */
    @ApiOperation(value = "编辑帖子页面", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/setActivity")
    @UserRoles(UserRole.DISCUSS_MANAGER)
    public String setActivity() {
        return "/admin/setActivity";
    }

    /**
     * 商品分类
     *
     * @return /admin/category
     */
    @ApiOperation(value = "商品分类", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/category")
    @UserRoles(UserRole.DISCUSS_MANAGER)
    public String eGoodsCategory() {
        return "/admin/category";
    }

    /**
     * 板块管理
     *
     * @return /admin/section
     */
    @ApiOperation(value = "板块管理", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/Section")
    @UserRoles(UserRole.DISCUSS_MANAGER)
    public String section() {
        return "/admin/section";
    }

    /**
     * 系统消息管理
     *
     * @return admin/systemMessage
     */
    @ApiOperation(value = "系统消息管理", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/SystemMessage")
    @UserRoles(UserRole.DISCUSS_MANAGER)
    public String showSystemMessage() {
        return "admin/systemMessage";
    }
    @ApiOperation(value = "optimizationPlate", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/optimizationPlate")
    @UserRoles(UserRole.DISCUSS_MANAGER)
    public String optimizationPlate() {
        return "/admin/optimizationPlate";
    }

    /**
     * 年级分类
     */
    @ApiOperation(value = "年级分类", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/gradeCategory")
    @UserRoles(UserRole.DISCUSS_MANAGER)
    public String eGradeCategory() {
        return "/admin/gradeCategory";
    }

    /**
     * 商品管理
     */
    @ApiOperation(value = "商品管理", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/goodsManage")
    @UserRoles(UserRole.DISCUSS_MANAGER)
    public String goodsManage() {
        return "/admin/goodsManage";
    }

    /**
     * 订单管理
     *
     * @return
     */
    @ApiOperation(value = "订单管理", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/manageOrders")
    @UserRoles(UserRole.DISCUSS_MANAGER)
    public String manageOrders() {
        return "/admin/manageOrders";
    }

    /**
     * 抵用券管理
     *
     * @return
     */
    @ApiOperation(value = "抵用券管理", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/manageVouchers")
    @UserRoles(UserRole.DISCUSS_MANAGER)
    public String manageVouchers() {
        return "/admin/manageVouchers";
    }


    /**
     * 商品分类上传视频
     */
    @ApiOperation(value = "商品分类上传视频", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/uploadVideo")
    @UserRoles(UserRole.DISCUSS_MANAGER)
    public String uploadVideo() {
        return "/admin/uploadVideo";
    }


    /**
     * 论坛等级界面
     *
     * @return
     */
    @ApiOperation(value = "论坛等级界面", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/level")
    @UserRoles(UserRole.DISCUSS_MANAGER)//这个代表只有管理员可以访问
    public String level() {
        return "/admin/level";
    }

    /**
     * app端banner 管理
     *
     * @param model
     * @return appBanner page
     */
    @ApiOperation(value = "app端banner 管理", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/appBannerManager")
    @UserRoles(UserRole.DISCUSS_MANAGER)
    public String appBannerManager(Map<String, Object> model) {
        List<AppBanner> banners = AppBanner.getList(appBannerDao.get());
        model.put("banners", Banner.appBannerReverse(banners));
        model.put("count", appBannerDao.getAppBannerCount(2));
        return "/admin/appBanner";
    }

    /**
     * PC端banner管理
     *
     * @return page
     */
    @ApiOperation(value = "PC端banner管理", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/bannerManager")
    @UserRoles(UserRole.DISCUSS_MANAGER)
    public String bannerManager() {
        return "/admin/banner";
    }


    /**
     * 处理数据
     * @return
     */
    @ApiOperation(value = "处理数据", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/institute")
    @UserRoles(UserRole.DISCUSS_MANAGER)
    public String instituteData() {
        return "/admin/institute";
    }

    /**
     * 处理数据
     * @return
     */
    @ApiOperation(value = "处理数据", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/batchImage")
    @UserRoles(UserRole.DISCUSS_MANAGER)
    public String batchImage() {
        return "/admin/batchImage";
    }


    /**
     * 管理评论
     * @return
     */
    @ApiOperation(value = "管理评论", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/manageCriticism")
    @UserRoles(UserRole.DISCUSS_MANAGER)
    public String manageCriticism() {
        return "/admin/manageCriticism";
    }

    /**
     * 管理评论
     * @return
     */
    @ApiOperation(value = "管理评论", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/manageItemType")
    @UserRoles(UserRole.DISCUSS_MANAGER)
    public String manageItemType() {
        return "/admin/manageItemType";
    }

    /**
     * 板块管理
     *
     * @param map
     * @return sectionManage page
     */
    @ApiOperation(value = "板块管理", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/sectionManage")
    @UserRoles(UserRole.DISCUSS_MANAGER)
    public String sectionManage(Map<String, Object> map) {
        List<FSectionCountDTO> fSectionDTOs = fSectionService.getFSectionList(1);
        map.put("sections", fSectionDTOs);
        map.put("masters", mUserDao.getMasters());
        map.put("managers", mUserDao.getManagers());
        return "/admin/sectionManage";
    }

    /**
     * 添加评论
     *
     * @param goodsId
     * @param eGoodCommentDTO
     * @return
     */
    @ApiOperation(value = "添加评论", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping(value = "/goods/{goodsId}/comments", method = RequestMethod.POST)
    @ResponseBody
    @UserRoles(UserRole.DISCUSS_MANAGER)
    public RespObj adminAddComment(@PathVariable("goodsId") String goodsId, @RequestBody EGoodCommentDTO eGoodCommentDTO) {
        EBusinessLog.info("Admin-addComment: UserId = " + getUserId() + "\tUserName = " + getSessionValue().getUserName() + "\tGoodsId = " + goodsId);
        RespObj respObj = RespObj.FAILD;
        try {
            EGoodsEntry eGoodsEntry = eGoodsService.getEGoodsEntry(new ObjectId(goodsId));
            if (null == eGoodsEntry) {
                respObj.setMessage("商品不存在");
            } else {
                eGoodCommentDTO.setGoodsId(goodsId);
                eGoodCommentDTO.setUserId(userService.randomAnId().toString());
                eGoodsCommentService.addComment(eGoodCommentDTO);
                respObj = RespObj.SUCCESS;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return respObj;
    }


    //=====================================================活动帖子管理===========================================================

    /**
     * 更新帖子信息
     *
     * @param fpostId
     * @param fPostDTO
     * @return
     */
    @ApiOperation(value = "更新帖子信息", httpMethod = "PUT", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping(value = "/post/{fpostId}", method = RequestMethod.PUT)
    @ResponseBody
    @UserRoles(UserRole.DISCUSS_MANAGER)
    public RespObj updatePost(@PathVariable("fpostId") @ObjectIdType ObjectId fpostId, @RequestBody FPostDTO fPostDTO) {
        RespObj respObj = RespObj.SUCCESS;
        FPostDTO fPostDTO1 = fPostService.detail(fpostId);
        if (fPostDTO.getCate() == 1) {
            if (fPostDTO.getInSet() == 1) { //上线
                fPostDTO1.setInSet(1);
                fPostDTO1.setCate(0);
                fPostDTO1.setUpdateDateTime(System.currentTimeMillis());
                if (StringUtils.isNotBlank(fPostDTO.getActivityMemo())) {
                    fPostDTO1.setActivityMemo(fPostDTO.getActivityMemo());
                }
                if (StringUtils.isNotBlank(fPostDTO.getActivityImage())) {
                    fPostDTO1.setActivityImage(fPostDTO.getActivityImage());
                }
                if (StringUtils.isNotBlank(fPostDTO.getActivityStartTime())) {
                    fPostDTO1.setActivityStartTime(fPostDTO.getActivityStartTime());
                }
                if (StringUtils.isNotBlank(fPostDTO.getActionHeadImage())){
                    fPostDTO1.setActionHeadImage(fPostDTO.getActionHeadImage());
                }
                if (StringUtils.isNotBlank(fPostDTO.getActivityEndTime())) {
                    fPostDTO1.setActivityEndTime(fPostDTO.getActivityEndTime());
                }
            } else { //下线
                fPostDTO1.setInSet(-1);
                fPostDTO1.setCate(1);
            }
        } else {
            fPostDTO1.setInSet(-1);//普通贴
        }
        fPostService.addFPostEntry(fPostDTO1);
        respObj.setMessage("编辑成功");
        return respObj;
    }

    //=====================================================商品管理===========================================================

    /**
     * 更新商品信息
     *
     * @param goodsId   商品id
     * @param eGoodsDTO 商品DTO
     * @return RespObj
     */
    @ApiOperation(value = "更新商品信息", httpMethod = "PUT", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping(value = "/goods/{goodsId}", method = RequestMethod.PUT)
    @ResponseBody
    @UserRoles(UserRole.DISCUSS_MANAGER)
    public RespObj updateGoods(@PathVariable("goodsId") @ObjectIdType ObjectId goodsId, @RequestBody EGoodsDTO eGoodsDTO) {
        EBusinessLog.info("Admin-updateGoods: UserId = " + getUserId() + "\tUserName = " + getSessionValue().getUserName() + "\tGoodsId = " + goodsId);
        RespObj respObj = RespObj.FAILD;
        EGoodsEntry eGoodsEntry = eGoodsService.getEGoodsEntry(goodsId);
        if (null == eGoodsEntry) {
            respObj.setMessage("商品不存在！");
        } else {
            try {
                eGoodsService.addEGoodsEntry(eGoodsDTO);
                respObj = RespObj.SUCCESS;
                respObj.setMessage("编辑成功");
            } catch (Exception e) {
                e.printStackTrace();
                respObj.setMessage("编辑失败");
            }
        }
        return respObj;
    }

    /**
     * 添加商品
     *
     * @param eGoodsDTO
     * @return
     */
    @ApiOperation(value = "添加商品", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping(value = "/goods", method = RequestMethod.POST)
    @ResponseBody
    @UserRoles(UserRole.DISCUSS_MANAGER)
    public RespObj addGoods(@RequestBody EGoodsDTO eGoodsDTO) {
        RespObj respObj = RespObj.FAILD;
        try {
            ObjectId goodsId = eGoodsService.addEGoodsEntry(eGoodsDTO);
            respObj = RespObj.SUCCESS;
            respObj.setMessage("添加成功");
            EBusinessLog.info("Admin-addGoods: UserId = " + getUserId() + "\tUserName = " + getSessionValue().getUserName() + "\tGoodsId = " + goodsId);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setMessage("添加失败");
        }
        return respObj;
    }

    /**
     * 删除商品信息
     *
     * @param goodsId
     * @return
     */
    @ApiOperation(value = "删除商品信息", httpMethod = "DELETE", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping(value = "/goods/{goodsId}", method = RequestMethod.DELETE)
    @ResponseBody
    @UserRoles(UserRole.DISCUSS_MANAGER)
    public RespObj deleteGoods(@PathVariable("goodsId") @ObjectIdType ObjectId goodsId) {
        EBusinessLog.info("Admin-deleteGoods: UserId = " + getUserId() + "\tUserName = " + getSessionValue().getUserName() + "\tGoodsId = " + goodsId);
        RespObj respObj = RespObj.FAILD;
        try {
            eGoodsService.deleteEGoods(goodsId);
            respObj = RespObj.SUCCESS;
            respObj.setMessage("删除成功");
        } catch (Exception e) {
            respObj.setMessage("删除失败");
        }
        return respObj;
    }


    /**
     * 商品列表
     */
    @ApiOperation(value = "商品列表", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = Map.class)})
    @RequestMapping("/goodsList")
    @ResponseBody
    @UserRoles(UserRole.DISCUSS_MANAGER)
    public Map<String, Object> goodsList(int state, int page, int pageSize) {
        List<EGoodsDTO> goodsDTOList = eGoodsService.getEGoodsList(state, -1, -1, -1, 2, "", "", "", 0, null, null, page, pageSize);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("goodsList", goodsDTOList);
        result.put("page", page);
        result.put("pageSize", pageSize);
        result.put("count", eGoodsService.getEGoodsCount(state, -1, -1, -1, "", "", "", 0, null, ""));
        return result;
    }

    //=====================================================板块管理===========================================================

    /**
     * 板块列表
     *
     * @param level
     * @return
     */
    @ApiOperation(value = "板块列表", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping(value = "/section", method = RequestMethod.GET)
    @ResponseBody
    @UserRoles(value = {UserRole.DISCUSS_MANAGER, UserRole.DISCUSS_SECTION_MANAGER})
    public List<FSectionDTO> getSectionDTOs(@RequestParam(required = false, defaultValue = "1") int level,
                                            @RequestParam(required = false, defaultValue = "") String parentId) {
        ObjectId e = null;
        if (!"".equals(parentId)) {
            e = new ObjectId(parentId);
        }
        return fSectionService.getFSectionListByLevel(level, e, "",0);
    }

    /**
     * 新增板块
     *
     * @param fSectionDTO
     * @return
     */
    @ApiOperation(value = "新增板块", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping(value = "/section", method = RequestMethod.POST)
    @ResponseBody
    @UserRoles(UserRole.DISCUSS_MANAGER)
    public RespObj addFSection(@RequestBody FSectionDTO fSectionDTO) {
        RespObj respObj = RespObj.FAILD;
        try {
            fSectionDTO.setTotalScanCount(0);
            fSectionDTO.setTotalCommentCount(0);
            fSectionDTO.setThemeCount(0);
            fSectionDTO.setPostCount(0);
            ObjectId sectionId = fSectionService.addFSection(fSectionDTO);
            respObj = RespObj.SUCCESS;
            respObj.setMessage("添加成功");
            EBusinessLog.info("Admin-addFSection: UserId = " + getUserId() + "\tUserName = " + getSessionValue().getUserName() + "\tsectionId = " + sectionId);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setMessage("添加失败");
        }
        return respObj;
    }
    @ApiOperation(value = "updateSection", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/updateSection")
    @ResponseBody
    @UserRoles(UserRole.DISCUSS_MANAGER) //这个代表只有管理员可以访问
    public RespObj updateSection(String id, String nm, String snm, String mmn) {
        fSectionDao.updateSection(id, nm, snm, mmn);
        return RespObj.SUCCESS;
    }

    /**
     * 删除板块
     */
    @ApiOperation(value = "删除板块", httpMethod = "DELETE", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping(value = "/section/{fSectionId}", method = RequestMethod.DELETE)
    @ResponseBody
    @UserRoles(UserRole.DISCUSS_MANAGER)
    public RespObj deleteFSection(@PathVariable("fSectionId") @ObjectIdType ObjectId fSectionId) {
        EBusinessLog.info("Admin-deleteFSection: UserId = " + getUserId() + "\tUserName = " + getSessionValue().getUserName() + "\tsectionId = " + fSectionId);
        RespObj respObj = RespObj.FAILD;
        fSectionService.deleteFSection(fSectionId);
        respObj = RespObj.SUCCESS;
        respObj.setMessage("删除成功");
        return respObj;
    }


    /**
     * 更新板块
     */
    @ApiOperation(value = "更新板块", httpMethod = "PUT", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping(value = "/section/{fSectionId}", method = RequestMethod.PUT)
    @ResponseBody
    @UserRoles(UserRole.DISCUSS_MANAGER)
    public RespObj updateEGoodsCategory(@RequestBody FSectionDTO fSectionDTO) {
        fSectionService.updateFSection(fSectionDTO);
        RespObj respObj = RespObj.SUCCESS;
        respObj.setMessage("更新成功！");
        return respObj;
    }

    /**
     * 更新板块排序
     */
    @ApiOperation(value = "更新板块排序", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping(value = "/section/changeSort", method = RequestMethod.POST)
    @ResponseBody
    @UserRoles(UserRole.DISCUSS_MANAGER)
    public RespObj updateFSectionSort(String selfId, int selfSort, String anotherId, int anotherSort) {
        fSectionService.updateFSectionSort(new ObjectId(selfId), selfSort, new ObjectId(anotherId), anotherSort);
        RespObj respObj = RespObj.SUCCESS;
        respObj.setMessage("更新成功！");
        return respObj;
    }

    @ApiOperation(value = "setSectionRemove", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping(value = "/section/setRemove/{fSectionId}", method = RequestMethod.POST)
    @ResponseBody
    @UserRoles(UserRole.DISCUSS_MANAGER)
    public RespObj setSectionRemove(@PathVariable @ObjectIdType ObjectId fSectionId,int remove){
        FSectionEntry entry=fSectionService.find(fSectionId);
        entry.setRemove(remove);
        fSectionService.saveOrUpdate(entry);
        return RespObj.SUCCESS;
    }

    /**
     * 上传首页板块图片
     */
    @ApiOperation(value = "上传首页板块图片", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping(value = "/section/uploadImg", method = RequestMethod.POST)
    @ResponseBody
    @UserRoles(UserRole.DISCUSS_MANAGER)
    public RespObj uploadFSectionImg(@RequestParam String id, @RequestParam String imgUrl) {
        RespObj respObj = RespObj.FAILD;
        try {
            fSectionService.updateFSectionImg(new ObjectId(id), "img", imgUrl);
            respObj = RespObj.SUCCESS;
            respObj.setMessage("上传成功！");
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setMessage("上传失败！");
        }
        return respObj;
    }

    /**
     * 上传移动端首页图片
     */
    @ApiOperation(value = "上传移动端首页图片", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping(value = "/section/uploadImgApp")
    @ResponseBody
    @UserRoles(UserRole.DISCUSS_MANAGER)
    public RespObj uploadFSectionImgApp(@RequestParam String id, @RequestParam String imgUrl) {
        RespObj respObj = RespObj.FAILD;
        try {
            fSectionService.updateFSectionImg(new ObjectId(id), "ias", imgUrl);
            respObj = RespObj.SUCCESS;
            respObj.setMessage("上传成功！");
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setMessage("上传失败！");
        }
        return respObj;
    }

    /**
     * 上传移动端首页大图片
     */
    @ApiOperation(value = "上传移动端首页大图片", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping(value = "/section/uploadImgBigApp", method = RequestMethod.POST)
    @ResponseBody
    @UserRoles(UserRole.DISCUSS_MANAGER)
    public RespObj uploadFSectionBigImgApp(@RequestParam String id, @RequestParam String imgUrl) {
        RespObj respObj = RespObj.FAILD;
        try {
            fSectionService.updateFSectionImg(new ObjectId(id), "ibs", imgUrl);
            respObj = RespObj.SUCCESS;
            respObj.setMessage("上传成功！");
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setMessage("上传失败！");
        }
        return respObj;
    }


    //=====================================================商品分类===========================================================

    /**
     * 商品分类列表
     *
     * @param level
     * @return
     */
    @ApiOperation(value = "商品分类列表", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = List.class)})
    @RequestMapping(value = "/eGoodsCategorys", method = RequestMethod.GET)
    @ResponseBody
    @UserRoles(UserRole.DISCUSS_MANAGER)
    public List<EGoodsCategoryDTO> getEGoodsCategoryDTOs(@RequestParam(required = false, defaultValue = "1") int level,
                                                         @RequestParam(required = false, defaultValue = "") String parentId) {
        ObjectId e = null;
        if (!"".equals(parentId)) {
            e = new ObjectId(parentId);
        }
        return eGoodsCategoryService.getEGoodsCategoryListByLevel(level, e);
    }

    /**
     * 新增商品分类
     *
     * @param eGoodsCategoryDTO
     * @return
     */
    @ApiOperation(value = "新增商品分类", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping(value = "/eGoodsCategorys", method = RequestMethod.POST)
    @ResponseBody
    @UserRoles(UserRole.DISCUSS_MANAGER)
    public RespObj addEGoodsCategory(@RequestBody EGoodsCategoryDTO eGoodsCategoryDTO) {
        RespObj respObj = RespObj.FAILD;
        try {
            ObjectId eGoodsCategoryId = eGoodsCategoryService.addEGoodsCategory(eGoodsCategoryDTO);
            respObj = RespObj.SUCCESS;
            respObj.setMessage("添加成功");
            EBusinessLog.info("Admin-addGoodsCategory: UserId = " + getUserId() + "\tUserName = " + getSessionValue().getUserName() + "\teGoodsCategoryId = " + eGoodsCategoryId);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setMessage("添加失败");
        }
        return respObj;
    }

    /**
     * 删除商品分类
     */
    @ApiOperation(value = "删除商品分类", httpMethod = "DELETE", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping(value = "/eGoodsCategorys/{eGoodsCategoryId}", method = RequestMethod.DELETE)
    @ResponseBody
    @UserRoles(UserRole.DISCUSS_MANAGER)
    public RespObj deleteEGoodsCategory(@PathVariable("eGoodsCategoryId") @ObjectIdType ObjectId eGoodsCategoryId) {
        EBusinessLog.info("Admin-deleteGoodsCategory: UserId = " + getUserId() + "\tUserName = " + getSessionValue().getUserName() + "\teGoodsCategoryId = " + eGoodsCategoryId);
        RespObj respObj = RespObj.FAILD;
        eGoodsCategoryService.deleteEGoodsCategory(eGoodsCategoryId);
        respObj = RespObj.SUCCESS;
        respObj.setMessage("删除成功");
        return respObj;
    }


    /**
     * 更新商品分类
     */
    @ApiOperation(value = "更新商品分类", httpMethod = "PUT", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping(value = "/eGoodsCategorys/{eGoodsCategoryId}", method = RequestMethod.PUT)
    @ResponseBody
    @UserRoles(UserRole.DISCUSS_MANAGER)
    public RespObj updateEGoodsCategory(@RequestBody EGoodsCategoryDTO eGoodsCategoryDTO) {
        eGoodsCategoryService.updateEGoodsCategory(eGoodsCategoryDTO);
        RespObj respObj = RespObj.SUCCESS;
        respObj.setMessage("更新成功！");
        return respObj;
    }

    /**
     * 更新商品分类排序
     */
    @ApiOperation(value = "更新商品分类排序", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping(value = "/eGoodsCategorys/changeSort", method = RequestMethod.POST)
    @ResponseBody
    @UserRoles(UserRole.DISCUSS_MANAGER)
    public RespObj updateEGoodsCategorySort(String selfId, int selfSort, String anotherId, int anotherSort) {
        eGoodsCategoryService.updateEGoodsCategorySort(new ObjectId(selfId), selfSort, new ObjectId(anotherId), anotherSort);
        RespObj respObj = RespObj.SUCCESS;
        respObj.setMessage("更新成功！");
        return respObj;
    }

    /**
     * 上传首页分类图片
     */
    @ApiOperation(value = "上传首页分类图片", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping(value = "/eGoodsCategorys/uploadImg", method = RequestMethod.POST)
    @ResponseBody
    @UserRoles(UserRole.DISCUSS_MANAGER)
    public RespObj uploadImg(@RequestParam String id, @RequestParam String imgUrl) {
        RespObj respObj = RespObj.FAILD;
        try {
            eGoodsCategoryService.updateEGoodsCategoryImg(new ObjectId(id), "img", imgUrl);
            respObj = RespObj.SUCCESS;
            respObj.setMessage("上传成功！");
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setMessage("上传失败！");
        }
        return respObj;
    }

    /**
     * 上传移动端首页分类图片
     */
    @ApiOperation(value = "上传移动端首页分类图片", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping(value = "/eGoodsCategorys/uploadMoblieImg", method = RequestMethod.POST)
    @ResponseBody
    @UserRoles(UserRole.DISCUSS_MANAGER)
    public RespObj uploadMobileImg(@RequestParam String id, @RequestParam String imgUrl) {
        RespObj respObj = RespObj.FAILD;
        try {
            eGoodsCategoryService.updateEGoodsCategoryImg(new ObjectId(id), "mimg", imgUrl);
            respObj = RespObj.SUCCESS;
            respObj.setMessage("上传成功！");
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setMessage("上传失败！");
        }
        return respObj;
    }

    /**
     * 上传移动端首页分类图片
     */
    @ApiOperation(value = "进行微信绑定操作", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping(value = "/eGoodsCategorys/uploadMoblieCategoryImg", method = RequestMethod.POST)
    @ResponseBody
    @UserRoles(UserRole.DISCUSS_MANAGER)
    public RespObj uploadMoblieCategoryImg(@RequestParam String id, @RequestParam String imgUrl) {
        RespObj respObj = RespObj.FAILD;
        try {
            eGoodsCategoryService.updateEGoodsCategoryImg(new ObjectId(id), "mcimg", imgUrl);
            respObj = RespObj.SUCCESS;
            respObj.setMessage("上传成功！");
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setMessage("上传失败！");
        }
        return respObj;
    }


    //======================================================年级分类==============================================================

    /**
     * 获取年级分类
     */
    @ApiOperation(value = "获取年级分类", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = List.class)})
    @RequestMapping(value = "/eGradeCategories", method = RequestMethod.GET)
    @ResponseBody
    @UserRoles(UserRole.DISCUSS_MANAGER)
    public List<EGradeCategoryDTO> getEGradeCategoryList() {
        return eGoodsCategoryService.getEGradeCategoryList();
    }

    /**
     * 新增年级分类
     */
    @ApiOperation(value = "新增年级分类", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping(value = "/eGradeCategories", method = RequestMethod.POST)
    @ResponseBody
    @UserRoles(UserRole.DISCUSS_MANAGER)
    public RespObj addEGradeCategory(@RequestBody EGradeCategoryDTO dto) {
        RespObj respObj = RespObj.FAILD;
        try {
            eGoodsCategoryService.addEGradeCategory(dto);
            respObj = RespObj.SUCCESS;
            respObj.setMessage("新增成功！");
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setMessage("新增失败！");
        }
        return respObj;
    }

    /**
     * 删除年级分类
     */
    @ApiOperation(value = "删除年级分类", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/deleteEGradeCategory")
    @ResponseBody
    @UserRoles(UserRole.DISCUSS_MANAGER)
    public RespObj deleteEGradeCategory(@ObjectIdType ObjectId id) {
        RespObj respObj = RespObj.FAILD;
        try {
            eGoodsCategoryService.deleteEGradeCategory(id);
            respObj = RespObj.SUCCESS;
            respObj.setMessage("删除成功！");
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setMessage("删除失败！");
        }
        return respObj;
    }


    //======================================================订单==============================================================
    @ApiOperation(value = "订单", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = Map.class)})
    @RequestMapping(value = "/orders", method = RequestMethod.GET)
    @ResponseBody
    @UserRoles(UserRole.DISCUSS_MANAGER)
    public Map<String, Object> orders(@RequestParam(required = false, defaultValue = "-1") int state, int page, int pageSize) {
        Map<String, Object> model = new HashMap<String, Object>();
        List<EOrderInfoDTO> orderList = eBusinessOrderService.getEOrderInfoDTOs(null, state, (page - 1) * pageSize, pageSize, true);
        model.put("orders", orderList);
        model.put("count", eBusinessOrderService.getOrdersCount(null, state, true));
        model.put("page", page);
        model.put("pageSize", pageSize);
        return model;
    }
    @ApiOperation(value = "修改订单", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = Map.class)})
    @RequestMapping(value = "/orders/{orderId}", method = RequestMethod.POST)
    @ResponseBody
    @UserRoles(UserRole.DISCUSS_MANAGER)
    public Map<String, Object> updateOrderState(@PathVariable("orderId") @ObjectIdType ObjectId orderId, int orderState, HttpServletResponse response) {
        Map<String, Object> model = new HashMap<String, Object>();
        try {
            eBusinessOrderService.updateOrderState(orderId, orderState);
            response.setStatus(200);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(500);
            model.put("message", "更改状态失败");
        }
        return model;
    }
    @ApiOperation(value = "修改订单", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping(value = "/orders/{orderId}/goods/{goodsId}", method = RequestMethod.POST)
    @ResponseBody
    @UserRoles(UserRole.DISCUSS_MANAGER)
    public RespObj updateOrderExpress(@PathVariable("orderId") @ObjectIdType ObjectId orderId,
                                      @PathVariable("goodsId") @ObjectIdType ObjectId goodsId,
                                      String objKinds, @RequestParam String expressComNo, @RequestParam String expressNo) {
        EBusinessLog.info("Admin-updateExpress: UserId = " + getUserId() + "\tUserName = " + getSessionValue().getUserName() + "\torderId = " + orderId +
                "\tgoodsId = " + goodsId + "\texpressComNo" + expressComNo + "\texpressNo" + expressNo);
        RespObj respObj = RespObj.FAILD;
        try {
            eBusinessOrderService.updateExpress(orderId, goodsId, objKinds, expressNo, expressComNo);
            respObj = RespObj.SUCCESS;
        } catch (IllegalParamException e) {
            e.printStackTrace();
        }
        return respObj;
    }

    //======================================================管理抵用券===============================================
    @ApiOperation(value = "管理抵用券", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping(value = "/vouchers", method = RequestMethod.POST)
    @ResponseBody
    @UserRoles(UserRole.DISCUSS_MANAGER)
    public RespObj generateVouchers(int count, int denomination, String expirationTime) {
        List<EVoucherEntry> eVoucherEntries = new ArrayList<EVoucherEntry>();
        for (int i = 0; i < count; i++) {
            String num = String.valueOf(System.currentTimeMillis());
            num += RandomUtils.nextInt(Constant.MIN_PASSWORD);
            Date date = DateTimeUtils.stringToDate(expirationTime + " 23:59:59", DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_SS_H);
            EVoucherEntry eVoucherEntry = new EVoucherEntry(null, num, denomination, date.getTime(), 3);
            eVoucherEntries.add(eVoucherEntry);
        }
        eBusinessVoucherService.addEVouchers(eVoucherEntries);
        return RespObj.SUCCESS;
    }
    @ApiOperation(value = "管理抵用券", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = Map.class)})
    @RequestMapping(value = "/vouchers", method = RequestMethod.GET)
    @ResponseBody
    @UserRoles(UserRole.DISCUSS_MANAGER)
    public Map<String, Object> getVouchers(int page, int pageSize) {
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("vouchers", eBusinessVoucherService.getEVouchers(page, pageSize));
        model.put("page", page);
        model.put("pageSize", pageSize);
        model.put("count", eBusinessVoucherService.countVouchers());
        return model;
    }


    //======================================================运费模板==============================================================

    /**
     * 运费模板界面
     */
    @ApiOperation(value = "运费模板界面", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/expressTemplate")
    @UserRoles(UserRole.DISCUSS_MANAGER)
    public String toExpTempPage() {
        return "/admin/expressTemplate";
    }

    /**
     * 运费模板列表(分页)
     */
    @ApiOperation(value = "运费模板列表(分页)", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = Map.class)})
    @RequestMapping("/expTempList")
    @ResponseBody
    @UserRoles(UserRole.DISCUSS_MANAGER)
    public Map<String, Object> getExpTempList(int page, int pageSize) {
        Map<String, Object> result = new HashMap<String, Object>();
        List<EExpressTemplateDTO> tempList = eExpTemplateService.getExpTempList((page - 1) * pageSize, pageSize);
        int count = eExpTemplateService.getTotal();
        result.put("tempList", tempList);
        result.put("count", count);
        result.put("page", page);
        result.put("pageSize", pageSize);

        return result;
    }

    /**
     * 详情/编辑
     */
    @ApiOperation(value = "详情/编辑", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = EExpressTemplateDTO.class)})
    @RequestMapping("/expTempDetail")
    @ResponseBody
    @UserRoles(UserRole.DISCUSS_MANAGER)
    public EExpressTemplateDTO getExpTempById(String id) {
        return eExpTemplateService.getTemplateById(id);
    }


    /**
     * 新增或更新模板
     */
    @ApiOperation(value = "新增或更新模板", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/saveExpTemplate")
    @ResponseBody
    @UserRoles(UserRole.DISCUSS_MANAGER)
    public RespObj saveOrUpdateExpTemplate(@RequestBody EExpressTemplateDTO template) {
        EBusinessLog.info("Admin-saveOrUpdateExpTemplate: UserId = " + getUserId() + "\tUserName = " + getSessionValue().getUserName() + "\texpTempId = " + template.getId() + "\ttemplateName" + template.getName());
        eExpTemplateService.saveOrUpdate(template);
        return RespObj.SUCCESS;
    }

    /**
     * 删除模板
     */
    @ApiOperation(value = "删除模板", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/removeExpTemplate")
    @ResponseBody
    @UserRoles(UserRole.DISCUSS_MANAGER)
    public RespObj removeTemplate(String id) {
        EBusinessLog.info("Admin-saveOrUpdateExpTemplate: UserId = " + getUserId() + "\tUserName = " + getSessionValue().getUserName() + "\texpTempId = " + id);
        eExpTemplateService.remove(new ObjectId(id));
        return RespObj.SUCCESS;
    }

    /**
     * 获取运费模板列表(不分页)
     */
    @ApiOperation(value = "获取运费模板列表(不分页)", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = List.class)})
    @RequestMapping("/eTemplateList")
    @ResponseBody
    @UserRoles(UserRole.DISCUSS_MANAGER)
    public List<EExpressTemplateDTO> getTempList() {
        return eExpTemplateService.getExpTempList();
    }

    /*=============================================================商品分类视频上传=====================================================================*/

    /**
     * 新增/更新商品分类视频
     */
    @ApiOperation(value = "更新商品分类视频", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping(value = "/video", method = RequestMethod.POST)
    @ResponseBody
    @UserRoles(UserRole.DISCUSS_MANAGER)
    public RespObj saveVideo(ECategoryVideoDTO dto) {
        RespObj respObj = RespObj.FAILD;
        if (!StringUtils.isEmpty(dto.getId())) {
            try {
                eCategoryVideoService.updateVideo(dto);
                respObj = RespObj.SUCCESS;
                respObj.setMessage("更新成功!");
            } catch (Exception e) {
                respObj.setMessage("更新失败!");
            }
        } else {
            try {
                eCategoryVideoService.addCategoryVideo(dto);
                respObj = RespObj.SUCCESS;
                respObj.setMessage("新增成功!");
            } catch (Exception e) {
                respObj.setMessage("新增失败!");
            }
        }
        return respObj;
    }

    /**
     * 删除商品分类视频
     */
    @ApiOperation(value = "删除商品分类视频", httpMethod = "DELETE", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping(value = "/video", method = RequestMethod.DELETE)
    @ResponseBody
    @UserRoles(UserRole.DISCUSS_MANAGER)
    public RespObj deleteVideo(String id) {
        RespObj respObj = RespObj.FAILD;
        try {
            eCategoryVideoService.deleteVideo(id);
            respObj = RespObj.SUCCESS;
            respObj.setMessage("删除成功!");
        } catch (Exception e) {
            respObj.setMessage("删除失败!");
        }
        return respObj;
    }

    /**
     * 获取商品分类视频列表
     */
    @ApiOperation(value = "获取商品分类视频列表", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = Map.class)})
    @RequestMapping(value = "/video", method = RequestMethod.GET)
    @ResponseBody
    @UserRoles(UserRole.DISCUSS_MANAGER) //这个代表只有管理员可以访问
    public Map<String, Object> getVideoList(@RequestParam(required = false, defaultValue = "") String categoryId, int page, int pageSize) {//商品分类可选
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("videoList", eCategoryVideoService.getVideoList(categoryId, (page - 1) * pageSize, pageSize));
        result.put("page", page);
        result.put("pageSize", pageSize);
        result.put("count", eCategoryVideoService.getCount(categoryId));
        return result;
    }


    /**
     * 根据id查询视频
     */
    @ApiOperation(value = "根据id查询视频", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/video/{videoId}")
    @ResponseBody
    public ECategoryVideoDTO getVIdeoById(@PathVariable("videoId") String videoId) {
        return eCategoryVideoService.getvideoById(videoId);
    }

    //===============================================版主管理========================================================================
    //添加管理员
    @ApiOperation(value = "添加管理员", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/addDiscussAdmin")
    @ResponseBody
    @UserRoles(UserRole.DISCUSS_MANAGER)
    public RespObj addAdmin(String name) {
        UserEntry entry = userService.findByUserName(name);
        if (entry == null) {
            return RespObj.FAILD("用户不存在");
        }
        userService.updateUserRole(entry.getID(), UserRole.DISCUSS_MANAGER);
        return RespObj.SUCCESS("修改成功");
    }

    //添加版主
    @ApiOperation(value = "添加版主", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/addSectionAdmin")
    @ResponseBody
    @UserRoles(UserRole.DISCUSS_MANAGER)
    public RespObj addSectionAdmin(String name) {
        UserEntry entry = userService.findByUserName(name);
        if (entry == null) {
            return RespObj.FAILD("用户不存在");
        }
        userService.updateUserRole(entry.getID(), UserRole.DISCUSS_SECTION_MANAGER);
        return RespObj.SUCCESS("修改成功");
    }
    @ApiOperation(value = "deleteMaster", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/deleteMaster")
    @ResponseBody
    @UserRoles(UserRole.DISCUSS_MANAGER)
    public RespObj deleteMaster(String id) {
        mUserDao.updateRole(new ObjectId(id), UserRole.STUDENT);
        return RespObj.SUCCESS;
    }
    @ApiOperation(value = "deleteManager", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/deleteManager")
    @ResponseBody
    @UserRoles(UserRole.DISCUSS_MANAGER)
    public RespObj deleteManager(String id) {
        mUserDao.updateRole(new ObjectId(id), UserRole.STUDENT);
        return RespObj.SUCCESS;
    }

    private static final Logger logger = Logger.getLogger(AdminController.class);
    @ApiOperation(value = "goodgood", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/goodgood")
    @ResponseBody
    @UserRoles(UserRole.DISCUSS_MANAGER)
    public RespObj goodgood(String reply, int value) {

        logger.info(reply + "" + value);
        BasicDBObject updateValue = new BasicDBObject();
        updateValue.append(Constant.MONGO_SET, new BasicDBObject("prc", value));
        MongoFacroty.getAppDB().getCollection("freply").update(new BasicDBObject("_id", new ObjectId(reply)), updateValue, false, true);
        return RespObj.SUCCESS;
    }


    /**
     * 系统消息
     **/
    @ApiOperation(value = "系统消息", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/SystemMsgManage")
    @ResponseBody
    @UserRoles(UserRole.DISCUSS_MANAGER)
    public RespObj systemMsgManage(@RequestParam(value = "userName", required = false, defaultValue = "") String userName,
                                   @RequestParam(value = "type", required = false, defaultValue = "1") int type,
                                   @RequestParam(value = "message", required = false, defaultValue = "") String message) {
        if (type != 1) { //发送给所有用户
//            fInformationService.sendSystemMessageToAllUser(message);
            if (StringUtils.isNotBlank(userName)) {
                String[] users = userName.split("#");
                for (String name : users) {
                    UserEntry userEntry = userService.findByUserName(name);
                    if (null != userEntry) {
                        fInformationService.sendSystemMessage(userEntry.getID(), message);
                    }
                }
            }
        }
        return RespObj.SUCCESS("发送成功");
    }

    //========================================================================================================================
    @ApiOperation(value = "showArticleManage", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/articleManager")
    @UserRoles(UserRole.DISCUSS_MANAGER)
    public String showArticleManage() {
        return "admin/articleManager";
    }
    @ApiOperation(value = "showPreview", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/preview")
    @UserRoles(UserRole.DISCUSS_MANAGER)
    public String showPreview() {
        return "admin/preview";
    }
    @ApiOperation(value = "loginInfo", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/loginInfo")
    @UserRoles(UserRole.DISCUSS_MANAGER)
    public String loginInfo() {
        return "/admin/loginInfo";
    }

    /**
     * 删除楼中楼
     *
     * @param rpid
     * @param lolId
     * @return
     */
    @ApiOperation(value = "删除楼中楼", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/deletelol")
    @SessionNeedless
    @ResponseBody
    public RespObj deletelol(@ApiParam(name = "rpid", required = true, value = "rpid") @ObjectIdType ObjectId rpid, @ApiParam(name = "lolId", required = true, value = "lolId") @ObjectIdType ObjectId lolId) {

        logger.info(rpid.toString() + "==" + lolId);
        fReplyService.deleteLol(rpid, lolId);
        return RespObj.SUCCESS;
    }


}
