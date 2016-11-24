<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 2016/7/15
  Time: 10:32
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8">
    <title>论坛等级</title>
    <link rel="stylesheet" type="text/css" href="/static/css/forum/forum.css">
    <link rel="stylesheet" type="text/css" href="/static/css/main.css">
    <script type="text/javascript" src="/static/js/modules/forum/jquery-1.11.1.js"></script>
    <script type="text/javascript">
        $(document).ready(function () {
            $('.touxian .p1 span').click(function () {
                $(this).addClass('span-sq-cur').siblings('.touxian .p1 span').removeClass('span-sq-cur');
            });
            $('.touxian .span1').click(function () {
                $('.sq-tx').show();
                $('.sq-zd').hide();
            });
            $('.touxian .span2').click(function () {
                $('.sq-tx').hide();
                $('.sq-zd').show();
            });
        })
    </script>
</head>
<body>
<%@ include file="../common/head.jsp" %>
<div class="user-contw">
    <img src="/static/images/forum/user_top_banner.png">
    <div class="touxian">
        <p class="p1">
            <span class="span1 span-sq-cur">社区积分制度</span>
            <span class="span2">社区头衔特权</span>
        </p>
        <div class="sq-tx ">
            <img src="/static/images/forum/user_introduce.png">
        </div>
        <div class="sq-zd">
            <img src="/static/images/forum/user_level_table.png">
        </div>
    </div>
</div>
<%@ include file="../common/footer.jsp" %>

</body>
</html>
