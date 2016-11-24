<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%--<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>--%>
<%@ taglib prefix="roles" uri="http://fulaan.userRole.com" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>用户手册</title>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <link rel="stylesheet" href="/static/css/bootstrap-combined.school.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/style.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/dialog.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/message.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/font-awesome.min.css"/>
    <script type="text/javascript" src="/static/js/jquery.min.js"></script>
    <script src="/static/js/bootstrap-paginator.min.js" type="text/javascript"></script>
    <script type="text/javascript" src="/static/js/sharedpart.js"></script>
    <script type="text/javascript" src="/static/js/fzprivatemessage.js"></script>
    <script type="text/javascript">


        $(function () {
            $('.help-container').hide();
            if (${roles:isStudent(sessionValue.userRole)}) {
                $('.student').parent().addClass('selected');
                $('#student-help-container').show();
            } else if (${roles:isTeacher(sessionValue.userRole)}) {
                $('.teacher').parent().addClass('selected');
                $('#teacher-help-container').show();
            } else if (${roles:isHeadmaster(sessionValue.userRole)}) {
                $('.headmaster').parent().addClass('selected');
                $('#headmaster-help-container').show();
            } else if (${roles:isParent(sessionValue.userRole)}) {
                $('.parent').parent().addClass('selected');
                $('#parent-help-container').show();
            }
            else if (${roles:isManager(sessionValue.userRole)}) {
                $('.parent').parent().addClass('selected');
                $('#admin-help-container').show();
            }


            //显示选择的用户手册
            $('.help').bind('click', function () {
                $('.help').parent().removeClass('selected');
                $(this).parent().addClass('selected');
                var showItem = $(this).prop("className").split(' ');
                $('.help-container').hide();
                $('#' + showItem[0] + '-help-container').show();
            });
        });
    </script>

</head>
<body>
<!-- user info start -->
<%@ include file="../common_new/head.jsp" %>
<jsp:include page="../common/infoBanner.jsp">
    <jsp:param name="bannerType" value="userCenter"/>
    <jsp:param name="menuIndex" value="2"/>
</jsp:include>
<!-- user info end -->
<%--<div id="intro-player"></div>--%>
<div id="content_main_container">
    <div id="play_I"
         style="width: 745px;height: 425px;position: fixed;top: 50%;left: 50%;margin-left: -365px;margin-top:-207px;z-index: 999;display: none;background-color: rgba(255, 255, 255, 0.5);box-shadow: 0 0 10px #666;">
        <div id='sewise-div' class="video-player-container" style="height: 100%">
            <script type="text/javascript" src="/static/plugins/sewiseplayer/sewise.player.min.js"></script>
        </div>
        <div>
            <a style="display: none;position: absolute;top: 1%;left: 98%;color: #666666;z-index: 999"
               onclick="closeMoviee()" class="close-dialog"><i class="fa fa-times-circle fa-lg"></i></a>
        </div>
    </div>
    <script type="text/javascript">
        SewisePlayer.setup({
            server: "vod",
            type: "m3u8",
            skin: "vodFlowPlayer",
            logo: "none",
            lang: "zh_CN",
            topbardisplay: 'enable',
            videourl: ''
        });
        var isFlash = false;
        function playMovie(url) {
            try {
                SewisePlayer.toPlay(url, "", 0, true);
            } catch (e) {
                playerReady.videoURL = url;
                isFlash = true;
            }
            $("#sewise-div").fadeIn();
            $("#play_I").fadeIn();
            $(".close-dialog").fadeIn();
        }



        function playerReady(name){
            if(isFlash){
                SewisePlayer.toPlay(playerReady.videoURL, "", 0, true);
            }
        }
        function closeMoviee() {
            var $player_container = $(".close-dialog");
            $player_container.fadeOut();
            $("#sewise-div").fadeOut();
            $("#play_I").fadeOut();
           /* $("#sewise-div").hide();
            $("#play_I").hide();*/
            window.location.reload();
        }
    </script>
    <div id="content_main">
        <!-- 左侧导航-->
        <%@ include file="../common_new/col-left.jsp" %>
        <!-- left end -->
        <!-- right start-->
        <div id="right-container">
            <div id="content_main_container" style="margin-top: 5px;">
                <div id="main-content" style="position: relative; overflow:hidden;" class="main-content-msg">
                    <div id="account-right">
                        <div id="account-right-title">
                            <span><a class="headmaster help">校领导手册</a></span>
                            <span><a class="teacher help">老师手册</a></span>
                            <span><a class="parent help">家长手册</a></span>
                            <span><a class="student help">学生手册</a></span>
                            <span><a class="admin help">管理员手册</a></span>
                            <span><a onclick="playMovie('http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/5680e4fa0cf2df401c14b1ea.mp4.m3u8')">
                                使用介绍 </a></span>
                        </div>
                        <div id="account-right-content" class="account-right-content" style="max-height: 800px;">
                            <!-- headmaster start -->
                            <div id="headmaster-help-container" class="help-container">

                                <div style="font-size: 20px;font-weight: bold;overflow: visible;width:100%;height: 40px;line-height: 40px;text-align:center;">
                                    亲爱的校领导，您好！感谢您使用K6KT-智慧校园平台！
                                </div>
                                <div style="font-size: 20px;font-weight: bold;overflow: visible;width:100%;line-height: 40px;text-align:center;">
                                    校领导您好，如果您也带班级，对于老师教学方面的功能介绍请您阅读《老师用户手册》；如果您同时还是管理员身份，对于全校师生、班级等的管理功能请您阅读《管理员用户手册》。
                                </div>
                                <ul>
                                    <li>
										<span class="tip-title">1、如何登陆全向互动K6KT-智慧校园？<!--<a onclick="playMovie('http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/55b844a40cf2a7f1af42dd4d.mp4.m3u8')">
                                            【观看演示视频】 </a>-->
                                        </span>

                                        <div>
                                            <pre>用户登录<a href="http://www.k6kt.com" target="_blank">www.k6kt.com</a>，在页面右上方填入学校分配的用户名与密码，即可登录全向互动K6KT-智慧校园。</pre>
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/I_dl.jpg" alt="" class="I_I" width="750">
                                        </div>
                                    </li>
                                    <li>
                                        <span class="tip-title">2、如何更改完善个人头像？<!--<a onclick="playMovie('http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/55b846770cf2a7f1af42dd50.mp4.m3u8')">
                                            【观看演示视频】 </a>-->
                                        </span>

                                        <div>
                                            <pre>登录后，点击头像右侧的笔形图标，即可进入个人设置，点击“修改头像”下的“点击上传图片”即可上传已有照片或者拍摄照片来修改头像。</pre>
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/I_tx.jpg" alt="" class="I_I">
                                        </div>
                                    </li>
                                    <li>
                                        <span class="tip-title">3、如何修改个人密码？<!--<a onclick="playMovie('http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/55b846770cf2a7f1af42dd50.mp4.m3u8')">
                                            【观看演示视频】 </a>-->
                                        </span>

                                        <div>
                                            <pre>登录后，点击头像右侧的笔形图标，进入个人设置，点击“修改密码”即可操作。</pre>
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/I_xgmm.jpg" alt="" class="I_I">
                                        </div>
                                    </li>
                                    <li>
                                        <span class="tip-title">4、如何发送私信？<!--<a onclick="playMovie('http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/55b846770cf2a7f1af42dd50.mp4.m3u8')">
                                            【观看演示视频】 </a>-->
                                        </span>

                                        <div>
                                            <pre>进入个人中心的“我的私信”，点击“发私信”，选择收信人名单，即可与学生、家长、老师、校领导进行私信交流。</pre>
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/I_fsx.jpg" alt="" class="I_I">
                                        </div>
                                    </li>
                                    <li>
                                        <span class="tip-title">5、校领导如何在微校园里发布主题帖?<!--a onclick="playMovie('http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/55b846770cf2a7f1af42dd50.mp4.m3u8')">
                                            【观看演示视频】 </a>-->
                                        </span>

                                        <div>
                                            <pre>在“家校互动”页面点击“微校园”，即可编辑文本和上传图片发帖，勾选“主题帖”后该贴即可置顶一周，如果您带了班，那您还可以选择帖子公开发表的范围：本校、本年级或者本班。</pre>
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/xwzt.png" alt="" class="I_I">
                                        </div>
                                    </li>
                                    <li>
                                        <span class="tip-title">6、校领导如何查看全校各个班级的在线教学情况？<!--<a onclick="playMovie('http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/55b846770cf2a7f1af42dd50.mp4.m3u8')">
                                            【观看演示视频】 </a>-->
                                        </span>

                                        <div>
                                            <pre>进入“办公OA”页面后点击“全校班级”。点击选择某一班级，可查看该班各科任课老师对该班级的课程推送，作业布置，考试统计的详细信息。还可查看本班师生列表，点击具体的一位老师可进入其备课空间查看其备课情况。</pre>
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/I-zxjx.png" alt="" class="I_I">
                                        </div>
                                    </li>
                                    <li>
                                        <span class="tip-title">7、校领导如何给全校的师生发通知？<!--<a onclick="playMovie('http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/55b846770cf2a7f1af42dd50.mp4.m3u8')">
                                            【观看演示视频】 </a>-->
                                        </span>

                                        <div>
                                            <pre>进入“家校互动”页面后点击“学校通知”，可点击“通讯录”勾选“全校”通过文本、语音、附件的形式通知全校师生以及家长。也可以针对某一个或多个班级及不同群体发送通知，编辑好后点击“发送”，老师、学生、家长们即刻就能收到通知。</pre>
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/I-qxtz.png" alt="" class="I_I">
                                        </div>
                                    </li>
                                    <li>
                                        <span class="tip-title">8、如何参加K6KT平台运营活动赢取大奖或现金？<!--<a onclick="playMovie('http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/55b846770cf2a7f1af42dd50.mp4.m3u8')">
                                            【观看演示视频】 </a>-->
                                        </span>

                                        <div>
                                            <pre>进入“火热活动”中“平台运营活动”，即可查看活动列表，点击其中一个活动，查看活动规则，即可参加并赢取奖品或现金。</pre>
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/I-cjdj.png" alt="" class="I_I">
                                        </div>
                                    </li>
                                    <li>
                                        <span class="tip-title">9、校领导如何进行教师管理，建立教师成长档案？<!--<a onclick="playMovie('http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/55b846770cf2a7f1af42dd50.mp4.m3u8')">
                                            【观看演示视频】 </a>-->
                                        </span>

                                        <div>
                                            <pre>A.在“平台管理”页面点击“教师管理”，可新添加老师，可以编辑教师基本信息、修改权限、重置密码、删除教师，可增加教师课程项目，形成教师成长档案。</pre>
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/I-jsgl.png" alt="" class="I_I">
                                        </div>
                                    </li>
                                    <li>
                                        <span class="tip-title">10、如何在平板电脑及手机移动终端，使用K6KT-智慧校园？<!--<a onclick="playMovie('http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/55b846770cf2a7f1af42dd50.mp4.m3u8')">
                                            【观看演示视频】 </a>-->
                                        </span>

                                        <div>
                                            <pre>A.安卓和苹果系统如何下载APP软件 ？<br>通过浏览器打开<a href="http://www.k6kt.com" target="_blank">www.k6kt.com</a>，点击“手机客户端APP”，扫描二维码安装。或者在APP Store里搜索“K6KT”安装。</pre>
                                        </div>A
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/I-appxz.png" alt="" class="I_I">
                                        </div>
                                    </li>
                                    <li>
                                        <span class="tip-title">11、利用移动终端，我们都能做些什么？<!--<a onclick="playMovie('http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/55b846770cf2a7f1af42dd50.mp4.m3u8')">
                                            【观看演示视频】 </a>-->
                                        </span>

                                        <div>
                                            <pre>*可以随时接收和在线提交学校发布的家庭作业<br>*可以随时接收学校发布的各类通知<br>*可以随时和学校老师家长进行私信交流，掌握孩子动态*可以随时拍照，上传到“微校园”进行分享<br>*随时随地，家校互动，在线学习，虚拟社区全分享。</pre>
                                        </div>

                                    </li>
                                    <!--<li>
                                         <span class="tip-title">3、如何修改个人密码？<a
                                                 onclick="playMovie('http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/55b846da0cf2a7f1af42dd53.mp4.m3u8')">

                                             <%--【观看演示视频】--%> </a>
                                         </span>
                                        <span class="tip-tw">
                                            1.如何发布一条校园安全隐患警示？
                                        </span>

                                        <div>
                                            <pre>进入“校园安全关注”，在文本框中输入文字说明，还可上传图片，点击“提交”即可发表，误报可删除。如果隐患已被处理好，点击“处理”即可。</pre>
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/tip-aqgz.png" alt="" class="I_I" width="750">
                                        </div>
                                    </li>
                                    <li>
                                        <span class="tip-title">四、家校互动<a
                                                onclick="playMovie('http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/55b8472f0cf2a7f1af42dd57.mp4.m3u8')">

                                            <%--【观看演示视频】--%> </a>
                                        </span>
                                        <span class="tip-tw">
                                            1.如何使用微校园、微家园社区发帖和评论?
                                        </span>
                                        <div>
                                            <pre>在“家校互动”页面点击“微校园”，即可发表自己的新鲜事或者对别人的新鲜事进行回复、点赞，还可选择帖子发表范围：本校、本年级或者本班，以及查看最新、最热和自己发的帖子。</pre>
                                            <pre>在“家校互动”页面点击“微家园”即可查看学生、家长发的帖子并进行留言和点赞。</pre>
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/tip-jxhd.png" alt="" class="I_I" width="750">
                                        </div>
                                        <span class="tip-tw">
                                            2.如何发布通知？
                                        </span>
                                        <div>
                                            <pre>在“家校互动”页面点击“学校通知”，“发布新通知”，从通讯录里勾选收件人后，即可以文字、语音、附件的形式发布在线通知，勾选“同步到日历”以及“有效时间”还可以实现收到此消息的人在有效时间内在日历中出现事件提醒。</pre>
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/tip-fbtz1.png" alt="" class="I_I" width="750">
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/tip-fbtz2.png" alt="" class="I_I" width="750">
                                        </div>
                                         <span class="tip-tw">
                                           3.如何在好友圈组织活动？
                                        </span>
                                        <div>
                                            <pre>在“家校互动”页面点击“好友圈”，点击“发起活动”，填写活动规则等信息即可发布活动。还可以分类查看“好友圈”里活动，比如：最近动态、好友活动、推进活动、我参加的、我发起的。</pre>
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/tip-zzhd.png" alt="" class="I_I" width="750">
                                        </div>
                                        <span class="tip-tw">
                                          4.如何发起投票选举？查看投票结果？
                                        </span>
                                        <div>
                                            <pre>在“家校互动”页面点击“投票选举”，即可编辑并发布投票选举，包括指定投票公开范围、参选角色、指定候选人、投票角色、投票时间等。同样在“投票选举”中可查看所有投票的结果。</pre>
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/tip-tpxj.png" alt="" class="I_I" width="750">
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/tip-tpjg.png" alt="" class="I_I" width="750">
                                        </div>
                                        <span class="tip-tw">
                                          5.如何发起问卷调查？
                                        </span>
                                        <div>
                                            <pre>在“家校互动”页面点击“调查问卷”，点击右上方的“新建问卷”进入问卷编辑页面，上传问卷文档（word格式）并对问卷进行相关配置后，点击“完成并发布”即可。</pre>
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/tip-scwj.png" alt="" class="I_I" width="750">
                                        </div>
                                    </li>
                                    <li>
                                        <span class="tip-title">五、资源中心<a
                                                onclick="playMovie('http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/55b8706d0cf2a7f1af42dda5.mp4.m3u8')">

                                            <%--【观看演示视频】--%> </a>
                                        </span>
                                        <span class="tip-tw">
                                          1.如何观看云课程并把云课程推送到备课空间？
                                        </span>
                                        <div>
                                            <pre>进入“资源中心”页面的“云课程”，可从中根据条件选择视频课件，点击该视频下方“试看”观看该云课程，点击“推送”将其推送到“备课空间”。推送之后的课程可以在“备课空间”内找到并可进行二次编辑加工。</pre>
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/tip-zyzx.png" alt="" class="I_I" width="750">
                                        </div>
                                         <span class="tip-tw">
                                          2.如何使用在线备课空间？
                                        </span>
                                        <div>
                                            <pre>进入“资源中心”页面的“备课空间”，在左侧点击目标文件夹，继而点击右上角的“+新建课程”，在弹出框中输入课程名，即可进入课程编辑页面，进行在线备课。</pre>
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/tip-bkkj.png" alt="" class="I_I" width="750">
                                        </div>
                                         <span class="tip-tw">
                                          3.如何制作一节在线课程？
                                        </span>
                                        <div>
                                            <pre><span class="tip-c">首先上传视频： </span>进入课程编辑页面后，可直接上传已有的视频文件或者进行在线录制微课。 点击“录制微课”或者录课神器图标即可下载安装K6KT录课神器，安装完成后再次点击“录制微课”即可启动。点击右上角绿色的“点击录制”圆形图标开始录课，再次点击停止录课。录制完成后可点击绿色箭头回看微课，输入课件名称直接上传，如果预览不满意，可点击再录。</pre>
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/tip-scsp1.png" alt="" class="I_I" width="750">
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/tip-scsp2.png" alt="" class="I_I" width="750">
                                        </div>
                                        <div>
                                            <pre><span class="tip-c">其次上传课件： </span>包括任务单，讲义等，不局限格式，可供学生下载使用。</pre>
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/tip-sckj.png" alt="" class="I_I" width="750">
                                        </div>
                                        <div>
                                            <pre><span class="tip-c">再次上传微课进阶练习题：</span>上传几道练习题以便检查学生的掌握情况。如上传解析，学生做完后，可直接查看答案。</pre>
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/tip-jjxt.png" alt="" class="I_I" width="750">
                                        </div>
                                        <div>
                                            <pre>选择word 格式的试卷，“上传并配置”，设置做题时间，对每一小题进行设置，可添加大题，完成后点击右上角“完成并发布”,完成习题设置。</pre>
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/tip-scbpz.png" alt="" class="I_I" width="750">
                                        </div>
                                        <div>
                                            <pre>之后会跳转到课程编辑页面，点击页面最下方的“保存”就可完成课程制作。</pre>
                                        </div>

                                        <span class="tip-tw">
                                          4.如何将课程从备课空间推送到班级课程？推送到校本资源和联盟资源？
                                        </span>
                                        <div>
                                            <pre><span class="tip-c">方式一，</span>在课程编辑页面中找到“推送”，在“班级课程”下和“校本资源”下选择要推送的目标文件夹。</pre>
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/tip-kcbj.png" alt="" class="I_I" width="750">
                                        </div>
                                        <div>
                                            <pre><span class="tip-c">方式二，</span>在“备课空间”页面下，点击某一课程下方的“推送到班级课程”可以将该课程推送给该班级的所有同学，点击某一课程下方的“推送到校本资源”可以将该课程推送给全校的其他老师。点击“推送到联盟资源”可让联盟内学校老师看到课程。</pre>
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/tip-kckj1.png" alt="" class="I_I" width="750">
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/tip-kckj2.png" alt="" class="I_I" width="750">
                                        </div>
                                         <span class="tip-tw">
                                          5.如何借鉴校本资源和联盟资源课程？
                                        </span>
                                        <div>
                                            <pre>进入“校本资源”和“联盟资源”选择所需要的课程，点击课程进行预览，点击课程下方的“推送”按钮就可以推送到个人“备课空间”，进入备课空间根据需要可进行课程的二次编辑加工。</pre>
                                        </div>
                                         <span class="tip-tw">
                                          6.如何查看某一课程的学习统计？
                                        </span>
                                        <div>
                                            <pre>进入“资源中心”页面的“班级课程”，点击某一课程下方的“统计”。</pre>
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/tip-xxtj.png" alt="" class="I_I" width="750">
                                        </div>
                                        <div>
                                            <pre>可查看该课程所含视频的学习统计、查看该课程微课进阶练习的习题统计并对主观题进行批改。</pre>
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/tip-xxtj2.png" alt="" class="I_I" width="750">
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/tip-xxtj3.png" alt="" class="I_I" width="750">
                                        </div>
                                    </li>
                                    <li>
                                        <span class="tip-title">六、翻转课堂<a
                                                onclick="playMovie('http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/55b872ac0cf2a7f1af42dda8.mp4.m3u8')">

                                            <%--【观看演示视频】--%> </a>
                                        </span>

                                         <span class="tip-tw">
                                          1.如何给所带班级发布作业？
                                        </span>
                                        <div>
                                            <pre>在“翻转课堂”页面点击“作业”，即可针对特定班级以文字、语音、附件形式布置家庭作业。</pre>
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/tip-ZY.png" alt="" class="I_I" width="750">
                                        </div>
                                         <span class="tip-tw">
                                          2.如何对学生拍照上传的作业进行涂鸦批改？
                                        </span>
                                        <div>
                                            <pre>在“翻转课堂”页面点击“作业”，进入作业列表，点击学生回复的图片，即可对学生拍照上传的作业图片进行涂鸦批改。</pre>
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/tip-typg.png" alt="" class="I_I" width="750">
                                        </div>
                                        <span class="tip-tw">
                                          3.如何使用K6KT的题库资源在线组卷？
                                        </span>
                                        <div>
                                            <span class="tip-c">1）手工组卷</span>
                                        </div>
                                        <div>
                                            <pre>进入“翻转课堂”页面的“组卷”，点击“手工组卷”，完整填写本次考试的基本信息后点击“下一步”进入选题页面，点击“综合知识点选题”并通过右边的“请选择知识点”筛选出符合本次测试的题目，也可以点击“同步教材选题”从页面右边的“选择章节”按钮根据出版社筛选题库。然后根据“题型”和“难度”将题目“加入试卷”，题目添加完成后点击“确定生成”试卷。再点击“推送”到班级，学生即可在线考试，或者导出为PDF。同时在“我的组卷”列表中可以对已发布的试卷进行编辑、预览、删除。</pre>
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/tip-sgzj.jpg" alt="" class="I_I" width="750">
                                        </div>
                                        <div>
                                            <span class="tip-c">2）智能组卷</span>
                                        </div>
                                        <div>
                                            <pre>进入“翻转课堂”页面的“组卷”，点击“智能组卷”，完整填写本次考试的基本信息后点击“下一步”进入选题页面，根据页面提示选择本次测试的教材版本、考试知识点、每道大题中包含小题的数量，系统即可智能生成一份试卷，最后点击“推送”按钮，学生即可在线考试，或者导出为PDF。同时在“我的组卷”列表中可以对已发布的试卷进行编辑、预览、删除。</pre>
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/tip-znzj.jpg" alt="" class="I_I" width="750">
                                        </div>

                                         <span class="tip-tw">
                                          4.如何上传试题并进行在线考试和批改？
                                        </span>
                                        <div>
                                            <pre>进入“翻转课堂”页面的“考试”，在“上传考卷”中发布在线考试，操作与发布课程微课进阶练习完全相同。对于已有学生答题的在线考试，点击“批改试卷”即可进行批改。</pre>
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/tip-jxpg.png" alt="" class="I_I" width="750">
                                        </div>
                                         <span class="tip-tw">
                                          5.如何将一份纸质考试成绩计入成绩统计分析？
                                        </span>
                                        <div>
                                            <pre>进入“翻转课堂”页面的“成绩分析”，点击页面右上角的“成绩录入”并依次填充本次考试的基本信息即可完成新建。</pre>
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/tip-wcxj.png" alt="" class="I_I" width="750">
                                        </div>
                                        <div>
                                            <pre>在页面下方列表中找到刚新建的考试，点击“打分”即可录入学生的考试成绩。或者下载页面下方模板，一次性导入成绩。</pre>
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/tip-drcj.png" alt="" class="I_I" width="750">
                                        </div>
                                        <span class="tip-tw">
                                          6.如何查看本班级考试成绩的统计分析？
                                        </span>
                                        <div>
                                            <pre>进入“翻转课堂”页面的“成绩分析”的首页，选择需要查看的班级名称和考试名称即可了解本年级所有班级的“班级成绩对比”、本班的本次考试的“成绩分布”、“成绩统计”、“学生列表”等信息。</pre>
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/tip-tjfx.jpg" alt="" class="I_I" width="750">
                                        </div>
                                        <div>
                                            <pre>同时进入“学期成绩”亦可了解一个学期本年级所有班级的“班级成绩对比”、本班本学期的“成绩统计”和“学生列表”</pre>
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/tip-cjdb.jpg" alt="" class="I_I" width="750">
                                        </div>
                                         <span class="tip-tw">
                                          7.如何对拓展课的学生进行打分以及管理？
                                        </span>
                                        <div>
                                            <pre>进入“翻转课堂”页面的“拓展课”，在班级列表里，可以看到您管理的才艺课，点击该课程右侧的“课程评价”按钮，点击“添加课堂表现”，可对所有学生的课堂表现分课时进行打分；点击“课程总评”，对所有学生进行总评并填写评语。</pre>
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/tip-kczp.png" alt="" class="I_I" width="750">
                                        </div>
                                         <span class="tip-tw">
                                          8.如何对拓展课的学生进行考勤管理？
                                        </span>
                                        <div>
                                            <pre>进入“翻转课堂”页面的“拓展课”，点击拓展课班级进入班级学生列表页面，点击“班级考勤”，选择缺勤学生名字，进行”缺勤日期”,”时间”等 ,然后“添加”即可。</pre>
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/tip-bjkq.png" alt="" class="I_I" width="750">
                                        </div>
                                        <span class="tip-tw">
                                         9.如何了解所带班级的学生的德育情况？
                                        </span>
                                        <div>
                                            <pre>进入“翻转课堂”页面的“德育”，通过筛选学期、年级、班级，即可了解该班级的德育统计情况，点击某一个学生名字可查看该学生近期德育的“个人成绩”雷达图。</pre>
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/tip-dyqk1.jpg" alt="" class="I_I" width="750">
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/tip-dyqk2.jpg" alt="" class="I_I" width="750">
                                        </div>
                                    </li>
                                    <li>
                                        <span class="tip-title">七、电子超市<a
                                                onclick="playMovie('http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/55b873070cf2a7f1af42ddab.mp4.m3u8')">

                                            <%--【观看演示视频】--%> </a>
                                        </span>

                                        <span class="tip-tw">
                                        1.如何在电子超市中售出自己的课程？
                                        </span>
                                        <div>
                                            <pre><span class="tip-c">方式一，</span>进入“资源中心”页面的“备课空间”，点击某一课程下方的“推送”，选择“推送到电子超市”。</pre>
                                        </div>
                                        <div>
                                            <pre><span class="tip-c">方式二，</span>进入“电子超市”的“电子超市首页”，点击右上角的“快速上传至电子超市”，按照提示填写相关信息并编辑课程，即可在电子超市中出售自己的课程。</pre>
                                        </div>
                                        <span class="tip-tw">
                                            2.如何购买和查看全国优秀教师们分享的课程？
                                        </span>
                                        <div>
                                            <pre>进入“电子超市”的“电子超市首页”可根据条件筛选课程，筛选好课程后点击该课程，并点击“我要购买”，通过K6KT账户余额或者支付宝支付。同时可以在“个人账户”里管理订单。</pre>
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/tip-wygm.jpg" alt="" class="I_I" width="750">
                                        </div>
                                         <span class="tip-tw">
                                            3.如何管理自己的个人账户？
                                        </span>
                                        <div>
                                            <pre>进入“电子超市”的“个人账户”，可查看订单情况，个人课程出售收入等，并可进行充值或提现。</pre>
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/tip-grzh.png" alt="" class="I_I" width="750">
                                        </div>
                                    </li>
                                    <li>
                                        <span class="tip-title">八、群组交流<a
                                                onclick="playMovie('http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/55b873650cf2a7f1af42ddae.mp4.m3u8')">

                                            <%--【观看演示视频】 --%></a>
                                        </span>

                                         <span class="tip-tw">
                                          1.如何组建一个群组进行交流？
                                        </span>
                                        <div>
                                            <pre>进入“群组交流”点击“新建群组”，输入群组名称后，可以点击“我的好友”左边的三角形按键逐个添加好友成员，也可以点击“我的好友”右边的加号一次将“我的好友”全部添加进入群组进行交流。同时可以在群组里发表情、共享文件、查看历史聊天记录、管理群组成员、或者解散该群组。</pre>
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/tip-qzjl.jpg" alt="" class="I_I" width="750">
                                        </div>
                                    </li>
                                    <li>
                                         <span class="tip-title">九、火热活动<a
                                                 onclick="playMovie('http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/55b874440cf2a7f1af42ddb1.mp4.m3u8')">

                                             <%--【观看演示视频】--%> </a>
                                        </span>

                                         <span class="tip-tw">
                                          1.如何参加K6KT平台运营活动赢取大奖或现金？
                                        </span>
                                        <div>
                                            <pre>进入“火热活动”即可查看活动列表，点击其中一个活动，查看活动规则，即可参加并赢取奖品或现金。</pre>
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/tip-hrhd.jpg" alt="" class="I_I" width="750">
                                        </div>
                                    </li>
                                    <li>
                                         <span class="tip-title">十、学校管理<a
                                                 onclick="playMovie('http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/55b875aa0cf2a7f1af42ddb4.mp4.m3u8')">

                                             <%--【观看演示视频】--%> </a>
                                        </span>

                                        <span class="tip-tw">
                                          1.如何进行公文流转？
                                        </span>
                                        <div>
                                            <pre>进入“学校管理”页面的“公文流转”，点击“发布新公文流转”即可以文字、语音、附件的形式发布公文，并在通讯录中选择接收公文流转的人群。</pre>
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/tip-gwlz.png" alt="" class="I_I" width="750">
                                        </div>
                                        <span class="tip-tw">
                                          2.如何查看任课班级学生的总体学习情况？
                                        </span>
                                        <div>
                                            <pre>进入“学校管理”页面的“任课班级”，点击班级列表中某一任课班级，即可在“班级人员”页面下的学生列表中查看该班级学生在线总体学习情况，如：看完视频数、做题数。还可点击“导出信息”以把学生的学习情况导出为表格，或者“导入学号及班干部”信息。</pre>
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/tip-dcxx.png" alt="" class="I_I" width="750">
                                        </div>
                                        <span class="tip-tw">
                                          3.如何对学生进行考勤管理？
                                        </span>
                                        <div>
                                            <pre>进入“学校管理”页面的“任课班级”，点击班级列表中某一任课班级，进入“班级考勤”页面，在学生列表中选择缺勤的学生姓名，即可记录该学生的缺勤日期、时间及做相关备注，点击“添加”即可完成，同时也可以删除缺勤记录。</pre>
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/tip-kqgl.png" alt="" class="I_I" width="750">
                                        </div>
                                        <span class="tip-tw">
                                          4.如何统计班内学生的选课情况？
                                        </span>
                                        <div>
                                            <pre>进入“学校管理”页面的“任课班级”，点击班级列表中某一任课班级，进入“选课去向”页面，即可查看该班学生的拓展课选课去向，点击“导出信息”导出记录该信息的Excel表格。</pre>
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/tip-xkqk.png" alt="" class="I_I" width="750">
                                        </div>
                                        <span class="tip-tw">
                                          5.如何给学生增加素质教育经验值？
                                        </span>
                                        <div>
                                            <pre>进入“学校管理”页面的“任课班级”，在班级列表中点击某一任课班级后面的“经验值加分”，进入打分页面，系统默认给每个学生加1分（表现较好），可以直接确定给分，可以屡次加分。其中：“表现欠佳”：系统默认加0分；“表现较好”：系统默认加1分；“表现优异”：系统默认加2分。</pre>
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/tip-bxyy.png" alt="" class="I_I" width="750">
                                        </div>
                                        <span class="tip-tw">
                                           6.校领导如何查看某班级师生信息及教学情况？
                                        </span>
                                        <div>
                                            <pre>进入“学校管理”页面的“全校班级”，根据年级搜索选择某班级名称，在“本班师生”中查看老师备课情况和学生学习总体情况，在“本班课程”和“本班作业”中查看各科任课老师对该班级的课程推送、作业布置的详细信息。</pre>
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/tip-bbss.png" alt="" class="I_I" width="750">
                                        </div>
                                        <span class="tip-tw">
                                           7.校领导如何查看每学科老师备课情况？
                                        </span>
                                        <div>
                                            <pre>进入“学校管理”页面的“全校老师”，根据学科、年级进行筛选，点击需要了解的老师头像即可查看该老师的备课情况。                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/tip-qbxk.png" alt="" class="I_I" width="750">
                                        </div>
                                         <span class="tip-tw">
                                           8.如何进行奖惩管理？
                                        </span>
                                        <div>
                                            <pre>进入“学校管理”页面的“表彰违纪”，即可添加、修改、删除学生奖惩记录，还可根据条件筛选查询过往奖惩记录，点击“导出”导出记录全校学生奖惩记录的Excel表格。</pre>
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/tip-jcgl.png" alt="" class="I_I" width="750">
                                        </div>
                                          <span class="tip-tw">
                                           9.如何进行综合评比的项目设置，分数导入及查看汇总统计？
                                        </span>
                                        <div>
                                            <pre>进入“学校管理”页面的“综合评比”，在“综合评比表设置”中设置此次班级综合</pre>
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/tip-zhpb.png" alt="" class="I_I" width="750">
                                        </div>
                                        <div>
                                            <pre>评比的项目名称、范围、评比项目，在“综合评比分数录入”中给参评班级打分且系统自动统计总分，在“综合评分汇总”中查看汇总统计并可导出统计表格。</pre>
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/tip-fslr1.png" alt="" class="I_I" width="750">
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/tip-fslr2.png" alt="" class="I_I" width="750">
                                        </div>
                                         <span class="tip-tw">
                                          10.如何进行考务管理？
                                        </span>
                                        <div>
                                            <pre>进入“学校管理”页面的“考务管理”。选择相应的年级和输入方式，点击录入选择班级和学科进行成绩录入，也可以采用模板一次性导入数据。成绩录入后也可以导出Exce表格。</pre>
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/tip-cjlr.png" alt="" class="I_I" width="750">
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/tip-cjlr2.png" alt="" class="I_I" width="750">
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/tip-cjlr3.png" alt="" class="I_I" width="750">
                                        </div>
                                        <span class="tip-tw">
                                          11.如何进行报修管理？
                                        </span>
                                        <div>
                                            <pre>进入“学校管理”页面的“报修管理”，在“报修申请”中填写好信息即可提交报修申请，在“报修情况查询”中查询已报修项目的进展情况，管理员可在“报修处理”中派发报修任务给指定人员。</pre>
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/tip-bxsq.png" alt="" class="I_I" width="750">
                                        </div>
                                        <span class="tip-tw">
                                        12.如何进行工资项目的调整、导入导出工资汇总信息？
                                        </span>
                                        <div>
                                            <pre>进入“学校管理”页面的“工资管理”，在“工资项目管理”中添加、修改、删除工资组成项目，在“薪酬表格”中针对每月每次工资发放明细制表。</pre>
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/tip-xmgl.png" alt="" class="I_I" width="750">
                                        </div>
                                        <span class="tip-tw">
                                        13.如何进行校产管理？
                                        </span>
                                        <div>
                                            <pre>针对固定校产，进入“学校管理”页面的“校产管理”，在“校产分类”中管理校产目录，在“使用校产”中设置校产类别信息、手动添加或根据模板填写信息后导入校产信息，对于已添加的校产信息可以查看、修改、删除。</pre>
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/tip-syxc.png" alt="" class="I_I" width="750">
                                        </div>
                                        <span class="tip-tw">
                                        14.如何进行校产使用登记？
                                        </span>
                                        <div>
                                            <pre>针对分配给老师个人使用的校产器材，进入“学校管理”页面的“使用登记”，在“器材分类”中管理器材目录，在“使用器材”中设置器材类别信息、手动添加或根据模板填写信息后导入器材及使用者信息，对于已添加的器材信息可以查看、修改、删除。</pre>
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/tip-syqc.png" alt="" class="I_I" width="750">
                                        </div>
                                    </li>
                                    <li>
                                         <span class="tip-title">十一、平台管理<a
                                                 onclick="playMovie('http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/55b874440cf2a7f1af42ddb1.mp4.m3u8')">

                                             <%--【观看演示视频】--%> </a>
                                        </span>

                                         <span class="tip-tw">
                                          1.如何设置学科？
                                        </span>
                                        <div>
                                            <pre>进入“平台管理”中的“设置学科”，点击右上角的“新建科目”，在对话框中填写相应的科目名称和从属年级，点击确定即可。</pre>
                                        </div>
                                         <span class="tip-tw">
                                          2.如何新增、删除老师信息？
                                        </span>
                                        <div>
                                            <pre>进入“平台管理”中的“教师管理”，点击右上角“新建老师”，在对话框填写相应的教师名称、教职工号、权限即可新增成功。在老师信息后面点击笔状、垃圾箱图标即可编辑、删除老师信息。</pre>
                                        </div>
                                         <span class="tip-tw">
                                          3.如何新增、编辑年级和班级信息？
                                        </span>
                                        <div>
                                            <pre>新增年级：进入“平台管理”的“设置班级学生”，点击右面“新建年级”，在对话框中输入相应信息，点击确定，即可生成新的年级。如需要编辑年级信息，点击相应年级后面的笔状、垃圾箱图标即可编辑或删除。</pre>
                                        </div>
                                        <div>
                                            <pre>新增班级：点击相应年级，然后点击右面的“新建班级”，在对话框中填写相应信息，点击确定即可新增班级。如需要编辑班级，点击相应班级后面笔状图标，在对话框相应位置填写、确定即可编辑成功。</pre>
                                        </div>
                                        <span class="tip-tw">
                                          4.如何调整班级代课老师及学生信息？
                                        </span>
                                        <div>
                                            <pre>进入步骤如3，点击相应班级，出现“新建学生”和“添加老师”，点击后在相应对话框填写相应信息确定即可。如需调整老师或学生信息，点击相应的老师或学生后面的笔状图标，即可在对话框中重新设置调整。点击垃圾箱图标，即可删除信息。学生如需调换班级，则点击学生信息后面调出图标，在对话框中进行操作、确定即可。</pre>
                                        </div>
                                        <span class="tip-tw">
                                          5.如何开设拓展课程？
                                        </span>
                                        <div>
                                            <pre>进入“平台管理”的“管理拓展课”，点击右上方的“新建拓展课”，在对话框中填写相应信息，点击确定即可。</pre>
                                        </div>
                                         <span class="tip-tw">
                                          6.如何发布管理新闻？
                                        </span>
                                        <div>
                                            <pre>进入“平台管理”的“管理新闻”。点击“栏目管理”，可以对栏目进行新增或编辑。点击“内容管理”，可以根据栏目类型，进行“搜索”或“发布内容”。发布内容时，点击“发布内容”，在对话框中填写相应信息，点击发布，即可完成。</pre>
                                        </div>
                                        <span class="tip-tw">
                                          7.如何管理教师所在的部门？
                                        </span>
                                        <div>
                                            <pre>进入“平台管理”中“管理教师部门”，点击右边“新建部门”，在对话框中输入相应部门名称，点击确定，然后点击相应部门，点击“添加成员”，进行教师的添加，选择相应教师后，点击确定即可。</pre>
                                        </div>
                                         <span class="tip-tw">
                                          8.如何设置德育项目指标？
                                        </span>
                                        <div>
                                            <pre>进入“平台管理”的“管理德育项目”，点击右面“新建项目”，在对话框中输入相应项目名称，点击确定即可。需要编辑或删除，点击相应项目后面的笔状或垃圾箱图标即可进行相关操作。</pre>
                                        </div>
                                         <span class="tip-tw">
                                          9.如何进行校区的管理？
                                        </span>
                                        <div>
                                            <pre>进入“平台管理”的“管理校区”，点击右面的“添加校区”，在对话框中输入相关信息，点击确定即可。</pre>
                                        </div>
                                         <span class="tip-tw">
                                          10.如何查看师生参与度和活跃度？
                                        </span>
                                        <div>
                                            <pre>进入“平台管理”的“平台使用统计”，即可看到访问统计、备课统计、资源统计等，并可根据条件筛选，看到不同班级，不同时间段的统计。点击右边的“平台功能统计”，就可以看到微校园发帖数统计和排行榜。</pre>
                                        </div>
                                    </li>-->
                                    <%--<li>
                                        <span class="tip-title">11、校领导如何对所带班级的学生学习情况进行了解？<a onclick="playMovie('http://player.polyv.net/videos/1a9bb5bed59fa86aa0770a3c58c8c293_1.swf')" class="vedio">&lt;%&ndash;【观看演示视频】&ndash;%&gt;</a></span>
                                        <div><pre>进入“我的班级”，在班级列表中选择某一班级，即可查看该班级所有学生的学习该汇总信息，包括看完视频数、做题数等。进一步点击某一学生的名字，还可更详细地查看其学习情况，包括其学习的所有课程的课程名称、课程学习情况、做题数占总题数百分比、答题正确率等。

如要查看某一课程的学生学习情况，在“我的微课”页面下的“班级课程”，选择某一课程，点击其下方的“统计”，即可查看该班学生学习该课程的汇总信息，包括视频观看统计信息、课后练习统计信息等等。</pre></div>
                                    </li>
                                    <li>
                                        <span class="tip-title">12、校领导如何给所带班级学生增加素质教育经验值？</span>
                                        <div><pre>进入“我的班级”，在班级列表中选择任教班级，点击“学生经验值加分”，进入打分页面，系统默认是给每个学生加1分（表现较好），可以直接确定给分，可以屡次加分。其中:
“表现差”?:系统默认加0分;
“表现较好”:系统默认加1分;
“表现优异”:系统默认加2分。</pre></div>
                                    </li>
                                    <li>
                                        <span class="tip-title">13、如何观看云课程并把云课程推送到备课空间？<a onclick="playMovie('http://player.polyv.net/videos/1a9bb5bed5b50e70aecfb26b31ec14d3_1.swf')" class="vedio">&lt;%&ndash;【观看演示视频】&ndash;%&gt;</a></span>
                                        <div><pre>进入“我的微课”页面，点击“云课程”，可从中根据条件选择视频课件，点击该视频下方“试看”观看该云课程，点击“推送”将其推送到“备课空间”。</pre></div>
                                    </li>
                                    <li>
                                        <span class="tip-title">14、如何在线制作微课？<a onclick="playMovie('http://player.polyv.net/videos/1a9bb5bed5e7833bcc575a97c6391091_1.swf')" class="vedio">&lt;%&ndash;【观看演示视频】&ndash;%&gt;</a></span>
                                        <div><pre>请进入“我的微课”页面点击右上角“如何录制课程”，根据页面提示操作即可录制微课。</pre></div>
                                    </li>
                                    <li>
                                        <span class="tip-title">15、如何使用录课神器录制视频？</span>
                                        <div><pre>在“我的微课”页面，点击顶部的“录制神器”图标，在弹出的窗口填写课程名称并选择存放文件夹，在接下来的课程信息页面点击“录制微课”即可安装K6KT录课神器，安装完成之后刷新页面，再次点击“录制微课”即可启动。录制完成后点击“上传”即可将录制的课程上传到K6KT快乐课堂。</pre></div>
                                    </li>
                                    <li>
                                        <span class="tip-title">16、如何将制作好的微课上传到备课空间？<a onclick="playMovie('http://player.polyv.net/videos/1a9bb5bed525bb9f304744f709acb3e4_1.swf')" class="vedio">&lt;%&ndash;【观看演示视频】&ndash;%&gt;</a></span>
                                        <div><pre>方法1：进入“我的微课”页面下的“备课空间”，在已有的文件夹或者新建的文件夹中点击“新建课程”即可建立新的空白课程。点击该课程右上角的标记进入“编辑”页面，在“上传视频”栏目中选择制作好的微课文件并上传。

方法2：进入“我的微课”页面下的“备课空间”，找到“快速上传至备课空间”，点击后即可跳出“新建课程”对话框，输入课程名称并选择需要放置的目录后（如目录中没有合适的文件夹存放课程，可选择某一文件夹后点击“新建文件夹”来添加子文件夹），点击“下一步”。在跳转后的课程编辑页面，可选择已经录制好的微课并上传。</pre></div>
                                    </li>
                                    <li>
                                        <span class="tip-title">17、除了视频课件，还可以上传什么资源供学生自主学习？<a onclick="playMovie('http://player.polyv.net/videos/1a9bb5bed579993d9ba144cdc4ab1349_1.swf')" class="vedio">&lt;%&ndash;【观看演示视频】&ndash;%&gt;</a></span>
                                        <div><pre>在“备课空间”中点击某课程右上角的标记进入“编辑”页面（或者由“快速上传至备课空间”进入编辑页面），除了上传视频课件，还可以上传word, excel, ppt, pdf等任何格式的文本课件供学生下载学习。同时可以针对视频内容上传配套习题及解析来辅助学生自主学习。

其中，建立配套习题， 支持手动输入题干，或者上传图片，如有解析， 可将解析上传。如果一道题录入完毕，点击“继续录题”进行下一题的编辑，如果没有下一题，则点击“完成”即可。</pre></div>
                                    </li>
                                    <li>
                                        <span class="tip-title">18、如何将课程从备课空间推送到班级课程？推送到校本资源？<a onclick="playMovie('http://player.polyv.net/videos/1a9bb5bed58c9525248116986fd0bb55_1.swf')" class="vedio">&lt;%&ndash;【观看演示视频】&ndash;%&gt;</a></span>
                                        <div><pre>进入“我的微课”页面下的“备课空间”，点击某一课程下方的“推送到班级”箭头图标可以将该课程推送给班级的所有同学，点击某一课程下方的“推送到校本资源”箭头图标可以将该课程推送给全校的其他老师。</pre></div>
                                    </li>
                                    <li>
                                        <span class="tip-title">19、如何使用在线备课空间？<a onclick="playMovie('http://player.polyv.net/videos/1a9bb5bed579993d9ba144cdc4ab1349_1.swf')" class="vedio">&lt;%&ndash;【观看演示视频】&ndash;%&gt;</a></span>
                                        <div><pre>A, 每位老师可享受“在线备课空间”无限大，位于“我的微课”页面。
B, "备课空间"是老师所有教学课程的“中转站”。
C, 老师可以从“云课程”推送优质课件到自己的“备课空间”，或者上传自己录制的“微课”到“备课空间”留存。
D, 老师在“备课空间”里自己定义目录进行分类课件管理，并且选择这里的课件，推送到“班级课程”供本班学生使用，或者推送到“校本资源”供全校老师参考使用。</pre></div>
                                    </li>
                                    <li>
                                        <span class="tip-title">20、如何在平板电脑及手机移动终端，使用K6KT-快乐课堂？</span>
                                        <div><pre>A, 安卓系统如何下载APP软件 ？
  请进入以下网址下载并安装android客户端，通过学校分配的用户名和密码，即可登录：           <a href="http://www.k6kt.com/upload/resources/k6kt.apk">http://www.k6kt.com/upload/resources/k6kt.apk</a>

B, 苹果系统如何下载APP软件？
   请进入苹果的APP Store搜索“K6KT”，通过学校分配的用户名和密码即可登录。

C, 利用移动终端，我们都能做些什么？
  *可以随时接收和在线提交学校发布的家庭作业
  *可以随时接收学校发布的各类通知
  *可以随时和学校老师家长进行私信交流，掌握孩子动态
  *可以随时拍照，上传到“微校园”进行分享
  *随时随地，家校互动，在线学习，虚拟社区全分享。</pre></div>
                                    </li>
                                    <li name="headmasterScore" id="headmasterScore">
                                        <span class="tip-title">21、校领导如何获得经验值? </span>
                                        <div><pre>途径1，完善个人资料
首次上传头像可获得1分，首次修改密码也可获得1分。
途径2，每日首次发帖，每天的第一次发帖加1分。
途径3，推送自制课程（视频或课件或习题）到校本资源加1分。
途径4，推送自制课程（视频或课件或习题）到班级课程加1分（推送到多个班级各得1分）。
途径5，推送云课程到班级课程加1分（推送到多个班级各得1分）。
途径6，通知、作业每发布一次到每一个班级，加1分。</pre></div>
                                    </li>--%>
                                </ul>
                            </div>
                            <!-- headmaster start -->

                            <!-- teacher start -->
                            <div id="teacher-help-container" class="help-container">
                                <div style="font-size: 20px;font-weight: bold;overflow: visible;width:100%;height: 40px;line-height: 40px;text-align: center">
                                    教师用户手册
                                </div>
                                <ul>
                                    <li>
                                        <span class="tip-title">一、如何登录智慧校园-K6KT快乐课堂平台？<a onclick="playMovie('http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/55b98bd50cf2a7f1af42de08.mp4.m3u8')">

                                            【观看演示视频】 </a>
                                        </span>

                                        <div>
                                            <pre>用户登录<a href="http://www.k6kt.com" target="_blank">www.k6kt.com</a>, 在页面右上方填入学校分配的用户名与密码，即可登录全向互动K6KT-快乐课堂。登录后根据需要点击相应版块即可进入使用界面。</pre>
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/I_dl.jpg" alt="" class="I_I" width="750">
                                        </div>
                                    </li>
                                    <li>
                                        <span class="tip-title">二、如何更改个人设置？<a
                                                onclick="playMovie('http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/55b846770cf2a7f1af42dd50.mp4.m3u8')">

                                            【观看演示视频】 </a>
                                        </span>

                                        <div>
                                            <pre>登录后，进入个人中心，点击“个人设置”，点击“修改密码”可设置您的登陆密码。点击“修改头像”下的“点击上传图片”即可上传已有照片或者拍摄照片来修改头像</pre>
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/tip-grzx.png" alt="" class="I_I" width="750">
                                        </div>
                                    </li>
                                    <li>
                                         <span class="tip-title">三、校园安全关注<a
                                                 onclick="playMovie('http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/55b846da0cf2a7f1af42dd53.mp4.m3u8')">

                                             <%--【观看演示视频】--%> </a>
                                         </span>
                                        <span class="tip-tw">
                                            1.如何发布一条校园安全隐患警示？
                                        </span>

                                        <div>
                                            <pre>进入“校园安全关注”，在文本框中输入文字说明，还可上传图片，点击“提交”即可发表，误报可删除。如果隐患已被处理好，点击“处理”即可。</pre>
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/tip-aqgz.png" alt="" class="I_I" width="750">
                                        </div>
                                    </li>
                                    <li>
                                        <span class="tip-title">四、家校互动<a
                                                onclick="playMovie('http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/55b8472f0cf2a7f1af42dd57.mp4.m3u8')">

                                            <%--【观看演示视频】--%> </a>
                                        </span>
                                        <span class="tip-tw">
                                            1.如何使用微校园、微家园社区发帖和评论?
                                        </span>
                                        <div>
                                            <pre>在“家校互动”页面点击“微校园”，即可发表自己的新鲜事或者对别人的新鲜事进行回复、点赞，还可选择帖子发表范围：本校、本年级或者本班，以及查看最新、最热和自己发的帖子。</pre>
                                            <pre>在“家校互动”页面点击“微家园”即可查看学生、家长发的帖子并进行留言和点赞。</pre>
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/tip-jxhd.png" alt="" class="I_I" width="750">
                                        </div>
                                        <span class="tip-tw">
                                            2.如何发布通知？
                                        </span>
                                        <div>
                                            <pre>在“家校互动”页面点击“学校通知”，“发布新通知”，从通讯录里勾选收件人后，即可以文字、语音、附件的形式发布在线通知，勾选“同步到日历”以及“有效时间”还可以实现收到此消息的人在有效时间内在日历中出现事件提醒。</pre>
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/tip-fbtz1.png" alt="" class="I_I" width="750">
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/tip-fbtz2.png" alt="" class="I_I" width="750">
                                        </div>
                                         <span class="tip-tw">
                                           3.如何在好友圈组织活动？
                                        </span>
                                        <div>
                                            <pre>在“家校互动”页面点击“好友圈”，点击“发起活动”，填写活动规则等信息即可发布活动。还可以分类查看“好友圈”里活动，比如：最近动态、好友活动、推进活动、我参加的、我发起的。</pre>
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/tip-zzhd.png" alt="" class="I_I" width="750">
                                        </div>
                                        <span class="tip-tw">
                                          4.如何发起投票选举？查看投票结果？
                                        </span>
                                        <div>
                                            <pre>在“家校互动”页面点击“投票选举”，即可编辑并发布投票选举，包括指定投票公开范围、参选角色、指定候选人、投票角色、投票时间等。同样在“投票选举”中可查看所有投票的结果。</pre>
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/tip-tpxj.png" alt="" class="I_I" width="750">
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/tip-tpjg.png" alt="" class="I_I" width="750">
                                        </div>
                                        <span class="tip-tw">
                                          5.如何发起问卷调查？
                                        </span>
                                        <div>
                                            <pre>在“家校互动”页面点击“调查问卷”，点击右上方的“新建问卷”进入问卷编辑页面，上传问卷文档（word格式）并对问卷进行相关配置后，点击“完成并发布”即可。</pre>
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/tip-scwj.png" alt="" class="I_I" width="750">
                                        </div>
                                    </li>
                                    <li>
                                        <span class="tip-title">五、资源中心<a
                                                onclick="playMovie('http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/55b8706d0cf2a7f1af42dda5.mp4.m3u8')">

                                            <%--【观看演示视频】--%> </a>
                                        </span>
                                        <span class="tip-tw">
                                          1.如何观看云课程并把云课程推送到备课空间？
                                        </span>
                                        <div>
                                            <pre>进入“资源中心”页面的“云课程”，可从中根据条件选择视频课件，点击该视频下方“试看”观看该云课程，点击“推送”将其推送到“备课空间”。推送之后的课程可以在“备课空间”内找到并可进行二次编辑加工。</pre>
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/tip-zyzx.png" alt="" class="I_I" width="750">
                                        </div>
                                         <span class="tip-tw">
                                          2.如何使用在线备课空间？
                                        </span>
                                        <div>
                                            <pre>进入“资源中心”页面的“备课空间”，在左侧点击目标文件夹，继而点击右上角的“+新建课程”，在弹出框中输入课程名，即可进入课程编辑页面，进行在线备课。</pre>
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/tip-bkkj.png" alt="" class="I_I" width="750">
                                        </div>
                                         <span class="tip-tw">
                                          3.如何制作一节在线课程？
                                        </span>
                                        <div>
                                            <pre><span class="tip-c">首先上传视频： </span>进入课程编辑页面后，可直接上传已有的视频文件或者进行在线录制微课。 点击“录制微课”或者录课神器图标即可下载安装K6KT录课神器，安装完成后再次点击“录制微课”即可启动。点击右上角绿色的“点击录制”圆形图标开始录课，再次点击停止录课。录制完成后可点击绿色箭头回看微课，输入课件名称直接上传，如果预览不满意，可点击再录。</pre>
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/tip-scsp1.png" alt="" class="I_I" width="750">
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/tip-scsp2.png" alt="" class="I_I" width="750">
                                        </div>
                                        <div>
                                            <pre><span class="tip-c">其次上传课件： </span>包括任务单，讲义等，不局限格式，可供学生下载使用。</pre>
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/tip-sckj.png" alt="" class="I_I" width="750">
                                        </div>
                                        <div>
                                            <pre><span class="tip-c">再次上传微课进阶练习题：</span>上传几道练习题以便检查学生的掌握情况。如上传解析，学生做完后，可直接查看答案。</pre>
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/tip-jjxt.png" alt="" class="I_I" width="750">
                                        </div>
                                        <div>
                                            <pre>选择word 格式的试卷，“上传并配置”，设置做题时间，对每一小题进行设置，可添加大题，完成后点击右上角“完成并发布”,完成习题设置。</pre>
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/tip-scbpz.png" alt="" class="I_I" width="750">
                                        </div>
                                        <div>
                                            <pre>之后会跳转到课程编辑页面，点击页面最下方的“保存”就可完成课程制作。</pre>
                                        </div>

                                        <span class="tip-tw">
                                          4.如何将课程从备课空间推送到班级课程？推送到校本资源和联盟资源？
                                        </span>
                                        <div>
                                            <pre><span class="tip-c">方式一，</span>在课程编辑页面中找到“推送”，在“班级课程”下和“校本资源”下选择要推送的目标文件夹。</pre>
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/tip-kcbj.png" alt="" class="I_I" width="750">
                                        </div>
                                        <div>
                                            <pre><span class="tip-c">方式二，</span>在“备课空间”页面下，点击某一课程下方的“推送到班级课程”可以将该课程推送给该班级的所有同学，点击某一课程下方的“推送到校本资源”可以将该课程推送给全校的其他老师。点击“推送到联盟资源”可让联盟内学校老师看到课程。</pre>
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/tip-kckj1.png" alt="" class="I_I" width="750">
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/tip-kckj2.png" alt="" class="I_I" width="750">
                                        </div>
                                         <span class="tip-tw">
                                          5.如何借鉴校本资源和联盟资源课程？
                                        </span>
                                        <div>
                                            <pre>进入“校本资源”和“联盟资源”选择所需要的课程，点击课程进行预览，点击课程下方的“推送”按钮就可以推送到个人“备课空间”，进入备课空间根据需要可进行课程的二次编辑加工。</pre>
                                        </div>
                                         <span class="tip-tw">
                                          6.如何查看某一课程的学习统计？
                                        </span>
                                        <div>
                                            <pre>进入“资源中心”页面的“班级课程”，点击某一课程下方的“统计”。</pre>
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/tip-xxtj.png" alt="" class="I_I" width="750">
                                        </div>
                                        <div>
                                            <pre>可查看该课程所含视频的学习统计、查看该课程微课进阶练习的习题统计并对主观题进行批改。</pre>
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/tip-xxtj2.png" alt="" class="I_I" width="750">
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/tip-xxtj3.png" alt="" class="I_I" width="750">
                                        </div>
                                    </li>
                                    <li>
                                        <span class="tip-title">六、翻转课堂<a
                                                onclick="playMovie('http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/55b872ac0cf2a7f1af42dda8.mp4.m3u8')">

                                            <%--【观看演示视频】--%> </a>
                                        </span>

                                         <span class="tip-tw">
                                          1.如何给所带班级发布作业？
                                        </span>
                                        <div>
                                            <pre>在“翻转课堂”页面点击“作业”，即可针对特定班级以文字、语音、附件形式布置家庭作业。</pre>
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/tip-ZY.png" alt="" class="I_I" width="750">
                                        </div>
                                         <span class="tip-tw">
                                          2.如何对学生拍照上传的作业进行涂鸦批改？
                                        </span>
                                        <div>
                                            <pre>在“翻转课堂”页面点击“作业”，进入作业列表，点击学生回复的图片，即可对学生拍照上传的作业图片进行涂鸦批改。</pre>
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/tip-typg.png" alt="" class="I_I" width="750">
                                        </div>
                                        <span class="tip-tw">
                                          3.如何使用K6KT的题库资源在线组卷？
                                        </span>
                                        <div>
                                            <span class="tip-c">1）手工组卷</span>
                                        </div>
                                        <div>
                                            <pre>进入“翻转课堂”页面的“组卷”，点击“手工组卷”，完整填写本次考试的基本信息后点击“下一步”进入选题页面，点击“综合知识点选题”并通过右边的“请选择知识点”筛选出符合本次测试的题目，也可以点击“同步教材选题”从页面右边的“选择章节”按钮根据出版社筛选题库。然后根据“题型”和“难度”将题目“加入试卷”，题目添加完成后点击“确定生成”试卷。再点击“推送”到班级，学生即可在线考试，或者导出为PDF。同时在“我的组卷”列表中可以对已发布的试卷进行编辑、预览、删除。</pre>
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/tip-sgzj.jpg" alt="" class="I_I" width="750">
                                        </div>
                                        <div>
                                            <span class="tip-c">2）智能组卷</span>
                                        </div>
                                        <div>
                                            <pre>进入“翻转课堂”页面的“组卷”，点击“智能组卷”，完整填写本次考试的基本信息后点击“下一步”进入选题页面，根据页面提示选择本次测试的教材版本、考试知识点、每道大题中包含小题的数量，系统即可智能生成一份试卷，最后点击“推送”按钮，学生即可在线考试，或者导出为PDF。同时在“我的组卷”列表中可以对已发布的试卷进行编辑、预览、删除。</pre>
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/tip-znzj.jpg" alt="" class="I_I" width="750">
                                        </div>

                                         <span class="tip-tw">
                                          4.如何上传试题并进行在线考试和批改？
                                        </span>
                                        <div>
                                            <pre>进入“翻转课堂”页面的“考试”，在“上传考卷”中发布在线考试，操作与发布课程微课进阶练习完全相同。对于已有学生答题的在线考试，点击“批改试卷”即可进行批改。</pre>
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/tip-jxpg.png" alt="" class="I_I" width="750">
                                        </div>
                                         <span class="tip-tw">
                                          5.如何将一份纸质考试成绩计入成绩统计分析？
                                        </span>
                                        <div>
                                            <pre>进入“翻转课堂”页面的“成绩分析”，点击页面右上角的“成绩录入”并依次填充本次考试的基本信息即可完成新建。</pre>
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/tip-wcxj.png" alt="" class="I_I" width="750">
                                        </div>
                                        <div>
                                            <pre>在页面下方列表中找到刚新建的考试，点击“打分”即可录入学生的考试成绩。或者下载页面下方模板，一次性导入成绩。</pre>
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/tip-drcj.png" alt="" class="I_I" width="750">
                                        </div>
                                        <span class="tip-tw">
                                          6.如何查看本班级考试成绩的统计分析？
                                        </span>
                                        <div>
                                            <pre>进入“翻转课堂”页面的“成绩分析”的首页，选择需要查看的班级名称和考试名称即可了解本年级所有班级的“班级成绩对比”、本班的本次考试的“成绩分布”、“成绩统计”、“学生列表”等信息。</pre>
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/tip-tjfx.jpg" alt="" class="I_I" width="750">
                                        </div>
                                        <div>
                                            <pre>同时进入“学期成绩”亦可了解一个学期本年级所有班级的“班级成绩对比”、本班本学期的“成绩统计”和“学生列表”</pre>
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/tip-cjdb.jpg" alt="" class="I_I" width="750">
                                        </div>
                                         <span class="tip-tw">
                                          7.如何对拓展课的学生进行打分以及管理？
                                        </span>
                                        <div>
                                            <pre>进入“翻转课堂”页面的“拓展课”，在班级列表里，可以看到您管理的才艺课，点击该课程右侧的“课程评价”按钮，点击“添加课堂表现”，可对所有学生的课堂表现分课时进行打分；点击“课程总评”，对所有学生进行总评并填写评语。</pre>
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/tip-kczp.png" alt="" class="I_I" width="750">
                                        </div>
                                         <span class="tip-tw">
                                          8.如何对拓展课的学生进行考勤管理？
                                        </span>
                                        <div>
                                            <pre>进入“翻转课堂”页面的“拓展课”，点击拓展课班级进入班级学生列表页面，点击“班级考勤”，选择缺勤学生名字，进行”缺勤日期”,”时间”等 ,然后“添加”即可。</pre>
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/tip-bjkq.png" alt="" class="I_I" width="750">
                                        </div>
                                        <span class="tip-tw">
                                         9.如何了解所带班级的学生的德育情况？
                                        </span>
                                        <div>
                                            <pre>进入“翻转课堂”页面的“德育”，通过筛选学期、年级、班级，即可了解该班级的德育统计情况，点击某一个学生名字可查看该学生近期德育的“个人成绩”雷达图。</pre>
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/tip-dyqk1.jpg" alt="" class="I_I" width="750">
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/tip-dyqk2.jpg" alt="" class="I_I" width="750">
                                        </div>
                                    </li>
                                    <li>
                                        <span class="tip-title">七、电子超市<a
                                                onclick="playMovie('http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/55b873070cf2a7f1af42ddab.mp4.m3u8')">

                                            <%--【观看演示视频】--%> </a>
                                        </span>

                                        <span class="tip-tw">
                                        1.如何在电子超市中售出自己的课程？
                                        </span>
                                        <div>
                                            <pre><span class="tip-c">方式一，</span>进入“资源中心”页面的“备课空间”，点击某一课程下方的“推送”，选择“推送到电子超市”。</pre>
                                        </div>
                                        <div>
                                            <pre><span class="tip-c">方式二，</span>进入“电子超市”的“电子超市首页”，点击右上角的“快速上传至电子超市”，按照提示填写相关信息并编辑课程，即可在电子超市中出售自己的课程。</pre>
                                        </div>
                                        <span class="tip-tw">
                                            2.如何购买和查看全国优秀教师们分享的课程？
                                        </span>
                                        <div>
                                            <pre>进入“电子超市”的“电子超市首页”可根据条件筛选课程，筛选好课程后点击该课程，并点击“我要购买”，通过K6KT账户余额或者支付宝支付。同时可以在“个人账户”里管理订单。</pre>
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/tip-wygm.jpg" alt="" class="I_I" width="750">
                                        </div>
                                         <span class="tip-tw">
                                            3.如何管理自己的个人账户？
                                        </span>
                                        <div>
                                            <pre>进入“电子超市”的“个人账户”，可查看订单情况，个人课程出售收入等，并可进行充值或提现。</pre>
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/tip-grzh.png" alt="" class="I_I" width="750">
                                        </div>
                                    </li>
                                    <li>
                                        <span class="tip-title">八、群组交流<a
                                                onclick="playMovie('http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/55b873650cf2a7f1af42ddae.mp4.m3u8')">

                                            <%--【观看演示视频】 --%></a>
                                        </span>

                                         <span class="tip-tw">
                                          1.如何组建一个群组进行交流？
                                        </span>
                                        <div>
                                            <pre>进入“群组交流”点击“新建群组”，输入群组名称后，可以点击“我的好友”左边的三角形按键逐个添加好友成员，也可以点击“我的好友”右边的加号一次将“我的好友”全部添加进入群组进行交流。同时可以在群组里发表情、共享文件、查看历史聊天记录、管理群组成员、或者解散该群组。</pre>
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/tip-qzjl.jpg" alt="" class="I_I" width="750">
                                        </div>
                                    </li>
                                    <li>
                                         <span class="tip-title">九、火热活动<a
                                                 onclick="playMovie('http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/55b874440cf2a7f1af42ddb1.mp4.m3u8')">

                                             <%--【观看演示视频】--%> </a>
                                        </span>

                                         <span class="tip-tw">
                                          1.如何参加K6KT平台运营活动赢取大奖或现金？
                                        </span>
                                        <div>
                                            <pre>进入“火热活动”即可查看活动列表，点击其中一个活动，查看活动规则，即可参加并赢取奖品或现金。</pre>
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/tip-hrhd.jpg" alt="" class="I_I" width="750">
                                        </div>
                                    </li>
                                    <li>
                                         <span class="tip-title">十、学校管理<a
                                                 onclick="playMovie('http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/55b875aa0cf2a7f1af42ddb4.mp4.m3u8')">

                                             <%--【观看演示视频】--%> </a>
                                        </span>

                                        <span class="tip-tw">
                                          1.如何进行公文流转？
                                        </span>
                                        <div>
                                            <pre>进入“学校管理”页面的“公文流转”，点击“发布新公文流转”即可以文字、语音、附件的形式发布公文，并在通讯录中选择接收公文流转的人群。</pre>
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/tip-gwlz.png" alt="" class="I_I" width="750">
                                        </div>
                                        <span class="tip-tw">
                                          2.如何查看任课班级学生的总体学习情况？
                                        </span>
                                        <div>
                                            <pre>进入“学校管理”页面的“任课班级”，点击班级列表中某一任课班级，即可在“班级人员”页面下的学生列表中查看该班级学生在线总体学习情况，如：看完视频数、做题数。还可点击“导出信息”以把学生的学习情况导出为表格，或者“导入学号及班干部”信息。</pre>
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/tip-dcxx.png" alt="" class="I_I" width="750">
                                        </div>
                                        <span class="tip-tw">
                                          3.如何对学生进行考勤管理？
                                        </span>
                                        <div>
                                            <pre>进入“学校管理”页面的“任课班级”，点击班级列表中某一任课班级，进入“班级考勤”页面，在学生列表中选择缺勤的学生姓名，即可记录该学生的缺勤日期、时间及做相关备注，点击“添加”即可完成，同时也可以删除缺勤记录。</pre>
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/tip-kqgl.png" alt="" class="I_I" width="750">
                                        </div>
                                        <span class="tip-tw">
                                          4.如何统计班内学生的选课情况？
                                        </span>
                                        <div>
                                            <pre>进入“学校管理”页面的“任课班级”，点击班级列表中某一任课班级，进入“选课去向”页面，即可查看该班学生的拓展课选课去向，点击“导出信息”导出记录该信息的Excel表格。</pre>
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/tip-xkqk.png" alt="" class="I_I" width="750">
                                        </div>
                                        <span class="tip-tw">
                                          5.如何给学生增加素质教育经验值？
                                        </span>
                                        <div>
                                            <pre>进入“学校管理”页面的“任课班级”，在班级列表中点击某一任课班级后面的“经验值加分”，进入打分页面，系统默认给每个学生加1分（表现较好），可以直接确定给分，可以屡次加分。其中：“表现欠佳”：系统默认加0分；“表现较好”：系统默认加1分；“表现优异”：系统默认加2分。</pre>
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/tip-bxyy.png" alt="" class="I_I" width="750">
                                        </div>
                                         <span class="tip-tw">
                                           6.如何进行奖惩管理？
                                        </span>
                                        <div>
                                            <pre>进入“学校管理”页面的“表彰违纪”，即可添加、修改、删除学生奖惩记录，还可根据条件筛选查询过往奖惩记录，点击“导出”导出记录全校学生奖惩记录的Excel表格。</pre>
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/tip-jcgl.png" alt="" class="I_I" width="750">
                                        </div>
                                         <span class="tip-tw">
                                          7.如何进行考务管理？
                                        </span>
                                        <div>
                                            <pre>进入“学校管理”页面的“考务管理”。选择相应的年级和输入方式，点击录入选择班级和学科进行成绩录入，也可以采用模板一次性导入数据。成绩录入后也可以导出Exce表格。</pre>
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/tip-cjlr.png" alt="" class="I_I" width="750">
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/tip-cjlr2.png" alt="" class="I_I" width="750">
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/tip-cjlr3.png" alt="" class="I_I" width="750">
                                        </div>
                                        <span class="tip-tw">
                                          8.如何进行报修管理？
                                        </span>
                                        <div>
                                            <pre>进入“学校管理”页面的“报修管理”，在“报修申请”中填写好信息即可提交报修申请，在“报修情况查询”中查询已报修项目的进展情况，管理员可在“报修处理”中派发报修任务给指定人员。</pre>
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/tip-bxsq.png" alt="" class="I_I" width="750">
                                        </div>
                                        <span class="tip-tw">
                                         9.如何查看工资信息？
                                        </span>
                                        <div>
                                            <pre>进入“学校管理”页面的“工资管理”，教师可以选择时间查看每月的工资情况。</pre>
                                        </div>
                                        <div class="tip-uim">
                                            <img src="/img/userhelp/images/tip-gzcx.png" alt="" class="I_I" width="750">
                                        </div>
                                    </li>
                                    <%--<li>
                                        <span class="tip-title">11、如何使用录课神器录制微课？<a
                                                onclick="playMovie('http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/55b8760d0cf2a7f1af42ddb7.mp4.m3u8')">

                                            【观看演示视频】 </a>
                                        </span>

                                        <div>
                                            <pre>按照10进入课程编辑页面，点击“录制微课”或者录课神器图标即可下载安装K6KT录课神器，安装完成后再次点击“录制微课”即可启动。点击右上角绿色的“点击录制”圆形图标开始录课，再次点击停止录课。录制完成后可回看微课，保存并上传或者再录。</pre>
                                        </div>
                                        <img src="/img/userhelp/images/I_lzwk.jpg" alt="" class="I_I">
                                        <img src="/img/userhelp/images/I_lzwk2.jpg" alt="">
                                        <img src="/img/userhelp/images/I_lzwk3.jpg" alt="">
                                    </li>
                                    <li>
                                        <span class="tip-title">12、可以上传哪些学习资源供学生进行自主学习？<a
                                                onclick="playMovie('http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/55b876c70cf2a7f1af42ddba.mp4.m3u8')">

                                            【观看演示视频】 </a>
                                        </span>

                                        <div>
                                            <pre>按照10进入课程编辑页面，可以上传视频、辅助学习资料以及课后练习供学生进行自主习。</pre>
                                        </div>
                                        <img src="/img/userhelp/images/I_zzxx.jpg" alt="" class="I_I">
                                    </li>
                                    <li name="teacherScore" id="teacherScore">
                                         <span class="tip-title">13、如何配置课程的微课进阶练习？<a
                                                 onclick="playMovie('7sbrbl.com1.z0.glb.clouddn.com/m3u8/55b877a20cf2a7f1af42ddc0.mp4.m3u8')">

                                             【观看演示视频】 </a>
                                        </span>

                                        <div>
                                            <pre>按照10进入课程编辑页面，在“微课进阶练习”中填写习题名称，选择一份习题文档（doc或者docx格式）和解析文档（doc或者docx格式；可选），点击“上传并配置”进入配置页面。在配置页面中设置考试时间，题目数量，每题的题型、分值、答案等参数，最后点击右上角“完成并发布”即可。</pre>
                                        </div>
                                        <img src="/img/userhelp/images/I_khlx1.jpg" alt="" class="I_I">
                                        <img src="/img/userhelp/images/I_khlx2.jpg" alt="">
                                    </li>
                                    <li>
                                        <span class="tip-title">14、编辑微课进阶练习时，上传解析与不上传解析有什么区别？&lt;%&ndash;<a
                                            onclick="playMovie('http://player.polyv.net/videos/1a9bb5bed5b50e70aecfb26b31ec14d3_1.swf')"
                                            class="vedio">&lt;%&ndash;【观看演示视频】&ndash;%&gt;</a>&ndash;%&gt;</span>

                                        <div>
                                            <pre>若上传解析，学生在交卷后即刻可查看解析文档，知晓正确答案。</pre>
                                        </div>
                                    </li>
                                    <li>
                                         <span class="tip-title">15、如何将课程从备课空间推送到班级课程？推送到校本资源？<a
                                                 onclick="playMovie('http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/55b878b60cf2a7f1af42ddc9.mp4.m3u8')">

                                             【观看演示视频】 </a>
                                         </span>

                                        <div>
                                            <pre>方式一，在课程编辑页面中找到“推送”，在“班级课程”下和“校本资源”下选择要推送的目标文件夹。</pre>
                                        </div>
                                        <img src="/img/userhelp/images/I_bjkh1.jpg" alt="" class="I_I">

                                        <div>
                                            <pre>方式二，在“备课空间”页面下，点击某一课程下方的“推送到班级课程”可以将该课程推送给该班级的所有同学，点击某一课程下方的“推送到校本资源”可以将该课程推送给全校的其他老师。</pre>
                                        </div>
                                        <img src="/img/userhelp/images/I_bjkh2.jpg" alt="" class="I_I">
                                    </li>
                                    <li>
                                        <span class="tip-title">16、如何使用在线备课空间？<a
                                                onclick="playMovie('http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/55b879260cf2a7f1af42ddcc.mp4.m3u8')">

                                            【观看演示视频】 </a></span>

                                        <div>
                                            <pre>A.每位老师可享受“在线备课空间”无限容量，位于“学习中心”页面的“翻转课堂”页面。</pre>
                                        </div>
                                        <div>
                                            <pre>B.“备课空间”是老师所有教学课程的“中转站”。</pre>
                                        </div>
                                        <div>
                                            <pre>C.老师可以从“云课程”推送优质课件到自己的“备课空间”，或者上传自己录制的“微课”到“备课空间”留存。</pre>
                                        </div>
                                        <div>
                                            <pre>D.老师在“备课空间”里自己定义目录进行分类课件管理，并且选择这里的课件，推送到“班级课程”供本班学生使用，或者推送到“校本资源”供全校老师参考使用。</pre>
                                        </div>
                                    </li>
                                    <li>
                                         <span class="tip-title">17、如何查看某一课程的学习统计？<a
                                                 onclick="playMovie('http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/55b879840cf2a7f1af42ddcf.mp4.m3u8')">

                                             【观看演示视频】 </a>
                                         </span>

                                        <div>
                                            <pre>进入“学习中心”页面的“翻转课堂”，点击“班级课程”，点击某一课程下方的“统计”，即可查看该课程所含视频的学习统计及听取学生留言、查看该课程微课进阶练习的习题统计并对主观题进行批改。</pre>
                                        </div>
                                        <img src="/img/userhelp/images/I_kctj1.jpg" alt="" class="I_I">
                                        <img src="/img/userhelp/images/I_kctj2.jpg" alt="">
                                        <img src="/img/userhelp/images/I_kctj3.jpg" alt="">
                                    </li>
                                    <li>
                                         <span class="tip-title">18、如何发布在线考试？批改在线考试？<a
                                                 onclick="playMovie('http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/55b879e30cf2a7f1af42ddd2.mp4.m3u8')">

                                             【观看演示视频】 </a>
                                         </span>

                                        <div>
                                            <pre>进入“学习中心”页面的“考试”，在“上传考卷”中发布在线考试，操作与发布课程微课进阶练习完全相同，具体请参见13。对于已有学生答题的在线考试，点击“批改试卷”即可进行批改。</pre>
                                        </div>
                                        <img src="/img/userhelp/images/I_zxks.jpg" alt="" class="I_I">
                                    </li>
                                    <li>
                                         <span class="tip-title">19、如何使用K6KT的题库资源在线组卷？<a
                                                 onclick="playMovie('7sbrbl.com1.z0.glb.clouddn.com/m3u8/55b87a6e0cf2a7f1af42ddd5.mp4.m3u8')">

                                             【观看演示视频】 </a>
                                         </span>

                                        <div>
                                            <pre>一、手工组卷</pre>
                                        </div>
                                        <div>
                                            <pre>进入“学习中心”的“题库/组卷”点击“手工组卷”，完整填写本次考试的基本信息后点击“下一步”进入选题页面，点击“综合知识点选题”并通过右边的“请选择知识点”筛选出符合本次测试的题目，也可以点击“同步教材选题”从页面右边的“选择章节”按钮根据出版社筛选题库。然后根据“题型”和“难度”将题目“加入试卷”，题目添加完成后点击“确定生成”试卷，再点击“推送”到班级，学生即可在线考试。同时在“我的组卷”列表中可以对已发布的试卷进行编辑、预览、删除。</pre>
                                        </div>
                                        <img src="/img/userhelp/images/I-ZJ.jpg" alt="" class="I_I">
                                        <img src="/img/userhelp/images/I-ZJ-2.jpg" alt="" class="I_I">
                                        <img src="/img/userhelp/images/I-ZJ-3.jpg" alt="" class="I_I">

                                        <div>
                                            <pre> 二、智能组卷</pre>
                                        </div>
                                        <div>
                                            <pre>进入“学习中心”的“题库/组卷”点击“智能组卷”，完整填写本次考试的基本信息后点击“下一步”进入选题页面，根据页面提示选择本次测试的教材版本、考试知识点、每道大题中包含小题的数量，系统即可智能生成一份试卷，最后点击“推送”按钮，学生就可以在线考试。同时在“我的组卷”列表中可以对已发布的试卷进行编辑、预览、删除。</pre>
                                        </div>
                                        <img src="/img/userhelp/images/I-ZJ-5.jpg" alt="" class="I_I">
                                    </li>
                                    <li>
                                         <span class="tip-title">20、如何录入一份考试成绩<a
                                                 onclick="playMovie('7sbrbl.com1.z0.glb.clouddn.com/m3u8/55b87ae00cf2a7f1af42ddd8.mp4.m3u8')">

                                             【观看演示视频】 </a>
                                         </span>

                                        <div>
                                            <pre>进入“学习中心”的“成绩分析”，点击页面右上角的“成绩录入”并依次填充本次考试的基本信息即可完成新建，在页面下面列表中找到刚新建的考试，点击“打分”即可录入学生的考试成绩。</pre>
                                        </div>
                                        <img src="/img/userhelp/images/I-kscj.jpg" alt="" class="I_I">
                                    </li>
                                    <li>
                                        <span class="tip-title">21、如何查看本班级考试成绩的统计分析？<a
                                                onclick="playMovie('http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/55b87b710cf2a7f1af42dddb.mp4.m3u8')">

                                            【观看演示视频】 </a>
                                         </span>

                                        <div>
                                            <pre>进入“学习中心”的“成绩分析”的首页，选择需要查看的班级名称和考试名称即可了解本年级所有班级的“班级成绩对比”、本班的本次考试的“成绩分布”、“成绩统计”、“学生列表”等信息。</pre>
                                        </div>
                                        <img src="/img/userhelp/images/I-tjfx.jpg" alt="" class="I_I">
                                        <div>
                                            <pre> 同时进入“学期成绩”亦可了解一个学期本年级所有班级的“班级成绩对比”、本班本学期的“成绩统计”和“学生列表”</pre>
                                        </div>
                                        <img src="/img/userhelp/images/I-tjfx2.jpg" alt="" class="I_I">
                                    </li>
                                    <li>
                                        <span class="tip-title">22、如何了解所带班级的学生的德育情况？<a
                                                onclick="playMovie('http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/55b87bfc0cf2a7f1af42ddde.mp4.m3u8')">

                                            【观看演示视频】 </a>
                                         </span>

                                        <div>
                                            <pre>进入“学习中心”的“德育”并点击“管理首页”，通过筛选学期、年级、班级，即可了解该班级的德育统计情况，点击某一个学生名字可查看该学生近期德育的“个人成绩”雷达图。
                                        </pre>
                                        </div>
                                        <img src="/img/userhelp/images/I-dyqk1.jpg" alt="" class="I_I">
                                        <img src="/img/userhelp/images/I-dyqk2.jpg" alt="" class="I_I">
                                    </li>
                                    <li>
                                        <span class="tip-title">23、如何将班级学生的学号和职务导出或导入系统？
                                            <a onclick="playMovie('http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/55b87c850cf2a7f1af42dde1.mp4.m3u8')">【观看演示视频】</a>
                                        </span>

                                        <div>
                                            <pre> 进入“我的班级”，选择“班级列表”下的任一班级，即可看到该班级的老师列表和学生列表。点老师列表右上方的“导出信息”即可导出一份包含学生姓名、在线学习统计、学号、职务的列表。同时点击“导入学号及班干部”根据系统提示下载模板，在模板表格中添加本班学生的学号和职务信息，即可完成信息的添加。
                                            </pre>
                                        </div>
                                        <img src="/img/userhelp/images/I-drdc.jpg" alt="" class="I_I">
                                    </li>
                                    <li>
                                         <span class="tip-title">24、如何在系统中记录学生缺勤情况？<a
                                                 onclick="playMovie('http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/55b87d130cf2a7f1af42dde4.mp4.m3u8')">

                                             【观看演示视频】 </a>
                                         </span>

                                        <div>
                                            <pre>进入“我的班级”，点击“班级考勤”，页面下方的学生列表中，选中缺勤的学生姓名，记录该学生的缺勤日期、时间或者做相关备注，点击“添加”即可完成，同时也可以删除缺勤记录。</pre>
                                        </div>
                                        <img src="/img/userhelp/images/I-qqqk.jpg" alt="" class="I_I">
                                    </li>
                                    <li>
                                         <span class="tip-title">25、如何获取班级学生在线学习情况？<a
                                                 onclick="playMovie('http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/55b87da80cf2a7f1af42dde7.mp4.m3u8')">

                                             【观看演示视频】 </a>
                                         </span>

                                        <div>
                                            <pre>进入“我的班级”，在班级列表中选择想要了解的班级名称班级，在学生列表中即可查看该班级学生在线做题和看视频数量。</pre>
                                        </div>
                                        <img src="/img/userhelp/images/I-zxxx.png" alt="" class="I_I">
                                        <img src="/img/userhelp/images/I-zxxx2.png" alt="" class="I_I">
                                    </li>
                                    <li>
                                        <span class="tip-title">26、如何给学生增加素质教育经验值？<a
                                                onclick="playMovie('http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/55b87e5f0cf2a7f1af42ddea.mp4.m3u8')">

                                            【观看演示视频】 </a>
                                         </span>

                                        <div>
                                            <pre>进入“我的班级”，在班级列表中选择任教班级，点击“学生经验值加分”，进入打分页面，系统默认给每个学生加1分（表现较好），可以直接确定给分，可以屡次加分。其中：
“表现欠佳”：系统默认加0分；
“表现较好”：系统默认加1分；
“表现优异”：系统默认加2分。</pre>
                                        </div>
                                        <img src="/img/userhelp/images/I_szjy1.jpg" alt="" class="I_I">
                                        <img src="/img/userhelp/images/I_szjy2.jpg" alt="" class="I_I">
                                    </li>
                                    <li>
                                        <span class="tip-title">27、老师如何获得经验值？<a
                                                onclick="playMovie('http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/55b87ec00cf2a7f1af42dded.mp4.m3u8')">

                                            【观看演示视频】 </a>
                                         </span>

                                        <div>
                                            <pre>
途径1，完善个人资料。首次上传头像可获得1分，首次修改密码可获得1分。
途径2，每日首次发帖，每天的第一次发帖加1分。
途径3，推送自制课程（视频或课件或习题）到校本资源加1分。
途径4，推送自制课程（视频或课件或习题）到班级课程加1分（推送到多个班级各得1分）。
途径5，推送云课程到班级课程加1分（推送到多个班级各得1分）。
途径6，上传1套在线试卷加5分。
途径7，通知、作业每发布一次到每一个班级，加1分。
                                            </pre>
                                        </div>
                                    </li>
                                    <li>
                                         <span class="tip-title">28、如何对才艺课的学生进行打分以及管理？<a
                                                 onclick="playMovie('http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/55b87f2e0cf2a7f1af42ddf0.mp4.m3u8')">

                                             【观看演示视频】 </a>
                                         </span>

                                        <div>
                                            <pre>从导航栏进入“才艺社/拓展课”，在班级列表里，可以看到您管理的才艺课，点击该课程右侧的“课程评价”按钮，点击“添加课堂表现”，可对所有学生的课堂表现分课时进行打分；点击“课程总评”，对所有学生进行总评并填写评语。</pre>
                                        </div>
                                        <img src="/img/userhelp/images/I-cygl1.png" alt="" class="I_I">
                                        <img src="/img/userhelp/images/I-cygl2.png" alt="">
                                    </li>
                                    <li>
                                        <span class="tip-title">
                                            29、如何邀请好友一起组织活动？
                                            <a onclick="playMovie('http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/55b87fa00cf2a7f1af42ddf3.mp4.m3u8')"></a>
                                        </span>

                                        <div>
                                            <pre>进入“好友圈”点击“发起活动”，并填写活动的规则以及这次活动公开的范围。还可以分类查看“好友圈”里活动，比如：最近动态、好友活动、推进活动、我参加的、我发起的。</pre>
                                        </div>
                                        <img src="/img/userhelp/images/I_hyhd.jpg" alt="" class="I_I">
                                    </li>
                                    <li>
                                        <span class="tip-title">
                                            30、如何组建一个群组进行交流？
                                        </span>

                                        <div>
                                            <pre>进入“群组交流”点击“新建群组”，输入群组名称后，可以点击“我的好友”左边的三角形按键逐个添加好友成员，也可以点击“我的好友”右边的加号一次将“我的好友”全部添加进入群组进行交流。同时可以在群组里发表情、共享文件、查看历史聊天记录、管理群组成员、或者解散该群组。</pre>
                                        </div>
                                        <img src="/img/userhelp/images/I-qzjl.jpg" alt="" class="I_I">
                                    </li>
                                    <li>
                                        <span class="tip-title">
                                            31、如何发起投票？查看投票结果？
                                            <a onclick="playMovie('http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/55b8803c0cf2a7f1af42ddf6.mp4.m3u8')"></a>
                                        </span>

                                        <div>
                                            <pre>进入“投票选举”，即可编辑并发布投票选举，包括指定投票公开范围、参选角色、指定候选人、投票角色、投票时间等。同样在“投票选举”中可查看所有投票的结果。</pre>
                                        </div>
                                        <img src="/img/userhelp/images/I-fqtp.png" alt="" class="I_I">
                                        <img src="/img/userhelp/images/I-fqtp2.png" alt="" class="I_I">
                                    </li>
                                    <li>
                                        <span class="tip-title">
                                            32、如何发起问卷调查？
                                            <a onclick="playMovie('http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/55b880b50cf2a7f1af42ddf9.mp4.m3u8')"></a>
                                        </span>

                                        <div>
                                            <pre>进入“调查问卷”，点击右上方的“新建问卷”进入问卷编辑页面，上传问卷文档并对问卷进行相关配置后，点击“完成并发布”即可。</pre>
                                        </div>
                                        <img src="/img/userhelp/images/I-fqwj1.png" alt="" class="I_I">
                                        <img src="/img/userhelp/images/I-fqwj2.png" alt="" class="I_I">
                                    </li>
                                    <li>
                                        <span class="tip-title">
                                            33、如何参加K6KT平台运营活动赢取大奖或现金？
                                            <a onclick="playMovie('http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/55b881fd0cf2a7f1af42ddfc.mp4.m3u8')"></a>
                                        </span>

                                        <div>
                                            <pre>进入“调查问卷”，点击右上方的“新建问卷”进入问卷编辑页面，上传问卷文档并对问卷进行相关配置后，点击“完成并发布”即可。</pre>
                                        </div>
                                        <img src="/img/userhelp/images/I-xjdj.jpg" alt="" class="I_I">
                                    </li>
                                    <li>
                                        <span class="tip-title">
                                            34、如何发布一条校园安全隐患警示？
                                            <a onclick="playMovie('http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/55b882590cf2a7f1af42ddff.mp4.m3u8')"></a>
                                        </span>

                                        <div>
                                            <pre>进入“校园安全”，在文本框中输入说明，还可以上传安全隐患的图片，点击“提交”即可，同时可以删除本条消息。其他人发布的隐患消息，如果已经被清除隐患，点击“处理”即可。</pre>
                                        </div>
                                        <img src="/img/userhelp/images/I-aqjs.jpg" alt="" class="I_I">
                                    </li>
                                    <li>
                                        <span class="tip-title">
                                            35、如何在平板电脑及手机移动终端，使用K6KT-快乐课堂？
                                            <a onclick="playMovie('http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/55b882e60cf2a7f1af42de02.mp4.m3u8')"></a>
                                        </span>

                                        <div>
                                            <pre> A.安卓和苹果系统如何下载APP软件 ？
通过浏览器打开www.k6kt.com，点击“手机客户端APP”，扫描二维码安装。或者在APP Store里搜索“K6KT”安装。
                                            </pre>
                                        </div>
                                        <img src="/img/userhelp/images/I_APP.jpg" alt="" class="I_I">

                                        <div>
                                            <pre>B.利用移动终端，我们都能做些什么？
  *可以随时接收和在线提交学校发布的家庭作业
  *可以随时接收学校发布的各类通知
  *可以随时和学校老师家长进行私信交流，掌握孩子动态
  *可以随时拍照，上传到“微校园”进行分享
  *随时随地，家校互动，在线学习，虚拟社区全分享。
                                            </pre>
                                        </div>
                                    </li>--%>
                                </ul>
                            </div>
                            <!-- teacher end -->

                            <!-- parent start -->
                            <div id="parent-help-container" class="help-container">
                                <div style="font-size: 20px;font-weight: bold;overflow: visible;width:100%;height: 40px;line-height: 40px;text-align: center">
                                    亲爱的家长，您好！感谢您使用K6KT-快乐课堂平台！
                                </div>
                                <ul>
                                    <li>
                                        <span class="tip-title">1、如何登陆全向互动K6KT-快乐课堂？<%--<a onclick="playMovie('http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/user/1429690227633lkYsnk.m3u8')">
                                            【观看演示视频】 </a>--%>
                                        </span>

                                        <div>
                                            <pre>用户登录<a href="http://www.k6kt.com" target="_blank">www.k6kt.com</a>, 在页面右上方填入学校分配的用户名与密码，即可登录全向互动K6KT-快乐课堂。</pre>
                                        </div>
                                        <img src="/img/userhelp/images/I_dl.jpg" alt="" class="I_I">
                                    </li>
                                    <li>
                                        <span class="tip-title">2、如何更改完善个人头像？<%--<a onclick="playMovie('http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/user/1429690385784HDvRnt.m3u8')">
                                            【观看演示视频】 </a>--%>
                                        </span>

                                        <div>
                                            <pre>登录后，点击头像右侧的笔形图标，即可进入个人设置，点击“修改头像”下的“点击上传图片”即可上传已有照片或者拍摄照片来修改头像。</pre>
                                        </div>
                                        <img src="/img/userhelp/images/I_tx.jpg" alt="" class="I_I">
                                    </li>
                                    <li>
                                        <span class="tip-title">3、如何修改个人密码？<%--<a onclick="playMovie('http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/user/1429868553456eKNcmT.m3u8')">
                                            【观看演示视频】 </a>--%>
                                        </span>

                                        <div>
                                            <pre>登录后，点击头像右侧的笔形图标，进入个人设置，点击“修改密码”即可操作。</pre>
                                        </div>
                                        <img src="/img/userhelp/images/I_xgmm.jpg" alt="" class="I_I">
                                    </li>
                                    <li>
                                        <span class="tip-title">4、如何发送私信？<%--<a onclick="playMovie('http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/user/1429868601984VwbIEk.m3u8')">
                                            【观看演示视频】 </a>--%>
                                        </span>

                                        <div>
                                            <pre>进入个人中心的“我的私信”，点击“发私信”，选择收信人名单，即可与学生、家长、老师、校领导进行私信交流。</pre>
                                        </div>
                                        <img src="/img/userhelp/images/I_fsx.jpg" alt="" class="I_I">
                                    </li>
                                    <li>
                                        <span class="tip-title">5、如何使用微家园、微校园社区发帖和评论?<%--<a onclick="playMovie('http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/user/1429868670765WjYvHh.m3u8')">
                                            【观看演示视频】 </a>--%>
                                        </span>

                                        <div><pre>在“家校互动”页面点击“微家园”，即可发表自己的新鲜事或者对别人的新鲜事进行留言、点赞，与他人进行公开交流互动，还可选择帖子发表范围：本校、本年级或者本班，以及查看最新、最热和自己发的帖子。
在“家校互动”页面点击“微校园”即可查看老师们发的帖子并进行留言和点赞。
                                        </pre>
                                        </div>
                                        <img src="/img/userhelp/images/I_dz.jpg" alt="" class="I_I">
                                    </li>
                                    <li>
                                        <span class="tip-title">6、如何查看通知？<%--<a onclick="playMovie('http://player.polyv.net/videos/1a9bb5bed592ff8a0255bb306c2bae45_1.swf')" class="vedio">&lt;%&ndash;【观看演示视频】&ndash;%&gt;</a>--%></span>

                                        <div>
                                            <pre>在“家校互动”页面点击“通知”，即可查看所有通知，最新的通知排列在最上方。</pre>
                                        </div>
                                        <img src="/img/userhelp/images/I_tz.jpg" alt="" class="I_I">
                                    </li>
                                    <li>
                                        <span class="tip-title">7、如何查看孩子的作业情况？</span>

                                        <div>
                                            <pre>在“家校互动”页面点击“作业”，再点击老师布置的某一作业，即可查看老师布置的文本、语音、附件形式发布的作业和班级其他学生答题列表。</pre>
                                        </div>
                                        <img src="/img/userhelp/images/I_zy2.jpg" alt="" class="I_I">
                                    </li>
                                    <li>
                                        <span class="tip-title">8、如何观看云课程？</span>

                                        <div>
                                            <pre>进入“学习中心”页面的“翻转课堂”，点击“云课程”可从中根据条件选择视频课件，点击该视频下方“试看”观看该云课程。</pre>
                                        </div>
                                        <img src="/img/userhelp/images/I_ykc.jpg" alt="" class="I_I">
                                    </li>
                                    <li>
                                        <span class="tip-title">9、如何查看老师推送的课程？</span>

                                        <div>
                                            <pre>进入“学习中心”页面的“翻转课堂”，点击“班级课程”，可以看到老师最新上传的课程。点击进入课程播放，可观看老师推送的课程，并可以在讨论区可看到孩子和与老师、同学之间交流。在右侧的“课件列表”可下载老师的课件。</pre>
                                        </div>
                                        <img src="/img/userhelp/images/I_bjkc.jpg" alt="" class="I_I">
                                    </li>
                                    <li>
                                        <span class="tip-title">10、如何查看孩子的考试情况？</span>

                                        <div>
                                            <pre>进入“学习中心”页面的“考试”，在“我的考试”页面可以查看所有任课老师发布的考试列表。</pre>
                                        </div>
                                        <img src="/img/userhelp/images/I_ckks.jpg" alt="" class="I_I">
                                    </li>
                                    <li>
                                        <span class="tip-title">11、如何购买和查看全国优秀教师们分享的课程？<a
                                                onclick="playMovie('http://player.polyv.net/videos/1a9bb5bed5b7da019e81ea04e4e9d138_1.swf')"
                                                class="vedio"><%--【观看演示视频】--%></a></span>

                                        <div>
                                            <pre>进入“电子超市”的“电子超市首页”可根据条件筛选课程，筛选好课程后点击该课程，并点击“我要购买”，通过K6KT账户余额或者支付宝支付。同时可以在“个人账户”里管理订单。</pre>
                                        </div>
                                        <img src="/img/userhelp/images/I_fxkc1.jpg" alt="" class="I_I">
                                        <img src="/img/userhelp/images/I_fxkc2.jpg" alt="">
                                    </li>
                                    <li>
                                        <span class="tip-title">12、如何了解本班级的学生经验值？</span>

                                        <div>
                                            <pre>进入“我的班级”，即可查看该班级所有同学的经验值情况。</pre>
                                        </div>
                                        <img src="/img/userhelp/images/I_jyz.jpg" alt="" class="I_I">
                                    </li>
                                    <li>
                                        <span class="tip-title">13、如何了解孩子的经验值明细？</span>

                                        <div>
                                            <pre>登入孩子的账号，在任意页面，点击头像栏右侧的宠物小精灵，点击“历史积分”即可弹出孩子的经验值组成明细。</pre>
                                        </div>
                                        <img src="/img/userhelp/images/I_jyz2.jpg" alt="" class="I_I">
                                    </li>
                                    <li>
                                        <span class="tip-title">14、如何邀请好友一起组织活动？<a
                                                onclick="playMovie('http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/user/1429869685181UAUcyC.m3u8')">
                                            【观看演示视频】 </a>
                                        </span>

                                        <div>
                                            <pre>进入“好友圈”点击“发起活动”，并填写活动的规则以及这次活动公开的范围。还可以分类查看“好友圈”里活动，比如：最近动态、好友活动、推进活动、我参加的、我发起的。</pre>
                                        </div>
                                        <img src="/img/userhelp/images/I_hyhd.jpg" alt="" class="I_I">
                                    </li>
                                    <li>
                                        <span class="tip-title">15、如何组建一个群组进行交流？<a
                                                onclick="playMovie('http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/user/1429869766830UtLIRH.m3u8')">
                                            【观看演示视频】 </a>
                                        </span>

                                        <div>
                                            <pre>进入“群组交流”点击“新建群组”，输入群组名称后，可以点击“我的好友”等组左边的三角形按键逐个添加好友成员，也可以点击“我的好友”等组右边的加号一次将该组全部成员添加进入群组进行交流。同时可以在群组里发表情、共享文件、查看历史聊天记录、管理群组成员、或者退出该群组。</pre>
                                        </div>
                                        <img src="/img/userhelp/images/I_qzjl.jpg" alt="" class="I_I">
                                    </li>
                                    <li>
                                        <span class="tip-title">16、如何查看孩子班上的投票结果？</span>

                                        <div>
                                            <pre>进入“投票选举”即可查看所有的投票选举列表。</pre>
                                        </div>
                                        <img src="/img/userhelp/images/I_tpjg.jpg" alt="" class="I_I">
                                    </li>
                                    <li>
                                        <span class="tip-title">17、如何参与学校发起的问卷调查？</span>

                                        <div>
                                            <pre>进入“问卷调查”，点击某份问卷后的“填写问卷”，进入问卷答题，勾选正确的答案后，点击“提交”即可。</pre>
                                        </div>
                                        <img src="/img/userhelp/images/I_cydc.jpg" alt="" class="I_I">
                                    </li>
                                    <li>
                                        <span class="tip-title">18、如何参加K6KT平台运营活动赢取大奖或现金？</span>

                                        <div>
                                            <pre>进入“火热活动”即可查看活动列表，点击其中一个活动，查看活动规则，即可参加并赢取奖品或现金。</pre>
                                        </div>
                                        <img src="/img/userhelp/images/I_hrhd.jpg" alt="" class="I_I">
                                    </li>
                                    <li>
                                        <span class="tip-title">19、如何在平板电脑及手机移动终端，使用K6KT-快乐课堂？</span>

                                        <div>
                                            <pre>A.安卓和苹果系统如何下载APP软件 ？
通过浏览器打开<a href="http://www.k6kt.com" target="_blank">www.k6kt.com</a>，点击“手机客户端APP”，扫描二维码安装。或者在APP Store里搜索“K6KT”安装。
                                            </pre>
                                        </div>
                                        <img src="/img/userhelp/images/I_APP.jpg" alt="" class="I_I">

                                        <div>
                                            <pre>B.利用移动终端，我们都能做些什么？
  *可以随时接收和在线提交学校发布的家庭作业
  *可以随时接收学校发布的各类通知
  *可以随时和学校老师家长进行私信交流，掌握孩子动态
  *可以随时拍照，上传到“微校园”进行分享
  *随时随地，家校互动，在线学习，虚拟社区全分享。
                                            </pre>
                                        </div>
                                    </li>
                                </ul>
                            </div>
                            <!-- parent end -->

                            <!-- student start -->
                            <div id="student-help-container" class="help-container">
                                <div style="font-size: 20px;font-weight: bold;overflow: visible;width:100%;height: 40px;line-height: 40px;text-align: center;">
                                    亲爱的同学，你好！感谢你使用K6KT-快乐课堂平台！
                                </div>
                                <ul>
                                    <li>
                                        <span class="tip-title">1、如何登陆全向互动K6KT-快乐课堂？<%--<a onclick="playMovie('http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/user/1429690227633lkYsnk.m3u8')">
                                            【观看演示视频】 </a>--%>
                                        </span>

                                        <div>
                                            <pre>用户登录<a href="http://www.k6kt.com" target="_blank">www.k6kt.com</a>, 在页面右上方填入学校分配的用户名与密码，即可登录全向互动K6KT-快乐课堂。</pre>
                                        </div>
                                        <img src="/img/userhelp/images/I_dl.jpg" alt="" class="I_I">
                                    </li>
                                    <li>
                                        <span class="tip-title">2、如何更改完善个人头像？<%--<a onclick="playMovie('http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/user/1429690385784HDvRnt.m3u8')">
                                            【观看演示视频】 </a>--%>
                                        </span>

                                        <div>
                                            <pre>登录后，点击头像右侧的笔形图标，即可进入个人设置，点击“修改头像”下的“点击上传图片”即可上传已有照片或者拍摄照片来修改头像。</pre>
                                        </div>
                                        <img src="/img/userhelp/images/I_tx.jpg" alt="" class="I_I">
                                    </li>
                                    <li>
                                        <span class="tip-title">3、如何修改个人密码？<%--<a onclick="playMovie('http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/user/1429868553456eKNcmT.m3u8')">
                                              【观看演示视频】 </a>--%>
                                        </span>

                                        <div>
                                            <pre>登录后，点击头像右侧的笔形图标，进入个人设置，点击“修改密码”即可操作。</pre>
                                        </div>
                                        <img src="/img/userhelp/images/I_xgmm.jpg" alt="" class="I_I">
                                    </li>
                                    <li>
                                        <span class="tip-title">4、如何发送私信？<%--<a onclick="playMovie('http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/user/1429868601984VwbIEk.m3u8')">
                                            【观看演示视频】 </a>--%>
                                        </span>

                                        <div>
                                            <pre>进入个人中心的“我的私信”，点击“发私信”，选择收信人名单，即可与学生、家长、老师、校领导进行私信交流。</pre>
                                        </div>
                                        <img src="/img/userhelp/images/I_fsx.jpg" alt="" class="I_I">
                                    </li>
                                    <li>
                                         <span class="tip-title">5、如何使用微家园、微校园社区发帖和评论?<%--<a onclick="playMovie('http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/user/1429868670765WjYvHh.m3u8')">
                                             【观看演示视频】 </a>--%>
                                         </span>

                                        <div>
											<pre>在“家校互动”页面点击“微家园”，即可发表自己的新鲜事或者对别人的新鲜事进行留言、点赞，与他人进行公开交流互动，还可选择帖子发表范围：本校、本年级或者本班，以及查看最新、最热和自己发的帖子。
在“家校互动”页面点击“微校园”即可查看老师们发的帖子并进行留言和点赞。
											</pre>
                                        </div>
                                        <img src="/img/userhelp/images/I_dz.jpg" alt="" class="I_I">
                                    </li>
                                    <li>
                                        <span class="tip-title">6、如何查看通知？</span>

                                        <div>
                                            <pre>在首页的家校互动区，点击“我的通知”，即可查看所有通知，最新的通知排列在最方。</pre>
                                        </div>
                                        <img src="/img/userhelp/images/I_tz.jpg" alt="" class="I_I">
                                    </li>
                                    <li>
                                        <span class="tip-title">7、如何完成老师布置的作业？<a
                                                onclick="playMovie('http://player.polyv.net/videos/1a9bb5bed592ff8a0255bb306c2bae45_1.swf')"
                                                class="vedio"><%--【观看演示视频】--%></a></span>

                                        <div>
                                            <pre>进入“家校互动”点击“作业”，再点击老师布置的某一个作业，可以通过“语音”、“拍照”、“添加附件”的形式回复老师的作业，作业上传后点击“提交”即可。</pre>
                                        </div>
                                        <img src="/img/userhelp/images/I_wczy.jpg" alt="" class="I_I">
                                    </li>
                                    <li>
                                        <span class="tip-title">8、如何观看云课程？</span>

                                        <div>
                                            <pre>进入“学习中心”页面的“翻转课堂”，点击“云课程”可从中根据条件选择视频课件，点击该视频下方“试看”观看该云课程。</pre>
                                        </div>
                                        <img src="/img/userhelp/images/I_ykc.jpg" alt="" class="I_I">
                                    </li>
                                    <li>
                                        <span class="tip-title">9、如何查看老师推送的课程？<a
                                                onclick="playMovie('http://player.polyv.net/videos/1a9bb5bed5dae8269a406a7e95a8f739_1.swf')"
                                                class="vedio"><%--【观看演示视频】--%></a></span>

                                        <div>
                                            <pre>进入“学习中心”页面的“翻转课堂”，点击“班级课程”，可以看到老师最新上传的课程。点击进入课程播放，可观看老师推送的课件或者课后作业。在视频下方“去答题”即可开始答课后习题，在讨论区可与老师、同学进行交流。在右侧的“课件列表”可下载老师的课件，还可以学习系统自动推送的“相关课程”进行学习。</pre>
                                        </div>
                                        <img src="/img/userhelp/images/I_bjkc.jpg" alt="" class="I_I">
                                    </li>
                                    <li>
                                        <span class="tip-title">10、如何在线考试？<a
                                                onclick="playMovie('http://player.polyv.net/videos/1a9bb5bed579e0318fd04cb9e3bebf87_1.swf')"
                                                class="vedio"><%--【观看演示视频】--%></a></span>

                                        <div>
                                            <pre>进入“学习中心”页面的“考试”，在“我的考试”页面可以查看所有任课老师发布的考试列表，点击“去答题”即可开始答题，点击“查看批改”可以看到自己的考试结果。</pre>
                                        </div>
                                        <img src="/img/userhelp/images/I_qdt.jpg" alt="" class="I_I">
                                    </li>
                                    <li>
                                        <span class="tip-title">11、如何在K6KT平台上自我测试对知识点的掌握？</span>

                                        <div>
                                            <pre>进入“学习中心”页面的“题库”，根据学科、知识点、题型等筛选题目，还可以将答错的题目添加到“错题集”，在“错题集”中查遗补漏，反复练习。</pre>
                                        </div>
                                        <img src="/img/userhelp/images/I_gktk.jpg" alt="" class="I_I">
                                    </li>
                                    <li>
                                        <span class="tip-title">12、如何购买和查看全国优秀教师们分享的课程？<a
                                                onclick="playMovie('http://player.polyv.net/videos/1a9bb5bed54a3bb7f2f66851a89ff87c_1.swf')"
                                                class="vedio"><%--【观看演示视频】--%></a></span>

                                        <div>
                                            <pre>进入“电子超市”的“电子超市首页”可根据条件筛选课程，筛选好课程后点击该课程，并点击“我要购买”，通过K6KT账户余额或者支付宝支付。同时可以在“个人账户”里管理订单。</pre>
                                        </div>
                                        <img src="/img/userhelp/images/I_fxkc1.jpg">
                                        <img src="/img/userhelp/images/I_fxkc2.jpg" alt="" class="I_I">
                                    </li>
                                    <li>
                                        <span class="tip-title">13、如何了解本班级同学的经验值？<a
                                                onclick="playMovie('http://player.polyv.net/videos/1a9bb5bed57a9d74eb4caf58f6a00ffd_1.swf')"
                                                class="vedio"><%--【观看演示视频】--%></a></span>

                                        <div>
                                            <pre>进入“我的班级”，即可查看该班级所有同学的经验值情况。</pre>
                                        </div>
                                        <img src="/img/userhelp/images/I_jyz.jpg" alt="" class="I_I">
                                    </li>
                                    <li id="studentScore" name="studentScore">
                                        <span class="tip-title">14、如何了解自己的经验值明细？</span>

                                        <div>
                                            <pre>在任意页面，点击头像栏右侧的宠物小精灵，点击“历史积分”即可弹出自己的经验值组成明细。</pre>
                                        </div>
                                        <img src="/img/userhelp/images/I_jyz2.jpg" alt="" class="I_I">
                                    </li>
                                    <li>
                                        <span class="tip-title">15、如何获得经验值？</span>

                                        <div>
                				<pre>1)  学生通过在K6KT平台在线学习获得经验值：
	a）完善个人资料获取经验值：
	首次上传头像可获得1分，首次修改密码可获得1分。
	b）每日首次发帖获取经验值：
	每天的第一次发帖加1分
	c）观看课程视频获取经验值
	观看完一个教师推送的“班级课程”视频，加2分；
	观看完一个“云课程”视频，加1分。
	d)完成“班级课程”配套练习获取经验值：
	每做1套班级课程的课后练习题，加1分。
	e）完成“在线考试”获取经验值：
	提交完成1套“在线考试”试题，加5分
2)  学生通过老师评价，获得经验值</pre>
                                        </div>
                                    </li>
                                    <li>
                                        <span class="tip-title">16、如何邀请好友一起组织活动？<a
                                                onclick="playMovie('http://player.polyv.net/videos/1a9bb5bed5b7da019e81ea04e4e9d138_1.swf')"
                                                class="vedio"><%--【观看演示视频】--%></a></span>

                                        <div>
                                            <pre>进入“好友圈”点击“发起活动”，并填写活动的规则以及这次活动公开的范围。还可以分类查看“好友圈”里活动，比如：最近动态、好友活动、推进活动、我参加的、我发起的。</pre>
                                        </div>
                                        <img src="/img/userhelp/images/I_fqhd.jpg" alt="" class="I_I">
                                    </li>
                                    <li>
                                        <span class="tip-title">17、如何组建一个群组进行交流？<a
                                                onclick="playMovie('http://player.polyv.net/videos/1a9bb5bed5705bf52456a571c5ebf637_1.swf')"
                                                class="vedio"><%--【观看演示视频】--%></a></span>

                                        <div>
                                            <pre>进入“群组交流”点击“新建群组”，输入群组名称后，可以点击“我的好友”等组左边的三角形按键逐个添加好友成员，也可以点击“我的好友”等组右边的加号一次将该组全部成员添加进入群组进行交流。同时可以在群组里发表情、共享文件、查看历史聊天记录、管理群组成员、或者退出该群组。</pre>
                                        </div>
                                        <img src="/img/userhelp/images/I_qzjl.jpg" alt="" class="I_I">
                                    </li>
                                    <li>
                                        <span class="tip-title">18、如何参与和查看班上的投票选举？<a
                                                onclick="playMovie('http://player.polyv.net/videos/1a9bb5bed5291bd9995a2fad9c28fd4d_1.swf')"
                                                class="vedio"><%--【观看演示视频】--%></a></span>

                                        <div>
                                            <pre>进入“投票选举”即可查看本班发布的所有的投票选举列表。点击“投ta一票”即可把自己宝贵的一票投给候选人。</pre>
                                        </div>
                                        <img src="/img/userhelp/images/I_cztp2.jpg" alt="" class="I_I">
                                    </li>
                                    <li>
                                        <span class="tip-title">19、如何参与学校发起的问卷调查？</span>

                                        <div>
                                            <pre>进入“问卷调查”点击“填写问卷”勾选正确的答案后，点击“提交”即可。</pre>
                                        </div>
                                        <img src="/img/userhelp/images/I_wjdc.jpg" alt="" class="I_I">
                                    </li>
                                    <li>
                                        <span class="tip-title">20、如何参加K6KT平台运营活动赢取大奖或现金？</span>

                                        <div>
                                            <pre>进入“火热活动”即可查看活动列表，点击其中一个活动，查看活动规则，即可参加并赢取奖品或现金。</pre>
                                        </div>
                                        <img src="/img/userhelp/images/I_hrhd.jpg" alt="" class="I_I">
                                    </li>
                                    <li>
                                        <span class="tip-title">21、如何选择拓展课？</span>

                                        <div>
                                            <pre>从导航栏进入“才艺社/拓展课”，点击“选课列表”进入可看到老师发布的所有拓展课列表，点击“我选”即可选课。选择拓展课后，可以在“我的选课”中查看所选拓展课，在规定的时间内可以退课或选择其他开放的拓展课。</pre>
                                        </div>
                                        <img src="/img/userhelp/images/I_xztzk.jpg" alt="" class="I_I">
                                    </li>
                                    <li>
                                        <span class="tip-title">22、如何在平板电脑及手机移动终端，使用K6KT-快乐课堂？</span>

                                        <div>
                                            <pre>A.安卓和苹果系统如何下载APP软件 ？
通过浏览器打开<a href="http://www.k6kt.com" target="_blank">www.k6kt.com</a>，点击“手机客户端APP”，扫描二维码安装。或者在APP Store里搜索“K6KT”安装。
                                            </pre>
                                        </div>
                                        <img src="/img/userhelp/images/I_APP.jpg" alt="" class="I_I">
                                        <pre>B.利用移动终端，我们都能做些什么？
  *可以随时接收和在线提交学校发布的家庭作业
  *可以随时接收学校发布的各类通知
  *可以随时和学校老师家长进行私信交流，掌握孩子动态
  *可以随时拍照，上传到“微校园”进行分享
  *随时随地，家校互动，在线学习，虚拟社区全分享。
                                            </pre>
                                    </li>
                                </ul>
                            </div>
                            <!-- student end -->
                            <!--管理员-->
                            <div id="admin-help-container" class="help-container">
                                <div style="font-size: 20px;font-weight: bold;overflow: visible;width:100%;height: 40px;line-height: 40px;text-align: center;">
                                    亲爱的管理员，您好！感谢您使用K6KT-快乐课堂平台！
                                </div>
                                <ul>
                                    <li>
                                        <span class="tip-title">1、如何登陆全向互动K6KT-智慧校园？<!--<a onclick="playMovie('http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/55becec30cf23fde5708c7a2.mp4.m3u8')">
                                            【观看演示视频】 </a>-->
                                         </span>

                                        <div>
                                            <pre>用户登录<a href="http://www.k6kt.com" target="_blank">www.k6kt.com</a>,在页面右上方填入学校分配的用户名与密码，即可登录全向互动K6KT-智慧校园。</pre>
                                        </div>
                                        <img src="/img/userhelp/images/I_dl.jpg" alt="" class="I_I">
                                    </li>
                                    <li>
                                        <span class="tip-title">2、如何更改完善管理员个人头像？<!--<a onclick="playMovie('http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/55b846770cf2a7f1af42dd50.mp4.m3u8')">
                                            【观看演示视频】 </a>-->
                                         </span>

                                        <div>
                                            <pre>登录后，进入个人中心，点击“个人设置”，再点击“修改头像”下的“点击上传图片”即可上传已有照片或者拍摄照片来修改头像。</pre>
                                        </div>
                                        <img src="/img/userhelp/images/I_tx.jpg" alt="" class="I_I">
                                    </li>
                                    <li>
                                        <span class="tip-title">3、如何修改管理员个人密码？<!--<a onclick="playMovie('http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/55b846da0cf2a7f1af42dd53.mp4.m3u8')">
                                            【观看演示视频】 </a>-->
                                         </span>

                                        <div>
                                            <pre>登录后，进入个人中心，点击“个人设置”，点击“修改密码”即可操作。</pre>
                                        </div>
                                        <img src="/img/userhelp/images/I_xgmm.jpg" alt="" class="I_I">
                                    </li>
                                   <!-- <li>
                                        <span class="tip-title">4、如何发送私信？<a onclick="playMovie('http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/55b8472f0cf2a7f1af42dd57.mp4.m3u8')">
                                            【观看演示视频】 </a></span>

                                        <div>
                                            <pre>进入个人中心的“我的私信”，点击“发私信”，选择收信人名单，即可与学生、家长、老师进行私信交流。</pre>
                                        </div>
                                        <img src="/img/userhelp/images/I_fsx.jpg" alt="" class="I_I">
                                    </li>-->
                                    <li>
                                        <span class="tip-title">4、如何新增、删除、修改一门学科？<!--<a onclick="playMovie('http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/55b84db00cf2a7f1af42dd7d.mp4.m3u8')">
                                            【观看演示视频】 </a></span>-->

                                        <div>
                                            <pre>进入“平台管理”的“设置学科”，点击“新建科目”可以实现新增一门新学科，点击笔形图标可以修改这门学科的从属年级和名称，点击垃圾桶图标可以一键删除这门学科。</pre>
                                        </div>
                                        <img src="/img/userhelp/images/I_czxk.jpg" alt="" class="I_I">
                                    </li
                                    <li>
                                        <span class="tip-title">5、如何批量导入老师学生信息？<!--<a onclick="playMovie('http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/55b84db00cf2a7f1af42dd7d.mp4.m3u8')">
                                            【观看演示视频】 </a></span>-->

                                        <div>
                                            <pre>A.在“平台管理”的“设置班级学生”页面，点击“批量导入人员”，“下载模版”后，根据操作提示在模版表格中填入相应的教师和学生基本信息，点击“选择文件”，找到已填写完毕的“师生基本信息模版”，点击“开始导入”即可批量导入师生信息。（请严格按模版格式填写，避免导入失败）</pre>
                                        </div>
                                        <img src="/img/userhelp/images/I-pldr1.png" alt="" class="I_I">
                                        <img src="/img/userhelp/images/I-pldr2.png" alt="" class="I_I">
                                    </li
                                    <li>
                                        <span class="tip-title">6、如何给新建的用户设置一个统一的默认密码？<!--<a onclick="playMovie('http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/55b86e2f0cf2a7f1af42dd96.mp4.m3u8')">
                                            【观看演示视频】 </a></span>-->

                                        <div>
                                            <pre>A.进入“平台管理”的“设置班级学生”，点击“设置新建用户默认密码”即可。</pre>
                                        </div>
                                        <img src="/img/userhelp/images/I_mrmm.png" alt="" class="I_I">
                                    </li>
                                    <li>
                                        <span class="tip-title">7、如何还原老师、学生、家长的初始密码？<!--<a onclick="playMovie('http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/55b86e2f0cf2a7f1af42dd96.mp4.m3u8')">
                                            【观看演示视频】 </a></span>-->

                                        <div>
                                            <pre>A.进入“平台管理”的“设置班级学生”，点击“重置密码”，选择需要还原初始密码的人员（可多选），点击“确定”即可完成操作。</pre>
                                        </div>
                                        <img src="/img/userhelp/images/I-hymm.png" alt="" class="I_I">
                                    </li>

                                    <li>
                                        <span class="tip-title">8、如何进行教师管理，建立教师成长档案？<!--<a onclick="playMovie('http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/55b86e2f0cf2a7f1af42dd96.mp4.m3u8')">
                                            【观看演示视频】 </a></span>-->

                                        <div>
                                            <pre>A.在“平台管理”页面点击“教师管理”，可新添加老师，可以编辑教师基本信息、修改权限、重置密码、删除教师，点击“编辑”可增加教师课程项目，形成教师成长档案。</pre>
                                        </div>
                                        <img src="/img/userhelp/images/I-jsgl.png" alt="" class="I_I">
                                    </li>
                                    <li>
                                        <span class="tip-title">9、如何新增、删除、修改年级信息？<!--<a onclick="playMovie('http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/55b84e1f0cf2a7f1af42dd80.mp4.m3u8')">
                                            【观看演示视频】 </a></span>-->

                                        <div>
                                            <pre>A.进入“平台管理”的“设置班级学生”的年级列表，点击“新建年级”可以实现新增一个年级，点击笔形图标可以修改这个年级的相关信息，点击垃圾桶图标可以一键删除这个年级。</pre>
                                        </div>
                                        <img src="/img/userhelp/images/I-njxx.png" alt="" class="I_I">
                                    </li>
                                    <li>
                                        <span class="tip-title">10、如何新增、删除、修改班级信息？<!--<a onclick="playMovie('http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/55b84ed00cf2a7f1af42dd83.mp4.m3u8')">
                                            【观看演示视频】 </a></span>-->

                                        <div>
                                            <pre>A. 进入“平台管理”的“设置班级学生”的年级列表，点击需要新建或编辑的班级所在的年级，点击“新建班级”可以实现新增一个班级，点击笔形图标可以修改这个班级的相关信息，点击垃圾桶图标可以一键删除这个班级。</pre>
                                        </div>
                                        <img src="/img/userhelp/images/I-bjxx.png" alt="" class="I_I">
                                    </li>
                                    <li>
                                        <span class="tip-title">11、新增、删除、修改班级师生信息？<!--<a onclick="playMovie('http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/55b84f440cf2a7f1af42dd86.mp4.m3u8')">
                                            【观看演示视频】 </a></span>-->

                                        <div>
                                            <pre>进入“平台管理”的“设置班级学生”的年级列表，点击需要编辑的班级，点击“添加老师”添加一名新任课教师信息。点击该老师姓名后面的笔形图标，可以编辑该老师的信息。点击该老师姓名后面的垃圾桶图标可以删除该老师的信息。点击“新建学生”添加一名新生信息。点击该学生姓名后面的笔形图标，可以编辑该名学生的信息。点击该学生姓名后面的垃圾桶图标可以删除该名学生的信息。</pre>
                                        </div>
                                        <img src="/img/userhelp/images/I-ssxx.png" alt="" class="I_I">
                                    </li>
                                    <li>
                                        <span class="tip-title">12、如何给学生更换班级？<!--<a onclick="playMovie('http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/55b86c120cf2a7f1af42dd8d.mp4.m3u8')">
                                            【观看演示视频】 </a></span>-->

                                        <div>
                                            <pre>A. 进入“平台管理”的“设置班级学生”的年级列表，点击该学生所从属的班级，进入该班级的学生列表，点击该学生姓名后面的人像图标，可将学生调换至新的班级。</pre>
                                        </div>
                                        <img src="/img/userhelp/images/I-ghbj.png" alt="" class="I_I">
                                    </li>
                                    <li>
                                        <span class="tip-title">13、如何开设一门新的拓展课？<!--<a onclick="playMovie('http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/55b86d800cf2a7f1af42dd90.mp4.m3u8')">
                                            【观看演示视频】 </a></span>-->

                                        <div>
                                            <pre>A. 进入“平台管理”的“管理拓展课”，选择拓展课所属分类或“新建分类”，点击进入，点击“新建拓展课”，完整填写该课程信息即可完成操作。</pre>
                                        </div>
                                        <img src="/img/userhelp/images/I-ktzk.png" alt="" class="I_I">

                                       <%-- <div>
                                            <pre>学生、家长初始密码还原：进入“我的学校”的“管理校园”的年级列表，点击需要还原初始密码的学生或家长（家长的孩子）所从属的班级，进入该班级的学生列表，点击该学生姓名后面的笔形按钮，点击“还原学生初始密码”、“还原家长初始密码”即可完成操作。</pre>
                                        </div>--%>
                                        <%--<img src="/img/userhelp/images/I_hymm2.jpg" alt="">--%>
                                    </li>


                                    <li>
                                        <span class="tip-title">14、如何管理学校新闻的发布？<!--<a onclick="playMovie('http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/55b86e860cf2a7f1af42dd99.mp4.m3u8')">
                                            【观看演示视频】 </a></span>-->

                                        <div>
                                            <pre>进入“平台管理”的“管理新闻”，点击“发布内容”，即可编辑发布新闻。点击“栏目管理”可添加不同栏目。</pre>
                                        </div>
                                        <img src="/img/userhelp/images/I-xwfb.png" alt="" class="I_I">
                                    </li>

                                </ul>
                            </div>
                            <!--end-->
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <!-- right end-->
    </div>
</div>
<%@ include file="../common_new/foot.jsp" %>

</body>
</html>
