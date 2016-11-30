package com.fulaan.util;

import net.coobird.thumbnailator.Thumbnails;

import java.io.File;
import java.io.FileInputStream;

/**
 * Created by admin on 2016/11/30.
 */
public class ThumbnailImage {

    public static void main(String[] args) throws Exception{
       File file=new File("D:\\image\\example2.jpg");
       File file1=new File("D:\\image\\example3.jpg");
       file1.createNewFile();
       Thumbnails.of(new FileInputStream(file)).scale(1f).outputQuality(0.08f).toFile(file1);
    }
}
