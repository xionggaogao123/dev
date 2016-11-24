<%--
  Created by IntelliJ IDEA.
  User: wang_xinxin
  Date: 2015/8/19
  Time: 19:48
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="roles" uri="http://fulaan.userRole.com" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <!-- Basic Page Needs-->
  <meta charset="utf-8">
  <link rel="dns-prefetch" href="//source.ycode.cn" />
  <title>复兰科技</title>
  <meta name="description" content="">
  <meta name="author" content="" />
  <meta name="copyright" content="" />
  <meta name="keywords" content="">
  <meta name="viewport" content="width=device-width,initial-scale=1, maximum-scale=1">
  <link href="/static_new/css/reset.css" rel="stylesheet" />
  <link href="/static_new/css/microlesson/microlesson.css?v=2015041602" rel="stylesheet" />
  <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery.min.js?v=1"></script>
</head>
<body>
<!--#head-->
<%@ include file="../common_new/head-cloud.jsp"%>
<!--/#head-->
<!--#content-->
<div id="content" class="clearfix">
  <!--.col-left-->
  <%--<%@ include file="../common_new/col-left.jsp" %>--%>
  <!--/.col-left-->

  <%--<c:choose>--%>
    <%--<c:when test="${roles:isStudentOrParent(sessionValue.userRole)}">--%>
      <%--<jsp:include page="/WEB-INF/pages/common/right_2.jsp"></jsp:include>--%>
    <%--</c:when>--%>
    <%--<c:otherwise>--%>
      <%--<jsp:include page="/WEB-INF/pages/common/right.jsp"></jsp:include>--%>
    <%--</c:otherwise>--%>
  <%--</c:choose>--%>
  <!--.col-right-->
  <div class="col-right" style="width: 1000px;">

    <!--.tab-col右侧-->
    <div class="tab-col bisailist">
      <c:if test="${roles:isEducation(sessionValue.userRole)}">
          <a href="javascript:;" class="kaishi">发起比赛</a><!-- 教育局页面特有 -->
      </c:if>
       <a href="javascript:;" class="shiyong">微课评比使用指南</a>
      <div class="liebiao">
        <div class="allKecheng">
        <span>全部比赛</span>
        </div>
        <ul class="clearfix listbiao">

        </ul>
      </div>

      <script id="matchlist" type="text/template">
        {{ if(it.rows.length>0){ }}
        {{ for (var i = 0, l = it.rows.length; i < l; i++) { }}
        {{var obj=it.rows[i];}}
        <li cid="{{=obj.id}}" class="match" style="cursor: pointer;width: 220px;">
          <img src="{{=obj.path}}" alt="" style="width: 210px;height: 153px;">
          <p>{{=obj.matchname}}</p>
          <span>{{=obj.begintime}}-{{=obj.endtime}}</span>
        </li>
        {{ } }}
        {{ } }}
      </script>
      <!-- 分页 -->
      <%--<div class="fenye">--%>
        <%--<a href="javascript:;" class="kuan"> < </a>--%>
        <%--<a href="javascript:;" class="current">1</a>--%>
        <%--<a href="javascript:;">2</a>--%>
        <%--<a href="javascript:;" class="more">...</a>--%>
        <%--<a href="javascript:;">100</a>--%>
        <%--<a href="javascript:;" class="kuan"> > </a>--%>
      <%--</div>--%>
      <div class="page-paginator">
        <span class="first-page">首页</span>
                        <span class="page-index">
                            <span class="active">1</span>
                            <span>2</span>
                            <span>3</span>
                            <span>4</span>
                            <span>5</span>
                            <i>···</i>
                        </span>
        <span class="last-page">尾页</span>

      </div>

        <!--=========微课评比使用指南为隐藏==========-->
        <div class="ah-KN">
            <div class="ah-top">
                <a href="javascript:;">&lt;返回上一级</a>
            </div>
            <div class="ah-bottom">
                <h2>微课大赛上报作品流程说明</h2>
                <h3>（义务教育阶段各学校上报流程）</h3>
                <p class="p15">登陆并上传微课作品：</p>
                <p>1、打开最新版本的网络浏览器（IE浏览器禁运行，推荐谷歌浏览器），进入米东区教育资源中心网址：midong.k6kt.com</p>
                <p>2、登入老师个人专属账号，（各学校信息办主任之前下发）如“a老师”，图例1，进入平台。</p>
                <div class='div-img'>
                    <img src="/img/manageCount/img-weike1.png" alt="" width="600px">
                    <p class="p-mid">图1</p>
                </div>
                <p>3、点击区平台下方功能区第一行第一个按钮，“微课大赛”。如图2中红框所示。</p>
                <div class='div-img'>
                    <img src="/img/manageCount/img-weike2.png" alt="" width="600px">
                    <p class="p-mid">图2</p>
                </div>
                <p>4、在新出现的窗口中点击“2016年米东区微课大赛”。如图3所示。</p>
                <div class='div-img'>
                    <img src="/img/manageCount/img-weike3.png" alt="" width="600px">
                    <p class="p-mid">图3</p>
                </div>
                <p>5、在新出现的窗口中点击“上传课程”按钮，如图4所示。</p>
                <div class='div-img'>
                    <img src="/img/manageCount/img-weike4.png" alt="" width="600px">
                    <p class="p-mid">图4</p>
                </div>
                <p>6、在新出现的窗口中输入课程名，选择相应的学科，点击确认，如图5所示。微课命名规则为：“学校-老师姓名-微课的名称” <br>(例如：“乌市79中-A老师-少年闰土”）</p>
                <div class='div-img'>
                    <img src="/img/manageCount/img-weike5.png" alt="" width="600px">
                    <p class="p-mid">图5</p>
                </div>
                <p>7、在新出现的页面中，点击“选择文件”上传微课课程，如图6所示。</p>
                <div class='div-img'>
                    <img src="/img/manageCount/img-weike6.png" alt="" width="600px">
                    <p class="p-mid">图6</p>
                </div>
                <p>8、辅助教学资料（教学设计文本、多媒体教学课件）点击辅助学习资料栏下方的“文件选择”键，上传相应文件，如图7所示。</p>
                <div class='div-img'>
                    <img src="/img/manageCount/img-weike7.png" alt="" width="600px">
                    <p class="p-mid">图7</p>
                </div>
                <p>9、点击“保存”按钮，保存微课，即上报作品成功。</p>
            </div>
        </div>
      <!-- 分页 -->
    </div>
    <!--/.tab-col右侧-->

  </div>
  <!--/.col-right-->

</div>
<!--/#content-->


<!--#foot-->
<%@ include file="../common_new/foot.jsp" %>
<script type="application/javascript">

    $(function(){
        $(".shiyong").click(function(){
            $(".ah-KS").hide();
            $(".ah-KN").show();
        })
        $(".ah-go").click(function(){
            $(".ah-KS").show();
            $(".ah-KN").hide();
        })
    })
</script>
<!--#foot-->
<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script src="/static_new/js/sea.js"></script>
<!-- Custom js -->
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
  seajs.use('microlesson');
</script>
</body>
</html>
