<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<head>
    <meta charset="utf-8">
    <title>发布通知</title>
    <link rel="stylesheet" type="text/css" href="/static/css/community/community.css">
    <link href="/static/js/modules/core/0.1.0/fancyBox/jquery.fancybox.css?v=2015041602" rel="stylesheet"
          type="text/css" media="screen">
    <script type="text/javascript" src="/static/js/modules/forum/jquery-1.11.1.js"></script>
    <script type="text/javascript" src="/static/js/modules/forum/jquery-browser.js"></script>
    <script type="text/javascript" src="/static/js/main.js"></script>
    <script type="text/javascript" src="/static/js/recorder.js"></script>
    <script type="text/javascript" src="/static/js/swfobject.js"></script>
</head>
<body style="background: #f5f5f5;" communityId="${communityId}" userId="${userId}">
<%--==============头部===================--%>
<%@ include file="../common/head.jsp" %>
<div class="container">
    <%@ include file="_layout.jsp" %>
    </div>
    <div class="f-cont">
        <div class="hd-nav">
            <span class="hd-green-cur">我的社区</span>
            <%--<span>找学习</span>--%>
            <%--<span>找玩伴</span>--%>
        </div>
    </div>
<div class="container">
    <div class="hd-cont-f hd-cont-f1">
        <div class="com-left">
            <div class="com-rlt">
                <img src="/static/images/community/result.png">
                <p class="p1">复兰社区</p>
                <p class="p4">
                    <em class="em-com-er">社区二维码</em>
                    <%--<em>查看</em>--%>
                    <%--<em>复制</em>--%>
                </p>
                <p class="p2">社区ID:fulaan666</p>
                <p class="p3">社区简介：本社区仅限用于家长和教师的日常沟通、学习...</p>
                <%--<button id="join">加入社区</button>--%>
                <button class="btn-yq">+邀请玩伴</button>
            </div>
            <div class="publish-cont" <c:if test="${operation!=1}">style="display: none"</c:if>>
                <ul class="publish-nav">
                    <li class="li1" type="1" class="green">
                        <span class="sp1" class="disn"></span>
                        <span class="sp2" class="disb"></span>
                        发通知
                    </li>
                    <li class="li2" type="2">
                        <span class="sp1"></span>
                        <span class="sp2"></span>
                        组织活动报名
                    </li>
                    <li class="li3" type="3">
                        <span class="sp1"></span>
                        <span class="sp2"></span>
                        火热分享
                    </li>
                    <li class="li4" type="4">
                        <span class="sp1"></span>
                        <span class="sp2"></span>
                        学习资料
                    </li>
                    <li class="li5" type="5">
                        <span class="sp1"></span>
                        <span class="sp2"></span>
                        布置作业
                    </li>
                    <li class="li6" type="6">
                        <span class="sp1"></span>
                        <span class="sp2"></span>
                        发布学习用品需求
                    </li>
                </ul>
                <div class="publish-putin">
                    <input type="text" placeholder="请输入大标题" id="title">
                    <textarea placeholder="请输入内容" id="content"></textarea>
                    <div class="publish-fj clearfix">
                        <div class="pub-fj-img clearfix">

                        </div>
                        <div class="pub-fj-yuyin clearfix" id="container">
                            <div id="recorder" class="">
                                <div class="area">
                                    <div style="padding-top: 10px;position: absolute;z-index: 50000;">
                                        <div class="sanjiao"
                                             style="width: 0;height: 0;border-left: 8px solid transparent;border-right: 8px solid transparent;border-bottom: 10px solid rgb(100,100,100);position:absolute;display: inline-block;top:0px;left:10px;display:none;">
                                        </div>
                                        <div id="myContent">
                                        </div>
                                    </div>
                                </div>

                                <form id="uploadForm" name="uploadForm">
                                    <input name="authenticity_token" value="xxxxx" type="hidden">
                                    <input name="upload_file[parent_id]" value="1" type="hidden">
                                    <input name="format" value="json" type="hidden">
                                </form>
                            </div>
                            <div id="voice_notice">
                            </div>
                        </div>
                        <div class="pub-fj-doc clearfix">

                        </div>
                        <div class="pub-fj-pro">
                            <div class="pub-pro-input">
                                <span>分享该用品的网址：</span>
                                <input type="text" placeholder="http://" id="shareUrl">
                                <button id="try">试一试</button>
                            </div>
                            <div class="clearfix pub-pro-show">
                                <%--<img src="/static/images/community/shouzhi.png">--%>
                                <%--<p class="p1"><a href="###">【天猫超市】超级无敌宇宙最牛逼奢侈豪华加长霹雳炫酷吊炸天厕纸</a></p>--%>
                                <%--<p class="p2">￥100000000</p>--%>
                            </div>
                        </div>
                        <div class="vote-vedio-container">
                            <ul>
                            </ul>
                        </div>
                    </div>
                </div>
                <input type="file" name="image-upload" id="image-upload" accept="image/*" size="1" hidden="hidden"/>
                <input type="file" name="attach-upload" id="attach-upload" hidden="hidden" size="1"/>
                <div class="publish-btn">
                    <span class="sp1"><label for="image-upload">上传照片</label></span>
                    <span class="sp2"><label for="attach-upload">上传附件</label></span>
                    <%--<span class="sp3" onclick="showflash('container')">语音</span>--%>
                    <button id="submit">发布</button>
                </div>
            </div>
            <div class="com-left-s" id="announcement">
            </div>

            <div class="com-left-s" id="activity">
            </div>

            <div class="com-left-s" id="materials">
            </div>

            <div class="com-left-s" id="share">
            </div>

            <div class="com-left-s" id="homework">
            </div>

            <div class="com-left-s" id="means">
            </div>
        </div>
        <div class="com-right">
            <div class="com-right-s clearfix">
                <div class="com-tit">当前社区</div>
                <div class="com-now">
                    <img src="/static/images/community/result.png" width="60px" height="60px">
                    <p class="p1">复兰社区</p>
                    <p class="p2">社区ID：1321465</p>
                </div>
            </div>
            <div class="com-right-s clearfix">
                <div class="com-tit">社区成员</div>
                <ul class="ul-com-member clearfix" id="member">
                </ul>
                <p class="member-more">更多</p>
            </div>
            <div class="com-right-s">
                <div class="com-tit">我的社区<c:if test="${login == true}"><a href="/community/communitySet.do" target="_blank"><span class="com-set-my-btn"></span></a></c:if></div>
                <ul class="ul-my-com clearfix" id="myCommunity">
                </ul>
            </div>
        </div>
    </div>
</div>
<!--=============底部版权=================-->
<%@ include file="../common/footer.jsp" %>
<%--等待弹窗--%>
<div class="wind-wait" hidden>
    <p class="p1"><em>×</em></p>
    <img src="/static/images/community/wait.gif">
    <p class="p2">正在拼命上传，请稍后...</p>
</div>

<div class="wind wind-erw">
    <div class="d-x">×</div>
    <div class="d1">
        <img src="/static/images/community/upload.png">
        <p class="dp1">复兰社区</p>
        <p class="dp2">社区ID:SDFS</p>
    </div>
    <div class="d2">
        <img src="/static/images/newIndex/store-WEII.jpg" class="ddimg">
    </div>
    <div class="d3">
        <p>*温馨提示：<br>1.新社员加入可通过“复兰教育社区”手机客户端，选择“扫一扫”，扫描二维码加入该社区；<br>2.若未安装“复兰教育社区”手机客户端，可先通过扫描二维码安装手机客户端后，再次扫描二维码加入该社区。</p>
    </div>
</div>
<div class="wind wind-yq">
    <p class="p1">邀请玩伴<em>×</em></p>
    <p class="p2">
        <input type="text" id="uid" placeholder="输入用户名/昵称/UID">
        <button class="btn1">查找</button>
    </p>
    <div id="load" style="width: 85px;height: 20px;margin-left: 151px;display: none">正在查找中...</div>
    <div class="d1" id="dlUser" hidden>
        <div class="d1-s1">查找结果
            <em class="em-green">（共找到<span id="userCount"></span>位相关用户）</em>
        </div>
        <script id="userListTmpl" type="text/template">
        {{~it:value:index}}
        <li>
            <img src="{{=value.value}}">
            <p>{{=value.name}}</p>
            <button class="btn3" userId="{{=value.id}}">邀请</button>
        </li>
        {{~}}
        </script>
        <ul class="d1-ul" id="userList">
            <li>
                <img src="">
                <p>shawn<br>ID:12345</p>
                <button class="btn3">邀请</button>
            </li>
            <li>
                <img src="">
                <p>shawn<br>ID:12345</p>
                <button class="btn3">邀请</button>
            </li>
            <li>
                <img src="">
                <p>shawn<br>ID:12345</p>
                <button class="btn3">邀请</button>
            </li>
        </ul>
    </div>
    <div class="new-page-links"></div>
</div>


<%--环信消息通知--%>
<div class="hx-notice">
    <span class="sp2" id="hx-icon"></span>
    <span class="sp3" id="hx-msg-count">你有0条未读消息</span>
</div>



<div class="wind-yins">
    <p class="p1">隐私设置<em>×</em></p>
    <label><input type="radio" name="ys-set">所有人可见</label>
    <label><input type="radio" name="ys-set">人认证好友可见</label>
    <label><input type="radio" name="ys-set">尽仅自己可见</label>
    <p class="p2">
        <button class="btn1">确认</button>
        <button class="btn2">取消</button>
    </p>
</div>
<div class="bg"></div>

<script type="text/javascript">
    function download(url, fileName) {
        $.ajax({
            url: "/forum/loginInfo.do?date=" + new Date(),
            type: "get",
            dataType: "json",
            async: false,
            data: {},
            success: function (resp) {
                var flag = resp.login;
                if (flag) {
                    location.href = "/commondownload/downloadFile.do?remoteFilePath=" + url + "&fileName=" + fileName;
                }else{
                    $('.store-register').fadeToggle();
                    $('.bg').fadeToggle();
                }
            }
        })
    }
</script>

<!-- template -->

<script type="text/template" id="myCommunityTmpl">
    {{~it:value:index}}
    <li>
        <a href="/community/communityPublish?communityId={{=value.id}}"><img src="{{=value.logo}}"></a>
        <p>{{=value.name}}</p>
    </li>
    {{~}}
</script>

<script type="text/template" id="memberTmpl">
    {{~it:value:index}}
    <li>
        <img src="{{=value.avator}}" style="cursor: pointer" class="personNal" userId="{{=value.userId}}">
        <p>{{=value.nickName}}</p>
        {{?value.role==2}}
        <p class="p-lel2">{{=value.roleStr}}</p>
        <%--<span class="sp-gly"></span>--%>
        {{??value.role==1}}
        <p class="p-lel1">{{=value.roleStr}}</p>
        <%--<span class="sp-bzr"></span>--%>
        {{??}}
        <p class="font-style">{{=value.roleStr}}</p>
        {{?}}
    </li>
    {{~}}
</script>

<script type="text/template" id="meansTmpl">
    {{~it:value:index}}
    <div class="com-tit" id="means_all" style="cursor: pointer">学习资料
        {{?value.unReadCount==0}}
        <em>全部</em>
        {{??}}
        <span class="i-newtips">{{=value.unReadCount}}</span>
        {{?}}
    </div>
    <div class="notice-container clearfix">
        <div class="notice-holder">
            <img src="{{=value.imageUrl}}">
            <p>{{=value.nickName}}</p>
            <p class="p1">{{=value.roleStr}}</p>
        </div>
        <div class="notice-cont">
            <p class="p-zl p1" style="cursor: pointer" onclick="window.open('/community/communityDetail?detailId={{=value.id}}')">{{=value.title}}</p>
            <p class="p-hw">
                {{~value.attachements:attachment:i}}
                <span class="sp-hw">{{=attachment.flnm}}<a><span
                        onclick="download('{{=attachment.url}}','{{=attachment.flnm}}')" style="cursor: pointer">下载</span></a></span>
                {{~}}
            </p>
            <p class="p-infor">
                <span>消息来源：{{=value.communityName}}</span>
                <span>发表时间：{{=value.time}}</span>
            </p>
        </div>
    </div>
    {{~}}
</script>

<script type="text/template" id="homeworkTmpl">
    {{~it:value:index}}
    <div class="com-tit" id="homework_all" style="cursor: pointer">作业
        {{?value.unReadCount==0}}
        <em>全部</em>
        {{??}}
        <span class="i-newtips">{{=value.unReadCount}}</span>
        {{?}}
    </div>
    <div class="notice-container clearfix">
        <div class="notice-holder">
            <img src="{{=value.imageUrl}}">
            <p>{{=value.nickName}}</p>
            <p class="p1">{{=value.roleStr}}</p>
        </div>
        <div class="notice-cont">
            <p class="p-zy p1"  style="cursor: pointer" onclick="window.open('/community/communityDetail?detailId={{=value.id}}')">{{=value.title}}</p>
            <p class="p-cont">{{=value.content}}<span
                    class="sp-more">{{?value.content.length>=90}}...<em class="spread">[展开全文]</em>{{?}}</span></p>
            <p class="p-infor">
                <span>消息来源：{{=value.communityName}}</span>
                <span>发表时间：{{=value.time}}</span>
            </p>
        </div>
        <%--<div class="notice-bm">--%>
            <%--<button class="commit" itemId="{{=value.id}}">提交作业</button>--%>
            <%--<span>已提交（{{=value.partInCount}}）人</span>--%>
        <%--</div>--%>
    </div>
    {{~}}
</script>

<script type="text/template" id="announcementTmpl">
    {{~it:value:index}}
    <div class="com-tit" id="announce_all" style="cursor: pointer">社区通知
        {{?value.unReadCount==0}}
        <em>全部</em>
        {{??}}
        <span class="i-newtips">{{=value.unReadCount}}</span>
        {{?}}
    </div>
    <div class="notice-container clearfix">
        <div class="notice-holder">
            <img src="{{=value.imageUrl}}">
            <p>{{=value.nickName}}</p>
            <p class="p1">{{=value.roleStr}}</p>
        </div>
        <div class="notice-cont">
            <p class="p-tz p1"  style="cursor: pointer" onclick="window.open('/community/communityDetail?detailId={{=value.id}}')">{{=value.title}}</p>
            <p class="p-cont">{{=value.content}}<span
                    class="sp-more">{{?value.content.length>=143}}...<em class="spread">[展开全文]</em>{{?}}</span></p>
            <p class="p-infor">
                <span>消息来源：{{=value.communityName}}</span>
                <span>发表时间：{{=value.time}}</span>
            </p>
        </div>
    </div>
    {{~}}
</script>


<script type="text/template" id="shareTmpl">
    {{~it:value:index}}
    <div class="com-tit" id="share_all" style="cursor: pointer">火热分享
        {{?value.unReadCount==0}}
        <em>全部</em>
        {{??}}
        <span class="i-newtips">{{=value.unReadCount}}</span>
        {{?}}
    </div>
    <div class="notice-container clearfix">
        <div class="notice-holder">
            <img src="{{=value.imageUrl}}">
            <p>{{=value.nickName}}</p>
            <p class="p1">{{=value.roleStr}}</p>
        </div>
        <div class="notice-cont">
            <p class="p-fx p1"  style="cursor: pointer" onclick="window.open('/community/communityDetail?detailId={{=value.id}}')">{{=value.title}}</p>
            <p class="p-cont">{{=value.content}}<span
                    class="sp-more">{{?value.content.length>=90}}...<em class="spread">[展开全文]</em>{{?}}</span></p>
            {{?value.images.length>0}}
            <p class="p-img clearfix">
                {{~value.images:image:i}}
                <a class="fancybox" style="cursor:pointer;" href="{{=image.url}}" data-fancybox-group="home" title="预览">
                    <img src="{{=image.url}}?imageView2/1/w/100/h/100"><br/>
                </a>
                {{~}}
            </p>
            {{?}}
            <p class="p-infor">
                <span>消息来源：{{=value.communityName}}</span>
                <span>发表时间：{{=value.time}}</span>
            </p>
        </div>
        <%--<div class="notice-bm">--%>
            <%--<button class="commit" itemId="{{=value.id}}">我要分享</button>--%>
            <%--<span>已分享（{{=value.partInCount}}）人</span>--%>
        <%--</div>--%>
    </div>
    {{~}}
</script>


<script type="text/template" id="activityTmpl">
    {{~it:value:index}}
    <div class="com-tit" id="activity_all" style="cursor: pointer">组织活动报名
        {{?value.unReadCount==0}}
        <em>全部</em>
        {{??}}
        <span class="i-newtips">{{=value.unReadCount}}</span>
        {{?}}
    </div>
    <div class="notice-container clearfix">
        <div class="notice-holder">
            <img src="{{=value.imageUrl}}">
            <p>{{=value.nickName}}</p>
            <p class="p1">{{=value.roleStr}}</p>
        </div>
        <div class="notice-cont">
            <p class="p-hd p1"  style="cursor: pointer" onclick="window.open('/community/communityDetail?detailId={{=value.id}}')">{{=value.title}}</p>
            <p class="p-cont">{{=value.content}}<span
                    class="sp-more">{{?value.content.length>=90}}...<em class="spread">[展开全文]</em>{{?}}</span></p>
            <p class="p-infor">
                <span>消息来源：{{=value.communityName}}</span>
                <span>发表时间：{{=value.time}}</span>
            </p>
        </div>
        <div class="notice-bm">
            <button class="commit" itemId="{{=value.id}}">我要报名</button>
            <span>已报名（<em>{{=value.partInCount}}</em>）人</span>
        </div>
    </div>
    {{~}}
</script>


<script type="text/template" id="materialsTmpl">
    {{~it:value:index}}
    <div class="com-tit" id="materials_all" style="cursor: pointer">学习用品
        {{?value.unReadCount==0}}
        <em>全部</em>
        {{??}}
        <span class="i-newtips">{{=value.unReadCount}}</span>
        {{?}}
    </div>
    <div class="notice-container clearfix">
        <div class="notice-holder">
            <img src="{{=value.imageUrl}}">
            <p>{{=value.nickName}}</p>
            <p class="p1">{{=value.roleStr}}</p>
        </div>
        <div class="notice-cont">
            <p class="p-tj p1"  style="cursor: pointer" onclick="window.open('/community/communityDetail?detailId={{=value.id}}')">{{=value.title}}</p>
            <p class="p-cont">{{=value.content}}<span
                    class="sp-more">{{?value.content.length>=90}}...<em class="spread">[展开全文]</em>{{?}}</span></p>
            <p class="p-img clearfix">
                {{?value.shareImage!=null&&value.shareImage!=""}}
                <img src="{{=value.shareImage}}">
                {{??value.shareUrl!=null&&value.shareUrl!=""}}
                <a  style="cursor: pointer" onclick="window.open('{{=value.shareUrl}}')" class="limitCount">{{=value.shareUrl}}</a>
                {{?}}
            </p>
            <p class="p-infor">
                <span>消息来源：{{=value.communityName}}</span>
                <span>发表时间：{{=value.time}}</span>
            </p>
        </div>
    </div>
    {{~}}
</script>

<!-- end template -->
<!--============登录================-->
<%@ include file="../common/login.jsp" %>
<script src="/static/js/sea.js"></script>
<script src="/static/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script type="text/javascript"
        src="/static/js/modules/core/0.1.0/jquery-upload/vendor/jquery.ui.widget.js?v=1"></script>
<script type="text/javascript" src="/static/js/modules/core/0.1.0/jquery-upload/jquery.fileupload.js"></script>
<script>
    seajs.use('/static/js/modules/community/communityPublish.js', function (communityPublish) {
        communityPublish.init();
    });

    //上传图片
    $('#image-upload').fileupload({
        url: '/community/images.do',
        done: function (e, response) {
            if (response.result.code != '500') {
//                $('.vote-vedio-container ul').html('');
                $('.wind-wait').hide();
                var url = response.result.message[0].path;
                var imageUrl = response.result.message[0].path;
                var fileName = response.result.message[0].fileName;
                var str = "<div class=\"pub-img\" fileName=\"" + fileName + "\"" + ">"
                        + "<img width=\"60px\" height=\"60px\" src=\"" + imageUrl + "\"" + ">"
                        + "<em></em></div>";
                $('.pub-fj-img').append(str);
            } else {
                alert("上传失败，请重新上传！");
            }        },
        progressall: function (e, data) {
            $('.wind-wait').show();
//            $('.vote-vedio-container ul').html('正在上传...');
        },
        submit: function (e) {
            if($('.p-doc').length>0){
                alert('上传附件了不能再上传图片！');
                return false;
            }
            if ($('.pub-img') != undefined) {
                if ($('.pub-img').length >= 9) {
                    alert("上传照片不能超过9张！");
                    return false;
                }
            }
        }
    });

    //上传附件
    $('#attach-upload').fileupload({
        url: '/commonupload/doc/upload.do',
        done: function (e, response) {
            if (response.result.code != '500') {
//                $('.vote-vedio-container ul').html('');
                $('.wind-wait').hide();
                var url = response.result.message[0].path;
                var url = response.result.message[0].path;
                var fileName = response.result.message[0].fileName;
                var str = "<p class=\"p-doc\" url=\"" + url + "\"" + ">" +
                        "<span>" + fileName + "</span><em></em></p> ";
                $('.pub-fj-doc').append(str);
            } else {
                alert("上传失败，请重新上传！");
            }
        },
        progressall: function (e, data) {
//            $('.vote-vedio-container ul').html('正在上传...');
            $('.wind-wait').show();
        },
        submit: function(e){
            if($('.pub-img').length>0){
                alert("上传图片了不能再上传附件！");
                return false;
            }
        }
    });

    //上传语音
    var foo = true;
    function showflash(container) {
        var mc = new FlashObject("/static/js/audiorecorder.swf", "recorderApp", "350px", "23px", "8");
        mc.setAttribute("id", "recorderApp");
        mc.setAttribute("name", "recorderApp");

        mc.addVariable("uploadAction", "/commonupload/doc/upload.do");
        mc.addVariable("fileName", "audio");
        mc.addVariable("recordTime", 10 * 60 * 1000);
        mc.addVariable("appName", "recorderApp");
        mc.write("myContent");
        if (foo) {
            $(container).append($('#recorder'));
            $('#recorder .sanjiao').show();
            $("#myContent").show();
            foo = false;
        } else {
            $("#myContent").hide();
            $('#recorder .sanjiao').hide();
            foo = true;
        }
    }


</script>
<script type="text/javascript" src="/static/js/modules/core/0.1.0/fancyBox/jquery.fancybox.js"></script>
<script type="text/javascript">
    $(document).ready(function() {
        $(".fancybox").fancybox({
        });

    })
</script>
</body>
</html>
