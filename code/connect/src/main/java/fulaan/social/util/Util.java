package fulaan.social.util;

import fulaan.social.constant.Charset;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by moslpc on 2017/1/16.
 */
public class Util {

    public static String strURLEncodeUTF8(String value){
        try {
            return URLEncoder.encode(value, Charset.UTF_8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
}
