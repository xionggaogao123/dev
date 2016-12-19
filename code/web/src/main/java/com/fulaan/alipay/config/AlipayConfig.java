package com.fulaan.alipay.config;

/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *版本：3.3
 *日期：2012-08-10
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
	
 *提示：如何获取安全校验码和合作身份者ID
 *1.用您的签约支付宝账号登录支付宝网站(www.alipay.com)
 *2.点击“商家服务”(https://b.alipay.com/order/myOrder.htm)
 *3.点击“查询合作者身份(PID)”、“查询安全校验码(Key)”

 *安全校验码查看时，输入支付密码后，页面呈灰色的现象，怎么办？
 *解决方法：
 *1、检查浏览器配置，不让浏览器做弹框屏蔽设置
 *2、更换浏览器或电脑，重新登录查询。
 */

public class AlipayConfig {

    //↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
    // 合作身份者ID，以2088开头由16位纯数字组成的字符串
    public static String partner = "2088311713347385";

    //商户收款账号
    public static String SELLER = "zhifu@fulaan.com";

    // 商户的私钥
    public static String SELLER_KEY = "r1pv9jky8s1ubzgkvpxvkls520y10jyc";

    //支付宝公钥
    public static String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwV" +
            "fgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAo" +
            "prih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5K" +
            "siNG9zpgmLCUYuLkxpLQIDAQAB";


    //商户私钥，pkcs8格式
    public static String RSA_PRIVATE = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBALQM3ozAvbJDk5rK" +
            " WXJbZ1WloXjEoYlMf51dlkB8BmbU9b87u3WnVkVVRpJ9UrqP7mMOcbzvy52+bmmH" +
            " ibpusXyqZaICDGBG1+fxEpTpH9dtRJQWsOn5yNibb2a31IlhMSxGH9utskqTs+qs" +
            " UXIUZhNUKGzHJrnpGkZNwgkL3ujDAgMBAAECgYBP/v/KTcB6uaeVOJ5xPsc0uHh7" +
            " rfGPjRuOEYy2beRyP+BH7QQnDAg6md7XKzNcjKKABGToaypulbN806mP2aqlWARt" +
            " f6gTHQdaT7NsOBgXNWMKxQnNX1/zpMXl2fnZVHs5wUry70YjhR9s+Bwnd9zUim+0" +
            " PHO6yef8gCKQVpVX4QJBAOMJ+njxD3GYTFAMwco8hpmEB2bObrZSl0oKkr3yRo8/" +
            " /kdPhqSzIC8mqGcCQEQMOGZv8S4sVZ7drmV8b3/NamkCQQDLBHYkdjdhOkSvsJj5" +
            " 0aZ7SWbLK2EtOX6pLOOXawzV4D6beksiM8TRlFyh9xY1GkBtf6oI2G1HRpzLY+Ro" +
            " e1xLAkEA1/wEJc3dzUj/QCACs2vE+IIbsHet5xDHEA7i7oRvD5PnSNuk0UX+1hxG" +
            " lEgV7yZY+UqA/FX81fc6Ex2/zKsUEQJBAKA6aTPlyVeHMWaoLlQczxRGnkaDejLD" +
            " SeAjeYysBHlYfpEfN+VKjnAKdAGwjWpjya2iMITCq5yxclZkBIUht3kCQQDBUFcj" +
            " YMkjoLhRGkOJ44aEcwLE8GXK0guQdlmvC3R8Y7BGN2r+zxncFT8e5OJzv80W6PBl" +
            " wqEVw5IttGBtV0fH";

    // 调试用，创建TXT日志文件夹路径
    public static String log_path = "D:\\";

    // 字符编码格式 目前支持 gbk 或 utf-8
    public static String input_charset = "utf-8";

    // MD5 签名方式
    public static String sign_md5 = "MD5";

    //RSA 签名方式
    public static String sign_rsa = "RSA";


    //↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

}
