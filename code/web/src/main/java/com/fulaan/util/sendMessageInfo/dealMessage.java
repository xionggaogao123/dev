package com.fulaan.util.sendMessageInfo;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by admin on 2017/1/13.
 */
public class dealMessage {

    public static void main(String[] args)throws Exception{
        List<String>  retList=  readFileByLines("D:\\info\\dataInit.txt");
        Set<String> set=new HashSet<String>();
        File file=new File("D:\\info\\sendMessage.txt");
        for(String item:retList){
            set.add(item);
        }
        List<String> data=new ArrayList<String>(set);
        FileOutputStream io=new FileOutputStream(file);
        IOUtils.writeLines(data,null,io,"UTF-8");
        IOUtils.closeQuietly(io);
    }

    public static List<String> readFileByLines(String fileName) {
        List<String> retList=new ArrayList<String>();
        File file = new File(fileName);
        BufferedReader reader = null;
        try {
            InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "UTF-8");
            reader = new BufferedReader(isr);
            String tempString;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                retList.add(tempString);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return retList;
    }
}
