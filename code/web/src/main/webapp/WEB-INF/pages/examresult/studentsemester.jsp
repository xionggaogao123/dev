<%--
  Created by IntelliJ IDEA.
  User: qinbo
  Date: 15/6/22
  Time: 下午3:41
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<html>
<head>
    <title>成绩分析</title>
    <meta name="description" content="">
    <meta name="author" content="" />
    <meta name="copyright" content="" />
    <meta name="keywords" content="">
    <meta name="viewport" content="width=device-width,initial-scale=1, maximum-scale=1">
    <link href="/static_new/css/reset.css" rel="stylesheet" />
    <link href="/static_new/css/chengji.css?v=2015041602" rel="stylesheet" />
    <link href="/static_new/css/examine.css?v=2015041602" rel="stylesheet" />
    <script type="text/javascript" src="/static/js/jquery-2.1.1.min.js"></script>
</head>
<body>

<%@ include file="../common_new/head.jsp" %>
<div id="content" class="clearfix">
    <%@ include file="../common_new/col-left.jsp" %>
    <!--.col-right-->
    <div class="col-right">

        <%--<!--.banner-info-->--%>
        <%--<img src="http://placehold.it/770x100" class="banner-info" />--%>
        <%--<!--/.banner-info-->--%>

        <!--.tab-col-->
        <div class="tab-col">

            <div class="tab-head clearfix">
                <ul>
                    <li ><a href="/score/student.do">首页</a></li>
                    <li class="cur"><a href="#">学期成绩</a></li>
                    <li><a href="/score/student/history.do">历史成绩</a></li>
                </ul>
            </div>

            <div class="tab-main">

                <!--.tiaojian-col-->
                <%--<div class="tiaojian-col clearfix">--%>
                    <%--<div class="tianjian-btn">--%>
                        <%--<ul>--%>
                            <%--<li class="cur" id="reality">实际分</li>--%>
                            <%--<li class="cu" id="hundred">百分制</li>--%>
                        <%--</ul>--%>
                        <%--<select>--%>
                            <%--<option>导出PDF</option>--%>
                        <%--</select>--%>
                    <%--</div>--%>

                <%--</div>--%>
                <!--/.tiaojian-col-->

                <!--.charts-col-->
                <div class="charts-col">

                    <h3 id="stuSemTitle"></h3>
                    <div class="charts-list" >
                        <dl>
                            <dt><em>1</em>成绩统计</dt>
                            <dd class="clearfix">

                                <div class="student_result_rol">
                                    <span id="cjfbId"></span>
                                </div>

                                <div class="student_result_rol">
                                    <span id="ccfbId"></span>
                                </div>
                            </dd>
                        </dl>
                        <dl>
                            <dt><em>2</em>个人成绩</dt>
                            <dd id="cjdbId" class="charts-height"></dd>
                        </dl>

                        <dl>
                            <dt><em>3</em>成绩表</dt>
                            <dd class="std-list">
                                <h4 id="stuScoreTitle"></h4>
                                <table width="100%">
                                    <thead>
                                    <th class="25%"><em>学科</em></th>
                                    <th class="25%"><em id="usualScore">平时成绩</em></th>
                                    <th class="25%"><em id="midScore">期中成绩</em></th>
                                    <th><em id="finalScore">期末成绩</em></th>
                                    </thead>
                                    <tbody id="scoreList">



                                    </tbody>
                                </table>
                            </dd>
                        </dl>
                    </div>
                </div>
                <!--/.charts-col-->

            </div>

        </div>
        <!--/.tab-col-->

    </div>
    <!--/.col-right-->

</div>
<!--/#content-->



<%@ include file="../common_new/foot.jsp" %>

<script id="scoreListTmpl" type="text/template">
    {{ for(var i = 0, l = it.subNameList.length; i < l; i++) {  }}
    <tr>
        <td>{{=it.subNameList[i]}}</td>
        <td>{{=it.aveScoreList[i]}}</td>
        {{? it.midScoreList[i] != null}}
            <td>{{=it.midScoreList[i]}}</td>
        {{??}}
            <td>--</td>
        {{?}}
        {{? it.finScoreList[i] != null}}
            <td>{{=it.finScoreList[i]}}</td>
        {{??}}
            <td>--</td>
        {{?}}
    </tr>
    {{ } }}
</script>

<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script src="/static_new/js/sea.js"></script>
<!-- Custom js -->
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('/static_new/js/modules/chengji/0.1.0/student_semester');
</script>
</body>
</html>

