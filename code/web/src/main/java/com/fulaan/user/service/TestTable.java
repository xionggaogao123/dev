package com.fulaan.user.service;

import com.fulaan.reportCard.dto.VirtualUserDTO;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//import com.sun.image.codec.jpeg.JPEGCodec;
//import com.sun.image.codec.jpeg.JPEGImageEncoder;
/**
 * Created by scott on 2018/1/5.
 */
public class TestTable {

    void createImage(String fileLocation, BufferedImage image) {
        try {
//            FileOutputStream fos = new FileOutputStream(fileLocation);
//            BufferedOutputStream bos = new BufferedOutputStream(fos);
//            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(bos);
//            encoder.encode(image);

            String formatName = fileLocation.substring(fileLocation.lastIndexOf(".") + 1);
            ImageIO.write(image, formatName , new File(fileLocation));
//            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void graphicsGeneration(String fileName, int totalrow, int totalcol, String title,
                                   List<VirtualUserDTO> userDTOs) throws Exception{
        //实际数据行数+标题+备注
        int imageWidth = 1024;
        int imageHeight = totalrow*40+20;
        int rowheight = 40;
        int startHeight = 10;
        int startWidth = 10;
        int colwidth = (int)((imageWidth-20)/totalcol);

         BufferedImage image = new BufferedImage(imageWidth, imageHeight,BufferedImage.TYPE_INT_RGB);
        Graphics graphics = image.getGraphics();

        graphics.setColor(Color.WHITE);
        graphics.fillRect(0,0, imageWidth, imageHeight);
        graphics.setColor(new Color(220,240,240));

        //画横线
        for(int j=0;j<totalrow-1;j++){
            graphics.setColor(Color.black);
            graphics.drawLine(startWidth, startHeight+(j+1)*rowheight, imageWidth-5, startHeight+(j+1)*rowheight);
        }
        //末行
        graphics.setColor(Color.black);
        graphics.drawLine(startWidth, imageHeight-90, imageWidth-5, imageHeight-90);


        //画竖线
        for(int k=0;k<totalcol;k++){
            graphics.setColor(Color.black);
            graphics.drawLine(startWidth+k*colwidth, startHeight+rowheight, startWidth+k*colwidth, imageHeight-50);
        }
        //末列
        graphics.setColor(Color.black);
        graphics.drawLine(imageWidth-5, startHeight+rowheight,imageWidth-5, imageHeight-50);

        //设置字体
        Font font = new Font("宋体",Font.BOLD,20);
        graphics.setFont(font);

        //写标题
        graphics.drawString(title, imageWidth/3+startWidth, startHeight+rowheight-10);

        font = new Font("宋体",Font.BOLD,18);
        graphics.setFont(font);

        //写入表头
        /*String[] headCells = {"姓名","学号"};
        for(int m=0;m<headCells.length;m++){
            graphics.drawString(headCells[m].toString(), startWidth+colwidth*m+5, startHeight+rowheight*2-10);
        }*/
        
        String[] headCells = {"姓名"};
        for(int m=0;m<headCells.length;m++){
            graphics.drawString(headCells[m].toString(), startWidth+colwidth*m+5, startHeight+rowheight*2-10);
        }

        //设置字体
        font = new Font("宋体",Font.PLAIN,16);
        graphics.setFont(font);
//        String[][] cellsValue = {{"101","xiaozhang"},
//                {"102","xiaowang"},
//                {"103","xiaoli"}};
        //写入内容
        int n=0;
        for(VirtualUserDTO stuEntry:userDTOs){
            graphics.drawString(stuEntry.getUserName(), startWidth+colwidth*0+5, startHeight+rowheight*(n+3)-10);
            //graphics.drawString(stuEntry.getUserNumber(), startWidth+colwidth*1+5, startHeight+rowheight*(n+3)-10);
            n++;
        }
//        for(int n=0;n<cellsValue.length;n++){
//            String[] arr = cellsValue[n];
//            for(int l=0;l<arr.length;l++){
//                graphics.drawString(cellsValue[n][l].toString(), startWidth+colwidth*l+5, startHeight+rowheight*(n+3)-10);
//            }
//        }

        font = new Font("宋体",Font.BOLD,18);
        graphics.setFont(font);
        graphics.setColor(Color.RED);

        //写备注
        String remark = "备注：未匹配成功的名单";
        graphics.drawString(remark, startWidth, imageHeight-30);

        createImage(fileName,image);
    }

    public static boolean createDir(String destDirName) {
        File dir = new File(destDirName);
        if (dir.exists()) {
            System.out.println("创建目录" + destDirName + "失败，目标目录已经存在");
            return false;
        }
        if (!destDirName.endsWith(File.separator)) {
            destDirName = destDirName + File.separator;
        }
        //创建目录
        if (dir.mkdirs()) {
            System.out.println("创建目录" + destDirName + "成功！");
            return true;
        } else {
            System.out.println("创建目录" + destDirName + "失败！");
            return false;
        }
    }

    public  boolean createFile(String destFileName) {
        File file = new File(destFileName);
        if(file.exists()) {
            System.out.println("创建单个文件" + destFileName + "失败，目标文件已存在！");
            return false;
        }
        if (destFileName.endsWith(File.separator)) {
            System.out.println("创建单个文件" + destFileName + "失败，目标文件不能为目录！");
            return false;
        }
        //判断目标文件所在的目录是否存在
        if(!file.getParentFile().exists()) {
            //如果目标文件所在的目录不存在，则创建父目录
            System.out.println("目标文件所在目录不存在，准备创建它！");
            if(!file.getParentFile().mkdirs()) {
                System.out.println("创建目标文件所在目录失败！");
                return false;
            }
        }
        //创建目标文件
        try {
            if (file.createNewFile()) {
                System.out.println("创建单个文件" + destFileName + "成功！");
                return true;
            } else {
                System.out.println("创建单个文件" + destFileName + "失败！");
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("创建单个文件" + destFileName + "失败！" + e.getMessage());
            return false;
        }
    }

    public static void main(String[] args) {
        TestTable cg = new TestTable();
        try {
            String dirName = "D:/work/scott/temp1";
            cg.createDir(dirName);
            //创建文件
            String fileName = dirName + "/temp2/1.jpg";
            cg.createFile(fileName);
            cg.graphicsGeneration(fileName,6,2,"标题",new ArrayList<VirtualUserDTO>());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
