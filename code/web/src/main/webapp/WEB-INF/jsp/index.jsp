<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>复兰教育社区</title>
    <meta property="qc:admins" content="227117016366541166375"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge, chrome=1">
    <meta name="renderer" content="webkit">
    <meta name="Keywords" content="商城，复兰商城，复兰科技，青少年，教育，学习，读书，才艺，创客，兴趣">
    <link rel="stylesheet" type="text/css" href="/static/css/entrance/mallindex.css">
    <link rel="stylesheet" type="text/css" href="/static/css/main.css">
    <script type="text/javascript" src="/static/js/modules/core/0.1.0/jquery.min.js"></script>
    <script type="text/javascript">
        $(function () {
            var container = $('#container');
            var list = $('#list');
            var buttons = $('#buttons span');
            var prev = $('#prev');
            var next = $('#next');
            var index = 1;
            var len = 4;
            var interval = 3000;
            var timer;


            function animate (offset) {
                var left = parseInt(list.css('left')) + offset;
                if (offset>0) {
                    offset = '+=' + offset;
                }
                else {
                    offset = '-=' + Math.abs(offset);
                }
                list.animate({'left': offset}, 300, function () {
                    if(left > -200){
                        list.css('left', -1250 * len);
                    }
                    if(left < (-1250 * len)) {
                        list.css('left', -1250);
                    }
                });
            }

            function showButton() {
                buttons.eq(index-1).addClass('on').siblings().removeClass('on');
            }

            function play() {
                timer = setTimeout(function () {
                    next.trigger('click');
                    play();
                }, interval);
            }
            function stop() {
                clearTimeout(timer);
            }

            next.bind('click', function () {
                if (list.is(':animated')) {
                    return;
                }
                if (index == 4) {
                    index = 1;
                }
                else {
                    index += 1;
                }
                animate(-1250);
                showButton();
            });

            prev.bind('click', function () {
                if (list.is(':animated')) {
                    return;
                }
                if (index == 1) {
                    index = 4;
                }
                else {
                    index -= 1;
                }
                animate(1250);
                showButton();
            });

            buttons.each(function () {
                $(this).bind('click', function () {
                    if (list.is(':animated') || $(this).attr('class')=='on') {
                        return;
                    }
                    var myIndex = parseInt($(this).attr('index'));
                    var offset = -1250 * (myIndex - index);

                    animate(offset);
                    index = myIndex;
                    showButton();
                })
            });

            container.hover(stop, play);

            play();

        });
    </script>
    <style>
        #buttons{
            width:80px;
            margin-left: -40px;
        }
        .btn-bg{
            width: 88px;
            margin-left: -44px;
        }
        #list{
            width: 10000px;
        }
    </style>
</head>
<body login="${login}">
<%@ include file="./common/head.jsp" %>
<div class="container clearfix">
    <!-- 广告 -->
    <%-- <div class="top_banner mid-cont"><img src="/static/images/forum/banner_hot23.png" style="cursor: pointer" onclick="window.open('/forum/competition.do')"></div>--%>

    <div class="top_banner mid-cont">
        <div id="container">
            <div id="list" style="left: -1250px;">
                <img src="/static/images/compe_banner.jpg" alt="4" style="cursor: pointer" onclick="window.open('/competition')">
                <img src="/static/images/jixian.jpg" alt="1" style="cursor: pointer" onclick="window.open('http://www.fulaan.com/forum/postIndex.do?pSectionId=575d4d8e0cf2ca0383166bba')">
                <img src="/static/images/hanghai.jpg" alt="2" style="cursor: pointer" onclick="window.open('http://hanghai.fulaan.com')">
                <img src="/static/images/forum_banner.jpg" alt="3" style="cursor: pointer" onclick="window.open('/forum')">
                <img src="/static/images/compe_banner.jpg" alt="4" style="cursor: pointer" onclick="window.open('/competition')">
                <img src="/static/images/jixian.jpg" alt="1" style="cursor: pointer" onclick="window.open('http://www.fulaan.com/forum/postIndex.do?pSectionId=575d4d8e0cf2ca0383166bba')">
            </div>
            <div class="btn-bg"></div>
            <div id="buttons">
                <span index="1" class="on"></span>
                <span index="2"></span>
                <span index="3"></span>
                <span index="4"></span>
            </div>
            <a href="javascript:;" id="prev" class="arrow">&lt;</a>
            <a href="javascript:;" id="next" class="arrow">&gt;</a>
        </div>
    </div>
    <!-- 广告 -->
    <div id="listContents" style="display:none">
        <!-- 晒才艺 begin -->
        <div class="bk-block mid-cont clearfix">
            <div class="img-jh clearfix">
                <img src="/static/images/entrance/cy_img1.png" width="144px" height="144px">
                <img src="/static/images/entrance/cy_img2.png" width="144px" height="144px">
                <img src="/static/images/entrance/cy_img3.png" width="144px" height="144px">
                <img src="/static/images/entrance/cy_img4.png" width="144px" height="144px">
                <div class="div-more"></div>
                <span class="span-more"
                      onclick="window.open('/mall/index.do?categoryId=56eb6a1d0cf234ce7e479c24')">More</span>
            </div>
            <div class="left-arrow"><span style="cursor: pointer"
                                          onclick="window.open('/mall/index.do?categoryId=56eb6a1d0cf234ce7e479c24')">文具 / 乐器 / 棋类</span>
                <div class="arrow-leftj"></div>
            </div>
            <div class="cy-card card-cy" value="575d4d2c0cf2ca0383166bb9">
            </div>
            <div class="right-arrow" style="cursor: pointer"
                 onclick="window.open('/forum/postIndex.do?pSectionId=575d4d2c0cf2ca0383166bb9')">晒才艺
                <div class="arrow-rightj"></div>
            </div>
        </div>
        <!-- 晒才艺 end-->

        <!-- STEM创客空间begin-->
        <div class="bk-block mid-cont clearfix">
            <div class="img-jh clearfix">
                <img src="/static/images/entrance/kx_img1.png" width="144px" height="144px">
                <img src="/static/images/entrance/kx_img2.png" width="144px" height="144px">
                <img src="/static/images/entrance/kx_img3.png" width="144px" height="144px">
                <img src="/static/images/entrance/kx_img4.png" width="144px" height="144px">
                <div class="div-more"></div>
                <span class="span-more"
                      onclick="window.open('/mall/index.do?categoryId=56eb6a0b0cf234ce7e479c1c')">More</span>
            </div>
            <div class="left-arrow"><span style="cursor: pointer"
                                          onclick="window.open('/mall/index.do?categoryId=56eb6a0b0cf234ce7e479c1c')">STEM创客 / 益智玩具</span>
                <div class="arrow-leftj"></div>
            </div>
            <div class="cy-card card-kx" value="575d4d8e0cf2ca0383166bba">
            </div>
            <div class="right-arrow" style="cursor: pointer"
                 onclick="window.open('/forum/postIndex.do?pSectionId=575d4d8e0cf2ca0383166bba')">STEM创客空间
                <div class="arrow-rightj"></div>
            </div>
        </div>
        <!-- STEM创客空间end -->

        <!-- 读书begin -->
        <div class="bk-block mid-cont clearfix">
            <div class="img-jh clearfix">
                <img src="/static/images/entrance/xb_img1.png" width="144px" height="144px">
                <img src="/static/images/entrance/xb_img2.png" width="144px" height="144px">
                <img src="/static/images/entrance/xb_img3.png" width="144px" height="144px">
                <img src="/static/images/entrance/xb_img4.png" width="144px" height="144px">
                <div class="div-more"></div>
                <span class="span-more"
                      onclick="window.open('/mall/index.do?categoryId=56eb6a2d0cf234ce7e479c26')">More</span>
            </div>
            <div class="left-arrow"><span style="cursor: pointer"
                                          onclick="window.open('/mall/index.do?categoryId=56eb6a2d0cf234ce7e479c26')">教辅教材 / 小说文学</span>
                <div class="arrow-leftj"></div>
            </div>
            <div class="cy-card card-xb" value="575d4dbc0cf2ca0383166bbb">
            </div>
            <div class="right-arrow" style="cursor: pointer"
                 onclick="window.open('/forum/postIndex.do?pSectionId=575d4dbc0cf2ca0383166bbb')">读书 / 学霸
                <div class="arrow-rightj"></div>
            </div>
        </div>
        <!-- 读书end -->

        <!-- 演讲与口才begin -->
        <div class="bk-block mid-cont clearfix">
            <div class="img-jh clearfix">
                <%--<img src="/static/images/entrance/yj_img1.png" width="144px" height="144px">
                <img src="/static/images/entrance/yj_img2.png" width="144px" height="144px">
                <img src="/static/images/entrance/yj_img3.png" width="144px" height="144px">
                <img src="/static/images/entrance/yj_img4.png" width="144px" height="144px">--%>
                <div class="div-more"></div>
                <span class="span-more"
                      onclick="window.open('/mall/index.do?categoryId=56eb6a350cf234ce7e479c27')">More</span>
            </div>
            <div class="left-arrow"><span style="cursor: pointer"
                                          onclick="window.open('/mall/index.do?categoryId=56eb6a350cf234ce7e479c27')">名人传记 / 成功励志</span>
                <div class="arrow-leftj"></div>
                <div class="arrow-leftj"></div>
            </div>
            <div class="cy-card card-yj" value="575d4e140cf2ca0383166bbc">
            </div>
            <div class="right-arrow" style="cursor: pointer"
                 onclick="window.open('/forum/postIndex.do?pSectionId=575d4e140cf2ca0383166bbc')">
                演讲口才
                <div class="arrow-rightj"></div>
            </div>
        </div>
        <!-- 演讲与口才end -->

        <!-- 安全卫士begin -->
        <div class="bk-block mid-cont clearfix">
            <div class="img-jh clearfix">
                <img src="/static/images/entrance/aq_img1.png" width="144px" height="144px">
                <img src="/static/images/entrance/aq_img2.png" width="144px" height="144px">
                <img src="/static/images/entrance/aq_img3.png" width="144px" height="144px">
                <img src="/static/images/entrance/aq_img4.png" width="144px" height="144px">
                <div class="div-more"></div>
                <span class="span-more"
                      onclick="window.open('/mall/index.do?categoryId=56eb6a440cf234ce7e479c29')">More</span>
            </div>
            <div class="left-arrow"><span style="cursor: pointer"
                                          onclick="window.open('/mall/index.do?categoryId=56eb6a440cf234ce7e479c29')">智能硬件</span>
                <div class="arrow-leftj"></div>
            </div>
            <div class="cy-card card-aq" value="575d4e4a0cf2ca0383166bbd">
            </div>
            <div class="right-arrow" style="cursor: pointer"
                 onclick="window.open('/forum/postIndex.do?pSectionId=575d4e4a0cf2ca0383166bbd')">
                安全健康
                <div class="arrow-rightj"></div>
            </div>
        </div>
        <!-- 安全卫士end -->
    </div>
    <div class="container-s" style="">
        <!--cont-left-->
        <div class="cont-left">
            <div class="div-list clearfix" id="kkl">
            </div>

            <div class="timegood">
                <div class="good-nav" id="goodscategory">
                </div>
            </div>

            <div class="goodlist clearfix" id="kk">
            </div>

        </div>
        <!--cont-left-->

        <!--cont-right-->
        <div class="cont-right">
            <div class="hot-list">
                <p class="p-hot">热门大赛</p>
                <ul class="ul-hot clearfix" id="talentList">
                </ul>
            </div>
            <div class="hot-list  mt40">
                <p class="p-hot">推荐文章</p>
                <ul class="ul-hot clearfix">
                    <c:forEach items="${articles}" var="article">
                        <li>
                            <a href="${article['target']}"><img src="${article['imageUrl']}"></a>
                            <a href="${article['target']}"><p class="pw">${article['title']}</p></a>
                            <p class="pt">
                                <em class="em1">${article['time']}</em>
                                <em class="em2">${article['count']}</em>
                            </p>
                        </li>
                    </c:forEach>
                </ul>
            </div>
        </div>
        <!--cont-right-->
    </div>
</div>

<%@ include file="./common/footer.jsp" %>
<a hidden class="backtop"></a>

<!--============登录================-->
<%@ include file="./common/login.jsp" %>

<script id="nvTml" type="text/template">
    {{ for(var i in it) { }}
    <font>{{=it[i].name}}</font>/
    {{ } }}
</script>

<script id="talentTml" type="text/template">
    {{ for(var i in it) { }}
    <li>
        <img src="{{=it[i].activityImage}}"
             onclick="window.open('/forum/postDetail.do?pSectionId={{=it[i].postSectionId}}&postId={{=it[i].fpostId}}')">
        <p class="py"><u><a href="/forum/postDetail.do?pSectionId={{=it[i].postSectionId}}&postId={{=it[i].fpostId}}"
                            target="_blank">{{=it[i].plainText}}</a></u></p>
    </li>
    {{ } }}
</script>

<script id="goodsListTmpl" type="text/template">
    {{ for(var i in it) { }}
    <span onclick="window.open('/mall/detail.do?id={{=it[i].goodsId}}')">
    <div class="good-li">
        <p class="good-name">{{=it[i].goodsName}}</p>
        <img src="{{=it[i].suggestImage}}?imageView2/2/w/200/h/200">
        <span>￥&nbsp;{{=it[i].discountPrice/100}}</span>
        <button>立即抢购</button>
    </div>
    </span>
    {{ } }}
</script>

<script id="goodscategoryTmpl" type="text/template">
    <em>精品限时购</em>
    {{ for(var i in it) { }}
    <span class="spanItem" value="{{=it[i].id}}"></span>
    {{ } }}
</script>

<script id="kklTml" type="text/template">
    {{ for(var i in it) { }}
    <div class="mk fl" onclick="window.open('/forum/postIndex.do?pSectionId={{=it[i].fSectionId}}')">
        <div class="d11">{{=it[i].memo}}</div>
        <div class="d12"></div>
        <div class="d21">
            <h3>{{=it[i].name}}</h3>
            <p>{{=it[i].memoName}}</p>
            <p class="p-icon" value="{{=it[i].fSectionId}}">
            </p>
        </div>
        <div class="d22"></div>
    </div>
    {{ } }}
</script>

<script id="ssTml" type="text/template">
    {{ for(var i in it) { }}
    <img src="/static/images/forum/pp_white.png"><span>{{=it[i].totalCommentCount}}</span>
    <img src="/static/images/forum/eye_white.png"><span>{{=it[i].totalScanCount}}</span>
    {{ } }}
</script>

<script id="ContentTml" type="text/template">
    {{ for(var i in it) { }}
    <div class="card-name">
        {{=it[i].memo}}
        <em>版主：{{=it[i].sectionName}}</em>
    </div>
    <span onclick="window.open('/forum/postIndex.do?pSectionId={{=it[i].fSectionId}}')"> <div class="card-cont">
      <div class="card-logo"></div>
      <%--
        <img src="/static/images/forum/logo_st.png">
        <img src="/static/images/forum/logo_ds.png">
        <img src="/static/images/forum/logo_yj.png">
        <img src="/static/images/forum/logo_aq.png">
      --%>
       <p class="p1">{{=it[i].name}}</p>
      <p class="p2">{{=it[i].memoName}}</p>
      <p class="p3" value="{{=it[i].fSectionId}}">

      </p>
    </div>
    </span>
    {{ } }}
</script>

<script id="ScTml" type="text/template">
    {{ for(var i in it) { }}
    <span>主题数：{{=it[i].themeCount}}</span>
    <span>贴数：{{=it[i].postCount}}</span>
    <em class='em-p'>{{=it[i].totalCommentCount}}</em>
    <em class="em-c">{{=it[i].totalScanCount}}</em>
    {{ } }}
</script>
<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script src="/static/js/sea.js"></script>
<!-- Custom js -->
<script src="/static/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('/static/js/modules/mall/0.1.0/entrance.js');
</script>
</body>
</html>
