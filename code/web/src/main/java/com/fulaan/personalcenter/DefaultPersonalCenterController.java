package com.fulaan.personalcenter;

import com.db.backstage.TeacherApproveDao;
import com.fulaan.base.BaseController;
import com.fulaan.cache.CacheHandler;
import com.fulaan.experience.service.ExperienceService;
import com.fulaan.forum.service.FScoreService;
import com.fulaan.service.MemberService;
import com.fulaan.user.service.UserService;
import com.fulaan.utils.QiniuFileUtils;
import com.pojo.app.SessionValue;
import com.pojo.backstage.TeacherApproveEntry;
import com.pojo.forum.FScoreDTO;
import com.pojo.user.ExpLogType;
import com.pojo.user.UserEntry;
import com.sys.constants.Constant;
import com.sys.utils.AvatarUtils;
import com.sys.utils.MD5Utils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by qinbo on 15/4/1.
 */
@Api(value="个人中心",hidden = true)
@Controller
@RequestMapping("/jxmapi/personal")
public class DefaultPersonalCenterController extends BaseController {

    private static final Logger logger = Logger.getLogger(DefaultPersonalCenterController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private FScoreService fScoreService;

    @Autowired
    private ExperienceService experienceService;

    @Autowired
    private MemberService memberService;

    private TeacherApproveDao teacherApproveDao = new TeacherApproveDao();

    @ApiOperation(value = "letterPage", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/letterpage")
    public String letterPage() {
        return "personalcenter/myMessage";
    }

    @ApiOperation(value = "replyLetterPage", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/replyletterpage")
    public String replyLetterPage(String replyId, Model model) {
        //把邮件表示为读过了
        model.addAttribute("replyId", replyId);
        return "personalcenter/replyMessage";
    }

    @ApiOperation(value = "userHelpPage", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/userhelp")
    public String userHelpPage() {
        return "personalcenter/userHelp";
    }

    @ApiOperation(value = "settingPage", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/settingpage")
    public String settingPage(Map<String, Object> model) {
        UserEntry e = userService.findById(getUserId());
        String avatar = AvatarUtils.getAvatar(e.getAvatar(), e.getRole(),e.getSex());
        model.put("avatar", avatar);
        return "personalcenter/myAccountInfo";
    }

    /**
     * 用户基本信息页面
     *
     * @param model
     * @return
     */
    @ApiOperation(value = "用户基本信息页面", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/basic")
    public String basicPage(Map<String, Object> model) {
        UserEntry e = userService.findById(getUserId());
        model.put("loginName", e.getLoginName());
        model.put("mobile", e.getMobileNumber());
        model.put("email", e.getEmail());
        model.put("sex", e.getSex());
        return "personalcenter/myAccountBasic";
    }

    @ApiOperation(value = "avatarPage", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/avatarpage")
    public String avatarPage() {
        return "personalcenter/avatar";
    }

    @ApiOperation(value = "passwordPage", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/passwordpage")
    public String passwordPage(Map<String, Object> model) {
        String params = getUserId().toString() + 0l;
        String key = CacheHandler.getKeyString(CacheHandler.CACHE_USER_FIRST_LOGIN, params);
        String value = CacheHandler.getStringValue(key);
        if (value != null && Constant.USER_FIRST_LOGIN.equals(value)) {
            model.put(Constant.USER_FIRST_LOGIN, "Y");
            CacheHandler.deleteKey(CacheHandler.CACHE_USER_FIRST_LOGIN, params);
        } else {
            model.put(Constant.USER_FIRST_LOGIN, "N");
        }
        return "personalcenter/myAccountPassword";
    }

    @ApiOperation(value = "friendshipPage", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/friendshippage")
    public String friendshipPage() {
        return "activity/friend-list";
    }


    /**
     * 获取列表
     */
    @ApiOperation(value = "获取列表", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = Map.class)})
    @RequestMapping("/getAddressBook")
    @ResponseBody
    public Map<String, Object> getAddressBook(Integer page, Integer size) {
        Map<String, Object> result = new HashMap<String, Object>();
        return result;
    }

    /**
     * 编辑用户头像
     */
    @ApiOperation(value = "编辑用户头像", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/avataredit")
    @ResponseBody
    public String avatarEdit(String a,
                             String input,
                             String avatar1, HttpServletRequest request) throws Exception {

        QiniuFileUtils.deleteFile(QiniuFileUtils.TYPE_IMAGE, input);
        byte[] headBytes = getFlashDataDecode(avatar1);
        InputStream byteStream = new ByteArrayInputStream(headBytes);
        QiniuFileUtils.uploadFile(input, byteStream, QiniuFileUtils.TYPE_IMAGE);

        SessionValue sv = getSessionValue();


        if (StringUtils.isBlank(sv.getAvatar()) || "head-default-head.jpg".equals(sv.getAvatar())) {
            userService.updateForumExperience(getUserId(), 10);
            userService.updateForumScore(getUserId(), 10);
            FScoreDTO fScoreDTO = new FScoreDTO();
            fScoreDTO.setTime(System.currentTimeMillis());
            fScoreDTO.setType(2);
            fScoreDTO.setOperation("设置头像");
            fScoreDTO.setPersonId(getUserId().toString());
            fScoreDTO.setScoreOrigin("第一次设置头像获得积分奖励！");
            fScoreDTO.setScore(10);
            fScoreService.addFScore(fScoreDTO);
        }


        userService.update(getUserId(), "avt", input);

        memberService.updateAllAvatar(getUserId(), input);

        sv.setAvatar(input);

        //s_key
        CacheHandler.cacheSessionValue(getCookieValue(Constant.COOKIE_USER_KEY), sv, Constant.SECONDS_IN_DAY);


        return "<?xml version=\"1.0\" ?><root><face success=\"1\"/></root>";


    }

    /**
     * 编辑用户头像
     */
    @ApiOperation(value = "编辑用户头像", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = MultipartFile.class)})
    @RequestMapping("/avatarupload")
    @ResponseBody
    public String avatarUpload(String input, MultipartFile Filedata) throws Exception {
        QiniuFileUtils.uploadFile(input, Filedata.getInputStream(), QiniuFileUtils.TYPE_IMAGE);
        return QiniuFileUtils.getPath(QiniuFileUtils.TYPE_IMAGE, input);
    }

    /**
     * 更新用户头像
     */
    @ApiOperation(value = "更新用户头像", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = Map.class)})
    @RequestMapping("/updateavatar")
    @ResponseBody
    public Map<String, Object> updateAvatar(String imgpath1) throws Exception {

        SessionValue sv = getSessionValue();
        TeacherApproveEntry entry = teacherApproveDao.getEntry(new ObjectId(sv.getId()));
        if(entry != null){
            if(entry.getType()==2){
                if(!imgpath1.contains("-headv1")){
                    String oldUrl = imgpath1;
                    imgpath1 = imgpath1+"-headv1";
                    teacherApproveDao.updateEntry4(new ObjectId(sv.getId()), entry.getType(), oldUrl, imgpath1);
                }else{
                    String imagpage2 = imgpath1.replaceAll("-headv1","");
                    String oldUrl = imagpage2;
                    imagpage2 = imagpage2+"-headv1";
                    teacherApproveDao.updateEntry4(new ObjectId(sv.getId()), entry.getType(), oldUrl, imagpage2);
                    imgpath1 = imagpage2;
                }

            }
        }

        userService.updateAvatar(sv.getId(), imgpath1);

        memberService.updateAllAvatar(new ObjectId(sv.getId()),imgpath1);
        sv.setAvatar(imgpath1 + "?v=1");

        String userKey = CacheHandler.getUserKey(sv.getId());
        CacheHandler.cacheSessionValue(userKey,
                sv, Constant.SECONDS_IN_HALF_YEAR);
        Map<String, Object> result = new HashMap<String, Object>();
        ExpLogType avatarScore = ExpLogType.AVATAR;
        if (experienceService.updateNoRepeat(getUserId().toString(), avatarScore)) {
            result.put("scoreMsg", avatarScore.getDesc());
            result.put("score", avatarScore.getExp());
        }
        result.put("result", "ok");
        result.put("imageUrl", imgpath1);
        return result;
    }

    /**
     * 检查密码，在修改密码前调用
     */
    @ApiOperation(value = "检查密码，在修改密码前调用", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = Map.class)})
    @RequestMapping("/checkpass")
    @ResponseBody
    public Map<String, Object> checkPassword(String password) throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();

        String uPassword = userService.getUserPassword(getSessionValue().getId());

        //todo : getmd5
        if (MD5Utils.getMD5(password).equals(uPassword) || uPassword.equalsIgnoreCase(password)) {
            result.put("repeat", "yes");
        }


        return result;
    }


    /**
     * 修改密码
     *
     * @param password
     * @return
     */
    @ApiOperation(value = "修改密码", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = Map.class)})
    @RequestMapping("/modifypassword")
    public
    @ResponseBody
    Map<String, Object> modifyPassword(String password, String verycode, HttpServletResponse response, HttpServletRequest request) throws Exception {
        //todo : getmd5
        Map<String, Object> result = new HashMap<String, Object>();
        String client = request.getHeader("User-Agent");
        //验证码
        String validateCode = "";
        String vckey = "";
        //获得请求信息中的Cookie数据
        Cookie[] cookies = request.getCookies();
        for (Cookie c : cookies) {
            if (Constant.COOKIE_VALIDATE_CODE.equals(c.getName())) {
                vckey = CacheHandler.getKeyString(CacheHandler.CACHE_VALIDATE_CODE, c.getValue());
                validateCode = CacheHandler.getStringValue(vckey);
                CacheHandler.deleteKey(CacheHandler.CACHE_VALIDATE_CODE, vckey);
                c.setMaxAge(0);
                c.setPath(Constant.BASE_PATH);
                response.addCookie(c);
            }
        }

        if (validateCode == null || "".equals(validateCode)) {
            result.put("result", "vcOutTime");
            return result;
        }
        verycode = verycode.toUpperCase();
        if (verycode == null || "".equals(verycode)) {
            result.put("result", "vcIsNull");
            return result;
        }
        if (!verycode.equals(validateCode)) {
            result.put("result", "vcError");
            return result;
        }

        userService.updatePassword(getSessionValue().getId(), MD5Utils.getMD5(password));

        ExpLogType passwordScore = ExpLogType.PASSWORD;
        if (experienceService.updateNoRepeat(getUserId().toString(), passwordScore)) {
            result.put("score", passwordScore.getExp());
            result.put("scoreMsg", passwordScore.getDesc());
        }
        result.put("result", "success");
        return result;
    }


    /**
     * @param src
     * @return
     */
    private byte[] getFlashDataDecode(String src) {
        char[] s = src.toCharArray();
        int len = s.length;
        byte[] r = new byte[len / 2];
        for (int i = 0; i < len; i = i + 2) {
            int k1 = s[i] - 48;
            k1 -= k1 > 9 ? 7 : 0;
            int k2 = s[i + 1] - 48;
            k2 -= k2 > 9 ? 7 : 0;
            r[i / 2] = (byte) (k1 << 4 | k2);
        }
        return r;
    }


}
