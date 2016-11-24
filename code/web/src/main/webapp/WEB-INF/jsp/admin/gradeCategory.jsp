<%--
  Created by IntelliJ IDEA.
  User: wangkaidong
  Date: 2016/4/8
  Time: 18:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>商品分类管理</title>
    <link href="http://apps.bdimg.com/libs/bootstrap/3.3.0/css/bootstrap.min.css" rel="stylesheet">

    <style type="text/css">
    </style>
</head>
<body ng-app="myApp" ng-controller="myCtrl">
<div class="container" style="width:100%;">
    <div class="col-md-2"></div>
    <div class="col-md-8">
        <div style="color:#E43838">
            警告：此页面为复兰科技内部页面，仅限复兰科技内部使用
        </div>
        <br>
        <br>
        <h3>年级分类</h3>
        <input ng-model="eGradeCategory.name">
        <button ng-click="addGradeCategory()">添加</button>
        <ul class="list-group">
            <li id="{{category.id}}" class="list-group-item" ng-repeat="category in eGradeCategoryList">
                <span>{{category.name}}</span>
                <span ng-click="delete(category.id)" style="position:absolute;left:200px;cursor:pointer;">删除</span>
            </li>
        </ul>

    </div>
    <div class="col-md-2"></div>
</div>

<%--javascript--%>
<script src="http://apps.bdimg.com/libs/jquery/2.0.0/jquery.min.js"></script>
<script src="/static/plugins/angularjs/angular-1.2.26.min.js"></script>
<script src="http://apps.bdimg.com/libs/bootstrap/3.3.0/js/bootstrap.min.js"></script>
<script src="/static/js/modules/mall/0.1.0/admin/gradeCategory.js"></script>
</body>
</html>
