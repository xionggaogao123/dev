<%--
  Created by IntelliJ IDEA.
  User: fl
  Date: 2015/7/22
  Time: 17:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<html>
<head>
  <!-- Basic Page Needs-->
  <meta charset="utf-8">
  <link rel="dns-prefetch" href="//source.ycode.cn" />
  <title>复兰科技-校区管理</title>
  <meta name="description" content="">
  <meta name="author" content="" />
  <meta name="copyright" content="" />
  <meta name="keywords" content="">
  <meta name="viewport" content="width=device-width,initial-scale=1, maximum-scale=1">
  <!-- css files -->
  <!-- Normalize default styles -->
  <link href="/static_new/css/reset.css" rel="stylesheet" />
  <script type="text/javascript" src="/static/js/jquery-1.11.1.min.js"></script>
  <!-- jquery artZoom4Liaoba styles -->
  <!-- Custom styles -->
  <link href="/static_new/css/xiaoquguanli.css?v=2015041602" rel="stylesheet" />
</head>
<body>
<%@ include file="../common_new/head.jsp" %>
<div id="content" class="clearfix">
  <%@ include file="../common_new/col-left.jsp" %>
  <!--.col-right-->

    <!-- 广告部分 -->
    <%@ include file="../common/right.jsp" %>
    <!-- 广告部分 -->
  <div class="col-right">



    <!--.tab-col-->
    <div class="tab-col">
      <div class="tab-head clearfix">
        <ul>
          <li class="cur"><a href="javascript:;">校区管理</a></li>
        </ul>
      </div>
      <div class="liebiao">
        <p class="clearfix"><span>添加校区</span></p>
        <h4>校区信息</h4>
        <table width="100%">
          <thead>
          <tr>
            <th>校区名称</th>
            <th>校区地址</th>
            <th>校区电话</th>
            <th>负责人</th>
            <th>操作</th>
          </tr>
          </thead>
          <tbody id="campusList">
          <%--<tr>--%>
            <%--<td>安徽省滁州市第三中学人民东路校区</td>--%>
            <%--<td>安徽省滁州市人明东路365号</td>--%>
            <%--<td>8000 0008</td>--%>
            <%--<td>张三</td>--%>
            <%--<td>--%>
              <%--<span>编辑</span>--%>
              <%--<span>删除</span>--%>
            <%--</td>--%>
          <%--</tr>--%>
          </tbody>
        </table>
      </div>
      <!-- 弹出层 -->
      <div class="gay"></div>
      <div class="zjxq">
        <h6>增加校区<span class="gb"></span></h6>
        <div class="zjxq_main clearfix">
          <p>
            <span>校区名称</span>
            <input type="text" id="name">
          </p>
          <p>
            <span>校区地址</span>
            <input type="text" id="addr">
          </p>
          <p>
            <span>校区电话</span>
            <input type="text"  id="phone">
          </p>
          <p>
            <span>负责人</span>
            <input type="text" id="manager">
          </p>
          <button>确定</button>
        </div>
      </div>
      <!-- 弹出层 -->
    </div>
    <!--/.tab-col-->





  </div>
  <!--/.col-right-->

</div>
<!--/#content-->



<%@ include file="../common_new/foot.jsp" %>
<script id="campusListTmpl" type="text/template">
  {{ for(var i = 0, l = it.length; i < l; i++) {  }}
  <tr>
    <td>{{=it[i].name}}</td>
    <td>{{=it[i].addr}}</td>
    <td>{{=it[i].phone}}</td>
    <td>{{=it[i].manager}}</td>
    <td>
      <span id="edit" value="{{=it[i].campusId}}">编辑</span>
      <span id="delete" value="{{=it[i].campusId}}">删除</span>
    </td>
  </tr>
  {{ } }}
</script>

<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script src="/static_new/js/sea.js"></script>
<!-- Custom js -->
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
//  seajs.use('/static_new/js/modules/myschool/0.1.0/xiaoquguanli', function(xiaoqu) {
//    xiaoqu.initPage();
//  });
  seajs.use('/static_new/js/modules/myschool/0.1.0/xiaoquguanli');
</script>
</body>
</html>
