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

    private static String Key = "4A9739DBB362FE6A240CDA937FF333FB";
    
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
    public static boolean isTenpaySign(SortedMap<String, String> smap) {
        StringBuffer sb = new StringBuffer();
        Set es = smap.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            String v = (String) entry.getValue();
            if (!"sign".equals(k) && null != v && !"".equals(v) && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" + Key);
        /** 验证的签名 */
        String sign = MD5Util.MD5Encode(sb.toString(), "utf-8").toUpperCase();
        /** 微信端返回的合法签名 */
        String validSign = ((String) smap.get("sign")).toUpperCase();
        return validSign.equals(sign);
    }
 
    
    
    
   

}
