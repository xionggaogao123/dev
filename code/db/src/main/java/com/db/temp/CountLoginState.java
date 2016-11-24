package com.db.temp;

import java.io.*;
import java.util.*;

/**
 * Created by guojing on 2015/7/27.
 */
public class CountLoginState {

    public static void main(String[] args) {

        try {
            Map<String, List<String>> citymap = readfile("D:\\temp\\Loginlog");
            Map<String, Set<String>> map = new HashMap<String, Set<String>>();
            Map<String, Integer> intMap = new HashMap<String, Integer>();
            for (List<String> strs : citymap.values()) {
                for (String str : strs) {
                    String[] subStrs = str.split(",");
                    String schoolName = subStrs[2].substring(12);
                    String userName = subStrs[4].substring(10);
                    Set<String> stuSet = map.get(schoolName);
                    if (stuSet != null) {
                        stuSet.add(userName);
                    } else {
                        stuSet = new HashSet<String>();
                        stuSet.add(userName);
                    }
                    map.put(schoolName, stuSet);
                    intMap.put(schoolName, stuSet.size());
                }
            }


        } catch (FileNotFoundException ex) {

        } catch (IOException ex) {

        }
        System.out.println("ok");
    }

    public static Map<String, List<String>> readfile(String filepath) throws FileNotFoundException, IOException {
        Map<String, List<String>> citymap = new HashMap<String, List<String>>();
        try {
            File file = new File(filepath);
            if (!file.isDirectory()) {
                System.out.println("文件");
                System.out.println("path=" + file.getPath());
                System.out.println("absolutepath=" + file.getAbsolutePath());
                System.out.println("name=" + file.getName());

            } else if (file.isDirectory()) {
                System.out.println("文件夹");
                String[] filelist = file.list();
                for (int i = 0; i < filelist.length; i++) {
                    File readfile = new File(filepath + "\\" + filelist[i]);
                    if (!readfile.isDirectory()) {
                        List<String> recodes = readLog(readfile.getPath());
                        for (String str : recodes) {
                            String subStr = str.substring(str.indexOf("[") + 1, str.indexOf("]"));
                            String[] subStrs = subStr.split(",");
                            String city = subStrs[0].substring(5);
                            List<String> list = citymap.get(city);
                            if (list != null) {
                                list.add(subStr);
                            } else {
                                list = new ArrayList<String>();
                                list.add(subStr);
                            }
                            citymap.put(city, list);
                        }

                        System.out.println("path=" + readfile.getPath());
                        System.out.println("absolutepath=" + readfile.getAbsolutePath());
                        System.out.println("name=" + readfile.getName());
                    } else if (readfile.isDirectory()) {
                        readfile(filepath + "\\" + filelist[i]);
                    }
                }

            }

        } catch (FileNotFoundException e) {
            System.out.println("readfile()   Exception:" + e.getMessage());
        }
        return citymap;
    }

    public static List<String> readLog(String filePath) {
        List<String> list = new ArrayList<String>();
        try {
            FileInputStream is = new FileInputStream(filePath);
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line;
            try {
                while ((line = br.readLine()) != null) {
                    if (line.equals(""))
                        continue;
                    else
                        list.add(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("读取一行数据时出错");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("文件读取路径错误FileNotFoundException");
        }
        return list;
    }
}
