<%@ page import="com.pojo.lesson.DirType" %>
<%--
  Created by IntelliJ IDEA.
  User: fl
  Date: 2016/3/23
  Time: 14:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
  <!-- Basic Page Needs-->
  <meta charset="utf-8">
  <link rel="dns-prefetch" href="//source.ycode.cn" />
  <title>复兰科技-党建</title>
  <meta name="description" content="">
  <meta name="author" content="" />
  <meta name="copyright" content="" />
  <meta name="keywords" content="">
  <meta name="viewport" content="width=device-width,initial-scale=1, maximum-scale=1">
  <link rel="shortcut icon" href="" type="image/x-icon" />
  <link rel="icon" href="" type="image/x-icon" />
  <!-- css files -->
  <!-- Normalize default styles -->
  <link href="/static_new/css/reset.css" rel="stylesheet" type="text/css">
  <!-- jquery artZoom4Liaoba styles -->
  <!-- Custom styles -->
  <link rel="stylesheet" type="text/css" href="/static_new/css/dangjianupload.css"/>
  <link rel="stylesheet" type="text/css" href="/static/css/zTreeStyle/zTreeStyle.css"/>
  <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery.min.js?v=1"></script>
  <script src="/static/js/lessons/coursesManage.js"></script>
  <script src="/static_new/js/modules/core/0.1.0/jquery-upload/jquery.fileupload.js"></script>
  <script type="text/javascript" src="/static/js/sharedpart.js"></script>
</head>
<body id="${id}" pmn="${partyManger}" pm="${partyMember}" te="${teacher}">


<!--#head-->
<%@ include file="../common_new/head.jsp" %>
<!--/#head-->

<!--#content ceshi-->
<div class="dangjianup_content">
  <div class="dangjianup_inf">
    <div class="dangjianup_inf_title">
      <p>资料信息</p>
    </div>
    <div class="dangjian_inf_step">
      <div>
        <span>资料名称</span><input type="text" class="dangjiang_inf_inp" id="name">
      </div>
      <div>
        <span>选择类型</span>
        <input type="radio" name="type" ty="7"><em>学习园地</em>
        <input type="radio" name="type" ty="8"><em>文明创建</em>
        <input type="radio" name="type" ty="9"><em>专题教育</em>
        <input type="radio" name="type" ty="10"><em>志愿服务</em>
        <input type="radio" name="type" ty="11"><em>党课活动</em>
        <br>
        <input type="radio" name="type" ty="12" class="dangjian_inf_check"><em>队伍建设</em>
        <input type="radio" name="type" ty="13"><em>共建活动</em>
        <input type="radio" name="type" ty="14"><em>党员风采</em>
        <input type="radio" name="type" ty="15"><em>工作布置</em>
        <input type="radio" name="type" ty="16"><em>组织生活</em>
      </div>
      <div class="div-bot">
        <span class="dangjian_step_span">选择文件夹</span>
        <div class="dangjian_step_select">
          <ul id="DirUl" class="ztree dir-tree">
          </ul>

        </div>
        <br>
        <button id="newFolder">新建文件夹</button>
      </div>
    </div>
  </div>
  <div class="dangjian_upload">
    <div class="dangjian_zlup">
      <span>上传资料</span>
    </div>
    <a href="#" class="dangjian_upload_select">
      <input type="file" id="file_attach">
      <em>选择文件</em>
    </a>
    <p>支持的文件格式:doc/docx/ppt/pptx/xls/xlsx</p>
    <div id="progress">
      <div class="bar" style="width: 0%;"></div>
    </div>
    <div>
        <div class="dangjian_file clearfix"  id="files">
          <%--<div class="file">--%>
            <%--<a href="http://7xiclj.com1.z0.glb.clouddn.com/56f4ff2c2947d9289df65ac4.doc" class="courselink">--%>
              <%--<img class="dangjian_pic" src="/img/docicon/doc.png">--%>
            <%--</a>--%>
            <%--<img class="dangjina_del" src="/img/dangjianup_del.png">--%>
            <%--<p>复兰商城使用规则复兰商城使用规则复兰商城使用规则</p>--%>
          <%--</div>--%>
          <%--<div class="file">--%>
            <%--<a href="http://7xiclj.com1.z0.glb.clouddn.com/56f4ff4c2947d9289df65ac6.xls" class="courselink">--%>
              <%--<img class="dangjian_pic" src="/img/docicon/xls.png">--%>
            <%--</a>--%>
            <%--<img class="dangjina_del" src="/img/dangjianup_del.png">--%>
            <%--<p title="复兰商城使用规则复兰商城使用规则复兰商城使用规则">复兰商城使用规则复兰商城使用规则复兰商城使用规则</p>--%>
          <%--</div>--%>
          <%--<div class="file">--%>
            <%--<a href="http://7xiclj.com1.z0.glb.clouddn.com/56f5036f2947d9289df65ac8.pptx" class="courselink">--%>
              <%--<img class="dangjian_pic" src="/img/docicon/ppt.png">--%>
            <%--</a>--%>
            <%--<img class="dangjina_del" src="/img/dangjianup_del.png">--%>
            <%--<p>复兰商城使用规则</p>--%>
          <%--</div>--%>
          <%--<div class="file">--%>
            <%--<a href="http://7xiclj.com1.z0.glb.clouddn.com/56f506a82947d9289df65acd.jpg" class="courselink">--%>
              <%--<img class="dangjian_pic" src="/img/docicon/unknow.png">--%>
            <%--</a>--%>
            <%--<img class="dangjina_del" src="/img/dangjianup_del.png">--%>
            <%--<p>复兰商城使用规则</p>--%>
          <%--</div>--%>

        </div>

    </div>

    <button id="savedto">保存</button>
  </div>
</div>
<!--/#content-->



<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script src="/static_new/js/sea.js"></script>
<!-- Custom js -->
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
  var dirType = '<%=DirType.getDirType(7)%>';
  var dirTypes = [];
  dirTypes[1] = '<%=DirType.getDirType(1)%>';
  dirTypes[3] = '<%=DirType.getDirType(3)%>';
  dirTypes[7] = '<%=DirType.getDirType(7)%>';
  dirTypes[8] = '<%=DirType.getDirType(8)%>';
  dirTypes[9] = '<%=DirType.getDirType(9)%>';
  dirTypes[10] = '<%=DirType.getDirType(10)%>';
  dirTypes[11] = '<%=DirType.getDirType(11)%>';
  dirTypes[12] = '<%=DirType.getDirType(12)%>';
  dirTypes[13] = '<%=DirType.getDirType(13)%>';
  dirTypes[14] = '<%=DirType.getDirType(14)%>';
  dirTypes[15] = '<%=DirType.getDirType(15)%>';
  dirTypes[16] = '<%=DirType.getDirType(16)%>';
  seajs.use('/static_new/js/modules/dangjian/0.1.0/dj_upload.js');

</script>

</body>
</html>
