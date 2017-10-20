package com.fulaan.controller;

import com.fulaan.annotation.SessionNeedless;
import com.fulaan.utils.QiniuFileUtils;
import com.sys.utils.RespObj;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;


/**
 * Created by jerry on 2016/8/23.
 * 处理上传语音的接口
 */
@Api(value="处理上传语音的接口")
@Controller
@RequestMapping("/jxmapi/audio")
public class DefaultAudioController {

    /**
     * 处理上传语音的接口
     *
     * @param name 文件名
     * @param file 文件
     * @param type 文件类型
     * @return RespObj
     * @throws Exception
     */
    @ApiOperation(value = "处理上传语音的接口", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @SessionNeedless
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public RespObj uploadAudio(@RequestParam("name") String name,
                               @RequestParam("file") MultipartFile file,
                               @RequestParam("type") String type) throws Exception {
        String path;
        if (!file.isEmpty()) {
            String fileName = String.valueOf(System.currentTimeMillis()) + "." + type;
            QiniuFileUtils.uploadFile(fileName, file.getInputStream(), QiniuFileUtils.TYPE_SOUND);
            path = QiniuFileUtils.getPath(QiniuFileUtils.TYPE_SOUND, fileName);
        } else {
            return RespObj.FAILD("文件为空");
        }
        return RespObj.SUCCESS(path);
    }
}
