<%--
  Created by IntelliJ IDEA.
  User: fulaan
  Date: 15-8-4
  Time: 上午10:22
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<html>
<head>
  <title>作业</title>
  <%--<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">--%>
  <link rel="stylesheet" href="/static_new/css/homework.css">
  <link href="/static_new/css/schoolsecurity.css?v=2015041602" rel="stylesheet" />
  <%--<link href="//netdna.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css" rel="stylesheet">--%>
  <link href="/static_new/css/reset.css" rel="stylesheet"/>
  <link href="/static_new/css/friendcircle/homepage.css?v=1" type="text/css" rel="stylesheet">
  <%--计算发表日期--%>
  <script src="/static/js/shareforflipped.js" type="text/javascript"></script>
  <%--录音--%>
  <script type="text/javascript" src="/static/js/swfobject.js"></script>
  <script type="text/javascript" src="/static/js/recorder.js"></script>


    <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery.min.js?v=1"></script>
    <script type="text/javascript" src="/static/js/main.js"></script>


  <%--<script src="/static/plugins/bootstrap/js/bootstrap-paginator.min.js" type="text/javascript"></script>--%>
  <%--<script src="/static/js/shareforflipped.js" type="text/javascript"></script>--%>
  <%--<script type="text/javascript" src="/static/js/template.js"></script>--%>
  <%--<script type="text/javascript" src="/static/js/myexam.js"></script>--%>



</head>
<body type="${param.type}">
<!--===========================startt头部==============================-->
<%@ include file="../common_new/head.jsp" %>
<!--===========================end头部==============================-->
<div class="homework-main">
  <!--===========================start左边导航==============================-->
  <%@ include file="../common_new/col-left.jsp" %>
  <!--===========================end左边导航==============================-->
  <!--===========================start广告==============================-->
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
  <!--===========================end广告==============================-->
  <div class="homework-right">
    <div class="homework-right-top">
      <div class="homework-banner">
        <span>
          <c:choose>
            <c:when test="${param.type == 'keqian'}">
              课前作业
            </c:when>
            <c:otherwise>
              课后作业
            </c:otherwise>
          </c:choose>
        </span>

      </div>
      <div class="homework-cls">
        <div class="cls-left">
          <em>学科</em>|
        </div>
        <div class="cls-center">
          <em class="checkedClass" id="qbbj" subjectId="000000000000000000000000" classType="1">全部</em>
        </div>
        <div class="cls-right" id="classSubject">
          <%--<span>三年级一班</span>--%>
        </div>

        <%--<div class="cls-left">--%>
          <%--<em>类型</em>|--%>
        <%--</div>--%>
        <div class="cls-right" hidden>
          <c:choose>
            <c:when test="${param.type == 'keqian'}">
              <span class="checkedType hwtype" value="0">课前</span>
            </c:when>
            <c:when test="${param.type == 'kehou'}">
              <span class="checkedType hwtype" value="1">课后</span>
            </c:when>
            <c:otherwise>
              <span class="checkedType hwtype" value="3">全部</span>
            </c:otherwise>
          </c:choose>
          <%--<span class="checkedType hwtype" value="3">全部</span>--%>
          <%--<span class="hwtype" value="0">课前</span>--%>
          <%--<span class="hwtype" value="1">课后</span>--%>
          <%--<span class="hwtype" value="2">其他</span>--%>
        </div>

        <div class="cls-left">
          <em>内容</em>|
        </div>
        <div class="cls-right">
          <span class="hwcon checkedType" value="0">全部</span>
          <span class="hwcon" value="1">有视频</span>
          <span class="hwcon" value="2">有附件</span>
          <span class="hwcon" value="3">有练习</span>
          <span class="hwcon" value="4">有语音</span>
        </div>

        <div class="cls-left">
          <em>时间</em>|
        </div>
        <div class="cls-right">
          <span class="hwterm checkedTerm" value="0">本学期作业</span>
          <span class="hwterm" value="1">全部作业</span>
        </div>
      </div>
    </div>
    <div class="homework-right-info">
      <dl id="homeworkList">

        <%--作业列表--%>

      </dl>
      <dl id="nohomework" style="color:#FF0000;display: none;text-align:center">暂时没有作业</dl>

      <%--分页--%>
      <div class="page-paginator">
        <span class="first-page">首页</span>
                        <span class="page-index">
                            <span class="active">1</span>
                            <%--<span>2</span>--%>
                            <%--<span>3</span>--%>
                            <%--<span>4</span>--%>
                            <%--<span>5</span>--%>
                            <i>···</i>
                        </span>
        <span class="last-page">尾页</span>

      </div>
      <%--分页结束--%>
    </div>
  </div>


  <!--=============================批改作业=============================-->
  <div class="correct-main">
    <div class="correct-top">
      <dl>
        <dt>
          <em>返回&gt;</em>
        </dt>
      </dl>
    </div>
  </div>
</div>


<div class="homework-bg" style="display: none">
</div>

<div style="clear: both"></div>
<%@ include file="../common_new/foot.jsp"%>

<script id="classSubjectTmpl" type="text/template">
  {{ for(var i in it) { }}
  <span class="classSubject" subjectId="{{=it[i].value}}" classId="{{=it[i].id}}" classType="{{? it[i].value=='000000000000000000000000'}}2{{??}}1{{?}}">
    <i>{{=it[i].name}}</i>
  {{? it[i].reminder>0 }}<ii class="redpointt"></ii>{{?}}
 <%--<ii class="redpoint"></ii>--%>
  </span>
  {{ } }}
</script>


<script id="homeworkListTmpl" type="text/template">
  {{ for(var i in it) { }}
  <dd id="detail" value="{{=it[i].id}}" pg="{{=it[i].pg}}" lessonId="{{=it[i].lessonId}}" jnum="{{=it[i].exerciseNum}}">
    <%--<img src="http://placehold.it/50x50" width="50px" height="50px" alt="">--%>
    <img class="discuss-info-img" src="{{=it[i].userAvatar}}" width="50px" height="50px"/>
    <div>
      {{? it[i].type==0}}
      <em class="hw-ye">课前</em>
      {{?? it[i].type==1}}
      <em class="hw-red">课后</em>
      {{?? it[i].type==2}}
      <em class="hw-blue">其他</em>
      {{?}}
      <i>{{=it[i].title}}</i>
      {{? it[i].reminder>0 }}<ii class="redpoint"></ii>{{?}}
      <%--<ii class="redpoint"></ii>--%>

      {{? it[i].voiceNum != 0}}
      <i class="fa fa-microphone  cur-en YY">
        <p class="hw-video">语音</p>
      </i>

      {{?}}
      {{? it[i].videoNum != 0}}
      <i class="fa fa-video-camera cur-en video">
        <p class="hw-video">视频数</p>
      </i>
      <span>{{=it[i].videoNum}}</span><!--视频数-->
      {{?}}
      {{? it[i].fileNum != 0}}
      <i class="fa fa-files-o cur-en files">
        <p class="hw-files">文档数</p>
      </i>
      <span>{{=it[i].fileNum}}</span><!--文档数-->
      {{?}}
      {{? it[i].exerciseNum != 0}}
      <i class="fa fa-file-o cur-en file">
        <p class="hw-file">练习数</p>
      </i>
      <span>{{=it[i].exerciseNum}}</span><!--习题数-->
      {{?}}
      <br>
                        <span>
                            <strong>{{=it[i].userName}}</strong> 于{{=it[i].time}}发表
                        </span>
      {{? it[i].submitCount != 0}}
      <em class="homework-ee">最后回复于{{=getAdaptTimeFromNow(it[i].lastSubmitTime)}}</em><em class="homework-ee">已经回复{{=it[i].submitCount}}条</em>
      {{?}}

                        <%--<span class="homework-info-right">--%>
                            <%--<i class="edit" value="{{=it[i].id}}" lessonId="{{=it[i].lessonId}}">--%>
                              <%--<p class="hw-edit">批改作业</p>--%>
                            <%--</i><!--批改作业-->--%>
                            <%--<i class="bar" value="{{=it[i].id}}" lessonId="{{=it[i].lessonId}}">--%>
                              <%--<p class="hw-bar">批改习题</p>--%>
                            <%--</i><!--批改习题-->--%>
                            <%--<i class="trash" value="{{=it[i].id}}" lessonId="{{=it[i].lessonId}}">--%>
                              <%--<p class="hw-trash">查看统计</p>--%>
                            <%--</i><!--查看统计-->--%>
                             <%--<i class="see" id="see" value="{{=it[i].id}}" lessonId="{{=it[i].lessonId}}">--%>
                               <%--<p class="hw-see">编辑</p>--%>
                             <%--</i><!--编辑-->--%>
                            <%--<i class="del" value="{{=it[i].id}}" lessonId="{{=it[i].lessonId}}">--%>
                              <%--<p class="hw-del">删除</p>--%>
                            <%--</i><!--删除-->--%>
                        <%--</span>--%>
    </div>
  </dd>
  {{ } }}
</script>

<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script src="/static_new/js/sea.js"></script>
<!-- Custom js -->
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
  seajs.use('/static_new/js/modules/homework/0.1.0/studentindex.js', function(stuIndex) {
    stuIndex.init();
  });
</script>
</body>
</html>
