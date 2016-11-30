<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>地址管理</title>
    <link href="/static/css/style.css" rel="stylesheet" type="text/css"/>
    <link href="/static/css/main.css" rel="stylesheet" type="text/css">
    <style type="text/css">
        .store-car {
            position: relative;
            top: 0px;;
        }
    </style>
</head>
<body>
<%@ include file="mallSectionHead.jsp" %>
<div style="border-top:4px solid #ff510d"></div>
<div class="ebusiness-T">
    <ul class="ul-nav-s" id="listAc">
    </ul>
    <%@include file="leftmenu.jsp" %>
    <div class="ebusiness-main-right">
        <div class="ebusiness-history-main">
            <div class="coupon-top">
                <ul>
                    <li>我的地址</li>
                </ul>
            </div>
            <div class="div-address">
                <h4>已保存的地址
                    <em id="addAddress">+新增收货地址</em>
                </h4>
                <table id="addressTable" class="table-address"><%--地址列表--%>
                </table>
            </div>
            <%--地址列表模板--%>
            <script id="addressScript" type="application/template">
                <tr>
                    <th class="th1">收货人</th>
                    <th class="th2">收货地址</th>
                    <th class="th3">联系电话</th>
                    <th class="th4">操作</th>
                    <th class="th5"></th>
                </tr>
                {{~it:value:index}}
                <tr>
                    <td>{{=value.userName}}</td>
                    <td>
                        <span>{{=value.province}} {{=value.city}} {{=value.district}} {{=value.address}}</span>
                    </td>
                    <td>{{=value.telephone}}</td>
                    <td class="td-center">
                        <em onclick="editAddress('{{=value.id}}','{{=value.userName}}','{{=value.province}}','{{=value.city}}','{{=value.district}}','{{=value.address}}','{{=value.telephone}}')">修改</em>
                        <em>|</em><em addressId="{{=value.id}}" class="del">删除</em>
                    </td>
                    <td class="td-center" isDefault="{{=value.isDefault}}" id="{{=value.id}}">
                        <button>默认地址</button>
                        <span>设为默认地址</span>
                    </td>
                </tr>
                {{~}}
            </script>
            <%--新增地址--%>
            <div id="addresseDiv" style="display: none;">
                <div class="store-affirm-info">
                    <dl>
                        <dd class="store-DZ">
                            <p>新增收货地址</p>
                        </dd>
                        <dd>
                            <em>*收货人：</em><input class="store-TE" type="text" id="user_input">
                        </dd>

                        <dd>
                            <em>*省市区：</em>
                            <select id="province">
                                <option value="">请选择</option>
                            </select>
                            <select id="city">
                                <option value="">请选择</option>
                            </select>
                            <select id="district">
                                <option value="">请选择</option>
                            </select>
                        </dd>
                        <script id="proviceTempJS" type="application/template">
                            {{~it:value:index}}
                            <option value="{{=value.name}}" pid="{{=value.id}}">{{=value.fullname}}</option>
                            {{~}}
                        </script>
                        <script id="cityTempJS" type="application/template">
                            {{~it:value:index}}
                            <option value="{{=value.name}}" cid="{{=value.id}}">{{=value.fullname}}</option>
                            {{~}}
                        </script>
                        <script id="districtTempJS" type="application/template">
                            <option value="">--</option>
                            {{~it:value:index}}
                            <option value="{{=value.fullname}}" did="{{=value.id}}">{{=value.fullname}}</option>
                            {{~}}
                        </script>
                        <dd>
                            <em>*详细地址：</em><input class="store-TEE" type="text" id="address_input">
                        </dd>
                        <dd>
                            <em>*手机号码：</em><input class="store-TE" type="text" id="telephone_input">
                        </dd>
                        <dd>
                            <button id="submit" addressId="">保存收货人信息</button>
                        </dd>
                    </dl>
                </div>
            </div>
        </div>
    </div>
</div>


<div class="bg"></div>
<%--提示弹窗--%>
<div class="notice wind">
    <p>提示</p>
    <div class="wind-cont"></div>
    <button>确定</button>
</div>
<%--确定弹窗--%>
<div class="confirm wind" adressId="">
    <p>提示</p>
    <div class="wind-cont">您确定要删除该地址吗？</div>
    <button class="yes" addressId="">确定</button>
    <button class="no">取消</button>
</div>
<!--=============底部版权=================-->
<%@ include file="../common/footer.jsp" %>

<script id="listAcTml" type="text/template">
    <li class="li1" onclick="window.open('/mall/mallSection.do')">商城专区首页<em>></em>
        <div class="ul-nav-bg "></div>
    </li>
    <li class="li2" onclick="window.open('/integrate.do')"><span>节日特惠专区</span><em>></em></li>
    <li class="li2" onclick="window.open('/mall/discount.do')"><span>全积分兑换专区</span><em>></em></li>
    {{ for(var i in it) { }}
    <li class="li2"><span value="{{=it[i].id}}" class="listData"></span><em>></em></li>
    {{ } }}
    <li class="li2" onclick="window.open('/mall/index.do')"><span>全部商品</span><em>></em></li>
</script>

<script id="listTml" type="text/template">
    {{ for(var i in it) { }}
    <span onclick="window.open('/mall/index.do?categoryId={{=it[i].parentId}}&levelCategoryId={{=it[i].id}}')">{{=it[i].name}}</span> /
    {{ } }}
</script>
<!-- Javascript -->
<script src="/static/js/sea.js"></script>
<script src="/static/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('addressManage', function (addressManage) {
        addressManage.init();
    });
</script>
</body>
</html>
