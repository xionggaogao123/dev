<%--
  Created by IntelliJ IDEA.
  User: fl
  Date: 2016/5/16
  Time: 12:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<html>
<head>
    <title>拓展课</title>
    <meta name="description" content="">
    <meta name="author" content="" />
    <meta name="copyright" content="" />
    <meta name="keywords" content="">
    <meta name="viewport" content="width=device-width,initial-scale=1, maximum-scale=1">
    <link href="/static/css/homepage.css" type="text/css" rel="stylesheet">
    <link href="/static_new/css/reset.css" rel="stylesheet" />
    <link href="/static_new/css/interestclass/attendance.css" rel="stylesheet" />
    <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery.min.js?v=1"></script>
</head>
<body cid="${param.cid}">

<!--=================================引入头部============================================-->
<%@ include file="../common_new/head.jsp" %>
<div id="content" class="clearfix classmasterlist">
    <p>
        <select class="s1" id="terms">
            <%--<option>2015-2016学年第一学期</option>--%>
        </select>
        <script id="termsTmpl" type="text/template">
            {{ for(var i=it.length-1;i>=0;i--) { }}
            <option value="{{=it[i].value}}">{{=it[i].name}}</option>
            {{ } }}
        </script>
        <span id="cnm"></span>
    </p>
    <table class="tableleft">
        <thead>
            <tr>
                <th>学生姓名</th>
                <th>课程名称</th>
            </tr>
        </thead>
        <tbody id="stus">
            <%--<tr>--%>
                <%--<td>张三</td>--%>
                <%--<td>篮球</td>--%>
            <%--</tr>--%>
        </tbody>
        <script id="stusTmpl" type="text/template">
            {{ for(var i in it) { }}
            <tr>
                <td>{{=it[i].stuNm}}</td>
                <td><div class="div-classnm" title="{{=it[i].classNm}}">{{=it[i].classNm}}</div></td>
            </tr>
            {{ } }}
        </script>
    </table>
    <div class="divright">
        <table class="tableright">
            <thead>
                <tr id="weeks">
                    <th><div class="width172">第1周</div></th>
                </tr>
                <script id="weeksTmpl" type="text/template">
                    {{ for(var i=it;i>=1;i--) { }}
                    <th><div class="width172">第{{=i}}周</div></th>
                    {{ } }}
                </script>
            </thead>
            <tbody id="at">
                <%--<tr>--%>
                    <%--<td>运球(2016/04/07)&nbsp;13:10</td>--%>
                <%--</tr>--%>
            </tbody>
            <script id="atTmpl" type="text/template">
                {{ for(var i in it) { }}
                    <tr>
                    {{var weeks = it[i].weeks;}}
                    {{for(var j=weeks.length-1;j>=0;j--){ }}
                        <td><div class="div-classnm" title="{{=weeks[j]}}" {{if(checkAttendance(weeks[j])){ }} style="color:red" {{ } }}>{{=weeks[j]}}</div></td>
                    {{ } }}
                    </tr>
                {{ } }}
            </script>
        </table>
    </div>
</div>
<!--/#content-->



<div style="clear: both"></div>
<%@ include file="../common_new/foot.jsp"%>



<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script src="/static_new/js/sea.js"></script>
<!-- Custom js -->
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('/static_new/js/modules/interestclass/0.1.0/classMasterCheckAttendance.js');
</script>
</body>
</html>
