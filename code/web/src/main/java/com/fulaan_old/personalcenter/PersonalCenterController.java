package com.fulaan_old.personalcenter;

import com.fulaan.controller.BaseController;
import com.fulaan.cache.CacheHandler;
import com.fulaan_old.experience.service.ExperienceService;
import com.fulaan_old.forum.service.FScoreService;
import com.fulaan.user.service.UserService;
import com.fulaan_old.utils.QiniuFileUtils;
import com.pojo.app.SessionValue;
import com.pojo.forum.FScoreDTO;
import com.pojo.user.AvatarType;
import com.pojo.user.ExpLogType;
import com.pojo.user.UserEntry;
import com.sys.constants.Constant;
import com.sys.utils.AvatarUtils;
import com.sys.utils.MD5Utils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
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

@Controller
@RequestMapping("/personal")
public class PersonalCenterController extends BaseController {

    private static final Logger logger = Logger.getLogger(PersonalCenterController.class);

    @Autowired
    private UserService userService;

    private FScoreService fScoreService = new FScoreService();

    @Autowired
    private ExperienceService experienceService;

    @RequestMapping("/letterpage")
    public String letterPage() {
        return "personalcenter/myMessage";
    }


    @RequestMapping("/replyletterpage")
    public String replyLetterPage(String replyId, Model model) {
        //把邮件表示为读过了
        model.addAttribute("replyId", replyId);
        return "personalcenter/replyMessage";
    }

    @RequestMapping("/userhelp")
    public String userHelpPage() {
        return "personalcenter/userHelp";
    }

    @RequestMapping("/settingpage")
    public String settingPage(Map<String, Object> model) {
        UserEntry e = userService.searchUserId(getUserId());
        String avatar = AvatarUtils.getAvatar(e.getAvatar(), AvatarType.MAX_AVATAR.getType());
        model.put("avatar", avatar);
        return "personalcenter/myAccountInfo";
    }

    /**
     * 用户基本信息页面
     *
     * @param model
     * @return
     */
    @RequestMapping("/basic")
    public String basicPage(Map<String, Object> model) {
        UserEntry e = userService.searchUserId(getUserId());
        model.put("loginName", e.getLoginName());
        model.put("mobile", e.getMobileNumber());
        model.put("email", e.getEmail());
        model.put("sex", e.getSex());
        return "personalcenter/myAccountBasic";
    }


    @RequestMapping("/avatarpage")
    public String avatarPage() {
        return "personalcenter/avatar";
    }

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


    @RequestMapping("/friendshippage")
    public String friendshipPage() {
        return "activity/friend-list";
    }


    /**
     * 获取列表
     */
    @RequestMapping("/getAddressBook")
    @ResponseBody
    public Map<String, Object> getAddressBook(Integer page, Integer size) {
        Map<String, Object> result = new HashMap<String, Object>();
        return result;
    }

    /**
     * 编辑用户头像
     */
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


        sv.setAvatar(input);

        //s_key
        CacheHandler.cacheSessionValue(getCookieValue(Constant.COOKIE_USER_KEY), sv, Constant.SECONDS_IN_DAY);


        return "<?xml version=\"1.0\" ?><root><face success=\"1\"/></root>";


    }

    /**
     * 编辑用户头像
     */
    @RequestMapping("/avatarupload")
    @ResponseBody
    public String avatarUpload(String input, MultipartFile Filedata) throws Exception {
        QiniuFileUtils.uploadFile(input, Filedata.getInputStream(), QiniuFileUtils.TYPE_IMAGE);
        return QiniuFileUtils.getPath(QiniuFileUtils.TYPE_IMAGE, input);
    }

    /**
     * 更新用户头像
     */
    @RequestMapping("/updateavatar")
    @ResponseBody
    public Map<String, Object> updateAvatar(String imgpath1) throws Exception {

        SessionValue sv = getSessionValue();

        userService.updateAvatar(sv.getId(), imgpath1);

        sv.setAvatar(imgpath1 + "?v=1");

        String userKey = CacheHandler.getUserKey(sv.getId());
        CacheHandler.cacheSessionValue(userKey,
                sv, Constant.SECONDS_IN_DAY);
        Map<String, Object> result = new HashMap<String, Object>();
        ExpLogType avatarScore = ExpLogType.AVATAR;
        if (experienceService.updateNoRepeat(getUserId().toString(), avatarScore)) {
            result.put("scoreMsg", avatarScore.getDesc());
            result.put("score", avatarScore.getExp());
        }
        result.put("result", "ok");

        return result;
    }

    /**
     * 检查密码，在修改密码前调用
     */
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
