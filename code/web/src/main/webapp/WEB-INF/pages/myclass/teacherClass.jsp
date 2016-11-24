<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib prefix="role" uri="http://fulaan.userRole.com" %>

<!DOCTYPE html>
<%------------------------------老师角色  我的班级--------------------------------------------------%>
<html>
<head>
    <title>我的班级-复兰科技 K6KT-快乐课堂</title>
    <meta charset="utf-8"/>
    <link rel="stylesheet" type="text/css" href="/static/css/dialog.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/style.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/flippedclassroom.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/bootstrap-combined.school.css"/>
    <link rel="stylesheet" type="text/css" href="/static_new/css/myclass/teacherClass.css"/>
    <script type="text/javascript" src="/static/js/jquery.min.js"></script>
    <script type="text/javascript" src="/static/plugins/bootstrap/js/bootstrap-paginator.min.js"></script>
    <script type="text/javascript" src="/static/js/sharedpart.js"></script>
    <script type="text/javascript" src="/static/js/common/role.js"></script>

    <script src="/static_new/js/modules/core/0.1.0/doT.min.js?v=1"></script>
</head>
<body interesttype="<% if(request.getParameter("type")!=null){out.println(java.net.URLDecoder.decode(request.getParameter("type"),"UTF-8")); }%>">
<%@ include file="../common_new/head.jsp" %>
<jsp:include page="/infoBanner.do"/>

<div id="content_main_container">
    <div id="content_main">
        <!-- 左侧导航-->
        <%@ include file="../common_new/col-left.jsp" %>
        <%@ include file="../common/right.jsp" %>
        <!-- left end -->
        <!-- right start-->
        <div id="right-container">
            <div id="fc_class_container">
                <h4>班级列表</h4>
                <hr style="background-color:#ff7e00;margin-top:10px;width:770px;float: left;">
                <div id="fc_class_list">

                    <script id="arraystmpl" type="text/x-dot-template">
                        {{~it.rows:v:index}}
                        <div class="fc_class_detail">
                            <div class="fc_class" style="cursor:pointer;width:770px;" href="/myclass/statstu/{{=v.id}}">
                                <div style="background: url(/img/activity/homepage-xuesheng.png);background-repeat:no-repeat;width: 90px;height: 87px;border: none"></div>
                                <div style="float: left;border: none">
                                    <div style="">
                                        <span class="blue">班级名称</span>
                                        <span>
                                            {{=v.className}}
                                        </span>
                                    </div>
                                    <div style="">
                                        <span class="blue">班主任</span>
                                        <span>
                                            {{=v.mainTeacher == '' ? '暂无' : v.mainTeacher == null ? '暂无' : v.mainTeacher}}
                                        </span>
                                    </div>
                                    <div style="">
                                        <span class="blue">班级学生数</span>
                                        <span>
                                            {{=v.studentCount}}
                                        </span>
                                    </div>
                                    <div style=";">
                                        <span class="blue">作业数</span>
                                        <span>
                                            {{=v.homeworkCount}}
                                        </span>
                                    </div>
                                    {{? it.role }}
                                    <div class="t_score" href="/myclass/addexp/{{=v.id}}">
                                        <span style="width: 94px;height: 32px;background: #FEA418;color: #fff;line-height: 30px;border-radius: 4px;">经验值加分</span>
                                    </div>
                                    {{? }}
                                    <p class="fix"></p>
                                </div>
                                <div class="fix"></div>
                            </div>
                            <div class="fix"></div>
                        </div>
                        {{~}}
                    </script>

                </div>
            </div>
            <div style="font-size: 14px;color: #000000;font-family: 'Microsoft YaHei';margin-top: 5em;color: rgb(255, 138, 0);font-size:16px; display:none; overflow: hidden;"
                 id="interest-info">
                <span style="margin-left:220px;"> 您还未开设“才艺社/拓展课”，加油哦！</span>
            </div>
            <div style="font-size: 14px;color: #000000;font-family: 'Microsoft YaHei';display:none; width: 780px; margin: 0 auto;margin-top: 5em;float: right;position: relative;left: -180px; "
                 id="interest-all-info">
                <span style="margin-left:52px;"> 如果您要开设拓展课供同学们选课，请让您的校领导或管理员用户进入“我的学校”下面的功能“管理拓展课”。</span><br>
                <span style="margin-left:140px;">拓展课开设完毕后，学生将在此版块根据设定的“选课规则”进行自主选课。</span>
            </div>
        </div>
        <!-- right end -->
    </div>
</div>

<%@ include file="../common_new/foot.jsp" %>
<script src="/static_new/js/sea.js"></script>
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use("teacherClass", function (teacherClass) {
        teacherClass.initPage('${currentUser.role}');
    });
</script>
</body>
</html>