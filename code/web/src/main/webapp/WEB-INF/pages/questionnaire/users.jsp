<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

    <link rel="stylesheet" type="text/css" href="/static/css/flippedStatistics.css" />
    <link href="/static_new/css/reset.css" rel="stylesheet" />
    <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery.min.js?v=1"></script>

    <style>
        #fc_video_statistics{background:white;}
        .header_foot{height:20px;background:#ECF5FF;border-left:1px solid #D0D0D0;border-right:1px solid #D0D0D0;}
        .ypheader >div{/* padding:10px 5px; */border:1px solid #D0D0D0;}

        #fc_video_see_list span.I{width:200px;}
        #fc_video_see_list span.II{width:200px;}
        #fc_video_see_list span.III{width:150px;}
        #fc_video_see_list span.IV{width:100px;}
        #fc_video_see_list span.V{width:100px;}
        #fc_video_see_list span.VI{width:80px;}


        #fc_video_see_list .header span.V{width:100px;}
        #fc_video_see_list .header span.VI{width:60px;}

        #fc_ques_list ul li{cursor:pointer;}
        #fc_ques_list ul span.I{text-align:left;word-break:break-word;
            word-wrap:break-word;}
        .fc_statistics_list span.I{width:250px;padding-left:10px;}
        .fc_statistics_list span.II{width:150px;}
        .fc_statistics_list span.III{width:150px;}
        .fc_statistics_list span.IV{width:150px;}
        .fc_statistics_list span.V{width:150px;}

        .fc_statistics_list li span.I{width:252px;padding-left:10px;}
        .fc_statistics_list li span.II{width:158px;}
        .fc_statistics_list li span.III{width:159px;}
        .fc_statistics_list li span.IV{width:159px;}
        .fc_statistics_list li span.V{width:150px;}
        .new-page-links{
            position: relative;
            left: -220px;
        }
        .red{
            color:red;
        }
        .blue{
            color:blue!important;
        }
        .letter{
            float: right;
            margin-top: 4px;
            margin-right: 20px;
            width: 120px;
        }
    </style>
    <title>调查问卷统计</title>
</head>
<body id="${param.id}">

<%@ include file="../common_new/head.jsp" %>
<div class="ypheader center forIEFloatP" id="hwtj">
    <div>
        <h4 style="display: inline-block;">调查问卷统计</h4>
        <c:if test="${param.state == 1}">
        <button class="letter" onclick="userjs.sendLetters('${param.id}')">私信通知</button>
        </c:if>
    </div>
</div>

<div class="fc_statistics_list ypheader center" id="hwtj1">
    <div class="header">
    <span class="I">姓名</span>|
    <span class="II">角色</span>|
    <span class="III">班级</span>|
    <span class="IV">状态</span>|
    </div>
    <ul  id="submitInfo">
        <%--<li class="content">--%>
            <%--<span class="I">张三</span>--%>
            <%--<span class="II">学生</span>--%>
            <%--<span class="III">二(3)班</span>--%>
            <%--<span class="IV">已提交</span>--%>
        <%--</li>--%>
    </ul>
</div>
<div class="new-page-links"></div>


<div id="fc_video_see_list" class="fc_statistics_list ypheader center">

</div>




<div id = "exercise_statistics" style="width: 900px;height: auto;border:1px solid #DDDDDD;overflow-x:hidden;position: relative;margin: 0 auto;background-color: #ffffff">



</div>


<%@ include file="../common_new/foot.jsp" %>

<script id="submitInfoTmpl" type="text/template">
    {{ for(var i in it) { }}
    <li class="content {{? it[i].submit==0}}red{{??}}blue{{?}}">
    <span class="I">{{=it[i].name}}</span>
    <span class="II">{{=it[i].role}}</span>
    <span class="III">{{=it[i].cName}}</span>
    <span class="IV">{{? it[i].submit==0}}未{{??}}已{{?}}提交</span>
    </li>
    {{ } }}
</script>

<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script src="/static_new/js/sea.js"></script>
<!-- Custom js -->
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    var userjs;
    seajs.use('/static_new/js/modules/questionnaire/0.1.0/users.js', function (statPage) {
        userjs = statPage;
        statPage.init();
    });
</script>
</body>
</html>