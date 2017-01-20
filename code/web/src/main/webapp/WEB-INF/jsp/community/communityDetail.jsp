<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" session="false" contentType="text/html;charset=UTF-8" isELIgnored="false" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="layout" uri="http://www.dreamlu.net/tags/jsp-layout.tld" %>
<%--==============头部===================--%>
<%@ include file="../common/head.jsp" %>
<%-- 填充head --%>
<layout:override name="head">
    <title>社区详情</title>
    <link href="/static/js/modules/core/0.1.0/fancyBox/jquery.fancybox.css?v=2015041602" rel="stylesheet"
          type="text/css" media="screen">
    <link rel="stylesheet" type="text/css" href="/static/css/friend/nearby.css">
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
            <span id="my-community-span" class="hd-green-cur">我的社区</span>
            <span id="myActivity-span">我的活动</span>
        </div>
    </div>
    <div id="communityType"></div>
    <span id="communityId" communityId="${detail.communityId}" detailId="${detailId}" voteType="${voteType}"
          voteDeadFlag="${voteDeadFlag}"></span>

    <div class="container">
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
                                <p class="p-wrap" id="detailContent"></p>
                                <span content="${detail.content}" id="getContent" style="display:none;"></span>
                                <script type="text/javascript">
                                    var content=$('#getContent').attr('content');
                                    var dealContent=content.replace(/\n/g,"<br/>");
                                    $('#detailContent').html(dealContent);
                                </script>
                                <c:forEach items="${detail.images}" var="image">
                                    <a class="fancybox" style="cursor:pointer;" href="${image.url}"
                                       data-fancybox-group="home" title="预览">
                                        <img src="${image.url}?imageView2/2/h/300"/>
                                    </a>
                                </c:forEach>
                                <c:forEach items="${detail.videoDTOs}" var="video">
                                    <div class="content-DV">
                                        <img class="content-img content-Im videoshow2" vurl="${video.videoUrl}" src="${video.imageUrl}">
                                        <img src="/static/images/play.png" class="video-play-btn" onclick="tryPlayYCourse('${video.videoUrl}')">
                                    </div>
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
                                <c:if test="${type==1}">
                                    <div class="vote-detail" >
                                        <h3>
                                            <c:choose>
                                            <c:when test="${voteMaxCount==1}">
                                                 <span>单选</span>
                                            </c:when>
                                            <c:otherwise>
                                                 <span>多选</span>
                                            </c:otherwise>
                                           </c:choose>投票：（最多可选${voteMaxCount}项），共有<em id="voteUserCount" count="${voteUserCount}">${voteUserCount}</em>人参与投票
                                            <c:if test="${voteFlagType==0}">
                                                <a href="javascript:void(0)" onclick="searchVote()">查看投票参与人</a>
                                            </c:if>
                                          </h3>
                                        <c:if test="${voteType==0&&voteDeadFlag==0}">
                                            <ul class="ul1">
                                                <c:if test="${check==1}">
                                                    <c:forEach items="${voteOptions}" var="voteOption" varStatus="status">
                                                        <li>
                                                            <label><input <c:if test="${login==false}">style="display: none" </c:if> onchange="voteCheck(this)" class="checkVote" type="checkbox" name="radio-s" value="${status.index + 1}">${status.index + 1}.${voteOption}</label>
                                                        </li>
                                                    </c:forEach>
                                                </c:if>
                                                <c:if test="${check!=1}">
                                                    <c:forEach items="${voteOptions}" var="voteOption" varStatus="status">
                                                        <li>
                                                            <label><input <c:if test="${login==false}">style="display: none" </c:if> class="checkVote" type="radio" name="radio-s" value="${status.index + 1}">${status.index + 1}.${voteOption}</label>
                                                        </li>
                                                    </c:forEach>
                                                </c:if>
                                            </ul>
                                        </c:if>
                                        <div class="vote-btn">
                                            <c:if test="${voteType==0}">
                                                <c:if test="${login==true&&voteDeadFlag==0}">
                                                    <button id="submitVote">提交</button>
                                                </c:if>
                                            </c:if>
                                            <c:if test="${voteDeadFlag==1}">
                                                    <span>投票已经结束</span>
                                            </c:if>
                                        </div>
                                        <c:if test="${voteType==1||voteDeadFlag==1}">
                                            <ul class="ul2" <c:if test="${login==false}">style="display: none" </c:if>>
                                                <c:forEach items="${voteMapList}" var="map" varStatus="status">
                                                    <li>
                                                        <div class="p1s">${status.index + 1}.${map.voteItemStr}</div>
                                                        <div class="p2s">
                                                            <div class="line-f">
                                                                <span style="width: ${map.voteItemPercent}"></span>
                                                            </div>
                                                            <span>${map.voteItemPercent}<em>(${map.voteItemCount})</em></span>
                                                        </div>
                                                    </li>
                                                </c:forEach>
                                                    <%--<li>--%>
                                                    <%--<div class="p1s">1.爱奇艺TV版</div>--%>
                                                    <%--<div class="p2s">--%>
                                                    <%--<div class="line-f">--%>
                                                    <%--<span style="width: 10%"></span>--%>
                                                    <%--</div>--%>
                                                    <%--<span>10%<em>(100)</em></span>--%>
                                                    <%--</div>--%>
                                                    <%--</li>--%>
                                            </ul>
                                        </c:if>
                                        <c:if test="${voteType==1}">
                                            <p style="margin:6px 0 0 0;">您已给<span style="color: #FF9F19">选项</span>&nbsp;<span style="color: #FF9F19">${voteSelected}</span>&nbsp;投票,感谢您的参与</p>
                                        </c:if>
                                        <c:if test="${login==false}">
                                            <p style="margin-top:-63px;">您没有投票的权限，请先登录相关帐号
                                            </p>
                                        </c:if>
                                    </div>
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
                                    <button id="send"
                                            style="width: 147px;line-height:37px;height: 37px;margin-top: -9px">发布
                                    </button>
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
                                <textarea placeholder="  推荐理由···" style="width: 100%;height: 78px"
                                          id="comment"></textarea>
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

                            </div>
                            <!-- 分页 -->
                            <div class="new-page-links" hidden></div>
                        </div>
                    </div>
                </div>
            </div>

            <script type="text/javascript">
                function voteCheck(obj) {
                    var voteCount = ${voteMaxCount};
                    var voteLength = $("input[type='checkbox']:checked").length;
                    if (voteCount == voteLength) {
                        $("input[type='checkbox']").not("input:checked").attr('disabled', 'disabled');
                    } else {
                        $("input[type='checkbox']").removeAttr('disabled');
                    }
                }
            </script>
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
                    <div class="com-tit">我的社区 <c:if test="${login == true}"><span class="com-set-my-btn"
                                                                                  onclick="window.open('/community/communitySet.do')"></span></c:if>
                    </div>
                    <ul class="ul-my-com">
                        <c:forEach items="${communitys}" var="community">
                            <li>
                                <a href="/community/communityPublish?communityId=${community.id}"><img
                                        src="${community.logo}"></a>
                                <p>${community.name}</p>
                            </li>
                        </c:forEach>
                    </ul>
                </div>
            </div>
        </div>

        <div class="hd-cont-f hd-cont-f2">
            <p class="p1">
                <span id="my-community-1" class="hd-cf-cur2">已报名活动</span>
                <span id="my-community-2">已发布活动</span>
                <span id="my-community-3">已参加活动</span>
            </p>

            <div id="activity-signed-div">
                <img src="/static/images/community/no_data.jpg" hidden>
                <ul class="ul-hds" id="ul-activity-signed">
                </ul>
                <div class="new-page-links signed-page"></div>
            </div>

            <div id="activity-published-dev">
                <img src="/static/images/community/no_data.jpg" hidden>
                <ul class="ul-hds" id="ul-activity-published">
                </ul>
                <div class="new-page-links published-page"></div>
            </div>

            <div id="activity-attended-div">
                <img src="/static/images/community/no_data.jpg" hidden>
                <ul class="ul-hds" id="ul-activity-attended">
                </ul>
                <div class="new-page-links attended-page"></div>
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


    <%--参与投票会员--%>
    <div class="wind wind-jv"  id="wind-jv" style="display: none;">
        <p class="p1"><em>×</em></p>
        <div class="ds">
            <select id="voteSelect">
                <c:forEach items="${voteMapList}" var="map" varStatus="status">
                    <option value="${map.voteUserName}">${map.voteItemStr}</option>
                </c:forEach>
            </select>
        </div>
        <ul class="ul-vj">
        </ul>
    </div>



    <!--报名提示start-->
    <div class="sign-alert sign-activity">
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

    <div class="sign-alert si-s3 alert-diglog">
        <p class="alert-title">提示<em>×</em></p>
        <div class="alert-main">
            <span>确认要取消和<em class="em-f">shawn</em>的玩伴关系吗？</span>
        </div>
        <div class="alert-btn">
            <button class="alert-btn-sure">确认</button>
            <button class="alert-btn-esc">取消</button>
        </div>
    </div>


    <div id="tuyaCanva"
         style="z-index: 9999;display: none;width:1000px;position:fixed;top: 10px;left: 50%;margin-left: -500px;padding: 10px;border-radius: 6px 6px 0 6px;background: #f7f7f7;">
        <canvas id="canvas" width="600" height="500"
                style="border:1px solid #999;position:fixed;left:50%;margin-left:-300px;top:100px;"></canvas>
        <canvas id="canvas2" width="600" height="500"
                style="border:1px solid #999;position:fixed;left:50%;margin-left:-300px;top:100px;"></canvas>
            <%--<span class="btn-canvas" onClick="$('#forbiden_back').fadeIn(300)" style="background-image: url(/static/js/modules/community/plugins/canvas/image/open_url.png)"></span>--%>
        <span class="btn-canvas" onClick="change_attr(0,-1,-1)"
              style="background-image: url(/static/js/modules/community/plugins/canvas/image/pencil.png);cursor: pointer"></span>
            <%--<span class="btn-canvas" onClick="change_attr(1,-1,-1)" style="background-image: url(/static/js/modules/community/plugins/canvas/image/straight.png)"></span>--%>
        <span class="btn-canvas" onClick="change_attr(2,-1,-1)"
              style="background-image: url(/static/js/modules/community/plugins/canvas/image/star_straight.png);cursor: pointer"></span>
            <%--<span class="btn-canvas" onClick="change_attr(3,-1,-1)" style="background-image: url(/static/js/modules/community/plugins/canvas/image/circle.png)"></span>--%>
            <%--<span class="btn-canvas" onClick="change_attr(4,-1,-1)" style="background-image: url(/static/js/modules/community/plugins/canvas/image/rect.png)"></span>--%>
        <span class="btn-canvas" onClick="gaussian()"
              style="background-image: url(/static/js/modules/community/plugins/canvas/image/blur.png);cursor: pointer"></span>
        <span class="btn-canvas active-undo" id="undo" onclick="undoPrev()"></span>
        <span class="btn-canvas active-redo" id="redo" onclick="undoNext()"></span>
        <span class="btn-canvas" onclick="saveUrl()"
              style="background-image: url(/static/js/modules/community/plugins/canvas/image/saveBtn.png);cursor: pointer"></span>
        <span class="btn-canvas" onclick="closeCanvas()"
              style="background-image: url(/static/js/modules/community/plugins/canvas/image/closeBtn.png);cursor: pointer"></span>
            <%--<span class="btn-canvas" onClick="change_attr(5,-1,-1)" style="background-image: url(/static/js/modules/community/plugins/canvas/image/eraser.png)"></span>--%>
            <%--<span class="btn-canvas" onClick="fill_canvas('#ffffff',0,0,canvas_size.x,canvas_size.y)" style="background-image: url(/static/js/modules/community/plugins/canvas/image/clear.png)"></span>--%>
        <span id="size_span"
              style="border: 1px solid #999;width:15px;height: 15px;margin-top:7px;margin-left: 50px;display: block;float: left;margin-left: 20px">1</span>
        <div id="size_bar"
             style="width: 100px;height: 5px;background-color:#999; float: left;margin: 12px;position: relative;">
		<span id="size_thumb" class="btn-canvas" onClick="" style="background-color:#666;;width: 15px; border-top-left-radius:8px; border-top-right-radius:8px; border-bottom-left-radius:8px;
		border-bottom-right-radius:8px;height: 15px;margin:0px; margin-top:-5px;position: absolute;left: 0px;"></span>
        </div>
        <span id="color_span"
              style="border: 1px solid #999;background-color:#00aeef;width:15px;height: 15px;margin-top:7px;display: block;float: left;margin-left: 10px"></span>
        <canvas id="canvas_color" width="198" height="15"
                style="border:1px solid #999;margin-top:7px;margin-left:10px;float:left;"></canvas>

        <div style="width: 248px;height: 162px;position: absolute;left:50%;top:50px;margin-left: 262px;background: #f7f7f7;border-radius: 0 0 6px 6px;">
            <span id="r_channel_span"
                  style="border: 1px solid #999;width:15px;height: 15px;margin-top:7px;margin-left: 50px;display: block;float: left;margin-left: 20px">r</span>
            <div id="r_channel_bar"
                 style="width: 100px;height: 5px;background-color:#999; float: left;margin: 12px;position: relative;">
			<span id="r_channel_thumb" class="btn-canvas" onClick="" style="background-color:#666;;width: 15px; border-top-left-radius:8px; border-top-right-radius:8px; border-bottom-left-radius:8px;
			border-bottom-right-radius:8px;height: 15px;margin:0px; margin-top:-5px;position: absolute;left: 45%;"></span>
            </div>
            <div style="clear: both;"></div>
            <span id="g_channel_span"
                  style="border: 1px solid #999;width:15px;height: 15px;margin-top:7px;margin-left: 50px;display: block;float: left;margin-left: 20px">g</span>
            <div id="g_channel_bar"
                 style="width: 100px;height: 5px;background-color:#999; float: left;margin: 12px;position: relative;">
			<span id="g_channel_thumb" class="btn-canvas" onClick="" style="background-color:#666;;width: 15px; border-top-left-radius:8px; border-top-right-radius:8px; border-bottom-left-radius:8px;
			border-bottom-right-radius:8px;height: 15px;margin:0px; margin-top:-5px;position: absolute;left: 45%;"></span>
            </div>
            <div style="clear: both;"></div>
            <span id="b_channel_span"
                  style="border: 1px solid #999;width:15px;height: 15px;margin-top:7px;margin-left: 50px;display: block;float: left;margin-left: 20px">b</span>
            <div id="b_channel_bar"
                 style="width: 100px;height: 5px;background-color:#999; float: left;margin: 12px;position: relative;">
			<span id="b_channel_thumb" class="btn-canvas" onClick="" style="background-color:#666;;width: 15px; border-top-left-radius:8px; border-top-right-radius:8px; border-bottom-left-radius:8px;
			border-bottom-right-radius:8px;height: 15px;margin:0px; margin-top:-5px;position: absolute;left: 45%;"></span>
            </div>
        </div>
        <div id="forbiden_back"
             style="width: 100%;height: 100%;background-image: url(/static/js/modules/community/plugins/canvas/image/pattern.png);position: absolute;top: 0px;left: 0px;display: none;">
            <div style="width: 382px;height: 170px;background-image: url(/static/js/modules/community/plugins/canvas/image/open_window.png);margin: 0 auto;margin-top: 200px;position: relative;">
                <input id="pic_url" type="text" style="width:250px; margin: 53px;margin-left: 83px;"/>
                <div id="close_window"
                     style="width: 20px;height: 15px;border: 0px solid green;position: absolute;right:20px;top: 10px"></div>
                <div id="open_pic"
                     style="width: 80px;height: 30px;border: 0px solid green;position: absolute;left:155px;top: 102px"
                     onClick="open_img(pic_url)"></div>
            </div>
        </div>
    </div>

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
                    } else {
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

        function searchVote() {
            $('#wind-jv').show();
            $('.bg').show();
        }

        $('body').on('click','.wind-jv em',function(){
            $('#wind-jv').hide();
            $('.bg').hide();
        })

        $('body').on('change','#voteSelect',function(){
            $('.ul-vj').empty();
            var voteUserName=$(this).val().split(",");
            var str="";
            if(voteUserName!=""){
                for(var i in voteUserName){
                    str=str+"<li>"+voteUserName[i]+"</li>";
                }
                $('.ul-vj').append(str);
            }
        })

        var value=$('#voteSelect').find('option').eq(0).val();
        $('#voteSelect').val(value);
        $('#voteSelect').trigger('change');

    </script>
</layout:override>
<layout:override name="script">

    <script type="text/javascript" src="/static/js/main.js"></script>
    <script type="text/javascript" src="/static/js/recorder.js"></script>
    <%--<script type="text/javascript" src="/static/js/swfobject.js"></script>--%>
    <script type="text/javascript" src="/static/js/modules/community/plugins/swfobject.js"></script>
    <script type="text/javascript"
            src="/static/js/modules/core/0.1.0/jquery-upload/vendor/jquery.ui.widget.js?v=1"></script>
    <script type="text/javascript" src="/static/js/modules/core/0.1.0/jquery-upload/jquery.fileupload.js"></script>
    <script type="text/javascript" src="/static/js/modules/community/plugins/canvas/lanrenzhijia.js"></script>
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
                if ($('.uploadContent').length > 0) {
                    alert("上传视频了不能再上传图片！");
                    return false;
                }

                if ($('.p-doc').length > 0) {
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
                if ($('.uploadContent').length > 0) {
                    alert("上传视频了不能再上传附件！");
                    return false;
                }
                if ($('.pub-img').length > 0) {
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
                if ($('.pub-img').length > 0) {
                    alert("上传图片了不能再上传视频！");
                    return false;
                }

                if ($('.p-doc').length > 0) {
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

    </script>

    <script type="text/template" id="activityBox">
        {{~it:value:index}}
        <li>
            <button value="{{=value.acid}}">取消报名</button>
            <p class="p1">
            <span># {{? value.activityTheme != null }}
                     {{= value.activityTheme.data }}
                    {{?}}
                  #</span> {{=value.title}}
            </p>
            <p class="p2">{{=value.description}}</p>
        </li>
        {{~}}
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
                            <img src="{{=value.shareImage}}" style="width: 117px;height:79px;cursor: pointer"
                                 onclick="window.open('{{=value.shareUrl}}')">
                        </div>
                        <dl>
                            <dt style="cursor: pointer" onclick="window.open('{{=value.shareUrl}}')">
                                {{=value.shareTitle}}
                            </dt>
                            <dd>{{=value.sharePrice}}
                            </dd>
                        </dl>

                    </div>
                    <c:if test="${login == true}">
                        {{?value.ownerZan==0}}
                        <span class="dianzan" ownerZan="{{=value.ownerZan}}" zanId="{{=value.partInContentId}}">{{=value.zan}}</span>
                        {{??}}
                        <span class="yidianzan" ownerZan="{{=value.ownerZan}}" zanId="{{=value.partInContentId}}">{{=value.zan}}</span>
                        {{?}}
                    </c:if>
                </div>
                {{??value.shareUrl!=null&&value.shareUrl!=""}}
                <div class="clearfix comdeti-zan" style="width:100%;">
                    <p><a style="cursor: pointer" onclick="window.open('{{=value.shareUrl}}')" class="urlCount">{{=value.shareUrl}}</a>
                    </p>
                    <c:if test="${login == true}">
                        {{?value.ownerZan==0}}
                        <span class="dianzan" ownerZan="{{=value.ownerZan}}" zanId="{{=value.partInContentId}}">{{=value.zan}}</span>
                        {{??}}
                        <span class="yidianzan" ownerZan="{{=value.ownerZan}}" zanId="{{=value.partInContentId}}">{{=value.zan}}</span>
                        {{?}}
                    </c:if>
                </div>
                {{?}}
                {{?value.imageList.length>0}}
                <div class="img-wrap clearfix">
                    {{~value.imageList:image:i}}
                    {{?value.type==5}}
                    <img src="{{=image}}?imageView2/1/w/82/h/82" vurl="{{=image}}"
                    <c:if test="${operation==true}"> contentId="{{=value.partInContentId}}" class="type-check"</c:if> >
                    {{??}}
                    <a class="fancybox" style="cursor:pointer;" href="{{=image}}" data-fancybox-group="home" title="预览">
                        <img src="{{=image}}?imageView2/1/w/82/h/82"><br/>
                    </a>
                    {{?}}
                    {{~}}

                </div>
                {{?}}
                <c:if test="${login == true}">
                    {{?value.type==5}}
                    {{?value.manager==true}}
                    <div class="py-wrap">
                        <button contentId="{{=value.partInContentId}}" {{?value.mark==1}} class="mark" {{??}}
                                class="un-mark" {{?}}>{{?value.mark==1}}已{{?}}批阅
                        </button>
                    </div>
                    {{?}}
                    {{?}}
                </c:if>
                {{?value.attachmentList.length>0||value.videoList.length>0}}
                <div class="doc-wrap">
                    {{?value.attachmentList.length>0}}
                        <%--<p class="p-hw"></p>--%>
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
            location.href = "/commondownload/downloadFile.do?remoteFilePath=" + url + "&amp;fileName=" + fileName;
        }

        function saveUrl() {
            var param = {};
            param.base64ImgData = getImage();
            $('.com-now').find('img').attr('src', getImage());
            param.oldImage = $('#tuyaCanva').data('oldImage');
            param.partContentId = $('#tuyaCanva').data('partInContentId');
            $.ajax({
                type: "POST",
                data: param,
                url: '/community/base64image.do',
                async: false,
                dataType: "json",
                contentType: "application/x-www-form-urlencoded; charset=UTF-8",
                success: function (rep) {
                    window.location.href = location.href;
                }
            });

        }

        function closeCanvas() {
            $('#tuyaCanva').hide();
            $('.bg').hide();
        }



        $('body').on('click', '.type-check', function () {
            var pUrl = $(this).attr('vurl');
//            var partInContentId = $(this).attr('contentId')+"-"+encodeURIComponent(pUrl.substring(7,pUrl.length));
            var requestData = {};
            var that = this;
            requestData.imageUrl = pUrl;
            var url = "/community/getQiuNiuImage.do";
            $.ajax({
                type: "GET",
                data: requestData,
                url: url,
                async: false,
                dataType: "json",
                contentType: "application/x-www-form-urlencoded; charset=UTF-8",
                success: function (resp) {
                    var picUrl;
                    if (resp.code == 200) {
                        picUrl = resp.url;
                    } else {
                        picUrl = pUrl;
                    }
                    $('#tuyaCanva').show();
                    $('#tuyaCanva').data('partInContentId', $(that).attr('contentId'));
                    $('#tuyaCanva').data('oldImage', encodeURIComponent(pUrl.substring(7, pUrl.length)));
                    $('.bg').show();
                    open_img(picUrl);
//                    checkPrintPos();
//                    var url="/community/saveEditedImage.do?partInContentId=" + partInContentId;
//                    $('#check-hw-container').show();
//                    $('.bg').show();
//                    var so = new FlashObject("/static/js/modules/community/plugins/pictureEditor/pictureEditor.swf", "flashApp", "1000px", "700px", "8");
//                    so.addVariable("picUrl", picUrl);
//                    so.addVariable("uploadUrl", url);
//                    so.addParam("wmode", "transparent");
//                    so.write("check-hw-container");
//                    $('#check-hw-container').append('<div class="close-check-hw" style=" color: white;top: 59px;right: 117px;width: 24px;height: 24px;z-index: 100;position: absolute;cursor: pointer;"></div>');
// $('#check-hw-container').append('<div class="close-save-hw" style=" background:#23CD77;color:#fff;border-radius:3px;top: 58px;right: 240px;width: 62px;height: 30px;line-height:30px;font-size:17px;text-align:center;z-index: 100;position: absolute;cursor: pointer;">保存</div>');
                }
            });
        });
        //
        //
        //        $('body').on('click','.close-check-hw',function(){
        //            closeCheck();
        //        })
        //
        //
        //        function checkPrintPos() {
        //            var whigh = document.documentElement.clientHeight;
        //            var wwidth = document.documentElement.clientWidth;
        //            if (wwidth > 900) {
        //                $('#check-hw-container').css({
        //                    'top': (whigh - 700) / 2,
        //                    'left': (wwidth - 1000) / 2
        //                });
        //            }
        //        }
        //
        //        function closeCheck() {
        //            $('#check-hw-container').empty().hide();
        //            $('.bg').hide();
        //        };


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
        $(document).ready(function () {
            $(".fancybox").fancybox({});

        })
    </script>
</layout:override>
<%@ include file="_layout.jsp" %>