<%--
  Created by IntelliJ IDEA.
  User: wang_xinxin
  Date: 2015/8/19
  Time: 17:07
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="roles" uri="http://fulaan.userRole.com" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <!-- Basic Page Needss-->
  <meta charset="utf-8">
  <link rel="dns-prefetch" href="//source.ycode.cn" />
  <title>微课大赛</title>
  <meta name="description" content="">
  <meta name="author" content="" />
  <meta name="copyright" content="" />
  <meta name="keywords" content="">
  <meta name="viewport" content="width=device-width,initial-scale=1, maximum-scale=1">
  <link href="/static_new/css/reset.css" rel="stylesheet" />
  <link href="/static_new/css/microlesson/microlesson.css?v=2015041602" rel="stylesheet" />
  <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery.min.js?v=1"></script>
  <script type="text/javascript" src="/static/plugins/ueditor/ueditor.parse.js"></script>



  <script type="application/javascript">

    function deltype(type) {
      if (confirm("删除分类相对应的课程也被删除，确认删除？")) {
        type.parentNode.remove();
      }
    }
    function deltype2(type) {
      if (confirm("删除打分分类相对应的分类也被删除，确认删除？")) {
        type.parentNode.remove();
      }
    }

  </script>
  
  
     <script type="application/javascript">
        var UEDITOR_HOME_URL = "/static/plugins/ueditor/";
        var UEDITOR_IMG_UPLOAD_URL = "/upload/";
        var ue=new UE.ui.Editor({

        });
        ue.render('ueditor_0');

        function getContentText()
        {
            return ue.getContent();
        }
    </script>
</head>
<body matchid="${matchid}">
<!--#head-->
<%@ include file="../common_new/head.jsp"%>
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

    <!--.tab-col右侧-->
    <div class="tab-col faqibisai">
      <h3>编辑比赛</h3>
      <ul>
        <li>
          <label>比赛名称</label>
          <input type="text" id="matchname">
        </li>
        <li>
          <label>比赛图片</label>
          <img src="" width="75" height="75" id="matchpic" style="display: none;">
          <i id="img-x" style="display: none;">x</i>
          <label class="micromatch_bt" id="upload-blog-img" for="image-upload" style="cursor:pointer;">
            <span class="micromatch_btt">上传</span>
          </label>
          <div class="size-zero" style="display: none;">
            <input type="file" name="image-upload" id="image-upload" accept="image/*" multiple="multiple"/>
          </div>
          <%--<img src="/img/loading4.gif" id="picuploadLoading"/>--%>
        </li>
        <li>
          <label>打分项</label>
          <button id="scoretype" style="width: 130px;" disabled>
            <span>+</span>
            新增打分项
          </button>
          <span style="color: #ff8a00;">打分项可以不设，如果设置总分为所有项之和。保存完不能修改。</span>
          <dl id="stype">
          </dl>
        </li>
        <li>
          <label>选择分类</label>
          <button id="addtype">
            <span>+</span>
            新增分类
          </button>
          <dl id="mtype" class="matype">

          </dl>
        </li>
        <li class="tops">
          <label>说明</label>
          <div class="fwbkj">
            <%--<div class="tou"></div>--%>
            <%--<textarea></textarea>--%>
            <textarea id="ueditor" class="content_manage_ter" name="content"></textarea>
              <div class="inform-BJ">
                <script id="container" name="content" type="text/plain">

                </script>
                <!-- 配置文件 -->
                <script type="text/javascript"
                        src="/static/plugins/ueditor/ueditor.config.js"></script>
                <!-- 编辑器源码文件 -->
                <script type="text/javascript"
                        src="/static/plugins/ueditor/ueditor.all.js"></script>
                <!-- 实例化编辑器 -->
                <style type="text/css">
                  .edui-default .edui-editor {
                    width: 480px !important;
                  }

                  .edui-default .edui-editor-iframeholder {
                    width: 480px !important;
                  }
                </style>
                <script type="text/javascript">
                  var ue = UE.getEditor('ueditor');
                </script>
              </div>
          </div>
        </li>
        <li class="tops">
          <label>评委</label>
          <textarea id="roteuser" disabled="disabled" readonly="readonly"></textarea>
          <span>添加评委</span>
        </li>
        <li>
          <label>参赛时间</label>
          <input type="text" class="rili" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})" id="begintime">
          <span>至</span>
          <input type="text" class="rili" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})" id="endtime">
        </li>
        <li>
          <label>打分时间</label>
          <input type="text" class="rili" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})" id="scorebegintime">
          <span>至</span>
          <input type="text" class="rili" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})" id="scoreendtime">
        </li>
      </ul>
      <a href="javascript:;" class="kaishi" id="updmatch">保存</a>
    </div>
    <!--/.tab-col右侧-->

  </div>
  <!--/.col-right-->

</div>
<!--/#content-->




<!--#foot-->
<%@ include file="../common_new/foot.jsp" %>
<!--#foot-->
<!-- 添加评委的弹出层 -->
<div class="gay"></div>
<div class="hylb">
  <p>添加评委<span class="gb"></span></p>
  <div class="hylb_main clearfix">
    <div class="hylb_left">
      <div class="b">
        <span>联系人</span>
        <textarea id="matchusers" disabled="disabled" readonly="readonly"></textarea>
        <button class="sureuser">确认</button>
        <button class="gry">取消</button>
      </div>
    </div>
    <div class="adress"></div>
  </div>
</div>
<script type="text/template" id="adress_templ">
  <div class="hylb_right">
    <ul>
      {{ if(it.rows.length>0){ }}
      {{ for (var i = 0, l = it.rows.length; i < l; i++) { }}
      {{var obj=it.rows[i];}}
      <div class="bj jb"><span>{{=obj.schoolname}}</span></div>
      <ol style="display: none;">
        {{ for (var j = 0, k = obj.userlist.length; j < k; j++) { }}
        {{var user=obj.userlist[j];}}
        <li class="usreli" userid="{{=user.userid}}" username="{{=user.userName}}">{{=user.userName}}</li>
        {{ } }}
      </ol>
      {{ } }}
      {{ } }}
    </ul>
  </div>

</script>

<div class="adtp">
  <p>新建分类<span class="gb"></span></p>
  <div class="hylb_main clearfix">
    <div class="hylb_left">
      <div class="b">
        <input placeholder="新建分类" id="classifyc"><br>
        <button id="typesuer">确认</button>
        <button class="tygry">取消</button>
      </div>
    </div>
  </div>
</div>
<div class="adtp2" style="left: 759px; top: 402.5px;">
  <p>新建打分项<span class="gb"></span></p>
  <div class="hylb_main clearfix">
    <div class="hylb_left">
      <div class="b">
        <input placeholder="新建打分分类" id="classifyc2"><br>
        <input placeholder="最高分值" id="score" style="margin-top: 10px;"><br>
        <button id="typesuer2">确认</button>
        <button class="tygry2">取消</button>
      </div>
    </div>
  </div>
</div>
<!-- 添加评委的弹出层 -->
<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script src="/static_new/js/sea.js"></script>
<!-- Custom js -->
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
  seajs.use('editmatch');
</script>
</body>
</html>
