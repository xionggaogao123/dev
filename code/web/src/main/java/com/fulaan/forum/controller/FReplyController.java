package com.fulaan.forum.controller;

import com.fulaan.annotation.ObjectIdType;
import com.fulaan.annotation.SessionNeedless;
import com.fulaan.base.BaseController;
import com.fulaan.pojo.LikeInfo;
import com.fulaan.util.DownloadUtil;
import com.fulaan.util.HttpUtils;
import com.fulaan.util.ZipCompressing;
import com.fulaan.forum.service.FReplyService;
import com.google.common.io.Files;
import com.pojo.forum.FReplyDTO;
import com.pojo.forum.FReplyEntry;
import com.sys.utils.RespObj;
import io.swagger.annotations.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

/**
 * Created by jerry on 2016/10/18.
 * 回复Controller
 */
@Api(value=" 回复Controller")
@Controller
@RequestMapping("/reply")
public class FReplyController extends BaseController {

    @Autowired
    private FReplyService fReplyService;

    /**
     * 设置楼层数，为老数据服务
     * @param replyId
     * @param floor
     * @return
     */
    @ApiOperation(value = "设置楼层数，为老数据服务", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/saveFReplyFloor")
    @ResponseBody
    public RespObj saveFReplyFloor(@ApiParam(name = "replyId", required = true, value = "replyId") @ObjectIdType ObjectId replyId,int floor){
        FReplyEntry entry=fReplyService.find(replyId);
        entry.setFloor(floor);
        fReplyService.saveFReplyEntryForFloor(entry);
        return RespObj.SUCCESS;
    }
    @ApiOperation(value = "downloadAttach", httpMethod = "POST", produces = "application/json")
    @RequestMapping("/downloadAttach")
    @SessionNeedless
    public void downloadAttach(@ApiParam(name = "replyId", required = true, value = "replyId") @ObjectIdType ObjectId replyId, HttpServletResponse response) throws UnsupportedEncodingException {

        FReplyDTO fReplyDTO = fReplyService.detail(replyId);
        String wordUrl = fReplyDTO.getWord();
        String wordName = fReplyDTO.getWordName();
        response.setCharacterEncoding("utf-8");
        response.setContentType("multipart/form-data");
        response.setHeader("Content-Disposition", "attachment;fileName="
                + java.net.URLEncoder.encode(wordName, "UTF-8"));
        try {
            InputStream inputStream = HttpUtils.getInputStream(wordUrl);
            OutputStream os = response.getOutputStream();
            IOUtils.copy(inputStream, os);
            // 关闭输入输出流
            IOUtils.closeQuietly(os);
            IOUtils.closeQuietly(inputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @ApiOperation(value = "downloadLikeInfos", httpMethod = "POST", produces = "application/json")
    @RequestMapping("/likeinfo")
    @SessionNeedless
    public void downloadLikeInfos(@ApiParam(name = "postId", required = true, value = "postId") @ObjectIdType ObjectId postId, HttpServletResponse response) throws IOException {

        response.setCharacterEncoding("utf-8");
        response.setContentType("multipart/form-data");
        response.setHeader("Content-Disposition", "attachment;fileName="
                + java.net.URLEncoder.encode(postId.toString() + ".xls", "UTF-8"));

        List<LikeInfo> likeInfos = fReplyService.getLikeInfos(postId);
        //创建excel工作簿
        Workbook wb = new HSSFWorkbook();
        //创建第一个sheet（页），命名为 new sheet
        Sheet sheet = wb.createSheet("点赞数据");

        for (int i = 0; i < likeInfos.size() + 1; i++) {
            // 创建一行，在页sheet上
            Row row = sheet.createRow((short) i);

            if (i == 0) {
                // Or do it on one line.
                row.createCell(0).setCellValue("回复id");
                row.createCell(1).setCellValue("用户id");
                row.createCell(2).setCellValue("用户名");
                row.createCell(3).setCellValue("用户昵称");
                row.createCell(4).setCellValue("用户邮箱");
                row.createCell(5).setCellValue("用户手机");
                row.createCell(6).setCellValue("楼层");
                row.createCell(7).setCellValue("回复内容");
                row.createCell(8).setCellValue("点赞数");
                row.createCell(9).setCellValue("点赞人[id,name,nick,ip]");
                continue;
            }

            LikeInfo likeInfo = likeInfos.get(i - 1);

            // Or do it on one line.
            row.createCell(0).setCellValue(likeInfo.getReplyId());
            row.createCell(1).setCellValue(likeInfo.getUserId());
            row.createCell(2).setCellValue(likeInfo.getUserName());
            row.createCell(3).setCellValue(likeInfo.getUserNickName());
            row.createCell(4).setCellValue(likeInfo.getUserEmail());
            row.createCell(5).setCellValue(likeInfo.getUserPhone());
            row.createCell(6).setCellValue(likeInfo.getFloor());
            row.createCell(7).setCellValue(likeInfo.getReplyContent());
            row.createCell(8).setCellValue(likeInfo.getLikeCount());

            int k = 9;
            List<LikeInfo.UserInfo> list = likeInfo.getUserInfoList();
            for (LikeInfo.UserInfo userInfo : list) {
                row.createCell(k).setCellValue(userInfo.toString());
                k++;
            }
        }

        //创建一个文件 命名为workbook.xls
        File file = File.createTempFile(postId.toString(), ".xls");
        FileOutputStream fileOut = new FileOutputStream(file);
        // 把上面创建的工作簿输出到文件中
        wb.write(fileOut);
        //关闭输出流
        IOUtils.closeQuietly(fileOut);

        try {
            FileInputStream input = new FileInputStream(file);
            OutputStream os = response.getOutputStream();
            IOUtils.copy(input, os);
            // 关闭输入输出流
            IOUtils.closeQuietly(input);
            IOUtils.closeQuietly(os);
            //删除文件
            FileUtils.deleteQuietly(file);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @ApiOperation(value = "downloadLikeInfosZip", httpMethod = "POST", produces = "application/json")
    @RequestMapping("/likeinfoZip")
    @SessionNeedless
    public void downloadLikeInfosZip(@ApiParam(name = "postId", required = true, value = "postId") @ObjectIdType ObjectId postId, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("utf-8");
        response.setContentType("multipart/form-data");
        response.setHeader("Content-Disposition", "attachment;fileName="
                + java.net.URLEncoder.encode(postId.toString() + ".zip", "UTF-8"));

        File dir = Files.createTempDir();
        List<FReplyEntry> fReplyEntries = fReplyService.getReplys(postId);
        for (FReplyEntry fReplyEntry : fReplyEntries) {
            String wordUrl = fReplyEntry.getUserWord();
            String wordName = fReplyEntry.getUserWordName();
            if (StringUtils.isNotBlank(wordUrl)) {
                try {
                    DownloadUtil.downLoadFromUrl(wordUrl, wordName, dir.getPath());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        File zip = File.createTempFile(new ObjectId().toString(), ".zip");
        try {
            new ZipCompressing().zip(dir, zip);
            FileInputStream inputStream = new FileInputStream(zip);
            OutputStream outputStream = response.getOutputStream();
            IOUtils.copy(inputStream, outputStream);
            // 关闭输入输出流
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        FileUtils.deleteDirectory(zip);
        FileUtils.deleteDirectory(dir);
    }
}
