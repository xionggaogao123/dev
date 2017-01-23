package fulaan.social.connect;


import fulaan.social.exception.ConnectException;
import fulaan.social.model.UserInfo;

/**
 * Created by jerry on 2017/1/16.
 * @see QQAuth
 * @see WeChatAuth
 *
 * 授权接口
 */
public interface Auth {

    String getAuthUrl();

    UserInfo getUserInfo(String authCode) throws ConnectException;

    String getWapAuthUrl();

    UserInfo getUserInfoByWap(String authCode) throws ConnectException;

}
