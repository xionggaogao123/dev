<%--
  Created by IntelliJ IDEA.
  User: fl
  Date: 2016/1/27
  Time: 10:55
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<html>
<head>
    <title>管理员添加评论</title>
    <link rel="stylesheet" type="text/css" href="/static_new/js/modules/diyUpload/css/webuploader.css">
    <link rel="stylesheet" type="text/css" href="/static_new/js/modules/diyUpload/css/diyUpload.css">
    <link rel="stylesheet" type="text/css" href="http://apps.bdimg.com/libs/bootstrap/3.3.0/css/bootstrap.min.css">

    <style type="text/css">
        * {
            margin: 0;
            padding: 0;
        }

        table, tr, td, th {
            border: solid 1px #B4B4B4;
        }

        table {
            border-collapse: collapse;
        }
    </style>
</head>
<body>
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
            <table>
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
                        <select>
                            <option value="5">5</option>
                            <option value="4">4</option>
                            <option value="3">3</option>
                            <option value="2">2</option>
                            <option value="1">1</option>
                        </select></td>
                </tr>
                <tr>
                    <td>晒图</td>
                    <td>
                        <div id="imgs"></div>
                    </td>
                </tr>
                <tr>
                    <td colspan="2" align="center">
                        <button id="confirm" class="btn btn-sm btn-primary">确定</button>
                    </td>
                </tr>
            </table>
        </div>
        <div class="col-md-3"></div>
    </div>
</div>


</body>
<script type="text/javascript" src="/static/js/jquery-1.11.1.min.js"></script>
<script src="http://apps.bdimg.com/libs/bootstrap/3.3.0/js/bootstrap.min.js"></script>
<script src="/static_new/js/modules/diyUpload/js/webuploader.html5only.min.js"></script>
<script src="/static_new/js/modules/diyUpload/js/diyUpload.js"></script>
<script type="text/javascript">
    var images = [];
    $(function () {
        $('#confirm').click(function () {
            addComment();
        })
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
    })

    function addComment() {
        var commentDto = {};
        var goodsId = $('#goodsId').val();

        commentDto.goodsId = goodsId;
        commentDto.content = $.trim($('#content').val());
        commentDto.score = $('select').val();
        commentDto.orderId = '000000000000000000000000';
        commentDto.images = images;
        if (goodsId == '') {
            alert('商品id为空');
            return;
        }
        if (commentDto.content == '') {
            alert('评论为空');
            return;
        }
        $.ajax({
            url: '/mall/admin/goods/' + goodsId + '/comments.do',
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
</html>
