package fulaan.social.model;

/**
 * Created by jerry on 2017/1/19.
 *
 * @see fulaan.social.connect.Auth
 * 授权类型
 * QQ 微信
 */
public enum AuthType {

    QQ("qq", 1),
    WECHAT("wechat", 2);

    private String type;
    private int code;

    AuthType(String type, int code) {
        this.type = type;
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "AuthType{" +
                "type='" + type + '\'' +
                ", code=" + code +
                '}';
    }
}