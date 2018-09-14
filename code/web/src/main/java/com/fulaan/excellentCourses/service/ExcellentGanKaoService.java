package com.fulaan.excellentCourses.service;

import com.db.excellentCourses.ClassOrderDao;
import com.db.excellentCourses.ExcellentCoursesDao;
import com.db.excellentCourses.HourClassDao;
import com.db.user.UserDao;
import com.pojo.excellentCourses.ClassOrderEntry;
import com.pojo.excellentCourses.ExcellentCoursesEntry;
import com.pojo.excellentCourses.HourClassEntry;
import com.pojo.user.UserEntry;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by James on 2018-09-13.
 */
@Service
public class ExcellentGanKaoService {

    private static final String DEVICE_ID = "";

    private static final String PARTNER_ID = "fulan";

    private static final String COURSE_ID = "";

    private static final String PUBLIC_KEY = "gankao123!#$";

    private static final int  STUDENT_TIME = 5*60*1000;  //学生提前时间  ->  5分钟

    private HourClassDao hourClassDao = new HourClassDao();

    private ExcellentCoursesDao excellentCoursesDao = new ExcellentCoursesDao();

    private ClassOrderDao classOrderDao = new ClassOrderDao();

    private UserDao userDao = new UserDao();

    public Map<String,Object> gotoNewClass(ObjectId id,ObjectId userId) throws Exception{
        Map<String,Object> map = new HashMap<String, Object>();
        //是否过期
        HourClassEntry hourClassEntry = hourClassDao.getEntry(id);
        if(hourClassEntry==null){
            throw  new Exception("该课程不存在！");
        }
        ExcellentCoursesEntry excellentCoursesEntry = excellentCoursesDao.getEntry(hourClassEntry.getParentId());
        if(excellentCoursesEntry==null){
            throw  new Exception("该课程不存在！");
        }
        ClassOrderEntry classOrderEntry = classOrderDao.getEntry(id, hourClassEntry.getParentId(), userId);
        if(classOrderEntry==null){
            throw  new Exception("无该订单！");
        }
        long current = System.currentTimeMillis();
        long start = hourClassEntry.getStartTime() -STUDENT_TIME;
        long end = hourClassEntry.getStartTime() + hourClassEntry.getCurrentTime();
        if(current>start){//上课中
            UserEntry userEntry = userDao.findByUserId(userId);
            map.put("sign",createSign(userId.toString(),userEntry.getMobileNumber()));
        }else{
            throw  new Exception("上课时间未到，请稍后进入");
        }
        return map;


    }


    public static String  createSign(String userId,String mobilePhone){
        String sign = "";
        //组装参数
        StringBuffer  sb = new StringBuffer();
        sb.append("device_id");
        sb.append("=");
        sb.append(userId);
        sb.append("&");
        sb.append("mobile");
        sb.append("=");
        sb.append(mobilePhone);
        sb.append("&");
        sb.append("partner_id");
        sb.append("=");
        sb.append(PARTNER_ID);
        sb.append(PUBLIC_KEY);
        sign = MD5(sb.toString());
        //拼装返回
        //myLiveCourseList?device_id=**&partner_id=**&course_id=**&sign=**"
        String url = "gankao://loginFromPartner?device_id="+userId+"&mobile="+mobilePhone+"&partner_id="+PARTNER_ID+"&sign="+sign;
        return url;
    }

    public static void main(String[] args){
        try{
            System.out.println(MD5("device_id=5b3b094e3d4df9509e29f241&mobile=18916638449&partner_id=fulangankao123!#$"));
        }catch (Exception e){

        }


    }

    public final static String MD5(String s) {
        char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        try {
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
