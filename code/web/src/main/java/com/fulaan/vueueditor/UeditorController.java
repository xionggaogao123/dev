package com.fulaan.vueueditor;

import com.baidu.ueditor.ActionEnter;
import com.fulaan.base.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

//复制代码
/**
* 用于处理关于ueditor插件相关的请求
* @author Guoqing
*
*/
@RestController
//@CrossOrigin
@RequestMapping("/sys/ueditor")
public class UeditorController extends BaseController {

    @RequestMapping(value = "/exec")
    @ResponseBody
    public String exec(HttpServletRequest request) throws UnsupportedEncodingException {
        request.setCharacterEncoding("utf-8");
       String rootPath = request.getRealPath("/");
       return new ActionEnter( request, rootPath).exec();
     }
 }