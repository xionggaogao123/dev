<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<html>
<head>
    <title>Banner管理</title>
    <link href="http://apps.bdimg.com/libs/bootstrap/3.3.0/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .masterButton {
        }

        .masterId {
        }

        .managerId {
        }

        .managerButton {
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
        <div class="container" style="width:100%;">
            <div class="col-md-2"></div>
            <div class="col-md-8">
                <h3>版主管理</h3>
                <div style="color:#E43838">
                    警告：此页面为复兰科技内部页面，仅限复兰科技内部使用
                </div>
                <table class="table table-bordered table-hover">
                    <tr>
                        <td>用户名<input name="name" id="UserNameSec"></td>
                        <td>板块
                            <select name="section" style='width:128px' id="section">
                                <c:forEach items="${sections}" var="item">
                                    <option value='${item['fSectionId']}'>${item['name']}</option>
                                </c:forEach>
                            </select>
                        </td>
                        <td>
                            <button type="button" id="addSectionBtn" class="btn btn-warning">添加版主</button>
                        </td>
                        <td>用户名<input name="name" id="UserNameAdmin"></td>
                        <td>
                            <button type="button" id="addAdminBtn" class="btn btn-danger">添加管理员</button>
                        </td>
                    </tr>

                    <tr>
                        <td>ID</td>
                        <td>版主名</td>
                        <td>昵称</td>
                        <td>操作</td>
                    </tr>
                    <c:forEach items="${masters}" var="master">
                        <tr>
                            <td class="masterId">${master['id']}</td>
                            <td>${master['name']}</td>
                            <td>${master['nickName']}</td>
                            <td>
                                <button type="button" class="masterButton btn btn-primary">取消版主</button>
                            </td>
                        </tr>
                    </c:forEach>
                    <tr>
                        <td>ID</td>
                        <td>管理员名</td>
                        <td>昵称</td>
                        <td>操作</td>
                    </tr>
                    <c:forEach items="${managers}" var="manager">
                        <tr>
                            <td class="managerId">${manager['id']}</td>
                            <td>${manager['name']}</td>
                            <td>${manager['nickName']}</td>
                            <td>
                                <button type="button" class="managerButton btn btn-primary">取消管理员</button>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
            </div>
        </div>
    </div>
    <!-- /#page-content-wrapper -->

</div>

<link href="/static/dist/popup/x0popup.min.css" rel="stylesheet">

<script src="/static/dist/popup/x0popup.min.js"></script>

<link href="/static/css/side_bar.css" rel="stylesheet">
<!-- 新 Bootstrap 核心 CSS 文件 -->
<link rel="stylesheet" href="http://cdn.bootcss.com/bootstrap/3.3.0/css/bootstrap.min.css">

<!-- 可选的Bootstrap主题文件（一般不用引入） -->
<link rel="stylesheet" href="http://cdn.bootcss.com/bootstrap/3.3.0/css/bootstrap-theme.min.css">

<!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->
<script src="http://cdn.bootcss.com/jquery/1.11.1/jquery.min.js"></script>

<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
<script src="http://cdn.bootcss.com/bootstrap/3.3.0/js/bootstrap.min.js"></script>

<script src="/static/js/jquery-1.11.1.min.js"></script>

<script type="text/javascript">
    $(document).ready(function () {
        $("#addSectionBtn").click(function () {
            x0p('Confirmation', 'Are you sure?', 'warning');
            $.ajax({
                url: '/admin/addSectionAdmin',
                data: {
                    section: $('#section').val(),
                    name: $('#UserNameSec').val()
                },
                type: 'post',
                cache: false,
                dataType: 'json',
                success: function (data) {
                    alert("修改成功！");
                    window.location.reload();
                },
                error: function () {
                    alert("出错了");
                }
            });
        });

        $('.masterButton').click(function () {
            var id = $(this).parent().parent().find(".masterId").text();
            $.ajax({
                url: '/admin/deleteMaster',
                data: {
                    id: id
                },
                type: 'post',
                cache: false,
                success: function () {
                    alert("修改成功！");
                    window.location.reload();
                },
                error: function () {
                    alert("出错了");
                }
            });
        });

        $('.managerButton').click(function () {
            var id = $(this).parent().parent().find(".managerId").text();
            $.ajax({
                url: '/admin/deleteManager',
                data: {
                    id: id
                },
                type: 'post',
                cache: false,
                success: function () {
                    alert("修改成功！");
                    window.location.reload();
                },
                error: function () {
                    alert("出错了");
                }
            });
        });

        $('#addAdminBtn').click(function () {
            $.ajax({
                url: '/admin/addDiscussAdmin',
                data: {
                    name: $('#UserNameAdmin').val()
                },
                type: 'post',
                cache: false,
                success: function () {
                    alert("修改成功！");
                    window.location.reload();
                },
                error: function () {
                    alert("出错了");
                }
            });
        });
    });
</script>
</body>
</html>
