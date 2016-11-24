<%--
  Created by IntelliJ IDEA.
  User: Tony
  Date: 2015/4/13
  Time: 10:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="roles" uri="http://fulaan.userRole.com" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>

<html>
<head>
    <meta charset="utf-8">
    <link rel="dns-prefetch" href="//source.ycode.cn" />
    <title>平台使用统计</title>
    <meta name="description" content="">
    <meta name="author" content="" />
    <meta name="copyright" content="" />
    <meta name="keywords" content="">
    <meta name="viewport" content="width=device-width,initial-scale=1, maximum-scale=1">

    <!-- css files -->
    <!-- Normalize default styles -->
    <link href="/static_new/css/reset.css" rel="stylesheet" />
    <link rel="stylesheet" type="text/css" href="/static_new/css/managecount/managecount.css">
    <link rel="stylesheet" type="text/css" href="/static_new/css/dialog.css">
    <script type="text/javascript" src="/static/js/jquery.min.js"></script>
    <script type="text/javascript" src="/static/js/sharedpart.js"></script>

    <style>
        select option[disabled] {
            display: none;
        }
    </style>
</head>
<body>
<!--=================================引入头部============================================-->
<%@ include file="../common_new/head.jsp" %>
<div class="student_infromation_main">
    <div style="width:1200px; margin: 0 auto; overflow: hidden; ">
        <!--=============================引入左边导航=======================================-->
        <%@ include file="../common_new/col-left.jsp" %>
        <!--=============================引入广告=======================================-->
        <!--广告-->
        <c:choose>
            <c:when test="${roles:isStudentOrParent(sessionValue.userRole)}">
                <jsp:include page="/WEB-INF/pages/common/right_2.jsp"></jsp:include>
            </c:when>
            <c:otherwise>
                <jsp:include page="/WEB-INF/pages/common/right.jsp"></jsp:include>
            </c:otherwise>
        </c:choose>
        <!--广告-->


        <div class="right">
            <div class="right_top">
                <p>
                    <%--<a href="javascript:;" style="width:130px;" onclick="location.href=''">统计首页</a>--%>
                    <a href="javascript:;" class="current">平台使用统计</a>
                </p>
                <div style="cursor:pointer; margin-bottom: 10px;"><a href="/manageCount/countMain.do">&nbsp;&nbsp;&nbsp;&nbsp;返回&gt;</a></div>
                <div class="select">
                    <select id="timeArea" name="timeArea" style="width:100px;">
                        <c:forEach items="${timeAreas}" var="item">
                            <option value="${item.key}">${item.value}</option>
                        </c:forEach>
                    </select>
                    <input id="dateStart" name="dateStart"  style="width:140px; height:30px; border:1px solid #a9a9a9; outline:none; margin-right:4px;"
                           onClick="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'dateEnd\')}'})"/>
                    <input id="dateEnd" name="dateEnd"  style="width:140px; height:30px; border:1px solid #a9a9a9; outline:none; margin-right:4px;"
                           onClick="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'dateStart\')}'})"/>
                    <input type="hidden" id="currTime" name="currTime" value="${currTime}"><br/>
                    <select id="school-search" style="width:129px;">
                        <option value="" schoolType="14">全部学校</option>
                        <c:forEach items="${list}" var="sl">
                            <option value="${sl.schoolId}" schoolType="${sl.schoolTypeInt}">${sl.schoolName}</option>
                        </c:forEach>
                    </select>
                    <select id="gradetype-search" style="width:100px;">
                        <option value="0">全部年级</option>
                    </select>
                    <span id="searchTotal">确定</span>
                </div>
            </div>
            <!--top-->
            <div class="tongji1">
                <div id="schoolName" style="font-size: 28px; color:#333; text-align: center; line-height: 40px;"></div>
                <p class="pp">
                    <span>1</span>
                    &nbsp;&nbsp;&nbsp;访问统计
                </p>
                <div class="fangwen">
                    <div class="title">访问人数</div>
                    <div class="zhexian1" id="main1"></div>
                    <div class="zhexian2">
                        <div class="zhexian2_1" id="main2"></div>
                        <div class="zhexian2_2" id="main3"></div>
                    </div>
                    <div class="touxiang">

                        <div class="bot">
                            <dl>
                                <dt><img src="/img/manageCount/3_3.png" height="62" width="60" alt="" /></dt>
                                <dd>家长</dd>
                            </dl>
                            <ul id="partcount"></ul>
                        </div>

                        <div class="bot">
                            <dl>
                                <dt><img src="/img/manageCount/3_2.png" height="62" width="60" alt="" /></dt>
                                <dd>学生</dd>
                            </dl>
                            <ul id="stucount"></ul>
                        </div>

                        <div class="bot">
                            <dl>
                                <dt><img src="/img/manageCount/3_1.png" height="62" width="60" alt="" /></dt>
                                <dd>老师</dd>
                            </dl>
                            <ul id="teacount"></ul>
                        </div>
                    </div>
                </div>
            </div>
            <!--统计1-->
            <div class="tongji2">
                <p class="pp">
                    <span>2</span>
                    &nbsp;&nbsp;&nbsp;备课统计
                </p>
                <div class="fangwen" style="margin-bottom:9px;">
                    <div class="title">老师备课数</div>
                    <div class="zhexian2" id="main4"></div>
                    <div class="touxiang">
                        <div class="bot">
                            <dl>
                                <dt><img src="/img/manageCount/5_1.png" height="62" width="60" alt="" /></dt>
                                <dd>备课空间</dd>
                            </dl>
                            <ul id="lescount"></ul>
                        </div>
                        <div class="bot">
                            <dl>
                                <dt><img src="/img/manageCount/5_2.png" height="62" width="60" alt="" /></dt>
                                <dd>班级课程</dd>
                            </dl>
                            <ul id="clscount"></ul>
                        </div>
                    </div>
                </div>
                <div class="fangwen">
                    <div class="title">学生观看数</div>
                    <div class="zhexian2" id="main5"></div>
                    <div class="touxiang">
                        <div class="bot">
                            <dl>
                                <dt><img src="/img/manageCount/7_1.png" height="62" width="60" alt="" /></dt>
                                <dd>云课程</dd>
                            </dl>
                            <ul id="cldcount"></ul>
                        </div>
                        <div class="bot">
                            <dl>
                                <dt><img src="/img/manageCount/5_2.png" height="62" width="60" alt="" /></dt>
                                <dd>班级课程</dd>
                            </dl>
                            <ul id="sclscount"></ul>
                        </div>
                    </div>
                </div>
            </div>
            <!--统计2-->
            <div class="tongji3" style="border-bottom:none;">
                <p class="pp">
                    <span>3</span>
                    &nbsp;&nbsp;&nbsp;资源统计
                </p>
                <div class="fangwen">
                    <div class="title">资源数量</div>
                    <div class="zhexian2" id="main6"></div>
                    <div class="touxiang">
                        <div class="bot" style="margin-left: 30px;">
                            <dl>
                                <dt><img src="/img/manageCount/9_1.png" height="62" width="60" alt="" /></dt>
                                <dd>作业</dd>
                            </dl>
                            <ul id="hwcount"></ul>
                        </div>
                        <div class="bot" style="margin-left: 155px;">
                            <dl>
                                <dt><img src="/img/manageCount/9_2.png" height="62" width="60" alt="" /></dt>
                                <dd>试卷</dd>
                            </dl>
                            <ul id="pcount"></ul>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div style="clear: both"></div>
    <%@ include file="../common_new/foot.jsp" %>

    <!-- Javascript Files -->

    <%--
    <script type="text/javascript" src="/static/js/WdatePicker.js"></script>
    <script src="/static/plugins/echarts/echarts.js"></script>--%>
    <!-- initialize seajs Library -->
    <script src="/static_new/js/sea.js"></script>
    <!-- Custom js -->
    <script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
    <script>
        seajs.use('educationSchools',function(educationSchools){
            educationSchools.init();
        });
    </script>
</body>
</html>