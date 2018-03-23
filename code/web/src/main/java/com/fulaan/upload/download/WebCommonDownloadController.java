package com.fulaan.upload.download;

import com.fulaan.annotation.SessionNeedless;
import com.fulaan.base.BaseController;
import com.sys.exceptions.IllegalParamException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by qinbo on 15/6/9.
 * connondownload
 */
@Api(value="下载远程文件并保存到本地",hidden = true)
@Controller
@RequestMapping("/web/commondownload")
public class WebCommonDownloadController extends BaseController {


    /**
     * 下载远程文件并保存到本地
     *
     * @param remoteFilePath 远程文件路径
     */
    @ApiOperation(value = "下载远程文件并保存到本地", httpMethod = "POST", produces = "application/json")
    @RequestMapping("/downloadFile")
    @ResponseBody
    @SessionNeedless
    public void downloadFile(String remoteFilePath, String fileName, HttpServletResponse response) throws IOException, IllegalParamException {
        if (StringUtils.isBlank(fileName)) {
            fileName = remoteFilePath.substring(remoteFilePath.lastIndexOf("/") + 1);
        }
        URL url = new URL(remoteFilePath);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        //设置超时间为10秒
        conn.setConnectTimeout(10 * 1000);
        //防止屏蔽程序抓取而返回403错误
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

        response.setContentType("application/x-download");// 设置response内容的类型
        response.setHeader("Content-disposition", "attachment; filename=" + new String(fileName.getBytes("gb2312"), "iso8859-1"));// 设置头部信息

        InputStream inputStream = conn.getInputStream();
        OutputStream outs = response.getOutputStream();// 获取文件输出IO流

        IOUtils.copy(inputStream, outs);
        IOUtils.closeQuietly(inputStream);
        IOUtils.closeQuietly(outs);
    }

    @ApiOperation(value = "downloadFile", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = Map.class)})
    @RequestMapping("/file")
    @ResponseBody
    public Map<String, Object> downloadFile(HttpServletRequest req, HttpServletResponse response, String url,
                                            @RequestParam(required = false) String name) throws Exception {

        //存在本地upload
        if (url.startsWith("/upload")) {
            String uploadpath = req.getServletContext().getRealPath("/upload");
            String filepath = uploadpath + url.substring(7);//去掉upload
            File downFile = new File(filepath);
            if (downFile.exists()) {
                response.setCharacterEncoding("utf-8");
                response.setContentType("multipart/form-data");
                String filename = name;
                if (filename == null) {
                    filename = url.substring(url.lastIndexOf("/"));
                }
                response.setHeader("Content-Disposition", "attachment;filename=" + new String(
                        filename.getBytes("utf-8"), "ISO8859-1"));

                try {
                    InputStream inputStream = new FileInputStream(downFile);
                    OutputStream os = response.getOutputStream();
                    byte[] b = new byte[2048];
                    int length;
                    while ((length = inputStream.read(b)) > 0) {
                        os.write(b, 0, length);
                    }
                    os.close();
                    inputStream.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }

        Map<String, Object> retMap = new HashMap<String, Object>();

        return retMap;

    }
}