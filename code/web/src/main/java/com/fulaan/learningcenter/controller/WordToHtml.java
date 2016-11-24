package com.fulaan.learningcenter.controller;

/**
 * Created by fl on 2015/12/21.
 */


import java.awt.image.BufferedImage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fulaan.utils.QiniuFileUtils;
import com.pojo.itempool.ItemPoolEntry;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.model.PicturesTable;
import org.apache.poi.hwpf.usermodel.CharacterRun;
import org.apache.poi.hwpf.usermodel.Picture;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Table;
import org.apache.poi.hwpf.usermodel.TableCell;
import org.apache.poi.hwpf.usermodel.TableIterator;
import org.apache.poi.hwpf.usermodel.TableRow;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.bson.types.ObjectId;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;


public class WordToHtml {
    /**
     * 回车符ASCII码
     */
    private static final short ENTER_ASCII = 13;

    /**
     * 空格符ASCII码
     */
    private static final short SPACE_ASCII = 32;

    /**
     * 水平制表符ASCII码
     */
    private static final short TABULATION_ASCII = 9;

    private static String htmlText = "";
    private static String htmlTextTbl = "";
    private static int counter=0;
    private static int beginPosi=0;
    private static int endPosi=0;
    private static int beginArray[];
    private static int endArray[];
    private static String htmlTextArray[];
    private static boolean tblExist=false;

    private String htmlFile="";
    private String path = "";

    public List<ItemPoolEntry> upload(MultipartFile file, HttpServletRequest req)
    {
        path =  req.getServletContext().getRealPath("/upload")+"/itempool";
        htmlFile = path +"/html.html";
        try {
            int docType = 1;
            String name = file.getOriginalFilename();
            if(name.contains(".docx")){
                docType = 2;
            }
            return getWordAndStyle(file.getInputStream(), docType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 读取每个文字样式
     *
     * @param inputStream
     * @throws Exception
     */


    private List<ItemPoolEntry> getWordAndStyle(InputStream inputStream, int docType) throws Exception {
//        FileInputStream in = new FileInputStream(new File(fileName));
        HWPFDocument doc = new HWPFDocument(inputStream);
//        XWPFDocument doc = new XWPFDocument(inputStream);
//        if(2 == docType){
//            doc
//        }

        Range rangetbl = doc.getRange();//得到文档的读取范围
        TableIterator it = new TableIterator(rangetbl);
        int num=100;

        beginArray=new int[num];
        endArray=new int[num];
        htmlTextArray=new String[num];

        // 取得文档中字符的总数
        int length = doc.characterLength();
        // 创建图片容器
        PicturesTable pTable = doc.getPicturesTable();

//        htmlText = "<html><head><title>" + doc.getSummaryInformation().getTitle() + "</title></head><body>";
        htmlText = "";
        // 创建临时字符串,好加以判断一串字符是否存在相同格式
        tblExist=false;
        if(it.hasNext())
        {
            readTable(it,rangetbl);
        }

        int cur=0;

        String tempString = "";
        for (int i = 0; i < length - 1; i++) {
            // 整篇文章的字符通过一个个字符的来判断,range为得到文档的范围
            Range range = new Range(i, i + 1, doc);

            CharacterRun cr = range.getCharacterRun(0);

            if(tblExist)
            {
                if(i==beginArray[cur])
                {
                    htmlText+=tempString+htmlTextArray[cur];
                    tempString="";
                    i=endArray[cur]-1;
                    cur++;
                    continue;
                }
            }
            if (pTable.hasPicture(cr)) {
                htmlText +=  tempString ;
                // 读写图片
                readPicture(pTable, cr);
                tempString = "";
            }
            else {

                Range range2 = new Range(i + 1, i + 2, doc);
                // 第二个字符
                CharacterRun cr2 = range2.getCharacterRun(0);
                char c = cr.text().charAt(0);

                // 判断是否为空格符
                if (c == SPACE_ASCII)
                    tempString += "&nbsp;";
                    // 判断是否为水平制表符
                else if (c == TABULATION_ASCII)
                    tempString += "&nbsp;&nbsp;&nbsp;&nbsp;";
                // 比较前后2个字符是否具有相同的格式
                boolean flag = compareCharStyle(cr, cr2);
                if (flag&&c !=ENTER_ASCII)
                    tempString += cr.text();
                else {
                    String fontStyle = "<span style='font-family:" + cr.getFontName() + ";font-size:" + cr.getFontSize() / 2
                            + "pt;color:"+getHexColor(cr.getIco24())+";";

                    if (cr.isBold())
                        fontStyle += "font-weight:bold;";
                    if (cr.isItalic())
                        fontStyle += "font-style:italic;";

//                    htmlText += fontStyle + "' >" + tempString + cr.text();
//                    htmlText +="</span>";
                    htmlText += "<p>" + tempString + cr.text() + "</p>";
                    tempString = "";
                }
                // 判断是否为回车符
                if (c == ENTER_ASCII)
                    htmlText += "<br/>";

            }
        }

//        htmlText += tempString+"</body></html>";
        htmlText += tempString;
        //生成html文件
        writeFile(htmlText);
        System.out.println("------------WordToHtml转换成功----------------");
        //word试卷数据模型化
        List<ItemPoolEntry> itemPoolEntries = analysisHtmlString(htmlText);
        System.out.println("------------WordToHtml模型化成功----------------");
        return itemPoolEntries;
    }

    /**
     * 读写文档中的表格
     *
     * @param
     * @param
     * @throws Exception
     */
    private void readTable(TableIterator it, Range rangetbl) throws Exception {

        htmlTextTbl="";
        //迭代文档中的表格

        counter=-1;
        while (it.hasNext())
        {
            tblExist=true;
            htmlTextTbl="";
            Table tb = (Table) it.next();
            beginPosi=tb.getStartOffset() ;
            endPosi=tb.getEndOffset();

            //System.out.println("............"+beginPosi+"...."+endPosi);
            counter=counter+1;
            //迭代行，默认从0开始
            beginArray[counter]=beginPosi;
            endArray[counter]=endPosi;

            htmlTextTbl+="<table border>";
            for (int i = 0; i < tb.numRows(); i++) {
                TableRow tr = tb.getRow(i);

                htmlTextTbl+="<tr>";
                //迭代列，默认从0开始
                for (int j = 0; j < tr.numCells(); j++) {
                    TableCell td = tr.getCell(j);//取得单元格
                    int cellWidth=td.getWidth();

                    //取得单元格的内容
                    String s = "";
                    for(int k=0;k<td.numParagraphs();k++){
                        if(k>0){
                            s +="<br/>";
                        }
                        Paragraph para =td.getParagraph(k);
                        s += para.text().toString().trim();
                        if(s=="")
                        {
                            s=" ";
                        }
                    }
                    htmlTextTbl += "<td width="+cellWidth+ ">"+s+"</td>";
                }
            }
            htmlTextTbl+="</table>" ;
            htmlTextArray[counter]=htmlTextTbl;

        } //end while
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

    private InputStream converter(InputStream imgFile,String format, String fileKey)
            throws IOException{
        BufferedImage bIMG =ImageIO.read(imgFile);
        File destFile=new File(path +"/"+ fileKey);
        ImageIO.write(bIMG, format, destFile);
        InputStream inputStream =new FileInputStream(destFile);
        return inputStream;
    }


    private boolean compareCharStyle(CharacterRun cr1, CharacterRun cr2)
    {
        boolean flag = false;
        if (cr1.isBold() == cr2.isBold() && cr1.isItalic() == cr2.isItalic() && cr1.getFontName().equals(cr2.getFontName())
                && cr1.getFontSize() == cr2.getFontSize()&& cr1.getColor() == cr2.getColor())
        {
            flag = true;
        }
        return flag;
    }

    /*** 字体颜色模块start ********/
    private int red(int c) {
        return c & 0XFF;
    }

    private int green(int c) {
        return (c >> 8) & 0XFF;
    }

    private int blue(int c) {
        return (c >> 16) & 0XFF;
    }

    private int rgb(int c) {
        return (red(c) << 16) | (green(c) << 8) | blue(c);
    }

    private String rgbToSix(String rgb) {
        int length = 6 - rgb.length();
        String str = "";
        while (length > 0) {
            str += "0";
            length--;
        }
        return str + rgb;
    }


    private String getHexColor(int color) {
        color = color == -1 ? 0 : color;
        int rgb = rgb(color);
        return "#" + rgbToSix(Integer.toHexString(rgb));
    }
    /** 字体颜色模块end ******/

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
            if(StringUtils.isNotBlank(q[i].toString().replaceAll("</?p>","").trim())){

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

