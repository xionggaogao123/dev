<%--
  Created by IntelliJ IDEA.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
  <!-- Basic Page Needs-->
  <meta charset="utf-8">
  <title>教学计划</title>
  <!-- css files -->
  <!-- Normalize default styles -->
  <meta name="renderer" content="webkit">
  <link rel="stylesheet" type="text/css" href="/static_new/css/teacherevaluation/teaPlan.css">
  <link rel="stylesheet" type="text/css" href="/static_new/css/reset.css">
  <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery.min.js?v=1"></script>
  <script type="text/javascript" src="/static/plugins/ueditor/ueditor.parse.js"></script>
  <script type="application/javascript">
    var UEDITOR_HOME_URL = "/static/plugins/ueditor/";
    var UEDITOR_IMG_UPLOAD_URL = "/upload/";
//    var ue=new UE.ui.Editor({
//
//    });
//    ue.render('ueditor_0');
//
//    function getContentText()
//    {
//      return ue.getContent();
//    }
  </script>
</head>

<body>
<!--#head-->
<%@ include file="../common_new/head.jsp"%>
<!--/#head-->

<!--#content-->
<div id="content" class="clearfix">

  <!--.col-left-->
  <%@ include file="../common_new/col-left.jsp" %>
  <!--/.col-left-->
  <c:choose>
    <c:when test="${roles:isStudentOrParent(sessionValue.userRole)}">
      <jsp:include page="/WEB-INF/pages/common/right_2.jsp"></jsp:include>
    </c:when>
    <c:otherwise>
      <jsp:include page="/WEB-INF/pages/common/right.jsp"></jsp:include>
    </c:otherwise>
  </c:choose>
  <!--.col-right-->
  <div class="col-right">
    <input hidden="hidden" id="userId" value="${userId}">
    <input hidden="hidden" id="term" value="${term}">
    <!--.tab-col右侧-->
    <div class="tab-col">
      <div class="plan-title"><em class="path-root">教学计划</em><em class="next-path" style="display:none;">&gt;&nbsp;<span id="title">新增教学计划</span></em></div>
      <div class="plan-con">
        <div class="plan-set clearfix">
          <div class="plan-set-l fl">
            <select class="select-year" id="termlist2">
              <c:forEach items="${termList}" var="term">
                <option value="${term}">${term}</option>
              </c:forEach>
            </select>
            <input type="text" placeholder="搜索关键字：教学计划名称" id="planName2"/>
            <button class="plan-search">查询</button>
          </div>
          <div class="plan-set-r fr">
            <button class="plan-newadd">新增教学计划</button>
          </div>
        </div>
        <table class="newTable">
          <thead>
          <tr>
            <th style="width:186px;">年度</th>
            <th style="width:385px;">教学计划</th>
            <th style="width:175px;">操作</th>
          </tr>
          </thead>
          <tbody class="planlist">

          </tbody>
          <script type="text/template" id="planlist_templ">
            {{ if(it.rows.length>0){ }}
            {{ for (var i = 0, l = it.rows.length; i < l; i++) { }}
            {{var obj=it.rows[i];}}
            <tr>
              <td>{{=obj.term}}</td>
              <td>{{=obj.plainName}}</td>
              <td pid="{{=obj.id}}">
                <a href="javascript:;" class="table-look view-plan">查看</a>
                <c:if test="${userId==''}">
                |
                <a href="javascript:;" class="table-look udp-plan">编辑</a>|
                <a href="javascript:;" class="table-look del-plan">删除</a>
                </c:if>
              </td>
            </tr>
            {{ } }}
            {{ } }}
          </script>
        </table>
      </div>
      <div class="addplan-con" style="display:none;">
        <div class="newadd-con">
          <div class="year-select">
            <input id="planid" hidden="hidden">
            <span class="select-nian">选择学期</span>
            <select class="select-year" id="termlist">
              <c:forEach items="${termList}" var="term">
                <option value="${term}">${term}</option>
              </c:forEach>
            </select>
          </div>
          <div class="year-select">
            <span class="select-nian">计划名称</span>
            <input id="planName">
            </div>
          <div class="plan-main">
            <span class="tea-plan">教学计划</span>
            <textarea id="contentShow"></textarea>
            <div class="inform-BJ">
              <!-- 配置文件 -->
              <script type="text/javascript"
                      src="/static/plugins/ueditor/ueditor.config.js"></script>
              <!-- 编辑器源码文件 -->
              <script type="text/javascript"
                      src="/static/plugins/ueditor/ueditor.all.js"></script>
              <style type="text/css">
                .edui-default .edui-editor {
                  width: 696px !important;
                  margin-left: 40px;
                }

                .edui-default .edui-editor-iframeholder {
                  width: 696px !important;
                }
              </style>
              <!-- 实例化编辑器 -->
              <script type="text/javascript">
                var ue = UE.getEditor('contentShow',{autoHeightEnabled :false});
              </script>
            </div>
            <label for="file_attach" class="fileAtt"> <img
                    src="/img/fileattach.png"/> 添加附件
            </label>

            <div style="width: 0; height: 0; overflow: visible">
              <input id="file_attach" type="file" name="file" value="添加附件"
                     size="1" style="width: 0; height: 0; opacity: 0">
            </div>
            <img src="/img/loading4.gif" id="fileuploadLoading" style="display: none;"/>
            <br/>
            <br/>
            <div id="fileListShow" style="padding-left: 40px;padding-bottom: 40px;">
            </div>

            <script type="application/template" id="fileListJs">
              {{?it.data.length>0}}
              <span>附件：</span>
              {{?}}
              {{~it.data:value:index}}

              <a href="{{=value.path}}" fn="{{=value.name}}" target="_blank">{{=value.name}}(我上传的)</a>
              <i class="removeFile" did="{{=value.path}}">x</i>
              {{~}}
            </script>
          </div>
          <div class="btn-wrap">
            <button class="btn-sure">确定</button>
            <button class="btn-esc">取消</button>
          </div>
        </div>
      </div>

    </div>
    <!--/.tab-col右侧-->

  </div>
  <!--/.col-right-->

</div>
<!--/#content-->
</div>
</div>
<div class="bg"></div>

<div class="popup-info" style="display:none;z-index: 990000009;">
  <dl>
    <dt>
      <em>提醒</em>
    </dt>
    <dd>
      <em></em>
    </dd>
    <dd>
      <em class="popup-op">确定要撤销改公文吗？</em>
    </dd>
    <dd>
      <em>
        <button class="he_qd">确定</button>
        <button class="he_qx">取消</button>
      </em>
    </dd>
  </dl>
</div>
<!--#foot-->
<%@ include file="../common_new/foot.jsp"%>
<!--#foot-->
<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script src="/static_new/js/sea.js"></script>
<!-- Custom js -->
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
  seajs.use('/static_new/js/modules/teacherevaluation/0.1.0/teaPlan');
</script>

</body>
</html>
