<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib prefix="role" uri="http://fulaan.userRole.com" %>
<html>
<head>
    <meta charset="utf-8">
    <link rel="dns-prefetch" href="//source.ycode.cn"/>
    <title>走班选课</title>
    <meta name="description" content="">
    <meta name="author" content=""/>
    <meta name="copyright" content=""/>
    <meta name="keywords" content="">
    <meta name="viewport" content="width=device-width,initial-scale=1, maximum-scale=1">
    <!-- css files -->
    <!-- Normalize default styles -->
    <link href="/static/css/homepage.css?v=1" rel="stylesheet"/>
    <link href="/static_new/css/reset.css?v=1" rel="stylesheet"/>

</head>
<body>
<!--#head-->
<%@ include file="../../common_new/head.jsp" %>
<!--/#head-->
<!--#content-->
<div id="content" class="clearfix">
    <!--.col-left-->
    <%@ include file="../../common_new/col-left.jsp" %>
    <!--/.col-left-->
    <div class="col-right">
        <input type="hidden" value="0" id="result">
        <!--.tab-col-->
        <div class="cont-right">
            <ul class="right-title">
                <li class="class-list">课表</li>
                <li class="class-select cont-style" style="margin-left:2px;">选课</li>
                <%--<i> < 返回教务管理首页</i>--%>
            </ul>

            <div class="right-main1">
                <i class="no-class">本学期课表未发布</i>
            </div>
            <div class="right-main2">
            </div>
            <script type="application/template" id="tempJs">
                <span class="studye">{{=it.data.term}}</span>
                <div style="margin-left:35px;">选课开放时间： <em>{{=it.data.conf.startDate}}</em> ~ <em>{{=it.data.conf.endDate}}</em></div>
                {{?it.data.adv.length>0}}
                <div class="class-selected">
                    <span>已选课程</span>
                    <div class="lesson-ted" style="margin-top:12px;">
                        <i>等级考试课程:</i>
                        <ul class="ted-ul">
                            {{~it.data.adv:value:index}}
                            <li>{{=value.value}}</li>
                            {{~}}
                        </ul>
                    </div>
                    <div class="lesson-ted">
                        <i>合格考试课程:</i>
                        <ul class="ted-ul">
                            {{~it.data.sim:value:index}}
                            <li>{{=value.value}}</li>
                            {{~}}
                        </ul>
                    </div>
                </div>
                {{?}}
                {{?it.data.result==2||it.data.result=="2"}}
               <%-- <div class="select-des">
                    <i>选{{=it.data.conf.advanceCount}}门等级考科目</i>
                    <i style="float:right;">选{{=it.data.conf.simpleCount}}门合格考科目</i>
                </div>--%>
                <div class="select-list">
                    <table>
                        <tr class="cow1">
                            <td class="cell1">科目名称</td>
                            <td class="cell2">等级考</td>
                            <td class="cell3">合格考</td>
                            <td class="cell4">说明</td>
                        </tr>
                        {{~it.data.conf.subConfList:value:index}}
                        <tr class="cow2" sid="{{=value.subjectId}}">
                            <td class="cell1">{{=value.subjectName}}</td>
                            <td class="cell2">
                                {{?$.inArray(value.subjectName,it.advChoose)>-1}}
                                <div class="select-me adv selected">我选</div>
                                {{??}}
                                <div class="select-me adv">我选</div>
                                {{?}}
                            </td>
                            <td class="cell3">
                                {{?$.inArray(value.subjectName,it.simChoose)>-1}}
                                <div class="select-me sim selected">我选</div>
                                {{??}}
                                    {{?value.simpleTime==0}}
                                    <div style="width: 38px;margin: auto;">-----</div>
                                    {{??}}
                                    <div class="select-me sim">我选</div>
                                    {{?}}
                                {{?}}
                                </td>
                            <td class="cell4">{{=value.explain}}</td>
                        </tr>
                        {{~}}
                    </table>
                </div>
                <input type="submit" class="select-sub" value="提交">
                {{?}}
            </script>
        </div>
        <!--/.tab-col-->
    </div>
    <!--/.col-right-->

</div>
<div class="sub-div">
    <div class="sub-wind-bg"></div>
    <div class="alert-wind">
        <div class="alert-top">
            <i >提示</i>
            <em class="win-cha" style="display:inline-block;">×</em>
        </div>
        <span>确认提交选课吗？</span>
        <div style="width:150px;height:30px;margin:35px auto;">
            <div class="sub-cof">确定</div>
            <div class="sub-can">取消</div>
        </div>
    </div>
</div>
<!--#foot-->
<%@ include file="../../common_new/foot.jsp" %>
<!--#foot-->
<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script src="/static_new/js/sea.js?v=1"></script>
<!-- Custom js -->
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('xuanke', function(xuanke) {
        xuanke.init();
    });
</script>
</body>
</html>