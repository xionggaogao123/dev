<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib prefix="role" uri="http://fulaan.userRole.com" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
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
    <!-- jquery artZoom4Liaoba styles -->
    <!-- Custom styles -->
    <link href="/static_new/css/zouban/checklist.css?v=2015041602" rel="stylesheet"/>

    <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery.min.js?v=1"></script>
</head>
<body>
<!--#head-->
<%@ include file="../../common_new/head.jsp" %>
<!--/#head-->
<!--#content-->
<div id="content" class="clearfix">
    <input id="gradeId" type="hidden" value="${gradeId}">
    <input id="publish" type="hidden" value="${publish}">
    <input id="termShow" type="hidden" value="${term}">
    <input id="yearShow" type="hidden" value="${year}">
    <input type="hidden" id="mode" value="${mode}">
    <!--.col-left-->
    <%@ include file="../../common_new/col-left.jsp" %>
    <!--/.col-left-->
    <!--.col-right-->
    <!--广告-->
<%--    <c:choose>
        <c:when test="${roles:isStudentOrParent(sessionValue.userRole)}">
            <jsp:include page="/WEB-INF/pages/common/right_2.jsp"></jsp:include>
        </c:when>
        <c:otherwise>
            <jsp:include page="/WEB-INF/pages/common/right.jsp"></jsp:include>
        </c:otherwise>
    </c:choose>--%>
    <!--广告-->
    <div class="col-right">
        <!--.tab-col-->
        <div class="cont-right">
            <ul class="right-title">
                <li class="main1-li cur">
                    发布课表
                </li>

                <i id="publishShow">课表已发布</i><a class="backUrl" href="javascript:;"<%--href="../paike/index.do?version=58"--%>><span >< 返回教务管理首页</span></a>
                <button class="publish-table">发布课表</button>
            </ul>

            <div class="right-main2">

                <div class="main2-1">${year}</div>
                <div class="main2-2">
						<span><i>年级:${gradeName}</i>
						</span>
						<span><i>班级</i>
							<select id="classShow2">
                            </select>
                            <script type="application/template" id="classTempJs2">
                                {{~it.data:value:index}}
                                <option value="{{=value.classId}}">{{=value.className}}</option>
                                {{~}}
                            </script>
                        </span>

                    <div class="main2-btn1" style="margin-left:16px;">筛选</div>
                    <div class="main2-btn1 exportTable" style="margin-left:72px;">导出</div>
                    <%--<div class="main2-btn1" style="margin-left:16px;">明细</div>--%>
                </div>
                <table class="main2-3" id="classTableShow">
                </table>
                <script type="application/template" id="classTableTempJs">
                    <tr class="row1">
                        <td class="col1">节次/时间</td>
                        {{~it.data.conf.classDays:value:index}}
                        <td class="col">星期{{?value==1}}一
                            {{??value==2}}二
                            {{??value==3}}三
                            {{??value==4}}四
                            {{??value==5}}五
                            {{??value==6}}六
                            {{??value==7}}日
                            {{?}}</td>
                        {{~}}
                    </tr>
                    {{~it.data.conf.classTime:value:index}}
                    <tr class="row">
                        <td class="col1">
                            第{{=index+1}}节
                            <br />{{=it.data.conf.classTime[index]}}
                        </td>
                        {{~it.data.conf.classDays:value1:index1}}
                        <td class="col col-class" style="cursor: pointer">
                            {{~it.data.course:value2:index2}}
                                {{?value2.xIndex==value1 && value2.yIndex==(index+1)}}
                                    {{?value2.type==0}}<%--走班--%>
                                        <div class="zbtd zb" x="{{=value1}}" y="{{=index+1}}" >{{=value2.className}}<br/>{{=value2.classRoom}}</div>
                                    {{??value2.type==2}}<%--非走班--%>
                                        {{=value2.className}}<br/>{{=value2.classRoom}}
                                    {{??value2.type==4}}<%--体育走班--%>
                                        <div class="zbtd tyzb" x="{{=value1}}" y="{{=index+1}}" >体育走班</div>
                                    {{??value2.type==5}}
                                        {{=value2.className}}
                                    {{??value2.type==6}}
                                        <div class="zbtd zb" x="{{=value1}}" y="{{=index+1}}" >{{=value2.className}}<br/>{{=value2.classRoom}}</div>
                                    {{?}}
                                {{?}}
                            {{~}}
                            {{~it.data.conf.events:value3:index3}}
                                {{?value3.xIndex==value1 && value3.yIndex==index+1}}
                                    {{?value3.forbidEvent.length==1}}
                                        {{=value3.forbidEvent[0]}}
                                    {{?}}
                                {{?}}
                            {{~}}
                        </td>
                        {{~}}
                    </tr>
                    {{~}}
                </script>
            </div>

        </div>

        <!--/.tab-col-->
    </div>
    <!--/.col-right-->

</div>
<div class="main2-hide">
    <div class="main2-hide-bg"></div>
    <div class="main2-hide-wind">
        <div class="main2wind-top">
            <i style="font-size:16px;">明细</i>
            <i class="main2wind-cl">×</i>
        </div>
        <div class="hide2-main1">
				<span>
					<i class="classShow">高一1班</i>
					<select id="weekshow">

                    </select>
                    <script type="application/template" id="weekTempJs">
                        {{~it.data:value:index}}
                                {{?it.x==value}}
                        <option value="{{=value}}" selected="selected">星期{{?value==1}}一
                            {{??value==2}}二
                            {{??value==3}}三
                            {{??value==4}}四
                            {{??value==5}}五
                            {{??value==6}}六
                            {{??value==7}}日
                            {{?}}</option>
                                {{??}}
                        <option value="{{=value}}">星期{{?value==1}}一
                            {{??value==2}}二
                            {{??value==3}}三
                            {{??value==4}}四
                            {{??value==5}}五
                            {{??value==6}}六
                            {{??value==7}}日
                            {{?}}</option>
                                {{?}}
                        {{~}}
                    </script>
					<select id="classDetailShow">

                    </select>
                    <script type="application/template" id="classDetailTempJs">
                        {{~it.data:value:index}}
                                {{?it.y==index+1}}
                        <option value="{{=index+1}}" selected="selected">第{{=index+1}}节</option>
                                {{??}}
                        <option value="{{=index+1}}">第{{=index+1}}节</option>
                                {{?}}
                        {{~}}
                    </script>
                    <i class="i2">8:50~9:30</i>
				</span>
            <div class="hidemain-btn select-btn">筛选</div>
            <%--<div class="hidemain-btn">导出</div>--%>
        </div>
        <table class="hide2-tab" id="detailShow">

        </table>
        <script type="application/template" id="detailTempJs">
            <tr class="row1 row">
                <td class="col1">教学班</td>
                <td class="col">教学班人数</td>
                <td class="col">任课老师</td>
                <td class="col room">上课教室</td>
                <td class="col">本班学生</td>
            </tr>
            {{~it.data:value:index}}
            <tr class="row">
                <td class="col1">{{=value.className}}</td>
                <td class="col">{{=value.people}}</td>
                <td class="col">{{=value.teacherName==null?"未设置":value.teacherName}}</td>
                <td class="col room">{{=value.classRoom}}</td>
                <td class="col">{{=value.myClassAmount}}</td>
            </tr>
            {{~}}
        </script>
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
    seajs.use('checklist');
</script>
</body>
</html>