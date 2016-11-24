<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>找附近</title>
    <meta charset="utf-8">
    <script type="text/javascript" src="/static/js/friend/jquery-1.11.1.js"></script>
    <script type="text/javascript" src="/static/js/friend/near.js"></script>
    <link rel="stylesheet" type="text/css" href="/static/css/friend/nearby.css">
    <link rel="stylesheet" type="text/css" href="/static/css/main.css">
    <script type="text/javascript">
        $(function () {
            var $this = $("#nearNews");
            var scrollTimer;
            $this.hover(function () {
                clearInterval(scrollTimer);
            }, function () {
                scrollTimer = setInterval(function () {
                    scrollNews($this);
                }, 2000);
            }).trigger("mouseleave");

            function scrollNews(obj) {
                var $self = obj.find("ul");
                var lineHeight = $self.find("li:first").height();
                $self.animate({
                    "marginTop": -lineHeight - 21 + "px"
                }, 1000, function () {
                    $self.css({
                        marginTop: 0
                    }).find("li:first").appendTo($self);
                })
            }
        })
    </script>
</head>
<body style="background: #f5f5f5">
<%@include file="../common/head.jsp" %>
<%@include file="../common/login.jsp" %>
<!--广告-->
<div class="near-banner"></div>
<div class="near-container clearfix">
    <div class="near-left clearfix">
        <div class="near-menu">
            <div class="menu1 clearfix">
                <div class="menu-item">兴趣爱好</div>
                <div class="clearfix menu-cont">
                    <span class="gre-cur">不限</span>
                    <c:forEach items="${hobbys}" var="hobby">
                        <span value="${hobby['id']}">${hobby['name']}</span>
                    </c:forEach>
                </div>
            </div>
            <div class="menu1 clearfix">
                <div class="menu-item">年&nbsp;龄&nbsp;段</div>
                <div class="clearfix menu-cont">
                    <span class="gre-cur">不限</span>
                    <span>运动</span>
                    <span>运动</span>
                    <span>运动</span>
                    <span>运动</span>
                    <span>运动</span>
                    <span>运动</span>
                    <span>运动</span>
                    <span>运动</span>
                </div>
            </div>
            <div class="menu1 clearfix">
                <div class="menu-item">距&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;离</div>
                <div class="clearfix menu-cont">
                    <span class="gre-cur">不限</span>
                    <span>运动</span>
                    <span>运动</span>
                    <span>运动</span>
                    <span>运动</span>
                    <span>运动</span>
                    <span>运动</span>
                    <span>运动</span>
                    <span>运动</span>
                </div>
            </div>
        </div>
        <div class="near-list">
            <p class="near-p">共有12位邻居符合条件</p>
            <div class="near-li">
                <img src=''>
                <div class="near-infor">
                    <p class="p1">
                        <span class="sp1">乔纳森</span>
                        <span class="sp2">查看更多</span>
                    </p>
                    <p class="p2">
                        标签：<i>阅读</i>、<i>运动</i>、饮食、旅游
                    </p>
                    <p class="p2">
                        您的认证好友<i>卢佳佳</i>对他进行了认证
                    </p>
                    <p class="p2">
                        <img src="/static/images/findnearby_zuobiao.png">距离您<i>2000</i>米
                    </p>
                </div>
                <button class="btn-lyl">聊一聊</button>
            </div>
            <div class="near-li">
                <img src="">
                <div class="near-infor">
                    <p class="p1">
                        <span class="sp1">乔纳森</span>
                        <span class="sp2">查看更多</span>
                    </p>
                    <p class="p2">
                        标签：<i>阅读</i>、<i>运动</i>、饮食、旅游
                    </p>
                    <p class="p2">
                        您的认证好友<i>卢佳佳</i>对他进行了认证
                    </p>
                    <p class="p2">
                        <img src="/static/images/findnearby_zuobiao.png">距离您<i>2000</i>米
                    </p>
                </div>
                <button class="btn-lyl">聊一聊</button>
            </div>
        </div>
    </div>
    <div class="near-right clearfix">
        <div class="act-nav">
            <div class="act-already-join">已参加活动<br><i>10</i></div>
            <div class="act-already-push">已发布活动<br><i>10</i></div>
            <div class="act-nav-line"></div>
        </div>
        <div class="act-nav2">
            <div class="btn-fbgb">发布广播</div>
            <div class="btn-wdbq">我的标签</div>
        </div>
        <div id="nearNews">
            <p>小喇叭广播站</p>
            <div class="news-f">
                <ul class="ul-nearnews">
                    <li class="clearfix">
                        <img src="http://placehold.it/58">
                        <p class="p1">哥白尼</p>
                        <p class="p2">标签:阅读、运动、饮食、旅游、音乐、电影、篮球</p>
                        <p class="p3">
                            <img src="/static/images/findnearby_zuobiao.png">
                            距离你<i>200</i>米
                        </p>
                        <p class="p4"><i>#篮球#</i>10月25日 周五下午3:30,学校操场打篮球，有没有人来呀？打篮球，有没有人来呀？打篮球，有没有人来呀？打篮球，有没有人来呀？</p>
                        <p class="p5">已有20人报名</p>
                        <button>我要报名</button>
                    </li>
                    <li class="clearfix">
                        <img src="http://placehold.it/58">
                        <p class="p1">哥白尼</p>
                        <p class="p2">标签:阅读、运动、饮食、旅游、音乐、电影、篮球</p>
                        <p class="p3">
                            <img src="/static/images/findnearby_zuobiao.png">
                            距离你<i>200</i>米
                        </p>
                        <p class="p4"><i>#篮球#</i>10月25日 周五下午3:30,学校操场打篮球，有没有人来呀？</p>
                        <p class="p5">已有20人报名</p>
                        <button>我要报名</button>
                    </li>
                    <li class="clearfix">
                        <img src="http://placehold.it/58">
                        <p class="p1">哥白尼</p>
                        <p class="p2">标签:阅读、运动、饮食、旅游、音乐、电影、篮球</p>
                        <p class="p3">
                            <img src="/static/images/findnearby_zuobiao.png">
                            距离你<i>200</i>米
                        </p>
                        <p class="p4"><i>#篮球#</i>10月25日 周五下午3:30,学校操场打篮球，有没有人来呀？</p>
                        <p class="p5">已有20人报名</p>
                        <button>我要报名</button>
                    </li>
                    <li class="clearfix">
                        <img src="http://placehold.it/58">
                        <p class="p1">哥白尼</p>
                        <p class="p2">标签:阅读、运动、饮食、旅游、音乐、电影、篮球</p>
                        <p class="p3">
                            <img src="/static/images/findnearby_zuobiao.png">
                            距离你<i>200</i>米
                        </p>
                        <p class="p4"><i>#篮球#</i>10月25日 周五下午3:30,学校操场打篮球，有没有人来呀？</p>
                        <p class="p5">已有20人报名</p>
                        <button>我要报名</button>
                    </li>
                    <li class="clearfix">
                        <img src="http://placehold.it/58">
                        <p class="p1">哥白尼</p>
                        <p class="p2">标签:阅读、运动、饮食、旅游、音乐、电影、篮球</p>
                        <p class="p3">
                            <img src="/static/images/findnearby_zuobiao.png">
                            距离你<i>200</i>米
                        </p>
                        <p class="p4"><i>#篮球#</i>10月25日 周五下午3:30,学校操场打篮球，有没有人来呀？</p>
                        <p class="p5">已有20人报名</p>
                        <button>我要报名</button>
                    </li>
                    <li class="clearfix">
                        <img src="http://placehold.it/58">
                        <p class="p1">哥白尼</p>
                        <p class="p2">标签:阅读、运动、饮食、旅游、音乐、电影、篮球</p>
                        <p class="p3">
                            <img src="/static/images/findnearby_zuobiao.png">
                            距离你<i>200</i>米
                        </p>
                        <p class="p4"><i>#篮球#</i>10月25日 周五下午3:30,学校操场打篮球，有没有人来呀？</p>
                        <p class="p5">已有20人报名</p>
                        <button>我要报名</button>
                    </li>
                    <li class="clearfix">
                        <img src="http://placehold.it/58">
                        <p class="p1">哥白尼</p>
                        <p class="p2">标签:阅读、运动、饮食、旅游、音乐、电影、篮球</p>
                        <p class="p3">
                            <img src="/static/images/findnearby_zuobiao.png">
                            距离你<i>200</i>米
                        </p>
                        <p class="p4"><i>#篮球#</i>10月25日 周五下午3:30,学校操场打篮球，有没有人来呀？</p>
                        <p class="p5">已有20人报名</p>
                        <button>我要报名</button>
                    </li>
                    <li class="clearfix">
                        <img src="http://placehold.it/58">
                        <p class="p1">哥白尼</p>
                        <p class="p2">标签:阅读、运动、饮食、旅游、音乐、电影、篮球</p>
                        <p class="p3">
                            <img src="/static/images/findnearby_zuobiao.png">
                            距离你<i>200</i>米
                        </p>
                        <p class="p4"><i>#篮球#</i>10月25日 周五下午3:30,学校操场打篮球，有没有人来呀？</p>
                        <p class="p5">已有20人报名</p>
                        <button>我要报名</button>
                    </li>
                    <li class="clearfix">
                        <img src="http://placehold.it/58">
                        <p class="p1">哥白尼</p>
                        <p class="p2">标签:阅读、运动、饮食、旅游、音乐、电影、篮球</p>
                        <p class="p3">
                            <img src="/static/images/findnearby_zuobiao.png">
                            距离你<i>200</i>米
                        </p>
                        <p class="p4"><i>#篮球#</i>10月25日 周五下午3:30,学校操场打篮球，有没有人来呀？</p>
                        <p class="p5">已有20人报名</p>
                        <button>我要报名</button>
                    </li>
                    <li class="clearfix">
                        <img src="http://placehold.it/58">
                        <p class="p1">哥白尼</p>
                        <p class="p2">标签:阅读、运动、饮食、旅游、音乐、电影、篮球</p>
                        <p class="p3">
                            <img src="/static/images/findnearby_zuobiao.png">
                            距离你<i>200</i>米
                        </p>
                        <p class="p4"><i>#篮球#</i>10月25日 周五下午3:30,学校操场打篮球，有没有人来呀？</p>
                        <p class="p5">已有20人报名</p>
                        <button>我要报名</button>
                    </li>
                    <li class="clearfix">
                        <img src="http://placehold.it/58">
                        <p class="p1">哥白尼</p>
                        <p class="p2">标签:阅读、运动、饮食、旅游、音乐、电影、篮球</p>
                        <p class="p3">
                            <img src="/static/images/findnearby_zuobiao.png">
                            距离你<i>200</i>米
                        </p>
                        <p class="p4"><i>#篮球#</i>10月25日 周五下午3:30,学校操场打篮球，有没有人来呀？</p>
                        <p class="p5">已有20人报名</p>
                        <button>我要报名</button>
                    </li>
                    <li class="clearfix">
                        <img src="http://placehold.it/58">
                        <p class="p1">哥白尼</p>
                        <p class="p2">标签:阅读、运动、饮食、旅游、音乐、电影、篮球</p>
                        <p class="p3">
                            <img src="/static/images/findnearby_zuobiao.png">
                            距离你<i>200</i>米
                        </p>
                        <p class="p4"><i>#篮球#</i>10月25日 周五下午3:30,学校操场打篮球，有没有人来呀？</p>
                        <p class="p5">已有20人报名</p>
                        <button>我要报名</button>
                    </li>
                    <li class="clearfix">
                        <img src="http://placehold.it/58">
                        <p class="p1">哥白尼</p>
                        <p class="p2">标签:阅读、运动、饮食、旅游、音乐、电影、篮球</p>
                        <p class="p3">
                            <img src="/static/images/findnearby_zuobiao.png">
                            距离你<i>200</i>米
                        </p>
                        <p class="p4"><i>#篮球#</i>10月25日 周五下午3:30,学校操场打篮球，有没有人来呀？</p>
                        <p class="p5">已有20人报名</p>
                        <button>我要报名</button>
                    </li>
                    <li class="clearfix">
                        <img src="http://placehold.it/58">
                        <p class="p1">哥白尼</p>
                        <p class="p2">标签:阅读、运动、饮食、旅游、音乐、电影、篮球</p>
                        <p class="p3">
                            <img src="/static/images/findnearby_zuobiao.png">
                            距离你<i>200</i>米
                        </p>
                        <p class="p4"><i>#篮球#</i>10月25日 周五下午3:30,学校操场打篮球，有没有人来呀？</p>
                        <p class="p5">已有20人报名</p>
                        <button>我要报名</button>
                    </li>
                    <li class="clearfix">
                        <img src="http://placehold.it/58">
                        <p class="p1">哥白尼</p>
                        <p class="p2">标签:阅读、运动、饮食、旅游、音乐、电影、篮球</p>
                        <p class="p3">
                            <img src="/static/images/findnearby_zuobiao.png">
                            距离你<i>200</i>米
                        </p>
                        <p class="p4"><i>#篮球#</i>10月25日 周五下午3:30,学校操场打篮球，有没有人来呀？</p>
                        <p class="p5">已有20人报名</p>
                        <button>我要报名</button>
                    </li>
                    <li class="clearfix">
                        <img src="http://placehold.it/58">
                        <p class="p1">哥白尼</p>
                        <p class="p2">标签:阅读、运动、饮食、旅游、音乐、电影、篮球</p>
                        <p class="p3">
                            <img src="/static/images/findnearby_zuobiao.png">
                            距离你<i>200</i>米
                        </p>
                        <p class="p4"><i>#篮球#</i>10月25日 周五下午3:30,学校操场打篮球，有没有人来呀？</p>
                        <p class="p5">已有20人报名</p>
                        <button>我要报名</button>
                    </li>
                    <li class="clearfix">
                        <img src="http://placehold.it/58">
                        <p class="p1">哥白尼</p>
                        <p class="p2">标签:阅读、运动、饮食、旅游、音乐、电影、篮球</p>
                        <p class="p3">
                            <img src="/static/images/findnearby_zuobiao.png">
                            距离你<i>200</i>米
                        </p>
                        <p class="p4"><i>#篮球#</i>10月25日 周五下午3:30,学校操场打篮球，有没有人来呀？</p>
                        <p class="p5">已有20人报名</p>
                        <button>我要报名</button>
                    </li>
                    <li class="clearfix">
                        <img src="http://placehold.it/58">
                        <p class="p1">哥白尼</p>
                        <p class="p2">标签:阅读、运动、饮食、旅游、音乐、电影、篮球</p>
                        <p class="p3">
                            <img src="/static/images/findnearby_zuobiao.png">
                            距离你<i>200</i>米
                        </p>
                        <p class="p4"><i>#篮球#</i>10月25日 周五下午3:30,学校操场打篮球，有没有人来呀？</p>
                        <p class="p5">已有20人报名</p>
                        <button>我要报名</button>
                    </li>
                    <li class="clearfix">
                        <img src="http://placehold.it/58">
                        <p class="p1">哥白尼</p>
                        <p class="p2">标签:阅读、运动、饮食、旅游、音乐、电影、篮球</p>
                        <p class="p3">
                            <img src="/static/images/findnearby_zuobiao.png">
                            距离你<i>200</i>米
                        </p>
                        <p class="p4"><i>#篮球#</i>10月25日 周五下午3:30,学校操场打篮球，有没有人来呀？</p>
                        <p class="p5">已有20人报名</p>
                        <button>我要报名</button>
                    </li>
                    <li class="clearfix">
                        <img src="http://placehold.it/58">
                        <p class="p1">哥白尼</p>
                        <p class="p2">标签:阅读、运动、饮食、旅游、音乐、电影、篮球</p>
                        <p class="p3">
                            <img src="/static/images/findnearby_zuobiao.png">
                            距离你<i>200</i>米
                        </p>
                        <p class="p4"><i>#篮球#</i>10月25日 周五下午3:30,学校操场打篮球，有没有人来呀？</p>
                        <p class="p5">已有20人报名</p>
                        <button>我要报名</button>
                    </li>
                </ul>
            </div>
        </div>
    </div>

</div>
<!--标签-->
<div class="wind-biaoq">
    <p class="p1">我的标签<em>×</em></p>
    <p class="p2">推荐标签</p>
    <div class="bq-list clearfix">
        <c:forEach items="${tags}" var="tag">
            <span value="${tag['id']}">${tag['name']}</span>
        </c:forEach>
    </div>
    <p class="p3">
        <button class="btn1">确认添加</button>
        <button class="btn-add-no">取消添加</button>
    </p>
</div>
<!--标签-->

<!--活动详情-->
<div class="wind-act-det">
    <p class="p1">活动详情<em>×</em></p>
    <p class="p2 clearfix">主题：<span><em>#篮球#</em>周末一起打篮球！</span></p>
    <p class="p3">时间：2016-06-06 15：00</p>
    <p class="p4 clearfix">详情：<span>广泛士大夫敢死队分别是的版本</span>
    <p class="p5">活动人员：已报名200人</p>
    <p class="p6 clearfix">
        <img src="">
        <img src="">
        <img src="">
        <img src="">
        <img src="">
        <img src="">
        <img src="">
        <img src="">
        <img src="">
    </p>
    <p class="p7">
        <button class="b1">确认报名</button>
        <button class="b2">取消报名</button>
    </p>
</div>
<!--活动详情-->

<!--发起活动-->
<div class="wind-act-fq">
    <p class="p1">发起活动<em>×</em></p>
    <p class="p2">主题<span>（请选择一个项目作为活动标签）</span></p>
    <p class="p3">
        <select>
            <c:forEach items="${themes}" var="theme">
                <option value="${theme['id']}">${theme['name']}</option>
            </c:forEach>
        </select>
        <input type="text">
    </p>
    <p class="p2">活动内容<span>(克简要说明一下活动时间、地点等)</span></p>
    <textarea></textarea>
    <p class="p2">活动时间</p>
    <input type="text" class="input1">
    <p class="p4 clearfix">
        <button class="b1">发布活动</button>
        <button class="b2">取消发布</button>
    </p>
</div>
<!--发起活动-->
<%@include file="../common/footer.jsp" %>

<!--背景-->
<%--<div class="bg"></div>--%>
<!--背景-->

</body>
</html>