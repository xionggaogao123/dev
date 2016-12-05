<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 2016/12/5
  Time: 9:50
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>培训详情</title>
    <meta charset="utf-8">
    <link rel="stylesheet" type="text/css" href="/static/css/train/findtrain.css">
    <script type="text/javascript" src="/static/js/modules/train/jquery-1.8.0.min.js"></script>
    <script type="text/javascript" src="/static/js/modules/train/jquery.raty.min.js"></script>
    <script type="text/javascript" src="/static/js/modules/train/trainDetail.js"></script>
    <script type="text/javascript">
        $(function(){
            $("#p-star").raty({
                starOff: '/static/images/train/star_gray.png',
                starOn : '/static/images/train/star_golden.png',
                cancel:false,
                target: "#p-rel",
                hints:['1分 差','2分 较差','3分 及格','4分 满意','5分 优秀'],
                targetKeep:true,
                width:150,
            })
        })
    </script>
</head>
<body class="gray-cont">
<div class="container" style="padding-top: 21px;width: 1100px;">
    <div class="train-nav">
        <span>找家教</span>
        <span class="cur1">找培训</span>
        <span>在线学习</span>
        <span>亲子活动</span>
        <button>搜索</button>
        <input type="text">
        <select>
            <option>家教</option>
            <option>培训</option>
            <option>在线学习</option>
            <option>亲子活动</option>
        </select>
        <div class="select-arrow"></div>
    </div>
    <div class="long-location">上海学习培训 > 普陀区 > 长寿路 > 外语培训 > 英语 > 环球美邦国际教育(托福雅思SAT)</div>
    <div class="train-left">
        <div class="train-intro">
            <img src="/static/images/train/peixun2.png">
            <div class="intro-txt">
                <div class="p1">[源深體育中心]浦東大道圍棋教室</div>
                <div class="p2 clearfix">
                    <span class="sp1">营业时间：</span>
                    <span class="sp2">8:00-23:00 周一至周日</span>
                </div>
                <div class="p2 clearfix">
                    <span class="sp1">上课地址：</span>
                    <span class="sp2">宝山区宝山区大事发生地方大范甘迪21松45收</span>
                </div>
                <div class="p2 clearfix">
                    <span class="sp1">咨询电话：</span>
                    <span class="sp2">021-12345678</span>
                </div>
                <div class="intro-star">
                    <p>
                        <img src="/static/images/train/star_golden.png">
                        <img src="/static/images/train/star_golden.png">
                        <img src="/static/images/train/star_golden.png">
                        <img src="/static/images/train/star_gray.png">
                        <img src="/static/images/train/star_gray.png">

                    </p>6
                    <img src="/static/images/train/shoucang.png">
                </div>
            </div>
        </div>
        <div class="train-detail">
            <div class="nav">
                <span class="cur4 sp1">培训机构详情</span>
                <span class="sp2">用户评论<em>（1000）</em></span>
            </div>
            <div class="train-infor">
                <h3 class="h3-intro">培训机构简介</h3>
                <div>松岛枫松岛枫松岛枫的双方各松岛枫松岛枫松岛枫的双方各松岛枫松岛枫松岛枫的双方各松岛枫松岛枫松岛枫的双方各松岛枫松岛枫松岛枫的双方各松岛枫松岛枫松岛枫的双方各松岛枫松岛枫松岛枫的双方各松岛枫松岛枫松岛枫的双方各松岛枫松岛枫松岛枫的双方各松岛枫松岛枫松岛枫的双方各松岛枫松岛枫松岛枫的双方各松岛枫松岛枫松岛枫的双方各松岛枫松岛枫松岛枫的双方各松岛枫松岛枫松岛枫的双方各松岛枫松岛枫松岛枫的双方各松岛枫松岛枫松岛枫的双方各松岛枫松岛枫松岛枫的双方各松岛枫松岛枫松岛枫的双方各松岛枫松岛枫松岛枫的双方各松岛枫松岛枫松岛枫的双方各松岛枫松岛枫松岛枫的双方各松岛枫松岛枫松岛枫的双方各松岛枫松岛枫松岛枫的双方各松岛枫松岛枫松岛枫的双方各松岛枫松岛枫松岛枫的双方各松岛枫松岛枫松岛枫的双方各松岛枫松岛枫松岛枫的双方各松岛枫松岛枫松岛枫的双方各松岛枫松岛枫松岛枫的双方各松岛枫松岛枫松岛枫的双方各</div>
            </div>
            <div class="train-pj">
                <div class="pj-cont">
                    <div class="pj-tit">评价 :</div>
                    <button>提交点评</button>
                    <textarea></textarea>
                    <div class="star-pj">
                        <p>星级评分</p>
                        <p id="p-star"></p>
                        <p id="p-rel"></p>
                    </div>
                </div>
                <ul class="ul-train-pj">
                    <li>
                        <img src="/static/images/train/hot_train.png" class="head-img">
                        <p class="p1">asdaasasdfsdf</p>
                        <p>
                            <img src="/static/images/train/star_golden.png">
                            <img src="/static/images/train/star_golden.png">
                            <img src="/static/images/train/star_golden.png">
                            <img src="/static/images/train/star_golden.png">
                            <img src="/static/images/train/star_gray.png">
                            <span>4.5</span>
                        </p>
                        <p>课程不粗按时大大说的vfdf</p>
                    </li>
                </ul>
            </div>
        </div>
    </div>
    <div class="train-right">
        <div class="train-map"></div>
        <div class="train-hot">
            <div class="nav">
                <span>热门培训机构</span>
            </div>
            <ul class="ul-hotrain">
                <li class="clearfix">
                    <img src="/static/images/train/hot_train.png">
                    <div class="hot-infor">
                        <p>北京万达集团北京万达集团北京万达集</p>
                        <p>
                            <img src="/static/images/train/star_golden.png">
                            <img src="/static/images/train/star_golden.png">
                            <img src="/static/images/train/star_golden.png">
                            <img src="/static/images/train/star_gray.png">
                            <img src="/static/images/train/star_gray.png">
                            <span>4.5</span>
                        </p>
                    </div>
                </li>
                <li class="clearfix">
                    <img src="/static/images/train/hot_train.png">
                    <div class="hot-infor">
                        <p>北京万达集团北京万达集团北京万达集</p>
                        <p>
                            <img src="/static/images/train/star_golden.png">
                            <img src="/static/images/train/star_golden.png">
                            <img src="/static/images/train/star_golden.png">
                            <img src="/static/images/train/star_gray.png">
                            <img src="/static/images/train/star_gray.png">
                            <span>4.5</span>
                        </p>
                    </div>
                </li>
                <li class="clearfix">
                    <img src="/static/images/train/hot_train.png">
                    <div class="hot-infor">
                        <p>北京万达集团北京万达集团北京万达集</p>
                        <p>
                            <img src="/static/images/train/star_golden.png">
                            <img src="/static/images/train/star_golden.png">
                            <img src="/static/images/train/star_golden.png">
                            <img src="/static/images/train/star_gray.png">
                            <img src="/static/images/train/star_gray.png">
                            <span>4.5</span>
                        </p>
                    </div>
                </li>
                <li class="clearfix">
                    <img src="/static/images/train/hot_train.png">
                    <div class="hot-infor">
                        <p>北京万达集团北京万达集团北京万达集</p>
                        <p>
                            <img src="/static/images/train/star_golden.png">
                            <img src="/static/images/train/star_golden.png">
                            <img src="/static/images/train/star_golden.png">
                            <img src="/static/images/train/star_gray.png">
                            <img src="/static/images/train/star_gray.png">
                            <span>4.5</span>
                        </p>
                    </div>
                </li>
            </ul>
        </div>
    </div>

</div>
</body>
</html>
