package com.fulaan.customizedpage.controller;

import com.fulaan.annotation.Customized;
import com.fulaan.annotation.SessionNeedless;
import com.fulaan.base.controller.BaseController;
import com.fulaan.cache.CacheHandler;
import com.fulaan.customizedpage.service.RecordVideoService;
import com.fulaan.customizedpage.utils.HttpUtil;
import com.fulaan.customizedpage.utils.LargeDownLoad;
import com.fulaan.screenshot.Encoder;
import com.fulaan.screenshot.EncoderException;
import com.fulaan.utils.QiniuFileUtils;
import com.fulaan.utils.StringUtil;
import com.fulaan.utils.pojo.HubeiLoginUtil;
import com.fulaan.video.service.VideoService;
import com.pojo.customized.RecordVideoDTO;
import com.pojo.customized.RecordVideoEntry;
import com.pojo.video.VideoEntry;
import com.pojo.video.VideoSourceType;
import com.sys.constants.Constant;
import com.sys.exceptions.FileUploadException;
import com.sys.exceptions.IllegalParamException;
import com.sys.props.Resources;
import com.sys.utils.DateTimeUtils;
import com.sys.utils.FileUtil;
import com.sys.utils.RespObj;
import com.sys.utils.ValidationUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;

/**
 * Created by admin on 2016/8/16.
 */
@Controller
@RequestMapping("/customized")
public class CustomizedController extends BaseController {

    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(CustomizedController.class);
    @Autowired
    private RecordVideoService recordVideoService;

    private VideoService videoService=new VideoService();

    @Autowired
    private LargeDownLoad largeDownLoad;

    private static final String url_prev="http://139.196.198.137";

//    private static final String url_prev="http://127.0.0.1";

    /**
     * 上传视频
     * @param
     * @return
     * @throws com.sys.exceptions.IllegalParamException
     * @throws java.io.IOException
     * @throws IllegalStateException
     * @throws com.fulaan.screenshot.EncoderException
     */
    @RequestMapping(value="/uploadVideo",method= RequestMethod.POST)
    @ResponseBody
    @SessionNeedless
    public RespObj uploadVideo(final  File file) throws IllegalParamException, IllegalStateException, IOException, EncoderException
    {

        MultipartFile Filedata= new MultipartFile() {
            @Override
            public String getName() {
                return null;
            }

            @Override
            public String getOriginalFilename() {
                return file.getName();
            }

            @Override
            public String getContentType() {
                return null;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public long getSize() {
                return file.length();
            }

            @Override
            public byte[] getBytes() throws IOException {


                byte[] bytes=new  byte[(int)file.length()];
                IOUtils.read(new FileInputStream(file),bytes);
                return bytes;
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return new FileInputStream(file);
            }

            @Override
            public void transferTo(File file) throws IOException, IllegalStateException {

            }
        };

        String fileName= FilenameUtils.getName(Filedata.getOriginalFilename());
        if(!ValidationUtils.isRequestVideoName(fileName))
        {
//            RespObj obj =new RespObj(Constant.FAILD_CODE, "视频名字非法");
//            map.put("flg",false);
//            map.put("mesg","视频名字非法");
//            return map;
        }

        //视频filekey
        String videoFilekey =new ObjectId().toString()+Constant.POINT+FilenameUtils.getExtension(fileName);
        String bathPath= Resources.getProperty("upload.file");
        File dir =new File(bathPath);
        if(!dir.exists())
        {
            dir.mkdir();
        }

        File savedFile = new File(bathPath, videoFilekey);
        OutputStream stream =new FileOutputStream(savedFile);
        FileCopyUtils.copy(Filedata.getInputStream(),stream);



//        String coverImage = new ObjectId().toString() + ".jpg";
        Encoder encoder = new Encoder();
//        File screenShotFile = new File(bathPath, coverImage);
        long videoLength = 60000;//缺省一分钟
        //是否生成了图片
//        boolean isCreateImage=false;
        try
        {
//            encoder.getImage(savedFile, screenShotFile, 1, 480, 270);
            videoLength = encoder.getInfo(savedFile).getDuration();
//            isCreateImage=true;
        }catch(Exception ex)
        {
            logger.error("", ex);
            ex.printStackTrace();
        }
        if(videoLength==-1){
            videoLength = 60000;//获取不到时间就设为1分钟
        }

//        logger.debug("begin upload video iamge");

        //String imageFilePath = null;
        //上传图片
//        if(isCreateImage&&screenShotFile.exists())
//        {
//            RespObj obj= QiniuFileUtils.uploadFile(coverImage, new FileInputStream(screenShotFile), QiniuFileUtils.TYPE_IMAGE);
//            if(!obj.getCode().equals(Constant.SUCCESS_CODE))
//            {
//                QiniuFileUtils.deleteFile(QiniuFileUtils.TYPE_VIDEO, videoFilekey);
//                obj =new RespObj(Constant.FAILD_CODE, "视频图片上传失败");
//                map.put("flg",false);
//                map.put("mesg","视频图片上传失败");
//                return map;
//            }
//        }
        VideoEntry ve =new VideoEntry(fileName, videoLength, VideoSourceType.USER_VIDEO.getType(),videoFilekey);
        ve.setID(new ObjectId());
//        logger.debug("begin upload video");

        QiniuFileUtils.uploadVideoFile(ve.getID(),videoFilekey, Filedata.getInputStream(), QiniuFileUtils.TYPE_USER_VIDEO);
//        if(isCreateImage&&screenShotFile.exists())
//        {
//            ve.setImgUrl(QiniuFileUtils.getPath(QiniuFileUtils.TYPE_IMAGE,coverImage));
//        }

//        ObjectId videoId=videoService.addVideoEntry(ve);
        //删除临时文件
        try
        {
            savedFile.delete();
//            screenShotFile.delete();
        }catch(Exception ex)
        {
            logger.error("", ex);
            ex.printStackTrace();
        }
//        byte[] data = new byte[stringBuffer.length()];
//        outputStream.write(data);
//        outputStream.flush();
//        outputStream.close();
//          out.println(QiniuFileUtils.getPath(QiniuFileUtils.TYPE_IMAGE, coverImage)+
//                  "$"+QiniuFileUtils.getPath(QiniuFileUtils.TYPE_USER_VIDEO, ve.getBucketkey()));
//          out.println();
//        return map;
//        QiniuFileUtils.getPath(QiniuFileUtils.TYPE_IMAGE, coverImage)
        RespObj respObj=new RespObj(Constant.SUCCESS_CODE);
        respObj.setMessage("default.jpg"+
                  "$"+QiniuFileUtils.getPath(QiniuFileUtils.TYPE_USER_VIDEO, ve.getBucketkey()));
        return respObj;
    }

    //得到文件名
    private String getFileName(MultipartFile file)
    {
        String orgname = file.getOriginalFilename();

        return new ObjectId().toString()+Constant.POINT+ orgname.substring(orgname.lastIndexOf(".") + 1);
    }



    @SessionNeedless
    @RequestMapping("/record_done")
    public void recordDone(HttpServletRequest request) {
        String path=request.getParameter("path");
        final String app=request.getParameter("app");
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        final String date=sdf.format(System.currentTimeMillis());

        //先下载到本地，然后上传到七牛服务器

        final String str=path.substring(path.lastIndexOf("/"));
        final String sendUrl=url_prev+"/"+app+str;
        logger.info("访问的远程路径："+sendUrl);

//        Runnable runnable=new Runnable() {
//            @Override
//            public void run() {
                try {
                    String appName="";
                    if("hubei".equals(app)){
                       appName="湖北"+ DateTimeUtils.convert(System.currentTimeMillis(),DateTimeUtils.DATE_YYYY_MM_DD)
                              +"-"+DateTimeUtils.convert(System.currentTimeMillis(),DateTimeUtils.DATE_HH_MM_SS);
                    }else if("xizang".equals(app)){
                       appName="西藏"+ DateTimeUtils.convert(System.currentTimeMillis(),DateTimeUtils.DATE_YYYY_MM_DD)
                                +"-"+DateTimeUtils.convert(System.currentTimeMillis(),DateTimeUtils.DATE_HH_MM_SS);
                    }else if("live".equals(app)){
                       appName="测试"+ DateTimeUtils.convert(System.currentTimeMillis(),DateTimeUtils.DATE_YYYY_MM_DD)
                                +"-"+DateTimeUtils.convert(System.currentTimeMillis(),DateTimeUtils.DATE_HH_MM_SS);
                    }
                    RecordVideoDTO dto=new RecordVideoDTO();
                    String bathPath= Resources.getProperty("upload.file");
//                    String tempDir=bathPath+str;
//                    System.out.println(sendUrl);
//                    String testUrl="http://139.196.198.137/xizang/xizang-xizang-24-Aug-10:17:52.flv";
//                    String filePath=HttpUtil.downloadFile(bathPath,
//                            sendUrl);
                    String filePath=largeDownLoad.doDownload(bathPath,sendUrl);

//                    String testUrl="D:\\upload\\testVideo.flv";
                    
                    File tempFile=new File(filePath);
                    RespObj respObj=uploadVideo(tempFile);

                    String result=(String)respObj.getMessage();
                    if(result.indexOf("$")>-1){
                        String []res=result.split("\\$");
                        dto.setUrl(res[1]);
                        dto.setImageUrl(res[0]);
                    }
                    dto.setDate(date);
                    dto.setName(appName);
                    dto.setApp(app);
                    dto.setRemove(0);

                    recordVideoService.saveOrUpdate(dto);
                } catch(Exception e){
                    logger.error("从服务器上下载视频出错!", e);
                }
//            }
//        };
//        Thread t=new Thread(runnable);
//        t.start();


    }



    @SessionNeedless
    @RequestMapping("/hubei/hubeiLogin")
    @ResponseBody
    public ModelAndView hubeiLogin(HttpServletRequest request) {
        ModelAndView  mav = new ModelAndView();
        mav.setViewName("customized/hubei/hubeiLogin");
        return mav;
    }

    @SessionNeedless
    @Customized
    @RequestMapping("/getRecord")
    @ResponseBody
    public Map<String,Object> getRecord(
            @RequestParam(value = "date", defaultValue = "") String date,
            @RequestParam(value = "app", defaultValue = "") String app,
            int page, int pageSize)throws Exception{
        Map<String,Object> map=new HashMap<String, Object>();
        List<RecordVideoDTO> recordVideoDTOList=recordVideoService.getRecordVideoList(date,app,page,pageSize);
        int count=recordVideoService.countRecordVideoList(date,app);
        map.put("list",recordVideoDTOList);
        map.put("page", page);
        map.put("pageSize", pageSize);
        map.put("count",count);
        return map;
    }

    @SessionNeedless
    @RequestMapping("/logout")
    @ResponseBody
    public RespObj logout(HttpServletRequest request){
        RespObj respObj=new RespObj(Constant.SUCCESS_CODE);
//        request.getSession().setAttribute("login", "false");
        Cookie cookies[] = request.getCookies();
        Cookie c = null;
        for (int i = 0; i < cookies.length; i++) {
            c = cookies[i];
            c.setMaxAge(0);
            if(c.getName().equals(Constant.COOKIE_PROVINCE_LOGIN))
            {
                CacheHandler.deleteKey(c.getValue());
            }
        }
        return respObj;
    }


    public Map<String,String> getMap(HttpServletRequest request){
        Cookie[] cookies=request.getCookies();
        if(null!=cookies)
        {
            for(Cookie cookie:cookies)
            {
                if(cookie.getName().equals(Constant.COOKIE_PROVINCE_LOGIN))
                {
                    return CacheHandler.getMapValue(cookie.getValue());
                }
            }
        }
        return null;
    }


    @SessionNeedless
    @RequestMapping("/login")
    @ResponseBody
    public RespObj sendHubei(
            @RequestParam(value = "userName", defaultValue = "") String userName,
            @RequestParam(value = "password", defaultValue = "") String password,
            HttpServletRequest request,HttpServletResponse response){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
//        ModelAndView  mav = new ModelAndView();
        if(null== HubeiLoginUtil.split_list|| HubeiLoginUtil.split_list.size()==0){
            HubeiLoginUtil.init();
        }

        if("".equals(userName)||"".equals(password)){
            request.getSession().setAttribute("login", "false");
        }else{
            boolean flag=false;
            for(String item: HubeiLoginUtil.split_list){
                String []myItem=item.split(",");
                if(userName.equals(myItem[0])&&password.equals(myItem[1])){
                     flag=true;
                     break;
                }
            }
            if(flag){
                ObjectId cacheUserKey=new ObjectId();
                Map<String,String> map=new HashMap<String, String>();
                map.put("username",userName);
                //s_key
                CacheHandler.cache(cacheUserKey.toString(), map, Constant.SECONDS_IN_DAY);
                //处理cookie
                Cookie userKeycookie = new Cookie(Constant.COOKIE_PROVINCE_LOGIN,cacheUserKey.toString());
                userKeycookie.setMaxAge(Constant.SECONDS_IN_DAY);
                userKeycookie.setPath(Constant.BASE_PATH);
                response.addCookie(userKeycookie);
//                request.getSession().setAttribute("login", "true");
//                request.getSession().setAttribute("userName",userName);
                respObj.setCode(Constant.SUCCESS_CODE);
            }else {
//                request.getSession().setAttribute("login", "false");
                respObj.setMessage("用户名或密码错误！");
            }
        }
//        try{
//            response.sendRedirect("/customized/hubei/livePlatform.do");
//        }catch ( Exception e){
//            e.printStackTrace();
//        }

        return respObj;
    }

    @SessionNeedless
    @Customized
    @RequestMapping("/hubei/livePlatform")
    @ResponseBody
    public ModelAndView livePlatform(HttpServletRequest request) {
        ModelAndView  mav = new ModelAndView();
        Map<String,String> map=getMap(request);
        mav.addObject("userName",map.get("username"));
        mav.setViewName("customized/hubei/livePlatform");
        return mav;
    }

    @SessionNeedless
    @Customized
    @RequestMapping("/hubei/hubeishuiguohu")
    @ResponseBody
    public ModelAndView hubeishuiguohu(HttpServletRequest request) {
        ModelAndView  mav = new ModelAndView();
        Map<String,String> map=getMap(request);
        mav.addObject("userName",map.get("username"));
        mav.setViewName("customized/hubei/hubeishuiguohu");
        return mav;
    }

    @SessionNeedless
    @Customized
    @RequestMapping("/hubei/xizangshannan")
    @ResponseBody
    public ModelAndView xizangshannan(HttpServletRequest request) {
        ModelAndView  mav = new ModelAndView();
        Map<String,String> map=getMap(request);
        mav.addObject("userName",map.get("username"));
        mav.setViewName("customized/hubei/xizangshannan");
        return mav;
    }

    @SessionNeedless
    @Customized
    @RequestMapping("/hubei/record")
    @ResponseBody
    public ModelAndView recordVideo(HttpServletRequest request) {
        ModelAndView  mav = new ModelAndView();
        Map<String,String> map=getMap(request);
        mav.addObject("userName",map.get("username"));
        mav.setViewName("customized/hubei/recordVideo");
        return mav;
    }

    /**
     * 视频删除
     */
    @SessionNeedless
    @RequestMapping(value ="/removeVideo",method = RequestMethod.GET)
    @ResponseBody
    public RespObj removeVideo(@RequestParam(value = "videoId", defaultValue = "") String videoId){

        RespObj respObj =new RespObj(Constant.FAILD_CODE);

        try{
            if(StringUtils.isNotBlank(videoId)){
                recordVideoService.removeVideo(new ObjectId(videoId));
            }
            respObj.setCode(Constant.SUCCESS_CODE);
        }catch (Exception e){
            logger.error("视频删除失败！",e);
            respObj.setMessage("删除失败");
        }
        return respObj;
    }

}
