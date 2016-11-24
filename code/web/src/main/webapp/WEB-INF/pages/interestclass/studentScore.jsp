<%--
  Created by IntelliJ IDEA.
  User: yan
  Date: 2015/8/5
  Time: 18:34
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib prefix="role" uri="http://fulaan.userRole.com" %>
<!DOCTYPE html>
<html>
<head>
    <!-- Basic Page Needs-->
    <meta charset="utf-8">
    <link rel="dns-prefetch" href="//source.ycode.cn"/>
    <title>复兰科技-拓展课评价-课程总评</title>
    <!-- css files -->
    <meta name="viewport" content="width=device-width,initial-scale=1, maximum-scale=1">
    <link href="/static_new/css/interestclass/reset.css?v=1" rel="stylesheet"/>
    <link href="/static_new/css/interestclass/expand.css?v=2015041602" rel="stylesheet"/>
    <link rel="stylesheet" href="/static_new/css/interestclass/common.css?v=1" type="text/css"/>
    <script type="text/javascript" src="/static/js/jquery-1.11.1.min.js"></script>
    <!-- Add fancyBox main JS and CSS files -->
    <script type="text/javascript" src="/static/plugins/fancyBox/jquery.fancybox.js?v=2.1.5"></script>
    <link rel="stylesheet" type="text/css" href="/static/plugins/fancyBox/jquery.fancybox.css?v=2.1.5" media="screen"/>
</head>
<script type="text/javascript">
    $('.fancybox').fancybox();
</script>
<body classId="${classId}" stuId="${stuId}" termType="${termType}">
<!--#head-->
<%@ include file="../common_new/head.jsp" %>
<!--/#head-->
<!--#content-->
<div id="content_main_container" style="overflow: visible">
    <div id="content" class="clearfix" style="overflow: visible">
        <!--.col-left-->
        <%@ include file="../common_new/col-left.jsp" %>
        <!--/.col-left-->
        <!-- 广告部分 -->
        <%@ include file="../common/right.jsp" %>
        <!-- 广告部分 -->
        <!--.col-right-->
        <div class="col-right">
            <!--.tab-col-->
            <div class="tab-col">
                <div id="pic_one" style="display:none;">
                    <script type="application/template" id="imgTwoJs">
                        <img src="{{=it.resultspicsrc==null?'':it.resultspicsrc}}"/>
                    </script>
                </div>
                <div id="zoom_word_one" style="display:none;">
                    <script type="application/template" id="WordTwoJs">
                        <p>{{=it.teachercomments==null?"":it.teachercomments}}</p>
                    </script>
                </div>
                <div class="student_list">
                    <h3>课程总评
                        <%--<a href="/myclass/toselinclass?version=19_1">返回</a>--%>
                    </h3>
                    <hr>
                    <table width="100%">
                        <thead>
                        <th>成果展示(上传一张图片)</th>
                        <th>老师评语(250字以内)</th>
                        <th>考勤</th>
                        <th>日常评价</th>
                        <th>总体评价</th>
                        </thead>
                        <tbody>
                        <tr id="lessonScriptShow">
                            <td>
                                <a class="fancybox" href="" id="imgOne">

                                </a>
                            </td>
                            <td>
                                <p class="zxx_zoom_word">
                                    <span id="tc"></span>
                                </p>
                            </td>
                            <td id="at"></td>
                            <script id="lessonScript" type="application/template">

                                <!--平时成绩-->
                            <td>
                                {{for(var i=0;i<5;i++){}}
                                {{? i < it.semesterscore}}
                                <em class="shi"></em>
                                {{??}}
                                <em></em>
                                {{?}}
                                {{}}}
                                {{? it.semesterscore > 4}}
                                <span>优秀</span>
                                {{?? it.semesterscore == 4}}
                                <span>良好</span>
                                {{?? it.semesterscore == 3}}
                                <span>合格</span>
                                {{??}}
                                <span>需努力</span>
                                {{?}}
                            </td>
                                <!--期末成绩-->
                                <td>
                                    {{for(var i=0;i<5;i++){}}
                                    {{? i < it.finalresult}}
                                    <em class="shi"></em>
                                    {{??}}
                                    <em></em>
                                    {{?}}
                                    {{}}}
                                    {{? it.finalresult > 4}}
                                    <span>优秀</span>
                                    {{?? it.finalresult == 4}}
                                    <span>良好</span>
                                    {{?? it.finalresult == 3}}
                                    <span>合格</span>
                                    {{??}}
                                    <span>需努力</span>
                                    {{?}}
                                </td>
                          </script>

                        </tr>
                        </tbody>
                    </table>
                    <h3>课堂表现</h3>
                    <hr>
                    <table width="100%">
                        <thead>
                        <th>课时</th>
                        <th>课堂成果</th>
                        <th>老师评语</th>
                        <th>考勤</th>
                        <th>课堂表现</th>
                        </thead>
                        <tbody id="lessonScoreShow">
                        </tbody>
                        <script id="lessonScore" type="application/template">
                            {{~it:value:index}}
                            <tr>
                                <td>课时{{=value.lessonindex}}</td>
                                <td>
                                    {{? value.pictureUrl != null && value.pictureUrl != ''}}
                                    <a class="fancybox" href="{{=value.pictureUrl}}" >
                                        <img src="{{=value.pictureUrl}}"/>
                                    </a>
                                    {{?}}
                                </td>
                                <td>
                                    <span class="lessonScore-WE">{{=value.teacherComment}}</span>
                                </td>
                                <td>
                                    {{? value.attendance==1}}
                                    <strong>到</strong>
                                    {{??}}
                                    <strong class="lessonScore-QQ">缺勤</strong>
                                    {{?}}
                                </td>
                                <td>
                                    {{for(var i=0;i<5;i++){}}
                                    {{?i < value.stuscore }}
                                    <em class="shi"></em>
                                    {{??}}
                                    <em></em>
                                    {{?}}
                                    {{}}}
                                    {{? value.stuscore > 4}}
                                    <span>优秀</span>
                                    {{?? value.stuscore == 4}}
                                    <span>良好</span>
                                    {{?? value.stuscore == 3}}
                                    <span>合格</span>
                                    {{??}}
                                    <span>需努力</span>
                                    {{?}}
                                   <!-- <s>&nbsp;{{=value.stuscore}}&nbsp;分</s>-->
                                </td>
                            </tr>
                            {{~}}
                        </script>
                    </table>
                </div>
            </div>
            <!--/.tab-col-->
        </div>
        <!--/.col-right-->
    </div>
</div>
<!--/#content-->
<!--#foot-->
<%@ include file="../common_new/foot.jsp" %>
<!--#foot-->
<!-- initialize seajs Library -->
<script src="/static_new/js/sea.js"></script>
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('studentScore');
</script>
</body>
</html>
