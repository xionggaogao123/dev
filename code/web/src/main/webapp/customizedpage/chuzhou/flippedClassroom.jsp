<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>安徽省滁州市智慧校园</title>
    <link rel="stylesheet" type="text/css" href="/customizedpage/chuzhou/style/style.css"/>
    <link href="//netdna.bootstrapcdn.com/font-awesome/4.0.3/css/font-awesome.css" rel="stylesheet">

    <script type="text/javascript" src="/customizedpage/chuzhou/js/jquery-1.11.1.min.js"></script>
    <script type="text/javascript" src="/customizedpage/chuzhou/js/indexjq.js"></script>
    <script type="text/javascript" src="/customizedpage/chuzhou/js/iepng.js"></script>
    <!--[if IE 6]>
    <script src="js/iepng.js" type="text/javascript"></script>
    <script type="text/javascript">
        EvPNG.fix('div,ul,img,li,input,span,b,h1,h2,h3,h4,a');
    </script>

    <![endif]-->
    <script type="text/javascript">
        $(function () {
            var name = '${session.currentUser.userName}';

            if (name == null || name == '') {
                name = 'test';
            }
            $('.menu').on('click', function () {
                window.open('http://121.40.203.77/demo/demo1.jsp?action=create&username=' + name);
            });
        });
    </script>

    <script type="text/javascript">
        $(function () {

            $('.input-password').keydown(function (event) {
                if (event.which == 13) {
                    loginIndex(1);
                }
            });
            $('.password2').keydown(function (event) {
                if (event.which == 13) {
                    loginIndex(2);
                }
            });
        });


        function loginIndex(index) {
            var username = $(".input-account").val();
            var password = $(".input-password").val();
            if (index == 2) {
                $('.username2').removeClass('error');
                $('.password2').removeClass('error');
                $('#buttonloadingindex').css('display', '');
                $("#logincontent2 .errorMessage").html("");
                username = $("#username2").val();
                password = $("#password2").val();
            }
            if (index == 1) {
                $('.login-btn').addClass('active');
            }
            $.ajax({
                url: "/user/login.do",
                type: "post",
                dataType: "json",
                data: {
                    'name': username,
                    'pwd': password
                },
                success: function (data) {
                    if (index == 1) {
                        if (data.code == 200) {
                            window.location = "/user?version=91&index=6";
                        } else {

                            if (data.message == '用户名非法') {
                                $('.input-account').addClass('error');
                                $('.username-error').show();
                            } else {
                                $('.input-password').addClass('error');
                                $('.password-error').show();
                            }
                        }
                    } else {
                        if (data.code == 200) {
                            window.location = "/myschool/paike.do";
                            $('#login-bg2').fadeOut('fast');
                        } else {
                            if (data.message == '用户名非法') {
                                $('.username2').addClass('error');
                            } else {
                                $('.password2').addClass('error');
                            }
                        }
                    }
                },
                complete: function () {
                    if (index == 1) {
                        $('.login-btn').removeClass('active');
                    } else {
                        $('#buttonloadingindex').css('display', 'none');
                        $("#username2").val("");
                        $("#password2").val("");
                    }

                }
            });
        }
        $(document).ready(function () {
            $('.username-error').hide();
            $('.password-error').hide();
            $('.input-account').focus(function () {
                $(this).removeClass('error');
                $('.username-error').hide();

            });
            $('.input-password').focus(function () {
                $(this).removeClass('error');
                $('.password-error').hide();

            });

            $(".login-btn").on("click", function () {
                loginIndex(1);
            });
        });
        function closepop() {
            $('#login-bg2').fadeOut('fast');
            $("#username2").val("");
            $("#password2").val("");
        }
    </script>

</head>
<body>
<!-- top -->
<div class="topbox">
    <div class="top">
        <div class="logo">
            <h1>
                <a href="javascript:void(0);">公司的名字或广告语写在这里</a>
            </h1>
        </div>
        <%--<div class="toplink">
            <a><span class="menu">微辅导</span></a>
            <span class="search"></span>
            <span class="land"><a href="/">登陆</a></span>
            <span class="register"><a href="/">注册</a></span>

        </div>--%>


        <div class="toplink" style="">


            <!--================================登录==================================-->
            <!--================================登录==================================-->
            <%--<a target="_blank" class="login-R" href="http://edu.wanfangdata.com.cn/"><img src="/customizedpage/fuyang21/images/heixielu_picture.png"></a>--%>
            <a class="login-btn" href="javascript:;">登 录</a>
            <input id="input-first" class="input-password" type="password" placeholder="密码" tabindex="2">
            <input id="input-last" class="input-account" type="text" placeholder="用户名" tabindex="1" value="">
            <!--==========================密码提示===============================-->
            <div id="tips-msg">
                <span class="password-error" style="">密码错误</span>
                <!--a class="forget-pass" href='#'>忘记密码？</a-->
                <span class="username-error">用户名不存在</span>
            </div>
        </div>
    </div>
</div>
<!-- banner -->
<div class="index-bannerbox">
    <div id="qie">
        <ul id="index-banner">
            <li><img src="/customizedpage/chuzhou/images/jpg-2.jpg"/></li>
            <li><img src="/customizedpage/chuzhou/images/jpg-3.jpg"/></li>
            <li><img src="/customizedpage/chuzhou/images/jpg-4.jpg"/></li>
            <li><img src="/customizedpage/chuzhou/images/jpg-2.jpg"/></li>
            <li><img src="/customizedpage/chuzhou/images/jpg-3.jpg"/></li>
        </ul>
        <a href="javascript:" class="left"></a>
        <a href="javascript:" class="right"></a>
    </div>
</div>
<div class="bg-line"></div>
<!-- news-->
<div class="index-newsbox">
    <div class="newsleft">
        <div class="newsleft-top">
            <div class="newsleft-top-dl">
                <dl>
                    <dt><img src="/customizedpage/chuzhou/images/cz-15.jpg"/></dt>
                    <dd>
                        <h5><a href="http://www.czjy.cn/Site/news/2016/9/23818.html" target="_blank">滁州市2016年中学音乐现场课评选活动圆满结束</a></h5>
                        <p>
                            近日，滁州市2016年中学音乐现场课评选活动在滁州中学、滁州市第六中学分别进行。
                        </p>
                        <a class="btn-more" href="/customizedpage/chuzhou/chuzhou.jsp?type=news"></a>

                    </dd>
                </dl>
                <dl>
                    <dt><img src="/customizedpage/chuzhou/images/cz-16.jpg"/></dt>
                    <dd>
                        <h5><a href="http://www.czjy.cn/Site/news/2016/9/23785.html" target="_blank">全市优秀教师微讲团宣传活动市级遴选工作正式启动</a></h5>
                        <p>9月26日，全市优秀教师微讲团宣传活动市级遴选工作正式启动。</p>
                        <a class="btn-more" href="/customizedpage/chuzhou/chuzhou.jsp?type=news"></a>

                    </dd>
                </dl>
                <dl>
                    <dt><img src="/customizedpage/chuzhou/images/cz-17.jpg"/></dt>
                    <dd>
                        <h5><a href="http://www.czjy.cn/Site/news/2016/9/23679.html" target="_blank">市人大视察调研我市农村义务教育工作</a></h5>
                        <p>9月18日，市人大调研组一行深入我市部分县（市、区）和学校专题调研我市农村义务教育工作开展情况。</p>
                        <a class="btn-more" href="/customizedpage/chuzhou/chuzhou.jsp?type=news"></a>

                    </dd>
                </dl>
                <dl>
                    <dt><img src="/customizedpage/chuzhou/images/cz-18.jpg"/></dt>
                    <dd>
                        <h5><a href="http://www.czjy.cn/Site/news/2016/9/23673.html" target="_blank">市委第三巡察组来我局反馈专项巡察情况</a></h5>
                        <p>
                            　　9月19日上午，市委第三巡察组到市教体局反馈专项巡察情况。
                        </p>
                        <a class="btn-more" href="/customizedpage/chuzhou/chuzhou.jsp?type=news"></a>

                    </dd>
                </dl>
                <dl>
                    <dt><img src="/customizedpage/chuzhou/images/cz-19.jpg"/></dt>
                    <dd>
                        <h5><a href="http://www.czjy.cn/Site/news/2016/9/23614.html" target="_blank">2016年全国女子手球锦标赛在滁胜利落幕</a>
                        </h5>

                        <p>
                            9月13日晚，2016年全国女子手球锦标赛在滁胜利落幕。
                        </p>
                        <a class="btn-more" href="/customizedpage/chuzhou/chuzhou.jsp?type=news"></a>

                    </dd>
                </dl>
                <dl>
                    <dt><img src="/customizedpage/chuzhou/images/cz-20.jpg"/></dt>
                    <dd>
                        <h5><a href="http://www.czjy.cn/Site/news/2016/9/23613.html" target="_blank">高维岭来我市调研体育工作</a>
                        </h5>

                        <p>
                            9月13日，高维岭深入我市部分学校调研体育工作。
                        </p>
                        <a class="btn-more" href="/customizedpage/chuzhou/chuzhou.jsp?type=news"></a>

                    </dd>
                </dl>
                <dl>
                    <dt><img src="/customizedpage/chuzhou/images/cz-21.jpg"/></dt>
                    <dd>
                        <h5><a href="http://www.czjy.cn/Site/news/2016/9/23584.html" target="_blank">市委教体纪工委严把中秋国庆“廉洁关”</a>
                        </h5>

                        <p>
                            　9月12日下午，市委教体纪工委召开市直教体系统党风廉政工作会议。
                        </p>
                        <a class="btn-more" href="/customizedpage/chuzhou/chuzhou.jsp?type=news"></a>

                    </dd>
                </dl>
              <%--  <dl>
                    <dt><img src="/customizedpage/chuzhou/images/4.jpg"/></dt>
                    <dd>
                        <h5><a href="http://www.czjy.cn/include/web_content.php?id=46810" target="_blank">市教体局召开全市校园安全暨信访工作会议</a>
                        </h5>

                        <p>
                            为切实做好全市校园安全暨信访工作，8月27日，市教体局召开全市校园安全暨信访工作会议。市委教体工委委员、市教体局副局长丁瑞，市公安局治安支队支队长吴久余，市教体局监察室及安全办主要负责同志，各县（市、区）教体局、市直各校安全或信访工作分管领导及科室负责人参加会议。
                            会议传达了市信访工作会议精神，通报了全市校园安全暨信访工作开展情况，并对近期开展的学校军训、“打非治违”、校车管理、消防安全、烟花爆竹限燃...
                        </p>
                        <a class="btn-more" href="/customizedpage/chuzhou/chuzhou.jsp?type=news"></a>

                    </dd>
                </dl>
                <dl>
                    <dt><img src="/customizedpage/chuzhou/images/5.jpg"/></dt>
                    <dd>
                        <h5><a href="http://www.czjy.cn/include/web_content.php?id=46602" target="_blank">市人大、市政府、市政协联合督查我局议案建议提案办理情况</a>
                        </h5>

                        <p>
                            为有效推进市人大议案和代表委员建议、提案办理工作，8月25日，市人大、市政府、市政协联合督查组一行对我局2015年办理议案、建议和提案进展情况进行督查。
                            督查组一行在市委教育体育工委书记、市教育体育局局长马占文，市委教育体育工委委员、市教育体育局副局长丁瑞等人的陪同下，先后深入滁州市第二实验小学、滁州市第十一中学和滁州市机电工程学校，实地查看相关议案、建议和提案办理情况，并在督查过程中对我市教育发展提出了很多富有针对性的意见和建议。
                        </p>
                        <a class="btn-more" href="/customizedpage/chuzhou/chuzhou.jsp?type=news"></a>

                    </dd>
                </dl>
                <dl>
                    <dt><img src="/customizedpage/chuzhou/images/6.jpg"/></dt>
                    <dd>
                        <h5><a href="http://www.czjy.cn/include/web_content.php?id=46572" target="_blank">我市在2015年全省国家学生体质健康标准测试工作业务培训会上做经验介绍</a>
                        </h5>

                        <p>
                            8月25日，安徽省教育厅举办2015年国家学生体质健康标准测试工作业务培训班。全省各市、省直管县教育局负责学校体育工作的局领导、科室负责人、信息技术人员、学校体育工作专家团队核心专家参加了会议。
                            会议总结了安徽省2014年国家学生体质健康标准测试和实施学校体育“三个办法”工作开展情况，部署了2015年国家学生体质健康标准测试和落实学校体育“三个办法”相关工作。
                        </p>
                        <a class="btn-more" href="/customizedpage/chuzhou/chuzhou.jsp?type=news"></a>

                    </dd>
                </dl>--%>
            </div>
            <div class="btnnum">
                <span class="btn1"></span>
                <span class="btn2"></span>
                <span class="btn3"></span>
                <span class="btn4"></span>
                <span class="btn5"></span>
                <span class="btn6"></span>
                <span class="btn7"></span>
            </div>
        </div>


        <div class="vieww">
            <div class="wsp">
                <ul>
                    <li class="singular "><img src="/customizedpage/chuzhou/images/chuzhou-1xiao.jpg" alt=""/>

                        <p>滁州第一小学</p></li>
                    <li><img src="/customizedpage/chuzhou/images/chuzhou-2xiao.png" alt=""/>

                        <p>滁州第二小学</p></li>
                    <li class="singular "><img src="/customizedpage/chuzhou/images/chuzhou-2zhong.jpg" alt=""/>

                        <p>滁州第二中学</p></li>
                    <li><img src="/customizedpage/chuzhou/images/chuzhou-3xiao.jpg" alt=""/>

                        <p>滁州第三小学</p></li>
                    <li class="singular "><img src="/customizedpage/chuzhou/images/chuzhou-3zhong.jpg" alt=""/>

                        <p>凤阳三中</p></li>

                </ul>
                <ul>
                    <li class="singular "><img src="/customizedpage/chuzhou/images/chuzhou-6zhong.jpg" alt=""/>

                        <p>滁州第六中学</p></li>
                    <li><img src="/customizedpage/chuzhou/images/chuzhou-12zhong.png" alt=""/>

                        <p>滁州十二中学</p></li>
                    <li class="singular "><img src="/customizedpage/chuzhou/images/chuzhou-huxin.jpg" alt=""/>

                        <p>湖心路小学</p></li>
                    <li><img src="/customizedpage/chuzhou/images/chuzhou-jiefang.png" alt=""/>

                        <p>滁州解放小学</p></li>
                    <li class="singular "><img src="/customizedpage/chuzhou/images/chuzhou-langya.jpg" alt=""/>

                        <p>琅琊路小学</p></li>

                </ul>
                <ul>
                    <li class="singular "><img src="/customizedpage/chuzhou/images/chuzhou-shiyan.jpg" alt=""/>

                        <p>滁州实验小学</p></li>
                    <li><img src="/customizedpage/chuzhou/images/img-shcool-logo.jpg" alt=""/>

                        <p>滁州实验中学</p></li>
                    <li class="singular "><img src="/customizedpage/chuzhou/images/chuzhou-zhongxue.jpg" alt=""/>

                        <p>滁州中学</p></li>
                    <li><img src="/customizedpage/chuzhou/images/dingyuan-3zhong.png" alt=""/>

                        <p>定远三中</p></li>
                    <li class="singular "><img src="/customizedpage/chuzhou/images/dingyuan-1zhong.png" alt=""/>

                        <p>定远一中</p></li>

                </ul>
                <ul>
                    <li class="singular "><img src="/customizedpage/chuzhou/images/dingyuan-shiyan.jpg" alt=""/>

                        <p>定远实验学校</p></li>
                    <li><img src="/customizedpage/chuzhou/images/dingyuan-zhangqiao.png" alt=""/>

                        <p>张桥中心学校</p></li>
                    <li class="singular "><img src="/customizedpage/chuzhou/images/fangyang-damiao.png" alt=""/>

                        <p>大庙中心小学</p></li>
                    <li><img src="/customizedpage/chuzhou/images/fengyang-yongan.jpg" alt=""/>

                        <p>凤阳永安小学</p></li>
                    <li class="singular "><img src="/customizedpage/chuzhou/images/fengyang-xiaogang.png" alt=""/>

                        <p>小岗村学校</p></li>

                </ul>

                <ul>
                    <li class="singular "><img src="/customizedpage/chuzhou/images/laian-3zhong.png" alt=""/>

                        <p>来安县第三中学</p></li>
                    <li><img src="/customizedpage/chuzhou/images/laian-shiyan.png" alt=""/>

                        <p>来安县实验小学</p></li>
                    <li class="singular "><img src="/customizedpage/chuzhou/images/guangming-zidi.jpg" alt=""/>

                        <p>光明市工人子弟小学</p></li>
                    <li><img src="/customizedpage/chuzhou/images/guangmin-minghu.png" alt=""/>

                        <p>明光市明湖学校</p></li>
                    <li class="singular "><img src="/customizedpage/chuzhou/images/mingguang-yifu.png" alt=""/>

                        <p>明光市逸夫学校</p></li>

                </ul>

                <ul>
                    <li class="singular "><img src="/customizedpage/chuzhou/images/quanjiao-shiyan.png" alt=""/>

                        <p>全椒县实验小学</p></li>
                    <li><img src="/customizedpage/chuzhou/images/quanjiao-xianghe.jpg" alt=""/>

                        <p>全椒县襄河中学</p></li>
                    <li><img src="/customizedpage/chuzhou/images/chuzhou-5zhong.jpg" alt=""/>

                        <p>滁州第五中学</p></li>
                    <li><img src="/customizedpage/chuzhou/images/chuzhou-longfan.jpg" alt=""/>

                        <p>滁州龙蟠小学</p></li>
                    <li><img src="/customizedpage/chuzhou/images/dingyuan-dongxing.png" alt=""/>

                        <p>定远东兴学校</p></li>
                </ul>
                <ul>
                    <li><img src="/customizedpage/chuzhou/images/laian-xuexiao.png" alt=""/>

                        <p>来安双唐九年一贯制学校</p></li>
                    <li><img src="/customizedpage/chuzhou/images/quanjiao-xiaoxue.jpg" alt=""/>

                        <p>全椒市江海小学</p></li>
                </ul>
            </div>
        </div>


        <%--
		<div class="newsleft-bottom">
			
            <a style="cursor: pointer" onclick="$('#login-bg').fadeIn('fast')"><img src="/customizedpage/chuzhou/images/a001_r1_c1.gif" width="337" height="99" /></a>
            <a href="/"><img src="/customizedpage/chuzhou/images/a001_r1_c3.gif" width="337" height="99" /></a>
        </div>--%>
    </div>

    <div class="newsright">
        <div class="view">
            <dl class="hexielunews">
                <dt><span>【公告】</span> <a class="bg-more" href="/customizedpage/chuzhou/chuzhou.jsp?type=notice"></a>
                </dt>
                <dd>
                    <a href="http://www.czjy.cn/Site/news/2016/9/23820.html" target="_blank">
                        <span>我市开展义务教育均衡发展示范校观摩活动</span><em>2016-09-29 </em></a>
                </dd>
                <dd>
                    <a href="http://www.czjy.cn/Site/news/2016/9/23642.html" target="_blank">
                        <span>教育部传达习近平教师节重要讲话精神 强调抓好转化</span><em>2016-09-18 </em></a>
                </dd>
                <dd>
                    <a href="http://www.czjy.cn/Site/news/2016/9/23641.html" target="_blank">
                        <span>2021年安徽省高考不再文理分科</span><em>2016-09-18 </em></a>
                </dd>
                <dd>
                    <a href="http://www.czjy.cn/Site/news/2016/9/23578.html" target="_blank">
                        <span>市委“两学一做”学习教育督查组来我局督导检查</span><em>2016-09-13</em></a>
                </dd>

                <dd>
                    <a href="http://www.czjy.cn/Site/news/2016/9/23577.html" target="_blank">
                        <span>国家级手球专家滁城传授技艺</span><em>2016-09-13</em></a>
                </dd>

                <dd>
                    <a href="http://www.czjy.cn/Site/news/2016/9/23558.html" target="_blank">
                        <span>我市教育系统开展系列主题活动 号召全市教师做党和人民满意的“四有”好教师</span><em>2016-09-12</em></a>
                </dd>
                <dd>
                    <a href="http://www.czjy.cn/Site/news/2016/9/23547.html" target="_blank">
                        <span>许同亚看望慰问市教体局机关退休老教师</span><em>2016-09-09</em></a>
                </dd>
                <dd>
                    <a href="http://www.czjy.cn/Site/news/2016/9/23531.html" target="_blank">
                        <span>教师节前夕市领导看望慰问教育工作者</span><em>2016-09-09</em></a>
                </dd>
                <dd>
                    <a href="http://www.czjy.cn/Site/news/2016/9/23524.html" target="_blank">
                        <span>市教体局召开教育精准扶贫工作推进会议</span><em>2016-09-08</em></a>
                </dd>
                <dd>
                    <a href="http://www.czjy.cn/Site/news/2016/9/23503.html" target="_blank">
                        <span>省教育厅来我市检查秋季开学工作</span><em>2016-09-07</em></a>
                </dd>
                <dd>
                    <a href="http://www.czjy.cn/Site/news/2016/9/23416.html" target="_blank">
                        <span>2016年下半年中小学教师资格考试9月6日起报名</span><em>2016-09-02</em></a>
                </dd>


             <%--   <dd>
                    <a href="http://www.czjy.cn/include/web_content.php?id=49013http://www.czjy.cn/include/web_content.php?id=49013" target="_blank">
                        <span>来安县双塘九年一贯制学校创新校园文化建设</span><em>2016-01-14 </em></a>
                </dd>
                <dd>
                    <a href="http://www.czjy.cn/Site/news/2016/1/124.html" target="_blank">
                        <span>安徽在第四届全国教育改革创新典型案例推选活动中获得8个奖项 </span><em>2016-01-07 </em></a>
                </dd>
                <dd>
                    <a href="http://www.czjy.cn/Site/news/2015/12/15778.html" target="_blank">
                        <span>滁州市2015年度中学一级教师、小学高级教师评审通过人员名单公示</span><em>2015-12-21 </em></a>
                </dd>




                <dd>
                    <a href="http://www.czjy.cn/include/web_content.php?id=46542" target="_blank"><span>滁州市教育体育局
						滁州市人力资源和社会保障局关于做好2015年全市中小学和中专教师专业技术资格评审工作的通知</span><em>2015-08-21 </em></a>
                </dd>
                <dd>
                    <a href="http://www.czjy.cn/include/web_content.php?id=46493" target="_blank"><span>南京市琅琊路小学苏滁分校公开招聘教师调档公告</span><em>2015-08-17 </em></a>
                </dd>
                <dd>
                    <a href="http://www.czjy.cn/include/web_content.php?id=46486" target="_blank"><span>滁州市教学研究室公开选调教研员拟选调人员公示</span><em>2015-08-13 </em></a>
                </dd>--%>
            <%--    <dd>
                    <a href="http://www.czjy.cn/include/web_content.php?id=46433" target="_blank"><span>滁州广播电视大学2015年秋季招生简章</span><em>2015-08-07</em></a>
                </dd>
                <dd>
                    <a href="http://www.czjy.cn/include/web_content.php?id=46432" target="_blank"><span>南京市琅琊路小学苏滁分校公开招聘教师参加面试人员名单</span><em>2015-08-07</em></a>
                </dd>--%>
            </dl>
        </div>
    </div>
    <%--<div class="newsright">
        <div class="view"><div class="wsp"><ul>
            <li  class="singular "><img src="/customizedpage/chuzhou/images/img-shcool-logo.jpg" alt="" /><p>黄陂前川五小</p></li>
            <li ><img src="/customizedpage/chuzhou/images/img-shcool-logo.jpg" alt="" /><p>黄陂前川五小</p></li>
            <li  class="singular "><img src="/customizedpage/chuzhou/images/img-shcool-logo.jpg" alt="" /><p>黄陂前川五小</p></li>
            <li ><img src="/customizedpage/chuzhou/images/img-shcool-logo.jpg" alt="" /><p>黄陂前川五小</p></li>
            <li  class="singular "><img src="/customizedpage/chuzhou/images/img-shcool-logo.jpg" alt="" /><p>黄陂前川五小</p></li>
            <li ><img src="/customizedpage/chuzhou/images/img-shcool-logo.jpg" alt="" /><p>黄陂前川五小</p></li>
        </ul>
        <ul>
            <li  class="singular "><img src="/customizedpage/chuzhou/images/img-shcool-logo.jpg" alt="" /><p>黄陂前川五小</p></li>
            <li ><img src="/customizedpage/chuzhou/images/img-shcool-logo.jpg" alt="" /><p>黄陂前川五小</p></li>
            <li  class="singular "><img src="/customizedpage/chuzhou/images/img-shcool-logo.jpg" alt="" /><p>黄陂前川五小</p></li>
            <li ><img src="/customizedpage/chuzhou/images/img-shcool-logo.jpg" alt="" /><p>黄陂前川五小</p></li>
            <li  class="singular "><img src="/customizedpage/chuzhou/images/img-shcool-logo.jpg" alt="" /><p>黄陂前川五小</p></li>
            <li ><img src="/customizedpage/chuzhou/images/img-shcool-logo.jpg" alt="" /><p>黄陂前川五小</p></li>
        </ul>
        <ul>
            <li  class="singular "><img src="/customizedpage/chuzhou/images/img-shcool-logo.jpg" alt="" /><p>黄陂前川五小</p></li>
            <li ><img src="/customizedpage/chuzhou/images/img-shcool-logo.jpg" alt="" /><p>黄陂前川五小</p></li>
            <li  class="singular "><img src="/customizedpage/chuzhou/images/img-shcool-logo.jpg" alt="" /><p>黄陂前川五小</p></li>
            <li ><img src="/customizedpage/chuzhou/images/img-shcool-logo.jpg" alt="" /><p>黄陂前川五小</p></li>
            <li  class="singular "><img src="/customizedpage/chuzhou/images/img-shcool-logo.jpg" alt="" /><p>黄陂前川五小</p></li>
            <li ><img src="/customizedpage/chuzhou/images/img-shcool-logo.jpg" alt="" /><p>黄陂前川五小</p></li>
        </ul>
        <ul>
            <li  class="singular "><img src="/customizedpage/chuzhou/images/img-shcool-logo.jpg" alt="" /><p>黄陂前川五小</p></li>
            <li ><img src="/customizedpage/chuzhou/images/img-shcool-logo.jpg" alt="" /><p>黄陂前川五小</p></li>
            <li  class="singular "><img src="/customizedpage/chuzhou/images/img-shcool-logo.jpg" alt="" /><p>黄陂前川五小</p></li>
            <li ><img src="/customizedpage/chuzhou/images/img-shcool-logo.jpg" alt="" /><p>黄陂前川五小</p></li>
            <li  class="singular "><img src="/customizedpage/chuzhou/images/img-shcool-logo.jpg" alt="" /><p>黄陂前川五小</p></li>
            <li ><img src="/customizedpage/chuzhou/images/img-shcool-logo.jpg" alt="" /><p>黄陂前川五小</p></li>
        </ul></div></div>

    </div>--%>
</div>

<div id="login-bg">
    <div id="login">
        <div id="logincontent">
            <form action="http://www.lqtedu.com/outsite/login.jsp">
                <div class="line" style="line-height:30px; ">
                    <div><img src="/img/loginicon.png"/></div>
                    <div style="color:rgb(51, 173, 225); font-weight:bold; font-size:14px; margin-left: 10px">登录</div>
                    <i class='fa fa-times fa-lg'
                       style="cursor:pointer; float: right; color:rgb(51, 173, 225); line-height: 30px"
                       onclick="$('#login-bg').fadeOut('fast')"></i>
                </div>
                <div class="line">
                    <input type="text" placeholder="登录名/身份证号/学籍号" class="username" name="UNAME"/>
                </div>
                <div class="line">
                    <input type="password" placeholder="密码" class="password" name="UPWD"/>
                </div>
                <div class="line" style="line-height:30px;">
                    <input type="submit" value=" 登 录 ">
                </div>
            </form>
        </div>
    </div>
</div>

<!-- footer -->
<div class="bg-line"></div>
<div class="footerbox">
    <div class="footer">
        <div class="host">
            <span>主办：滁州教育局 </span><span>技术支持：上海复兰信息科技有限公司</span><span>客服热线：400 820 6735</span>
        </div>
        <div class="footerlink">
            <a href="javascript:void(0);">后台管理</a>|
            <a href="javascript:void(0);">站长中心</a>|
            <a href="javascript:void(0);">帮助中心</a>
        </div>
    </div>
</div>


</body>
</html>