<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>

<%--  打印成绩单页面--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<html>
<head>
    <title>课时评分-复兰科技 K6KT-快乐课堂</title>
    <link rel="stylesheet" href="/static/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="/static/css/k6kt.css"/>
    <link rel="stylesheet" href="/static/css/dialog.css"/>
    <link rel="stylesheet" href="/static/css/evaluation/each-lesson.css"/>
    <link rel="stylesheet" href="/static/css/evaluation/term-end.css">
    <link rel="stylesheet" href="/static/css/font-awesome.min.css">
    <script type="text/javascript" src="/static/js/jquery-1.11.1.min.js"></script>
    <script type="text/javascript" src="/static/js/sharedpart.js"></script>
    <script type="text/javascript" src="/static/plugins/jquery-upload/vendor/jquery.ui.widget.js"></script>
    <%--<script type="text/javascript" src="/static/plugins/jquery-upload/jquery.iframe-transport.js"></script>--%>
    <script type="text/javascript" src="/static/plugins/jquery-upload/jquery.fileupload.js"></script>
    <script type="text/javascript" src="/static/plugins/fancyBox/jquery.fancybox.js?v=2.1.5"></script>
    <link rel="stylesheet" type="text/css" href="/static/plugins/fancyBox/jquery.fancybox.css?v=2.1.5" media="screen"/>
    <script type="text/javascript" src="/static/js/evaluation/each-lesson.js"></script>

    <script type="text/javascript">
        var currentPageID = 2;
        var classId = '${param.classId}';
        var type = '${param.type}';
        var name = '${param.name}';
        var termType = '${param.termType}';
    </script>
    <style>
        html,body{font: 12px/18px "lucida grande", "lucida sans unicode", 'Microsoft YaHei', tahoma, verdana, arial, sans-serif !important;}
    </style>
</head>
<body>

<%@ include file="../common_new/head.jsp" %>
<div id="content_main_container">
    <div id="content_main">

        <%@ include file="../common_new/col-left.jsp" %>


        <div id="right-container">
            <!-- 内容 -->
            <div class="k6kt-body" style="width: 770px;position:relative;">

                <div class="lesson-score-list">
                    <table class="table-k6kt">
                        <tr>
                            <th>学生姓名</th>
                            <th colspan="2">课堂成果</th>
                            <th>老师评语</th>
                            <th>考勤</th>
                            <th>课堂表现</th>
                        </tr>
                        <c:forEach items="${scoreList}" var="lessonScore" varStatus="loop">
                            <tr class="score-line" stu-id="${lessonScore.userid}" index="${lessonScore.lessonindex}">
                                <td>
                                    <input type="hidden" name="userid" value="${lessonScore.userid}">
                                    <img class="stu-list-img avatar-min" src="${lessonScore.studentAvatar}"/>
                                        ${lessonScore.studentName}
                                </td>
                                <%--课堂成果--%>
                                <td class="image-statement-td">
                                    <c:if test="${lessonScore.pictureUrl != null && lessonScore.pictureUrl != ''}">
                                        <a class="fancybox" href="${lessonScore.pictureUrl}"
                                           data-fancybox-group="gallery" title="${lessonScore.studentName}">
                                            <img src="${lessonScore.pictureUrl}" class="statement-image"/>
                                        </a>
                                    </c:if>
                                </td>
                                <td class="uploader-td">
                                    <label for="upload-statement-${loop.index}" class="upload-label">
                                        <span class="upload-button">上传</span>
                                    </label>
                                    <i class="fa fa-spinner fa-spin"></i>

                                    <div class="input-file-container">
                                        <input type="file" class="upload-statement"
                                               id="upload-statement-${loop.index}" name="Filedata"
                                               accept="image/*"  stu-id="${lessonScore.userid}" index="${lessonScore.lessonindex}"/>
                                    </div>
                                </td>
                                <%--老师评语--%>
                                <td>
                                    <c:choose>
                                        <c:when test="${lessonScore.teacherComment != null && lessonScore.teacherComment != ''}">
                                                    <textarea class="statement-textarea readonly" readonly
                                                              maxlength="500">${lessonScore.teacherComment}</textarea>
                                                    <span><button class="button-k6kt"
                                                                  onclick="statementButtonAction(this)" state="change">
                                                        修改
                                                    </button></span>
                                        </c:when>
                                        <c:otherwise>
                                            <textarea class="statement-textarea" maxlength="500"></textarea>
                                                    <span><button class="button-k6kt"
                                                                  onclick="statementButtonAction(this)" state="save">保存
                                                    </button></span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${lessonScore.attendance == 1}">
                                            <input type="radio" name="${lessonScore.studentName}" value="1" checked><span>到</span>
                                            <input type="radio" name="${lessonScore.studentName}" value="0"><span class="score-KK">缺勤</span>
                                        </c:when>
                                        <c:otherwise>
                                            <input type="radio" name="${lessonScore.studentName}" value="1"><span>到</span>
                                            <input type="radio" name="${lessonScore.studentName}" value="0" checked><span class="score-KK">缺勤</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <!--课堂表现-->
                                <td class="stars-container">
                                    <i class="fa fa-star-o fa-lg orange-color"></i>
                                    <i class="fa fa-star-o fa-lg orange-color"></i>
                                    <i class="fa fa-star-o fa-lg orange-color"></i>
                                    <i class="fa fa-star-o fa-lg orange-color"></i>
                                    <i class="fa fa-star-o fa-lg orange-color"></i>
                                    <input type="text" name="stuscore" class="score" hidden="true"
                                           value="${lessonScore.stuscore != null? lessonScore.stuscore : 5}"
                                           readonly/><span></span>
                                    <c:if test="${lessonScore.id != null}">
                                        <input type="hidden" name="id" value="${lessonScore.id}"/>
                                        <input type="hidden" name="lessonindex" value="${lessonScore.lessonindex}"/>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                    </table>
                </div>

                <div>
                    <a href="/myclass/lead2ls/1/${param.classId}/${param.termType}" class="score-btn"><span
                            class="inline-button btn-orange fixed-size">返回</span></a>
                    <span class="inline-button btn-orange fixed-size" style="float: right !important;"
                          onclick="saveLessonScore()">保存</span>
                </div>


            </div>


        </div>

    </div>
    <div>
    </div>
    <!-- 页尾 -->
    <%@ include file="../common_new/foot.jsp" %>
</div>


</body>
</html>