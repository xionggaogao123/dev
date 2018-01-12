package com.fulaan.vueueditor;

import com.fulaan.base.BaseController;
import com.fulaan.ueditor.ActionEnter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

//复制代码
/**
* 用于处理关于ueditor插件相关的请求
* @author Guoqing
*
*/
@Controller
//@CrossOrigin
@RequestMapping("/web/sys/ueditor")
public class UeditorController extends BaseController {

    @RequestMapping(value = "/exec")
    @ResponseBody
    public String exec(HttpServletRequest request) throws UnsupportedEncodingException {
        try{
            request.setCharacterEncoding("utf-8");
            String rootPath = request.getRealPath("/");
            return new ActionEnter( request, rootPath).exec();
        }catch (Exception e){

        }
       return "";
    }
 }