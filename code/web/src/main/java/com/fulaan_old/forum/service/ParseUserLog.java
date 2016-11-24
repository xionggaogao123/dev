package com.fulaan_old.forum.service;

import com.db.user.UserDao;
import com.pojo.user.UserEntry;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.bson.types.ObjectId;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by admin on 2016/6/24.
 */
public class ParseUserLog {

    public static UserDao dao = new UserDao();

    public static class DTO {
        private String timeText;
        private String nickName;
        private String schoolName;
        private String userId;
        private String userName;
        private String ip;

        public String getTimeText() {
            return timeText;
        }

        public void setTimeText(String timeText) {
            this.timeText = timeText;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getSchoolName() {
            return schoolName;
        }

        public void setSchoolName(String schoolName) {
            this.schoolName = schoolName;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }
    }

    private static String SCHOOLNAME = "schoolName";
    private static String USERID = "userId";
    private static String IP = "ipAddr";

    public static void readFileByLines(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        try {
            System.out.println("以行为单位读取文件内容，一次读一整行：");
            InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "UTF-8");
            reader = new BufferedReader(isr);
            String tempString = null;
            Map<String, Object> map = new HashMap<String, Object>();
            List<DTO> dtoList = new ArrayList<DTO>();
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                DTO dto = new DTO();
                String str = tempString.substring(0, tempString.indexOf(","));
                dto.setTimeText(str);
                if (tempString.contains(SCHOOLNAME)) {
                    int userindex = tempString.indexOf(SCHOOLNAME);
                    String schoolName = tempString.substring(userindex + 11, tempString.indexOf(",", userindex));
                    if (schoolName != null && schoolName != "") {
                        dto.setSchoolName(schoolName);
                    }
                }

                if (tempString.contains(IP)) {
                    int userindex = tempString.indexOf(IP);
                    String ip = tempString.substring(userindex + 7, tempString.indexOf(",", userindex));
                    if (ip != null && ip != "") {
                        dto.setIp(ip);
                    }
                }

                if (tempString.contains(USERID)) {
                    int userindex = tempString.indexOf(USERID);
                    String userId = tempString.substring(userindex + 7, tempString.indexOf(",", userindex));

                    if (userId != null && userId != "") {
                        UserEntry u = dao.getUserEntry(new ObjectId(userId), null);
                        if (u != null) {
                            dto.setUserId(userId);
                            dto.setNickName(u.getNickName());
                            dto.setUserName(u.getUserName());
                        }

                    }
                }
                dtoList.add(dto);
            }
            reader.close();
            for (DTO dto : dtoList) {
//                    String schoolName=dto.getSchoolName();
//                    System.out.println(schoolName);
//                    System.out.println(dto.getTimeText());
//                    System.out.println(dto.getNickName());
//                    System.out.println(dto.getUserName());
//                    System.out.println(dto.getUserId());
            }
            createExcel(dtoList);
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
    }

    public static void createExcel(List<DTO> dtoList) {
        HSSFWorkbook wb = new HSSFWorkbook();
        //生成一个sheet1
        HSSFSheet sheet = wb.createSheet("成绩列表");
        //为sheet1生成第一行，用于放表头信息
        HSSFRow row = sheet.createRow(0);

        HSSFCell cell = row.createCell(0);
        cell.setCellValue("用户名");
        cell = row.createCell(1);
        cell.setCellValue("昵称");
        cell = row.createCell(2);
        cell.setCellValue("时间");
        cell = row.createCell(3);
        cell.setCellValue("学校");
        cell = row.createCell(4);
        cell.setCellValue("登录Ip");

        int page = 0;
        for (int i = 0; i < dtoList.size(); i++) {
            DTO dto = dtoList.get(i);
            page++;
            row = sheet.createRow(page);

            cell = row.createCell(0);
            cell.setCellValue(dto.getUserName());
            cell = row.createCell(1);
            cell.setCellValue(dto.getNickName());
            cell = row.createCell(2);
            cell.setCellValue(dto.getTimeText());
            cell = row.createCell(3);
            cell.setCellValue(dto.getSchoolName());
            cell = row.createCell(4);
            cell.setCellValue(dto.getIp());
        }

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            wb.write(os);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] content = os.toByteArray();
        File file = new File("E:\\log\\2016-09-09.xls");
        OutputStream out = null;
        try {
            out = new FileOutputStream(file);
            try {
                out.write(content);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (FileNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    public static String getNoHTMLString(String content, int p) {

        if (null == content) return "";
        if (0 == p) return "";

        java.util.regex.Pattern p_script;
        java.util.regex.Matcher m_script;
        java.util.regex.Pattern p_style;
        java.util.regex.Matcher m_style;
        java.util.regex.Pattern p_html;
        java.util.regex.Matcher m_html;

        try {
            String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>";
            //定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script> }
            String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>";
            //定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style> }
            String regEx_html = "<[^>]+>"; //定义HTML标签的正则表达式

            p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
            m_script = p_script.matcher(content);
            content = m_script.replaceAll(""); //过滤script标签
            p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
            m_style = p_style.matcher(content);
            content = m_style.replaceAll(""); //过滤style标签

            p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
            m_html = p_html.matcher(content);

            content = m_html.replaceAll(""); //过滤html标签
        } catch (Exception e) {
            return "";
        }

        if (content.length() > p) {
            content = content.substring(0, p) + "...";
        } else {
            content = content + "...";
        }


        return content;
    }

    public static void main(String[] args) {
        ParseUserLog.readFileByLines("C:\\Users\\moslpc\\Desktop\\data\\1\\loginLog.log.2016-09-09");
    }
}
