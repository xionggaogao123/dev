package com.fulaan.forum.service;

import com.db.user.UserDao;
import com.pojo.user.UserEntry;
import com.sys.constants.Constant;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.bson.types.ObjectId;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by admin on 2016/8/9.
 */
public class ParseUserData {
    private static UserDao userDao = new UserDao();
    public static List<DTO> dtoList = new ArrayList<DTO>();

    public static void readFileByLines(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        try {
            System.out.println("以行为单位读取文件内容，一次读一整行：");
            InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "UTF-8");
            reader = new BufferedReader(isr);
            String tempString;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                UserEntry userEntry = userDao.getUserEntry(new ObjectId(StringUtils.trim(tempString).replace("\"", "")), Constant.FIELDS);
                System.out.println(userEntry.getUserName());
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
    }

    private static String getResult(String urlStr, String content, String encoding) {
        URL url = null;
        HttpURLConnection connection = null;
        try {
            url = new URL(urlStr);
            connection = (HttpURLConnection) url.openConnection();// 新建连接实例
            connection.setConnectTimeout(5000);// 设置连接超时时间，单位毫秒
            connection.setReadTimeout(5000);// 设置读取数据超时时间，单位毫秒
            connection.setDoOutput(true);// 是否打开输出流 true|false
            connection.setDoInput(true);// 是否打开输入流true|false
            connection.setRequestMethod("POST");// 提交方法POST|GET
            connection.setUseCaches(false);// 是否缓存true|false
            connection.connect();// 打开连接端口
            DataOutputStream out = new DataOutputStream(connection
                    .getOutputStream());// 打开输出流往对端服务器写数据
            out.writeBytes(content);// 写数据,也就是提交你的表单 name=xxx&pwd=xxx
            out.flush();// 刷新
            out.close();// 关闭输出流
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    connection.getInputStream(), encoding));// 往对端写完数据对端服务器返回数据
            // ,以BufferedReader流来读取
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            reader.close();
            return buffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();// 关闭连接
            }
        }
        return null;
    }

    public static String decodeUnicode(String theString) {
        char aChar;
        int len = theString.length();
        StringBuffer outBuffer = new StringBuffer(len);
        for (int x = 0; x < len; ) {
            aChar = theString.charAt(x++);
            if (aChar == '\\') {
                aChar = theString.charAt(x++);
                if (aChar == 'u') {
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        aChar = theString.charAt(x++);
                        switch (aChar) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                value = (value << 4) + aChar - '0';
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                value = (value << 4) + 10 + aChar - 'a';
                                break;
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                value = (value << 4) + 10 + aChar - 'A';
                                break;
                            default:
                                throw new IllegalArgumentException(
                                        "Malformed      encoding.");
                        }
                    }
                    outBuffer.append((char) value);
                } else {
                    if (aChar == 't') {
                        aChar = '\t';
                    } else if (aChar == 'r') {
                        aChar = '\r';
                    } else if (aChar == 'n') {
                        aChar = '\n';
                    } else if (aChar == 'f') {
                        aChar = '\f';
                    }
                    outBuffer.append(aChar);
                }
            } else {
                outBuffer.append(aChar);
            }
        }
        return outBuffer.toString();
    }

    public static String getAddresses(String content, String encodingString)
            throws UnsupportedEncodingException {
        // 这里调用pconline的接口
        String urlStr = "http://ip.taobao.com/service/getIpInfo.php";
        // 从http://whois.pconline.com.cn取得IP所在的省市区信息
        String returnStr = getResult(urlStr, content, encodingString);
        if (returnStr != null) {
            // 处理返回的省市区信息
            System.out.println(returnStr);
            String[] temp = returnStr.split(",");
            if (temp.length < 3) {
                return "0";//无效IP，局域网测试
            }
            String region = (temp[5].split(":"))[1].replaceAll("\"", "");
            region = decodeUnicode(region);// 省份
            return region;
        }
        return null;
    }

    public static class DTO {
        private String userName;
        private String time;
        private String ip;
        private String address;
        private String phone;
        private String email;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }
    }

    public static void createExcel(List<DTO> dtoList) {
        HSSFWorkbook wb = new HSSFWorkbook();
        //生成一个sheet1
        HSSFSheet sheet = wb.createSheet("注册列表");
        //为sheet1生成第一行，用于放表头信息
        HSSFRow row = sheet.createRow(0);

        HSSFCell cell = row.createCell(0);
        cell.setCellValue("时间");
        cell = row.createCell(1);
        cell.setCellValue("昵称");
        cell = row.createCell(2);
        cell.setCellValue("手机号码");
        cell = row.createCell(3);
        cell.setCellValue("邮箱");
        cell = row.createCell(4);
        cell.setCellValue("注册ip");


        int page = 0;
        for (int i = 0; i < dtoList.size(); i++) {
            DTO dto = dtoList.get(i);
            page++;
            row = sheet.createRow(page);

            cell = row.createCell(0);
            cell.setCellValue(dto.getTime());
            cell = row.createCell(1);
            cell.setCellValue(dto.getUserName());
            cell = row.createCell(2);
            cell.setCellValue(dto.getPhone());
            cell = row.createCell(3);
            cell.setCellValue(dto.getEmail());
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
        File file = new File("D:\\log\\zhuce.xls");
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

    public static void main(String[] args) {

//        parseUserData.readFileByLines("D:\\txt\\178.txt");
//        System.out.println(System.currentTimeMillis());
//        System.out.println(System.currentTimeMillis()-30*24*60*60*1000L);
        List<DTO> dtos = new ArrayList<DTO>();
        List<ObjectId> list = new ArrayList<ObjectId>();
        fg("D:\\log\\qqww.txt");
//        List<UserEntry> userEntries=userDao.testGet();
//        List<UserEntry> userEntries=userDao.testTotal(list);
//        for(UserEntry userEntry:userEntries){
//            DTO dto=new DTO();
////            if(userEntry.getK6KT()!=1){
//                String address="";
//                try {
//                    if(StringUtils.isNotBlank(userEntry.getRegisterIP())){
////                    address = getAddresses("ip=" + userEntry.getRegisterIP(), "utf-8");
//                    }
//
//                } catch (Exception e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//                dto.setUserName(userEntry.getUserName());
//                dto.setIp(userEntry.getRegisterIP());
//                dto.setTime(DateTimeUtils.convert(userEntry.getRegisterTime(),DateTimeUtils.DATE_YYYY_MM_DD_HH_MM));
//                dto.setAddress(address);
//
//                dtos.add(dto);
//        }
//
//
//        createExcel(dtos);

    }

    public static void fg(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        try {
            System.out.println("以行为单位读取文件内容，一次读一整行：");
            InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "gbk");
            reader = new BufferedReader(isr);
            String tempString = null;
            Map<String, Object> map = new HashMap<String, Object>();

            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                DTO dto = new DTO();
                String[] str = tempString.split(",");
                DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//                Date date = new ObjectId(str[0]).getDate();
//                dto.setTime(df.format(date));
                dto.setUserName(str[1]);
                dto.setPhone(str[2]);
                dto.setEmail(str[3]);
                dto.setIp(str[5]);
                dtoList.add(dto);
            }
            reader.close();
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
}
