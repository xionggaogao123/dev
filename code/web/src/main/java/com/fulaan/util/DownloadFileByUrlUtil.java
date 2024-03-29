package com.fulaan.util;

import java.io.*;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.FileChannel;

/**
 * Created by admin on 2017/10/24.
 */
public class DownloadFileByUrlUtil {

    /**
     * 从网络Url中下载文件
     * @param urlStr
     * @param fileName
     * @param savePath
     * @throws IOException
     */
    public static void  downLoadFromUrl(String urlStr,String fileName,String savePath) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        //设置超时间为3秒
        conn.setConnectTimeout(3*1000);
        //防止屏蔽程序抓取而返回403错误
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

        //得到输入流
        InputStream inputStream = conn.getInputStream();
        //获取自己数组
        byte[] getData = readInputStream(inputStream);

        //文件保存位置
        File saveDir = new File(savePath);
        if(!saveDir.exists()){
            saveDir.mkdir();
        }
        File file = new File(saveDir+File.separator+fileName);
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(getData);
        if(fos!=null){
            fos.close();
        }
        if(inputStream!=null){
            inputStream.close();
        }


        System.out.println("info:"+url+" download success");

    }



    /**
     * 从输入流中获取字节数组
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static  byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }

    public static void getSizeByFile(String fileUrl) {
        FileChannel fc= null;
        try {
            File f= new File(fileUrl);
            if (f.exists() && f.isFile()){
                FileInputStream fis= new FileInputStream(f);
                fc= fis.getChannel();
                System.out.println(fc.size());
                long size=fc.size();
                long mSize=1024L*1024L;
                long kSize=1024L;
                String str="";
                if(size>=kSize&&mSize>size){
                    double f1 = new BigDecimal((float)size/kSize).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
                    str=f1+"KB";
                }else if(mSize<size){
                    double f1 = new BigDecimal((float)size/mSize).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
                    str=f1+"MB";
                }else if(size<kSize){
                    str=size+"B";
                }
                System.out.println(str);
            }else{
                System.out.println("file doesn't exist or is not a file");
            }
        } catch (FileNotFoundException e) {
            System.out.println(e);
        } catch (IOException e) {
            System.out.println(e);
        } finally {
            if (null!=fc){
                try{
                    fc.close();
                }catch(IOException e){
                    System.out.println(e.getMessage());
                }
            }
        }
    }


    public static void main(String[] args) {
        try{
//            "http://101.95.48.97:8005/res/upload/interface/apptutorials/manualstypeico/6f83ce8f-0da5-49b3-bac8-fd5fc67d2725.png",
//                    "百度.jpg","d:/resource/images/diaodiao/country/"
//            downLoadFromUrl("",
//                    "","D:\\apkSize");
            getSizeByFile("D:\\apkSize\\com.miui.calculator.apk");
        }catch (Exception e) {
            // TODO: handle exception
        }
    }
}
