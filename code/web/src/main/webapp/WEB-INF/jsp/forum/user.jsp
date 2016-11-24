<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>个人中心</title>
    <link href="/static/css/style.css" rel="stylesheet" type="text/css"/>
    <link href="/static/css/main.css" rel="stylesheet" type="text/css">
    <link href="/static/css/forum/forum.css" rel="stylesheet" type="text/css">
    <link href="/static/css/account/account.css" rel="stylesheet" type="text/css">
</head>
<body>
<%@ include file="../common/head.jsp" %>
<div style="border-top:4px solid #ff510d"></div>
<div class="ebusiness-T">
    <%@include file="Item.jsp" %>
    <div class="ebusiness-main-right">
        <div class="ebusiness-history-main ehm1">
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
                    <form id="base">
                        <div>
                            <label>用户名：</label><em id="name"></em>
                        </div>
                        <div>
                            <label>*昵称：</label>
                            <input class="user-in" type="text" name="nickName" id="nickName" required>
                            <span class="span-bm">（昵称不能用于登录）</span>
                        </div>
                        <div>
                            <label>*性别：</label>
                            <input type="radio" name="sex" value="1"> 男
                            <input type="radio" name="sex" value="0"> 女
                        </div>
                        <div style="height: 100px;"></div>
                        <div>
                            <label>*生日：</label>
                            <select name="year" class="sel_year" id="sel_year" rel="2016" required></select> 年
                            <select name="month" class="sel_month" id="sel_month" rel="2" required></select> 月
                            <select name="day" class="sel_day" id="sel_day" rel="14" required></select> 日
                            <span class="span-bm">（出生年月为保密）</span>
                        </div>
                        <div class="vo-div">
                            <button id="changeSub">提交</button>
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
                    <form id="account" action="/forum/userCenter/user.do" method="post">
                        <table class="account-tb">
                            <tr>
                                <td class="account-tb-r">登录密码：</td>
                                <td>
                                    <input type="password" name="" id="loginpassword"/>
                                </td>
                                <td></td>
                            </tr>
                            <tr>
                                <td class="account-tb-r">新的登录密码：</td>
                                <td>
                                    <input type="password" name="password" id="newpassword"/>
                                </td>
                                <td></td>
                            </tr>
                            <tr>
                                <td class="account-tb-r">确认新密码：</td>
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
            </div>
        </div>
        <div class="ebusiness-history-main ehm2">
            <div class="coupon-top">
                <ul>
                    <li>积分记录</li>
                    <span onclick="window.open('/forum/forumLevel.do')">积分规则</span>
                </ul>
            </div>
            <div class="ebusiness-info">
                <ul>
                    <li class="ebusiness-info-li jf-li1">积分收益</li>
                    <li class="jf-li2">系统奖励</li>
                    <span>积分：${formScore}&nbsp;&nbsp;经验值：${formExpermence}</span>
                </ul>
            </div>
            <div class="cont-jf jf1">
                <table class='tab-jf'>
                    <tr>
                        <th class="th1">操作</th>
                        <th class="th2">财富变更</th>
                        <th class="th3">详情</th>
                        <th class="th4">变更时间</th>
                    </tr>
                    <c:forEach items="${scores}" var="score">
                        <tr>
                            <td class="td1">${score.operation}</td>
                            <td>积分+${score.score}</td>
                            <td><a href="#">${score.scoreOrigin}</a></td>
                            <td>${score.timeText}</td>
                        </tr>
                    </c:forEach>
                </table>
                <div class="new-page-links"></div>
            </div>
            <div class="cont-jf jf2">
                <p>进行一下时间动作，会得到积分奖励。不过，在一个周期内，您最多得到的奖励次数有限制</p>
                <table class='tab-xtjl'>
                    <tr>
                        <th class="th1">动作名称</th>
                        <th class="th2">总次数</th>
                        <th class="th3">周期次数</th>
                        <th class="th4">积分</th>
                        <th class="th5">最后奖励时间</th>
                    </tr>
                    <tr>
                        <td class="td1">每天登陆</td>
                        <td>15</td>
                        <td>1</td>
                        <td>1</td>
                        <td>2016-08-26 11:12</td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
</div>
<%@ include file="../common/footer.jsp" %>
<script src="/static/js/sea.js"></script>
<script src="/static/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script src="/static/js/validate-form.js"></script>
<script>
    seajs.use('/static/js/modules/forum/user.js', function (user) {
        user.init();
    });
</script>
</body>
</html>
