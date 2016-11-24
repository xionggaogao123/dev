<%--
  Created by IntelliJ IDEA.
  User: wangkaidong
  Date: 2016/4/20
  Time: 10:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>注册成功</title>
    <meta http-equiv="refresh" content="5; url=/mall/index.do">
</head>
<body>
<div>
    <p>您已注册成功，获得5张优惠券，快去购物吧！<em>5</em></p>
    <button>立即去购物</button>
</div>


<script src="/static/js/modules/core/0.1.0/jquery.min.js"></script>
<script>
    $(document).ready(function () {
        setTime();

        $('body').on('click', 'button', function () {
            window.location.href = '/mall/index.do';
        });

    });
    setTime = (function () {
        var time = 4;
        return setInterval(function () {
            $('div p em').text(time);
            if (time > 0) {
                time--;
            }
        }, 1000);
    });
</script>
</body>


</html>
