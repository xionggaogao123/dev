<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>找玩伴</title>
    <meta charset="utf-8">
    <script type="text/javascript" src="/static/js/friend/jquery-1.11.1.js"></script>
    <link rel="stylesheet" type="text/css" href="/static/css/friend/nearby.css">
    <link rel="stylesheet" type="text/css" href="/static/css/main.css">
    <script type="text/javascript"
            src="http://api.map.baidu.com/api?v=2.0&ak=gLtMQemqya8xYLSOlUkfGL0iCKGsqdfw"></script>
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
                <div class="clearfix menu-cont mate-xingqu">
                    <span value="-1" class="gre-cur">不限</span>
                    <span code="100" tag="旅行">旅行</span>
                    <span code="101" tag="英语">英语</span>
                    <span code="102" tag="阅读">阅读</span>
                    <span code="103" tag="足球">足球</span>
                    <span code="104" tag="篮球">篮球</span>
                    <span code="105" tag="羽毛球">羽毛球</span>
                    <span code="106" tag="网球">网球</span>
                    <span code="107" tag="乒乓球">乒乓球</span>
                    <span code="108" tag="轮滑跆">轮滑跆</span>
                    <span code="109" tag="拳道">拳道</span>
                    <span code="110" tag="跳绳">跳绳</span>
                    <span code="111" tag="歌唱">歌唱</span>
                    <span code="112" tag="表演">表演</span>
                    <span code="113" tag="舞蹈">舞蹈</span>
                    <span code="114" tag="美术">美术</span>
                    <span code="115" tag="钢琴">钢琴</span>
                    <span code="116" tag="古筝">古筝</span>
                    <span code="117" tag="二胡">二胡</span>
                    <span code="118" tag="小提琴">小提琴</span>
                    <span code="119" tag="笛子">笛子</span>
                    <span code="120" tag="架子鼓">架子鼓</span>
                    <span code="121" tag="围棋">围棋</span>
                    <span code="122" tag="跳棋">跳棋</span>
                    <span code="123" tag="象棋">象棋</span>
                    <span code="124" tag="桥牌">桥牌</span>
                    <span code="125" tag="演讲">演讲</span>
                    <span code="126" tag="航模">航模</span>
                    <span code="127" tag="航海">航海</span>
                    <span code="128" tag="机器人">机器人</span>
                    <span code="129" tag="演讲">演讲</span>
                </div>
            </div>
            <div class="menu1 clearfix">
                <div class="menu-item">年&nbsp;龄&nbsp;段</div>
                <div class="clearfix menu-cont mate-aged">
                    <span value='-1' class="gre-cur">不限</span>
                    <span value="1">3-5岁</span>
                    <span value="2">5-8岁</span>
                    <span value="3">8-11岁</span>
                    <span value="4">11-15岁</span>
                    <span value="5">15-18岁</span>
                    <span value="6">18岁以上</span>
                </div>
            </div>
            <div class="menu1 clearfix">
                <div class="menu-item">距&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;离</div>
                <div class="clearfix menu-cont mate-distance">
                    <span value='-1' class="gre-cur">不限</span>
                    <span value='500'>500米</span>
                    <span value='1000'>1km</span>
                    <span value='2000'>2km</span>
                    <span value="5000">5km以上</span>
                </div>
            </div>
            <div class="menu1 clearfix">
                <div class="menu-item">时&nbsp;间&nbsp;段</div>
                <div class="clearfix menu-cont mate-timed">
                    <span value='-1' class="gre-cur">不限</span>
                    <span value='1'>周六08:00~11:00</span>
                    <span value='2'>周六11:00~14:00</span>
                    <span value='3'>周六14:00~17:00</span>
                    <span value="4">周日08:00~11:00</span>
                    <span value="5">周日11:00~14:00</span>
                    <span value="6">周日14:00~17:00</span>
                </div>
            </div>
        </div>
        <div class="near-list">
            <p class="near-p">共有<em class="mate-count"></em>位邻居符合条件</p>
            <div id="near-mates"></div>
            <div class="new-page-links"></div>
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
    <div class="bq-list clearfix biaoq-all">
        <span code="100" tag="旅行">旅行</span>
        <span code="101" tag="英语">英语</span>
        <span code="102" tag="阅读">阅读</span>
        <span code="103" tag="足球">足球</span>
        <span code="104" tag="篮球">篮球</span>
        <span code="105" tag="羽毛球">羽毛球</span>
        <span code="106" tag="网球">网球</span>
        <span code="107" tag="乒乓球">乒乓球</span>
        <span code="108" tag="轮滑跆">轮滑跆</span>
        <span code="109" tag="拳道">拳道</span>
        <span code="110" tag="跳绳">跳绳</span>
        <span code="111" tag="歌唱">歌唱</span>
        <span code="112" tag="表演">表演</span>
        <span code="113" tag="舞蹈">舞蹈</span>
        <span code="114" tag="美术">美术</span>
        <span code="115" tag="钢琴">钢琴</span>
        <span code="116" tag="古筝">古筝</span>
        <span code="117" tag="二胡">二胡</span>
        <span code="118" tag="小提琴">小提琴</span>
        <span code="119" tag="笛子">笛子</span>
        <span code="120" tag="架子鼓">架子鼓</span>
        <span code="121" tag="围棋">围棋</span>
        <span code="122" tag="跳棋">跳棋</span>
        <span code="123" tag="象棋">象棋</span>
        <span code="124" tag="桥牌">桥牌</span>
        <span code="125" tag="演讲">演讲</span>
        <span code="126" tag="航模">航模</span>
        <span code="127" tag="航海">航海</span>
        <span code="128" tag="机器人">机器人</span>
        <span code="129" tag="演讲">演讲</span>
    </div>
    <p class="p2">已选标签</p>
    <div class="bq-list clearfix biaoq-selected">
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
            <option>是的范德萨</option>
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

<script type="text/template" id="mateBox">
    {{~it:value:index}}
    <div class="near-li">
        <img src='{{=value.avatar}}'>
        <div class="near-infor">
            <p class="p1">
                <span class="sp1">{{=value.nickName}}</span>
                <span class="sp2">查看更多</span>
            </p>
            <p class="p2">
                标签：{{~value.tags:tag:i}}
                <i>{{=tag}}</i>、
                {{~}}
            </p>
            <p class="p2">
                您的认证好友<i>卢佳佳</i>对他进行了认证
            </p>
            <p class="p2">
                <img src="/static/images/findnearby_zuobiao.png">距离您<i>{{=value.distance}}</i>米
            </p>
        </div>
        <button class="btn-lyl">聊一聊</button>
    </div>
    {{~}}
</script>
<!--发起活动-->
<%@include file="../common/footer.jsp" %>
<script src="/static/js/sea.js"></script>
<script src="/static/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('/static/js/modules/mate/seekMate.js', function (seekMate) {
        seekMate.init();
    });
</script>

</body>
</html>