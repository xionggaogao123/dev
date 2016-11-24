package com.fulaan.customizedpage.utils;

/**
 * Created by admin on 2016/8/24.
 */
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;

import com.db.customized.RecordVideoDao;
import com.pojo.customized.RecordVideoEntry;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HTTP工具类.
 *
 * @author David.Huang
 */
public class HttpUtil {

    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(HttpUtil.class);

    /** 默认编码方式 -UTF8 */
    private static final String DEFAULT_ENCODE = "utf-8";

    // 信任所有站点
//    static {
//        SSLUtil.trustAllHostnames();
//        SSLUtil.trustAllHttpsCertificates();
//    }

    /**
     * 构造方法
     */
    public HttpUtil() {
        // empty constructor for some tools that need an instance object of the
        // class
    }

    /**
     * GET请求, 结果以字符串形式返回.
     *
     * @param url
     *            请求地址
     * @return 内容字符串
     */
    public static String getUrlAsString(String url) throws Exception {
        return getUrlAsString(url, null, DEFAULT_ENCODE);
    }

    /**
     * GET请求, 结果以字符串形式返回.
     *
     * @param url
     *            请求地址
     * @param params
     *            请求参数
     * @return 内容字符串
     */
    public static String getUrlAsString(String url, Map<String, String> params)
            throws Exception {
        return getUrlAsString(url, params, DEFAULT_ENCODE);
    }

    /**
     * GET请求, 结果以字符串形式返回.
     *
     * @param url
     *            请求地址
     * @param params
     *            请求参数
     * @param encode
     *            编码方式
     * @return 内容字符串
     */
    public static String getUrlAsString(String url, Map<String, String> params,
                                        String encode) throws Exception {
        // 开始时间
        long t1 = System.currentTimeMillis();
        // 获得HttpGet对象
        HttpGet httpGet = getHttpGet(url, params, encode);
        // 调试信息
        log.debug("url:" + url);
        log.debug("params:" + params.toString());
        log.debug("encode:" + encode);
        // 发送请求
        String result = executeHttpRequest(httpGet, null);
        // 结束时间
        long t2 = System.currentTimeMillis();
        // 调试信息
        log.debug("result:" + result);
        log.debug("consume time:" + ((t2 - t1)));
        // 返回结果
        return result;
    }

    /**
     * POST请求, 结果以字符串形式返回.
     *
     * @param url
     *            请求地址
     * @return 内容字符串
     */
    public static String postUrlAsString(String url) throws Exception {
        return postUrlAsString(url, null, null, null);
    }

    /**
     * POST请求, 结果以字符串形式返回.
     *
     * @param url
     *            请求地址
     * @param params
     *            请求参数
     * @return 内容字符串
     */
    public static String postUrlAsString(String url, Map<String, String> params)
            throws Exception {
        return postUrlAsString(url, params, null, null);
    }

    /**
     * POST请求, 结果以字符串形式返回.
     *
     * @param url
     *            请求地址
     * @param params
     *            请求参数
     * @param reqHeader
     *            请求头内容
     * @return 内容字符串
     * @throws Exception
     */
    public static String postUrlAsString(String url,
                                         Map<String, String> params, Map<String, String> reqHeader)
            throws Exception {
        return postUrlAsString(url, params, reqHeader, null);
    }

    /**
     * POST请求, 结果以字符串形式返回.
     *
     * @param url
     *            请求地址
     * @param params
     *            请求参数
     * @param reqHeader
     *            请求头内容
     * @param encode
     *            编码方式
     * @return 内容字符串
     * @throws Exception
     */
    public static String postUrlAsString(String url,
                                         Map<String, String> params, Map<String, String> reqHeader,
                                         String encode) throws Exception {
        // 开始时间
        long t1 = System.currentTimeMillis();
        // 获得HttpPost对象
        HttpPost httpPost = getHttpPost(url, params, encode);
        // 发送请求
        String result = executeHttpRequest(httpPost, reqHeader);
        // 结束时间
        long t2 = System.currentTimeMillis();
        // 调试信息
        log.debug("url:" + url);
        log.debug("params:" + params.toString());
        log.debug("reqHeader:" + reqHeader);
        log.debug("encode:" + encode);
        log.debug("result:" + result);
        log.debug("consume time:" + ((t2 - t1)));
        // 返回结果
        return result;
    }

    /**
     * 获得HttpGet对象
     *
     * @param url
     *            请求地址
     * @param params
     *            请求参数
     * @param encode
     *            编码方式
     * @return HttpGet对象
     */
    private static HttpGet getHttpGet(String url, Map<String, String> params,
                                      String encode) {
        StringBuffer buf = new StringBuffer(url);
        if (params != null) {
            // 地址增加?或者&
            String flag = (url.indexOf('?') == -1) ? "?" : "&";
            // 添加参数
            for (String name : params.keySet()) {
                buf.append(flag);
                buf.append(name);
                buf.append("=");
                try {
                    String param = params.get(name);
                    if (param == null) {
                        param = "";
                    }
                    buf.append(URLEncoder.encode(param, encode));
                } catch (UnsupportedEncodingException e) {
                    log.error("URLEncoder Error,encode=" + encode + ",param="
                            + params.get(name), e);
                }
                flag = "&";
            }
        }
        HttpGet httpGet = new HttpGet(buf.toString());
        return httpGet;
    }

    /**
     * 获得HttpPost对象
     *
     * @param url
     *            请求地址
     * @param params
     *            请求参数
     * @param encode
     *            编码方式
     * @return HttpPost对象
     */
    private static HttpPost getHttpPost(String url, Map<String, String> params,
                                        String encode) {
        HttpPost httpPost = new HttpPost(url);
        if (params != null) {
            List<NameValuePair> form = new ArrayList<NameValuePair>();
            for (String name : params.keySet()) {
                form.add(new BasicNameValuePair(name, params.get(name)));
            }
            try {
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(form,
                        encode);
                httpPost.setEntity(entity);
            } catch (UnsupportedEncodingException e) {
                log.error("UrlEncodedFormEntity Error,encode=" + encode
                        + ",form=" + form, e);
            }
        }
        return httpPost;
    }

    /**
     * 执行HTTP请求
     *
     * @param request
     *            请求对象
     * @param reqHeader
     *            请求头信息
     * @return 内容字符串
     */
    private static String executeHttpRequest(HttpUriRequest request,
                                             Map<String, String> reqHeader) throws Exception {
        HttpClient client = null;
        String result = null;
        try {
            // 创建HttpClient对象
            client = new DefaultHttpClient();
            // 设置连接超时时间
            client.getParams().setParameter(
                    CoreConnectionPNames.CONNECTION_TIMEOUT, 60);
            // 设置Socket超时时间
            client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
                    36600);
            // 设置请求头信息
            if (reqHeader != null) {
                for (String name : reqHeader.keySet()) {
                    request.addHeader(name, reqHeader.get(name));
                }
            }
            // 获得返回结果
            HttpResponse response = client.execute(request);
            // 如果成功
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                result = EntityUtils.toString(response.getEntity());
            }
            // 如果失败
            else {
                StringBuffer errorMsg = new StringBuffer();
                errorMsg.append("httpStatus:");
                errorMsg.append(response.getStatusLine().getStatusCode());
                errorMsg.append(response.getStatusLine().getReasonPhrase());
                errorMsg.append(", Header: ");
                Header[] headers = response.getAllHeaders();
                for (Header header : headers) {
                    errorMsg.append(header.getName());
                    errorMsg.append(":");
                    errorMsg.append(header.getValue());
                }
                log.error("HttpResonse Error:" + errorMsg);
            }
        } catch (Exception e) {
            log.error("http连接异常", e);
            throw new Exception("http连接异常");
        } finally {
            try {
                client.getConnectionManager().shutdown();
            } catch (Exception e) {
                log.error("finally HttpClient shutdown error", e);
            }
        }
        return result;
    }

    /**
     * 下载文件保存到本地
     *
     * @param path
     *            文件保存位置
     * @param url
     *            文件地址
     * @throws IOException
     */
    public static String downloadFile(String path, String url) throws IOException {
        log.debug("下载到本地文件路径:" + path);
        log.debug("下载文件的http路径:" + url);
        HttpClient client = null;
        try {
            // 创建HttpClient对象
            client = new DefaultHttpClient();
            // 获得HttpGet对象
            HttpGet httpGet = getHttpGet(url, null, null);
            // 发送请求获得返回结果
            HttpResponse response = client.execute(httpGet);
            // 如果成功
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                byte[] result = EntityUtils.toByteArray(response.getEntity());
                BufferedOutputStream bw = null;
                try {
                    // 创建文件对象
                    File f = new File(path);
                    // 创建文件路径
                    if (!f.getParentFile().exists())
                        f.getParentFile().mkdirs();
                    // 写入文件
                    bw = new BufferedOutputStream(new FileOutputStream(path));
                    bw.write(result);
                } catch (Exception e) {
                    log.error("保存文件错误,path=" + path + ",url=" + url, e);
                    e.printStackTrace();
                } finally {
                    try {
                        if (bw != null)
                            bw.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.error(
                                "finally BufferedOutputStream shutdown close",
                                e);
                    }
                }
            }
            // 如果失败
            else {
                StringBuffer errorMsg = new StringBuffer();
                errorMsg.append("httpStatus:");
                errorMsg.append(response.getStatusLine().getStatusCode());
                errorMsg.append(response.getStatusLine().getReasonPhrase());
                errorMsg.append(", Header: ");
                Header[] headers = response.getAllHeaders();
                for (Header header : headers) {
                    errorMsg.append(header.getName());
                    errorMsg.append(":");
                    errorMsg.append(header.getValue());
                }
                log.error("HttpResonse Error:" + errorMsg);
            }
        } catch (ClientProtocolException e) {
            log.error("下载文件保存到本地,http连接异常,path=" + path + ",url=" + url, e);
            e.printStackTrace();
            throw e;
        } catch (IOException e) {
            log.error("下载文件保存到本地,文件操作异常,path=" + path + ",url=" + url, e);
            e.printStackTrace();
            throw e;
        } finally {
            try {
                client.getConnectionManager().shutdown();
            } catch (Exception e) {
                log.error("finally HttpClient shutdown error", e);
                e.printStackTrace();
            }
            try{
//                return sendUrl(path);
                return path;
            }catch(Exception e){
                log.error("上传视频到七牛出错！", e);
                e.printStackTrace();
            }
        }
        return "";
    }

    public static String send(String filePath) throws Exception {
        String result = null;
        File file = new File(filePath);
        if (!file.exists() || !file.isFile()) {
            throw new IOException("文件不存在");
        }

        /**
         * 第一部分
         */
        URL urlObj = new URL("http://127.0.0.1/customized/uploadVideo.do");

        HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
        con.setRequestMethod("POST"); // 以Post方式提交表单，默认get方式
        con.setDoInput(true);
        con.setDoOutput(true);
        con.setUseCaches(false); // post方式不能使用缓存
        // 设置请求头信息
        con.setRequestProperty("Connection", "Keep-Alive");
        con.setRequestProperty("Charset", "UTF-8");
        // 设置边界
        String BOUNDARY = "----------" + System.currentTimeMillis();
        con.setRequestProperty("Content-Type", "multipart/form-data; boundary="+ BOUNDARY);
        // 请求正文信息
        // 第一部分：
        StringBuilder sb = new StringBuilder();
        sb.append("--"); // 必须多两道线
        sb.append(BOUNDARY);
        sb.append("\r\n");
        sb.append("Content-Disposition: form-data;name=\"media\";filename=\""+ file.getName() + "\"\r\n");
        sb.append("Content-Type:application/octet-stream\r\n\r\n");
        byte[] head = sb.toString().getBytes("utf-8");
        // 获得输出流
        OutputStream out = new DataOutputStream(con.getOutputStream());
        // 输出表头
        out.write(head);
        // 文件正文部分
        // 把文件已流文件的方式 推入到url中
        DataInputStream in = new DataInputStream(new FileInputStream(file));
        int bytes = 0;
        byte[] bufferOut = new byte[1024];
        while ((bytes = in.read(bufferOut)) != -1) {
            out.write(bufferOut, 0, bytes);
        }
        in.close();
        // 结尾部分
        byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");// 定义最后数据分隔线
        out.write(foot);
        out.flush();
        out.close();
        StringBuffer buffer = new StringBuffer();
        BufferedReader reader = null;
        try {
            // 定义BufferedReader输入流来读取URL的响应
            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                //System.out.println(line);
                buffer.append(line);
            }
            if(result==null){
                result = buffer.toString();
            }
        } catch (IOException e) {
            System.out.println("发送POST请求出现异常！" + e);
            e.printStackTrace();
            throw new IOException("数据读取异常");
        } finally {
            if(reader!=null){
                reader.close();
            }
//            file.delete();
        }

        System.out.println(result);
        return result;
    }

    public static String sendUrl(String filePath) throws Exception {

        log.info("下载到本地的文件路径："+filePath);
        File file = new File(filePath);
        if (!file.exists() || !file.isFile()) {
            throw new IOException("文件不存在");
        }

        String urlStr="http://www.k6kt.com/customized/uploadVideo.do";
        String res = "";
        HttpURLConnection conn = null;
        String BOUNDARY = "----------" + System.currentTimeMillis(); //boundary就是request头和上传文件内容的分隔符
        try {
            URL url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(30000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);

            OutputStream out = new DataOutputStream(conn.getOutputStream());

            Map<String, String> fileMap = new HashMap<String, String>();
            fileMap.put("userfile", filePath);
            // file
            if (fileMap != null) {
                Iterator<Map.Entry<String, String>> iter = fileMap.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry<String, String> entry = iter.next();
                    String inputName = (String) entry.getKey();
                    String inputValue = (String) entry.getValue();
                    if (inputValue == null) {
                        continue;
                    }
                    String filename = file.getName();
//                    MagicMatch match = Magic.getMagicMatch(file, false, true);match.getMimeType();
                    String contentType = "application/octet-stream";

                    StringBuffer strBuf = new StringBuffer();
                    strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
                    strBuf.append("Content-Disposition: form-data; name=\"" + inputName + "\"; filename=\"" + filename + "\"\r\n");
                    strBuf.append("Content-Type:" + contentType + "\r\n\r\n");

                    out.write(strBuf.toString().getBytes());

                    DataInputStream in = new DataInputStream(new FileInputStream(file));
                    int bytes = 0;
                    byte[] bufferOut = new byte[1024];
                    while ((bytes = in.read(bufferOut)) != -1) {
                        out.write(bufferOut, 0, bytes);
                    }
                    in.close();
                }
            }

            byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
            out.write(endData);
            out.flush();
            out.close();

            // 读取返回数据
            StringBuffer strBuf = new StringBuffer();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                strBuf.append(line);

            }
            res = strBuf.toString();
//            System.out.println(res);
            reader.close();
            reader = null;
        } catch (Exception e) {
            System.out.println("发送POST请求出错。" + urlStr);
            e.printStackTrace();
            log.error("发送POST请求出错",e);
        } finally {
            if (conn != null) {
                conn.disconnect();
                conn = null;
            }
            log.info("删除文件！");
//            file.delete();
        }
        return res;
    }

    public static void main(String[] args) throws IOException {
        // String result = getUrlAsString("http://www.gewara.com/");
        // System.out.println(result);
        final String sendUrl="http://139.196.198.137/xizang/xizang-xizang-24-Aug-10:17:52.flv";


//        Runnable runnable=new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    downloadFile("E:/exam.flv",
//                            sendUrl);
////                    send("E:\\video.mp4");
//                } catch(Exception e){
//                    log.error("从服务器上下载视频出错!", e);
//                }
//            }
//        };
//        Thread t=new Thread(runnable);
//        t.start();
//        try{
////            send("E:\\video.mp4");
//            downloadFile("E:/exam.flv",
//                            sendUrl);
//        }catch(Exception e){
//            e.printStackTrace();
//        }
        RecordVideoDao recordVideoDao=new RecordVideoDao();
        List<RecordVideoEntry> l=recordVideoDao.testAll();
        for(RecordVideoEntry r:l){
           r.setRemove(0);
            recordVideoDao.add(r);
        }


    }
}
