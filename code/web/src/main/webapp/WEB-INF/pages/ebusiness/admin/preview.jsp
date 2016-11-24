<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: fl
  Date: 2016/1/15
  Time: 13:04
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
  <title>复兰商城</title>
  <link rel="stylesheet" type="text/css" href="/static/css/style.css"/>
  <link rel="stylesheet" type="text/css" href="/static/css/main.css"/>
  <link href="/static_new/js/modules/core/0.1.0/fancyBox/jquery.fancybox.css?v=2015041602" rel="stylesheet" type="text/css" media="screen"/>
  <!-- Start of KF5 supportbox script -->
  <script src="//assets.kf5.com/supportbox/main.js" id="kf5-provide-supportBox" kf5-domain="fulankeji.kf5.com"></script>
  <!-- End of KF5 supportbox script -->
  <script type="text/javascript" src="/static/js/jquery-1.11.1.min.js"></script>
</head>
<body>
<%--===============头部================--%>
<%--<%@ include file="../../mall/head.jsp" %>--%>
<%--===============头部================--%>

<!--===============电子商城==================-->
<div class="store-home-top">
  <div class="store-top-center">
    <a href="/mall" class="store-top-SCSY" href="">商城首页</a>
    <a href="/mall">复兰商城&nbsp;&nbsp;&gt;&nbsp;&nbsp;</a><em id="title"></em>
    <div>
      <a>
        <em id="car">我的购物车</em><i id="goodsCount">（0件商品）</i>
      </a>
    </div>
  </div>
</div>
<!--================查看商品==============-->
<div class="store-check">
  <div class="store-check-top">
    <div class="store-check-XQ">
      <div class="store-check-left">
        <img src="" id="sugimg">
        <div id="images">
          <img src="">
          <img src="">
          <img src="">
          <img src="">
        </div>
      </div>
      <div class="store-check-right">
        <dl>
          <dt>
          <h1 id="name">dsfasdas</h1>
          </dt>
          <dt>
            <em id="price">￥</em>
          </dt>
          <dt>
            <i>运费：0.00元起</i><i>非包邮地区</i>
          </dt>
          <dt>
            <span>七天无理由退货&nbsp;|&nbsp;正品保证</span>
          </dt>
          <div style="border-top: 1px solid #dddddd;" id="kindList">
            <%--<dd>--%>
            <%--<em>颜色：</em>--%>
            <%--<span>黑色表带白色表盘</span>--%>
            <%--<span class="store-BJ">白色表带黑色表盘</span>--%>
            <%--</dd>--%>
          </div>
          <dt>
            <i>数量：</i>
            <label id="reduceNum">-</label><input id="num" value="1"><label id="addNum">+</label>
          </dt>
          <dt>
            <span class="store-LJ">立即购买</span>
            <span class="store-JR">加入购物车</span>
          </dt>
        </dl>
      </div>
    </div>
    <!--==================详情卖家评价=================-->
    <div class="store-check-MJ">
      <div class="store-MJ-left">
        <div class="store-MM">
          <ul>
            <li  class="store-MJ">
              商品详情
            </li>
            <li class="store-MJJ">
              买家评价（<n id="mjpj">0</n>）
            </li>
          </ul>
        </div>
        <!--====================买家评价=============================-->
        <div class="store-shangpin">
          <div class="store-shangpin-top">
            <dl>
              <dt>
                <img src="/img/K6KT/main-page/store-xing.png">
                <img src="/img/K6KT/main-page/store-xing.png">
                <img src="/img/K6KT/main-page/store-xing.png">
                <img src="/img/K6KT/main-page/store-xing.png">
                <img src="/img/K6KT/main-page/store-xing.png">
                                <span class="store-shangpin-I">
                                    <em>:</em>
                                    <span class="store-SP"></span>
                                    <i id="score5">0</i><i>很好</i>
                                </span>
              </dt>
              <dt>
                <img src="/img/K6KT/main-page/store-xing.png">
                <img src="/img/K6KT/main-page/store-xing.png">
                <img src="/img/K6KT/main-page/store-xing.png">
                <img src="/img/K6KT/main-page/store-xing.png">
                                <span class="store-shangpin-I">
                                    <em>:</em>
                                    <span class="store-SP"></span>
                                    <i id="score4">0</i><i>较好</i>
                                 </span>
              </dt>
              <dt>
                <img src="/img/K6KT/main-page/store-xing.png">
                <img src="/img/K6KT/main-page/store-xing.png">
                <img src="/img/K6KT/main-page/store-xing.png">
                                <span class="store-shangpin-I">
                                    <em>:</em>
                                    <span class="store-SP"></span>
                                    <i id="score3">0</i><i>一般</i>
                                </span>
              </dt>
              <dt>
                <img src="/img/K6KT/main-page/store-xing.png">
                <img src="/img/K6KT/main-page/store-xing.png">
                                <span class="store-shangpin-I">
                                    <em>:</em>
                                    <span class="store-SP"></span>
                                    <i id="score2">0</i><i>差</i>
                                </span>
              </dt>
              <dt>
                <img src="/img/K6KT/main-page/store-xing.png">
                                <span class="store-shangpin-I">
                                    <em>:</em>
                                    <span class="store-SP"></span>
                                    <i id="score1">0</i><i>极差</i>
                                 </span>
              </dt>
              <dd>
                <input type="checkbox" value="0"><em>只看晒图</em>
              </dd>
            </dl>
          </div>
          <div id="commentList">
            <div class="store-message">
              <img class="store-message-IM" src="">
              <div>
                <em>siri</em><img src="/img/K6KT/main-page/store-Hxing.png"><img src="/img/K6KT/main-page/store-Hxing.png">
                <p>好啊好哦啊哈好</p>
                <!--<img class="store-message-IMM" src=""><img class="store-message-IMM" src="">-->
                <label>颜色：黑色</label>
              </div>
              <span>2015-11-11</span>
            </div>
            <div class="store-message">
              <img class="store-message-IM" src="">
              <div>
                <em>siri</em><img src="/img/K6KT/main-page/store-Hxing.png"><img src="/img/K6KT/main-page/store-Hxing.png">
                <p>好啊好哦啊哈好</p>
                <img class="store-message-IMM" src="http://img14.360buyimg.com/n0/jfs/t2461/281/145335373/97081/8af73dbf/55f0e80aN532efcae.jpg">
                <img class="store-message-IMM" src="http://img14.360buyimg.com/n0/jfs/t2461/281/145335373/97081/8af73dbf/55f0e80aN532efcae.jpg">
                <label>颜色：黑色</label>
              </div>
              <span>2015-11-11</span>
            </div>
          </div>
        </div>
        <%--<div class="new-page-links" hidden></div>--%>
        <!--=================商品详情================================-->
        <div class="store-pingjia">
          <img src="">
          <img src="">
        </div>
      </div>
      <div class="store_MJ-right">
        <div>
          <div class="store-MJ-right-top">
            您可能喜欢的商品
          </div>
          <div class="store-MJ-right-bottom">
            <ul id="goodsList">
              <%--<li>--%>
              <%--<a href="store-details.html"></a>--%>
              <%--<img class="store-IIM" src="/img/K6KT/main-page/store-NEW.png">--%>
              <%--<div>--%>
              <%--<img src="">--%>
              <%--<dl>--%>
              <%--<dd>真皮牛皮厚底高帮系带黑色男鞋子</dd>--%>
              <%--<dt>￥&nbsp;1999.00</dt>--%>
              <%--<em></em><a href="javascript:;"></a>--%>
              <%--</dl>--%>
              <%--</div>--%>
              <%--</li>--%>
            </ul>
          </div>
        </div>
      </div>
    </div>

  </div>
</div>

<!--=============底部版权=================-->
<div class="store-foott">
  <div class="store-foott-main">
    <div class="store-foott-left">
      <span>版权所有：上海复兰信息科技有限公司</span><a target="_blank" href="http://www.fulaan-tech.com">www.fulaan-tech.com</a>
                   <span>
                       <a href="/aboutus/k6kt">关于我们</a>
                       <a href="/contactus/k6kt">联系我们</a>
                       <a href="/service/k6kt">服务条款 </a>
                       <a href="/privacy/k6kt">隐私保护 </a>
                       <a href='http://wpa.qq.com/msgrd?v=1&uin=2803728882&site=qq&menu=yes' target="_blank">在线客服</a>
                       <a href='http://wpa.qq.com/msgrd?v=1&uin=2803728882&site=qq&menu=yes' style="position: relative;top: 5px;"><img src="/img/QQService.png"></a>
                   </span>
      <span>沪ICP备14004857号</span>
    </div>
    <div class="store-foott-right">
      <div>
        <img src="/img/K6KT/main-page/store-phone.png">
                    <span>
                        <i>关注我们：</i>
                        <a target="_blank" href='http://weibo.com/FulaanTechnology'><img src="/img/K6KT/main-page/store-WEB.png"></a>
                        <a target="_blank" href='http://t.qq.com/FulaanTechnology'><img src="/img/K6KT/main-page/store-WEBI.png"></a>
                        <a><img src="/img/K6KT/main-page/store-WEX.png"></a>
                    </span>
      </div>
      <img src="/img/K6KT/main-page/store-WEII.jpg">
    </div>
  </div>
</div>
<a hidden class="backtop"></a>

<!--=============================注册弹出框================================-->
<!--==========背景============-->
<div class="bg" hidden>

</div>
<div class="store-register" hidden>
  <dl>
    <dt>
      <em>登录</em>
    <div>没有账号，<a href="register.do">立即注册</a></div>
    <i id="close">X</i>
    </dt>
    <dd>
      <div hidden id="accountError" class="error">帐户名与密码不匹配，请重新输入</div>
      <h1>注册复兰商城账号，购买正品精品！</h1>
      <div class="store-MF" hidden>该登录名不存在<a href="register.do">免费注册？</a></div>
    </dd>
    <dd>
      <em>用户名：</em><input type="text" placeholder="用户名/邮箱/手机号" id="account">
    </dd>
    <dd>
      <em>密码：</em><input type="password" id="password">
    </dd>
    <%--<dd>--%>
    <%--<em>验证码：</em><input class="store-IN" id="verifyCode">--%>
    <%--<span><img id="vc" alt="" src="/verify/verifyCode.do"></span>--%>
    <%--</dd>--%>
    <dd>
      <div class="store-YZ error" hidden id="passwordError">密码错误</div>
      <div class="store-YZ error" hidden id="vcError">验证码错误</div>
      <div class="store-YZ error" hidden id="vcEmpty">验证码为空</div>
      <em></em><span class="store-DL" id="logIn">登录</span>
      <%--<a>忘记密码？</a>--%>
    </dd>
  </dl>
</div>


<script id="kindListTmpl" type="text/template">
  {{ for(var i in it) { }}
  <dd>
    <em id="{{=it[i].kindId}}">{{=it[i].kindName}}：</em>
    {{var specList = it[i].specList;}}
    {{ for(var j in specList){ }}
    <span id="{{=specList[j].id}}" p="{{=specList[j].price}}">{{=specList[j].name}}</span>
    {{} }}
  </dd>
  {{ } }}
</script>

<script id="commentListTmpl" type="text/template">
  {{ for(var i in it) { }}
  <div class="store-message">
    <img class="store-message-IM" src="{{=it[i].userAvatar}}">
    <div>
      <em>{{=it[i].userName}}</em>
      {{ for(var k=0; k< it[i].score;k++){ }}
      <img src="/img/K6KT/main-page/store-Hxing.png">
      {{}}}
      <p>{{=it[i].content}}</p>
      {{var imgs = it[i].images;}}
      {{for(var j in imgs){ }}
      <a class="fancybox" href="{{=imgs[j].value}}" data-fancybox-group="{{=it[i].eCommentId}}">
        <img class="store-message-IMM" src="{{=imgs[j].value}}">
      </a>
      {{} }}

      <%--<img class="store-message-IMM" src="http://img14.360buyimg.com/n0/jfs/t2461/281/145335373/97081/8af73dbf/55f0e80aN532efcae.jpg">--%>
      <%--<label>颜色：黑色</label>--%>
    </div>
    <%--<span>{{=it[i].date}}</span>--%>
  </div>
  {{ } }}
</script>

<script id="goodsListTmpl" type="text/template">
  {{ for(var i in it) { }}
  <li class="detail" id="{{=it[i].goodsId}}">
    <a href="/mall/detail.do?id={{=it[i].goodsId}}" target="_blank"></a>
    <img class="store-IIM" src="/img/K6KT/main-page/store-NEW.png">
    <div>
      <img src="{{=it[i].suggestImage}}">
      <dl>
        <dd>{{=it[i].goodsName}}</dd>
        <dt>￥&nbsp;{{=it[i].price/100}}</dt>
        <em></em>
        <%--<a href="javascript:;"></a>--%>
      </dl>
    </div>
  </li>
  {{ } }}
</script>
<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script src="/static_new/js/sea.js"></script>
<!-- Custom js -->
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
  seajs.use('/static_new/js/modules/mall/0.1.0/admin/preview');
</script>

</body>
</html>
