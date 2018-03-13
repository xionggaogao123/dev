<%@ page language="java" pageEncoding="gbk" %>
<%@ page import="java.net.URLEncoder" %>
<%!
    String imagepath1 = "";

    //编辑页面中包含 camera.swf 的 HTML 代码
    public String renderHtml(String id, String basePath, String input) {

        String uc_api = URLEncoder.encode(basePath + "personal/avatarupload.do");

        String urlCameraFlash = "/static/plugins/avatar/camera.swf?nt=1&inajax=1&appid=1&input=" + input + "&uploadSize=1024*1024&ucapi=" + uc_api;
        urlCameraFlash = "<script src=\"/static/plugins/avatar/camera.js?B6k\" type=\"text/javascript\"></script><script type=\"text/javascript\">document.write(AC_FL_RunContent(\"width\",\"450\",\"height\",\"253\",\"scale\",\"exactfit\",\"src\",\"" + urlCameraFlash + "\",\"id\",\"mycamera\",\"name\",\"mycamera\",\"quality\",\"high\",\"bgcolor\",\"#ffffff\",\"wmode\",\"transparent\",\"menu\",\"false\",\"swLiveConnect\",\"true\",\"allowScriptAccess\",\"always\"));</script>";

        return urlCameraFlash;
    }
%>
<%
    //最终裁剪好的图片存放位置
    String uid = request.getParameter("uid");
    imagepath1 = "head-" + uid + ".jpg";
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=GBK"/>
    <script type="text/javascript" src="/static/js/jquery.min.js"></script>
    <style>
        body {
            margin: 0;
            border: 0;
        }
        embed{
        	position:fixed;
        	top:50%;
        	left:50%;
        	transform:translate(-50%,-50%);
        }
    </style>
    <script type="text/javascript">
        function updateavatar() {
            var img1 = "<%=imagepath1%>";
            $.ajax({
                url: "/personal/updateavatar.do",
                data: "imgpath1=" + img1,
                type: "post",
                dataType: "json",
                success: function (data) {
                    if (data.result == 'false') {
                        alert("头像修改失败");
                        return;
                    }
                    try{
                    	window.opener.location.reload();
                    }catch(e){
                    	
                    }
                    

                    window.close();
                }
            });

        }

    </script>
</head>
<body style="overflow:hidden;">
<%
    out.print(renderHtml("5", basePath, URLEncoder.encode(imagepath1)));
%>
</body>
</html>
