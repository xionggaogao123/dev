<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<html>
<head>
    <title>文件管理-复兰科技</title>
    <meta charset="utf-8">
    <link rel="stylesheet" href="/static_new/css/myschool/fileManage.css">
    <link rel="stylesheet" type="text/css" href="/static/css/activity/foots.css">

    <!-- css files -->
    <!-- Normalize default styles -->
    <link href="/static_new/css/reset.css" rel="stylesheet" />
    <link href="/static_new/js/modules/core/0.1.0/fancyBox/jquery.fancybox.css?v=2015041602" rel="stylesheet" type="text/css" media="screen"/>
    <link href="/static/css/homepage.css" type="text/css" rel="stylesheet">
    <!-- jquery artZoom4Liaoba styles -->
    <!-- Custom styles -->
    <link href="/static_new/css/schoolsecurity.css?v=2015041602" rel="stylesheet" />
    <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery.min.js?v=1"></script>


    <style type="text/css">
        .lxfscroll ul{
            margin: 0px!important;
        }
        .lxfscroll-title ul{
            margin: 0px!important;
        }
        ul{
            margin: 0px!important;
        }
    </style>
</head>
<body>
<!--==================start引入头部==================-->
<%@ include file="../common_new/head.jsp" %>
<!--====================end引入头部====================-->
<div class="f-manage-main">
<%@ include file="../common_new/col-left.jsp" %>
    <!--广告-->
    <c:choose>
            <c:when test="${roles:isStudentOrParent(sessionValue.userRole)}">
                <jsp:include page="/WEB-INF/pages/common/right_2.jsp"></jsp:include>
            </c:when>
            <c:otherwise>
                <jsp:include page="/WEB-INF/pages/common/right.jsp"></jsp:include>
            </c:otherwise>
        </c:choose>
    <!--广告-->
    <div class="f-manage-right">
       <input type="hidden" id="hidden_id" value="${id}"/>
        <!--========================头部=============================-->
        <div class="f-right-top">
            <ul>
                <li><a href="javascript:showList();" class="cur">文件管理</a></li>
            </ul>
            <button onclick="addFile()">文件管理</button>
        </div>
        <!--===============================start文件管理===========================-->
        
        
        
        
        <script id="file_script" type="text/x-dot-template">
						{{~it:value:index}}

                <dd>
                    <p>
                        <em  class="f-ziti">文件名：{{=value.name}}<br>
                            <i>上传时间：{{=value.time}}</i>
                        </em>
                    </p>
                    <span>
                        <em  class="f-ziti">
                            文件大小：{{=value.size}}<br>
                            <label>上传人：{{=value.user}}</label>
                        </em>
                    </span>
                    <span>
                        <em  class="f-ziti">
                                                                        文件类型：{{=value.type}}
                        </em>
                    </span>
                    <div class="file-img">
                        <i class="fa fa-download"><a href="/myschool/department/file/down.do?fileId={{=value.id}}">下载</a></i>
                        <i class="fa fa-trash" onclick=delFile('{{=value.id}}')></i>
                    </div>
                </dd>
						{{~}}
					  </script>
    					
    					
    					
    					
        <div class="f-middle-info">
            <dl  id="file_div">
            
                

            </dl>
        </div>
        <!--===============================end文件管理===========================-->
        <!--===============================start信息修改===========================-->
        <div class="upload-manage" style="display:none;">
            <dl>
                <dd>
                    <span>

                    </span>
                </dd>
                
                <dd class="upload-line">
                    <em>上传文件</em>
                </dd>
                <dd class="upload-bottom">
                    <input type="file" id="file" name="file">
                    <button onclick="upload()">
                        上传文件
                    </button>
                </dd>
            </dl>
        </div>
        <!--===============================end信息修改===========================-->
    </div>
</div>
<div style="clear: both"></div>
<div id="footer">
    <div id="footer-w" style="font-size: 12px;">
        <span style="float: left">版权所有：上海复兰信息科技有限公司          <a href="http://www.fulaan-tech.com" target="_blank">www.fulaan-tech.com</a>        沪ICP备14004857号</span>
                    <span style="float: right"><a href="/aboutus/k6kt" style="font-size: 12px">关于我们</a>  |  <a href="/contactus/k6kt" style="font-size: 12px">联系我们</a>   |  <a href="/service/k6kt" style="font-size: 12px">服务条款 </a> |  <a href="/privacy/k6kt" style="font-size: 12px">隐私保护 </a> |  <a href='http://wpa.qq.com/msgrd?v=1&uin=2803728882&site=qq&menu=yes' target="_blank">
                        <img src="/img/activity/QQService.png"></a></span>
    </div>
</div>


<script src="/static_new/js/sea.js"></script>
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('depFile',function(depFile){
    	depFile.init();
    });


</script>


</body>
</html>
