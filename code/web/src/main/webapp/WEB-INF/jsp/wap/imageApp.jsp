<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 2016/8/18
  Time: 18:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
    <script type="text/javascript" src="/wap/js/jquery-1.11.1.js"></script>
    <%--<script type="text/javascript" src="/wap/js/post.js"></script>--%>
    <script type="text/javascript" src="/wap/js/TouchSlide.1.1.js"></script>
    <style>
        /*弹框*/
        .tip_boxs {
            z-index: 9999;
            width: 100%;
            position: absolute;
            left: 0%;
            display: none;
            height: 100%;
            display: -webkit-box;
            -webkit-box-pack: center;
            -webkit-box-align: center;
            text-align: center;
            margin: 0 auto;
        }

        .tip_boxs .align {
            border-radius: 10px;
            overflow: hidden;
            width: 100%;
            background: #fff;
        }

        .tip_boxs_zhezhao {
            background: #000;
            opacity: 0.8;
            z-index: 8888;
            display: none;
            position: fixed;
            top: 0;
            left: 0;
        }

        #slideBoxindexb {
            position: relative;
            width: 100%;
            overflow: hidden;
            margin: 0 auto;
        }

        #slideBoxindexb .bd {
            position: relative;
            z-index: 0;
            width: 100%;
        }

        #slideBoxindexb .bd .tempWrap {
            width: 100%;
        }

        #slideBoxindexb .bd li {
            position: relative;
            float: left;
        }

        #slideBoxindexb .bd li {
            display: block;
            width: 100%;
            display: -webkit-box;
            -webkit-box-pack: center;
            -webkit-box-align: center;
        }

        #slideBoxindexb .bd li img {
            display: block;
            width: 100%;
            border-top-left-radius: 10px;
            border-top-right-radius: 10px;
        }

        #slideBoxindexb .prev, #slideBoxindexb .next {
            cursor: pointer;
            position: absolute;
            top: 50%;
            margin-top: -16px;
            display: block;
            width: 30px;
            height: 32px;
            background: url(../images/prev_next_bg.png) no-repeat;
        }

        #slideBoxindexb .prev {
            left: 5px;
        }

        #slideBoxindexb .next {
            right: 5px;
            background-position: -30px 0;
        }

        .tip_boxs .pop_con {
            width: 100%;
            padding: 10px;
            box-sizing: border-box;
            background: #fff;
            font-size: 12px;
            color: #060505;
            text-align: left;
        }

        .tip_boxs .pop_con p {
            text-indent: 2em;
            line-height: 26px;
        }

        .tip_boxs .pop_con p.time {
            color: #000;
            text-indent: 0;
        }

        .tip_boxs .pop_con h5 {
            font-size: 14px;
            font-weight: bold;
        }
    </style>
</head>
<body bgcolor="black">
<input type="hidden" id="replyId" value="${replyId}">
<%--<input type="hidden" id="index" value="${index}">--%>
<div style="width: 100%;height: 4%">
    <span onclick="history.go(-1)" style="font-family:华文中宋; color:#ffffff;font-size: xx-large">返回上一页</span>
</div>
<div class="tip_boxs" id="tip_boxs">
    <div class="align">
        <div id="slideBoxindexb">
            <div class="bd">
                <ul class="clearfix" id="hsCont">

                </ul>
            </div>
            <div class="hd">
                <span class="prev"></span>
                <span class="next"></span>
            </div>
        </div>
        <%--<div class="pop_con close">--%>
        <%--<p class="time">2014-10-06</p>--%>
        <%--<h5>餐桌上的创意美学</h5>--%>
        <%--<p>Audi City Beijing是亚洲首家数字化奥迪展厅，坐落于市中心东方广场。Audi City Beijing是奥迪品牌为粉丝们创造的一个可以相识交流的奇幻场所，同时，Audi City Beijing为人们提供了一个与品牌交流对话的空间,让奥迪与城市生活更加紧密地联系在一起。</p>--%>
        <%--</div>--%>
    </div>
</div>
<script id="hsContTml" type="text/template">
    {{~it:value:index}}
    {{~value.imageList:images:i}}
    <li><img src="{{=images}}" class="ssl"/></li>
    {{~}}
    {{~}}
</script>
<script src="/static/js/sea.js"></script>
<!-- Custom js -->
<script src="/static/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('/static/js/modules/forum/imageApp.js', function (imageApp) {
        imageApp.init();
    });
</script>

<script type="text/javascript">

    //      $("#tip_boxs").show();


</script>
</body>
</html>
