<%--
  Created by IntelliJ IDEA.
  User: qiangm
  Date: 2015/7/27
  Time: 11:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib prefix="role" uri="http://fulaan.userRole.com" %>
<html>
<head>
    <meta charset="utf-8">
    <link rel="dns-prefetch" href="//source.ycode.cn"/>
    <title>复兰科技-私信</title>
    <meta name="viewport" content="width=device-width,initial-scale=1, maximum-scale=1">
    <link href="/static_new/css/reset.css" rel="stylesheet"/>
    <link href="/static/css/homepage.css" type="text/css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="/static_new/css/friendcircle/rome.css?v=1"/>
    <link rel="stylesheet" type="text/css" href="/static_new/css/friendcircle/wanban.css?v=1"/>
    <!-- jquery artZoom4Liaoba styles -->
    <!-- Custom styles -->

    <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery.min.js?v=1"></script>
</head>
<body replyId="${replyId}" style="overflow-x:hidden;">
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
    <!--广告-->
        <div class="right-border" style="overflow: visible;">
            <!--.col-right-->
            <div class="col-right ptl" style="padding-left: 10px;overflow: visible">
                <div class="wfsx hsx">清空所有私信</div>
                <div class="toname clearfix" id="receiverName">

                </div>
                <script id="receiverNameJs" type="application/template">
                    <p>发私信给：
                        <sapn>
                            {{=it.data}}
                        </sapn>
                    </p>
                    <textarea maxlength="1000" style="width: 770px" id="pm-content"></textarea>
                    <label>（限1000个字）</label>
                    <button id="sendPrivateMsgBtn">发送</button>
                </script>
                <ul class="both" id="replyListOl" style="width: 748px">
                </ul>
                <script id="replyListJs" type="application/template">
                    {{~it.data:value:index}}
                    {{?value.recipientName!=it.recipient}}
                    <li>
                        <div class="clearfix">
                            <img src="{{=value.userImage}}" alt="">

                            <h3>{{=value.senderName}}<span>{{=value.sendingTime}}</span></h3>

                            <p>{{=value.content}}</p>
                        </div>
                        <div class="yxj"><span letterId="{{=value.letterId}}"></span></div>
                    </li>
                    {{??}}
                    <ol>
                        <li>
                            <div class="clearfix">
                                <img src="{{=value.userImage}}" alt="">

                                <h3>{{=value.senderName}}<span>{{=value.sendingTime}}</span></h3>

                                <p>{{=value.content}}</p>
                            </div>
                            <div class="yxj"><span letterId="{{=value.letterId}}"></span></div>
                        </li>
                    </ol>
                    {{?}}
                    {{~}}
                </script>
                <!--.page-links-->
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
            <!--/.col-right-->
        </div>
</div>
<!-- 弹出层 -->
<div class="gay"></div>
<div class="tan1">
    <p>提示<span></span></p>

    <div class="jinggao">确定删除该对话？删除对话内容将不可恢复。</div>
    <div class="muo" style="width: 50%;margin:0 auto;">
        <button class="enter">确定</button>
        <button class="huidu">取消</button>
    </div>
</div>
<div class="tan2">
</div>
<script id="clearAllJs" type="application/template">
    <p>提示<span></span></p>
    <div class="jinggao">删除对话内容将不可恢复，确定要删除与<span>
        {{=it.data}}
    </span>
        之间的<span>所有</span>对话？
    </div>

    <div class="muo" style="width: 50%;margin:0 auto;">
        <button class="enter2">确定</button>
        <button class="huidu">取消</button>
    </div>
</script>
<!-- 弹出层 -->
<%@ include file="../common_new/foot.jsp" %>
<script src="/static_new/js/sea.js?v=1"></script>
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('friendReplyMsg');
</script>
</body>
</html>
