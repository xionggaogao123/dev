package com.sys.utils;

import java.text.MessageFormat;

import com.sys.constants.Constant;
import com.sys.props.Resources;

/**
 * 头像辅助类
 *
 * @author fourer
 */

public class AvatarUtils {


    public static final String MIN_AVATAR_FORMAT = Resources.getProperty("min.avatar.format");
    public static final String MIDDLE_AVATAR_FORMAT = Resources.getProperty("mid.avatar.format");
    public static final String MAX_AVATAR_FORMAT = Resources.getProperty("max.avatar.format");

    /**
     * 得到头像
     *
     * @param avatar
     * @param type
     * @return
     */
    public static String getAvatar(String avatar, int type) {

        //这是一个意外
        if(avatar != null && avatar.contains("qlogo")){
            return avatar;
        }
        if (Constant.ONE == type) {
            return MessageFormat.format(MIN_AVATAR_FORMAT, avatar);
        }
        if (Constant.TWO == type) {
            return MessageFormat.format(MIDDLE_AVATAR_FORMAT, avatar);
        }
        return MessageFormat.format(MAX_AVATAR_FORMAT, avatar);
    }
}
