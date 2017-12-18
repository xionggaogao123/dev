package com.sys.utils;

import com.sys.constants.Constant;
import com.sys.props.Resources;
import org.apache.commons.lang.StringUtils;

import java.text.MessageFormat;

/**
 * 头像辅助类
 *
 * @author fourer
 */

public class AvatarUtils {


    public static final String MIN_AVATAR_FORMAT = Resources.getProperty("min.avatar.format");
    public static final String MIDDLE_AVATAR_FORMAT = Resources.getProperty("mid.avatar.format");
    public static final String MAX_AVATAR_FORMAT = Resources.getProperty("max.avatar.format");

    public static final String USER_AVATAR_FORMAT =Resources.getProperty("max.avatar.format");
    public static final String DEFAULT_AVATOR_FORMAT="http://7xiclj.com1.z0.glb.clouddn.com/d_{0}_{1}.png";
    public static final String DEFAULT_NEW_AVATOR_FORMAT="http://7xiclj.com1.z0.glb.clouddn.com/d_{0}_{1}.png";
    public static final String DEFAULT_FORMAT="head-default-head.jpg";
    public static final String DEFAULT_PREV="http://7xiclj.com1.z0.glb.clouddn.com";

    public static final int DEFAULT_ROLE=1;
    public static final int DEFAULT_SEX=1;
    //男     学生  http://7xiclj.com1.z0.glb.clouddn.com/head-0.7453231568799419.jpg
    //女     学生  http://7xiclj.com1.z0.glb.clouddn.com/head-0.3171187819843335.jpg
    //男     家长  http://7xiclj.com1.z0.glb.clouddn.com/head-0.31379172537414024.jpg
    //女     家长  http://7xiclj.com1.z0.glb.clouddn.com/head-0.9670962144527113.jpg

    /**
     * 得到头像
     *
     * @param avatar
     * @param type
     * @return
     */
    public static String getAvatar2(String avatar, int type) {

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

        return getAvatar(avatar,3,-1);
    }

    /**
     * 得到头像
     * @param avatar
     * role 1学生 2 老师 4家长 8领导;默认是学生
     * sex  1男 0女 ;默认是男
     * @return
     */
    public static String getAvatar(String avatar,int role,int sex){
        if(avatar == null ||  avatar.equals("") || avatar.contains("head-default-head.jpg")){
            if(role == 1 && sex == 1){//男学生
                return "http://7xiclj.com1.z0.glb.clouddn.com/head-0.7453231568799419.jpg";
            }else if (role ==1 && sex==0){//女学生
                return "http://7xiclj.com1.z0.glb.clouddn.com/head-0.3171187819843335.jpg";
            }else if(role ==2 && sex== 1){//男老师
                return "http://7xiclj.com1.z0.glb.clouddn.com/head-0.31379172537414024.jpg";
            }else if(role ==2 && sex== 0){
                return "http://7xiclj.com1.z0.glb.clouddn.com/head-0.9670962144527113.jpg";
            }else{
                return "http://7xiclj.com1.z0.glb.clouddn.com/5a3722270a9d3251e386c4a0.png";
            }
        }

        if(avatar.contains("http://7xiclj.com1.z0.glb.clouddn.com")){

        }else{
            avatar = "http://7xiclj.com1.z0.glb.clouddn.com/"+ avatar;
        }

        return avatar;

    }
    public static String getAvatar2(String avatar,int role,int sex)
    {
        if(StringUtils.isNotBlank(avatar) && !avatar.equals(DEFAULT_FORMAT))
        {
            if(avatar.contains(DEFAULT_PREV)) {
                return avatar;
            }else{
                return MessageFormat.format(USER_AVATAR_FORMAT, avatar);
            }
        }
        else
        {
            int defrole=Constant.ONE;

            if(isHeadMaster(role))
            {
                defrole=Constant.EIGHT;
            }
            else if(isTeacher(role))
            {
                defrole=Constant.TWO;
            }
            else if(isParent(role))
            {
                defrole=Constant.FOUR;
            }

            int defSex=Constant.ONE;
            if(sex!=Constant.ONE)
                defSex=Constant.ZERO;

            return MessageFormat.format(DEFAULT_AVATOR_FORMAT, defrole,defSex);
        }
    }

    private static boolean isHeadMaster(int role)
    {
        return role!= Constant.ONE && role!= Constant.TWO   && role!= Constant.FOUR;
    }
    private static boolean isParent(int role)
    {
        return role== Constant.FOUR;
    }
    private static boolean isTeacher(int role)
    {
        return role== Constant.TWO;
    }
}
