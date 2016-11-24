<%--
  Created by IntelliJ IDEA.
  User: fl
  Date: 2016/1/27
  Time: 10:55
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
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
      <h3>商品一级分类</h3>
      <input ng-model="eGoodsCategory.name">
      <button ng-click="addGoodsCategory(1)">添加</button>
      <ul class="list-group">
        <li id="{{category.id}}" class="list-group-item" ng-repeat="category in eGoodsCategoryList">
          <span name="nameSpan" class="nameText">{{category.name}}</span>
          <input name="nameInput" name="categoryName" ng-model="category.name" style="display: none;">
          <i ng-click="deleteGoodsCategory(category.id,1,'')" style="position: relative; left: 60px;cursor: pointer;">X</i>
          <span name="edit" ng-click="edit()" style="position: relative; left: 80px;cursor: pointer;">编辑</span>
          <span name="submit" ng-click="updateGoodsCategory(category.id,category.name,category.parentId,category.level,category.sort)" style="position: relative; left: 80px;cursor: pointer;display: none;">确定</span>
          <span style="position: relative; left: 100px;cursor: pointer;" ng-click="up()">↑</span>
          <span style="position: relative; left: 110px;cursor: pointer;" ng-click="down()">↓</span>
          <%--<span style="position: relative; left: 140px;cursor: pointer;" ng-click="uploadImg(category.id)">上传首页图片</span>--%>
          <span style="position: relative; left: 160px;cursor: pointer;" ng-click="uploadMobileImg(category.id)">上传移动端首页图片</span>
          <span style="position: relative; left: 160px;cursor: pointer;" ng-click="uploadMobileCategoryImg(category.id)">上传移动端分类图片</span>
          <span style="position: relative; left: 160px;cursor: pointer;" ng-click="addSecondaryCategory(category.id)">添加二级分类</span>
        </li>
      </ul>

      <div id="upload" style="position:absolute;top:300px;left:50%;border:1px solid #aaa;background-color:#aaa;padding:5px;display:none;">
        <input id="img" type="file" name="file" value="上传图片"
               size="1" style="width: 200px; height: 20px; opacity: 1"
               accept="image/*">
        <button class="btn btn-primary" style="margin:10px 70px;" ng-click="closeImg()">取消</button>
      </div>

      <div id="addSecondary" style="position:absolute;top:300px;left:50%;border:1px solid #aaa;background-color:#aaa;padding:5px;display:none;">
           <span>二级分类:<input id="aS" name="aS"></span><span id="tip" hidden>填写二级分类不能为空</span>
           <button ng-click="submit()">提交</button>
           <button class="btn btn-primary" style="margin:10px 70px;" ng-click="closeDialog()">取消</button>
           <ul class="list-group">
               <li id="{{categoryl.id}}" class="list-group-item" ng-repeat="categoryl in eGoodsCategoryListl">
               <span name="nameSpan" class="nameText">{{categoryl.name}}</span>
               <input name="nameInput" name="categoryName" ng-model="categoryl.name" style="display: none;">
               <i ng-click="deleteGoodsCategory(categoryl.id,2,categoryl.parentId)" style="position: relative; left: 60px;cursor: pointer;">X</i>
               <span name="edit" ng-click="edit()" style="position: relative; left: 80px;cursor: pointer;">编辑</span>
               <span name="submit" ng-click="updateGoodsCategory(categoryl.id,categoryl.name,categoryl.parentId,categoryl.level,categoryl.sort)" style="position: relative; left: 80px;cursor: pointer;display: none;">确定</span>
          </li>
        </ul>
      </div>

    </div>
    <div class="col-md-2"></div>
  </div>

  <%--javascript--%>
  <script src="http://apps.bdimg.com/libs/jquery/2.0.0/jquery.min.js"></script>
  <script src="/static/plugins/angularjs/angular-1.2.26.min.js"></script>
  <script src="http://apps.bdimg.com/libs/bootstrap/3.3.0/js/bootstrap.min.js"></script>
  <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery-upload/vendor/jquery.ui.widget.js?v=1"></script>
  <script type="text/javascript" src="/static/plugins/jquery-upload/jquery.fileupload.js"></script>
  <script src="/static_new/js/modules/mall/0.1.0/admin/category.js"></script>
</body>
</html>
