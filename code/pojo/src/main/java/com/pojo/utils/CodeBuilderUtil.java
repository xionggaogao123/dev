package com.pojo.utils;

import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

import java.io.File;
import java.io.FileWriter;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

/**
 * Created by scott on 2017/9/22.
 * 代码生成器
 */
public class CodeBuilderUtil {
    //公共部分
    private static final String RT_1 = "\r\n";
    private static final String RT_2 = RT_1+RT_1;
    private static final String BLANK_1 =" ";
    private static final String BLANK_4 ="    ";
    private static final String BLANK_8 =BLANK_4 + BLANK_4;
    public void createEntry(Class c,String path,String packagePath,
                            List<Map<String,String>> list,Map<String,Object> map,
                            Map<String,String> name) throws Exception {
        String cName = c.getName();
        String suffix=".java";
        String lastName=getLastChar(cName);
        String fileName = System.getProperty("user.dir").substring(0,System.getProperty("user.dir").length()-6) + path
                + "/" + lastName+suffix;
        File f = new File(fileName);
        FileWriter fw = new FileWriter(f);
        fw.write("package"+BLANK_4+packagePath);
        fw.write(RT_2+RT_2+RT_2+RT_2+RT_2);
        fw.write("public class"+BLANK_1+lastName+BLANK_1+"extends"+BLANK_1+"BaseDBObject{");
        fw.write(RT_1);
        fw.write("public"+BLANK_1+lastName+"(DBObject"+BLANK_1+"dbObject){");
        fw.write(RT_1);
        fw.write("setBaseEntry((BasicDBObject)dbObject)");
        fw.write(RT_1);
        fw.write("}");
        fw.write(RT_2);
        fw.write("public"+BLANK_1+lastName+"(");
        fw.write(RT_1);
        int count=0;
        for(Map<String,String> item:list){
            if(count!=list.size()) {
                for (Map.Entry<String, String> entry : item.entrySet()) {
                    fw.write(entry.getKey() + BLANK_1 + entry.getValue() + ",");
                    fw.write(RT_1);
                }
            }else{
                for (Map.Entry<String, String> entry : item.entrySet()) {
                    fw.write(entry.getKey() + BLANK_1 + entry.getValue() + "){");
                    fw.write(RT_1);
                }
            }
            count++;
        }
        fw.write("BasicDBObject basicDBObject=new BasicDBObject()");
        for(Map.Entry<String,Object> mapEntry:map.entrySet()){
            if(mapEntry.getValue() instanceof List){
                List<Object>  itemEntry=(List)mapEntry.getValue();
                if(itemEntry.get(0) instanceof ObjectId||
                        itemEntry.get(0) instanceof Integer||
                        itemEntry.get(0) instanceof Boolean||
                        itemEntry.get(0) instanceof Double||
                        itemEntry.get(0) instanceof Float||
                        itemEntry.get(0) instanceof String){
                    fw.write(".append(\""+name.get(mapEntry.getKey())+"\","+"MongoUtils.convert("+mapEntry.getKey()+"))");
                    fw.write(RT_1);
                }else{
                    fw.write(".append(\""+name.get(mapEntry.getKey())+"\","+"MongoUtils.fetchDBObjectList("+mapEntry.getKey()+"))");
                    fw.write(RT_1);
                }
            }else{
                fw.write(".append(\""+name.get(mapEntry.getKey())+"\","+mapEntry.getKey()+")");
                fw.write(RT_1);
            }
        }
        fw.write(".append(\"ir\",Constant.ZERO);");
        fw.write(RT_1);
        fw.write("setBaseEntry(basicDBObject);");
        fw.write(RT_1);
        fw.write("}");
        fw.write(RT_2+RT_2);
//        public void setSubject(String subject){
//            setSimpleValue("su",subject);
//        }
//
//        public String getSubject(){
//            return getSimpleStringValue("su");
//        }



        fw.flush();
        fw.close();
        showInfo(fileName);
    }

    public void showInfo(String fileName){
        System.out.println("创建文件："+ fileName+ "成功！");
    }

    /**
      * 获取路径的最后面字符串<br>
   * 如：<br>
      *     <code>str = "com.b510.base.bean.User"</code><br>
         *     <code> return "User";<code>
        * @param str
       * @return
         */
     public String getLastChar(String str) {
         if ((str != null) && (str.length() > 0)) {
             int dot = str.lastIndexOf('.');
             if ((dot > -1) && (dot < (str.length() - 1))) {
                 return str.substring(dot + 1);
             }
         }
         return str;
     }

}
