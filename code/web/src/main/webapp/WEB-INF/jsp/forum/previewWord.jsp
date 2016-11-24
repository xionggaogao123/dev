<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>预览</title>
    <!-- require css -->
    <link rel="stylesheet" href="/static/dist/MPreview/css/MPreview.css">

    <link rel="stylesheet" href="/static/dist/MPreview/css/base.css">

    <!-- require js -->
    <link rel="stylesheet" href="http://cdn.bootcss.com/bootstrap/3.3.0/css/bootstrap.min.css">

    <!-- 可选的Bootstrap主题文件（一般不用引入） -->
    <link rel="stylesheet" href="http://cdn.bootcss.com/bootstrap/3.3.0/css/bootstrap-theme.min.css">

    <!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->
    <script src="http://cdn.bootcss.com/jquery/1.11.1/jquery.min.js"></script>

    <!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
    <script src="http://cdn.bootcss.com/bootstrap/3.3.0/js/bootstrap.min.js"></script>

    <script type="text/javascript" src="/static/dist/MPreview/js/MPreview.js"></script>

</head>
<body>
<!-- html -->
<div class="wrapper">
    <div class="doc" id="doc"></div>
</div>
<script type="text/javascript">

    $(document).ready(function () {
        //关于 data 参数的用法
        var data = ['/static/dist/MPreview/images/01.png', '/static/dist/MPreview/images/11.png', '/static/dist/MPreview/images/21.png'];
        $('#doc').MPreview({
            data: data,
            offset: 100,                            //每次滚动偏移多少像素，默认 100px
            loadSize: 5,                            //每次加载几张图片
            pageFix: 50,                            //当前页数判定的衡量标准
            scrollFix: 5,                           //当前默认滚动条距离外容器的边距
            minScrollHeight: 20
        });

    });

</script>

</body>
</html>
