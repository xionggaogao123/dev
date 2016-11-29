<%@ page language="java" session="false" contentType="text/html;charset=UTF-8" isELIgnored="false" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="layout" uri="http://www.dreamlu.net/tags/jsp-layout.tld" %>
<%--==============头部===================--%>
<%@ include file="../common/head.jsp" %>
<%-- 填充head --%>
<layout:override name="head">
    <title>复兰后台管理</title>
    <link href="/static/js/modules/core/0.1.0/fancyBox/jquery.fancybox.css?v=2015041602" rel="stylesheet"
          type="text/css" media="screen">
</layout:override>
<%-- 填充content --%>
<layout:override name="content">
    <div class="f-cont">
        <div class="hd-nav">
            <c:if test="${login == false}">
                <div class="login-mk-btn">
                    <div class="d1" onclick="window.open('/mall/register.do')"></div>
                    <div class="d2"></div>
                </div>
            </c:if>
            <span class="hd-green-cur">我的社区</span>
                <%--<span>找学习</span>--%>
                <%--<span>找玩伴</span>--%>
        </div>
    </div>
    <div id="communityType"></div>
    <span id="communityId" communityId="${detail.communityId}" detailId="${detailId}"></span>
    <div class="hd-cont-f hd-cont-f1 clearfix">
        <div class="det-left">
            <div class="act-details">
                <div class="det-title">
                    <span id="title"></span>
                </div>
                <div class="det-main">
                    <div class="det-inf">
                        <span>活动发起人：${detail.nickName}</span>
                        <span>发表时间:<em>${detail.time}</em></span>
                    </div>
                    <div class="act-title">
                        <p class="p1" id="type">${detail.title}</p>
                        <div class="txt-wrap">
                            <p class="p-wrap">${detail.content}</p>
                            <c:forEach items="${detail.images}" var="image">
                            <a class="fancybox" style="cursor:pointer;" href="${image.url}" data-fancybox-group="home" title="预览">
                                <img src="${image.url}?imageView2/2/h/300"/>
                            </a>
                            </c:forEach>
                            <c:if test="${detail.attachements != null}">
                                <dl class="file-wrap">
                                    <dt>附件下载：</dt>
                                    <c:forEach items="${detail.attachements}" var="attachement">
                                        <dd class="clearfix">
                                            <span>${attachement.flnm}</span>
                                            <a href="javascript:;" class="file-download" url="${attachement.url}"
                                               flnm="${attachement.flnm}">下载</a>
                                        </dd>
                                    </c:forEach>
                                </dl>
                            </c:if>
                        </div>

                        <!-- 编辑器 -->
                        <div class="act-bottom">
                            <button class="share-btn" hidden></button>
                        </div>
                        <div class="share-upload" hidden>
                            <div class="sign-title clearfix">
                                <span class="fl" id="input_title">我要分享</span>
                            </div>
                            <div class="needs-inf" hidden>
                                <%--<input type="text" placeholder="在这里粘贴单品链接"/>--%>
                                <%--<span class="get-inf fr">获取商品信息</span>--%>
                                <%--<div class="clearfix pub-pro-show"><img--%>
                                        <%--src="http://7xiclj.com1.z0.glb.clouddn.com/20160831170237.jpg">--%>
                                    <%--<p class="p2">undefined</p>--%>
                                    <%--<p class="p2">undefined</p>--%>
                                <%--</div>--%>
                            </div>
                            <textarea></textarea>
                            <div class="publish-fj" style="border: white">
                                <div class="pub-fj-img clearfix">

                                </div>
                                <div class="pub-fj-vedio clearfix">

                                </div>
                                <div class="pub-fj-doc clearfix">

                                </div>
                                <div class="vote-vedio-container">
                                    <ul>
                                        </ul>
                                    </div>
                            </div>

                            <input type="file" name="image-upload" id="image-upload" accept="image/*" size="1"
                                   hidden="hidden"/>
                            <input type="file" name="attach-upload" id="attach-upload" hidden="hidden"/>
                            <input type="file" name="vedio-upload" id="vedio-upload" accept="video/*" hidden>
                            <div class="publish-btn" style="height: 37px">
                                <span class="sp1"><label for="image-upload">上传照片</label></span>
                                <span class="sp2" id="uploadAtt"><label for="attach-upload">上传附件</label></span>
                                <span class="sp4"><label for="vedio-upload">上传视频</label></span>
                                <button id="send" style="width: 147px;line-height:37px;height: 37px;margin-top: -9px">发布</button>
                            </div>
                        </div>

                        <div id="recommend" hidden>
                            <div class="sign-title clearfix">
                                <span class="fl"></span>
                            </div>
                            <div class="needs-inf">
                                <input type="text" placeholder=" 在这里粘贴单品链接" id="shareUrl"/>
                                <span class="get-inf fr" id="get">获取商品信息</span>
                                <div class="pub-pro-show">
                                </div>
                            </div>
                            <textarea placeholder="  推荐理由···" style="width: 100%;height: 78px" id="comment"></textarea>
                            <div class="share-type clearfix">
                                <div class="share-select fl" hidden>
                                    <span class="img-upload"><label for="image-upload">上传照片</label></span>
                                </div>
                                <button class="share-recommend fr" id="submit">推荐</button>
                            </div>
                        </div>

                        <div class="sign-details" id="users" hidden>
                            <div class="sign-title clearfix">
                                <span class="fl" id="result"></span>
                                <button class="fr" hidden>导出数据</button>
                            </div>
                            <div class="sign-main">
                                <ul class="clearfix down-result">

                                </ul>
                            </div>
                        </div>


                        <div class="sign-details">
                            <div class="sign-title clearfix" hidden>
                                <span class="fl" id="dload">推荐结果</span>
                                <button class="fr" id="outResult" hidden>导出数据</button>
                            </div>
                            <span class="sign-num" style="display: none">已经报名<em id="partInCount">12</em>人</span>
                            <div id="partInContent"></div>
                            <div class="sign-main" id="dloadData" hidden>
                                <ul class="clearfix down-result" id="signData">
                                </ul>
                            </div>
                                <%--<!-- 推荐结果 -->--%>
                                <%--<div class="sign-txt">--%>
                                <%--<div class="sign-div">--%>
                                <%--<div class="txt-head clearfix">--%>
                                <%--<span class="per-inf fl">--%>
                                <%--<img src="http://7xiclj.com1.z0.glb.clouddn.com/20160831170237.jpg"/>--%>
                                <%--<em>动力系统综合</em>--%>
                                <%--</span>--%>
                                <%--<span class="fr">发表于：2016-10-25<em>13:33</em></span>--%>
                                <%--</div>--%>
                                <%--<div>--%>
                                <%--<div class="needs-wrap">--%>
                                <%--<div class="needs-img-wrap">--%>
                                <%--<img src="/static/images/needs-2.png">--%>
                                <%--</div>--%>
                                <%--<dl>--%>
                                <%--<dt>【天猫超市】奥利奥奥利奥奥利奥奥利奥</dt>--%>
                                <%--<dd>￥44.70</dd>--%>
                                <%--</dl>--%>
                                <%--</div>--%>
                                <%--</div>--%>
                                <%--</div>--%>
                                <%--</div>--%>

                        </div>
                        <!-- 分页 -->
                        <div class="new-page-links" hidden></div>
                    </div>
                </div>
            </div>
        </div>


        <div class="com-right">
            <div class="com-right-s clearfix">
                <div class="com-tit">当前社区</div>
                <div class="com-now">
                    <img src="/static/images/community/result.png" width="60px" height="60px">
                    <p class="p1">弗兰社区</p>
                    <p class="p2">社区ID：1321465</p>
                </div>
            </div>
            <div class="com-right-s clearfix">
                <div class="com-tit">我的社区 <c:if test="${login == true}"><span class="com-set-my-btn" onclick="window.open('/community/communitySet.do')"></span></c:if></div>
                <ul class="ul-my-com">
                    <c:forEach items="${communitys}" var="community">
                        <li>
                            <a href="/community/communityPublish?communityId=${community.id}"><img src="${community.logo}"></a>
                            <p>${community.name}</p>
                        </li>
                    </c:forEach>
                </ul>
            </div>
        </div>
    </div>

    <!--=============底部版权=================-->
    <%@ include file="../common/footer.jsp" %>

    <%--环信消息通知--%>
    <div class="hx-notice">
        <span class="sp2" id="hx-icon"></span>
        <span class="sp3" id="hx-msg-count">您有0条未读消息</span>
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
    <!--报名提示start-->
    <div class="sign-alert">
        <p class="alert-title">提示<em>×</em></p>
        <div class="alert-main">
            <span>您确定要参加该活动吗？</span>
            <input type="text" placeholder="备注：" id="beizhu">
        </div>
        <div class="alert-btn">
            <button class="alert-btn-sure">确认</button>
            <button class="alert-btn-esc">取消</button>
        </div>
    </div>
    <!--报名提示end-->

    <!--取消报名提示start-->
    <div class="esc-alert">
        <p class="alert-title">提示<em>×</em></p>
        <div class="alert-main">
            <span>您确认要取消参加该活动吗？</span>
        </div>
        <div class="alert-btn">
            <button class="alert-btn-sure_cancel">确认</button>
            <button class="alert-btn-esc_cancel">取消</button>
        </div>
    </div>
    <!--取消报名提示end-->
    <%--<div class="bg"></div>--%>
    <div id="YCourse_player" class="player-container" style="display: none">
        <div id="player_div" class="player-div"></div>
        <div id="sewise-div"
             style="display: none; width: 630px; height: 360px; max-width: 800px;">
            <script type="text/javascript"
                    src="/static/plugins/sewiseplayer/sewise.player.min.js"></script>

            <span class="player-close-btn"></span>
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
            </script>
        </div>
    </div>

    <script type="text/javascript">

        var isFlash = false;
        function getVideoType(url) {
            if (url.indexOf('polyv.net') > -1) {
                return "POLYV";
            }
            if (url.indexOf('.swf') > 0) {
                return 'FLASH';
            }
            return 'HLS';
        }

        function playerReady(name) {
            if (isFlash) {
                SewisePlayer.toPlay(playerReady.videoURL, "视频", 0, false);
            }
        }

        $('.player-close-btn').click(function () {
            $('#YCourse_player').hide();
            $(".bg").hide();
        });

        function download(url) {
            $.ajax({
                url: "/forum/loginInfo.do?date=" + new Date(),
                type: "get",
                dataType: "json",
                async: false,
                data: {},
                success: function (resp) {
                    var flag = resp.login;
                    if (flag) {
                        location.href = "/forum/userCenter/m3u8ToMp4DownLoad.do?filePath=" + url;
                    }else{
                        $('.store-register').fadeToggle();
                        $('.bg').fadeToggle();
                    }
                }
            });
        }
        function tryPlayYCourse(url) {
            $("#YCourse_player").show();
            $(".player-close-btn").css({
                "display": 'block'
            });
            var videoSourceType = getVideoType(url);
            $('.bg').fadeIn('fast');
            var $player_container = $("#YCourse_player");
            $player_container.fadeIn();

            if (videoSourceType == "POLYV") {
                $('#sewise-div').hide();
                $('#player_div').show();
                var player = polyvObject('#player_div').videoPlayer({
                    'width': '800',
                    'height': '450',
                    'vid': url.match(/.+(?=\.swf)/)[0].replace(/.+\//, '')
                });
            } else {
                $('#player_div').hide();
                $('#sewise-div').show();
                try {
                    SewisePlayer.toPlay(url, "视频", 0, true);
                } catch (e) {
                    playerReady.videoURL = url;
                    isFlash = true;
                }
            }
        }
    </script>
</layout:override>
<layout:override name="script">

    <script type="text/javascript" src="/static/js/main.js"></script>
    <script type="text/javascript" src="/static/js/recorder.js"></script>
    <script type="text/javascript" src="/static/js/swfobject.js"></script>
    <script type="text/javascript"
            src="/static/js/modules/core/0.1.0/jquery-upload/vendor/jquery.ui.widget.js?v=1"></script>
    <script type="text/javascript" src="/static/js/modules/core/0.1.0/jquery-upload/jquery.fileupload.js"></script>
    <%-- 填充script --%>
    <script>
            //上传图片
            $('#image-upload').fileupload({
                url: '/community/images.do',
                done: function (e, response) {
                    if (response.result.code != '500') {
                        $(this).closest('div').find('.vote-vedio-container ul').html('');
                        var imageUrl = response.result.message[0].path;
                        var fileName = response.result.message[0].fileName;
                        var str = "<div class=\"pub-img\" fileName=\"" + fileName + "\"" + ">"
                                + "<img width=\"60px\" height=\"43px\" src=\"" + imageUrl + "\"" + ">"
                                + "<em></em></div>";
                        $('.pub-fj-img').append(str);
                    } else {
                        alert("上传失败，请重新上传！");
                    }
                },
                progressall: function (e, data) {
                    $(this).closest('div').find('.vote-vedio-container ul').html('正在上传...');
                },
                submit: function (e) {
                    if($('.uploadContent').length>0){
                        alert("上传视频了不能再上传图片！");
                        return false;
                    }

                    if($('.p-doc').length>0){
                        alert("上传附件了不能再上传图片！");
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
                        $(this).closest('div').find('.vote-vedio-container ul').html('');
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
                    $(this).closest('div').find('.vote-vedio-container ul').html('正在上传...');
                },
                submit: function (e) {
                    if ($('.uploadContent').length>0) {
                        alert("上传视频了不能再上传附件！");
                        return false;
                    }
                    if($('.pub-img').length>0){
                        alert("上传图片了不能再上传附件！");
                        return false;
                    }
                }
            });

            //上传视频
            $('#vedio-upload').fileupload({
                url: '/commonupload/video.do',
                done: function (e, response) {
                    if (response.result.result) {
                        $(this).closest('div').find('.vote-vedio-container ul').html('');
                        var str = "<div class=\"content-DV uploadContent\" >" +
                                "<img class=\"content-img content-Im videoshow2\" vurl=\"" + response.result.videoInfo.url + "\" src=\"" + response.result.videoInfo.imageUrl + "\">" +
                                "<img src=\"/static/images/play.png\" class=\"video-play-btn\" onclick=\"tryPlayYCourse('" + response.result.videoInfo.url + "')\"> </div>";
                        $('.pub-fj-vedio').append(str);
                    } else {
                        alert("上传失败，请重新上传！");
                    }
                },
                progressall: function (e, data) {
                    $(this).closest('div').find('.vote-vedio-container ul').html('正在上传...');
                },
                submit: function (e) {
                    if($('.pub-img').length>0){
                        alert("上传图片了不能再上传视频！");
                        return false;
                    }

                    if($('.p-doc').length>0){
                        alert("上传附件了不能再上传视频！");
                        return false;
                    }

                    if ($('.uploadContent') != undefined) {
                        if ($('.uploadContent').length >= 1) {
                            alert("上传视频不能超过1个！");
                            return false;
                        }
                    }
                }
            });

//            $('.btn1').click(function () {
//                var tags = [];
//                $('.bq-cur').each(function () {
//                    tags.push($(this).attr('value'));
//                });
//                $.ajax({
//                    type: 'POST',
//                    dataType: 'json',
//                    url: "/v2/user/pushTag",
//                    data: {
//                        name: 'mosdf',
//                        ps: 'sdfdsf',
//                        data: tags
//                    },
//                    success: function (data) {
//                        alert(JSON.stringify(data));
//                    }
//                })
//            });
    </script>

    <script type="text/template" id="usersTmpl">
        {{~it:value:index}}
        <li class="fl">
            <div class="load-wrap">
                <img src="{{=value.img}}">
            </div>
            <span class="name-wrap">{{=value.name}}</span>
        </li>
        {{~}}
    </script>

    <script id="downLoadTmpl" type="text/template">
        {{~it:value:index}}
        <li class="fl">
            <div class="load-wrap">
                <img src="{{=value.avator}}">
            </div>
            <span class="name-wrap">{{=value.nickName}}</span>
        </li>
        {{~}}
    </script>

    <script type="text/template" id="userTmpl">
        {{~it:value:index}}
        <div class="sign-txt">
            <div class="sign-div">
                <div class="txt-head">
                    <span class="per-inf fl">
                        <img src="{{=value.img}}">
                        <em>{{=value.name}}</em>
                    </span>
                    <span class="fr">报名时间：<em>{{=value.time}}</em></span>
                </div>
                {{?value.content!=null&&value.content!=""}}
                <div>
                    <p>{{=value.content}}</p>
                </div>
                {{?}}
            </div>
        </div>
        {{~}}
    </script>

    <script type="text/template" id="textTempl">
        {{~it:value:index}}
        <div class="sign-txt">
            <div class="sign-div">
                <div class="txt-head">
                    <span class="per-inf fl">
                        <img src="{{=value.avator}}">
                        <em>{{= value.nickName}}</em>
                    </span>
                    <span class="fr">发表于：<em>{{=value.time}}</em></span>
                </div>
                {{?value.information!=null&&value.information!=""}}
                <div>
                    <p>{{=value.information}}</p>
                </div>
                {{?}}
                {{?value.shareImage!=null&&value.shareImage!=""}}
                <div class="clearfix comdeti-zan" style="width:100%;">
                    <div class="needs-wrap">
                        <div class="needs-img-wrap">
                            <img src="{{=value.shareImage}}" style="width: 117px;height:79px;cursor: pointer" onclick="window.open('{{=value.shareUrl}}')">
                        </div>
                        <dl>
                            <dt style="cursor: pointer" onclick="window.open('{{=value.shareUrl}}')">{{=value.shareTitle}}</dt>
                            <dd>{{=value.sharePrice}}
                            </dd>
                        </dl>

                    </div>
                    <c:if test="${login == true}">
                    {{?value.ownerZan==0}}
                    <span class="dianzan"  ownerZan="{{=value.ownerZan}}" zanId="{{=value.partInContentId}}" >{{=value.zan}}</span>
                    {{??}}
                    <span class="yidianzan"  ownerZan="{{=value.ownerZan}}" zanId="{{=value.partInContentId}}" >{{=value.zan}}</span>
                    {{?}}
                    </c:if>
                </div>
                {{??value.shareUrl!=null&&value.shareUrl!=""}}
                <div class="clearfix comdeti-zan" style="width:100%;">
                    <p><a style="cursor: pointer" onclick="window.open('{{=value.shareUrl}}')" class="urlCount">{{=value.shareUrl}}</a></p>
                    <c:if test="${login == true}">
                    {{?value.ownerZan==0}}
                    <span class="dianzan"  ownerZan="{{=value.ownerZan}}" zanId="{{=value.partInContentId}}" >{{=value.zan}}</span>
                    {{??}}
                    <span class="yidianzan"  ownerZan="{{=value.ownerZan}}" zanId="{{=value.partInContentId}}" >{{=value.zan}}</span>
                    {{?}}
                    </c:if>
                </div>
                {{?}}
                {{?value.imageList.length>0}}
                <div class="img-wrap">
                    {{~value.imageList:image:i}}
                    {{?value.type==5}}
                    <img src="{{=image}}?imageView2/1/w/82/h/82" vurl="{{=image}}" <c:if test="${operation==true}"> contentId="{{=value.partInContentId}}" class="type-check"</c:if> >
                    {{??}}
                    <a class="fancybox" style="cursor:pointer;" href="{{=image}}" data-fancybox-group="home" title="预览">
                        <img src="{{=image}}?imageView2/1/w/82/h/82"><br/>
                    </a>
                    {{?}}
                    {{~}}

                </div>
                <div class="py-wrap">
                    <c:if test="${login == true}">
                        {{?value.type==5}}{{?value.manager==true}}<button contentId="{{=value.partInContentId}}" {{?value.mark==1}} class="mark"{{??}} class="un-mark"{{?}}>{{?value.mark==1}}已{{?}}批阅</button>{{?}}{{?}}
                    </c:if>
                </div>
                {{?}}
                {{?value.attachmentList.length>0||value.videoList.length>0}}
                <div class="doc-wrap">
                    {{?value.attachmentList.length>0}}
                    <p class="p-hw">
                        {{~value.attachmentList:attachment:i}}
                        <div>
                        <span class="sp-hw">
                            {{=attachment.flnm}}
                            <a style="margin: 20px">
                                <span onclick="download('{{=attachment.url}}','{{=attachment.flnm}}')"
                                      style="color: #3C6CE1;cursor: pointer;">下载</span>
                            </a>
                        </span>
                        </div>
                    {{~}}
                    </p>
                    {{?}}
                    {{~value.videoList:video:i}}
                    <div class="content-DV">
                        <img class="content-img content-Im videoshow2" vurl="{{=video.videoUrl}}"
                             src="{{=video.imageUrl}}">
                        <img src="/static/images/play.png" class="video-play-btn"
                             onclick="tryPlayYCourse('{{=video.videoUrl}}')">
                    </div>
                    {{~}}
                </div>
                {{?}}
            </div>
        </div>
        {{~}}
    </script>

    <script type="text/javascript">
        function download(url, fileName) {
            location.href = "/commondownload/downloadFile.do?remoteFilePath=" + url + "&fileName=" + fileName;
        }
    </script>
    <!--============登录================-->
    <%@ include file="../common/login.jsp" %>
    <div id="check-hw-container" hidden>
    </div>
    <script src="/static/js/sea.js"></script>
    <script src="/static/js/modules/core/0.1.0/config.js?v=2015041602"></script>
    <script>
        seajs.use('/static/js/modules/community/communityDetail.js', function (communityDetail) {
            communityDetail.init();
        });

    </script>
    <script type="text/javascript" src="/static/js/modules/core/0.1.0/fancyBox/jquery.fancybox.js"></script>
    <script type="text/javascript">
        $(document).ready(function() {
            $(".fancybox").fancybox({
            });

        })
    </script>

</layout:override>
<%@ include file="_layout.jsp" %>