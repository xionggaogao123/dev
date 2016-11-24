<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>管理员添加评论</title>
    <link rel="stylesheet" type="text/css" href="/static/js/modules/diyUpload/css/webuploader.css">
    <link rel="stylesheet" type="text/css" href="/static/js/modules/diyUpload/css/diyUpload.css">
    <link rel="stylesheet" type="text/css" href="http://apps.bdimg.com/libs/bootstrap/3.3.0/css/bootstrap.min.css">
</head>
<body>
<div id="wrapper">

    <!-- Sidebar -->
    <%@include file="side_bar.jsp" %>
    <!-- /#sidebar-wrapper -->

    <!-- Page Content -->
    <div id="page-content-wrapper">
        <div class="container-fluid">
            <div class="row">
                <div class="container-fluid">
                    <div class="row">
                        <div class="col-md-3"></div>
                        <div class="col-md-6">
                            <div style="color:#E43838">
                                警告：此页面为复兰科技内部页面，仅限复兰科技内部使用
                            </div>
                            <br>
                            <br>

                            <h3>添加评论</h3>
                            <div id="alert" class="alert alert-warning alert-dismissible" role="alert" hidden>
                                <button type="button" class="close" data-dismiss="alert"><span
                                        aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                                <strong>Warning!</strong> 商品id不能为空！
                            </div>
                            <table class="table table-bordered table-hover">
                                <tr>
                                    <td>商品ID</td>
                                    <td><input id="goodsId"></td>
                                </tr>
                                <tr>
                                    <td>评论</td>
                                    <td><textarea id="content" style="height: 100px;width: 500px;"></textarea></td>
                                </tr>
                                <tr>
                                    <td>评分</td>
                                    <td>
                                        <select class="form-control">
                                            <option value="5">5</option>
                                            <option value="4">4</option>
                                            <option value="3">3</option>
                                            <option value="2">2</option>
                                            <option value="1">1</option>
                                        </select>
                                    </td>
                                </tr>
                                <tr>
                                    <td>晒图</td>
                                    <td>
                                        <div id="imgs"></div>
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan="2" align="center">
                                        <button id="confirm" type="button" class="btn btn-success btn-lg">确定</button>
                                    </td>
                                </tr>
                            </table>
                        </div>
                        <div class="col-md-3"></div>
                    </div>
                </div>
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

<script src="/static/js/modules/diyUpload/js/webuploader.html5only.min.js"></script>
<script src="/static/js/modules/diyUpload/js/diyUpload.js"></script>
<script type="text/javascript">
    var images = [];
    $(function () {
        $('#confirm').click(function () {
            addComment();
        });
        $('#imgs').diyUpload({
            url: '/mall/images.do',
            success: function (data) {
                images.push({id: data.message[0].id, value: data.message[0].path});
                console.info(data);
            },
            error: function (err) {
                console.info(err);
            }
        });
    });

    function addComment() {
        var commentDto = {};
        var goodsId = $('#goodsId').val();

        commentDto.goodsId = goodsId;
        commentDto.content = $.trim($('#content').val());
        commentDto.score = $('select').val();
        commentDto.orderId = '000000000000000000000000';
        commentDto.images = images;
        if (goodsId == '') {
            $('#alert').show();
            return;
        }
        if (commentDto.content == '') {
            alert('评论为空');
            return;
        }
        $.ajax({
            url: '/admin/goods/' + goodsId + '/comments.do',
            type: 'post',
            data: JSON.stringify(commentDto),
            contentType: 'application/json',
            success: function (result) {
                if (result.code == '200') {
                    alert('添加成功');
                } else {
                    alert(result.message);
                }
            },
            error: function (e) {
                alert('添加失败');
            }
        });
    }
</script>
</body>
</html>
