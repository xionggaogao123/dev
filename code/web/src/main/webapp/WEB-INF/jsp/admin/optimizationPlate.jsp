<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>优化板块管理</title>
    <style type="text/css">
        table {
            border-collapse: collapse;
        }
    </style>
</head>
<body>

<div id="wrapper">

    <!-- Sidebar -->
    <%@include file="side_bar.jsp" %>
    <!-- /#sidebar-wrapper -->

    <!-- Page Content -->
    <div id="page-content-wrapper">
        <div class="container-fluid">
            <div class="container" style="width:50%;">
                <h1>优化板块管理</h1>
                <div style="color:#E43838">
                    警告：此页面为复兰科技内部页面，仅限复兰科技内部使用
                </div>
                <table class="table table-bordered table-hover">
                    <tr>
                        <td>板块Id</td>
                        <td><input id="id">
                        </td>
                    </tr>
                    <tr>
                        <td>板块名</td>
                        <td><input id="nm">
                        </td>
                    </tr>

                    <tr>
                        <td>板块描述</td>
                        <td><input id="mmn">
                        </td>
                    </tr>
                    <tr>
                        <td>版主</td>
                        <td><input id="snm">
                        </td>
                    </tr>

                    <tr>
                        <td style='vertical-align: middle;text-align: center;' colspan="2">
                            <button id="submit" class="btn btn-primary">提交</button>
                        </td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
    <!-- /#page-content-wrapper -->

</div>

<link href="/static/css/side_bar.css" rel="stylesheet">
<!-- 新 Bootstrap 核心 CSS 文件 -->
<link rel="stylesheet" href="http://cdn.bootcss.com/bootstrap/3.3.0/css/bootstrap.min.css">

<!-- 可选的Bootstrap主题文件（一般不用引入） -->
<link rel="stylesheet" href="http://cdn.bootcss.com/bootstrap/3.3.0/css/bootstrap-theme.min.css">

<!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->
<script src="http://cdn.bootcss.com/jquery/1.11.1/jquery.min.js"></script>

<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
<script src="http://cdn.bootcss.com/bootstrap/3.3.0/js/bootstrap.min.js"></script>

<script>
    $(document).ready(function () {
        $('#submit').click(function () {
            var formData = {
                id: $('#id').val(),
                nm: $('#nm').val(),
                snm: $('#snm').val(),
                mmn: $('#mmn').val()
            };
            $.ajax({
                url: '/admin/updateSection.do',
                data: formData,
                type: 'post',
                cache: false,
                success: function () {
                    alert("修改成功！");
                    window.location.reload();
                },
                error: function () {
                    alert("异常！");
                }
            });
        });
    });
</script>

</body>
</html>
