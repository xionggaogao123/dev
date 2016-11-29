<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>

<style type="text/css">
    .weixin > img {
        position: absolute;

        left: 80%;
        z-index: 99999999999999;
        display: none;
    }

    #weixin {
        cursor: pointer;
    }

    div {
        overflow: visible !important;
    }
</style>
<div class="index-title-bar">
    <div class="title-bar-container">
        <div class="company-name">
            <img src="/static/images/phone-logo.jpg" style="margin-top: -5px;margin-right: 2px;">
            <span>400-820-6735</span>
        </div>
        <div class="company-phone" style="position:relative">
            <a href="#">手机客户端下载</a>
            <img src="/static/images/aboutus_ios.png" class="img11">
        </div>
        <div class="guanzhu">
            <span>关注我们：</span>
            <a href='http://weibo.com/FulaanTechnology'><img src="/static/images/WEIBO.png"></a>
            <a href='http://t.qq.com/FulaanTechnology'><img src="/static/images/WEIBO2.png"></a>
            <img id="weixin" src="/static/images/weixin.png">
            <div class="weixin">
                <img src="/static/images/weixin-IM.png" width="100px;">
            </div>

        </div>
    </div>
</div>

<div id="weixin_beijing"
     style="background-color: #000000;opacity: 0.7;filter:alpha(opacity=70);width: 100%;height: 100%;position: fixed;top:0%;left: 0%;z-index: 99;display: none;">

</div>

<script type="text/javascript">
    $("#weixin").click(function () {
        $(".weixin>img").show();
        $("#weixin_beijing").show();
    })
    $("#weixin_beijing").click(function () {
        $(".weixin>img").hide();
        $("#weixin_beijing").hide();
    })
</script>