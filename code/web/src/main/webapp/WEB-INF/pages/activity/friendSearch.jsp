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
    <title>好友圈</title>
    <meta name="viewport" content="width=device-width,initial-scale=1, maximum-scale=1">
    <link href="/static_new/css/reset.css" rel="stylesheet"/>
    <link href="/static/css/homepage.css" type="text/css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="/static_new/css/friendcircle/rome.css?v=1"/>
    <link rel="stylesheet" type="text/css" href="/static_new/css/friendcircle/wanban.css?v=1"/>
    <!-- jquery artZoom4Liaoba styles -->
    <!-- Custom styles -->
    <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery.min.js?v=1"></script>
    <link href="/static_new/css/friendcircle/wanban.css?v=2015041602" rel="stylesheet"/>
</head>
<body>
<%@ include file="../common_new/head.jsp" %>
<div id="content" class="clearfix">
        <%@ include file="../common_new/col-left.jsp" %>
        <%@ include file="../common/right.jsp" %>
            <div class="col-right" style="overflow: visible;padding-left: 10px;">
                <div class="slm" style="overflow: visible">
                    <div class="zhongdian">
                        <select name="" id="geo">
                            <option value="1">全部学校</option>
                            <option value="2">本校</option>
                            <option value="3">本年级</option>
                        </select>
                        <select name="" id="role">
                            <option value="-1">全部</option>
                            <option value="2">老师</option>
                            <option value="4">家长</option>
                            <option value="1">学生</option>
                        </select>
                        <input type="text" placeholder="请输入好友姓名" id="searchkeywords">
                        <img src="/static_new/images/search.png" height="16" width="15" alt="" id="searchBtn">
                    </div>
                    <div class="slbm slmT">
                        <div class="rlb clearfix sousuo">

                        </div>

                        <!-- 好友列表 -->
                        <script type="text/template" id="rlb">
                            {{~it.data:value:index}}
                            <dl>
                                <p class="clearfix">
                                    <img src="{{=value.imgUrl}}" alt="">
                                        <span>{{=value.userName}}&nbsp;
                                        {{? isTeacher(value.role)}}
                                    老师
                                    {{??isStudent(value.role)}}
                                    学生
                                    {{??isHeadMaster(value.role)|| isK6ktHelper(value.role)}}
                                    校领导
                                    {{??isParent(value.role)}}
                                    家长
                                    {{??isEducation(value.role)}}
                                    教育局
                                    {{?}}</span>
                                        <span>
                                        {{?value.provinceName != null}}
                                                {{=value.provinceName}}
                                            {{?}}
                                        {{?value.cityName != null}}
                                                {{=value.cityName}}
                                            {{?}}

                                        </span>
                                    <span>{{=value.schoolName==null?"":value.schoolName}}</span>
                                </p>

                                <p class="clearfix">
                                    <em class="xsf" userName="{{=value.userName}}"  userId="{{=value.id}}">发私信</em>
                                    {{?value.relation == 1}}
                                    已经是好友
                                    {{??value.relation == 2}}
                                    申请已发出
                                    {{??value.relation == 3}}
                                    <em uid="{{=value.id}}" class="addFriendSpan" style="float: right;cursor: pointer; color: #ED6162;">加好友</em>
                                    {{?}}
                                </p>
                            </dl>
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
                    </div>
                </div>
            </div>
        </div>
<!-- 弹出层 -->
<div class="gay"></div>
<div class="hylb" style="margin-top: 0px;margin-left: 0px;width: 450px;">
    <p>发送私信<span class="gb"></span></p>

    <div class="hylb_main clearfix">
        <div class="hylb_left">
            <div class="t">
                <span>联系人：</span>
                <input type="text" disabled="disabled" id="choosedFriend" style="width:300px;">
            </div>
            <div class="b">
                <span>内容：</span>
                <textarea style="border: 1px solid #DBDBDB;height: 102px;width:300px"
                          id="pm-content"></textarea>
            </div>
            <button>发送</button>
        </div>
    </div>
</div>
<!-- 弹出层 -->
<%@ include file="../common_new/foot.jsp" %>
<script src="/static_new/js/sea.js?v=1"></script>
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('friendSearch');
</script>
</body>
</html>
