<%--
  Created by IntelliJ IDEA.
  User: Tony
  Date: 2015/4/13
  Time: 10:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<html>
<head>
    <title>管理统计-教育局首页</title>
    <script type="text/javascript" src="/static/js/jquery.min.js"></script>
    <link href="/static_new/css/reset.css" rel="stylesheet" />
    <link rel="stylesheet" type="text/css" href="/static/css/manageCount/jiaoyuju1.css">
    <%--<script src="http://echarts.baidu.com/build/dist/echarts.js"></script>--%>
    <%--<script type="text/javascript" src="/js/manageCount/xiaozhang1.js "></script>--%>
    <%--<script type="text/javascript" src="/js/manageCount/WdatePicker.js"></script>--%>
</head>
<body>
<!--=================================引入头部============================================-->
<%@ include file="../common_new/head.jsp" %>
<div class="student_infromation_main">
    <div style="width:1200px; margin: 0 auto; overflow: hidden; ">
    <!--=============================引入左边导航=======================================-->
        <%@ include file="../common_new/col-left.jsp" %>
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
                <a href="javascript:;" class="current" style="width:130px;">统计首页</a>
                <span style="float:right;"><a style="width:130px;" href="javascript:;" onclick="location.href='/manageCount/educationschools.do'">全部学校统计</a></span>
            </p>
        </div>
        <div class="ah-bg">
            <dl>
                <dd class="ah-city">
                    <em>学区：</em>
                    <div>
                        <input type="hidden" id="provinceId" value="${provinceId}">
                        <span cityId="" class="sp-ho">全部</span>
                        <c:forEach items="${citys}" var="city">
                            <span cityId="${city.id}">${city.name}</span>
                        </c:forEach>
                    </div>
                </dd>
                <dd class="ah-cou">
                    <em>区县：</em>
                    <div>
                    </div>
                </dd>
                <dd class="ah-stype">
                    <em>学段：</em>
                    <div>
                        <span schoolType="0" class="sp-schooltype">全部</span>
                        <c:forEach items="${schTypes}" var="type">
                            <span schoolType="${type.type}">${type.name}</span>
                        </c:forEach>
                    </div>
                </dd>
            </dl>
        </div>
        <div class="ah-in">
            <em>学校列表</em>
        </div>

        <div class="right_bottom">
            <ul class="sub-info-list">

            </ul>
            <!--.list-info-->
            <script type="text/template" id="j-tmpl">
                {{ if(it.rows.length>0){ }}
                    {{ for (var i = 0, l = it.rows.length; i < l; i++) { }}
                        {{var obj=it.rows[i];}}
                        <%--{{ if(obj.schoolId==''){ }}
                            <!---====================未开通样式============-->
                            <a class="ah-WKT" target="_blank">
                                <li>
                                    <span>{{=obj.schoolName}}</span>
                                    <img onclick="window.location.href='http://wpa.qq.com/msgrd?v=1&uin=2793993118&site=qq&menu=yes'" src="/img/manageCount/ah-QQ.png">
                                </li>
                            </a>
                        {{}else{}}
                            <!---====================已开通样式============-->
                            <a class="ah-KT" target="_blank" href="/managetotal/{{=obj.schoolId}}">
                                <li>
                                    <span>{{=obj.schoolName}}</span>
                                    <img src="/img/manageCount/ah-SD.png">
                                </li>
                            </a>
                        {{}}}--%>
                        {{ if(obj.test==0){ }}
                            <!---====================未开通样式============-->
                            <a class="ah-WKT" target="_blank">
                                <li>
                                    <span>{{=obj.schoolName}}</span>
                                    <img onclick="window.location.href='http://wpa.qq.com/msgrd?v=1&uin=2793993118&site=qq&menu=yes'" src="/img/manageCount/ah-QQ.png">
                                </li>
                            </a>
                        {{ }else if(obj.test==2){ }}
                            <!---====================已开通样式============-->
                            <a class="ah-KT" target="_blank" href="/managetotal/{{=obj.schoolId}}">
                                <li>
                                    <span>{{=obj.schoolName}}</span>
                                    <img src="/img/manageCount/ah-SD.png">
                                </li>
                            </a>
                        {{ }else{ }}
                            <a class="ah-KT" target="_blank" href="/managetotal/{{=obj.schoolId}}">
                                <li>
                                    <span>{{=obj.schoolName}}</span>
                                    <%--<img src="/img/manageCount/ah-SD.png">--%>
                                </li>
                            </a>
                        {{}}}
                    {{}}}
                {{}}}
            </script>

            <!--/.list-info-->

        </div>
        <!--.page-links-->
        <%--<div style="margin-top: 30px; margin-bottom: 30px; text-align:center;overflow:visible;">
            <div id="example"></div>
        </div>--%>
        <div class="new-page-links" id="pageDiv"></div>
        <!--/.page-links-->
        <%--<div class="right_bottom">
            <ul>
        		<a target="_blank" href="/managetotal/55934c15f6f28b7261c19cbd"><li><img src="/img/manageCount/ah_20.jpg"></li></a>         &lt;%&ndash;<li><img src="/img/manageCount/552e05f8f6f27442d6b51497.jpg"></li>&ndash;%&gt;
                <a target="_blank" href="/managetotal/55934c15f6f28b7261c19c93"><li><img src="/img/manageCount/ah_29.jpg"></li></a>         &lt;%&ndash;<li><img src="/img/manageCount/552e05f8f6f27442d6b5146d.jpg"></li>&ndash;%&gt;
                <a target="_blank" href="/managetotal/55934c15f6f28b7261c19c8a"><li><img src="/img/manageCount/ah_2.jpg" ></li></a>         &lt;%&ndash;<li><img src="/img/manageCount/552e05f8f6f27442d6b51464.jpg ></li>&ndash;%&gt;
                <a target="_blank" href="/managetotal/55934c15f6f28b7261c19ca8"><li><img src="/img/manageCount/ah_68.jpg"></li></a>         &lt;%&ndash;<li><img src="/img/manageCount/552e05f8f6f27442d6b51482.jpg"></li>&ndash;%&gt;

                <a target="_blank" href="/managetotal/55934c14f6f28b7261c19c60"><li><img src="/img/manageCount/ah_45.jpg"></li></a>         &lt;%&ndash;<li><img src="/img/manageCount/552e05f8f6f27442d6b5143a.jpg"></li>&ndash;%&gt;
                <a target="_blank" href="/managetotal/55934c15f6f28b7261c19c76"><li><img src="/img/manageCount/ah_51.jpg"></li></a>         &lt;%&ndash;<li><img src="/img/manageCount/552e05f8f6f27442d6b51450.jpg"></li>&ndash;%&gt;
                <a target="_blank" href="/managetotal/55934c15f6f28b7261c19d2d"><li><img src="/img/manageCount/ah_1.jpg" ></li></a>         &lt;%&ndash;<li><img src="/img/manageCount/552e05f9f6f27442d6b51507.jpg ></li>&ndash;%&gt;
                &lt;%&ndash;<a target="_blanck" href="/managetotal/211                "><li><img src="/img/manageCount/ah_3.jpg" ></li></a><li><img src="/img/manageCount/otal/211                .jpg ></li>&ndash;%&gt;
                <a target="_blank" href="/managetotal/55934c15f6f28b7261c19c9b"><li><img src="/img/manageCount/ah_4.jpg" ></li></a>         &lt;%&ndash;<li><img src="/img/manageCount/552e05f8f6f27442d6b51475.jpg ></li>&ndash;%&gt;

                <a target="_blank" href="/managetotal/55934c15f6f28b7261c19c8c"><li><img src="/img/manageCount/ah_5.jpg" ></li></a>         &lt;%&ndash;<li><img src="/img/manageCount/552e05f8f6f27442d6b51466.jpg ></li>&ndash;%&gt;
                <a target="_blank" href="/managetotal/55934c15f6f28b7261c19d36"><li><img src="/img/manageCount/ah_6.jpg" ></li></a>         &lt;%&ndash;<li><img src="/img/manageCount/tal/627                 .jpg ></li>&ndash;%&gt;
                <a target="_blank" href="/managetotal/55934c15f6f28b7261c19cc3"><li><img src="/img/manageCount/ah_7.jpg" ></li></a>         &lt;%&ndash;<li><img src="/img/manageCount/552e05f8f6f27442d6b5149d.jpg ></li>&ndash;%&gt;
                <a target="_blank" href="/managetotal/55934c15f6f28b7261c19d2b"><li><img src="/img/manageCount/ah_8.jpg" ></li></a>         &lt;%&ndash;<li><img src="/img/manageCount/552e05f9f6f27442d6b51505.jpg ></li>&ndash;%&gt;
                &lt;%&ndash;<a target="_blank" href="/managetotal/517                 "><li><img src="/img/manageCount/ah_9.jpg" ></li></a>         <li><img src="/img/manageCount/tal/517                 .jpg ></li>&ndash;%&gt;
                <a target="_blank" href="/managetotal/55934c15f6f28b7261c19c98"><li><img src="/img/manageCount/ah_10.jpg"></li></a>         &lt;%&ndash;<li><img src="/img/manageCount/500                     .jpg"></li>&ndash;%&gt;
                <a target="_blank" href="/managetotal/55934c15f6f28b7261c19d34"><li><img src="/img/manageCount/ah_11.jpg"></li></a>         &lt;%&ndash;<li><img src="/img/manageCount/552e05f9f6f27442d6b5150e.jpg"></li>&ndash;%&gt;

                <a target="_blank" href="/managetotal/55934c15f6f28b7261c19c9e"><li><img src="/img/manageCount/ah_12.jpg"></li></a>         &lt;%&ndash;<li><img src="/img/manageCount/552e05f8f6f27442d6b51478.jpg"></li>&ndash;%&gt;
                <a target="_blank" href="/managetotal/55934c15f6f28b7261c19caf"><li><img src="/img/manageCount/ah_13.jpg"></li></a>         &lt;%&ndash;<li><img src="/img/manageCount/552e05f8f6f27442d6b51489.jpg"></li>&ndash;%&gt;
                <a target="_blank" href="/managetotal/55934c15f6f28b7261c19ca7"><li><img src="/img/manageCount/ah_14.jpg"></li></a>         &lt;%&ndash;<li><img src="/img/manageCount/552e05f8f6f27442d6b51481.jpg"></li>&ndash;%&gt;
                <a target="_blank" href="/managetotal/55934c15f6f28b7261c19cb0"><li><img src="/img/manageCount/ah_15.jpg"></li></a>         &lt;%&ndash;<li><img src="/img/manageCount/552e05f8f6f27442d6b5148a.jpg"></li>&ndash;%&gt;

                <a target="_blank" href="/managetotal/55934c15f6f28b7261c19d2c"><li><img src="/img/manageCount/ah_16.jpg"></li></a>         &lt;%&ndash;<li><img src="/img/manageCount/552e05f9f6f27442d6b51506.jpg"></li>&ndash;%&gt;
                <a target="_blank" href="/managetotal/55934c15f6f28b7261c19d41"><li><img src="/img/manageCount/ah_17.jpg"></li></a>         &lt;%&ndash;<li><img src="/img/manageCount/tal/211                 .jpg"></li>&ndash;%&gt;
                <a target="_blank" href="/managetotal/55934c15f6f28b7261c19cbe"><li><img src="/img/manageCount/ah_18.jpg"></li></a>         &lt;%&ndash;<li><img src="/img/manageCount/552e05f8f6f27442d6b51498.jpg"></li>&ndash;%&gt;
                <a target="_blank" href="/managetotal/55934c15f6f28b7261c19cc9"><li><img src="/img/manageCount/ah_19.jpg"></li></a>         &lt;%&ndash;<li><img src="/img/manageCount/552e05f8f6f27442d6b514a3.jpg"></li>&ndash;%&gt;
                <a target="_blank" href="/managetotal/55934c15f6f28b7261c19cce"><li><img src="/img/manageCount/ah_21.jpg"></li></a>         &lt;%&ndash;<li><img src="/img/manageCount/552e05f8f6f27442d6b514a8.jpg"></li>&ndash;%&gt;

                <a target="_blank" href="/managetotal/55934c15f6f28b7261c19d00"><li><img src="/img/manageCount/ah_22.jpg"></li></a>         &lt;%&ndash;<li><img src="/img/manageCount/552e05f8f6f27442d6b514da.jpg"></li>&ndash;%&gt;
                <a target="_blank" href="/managetotal/55934c15f6f28b7261c19d6b"><li><img src="/img/manageCount/ah_23.jpg"></li></a>         &lt;%&ndash;<li><img src="/img/manageCount/tal/211                 .jpg"></li>&ndash;%&gt;
                <a target="_blank" href="/managetotal/55934c15f6f28b7261c19c9f"><li><img src="/img/manageCount/ah_24.jpg"></li></a>         &lt;%&ndash;<li><img src="/img/manageCount/552e05f8f6f27442d6b51479.jpg"></li>&ndash;%&gt;
                <a target="_blank" href="/managetotal/55934c15f6f28b7261c19cba"><li><img src="/img/manageCount/ah_25.jpg"></li></a>         &lt;%&ndash;<li><img src="/img/manageCount/552e05f8f6f27442d6b51494.jpg"></li>&ndash;%&gt;
                <a target="_blank" href="/managetotal/55934c15f6f28b7261c19c7f"><li><img src="/img/manageCount/ah_26.jpg"></li></a>         &lt;%&ndash;<li><img src="/img/manageCount/552e05f8f6f27442d6b51459.jpg"></li>&ndash;%&gt;

                <a target="_blank" href="/managetotal/55934c15f6f28b7261c19ca5"><li><img src="/img/manageCount/ah_27.jpg"></li></a>         &lt;%&ndash;<li><img src="/img/manageCount/552e05f8f6f27442d6b5147f.jpg"></li>&ndash;%&gt;
                <a target="_blank" href="/managetotal/55934c15f6f28b7261c19c99"><li><img src="/img/manageCount/ah_28.jpg"></li></a>         &lt;%&ndash;<li><img src="/img/manageCount/552e05f8f6f27442d6b51473.jpg"></li>&ndash;%&gt;
                <a target="_blank" href="/managetotal/55934c15f6f28b7261c19d37"><li><img src="/img/manageCount/ah_30.jpg"></li></a>         &lt;%&ndash;<li><img src="/img/manageCount/tal/628                 .jpg"></li>&ndash;%&gt;
                <a target="_blank" href="/managetotal/55934c15f6f28b7261c19d10"><li><img src="/img/manageCount/ah_31.jpg"></li></a>         &lt;%&ndash;<li><img src="/img/manageCount/552e05f8f6f27442d6b514ea.jpg"></li>&ndash;%&gt;
                <a target="_blank" href="/managetotal/55934c15f6f28b7261c19cb6"><li><img src="/img/manageCount/ah_32.jpg"></li></a>         &lt;%&ndash;<li><img src="/img/manageCount/552e05f8f6f27442d6b51490.jpg"></li>&ndash;%&gt;

                <a target="_blank" href="/managetotal/55934c15f6f28b7261c19cbb"><li><img src="/img/manageCount/ah_33.jpg"></li></a>         &lt;%&ndash;<li><img src="/img/manageCount/552e05f8f6f27442d6b51495.jpg"></li>&ndash;%&gt;
                &lt;%&ndash;<a target="_blank" href="/managetotal/496                 "><li><img src="/img/manageCount/ah_34.jpg"></li></a><li><img src="/img/manageCount/tal/496                 .jpg"></li>&ndash;%&gt;
                <a target="_blank" href="/managetotal/55934c15f6f28b7261c19c96"><li><img src="/img/manageCount/ah_35.jpg"></li></a>         &lt;%&ndash;<li><img src="/img/manageCount/552e05f8f6f27442d6b51470.jpg"></li>&ndash;%&gt;
                <a target="_blank" href="/managetotal/55934c15f6f28b7261c19cbf"><li><img src="/img/manageCount/ah_36.jpg"></li></a>         &lt;%&ndash;<li><img src="/img/manageCount/552e05f8f6f27442d6b51499.jpg"></li>&ndash;%&gt;
                <a target="_blank" href="/managetotal/55934c15f6f28b7261c19d12"><li><img src="/img/manageCount/ah_37.jpg"></li></a>         &lt;%&ndash;<li><img src="/img/manageCount/552e05f8f6f27442d6b514ec.jpg"></li>&ndash;%&gt;

                <a target="_blank" href="/managetotal/55934c15f6f28b7261c19cac"><li><img src="/img/manageCount/ah_38.jpg"></li></a>         &lt;%&ndash;<li><img src="/img/manageCount/552e05f8f6f27442d6b51486.jpg"></li>&ndash;%&gt;
                <a target="_blank" href="/managetotal/55934c15f6f28b7261c19cd5"><li><img src="/img/manageCount/ah_39.jpg"></li></a>         &lt;%&ndash;<li><img src="/img/manageCount/552e05f8f6f27442d6b514af.jpg"></li>&ndash;%&gt;
                <a target="_blank" href="/managetotal/55934c15f6f28b7261c19cd6"><li><img src="/img/manageCount/ah_40.jpg"></li></a>         &lt;%&ndash;<li><img src="/img/manageCount/552e05f8f6f27442d6b514b0.jpg"></li>&ndash;%&gt;
                <a target="_blank" href="/managetotal/55934c15f6f28b7261c19cc8"><li><img src="/img/manageCount/ah_41.jpg"></li></a>         &lt;%&ndash;<li><img src="/img/manageCount/552e05f8f6f27442d6b514a2.jpg"></li>&ndash;%&gt;

                <a target="_blank" href="/managetotal/55934c15f6f28b7261c19cb5"><li><img src="/img/manageCount/ah_42.jpg"></li></a>         &lt;%&ndash;<li><img src="/img/manageCount/552e05f8f6f27442d6b5148f.jpg"></li>&ndash;%&gt;
                <a target="_blank" href="/managetotal/55934c15f6f28b7261c19d0c"><li><img src="/img/manageCount/ah_43.jpg"></li></a>         &lt;%&ndash;<li><img src="/img/manageCount/552e05f8f6f27442d6b514e6.jpg"></li>&ndash;%&gt;
                &lt;%&ndash;<a target="_blank" href="/managetotal/211                 "><li><img src="/img/manageCount/ah_44.jpg"></li></a><li><img src="/img/manageCount/tal/211                 .jpg"></li>&ndash;%&gt;
                <a target="_blank" href="/managetotal/55934c15f6f28b7261c19ca4"><li><img src="/img/manageCount/ah_46.jpg"></li></a>         &lt;%&ndash;<li><img src="/img/manageCount/552e05f8f6f27442d6b5147e.jpg"></li>&ndash;%&gt;
                <a target="_blank" href="/managetotal/55934c15f6f28b7261c19d2e"><li><img src="/img/manageCount/ah_47.jpg"></li></a>         &lt;%&ndash;<li><img src="/img/manageCount/552e05f9f6f27442d6b51508.jpg"></li>&ndash;%&gt;

                <a target="_blank" href="/managetotal/55934c14f6f28b7261c19c64"><li><img src="/img/manageCount/ah_48.jpg"></li></a>         &lt;%&ndash;<li><img src="/img/manageCount/552e05f8f6f27442d6b5143e.jpg"></li>&ndash;%&gt;
                <a target="_blank" href="/managetotal/55934c15f6f28b7261c19d2f"><li><img src="/img/manageCount/ah_49.jpg"></li></a>         &lt;%&ndash;<li><img src="/img/manageCount/552e05f9f6f27442d6b51509.jpg"></li>&ndash;%&gt;
                <a target="_blank" href="/managetotal/55934c15f6f28b7261c19d4c"><li><img src="/img/manageCount/ah_50.jpg"></li></a>         &lt;%&ndash;<li><img src="/img/manageCount/tal/211                 .jpg"></li>&ndash;%&gt;
                <a target="_blank" href="/managetotal/55934c15f6f28b7261c19c86"><li><img src="/img/manageCount/ah_52.jpg"></li></a>         &lt;%&ndash;<li><img src="/img/manageCount/552e05f8f6f27442d6b51460.jpg"></li>&ndash;%&gt;

                <a target="_blank" href="/managetotal/55934c15f6f28b7261c19c88"><li><img src="/img/manageCount/ah_53.jpg"></li></a>         &lt;%&ndash;<li><img src="/img/manageCount/552e05f8f6f27442d6b51462.jpg"></li>&ndash;%&gt;
                <a target="_blank" href="/managetotal/55934c15f6f28b7261c19c91"><li><img src="/img/manageCount/ah_54.jpg"></li></a>         &lt;%&ndash;<li><img src="/img/manageCount/552e05f8f6f27442d6b5146b.jpg"></li>&ndash;%&gt;
                <a target="_blank" href="/managetotal/55934c15f6f28b7261c19cfe"><li><img src="/img/manageCount/ah_55.jpg"></li></a>         &lt;%&ndash;<li><img src="/img/manageCount/552e05f8f6f27442d6b514d8.jpg"></li>&ndash;%&gt;
                <a target="_blank" href="/managetotal/55934c15f6f28b7261c19cae"><li><img src="/img/manageCount/ah_56.jpg"></li></a>         &lt;%&ndash;<li><img src="/img/manageCount/552e05f8f6f27442d6b51488.jpg"></li>&ndash;%&gt;

                <a target="_blank" href="/managetotal/55934c15f6f28b7261c19cfd"><li><img src="/img/manageCount/ah_57.jpg"></li></a>         &lt;%&ndash;<li><img src="/img/manageCount/552e05f8f6f27442d6b514d7.jpg"></li>&ndash;%&gt;
                <a target="_blank" href="/managetotal/55934c15f6f28b7261c19c95"><li><img src="/img/manageCount/ah_58.jpg"></li></a>         &lt;%&ndash;<li><img src="/img/manageCount/552e05f8f6f27442d6b5146f.jpg"></li>&ndash;%&gt;
                <a target="_blank" href="/managetotal/55934c15f6f28b7261c19cb1"><li><img src="/img/manageCount/ah_59.jpg"></li></a>         &lt;%&ndash;<li><img src="/img/manageCount/552e05f8f6f27442d6b5148b.jpg"></li>&ndash;%&gt;
                <a target="_blank" href="/managetotal/55934c15f6f28b7261c19cc0"><li><img src="/img/manageCount/ah_60.jpg"></li></a>         &lt;%&ndash;<li><img src="/img/manageCount/552e05f8f6f27442d6b5149a.jpg"></li>&ndash;%&gt;

                <a target="_blank" href="/managetotal/55934c15f6f28b7261c19d11"><li><img src="/img/manageCount/ah_61.jpg"></li></a>         &lt;%&ndash;<li><img src="/img/manageCount/552e05f8f6f27442d6b514eb.jpg"></li>&ndash;%&gt;
                <a target="_blank" href="/managetotal/55934c15f6f28b7261c19cc4"><li><img src="/img/manageCount/ah_62.jpg"></li></a>         &lt;%&ndash;<li><img src="/img/manageCount/552e05f8f6f27442d6b5149e.jpg"></li>&ndash;%&gt;
                <a target="_blank" href="/managetotal/55934c15f6f28b7261c19d81"><li><img src="/img/manageCount/ah_63.jpg"></li></a>         &lt;%&ndash;<li><img src="/img/manageCount/tal/211                 .jpg"></li>&ndash;%&gt;
                <a target="_blank" href="/managetotal/55934c15f6f28b7261c19d43"><li><img src="/img/manageCount/ah_64.jpg"></li></a>         &lt;%&ndash;<li><img src="/img/manageCount/507                     .jpg"></li>&ndash;%&gt;
                <a target="_blank" href="/managetotal/55934c15f6f28b7261c19c8f"><li><img src="/img/manageCount/ah_65.jpg"></li></a>         &lt;%&ndash;<li><img src="/img/manageCount/552e05f8f6f27442d6b51469.jpg"></li>&ndash;%&gt;
                <a target="_blank" href="/managetotal/55934c15f6f28b7261c19cc1"><li><img src="/img/manageCount/ah_66.jpg"></li></a>         &lt;%&ndash;<li><img src="/img/manageCount/552e05f8f6f27442d6b5149b.jpg"></li>&ndash;%&gt;
                &lt;%&ndash;<a target="_blank" href="/managetotal/211                 "><li><img src="/img/manageCount/ah_67.jpg"></li></a><li><img src="/img/manageCount/tal/211                 .jpg"></li>&ndash;%&gt;

                &lt;%&ndash;<c:forEach items="${list}" var="item">
                    <a target="_blank" href="/managetotal/${item.schoolId}"><li>${item.schoolName}</li></a>
                </c:forEach>&ndash;%&gt;
            </ul>
        </div>--%>
    </div>
</div>
<div style="clear: both"></div>
<%@ include file="../common_new/foot.jsp"%>

    <!-- Javascript Files -->

    <%--
    <script type="text/javascript" src="/static/js/WdatePicker.js"></script>
    <script src="/static/plugins/echarts/echarts.js"></script>--%>
    <!-- initialize seajs Library -->
    <script src="/static_new/js/sea.js"></script>
    <!-- Custom js -->
    <script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
    <script>
        seajs.use('countMain',function(countMain){
            countMain.init();
        });
    </script>
</body>
</html>




