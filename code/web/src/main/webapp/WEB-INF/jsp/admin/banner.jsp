<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<html>
<head>
    <title>Banner管理</title>
    <link href="http://apps.bdimg.com/libs/bootstrap/3.3.0/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .objectId {
        }

        .delete {
        }

        .onlineButton {
        }

        .offLine {
            color: green;
        }

        .onLine {
            color: #F00;
            font-weight: bold;
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
                <div style="color:#E43838">
                    警告：此页面为复兰科技内部页面，仅限复兰科技内部使用
                </div>
                <h3>添加Banner</h3>
                当前上线的Banner个数<span class="onLine">${count}</span>
                <form enctype="multipart/form-data" method="post" action="/vv/add.do">
                    <table class="table table-bordered table-hover">
                        <tr>
                            <td>商品名称<input name="name"></td>
                            <td>商品ID<input name="targetId"></td>
                            <td>
                                <span>Banner图</span>
                                <input type="file" name="file">
                            </td>
                            <td>
                                <span>是否上线</span>
                                <input type="radio" id="one" value="2" name="status">
                                <label for="one">是</label>
                                <input type="radio" id="two" value="1" name="status">
                                <label for="two">否</label>
                            </td>
                            <td>
                                <button type="submit">添加</button>
                            </td>
                        </tr>
                        <tr>
                            <td>BannerID</td>
                            <td>商品名称</td>
                            <td>商品ID</td>
                            <td>Banner图</td>
                            <td>状态</td>
                            <td>操作</td>
                        </tr>
                        <c:forEach items="${banners}" var="banner">
                            <tr>
                                <td><span class="objectId">${banner['id']}</span></td>
                                <td>${banner['name']}</td>
                                <td><span>${banner['targetId']}</span></td>
                                <td><img src="${banner['imageUrl']}" height="100" width="200"></td>
                                <td><c:if test="${banner.status == 1}">
                                    <span class="offLine">未上线</span>
                                </c:if>
                                    <c:if test="${banner['status'] == 2}">
                                        <span class="onLine">上线中</span>
                                    </c:if>
                                </td>
                                <td>
                                    <div class="btn-group">
                                        <button type="button" class="onlineButton btn btn-primary btn-block">
                                            <c:if test="${banner['status'] == 1}">
                                                上线
                                            </c:if>
                                            <c:if test="${banner['status'] == 2}">
                                                下线
                                            </c:if>
                                        </button>
                                        <button type="button" class="delete btn btn-primary">删除</button>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                    </table>
                </form>
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

<script src="/static/js/jquery-1.11.1.min.js"></script>
<script type="text/javascript">
    $(document).ready(function () {
        $("#submit").click(function () {
            var formData = new FormData($('form'));
            $.ajax({
                url: '/v1/app/insertBanner.do',// 跳转到 action
                data: formData,
                type: 'post',
                cache: false,
                dataType: 'json',
                success: function (data) {
                    if (data.msg == "true") {
                        alert("修改成功！");
                    }
                },
                error: function (data) {
                    alert(data.message);
                }
            });
        });

        $('.delete').click(function () {
            var id = $(this).parent().parent().find('.objectId').text();
            var formData = {
                id: id
            };
            $.ajax({
                url: '/vv/delete.do',// 跳转到 action
                data: formData,
                type: 'post',
                cache: false,
                success: function () {
                    alert("修改成功！");
                    window.location.reload();
                },
                error: function (data) {
                    alert(data.message);
                }
            });
        });

        $('.onlineButton').click(function () {
            var id = $(this).parent().parent().find('.objectId').text();
            var text = $.trim($(this).text());
            var action = 1;
            if (text == '上线') {
                action = 2;
            }
            alert(action);
            var formData = {
                id: id,
                action: action
            };
            $.ajax({
                url: '/vv/updateStatus.do',
                data: formData,
                type: 'post',
                cache: false,
                success: function () {
                    window.location.reload();
                },
                error: function (data) {
                    alert(data.message);
                }
            });
        });
    });
</script>
</body>
</html>
