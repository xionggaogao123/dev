<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>个人中心</title>
    <link href="/static/css/style.css" rel="stylesheet" type="text/css"/>
    <link href="/static/css/main.css" rel="stylesheet" type="text/css">
    <link href="/static/css/account/account.css" rel="stylesheet" type="text/css">
    <style type="text/css">
        .orange-submit-btn .active {
            background: #32A2E3 url(/img/loading4.gif) no-repeat 13px 7px;
        }

        .store-car {
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
                    <li>个人中心</li>
                </ul>
            </div>
            <div class="ebusiness-info">
                <ul>
                    <li id="baseInfo" class="ebusiness-info-li">基本资料</li>
                    <li id="accountSafe">账户安全</li>
                </ul>
            </div>

            <div>
                <div id="baseInfoDiv">
                    <form id="base" action="/mall/userCenter/userInfo.do" method="post">
                        <div>
                            <label>用户名：</label><em id="name"></em>
                        </div>
                        <div>
                            <label>*昵称：</label>
                            <input class="user-in" type="text" name="nickName" required>
                            <span class="span-bm">（昵称不可用于登陆）</span>
                        </div>
                        <div>
                            <label>*性别：</label>
                            <input type="radio" name="sex" value="1"> 男
                            <input type="radio" name="sex" value="0"> 女
                        </div>
                        <div style="height: 100px;"></div>
                        <div>
                            <label>*生日：</label>
                            <select name="year" class="sel_year" rel="2016" require></select> 年
                            <select name="month" class="sel_month" rel="2" required></select> 月
                            <select name="day" class="sel_day" rel="14" required></select> 日
                            <span class="span-bm">（出生年月为保密）</span>
                        </div>
                        <div class="vo-div">
                            <button type="submit">提交</button>
                        </div>
                    </form>
                    <div class="div-photo">
                        <label>*头像：</label>
                        <span class="span-photo">
                                <img style="height:98px;width:98px;" src='${avatar}'>
                                <input type="button" value="编辑头像" class="btn-edit-head">
                            </span>
                    </div>
                </div>
                <div id="accountSafeDiv">
                    <form id="account" action="/mall/userCenter/user.do" method="post">
                        <table class="account-tb">
                            <tr>
                                <td class="account-tb-r">* 登录密码：</td>
                                <td>
                                    <input type="password" name="" id="loginpassword"/>
                                </td>
                                <td></td>
                            </tr>
                            <tr>
                                <td class="account-tb-r">* 新的登录密码：</td>
                                <td>
                                    <input type="password" name="password" id="newpassword"/>
                                </td>
                                <td></td>
                            </tr>
                            <tr>
                                <td class="account-tb-r">*确认新密码：</td>
                                <td>
                                    <input type="password" name="rpassword" id="newrpassword"/>
                                </td>
                                <td></td>
                            </tr>
                            <tr>
                                <td class="account-tb-r"></td>
                                <td colspan="2">
                                    <button type="submit">提交</button>
                                </td>
                            </tr>
                        </table>
                    </form>
                </div>
                <!--<div class="account-AQ" id="account-AQ">
                    <div class="account-SF">
                        <dl>
                            <dt></dt>
                            <dd>
                                <em><i>已验证手机：</i></em>
                                <input id="phone" hidden><span></span>
                            </dd>
                            <dd>
                                <em>
                                    <i class="account-I">*</i>验证码：
                                </em>
                                <input id="verifyCode" style="width: 110px"><img id="verifyImg" alt="" src="/verify/verifyCode.do" style="position: relative;top: 7px;left: 2px"/>
                                <span class="register-ZC" id="verifyCode2" >请输入图片验证码</span>
                                <span class="register-ZC" id="verifyCode3" >图片验证码错误或已失效<%--或<a>找回密码</a>--%></span>
                                <!--<input>
                                <span class="account-YZM">11111</span>
                            </dd>
                            <dd>
                                <em>
                                    <i class="account-I">*</i>请填写手机验证码：
                                </em>
                                <input class="register-YZ">
                                <span class="account-HQ">获取短信效验码</span>
                                <!--<label>校验码已发出，请注意查收短信，如果没有收到，你可以在50秒后要求系统重新发送</label>
                            </dd>
                            <dd>
                                <span class="account-TJ">提交</span>
                            </dd>
                        </dl>
                    </div>
                    <div class="account-XG">
                        <form id="account" action="/mall/userCenter/user.do" method="post">
                        <dl>
                            <dt></dt>
                            <dd>
                                <em>
                                    <i class="account-I">*</i>登录密码:
                                </em>
                                <input type="password" name="" id="loginpassword"/>
                                <!--<label>密码太弱，有被盗风险，请设置由“字母+数字”组成的复杂密码</label>
                                <label style="display: none">密码长度只能在6-20位字符之间</label>
                                <label style="display: none">该密码比较简单，建议您更改为复杂密码</label>
                            </dd>
                            <dd>
                                <em>
                                    <i class="account-I">*</i>新的登录密码
                                </em>
                                <input type="password" name="password" id="newpassword"/>
                                <label id="comparePwd">两次输入密码不一致</label>
                            </dd>
                            <dd>
                                <em>
                                    <i class="account-I">*</i>请再输入一次密码
                                </em>
                                <input type="password" name="rpassword" id="newrpassword"/>
                            </dd>
                            <dd>
                                <em></em>
                                <span class="account-TJJ"><button type="submit">提交</button></span>
                            </dd>
                        </dl>
                        </form>
                    </div>-
                    <div class="account-CG">
                        <dl>
                            <dt></dt>
                            <dd class="dd-ok">
                                恭喜您，密码修改成功！
                            </dd>
                        </dl>
                    </div>-->
            </div>
        </div>
    </div>
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
<script src="/static/js/validate-form.js"></script>
<script>
    seajs.use('user', function (user) {
        user.init();
    });
</script>
</body>
</html>
