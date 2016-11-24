<%--
  Created by IntelliJ IDEA.
  User: fl
  Date: 2016/1/29
  Time: 16:04
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<html>
<head>
    <title>管理员管理商品</title>
  <link rel="stylesheet" type="text/css" href="/static_new/js/modules/diyUpload/css/webuploader.css">
  <link rel="stylesheet" type="text/css" href="/static_new/js/modules/diyUpload/css/diyUpload.css">
  <!-- 新 Bootstrap 核心 CSS 文件 -->
  <link href="http://apps.bdimg.com/libs/bootstrap/3.3.0/css/bootstrap.min.css" rel="stylesheet">

  <style type="text/css">
    table{
      border-collapse: collapse;
    }
  </style>
</head>
<body ng-app="myApp" goodsId="${param.goodsId}">
  <div  class="container" style="width:100%;">
    <div class="col-md-2"></div>
    <div class="col-md-8" style="border-right:1px solid #aaa;border-left:1px solid #aaa;">
      <div style="color:#E43838">
        警告：此页面为复兰科技内部页面，仅限复兰科技内部使用<br><br>
        <t1 style="font-size:25px">注意：本页面所有价格单位为分！！！！！！！！！！<br>例如1元表示为100<br>123.45元表示为12345</t1>
      </div>
      <br>
      <br>

      <table ng-controller="myCtrl">
        <tr>
          <td>商品ID</td>
          <td><input id="goodsId" ng-model="goods.goodsId"><button id="getGoods" ng-click="getGoods()">获取商品信息</button></td>
        </tr>
        <tr>
          <td>商品名</td>
          <td><textarea id="goodsName" style="height: 100px;width: 500px;" ng-model="goods.goodsName" required="required"></textarea></td>
        </tr>
        <tr>
          <td>品牌</td>
          <td><input id="brand" ng-model="goods.brand"></td>
        </tr>
        <tr>
          <td>介绍</td>
          <td><textarea id="introduce" style="height: 100px;width: 500px;" ng-model="goods.introduction"></textarea></td>
        </tr>
        <tr>
          <td>基础价格</td>
          <td>
            <input id="price" ng-model="goods.price" required="required"
                     onkeyup="if(isNaN(value))execCommand('undo')"
                     onafterpaste="if(isNaN(value))execCommand('undo')">
            <span style="color:red">*必填</span></td>
        </tr>
        <tr>
          <td>基础折扣价格</td>
          <td>
            <input  ng-model="goods.discountPrice" required="required"
                    onkeyup="if(isNaN(value))execCommand('undo')"
                    onafterpaste="if(isNaN(value))execCommand('undo')">
            <span style="color:red">*必填</span></td>
        </tr>
        <tr>
          <td>积分可抵用</td>
          <td>
            <select  ng-model="expOff">
              <option value="0.00">0%</option>
              <option value="0.15">15%</option>
              <option value="0.20">20%</option>
              <option value="0.30">30%</option>
              <option value="0.50">50%</option>
              <option value="1.00">100%</option>
            </select>
          </td>
        </tr>
        <tr>
          <td>抵用券可抵用</td>
          <td>
            <select  ng-model="vouOff">
              <option value="0.00">0%</option>
              <option value="0.15">15%</option>
              <option value="0.20">20%</option>
              <option value="0.50">50%</option>
              <option value="1.00">100%</option>
            </select>
          </td>
        </tr>
        <tr>
          <td>运费模板</td>
          <td>
            <select id="expressTemplate" ng-model="goods.expressTemplateId" required="required">
              <option ng-repeat="expressTemplate in expressTemplateList" value="{{expressTemplate.id}}">{{expressTemplate.name}}</option>
            </select>
            <span style="color:red">*必填</span>
          </td>
        </tr>
        <tr>
          <td>产品分类</td>
          <td>
            <select ng-model="goods.goodsCategoryList" multiple required="required" id="category">
              <option ng-repeat="goodsCategory in goodsCategoryList" value="{{goodsCategory.id}}">{{goodsCategory.name}}</option>
            </select>
            <select ng-model="goods.levelGoodsCategoryList" multiple required="required" hidden id="levelCategory">
              <option ng-repeat="levelGoodsCategory in levelGoodsCategoryList" value="{{levelGoodsCategory.id}}">{{levelGoodsCategory.name}}</option>
            </select>
          </td>
        </tr>
        <tr>
          <td>教辅教材年级分类</td>
          <td>
            <select ng-model="goods.gradeCategoryList" multiple>
              <option ng-repeat="gradeCategory in gradeCategoryList" value="{{gradeCategory.id}}">{{gradeCategory.name}}</option>
            </select>
            <span style="color:red">*书籍绘本教辅教材类商品必填</span>
          </td>
        </tr>
        <tr>
          <td>课外阅读</td>
          <td>
            <select ng-model="goods.bookCategory">
              <option value="1">绘本</option>
              <option value="2">文学</option>
              <option value="3">科普百科</option>
            </select>
            <span style="color:red">*书籍绘本课外阅读类商品必填</span>
          </td>
        </tr>
        <tr>
          <td>封面图片</td>
          <td>
            <div id="suggestImg"></div>
          </td>
        </tr>
        <tr>
          <td>简介图片</td>
          <td>
            <div id="imgs"></div>
          </td>
        </tr>
        <tr>
          <td>详情图片</td>
          <td>
            <div id="html"></div>
          </td>
        </tr>
        <tr>
          <td>状态</td>
          <td>
            <select id="state" ng-model="goods.state" required="required">
              <option value="2">未上架</option>
              <option value="0">销售中</option>
              <option value="1">已下架</option>
            </select>
          </td>
        </tr>
        <tr>
          <td>活动专区</td>
          <td>
            <select id="activity" ng-model="goods.activity" required="required">
              <option value="0">六一活动</option>
            </select>
          </td>
        </tr>
        <tr>
          <td>购物类型</td>
          <td>
          <select id="groupPurchase" ng-model="goods.groupPurchase" required="required">
            <option value="0">团购</option>
            <option value="1">精品限时购</option>
          </select>
          </td>
        </tr>
        <tr>
          <td>销售数量</td>
          <td><input id="sellCount" readonly ng-model="goods.sellCount" style="background-color:#999"></td>
        </tr>
        <tr>
          <td>人气</td>
          <td><input id="popular" readonly ng-model="goods.popularLevel" style="background-color:#999"></td>
        </tr>
        <tr>
          <td>评价总计</td>
          <td>
            <span>5颗星</span><input class="cs5" readonly ng-model="goods.commentSummary[0].value"><br>
            <span>4颗星</span><input class="cs4" readonly ng-model="goods.commentSummary[1].value"><br>
            <span>3颗星</span><input class="cs3" readonly ng-model="goods.commentSummary[2].value"><br>
            <span>2颗星</span><input class="cs2" readonly ng-model="goods.commentSummary[3].value"><br>
            <span>1颗星</span><input class="cs1" readonly ng-model="goods.commentSummary[4].value"><br>
          </td>
        </tr>
        <tr>
          <td>规格</td>
          <td>
            <table class="table table-bordered">
              <thead>
              <tr><th>名称</th><th>列表</th></tr>
              </thead>
              <tbody id="kindList">
              <tr>
                <td align="center"><button id="addKind" ng-click="addKind()">增加</button></td>
                <td align="center"><button id="deleteKind" ng-click="deleteKind()">减少</button></td>
              </tr>
              <tr ng-repeat="kind in goods.kindDTOList">
                <td><input placeholder="例如：内存" ng-model="kind.kindName"></td>
                <td>
                  <table class="table table-bordered">
                    <thead><tr><th>名称</th><th>价格</th></tr></thead>
                    <tbody>
                    <tr>
                      <td align="center">
                        <button class="add" ng-click="add($index)">增加</button>
                      </td>
                      <td align="center">
                        <button class="delete" ng-click="delete($index)">减少</button>
                      </td>
                    </tr>
                    <tr ng-repeat="spec in kind.specList">
                      <td>
                        <input placeholder="例如：32G" ng-model="spec.name">
                      </td>
                      <td>
                        <input  placeholder="例如：10000" ng-model="spec.price">
                      </td>
                    </tr>
                    </tbody>
                  </table>
                </td>
              </tr>
              </tbody>
            </table>
          </td>
        </tr>
        <tr>
          <td colspan="2" align="center">
            <button class="btn btn-primary" style="position: absolute;left: 40%;" id="submit" ng-click="submit()">提交</button>
            <button class="btn btn-primary"  ng-click="preview()">预览</button>
          </td>
        </tr>
      </table>
    </div>
    <div class="col-md-2"></div>
  </div>



<script type="text/javascript" src="/static/js/jquery-1.11.1.min.js"></script>
<script src="/static_new/js/modules/diyUpload/js/webuploader.html5only.min.js"></script>
<script src="/static_new/js/modules/diyUpload/js/diyUpload.js"></script>
<script src="/static/plugins/angularjs/angular-1.2.26.min.js"></script>
<script src="/static_new/js/modules/mall/0.1.0/admin/upsetGoods.js"></script>
<script src="/static_new/js/modules/core/0.1.0/doT.min.js"></script>
<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
<script src="http://apps.bdimg.com/libs/bootstrap/3.3.0/js/bootstrap.min.js"></script>
</body>
</html>
