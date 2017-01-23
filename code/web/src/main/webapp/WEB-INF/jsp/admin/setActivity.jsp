<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>管理员管理商品</title>
    <link rel="stylesheet" type="text/css" href="/static/js/modules/diyUpload/css/webuploader.css">
    <link rel="stylesheet" type="text/css" href="/static/js/modules/diyUpload/css/diyUpload.css">
    <!-- 新 Bootstrap 核心 CSS 文件 -->
    <link href="http://apps.bdimg.com/libs/bootstrap/3.3.0/css/bootstrap.min.css" rel="stylesheet">
    <script type="text/javascript" src="/static/js/WdatePicker.js"></script>
    <style type="text/css">
        table {
            border-collapse: collapse;
        }

        .account-input {
            vertical-align: top;;
            border: 1px solid darkgray;
            height: 28px;
            line-height: 22px;
            border-radius: 4px;
            width: 180px;
            padding: 0 10px;
            margin: 0 5px;
        }
    </style>
</head>
<body ng-app="myApp" goodsId="${param.fpostId}">

<div id="wrapper">

    <!-- Sidebar -->
    <%@include file="side_bar.jsp" %>
    <!-- /#sidebar-wrapper -->

    <!-- Page Content -->
    <div id="page-content-wrapper">
        <div class="container-fluid">
            <div class="row">
                <div class="container" style="width:75%;">
                    <div style="color:#E43838">
                        警告：此页面为复兰科技内部页面，仅限复兰科技内部使用
                    </div>
                    <table ng-controller="myCtrl" class="table table-bordered table-hover ">
                        <tr>
                            <td>帖子Id</td>
                            <td><input id="fpostId" ng-model="post.fpostId">
                            </td>
                        </tr>
                        <tr>
                            <td>帖子类型</td>
                            <td>
                                <select id="categray" ng-model="post.cate" required="required">
                                    <option value="-1">普通帖</option>
                                    <option value="1">大赛帖</option>
                                </select>
                            </td>
                        </tr>
                        <tr ng-if="post.cate == 1">
                            <td>活动名称</td>
                            <td><textarea id="activityMemo" style="height: 100px;width: 500px;"
                                          ng-model="post.activityMemo" required="required"></textarea></td>
                        </tr>
                        <tr>
                            <td>活动封面图片</td>
                            <td>
                                <div id="suggestImg"></div>
                            </td>
                        </tr>
                        <tr>
                            <td>banner首页图片</td>
                            <td>
                                <div id="headImage"></div>
                            </td>
                        </tr>
                        <tr ng-if="post.cate == 1">
                            <td>上线开始时间</td>
                            <td>
                                <input id="startTime" class="Wdate account-input" type="text"
                                       onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" value=""/>
                            </td>
                        </tr>
                        <tr ng-if="post.cate == 1">
                            <td>上线结束时间</td>
                            <td>
                                <input id="endTime" class="Wdate account-input" type="text"
                                       onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'startTime\')}'})"
                                       value="">
                            </td>
                        </tr>
                        <tr ng-if="post.cate == 1">
                            <td>控制是否上线</td>
                            <td>
                                <select id="inSet" ng-model="post.inSet" required="required">
                                    <option value="-1">下线</option>
                                    <option value="1">上线</option>
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td style='vertical-align: middle;text-align: center;' colspan="2">
                                <button class="btn btn-primary" id="submit" ng-click="submit()">提交</button>
                            </td>
                        </tr>
                    </table>
                </div>
            </div>
        </div>
    </div>
    <!-- /#page-content-wrapper -->

</div>

<link href="/static/css/side_bar.css" rel="stylesheet">
<!-- 新 Bootstrap 核心 CSS 文件 -->
<link rel="stylesheet" href="http://cdn.bootcss.com/bootstrap/3.3.0/css/bootstrap.min.css">

<!-- 可选的Bootstrap主题文件（一般不用引入） -->
<link rel="stylesheet" href="http://cdn.bootcss.com/bootstrap/3.3.0/css/bootstrap-theme.min.css">

<!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->
<script src="http://cdn.bootcss.com/jquery/1.11.1/jquery.min.js"></script>

<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
<script src="http://cdn.bootcss.com/bootstrap/3.3.0/js/bootstrap.min.js"></script>

<script type="text/javascript" src="/static/js/jquery-1.11.1.min.js"></script>
<script src="/static/js/modules/diyUpload/js/webuploader.html5only.min.js"></script>
<script src="/static/js/modules/diyUpload/js/diyUpload.js"></script>
<script src="/static/plugins/angularjs/angular-1.2.26.min.js"></script>
<script src="/static/js/modules/mall/0.1.0/admin/setActivity.js"></script>
<script src="/static/js/modules/core/0.1.0/doT.min.js"></script>
<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
<script src="http://apps.bdimg.com/libs/bootstrap/3.3.0/js/bootstrap.min.js"></script>
</body>
</html>
