<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 2016/7/12
  Time: 11:34
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>jQuery抽奖转盘效果代码（兼容IE浏览器） - 素材家园（www.sucaijiayuan.com）</title>
    <link rel="stylesheet" href="/static/css/forum/lottery.css" type="text/css"/>
    <script type="text/javascript" src="/static/js/modules/forum/jquery-1.11.1.js"></script>
    <script type="text/javascript" src="/static/js/modules/forum/awardRotate.js"></script>
    <script type="text/javascript">
        $(function () {

            var rotateTimeOut = function () {
                $('#rotate').rotate({
                    angle: 0,
                    animateTo: 2160,
                    duration: 8000,
                    callback: function () {
                        alert('网络超时，请检查您的网络设置！');
                    }
                });
            };
            var bRotate = false;

            var rotateFn = function (awards, angles, txt) {
                bRotate = !bRotate;
                $('#rotate').stopRotate();
                $('#rotate').rotate({
                    angle: 0,
                    animateTo: angles + 1800,
                    duration: 8000,
                    callback: function () {
                        alert(txt);
                        bRotate = !bRotate;
                    }
                })
            };

            $('.pointer').click(function () {

                if (bRotate)return;
                var item = rnd(0, 7);

                switch (item) {
                    case 0:
                        //var angle = [26, 88, 137, 185, 235, 287, 337];
                        rotateFn(0, 337, '未中奖');
                        break;
                    case 1:
                        //var angle = [88, 137, 185, 235, 287];
                        rotateFn(1, 26, '免单4999元');
                        break;
                    case 2:
                        //var angle = [137, 185, 235, 287];
                        rotateFn(2, 88, '免单50元');
                        break;
                    case 3:
                        //var angle = [137, 185, 235, 287];
                        rotateFn(3, 137, '免单10元');
                        break;
                    case 4:
                        //var angle = [185, 235, 287];
                        rotateFn(4, 185, '免单5元');
                        break;
                    case 5:
                        //var angle = [185, 235, 287];
                        rotateFn(5, 185, '免单5元');
                        break;
                    case 6:
                        //var angle = [235, 287];
                        rotateFn(6, 235, '免分期服务费');
                        break;
                    case 7:
                        //var angle = [287];
                        rotateFn(7, 287, '提高白条额度');
                        break;
                }

                console.log(item);
            });
        });
        function rnd(n, m) {
            return Math.floor(Math.random() * (m - n + 1) + n)
        }
    </script>
</head>
<body>
<div class="turntable-bg">
    <div class="pointer"><img src="/static/images/forum/pointer.png" alt="pointer"/></div>
    <div class="rotate"><img id="rotate" src="/static/images/forum/turntable.png" alt="turntable"/></div>
</div>
</body>
</html>
