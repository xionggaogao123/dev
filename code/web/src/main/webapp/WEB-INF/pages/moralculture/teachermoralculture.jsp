<%--
  Created by IntelliJ IDEA.
  User: guojing
  Date: 2015/7/9
  Time: 17:02
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <link rel="dns-prefetch" href="//source.ycode.cn" />
    <title>德育-项目首页</title>
    <meta name="description" content="">
    <meta name="author" content="" />
    <meta name="copyright" content="" />
    <meta name="keywords" content="">
    <meta name="viewport" content="width=device-width,initial-scale=1, maximum-scale=1">
    <link rel="stylesheet" type="text/css" href="/static/css/homepage.css">
    <script type="text/javascript" src="/static/js/jquery-1.11.1.min.js"></script>
    <!-- css files -->
    <!-- Normalize default styles -->
    <link href="/static_new/css/reset.css" rel="stylesheet" />
    <!-- jquery artZoom4Liaoba styles -->
    <!-- Custom styles -->
    <link href="/static_new/css/moralculture.css?v=2015041602" rel="stylesheet" />
</head>
<body>
<!--#head-->
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


        <!--.tab-col右侧-->
        <div class="tab-col">

            <div class="tab-head clearfix">
                <ul>
                    <li class="cur"><a href="javascript:;">管理首页</a></li>
                </ul>
            </div>

            <div class="tab-main">
                <!--.tiaojian-col-->
                <div class="tiaojian-col clearfix">
                    <div class="select-style">
                        <select id="semesterId" name="semesterId" style="width:216px;">
                            <c:forEach items="${semesters}" var="item">
                                <option value="${item.key}">${item.value}</option>
                            </c:forEach>
                        </select>
                        <select id="gradeId" name="gradeId" style="width:90px;">
                            <c:forEach items="${gradeList}" var="item">
                                <option value="${item.id}">${item.name}</option>
                            </c:forEach>
                        </select>
                        <select id="classId" name="classId" style="width:90px;">
                        </select>
                    </div>
                    <div class="tianjian-btn" style="display: none">
                        <select>
                            <option>导出PDF</option>
                        </select>
                    </div>
                </div>
                <!--/.tiaojian-col-->
                <!-- 个人成绩vs班级均分 -->
                <dl class="gvsb">
                    <dt><em>1</em>学生列表</dt>
                    <!-- 表格 -->
                    <dd class="std-list">
                        <h4><span id="h4Title">2014-2015学年上学期三年一班</span>（德育）学期成绩</h4>
                        <table width="100%">
                            <thead>
                            <tr>
                                <th class="25%"><em>学生姓名</em></th>
                                <th class="25%"><em>提交情况</em></th>
                                <c:forEach items="${list}" var="item">
                                    <th class="25%"><em>${item.moralCultureName}</em></th>
                                </c:forEach>
                            </tr>
                            </thead>
                            <tbody class="table">

                            </tbody>
                            <script type="text/template" id="table">
                                {{ for (var i = 0, l = it.allScore.length; i < l; i++) { }}
                                    {{ var obj=it.allScore[i];}}
                                    <tr id="{{=obj.userId}}" class="tr-class">
                                        <td>{{=obj.userName}}</td>
                                        {{ if(obj.state==1){ }}
                                            <td>{{=obj.stateDesc}}</td>
                                            {{~obj.moralCultureScores:value:index}}

                                                {{ if(value.projectScore==''){ }}
                                                    <td class="kong">——</td>
                                                {{ } }}
                                                {{ if(value.projectScore!=''){ }}
                                                    <td>{{=value.projectScore}}</td>
                                                {{ } }}
                                            {{~}}
                                        {{ } }}
                                        {{ if(obj.state==0){ }}
                                            <td class="kong">{{=obj.stateDesc}}</td>
                                            {{~obj.moralCultureScores:value:index}}
                                                <td class="kong">——</td>
                                            {{~}}
                                        {{ } }}
                                    </tr>
                                {{ } }}
                            </script>
                        </table>
                    </dd>
                    <!-- 表格 -->
                </dl>
                <!-- 个人成绩vs班级均分 -->

            </div>

        </div>
        <!--/.tab-col右侧-->

    </div>
    <!--/.col-right-->

</div>
<!--/#content-->
<!--#foot-->
<%@ include file="../common_new/foot.jsp" %>
<!--#foot-->
<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script src="/static_new/js/sea.js"></script>
<!-- Custom js -->
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('teachermoralculture');
</script>
</body>
</html>