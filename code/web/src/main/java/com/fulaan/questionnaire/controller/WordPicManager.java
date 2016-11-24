package com.fulaan.questionnaire.controller;

import org.apache.poi.hwpf.converter.PicturesManager;
import org.apache.poi.hwpf.usermodel.PictureType;

import java.io.*;

/**
 * Created by qinbo on 15/5/31.
 */
public class WordPicManager implements PicturesManager {
    private String url;

    private String path;

    private float quality = .7f;


    public WordPicManager(String url, String path) {
        this.url = url;
        this.path = path;
    }

    public void setQuality(float quality) {
        this.quality = quality;
    }

    /**
     * 保存word文档中的图片到img目录下
     *
     * @param bytes
     * @param pictureType
     * @param name
     * @param v
     * @param v1
     * @return
     */
    @Override
    public String savePicture(byte[] bytes, PictureType pictureType, String name, float v, float v1) {
        File dir = new File(path + "/img");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        if (bytes != null) {
            File img = new File(dir, name);
            try {
                FileOutputStream outputStream = new FileOutputStream(img);
                outputStream.write(bytes, 0, bytes.length);
                outputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return url + "/img/" + name;
    }

}
