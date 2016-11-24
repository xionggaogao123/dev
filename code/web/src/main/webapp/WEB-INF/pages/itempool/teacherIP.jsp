<%--
  Created by IntelliJ IDEA.
  User: fl
  Date: 2015/12/23
  Time: 17:11
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
  <!-- Basic Page Needs-->
  <meta charset="utf-8">
  <link rel="dns-prefetch" href="//source.ycode.cn" />
  <title>题库</title>
  <meta name="description" content="">
  <meta name="author" content="" />
  <meta name="copyright" content="" />
  <meta name="keywords" content="">
  <meta name="viewport" content="width=device-width,initial-scale=1, maximum-scale=1">
  <!-- css files -->
  <!-- Normalize default styles -->
  <link href="/static_new/css/reset.css" rel="stylesheet" />
  <!-- jquery artZoom4Liaoba styles -->
  <!-- Custom styles -->
  <link href="/static_new/css/tiku.css" rel="stylesheet" />

  <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery.min.js?v=1"></script>
</head>
<body userRole = ${userRole}>


<!--#head-->
<%@ include file="../common_new/head.jsp" %>
<!--/#head-->

<!--#content-->
<div id="content" class="clearfix">

  <!--.col-left-->
  <%@ include file="../common_new/col-left.jsp" %>
  <!--/.col-left-->
  <!-- 广告部分 -->
  <c:choose>
    <c:when test="${roles:isStudentOrParent(sessionValue.userRole)}">
      <jsp:include page="/WEB-INF/pages/common/right_2.jsp"></jsp:include>
    </c:when>
    <c:otherwise>
      <jsp:include page="/WEB-INF/pages/common/right.jsp"></jsp:include>
    </c:otherwise>
  </c:choose>
  <!-- 广告部分 -->
  <!--.col-right-->
  <div class="col-right">

    <ul class="top-nav">
      <li class="top1" pool="1">云题库</li>
      <li pool="2">教师个人题库</li>
      <li pool="3">校本题库</li>
      <li pool="4">学生错题本</li>
    </ul>

    <div class="div-sx">
      <ul class="top-sx">
        <li class="top2">同步教材版本</li>
        <li>综合知识点</li>
        <span class="sx-search">
            <input type="text"  placeholder="在该条件下查找" onfocus="(this).placeholder=''" onblur="(this).placeholder='在该条件下查找'">
            <span id="regular"></span>
        </span>
      </ul>
    </div>
    <div class="sx-cont1">
      <dl class="dl-sx">
        <dd>
          <span class="span1">学段</span>
          <div class="sx-list" id="TermType" subId="subject" typeInt="1">
            <span class="top3" value="">全部</span>
            <span value="55d41e47e0b064452581269a">小学</span>
            <span value="55d41e47e0b064452581269c">初中</span>
            <span value="55d41e47e0b064452581269e">高中</span>
          </div>
        </dd>
        <dd>
          <span class="span1">学科</span>
          <div class="sx-list" id="subject" subId="bookVertion" typeInt="2">
            <span class="top3" value="">全部</span>
          </div>
        </dd>
        <dd>
          <span class="span1">教版</span>
          <div class="sx-list" id="bookVertion" subId="grade" typeInt="3">
            <span class="top3">全部</span>
          </div>
        </dd>
        <dd>
          <span class="span1">课本</span>
          <div class="sx-list" id="grade" subId="chapter" typeInt="4">
            <span class="top3">全部</span>
          </div>
        </dd>
        <dd>
          <span class="span1">题型</span>
          <div class="sx-list" id="itemType">
            <span class="top3" value="0">全部</span>
            <span value="1">选择题</span>
            <span value="3">判断题</span>
            <span value="4">填空题</span>
            <span value="5">主观题</span>
          </div>
        </dd>
        </dd>
      </dl>
    </div>

    <div class="sx-cont2">
      <dl class="dl-sx">
        <dd>
          <span class="span1">学段</span>
          <div class="sx-list" id="TermType1" subId="subject1" typeInt="1">
            <span class="top3" value="">全部</span>
            <span value="55d41e47e0b064452581269a">小学</span>
            <span value="55d41e47e0b064452581269c">初中</span>
            <span value="55d41e47e0b064452581269e">高中</span>
          </div>
        </dd>
        <dd>
          <span class="span1">学科</span>
          <div class="sx-list" id="subject1" subId="area" typeInt="2">
            <span class="top3" value="">全部</span>
          </div>
        </dd>
        <dd>
          <span class="span1">题型</span>
          <div class="sx-list" id="itemType1">
            <span class="top3" value="0">全部</span>
            <span value="1">选择题</span>
            <span value="3">判断题</span>
            <span value="4">填空题</span>
            <span value="5">主观题</span>
          </div>
        </dd>
      </dl>
    </div>
    <div id="itemList">




    </div>
    <div class="new-page-links"></div>

    <!--教材目录、推送上传-->
    <div class="float-right">
      <div class="ml-list sx-cont1">
        <div class="ml-top">
          <a id="title">教材目录</a>
        </div>
        <dl class="ml-dl" id="chapter" subId="part" typeInt="5">
          <%--<dd>--%>
            <%--<div class="ml-title">第一章</div>--%>
            <%--<ul class="ml-ul">--%>
              <%--<li>第一节</li>--%>
              <%--<li>第二节</li>--%>
              <%--<li>第三节</li>--%>
              <%--<li>第四节</li>--%>
              <%--<li>第五节</li>--%>
              <%--<li>第六节</li>--%>
            <%--</ul>--%>
          <%--</dd>--%>
        </dl>
      </div>
      <div class="ml-list sx-cont2" hidden>
        <div class="ml-top">
          <a>知识点</a>
        </div>
        <dl class="ml-dl" id="area" subId="point" typeInt="7">

        </dl>
      </div>
      <div class="img-sc">
        <div>
          <a href="/itempool/editMulItem.do?ids=&show=0" target="_blank"><img src="/images/kssc.png" height="80" width="80"></a>
        </div>
        <div>
          <a href="/itempool/uploadItem.do" target="_blank"><img src="/images/plsc.png" height="80" width="80"></a>
        </div>
      </div>
    </div>
    <!--/教材目录、推送上传-->

    <!--推送提示-->
    <div class="wind-tc">
      <div class="tc-top">推送</div>
      <div class="tc-cont1">
        是否推送到教师题库？
      </div>
      <div class="tc-cont2">
        <button class="btn-ok">推送</button>
        <button class="btn-no">取消</button>
      </div>
    </div>
    <!--/推送提示-->

    <!--推送成功-->
    <div class="wind-tccg">
      <div class="tc-top">推送</div>
      <div class="tc-cont1">
        推送成功
      </div>
      <div class="tc-cont2">
        <button class="btn-ok">确定</button>
      </div>
    </div>
    <!--/推送成功-->




    <!--半透明背景-->
    <div class="bg"></div>
    <!--/半透明背景-->
  </div>
  <!--/.col-right-->
</div>
<!--/#content-->

<!--#foot-->
<%@ include file="../common_new/foot.jsp"%>
<!--#foot-->

<script id="selectionTmpl" type="text/template">
  <span class="top3" value="">全部</span>
  {{ for(var i in it) { }}
  <span value="{{=it[i].idStr}}">{{=it[i].value}}</span>
  {{ } }}
</script>

<script id="selectionTmpl2" type="text/template">
  {{ for(var i in it) { }}
  <dd>
    <div class="ml-title" value="{{=it[i].idStr}}">{{=it[i].value}}</div>
    <ul class="ml-ul" id="{{=it[i].idStr}}">
    </ul>
  </dd>
  {{ } }}
</script>

<script id="selectionTmpl3" type="text/template">
  {{ for(var i in it) { }}
      <li value="{{=it[i].idStr}}">{{=it[i].value}}</li>
  {{ } }}
</script>

<script id="itemListTmpl" type="text/template">
  {{ for(var i in it) { }}
  <div class="ti-list">
    <div class="ti-top"  id="{{=it[i].id}}">
      <span class="ti-span1">【题号】{{=it[i].id}}</span>
      <span class="ti-span2">【题型】{{=it[i].itemType}}</span>
      <button class="btn-x">删除</button>
      <button class="btn-bj">编辑</button>
      <button class="btn-ts">推送</button>
    </div>
    <div>{{=it[i].item}}</div>
    <div class="ti-top">
      <span>【答案】</span>
    </div>
    <div>{{=it[i].answer}}</div>
    <div class="ti-top">
      <span>【解析】</span>
    </div>
    <div>{{=it[i].parse}}</div>
  </div>
  {{ } }}
</script>

<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script src="/static_new/js/sea.js"></script>
<!-- Custom js -->
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
  var tiku;
  seajs.use('/static_new/js/modules/itempool/0.1.0/tiku', function(tiku1){
    tiku = tiku1;
  });
</script>
</body>
</html>
