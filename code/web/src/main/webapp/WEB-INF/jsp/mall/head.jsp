<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: fl
  Date: 2016/1/19
  Time: 10:01
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<script type="text/javascript" src="/static/js/jquery-1.11.1.min.js"></script>
<script type="text/javascript">
    $(function () {
        if ($('.ebusiness-main').attr('hidden') == null) {
            getGoodsNum();
        }


        $('#toplogin').click(function () {//登录
            $('.store-register').fadeToggle();
            $('.bg').fadeToggle();
        })

        $('#logout').click(function () {//退出登录
            logout();
        })
        $('.input-password').keydown(function (event) {
            if (event.which == 13) {
                loginIndex();
            }
        });

        $('.store-car').click(function () {
            goToCart();
        })

        $('.input-huntt').click(function () {
            sessionStorage.setItem("page", 1);
            var regular = $(this).siblings('.input-hunt').val();
            //sessionStorage.setItem("mall_regular", regular);
            window.location.href = '/mall?regular=' + regular;
        })

        $('.input-hunt').keydown(function (event) {
            if (event.which == 13) {
                sessionStorage.setItem("page", 1);
                var regular = $(this).val();
                sessionStorage.setItem("mall_regular", regular);
                window.location.href = '/mall?regular=' + regular;
            }
        });

    })

    function searchGoods(regular) {

    }

    function goToCart() {
        $.ajax({
            url: "/mall/loginInfo.do?date=" + new Date(),
            type: "get",
            dataType: "json",
            data: {},
            success: function (resp) {
                var flag = resp.login;
                if (flag) {
//                  window.open('/mall/cars/load.do');
                    location.href = '/mall/cars/load.do';
                } else {
                    $('.store-register').fadeToggle();
                    $('.bg').fadeToggle();
                }
            }
        });
    }
    function loginIndex() {
        $('.login-btn').addClass('active');
        $.ajax({
            url: "/user/login.do",
            type: "post",
            dataType: "json",
            data: {
                'name': $(".input-account").val(),
                'pwd': $(".input-password").val(),
                'verifyCode': $(".input-verycode").val()
            },
            success: function (data) {
                if (data.code == 200) {
                    $('#veryCodeSpan').hide();
                    var session = data.message;
                    $('#userName').text('欢迎您，' + session.userName);
                    $('body').attr('login', 'true');
                    if (session.k6kt == 1) {
                        $('#k6kt').show();
                    } else {
                        $('#k6kt').hide();
                    }
                    getGoodsNum();
                    showLoginTitle(true);
                } else {
                    if (data.message == '用户名非法' || data.message == 'accountError') {
                        $('.input-account').addClass('error');
                        $('.username-error').show();
                    } else if (data.message.substr(0, 3) == '验证码') {
                        $('.input-verycode').addClass('error');
                        $('.verycode-error').text(data.message);
                        $('.verycode-error').show();
                        $('#veryCodeSpan').show();
                        //changeImg();
                    } else if (data.message.substr(0, 3) == '2秒内') {
                        //alert("2秒内不可重复登陆");
                    } else {
                        $('.input-password').addClass('error');
                        $('.password-error').show();
                        var strCookie = document.cookie;
                        var arrCookie = strCookie.split("; ");
                        for (var i = 0; i < arrCookie.length; i++) {
                            var arr = arrCookie[i].split("=");
                            if ("password" == arr[0]) {
                                var pwErCoun = arr[1];
                                if (pwErCoun >= 3) {
                                    $('#veryCodeSpan').show();
                                    //changeImg();
                                }
                            }
                        }
                    }
                }
            },
            complete: function () {
                $('.login-btn').removeClass('active');
            }
        });
    }

    function logout() {
        $.ajax({
            url: "/user/logout.do",
            type: "post",
            dataType: "json",
            data: {
                'inJson': true
            },
            success: function (data) {

                var href = location.href;
                if (href.indexOf("mall/order") > 0 || href.indexOf("mall/cars") > 0 || href.indexOf("mall/voucher") > 0 || href.indexOf('mall/userCenter') > 0) {
                    location.href = "/mall/index.do";
                }

                $('body').attr('login', 'false');
                $('#goodsCount').text('（0件商品）');
                showLoginTitle(false);

            }
        });
        ssoLoginout();
    }


    function ssoLoginout() {
        var logoutURL = "http://ah.sso.cycore.cn/sso/logout";

        $.ajax({
            url: logoutURL,
            type: "GET",
            dataType: 'jsonp',
            jsonp: "callback",
            crossDomain: true,
            cache: false,
            success: function (html) {

            },
            error: function (data) {

            }
        });
    }


    function showLoginTitle(login) {
        if (login) {
            $('.ebusiness-main').show();
            $('.login-bar').hide();
        } else {
            $('.ebusiness-main').hide();
            $('.login-bar').show();
        }
    }

    function getGoodsNum() {
        $.ajax({
            url: "/mall/cars/list.do?date=" + new Date(),
            type: "get",
            dataType: "json",
            data: {},
            success: function (resp) {
                if (resp.list) {
                    var goodsCount = resp.list.length;
                    if (goodsCount > 0) {
                        $('#goodsCount').show().text(goodsCount);
                    } else {
                        $('#goodsCount').hide();
                    }

                }
            }
        });
    }

</script>
<div class="ebusiness-main" <c:if test="${login == false}">style="display: none;" </c:if>>
    <div class="ebusiness-main-top">
        <div class="ebusiness-main-top-l">
            <img src="/img/K6KT/main-page/store-ebusiness-V.png" onclick="window.open('/mall/mallSection.do','target')">
            <%--<c:if test="${k6kt == 0}">style="display: none" </c:if>--%>
            <a href="/user/homepage.do" id="k6kt" style="display: none"></a>
        </div>
        <div class="ebusiness-main-top-r">
            <input class="input-hunt" type="text" placeholder="" value="${param.regular}"><em
                class="input-huntt">搜索</em>
            <div>
                <em id="userName" class="user-no">Hi，${userName}</em>
                <a id="logout" class="out-no">[退出]</a>
                <span class="ebusiness-r-w"
                      onclick="window.open('/mall/order/page.do')">我的复兰商城&nbsp;&nbsp;&nbsp;&gt;</span>
                <span class="ebusiness-r-l">
                <em class="ebusiness-D" onclick="window.open('/mall/order/page.do')">交易中心</em>
                <em class="ebusiness-Z" onclick="window.open('/mall/userCenter/collection.do')">关注中心</em>
                <em class="ebusiness-G" onclick="window.open('/mall/userCenter/user.do')">设置中心</em>

            </span>
                <span class="store-car">购物车<em id="goodsCount" style="display: none"></em></span>
            </div>
        </div>
    </div>
</div>
<div class="login-bar" <c:if test="${login == true}">style="margin: 0 auto; display: none;"</c:if>>
    <div class='title-bar-container' style="height:80px;overflow: hidden;">
        <div class="ebusiness-main-top-l">
            <%--<img class="title-logo" src="/img/K6KT/main-page/store-logo.png">--%>
            <img class="title-logo" src="/img/K6KT/main-page/store-ebusiness-V.png"
                 onclick="window.open('/mall/index.do','target')">
            <i style="display: inline-block;width: 130px;"></i>
            <!--申请试用-->
        </div>
        <div class="ebusiness-main-top-r">
            <span class="store-car">购物车</span>
            <a class="login-find" href="/mall/findUserPwd.do">找回密码</a>
            <a class="login-log login-lo" href="/account/register">注册</a>
            <a class="login-log" href="javascript:;" id="toplogin">登 录</a>
            <span id="veryCodeSpan" style="display: none" class="very-code">
                        <input id="veryCode" class="input-verycode" name="veryCode" type="text"/>
                        <img id="imgObj" alt="" src=""/>
                        <a href="javascript:;" onclick="changeImg()">看不清楚？换一张</a>
                    </span>
            <input class="input-hunt" id="" type="text" placeholder="" value="${param.regular}"><em class="input-huntt">搜索</em>
            <%-- <input id="input-first" class="input-password" type="password" placeholder="密码" tabindex="2">
             <input id="input-last" class="input-account" type="text" placeholder="用户名/邮箱/手机号" tabindex="1" value="">--%>
        </div>
        <div id="tips-msg">
            <span class="verycode-error"></span>
            <span class="password-error">密码错误</span>
            <span class="username-error">用户名不存在</span>
        </div>
    </div>
</div>
