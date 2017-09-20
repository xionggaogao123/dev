package com.fulaan.video.controller;

import com.fulaan.annotation.SessionNeedless;
import com.fulaan.base.BaseController;
import com.fulaan.video.service.VideoService;
import com.pojo.video.VideoEntry;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * Created by qinbo on 15/5/20.
 */
@Controller
@RequestMapping("/video")
public class VideoController extends BaseController {

    private VideoService videoService = new VideoService();
    private static final Logger logger = Logger.getLogger(VideoController.class);

    @ApiOperation(value = "persistentNotify", httpMethod = "POST", produces = "application/json")
    @RequestMapping("/persistentNotify")
    @SessionNeedless
    public
    @ResponseBody
    void persistentNotify(@RequestBody Map requestBody) {

        logger.info("video persistent enter");
        String persistentId = (String) requestBody.get("id");
        String bucketKey = null;
        try {
            String inputKey = (String) requestBody.get("inputKey");
            String[] tempArr = inputKey.split("/");
            bucketKey = tempArr[tempArr.length - 1];
        } catch (Exception e) {
        }

        int code = Integer.parseInt(requestBody.get("code").toString());
        VideoEntry videoEntry = videoService.getVideoEntryByPersistentId(persistentId);
        if (code == 0) {
            logger.info("video persistent code = 0");

            videoService.updateVideoUpdateStatus(videoEntry.getID(), 1);
        } else {
            logger.info("video persistent code = 1");
            videoService.updateVideoUpdateStatus(videoEntry.getID(), 2);

        }
    }
}
