package fulaan.social.factory;

import fulaan.social.connect.Auth;
import fulaan.social.connect.QQAuth;
import fulaan.social.connect.WeChatAuth;
import fulaan.social.model.AuthType;

/**
 * Created by moslpc on 2017/1/17.
 */
public class AuthFactory {

    public static Auth getQQAuth() {
        return new QQAuth();
    }

    public static Auth getWechatAuth() {
        return new WeChatAuth();
    }

    public static Auth getAuth(AuthType authType) {
        if (authType == AuthType.QQ) {
            return new QQAuth();
        } else if (authType == AuthType.WECHAT) {
            return new WeChatAuth();
        }
        return null;
    }

}
