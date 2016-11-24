package com.fulaan.learningcenter.controller;

import com.fulaan.utils.QiniuFileUtils;
import com.pojo.itempool.ItemPoolEntry;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.POITextExtractor;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.extractor.ExtractorFactory;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.model.PicturesTable;
import org.apache.poi.hwpf.usermodel.*;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.*;
import org.bson.types.ObjectId;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
* Created by fl on 2016/2/16.
*/
public class DocxToHtml {
    private static String htmlText = "";

    private String htmlFile="D:\\test.html";
    private String path = "";
    public static void main(String[] args) throws Exception{
        InputStream is = new FileInputStream("D:\\test.docx");
        XWPFDocument doc = new XWPFDocument(is);
        new DocxToHtml().readDocx(doc);
//new DocxToHtml().extractImage();

        if (is != null) {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

         /**
         * 通过XWPFDocument对内容进行访问。对于XWPF文档而言，用这种方式进行读操作更佳。
         * @throws Exception
         */
        private void readDocx(XWPFDocument doc) throws Exception {
            List<IBodyElement> eles = doc.getBodyElements();
            int paraIndex = 0;
            int tableIndex = 0;
            List<XWPFTable> tables = doc.getTables();
            for(IBodyElement ele : eles){
                if(ele.getElementType().equals(BodyElementType.PARAGRAPH)){
                    XWPFParagraph paragraph = doc.getParagraphArray(paraIndex++);
                    System.out.println(paragraph.getText());
                    htmlText += "<p>" + paragraph.getText() + "</p></br>";
                } else if(ele.getElementType().equals(BodyElementType.TABLE)){
                    XWPFTable table = tables.get(tableIndex++);
                    extractTable(table);
                }
            }
            writeFile(htmlText);
        }



    //处理图片
    private void extractImage(XWPFDocument xwpfd){
        try{
            String imgPath = "D:/img";
            File imgFile = new File(imgPath);      
            if(!imgFile.exists()){       
                imgFile.mkdir();      }
                //获取所图片
            List piclist = xwpfd.getAllPictures();
            for(int j = 0; j < piclist.size(); j++){         
                XWPFPictureData pic = (XWPFPictureData) piclist.get(j);
                //获取图片数据流
                byte[] picbyte = pic.getData();         
                //将图片写入本地文件
                FileOutputStream fos = new FileOutputStream(imgPath +"/"+ new ObjectId().toString()+"-"+j +".jpg");
                fos.write(picbyte);         }        
        }
        catch (FileNotFoundException e){
          e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
            }
    }

    //处理表格
    private void extractTable(XWPFTable table){
        htmlText += "<table>";
        List<XWPFTableRow> rows = table.getRows();
        for (XWPFTableRow row : rows) {
            htmlText += "<tr>";
            //获取行对应的单元格
            List<XWPFTableCell> cells = row.getTableCells();
            for (XWPFTableCell cell : cells) {
                htmlText += "<td>";
                htmlText += cell.getText();
                htmlText += "</td>";
                System.out.print(cell.getText() + "\t");
            }
            System.out.println();
            htmlText += "</tr>";
        }
        htmlText += "</table>";
    }


    /**
     * 读写文档中的图片
     *
     * @param pTable
     * @param cr
     * @throws Exception
     */
    private void readPicture(PicturesTable pTable, CharacterRun cr) throws Exception {
        // 提取图片
        Picture pic = pTable.extractPicture(cr, false);
        // 返回POI建议的图片文件名
        String afileName = pic.suggestFullFileName();
        String fileKey = "itempool-"+new ObjectId().toString() + afileName.substring(afileName.lastIndexOf('.'));
//        String fileKey = "itempool-"+new ObjectId().toString() + ".jpg";

        InputStream inputStream =new ByteArrayInputStream(pic.getContent());
//        InputStream converterIS = converter(inputStream, "jpeg", fileKey);
        QiniuFileUtils.uploadFile(fileKey, inputStream, QiniuFileUtils.TYPE_IMAGE);
        String path  =QiniuFileUtils.getPath(QiniuFileUtils.TYPE_DOCUMENT, fileKey);

        htmlText += "<img src='"+ path + "'/>";
    }


    /**
     * 写文件
     *
     * @param s
     */
    private void writeFile(String s) {
        FileOutputStream fos = null;
        BufferedWriter bw = null;
        PrintWriter writer = null;
        try {
            File file = new File(htmlFile);
            fos = new FileOutputStream(file);
            bw = new BufferedWriter(new OutputStreamWriter(fos));
            bw.write(s);
            bw.close();
            fos.close();
            //编码转换
            writer = new PrintWriter(file, "GB2312");
            writer.write(s);
            writer.flush();
            writer.close();
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

    }

    /**
     * 分析html
     * @param s
     */
    private List<ItemPoolEntry> analysisHtmlString(String s){
        List<ItemPoolEntry> itemPoolEntries = new ArrayList<ItemPoolEntry>();
        ItemPoolEntry.ObjectiveItem objectiveItem = new ItemPoolEntry.ObjectiveItem("", 0, 0);

        String q[] = s.split("<br/>");

        LinkedList<String> list = new LinkedList<String>();

        //清除空字符
        for (int i = 0; i < q.length; i++) {
            if(StringUtils.isNotBlank(q[i].toString().replaceAll("</?p>", "").trim())){

                list.add(q[i].toString().trim());
            }
        }
        String[] result = {};
        String ws[]=list.toArray(result);

        /****************************************************************/
        int state = 0;
        String tigan = "";
        String answer = "";
        String jiexi = "";
        Boolean isChange;
        for (int i = 0; i < ws.length; i++) {
            String delHtml=ws[i].toString().replaceAll("</?p>","").trim();//去除html
            delHtml = delHtml.replaceAll("^\\s*[0-9]+[\\.、]", "题干");
            delHtml = filter(delHtml);
            isChange = false;
            if(delHtml.contains("题干")){
                isChange = true;
                state = 1;
                delHtml = delHtml.replaceAll("题干", "");
            }else if(delHtml.contains("答案")){
                state = 2;
                delHtml = delHtml.replaceAll("答案[:：]", "");
            }else if(delHtml.contains("解析")){
                state = 3;
                delHtml = delHtml.replaceAll("解析[:：]", "");
            }
            delHtml = delHtml.replaceAll("^[一二三四五六七八九十]+[、\\.]", "大标题");
            if(delHtml.contains("大标题")){
                continue;
            }

            if(state == 1){
                if(isChange && !tigan.equals("")){
                    //new Entry
                    ItemPoolEntry itemPoolEntry = new ItemPoolEntry(null, null, 0, null, null, 0, tigan, answer, jiexi, null, null, null, 0);
                    itemPoolEntry.setItem(objectiveItem);
                    itemPoolEntries.add(itemPoolEntry);
                    System.out.println("题干：" + tigan);
                    System.out.println("答案：" + answer);
                    System.out.println("解析：" + jiexi);
                    //清空string
                    tigan = delHtml +"<br/>";
                    answer = "";
                    jiexi = "";
                } else {
                    //保存题干
                    tigan += delHtml +"<br/>";
                }
            } else if(2 == state){
                //保存答案
                answer += delHtml +"<br/>";
            } else if(3 == state){
                //保存解析
                jiexi += delHtml +"<br/>";
            }

        }

        ItemPoolEntry itemPoolEntry = new ItemPoolEntry(null, null, 0, null, null, 0, tigan, answer, jiexi, null, null, null, 0);
        itemPoolEntry.setItem(objectiveItem);
        itemPoolEntries.add(itemPoolEntry);
        System.out.println("题干：" + tigan);
        System.out.println("答案：" + answer);
        System.out.println("解析：" + jiexi);
        return itemPoolEntries;

    }

    private String filter(String s){
        s = s.replaceAll("INCLUDEPICTURE.*MERGEFORMATINET", "");
        return s;
    }
}
