<%@ page import="java.util.Calendar" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>

<%--  打印成绩单页面--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%
    pageContext.setAttribute("newLineChar", "\n");
    Calendar calendar = Calendar.getInstance();
    pageContext.setAttribute("currentYear", calendar.get(Calendar.YEAR));
%>
<html>
<head>
    <title>${transcriptView.schoolName}成绩单</title>
    <link rel="stylesheet" href="/static/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="/static/css/k6kt.css"/>
    <link rel="stylesheet" href="/static/css/dialog.css"/>
    <link rel="stylesheet" href="/static/css/evaluation/transcript.css">

    <script type="text/javascript" src="/static/js/jquery-1.11.1.min.js"></script>
    <script type="text/javascript" src="/static/plugins/printThis.js"></script>
    <script type="text/javascript" src="/static/js/sharedpart.js"></script>
    <script type="text/javascript">
        var currentPageID = 2;
    </script>
</head>
<body>


<%@ include file="../common_new/head.jsp" %>
<div id="content_main_container">
    <div id="content_main">

        <%@ include file="../common_new/col-left.jsp" %>


        <div id="right-container">
            <!-- 内容 -->
            <div class="k6kt-body">
                <div class="transcript-container">

                    <h2>${transcriptView.termName}"${transcriptView.classname}"课程评价</h2>

                    <table class="transcript-table">
                        <tr>
                            <th width="15%">班级</th>
                            <td width="18%">${transcriptView.classname}</td>
                            <th width="15%">姓名</th>
                            <td width="19%">${transcriptView.nickName}</td>
                            <th width="15%">学号</th>
                            <td>${transcriptView.studentNum}</td>
                        </tr>
                        <tr>
                            <th>执教老师</th>
                            <td>${transcriptView.teacherNAME}</td>
                            <th>日常评价</th>
                            <td>
                                <c:choose>
                                    <c:when test="${transcriptView.usualResult == null}">
                                        <c:forEach begin="0" end="2">
                                            <img src="/images/white-star.jpg">
                                        </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                        <c:forEach begin="1" end="${transcriptView.usualResult}">
                                            <img src="/images/yello-star.jpg">
                                        </c:forEach>
                                        <c:forEach begin="${transcriptView.usualResult + 1}" end="5">
                                            <img src="/images/white-star.jpg">
                                        </c:forEach><br/>
                                        <c:choose>
                                            <c:when test="${transcriptView.usualResult == 5}">
                                                <span>优秀</span>
                                            </c:when>
                                            <c:when test="${transcriptView.usualResult == 4}">
                                                <span>良好</span>
                                            </c:when>
                                            <c:when test="${transcriptView.usualResult == 3}">
                                                <span>合格</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span>需努力</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <th>总体评价</th>
                            <td>
                                <c:choose>
                                    <c:when test="${transcriptView.finalResult == null}">
                                        <c:forEach begin="0" end="4">
                                            <img src="/images/white-star.jpg">
                                        </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                        <c:forEach begin="1" end="${transcriptView.finalResult}">
                                            <img src="/images/yello-star.jpg">
                                        </c:forEach>
                                        <c:forEach begin="${transcriptView.finalResult + 1}" end="5">
                                            <img src="/images/white-star.jpg">
                                        </c:forEach><br/>
                                        <c:choose>
                                            <c:when test="${transcriptView.finalResult == 5}">
                                                <span>优秀</span>
                                            </c:when>
                                            <c:when test="${transcriptView.finalResult == 4}">
                                                <span>良好</span>
                                            </c:when>
                                            <c:when test="${transcriptView.finalResult == 3}">
                                                <span>合格</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span>需努力</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:otherwise>
                                </c:choose>

                            </td>
                        </tr>
                    </table>
                    <div class="subtitle">
                        学习评价
                    </div>
                    <table class="transcript-table">
                        <tr>
                            <th width="10%">课时</th>
                            <th width="40%">课堂表现</th>
                            <th width="10%">课时</th>
                            <th width="40%">课堂表现</th>
                        </tr>
                        <c:forEach items="${lessonScoreList}" var="lessonScore" varStatus="i">
                            <c:if test="${i.index%2 == 0}">
                                <tr>
                            </c:if>

                            <td>
                                ${lessonScore.lessonName}
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${lessonScore.stuscore == null}">
                                        <c:forEach begin="0" end="2">
                                            <img src="/images/white-star.jpg">
                                        </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                        <c:forEach begin="1" end="${lessonScore.stuscore}">
                                            <img src="/images/yello-star.jpg">
                                        </c:forEach>
                                        <c:forEach begin="${lessonScore.stuscore +1 }" end="5">
                                            <img src="/images/white-star.jpg">
                                        </c:forEach><br />
                                        <c:choose>
                                            <c:when test="${lessonScore.stuscore == 5}">
                                                <span>优秀</span>
                                            </c:when>
                                            <c:when test="${lessonScore.stuscore == 4}">
                                                <span>良好</span>
                                            </c:when>
                                            <c:when test="${lessonScore.stuscore == 3}">
                                                <span>合格</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span>需努力</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <c:if test="${i.last && i.index % 2 == 0}">
                                <td colspan="2"></td>
                            </c:if>
                            <c:if test="${i.index % 2 == 1 || i.last}">
                                </tr>
                            </c:if>
                        </c:forEach>
                        <tr style="height: 206px;">
                            <th><span class="inline-block" style="width: 10px;">成果展示</span></th>
                            <td>
                                <c:if test="${transcriptView.resultsPicSrc!=null}">
                                    <img src="${transcriptView.resultsPicSrc}" class="statement-pic" width="345"/>
                                </c:if>

                            </td>
                            <th><span class="inline-block" style="width: 10px;">老师评语</span></th>
                            <td>
                    <span class="statement-container inline-block break-word">
                        ${fn:replace(transcriptView.teacherComments, newLineChar, "<br/>")}
                    </span>
                            </td>
                        </tr>
                        </tr>


                    </table>
                </div>

                <button id="print-button" class="button-k6kt fixed-size"
                        onclick="$('.transcript-container').printThis({
                                pageTitle:'${transcriptView.schoolName}成绩单'
                                });">打印
                </button>
            </div>


        </div>

    </div>
</div>

<!-- 页尾 -->
<%@ include file="../common_new/foot.jsp" %>
</body>
</html>