package com.fulaan.txpay.Utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;



public class PayCommonUtil {

    private static String Key = "你们的Key";
    
    public static String getChildrenText(List children) {  
        StringBuffer sb = new StringBuffer();  
        if(!children.isEmpty()) {  
            Iterator it = children.iterator();  
            while(it.hasNext()) {  
                Element e = (Element) it.next();
                String name = e.getName();  
                String value = e.getTextNormalize();  
                List list = e.getChildren();  
                sb.append("<" + name + ">");  
                if(!list.isEmpty()) {  
                    sb.append(getChildrenText(list));  
                }  
                sb.append(value);  
                sb.append("</" + name + ">");  
            }  
        }  
          
        return sb.toString();  
  }

    
  //xml解析  
    public static Map doXMLParse(String strxml) throws JDOMException, IOException {
          strxml = strxml.replaceFirst("encoding=\".*\"", "encoding=\"UTF-8\"");  
 
          if(null == strxml || "".equals(strxml)) {  
              return null;  
          }  
            
          Map m = new HashMap();  
            
          InputStream in = new ByteArrayInputStream(strxml.getBytes("UTF-8"));  
          SAXBuilder builder = new SAXBuilder();
          Document doc = builder.build(in);
          Element root = doc.getRootElement();
          List list = root.getChildren();  
          Iterator it = list.iterator();  
          while(it.hasNext()) {  
              Element e = (Element) it.next();
              String k = e.getName();  
              String v = "";  
              List children = e.getChildren();  
              if(children.isEmpty()) {  
                  v = e.getTextNormalize();  
              } else {  
                  v = getChildrenText(children);  
              }  
                
              m.put(k, v);  
          }  
            
          //关闭流  
          in.close();  
            
          return m;  
    }  

    
    /**
     * 验证回调签名
     * @return
     */
    public static boolean isTenpaySign(Map<String, String> map) {
        String result_code = (String) map.get("result_code");  
        String out_trade_no  = (String) map.get("out_trade_no");  
        String total_fee  = (String) map.get("total_fee");  
        String sign  = (String) map.get("sign");  
        Double amount = new Double(total_fee)/100;//获取订单金额  
        String attach = (String) map.get("attach");  
        String sn = out_trade_no.split("\\|")[0];//获取订单编号  

        
        String fee_type  = (String) map.get("fee_type");  
        String bank_type  = (String) map.get("bank_type");  
        String cash_fee  = (String) map.get("cash_fee");  
        String is_subscribe  = (String) map.get("is_subscribe");  
        String nonce_str  = (String) map.get("nonce_str");  
        String openid  = (String) map.get("openid");  
        String return_code  = (String) map.get("return_code");  
        String sub_mch_id  = (String) map.get("sub_mch_id");  
        String time_end  = (String) map.get("time_end");  
        String trade_type  = (String) map.get("trade_type");  
        String transaction_id  = (String) map.get("transaction_id");  
        String appid = (String) map.get("appid");
        String partner = (String) map.get("mch_id");

                //需要对以下字段进行签名  
        SortedMap<Object, Object> packageParams = new TreeMap<Object, Object>();  
        packageParams.put("appid", appid);     
        packageParams.put("bank_type", bank_type);    
        packageParams.put("cash_fee", cash_fee);    
        packageParams.put("fee_type", fee_type);      
        packageParams.put("is_subscribe", is_subscribe);    
        packageParams.put("mch_id", partner);    
        packageParams.put("nonce_str", nonce_str);        
        packageParams.put("openid", openid);   
        packageParams.put("out_trade_no", out_trade_no);  
        packageParams.put("result_code", result_code);    
        packageParams.put("return_code", return_code);        
        packageParams.put("sub_mch_id", sub_mch_id);   
        packageParams.put("time_end", time_end);  
        packageParams.put("total_fee", total_fee);    
        packageParams.put("trade_type", trade_type);   
        packageParams.put("transaction_id", transaction_id);  
        String s = WXSignUtils.createSign("UTF-8", packageParams);
        if (sign.equals(s)) {
            return true;
        } else {
            return false;
        }
    }
 
    
    
    
   

}
