package com.fulaan.controlservice.service;

import com.db.user.NewVersionUserRoleDao;
import com.fulaan.controlservice.api.ControlTokenAPI;
import com.fulaan.user.model.ThirdLoginEntry;
import com.fulaan.user.model.ThirdType;
import com.fulaan.user.service.UserService;
import com.pojo.user.NewVersionUserRoleEntry;
import com.pojo.user.UserEntry;
import fulaan.social.model.Sex;
import fulaan.social.model.UserInfo;
import org.bson.types.ObjectId;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by James on 2018-04-08.
 */
@Service
public class ControlTokenService {

    private UserService userService = new UserService();

    private NewVersionUserRoleDao newVersionUserRoleDao = new NewVersionUserRoleDao();

    public String getAccessToken(String sysCode){
        String str= ControlTokenAPI.getAccessToken(sysCode);
        try{
            JSONObject dataJson = new JSONObject(str);
            String rows = dataJson.getString("retCode");
            if(rows.equals("000000")){
                JSONObject rows2 =dataJson.getJSONObject("data");
                return rows2.getString("accessToken");
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return "";
    }
    public static void main(String[] args){
        System.out.print("1");
    }

    public String getUsessionId(String sysCode){
        String accessToken = getAccessToken(sysCode);
        String str = ControlTokenAPI.getUsessionId(accessToken);
        return str;
    }

    public String getTicket(String sysCode){
        String accessToken = getAccessToken(sysCode);
        String str = ControlTokenAPI.getUsessionId(accessToken);
        String str2 = ControlTokenAPI.getTicket(accessToken,str);

        return str2;
    }
/*{
"retCode": "000000",
"retDesc": "success",
"data": {
"userId": "0040032E20184955GHJHK9EACA1501F8F",
"name": "周力*",
"gender": "1",
"dafaultIdentity": "1"
"orgRelList":[
{
"orgCode": "31450069822",
"orgName": "某某中学",
"orgIdentity": "1"
}
]
}
}*/

    //获取用户信息
    public UserEntry getUserInfo(String ticket,String sysCode){
        String accessToken = getAccessToken(sysCode);
        //String usessionId  = ControlTokenAPI.getUsessionId(accessToken);
        String resultStr = ControlTokenAPI.validaTicket(accessToken, ticket);
        try {
            JSONObject dataJson = new JSONObject(resultStr);
            String rows = dataJson.getString("retCode");
            if(rows.equals("000000")){
                JSONObject rows2 =dataJson.getJSONObject("data");
                if(rows2!=null){
                    UserInfo userInfo = new UserInfo();
                    userInfo.setAvatar("");
                    userInfo.setNickName(rows2.getString("name"));
                    userInfo.setSex(Sex.MALE);
                    userInfo.setUniqueId(rows2.getString("userId"));
                    UserEntry userEntry = this.getThirdInfo(userInfo, ThirdType.COUNTRY);
                    return userEntry;
                }
            }

        } catch (Exception e) {
            e.getMessage();
        }
        return null;
    }

    //第三方登陆处理
    private UserEntry getThirdInfo(UserInfo userInfo, ThirdType type) throws IOException {
        Map<String, Object> query = new HashMap<String, Object>();
        if (type.getCode() == ThirdType.COUNTRY.getCode()) {
            query.put("oid", userInfo.getUniqueId());
            query.put("type", ThirdType.COUNTRY.getCode());
        }
        UserEntry userEntry = userService.getThirdEntryByMap(query);
        if (userEntry == null) { //创建用户
            userEntry = userService.createUser(new ObjectId().toString(), userInfo.getNickName(), userInfo.getSex().getType());
            if (type.getCode() == 3) {
                //保存第三方登录数据
                ThirdLoginEntry thirdLoginEntry = new ThirdLoginEntry(userEntry.getID(), userInfo.getUniqueId(), null, ThirdType.COUNTRY);
                userService.saveThirdEntry(thirdLoginEntry);
            }
        }
        //保存为家长用户
        if(null==newVersionUserRoleDao.getEntry(userEntry.getID())){
            newVersionUserRoleDao.saveEntry(new NewVersionUserRoleEntry(userEntry.getID(), 0));
        }
        return userEntry;
    }




    public UserEntry receiveTicket(String ticket,String sysCode){
        String accessToken = ControlTokenAPI.getAccessToken(sysCode);





        return new UserEntry();
    }
}
