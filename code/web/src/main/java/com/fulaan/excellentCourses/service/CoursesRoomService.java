package com.fulaan.excellentCourses.service;

import com.db.excellentCourses.CoursesRoomDao;
import com.db.user.UserDao;
import com.fulaan.excellentCourses.api.CoursesRoomAPI;
import com.fulaan.excellentCourses.dto.CCLoginDTO;
import com.fulaan.excellentCourses.dto.ReplayDTO;
import com.fulaan.excellentCourses.util.RoomUtil;
import com.pojo.excellentCourses.CoursesRoomEntry;
import com.pojo.user.UserEntry;
import com.sys.utils.AvatarUtils;
import com.sys.utils.DateTimeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;

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

    private static final String CC_BACKAUTHTYPE = "0";
    //验证地址
    private static final String CC_CHECKURL = "";
    //回调验证地址
    private static final String CC_BACKCHECKURL = "http/www.jiaxiaomei.com/web/excellentCourses/openBackCreate.do";
    //讲师端密码
    private static final String CC_PUBLISHERPASS = "123456";
    //助教端密码
    private static final String CC_ASSISTANTPASS = "123456";
    //播放端密码
    private static final String CC_PLAYPASS = "123456";
    //提示语
    private static final String CC_PLAYERBACKGROUNDHINT = "直播时间还未开始，请耐心等待！";

    private CoursesRoomDao coursesRoomDao = new CoursesRoomDao();

    private static final Logger logger =Logger.getLogger(CoursesRoomService.class);

    private UserDao userDao = new UserDao();

    //cc登陆回调
    public void backCreate(Map<String,String> map){


    }


    /**
     * 创建密码登陆直播间
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
            map.put("playerbackgroundhint", URLEncoder.encode(CC_PLAYERBACKGROUNDHINT, "utf-8"));
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
/*
参数	说明	备注
roomid	直播间id
userid	CC账户ID
pagenum	每页显示的个数	可选，系统默认值为50
pageindex	页码	可选，系统默认值为1
starttime	查询起始时间,如需按时间范围查询可添加该参数和下面的endtime参数，该查询是按直播的开始时间作为查询条件的。	可选，如果填写该参数则endtime参数必填；精确到分钟，例："2015-01-01 12:30"
endtime	查询截止时间	可选 ，如果填写该参数则starttime必填；精确到分钟，例："2015-01-02 12:30"
liveid	直播id*/
    /**
     * 获取回放列表
     */
    public List<ReplayDTO> getBackList(ObjectId cid,String userName,String teacherName,long stm,long etm){
        List<ReplayDTO> replayDTOList =  new ArrayList<ReplayDTO>();
        CoursesRoomEntry coursesRoomEntry = coursesRoomDao.getEntry(cid);
        if(coursesRoomEntry==null){
            return replayDTOList;
        }
        String roomid = coursesRoomEntry.getRoomId();
        Map<String,String> map = new TreeMap<String, String>();
        map.put("userid",CC_USERID);
        map.put("roomid",roomid);
        try{
            String sysCode = RoomUtil.createHashedQueryString(map,CC_API_KEY);
            String str3 = URLDecoder.decode(sysCode, "utf-8");
            String str =  CoursesRoomAPI.getRoomList(str3);
            JSONObject dataJson = new JSONObject(str);
            String rows = dataJson.getString("result");
            if(rows.equals("OK")){
                JSONArray jsonArray = dataJson.getJSONArray("records");
                if(jsonArray!=null&&jsonArray.length()>0) {
                    for (int j = 0; j < jsonArray.length(); j++) {
                        JSONObject rows2 = jsonArray.getJSONObject(j);
                        String id =  rows2.getString("id");
                        String liveId =  rows2.getString("liveId");
                        String stopTime =  rows2.getString("stopTime");
                        String startTime =  rows2.getString("startTime");
                        int recordStatus =  rows2.getInt("recordStatus");
                        String recordVideoId =  rows2.getString("recordVideoId");
                        String replayUrl =  rows2.getString("replayUrl");
                        ReplayDTO dto = new ReplayDTO(id,liveId,roomid,recordVideoId,CC_USERID,userName,CC_PLAYPASS,startTime,stopTime,recordStatus,replayUrl,teacherName);
                        //if(checkTime(stm,etm,startTime,stopTime)){
                            replayDTOList.add(dto);
                      //  }

                    }
                }
            }else{
            }
        }catch(Exception e){

        }
        Collections.reverse(replayDTOList);//逆序排列
        return replayDTOList;

    }

    /**
     * 获取回放列表
     */
    public List<ReplayDTO> getAllBackList(ObjectId cid,String userName,String teacherName){
        List<ReplayDTO> replayDTOList =  new ArrayList<ReplayDTO>();
        CoursesRoomEntry coursesRoomEntry = coursesRoomDao.getEntry(cid);
        if(coursesRoomEntry==null){
            return replayDTOList;
        }
        String roomid = coursesRoomEntry.getRoomId();
        Map<String,String> map = new TreeMap<String, String>();
        map.put("userid",CC_USERID);
        map.put("roomid",roomid);
        try{
            String sysCode = RoomUtil.createHashedQueryString(map,CC_API_KEY);
            String str3 = URLDecoder.decode(sysCode, "utf-8");
            String str =  CoursesRoomAPI.getRoomList(str3);
            JSONObject dataJson = new JSONObject(str);
            String rows = dataJson.getString("result");
            if(rows.equals("OK")){
                JSONArray jsonArray = dataJson.getJSONArray("records");
                if(jsonArray!=null&&jsonArray.length()>0) {
                    for (int j = 0; j < jsonArray.length(); j++) {
                        JSONObject rows2 = jsonArray.getJSONObject(j);
                        String id =  rows2.getString("id");
                        String liveId =  rows2.getString("liveId");
                        String stopTime =  rows2.getString("stopTime");
                        String startTime =  rows2.getString("startTime");
                        int recordStatus =  rows2.getInt("recordStatus");
                        String recordVideoId =  rows2.getString("recordVideoId");
                        String replayUrl =  rows2.getString("replayUrl");
                        ReplayDTO dto = new ReplayDTO(id,liveId,roomid,recordVideoId,CC_USERID,userName,CC_PLAYPASS,startTime,stopTime,recordStatus,replayUrl);
                        replayDTOList.add(dto);
                    }
                }
            }else{
            }
        }catch(Exception e){

        }
        Collections.reverse(replayDTOList);//逆序排列
        return replayDTOList;

    }


    public boolean checkTime(long stm,long etm,String ostm,String estm){
        long stm2 = DateTimeUtils.getStrToLongTime(ostm, "yyyy-MM-dd HH:mm:ss");
        long etm2 = DateTimeUtils.getStrToLongTime(estm, "yyyy-MM-dd HH:mm:ss");
        if(stm2>stm && etm2<etm ){//1
            return true;
        }

        if(etm2 > stm && etm2 < etm ){//2
            return true;
        }

        if(stm2 < stm && etm2 > etm){//3
            return true;
        }

        if(stm2 >stm && stm2 < etm){//4
            return true;
        }
        return false;
    }


    public static void main(String[] args){
        CoursesRoomService coursesRoomService = new CoursesRoomService();
        long startTime = System.currentTimeMillis();
        //coursesRoomService.getBackList("99DC40755A6346189C33DC5901307461","","");
        long endTime = System.currentTimeMillis();
        System.out.println("时间");
        System.out.println(endTime - startTime);
    }



    /**
     * 创建回调登陆直播间
     */
    public  void createBackCourses(String name,String description,ObjectId contactId,String dateTime){
        Map<String,String> map = new TreeMap<String, String>();
        map.put("userid",CC_USERID);
        map.put("templatetype",CC_TEMPLATETYPE);
        map.put("authtype",CC_BACKAUTHTYPE);
        map.put("publisherpass",CC_PUBLISHERPASS);
        map.put("assistantpass",CC_ASSISTANTPASS);
        map.put("playpass",CC_PLAYPASS);
        map.put("checkurl",CC_BACKCHECKURL);
        String id = "";
        try{
            map.put("playerbackgroundhint", URLEncoder.encode(CC_PLAYERBACKGROUNDHINT, "utf-8"));
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
                        Integer.parseInt(CC_BACKAUTHTYPE),
                        Integer.parseInt(CC_TEMPLATETYPE),
                        CC_PUBLISHERPASS,
                        CC_ASSISTANTPASS,
                        CC_PLAYPASS,
                        CC_BACKCHECKURL,
                        dateTime,
                        CC_PLAYERBACKGROUNDHINT);
                coursesRoomDao.addEntry(coursesRoomEntry);
            }else{

            }

        }catch(Exception e){

        }

    }

    /**
     * 处理cc接口登陆的验证信息
     * @return
    result	字符串	验证结果，除“ok”外，云平台将其他结果均认为验证失败，即不允许登陆
    message	字符串	当用户不能登陆（验证结果不为“ok”）时，展示给登陆用户的提示信息。(长度不能超过40个字符)
    id	字符串	用户的唯一标示(长度不能超过40个字符)
    name	字符串	用户的名称，在聊天室中显示该名称(长度不能超过20个字符)
    avatar	字符串	可选，用户的头像，在直播页面中显示该用户头像信息(长度不能超过400个字符，如果超过400个字符，登录会提示参数错误)
    customua	字符串	可选，用户自定义UA信息（该信息不能包含\、/、|等特殊字符，长度不能超过50个字符）
    viewercustommark	字符串	可选，用户自定义标识信息（该信息不能包含\、/、|等特殊字符，长度不能超过300个字符）
    marquee	字符串	可选，跑马灯信息(长度不能超过2000个字符)
     */
    public Map<String,Object> openBackCreate(CCLoginDTO ccLoginDTO){
        Map<String,Object> map = new HashMap<String, Object>();
        String userId = ccLoginDTO.getUserid();
        String roomId = ccLoginDTO.getRoomid();
        String viewerName = ccLoginDTO.getViewername();
        String viewerToken = ccLoginDTO.getViewertoken();
        logger.error("登陆回调："+userId+"--"+roomId+"--"+viewerName+"--"+viewerToken);
        map.put("result","no");
        map.put("message","登陆失败");
        Map<String,Object> userMap = new HashMap<String, Object>();
        if(userId.equals(CC_USERID)){//验证cc用户通过
            CoursesRoomEntry coursesRoomEntry = coursesRoomDao.getRoomEntry(roomId);
            if(coursesRoomEntry!=null){//验证直播间存在
                if(ObjectId.isValid(viewerToken)){//验证码是否为用户id
                    UserEntry userEntry = userDao.findByUserId(new ObjectId(viewerToken));
                    if(userEntry!=null){//验证用户成功
                        map.put("result","ok");
                        map.put("message","登陆成功");
                        userMap.put("id",viewerToken);
                        String name = StringUtils.isNotBlank(userEntry.getNickName())?userEntry.getNickName():userEntry.getUserName();
                        userMap.put("name",name);
                        userMap.put("avatar", AvatarUtils.getAvatar(userEntry.getAvatar(),userEntry.getRole(),userEntry.getSex()));
                        userMap.put("customua","");
                        userMap.put("viewercustommark","");
                        userMap.put("marquee","");
                    }
                }
            }
        }
        map.put("user",userMap);
        logger.error("登陆成功");
        return map;
    }
}
