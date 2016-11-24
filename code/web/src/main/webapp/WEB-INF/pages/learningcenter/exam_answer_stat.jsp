<%@ page import="com.pojo.exercise.ExerciseItemStateDTO" %>
<%@ page import="com.pojo.exercise.ExerciseItemStateDTO.AnswerCount" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib prefix="role" uri="http://fulaan.userRole.com" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
    <title>复兰科技 K6KT-快乐课堂</title>
    <link rel="stylesheet" href="/static/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="/static/css/k6kt.css"/>
    <link rel="stylesheet" href="/static/css/dialog.css"/>
    <%--<link rel="stylesheet" type="text/css" href="/css/style.css" />--%>
    <link rel="stylesheet" type="text/css" href="/static/css/exercise/question.css">
    <link rel="stylesheet" type="text/css" href="/static/css/exercise/teacher_configuration.css">
    <script type="text/javascript" src="/static/js/jquery-1.11.1.min.js"></script>

    <script type="text/javascript">
        var currentPageID = 7;
    </script>

    <script type="text/javascript" src="/static/js/sharedpart.js"></script>
    <style type="text/css">
        html, body {
            background: #f9f9f9 !important;
        }
    </style>
</head>
<body style="background: #ffffff!important">
<!-- 页头 -->
<%@ include file="../common_new/head.jsp" %>
<div id="content_main_container">
    <div id="content_main" style="width: 1000px;margin: 0 auto;">

        <%@ include file="../common_new/col-left.jsp" %>
        <!--广告-->
        <%--<%@ include file="../common/right.jsp" %>--%>
        <!--/广告-->
        <div id="right-container" style="width: 770px;float: left;margin-left: 10px;">


            <link rel="stylesheet" href="/static/css/common/test-banner.css"/>

            <div class="modify_main_I">
                <c:choose>
                    <c:when test="${roles:isTeacher(sessionValue.userRole)}">
                        <span style="cursor: pointer;font-size: 14px;color: #000000" onclick="gotoPapers()" >全部试卷></span>
                    </c:when>
                </c:choose>
                <span>${name}</span>
            </div>

            <%--角色判断，不带课的校长不可以上传考卷， modify by miaoqiang--%>
            <c:choose>
                <c:when test="${roles:isTeacher(sessionValue.userRole)}">
                    <div class="test-banner" style="width:885px">
                        <div class="test-banner-container" style="margin:16px auto">
                            <table>
                                <tr>
                                    <td>
                                        <a href="/exam/index.do">
                                            <div class="test-item my-exam active">上传考卷</div>
                                        </a>
                                    </td>

                                        <%--<td>--%>
                                        <%--<a href="/flippedExams"><div class="test-item online-test">高考题库</div></a>--%>
                                        <%--</td>--%>
                                        <%--<td>--%>
                                        <%--<a href="/user/flippedMyQuestionBook"><div class="test-item ques-book">错题集</div></a>--%>
                                        <%--</td>--%>
                                </tr>
                            </table>
                        </div>
                    </div>
                </c:when>
            </c:choose>

            <div class="tab-col">
                <div class="tab-top clearfix" style="width: 885px;!important">
                    <ul>
                        <li class="cur" id=""><a href="javascript:;">考试题目</a></li>
                        <li  ><a href="/exam/studentlist.do?did=${docId}">考试学生</a></li>
                    </ul>
                </div>
            </div>
            <!-- 内容 -->
            <div class="modify_main" style="width: 885px;">
                <div>
                    <ul>
                        <li>
                            <%--<div class="modify_main_I">
                                <c:choose>
                                    <c:when test="${roles:isTeacher(sessionValue.userRole)}">
                                        <span style="cursor: pointer" onclick="gotoPapers()">全部试卷></span>
                                    </c:when>
                                </c:choose>
                                <span>${name}</span>
                            </div>--%>
                        </li>


                        <%
                            List<ExerciseItemStateDTO> list = (List<ExerciseItemStateDTO>) request.getAttribute("statList");
                            if (null != list && list.size() > 0) {
                        %>
                        <li>
                            <div class="modify_main_II">
                                <span class="modify_main_II_I">题号</span>
                                <span class="modify_main_II_I">正确率</span>
                                <span class="modify_main_II_II">统计</span>
                                <span class="modify_main_II_II">批改进度</span>
                            </div>
                        </li>

                        <%
                            for (ExerciseItemStateDTO stat : list) {
                                if (stat.getType() == 1) //选择题
                                {
                        %>

                        <!--========================================选择题=============================================-->
                        <li>
                            <div class="modify_main_III">
                                <div class="modify_main_III_XZ_I">
                                    <span class="modify_main_III_XZ_II"><%=stat.getTitleId() %></span><br>
                                    <span>选择题&nbsp;&nbsp;</span><span><%=stat.getScore() %>分</span>
                                </div>

                                <div class="modify_main_III_XZ_III">
                                    <%=stat.getRate() %>
                                </div>
                                <div class="modify_main_III_XZ_IIII">
                                    <div>
                                        <%
                                            for (ExerciseItemStateDTO.AnswerCount ac : stat.getAnswerCounts()) {
                                        %>
                                        <div style="position: relative;height: 30px;width:180px;overflow: hidden">
                                            <span class="modify_main_III_XZ_V"><%=ac.getAnswer() %></span> <span
                                                class="modify_main_III_XZ_VI"
                                                style="width: <%=stat.getCssWidth(ac.getAnswer()) %>px;"></span><span
                                                class="modify_main_III_XZ_VII"><%=ac.getCount() %>人</span>
                                        </div>
                                        <%
                                            }
                                        %>
                                    </div>
                                </div>
                                <div class="modify_main_III_XZ_SU">
                                    <span><%=stat.getAnswerCount()%></span><span>/</span><span><%=stat.getAnswerCount()%></span>
                                </div>
                                <div class="modify_main_III_XZ_CK">
                                    <a href="/exam/item/answer.do?docId=<%=stat.getDocumentId() %>&titleId=<%=stat.getId() %>"
                                       style="background-color:#60B760;padding:5px 20px;;position: relative;display: inline;color: #ffffff;border-radius: 3px;cursor: pointer">查看</a>
                                </div>
                            </div>
                        </li>

                        <%
                            }
                            if (stat.getType() == 3) //判断题
                            {
                        %>

                        <!--========================================判断题======================================================-->
                        <li>
                            <div class="modify_main_III">

                                <div class="modify_main_III_XZ_I">
                                    <span class="modify_main_III_XZ_II"><%=stat.getTitleId() %></span><br>
                                    <span>判断题&nbsp;&nbsp;</span><span><%=stat.getScore()%>分</span>
                                </div>

                                <div class="modify_main_III_XZ_III">
                                    <%=stat.getRate() %>
                                </div>
                                <div class="modify_main_III_PD_I">
                                    <div>
                                        <span class="modify_main_III_PD_II">选对&nbsp;&nbsp;</span> <span
                                            class="modify_main_III_PD_III"
                                            style="width: <%=stat.getCssWidth("1") %>px;"></span><span
                                            class="modify_main_III_XZ_VII"><%=stat.getAnswerMap().get("1") %>人</span><br>
                                        <span class="modify_main_III_PD_II">选错&nbsp;&nbsp;</span> <span
                                            class="modify_main_III_PD_IIII"
                                            style="width: <%=stat.getCssWidth("0") %>px"></span><span
                                            class="modify_main_III_XZ_VII"><%=stat.getAnswerMap().get("0") %>人</span><br>
                                    </div>
                                </div>
                                <div class="modify_main_III_XZ_SU">
                                    <span><%=stat.getAnswerCount()%></span><span>/</span><span><%=stat.getAnswerCount()%></span>
                                </div>
                                <div class="modify_main_III_XZ_CK">
                                    <a href="/exam/item/answer.do?docId=<%=stat.getDocumentId() %>&titleId=<%=stat.getId() %>"
                                       style="background-color:#60B760;padding:5px 20px;;position: relative;display: inline;color: #ffffff;border-radius: 3px;cursor: pointer">查看</a>
                                </div>
                            </div>
                        </li>

                        <%
                            }


                            if (stat.getType() == 4) ////填空
                            {
                        %>
                        <!--========================================填空题=============================================-->
                        <li>
                            <div class="modify_main_III">
                                <div class="modify_main_III_XZ_I">
                                    <span class="modify_main_III_XZ_II"><%=stat.getTitleId() %></span><br>
                                    <span>填空题&nbsp;&nbsp;</span><span><%=stat.getScore() %>分</span>
                                </div>

                                <div class="modify_main_III_XZ_III">
                                    <%=stat.getRate() %>
                                </div>
                                <div class="modify_main_TK">
                                    <div class="modify_main_TK_I">
                                        <span class="modify_main_III_TK_II"> <%=stat.getScore() %>分</span> <span
                                            class="modify_main_III_TK_V_I"
                                            style="width: <%=stat.getCssWidth(String.valueOf(stat.getScore())) %>px;"></span><span
                                            class="modify_main_III_TK_V_V"><%=stat.getAnswerMap().get(String.valueOf(stat.getScore())) %>人</span><br>
                                        <span class="modify_main_III_TK_II">0分</span> <span
                                            class="modify_main_III_TK_V_II"
                                            style="width: <%=stat.getCssWidth(String.valueOf(0.0)) %>px;"></span><span
                                            class="modify_main_III_TK_V_V"><%=stat.getAnswerMap().get("0.0") %>人</span><br>

                                    </div>
                                </div>
                                <div class="modify_main_III_XZ_SU">
                                    <span><%=stat.getAnswerCount()%></span><span>/</span><span><%=stat.getAnswerCount()%></span>
                                </div>
                                <div class="modify_main_III_XZ_CK">
                                    <a href="/exam/item/answer.do?docId=<%=stat.getDocumentId() %>&titleId=<%=stat.getId() %>"
                                       style="background-color:#60B760;padding:5px 20px;;position: relative;display: inline;color: #ffffff;border-radius: 3px;cursor: pointer">查看</a>
                                </div>
                            </div>
                        </li>

                        <%
                            }
                            if (stat.getType() == 5) ////主观题
                            {
                        %>


                        <!--========================================主观题=============================================-->
                        <li>
                            <div class="modify_main_III">
                                <div class="modify_main_III_XZ_I">
                                    <span class="modify_main_III_XZ_II"><%=stat.getTitleId() %></span><br>
                                    <span>主观题&nbsp;&nbsp;</span><span><%=stat.getScore() %>分</span>
                                </div>

                                <div class="modify_main_III_XZ_III">

                                </div>
                                <div class="modify_main_TK">
                                    <div>
                                        <%
                                            for (Map.Entry<String, Integer> entry : stat.getAnswerMap().entrySet()) {
                                        %>
                                        <div style="position: relative;height: 30px;width:180px;overflow: hidden">
                                            <span class="modify_main_III_XZ_V"><%=entry.getKey() %></span> <span
                                                class="modify_main_III_XZ_VI"
                                                style="width: <%=stat.getCssWidth(entry.getKey()) %>px;"></span><span
                                                class="modify_main_III_XZ_VII"><%=entry.getValue() %>人</span>
                                        </div>
                                        <%
                                            }
                                        %>
                                    </div>
                                </div>
                                <div class="modify_main_III_XZ_SU">
                                    <span><%=stat.getScoreCount() %>/</span><span><%=stat.getAnswerCount()%></span>
                                </div>
                                <div class="modify_main_III_XZ_CK">
                                    <a href="/exam/item/answer.do?docId=<%=stat.getDocumentId() %>&titleId=<%=stat.getId() %>"
                                       style="background-color:#60B760;padding:5px 20px;;position: relative;display: inline;color: #ffffff;border-radius: 3px;cursor: pointer">进入批改</a>
                                </div>
                            </div>
                        </li>

                        <%
                                }
                            }
                        %>

                        <%
                            }

                        %>

                    </ul>
                </div>
                <div style="background: #ffffff;height: 100px;width:100%"></div>
            </div>

            <!-- 页尾 -->

        </div>

    </div>
    <div style="clear: both"></div>
    <%@ include file="../common_new/foot.jsp" %>
</div>
</body>

<script type="text/javascript">
    function gotoPapers() {
        window.location.href = "/exam/index.do";
    }
</script>
</html>
