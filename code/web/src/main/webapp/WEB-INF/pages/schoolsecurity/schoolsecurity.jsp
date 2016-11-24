
<%--
  Created by IntelliJ IDEA.
  User: guojing
  Date: 2015/6/26
  Time: 14:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="roles" uri="http://fulaan.userRole.com" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <!-- Basic Page Needs-->
    <meta charset="utf-8">
    <link rel="dns-prefetch" href="//source.ycode.cn" />
    <title>复兰科技</title>
    <meta name="description" content="">
    <meta name="author" content="" />
    <meta name="copyright" content="" />
    <meta name="keywords" content="">
    <meta name="viewport" content="width=device-width,initial-scale=1, maximum-scale=1">
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
    </style>
</head>
<body>


<!--#head-->
<!--=================================引入头部============================================-->
<%@ include file="../common_new/head.jsp" %>
<!--/#head-->

<!--#content-->
<div id="content" class="clearfix">

    <!--.col-left-->
    <%@ include file="../common_new/col-left.jsp" %>
    <!--/.col-left-->

    <!--.col-right-->
    <!--广告-->
    <c:choose>
        <c:when test="${roles:isStudentOrParent(sessionValue.userRole)}">
            <jsp:include page="/WEB-INF/pages/common/right_2.jsp"></jsp:include>
        </c:when>
        <c:otherwise>
            <jsp:include page="/WEB-INF/pages/common/right.jsp"></jsp:include>
        </c:otherwise>
    </c:choose>

    <div class="col-right">


        <!--.tab-col-->
        <div class="tab-col">

            <div class="tab-head clearfix">
                <ul>
                    <li class="cur"><a href="javascript:;">校园安全</a></li>
                </ul>
            </div>

            <div class="tab-main txt-info">
                <textarea id="security_content" class='input-newthing' rows='2' placeholder="来说一句" maxlength="140"></textarea>
                <label class="font-num">（限140个字）</label>
                <div class="upload-info clearfix">
                    <label id="upload-img" for="image-upload" style="cursor:pointer;">
                        <%--<img src="/img/tp_tool_img.png" class="upload-image" alt="" title="上传图片"/>--%>
                        <span class="upload-image">上传图片</span>
                    </label>

                    <div class="size-zero">
                        <input type="file" name="image-upload" id="image-upload" accept="image/*" multiple="multiple"/>
                    </div>
                    <img src="/img/loading4.gif" id="picuploadLoading"/>

                    <div id="img-container">
                        <ul></ul>
                    </div>
                    <button type="button" class="orange-btn">提交</button>
                </div>
            </div>

        </div>
        <!--/.tab-col-->

        <!--/.info-list-->
        <div class="info-list clearfix">

            <!--.info-head-->
            <div class="info-head clearfix">
                <ul>
                    <li value="2" class="cur">最新</li>
                    <li value="1">已处理</li>
                    <li value="0">未处理</li><!-- <li>待处理</li> 工作人员页面使用-->
                </ul>
                <c:if test="${roles:isHeadmaster(sessionValue.userRole)}">
                    <input id="allCheck" name="allCheck" type="checkbox">全选
                    <button type="button" class="batch-del">批量删除</button>
                </c:if>
            </div>
            <!--/.info-head-->

            <!--.info-main-->
            <div class="info-main">

                <!--.sub-info-list-->
                <div class="sub-info-list">

                </div>
                <!--.sub-info-list-->

                <!--.list-info-->
                <script type="text/template" id="j-tmpl">
                    {{ if(it.rows.length>0){ }}
                    {{ for (var i = 0, l = it.rows.length; i < l; i++) { }}
                    {{var obj=it.rows[i];}}
                    {{ if(${roles:isHeadmaster(sessionValue.userRole)}){ }}
                        <input name="idCheckbox" type="checkbox" value="{{=obj.id}}">
                    {{ } }}
                    <div class="list-info clearfix">
                        {{ if(obj.isHandle==0){ }}
                            {{ if(${roles:isNotStudentAndParent(sessionValue.userRole)}){ }}
                                <button value="{{=obj.id}}" class="stips-deal">处理</button>
                            {{ } }}
                            {{ if(${roles:isStudentOrParent(sessionValue.userRole)}){ }}
                                <label class="stips-un"></label>
                            {{ } }}
                        {{ }else if(obj.isHandle==1){ }}
                            <label class="stips-on"></label>
                        {{ } }}
                        <img src="{{=obj.userImage}}" class="user-img" />
                        <div class="list-txt">
                            <h4>{{=obj.userName}}
                                <em class="info-role">
                                    {{=obj.roleDescription}}
                                </em>
                            </h4>
                            <p>{{=obj.publishContent}}</p>
                                <ul class="clearfix">
                                {{~obj.fileNameAry:value:index}}
                                    {{ if(value!=''){ }}
                                         <li>
                                             <a class="fancybox" href="{{=value}}" data-fancybox-group="home" title="预览"><img class="content-img" title="点击查看大图" src="{{=value}}?imageView/1/h/80/w/80"></a>
                                         </li>
                                    {{}}}
                                {{~}}
                            </ul>
                            <div class="date-txt clearfix">
                                <span>{{=obj.createTime}}<em>来自{{=obj.platformDesc}}</em></span>
                                {{ if(obj.userId=='${sessionValue.id}'||${roles:isHeadmaster(sessionValue.userRole)}||${roles:isManager(sessionValue.userRole)}){ }}
                                    <label id="{{=obj.id}}" class="icon-del"></label>
                                {{}}}
                            </div>
                        </div>
                    </div>
                    {{ } }}
                    {{}else{ }}
                    <!-- 暂无记录 当.info-list没有的时候会用此替换掉-->
                    <div class="record">暂无记录</div>
                    <!-- 暂无记录 -->
                    {{ } }}
                </script>

                <!--/.list-info-->

                <!--.page-links-->
                <%--<div style="margin-top: 30px; margin-bottom: 30px; text-align:center;overflow:visible;">
                    <div id="example"></div>
                </div>--%>
                <div class="page-paginator">
                    <span class="first-page">首页</span>
                        <span class="page-index">
                            <span class="active">1</span>
                            <span>2</span>
                            <span>3</span>
                            <span>4</span>
                            <span>5</span>
                            <i>···</i>
                        </span>
                    <span class="last-page">尾页</span>

                </div>
                <!--/.page-links-->

            </div>
            <!--/.info-main-->

        </div>
        <!--/.info-list-->
    </div>
    <!--/.col-right-->

</div>
<!--/#content-->

<!--#foot-->
<%@ include file="../common_new/foot.jsp" %>
<!--#foot-->

<!-- <script id="j-tmpl" type="text/template">
    {{ if(it.success){ }}
    <h2>results:</h2>
    <ul>
        {{ for (var i = 0, l = it.data.length; i < l; i++) { }}
        <li>
            <h5>{{=it.data[i].title}}</h5>
            <p>{{!it.data[i].message}}</p>
        </li>
        {{ } }}
        <ul>
            {{ }else{ }}
            <h2>暂无数据</h2>
            {{ } }}
</script> -->

<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script src="/static_new/js/sea.js"></script>
<!-- Custom js -->
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('schoolsecurity',function(schoolsecurity){
        schoolsecurity.init();
    });
</script>

</body>
</html>