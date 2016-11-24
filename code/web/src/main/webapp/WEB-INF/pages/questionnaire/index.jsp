<%--
  Created by IntelliJ IDEA.
  User: Tony
  Date: 2015/1/8
  Time: 14:24
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE HTML>
<html>
<head>
    <title>问卷调查</title>
    <link rel="stylesheet" type="text/css" href="/static/css/style.css">
    <link rel="stylesheet" type="text/css" href="/static/css/k6kt.css">
    <link rel="stylesheet" type="text/css" href="/static/plugins/jquery-ui/themes/hot-sneaks/jquery-ui.css">
    <link rel="stylesheet" type="text/css" href="/static/css/font-awesome.min.css">
    <link rel="stylesheet" type="text/css" href="/static/css/questionnaire/style.css?v=1">
    <link rel="stylesheet" type="text/css" href="/static_new/css/reset.css" />
    <style type="text/css">
        #footer {
            position: fixed;
            bottom: 0;
        }
    </style>
</head>
<body ng-app="questionnaire">




<div class="questionnaire-out-div" ng-view ng-controller="QuestionCtrl" style="overflow:hidden"></div>

<jsp:include page="../common_new/foot.jsp"></jsp:include>

<script src="/static_new/js/modules/questionnaire/0.1.0/pdfobject.js"></script>
<%--pdf 预览 控件--%>
<script src="/static/js/exercise/plugins/util.js"></script>
<script src="/static/js/exercise/plugins/api.js"></script>
<script src="/static/js/exercise/plugins/metadata.js"></script>
<script src="/static/js/exercise/plugins/canvas.js"></script>
<script src="/static/js/exercise/plugins/webgl.js"></script>
<script src="/static/js/exercise/plugins/pattern_helper.js"></script>
<script src="/static/js/exercise/plugins/font_loader.js"></script>
<script src="/static/js/exercise/plugins/annotation_helper.js"></script>

<script type="text/javascript" src="/static/plugins/flexpaper/flexpaper_flash.js"></script>
<script type="text/javascript" src="/static/plugins/flexpaper/flexpaper_flash_debug.js"></script>

<script type="text/javascript" src="/static/plugins/es5-shim/es5-shim.min.js"></script>
<script type="text/javascript" src="/static/plugins/es5-shim/es5-sham.min.js"></script>
<script type="text/javascript" src="/static/js/jquery-1.11.1.min.js"></script>
<script type="text/javascript" src="/static/plugins/jquery-ui/jquery-ui.js"></script>
<script type="text/javascript" src="/static/plugins/jquery-ui/ui/i18n/datepicker-zh-CN.js"></script>
<script type="text/javascript" src="/static/js/sharedpart.js"></script>
<script type="text/javascript" src="/static/plugins/angular-1.2.28/angular.min.js"></script>
<script type="text/javascript" src="/static/plugins/angular-1.2.28/angular-route.min.js"></script>
<script type="text/javascript" src="/static/plugins/angular-file-upload/angular-file-upload.min.js"></script>
<script type="text/javascript" src="/static/plugins/angular-ui-date/src/date.js"></script>
<script type="text/javascript" src="/static/plugins/echarts/angular-echarts.js"></script>
<script type="text/javascript" src="/static/plugins/echarts/echarts-all.js"></script>

<script type="text/javascript" src="/static/plugins/ui-bootstrap-tpls-0.11.2.min.js"></script>

<script type="text/javascript" src="/static/js/filter.js"></script>
<script type="text/javascript" src="/static/js/questionnaire/app.js"></script>
<script type="text/javascript" src="/static/js/questionnaire/service.js?v=1"></script>
<script type="text/javascript" src="/static/js/questionnaire/controller.js"></script>




</body>
</html>
