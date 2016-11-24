<%--
  Created by IntelliJ IDEA.
  User: qiangm
  Date: 2015/7/27
  Time: 11:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib prefix="role" uri="http://fulaan.userRole.com" %>
<html>
<head>
    <meta charset="utf-8">
    <link rel="dns-prefetch" href="//source.ycode.cn"/>
    <title>私信</title>
    <meta name="viewport" content="width=device-width,initial-scale=1, maximum-scale=1">
    <link href="/static/css/reset.css" rel="stylesheet"/>
    <link href="/static/css/homepage.css" type="text/css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="/static/css/friendcircle/rome.css?v=1"/>
    <link rel="stylesheet" type="text/css" href="/static/css/friendcircle/wanban.css?v=1"/>
    <!-- jquery artZoom4Liaoba styles -->
    <!-- Custom styles -->
    <script type="text/javascript" src="/static/js/jquery.min.js"></script>
</head>
<body>
<%@ include file="../common_new/head.jsp" %>
<div id="content" class="clearfix">
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
    <div class="col-right">
        <div class="wfsx">发私信</div>
        <ul class="hyyq" style="width: 100%;padding-bottom: 37px;">

        </ul>
        <!-- 收到的私信 -->
        <script type="text/template" id="hyyq">
            {{~it.data:value:index}}
            <li class="clearfix">
                <div class="clearfix">


                    <img src="{{=value.userImage}}" alt="">

                    <h3>{{=value.senderName}}
                        {{? value.unread ==1 }}
                        <ii class="redpointt"></ii>
                        {{?}}
                        <span>{{=value.sendingTime}}</span></h3>
                    <a href="/replyMessage?replyId={{=value.senderId}}"><p>{{=value.content}}</p></a>
                </div>
                <div class="yxj"><a href="/replyMessage?replyId={{=value.senderId}}">回复</a> | <span
                        userName="{{=value.senderName}}" userId="{{=value.senderId}}"></span></div>
            </li>
            {{~}}
        </script>
        <!-- 收到的私信 -->
        <!--.page-links-->
        <div class="page-paginator" style="margin-top: 0px;">
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
</div>
<!-- 弹出层 -->
<div class="gay"></div>
<div class="hylb" style="margin-top: 0px;margin-left: 0px;">
    <p>发送私信<span class="gb"></span></p>

    <div class="hylb_main clearfix">
        <div class="hylb_left">
            <div class="t">
                <span>联系人：</span>
                <input type="text" disabled="disabled" id="choosedFriend">
            </div>
            <div class="b">
                <span>内容：</span>
                <textarea style="border: 1px solid #DBDBDB;height: 102px;"
                          id="pm-content"></textarea>
            </div>
            <button class="sendMsg">发送</button>
        </div>
        <div class="hylb_right">

            <!--=====================搜索========================-->
            <div class="hylb_right_IN">
                <input id="searchContent" placeholder="请输入查询人姓名">
                <span id="searchBtn"></span>
            </div>
            <!--=====================搜索========================-->

            <ul style="width: 100%">
                <div class="bj jb">校领导</div>
                <ol id="headMasterListOl">
                </ol>
                <script id="headMasterListJs" type="application/template">
                    {{~it.data:value:index}}
                    <li class="frinedLi" userId="{{=value.id}}"
                        userName="{{=value.userName}}">{{=value.userName}}
                    </li>
                    {{~}}
                </script>
                <div class="bj jb">老师</div>
                <ol id="teacherListOl">
                </ol>
                <script id="teacherListJs" type="application/template">
                    {{~it.data:value:index}}
                    <li class="frinedLi" userId="{{=value.id}}"
                        userName="{{=value.userName}}">{{=value.userName}}
                    </li>
                    {{~}}
                </script>
                <div class="bj jb">学生/同学</div>
                <ol id="studentListOl">
                </ol>
                <script id="studentListJs" type="application/template">
                    {{~it.data:value:index}}
                    <li class="frinedLi" userId="{{=value.id}}"
                        userName="{{=value.userName}}">{{=value.userName}}
                    </li>
                    {{~}}
                </script>
                <div class="bj jb">家长</div>
                <ol id="parentListOl">
                </ol>
                <script id="parentListJs" type="application/template">
                    {{~it.data:value:index}}
                    <li class="frinedLi" userId="{{=value.id}}"
                        userName="{{=value.userName}}">{{=value.userName}}
                    </li>
                    {{~}}
                </script>
                <div class="bj jb">教育局</div>
                <ol id="departmentListOl">
                </ol>
                <script id="departmentListJs" type="application/template">
                    {{~it.data:value:index}}
                    <li class="frinedLi" userId="{{=value.id}}"
                        userName="{{=value.userName}}">{{=value.userName}}
                    </li>
                    {{~}}
                </script>
                <div class="bj jb">好友</div>
                <ol id="friendListOl">
                </ol>
                <script id="friendListJs" type="application/template">
                    {{~it.data:value:index}}
                    <li class="frinedLi" userId="{{=value.id}}"
                        userName="{{=value.userName}}">{{=value.userName}}
                    </li>
                    {{~}}
                </script>
            </ul>
        </div>
    </div>
</div>
<div class="tan2">

</div>
<script id="clearAllJs" type="application/template">
    <p>提示<span></span></p>
    <div class="jinggao">删除对话内容将不可恢复，确定要删除与<span>{{=it.data}}</span>之间的<span>所有</span>对话？</div>
    <div class="muo" style="width: 50%;margin: 0 auto;">
        <button class="enter">确定</button>
        <button class="huidu">取消</button>
    </div>
</script>
<!-- 弹出层 -->
<%@ include file="../common_new/foot.jsp" %>
<script src="/static/js/sea.js?v=1"></script>
<script src="/static/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('friendMessage');
</script>
</body>
</html>
