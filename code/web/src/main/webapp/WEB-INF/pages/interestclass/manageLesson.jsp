<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>

<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>课程评价-复兰科技 K6KT-快乐课堂</title>
    <link rel="stylesheet" href="/static/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="/static/css/k6kt.css"/>
    <link rel="stylesheet" href="/static/css/dialog.css"/>
    <link rel="stylesheet" href="/static/css/evaluation/lessonHours.css"/>
    <script type="text/javascript" src="/static/js/jquery-1.11.1.min.js"></script>
    <script type="text/javascript" src="/static/js/sharedpart.js"></script>
    <script type="text/javascript" src="/static/js/evaluation/lesson-hours.js"></script>
    <script type="text/javascript">
        var currentPageID = 2;
        var classId = '${param.classId}';
        var termType = '${param.termType}';
    </script>
</head>
<body>


<%@ include file="../common_new/head.jsp" %>
<div id="content_main_container">
    <div id="content_main">

        <%@ include file="../common_new/col-left.jsp" %>


        <div id="right-container">
            <!-- 内容 -->
            <div class="k6kt-body" style="width: 770px;margin: auto;min-height: 400px;position: relative;">

                <div class="main-container">
                    <div class="top-button-container">
                        <%--<a href="/myclass/addlesson.do?classId=${param.classId}&termType=${param.termType}">--%>
                            <span class="inline-button btn-orange big" id="addLesson" classId="${param.classId}" termType="${param.termType}">添加课时</span>
                        <%--</a>--%>

                        <%--<a href="/myclass/finalcomment/${param.classId}">--%>
                            <%--<span class="inline-button btn-orange big">课程总评</span>--%>
                        <%--</a>--%>
                    </div>

                    <hr class="k6kt-hr"/>

                    <div class="hours-container">
                        <c:forEach items="${lessonScores}" var="lessonIndex" varStatus="status">
                            <div class="lesson-hour-div">
                                <a href="/myclass/addlesson.do?classId=${param.classId}&idx=${lessonIndex.li}&name=${lessonIndex.lnm}&termType=${param.termType}">
                                    <div class="hour-title inline-button fixed-size" index="${lessonIndex.li}" wi="${lessonIndex.wi}">${lessonIndex.lnm}</div>
                                </a>
                                <div class="update-btn">
                                    <img src="/img/myclass/inte-edit.jpg">
                                </div>
                                <c:if test="${status.last}">
                                    <div class="delete-btn" onclick="deleteLessonHour('${lessonIndex.li}')"><img src="/img/myclass/inte-de.png"></div>
                                </c:if>
                            </div>
                        </c:forEach>
                    </div>
                </div>

                <a href="/myclass/interstat/${param.classId}/${param.termType}"><span class="inline-button btn-orange fixed-size">返回</span></a>
            </div>

            <!-- 页尾 -->

        </div>

    </div>
</div>

<div class="homework-bg" style="display: none;z-index: 99">
</div>
<div class="homework-popup" style="display: none;z-index: 999">
    <dl>
        <dt>
            <span class="popup-to-le">编辑课时</span>
            <span class="popup-to-ri" style="margin-right: 40px">x</span>
        </dt>
        <dd>
            <em>课时名：</em>
            <input type="text" class="popup-te" id="title">
        </dd>
        <dd>
            <em>周次：</em>
            <select name="" id="weeks">
                <option value="1">第1周</option>
                <option value="2">第2周</option>
                <option value="3">第3周</option>
                <option value="4">第4周</option>
                <option value="5">第5周</option>
                <option value="6">第6周</option>
                <option value="7">第7周</option>
                <option value="8">第8周</option>
                <option value="9">第9周</option>
                <option value="10">第10周</option>
                <option value="11">第11周</option>
                <option value="12">第12周</option>
                <option value="13">第13周</option>
                <option value="14">第14周</option>
                <option value="15">第15周</option>
                <option value="16">第16周</option>
                <option value="17">第17周</option>
                <option value="18">第18周</option>
                <option value="19">第19周</option>
                <option value="20">第20周</option>
                <option value="21">第21周</option>
                <option value="22">第22周</option>
                <option value="23">第23周</option>
                <option value="24">第24周</option>
            </select>
        </dd>
        <dd>
            <button id="fb">确定</button>
        </dd>
    </dl>
</div>

<div>
    <%@ include file="../common_new/foot.jsp" %>

</div>

</body>
</html>
