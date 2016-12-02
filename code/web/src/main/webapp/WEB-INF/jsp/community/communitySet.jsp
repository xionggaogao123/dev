<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>管理社区</title>
    <link rel="stylesheet" type="text/css" href="/static/css/community/community.css">
    <script type="text/javascript" src="/static/js/modules/forum/jquery-1.11.1.js"></script>
</head>
<body style="background: #f5f5f5;">
<%--==============头部===================--%>
<%@ include file="../common/head.jsp" %>
<div class="container">

    <%@ include file="_layout.jsp" %>
    </div>
<div class="f-cont">
    <div class="hd-nav">
        <span class="hd-green-cur">我的社区</span>
    </div>
</div>
<div class="container">
    <div class="hd-cont-f hd-cont-f1">
        <div class="com-left">
            <div class="com-left-s">
                <div class="com-tit">管理社区</div>
                <div class="my-comm-container clearfix">
                    <ul class="ul-com" id="myCommunity">

                    </ul>
                </div>
                <div class="new-page-links" id="myPage"></div>
            </div>
        </div>
        <div class="com-right">
            <div class="com-btn">
                <span class="btn-create" onclick="window.location.href='/communityCreate.do'">创建社区</span>
                <span class="btn-join" onclick="window.location.href='/communityJoin.do'">加入社区</span>
            </div>
            <div class="com-right-s clearfix">
                <div class="com-tit">热门社区</div>
                <ul class="ul-my-com" id="hotCommunity">

                </ul>
            </div>
        </div>
    </div>
</div>
<!--=============底部版权=================-->
<%@ include file="../common/footer.jsp" %>

<div class="wind-com-edit">
    <div class="p1">编辑社区<em id="cancel">×</em></div>
    <div class="p2 clearfix">
        <span class="sp1">社区名称：</span>
        <input type="text" id="communityName">
    </div>
    <div class="p2 clearfix">
        <span class="sp1">社区logo：</span>
        <P>
            <img src="" id="communityLogo">
            <label for="image-upload" style="cursor: pointer"><span class="btn-up">上传图片</span></label>
            <span class="img-tx">你可以上传JPG、GIF或PNG格式的文件，文件大小不能超过2M</span>
            <input type="file" name="image-upload" id="image-upload" accept="image/*" size="1"
                   hidden="hidden"/>
        </P>
    </div>
    <div class="p2 clearfix">
        <span class="sp1" id="communityDesc">社区简介：</span>
        <textarea></textarea>
    </div>
    <div class="p2 clearfix">
        <span class="sp1">是否公开：</span>
        <select id="selectOpen">
            <option class="op1" value="0">不公开</option>
            <option class="op2" value="1">公开</option>
        </select>
        <span class="gk-tx xt1" id="xt1">"不公开"社区，系统将不会将本社区作为热门社区推荐给其他用户</span>
        <span class="gk-tx xt2" id="xt2">"公开"社区，系统将会降本社区作为热门社区推荐给其他用户</span>
    </div>
    <div class="p2"><button class="btn-save">保存</button></div>
</div>

<div class="bg"></div>

<%--环信消息通知--%>
<div class="hx-notice">
    <span class="sp1"></span>
    <span class="sp3">您有3条未读消息</span>
</div>


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


<script type="text/template" id="communityTmpl">
    {{~it:value:index}}
    <li>
        <a><img src="{{=value.logo}}"></a>
        <p>{{=value.name}}</p>
        <div class="com-hover-card clearfix">
            <div class="clearfix">
                <img src="{{=value.logo}}"><span></span>
                <span class="sp1">{{=value.name}}</span>
                <span class="sp2">社区ID：{{=value.searchId}}</span>
                <span class="sp-short sp3">社区简介：<em>...[详细]</em>{{=value.desc}} </span>
            </div>
            <p>
                <button class="join" cid="{{=value.id}}">+加入社区</button>
            </p>
            <div class="train-f">
                <div class="down-train"></div>
            </div>
        </div>
    </li>
    {{~}}
</script>


<script type="text/template" id="myCommunityTmpl">
    {{~it:value:index}}
    <li>
        <img src="{{=value.logo}}">
        <p>{{=value.name}}</p>
        <div id="manager">
            管理社区
            <p>
                {{?value.owerId==userId}}
                <span class="sp-edit" cmId="{{=value.id}}">编辑社区</span>
                {{??}}
                <span class="quit" communidyId="{{=value.id}}">退出社区</span>
                {{?}}
            </p>
        </div>
    </li>
    {{~}}
</script>
<script src="/static/js/sea.js"></script>
<script src="/static/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script type="text/javascript"
        src="/static/js/modules/core/0.1.0/jquery-upload/vendor/jquery.ui.widget.js?v=1"></script>
<script type="text/javascript" src="/static/js/modules/core/0.1.0/jquery-upload/jquery.fileupload.js"></script>

<script type="text/javascript">
    var userId = "${userId}"
</script>
<script>
    seajs.use('/static/js/modules/community/communitySet.js', function (communitySet) {
        communitySet.init();
    });

    //上传图片
    $('#image-upload').fileupload({
        url: '/community/images.do',
        done: function (e, response) {
            if (response.result.code != '500') {
                var image = response.result.message[0].path;
                $('#communityLogo').attr('src', image);
            } else {
                alert("上传失败，请重新上传！");
            }
        },
        progressall: function (e, data) {
            $(this).closest('.tp-bm-box').find('.vote-vedio-container ul').html('正在上传...');
        }
    });
</script>
</body>
</html>