<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 2016/12/5
  Time: 9:48
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>找培训</title>
    <meta charset="utf-8">
    <link rel="stylesheet" type="text/css" href="/static/css/train/findtrain.css">
    <script type="text/javascript" src="/static/js/modules/train/jquery-1.8.0.min.js"></script>
    <script type="text/javascript" src="/static/js/modules/train/trainList.js"></script>
</head>
<body>
<div class="container">
    <div class="top-city">
        <img src="/static/images/train/location01.png">
        <div class="city">
            <span id="DY_site_name" class="city-name Left">上海</span>
            <div id="JS_hide_city_menu_11" class="city-select cut_handdler Left">
                <a href="javascript:void(0);" class="common-bg selector">切换城市</a>
                <div id="JS_header_city_bar_box" class="hide_city_group">
                    <div class="hideMap">
                        <div class="showPanel clearfix">
                            <div class="Left mycity">
                                <div id="JS_current_city_box">
                                    当前城市：<strong id="JS_city_current_city">北京</strong>
                                </div>
                                <div id="JS_default_city_delete" style="display: none;"></div>
                            </div>
                        </div>
                        <div class="showPanel showPanel2 clearfix">
                            <div class="hot_city" id="JS_header_city_hot">
                            </div>
                            <div class="city_words mt10" id="JS_header_city_char">
                            </div>
                        </div>
                        <div class="scrollBody">
                            <div class="cityMap clearfix">
                                <table id="JS_header_city_list" class="city_list" style="margin-top: 0px;">
                                    <tbody>
                                    </tbody>
                                </table>
                            </div>
                            <div class="scrollBar">
                                <span id="JS_header_city_bar" style="margin-top: 0px;"></span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <span class="common-bg city-logo"></span>
        </div>
        <button>添加商户信息</button>
        <button>老师入口</button>
        <button>培训机构入口</button>
    </div>
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
    <div class="train-type">
        <div class="clearfix d1f ">
            <div class="dl">
                <p class="sp1">分类</p>
                <span class="cur2">不限</span>
            </div>
            <div class="dr clearfix">
                <p><span>外语培训</span></p>
                <p><span>音乐培训</span></p>
                <p><span>美术培训</span></p>
                <p><span>兴趣生活</span></p>
                <p><span>计算机培训</span></p>
            </div>
        </div>
        <div class="clearfix d2f">
            <div class="dl">
                <p class="sp1">位置</p>
                <span class="cur2">不限</span>
            </div>
            <div class="dr clearfix">
                <p><span>浦东新区</span></p>
                <p><span>闵行区</span></p>
                <p><span>普陀区</span></p>
                <p><span>嘉定区</span></p>
                <p><span>徐汇区</span></p>
                <p><span>宝山区</span></p>
                <p><span>长宁区</span></p>
                <p><span>虹口区</span></p>
                <p><span>黄埔区</span></p>
                <p><span>杨浦区</span></p>
                <p><span>松江区</span></p>
                <p><span>闸北区</span></p>
                <p><span>静安区</span></p>
                <p><span>卢湾区</span></p>
                <p><span>奉贤区</span></p>
                <p><span>青浦区</span></p>
                <p><span>金山区</span></p>
                <p><span>崇明区</span></p>
            </div>
        </div>
    </div>
    <div class="lesson-menu">
        <span class="cur3 bln">默认</span>
        <span>最新发布<img src="/static/images/train/arrow_lesson.png"></span>
        <p class="p-page">
            <i class="i1"></i><em>1</em>/1<i class="i2"></i>
        </p>
    </div>
    <ul class="lesson-list clearfix">
        <li>
            <img src="/static/images/train/peixun.png">
            <div class="name">环球美邦国际教育</div>
            <div class="star">
                <span>4.5</span>
                <p>
                    <img src="/static/images/train/star_gray.png">
                    <img src="/static/images/train/star_gray.png">
                    <img src="/static/images/train/star_golden.png">
                    <img src="/static/images/train/star_golden.png">
                    <img src="/static/images/train/star_golden.png">
                </p>
            </div>
        </li>
        <li>
            <img src="/static/images/train/peixun.png">
            <div class="name">环球美邦国际教育</div>
            <div class="star">
                <span>4.5</span>
                <p>
                    <img src="/static/images/train/star_gray.png">
                    <img src="/static/images/train/star_gray.png">
                    <img src="/static/images/train/star_golden.png">
                    <img src="/static/images/train/star_golden.png">
                    <img src="/static/images/train/star_golden.png">
                </p>
            </div>
        </li>
        <li>
            <img src="/static/images/train/peixun.png">
            <div class="name">环球美邦国际教育</div>
            <div class="star">
                <span>4.5</span>
                <p>
                    <img src="/static/images/train/star_gray.png">
                    <img src="/static/images/train/star_gray.png">
                    <img src="/static/images/train/star_golden.png">
                    <img src="/static/images/train/star_golden.png">
                    <img src="/static/images/train/star_golden.png">
                </p>
            </div>
        </li>
        <li>
            <img src="/static/images/train/peixun.png">
            <div class="name">环球美邦国际教育</div>
            <div class="star">
                <span>4.5</span>
                <p>
                    <img src="/static/images/train/star_gray.png">
                    <img src="/static/images/train/star_gray.png">
                    <img src="/static/images/train/star_golden.png">
                    <img src="/static/images/train/star_golden.png">
                    <img src="/static/images/train/star_golden.png">
                </p>
            </div>
        </li>
        <li>
            <img src="/static/images/train/peixun.png">
            <div class="name">环球美邦国际教育</div>
            <div class="star">
                <span>4.5</span>
                <p>
                    <img src="/static/images/train/star_gray.png">
                    <img src="/static/images/train/star_gray.png">
                    <img src="/static/images/train/star_golden.png">
                    <img src="/static/images/train/star_golden.png">
                    <img src="/static/images/train/star_golden.png">
                </p>
            </div>
        </li>
        <li>
            <img src="/static/images/train/peixun.png">
            <div class="name">环球美邦国际教育</div>
            <div class="star">
                <span>4.5</span>
                <p>
                    <img src="/static/images/train/star_gray.png">
                    <img src="/static/images/train/star_gray.png">
                    <img src="/static/images/train/star_golden.png">
                    <img src="/static/images/train/star_golden.png">
                    <img src="/static/images/train/star_golden.png">
                </p>
            </div>
        </li>
        <li>
            <img src="/static/images/train/peixun.png">
            <div class="name">环球美邦国际教育</div>
            <div class="star">
                <span>4.5</span>
                <p>
                    <img src="/static/images/train/star_gray.png">
                    <img src="/static/images/train/star_gray.png">
                    <img src="/static/images/train/star_golden.png">
                    <img src="/static/images/train/star_golden.png">
                    <img src="/static/images/train/star_golden.png">
                </p>
            </div>
        </li>
        <li>
            <img src="/static/images/train/peixun.png">
            <div class="name">环球美邦国际教育</div>
            <div class="star">
                <span>4.5</span>
                <p>
                    <img src="/static/images/train/star_gray.png">
                    <img src="/static/images/train/star_gray.png">
                    <img src="/static/images/train/star_golden.png">
                    <img src="/static/images/train/star_golden.png">
                    <img src="/static/images/train/star_golden.png">
                </p>
            </div>
        </li>
        <li>
            <img src="/static/images/train/peixun.png">
            <div class="name">环球美邦国际教育</div>
            <div class="star">
                <span>4.5</span>
                <p>
                    <img src="/static/images/train/star_gray.png">
                    <img src="/static/images/train/star_gray.png">
                    <img src="/static/images/train/star_golden.png">
                    <img src="/static/images/train/star_golden.png">
                    <img src="/static/images/train/star_golden.png">
                </p>
            </div>
        </li>
        <li>
            <img src="/static/images/train/peixun.png">
            <div class="name">环球美邦国际教育</div>
            <div class="star">
                <span>4.5</span>
                <p>
                    <img src="/static/images/train/star_gray.png">
                    <img src="/static/images/train/star_gray.png">
                    <img src="/static/images/train/star_golden.png">
                    <img src="/static/images/train/star_golden.png">
                    <img src="/static/images/train/star_golden.png">
                </p>
            </div>
        </li>
        <li>
            <img src="/static/images/train/peixun.png">
            <div class="name">环球美邦国际教育</div>
            <div class="star">
                <span>4.5</span>
                <p>
                    <img src="/static/images/train/star_gray.png">
                    <img src="/static/images/train/star_gray.png">
                    <img src="/static/images/train/star_golden.png">
                    <img src="/static/images/train/star_golden.png">
                    <img src="/static/images/train/star_golden.png">
                </p>
            </div>
        </li>
        <li>
            <img src="/static/images/train/peixun.png">
            <div class="name">环球美邦国际教育</div>
            <div class="star">
                <span>4.5</span>
                <p>
                    <img src="/static/images/train/star_gray.png">
                    <img src="/static/images/train/star_gray.png">
                    <img src="/static/images/train/star_golden.png">
                    <img src="/static/images/train/star_golden.png">
                    <img src="/static/images/train/star_golden.png">
                </p>
            </div>
        </li>
        <li>
            <img src="/static/images/train/peixun.png">
            <div class="name">环球美邦国际教育</div>
            <div class="star">
                <span>4.5</span>
                <p>
                    <img src="/static/images/train/star_gray.png">
                    <img src="/static/images/train/star_gray.png">
                    <img src="/static/images/train/star_golden.png">
                    <img src="/static/images/train/star_golden.png">
                    <img src="/static/images/train/star_golden.png">
                </p>
            </div>
        </li>
        <li>
            <img src="/static/images/train/peixun.png">
            <div class="name">环球美邦国际教育</div>
            <div class="star">
                <span>4.5</span>
                <p>
                    <img src="/static/images/train/star_gray.png">
                    <img src="/static/images/train/star_gray.png">
                    <img src="/static/images/train/star_golden.png">
                    <img src="/static/images/train/star_golden.png">
                    <img src="/static/images/train/star_golden.png">
                </p>
            </div>
        </li>
        <li>
            <img src="/static/images/train/peixun.png">
            <div class="name">环球美邦国际教育</div>
            <div class="star">
                <span>4.5</span>
                <p>
                    <img src="/static/images/train/star_gray.png">
                    <img src="/static/images/train/star_gray.png">
                    <img src="/static/images/train/star_golden.png">
                    <img src="/static/images/train/star_golden.png">
                    <img src="/static/images/train/star_golden.png">
                </p>
            </div>
        </li>
    </ul>
</div>

</body>
</html>
