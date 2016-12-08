package com.fulaan.util;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Random;

/**
 * Created by admin on 2016/12/8.
 */
public class imageInit {

    public static byte[] imageWatermarkProcess(String originalImagePath,String watermarkImagePath, float alpha, int x, int y) {
        try {
            // 原图
            Image original = Toolkit.getDefaultToolkit().getImage(originalImagePath);
            original = new ImageIcon(original).getImage();
            int width = original.getWidth(null);
            int height = original.getHeight(null);
            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics2d = bufferedImage.createGraphics();
            graphics2d.drawImage(original, 0, 0, width, height, null);
            // 水印图
            Image watermark = Toolkit.getDefaultToolkit().getImage(watermarkImagePath);
            watermark = new ImageIcon(watermark).getImage();
            int watermarkWidth = watermark.getWidth(null);
            int watermarkHeight = watermark.getHeight(null);
            graphics2d.setComposite(AlphaComposite.getInstance(10, alpha));
            graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
            int widthDiff = width - watermarkWidth;
            int heightDiff = height - watermarkHeight;
            // 若水印图尺寸大于原图，等比例缩小1/4
            if (widthDiff <= 0 || heightDiff <= 0) {
                watermarkWidth /= 4;
                watermarkHeight /= 4;
                widthDiff = width - watermarkWidth;
                heightDiff = height - watermarkHeight;
            }
            // 保证水印图全部出现在原图中
            if (x < 0)
                x = widthDiff / 2;
            else if (x > widthDiff) {
                x = widthDiff;
            }
            if (y < 0)
                y = heightDiff / 2;
            else if (y > heightDiff) {
                y = heightDiff;
            }
            graphics2d.drawImage(watermark, width-watermarkWidth-20, height-watermarkHeight-20, watermarkWidth,watermarkHeight, null);
            graphics2d.dispose();
            String fileType = originalImagePath.substring(originalImagePath.lastIndexOf(".") + 1);
            ByteArrayOutputStream baops = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, fileType, baops);
            return baops.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String mergeWaterMark(String imagePath,String logoPath) {
        if (null == imagePath || -1 != imagePath.indexOf("-merge-") || null == logoPath) {
            return "ERROR";
        }
        String watermarkPath = imagePath.substring(0,imagePath.lastIndexOf('/')+1);
        // 添加随机4位的数字目的是为了避免切换其他logo合成水印后，页面图片依然显示第一次logo水印合成图片（缓存的原因）
        String watermarkImagePath = imagePath.substring(0,imagePath.lastIndexOf("."))+"-merge-"+ new Random().nextInt(9999) +".jpg";
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        File imgDir = new File(watermarkPath);
        if(!imgDir.exists()){
            imgDir.mkdirs();
        }
        File fileImg = new File(watermarkImagePath);
        try {
            fos = new FileOutputStream(fileImg);
            bos = new BufferedOutputStream(fos);
            bos.write(imageWatermarkProcess(imagePath, logoPath, 1.0F,10,10));
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR";
        } finally {
            try {
                if(null != bos){
                    bos.close();
                }
                if(null != fos){
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return watermarkImagePath;
    }

    public static String clearWaterMark(String imagePath){
        String clearName = imagePath.substring(imagePath.lastIndexOf("-merge-"),imagePath.lastIndexOf('.'));
        String originalImagePath = imagePath.replace(clearName,"");
        // 清除水印后删除水印图片
        File fileImg = new File(imagePath);
        if (fileImg.isFile()) {
            fileImg.delete();
        }
        // 返回原图地址
        return originalImagePath;
    }

    public static void main(String[] args) {
        // 路径为绝对路径
        String logoImg = "D:/water/logo.png";
        String originalImg = "D:/water/test4.jpg";
        mergeWaterMark(originalImg,logoImg);
    }


}
