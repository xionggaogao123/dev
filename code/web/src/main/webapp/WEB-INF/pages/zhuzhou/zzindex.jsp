<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <!-- Basic Page Needs-->
    <meta charset="utf-8">
    <title>3+3走班</title>
    <!-- css files -->
    <!-- Normalize default styles -->
    <link href="/static_new/css/reset.css" rel="stylesheet"/>
    <link href="/static_new/css/zouban/zoubannew.css" rel="stylesheet"/>
    <link href="/static_new/js/modules/core/0.1.0/layer/skin/layer.css" rel="stylesheet">
    <script type="text/javascript" src="/static/js/jquery-2.1.1.min.js"></script>
</head>
<body>
<!--#head-->
<%@ include file="../common_new/head.jsp" %>
<!--#content-->
<div id="content" class="clearfix">
    <!--.col-left-->
    <%@ include file="../common_new/col-left.jsp" %>
    <!--/.col-left-->

    <!--.col-right-->
    <div class="col-right">

        <!--.tab-col右侧-->
        <div class="tab-col">

            <div class="tab-head clearfix">
                <ul>
                    <li class="cur"><a href="javascript:;">走班管理</a><em></em></li>
                </ul>
            </div>

            <div class="tab-main">
                <!--=====================================走班教务管理start=============================-->
                <div id="tab-ZNGL" class="tab-ZNGL">
                    <div class="ZNGL-top">
                        <%--学期--%>
                        <select id="termListCtx">
                            <option value="20152016">2015~2016学年</option>
                            <option value="20162017" selected>2016~2017学年</option>
                        </select>
                        <%--年级--%>
                        <select id="gradeListCtx">
                            <option value="1" selected>高一年级</option>
                            <option value="2">高二年级</option>
                            <option value="3">高三年级</option>
                        </select>
                    </div>
                    <div class="ZNGL-main">
                        <div class="ZNGL-info">
                            <div class="ZNBL-info-n">
                                <div class="ZNBL-num-I ZNBL-S">
                                    <span id="step1" class="h-cursor" onclick="window.location.href='/zouban/zhuzhou/subjectConf.do'">课程设置</span><img src="/static_new/images/ZNBL-RT.png">
                                </div>
                                <div class="ZNBL-num-II ZNBL-S">
                                    <span id="step2" class="h-cursor" onclick="location.href='/zouban/zhuzhou/xuanke.do'">选课</span><img src="/static_new/images/ZNBL-RT.png">
                                </div>
                                <div class="ZNBL-num-III ZNBL-S">
                                    <span id="step5" class="h-cursor" onclick="location.href='/zouban/zhuzhou/timetable.do'">查看课表</span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <!--================================走班教务管理end==============================-->

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
<div class="bg"></div>


<!-- Javascript Files -->
<script src="/static_new/js/sea.js"></script>
<script src="/static_new/js/modules/core/0.1.0/config.js"></script>
<script>
    seajs.use('/static_new/js/modules/zhuzhou/index.js', function (zhuzhouIndex) {
        zhuzhouIndex.init();
    });
</script>

</body>
</html>