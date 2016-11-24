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
    <link rel="stylesheet" type="text/css" href="/customizedpage/chuzhou/style/new.css"/>
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

            $('.input-password').keydown(function (event) {
                if (event.which == 13) {
                    loginIndex();
                }
            });
        });


        function loginIndex() {
            $('.login-btn').addClass('active');
            $.ajax({
                url: "/user/login.do",
                type: "post",
                dataType: "json",
                data: {
                    'name': $(".input-account").val(),
                    'pwd': $(".input-password").val()
                },
                success: function (data) {
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
                },
                complete: function () {

                    $('.login-btn').removeClass('active');

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
                loginIndex();
            });
        });
        $(function () {
            var column = getQueryString("type");//notice或news
            if (column == "notice") {
                $("#fuyang21_type").text("公告首页");
            }
            else if (column == "news") {
                $("#fuyang21_type").text("新闻首页");
            }
            getListByType(column);
        });

        function getQueryString(name) {
            var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
            var r = window.location.search.substr(1).match(reg);
            if (r != null) return unescape(r[2]);
            return null;
        }
        function getListByType(type) {
            var typeName = "";
            if (type == "notice") {
                $(".middle_left li a").eq(0).addClass("cur");
                $(".middle_left li a").eq(1).removeClass("cur");
                $(".middle_right_II").eq(0).addClass("middle_right_I");
                $(".middle_right_II").eq(1).removeClass("middle_right_I");
            }
            else if (type == "news") {
                $(".middle_left li a").eq(0).removeClass("cur");

                $(".middle_left li a").eq(1).addClass("cur");
                $(".middle_right_II").eq(0).removeClass("middle_right_I");
                $(".middle_right_II").eq(1).addClass("middle_right_I");
            }
        }
    </script>
</head>
<body>
<!-- top -->
<div class="topbox">
    <div class="top">
        <div class="logo">
            <h1>
                <a>公司的名字或广告语写在这里</a>
            </h1>
        </div>
        <div class="toplink" style="">
            <!--================================登录==================================-->
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
<!-- info -->
<input type="hidden" id="hexieluSchoolId" value="559cefe863e73dbde154a709">

<div class="middle_info">
    <dl>
        <dt><a href="/customizedpage/chuzhou/flippedClassroom.jsp">首页</a>&nbsp;>&nbsp;<span
                id="fuyang21_type">新闻首页</span></dt>
        <dd>
            <table>
                <tr>
                    <!--==============================左边部分==============================-->
                    <td>
                        <div class="middle_left">
                            <ul>
                                <li><a href="/customizedpage/chuzhou/chuzhou.jsp?type=notice" class="cur">公告</a></li>
                                <li><a href="/customizedpage/chuzhou/chuzhou.jsp?type=news">新闻</a></li>
                            </ul>
                        </div>
                    </td>
                    <!--==============================公告部分==============================-->
                    <td>
                        <div class="middle_right_I middle_right_II">
                            <div>
                                <dl class="recomand_notice">
                                    <dt>
                                        <em>推荐公告</em>
                                    </dt>
                                    <dd>
                                        <a href="http://www.czjy.cn/include/web_content.php?id=46542" target="_blank">滁州市教育体育局
                                            滁州市人力资源和社会保障局关于做好2015年全市中小学和中专教师专业技术资格评审工作的通知</a>
                                        <i>2015-08-21 </i>
                                    </dd>
                                    <dd>
                                        <a href="http://www.czjy.cn/include/web_content.php?id=46493" target="_blank">南京市琅琊路小学苏滁分校公开招聘教师调档公告</a>
                                        <i>2015-08-17 </i>
                                    </dd>
                                    <dd>
                                        <a href="http://www.czjy.cn/include/web_content.php?id=46486" target="_blank">滁州市教学研究室公开选调教研员拟选调人员公示</a>
                                        <i>2015-08-13 </i>
                                    </dd>
                                    <dd>
                                        <a href="http://www.czjy.cn/include/web_content.php?id=46433" target="_blank">滁州广播电视大学2015年秋季招生简章</a>
                                        <i>2015-08-07</i>
                                    </dd>
                                    <dd>
                                        <a href="http://www.czjy.cn/include/web_content.php?id=46432" target="_blank">南京市琅琊路小学苏滁分校公开招聘教师参加面试人员名单</a>
                                        <i>2015-08-07</i>
                                    </dd>
                                </dl>
                                <div class="middle_view"></div>
                                <dl class="recent_notice">
                                    <dt>
                                        <em>最新公告</em>
                                    </dt>
                                    <dd>
                                        <a href="http://www.czjy.cn/include/web_content.php?id=49044" target="_blank">2015年高中英语优秀课例展评暨词汇教学微课评选结果的通知</a>
                                        <i>2016-01-18</i>
                                    </dd>
                                    <dd>
                                        <a href="http://www.czjy.cn/include/web_content.php?id=49029" target="_blank">教育部：新一轮高考改革 高中将迎来三大变化</a>
                                        <i>2016-01-15</i>
                                    </dd>
                                    <dd>
                                        <a href="http://www.czjy.cn/include/web_content.php?id=49021" target="_blank">来安县实验小学积极配合促均衡教育发展顺利通过验收</a>
                                        <i>2016-01-14</i>
                                    </dd>
                                    <dd>
                                        <a href="http://www.czjy.cn/include/web_content.php?id=49019" target="_blank">滁州市第二实验小学开展班主任撰写操行评语专项培训</a>
                                        <i>2016-01-14</i>
                                    </dd>
                                    <dd>
                                        <a href="http://www.czjy.cn/include/web_content.php?id=49013http://www.czjy.cn/include/web_content.php?id=49013" target="_blank">来安县双塘九年一贯制学校创新校园文化建设</a>
                                        <i>2016-01-14</i>
                                    </dd>
                                    <dd>
                                        <a href="www.czjy.cn/include/web_content.php?id=48906" target="_blank">安徽在第四届全国教育改革创新典型案例推选活动中获得8个奖项</a>
                                        <i>2016-01-07</i>
                                    </dd>

                                    <dd>
                                        <a href="http://www.czjy.cn/include/web_content.php?id=48725" target="_blank">滁州市2015年度中学一级教师、小学高级教师评审通过人员名单公示</a>
                                        <i>2015-12-21</i>
                                    </dd>
                                    <dd>
                                        <a href="http://www.czjy.cn/include/web_content.php?id=46472" target="_blank">南京市琅琊路小学苏滁分校公开招聘教师参加体检人员名单</a>
                                        <i>2015-08-12</i>
                                    </dd>
                                    <dd>
                                        <a href="http://www.czjy.cn/include/web_content.php?id=46450" target="_blank">滁州市教育体育局关于授予庄晓民等人2015年度二级社会体育指导员称号的决定</a>
                                        <i>2015-08-10</i>
                                    </dd>
                                    <dd>
                                        <a href="http://www.czjy.cn/include/web_content.php?id=46436" target="_blank">南京市琅琊路小学苏滁分校2015年教师招聘面试方案</a>
                                        <i>2015-08-07</i>
                                    </dd>

                                    <dd>
                                        <a href="http://www.czjy.cn/include/web_content.php?id=46422" target="_blank">南京市琅琊路小学苏滁分校招聘教师笔试成绩公告</a>
                                        <i>2015-08-06</i>
                                    </dd>
                                    <dd>
                                        <a href="http://www.czjy.cn/include/web_content.php?id=46360" target="_blank">南湖四期游泳池升级改造公告</a>
                                        <i>2015-07-28</i>
                                    </dd>
                                    <dd>
                                        <a href="http://www.czjy.cn/include/web_content.php?id=46357" target="_blank">南京市琅琊路小学苏滁分校公开招聘教师补充公告</a>
                                        <i>2015-07-27</i>
                                    </dd>
                                    <dd>
                                        <a href="http://www.czjy.cn/include/web_content.php?id=46347" target="_blank">滁州市首届中小学心理健康教育特色校评选结果的公示</a>
                                        <i>2015-07-27</i>
                                    </dd>
                                    <dd>
                                        <a href="http://www.czjy.cn/include/web_content.php?id=46319" target="_blank">2015年南京市琅琊路小学苏滁分校公开招聘教师公告</a>
                                        <i>2015-07-27</i>
                                    </dd>
                                    <dd>
                                        <a href="http://www.czjy.cn/include/web_content.php?id=46286" target="_blank">滁州市教学研究室公开选调教研员进入体检人员名单</a>
                                        <i>2015-07-21</i>
                                    </dd>

                                </dl>


                            </div>
                        </div>
                    </td>
                    <td>
                        <div class="middle_right_II">
                            <div>
                                <dl class="recomand_notice">
                                    <dt>
                                        <em>推荐新闻</em>
                                    </dt>
                                    <dd>
                                        <a href="http://www.czjy.cn/include/web_content.php?id=46904" target="_blank">章义看望慰问市教体局离休老干部</a>
                                        <i>2015-09-06 </i>
                                    </dd>
                                    <dd>
                                        <a href="http://www.czjy.cn/include/web_content.php?id=46879" target="_blank">我市教师在全国中学物理教学改革创新大赛中再创佳绩</a>
                                        <i>2015-09-02 </i>
                                    </dd>
                                    <dd>
                                        <a href="http://www.czjy.cn/include/web_content.php?id=46822" target="_blank">滁州市中小学教育装备管理应用年活动启动仪式暨动员大会顺利召开</a>
                                        <i>2015-08-31 </i>
                                    </dd>
                                    <dd>
                                        <a href="http://www.czjy.cn/include/web_content.php?id=46810" target="_blank">市教体局召开全市校园安全暨信访工作会议</a>
                                        <i>2015-08-31 </i>
                                    </dd>
                                    <dd>
                                        <a href="http://www.czjy.cn/include/web_content.php?id=46602" target="_blank">市人大、市政府、市政协联合督查我局议案建议提案办理情况</a>
                                        <i>2015-08-27 </i>
                                    </dd>
                                    <dd>
                                        <a href="http://www.czjy.cn/include/web_content.php?id=46572" target="_blank">我市在2015年全省国家学生体质健康标准测试工作业务培训会上做经验介绍</a>
                                        <i>2015-08-26 </i>
                                    </dd>
                                </dl>
                                <div class="middle_view"></div>
                                <dl class="recent_notice">
                                    <dt>
                                        <em>最新新闻</em>
                                    </dt>

                                    <dd>
                                        <a href="http://www.czjy.cn/include/web_content.php?id=49017" target="_blank">滁州市第二实验小学开展“送温暖献爱心”捐款活动</a>
                                        <i>2016-01-14</i>
                                    </dd>
                                    <dd>
                                        <a href="http://www.czjy.cn/include/web_content.php?id=48994" target="_blank">湖心路小学举行“中华魂”主题演讲比赛</a>
                                        <i>2016-01-13</i>
                                    </dd>
                                    <dd>
                                        <a href="http://www.czjy.cn/include/web_content.php?id=48987" target="_blank">琅琊路小学作品在琅琊区第三届青少年科技创新大赛中喜获佳绩</a>
                                        <i>2016-01-12</i>
                                    </dd>
                                    <dd>
                                        <a href="http://www.czjy.cn/include/web_content.php?id=48960" target="_blank">琅琊路小学荣获青少年“奋发向上崇德向善”爱国主义读书教育活动“先进学校”荣誉称号</a>
                                        <i>2016-01-12</i>
                                    </dd>
                                    <dd>
                                        <a href="http://www.czjy.cn/include/web_content.php?id=46918" target="_blank">定远县初中物理学科教师实验教学基本功竞赛成功举办</a>
                                        <i>2015-09-06</i>
                                    </dd>
                                    <dd>
                                        <a href="http://www.czjy.cn/include/web_content.php?id=46916" target="_blank">滁州四中举行2015级新生军训总结汇报表演</a>
                                        <i>2015-09-06</i>
                                    </dd>
                                    <dd>
                                        <a href="http://www.czjy.cn/include/web_content.php?id=46915" target="_blank">逸夫小学全体师生观看纪念抗战胜利70周年阅兵式</a>
                                        <i>2015-09-06</i>
                                    </dd>
                                    <dd>
                                        <a href="http://www.czjy.cn/include/web_content.php?id=46912" target="_blank">孙新安到实验幼儿园分园视察开学工作</a>
                                        <i>2015-09-06</i>
                                    </dd>
                                    <dd>
                                        <a href="http://www.czjy.cn/include/web_content.php?id=46909" target="_blank">会峰小学掀起阅读《大抗战知识读本》热浪重温抗战史</a>
                                        <i>2015-09-06</i>
                                    </dd>
                                    <dd>
                                        <a href="http://www.czjy.cn/include/web_content.php?id=46897" target="_blank">滁州市第二实验小学正式投入使用</a>
                                        <i>2015-09-02</i>
                                    </dd>
                                    <dd>
                                        <a href="http://www.czjy.cn/include/web_content.php?id=46900" target="_blank">凤阳中学举行“纪念抗日战争胜利70周年暨2015级新生军训闭营仪式”</a>
                                        <i>2015-09-02</i>
                                    </dd>
                                    <dd>
                                        <a href="http://www.czjy.cn/include/web_content.php?id=46892" target="_blank">黄泥岗中心小学开学不忘安全教育</a>
                                        <i>2015-09-02</i>
                                    </dd>
                                    <dd>
                                        <a href="http://www.czjy.cn/include/web_content.php?id=46883" target="_blank">逸夫小学开展安全教育主题班会活动</a>
                                        <i>2015-09-02</i>
                                    </dd>
                                    <dd>
                                        <a href="http://www.czjy.cn/include/web_content.php?id=46896" target="_blank">来安县教体局推进教师公租房建设</a>
                                        <i>2015-09-02</i>
                                    </dd>
                                    <dd>
                                        <a href="http://www.czjy.cn/include/web_content.php?id=46882" target="_blank">滁州市第三批“三区”人才支持计划教师到定远支教</a>
                                        <i>2015-09-02</i>
                                    </dd>
                                    <dd>
                                        <a href="http://www.czjy.cn/include/web_content.php?id=46881" target="_blank">定远对全县学籍管理员进行业务培训并表彰先进集体和个人</a>
                                        <i>2015-09-02</i>
                                    </dd>
                                    <dd>
                                        <a href="http://www.czjy.cn/include/web_content.php?id=46880" target="_blank">定远县教体局联合交警部门开展校车驾驶员安全再培训活动</a>
                                        <i>2015-09-02</i>
                                    </dd>


                                </dl>
                                <!--分页-->
                                <div class="notice-page-paginator">
                                    <span class="first-page">首页</span>
                                    <span class="page-index">
                                        <span class="active">1</span>
                                        <span>2</span>
                                        <span>3</span>
                                        <span>4</span>
                                        <span>5</span>
                                        <i>···</i>
                                    </span>
                                    <span class="last-page">尾页</span>

                                </div>

                            </div>
                        </div>
                    </td>
                </tr>
            </table>
        </dd>
    </dl>
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