<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>个人空间</title>
    <link rel="stylesheet" type="text/css" href="/static/css/community/community.css">
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
        <%--<span>我的学习</span>--%>
        <%--<span>我的玩伴</span>--%>
    </div>
</div>
<div class="container">
    <div class="back-prev">
        <a onclick="window.history.go(-1)">< 返回上一页</a>
    </div>
    <div class="hd-cont-f hd-cont-f1">
        <div class="com-left">
            <div class="com-left-s com-l1">

                <div class="com-tit">创建社区</div>
                <ul class="ul-create">
                    <li class="clearfix">
                        <span>社区名称：</span>
                        <input type="text" id="communityName">
                    </li>
                    <li class="clearfix">
                        <span>社区logo：</span>
                        <div>
                            <img id="imageUrl" src="/static/images/community/upload.png">
                            <p>
                                <label for="image-upload"><span class="spa-upload">上传图片</span>你可以上传JPG、GIF、或PNG格式的文件，文件大小不超过2M</label>
                            </p>
                            <input type="file" name="image-upload" id="image-upload" accept="image/*" size="1"
                                   hidden="hidden"/>
                        </div>
                    </li>
                    <li class="clearfix">
                        <span>社区简介：</span>
                        <textarea id="communityDes"></textarea>
                    </li>
                    <li class="clearfix">
                        <span>是否公开：</span>
                        <div>
                            <select id="selectOpen">
                                <option value="0">不公开</option>
                                <option value="1">公开</option>
                            </select>
                            <p class="p-gk">*"不公开"社区，系统不会将本社区作为热门社区推荐给用户<br>*"公开"社区，系统会将本社区作为热门社区推荐给用户</p>
                        </div>
                    </li>
                    <li class="clearfix">
                        <span></span>
                        <button class="btn-ok">确认创建</button>
                    </li>
                </ul>
            </div>

            <div class="com-left-s com-l2">
                <div class="com-tit">创建社区</div>
                <div class="com-cre-infor">
                    <h3>“<span class="sq-nm"></span>”社区创建成功！</h3>
                    <p>*温馨提示:</p>
                    <p>1、新社员加入可通过“复兰教育社区”手机客户端，选择“扫一扫”，扫描二维码加入该社区；</p>
                    <p>2、若未安装“复兰教育社区”手机客户端，可先通过扫描APP客户端二维码（<span><i id="zk">展开</i><em class="erw-em1"></em></span>）后，再扫描下方二维码加入该社区。
                    </p>
                </div>
                <div class="com-card">
                    <div class="card-tit">
                        <img src="/static/images/community/wuling.png" id="image">
                        <span class="sp1" id="comm_name"></span>
                        <span class="sp2" id="comm_id"></span>
                    </div>
                    <div class="card-pic">
                        <img id="QRcode" src="/static/images/community/card-er.png">
                    </div>
                </div>
            </div>
        </div>
        <div class="com-right">
            <div class="com-right-s clearfix">
                <div class="com-tit">我的社区<span class="com-set-my-btn"></span></div>
                <ul class="ul-my-com" id="myCommunity">
                </ul>
                <script type="text/template" id="myCommunityTmpl">
                    {{~it:value:index}}
                    <li>
                        <a href="/community/communityPublish?communityId={{=value.id}}"><img src="{{=value.logo}}"></a>
                        <p>{{=value.name}}</p>
                    </li>
                    {{~}}
                </script>
            </div>
            <div class="com-right-s clearfix">
                <div class="com-tit">热门社区</div>
                <ul class="ul-my-com" id="hotCommunity">
                </ul>
                <script type="text/template" id="hotCommunityTmpl">
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
            </div>
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
<div class="bg"></div>
<script type="text/javascript" src="/static/js/modules/forum/jquery-1.11.1.js"></script>
<script type="text/javascript"
        src="/static/js/modules/core/0.1.0/jquery-upload/vendor/jquery.ui.widget.js?v=1"></script>
<script type="text/javascript" src="/static/js/modules/core/0.1.0/jquery-upload/jquery.fileupload.js"></script>
<script src="/static/js/sea.js"></script>
<script src="/static/js/modules/core/0.1.0/config.js?v=2015041602"></script>

<script>
    seajs.use('/static/js/modules/community/communityCreate.js', function (communityCreate) {
        communityCreate.init();
    });

    //上传图片
    $('#image-upload').fileupload({
        url: '/community/images.do',
        done: function (e, response) {
            if (response.result.code != '500') {
                var image = response.result.message[0].path;
                $('#imageUrl').attr('src', image);
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
