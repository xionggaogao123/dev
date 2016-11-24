<%@ page import="com.pojo.exercise.ExerciseItemStateDTO" %>
<%@ page import="com.pojo.exercise.ExerciseItemStateDTO.AnswerCount" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
    <title>批改习题</title>
    <link rel="stylesheet" href="/static/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="/static/css/k6kt.css"/>
    <link rel="stylesheet" href="/static/css/dialog.css"/>
    <%--<link rel="stylesheet" type="text/css" href="/css/style.css" />--%>
    <link href="//netdna.bootstrapcdn.com/font-awesome/4.0.3/css/font-awesome.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="/static/css/exercise/question.css">
    <link rel="stylesheet" type="text/css" href="/static/css/exercise/teacher_configuration.css">
    <script type="text/javascript" src="/static/js/jquery-1.11.1.min.js"></script>

    <script type="text/javascript">
        var currentPageID = 7   ;
    </script>

    <script type="text/javascript" src="/static/js/sharedpart.js"></script>
    <style type="text/css">
        html, body {
            background: #f9f9f9 !important;
        }
    </style>
</head>
<body>
<!-- 页头 -->
<%@ include file="../common_new/head.jsp" %>
<div id="content_main_container" >
<div id="content_main" style="width: 1000px;margin: 0 auto;">

 <%@ include file="../common_new/col-left.jsp" %>


<div id="right-container" style="width: 770px;float: left;margin-left: 10px;">


<link rel="stylesheet" href="/static/css/common/test-banner.css"/>
<%--<div class="test-banner" style="width:885px">--%>
    <%--<div class="test-banner-container" style="margin:16px auto">--%>
        <%--<table>--%>
            <%--<tr>--%>

                <%--<td>--%>
                    <%--<a href="/exam/index.do"><div class="test-item my-exam active" >上传考卷</div></a>--%>
                <%--</td>--%>

                <%--&lt;%&ndash;<td>&ndash;%&gt;--%>
                    <%--&lt;%&ndash;<a href="/flippedExams"><div class="test-item online-test">高考题库</div></a>&ndash;%&gt;--%>
                <%--&lt;%&ndash;</td>&ndash;%&gt;--%>
                <%--&lt;%&ndash;<td>&ndash;%&gt;--%>
                    <%--&lt;%&ndash;<a href="/user/flippedMyQuestionBook"><div class="test-item ques-book">错题集</div></a>&ndash;%&gt;--%>
                <%--&lt;%&ndash;</td>&ndash;%&gt;--%>
            <%--</tr>--%>
        <%--</table>--%>
    <%--</div>--%>
<%--</div>--%>

<!-- 内容 -->
<div class="modify_main" style="width: 885px;">
<div>
<ul>
<li>
    <div class="modify_main_I">
        <%--<span style="cursor: pointer" onclick="gotoPapers()">全部试卷></span>--%>
        <%--<span>${name}</span>--%>
            <span>微课进阶练习统计</span>
    </div>
</li>


<%
	List<ExerciseItemStateDTO> list =(List<ExerciseItemStateDTO>)request.getAttribute("statList");
    if(null!=list && list.size()>0)
    {
%>


<li>
    <div class="modify_main_II">
        <span class="modify_main_II_I" >题号</span>
        <span class="modify_main_II_I">正确率</span>
        <span class="modify_main_II_II">统计</span>
        <span class="modify_main_II_II">批改进度</span>
    </div>
</li>

<%
	for(ExerciseItemStateDTO stat:list)
    {
        if(stat.getType()==1) //选择题
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
            <br><span style="font-size: small">正确：<%=stat.getRightCount()%>人，错误：<%=stat.getWrongCount()%>人</span>
        </div>
        <div class="modify_main_III_XZ_IIII">
            <div>
                <%
                    for(ExerciseItemStateDTO.AnswerCount ac:stat.getAnswerCounts())
                    {
                %>
                 <div style="position: relative;height: 30px;width:180px;overflow: hidden">
                  <span class="modify_main_III_XZ_V"><%=ac.getAnswer() %></span> <span class="modify_main_III_XZ_VI" style="width: <%=stat.getCssWidth(ac.getAnswer()) %>px;"></span><span class="modify_main_III_XZ_VII"><%=ac.getCount() %>人</span>
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
            <a href="/exam/item/answer.do?docId=<%=stat.getDocumentId() %>&titleId=<%=stat.getId() %>&classId=${param.classId}" style="background-color:#60B760;padding:5px 20px;;position: relative;display: inline;color: #ffffff;border-radius: 3px;cursor: pointer" target="_blank">查看</a>
        </div>
    </div>
</li>

<%
    }
    if(stat.getType()==3) //判断题
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
            <br><span style="font-size: small">正确：<%=stat.getRightCount()%>人，错误：<%=stat.getWrongCount()%>人</span>
        </div>
        <div class="modify_main_III_PD_I">
            <div>
                <span class="modify_main_III_PD_II">选对&nbsp;&nbsp;</span> <span class="modify_main_III_PD_III" style="width: <%=stat.getCssWidthForPanDuan("1") %>px;"></span><span class="modify_main_III_XZ_VII"><%=stat.getRightCount() %>人</span><br>
                <span class="modify_main_III_PD_II">选错&nbsp;&nbsp;</span> <span class="modify_main_III_PD_IIII" style="width: <%=stat.getCssWidthForPanDuan("0") %>px"></span><span class="modify_main_III_XZ_VII"><%=stat.getWrongCount() %>人</span><br>
            </div>
        </div>
        <div class="modify_main_III_XZ_SU">
            <span><%=stat.getAnswerCount()%></span><span>/</span><span><%=stat.getAnswerCount()%></span>
        </div>
        <div class="modify_main_III_XZ_CK">
            <a href="/exam/item/answer.do?docId=<%=stat.getDocumentId() %>&titleId=<%=stat.getId() %>&classId=${param.classId}" style="background-color:#60B760;padding:5px 20px;;position: relative;display: inline;color: #ffffff;border-radius: 3px;cursor: pointer" target="_blank">查看</a>
        </div>
    </div>
</li>

<%
    }


    if(stat.getType()==4) ////填空
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
            <br><span style="font-size: small">正确：<%=stat.getRightCount()%>人，错误：<%=stat.getWrongCount()%>人</span>
        </div>
        <div class="modify_main_TK">
            <div class="modify_main_TK_I">
                <span class="modify_main_III_TK_II"> <%=stat.getScore() %>分</span> <span class="modify_main_III_TK_V_I" style="width: <%=stat.getCssWidth(String.valueOf(stat.getScore())) %>px;"></span><span class="modify_main_III_TK_V_V" ><%=stat.getAnswerMap().get(String.valueOf(stat.getScore())) %>人</span><br>
                <span class="modify_main_III_TK_II">0分</span> <span class="modify_main_III_TK_V_II" style="width: <%=stat.getCssWidth(String.valueOf(0.0)) %>px;"></span><span class="modify_main_III_TK_V_V"><%=stat.getAnswerMap().get("0.0") %>人</span><br>

            </div>
        </div>
        <div class="modify_main_III_XZ_SU">
            <span><%=stat.getAnswerCount()%></span><span>/</span><span><%=stat.getAnswerCount()%></span>
        </div>
        <div class="modify_main_III_XZ_CK">
            <a href="/exam/item/answer.do?docId=<%=stat.getDocumentId() %>&titleId=<%=stat.getId() %>&classId=${param.classId}" style="background-color:#60B760;padding:5px 20px;;position: relative;display: inline;color: #ffffff;border-radius: 3px;cursor: pointer" target="_blank">查看</a>
        </div>
    </div>
</li>

<%
    }
    if(stat.getType()==5) ////主观题
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
                    for(Map.Entry<String, Integer> entry:stat.getAnswerMap().entrySet())
                    {
                %>
                 <div style="position: relative;height: 30px;width:180px;overflow: hidden">
                  <span class="modify_main_III_XZ_V"><%=entry.getKey() %></span> <span class="modify_main_III_XZ_VI" style="width: <%=stat.getCssWidth(entry.getKey()) %>px;"></span><span class="modify_main_III_XZ_VII"><%=entry.getValue() %>人</span>
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
             <a href="/exam/item/answer.do?docId=<%=stat.getDocumentId() %>&titleId=<%=stat.getId() %>&classId=${param.classId}" style="background-color:#60B760;padding:5px 20px;;position: relative;display: inline;color: #ffffff;border-radius: 3px;cursor: pointer"  target="_blank">进入批改</a>
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
    function gotoPapers(){
        window.location.href = "/exam/index.do";
    }
</script>
</html>
