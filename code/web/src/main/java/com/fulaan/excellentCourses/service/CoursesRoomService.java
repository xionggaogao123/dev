package com.fulaan.excellentCourses.service;

import com.db.excellentCourses.CoursesRoomDao;
import com.fulaan.excellentCourses.api.CoursesRoomAPI;
import com.fulaan.excellentCourses.util.RoomUtil;
import com.pojo.excellentCourses.CoursesRoomEntry;
import org.bson.types.ObjectId;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;
import java.util.TreeMap;

/**
 * 直播间相关
 * Created by James on 2018-05-08.
 */
@Service
public class CoursesRoomService {
    //userId
    private static final String CC_USERID = "A39B161DFE593520";
    //apikey
    private static final String CC_API_KEY = "s521Bt59I02irf0UKQjuwhpyCAyuYJNI";
    //直播模板  模板四 视频直播+聊天互动+直播文档
    private static final String CC_TEMPLATETYPE = "4";
    //验证方式  默认 0：接口验证，需要填写下面的checkurl；1：密码验证，需要填写下面的playpass；2：免密码验证
    private static final String CC_AUTHTYPE = "1";
    //验证地址
    private static final String CC_CHECKURL = "";
    //讲师端密码
    private static final String CC_PUBLISHERPASS = "123456";
    //助教端密码
    private static final String CC_ASSISTANTPASS = "123456";
    //播放端密码
    private static final String CC_PLAYPASS = "123456";
    //提示语
    private static final String CC_PLAYERBACKGROUNDHINT = "直播时间还未开始，请耐心等待！";

    private CoursesRoomDao coursesRoomDao = new CoursesRoomDao();

    //cc登陆回调
    public void backCreate(Map<String,String> map){


    }


    /**
     * 创建直播间
     */
    public  void createCourses(String name,String description,ObjectId contactId,String dateTime){
        Map<String,String> map = new TreeMap<String, String>();
        map.put("userid",CC_USERID);
        map.put("templatetype",CC_TEMPLATETYPE);
        map.put("authtype",CC_AUTHTYPE);
        map.put("publisherpass",CC_PUBLISHERPASS);
        map.put("assistantpass",CC_ASSISTANTPASS);
        map.put("playpass",CC_PLAYPASS);
        map.put("checkurl",CC_CHECKURL);
        String id = "";
        try{
            //map.put("name", URLEncoder.encode(name, "UTF-8"));
            map.put("name", URLEncoder.encode(name, "utf-8"));
            map.put("desc",URLEncoder.encode(description, "utf-8"));
            //map.put("desc",description);
            map.put("livestarttime", URLEncoder.encode(dateTime,"utf-8"));
            //long time = new Date().getTime();
            String sysCode = RoomUtil.createHashedQueryString(map,CC_API_KEY);
            String str3 = URLDecoder.decode(sysCode, "utf-8");
            String str =  CoursesRoomAPI.createRoom(str3);
            JSONObject dataJson = new JSONObject(str);
            String rows = dataJson.getString("result");
            if(rows.equals("OK")){
                JSONObject rows2 =dataJson.getJSONObject("room");
                id =  rows2.getString("id");
                CoursesRoomEntry coursesRoomEntry = new CoursesRoomEntry(
                        CC_USERID,
                        name,
                        id,
                        contactId,
                        description,
                        Integer.parseInt(CC_AUTHTYPE),
                        Integer.parseInt(CC_TEMPLATETYPE),
                        CC_PUBLISHERPASS,
                        CC_ASSISTANTPASS,
                        CC_PLAYPASS,
                        CC_CHECKURL,
                        dateTime,
                        CC_PLAYERBACKGROUNDHINT);
                coursesRoomDao.addEntry(coursesRoomEntry);
            }else{

            }

        }catch(Exception e){

        }

    }
}
