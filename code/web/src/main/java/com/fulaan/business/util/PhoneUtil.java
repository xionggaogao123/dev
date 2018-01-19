package com.fulaan.business.util;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by James on 2018/1/18.
 */
public class PhoneUtil {

    public static void main(String[] args) throws Exception{
        getMobileLocation("13788951487");
    }
    public static String calcMobileCity(String mobileNumber) throws MalformedURLException {

        String jsonString = null;
        JSONArray array = null;
        JSONObject jsonObject = null;
        String name = "";
        //获取拍拍网的API地址
        String urlString = "http://virtual.paipai.com/extinfo/GetMobileProductInfo?mobile="+mobileNumber+"&amount=10000&callname=getPhoneNumInfoExtCallback";
        StringBuffer sb = new StringBuffer();
        BufferedReader buffer;
        URL url = new URL(urlString);
        try{
            //获取URL地址中的页面内容
            InputStream in = url.openStream();
            // 解决乱码问题
            buffer = new BufferedReader(new InputStreamReader(in,"gb2312"));
            String line = null;
            //一行一行的读取数据
            while((line = buffer.readLine()) != null){
                sb.append(line);
            }
            in.close();
            buffer.close();
            // System.out.println(sb.toString());
            jsonString = sb.toString();
            // 替换掉“getPhoneNumInfoExtCallback(，);<!--[if !IE]>|xGv00|6741027ad78d9b06f5642b25ebcb1536<![endif]-->”，让它能转换为JSONArray对象
            jsonString = jsonString.replace("getPhoneNumInfoExtCallback(", "[");
            jsonString = jsonString.replace(");<!--[if !IE]>|xGv00|6741027ad78d9b06f5642b25ebcb1536<![endif]-->", "]");
            // 把jsonString转化为json对象
            array = JSONArray.fromObject(jsonString);
            // 获取JSONArray的JSONObject对象，便于读取array里的键值对
            jsonObject = array.getJSONObject(0);
            name = jsonObject.getString("cityname");
        }catch(Exception e){
            e.printStackTrace();
        }
        //从JSONObject对象中读取城市名称
        return name;
    }

    /**
     * 手机号码归属地
     * @param tel  手机号码
     * @return 135XXXXXXXX,联通/移动/电信,湖北武汉
     * @throws Exception
     * @author
     */
    public static String getMobileLocation(String tel) throws Exception{
        Pattern pattern = Pattern.compile("1\\d{10}");
        Matcher matcher = pattern.matcher(tel);
        if(matcher.matches()){
            String url = "http://life.tenpay.com/cgi-bin/mobile/MobileQueryAttribution.cgi?chgmobile=" + tel;
            String result = callUrlByGet(url,"GBK");
            StringReader stringReader = new StringReader(result);
            InputSource inputSource = new InputSource(stringReader);
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(inputSource);
            String retmsg = document.getElementsByTagName("retmsg").item(0).getFirstChild().getNodeValue();
            if(retmsg.equals("OK")){
                String supplier = document.getElementsByTagName("supplier").item(0).getFirstChild().getNodeValue().trim();
                String province = document.getElementsByTagName("province").item(0).getFirstChild().getNodeValue().trim();
                String city = document.getElementsByTagName("city").item(0).getFirstChild().getNodeValue().trim();
                if (province.equals("-") || city.equals("-")) {

//                    return (tel + "," + supplier + ","+ getLocationByMobile(tel));
                    return (getLocationByMobile(tel) + "," + supplier);
                }else {

//                    return (tel + "," + supplier + ","+ province + city);
                    return (province + city + "," + supplier );
                }

            }else {

                return "无此号记录！";

            }

        }else{

            return tel+ "：手机号码格式错误！";

        }

    }
    /**
     * 归属地查询
     * @param mobile
     * @return mobileAddress
     */
    @SuppressWarnings("unused")
    private static String getLocationByMobile(final String mobile) throws ParserConfigurationException, SAXException, IOException {
        String MOBILEURL = " http://www.youdao.com/smartresult-xml/search.s?type=mobile&q=";
        String result = callUrlByGet(MOBILEURL + mobile, "GBK");
        StringReader stringReader = new StringReader(result);
        InputSource inputSource = new InputSource(stringReader);
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(inputSource);

        if (!(document.getElementsByTagName("location").item(0) == null)) {
            return document.getElementsByTagName("location").item(0).getFirstChild().getNodeValue();
        }else{
            return "无此号记录！";
        }
    }
    /**
     * 获取URL返回的字符串
     * @param callurl
     * @param charset
     * @return
     */
    private static String callUrlByGet(String callurl,String charset){
        String result = "";
        try {
            URL url = new URL(callurl);
            URLConnection connection = url.openConnection();
            connection.connect();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(),charset));
            String line;
            while((line = reader.readLine())!= null){
                result += line;
                result += "\n";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return result;
    }
}
